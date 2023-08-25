package org.backend.cloud.authentication.provider;

import org.backend.cloud.api.client.user.UserServiceClient;
import org.backend.cloud.authentication.model.PasswordAuthenticationToken;
import org.backend.cloud.common.utils.JSON;
import org.backend.cloud.user.model.entity.User;
import org.backend.cloud.web.model.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 帐号密码登录认证
 */
@Component
public class PasswordLoginProvider implements AuthenticationProvider {

  private final PasswordEncoder passwordEncoder;

  @Autowired
  @Lazy
  private UserServiceClient userServiceClient;

  public PasswordLoginProvider(PasswordEncoder passwordEncoder) {
    super();
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    // 用户提交的用户名 + 密码：
    String username = authentication.getName();
    String password = (String) authentication.getCredentials();

    // 查数据库，匹配用户信息
    User user = userServiceClient.getUserByUsername(username);
    if (user == null
        || !passwordEncoder.matches(password.concat(user.getPasswordSalt()), user.getPassword())) {
      // 密码错误，直接抛异常。
      // 根据SpringSecurity框架的代码逻辑，认证失败时，应该抛这个异常：org.springframework.security.core.AuthenticationException
      // BadCredentialsException就是这个异常的子类
      throw new BadCredentialsException("用户名或密码不正确!");
    }

    return PasswordAuthenticationToken.loginSuccess(JSON.convertTo(user, CurrentUser.class));
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.isAssignableFrom(PasswordAuthenticationToken.class);
  }

}

