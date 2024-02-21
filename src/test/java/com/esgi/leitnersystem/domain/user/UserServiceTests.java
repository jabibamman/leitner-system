package com.esgi.leitnersystem.domain.user;

import com.esgi.leitnersystem.infrastructure.entity.UserEntity;
import com.esgi.leitnersystem.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTests {
  @Test
  public void login_ReturnsTheUserWhenFound() {
    UserEntity user = new UserEntity();
    UserRepository userRepository = mock(UserRepository.class);
    UserService userService = new UserService(userRepository);

    when(userRepository.login("username", "password")).thenReturn(user);
    User result = userService.login("username", "password");

    assertEquals(user.getUsername(), result.getUsername());
    assertEquals(user.getPassword(), result.getPassword());
  }

  @Test
  public void register_SaveTheUser() {
    UserEntity user = new UserEntity("username", "password");
    UserRepository userRepository = mock(UserRepository.class);
    UserService userService = new UserService(userRepository);

    userService.register(user.getUsername(), user.getPassword());
    User result = userService.login(user.getUsername(), user.getPassword());

    assertEquals(user.getUsername(), result.getUsername());
    assertEquals(user.getPassword(), result.getPassword());
  }
}
