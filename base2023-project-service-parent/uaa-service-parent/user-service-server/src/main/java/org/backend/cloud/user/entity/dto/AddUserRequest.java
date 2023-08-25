package org.backend.cloud.user.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class AddUserRequest {

  /** 登录账号 */
  @NotBlank
  @Length(max = 30)
  private String username;
  /** 用户昵称 */
  @NotBlank
  @Length(max = 20)
  private String nickname;
  /** 密码 */
  @NotBlank
  @Length(max = 255)
  private String password;

  /**
   * 用户类型（0系统用户 1注册用户）
   * @see org.backend.cloud.user.model.constant.UserType
   */
  @NotNull
  private Integer userType;

  /**
   * 账号状态
   * @see org.backend.cloud.user.model.constant.UserStatus
   */
  @NotNull
  private Integer status;

  @Pattern(regexp = "^\\+\\d{1,4}-\\d{3,15}$", message = "${invalid.phone.number.format:无效的手机号格式, +区号-手机号}")
  private String phone;

  @NotBlank
  private String roleId;

  private String email;

  private Integer gender;

  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Integer getUserType() {
    return userType;
  }

  public void setUserType(Integer userType) {
    this.userType = userType;
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
