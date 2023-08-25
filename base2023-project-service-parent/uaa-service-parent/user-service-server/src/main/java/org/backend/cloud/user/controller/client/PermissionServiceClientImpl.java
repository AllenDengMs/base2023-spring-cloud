package org.backend.cloud.user.controller.client;

import org.backend.cloud.api.client.user.PermissionServiceClient;
import org.backend.cloud.api.client.user.UserServiceClientApiPath;
import org.backend.cloud.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserServiceClientApiPath.PERMISSION)
public class PermissionServiceClientImpl implements PermissionServiceClient {

  @Autowired
  private RoleService roleService;

  @Override
  public boolean hasPermissionOnPath(String roleId, String path, String method) {
    return roleService.hasPermissionOnPath(roleId, path, method);
  }
}
