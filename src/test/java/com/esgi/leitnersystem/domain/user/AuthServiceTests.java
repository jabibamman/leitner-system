package com.esgi.leitnersystem.domain.user;

import com.esgi.leitnersystem.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthServiceTests {
  @Test
  public void Login_ReturnsTheUserWhenFound() {
    User user = new User("username", "password");
    UserRepository userRepository = mock(UserRepository.class);
    when(userRepository.login("username", "password")).thenReturn(user);
    AuthService authService = new AuthService(userRepository);

    User result = authService.login("username", "password");

    assertEquals(user, result);
  }
}
