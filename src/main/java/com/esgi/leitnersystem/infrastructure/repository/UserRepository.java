package com.esgi.leitnersystem.infrastructure.repository;

import com.esgi.leitnersystem.domain.user.User;
import com.esgi.leitnersystem.infrastructure.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
  @Query(
      "SELECT user FROM UserEntity user WHERE user.username = :username AND user.password = :password")
  UserEntity
  login(String username, String password);

  @Query("DELETE FROM UserEntity user WHERE user.id = :userId")
  void deleteByUserId(String userId);

  Optional<UserEntity> findByUsername(String username);
}
