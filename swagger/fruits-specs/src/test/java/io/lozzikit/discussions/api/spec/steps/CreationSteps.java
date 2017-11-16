package io.lozzikit.discussions.api.spec.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.lozzikit.discussions.ApiException;
import io.lozzikit.discussions.ApiResponse;
import io.lozzikit.discussions.api.CommentsApi;
import io.lozzikit.discussions.api.dto.*;
import io.lozzikit.discussions.api.spec.helpers.Environment;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by Olivier Liechti on 27/07/17.
 */
public class CreationSteps {

    private Environment environment;
    private CommentsApi api;


    private ApiResponse lastApiResponse;
    private ApiException lastApiException;
    private boolean lastApiCallThrewException;
    private int lastStatusCode;

    public CreationSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
    }

    @Given("^there is an article server$")
    public void there_is_an_article_server() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Given("^I have a comment payload$")
    public void i_have_a_comment_payload() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @When("^I POST it to the /comments endpoint$")
    public void i_POST_it_to_the_comments_endpoint() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^I receive a (\\d+) status code$")
    public void i_receive_a_status_code(int arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Given("^There are some articles on the server$")
    public void there_are_some_articles_on_the_server() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @When("^I send a GET to the /comments endpoint$")
    public void i_send_a_GET_to_the_comments_endpoint() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^I receive a list of these articles$")
    public void i_receive_a_list_of_these_articles() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @When("^I ask for a list of all the comments by sending a GET to the /comments endpoint$")
    public void i_ask_for_a_list_of_all_the_comments_by_sending_a_GET_to_the_comments_endpoint() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^The new comment should be in the list$")
    public void the_new_comment_should_be_in_the_list() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }
}
