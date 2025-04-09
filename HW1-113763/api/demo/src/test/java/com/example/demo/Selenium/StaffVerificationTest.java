package com.example.demo.Selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StaffVerificationTest {

    private static WebDriver driver;

    // 🔁 Coloca o código de reserva aqui, ou injeta dinamicamente no futuro
    private static final String reservationCode = "RNK1HSL8";

    @BeforeAll
    static void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(1543, 985));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @Test
    void verifyReservationAsStaff() {
        driver.get("http://127.0.0.1:5500/frontend/Page.html");

        // Aceder ao separador Staff Verification
        driver.findElement(By.linkText("Staff Verification")).click();

        // Introduzir o código da reserva
        WebElement codeInput = driver.findElement(By.id("verify-code"));
        codeInput.clear();
        codeInput.sendKeys(reservationCode);

        // Submeter
        driver.findElement(By.cssSelector("#staff-verification-form button")).click();

        // Verificar sucesso
        WebElement successMsg = driver.findElement(By.cssSelector("#verification-result p"));
        String expected = "Check-in successful! The customer can now proceed.";
        Assertions.assertTrue(successMsg.getText().contains(expected), "Mensagem de sucesso esperada");
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) driver.quit();
    }
}
