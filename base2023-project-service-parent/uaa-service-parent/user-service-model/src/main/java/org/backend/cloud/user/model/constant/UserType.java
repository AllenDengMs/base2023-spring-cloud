package org.backend.cloud.user.model.constant;

public enum UserType {
  /** 平台用户 */
  PLATFORM(0),
  /** 代理商 */
  AGENT(10),
  /** 企业商户 */
  MERCHANT(20),
  /** 客服 */
  CUSTOMER_SERVICE(30),
  ;

  private final int value;

  UserType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
