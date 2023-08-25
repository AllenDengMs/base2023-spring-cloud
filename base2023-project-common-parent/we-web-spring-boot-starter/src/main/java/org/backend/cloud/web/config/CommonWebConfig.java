package org.backend.cloud.web.config;

import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.backend.cloud.common.utils.IdGenerator;
import org.backend.cloud.web.annotation.impl.LoginUserResolver;
import org.backend.cloud.web.utils.I18nMessageTool;
import org.backend.cloud.web.utils.SpringBeanTool;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CommonWebConfig implements WebMvcConfigurer, InitializingBean {

  @Value("${project.machine-id:1}")
  private Integer machineId;
  @Value("${project.datacenter-id:1}")
  private Integer datacenterId;

  @Autowired
  private ApplicationContext applicationContext;

  /**
   * 获取登陆用户的信息。
   */
  @Bean
  public LoginUserResolver loginUserResolver() {
    return new LoginUserResolver();
  }

  /**
   * 注册自定义的SpringMVC请求参数解析器
   */
  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(loginUserResolver());
  }

  /**
   * Springboot的国际化翻译，
   *    默认读取src/resource/i18n/messages.properties文件
   */
  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:i18n/messages");
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setCacheSeconds(3600);
    messageSource.setAlwaysUseMessageFormat(false);
    messageSource.setFallbackToSystemLocale(true);
    // 支持国际化消息
    I18nMessageTool.setMessageSource(messageSource);
    return messageSource;
  }

  /**
   * 全局配置Spring Controller序列化
   */
  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    // 数字相关的类型，全部格式化成字符串
    objectMapper.configure(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS.mappedFeature(), true);
    // 当json传来多余的字段过来，反序列化时不抛异常
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return objectMapper;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    // 更改雪花算法的machineId、datacenterId避免分布式环境下产生相同的id
    IdGenerator.register(machineId.longValue(), datacenterId.longValue());
    // 某些场景，需要通过静态方法获取到Bean，所以要用一个静态变量，指向 applicationContext
    SpringBeanTool.setApplicationContext(applicationContext);
  }
}
