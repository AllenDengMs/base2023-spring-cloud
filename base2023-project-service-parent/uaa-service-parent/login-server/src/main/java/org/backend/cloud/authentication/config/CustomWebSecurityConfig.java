package org.backend.cloud.authentication.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.backend.cloud.authentication.filter.GlobalSpringSecurityExceptionHandlerFilter;
import org.backend.cloud.authentication.filter.PasswordLoginFilter;
import org.backend.cloud.authentication.filter.SmsCodeLoginFilter;
import org.backend.cloud.authentication.handler.CustomAccessDeniedHandler;
import org.backend.cloud.authentication.handler.LoginFailHandler;
import org.backend.cloud.authentication.handler.LoginSuccessHandler;
import org.backend.cloud.web.constant.URLWitheList;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfig {


  private final LoginSuccessHandler loginSuccessHandler;

  private final LoginFailHandler loginFailHandler;

  private final ApplicationContext applicationContext;

  public CustomWebSecurityConfig(LoginSuccessHandler loginSuccessHandler,
      LoginFailHandler loginFailHandler, ApplicationContext applicationContext) {
    this.loginSuccessHandler = loginSuccessHandler;
    this.loginFailHandler = loginFailHandler;
    this.applicationContext = applicationContext;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // 请求拦截
    http.authorizeHttpRequests()
        .anyRequest().permitAll(); // 网关鉴权，内部微服务不鉴权

    // 不需要内置的表单登录
    http.formLogin().disable();
    // 不需要内置的session管理
    http.sessionManagement().disable()
        .logout().disable()
        .httpBasic().disable()
        .csrf().disable();

    // 全局异常配置
    http.exceptionHandling()
        .accessDeniedHandler(new CustomAccessDeniedHandler());

    // 统一管理SpringSecurity的异常。
    http.addFilterBefore(new GlobalSpringSecurityExceptionHandlerFilter(), LogoutFilter.class);

    // 加一个登录方式。用户名、密码登录
    http.addFilterBefore(passwordAuthenticationFilter(), LogoutFilter.class);
    http.addFilterBefore(smsCodeFilter(), LogoutFilter.class);
    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    List<AuthenticationProvider> providers = new ArrayList<>();

    // 手动注册provider示例：
    // providers.add(applicationContext.getBean(SmsCodeLoginProvider.class));
    // providers.add(applicationContext.getBean(PasswordLoginProvider.class));

    // 更方便简单的注册provider示例：直接获取所有AuthenticationProvider的bean
    Map<String, AuthenticationProvider> beansMap = applicationContext.getBeansOfType(
        AuthenticationProvider.class);
    providers.addAll(beansMap.values());
    return new ProviderManager(providers);
  }

  /**
   * 用户名密码登录
   */
  @Bean
  public PasswordLoginFilter passwordAuthenticationFilter() throws Exception {
    // 请求路径
    String path = "/auth/login";
    URLWitheList.NO_NEED_LOGIN.add(path); // 加鉴权放行白名单
    AntPathRequestMatcher requestPath = new AntPathRequestMatcher(path, "POST");

    PasswordLoginFilter filter = new PasswordLoginFilter(requestPath);
    filter.setAuthenticationManager(authenticationManager());
    filter.setAuthenticationSuccessHandler(loginSuccessHandler); // 登录成功后，要执行的代码
    filter.setAuthenticationFailureHandler(loginFailHandler); // 登录失败后，要执行的代码
    return filter;
  }

  @Bean
  public SmsCodeLoginFilter smsCodeFilter() throws Exception {
    String path = "/auth/login/sms";
    URLWitheList.NO_NEED_LOGIN.add(path); // 加鉴权放行白名单
    AntPathRequestMatcher requestPath = new AntPathRequestMatcher(path, "POST");

    SmsCodeLoginFilter filter = new SmsCodeLoginFilter(requestPath);
    filter.setAuthenticationManager(authenticationManager());
    filter.setAuthenticationSuccessHandler(loginSuccessHandler); // 登录成功，要执行的代码
    filter.setAuthenticationFailureHandler(loginFailHandler); // 登录失败后，要执行的代码
    return filter;
  }
}
