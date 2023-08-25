package org.backend.cloud.user.model.dto;

import java.util.Objects;

public class ApiPathDto {

  private String path;
  /** Get/Post/Delete/Put */
  private String method;

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiPathDto that = (ApiPathDto) o;
    return Objects.equals(path, that.path) && Objects.equals(method,
        that.method);
  }

  @Override
  public int hashCode() {
    return Objects.hash(path, method);
  }
}
