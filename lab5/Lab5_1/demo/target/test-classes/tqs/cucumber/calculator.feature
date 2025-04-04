Feature: Basic Arithmetic

  Background: A Calculator
    Given a calculator I just turned on

  Scenario: Addition
    When I add 4 and 5
    Then the result is 9

  Scenario: Addition
    When I add floats 4.7 and 5.3
    Then the result is 10

  Scenario: Substraction
    When I substract 7 to 2
    Then the result is 5

  Scenario: Multiply
    When I multiply 5 to 2
    Then the result is 10

  Scenario: Divide
    When I divide 10 to 2
    Then the result is 5

  Scenario: Square
    When I square 5
    Then the result is 25

  Scenario Outline: Several additions
    When I add <a> and <b>
    Then the result is <c>

    Examples: Single digits
      | a  | b  | c  |
      |  1 |  2 |  3 |
      |  3 |  7 | 10 |
      | 13 | 12 | 25 |
