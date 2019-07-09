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
    private WebDriver driver;

    @BeforeTest
    public void setUp(){
        String chromeDriverPath = Paths.get(System.getProperty("user.home"), "drivers", "chromedriver").toString();

        // Optional, if not specified, WebDriver will search your path for chromedriver.
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
    }

    @BeforeMethod
    public void prepareWebDriver(){
        driver = new ChromeDriver();
    }

    @Test(description = "Verify successful captcha input")
    public void verifyCaptchaTest(){
        driver.get("https://www.ultimateqa.com/filling-out-forms/");

        //Find name field on the right side
        WebElement rightNameInputElement = driver.findElement(By.id("et_pb_contact_name_1"));
        //TODO Replace with your own name
        //Enter your name
        rightNameInputElement.sendKeys("Your NAME");

        //Find message field on the right side
        WebElement rightMessageInputElement = driver.findElement(By.id("et_pb_contact_message_1"));
        //Enter "Hello World!"
        rightMessageInputElement.sendKeys("Hello World!");

        //Find equation text
        String equationText = driver.findElement(By.className("et_pb_contact_captcha_question")).getText();

        String numbers[] =  equationText.split("\\s*\\+\\s*");
        //Add two numbers
        int total = Integer.parseInt(numbers[0]) + Integer.parseInt(numbers[1]);

        //Find answer field
        final String ANSWER_INPUT_XPATH = "//input[contains(@class, 'input et_pb_contact_captcha')]";
        WebElement answerInputElement = driver.findElement(By.xpath(ANSWER_INPUT_XPATH));
        //Enter the answer
        answerInputElement.sendKeys(String.valueOf(total));

        //Find the submit button, and click it
        final String RIGHT_SUBMIT_BUTTON_XPATH = "//div[@id='et_pb_contact_form_1']//button";
        driver.findElement(By.xpath(RIGHT_SUBMIT_BUTTON_XPATH)).click();

        final String MESSAGE_TEXT_XPATH = "//div[@id='et_pb_contact_form_1']//div[@class='et-pb-contact-message']";
        //Initialize wait
        WebDriverWait wait = new WebDriverWait(driver, 10);
        //wait for visibility of the element contains "Successful" message, then get the text.
        String successText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(MESSAGE_TEXT_XPATH))).getText();

        //verify obtained text is "Successful"
        Assert.assertEquals(successText, "Success", "successful submission message should be Success");
    }

    @AfterMethod
    public void tearDown() {
        //terminate webdriver
        driver.quit();
    }
}
