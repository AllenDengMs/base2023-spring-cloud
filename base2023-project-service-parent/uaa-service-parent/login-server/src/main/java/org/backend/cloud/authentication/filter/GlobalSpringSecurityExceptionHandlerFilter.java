package org.backend.cloud.authentication.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.backend.cloud.common.utils.JSON;
import org.backend.cloud.web.exception.BaseException;
import org.backend.cloud.web.model.Result;
import org.backend.cloud.web.model.ResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 捕捉Spring security filter chain 中抛出的异常
 */
public class GlobalSpringSecurityExceptionHandlerFilter extends OncePerRequestFilter {

  public static final Logger logger = LoggerFactory.getLogger(
      GlobalSpringSecurityExceptionHandlerFilter.class);

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (BaseException e) {
      // 自定义异常
      Result result = ResultBuilder.aResult()
          .msg(e.getMessage())
          .code(e.getCode())
          .build();
      response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
      response.setStatus(e.getHttpStatus().value());
      response.getWriter().write(JSON.stringify(result));
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      // 未知异常
      Result result = ResultBuilder.aResult()
          .msg("System Error")
          .code("system.error")
          .build();
      response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
      response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      response.getWriter().write(JSON.stringify(result));
    }
  }
}
