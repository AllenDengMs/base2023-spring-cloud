package org.backend.cloud.api.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiGatewayApplication.class, args);
  }

  /**
   * 官方提示
   * {@link https://spring.io/blog/2020/03/25/spring-tips-spring-cloud-loadbalancer}
   */
  @Bean
  @LoadBalanced
  public WebClient.Builder builder() {
    return WebClient.builder();
  }

  @Bean
  public WebClient webClient(WebClient.Builder builder) {
    return builder.build();
  }

}