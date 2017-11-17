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

    @Given("^I have a comment payload$")
    public void i_have_a_comment_payload() throws Throwable {
        commentRequest = new CommentRequest();
    }

    @Given("^There are some comment for that article on the server$")
    public void there_are_some_comment_for_that_article_on_the_server() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^I POST it to the /comments endpoint$")
    public void i_POST_it_to_the_comments_endpoint() throws Throwable {
        try {
            api.commentsPost(commentRequest);
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

    @When("^I send a GET to the /comments endpoint for an article$")
    public void i_send_a_GET_to_the_comments_endpoint_for_an_article() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^I receive a (\\d+) status code$")
    public void i_receive_a_status_code(int arg1) throws Throwable {
        assertEquals(201, lastStatusCode);
    }

    @Then("^I receive a list of the article comments$")
    public void i_receive_a_list_of_the_article_comments() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^I ask for a list of all the comments for an article by sending a GET to the /comments endpoint$")
    public void i_ask_for_a_list_of_all_the_comments_for_an_article_by_sending_a_GET_to_the_comments_endpoint() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^The payload is an empty comment$")
    public void the_payload_is_an_empty_comment() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^The new comment should be in the list$")
    public void the_new_comment_should_be_in_the_list() throws Throwable {
        //assertTrue(commentsResponse.find(comment));
    }
}
