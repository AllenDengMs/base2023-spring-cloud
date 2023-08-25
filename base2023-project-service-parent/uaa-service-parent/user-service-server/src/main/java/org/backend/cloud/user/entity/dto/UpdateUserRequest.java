package org.backend.cloud.user.entity.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class UpdateUserRequest {

  /** 用户昵称 */
  @Length(max = 20)
  private String nickname;
  /** 密码 */
  @Length(max = 255)
  private String password;

  /**
   * 账号状态
   * @see org.backend.cloud.user.model.constant.UserStatus
   */
  private Integer status;

  @Pattern(regexp = "^\\+\\d{1,4}-\\d{3,15}$", message = "${invalid.phone.number.format:无效的手机号格式, +区号-手机号}")
  private String phone;

  private String roleId;

  private String email;

  private Integer gender;

  @NotNull
  private Long userId;

  private Long operatorUserId;

  public Long getOperatorUserId() {
    return operatorUserId;
  }

  public void setOperatorUserId(Long operatorUserId) {
    this.operatorUserId = operatorUserId;
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Integer getGender() {
    return gender;
  }

  public void setGender(Integer gender) {
    this.gender = gender;
  }
}
