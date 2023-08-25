package org.backend.cloud.common.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.backend.cloud.common.annotation.impl.MaskSerializer;

/**
 * 屏蔽手机号
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JsonSerialize(using = MaskSerializer.class)
@JacksonAnnotationsInside
public @interface Mask {

  /**
   * 指定屏蔽数据的处理器。默认支持：phone、star
   * @see org.backend.cloud.common.annotation.service.MaskDataHandler
   * @see org.backend.cloud.common.annotation.service.impl.MaskPhoneNumberHandler
   * @see org.backend.cloud.common.annotation.service.impl.MaskWithStarHandler
   */
  String handler() default "";
}
