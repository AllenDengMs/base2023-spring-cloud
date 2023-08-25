package org.backend.cloud.authentication.model.entity;

public class AuthorizeResult {

  private boolean canVisit;
  private String errorMessage; // canVisit == false时，要放回给前端的消息;
  private int httpStatus; // canVisit == false时，要放回给前端的状态;

  private String userInfo;

  public static AuthorizeResult success(String userInfo) {
    AuthorizeResult authorizeResult = new AuthorizeResult();
    authorizeResult.setCanVisit(true);
    authorizeResult.setUserInfo(userInfo);
    return authorizeResult;
  }

  public static AuthorizeResult fail(String errorMessage, int httpStatus) {
    AuthorizeResult authorizeResult = new AuthorizeResult();
    authorizeResult.setCanVisit(false);
    authorizeResult.setErrorMessage(errorMessage);
    authorizeResult.setHttpStatus(httpStatus);
    return authorizeResult;
  }

  public boolean isCanVisit() {
    return canVisit;
  }

  public void setCanVisit(boolean canVisit) {
    this.canVisit = canVisit;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public int getHttpStatus() {
    return httpStatus;
  }

  public void setHttpStatus(int httpStatus) {
    this.httpStatus = httpStatus;
  }

  public String getUserInfo() {
    return userInfo;
  }

  public void setUserInfo(String userInfo) {
    this.userInfo = userInfo;
  }
}
