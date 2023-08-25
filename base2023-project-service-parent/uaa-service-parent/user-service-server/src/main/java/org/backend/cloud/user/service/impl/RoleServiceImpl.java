package org.backend.cloud.user.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.backend.cloud.common.utils.TimeTool;
import org.backend.cloud.user.mapper.RoleMapper;
import org.backend.cloud.user.model.dto.ApiPathDto;
import org.backend.cloud.user.model.dto.UpdateRolePermissionRequest;
import org.backend.cloud.user.model.entity.ResourcePermission;
import org.backend.cloud.user.model.entity.Role;
import org.backend.cloud.user.model.entity.RoleResourcePermission;
import org.backend.cloud.user.model.entity.RoleUiPermission;
import org.backend.cloud.user.service.RoleService;
import org.backend.cloud.web.exception.Exceptions;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

@Service
@CacheConfig(cacheNames = {"webase2023:roles"}) // 指定redis的key前缀
public class RoleServiceImpl implements RoleService {

  private final RoleMapper roleMapper;

  public RoleServiceImpl(RoleMapper roleMapper) {
    this.roleMapper = roleMapper;
  }

  @Caching(evict = {@CacheEvict(allEntries = true)}) // 移除角色缓存
  @Override
  public int addRole(Role role) {
    if (roleMapper.existsRole(role.getRoleId())) {
      Exceptions.error("${roleId.exists:roleId已经存在}");
    }
    role.setCreateTime(TimeTool.now());
    role.setUpdateTime(TimeTool.now());
    return roleMapper.addRole(role);
  }

  @Cacheable(key = "'all_roles'") // 把结果缓存到Redis
  @Override
  public List<Role> findAllRoles() {
    return roleMapper.findAllRoles();
  }

  @Override
  public int updateRole(Role role) {
    role.setUpdateTime(TimeTool.now());
    return roleMapper.updateRole(role);
  }

  @Caching(evict = {@CacheEvict(allEntries = true)}) // 移除角色缓存
  @Override
  @Transactional(rollbackFor = Throwable.class)
  public int updateRolePermission(UpdateRolePermissionRequest request) {
    final String roleId = request.getRoleId();
    final Date now = TimeTool.now();
    roleMapper.deleteResourcePermissionByRoleId(roleId);
    roleMapper.deleteUiPermissionByRoleId(roleId);
    // 新增资源权限
    if (!CollectionUtils.isEmpty(request.getApiPermissions())) {
      List<RoleResourcePermission> apiPermissionsBindInfo = new ArrayList<>();
      for (ApiPathDto apiPath : request.getApiPermissions()) {
        ResourcePermission resourcePermission = saveResourcePermission(apiPath,
            request.getOperatorUserId());
        RoleResourcePermission bindInfo = new RoleResourcePermission();
        bindInfo.setRoleId(roleId);
        bindInfo.setCreateTime(now);
        bindInfo.setPermissionId(resourcePermission.getPermissionId());
        apiPermissionsBindInfo.add(bindInfo);
      }
      if (apiPermissionsBindInfo.size() > 0) {
        roleMapper.roleBindResourcePermissions(apiPermissionsBindInfo);
      }
    }
    // 新增ui权限
    if (Objects.nonNull(request.getUiPermissions())) {
      RoleUiPermission roleUiPermission = new RoleUiPermission();
      roleUiPermission.setRoleId(roleId);
      roleUiPermission.setPermissions(request.getUiPermissions());
      roleUiPermission.setCreateTime(now);
      roleMapper.roleBindUiPermissions(roleUiPermission);
    }
    return 1;
  }

  /**
   * 初始化资源权限数据
   */
  private ResourcePermission saveResourcePermission(ApiPathDto apiPath, Long operatorUserId) {
    ResourcePermission permission = roleMapper.getResourcePermissionByPath(apiPath);
    if (permission == null) {
      permission = new ResourcePermission();
      permission.setPath(apiPath.getPath().toLowerCase());
      permission.setMethod(apiPath.getMethod().toUpperCase());
      permission.setOperatorUserId(operatorUserId);
      permission.setCreateTime(TimeTool.now());
      // 权限id，给个唯一值，直接md5(apiPath+method)
      String md5Content = permission.getPath().concat(permission.getMethod());
      permission.setPermissionId(
          DigestUtils.md5DigestAsHex(md5Content.getBytes(StandardCharsets.UTF_8)));
      roleMapper.addResourcePermission(permission);
    }
    return permission;
  }

  @Override
  public RoleUiPermission getRoleUiPermissionByRoleId(String roleId) {
    return roleMapper.getRoleUiPermissionByRoleId(roleId);
  }

  @Override
  public String getUserRoleIdByUserId(long userId) {
    return roleMapper.getUserRoleIdByUserId(userId);
  }

  @Cacheable(key = "'rp_' + #roleId + #path + #method") // 把结果缓存到Redis
  @Override
  public boolean hasPermissionOnPath(String roleId, String path, String method) {
    return roleMapper.hasPermissionOnPath(roleId, path, method.toUpperCase());
  }
}
