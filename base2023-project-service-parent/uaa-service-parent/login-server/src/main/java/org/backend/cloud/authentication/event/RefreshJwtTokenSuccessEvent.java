package org.backend.cloud.authentication.event;

import org.backend.cloud.authentication.model.entity.UserLoginInfo;
import org.backend.cloud.web.model.CurrentUser;

public class RefreshJwtTokenSuccessEvent {

  private UserLoginInfo userLoginInfo;

  public static RefreshJwtTokenSuccessEvent of(CurrentUser currentUser) {
    UserLoginInfo loginInfo = new UserLoginInfo();
    loginInfo.setSessionId(currentUser.getSessionId());
    loginInfo.setExpiredTime(currentUser.getExpiredTime());
    loginInfo.setUserId(currentUser.getUserId());

    RefreshJwtTokenSuccessEvent event = new RefreshJwtTokenSuccessEvent();
    event.setUserLoginInfo(loginInfo);
    return event;
  }

  public UserLoginInfo getUserLoginInfo() {
    return userLoginInfo;
  }

  public void setUserLoginInfo(UserLoginInfo userLoginInfo) {
    this.userLoginInfo = userLoginInfo;
  }

}
