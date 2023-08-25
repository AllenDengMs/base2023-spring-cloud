package org.backend.cloud.authentication.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.backend.cloud.api.client.user.UserServiceClient;
import org.backend.cloud.authentication.event.LoginSuccessEvent;
import org.backend.cloud.authentication.service.AuthorizationService;
import org.backend.cloud.authentication.service.JwtService;
import org.backend.cloud.common.utils.IdGenerator;
import org.backend.cloud.common.utils.JSON;
import org.backend.cloud.common.utils.TimeTool;
import org.backend.cloud.web.exception.Exceptions;
import org.backend.cloud.web.model.CurrentUser;
import org.backend.cloud.web.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 认证成功/登录成功 事件处理器
 */
@Component
public class LoginSuccessHandler extends
    AbstractAuthenticationTargetUrlRequestHandler implements AuthenticationSuccessHandler {

  private static final Logger logger = LoggerFactory.getLogger(LoginSuccessHandler.class);

  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  @Autowired
  private AuthorizationService authorizationService;

  @Autowired
  private UserServiceClient userServiceClient;

  @Autowired
  private JwtService jwtService;

  public LoginSuccessHandler() {
    this.setRedirectStrategy(new NoRedirectStrategy());
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    Object principal = authentication.getPrincipal();
    if (principal == null) {
      Exceptions.error("Authentication.principal不能为null！");
    }
    if (!(principal instanceof CurrentUser)) {
      Exceptions.error("登陆认证成功后，org.springframework.security.core.Authentication.getPrincipal()返回的Object对象必须是：CurrentUserInfo类型！");
    }
    CurrentUser currentUser = (CurrentUser) principal;

    // 登录成功后，获取角色、权限相关信息
    String roleId = userServiceClient.getRoleIdByUserId(currentUser.getUserId());
    currentUser.setRoleId(roleId);

    // 生成token和refreshToken
    Map<String, Object> responseData = new LinkedHashMap<>();
    responseData.put("token", generateToken(currentUser));
    responseData.put("refreshToken", generateRefreshToken(currentUser));

    authorizationService.cleanAccessBlackList(currentUser);
    applicationEventPublisher.publishEvent(LoginSuccessEvent.of(currentUser));

    // 虽然APPLICATION_JSON_UTF8_VALUE过时了，但也要用。因为Postman工具不声明utf-8编码就会出现乱码
    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    PrintWriter writer = response.getWriter();
    writer.print(JSON.stringify(Result.data(responseData, "${login.success:登录成功！}")));
    writer.flush();
    writer.close();
  }

  public String generateToken(CurrentUser currentUser) {
    currentUser.setSessionId(IdGenerator.nextId());
    long expiredTime = TimeTool.nowMilli() + TimeUnit.MINUTES.toMillis(10); // 10分钟后过期
    currentUser.setExpiredTime(expiredTime);
    return jwtService.createJwt(currentUser, expiredTime);
  }

  private String generateRefreshToken(CurrentUser loginInfo) {
    return jwtService.createJwt(loginInfo, TimeTool.nowMilli() + TimeUnit.DAYS.toMillis(30));
  }

}
