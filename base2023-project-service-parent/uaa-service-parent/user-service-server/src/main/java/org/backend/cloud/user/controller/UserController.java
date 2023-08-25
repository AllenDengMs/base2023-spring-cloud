package org.backend.cloud.user.controller;

import java.util.HashMap;
import org.backend.cloud.user.entity.dto.AddUserRequest;
import org.backend.cloud.user.entity.dto.UpdateUserRequest;
import org.backend.cloud.user.model.builder.UserQueryBuilder;
import org.backend.cloud.user.model.entity.RoleUiPermission;
import org.backend.cloud.user.service.RoleService;
import org.backend.cloud.user.service.UserService;
import org.backend.cloud.web.annotation.LoginInfo;
import org.backend.cloud.web.exception.Exceptions;
import org.backend.cloud.web.model.CurrentUser;
import org.backend.cloud.web.model.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  private final RoleService roleService;

  public UserController(UserService userService, RoleService roleService) {
    this.userService = userService;
    this.roleService = roleService;
  }

  @GetMapping("/login/info")
  public Result getLoginUserInfo(@LoginInfo CurrentUser user) {
    HashMap<Object, Object> userVO = new HashMap<>();
    userVO.put("userName", user.getNickname());
    userVO.put("userId", user.getUserId());

    String roleId = roleService.getUserRoleIdByUserId(user.getUserId());
    userVO.put("userRole", roleId);

    RoleUiPermission uiPermission = roleService.getRoleUiPermissionByRoleId(roleId);
    userVO.put("permissions", uiPermission == null ? null : uiPermission.getPermissions());
    return Result.data(userVO);
  }

  @PostMapping
  public Result addUser(@LoginInfo CurrentUser currentUser,
      @RequestBody @Validated AddUserRequest request) {

    // 数据校验
    String currentUserRoleId = roleService.getUserRoleIdByUserId(currentUser.getUserId());
    if (!currentUserRoleId.equals("super") && request.getRoleId() != null && request.getRoleId()
        .equalsIgnoreCase("super")) {
      Exceptions.error("非超管账号，不允许给其他账号设置超管角色");
    }

    return Result.row(userService.addUser(request));
  }

  @PutMapping
  public Result updateUser(@LoginInfo CurrentUser currentUser,
      @RequestBody @Validated UpdateUserRequest request) {
    // 数据校验
    String roleId = roleService.getUserRoleIdByUserId(request.getUserId());
    if (roleId != null && roleId.equals("super")) {
      Exceptions.error("不允许修改超级管理员的信息");
    }
    String currentUserRoleId = roleService.getUserRoleIdByUserId(currentUser.getUserId());
    if (!currentUserRoleId.equals("super") && request.getRoleId() != null && request.getRoleId()
        .equalsIgnoreCase("super")) {
      Exceptions.error("非超管账号，不允许给其他账号设置超管角色");
    }

    request.setOperatorUserId(currentUser.getUserId());
    return Result.row(userService.updateUser(request));
  }


  /** 查询下级用户 */
  @GetMapping("/mine")
  public Result pageQueryUsers(UserQueryBuilder queryBuilder) {
    return Result.data(userService.pageQueryUsers(queryBuilder));
  }

}
