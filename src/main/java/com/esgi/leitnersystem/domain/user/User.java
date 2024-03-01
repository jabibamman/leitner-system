package com.esgi.leitnersystem.domain.user;

import java.util.UUID;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
  private String username;
  private String password;
}
