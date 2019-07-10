import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.nio.file.Paths;


public class HelloWorldTests {
    private final static int TEN_SECOND = 10;

    private final String RIGHT_NAME_INPUT_ID = "et_pb_contact_name_1";
    private final String RIGHT_MESSAGE_INPUT_ID = "et_pb_contact_message_1";
    private final String EQUATION_TEXT_CLASS = "et_pb_contact_captcha_question";
    private final String RIGHT_ANSWER_INPUT_CSS = ".input.et_pb_contact_captcha";
    private final String RIGHT_SUBMIT_BUTTON_CSS = "div#et_pb_contact_form_1 button";
    private final String MESSAGE_TEXT_CSS = "div#et_pb_contact_form_1 div.et-pb-contact-message";

    private WebDriver driver;

    @BeforeTest
    public void setUp() {
        String chromeDriverPath = Paths.get(System.getProperty("user.home"), "drivers", "chromedriver").toString();

        // Optional, if not specified, WebDriver will search your path for chromedriver.
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
    }

    @BeforeMethod
    public void prepareWebDriver() {
        driver = new ChromeDriver();
    }

    @Test(description = "Verify successful captcha input")
    public void verifyCaptchaTest() {
        driver.get("https://www.ultimateqa.com/filling-out-forms/");

        //Find name field on the right side
        WebElement rightNameInputElement = driver.findElement(By.id(RIGHT_NAME_INPUT_ID));
        //TODO Replace with your own name
        //Enter your name
        rightNameInputElement.sendKeys("Your NAME");

        //Find message field on the right side
        WebElement rightMessageInputElement = driver.findElement(By.id(RIGHT_MESSAGE_INPUT_ID));
        //Enter "Hello World!"
        rightMessageInputElement.sendKeys("Hello World!");

        //Find equation text
        String equationText = driver.findElement(By.className(EQUATION_TEXT_CLASS)).getText();

        String[] numbers = equationText.split("\\s*\\+\\s*");
        //Add two numbers
        int total = Integer.parseInt(numbers[0]) + Integer.parseInt(numbers[1]);

        //Find answer field
        WebElement answerInputElement = driver.findElement(By.cssSelector(RIGHT_ANSWER_INPUT_CSS));
        //Enter the answer
        answerInputElement.sendKeys(String.valueOf(total));

        //Find the submit button, and click it
        driver.findElement(By.cssSelector(RIGHT_SUBMIT_BUTTON_CSS)).click();

        //Initialize wait
        WebDriverWait wait = new WebDriverWait(driver, TEN_SECOND);
        //wait for visibility of the element contains "Success" message, then get the text.
        String successText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(MESSAGE_TEXT_CSS))).getText();

        //verify obtained text is "Success"
        Assert.assertEquals(successText, "Success", "Message should be Success");
    }

    @AfterMethod
    public void tearDown() {
        //terminate webdriver
        driver.quit();
    }
}
