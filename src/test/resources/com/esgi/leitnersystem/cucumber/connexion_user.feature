Feature: User login

  Scenario: Unconnected user successfully logs in
    Given an unconnected user on the login page with "username" and "password"
    When the user submits login form with valid
    Then the user is redirected to their homepage

  Scenario: Unconnected user fails to log in
    Given an unconnected user on the login page with incorrect "azerty" and "uop"
    When the user submits login form with invalid credentials
    Then the user is redirected to the login page with an error message
