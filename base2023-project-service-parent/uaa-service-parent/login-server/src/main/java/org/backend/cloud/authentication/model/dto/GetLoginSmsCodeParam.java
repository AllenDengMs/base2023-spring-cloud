package org.backend.cloud.authentication.model.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class GetLoginSmsCodeParam {

  @NotBlank
  @Length(max = 30)
  private String phone;

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }
}
