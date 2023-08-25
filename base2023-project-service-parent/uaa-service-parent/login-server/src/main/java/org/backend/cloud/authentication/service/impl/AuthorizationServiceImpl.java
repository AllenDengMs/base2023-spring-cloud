package org.backend.cloud.authentication.service.impl;

import java.util.concurrent.TimeUnit;
import org.backend.cloud.api.client.user.PermissionServiceClient;
import org.backend.cloud.authentication.event.LogoutSuccessEvent;
import org.backend.cloud.authentication.exception.JwtExpiredException;
import org.backend.cloud.authentication.exception.JwtVerifyFailException;
import org.backend.cloud.authentication.model.entity.AuthorizeResult;
import org.backend.cloud.authentication.service.AuthorizationService;
import org.backend.cloud.authentication.service.JwtService;
import org.backend.cloud.common.utils.JSON;
import org.backend.cloud.common.utils.TimeTool;
import org.backend.cloud.web.constant.URLWitheList;
import org.backend.cloud.web.model.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

  @Autowired
  private PermissionServiceClient permissionServiceClient;

  @Autowired
  private StringRedisTemplate stringRedisTemplate;

  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  @Autowired
  private JwtService jwtService;

  @Override
  public boolean inAccessBlackList(CurrentUser user) {
    // 注意: 每个请求都来到这里会查两次Redis，在高并发下情景会影响性能。可自行根据项目需求，选择关闭这个功能（直接返回false）。
    // 先看用户是否被禁用（用户主动下线、被强退，都会触发禁用）
    if (Boolean.TRUE.equals(
        stringRedisTemplate.hasKey(getLoginBackListKey(user.getUserId())))) {
      return true;
    }
    // 再看Jwt是否可用
    return Boolean.TRUE.equals(
        stringRedisTemplate.hasKey(getLoginBackListKey(user.getUserId(), user.getSessionId())));
  }

  @Override
  public void cleanAccessBlackList(CurrentUser user) {
    stringRedisTemplate.delete(getLoginBackListKey(user.getUserId()));
  }

  @Override
  public boolean hasPermission(CurrentUser user, String toApiPath, HttpMethod method) {
    if ((user != null && user.getRoleId().equals("super")) // 不拦截超管
        || URLWitheList.NO_NEED_PERMISSION.contains(toApiPath.concat(method.toString()))) {
      return true;
    }

    // 普通角色
    if (user == null || user.getRoleId() == null) {
      return false;
    }
    return permissionServiceClient.hasPermissionOnPath(user.getRoleId(), toApiPath,
        method.toString());
  }

  private long oneHour = TimeUnit.HOURS.toMillis(1);
  private long oneSecond = TimeUnit.SECONDS.toMillis(1);
  @Override
  public void logout(CurrentUser currentUser) {
    long ttl = currentUser.getExpiredTime().longValue() - TimeTool.nowMilli();
    if (ttl <= oneSecond) {
      // 剩下1秒就过期了，不用踢下线了
      return;
    } else if (ttl > oneHour) {
      // 最长拉入黑名单时间1个小时，省点内存空间
      ttl = oneHour;
    }

    // 毫秒转秒，之后在加个5s时间，使得黑名单时间比jwt过期时间长一点点，保证黑名单机制全程生效。
    ttl = (ttl / 1000) + 5;

    // 拉入授权黑名单. 弥补jwt无状态性质，导致无法强制下线的缺点
    stringRedisTemplate.opsForValue()
        .set(getLoginBackListKey(currentUser.getUserId()), "0", ttl, TimeUnit.SECONDS);
    stringRedisTemplate.opsForValue()
        .set(getLoginBackListKey(currentUser.getUserId(), currentUser.getSessionId()), "0", ttl, TimeUnit.SECONDS);
    applicationEventPublisher.publishEvent(LogoutSuccessEvent.of(currentUser));
  }

  public static final String REDIS_KEY_USER_ID = "weBase2023:login:backList:userId:";
  public static final String REDIS_KEY_JWT_ID = "weBase2023:login:backList:jwt:";
  private String getLoginBackListKey(Long userId) {
    return REDIS_KEY_USER_ID.concat(userId.toString());
  }

  private String getLoginBackListKey(Long userId, Long sessionId) {
    return REDIS_KEY_JWT_ID.concat(userId.toString()).concat(":").concat(sessionId.toString());
  }

  /** 未授权消息 */
  public static final String UN_AUTH_MESSAGE = "{\"code\":\"unauthorized\",\"message\":\"无效的授权信息\",\"data\":null}";
  public static final String PLEASE_LOGIN_MESSAGE = "{\"code\":\"unauthorized\",\"message\":\"请重新登录\",\"data\":null}";
  public static final String LOW_POWER_MESSAGE = "{\"code\":\"unauthorized\",\"message\":\"权限不足\",\"data\":null}";
  public static final String TOKEN_EXPIRED_MESSAGE = "{\"code\":\"token.expired\",\"message\":\"请重新登录\",\"data\":null}";


  // ============================= 网关鉴权 start ===================================
  @Override
  public AuthorizeResult checkPermissionOnPath(String token, String toPath, String method) {
    CurrentUser userInfo = null;

    // 访问白名单
    if (URLWitheList.NO_NEED_LOGIN.contains(toPath)) {
      // 部分业务场景，允许用户不登陆访问。如果用户登陆要求获取到用户信息
      userInfo = jwtService.getPayload(token, CurrentUser.class);
      return AuthorizeResult.success(JSON.stringify(userInfo));
    }

    // 访问非白名单，一定要拿到用户信息
    try {
      userInfo = jwtService.verifyJwt(token, CurrentUser.class);
    } catch (JwtExpiredException e) {
      // token过期
      return AuthorizeResult.fail(TOKEN_EXPIRED_MESSAGE, HttpStatus.OK.value());
    } catch (JwtVerifyFailException e) {
      // token签名错误
      return AuthorizeResult.fail(UN_AUTH_MESSAGE, HttpStatus.FORBIDDEN.value());
    }
    if (userInfo == null) {
      // 解析用户信息失败
      return AuthorizeResult.fail(UN_AUTH_MESSAGE, HttpStatus.UNAUTHORIZED.value());
    }

    // 2. 拦截黑名单中的用户.（用户主动退出登录了。就会列入授权黑名单，当用户重新登陆后会删除黑名单）
    if (inAccessBlackList(userInfo)) {
      return AuthorizeResult.fail(PLEASE_LOGIN_MESSAGE, HttpStatus.UNAUTHORIZED.value());
    }
    // 3. api鉴权
    if (!hasPermission(userInfo, toPath, HttpMethod.valueOf(method))) {
      return AuthorizeResult.fail(LOW_POWER_MESSAGE, HttpStatus.FORBIDDEN.value());
    }

    return AuthorizeResult.success(JSON.stringify(userInfo));
  }

  // ============================= 网关鉴权 end ===================================
}
