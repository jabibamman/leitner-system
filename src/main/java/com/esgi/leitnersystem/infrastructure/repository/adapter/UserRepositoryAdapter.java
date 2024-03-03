package com.esgi.leitnersystem.infrastructure.repository.adapter;

import com.esgi.leitnersystem.domain.user.UserRepositoryPort;
import com.esgi.leitnersystem.infrastructure.entity.UserEntity;
import com.esgi.leitnersystem.infrastructure.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRepositoryAdapter implements UserRepositoryPort {
  private final UserRepository userRepository;

  @Autowired
  public UserRepositoryAdapter(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public Optional<UserEntity> login(String username, String password) {
    return Optional.ofNullable(userRepository.login(username, password));
  }
  @Override
  public UserEntity save(UserEntity user) {
    return userRepository.save(user);
  }
}
