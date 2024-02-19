package com.esgi.leitnersystem.domain.user;

import com.esgi.leitnersystem.infrastructure.entity.UserEntity;
import com.esgi.leitnersystem.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthServiceTests {
  @Test
  public void login_ReturnsTheUserWhenFound() {
    UserEntity user = new UserEntity(UUID.randomUUID(), "username", "password");
    UserRepository userRepository = mock(UserRepository.class);
    when(userRepository.login("username", "password")).thenReturn(user);
    AuthService authService = new AuthService(userRepository);

    User result = authService.login("username", "password");

    assertEquals(user.getUsername(), result.getUsername());
    assertEquals(user.getPassword(), result.getPassword());
  }
}
