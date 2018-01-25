package io.lozzikit.discussions.api.spec.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.lozzikit.discussions.ApiException;
import io.lozzikit.discussions.ApiResponse;
import io.lozzikit.discussions.api.CommentsApi;
import io.lozzikit.discussions.api.dto.CommentRequest;
import io.lozzikit.discussions.api.dto.CommentResponse;
import io.lozzikit.discussions.api.dto.Reaction;
import io.lozzikit.discussions.api.spec.helpers.Environment;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Olivier Liechti on 27/07/17.
 */
public class CreationSteps {

    private Environment environment;
    private CommentsApi api;

    private CommentRequest commentRequest;
    private CommentResponse commentResponse;
    private List<CommentResponse> commentsResponse;

    private ApiResponse lastApiResponse;
    private ApiException lastApiException;
    private boolean lastApiCallThrewException;
    private int lastStatusCode;
    private long commentID;
    private final String TOKEN1 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiIiLCJpYXQiOjE1MTYzOTI4NTcsImV4cCI6MTU0NzkyODg1OCwiYXVkIjoiIiwic3ViIjoiTmFkaWRpZGkiLCJ1c2VySUQiOiIyIn0.43ZOfTOdyXlLd44nbwZCdGtOUQ62TyAkBEjb1fvDIVI";
    private final String TOKEN2 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiIiLCJpYXQiOjE1MTYzOTI4NTcsImV4cCI6MTU0NzkyODg1OCwiYXVkIjoiIiwic3ViIjoiYm91aWxsYWJhaXNzZSIsInVzZXJJRCI6IjcifQ.GMb-VpMvinBR_fcMW7ATH38dXFk6yh3x7A7w7rAiXqY";

    private String userToken = "";

    private int userId1 = 2;
    private int userId2 = 7;

    private long reactionNumber;

    private String userName1 = "Nadididi";
    private String userName2 = "bouillabaisse";


