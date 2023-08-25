package org.backend.cloud.user.service;

import java.util.List;
import org.backend.cloud.user.model.dto.UpdateRolePermissionRequest;
import org.backend.cloud.user.model.entity.Role;
import org.backend.cloud.user.model.entity.RoleUiPermission;

public interface RoleService {

  int addRole(Role role);

  List<Role> findAllRoles();

  int updateRole(Role role);

  int updateRolePermission(UpdateRolePermissionRequest request);

  RoleUiPermission getRoleUiPermissionByRoleId(String roleId);

  String getUserRoleIdByUserId(long userId);

  boolean hasPermissionOnPath(String roleId, String path, String method);
}
