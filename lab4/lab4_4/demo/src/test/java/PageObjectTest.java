


import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.Dimension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SeleniumJupiter.class)
public class PageObjectTest {
    private WebDriver driver;

    private static String PAGE_URL = "https://blazedemo.com/";

    @FindBy(name = "fromPort")
    private WebElement fromPortButton;

    @FindBy(xpath = "//option[. = 'Paris']")
    private WebElement pariOption;

    @FindBy(css = ".form-inline:nth-child(1) > option:nth-child(5)")
    private WebElement button1;

    @FindBy(name = "toPort")
    private WebElement toPortButton;

    @FindBy(xpath = "//option[. = 'Berlin']")
    private WebElement berOption;

    @FindBy(css = ".form-inline:nth-child(4) > option:nth-child(4)")
    private WebElement button2;

    @FindBy(css = ".btn-primary")
    private WebElement button3;

    @FindBy(css = "tr:nth-child(4) .btn")
    private WebElement button4;

    @FindBy(id = "inputName")
    private WebElement nameInput;

    @FindBy(id = "address")
    private WebElement addressInput;

    @FindBy(id = "city")
    private WebElement cityInput;

    @FindBy(id = "state")
    private WebElement stateInput;

    @FindBy(id = "zipCode")
    private WebElement zipCodeInput;

    @FindBy(xpath = "//option[. = 'Visa']")
    private WebElement cardTypeOption;

    @FindBy(id = "creditCardNumber")
    private WebElement cardNumber;

    @FindBy(id = "creditCardMonth")
    private WebElement cardMonth;

    @FindBy(id = "creditCardYear")
    private WebElement cardYear;

    @FindBy(id = "nameOnCard")
    private WebElement nameOnCard;

    @FindBy(css = ".btn-primary")
    private WebElement button5;

    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();  
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void reset() {
        driver.quit();
    }

    @Test
    public void translatedTest() {
        driver.get(PAGE_URL);
        driver.manage().window().setSize(new Dimension(1600, 900));
        PageFactory.initElements(driver, this);

        fromPortButton.click();
        pariOption.click();
        button1.click();

        toPortButton.click();
        berOption.click();
        button2.click();

        button3.click();
        button4.click();

        assertThat(driver.getTitle()).isEqualTo("BlazeDemo Purchase");

        nameInput.sendKeys("Jaime");
        addressInput.sendKeys("Sesamo");
        cityInput.sendKeys("Street");
        stateInput.sendKeys("Pt");
        zipCodeInput.sendKeys("1111");

        cardTypeOption.click();

        cardNumber.sendKeys("12221");
        cardMonth.sendKeys("2");
        cardYear.sendKeys("2000");

        nameOnCard.sendKeys("Jaime");
        
        button5.click();

        assertThat(driver.getTitle()).isEqualTo("BlazeDemo Confirmation");
    }
}