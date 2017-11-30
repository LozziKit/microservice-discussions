package io.lozzikit.discussions.api.spec.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.lozzikit.discussions.ApiException;
import io.lozzikit.discussions.ApiResponse;
import io.lozzikit.discussions.api.CommentsApi;
import io.lozzikit.discussions.api.dto.CommentRequest;
import io.lozzikit.discussions.api.dto.CommentResponse;
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

    public CreationSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();

    }

    @Given("^there is a discussion microservice up$")
    public void there_is_an_article_server() throws Throwable {
        assertNotNull(api);
    }

    @When("^I POST it to the /comments endpoint$")
    public void i_POST_it_to_the_comments_endpoint() throws Throwable {
        try {
            lastApiResponse = api.commentsPostWithHttpInfo(commentRequest);
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

    @Then("^I receive a (\\d+) status code$")
    public void i_receive_a_status_code(int arg1) throws Throwable {
        assertEquals(arg1, lastStatusCode);
    }

    @When("^The payload is an empty comment$")
    public void the_payload_is_an_empty_comment() throws Throwable {
        if (commentRequest != null) {
            if (!commentRequest.getMessage().isEmpty()) {
                commentRequest.setMessage("");
            }
        } else {
            commentRequest = createCommentRequest(1L, 1L, "El papa", "", 1L);
        }
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

    @Given("^I have a comment payload for article (\\d+)$")
    public void i_have_a_comment_payload_for_article(long arg1) throws Throwable {
        commentRequest = createCommentRequest(arg1, 42L, "Le pape", "Amen", (long) 1);
    }

    @Given("^There are some comment for article (\\d+) on the server$")
    public void there_are_some_comment_for_article_on_the_server(long arg1) throws Throwable {
        api.commentsPost(createCommentRequest(arg1, 2L, "Guy1", "Hello", 1L));
        api.commentsPost(createCommentRequest(arg1, 3L, "Guy2", "How do you do", 1L));
    }

    @When("^I send a GET to the /comments endpoint for article (\\d+)$")
    public void i_send_a_GET_to_the_comments_endpoint_for_article(long arg1) throws Throwable {
        try {
            lastApiResponse = api.commentsGetWithHttpInfo(arg1, false);
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
    public void i_receive_a_list_of_only_the_article_comments(long arg1) throws Throwable {
        assertTrue(!commentsResponse.isEmpty());
        commentsResponse.forEach((commentResponse -> {
            assertTrue(commentResponse.getArticleID() == arg1);
        }));
    }


    @Then("^Every comment has no child$")
    public void every_comment_has_no_child() throws Throwable {
        commentsResponse.forEach((commentResponse -> {
            assertTrue(commentResponse.getChildren() == null || commentResponse.getChildren().size() == 0);
        }));
    }

    @When("^I send a GET to the /comments endpoint for article (\\d+) with parameter tree equal to (\\d+)$")
    public void i_send_a_GET_to_the_comments_endpoint_for_article_with_parameter_tree_equal_to(long arg1, int treeInt) throws Throwable {
        try {
            lastApiResponse = api.commentsGetWithHttpInfo(arg1, treeInt == 1);
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
    public void there_is_at_least_answer_to_a_comment_for_article(int arg1, long articleId) throws Throwable {
        // Posting 2 comments
        CommentRequest comment = createCommentRequest(articleId, 1L, "MonAuthor", "Little message", null);
        lastApiResponse = api.commentsPostWithHttpInfo(comment);

        // Getting the id from the header
        String location = ((ArrayList<String>) lastApiResponse.getHeaders().get("Location")).get(0);
        long id = Long.parseLong(location.substring(location.lastIndexOf("/") + 1));

        CommentRequest commentResponse = createCommentRequest(articleId, 2L, "MonAutreAuteur", "Little message", id);
        api.commentsPost(commentResponse);
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

    public CommentRequest createCommentRequest(
            Long articleID,
            Long authorID,
            String author,
            String message,
            Long parentID) {
        CommentRequest cr = new CommentRequest();
        cr.setArticleID(articleID);
        cr.setAuthor(author);
        cr.setAuthorID(authorID);
        cr.setMessage(message);
        cr.setParentID(parentID);

        return cr;
    }

    private boolean compareCommentRequestAndCommentResponse(CommentRequest commentRequest, CommentResponse commentResponse) {
        System.out.println(commentRequest.getAuthorID() + " " + commentResponse.getAuthorID());
        return commentRequest.getMessage().equals(commentResponse.getMessage()) &&
                commentRequest.getArticleID().equals(commentResponse.getArticleID()) &&
                commentRequest.getAuthor().equals(commentResponse.getAuthor()) &&
                commentRequest.getAuthorID().equals(commentResponse.getAuthorID()) &&
                commentRequest.getParentID().equals(commentResponse.getParentID());
    }
}
