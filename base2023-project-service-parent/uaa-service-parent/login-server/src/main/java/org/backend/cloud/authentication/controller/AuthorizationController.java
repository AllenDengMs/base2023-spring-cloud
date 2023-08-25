package org.backend.cloud.authentication.controller;

import org.backend.cloud.authentication.model.entity.AuthorizeResult;
import org.backend.cloud.authentication.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authorization")
public class AuthorizationController {

  @Autowired
  private AuthorizationService authorizationService;

  @GetMapping("/check-permission")
  public AuthorizeResult checkPermission(
      @RequestHeader(value = "Authorization", required = false) String token,
      @RequestParam("toPath") String toPath, @RequestParam("method") String method) {
    return authorizationService.checkPermissionOnPath(token, toPath, method);
  }
}
