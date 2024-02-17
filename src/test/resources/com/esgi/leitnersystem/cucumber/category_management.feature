Feature: Card Category Management

  Scenario: Promoting a card after a successful answer
    Given a card in category "SEVENTH"
    When the card is answered correctly
    Then the card should be promoted to category "DONE"

  Scenario: Demoting a card after an incorrect answer
    Given a card in category "SEVENTH"
    When the card is answered incorrectly
    Then the card should be demoted to category "FIRST"

  Scenario: Promoting a card to the final category
    Given a card in category "SIXTH"
    When the card is answered correctly
    Then the card should be promoted to category "SEVENTH"

  Scenario: Marking a card as done when it is in the final category
    Given a card in category "SEVENTH"
    When the card is answered correctly
    Then the card should be marked as done
