package stepDefinitions;

import au.com.telstra.simcardactivator.SimCardActivator;
import au.com.telstra.simcardactivator.models.SimCard;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = SimCardActivator.class, loader = SpringBootContextLoader.class)
public class SimCardActivatorStepDefinitions {
    @Autowired
    private TestRestTemplate restTemplate;

    @Given("the SIM card Actuator is running")
    public void simCardActuatorIsRunning() {
        String actuatorJarPath = "services/SimCardActuator.jar";
        String command = "java -jar " + actuatorJarPath;

        try {
            Process process = Runtime.getRuntime().exec(command);
            Assertions.assertTrue(process.isAlive());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @When("I submit an activation request with ICCID {string}")
    public void submitActivationRequest(String iccid) {
        // Implement logic to submit activation request with the given ICCID
        SimCard activatedSimCard = new SimCard("1255789453849037777", "john@email.com");
    }

    @Then("the activation should be successful")
    public void activationShouldBeSuccessful() {
        // Implement assertion for successful activation
    }

    @Then("the activation should fail")
    public void activationShouldFail() {
        // Implement assertion for failed activation
    }

    @Then("the response confirms the activation success")
    public void responseConfirmsActivationSuccess() {
        // Implement assertion based on the query response
    }

    @Then("the response confirms the activation failure")
    public void responseConfirmsActivationFailure() {
        // Implement assertion based on the query response
    }
}