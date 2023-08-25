package org.backend.cloud.user.entity.convert;

import java.util.List;
import org.backend.cloud.user.entity.dto.AddUserRequest;
import org.backend.cloud.user.entity.dto.UpdateUserRequest;
import org.backend.cloud.user.entity.vo.UserVO;
import org.backend.cloud.user.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserConvert {

  UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

  UserVO toVo(User user);

  List<UserVO> toVoList(List<User> list);

  User toUser(AddUserRequest request);

  User toUser(UpdateUserRequest request);
}