package org.backend.cloud.web.constant;

import java.util.HashSet;
import java.util.Set;
import org.springframework.http.HttpMethod;

public class URLWitheList {

  /** 公开接口 */
  public static final Set<String> NO_NEED_LOGIN;
  static {
    NO_NEED_LOGIN = new HashSet<>();
    NO_NEED_LOGIN.add("/auth/refresh/token");
    NO_NEED_LOGIN.add("/authorization/check-permission"); // 检查权限，只有网关使用，外部网络无法访问
  }

  /** 要求登录后才能访问，同时无需其他权限 */
  public static final Set<String> NO_NEED_PERMISSION = new HashSet<>();
  static {
    NO_NEED_PERMISSION.add("/users/login/info".concat(HttpMethod.GET.toString())); // 用户获取自己的信息
    NO_NEED_PERMISSION.add("/auth/logout".concat(HttpMethod.GET.toString())); // 退出登录
  }
}