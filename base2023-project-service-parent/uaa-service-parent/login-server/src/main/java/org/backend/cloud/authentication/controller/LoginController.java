package org.backend.cloud.authentication.controller;

import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import org.backend.cloud.api.client.user.UserServiceClient;
import org.backend.cloud.authentication.event.RefreshJwtTokenSuccessEvent;
import org.backend.cloud.authentication.handler.LoginSuccessHandler;
import org.backend.cloud.authentication.model.dto.GetLoginSmsCodeParam;
import org.backend.cloud.authentication.model.dto.RefreshTokenDto;
import org.backend.cloud.authentication.service.AuthorizationService;
import org.backend.cloud.authentication.service.JwtService;
import org.backend.cloud.web.annotation.LoginInfo;
import org.backend.cloud.web.model.CurrentUser;
import org.backend.cloud.web.model.Result;
import org.backend.cloud.web.model.ResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {

  private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

  @Autowired
  private LoginSuccessHandler loginSuccessHandler;

  @Autowired
  @Lazy
  private UserServiceClient userServiceClient;

  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  @Autowired
  private AuthorizationService authorizationService;

  @Autowired
  private JwtService jwtService;

  @PostMapping("/login/sms/code")
  public Result getLoginSmsCode(@RequestBody @Validated GetLoginSmsCodeParam param) {
    // TODO: 2023/8/20 发送短信验证码
    return Result.data(true, "${send.login.sms.code.success:发送短信验证码成功!}");
  }

  @GetMapping("/logout")
  public Result logout(@LoginInfo CurrentUser currentUser) {
    authorizationService.logout(currentUser);
    return Result.success();
  }

  @PostMapping("/refresh/token")
  public Result updateToken(@RequestBody RefreshTokenDto request, HttpServletResponse response) {
    try {
      String refreshToken = request.getRefreshToken();
      CurrentUser currentUser = jwtService.verifyJwt(refreshToken, CurrentUser.class);
      // 刷新token使，重新获取roleId
      String roleId = userServiceClient.getRoleIdByUserId(currentUser.getUserId());
      currentUser.setRoleId(roleId);
      String token = loginSuccessHandler.generateToken(currentUser);

      HashMap<String, String> responseData = new HashMap<>();
      responseData.put("token", token);
      responseData.put("refreshToken", refreshToken);

      applicationEventPublisher.publishEvent(RefreshJwtTokenSuccessEvent.of(currentUser));
      return Result.data(responseData);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      return ResultBuilder.aResult()
          .data(null)
          .code("unauthorized")
          .msg("${refresh.token.expired:登录信息已过期，请重新登录}")
          .build();
    }
  }
}
