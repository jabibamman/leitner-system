Feature: Cards matching their category frequency

  Scenario Outline: Connected user sees cards according to Leitner system frequency
    Given a connected user is ready to take a quiz on the current date
    And the system has cards in various categories with last revision dates
    When the user triggers the quiz <DaysLater> days later
    Then only cards eligible for review based on their category frequency are shown
    Examples:
      | DaysLater |
      | 1         |
      | 2         |
      | 4         |
      | 8         |
      | 16        |
      | 32        |
      | 64        |
