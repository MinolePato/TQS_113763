
import static java.lang.invoke.MethodHandles.lookup;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

import io.github.bonigarcia.wdm.WebDriverManager;






class HelloWorldChromeJupiterTest {

    static final Logger log = getLogger(lookup().lookupClass());

    private WebDriver driver;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup(); 
    }

    @BeforeEach
    void setup() {
        driver = new ChromeDriver(); 
    }

    @Test
    void test() {
        // Exercise
        String sutUrl = "https://www.selenium.dev/selenium/web/web-form.html";
        driver.get(sutUrl); 
        String title = driver.getTitle(); 
        log.debug("The title of {} is {}", sutUrl, title); 
        driver.getTitle();

        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

        WebElement textBox = driver.findElement(By.name("my-text"));
        WebElement submitButton = driver.findElement(By.cssSelector("button"));

        textBox.sendKeys("Selenium");
        submitButton.click();

        WebElement message = driver.findElement(By.id("message"));
        message.getText();
        
        // Verify
        assertThat(title).isEqualTo("Web form"); 
        
    }

    @AfterEach
    void teardown() {
        driver.quit(); 
    }

}