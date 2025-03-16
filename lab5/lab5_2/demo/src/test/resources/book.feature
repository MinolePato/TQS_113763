Feature: Book Search
  Customers should be able to quickly find books using different search criteria.

  Scenario: Search books by publication year
    Given the following books are available in the store:
      | Title                  | Author          | Published  |
      | One Good Book          | Anonymous       | 2013-03-14 |
      | Some Other Book        | Tim Tomson      | 2014-08-23 |
      | How to Cook a Dino     | Fred Flintstone | 2012-01-01 |
    When the customer searches for books published between 2011-12-31 and 2014-01-01
    Then 2 books should be found

  Scenario: Search books by title
    Given the following books are available in the store:
      | Title                  | Author          | Published  |
      | One Good Book          | Anonymous       | 2013-03-14 |
      | Some Other Book        | Tim Tomson      | 2014-08-23 |
      | How to Cook a Dino     | Fred Flintstone | 2012-01-01 |
    When the customer searches for a book titled "One Good Book"
    Then 1 book should be found

  Scenario: Search books by author
    Given the following books are available in the store:
      | Title                  | Author          | Published  |
      | One Good Book          | Anonymous       | 2013-03-14 |
      | Some Other Book        | Tim Tomson      | 2014-08-23 |
      | How to Cook a Dino     | Fred Flintstone | 2012-01-01 |
    When the customer searches for books written by "Tim Tomson"
    Then 1 book should be found

  Scenario: No books found
    Given the following books are available in the store:
      | Title                  | Author          | Published  |
      | One Good Book          | Anonymous       | 2013-03-14 |
      | Some Other Book        | Tim Tomson      | 2014-08-23 |
      | How to Cook a Dino     | Fred Flintstone | 2012-01-01 |
    When the customer searches for books written by "Charles Darwin"
    Then no books should be found
