package org.backend.cloud.web.openfeign.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class CustomizedFeignAutoConfiguration {

  private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

  /**
   * <pre>
   *   参考文档：https://juejin.cn/post/7111656818060820493
   *    自定义的请求头处理类，处理服务发送时的请求头；
   *    将服务接收到的请求头中的uniqueId和token字段取出来，并设置到新的请求头里面去转发给下游服务
   *    比如A服务收到一个请求，请求头里面包含token字段，A处理时会使用Feign客户端调用B服务
   *    那么token这个字段就会添加到请求头中一并发给B服务；
   * </pre>
   *
   */
  @Bean
  public RequestInterceptor requestInterceptor() {
    return requestTemplate -> {
      ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      if (attrs != null) {
        HttpServletRequest request = attrs.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
          // 遍历请求头里面的属性字段，token添加到新的请求头中转发到下游服务
          while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            requestTemplate.header(name, value);
          }
        } else {
          logger.warn("FeignHeadConfiguration: " + "获取请求头失败！");
        }
      }
    };
  }

}
