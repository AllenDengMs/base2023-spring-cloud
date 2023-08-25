package org.backend.cloud.authentication.filter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import org.backend.cloud.authentication.model.SmsCodeAuthenticationToken;
import org.backend.cloud.common.utils.JSON;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 要在Spring Security中实现短信验证码登录的方式，您可以按照以下步骤进行操作：
 *  1. 创建一个自定义的短信验证码过滤器（SmsCodeFilter），继承自AbstractAuthenticationProcessingFilter，并实现相关的逻辑。该过滤器将负责拦截短信验证码登录请求，并验证验证码的正确性。
 *  2. 创建一个自定义的身份验证提供者（SmsCodeAuthenticationProvider），实现AuthenticationProvider接口，并实现相关的逻辑。该提供者将负责验证用户的手机号和验证码是否匹配，并返回相应的认证结果。
 *  3. 配置Spring Security，将自定义的短信验证码过滤器和身份验证提供者添加到过滤器链中。
 *  以下是一个简单的示例代码，演示如何实现短信验证码登录：
 */
public class SmsCodeLoginFilter extends AbstractAuthenticationProcessingFilter {

  public SmsCodeLoginFilter(AntPathRequestMatcher requestMatcher) {
    super(requestMatcher);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    // 提取请求数据
    String requestJsonData = request.getReader().lines()
        .collect(Collectors.joining(System.lineSeparator()));
    Map<String, Object> requestMapData = JSON.parseToMap(requestJsonData);
    String phoneNumber = requestMapData.get("phone").toString();
    String smsCode = requestMapData.get("captcha").toString();

    SmsCodeAuthenticationToken token = SmsCodeAuthenticationToken.clientPost(phoneNumber, smsCode);
    setDetails(request, token);
    return this.getAuthenticationManager().authenticate(token);
  }

  /**
   * 提取用户客户端ip等数据（可选）
   */
  private void setDetails(HttpServletRequest request, SmsCodeAuthenticationToken authRequest) {
    authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
  }
}