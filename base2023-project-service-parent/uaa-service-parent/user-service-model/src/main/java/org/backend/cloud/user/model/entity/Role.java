package org.backend.cloud.user.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色表;
 * @date : 2023-7-13
 */
public class Role implements Serializable, Cloneable {

  /** 唯一标识符 */
  private String roleId;
  /** 角色名称 */
  private String roleName;
  /** 创建时间 */
  private Date updateTime;
  /** 更新时间 */
  private Date createTime;

  /** 是否展示给前端看，像超级管理员这些角色，就不展示了 */
  private Boolean display;

  public Boolean getDisplay() {
    return display;
  }

  public void setDisplay(Boolean display) {
    this.display = display;
  }

  public String getRoleId() {
    return this.roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

  public String getRoleName() {
    return this.roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public Date getUpdateTime() {
    return this.updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public Date getCreateTime() {
    return this.createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
}