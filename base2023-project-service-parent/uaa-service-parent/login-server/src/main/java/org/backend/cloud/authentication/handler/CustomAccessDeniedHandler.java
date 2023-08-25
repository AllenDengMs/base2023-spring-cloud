package org.backend.cloud.authentication.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.backend.cloud.common.utils.JSON;
import org.backend.cloud.web.model.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * 登录成功, 但无权访问时。会执行这个方法
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @SuppressWarnings("deprecation")
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    String error =
        "请求Url:" + request.getRequestURI() + " 鉴权失败:" + accessDeniedException.getMessage();
    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    PrintWriter writer = response.getWriter();
    writer.print(JSON.stringify(Result.data(HttpStatus.UNAUTHORIZED.value(), error)));
    writer.flush();
    writer.close();
  }
}
