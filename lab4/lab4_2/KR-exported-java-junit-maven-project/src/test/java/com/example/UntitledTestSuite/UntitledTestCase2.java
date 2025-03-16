package com.example.UntitledTestSuite;

import java.time.Duration;

import org.junit.After;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class UntitledTestCase2 {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  JavascriptExecutor js;
  @Before
  public void setUp() throws Exception {
    System.setProperty("webdriver.chrome.driver", "");
    driver = new ChromeDriver();
    baseUrl = "https://www.google.com/";
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
    js = (JavascriptExecutor) driver;
  }

  @Test
  public void testUntitledTestCase() throws Exception {
    //ERROR: Caught exception [unknown command []]
    driver.findElement(By.name("fromPort")).click();
    new Select(driver.findElement(By.name("fromPort"))).selectByVisibleText("Boston");
    driver.findElement(By.xpath("//form[@action='reserve.php']")).click();
    new Select(driver.findElement(By.name("toPort"))).selectByVisibleText("Rome");
    driver.findElement(By.xpath("//input[@value='Find Flights']")).click();
    driver.get("https://blazedemo.com/reserve.php");
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='$472.56'])[1]/following::td[1]")).click();
    driver.findElement(By.xpath("//tr[2]/td/input")).click();
    driver.get("https://blazedemo.com/purchase.php");
    driver.findElement(By.xpath("//input[@value='Purchase Flight']")).click();
    driver.get("https://blazedemo.com/confirmation.php");
    //ERROR: Caught exception [unknown command []]
    driver.findElement(By.linkText("Travel The World")).click();
    driver.get("https://blazedemo.com/index.php");
    new Select(driver.findElement(By.name("fromPort"))).selectByVisibleText("Boston");
    new Select(driver.findElement(By.name("toPort"))).selectByVisibleText("New York");
    driver.findElement(By.xpath("//input[@value='Find Flights']")).click();
    driver.get("https://blazedemo.com/reserve.php");
    driver.findElement(By.xpath("//input[@value='Choose This Flight']")).click();
    driver.get("https://blazedemo.com/purchase.php");
    driver.findElement(By.id("inputName")).click();
    driver.findElement(By.id("inputName")).clear();
    driver.findElement(By.id("inputName")).sendKeys("fg");
    driver.findElement(By.id("address")).click();
    driver.findElement(By.id("address")).clear();
    driver.findElement(By.id("address")).sendKeys("rrr");
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Address'])[1]/following::div[1]")).click();
    driver.findElement(By.id("city")).click();
    driver.findElement(By.id("city")).clear();
    driver.findElement(By.id("city")).sendKeys("hyhyhy");
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Address'])[1]/following::div[1]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='City'])[1]/following::div[2]")).click();
    driver.findElement(By.id("state")).click();
    driver.findElement(By.id("state")).clear();
    driver.findElement(By.id("state")).sendKeys("hfdhd");
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='City'])[1]/following::div[1]")).click();
    driver.findElement(By.id("zipCode")).click();
    driver.findElement(By.id("zipCode")).clear();
    driver.findElement(By.id("zipCode")).sendKeys("1221");
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='State'])[1]/following::div[1]")).click();
    new Select(driver.findElement(By.id("cardType"))).selectByVisibleText("American Express");
    driver.findElement(By.id("creditCardNumber")).click();
    driver.findElement(By.id("creditCardNumber")).clear();
    driver.findElement(By.id("creditCardNumber")).sendKeys("3333");
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Credit Card Number'])[1]/following::div[1]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Credit Card Number'])[1]/following::div[2]")).click();
    driver.findElement(By.id("creditCardMonth")).click();
    driver.findElement(By.id("creditCardMonth")).clear();
    driver.findElement(By.id("creditCardMonth")).sendKeys("");
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Month'])[1]/following::div[1]")).click();
    driver.findElement(By.id("creditCardMonth")).click();
    driver.findElement(By.id("creditCardMonth")).click();
    driver.findElement(By.id("creditCardMonth")).clear();
    driver.findElement(By.id("creditCardMonth")).sendKeys("2");
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Month'])[1]/following::div[1]")).click();
    driver.findElement(By.id("creditCardYear")).click();
    driver.findElement(By.id("creditCardYear")).clear();
    driver.findElement(By.id("creditCardYear")).sendKeys("2000");
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Month'])[1]/following::div[1]")).click();
    driver.findElement(By.id("nameOnCard")).click();
    driver.findElement(By.id("nameOnCard")).clear();
    driver.findElement(By.id("nameOnCard")).sendKeys("ggg");
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Year'])[1]/following::div[1]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Name on Card'])[1]/following::label[1]")).click();
    driver.findElement(By.xpath("//input[@value='Purchase Flight']")).click();
    driver.get("https://blazedemo.com/confirmation.php");
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}