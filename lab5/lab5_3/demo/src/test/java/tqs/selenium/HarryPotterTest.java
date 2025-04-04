package tqs.selenium;

import org.assertj.core.api.Assertions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class HarryPotterTest {
   WebDriver driver;

   @When("I navigate to {string}")
   public void i_navigate_to(String string) {
      WebDriverManager.chromedriver().setup();
      ChromeOptions options = new ChromeOptions().setBinary("/usr/bin/brave-browser");
      this.driver = new ChromeDriver(options);
      this.driver.get(string);
   }

   @And("I search for {string}")
   public void i_search_for(String string) {
      WebElement searchElement = this.driver.findElement(By.cssSelector("[data-testid=book-search-input]"));
      searchElement.sendKeys(string);
   }

   @And("I click submit")
   public void i_press_enter() {
      WebElement searchElement = this.driver.findElement(By.cssSelector("[data-testid=book-search-input]"));
      searchElement.sendKeys(Keys.RETURN);
   }

   @Then("the user should see {string} in the search results")
   public void the_user_should_see(String string) {
      WebElement bookTitleElement = this.driver.findElement(By.cssSelector("[class=SearchList_bookTitle__1wo4a]"));
      Assertions.assertThat(bookTitleElement.getText()).contains(string);
   }
}