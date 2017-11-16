package io.lozzikit.discussions.api.spec.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
/*import io.avalia.fruits.ApiException;
import io.avalia.fruits.ApiResponse;
import io.avalia.fruits.api.DefaultApi;
import io.avalia.fruits.api.dto.Fruit;*/
import io.lozzikit.discussions.api.spec.helpers.Environment;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by Olivier Liechti on 27/07/17.
 */
public class CreationSteps {

    private Environment environment;
    /*private DefaultApi api;

    Fruit fruit;

    private ApiResponse lastApiResponse;
    private ApiException lastApiException;*/
    private boolean lastApiCallThrewException;
    private int lastStatusCode;

    public CreationSteps(Environment environment) {
        this.environment = environment;
        //this.api = environment.getApi();
    }


}
