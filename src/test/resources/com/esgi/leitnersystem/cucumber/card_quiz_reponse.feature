Feature: Answering Card Questions

  Scenario: Connected user answers a card question
    Given A connected user triggers a quiz
    When The user answers the card's question
    Then The answer is recorded, and the user can see if they answered correctly or not
