package org.backend.cloud.web.exception;

import feign.FeignException;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import org.backend.cloud.common.utils.JSON;
import org.backend.cloud.web.model.Result;
import org.backend.cloud.web.model.ResultBuilder;
import org.backend.cloud.web.utils.I18nMessageTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WebGlobalExceptionHandler {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @ExceptionHandler(value = Exception.class)
  public Result exceptionHandler(HttpServletResponse response, Exception e) {
    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    logger.info("服务器异常", e);
    return Result.fail("服务器异常");
  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public Result exceptionHandler(HttpServletResponse response, MethodArgumentNotValidException e) {
    response.setStatus(HttpStatus.BAD_REQUEST.value());

    // 国际化翻译 数据校验异常信息
    BindingResult result = e.getBindingResult();
    List<FieldError> fieldErrors = result.getFieldErrors();
    HashMap<String, String> errorFields = new HashMap<>();
    for (FieldError error : fieldErrors) {
      String fieldName = error.getField();
      errorFields.put(fieldName, I18nMessageTool.translate(error.getDefaultMessage()));
    }

    return Result.fail(JSON.stringify(errorFields));
  }

  @ExceptionHandler(value = BaseException.class)
  public Result exceptionHandler(HttpServletResponse response, BaseException e) {
    response.setStatus(e.getHttpStatus().value());
    return createResult(e);
  }

  /**
   * 传递微服务抛出的业务异常消息给客户端
   */
  @ExceptionHandler(value = FeignException.class)
  public Result exceptionHandler(HttpServletResponse response, FeignException feignException) {
    response.setStatus(feignException.status());
    return JSON.parse(feignException.contentUTF8(), Result.class);
  }

  private Result createResult(BaseException e) {
    return ResultBuilder.aResult()
        .msg(e.getMessage())
        .code(e.getCode() == null ? Result.FAIL_CODE : e.getCode())
        .build();
  }
}
