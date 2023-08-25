package org.backend.cloud.web.exception;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {

  private static final long serialVersionUID = -7972131521045668011L;
  private final HttpStatus httpStatus;
  private String code; // 自定义一个全局唯一的code，

  public BaseException() {
    httpStatus = HttpStatus.BAD_REQUEST;
  }

  public BaseException(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }

  public BaseException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public BaseException(String message, Throwable cause, HttpStatus httpStatus) {
    super(message, cause);
    this.httpStatus = httpStatus;
  }

  public BaseException(Throwable cause, HttpStatus httpStatus) {
    super(cause);
    this.httpStatus = httpStatus;
  }

  public BaseException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace, HttpStatus httpStatus) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.httpStatus = httpStatus;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}