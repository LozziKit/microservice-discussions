Feature: Creation of a discussion

  Background:
    Given there is a discussion microservice up

  Scenario: create a new comment for article 1
    Given I have a comment payload
    When I POST it to the /comments endpoint for the article 1
    Then I receive a 201 status code

  Scenario: getting all comments of article 1
    Given There are some comments for article 1 on the server
    When I send a GET to the /comments endpoint for article 1
    Then I receive a list of only the article 1 comments

  Scenario: check that the comment has been written
    Given I have a comment payload
    When I POST it to the /comments endpoint for the article 2
    And I send a GET to the /comments endpoint for article 2
    Then The new comment should be in the list

  Scenario: not create a new comment if it is empty
    Given I have a comment payload
    When The payload is an empty comment
    And I POST it to the /comments endpoint for the article 3
    Then I receive a 422 status code

  Scenario: check that the comment has been written to the right article
    Given I have a comment payload
    And I POST it to the /comments endpoint for article 1
    And I have a comment payload
    And I POST it to the /comments endpoint for article 2
    When I send a GET to the /comments endpoint for article 2
    Then I receive a list of only the article 2 comments

  Scenario: receiving a flat list of comments for article 1
    Given There are some comments for article 1 on the server
    When I send a GET to the /comments endpoint for article 1 with parameter tree equal to 0
    Then I receive a list of only the article 1 comments
    And Every comment has no child

  Scenario: receiving a tree list of comments for article 6
    Given There are some comments for article 6 on the server
    And There is at least 1 answer to a comment for article 6
    When I send a GET to the /comments endpoint for article 6 with parameter tree equal to 1
    Then I receive a list of only the article 6 comments
    And I receive a list where some comments have children

  Scenario: delete a comment for article 1
    Given There are some comments for article 1 on the server
    And There is at least 1 answer to a comment for article 1
    When I delete one of them wich is a not leaf in article 1
    And I send a GET to the /comments endpoint for article 1
    Then the list should contain the deleted comment with no message

  Scenario: delete a leaf comment for article 2
    Given There are some comments for article 2 on the server
    When I delete one of them wich is a leaf in article 2
    And I send a GET to the /comments endpoint for article 2
    Then the list should not contain the deleted comment

