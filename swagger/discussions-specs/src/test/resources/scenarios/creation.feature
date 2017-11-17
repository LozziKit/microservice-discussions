Feature: Creation of a discussion

  Background:
    Given there is a discussion microservice up

  Scenario: create a new comment for an article
    Given I have a comment payload
    When I POST it to the /comments endpoint
    Then I receive a 201 status code

  Scenario: getting all comments of an article
    Given There are some comment for that article on the server
    When I send a GET to the /comments endpoint for an article
    Then I receive a list of the article comments

  Scenario: check that the comment has been written
    Given I have a comment payload
    When I POST it to the /comments endpoint
    And I ask for a list of all the comments for an article by sending a GET to the /comments endpoint
    Then The new comment should be in the list

  Scenario: not create a new comment if it's empty
    Given I have a comment payload
    When I POST it to the /comments endpoint
    And The payload is an empty comment
    Then I receive a 422 status code