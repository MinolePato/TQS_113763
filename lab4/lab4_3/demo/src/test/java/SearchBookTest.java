import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;
class SearchBookTest {
    WebDriver driver;
    Wait<WebDriver> wait;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions().setBinary("/usr/bin/brave-browser");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(2));
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    @Test
    void test() {
        driver.get("https://cover-bookstore.onrender.com/");
        assertThat(driver.getTitle()).contains("Cover");

        WebElement searchElement = driver.findElement(By.cssSelector("[data-testid=book-search-input]"));
        searchElement.sendKeys("Harry Potter");
        searchElement.sendKeys(Keys.RETURN);
        WebElement bookTitleElement = driver.findElement(By.cssSelector("[class=SearchList_bookTitle__1wo4a]"));
        wait.until(b -> bookTitleElement.isDisplayed());
        assertThat(bookTitleElement.getText()).contains("Harry Potter and the Sorcerer's Stone");

    }
}
