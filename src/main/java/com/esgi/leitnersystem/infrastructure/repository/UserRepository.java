package com.esgi.leitnersystem.infrastructure.repository;

import com.esgi.leitnersystem.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  @Query("SELECT user FROM User user WHERE user.username = :username AND user.password = :password")
  User login(String username, String password);

  @Query("SELECT user FROM User user WHERE user.username = :username")
  User findByUsername(String username);

  @Query("DELETE FROM User user WHERE user.id = :userId")
  void deleteByUserId(String userId);

  @Query("INSERT INTO User (username, password) VALUES (:user.GetUsername(), :user.GetPassword())")
  void register(User user);
}
