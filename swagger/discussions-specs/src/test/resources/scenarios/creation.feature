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

  Scenario: Modifying a comment that does not exist
    Given I have the id of a comment that does not exist
    And I have a comment payload
    When I try to modify the comment
    Then I receive a 404 status code

  Scenario: Modifying a comment with an empty comment
    Given There are some comments for article 10 on the server
    And I have a empty comment payload
    When I try to modify the comment
    Then I receive a 400 status code

  Scenario: Modifying a deleted comment which is not a leaf
    Given The author 1 posted a comment for article 11 on the server
    And There is at least 1 answer to a comment for article 11
    And I have a comment payload
    When I try to modify the comment
    Then I receive a 403 status code

  Scenario: Modifying a comment with a payload that does not contain an author
    Given The author 1 posted a comment for article 12 on the server
    And I have a comment payload without author
    When I try to modify the comment
    Then I receive a 400 status code

  Scenario: Modifying a comment which i'm not the author
    Given The author 1 posted a comment for article 13 on the server
    And I have a comment payload from the author 2
    When I try to modify the comment
    Then I receive a 403 status code

  Scenario: Modifying a comment
    Given The author 1 posted a comment for article 14 on the server
    And I have a comment payload from the author 1
    When I try to modify the comment
    Then I receive a 201 status code
    
  Scenario: Reacting to a comment
    Given The author 1 posted a comment for article 1 on the server
    And The comment is not deleted
    And The author 1 has not reacted to the comment
    When The author 1 reacts to the comment
    Then I receive a 201 status code

  Scenario: Reacting to a comment I already reacted to should return a forbidden error
    Given The author 1 posted a comment for article 1 on the server
    And The comment is not deleted
    And The author 1 has reacted to the comment
    When The author 1 reacts to the comment
    Then I receive a 403 status code
    
  Scenario: Reacting to a deleted comment should return a forbidden error
    Given The author 1 posted a comment for article 1 on the server
    And The comment is deleted
    When The author 1 reacts to the comment
    Then I receive a 403 status code

  Scenario: Getting the number of reactions of a comment
    Given The author 1 posted a comment for article 1 on the server
    And The comment is not deleted
    When I send a GET to the /comments/id/reaction endpoint
    Then I receive a 201 status code

  Scenario: Getting the number of reactions of a comment which is deleted should return a forbidden error
    Given The author 1 posted a comment for article 1 on the server
    And The comment is deleted
    When I send a GET to the /comments/id/reaction endpoint
    Then I receive a 403 status code

  Scenario: Deleting a reaction to a comment
    Given The author 1 posted a comment for article 1 on the server
    And The comment is not deleted
    And The author 1 has reacted to the comment
    When The author 1 sends a DELETE to the /comment/id/reaction endpoint
    Then I receive a 201 status code

  Scenario: Deleting a reaction to a comment I never reacted to should return a bad request error
    Given The author 1 posted a comment for article 1 on the server
    And The comment is not deleted
    And The author 1 has not reacted to the comment
    When The author 1 sends a DELETE to the /comment/id/reaction endpoint
    Then I receive a 400 status code

  Scenario: Deleting a reaction to a deleted comment should return a forbidden error
    Given The author 1 posted a comment for article 1 on the server
    And The comment is deleted
    When The author 1 sends a DELETE to the /comment/id/reaction endpoint
    Then I receive a 403 status code
