package org.backend.cloud.authentication.service;

import org.backend.cloud.authentication.model.entity.AuthorizeResult;
import org.backend.cloud.web.model.CurrentUser;
import org.springframework.http.HttpMethod;

public interface AuthorizationService {

  /** 是否在授权黑名单之中 */
  boolean inAccessBlackList(CurrentUser user);

  void cleanAccessBlackList(CurrentUser user);

  /**
   * 校验用户是否有权访问这个路径
   * @param user 用户信息
   * @param toApiPath 将要访问的路径
   * @return
   */
  boolean hasPermission(CurrentUser user, String toApiPath, HttpMethod method);

  void logout(CurrentUser currentUser);

  /**
   * 网关鉴权
   */
  AuthorizeResult checkPermissionOnPath(String jwtToken, String toPath, String method);
}
