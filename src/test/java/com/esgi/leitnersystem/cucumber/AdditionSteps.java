package com.esgi.leitnersystem.cucumber;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AdditionSteps {
  private int number1;
  private int number2;
  private int result;

  @Given("the first number is {int}")
  public void the_first_number_is(int number) {
    this.number1 = number;
  }

  @Given("the second number is {int}")
  public void the_second_number_is(int number) {
    this.number2 = number;
  }

  @When("I add the two numbers")
  public void i_add_the_two_numbers() {
    this.result = number1 + number2;
  }

  @Then("the result should be {int}")
  public void the_result_should_be(int expectedResult) {
    assertEquals(expectedResult, result);
  }
}
