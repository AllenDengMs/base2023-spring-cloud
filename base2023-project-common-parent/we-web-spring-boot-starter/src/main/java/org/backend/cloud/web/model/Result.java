package org.backend.cloud.web.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 响应信息主体
 *
 */
public class Result implements Serializable {

  private static final long serialVersionUID = -6149948941889889657L;
  private String code;
  private String message;
  private Object data;

  public static final String SUCCESS_CODE = "success";
  public static final String FAIL_CODE = "fail";

  public static Result success() {
    return ResultBuilder.aResult()
        .data(null)
        .code(SUCCESS_CODE)
        .msg(SUCCESS_CODE)
        .build();
  }

  public static Result success(String message) {
    return ResultBuilder.aResult()
        .data(null)
        .code(SUCCESS_CODE)
        .msg(message)
        .build();
  }

  public static Result row(int row) {
    HashMap<String, Object> data = new HashMap<>();
    data.put("row", row);
    if (row > 0) {
      return data(data);
    } else {
      return fail(data);
    }
  }

  public static Result data(Object data) {
    return ResultBuilder.aResult()
        .data(data)
        .code(SUCCESS_CODE)
        .msg(SUCCESS_CODE)
        .build();
  }

  public static Result data(Object data, String responseMessage) {
    return ResultBuilder.aResult()
        .data(data)
        .code(SUCCESS_CODE)
        .msg(responseMessage)
        .build();
  }

  public static Result fail(Object data, String msg) {
    return ResultBuilder.aResult()
        .data(data)
        .code(FAIL_CODE)
        .msg(msg)
        .build();
  }

  public static Result fail(String msg) {
    return ResultBuilder.aResult()
        .data(null)
        .code(FAIL_CODE)
        .msg(msg)
        .build();
  }

  public static Result fail(Object data) {
    return ResultBuilder.aResult()
        .data(data)
        .code(FAIL_CODE)
        .msg(FAIL_CODE)
        .build();
  }

  public Result() {
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }
}
