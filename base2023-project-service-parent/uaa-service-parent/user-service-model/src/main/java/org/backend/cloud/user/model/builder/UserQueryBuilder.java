package org.backend.cloud.user.model.builder;

import org.backend.cloud.common.builder.PageQueryBuilder;

public class UserQueryBuilder extends PageQueryBuilder {

  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
