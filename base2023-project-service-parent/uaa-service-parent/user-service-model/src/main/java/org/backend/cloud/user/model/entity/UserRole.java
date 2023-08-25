package org.backend.cloud.user.model.entity;

import java.util.Date;
import org.backend.cloud.common.utils.TimeTool;

public class UserRole {

  private Long id;
  private Long userId;
  private String roleId;
  private Date createTime;

  public static UserRole of(Long userId, String roleId) {
    UserRole userRole = new UserRole();
    userRole.setUserId(userId);
    userRole.setRoleId(roleId);
    userRole.setCreateTime(TimeTool.now());
    return userRole;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
}
