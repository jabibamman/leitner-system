package com.esgi.leitnersystem.cucumber;

import static org.junit.jupiter.api.Assertions.*;

import com.esgi.leitnersystem.domain.user.UserService;
import com.esgi.leitnersystem.infrastructure.entity.UserEntity;
import com.esgi.leitnersystem.infrastructure.exception.UnauthorizedException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserLoginSteps {
  @Autowired private UserService userService;

  private Exception exception;
  private UserEntity loggedInUser;
  private UserEntity user;
  @Given("an unconnected user on the login page with {string} and {string}")
  public void an_unconnected_user_on_the_login_page(String username,
                                                    String password) {
    exception = null;
    loggedInUser = null;
    var userRegistered = userService.register(username, password);
    assertNotNull(userRegistered, "L'utilisateur doit être créé avec succès");
    this.user = new UserEntity(username, password);
  }

  @Given(
      "an unconnected user on the login page with incorrect {string} and {string}")
  public void
  an_unconnected_user_on_the_login_page_with_incorrect_credentials(
      String username, String password) {
    exception = null;
    loggedInUser = null;
    user = new UserEntity(username, password);
  }

  @When("the user submits login form with valid")
  public void the_user_submits_login_form_with_valid_credentials() {
    try {
      loggedInUser = userService.login(user.getUsername(), user.getPassword());
      exception = null;
    } catch (UnauthorizedException e) {
      exception = e;
    }
  }

  @Then("the user is redirected to their homepage")
  public void the_user_is_redirected_to_their_homepage() {
    assertNotNull(loggedInUser, "L'utilisateur doit être connecté avec succès");
    assertNull(
        exception,
        "Aucune exception ne doit être levée lors d'une connexion valide");
  }

  @Then("the user is shown an error message")
  public void the_user_is_shown_an_error_message() {
    assertNull(
        loggedInUser,
        "Aucun utilisateur ne doit être connecté si les identifiants sont invalides");
    assertNotNull(
        exception,
        "Une exception doit être levée pour une tentative de connexion invalide");
    assertInstanceOf(UnauthorizedException.class, exception,
                     "L'exception doit être de type UnauthorizedException");
  }

  @When("the user submits login form with invalid credentials")
  public void the_user_submits_login_form_with_invalid_credentials() {
    try {
      loggedInUser = userService.login(user.getUsername(), user.getPassword());
      exception = null;
    } catch (UnauthorizedException e) {
      exception = e;
    }
  }

  @Then("the user is redirected to the login page with an error message")
  public void the_user_is_redirected_to_the_login_page_with_an_error_message() {
    assertNull(
        loggedInUser,
        "Aucun utilisateur ne doit être connecté si les identifiants sont invalides");
    assertNotNull(
        exception,
        "Une exception doit être levée pour une tentative de connexion invalide");
    assertInstanceOf(UnauthorizedException.class, exception,
                     "L'exception doit être de type UnauthorizedException");
  }
}
