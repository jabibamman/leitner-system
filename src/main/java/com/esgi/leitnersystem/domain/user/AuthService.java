package com.esgi.leitnersystem.domain.user;

import com.esgi.leitnersystem.infrastructure.entity.UserEntity;
import com.esgi.leitnersystem.infrastructure.repository.UserRepository;

public class AuthService {
  private final UserRepository userRepository;

  public AuthService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User login(String username, String password) {
    UserEntity user = userRepository.login(username, password);
    return new User(user.getUsername(), user.getPassword());
  }
}
