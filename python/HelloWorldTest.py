from selenium import webdriver
import unittest
from pathlib import Path
import os
from sys import platform as _platform

from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC
import re


class HelloWorldTests (unittest.TestCase):
    TEN_SECOND = 10

    RIGHT_NAME_INPUT_ID = "et_pb_contact_name_1"
    RIGHT_MESSAGE_INPUT_ID = "et_pb_contact_message_1"
    EQUATION_TEXT_CLASS = "et_pb_contact_captcha_question"
    RIGHT_ANSWER_INPUT_CSS = ".input.et_pb_contact_captcha"
    RIGHT_SUBMIT_BUTTON_CSS = "div#et_pb_contact_form_1 button"
    MESSAGE_TEXT_CSS = "div#et_pb_contact_form_1 div.et-pb-contact-message"

    def setUp(self):
        # It assumes chrome driver is saved at <user home>/drivers/chromedriver
        user_home = str(Path.home())
        driver_path = r"drivers"
        driver_name = r"chromedriver.exe" if _platform == "Windows" else r"chromedriver"
        webdriver_path = os.path.join(user_home, driver_path, driver_name)
        chrome_options: Options = webdriver.ChromeOptions()
        prefs = {"profile.default_content_setting_values.notifications": 2}
        chrome_options.add_experimental_option("prefs", prefs)
        self.driver = webdriver.Chrome(executable_path=webdriver_path, chrome_options=chrome_options)

    def test_verify_captcha(self):
        # Go to the site
        self.driver.get("https://www.ultimateqa.com/filling-out-forms/")

        # Find right input field
        right_name_input_element = self.driver.find_element(By.ID, HelloWorldTests.RIGHT_NAME_INPUT_ID)
        # TODO change it to your own name
        # Enter your name to right input field
        right_name_input_element.send_keys("My Name")

        # find right message field
        right_message_input_element = self.driver.find_element(By.ID, HelloWorldTests.RIGHT_MESSAGE_INPUT_ID)
        # enter "Hello World!"
        right_message_input_element.send_keys("Hello World!")

        # find the equation text
        equation_text = self.driver.find_element(By.CLASS_NAME, HelloWorldTests.EQUATION_TEXT_CLASS).text
        # extract numbers
        numbers = list(map(int, re.split(r"\s*\+\s*", equation_text)))
        total = sum(numbers)

        # find the answer field
        answer_input_element = self.driver.find_element(By.CSS_SELECTOR, HelloWorldTests.RIGHT_ANSWER_INPUT_CSS)
        # enter the answer
        answer_input_element.send_keys(total)
        # find the submit button and click it
        self.driver.find_element(By.CSS_SELECTOR, HelloWorldTests.RIGHT_SUBMIT_BUTTON_CSS).click()

        # Wait for visibility of message
        success_text = WebDriverWait(self.driver, HelloWorldTests.TEN_SECOND).until(
            EC.visibility_of_element_located((By.CSS_SELECTOR, HelloWorldTests.MESSAGE_TEXT_CSS))).text

        # Verify obtained text is "Success"
        self.assertEqual(success_text, "Success", "Message should be Success")

    def earDown(self):
        # terminate webdriver
        self.driver.quit()


if __name__ == '__main__':
    unittest.main()
