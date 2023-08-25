package org.backend.cloud.web.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.backend.cloud.web.annotation.impl.ConvertConstantValueSerializer;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JsonSerialize(using = ConvertConstantValueSerializer.class)
@JacksonAnnotationsInside
public @interface ConvertConstantValue {

  /**
   * 常量名
   */
  String value();

  /**
   * 翻译后的结果放哪个字段
   */
  String targetFieldName() default "";
}
