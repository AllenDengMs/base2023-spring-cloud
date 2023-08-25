package org.backend.cloud.user.model.dto;

import java.util.Map;
import java.util.Set;

public class UpdateRolePermissionRequest {

  private String roleId;
  private Long operatorUserId;
  private Set<ApiPathDto> apiPermissions;
  private Map<String, Object> uiPermissions;


  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

  public Long getOperatorUserId() {
    return operatorUserId;
  }

  public void setOperatorUserId(Long operatorUserId) {
    this.operatorUserId = operatorUserId;
  }

  public Set<ApiPathDto> getApiPermissions() {
    return apiPermissions;
  }

  public void setApiPermissions(Set<ApiPathDto> apiPermissions) {
    this.apiPermissions = apiPermissions;
  }

  public Map<String, Object> getUiPermissions() {
    return uiPermissions;
  }

  public void setUiPermissions(Map<String, Object> uiPermissions) {
    this.uiPermissions = uiPermissions;
  }
}
