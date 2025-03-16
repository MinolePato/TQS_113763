Feature: Basic Arithmetic

  Background: A Calculator
    Given a calculator I just turned on

  Scenario: Addition
    When I add 4 and 5
    Then the result is 9

  Scenario: Subtraction
    When I subtract 7 from 2
    Then the result is -5

  Scenario Outline: Several additions
    When I add <a> and <b>
    Then the result is <c>

  Examples: Single digits
    | a | b | c  |
    | 1 | 2 | 3  |
    | 3 | 7 | 10 |

  Scenario: Multiplication
    When I multiply 3 by 4
    Then the result is 12

  Scenario: Division
    When I divide 10 by 2
    Then the result is 5

  Scenario: Division by zero
    When I divide 5 by 0
    Then an error occurs

  Scenario Outline: Several subtractions
    When I subtract <b> from <a>
    Then the result is <c>

  Examples: Various cases
    | a  | b  | c  |
    | 10 | 5  | 5  |
    | 7  | 2  | 5  |
    | 5  | 8  | -3 |

  Scenario Outline: Several multiplications
    When I multiply <a> by <b>
    Then the result is <c>

  Examples: Multiplication cases
    | a  | b  | c  |
    | 2  | 3  | 6  |
    | 4  | 5  | 20 |
    | 6  | 7  | 42 |

  Scenario Outline: Several divisions
    When I divide <a> by <b>
    Then the result is <c>

  Examples: Division cases
    | a  | b  | c  |
    | 8  | 2  | 4  |
    | 9  | 3  | 3  |
    | 15 | 5  | 3  |
