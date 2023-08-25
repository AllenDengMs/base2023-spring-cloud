package org.backend.cloud.user.controller;

import org.backend.cloud.user.model.dto.UpdateRolePermissionRequest;
import org.backend.cloud.user.model.entity.Role;
import org.backend.cloud.user.service.RoleService;
import org.backend.cloud.web.annotation.LoginInfo;
import org.backend.cloud.web.model.CurrentUser;
import org.backend.cloud.web.model.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleController {

  private final RoleService roleService;

  public RoleController(RoleService roleService) {
    this.roleService = roleService;
  }

  @PutMapping
  public Result updateRole(@RequestBody Role role) {
    return Result.row(roleService.updateRole(role));
  }

  @PostMapping
  public Result addRole(@RequestBody Role role) {
    return Result.data(roleService.addRole(role));
  }

  @GetMapping
  public Result findRoles() {
    return Result.data(roleService.findAllRoles());
  }

  @PutMapping("/permissions")
  public Result updateRolePermission(@LoginInfo CurrentUser currentUserInfo,
      @RequestBody UpdateRolePermissionRequest request) {
    request.setOperatorUserId(currentUserInfo.getUserId());
    return Result.data(roleService.updateRolePermission(request));
  }

  @GetMapping("/ui-permissions")
  public Result findRoleUiPermission(@RequestParam String roleId) {
    return Result.data(roleService.getRoleUiPermissionByRoleId(roleId));
  }
}