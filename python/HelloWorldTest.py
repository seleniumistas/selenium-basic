import re
import unittest

from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait


class HelloWorldTests(unittest.TestCase):
    TEN_SECONDS = 10

    RIGHT_NAME_INPUT_ID = "et_pb_contact_name_1"
    RIGHT_MESSAGE_INPUT_ID = "et_pb_contact_message_1"
    EQUATION_TEXT_CLASS = "et_pb_contact_captcha_question"
    RIGHT_ANSWER_INPUT_CSS = ".input.et_pb_contact_captcha"
    RIGHT_SUBMIT_BUTTON_CSS = "div#et_pb_contact_form_1 button"
    MESSAGE_TEXT_CSS = "div#et_pb_contact_form_1 div.et-pb-contact-message"

    def setUp(self):
        chrome_options: Options = webdriver.ChromeOptions()
        prefs = {"profile.default_content_setting_values.notifications": 2}
        chrome_options.add_experimental_option("prefs", prefs)
        self.driver = webdriver.Chrome(options=chrome_options)

    def test_verify_captcha(self):
        driver = self.driver
        # Go to the site
        driver.get("https://www.ultimateqa.com/filling-out-forms/")

        # Find right input field
        right_name_input_element = driver.find_element(By.ID, self.RIGHT_NAME_INPUT_ID)
        # TODO change it to your own name
        # Enter your name to right input field
        right_name_input_element.send_keys("My Name")

        # find right message field
        right_message_input_element = driver.find_element(By.ID, self.RIGHT_MESSAGE_INPUT_ID)
        # enter "Hello World!"
        right_message_input_element.send_keys("Hello World!")

        # find the equation text
        equation_text = driver.find_element(By.CLASS_NAME, self.EQUATION_TEXT_CLASS).text
        # extract numbers
        numbers = list(map(int, re.split(r"\s*\+\s*", equation_text)))
        total = sum(numbers)

        # find the answer field
        answer_input_element = driver.find_element(By.CSS_SELECTOR, self.RIGHT_ANSWER_INPUT_CSS)
        # enter the answer
        answer_input_element.send_keys(total)
        # find the submit button and click it
        driver.find_element(By.CSS_SELECTOR, self.RIGHT_SUBMIT_BUTTON_CSS).click()

        # Wait for visibility of message
        success_text = WebDriverWait(driver, self.TEN_SECONDS).until(
            EC.visibility_of_element_located((By.CSS_SELECTOR, self.MESSAGE_TEXT_CSS))).text

        # Verify obtained text is "Success"
        self.assertEqual(success_text, "Success", "Message should be Success")

    def tearDown(self):
        # terminate webdriver
        self.driver.quit()


if __name__ == '__main__':
    unittest.main()
