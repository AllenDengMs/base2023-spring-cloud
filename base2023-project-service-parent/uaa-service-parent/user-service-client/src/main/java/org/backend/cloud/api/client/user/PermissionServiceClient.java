package org.backend.cloud.api.client.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = UserServiceClientApiPath.APPLICATION_NAME,
    path = UserServiceClientApiPath.PERMISSION)
public interface PermissionServiceClient {

  @GetMapping("/has-permission-on-path")
  boolean hasPermissionOnPath(@RequestParam("roleId") String roleId,
      @RequestParam("path") String path,
      @RequestParam("method") String method);

}
