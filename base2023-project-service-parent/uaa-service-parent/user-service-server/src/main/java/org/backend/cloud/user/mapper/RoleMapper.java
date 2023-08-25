package org.backend.cloud.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.backend.cloud.user.model.dto.ApiPathDto;
import org.backend.cloud.user.model.entity.ResourcePermission;
import org.backend.cloud.user.model.entity.Role;
import org.backend.cloud.user.model.entity.RoleResourcePermission;
import org.backend.cloud.user.model.entity.RoleUiPermission;
import org.backend.cloud.user.model.entity.UserRole;

@Mapper
public interface RoleMapper {

  int addRole(Role role);

  List<Role> findAllRoles();

  int updateRole(Role role);

  int deleteResourcePermissionByRoleId(@Param("roleId") String roleId);

  int deleteUiPermissionByRoleId(@Param("roleId") String roleId);

  RoleUiPermission getRoleUiPermissionByRoleId(@Param("roleId") String roleId);

  int roleBindResourcePermissions(@Param("permissions") List<RoleResourcePermission> permissions);

  int roleBindUiPermissions(RoleUiPermission permissions);

  boolean existsRole(@Param("roleId") String roleId);

  ResourcePermission getResourcePermissionByPath(ApiPathDto apiPath);

  String getUserRoleIdByUserId(@Param("userId") long userId);

  int bindUser(UserRole userRole);

  int addResourcePermission(ResourcePermission permission);

  int deleteUserRoles(@Param("userId") Long userId);

  boolean hasPermissionOnPath(
      @Param("roleId") String roleId, @Param("path") String path, @Param("method") String method);
}
