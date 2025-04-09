package com.example.demo.Selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateAndVerifyReservationTest {

    private static WebDriver driver;
    private static String reservationCode;

    @BeforeAll
    static void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(1543, 985));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @Test
    @Order(1)
    void createReservation() {
        driver.get("http://127.0.0.1:5500/frontend/Page.html");

        // Navegar até "View Meals" e selecionar restaurante
        driver.findElement(By.linkText("View Meals")).click();
        new Select(driver.findElement(By.id("restaurant-select")))
            .selectByVisibleText("Restaurante para teste de selenium");

        // Ir para "Make Reservation"
        driver.findElement(By.linkText("Make Reservation")).click();
        new Select(driver.findElement(By.id("reservation-restaurant")))
            .selectByVisibleText("Restaurante para teste de selenium");
        new Select(driver.findElement(By.id("reservation-meal")))
            .selectByVisibleText("Almoço com tudo incluido - €20.00 - 12/04/2025 at 18:30:00");

        // Preencher nome do cliente
        driver.findElement(By.id("customer-name")).sendKeys("Teste1");

        // Submeter
        driver.findElement(By.cssSelector("#reservation-form button[type='submit']")).click();

        // Capturar o código de reserva
        WebElement result = driver.findElement(By.cssSelector("#reservation-result p"));
        String text = result.getText();

        // Extrair o código (ex: "Reservation Code: RNK1HSL8")
        Matcher matcher = Pattern.compile("Reservation Code:\\s+(\\w+)").matcher(text);
        if (matcher.find()) {
            reservationCode = matcher.group(1);
            System.out.println("Código de reserva capturado: " + reservationCode);
        }

        Assertions.assertNotNull(reservationCode, "Reservation code should be captured");
    }

    @Test
    @Order(2)
    void checkReservationByCode() {
        Assertions.assertNotNull(reservationCode, "Reservation code must be set from the previous test");

        driver.findElement(By.linkText("Check Reservation")).click();
        driver.findElement(By.id("reservation-code")).sendKeys(reservationCode);
        driver.findElement(By.cssSelector("#check-reservation-form button")).click();

        WebElement status = driver.findElement(By.cssSelector("#reservation-details p:nth-child(8)"));
        Assertions.assertTrue(status.getText().contains("Status"), "Status should be displayed");
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
