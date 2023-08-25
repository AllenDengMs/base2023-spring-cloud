package org.backend.cloud.authentication.listener;

import org.backend.cloud.api.client.user.UserServiceClient;
import org.backend.cloud.authentication.constant.UserLoginStatus;
import org.backend.cloud.authentication.event.LoginSuccessEvent;
import org.backend.cloud.authentication.event.LogoutSuccessEvent;
import org.backend.cloud.authentication.event.RefreshJwtTokenSuccessEvent;
import org.backend.cloud.authentication.mapper.LoginInfoMapper;
import org.backend.cloud.authentication.model.entity.UserLoginInfo;
import org.backend.cloud.authentication.service.AuthorizationService;
import org.backend.cloud.common.utils.TimeTool;
import org.backend.cloud.user.model.entity.User;
import org.backend.cloud.web.model.CurrentUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class LoginEventListener {

  private static final Logger logger = LoggerFactory.getLogger(LoginEventListener.class);

  @Autowired
  private LoginInfoMapper loginMapper;

  @Autowired
  private UserServiceClient userServiceClient;

  @Autowired
  private AuthorizationService authorizationService;

  /**
   * 登录成功后，开一条异步线程，记录用户登录信息
   */
  @EventListener
  @Async // 异步执行
  public void onLoginSuccessEvent(LoginSuccessEvent event) {
    try {
      UserLoginInfo userLoginInfo = event.getUserLoginInfo();
      User user = userServiceClient.getUserByUserId(userLoginInfo.getUserId().toString());
      userLoginInfo.setStatus(UserLoginStatus.ONLINE.getValue());
      userLoginInfo.setUsername(user.getUsername());
      loginMapper.insertUserLoginInfo(userLoginInfo);

      authorizationService.cleanAccessBlackList(event.getCurrentUser());
    } catch (Exception e) {
      // 异步执行的，不跑异常，防止异步线程池阻塞
      logger.error(e.getMessage(), e);
    }
  }

  /**
   * 登录成功后，开一条异步线程，更新用户登陆信息
   */
  @EventListener
  @Async // 异步执行
  public void onRefreshJwtTokenSuccessEvent(RefreshJwtTokenSuccessEvent event) {
    try {
      UserLoginInfo userLoginInfo = event.getUserLoginInfo();
      userLoginInfo.setUpdateTime(TimeTool.nowMilli());
      userLoginInfo.setStatus(UserLoginStatus.ONLINE.getValue());
      loginMapper.updateUserLoginInfo(userLoginInfo);
    } catch (Exception e) {
      // 异步执行的，不跑异常，防止异步线程池阻塞
      logger.error(e.getMessage(), e);
    }
  }

  /**
   * 退出登陆后
   */
  @EventListener
  @Async // 异步执行
  public void onLogoutSuccessEvent(LogoutSuccessEvent event) {
    try {
      CurrentUser currentUser = event.getCurrentUser();
      UserLoginInfo loginInfo = new UserLoginInfo();
      loginInfo.setSessionId(currentUser.getSessionId());
      loginInfo.setUpdateTime(TimeTool.nowMilli());
      loginInfo.setStatus(UserLoginStatus.OFFLINE.getValue());
      loginMapper.updateUserLoginInfo(loginInfo);
    } catch (Exception e) {
      // 异步执行的，不跑异常，防止异步线程池阻塞
      logger.error(e.getMessage(), e);
    }
  }
}
