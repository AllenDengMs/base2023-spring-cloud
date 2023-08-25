package org.backend.cloud.user.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 角色-UI权限表;
 * @date : 2023-7-13
 */
public class RoleUiPermission implements Serializable, Cloneable {

  /** 外键,角色id */
  private String roleId;
  /** ui权限，前端自定义数据结构 */
  private Map<String, Object> permissions;
  /** 创建时间 */
  private Date createTime;


  public String getRoleId() {
    return this.roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

  public Map<String, Object> getPermissions() {
    return this.permissions;
  }

  public void setPermissions(Map<String, Object> permissions) {
    this.permissions = permissions;
  }

  public Date getCreateTime() {
    return this.createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
}