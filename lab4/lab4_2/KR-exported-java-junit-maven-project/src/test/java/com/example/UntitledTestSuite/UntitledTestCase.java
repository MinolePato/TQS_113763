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

public class UntitledTestCase {
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
