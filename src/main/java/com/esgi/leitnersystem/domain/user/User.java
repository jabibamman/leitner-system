package com.esgi.leitnersystem.domain.user;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
  private String username;
  private String password;
}
