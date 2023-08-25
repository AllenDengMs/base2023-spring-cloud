package org.backend.cloud.user.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.backend.cloud.common.annotation.Mask;
import org.backend.cloud.user.model.entity.User;
import org.backend.cloud.web.annotation.ConvertConstantValue;

public class UserVO extends User {

  @ConvertConstantValue("UserType")
  private Integer userType;

  @Mask // 序列化成json时，把值设为null
  private String password;

  @JsonIgnore // 整个字段都不返回给前端
  private String passwordSalt;

  private String roleId;

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

  public String getRoleId() {
    return roleId;
  }
}
