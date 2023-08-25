package org.backend.cloud.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.backend.cloud.user.entity.dto.AddUserRequest;
import org.backend.cloud.user.entity.dto.UpdateUserRequest;
import org.backend.cloud.user.entity.vo.UserVO;
import org.backend.cloud.user.model.builder.UserQueryBuilder;
import org.backend.cloud.user.model.entity.User;

public interface UserService {

  int addUser(AddUserRequest user);

  User getUserById(long userId);

  User getUserByUsername(String username);

  Page<UserVO> pageQueryUsers(UserQueryBuilder queryBuilder);

  int updateUser(UpdateUserRequest request);

  User getUserByPhone(String phone);
}
