package org.backend.cloud.authentication.event;

import org.backend.cloud.web.model.CurrentUser;

public class LogoutSuccessEvent {

  private CurrentUser currentUser;

  public static LogoutSuccessEvent of(CurrentUser currentUser) {
    LogoutSuccessEvent logoutSuccessEvent = new LogoutSuccessEvent();
    logoutSuccessEvent.setCurrentUser(currentUser);
    return logoutSuccessEvent;
  }

  public CurrentUser getCurrentUser() {
    return currentUser;
  }

  public void setCurrentUser(CurrentUser currentUser) {
    this.currentUser = currentUser;
  }
}
