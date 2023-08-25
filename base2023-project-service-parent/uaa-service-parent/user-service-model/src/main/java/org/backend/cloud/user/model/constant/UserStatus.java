package org.backend.cloud.user.model.constant;

public enum UserStatus {
  DISABLED(0), // 禁用
  OK(1), // 正常
  ;

  private final int value;

  UserStatus(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
