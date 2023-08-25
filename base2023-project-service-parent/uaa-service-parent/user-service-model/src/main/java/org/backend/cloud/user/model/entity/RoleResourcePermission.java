package org.backend.cloud.user.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色-API权限表;
 * @date : 2023-7-13
 */
public class RoleResourcePermission implements Serializable, Cloneable {

  /** 外键,角色id */
  private String roleId;
  /** 外键,api权限id */
  private String permissionId;
  /** 创建时间 */
  private Date createTime;


  public String getRoleId() {
    return this.roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

  public String getPermissionId() {
    return this.permissionId;
  }

  public void setPermissionId(String permissionId) {
    this.permissionId = permissionId;
  }

  public Date getCreateTime() {
    return this.createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
}