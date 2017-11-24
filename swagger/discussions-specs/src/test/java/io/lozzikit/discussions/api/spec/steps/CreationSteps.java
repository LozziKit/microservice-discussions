package io.lozzikit.discussions.api.spec.steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.lozzikit.discussions.ApiException;
import io.lozzikit.discussions.ApiResponse;
import io.lozzikit.discussions.api.CommentsApi;
import io.lozzikit.discussions.api.dto.CommentRequest;
import io.lozzikit.discussions.api.dto.CommentResponse;
import io.lozzikit.discussions.api.spec.helpers.Environment;

import static org.junit.Assert.*;
import java.util.List;

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
        if (commentRequest != null){
            if (!commentRequest.getMessage().isEmpty()){
                commentRequest.setMessage("");
            }
        } else {
            commentRequest = createCommentRequest(1, 1, "El papa", "", 1);
        }
    }

    @Then("^The new comment should be in the list$")
    public void the_new_comment_should_be_in_the_list() throws Throwable {
        boolean commentFound = false;
        for (CommentResponse commentResponse : commentsResponse){
            if (compareCommentRequestAndCommentResponse(commentRequest, commentResponse)){
                commentFound = true;
                break;
            }
        }
        assertTrue(commentFound);
    }

    @Given("^I have a comment payload for article (\\d+)$")
    public void i_have_a_comment_payload_for_article(long arg1) throws Throwable {
        commentRequest = createCommentRequest(arg1, 42, "Le pape", "Amen", (long) 1);
    }

    @Given("^There are some comment for article (\\d+) on the server$")
    public void there_are_some_comment_for_article_on_the_server(int arg1) throws Throwable {
        api.commentsPost(createCommentRequest(arg1, 2, "Guy1", "Hello", 1));
        api.commentsPost(createCommentRequest(arg1, 3, "Guy2", "How do you do", 1));
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

    @Then("^I receive a list of the article (\\d+) comments$")
    public void i_receive_a_list_of_the_article_comments(long arg1) throws Throwable {
        assertTrue(!commentsResponse.isEmpty());
    }

    public CommentRequest createCommentRequest(
            long articleID,
            long authorID,
            String author,
            String message,
            long parentID)
    {
        CommentRequest cr = new CommentRequest();
        cr.setArticleID(articleID);
        cr.setAuthor(author);
        cr.setAuthorID(authorID);
        cr.setMessage(message);
        cr.setParentID(parentID);

        return cr;
    }

    public boolean compareCommentRequestAndCommentResponse(CommentRequest commentRequest, CommentResponse commentResponse){
        System.out.println(commentRequest.getAuthorID() + " " + commentResponse.getAuthorID());
        return  commentRequest.getMessage().equals(commentResponse.getMessage()) &&
                commentRequest.getArticleID().equals(commentResponse.getArticleID()) &&
                commentRequest.getAuthor().equals(commentResponse.getAuthor()) &&
                commentRequest.getAuthorID().equals(commentResponse.getAuthorID()) &&
                commentRequest.getParentID().equals(commentResponse.getParentID());
    }
}
