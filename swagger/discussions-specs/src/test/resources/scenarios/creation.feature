Feature: Creation of a discussion

  Background:
    Given there is a discussion microservice up

  Scenario: create a new comment for a article 1
    Given I have a comment payload for article 1
    When I POST it to the /comments endpoint
    Then I receive a 201 status code

  Scenario: getting all comments of article 1
    Given There are some comment for article 1 on the server
    When I send a GET to the /comments endpoint for article 1
    Then I receive a list of the article 1 comments

  Scenario: check that the comment has been written
    Given I have a comment payload for article 2
    When I POST it to the /comments endpoint
    And I send a GET to the /comments endpoint for article 2
    Then The new comment should be in the list

  Scenario: not create a new comment if it is empty
    Given I have a comment payload for article 3
    When The payload is an empty comment
    And I POST it to the /comments endpoint
    Then I receive a 422 status code