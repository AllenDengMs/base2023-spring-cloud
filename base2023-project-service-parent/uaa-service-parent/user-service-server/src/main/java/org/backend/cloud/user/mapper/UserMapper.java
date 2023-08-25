package org.backend.cloud.user.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.backend.cloud.user.entity.vo.UserVO;
import org.backend.cloud.user.model.builder.UserQueryBuilder;
import org.backend.cloud.user.model.entity.User;

@Mapper
public interface UserMapper {

  int addUser(User user);

  User getUserById(@Param("userId") long userId);

  User getUserByUsername(@Param("username") String username);

  boolean existsUsername(@Param("username") String username);

  Page<UserVO> pageQueryUsers(Page rowPage, @Param("queryBuilder") UserQueryBuilder queryBuilder);

  int updateUser(User user);

  boolean existsPhone(@Param("phone") String phone);

  User getUserByPhone(@Param("phone") String phone);
}
