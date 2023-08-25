package org.backend.cloud.authentication.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.web.RedirectStrategy;

/**
 * 无需重定向
 */
public class NoRedirectStrategy implements RedirectStrategy {

  @Override
  public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url)
      throws IOException {
    //Do nothing, no redirects in REST
  }
}
