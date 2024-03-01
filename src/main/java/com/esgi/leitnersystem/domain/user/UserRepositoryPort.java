package com.esgi.leitnersystem.domain.user;

import com.esgi.leitnersystem.infrastructure.entity.UserEntity;
import java.util.Optional;

public interface UserRepositoryPort {
  Optional<UserEntity> findByUsername(String username);
  Optional<UserEntity> login(String username, String password);
  UserEntity save(UserEntity user);
}
