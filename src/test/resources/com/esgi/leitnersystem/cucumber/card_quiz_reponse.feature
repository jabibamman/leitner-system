Feature: Answering Card Questions

  Scenario: Connected user answers a card question correctly
    Given A connected user triggers a quiz
    When The user answers the card's question
    Then The answer is recorded, and the user can see if they answered correctly or not
    And The card is moved to category "SECOND"

  Scenario: Connected user answers a card question with a wrong answer
    Given A connected user triggers a quiz
    When The user answers the card's question with an incorrect answer
    Then The answer is recorded, and the user can see if they answered correctly or not
    And The card is moved to category "FIRST"

  Scenario: Connected user answers a card question with the category SEVENTH
    Given A connected user triggers a quiz
    When The user answers the card's question with the category "SEVENTH"
    Then The answer is recorded, and the user can see if they answered correctly or not
    And The card should be promoted to "DONE" and not be asked again
