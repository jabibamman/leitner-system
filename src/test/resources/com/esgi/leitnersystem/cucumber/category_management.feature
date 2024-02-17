Feature: Card Category Management

  Scenario: Promoting a card after a successful answer
    Given a card in category "FIRST"
    When the card is answered correctly
    Then the card should be promoted to category "SECOND"
