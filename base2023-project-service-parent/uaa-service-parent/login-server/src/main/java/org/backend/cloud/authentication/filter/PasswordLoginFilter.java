package org.backend.cloud.authentication.filter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import org.backend.cloud.authentication.model.PasswordAuthenticationToken;
import org.backend.cloud.common.utils.JSON;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 用户名密码登录
 */
public class PasswordLoginFilter extends AbstractAuthenticationProcessingFilter {

  public PasswordLoginFilter(AntPathRequestMatcher requestMatcher) {
    super(requestMatcher);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    // 提取请求数据
    String requestJsonData = request.getReader().lines()
        .collect(Collectors.joining(System.lineSeparator()));
    Map<String, Object> requestMapData = JSON.parseToMap(requestJsonData);
    String username = requestMapData.get("username").toString();
    String password = requestMapData.get("password").toString();

    // 封装成Spring Security需要的对象
    Authentication authentication = PasswordAuthenticationToken.clientPost(username, password);

    // 开始登录认证。SpringSecurity会利用 Authentication对象去寻找 AuthenticationProvider进行登录认证
    return getAuthenticationManager().authenticate(authentication);
  }

}
