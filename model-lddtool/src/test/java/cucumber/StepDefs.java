package cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class connects the feature files with the Cucumber test code
 */
public class StepDefs {
    // The values of these variables should come from a row in the table in the
    // feature file.
    private String resourceDirectory;
    private String testDirectory;
    private String commandArgs;
    private String actualResponse;

    private String[] resolveArgumentStrings() {
        String array1[] = this.commandArgs.split("\\s+");  // split the commandArgs into a String array
        String[] args = new String[array1.length];
        int argIndex = 0;
        String resolvedToken = "";
        for (String temp : array1) {
            // Replace every occurence of "{resourceDirectory}" with actual value
            resolvedToken = temp.replace("{resourceDirectory}", this.resourceDirectory);
            // Replace every occurence of "{testDirectory}" with actual value.
            resolvedToken = resolvedToken.replace("{testDirectory}", this.testDirectory);
            // Replace every occurence of "%20" with a space
            resolvedToken = resolvedToken.replace("%20", " ");
            args[argIndex++] = resolvedToken;  // add the resolved token to the args array
        }

        return args;
    }

    @Given("the test directories {string} and {string} and command arguments {string}")
    public void i_have_lddtool_installed(String resourceDirectory, String testDirectory, String commandArgs) {
        this.resourceDirectory = resourceDirectory;
        this.testDirectory = testDirectory;
        this.commandArgs = commandArgs;
    }

    @When("the lddtool tool is run")
    public void i_run_lddtool_with() {
        String[] args = this.resolveArgumentStrings();  // resolve the commandArgs into a String array
        try {
            // run lddtool with the commandArgs and capture the output
            this.actualResponse = LddToolRunner.runLddTool(args);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new RuntimeException("DMDocument error", ex);
        }
    }

    @Then("the produced output from lddtool command should match or be similar to the expected results {string}")
    public void i_should_receive(String expectedResponse) {
        // compare the actual output of running lddtool with the expected response
        assertTrue(this.actualResponse.contains(expectedResponse),
                "Expected response: " + expectedResponse + " Actual response: " + this.actualResponse);
    }
}
