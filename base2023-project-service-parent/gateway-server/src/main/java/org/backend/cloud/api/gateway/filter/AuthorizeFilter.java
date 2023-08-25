package org.backend.cloud.api.gateway.filter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.backend.cloud.authentication.model.entity.AuthorizeResult;
import org.backend.cloud.common.utils.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Order(-1)
@Component
public class AuthorizeFilter implements GlobalFilter {

  private static final Logger logger = LoggerFactory.getLogger(AuthorizeFilter.class);

  private final WebClient webClient;

  public AuthorizeFilter(WebClient webClient) {
    this.webClient = webClient;
  }

  public static final String SYSTEM_ERROR_MESSAGE = "{\"code\":\"system.error\",\"message\":\"系统错误\",\"data\":null}";
  /** 授权服务器地址 */
  public static final String AUTH_SERVER_ENDPOINT = "http://authentication-service/authorization/check-permission?toPath=";
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    String toPath = request.getPath().value();
    String method = request.getMethod().toString();
    String token = request.getHeaders().getFirst("Authorization");
    String authUrl = AUTH_SERVER_ENDPOINT.concat(toPath).concat("&method=").concat(method);

    try {
      return webClient.get().uri(authUrl)// 调用鉴权微服务的接口进行鉴权
          .header("Authorization", token)
          .retrieve().bodyToMono(String.class)
          .flatMap(response -> {
            // 得到鉴权结果，根据鉴权结果
            AuthorizeResult authorizeResult = JSON.parse(response, AuthorizeResult.class);
            if (authorizeResult.isCanVisit()) {
              // 可以访问
              setUserInfoIntoHeader(authorizeResult.getUserInfo(), exchange);
              return chain.filter(exchange);
            } else {
              // 无权访问
              exchange.getResponse()
                  .setStatusCode(HttpStatus.valueOf(authorizeResult.getHttpStatus()));
              exchange.getResponse().getHeaders().set("Content-Type", "text/plain; charset=UTF-8");
              byte[] bytes = authorizeResult.getErrorMessage().getBytes(StandardCharsets.UTF_8);
              DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
              return exchange.getResponse().writeWith(Flux.just(buffer));
            }
          });
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
      exchange.getResponse().getHeaders().set("Content-Type", "text/plain; charset=UTF-8");
      byte[] bytes = SYSTEM_ERROR_MESSAGE.getBytes(StandardCharsets.UTF_8);
      DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
      return exchange.getResponse().writeWith(Flux.just(buffer));
    }
  }

  private static void setUserInfoIntoHeader(String  userInfoJson, ServerWebExchange exchange) {
    if (userInfoJson == null) {
      return;
    }
    // 网关下发请求给微服务时，去掉jwt数据，减少网络传输数据量，从而提高性能
    ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();
    requestBuilder.headers(header -> header.remove("Authorization"));
    // 将用户信息设置进入
    requestBuilder.headers(header -> {
      header.set("userInfo", URLEncoder.encode(userInfoJson, StandardCharsets.UTF_8));
    });
    exchange.mutate().request(requestBuilder.build()).build();
  }

}