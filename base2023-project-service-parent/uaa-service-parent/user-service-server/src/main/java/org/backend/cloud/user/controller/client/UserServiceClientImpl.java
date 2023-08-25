package org.backend.cloud.user.controller.client;

import org.backend.cloud.api.client.user.UserServiceClient;
import org.backend.cloud.api.client.user.UserServiceClientApiPath;
import org.backend.cloud.user.model.entity.User;
import org.backend.cloud.user.service.RoleService;
import org.backend.cloud.user.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserServiceClientApiPath.USER)
public class UserServiceClientImpl implements UserServiceClient {

  private final UserService userService;

  private final RoleService roleService;

  public UserServiceClientImpl(UserService userService, RoleService roleService) {
    this.userService = userService;
    this.roleService = roleService;
  }

  @Override
  public User getUserByUserId(String userId) {
    return userService.getUserById(Long.parseLong(userId));
  }

  @Override
  public User getUserByUsername(String username) {
    return userService.getUserByUsername(username);
  }

  @Override
  public User getUserByPhoneNumber(String phone) {
    return userService.getUserByPhone(phone);
  }

  @Override
  public String getRoleIdByUserId(long userId) {
    return roleService.getUserRoleIdByUserId(userId);
  }
}
