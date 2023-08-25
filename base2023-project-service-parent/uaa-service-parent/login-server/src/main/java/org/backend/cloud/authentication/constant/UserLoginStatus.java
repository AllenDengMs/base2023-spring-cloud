package org.backend.cloud.authentication.constant;

public enum UserLoginStatus {
  OFFLINE(0), // 下线
  ONLINE(1), // 在线
  ;
  private final int value;

  UserLoginStatus(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
