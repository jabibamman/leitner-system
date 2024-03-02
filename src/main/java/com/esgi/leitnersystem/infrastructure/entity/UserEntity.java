package com.esgi.leitnersystem.infrastructure.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(updatable = false, nullable = false)
  @Schema(description = "Generated identifier of an user",
          example = "6c10ad48-2bb8-4e2e-900a-21d62c00c07b", required = true)
  private UUID id;

  @Column(nullable = false, unique = true)
  @Schema(description = "The username of the user", example = "jimso91",
          required = true)
  private String username;

  @Column(nullable = false)
  @Schema(description = "The password of the user", example = "password1234",
          required = true)
  private String password;

  public UserEntity(String username, String password) {
    this.id = UUID.randomUUID();
    this.username = username;
    this.password = password;
  }

  public boolean verifyPassword(String password) {
    return this.password.equals(password);
  }
}
