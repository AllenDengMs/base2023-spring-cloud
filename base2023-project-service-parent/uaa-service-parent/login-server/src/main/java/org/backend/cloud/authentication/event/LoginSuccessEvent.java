package org.backend.cloud.authentication.event;

import org.backend.cloud.authentication.model.entity.UserLoginInfo;
import org.backend.cloud.common.utils.TimeTool;
import org.backend.cloud.web.model.CurrentUser;
import org.backend.cloud.web.utils.HttpTool;

public class LoginSuccessEvent {

  private UserLoginInfo userLoginInfo;

  private CurrentUser currentUser;

  public static LoginSuccessEvent of(CurrentUser currentUser) {
    LoginSuccessEvent event = new LoginSuccessEvent();
    event.setUserLoginInfo(toLoginInfo(currentUser));
    event.setCurrentUser(currentUser);
    return event;
  }

  private static UserLoginInfo toLoginInfo(CurrentUser currentUser) {
    UserLoginInfo loginInfo = new UserLoginInfo();
    loginInfo.setSessionId(currentUser.getSessionId());
    loginInfo.setLoginTime(TimeTool.nowMilli());
    loginInfo.setNickname(currentUser.getNickname());
    loginInfo.setIp(HttpTool.getIpAddress());
    loginInfo.setUserId(currentUser.getUserId());
    loginInfo.setExpiredTime(currentUser.getExpiredTime());
    return loginInfo;
  }

  public UserLoginInfo getUserLoginInfo() {
    return userLoginInfo;
  }

  public void setUserLoginInfo(UserLoginInfo userLoginInfo) {
    this.userLoginInfo = userLoginInfo;
  }

  public CurrentUser getCurrentUser() {
    return currentUser;
  }

  public void setCurrentUser(CurrentUser currentUser) {
    this.currentUser = currentUser;
  }
}
