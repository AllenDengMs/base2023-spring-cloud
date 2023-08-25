package org.backend.cloud.web.model;

/**
 * 用户信息登陆后的信息，会序列化到Jwt的payload
 */
public class CurrentUser {

  private Long sessionId; // 会话id，全局唯一
  private Long userId;
  private String nickname; // 昵称
  private String roleId;

  private Long expiredTime; // 过期时间

  public Long getSessionId() {
    return sessionId;
  }

  public void setSessionId(Long sessionId) {
    this.sessionId = sessionId;
  }

  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public Long getExpiredTime() {
    return expiredTime;
  }

  public void setExpiredTime(Long expiredTime) {
    this.expiredTime = expiredTime;
  }

  public static final class CurrentUserBuilder {

    private Long sessionId;
    private Long userId;
    private String nickname;
    private String roleId;
    private Long expiredTime;

    private CurrentUserBuilder() {
    }

    public static CurrentUserBuilder aCurrentUser() {
      return new CurrentUserBuilder();
    }

    public CurrentUserBuilder sessionId(Long sessionId) {
      this.sessionId = sessionId;
      return this;
    }

    public CurrentUserBuilder userId(Long userId) {
      this.userId = userId;
      return this;
    }

    public CurrentUserBuilder nickname(String nickname) {
      this.nickname = nickname;
      return this;
    }

    public CurrentUserBuilder roleId(String roleId) {
      this.roleId = roleId;
      return this;
    }

    public CurrentUserBuilder expiredTime(Long expiredTime) {
      this.expiredTime = expiredTime;
      return this;
    }

    public CurrentUser build() {
      CurrentUser currentUser = new CurrentUser();
      currentUser.setSessionId(sessionId);
      currentUser.setUserId(userId);
      currentUser.setNickname(nickname);
      currentUser.setRoleId(roleId);
      currentUser.setExpiredTime(expiredTime);
      return currentUser;
    }
  }
}
