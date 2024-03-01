package com.esgi.leitnersystem.domain.user;

import com.esgi.leitnersystem.infrastructure.entity.UserEntity;
import com.esgi.leitnersystem.infrastructure.exception.UnauthorizedException;
import com.esgi.leitnersystem.infrastructure.exception.UserAlreadyExistsException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UserService {
  private final UserRepositoryPort userRepository;

  @Autowired
  public UserService(UserRepositoryPort userRepository) {
    this.userRepository = userRepository;
  }

  public UserEntity login(String username, String password) {
    var userEntity = userRepository.login(username, password);
    if (userEntity == null) {
      throw new UnauthorizedException("Invalid username or password");
    }
    return userEntity;
  }

  @SneakyThrows
  public User register(String username, String password) {
    var userEntity = new UserEntity(username, password);
    if (userRepository.findByUsername(username).isPresent()) {
      throw new UserAlreadyExistsException("Username already exists");
    }
    var user = userRepository.save(userEntity);
    return new User(user.getUsername(), user.getPassword());
  }
}
