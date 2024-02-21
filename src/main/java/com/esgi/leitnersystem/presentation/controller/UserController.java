package com.esgi.leitnersystem.presentation.controller;

import com.esgi.leitnersystem.domain.user.UserService;
import com.esgi.leitnersystem.domain.user.User;
import com.esgi.leitnersystem.infrastructure.dto.UserLoginDto;
import com.esgi.leitnersystem.infrastructure.dto.UserRegisterDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/login")
  @Tag(name = "User")
  @Operation(
      summary = "Log in the user",
      description = "Used to log in a registered user. The login endpoint will work only if a user with the same username and password is already registered")
  @ApiResponse(
      responseCode = "200",
      description = "User successfully logged in",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
  @ApiResponse(
      responseCode = "401",
      description = "Unauthorized",
      content = @Content())
  public ResponseEntity<User> login(@RequestBody UserLoginDto loginBody) {
    try {
      User logined = userService.login(loginBody.getUsername(), loginBody.getPassword());
      return new ResponseEntity<>(logined, HttpStatus.OK);
    }
    catch (Exception e) {
      Logger.getLogger(UserController.class.getName())
          .log(Level.FINER, "Login failed: ", e);
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping("/register")
  @Tag(name = "User")
  @Operation(
      summary = "Create a new user",
      description = "Used to create a new user in the system. This new user will be able to login with the login endpoint.")
  @ApiResponse(
      responseCode = "201",
      description = "User successfully created",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
  @ApiResponse(
      responseCode = "400",
      description = "Bad request",
      content = @Content())
  public ResponseEntity<?> register(@RequestBody @Valid UserRegisterDto registerBody) {
    try {
      User createdUser = userService.register(registerBody.getUsername(), registerBody.getPassword());
      return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    catch(Exception e) {
      Logger.getLogger(UserController.class.getName())
          .log(Level.SEVERE ,"Error creating the user", e);
      return ResponseEntity.badRequest().body("Error creating the user: " +
          e.getMessage());
    }
  }
}