    public CreationSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();

    }

    @Given("^there is a discussion microservice up$")
    public void there_is_an_article_server() throws Throwable {
        assertNotNull(api);
    }

    @Given("^I POST it to the /comments endpoint for the article (\\d+)$")
    public void i_POST_it_to_the_comments_endpoint_for_the_article(long articleID) throws Throwable {
        postMessage(userToken, articleID, null);
    }

    @When("^I POST it to the /comments endpoint for article (\\d+)$")
    public void i_POST_it_to_the_comments_endpoint_for_article(long articleID) throws Throwable {
        postMessage(userToken, articleID, null);
    }

    @Then("^I receive a (\\d+) status code$")
    public void i_receive_a_status_code(int arg1) throws Throwable {
        assertEquals(arg1, lastStatusCode);
    }

    @When("^The payload is an empty comment$")
    public void the_payload_is_an_empty_comment() throws Throwable {
        if (commentRequest != null)
            commentRequest.setMessage("");
        else
            commentRequest = createCommentRequest("");
    }

    @Then("^The new comment should be in the list$")
    public void the_new_comment_should_be_in_the_list() throws Throwable {
        boolean commentFound = false;
        for (CommentResponse commentResponse : commentsResponse) {
            if (compareCommentRequestAndCommentResponse(commentRequest, commentResponse)) {
                commentFound = true;
                break;
            }
        }
        assertTrue(commentFound);
    }

    @Given("^I have a comment payload$")
    public void i_have_a_comment_payload() throws Throwable {
        commentRequest = createCommentRequest("Amen");
        userToken = TOKEN1;
    }

    @Given("^There are some comments for article (\\d+) on the server$")
    public void there_are_some_comments_for_article_on_the_server(long articleID) throws Throwable {
        api.commentsPost(TOKEN1, articleID, createCommentRequest("Hello"), null);
        api.commentsPost(TOKEN2, articleID, createCommentRequest("How do you do"), null);
    }

    @When("^I send a GET to the /comments endpoint for article (\\d+)$")
    public void i_send_a_GET_to_the_comments_endpoint_for_article(long articleID) throws Throwable {
        try {
            lastApiResponse = api.commentsGetWithHttpInfo(articleID, false);
            commentsResponse = (List<CommentResponse>) lastApiResponse.getData();
            lastApiCallThrewException = false;
            lastApiException = null;
            lastStatusCode = lastApiResponse.getStatusCode();
        } catch (ApiException e) {
            lastApiCallThrewException = true;
            lastApiResponse = null;
            lastApiException = e;
            lastStatusCode = lastApiException.getCode();
        }
    }

    @Then("^I receive a list of only the article (\\d+) comments$")
    public void i_receive_a_list_of_only_the_article_comments(long articleID) throws Throwable {
        assertTrue(!commentsResponse.isEmpty());
        commentsResponse.forEach((commentResponse -> {
            assertTrue(commentResponse.getArticleID() == articleID);
        }));
    }


    @Then("^Every comment has no child$")
    public void every_comment_has_no_child() throws Throwable {
        commentsResponse.forEach((commentResponse -> {
            assertTrue(commentResponse.getChildren() == null || commentResponse.getChildren().size() == 0);
        }));
    }

    @When("^I send a GET to the /comments endpoint for article (\\d+) with parameter tree equal to (\\d+)$")
    public void i_send_a_GET_to_the_comments_endpoint_for_article_with_parameter_tree_equal_to(long articleID, int treeInt) throws Throwable {
        try {
            lastApiResponse = api.commentsGetWithHttpInfo(articleID, treeInt == 1);
            commentsResponse = (List<CommentResponse>) lastApiResponse.getData();
            lastApiCallThrewException = false;
            lastApiException = null;
            lastStatusCode = lastApiResponse.getStatusCode();
        } catch (ApiException e) {
            lastApiCallThrewException = true;
            lastApiResponse = null;
            lastApiException = e;
            lastStatusCode = lastApiException.getCode();
        }
    }

    @Given("^There is at least (\\d+) answer to a comment for article (\\d+)$")
    public void there_is_at_least_answer_to_a_comment_for_article(int arg1, long articleID) throws Throwable {
        // Posting 2 comments
        commentRequest = createCommentRequest("Little message");
        postMessage(TOKEN1, articleID, null);
        System.out.println("comment ID just after the posting method: " + commentID);

        commentRequest = createCommentRequest("Little response");
        postMessage(TOKEN2, articleID, commentID);
    }

    @Then("^I receive a list where some comments have children$")
    public void i_receive_a_list_where_some_comments_have_children() throws Throwable {
        boolean ok = false;

        for (CommentResponse comment : commentsResponse) {
            if (comment.getChildren() != null && comment.getChildren().size() >= 1) {
                ok = true;
            }
        }

        assertTrue(ok);
    }

    @When("^I delete one of them which is a not leaf in article (\\d+)$")
    public void i_delete_one_of_them_which_is_a_not_leaf_in_article(int arg1) throws Throwable {
        //Getting all comments for article with arg1 id
        commentsResponse = api.commentsGet((long) arg1, true);

        //Finding the first comment which is not a leaf
        for (CommentResponse comment : commentsResponse) {
            if (!comment.getChildren().isEmpty()) {
                commentResponse = comment;
                break;
            }
        }

        commentID = commentResponse.getId();
        //Delete the comment
        api.commentsIdDelete(TOKEN1, commentID);

    }

    @Then("^the list should contain the deleted comment with no message$")
    public void the_list_should_contain_the_deleted_comment_with_no_message() throws Throwable {
        boolean exists = false;
        for (CommentResponse comment : commentsResponse) {
            if (comment.getId() == commentResponse.getId()) {
                exists = true;
                assertNull(comment.getMessage());
            }
        }

        assertTrue(exists);
    }

    @When("^I delete one of them which is a leaf in article (\\d+)$")
    public void i_delete_one_of_them_which_is_a_leaf_in_article(int arg1) throws Throwable {
        //Getting all comments for article with arg1 id
        commentsResponse = api.commentsGet((long) arg1, true);

        //Finding the first comment which is not a leaf
        CommentResponse comment = commentsResponse.get(0);
        List<CommentResponse> children = comment.getChildren();

        while (!children.isEmpty()) {
            comment = children.get(0);
            children = comment.getChildren();
        }

        //Delete the comment
        api.commentsIdDelete(TOKEN1, comment.getId());
    }

    @Then("^the list should not contain the deleted comment$")
    public void the_list_should_not_contain_the_deleted_comment() throws Throwable {
        assertFalse(commentsResponse.contains(commentResponse));
    }

    @Given("^I have the id of a comment that does not exist$")
    public void i_have_the_id_of_a_comment_that_does_not_exist() throws Throwable {
        commentID = -1;
    }

    @When("^I try to modify the comment$")
    public void i_try_to_modify_the_comment() throws Throwable {
        try {
            lastApiResponse = api.commentsIdPutWithHttpInfo(userToken, commentID, commentRequest);
            commentsResponse = (List<CommentResponse>) lastApiResponse.getData();
            lastApiCallThrewException = false;
            lastApiException = null;
            lastStatusCode = lastApiResponse.getStatusCode();
        } catch (ApiException e) {
            lastApiCallThrewException = true;
            lastApiResponse = null;
            lastApiException = e;
            lastStatusCode = lastApiException.getCode();
        }
    }

    @Given("^I have a empty comment payload$")
    public void i_have_a_empty_comment_payload() throws Throwable {
        commentRequest = createCommentRequest("");
        userToken = TOKEN1;
    }

    @Given("^The author (\\d+) posted a comment for article (\\d+) on the server$")
    public void the_author_posted_a_comment_for_article_on_the_server(long authorID, long articleID) throws Throwable {
        String token = authorID == 1 ? TOKEN1 : TOKEN2;

        commentRequest = createCommentRequest("Coucou <3");
        postMessage(token, articleID, null);
    }

    @Given("^I have a comment payload without author$")
    public void i_have_a_comment_payload_without_author() throws Throwable {
        commentRequest = createCommentRequest("Coucou <3");
        userToken = "";
    }

    @Given("^I have a comment payload from the author (\\d+)$")
    public void i_have_a_comment_payload_from_the_author(long authorID) throws Throwable {
        commentRequest = createCommentRequest("Je suis un auteur");
        userToken = authorID == 1 ? TOKEN1 : TOKEN2;
    }

    public CommentRequest createCommentRequest(
            String message) {
        CommentRequest cr = new CommentRequest();
        cr.setMessage(message);

        return cr;
    }

    @Given("^The author (\\d+) has not reacted to the comment$")
    public void the_author_has_not_reacted_to_the_comment(int arg1) throws Throwable {
        for (Reaction reaction : commentResponse.getReactions()) {
            assertFalse(reaction.getAuthorID() == arg1);
        }
    }

    @When("^The author (\\d+) reacts to the comment$")
    public void the_author_reacts_to_the_comment(int arg1) throws Throwable {
        String token = (arg1 == 1 ? TOKEN1 : TOKEN2);

        reactToMessage(token);
    }

    @Given("^The author (\\d+) has reacted to the comment$")
    public void the_author_has_reacted_to_the_comment(int arg1) throws Throwable {
        String token = (arg1 == 1 ? TOKEN1 : TOKEN2);
        reactToMessage(token);
    }

    @Given("^The comment is deleted$")
    public void the_comment_is_deleted() throws Throwable {
        api.commentsIdDelete(TOKEN1, commentID);
    }

    @When("^I send a GET to the /comments/id/reaction endpoint$")
    public void i_send_a_GET_to_the_comments_id_reaction_endpoint() throws Throwable {
        try {
            lastApiResponse = api.commentsIdReactionGetWithHttpInfo(commentID);
            reactionNumber = (long) lastApiResponse.getData();
            lastApiCallThrewException = false;
            lastApiException = null;
            lastStatusCode = lastApiResponse.getStatusCode();
        } catch (ApiException e) {
            lastApiCallThrewException = true;
            lastApiResponse = null;
            lastApiException = e;
            lastStatusCode = lastApiException.getCode();
        }
    }

    @When("^The author (\\d+) sends a DELETE to the /comment/id/reaction endpoint$")
    public void the_author_sends_a_DELETE_to_the_comment_id_reaction_endpoint(int arg1) throws Throwable {
        try {
            String token = arg1 == 1 ? TOKEN1 : TOKEN2;
            lastApiResponse = api.commentsIdReactionDeleteWithHttpInfo(token, commentID);
            reactionNumber = (long) lastApiResponse.getData();
            lastApiCallThrewException = false;
            lastApiException = null;
            lastStatusCode = lastApiResponse.getStatusCode();
        } catch (ApiException e) {
            lastApiCallThrewException = true;
            lastApiResponse = null;
            lastApiException = e;
            lastStatusCode = lastApiException.getCode();
        }
    }

    @When("^The author (\\d+) reacts to the deleted comment$")
    public void the_author_reacts_to_the_deleted_comment(int arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        String token = (arg1 == 1 ? TOKEN1 : TOKEN2);
        reactToMessage(token);
    }

    @Given("^I delete one of them which is a leaf in article$")
    public void i_delete_one_of_them_which_is_a_leaf_in_article() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        api.commentsIdDelete(TOKEN1, commentID);
    }

    private boolean compareCommentRequestAndCommentResponse(CommentRequest commentRequest, CommentResponse commentResponse) {
        return commentRequest.getMessage().equals(commentResponse.getMessage());
    }

    private void postMessage(String token, long articleID, Long parentID) {
        try {
            lastApiResponse = api.commentsPostWithHttpInfo(token, articleID, commentRequest, parentID);
            lastApiCallThrewException = false;
            lastApiException = null;
            lastStatusCode = lastApiResponse.getStatusCode();
            String location = ((ArrayList<String>) lastApiResponse.getHeaders().get("Location")).get(0);
            commentID = Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
        } catch (ApiException e) {
            System.out.println("catch : " + e.getCode());
            lastApiCallThrewException = true;
            lastApiResponse = null;
            lastApiException = e;
            lastStatusCode = lastApiException.getCode();
        }
    }

    private void reactToMessage(String token) {
        try {
            lastApiResponse = api.commentsIdReactionPostWithHttpInfo(token, commentID);
            lastApiCallThrewException = false;
            lastApiException = null;
            lastStatusCode = lastApiResponse.getStatusCode();
            reactionNumber = (long) lastApiResponse.getData();
        } catch (ApiException e) {
            System.out.println("catch : " + e.getCode());
            lastApiCallThrewException = true;
            lastApiResponse = null;
            lastApiException = e;
            lastStatusCode = lastApiException.getCode();
        }
    }

    private void setUserToken() {
        userToken = TOKEN1;
    }
}
