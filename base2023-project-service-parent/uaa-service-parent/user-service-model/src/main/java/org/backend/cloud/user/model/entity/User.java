package org.backend.cloud.user.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表;
 * @date : 2023-7-10
 */
public class User implements Serializable, Cloneable {

  /** 用户ID */
  private Long userId;
  /** 登录账号 */
  private String username;
  /** 用户昵称 */
  private String nickname;
  /** 用户类型（0系统用户 1注册用户） */
  private Integer userType;
  /** 用户邮箱 */
  private String email;
  /** 手机号码 */
  private String phone;
  /** 用户性别（0未知 1男 2女） */
  private Integer gender;
  /** 头像路径 */
  private String avatar;
  /** 密码 */
  private String password;
  /** 盐加密 */
  private String passwordSalt;
  /** 帐号状态（0停用 1正常） */
  private Integer status;
  /** 最后登录IP */
  private String latestLoginIp;
  /** 操作数据者 */
  private Long operatorUserId;
  /** 最后登录时间 */
  private Date latestLoginTime;
  /** 密码最后更新时间 */
  private Date latestPwdUpdateTime;
  /** 创建时间 */
  private Date createTime;
  /** 更新时间 */
  private Date updateTime;


  public Long getUserId() {
    return this.userId;
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
    return this.nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public Integer getUserType() {
    return this.userType;
  }

  public void setUserType(Integer userType) {
    this.userType = userType;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return this.phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Integer getGender() {
    return this.gender;
  }

  public void setGender(Integer gender) {
    this.gender = gender;
  }

  public String getAvatar() {
    return this.avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPasswordSalt() {
    return this.passwordSalt;
  }

  public void setPasswordSalt(String passwordSalt) {
    this.passwordSalt = passwordSalt;
  }

  public Integer getStatus() {
    return this.status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getLatestLoginIp() {
    return this.latestLoginIp;
  }

  public void setLatestLoginIp(String latestLoginIp) {
    this.latestLoginIp = latestLoginIp;
  }

  public Long getOperatorUserId() {
    return this.operatorUserId;
  }

  public void setOperatorUserId(Long operatorUserId) {
    this.operatorUserId = operatorUserId;
  }

  public Date getLatestLoginTime() {
    return this.latestLoginTime;
  }

  public void setLatestLoginTime(Date latestLoginTime) {
    this.latestLoginTime = latestLoginTime;
  }

  public Date getLatestPwdUpdateTime() {
    return this.latestPwdUpdateTime;
  }

  public void setLatestPwdUpdateTime(Date latestPwdUpdateTime) {
    this.latestPwdUpdateTime = latestPwdUpdateTime;
  }

  public Date getCreateTime() {
    return this.createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return this.updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }
}