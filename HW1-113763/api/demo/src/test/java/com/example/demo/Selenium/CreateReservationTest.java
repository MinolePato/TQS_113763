package com.example.demo.Selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateReservationTest {

    private static WebDriver driver;

    @BeforeAll
    public static void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().setSize(new Dimension(602, 746));
    }

    @Test
    @Order(1)
    public void createReservation() {
        driver.get("http://127.0.0.1:5500/frontend/Page.html");

        // Selecionar restaurante
        WebElement restaurantSelect = driver.findElement(By.id("restaurant-select"));
        new Select(restaurantSelect).selectByVisibleText("Groto2");

        // Clicar no botão para "Make Reservation"
        driver.findElement(By.linkText("Make Reservation")).click();

        // Preencher form de reserva
        new Select(driver.findElement(By.id("reservation-restaurant"))).selectByVisibleText("Groto2");
        new Select(driver.findElement(By.id("reservation-meal"))).selectByVisibleText("Pizza - €10.00 - 10/04/2025 at 14:30:00");

        WebElement nameInput = driver.findElement(By.id("customer-name"));
        nameInput.clear();
        nameInput.sendKeys("Francisco Pinto");

        // Submeter formulário
        driver.findElement(By.cssSelector("form#reservation-form button[type='submit']")).click();

        // Verificar se código de reserva apareceu (exemplo simples)
        WebElement result = driver.findElement(By.cssSelector("#reservation-result p"));
        Assertions.assertTrue(result.getText().contains("Reservation Code:"));
    }

    @Test
    @Order(2)
    public void checkReservation() {
        driver.findElement(By.linkText("Check Reservation")).click();
        WebElement codeInput = driver.findElement(By.id("reservation-code"));
        codeInput.clear();
        codeInput.sendKeys("YIPNBRWF"); // Código gerado no teste anterior

        driver.findElement(By.cssSelector("#check-reservation-form button")).click();
        // Podes adicionar verificações aqui também se houver output visível
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
