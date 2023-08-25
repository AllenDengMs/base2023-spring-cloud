package org.backend.cloud.api.client.user;

import org.backend.cloud.user.model.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = UserServiceClientApiPath.APPLICATION_NAME,
    path = UserServiceClientApiPath.USER)
public interface UserServiceClient {

  @GetMapping("/{userId}")
  User getUserByUserId(@PathVariable("userId") String userId);

  @GetMapping("/name/{username}")
  User getUserByUsername(@PathVariable("username") String username);

  @GetMapping("/phone/{phone}")
  User getUserByPhoneNumber(@PathVariable("phone") String phone);

  @PostMapping("/{userId}/role")
  String getRoleIdByUserId(@PathVariable("userId") long userId);
}
