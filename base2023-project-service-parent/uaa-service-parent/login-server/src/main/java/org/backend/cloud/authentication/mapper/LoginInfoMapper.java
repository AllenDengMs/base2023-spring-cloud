package org.backend.cloud.authentication.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.backend.cloud.authentication.model.entity.UserLoginInfo;

@Mapper
public interface LoginInfoMapper {

  int insertUserLoginInfo(UserLoginInfo userLoginInfo);

  int updateUserLoginInfo(UserLoginInfo userLoginInfo);
}
