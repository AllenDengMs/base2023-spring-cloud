package org.backend.cloud.web.annotation.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import org.backend.cloud.common.utils.JSON;
import org.backend.cloud.web.annotation.LoginInfo;
import org.backend.cloud.web.model.CurrentUser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginUserResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType() == CurrentUser.class
        && parameter.hasParameterAnnotation(LoginInfo.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
    String userInfoJSON = httpServletRequest.getHeader("userInfo");
    if (userInfoJSON == null) {
      return null;
    }

    return JSON.parse(URLDecoder.decode(userInfoJSON, StandardCharsets.UTF_8),
        new TypeReference<CurrentUser>() {
        });
  }
}
