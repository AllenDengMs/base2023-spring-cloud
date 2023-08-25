package org.backend.cloud.authentication.provider;

import org.backend.cloud.api.client.user.UserServiceClient;
import org.backend.cloud.authentication.model.SmsCodeAuthenticationToken;
import org.backend.cloud.common.utils.JSON;
import org.backend.cloud.user.model.entity.User;
import org.backend.cloud.web.model.CurrentUser;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class SmsCodeLoginProvider implements AuthenticationProvider {

  private UserServiceClient userService;

  public SmsCodeLoginProvider(UserServiceClient userService) {
    this.userService = userService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String phoneNumber = authentication.getPrincipal().toString();
    String smsCode = authentication.getCredentials().toString();
    // 验证验证码是否正确
    if (validateSmsCode(smsCode)) {
      // 根据手机号查询用户信息
      User user = userService.getUserByPhoneNumber(phoneNumber);
      if (user == null) {
        // 密码错误，直接抛异常。
        // 根据SpringSecurity框架的代码逻辑，认证失败时，应该抛这个异常：org.springframework.security.core.AuthenticationException
        // BadCredentialsException就是这个异常的子类
        throw new BadCredentialsException("${user.not.found:找不到用户!}");
      }
      CurrentUser currentUser = JSON.convertTo(user, CurrentUser.class);

      SmsCodeAuthenticationToken token = SmsCodeAuthenticationToken.loginSuccess(currentUser);
      token.setDetails(authentication.getDetails()); // 传递客户端信息（请求ip、浏览器、操作系统等信息）
      return token;
    } else {
      throw new BadCredentialsException("${verify.sms.code.fail:验证码不正确！}");
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
  }

  private boolean validateSmsCode(String smsCode) {
    // todo
    return smsCode.equals("000000");
  }
}