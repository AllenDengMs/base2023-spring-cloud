package org.backend.cloud.user.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 服务器端API权限表;
 * @date : 2023-7-13
 */
public class ResourcePermission implements Serializable, Cloneable {

  /** 权限标识 */
  private String permissionId;
  /** api访问路径 */
  private String path;
  /** GET、POST、PUT、DELETE */
  private String method;
  /** 备注 */
  private String remarks;

  private Long operatorUserId;

  private Date createTime;


  public String getPermissionId() {
    return this.permissionId;
  }

  public void setPermissionId(String permissionId) {
    this.permissionId = permissionId;
  }

  public String getPath() {
    return this.path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getMethod() {
    return this.method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getRemarks() {
    return this.remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public Long getOperatorUserId() {
    return operatorUserId;
  }

  public void setOperatorUserId(Long operatorUserId) {
    this.operatorUserId = operatorUserId;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
}