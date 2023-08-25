package org.backend.cloud.authentication.model.entity;

/**
 * 用户登陆信息表
 */
public class UserLoginInfo {

  /**
   * 会话id
   */
  private Long sessionId;

  /**
   * 用户id
   */
  private Long userId;

  /**
   * 登陆账号名
   */
  private String username;

  /**
   * 用户昵称
   */
  private String nickname;

  /**
   * 登陆状态，0:下线（强制下线/退出登陆/jwt过期）,1:在线
   */
  private int status;

  /**
   * 登陆时间，时间戳
   */
  private Long loginTime;

  /**
   * 刷新jwt时间，时间戳
   */
  private Long updateTime;

  /**
   * jwt过期时间，时间戳
   */
  private Long expiredTime;

  /**
   * 登陆ip
   */
  private String ip;

  /**
   * IP归属地
   */
  private String ipGeolocation;

  public Long getSessionId() {
    return sessionId;
  }

  public void setSessionId(Long sessionId) {
    this.sessionId = sessionId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public Long getLoginTime() {
    return loginTime;
  }

  public void setLoginTime(Long loginTime) {
    this.loginTime = loginTime;
  }

  public Long getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Long updateTime) {
    this.updateTime = updateTime;
  }

  public Long getExpiredTime() {
    return expiredTime;
  }

  public void setExpiredTime(Long expiredTime) {
    this.expiredTime = expiredTime;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getIpGeolocation() {
    return ipGeolocation;
  }

  public void setIpGeolocation(String ipGeolocation) {
    this.ipGeolocation = ipGeolocation;
  }
}