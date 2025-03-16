package com.example;

import io.cucumber.java.After;
import io.cucumber.java.ParameterType;
import io.cucumber.java.bs.A;

import java.time.LocalDate;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedCondition;


import ch.qos.logback.core.util.Duration;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class BlazeStepsWeb {
    
    private final WebDriver driver = new ChromeDriver();
    
    @Given("I navigate to {string}")
    public void BeOnWebsite(String url) {
        driver.get(url);
    }

    @When("I select {string} for my departure City")
        public void selectFrom(String from) {
        WebElement element = driver.findElement(By.name("fromPort"));
        element.sendKeys(from);
    }

    @And("I select {string} for my destination City")
    public void selectTo(String to) {
        WebElement element = driver.findElement(By.name("toPort"));
        element.sendKeys(to);
    }

    @When("I click {string}")
    public void click(String button) {
        driver.findElement(By.xpath("//input[@value='" + button + "']")).click();
    }  

    @Then("I should be redirected to the page with title {string}")
    public void reservepage(String title) {
        String Title = driver.getTitle();
        assert Title.contains(title);
    }

    @And("I enter my Name {string}")
    public void enterName(String name) {
        WebElement element = driver.findElement(By.id("inputName"));
        element.sendKeys(name);
    }

    @And("I enter my Address {string}")
    public void enterAddress(String address) {
        WebElement element = driver.findElement(By.id("address"));
        element.sendKeys(address);
    }

    @And("I enter my City {string}")
    public void enterCity(String city) {
        WebElement element = driver.findElement(By.id("city"));
        element.sendKeys(city);
    }

    @And("I enter my State {string}")
    public void enterState(String state) {
        WebElement element = driver.findElement(By.id("state"));
        element.sendKeys(state);
    }

    @And("I enter my Zip Code {int}")
    public void enterZip(Integer zip) {
        WebElement element = driver.findElement(By.id("zipCode"));
        element.sendKeys(zip.toString());
    }

    @And("I enter my Credit Card Number {int}")
    public void enterCard(Integer card) {
        WebElement element = driver.findElement(By.id("creditCardNumber"));
        element.sendKeys(card.toString());
    }

    @And("I enter my Name on Card {string}")
    public void enterNameCard(String name) {
        WebElement element = driver.findElement(By.id("nameOnCard"));
        element.sendKeys(name);
    }

    @And("I click Purchase Flight")
    public void purchaseFlight() {
        driver.findElement(By.xpath("//input[@value='Purchase Flight']"));
    }

    @After()
    public void closeBrowser() {
        driver.quit();
    }
}