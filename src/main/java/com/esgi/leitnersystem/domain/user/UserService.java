package com.esgi.leitnersystem.domain.user;

import com.esgi.leitnersystem.infrastructure.entity.UserEntity;
import com.esgi.leitnersystem.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User login(String username, String password) {
    UserEntity user = userRepository.login(username, password);
    return new User(user.getUsername(), user.getPassword());
  }

  public User register(String username, String password) {
    UserEntity user = new UserEntity(username, password);
    user = userRepository.save(user);
    return new User(user.getUsername(), user.getPassword());
  }
}
