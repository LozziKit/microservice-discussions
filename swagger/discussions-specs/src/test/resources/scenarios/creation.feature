Feature: Commenting an article

  Background:
    Given there is an article server

  Scenario: create a new comment for an article
    Given I have a comment payload
    When I POST it to the /comments endpoint
    Then I receive a 201 status code

  Scenario: getting all comments of an article
    Given There are some articles on the server
    When I send a GET to the /comments endpoint
    Then I receive a list of these articles

  Scenario: check that the comment has been written
    Given I have a comment payload
    When I POST it to the /comments endpoint
    And I ask for a list of all the comments by sending a GET to the /comments endpoint
    Then The new comment should be in the list