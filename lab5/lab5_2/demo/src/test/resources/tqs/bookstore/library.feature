Feature: Basic Library Features

  Scenario: Search for books published between two dates
    Given a book with the title "The Lord of the Rings", written by "J.R.R. Tolkien", published in 1954
    And a book with the title "The Hobbit", written by "J.R.R. Tolkien", published in 1937
    When search for books published between 1930 and 1940
    Then the search result should contain the book "The Hobbit" by "J.R.R. Tolkien"
    And the search result should not contain the book "The Lord of the Rings" by "J.R.R. Tolkien"
