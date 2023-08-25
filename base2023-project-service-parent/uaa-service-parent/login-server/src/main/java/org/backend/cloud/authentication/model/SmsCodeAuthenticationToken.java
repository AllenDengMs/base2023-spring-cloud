package org.backend.cloud.authentication.model;

import org.backend.cloud.web.model.CurrentUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken {

  /**
   * 开始认证前,调用getPrincipal()获取到的是一个字符串类型的手机号,这是用户输入的手机号信息。
   * 在认证过程中,AuthenticationManager会对输入的手机号 + 验证码进行认证。
   * 如果认证通过,AuthenticationManager会生成一个全新的Authentication对象, 里面包含了一个UserDetails类型的principal。
   * 这个UserDetails principal包含了用户名、密码、权限等更多信息,它通过认证后才能获取到。
   * 所以认证后再调用getPrincipal()获取到的就是一个包含详细信息的UserDetails对象,不再是简单的手机号字符串。
   * 应用程序通过这个UserDetails principal对象,可以获得用户的详细信息,来进行授权等操作。
   */
  private Object principal;
  /**
   * 获取用户输入的凭证信息,如密码、短信验证码。
   */

  private Object credentials;

  // 改成私有方法，不允许外部乱new对象，调用下面的静态方法new，保存数据的正确性
  private SmsCodeAuthenticationToken() {
    super(null); // 权限，用不上，直接null
  }

  /**
   * 请求到达后台服务器后，需要从HttpServletRequest获取用户提交的表单数据
   */
  public static SmsCodeAuthenticationToken clientPost(String phone, String code) {
    SmsCodeAuthenticationToken authenticationToken = new SmsCodeAuthenticationToken();
    authenticationToken.setPrincipal(phone);
    authenticationToken.setCredentials(code);
    // 方便其他地方获取到token
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    return authenticationToken;
  }

  public static SmsCodeAuthenticationToken loginSuccess(CurrentUser currentUser) {
    SmsCodeAuthenticationToken authenticationToken = new SmsCodeAuthenticationToken();
    authenticationToken.setPrincipal(currentUser);
    authenticationToken.setAuthenticated(true); // 认证成功
    // 方便其他地方获取到token
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    return authenticationToken;
  }

  @Override
  public Object getCredentials() {
    return this.credentials;
  }

  @Override
  public Object getPrincipal() {
    return this.principal;
  }

  public void setPrincipal(Object principal) {
    this.principal = principal;
  }

  public void setCredentials(Object credentials) {
    this.credentials = credentials;
  }
}