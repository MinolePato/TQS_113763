import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class SearchBookTest {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.get("https://cover-bookstore.onrender.com/");

        try {
            // Locate the search input field using a well-defined selector
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement searchBox = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid=book-search-input]"))
            );
            searchBox.sendKeys("Harry Potter" + Keys.RETURN);

            // Wait for search results to load
            List<WebElement> results = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("[data-testid=book-search-item]"))
            );

            // Check for the correct book in the search results
            boolean bookFound = false;
            for (WebElement result : results) {
                String title = result.findElement(By.cssSelector("[data-testid=book-title]")).getText();
                String author = result.findElement(By.cssSelector("[data-testid=book-author]")).getText();

                if (title.contains("Harry Potter and the Sorcerer's Stone") && author.contains("J. K. Rowlling")) {
                    bookFound = true;
                    break;
                }
            }

            if (bookFound) {
                System.out.println("Test passed: Book found!");
            } else {
                throw new AssertionError("Expected book not found in search results");
            }
        } finally {
            driver.quit();
        }
    }
}
