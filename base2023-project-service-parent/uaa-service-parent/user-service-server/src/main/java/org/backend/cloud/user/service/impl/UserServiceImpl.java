package org.backend.cloud.user.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Date;
import java.util.UUID;
import org.backend.cloud.common.utils.TimeTool;
import org.backend.cloud.user.entity.convert.UserConvert;
import org.backend.cloud.user.entity.dto.AddUserRequest;
import org.backend.cloud.user.entity.dto.UpdateUserRequest;
import org.backend.cloud.user.entity.vo.UserVO;
import org.backend.cloud.user.mapper.RoleMapper;
import org.backend.cloud.user.mapper.UserMapper;
import org.backend.cloud.user.model.builder.UserQueryBuilder;
import org.backend.cloud.user.model.entity.User;
import org.backend.cloud.user.model.entity.UserRole;
import org.backend.cloud.user.service.UserService;
import org.backend.cloud.web.exception.Exceptions;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

  private final UserMapper userMapper;

  private final PasswordEncoder passwordEncoder;

  private final RoleMapper roleMapper;

  public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder,
      RoleMapper roleMapper) {
    this.userMapper = userMapper;
    this.passwordEncoder = passwordEncoder;
    this.roleMapper = roleMapper;
  }

  @Override
  @Transactional(rollbackFor = Throwable.class)
  public int addUser(AddUserRequest request) {
    if (userMapper.existsUsername(request.getUsername())) {
      Exceptions.error("${exists.username:登陆用户名已存在}");
    }
    if (request.getPhone() != null && userMapper.existsPhone(request.getPhone())) {
      Exceptions.error("${exists.user.phone:手机号已存在}");
    }
    User user = UserConvert.INSTANCE.toUser(request);
    String passwordSalt = UUID.randomUUID().toString().replace("-", "");
    Date now = TimeTool.now();
    user.setCreateTime(now);
    user.setUpdateTime(now);
    user.setPasswordSalt(passwordSalt);
    user.setPassword(encodePassword(user.getPassword(), passwordSalt));

    int row = userMapper.addUser(user);
    if (row > 0) {
      saveUserRole(request.getRoleId(), user.getUserId());
    }
    return row;
  }

  private void saveUserRole(String roleId, Long userId) {
    if (!roleMapper.existsRole(roleId)) {
      Exceptions.error("${invalid.roleId:无效的roleId}");
    }
    roleMapper.deleteUserRoles(userId);
    roleMapper.bindUser(UserRole.of(userId, roleId));
  }

  private String encodePassword(String password, String passwordSalt) {
    return passwordEncoder.encode(password.concat(passwordSalt));
  }

  @Override
  public User getUserById(long userId) {
    return userMapper.getUserById(userId);
  }

  @Override
  public User getUserByUsername(String username) {
    return userMapper.getUserByUsername(username);
  }

  @Override
  public Page<UserVO> pageQueryUsers(UserQueryBuilder queryBuilder) {
    Page<Object> page = Page.of(queryBuilder.getCurrentPage(), queryBuilder.getSize());
    return userMapper.pageQueryUsers(page, queryBuilder);
  }

  @Override
  @Transactional(rollbackFor = Throwable.class)
  public int updateUser(UpdateUserRequest request) {
    User userFromDB = userMapper.getUserById(request.getUserId());
    if (userFromDB == null) {
      Exceptions.error("${user.not.found:用户不存在！}");
    }

    User user = UserConvert.INSTANCE.toUser(request);
    if (request.getPassword() != null) {
      user.setPassword(encodePassword(request.getPassword(), userFromDB.getPasswordSalt()));
    }
    user.setUpdateTime(TimeTool.now());
    user.setOperatorUserId(request.getOperatorUserId());
    int row = userMapper.updateUser(user);
    if (row > 0) {
      if (request.getRoleId() != null) {
        saveUserRole(request.getRoleId(), request.getUserId());
      }
    }
    return row;
  }

  @Override
  public User getUserByPhone(String phone) {
    return userMapper.getUserByPhone(phone);
  }

}
