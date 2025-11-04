package com.Selenium_API;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import com.Enum.Browser;
import com.Enum.Locators;

public interface Selenium_API {

    // ----------------------------------------------
    // ✅ Browser Setup
    // ----------------------------------------------
    void setup(String url);
    void setupBrowser(Browser browserName, String url);
    void close();
    void quit();

    // ----------------------------------------------
    // ✅ Element Actions
    // ----------------------------------------------
    WebElement element(Locators type, String value);
    WebElement element(Locators type, String value, String textToType);
    void click(WebElement ele);

    void type(WebElement ele, String testData);
    void typeAndEnter(WebElement ele, String testData, Keys key);
    void sendKeys(Locators type, String value, String text);
    void sendKeyEle(Locators type, String value);

    // ----------------------------------------------
    // ✅ Dropdowns
    // ----------------------------------------------
    void selectByVisibleText(Locators locatorType, String value, String textToSelect);
    void selectByValue(Locators locatorType, String value, String optionValue);
    void selectByIndex(Locators locatorType, String value, int index);
    void selectDropdownOption(By locator, String visibleText);
    void selectValue(WebElement ele, String value);
    void selectText(WebElement ele, String value);
    void selectIndex(WebElement ele, String value);

    // ----------------------------------------------
    // ✅ Checkboxes
    // ----------------------------------------------
    void selectCheckboxesById(String... idsToSelect);
    void deselectCheckboxesById(String... idsToUnselect);
    void selectAllCheckboxes();

    // ----------------------------------------------
    // ✅ Auto-complete / Multi-select
    // ----------------------------------------------
    void selectAutoCompleteOptions(Locators inputLocator, String inputValue, String commonXpath, List<String> valuesToSelect);

    // ----------------------------------------------
    // ✅ Waits
    // ----------------------------------------------
    WebElement waitForElement(Locators type, String value, int seconds);
    WebElement waitUntilClickable(By by);

    // ----------------------------------------------
    // ✅ Screenshot
    // ----------------------------------------------
    void Screenss() throws Exception;

    // ----------------------------------------------
    // ✅ Scrolling
    // ----------------------------------------------
    void scrollToElement(WebElement element);

    // ----------------------------------------------
    // ✅ Window Handling
    // ----------------------------------------------
    void switchToWindow(int i);
}
