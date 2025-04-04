package tqs.cucumber;

import io.cucumber.java.en.*;

import static java.lang.invoke.MethodHandles.lookup;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import org.junit.jupiter.api.Assertions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StepDefinitions {

static final Logger log = getLogger(lookup().lookupClass());
private Calculator calc;

    @Given("^a calculator I just turned on$")
    public void setup() {
        calc = new Calculator();
    }

    @When("I add {int} and {int}")
    public void add(int arg1, int arg2) {
        log.debug("Adding {} and {}", arg1, arg2);
        calc.push(arg1);
        calc.push(arg2);
        calc.push("+");
    }


    @When("I substract {int} to {int}")
    public void substract(int arg1, int arg2) {
        log.debug("Substracting {} and {}", arg1, arg2);
        calc.push(arg1);
        calc.push(arg2);
        calc.push("-");
    }

    @When("I add floats {double} and {double}")
    public void i_add_floats_and(Double double1, Double double2) {
        log.debug("Adding {} and {}", double1, double2);
        calc.push(double1);
        calc.push(double2);
        calc.push("+");
    }

    @When("I multiply {int} to {int}")
    public void i_multiply_and(int arg1, int arg2) {
        log.debug("Multiplying {} and {}", arg1, arg2);
        calc.push(arg1);
        calc.push(arg2);
        calc.push("*");
    }

    @When("I divide {int} to {int}")
    public void i_divide_by(int arg1, int arg2) {
        log.debug("Dividing {} by {}", arg1, arg2);
        calc.push(arg1);
        calc.push(arg2);
        calc.push("/");
    }

    @When("I square {int}")
    public void i_square(int arg1) {
        log.debug("Squaring {}", arg1);
        calc.push(arg1);
        calc.push(arg1);
        calc.push("*");
    }


    @Then("the result is {int}")
    public void the_result_is(double expected) {
        Number value = calc.value();
        log.debug("Result: {} (expected {})", value, expected);
        assertEquals(expected, value);
    }

}
