package com.esgi.leitnersystem.domain.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.esgi.leitnersystem.infrastructure.entity.UserEntity;
import com.esgi.leitnersystem.infrastructure.exception.UnauthorizedException;
import com.esgi.leitnersystem.infrastructure.exception.UserAlreadyExistsException;
import com.esgi.leitnersystem.infrastructure.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class UserServiceTests {
  @Test
  public void login_returns_the_user_when_found() {
    UserEntity user = new UserEntity();
    UserRepositoryPort userRepository = mock(UserRepositoryPort.class);
    UserService userService = new UserService(userRepository);

    when(userRepository.login("username", "password"))
        .thenReturn(Optional.of(user));
    Optional<UserEntity> result =
        Optional.ofNullable(userService.login("username", "password"));

    assertTrue(result.isPresent());
    assertEquals(user.getUsername(), result.get().getUsername());
    assertEquals(user.getPassword(), result.get().getPassword());
  }

  @Test
  public void test_login_with_invalid_credentials() {
    UserRepositoryPort userRepository = mock(UserRepositoryPort.class);
    UserService userService = new UserService(userRepository);

    when(userRepository.login("invalid_username", "invalid_password"))
        .thenReturn(Optional.empty());
    assertThrows(UnauthorizedException.class, () -> {
      userService.login("invalid_username", "invalid_password");
    });
  }

  @Test
  public void register_save_the_user_with_the_good_credentials() {
    UserEntity user = new UserEntity("username", "password");
    UserRepositoryPort userRepository = mock(UserRepositoryPort.class);
    UserService userService = new UserService(userRepository);

    when(userRepository.save(any(UserEntity.class))).thenReturn(user);
    userService.register(user.getUsername(), user.getPassword());

    when(userRepository.login(user.getUsername(), user.getPassword()))
        .thenReturn(Optional.of(user));
    Optional<UserEntity> result = Optional.ofNullable(
        userService.login(user.getUsername(), user.getPassword()));

    assertTrue(result.isPresent());
    assertEquals(user.getUsername(), result.get().getUsername());
    assertEquals(user.getPassword(), result.get().getPassword());
  }

  @Test
  public void
  register_throws_UserAlreadyExistsException_when_user_already_exists() {
    UserEntity user = new UserEntity("username", "password");
    UserRepositoryPort userRepository = mock(UserRepositoryPort.class);
    UserService userService = new UserService(userRepository);

    when(userRepository.findByUsername(user.getUsername()))
        .thenReturn(Optional.of(user));
    assertThrows(UserAlreadyExistsException.class, () -> {
      userService.register(user.getUsername(), user.getPassword());
    });
  }
}
