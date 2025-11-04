package com.SeleniumBase;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.edge.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.ie.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.*;
import org.openqa.selenium.support.ui.*;

import com.Enum.Browser;
import com.Enum.Locators;
import com.Selenium_API.Selenium_API;
import com.Reports.TestListener;
import com.aventstack.extentreports.Status;
import com.google.common.io.Files;

public class Base implements Selenium_API {

    protected WebDriver dr;
    protected WebDriverWait wait;

    long timeOut = 10;
    long maxWaitTime = 30;

    // ----------------------------------------------------------------
    // ✅ Browser Setup
    // ----------------------------------------------------------------
    @Override
    public void setup(String url) {
        dr = new ChromeDriver();
        dr.manage().window().maximize();
        dr.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeOut));
        dr.get(url);
        wait = new WebDriverWait(dr, Duration.ofSeconds(maxWaitTime));
    }

    @Override
    public void setupBrowser(Browser browserName, String url) {
        switch (browserName) {
            case Chrome:
                Map<String, Object> chromePrefs = new HashMap<>();
                chromePrefs.put("credentials_enable_service", false);
                chromePrefs.put("profile.password_manager_enabled", false);
                chromePrefs.put("profile.default_content_setting_values.notifications", 2);

                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setExperimentalOption("prefs", chromePrefs);
                chromeOptions.addArguments("--incognito", "--start-maximized",
                        "--disable-notifications", "--no-default-browser-check", "--no-first-run");
                dr = new ChromeDriver(chromeOptions);
                break;

            case Edge:
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized", "--inprivate", "--disable-notifications");
                dr = new EdgeDriver(edgeOptions);
                break;

            case Firefox:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addPreference("dom.webnotifications.enabled", false);
                firefoxOptions.addPreference("signon.rememberSignons", false);
                firefoxOptions.addArguments("-private");
                dr = new FirefoxDriver(firefoxOptions);
                break;

            case InternetExplorer:
                InternetExplorerOptions ieOptions = new InternetExplorerOptions();
                ieOptions.ignoreZoomSettings();
                ieOptions.introduceFlakinessByIgnoringSecurityDomains();
                dr = new InternetExplorerDriver(ieOptions);
                break;

            case Safari:
                dr = new SafariDriver(); // macOS only
                break;

            default:
                throw new IllegalArgumentException("❌ Invalid browser name: " + browserName);
        }

        dr.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeOut));
        dr.manage().window().maximize();
        dr.get(url);
        wait = new WebDriverWait(dr, Duration.ofSeconds(maxWaitTime));

        // ✅ Detect browser automatically
        Capabilities caps = ((RemoteWebDriver) dr).getCapabilities();
        String browserUsed = caps.getBrowserName().substring(0, 1).toUpperCase() + caps.getBrowserName().substring(1);
        String version = caps.getBrowserVersion();

        System.out.println("✅ Browser launched → " + browserUsed + " " + version);

        // ✅ Send this info to Extent Report automatically
        TestListener.extent.setSystemInfo("Browser", browserUsed);
        TestListener.extent.setSystemInfo("Browser Version", version);
    }

    // ----------------------------------------------------------------
    // ✅ Wait Helper
    // ----------------------------------------------------------------
    @Override
    public WebElement waitForElement(Locators type, String value, int seconds) {
        By by = getBy(type, value);
        WebDriverWait localWait = new WebDriverWait(dr, Duration.ofSeconds(seconds));
        return localWait.until(ExpectedConditions.elementToBeClickable(by));
    }

    @Override
    public WebElement waitUntilClickable(By by) {
        return new WebDriverWait(dr, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(by));
    }

    // ----------------------------------------------------------------
    // ✅ Core Actions with Wait
    // ----------------------------------------------------------------
    @Override
    public void close() {
        dr.close();
    }

    @Override
    public void quit() {
        dr.quit();
    }

    @Override
    public WebElement element(Locators type, String value) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(getBy(type, value)));
    }

    // --- Click ---
    @Override
    public void click(WebElement ele) {
        WebDriverWait localWait = new WebDriverWait(dr, Duration.ofSeconds(10));
        WebElement element = localWait.until(ExpectedConditions.elementToBeClickable(ele));
        element.click();
        TestListener.getTest().log(Status.INFO, "🖱️ Clicked element: " + ele);
    }

    public void click(Locators type, String value) {
        WebElement el = waitForElement(type, value, 10);
        el.click();
        TestListener.getTest().log(Status.INFO, "🖱️ Clicked element: " + value);
    }

    // --- SendKeys with 10 sec Wait ---
    @Override
    public void sendKeys(Locators type, String value, String text) {
        try {
            By locator = getBy(type, value);
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

            // ✅ Fixed 10 sec wait before typing
            Thread.sleep(1000);

            el.clear();
            el.sendKeys(text);

            System.out.println("✅ Waited 10s and typed '" + text + "' in element: " + value);
            TestListener.getTest().log(Status.INFO, "⌛ Waited 10s and typed '" + text + "' in element: " + value);
        } catch (Exception e) {
            TestListener.getTest().log(Status.FAIL, "❌ Failed to type in element: " + value + " → " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void type(WebElement ele, String testData) {
        WebElement element = wait.until(ExpectedConditions.visibilityOf(ele));
        element.clear();
        element.sendKeys(testData);
        TestListener.getTest().log(Status.INFO, "⌨️ Entered text: " + testData);
    }

    @Override
    public void typeAndEnter(WebElement ele, String testData, Keys key) {
        WebElement element = wait.until(ExpectedConditions.visibilityOf(ele));
        element.sendKeys(testData, key);
    }

    // ----------------------------------------------------------------
    // ✅ Dropdown Handling
    // ----------------------------------------------------------------
    @Override
    public void selectByVisibleText(Locators locatorType, String value, String textToSelect) {
        WebElement el = waitForElement(locatorType, value, 10);
        new Select(el).selectByVisibleText(textToSelect);
        TestListener.getTest().log(Status.INFO, "📋 Selected by visible text: " + textToSelect);
    }

    @Override
    public void selectByValue(Locators locatorType, String value, String optionValue) {
        WebElement el = waitForElement(locatorType, value, 10);
        new Select(el).selectByValue(optionValue);
        TestListener.getTest().log(Status.INFO, "📋 Selected by value: " + optionValue);
    }

    @Override
    public void selectDropdownOption(By locator, String visibleText) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(locator));
        new Select(dropdown).selectByVisibleText(visibleText);
        TestListener.getTest().log(Status.INFO, "📋 Selected '" + visibleText + "' from dropdown");
    }

    // ----------------------------------------------------------------
    // ✅ Checkboxes
    // ----------------------------------------------------------------
    @Override
    public void selectCheckboxesById(String... idsToSelect) {
        List<WebElement> checkboxes = dr.findElements(By.xpath("//input[@type='checkbox']"));
        for (WebElement checkbox : checkboxes) {
            String id = checkbox.getAttribute("id");
            for (String targetId : idsToSelect) {
                if (id.equals(targetId) && !checkbox.isSelected()) {
                    wait.until(ExpectedConditions.elementToBeClickable(checkbox)).click();
                    
                }
            }
        }
        TestListener.getTest().log(Status.INFO, "📋 Selected by visible text: " + idsToSelect);

    }

    @Override
    public void deselectCheckboxesById(String... idsToUnselect) {
        List<WebElement> checkboxes = dr.findElements(By.xpath("//input[@type='checkbox']"));
        for (WebElement checkbox : checkboxes) {
            String id = checkbox.getAttribute("id");
            for (String targetId : idsToUnselect) {
                if (id.equals(targetId) && checkbox.isSelected()) {
                    checkbox.click();
                }
            }
        }
        TestListener.getTest().log(Status.INFO, "📋 Selected by visible text: " + idsToUnselect);

    }

    @Override
    public void selectAllCheckboxes() {
        List<WebElement> checkboxes = dr.findElements(By.xpath("//input[@type='checkbox']"));
        for (WebElement cb : checkboxes) {
            if (!cb.isSelected()) cb.click();
        }
        TestListener.getTest().log(Status.INFO, "📋 Selected by visible text: " + "");

    }

    // ----------------------------------------------------------------
    // ✅ Screenshot
    // ----------------------------------------------------------------
    @Override
    public void Screenss() throws Exception {
        File src = ((TakesScreenshot) dr).getScreenshotAs(OutputType.FILE);
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File destDir = new File("./Pic");
        if (!destDir.exists()) destDir.mkdirs();
        File dest = new File(destDir, "img_" + timestamp + ".png");
        Files.copy(src, dest);
        TestListener.getTest().log(Status.INFO, "📋 Selected by visible text: " + src);

    }

    // ----------------------------------------------------------------
    // ✅ Helper Methods
    // ----------------------------------------------------------------
    private By getBy(Locators locatorType, String value) {
        switch (locatorType) {
            case ID: return By.id(value);
            case Name: return By.name(value);
            case ClassName: return By.className(value);
            case TagName: return By.tagName(value);
            case LinkText: return By.linkText(value);
            case PartialLinkText: return By.partialLinkText(value);
            case XPath: return By.xpath(value);
            case CSSSelector: return By.cssSelector(value);
            default: throw new IllegalArgumentException("Invalid locator type: " + locatorType);
        }
        
       

    }

@Override
public void sendKeyEle(Locators type, String value) {
    try {
        WebElement ele = element(type, value);
        ele.sendKeys(Keys.ENTER);
    } catch (Exception e) {
        System.err.println("❌ sendKeyEle() failed for: " + value + " — " + e.getMessage());
    }
    TestListener.getTest().log(Status.INFO, "📋 Selected by visible text: " + value);

}

@Override
public void selectByIndex(Locators locatorType, String value, int index) {
    try {
        WebElement ele = element(locatorType, value);
        new Select(ele).selectByIndex(index);
    } catch (Exception e) {
        System.err.println("❌ selectByIndex() failed: " + e.getMessage());
    }
    TestListener.getTest().log(Status.INFO, "📋 Selected by visible text: " + value);

}

@Override
public void selectValue(WebElement ele, String value) {
    try {
        new Select(ele).selectByValue(value);
    } catch (Exception e) {
        System.err.println("❌ selectValue() failed: " + e.getMessage());
    }
    TestListener.getTest().log(Status.INFO, "📋 Selected by visible text: " + value);

}

@Override
public void selectText(WebElement ele, String value) {
    try {
        new Select(ele).selectByVisibleText(value);
    } catch (Exception e) {
        System.err.println("❌ selectText() failed: " + e.getMessage());
    }
    TestListener.getTest().log(Status.INFO, "📋 Selected by visible text: " + value);

}

@Override
public void selectIndex(WebElement ele, String value) {
    try {
        int index = Integer.parseInt(value);
        new Select(ele).selectByIndex(index);
    } catch (Exception e) {
        System.err.println("❌ selectIndex() failed: " + e.getMessage());
    }
    TestListener.getTest().log(Status.INFO, "📋 Selected by visible text: " + value);

}

@Override
public void selectAutoCompleteOptions(Locators inputLocator, String inputValue, String commonXpath,
                                      List<String> valuesToSelect) {
    try {
        WebElement inputField = element(inputLocator, inputValue);
        inputField.click();

        for (String val : valuesToSelect) {
            WebElement option = dr.findElement(
                    By.xpath(commonXpath + "[normalize-space()='" + val + "']")
            );
            option.click();
            Thread.sleep(500); // slight pause between selections
        }
    } catch (Exception e) {
        System.err.println("❌ selectAutoCompleteOptions() failed: " + e.getMessage());
    }
    TestListener.getTest().log(Status.INFO, "📋 Selected by visible text: " + valuesToSelect);

}

@Override
public void scrollToElement(WebElement element) {
    try {
        ((JavascriptExecutor) dr).executeScript("arguments[0].scrollIntoView(true);", element);
        Thread.sleep(500);
    } catch (Exception e) {
        System.err.println("❌ scrollToElement() failed: " + e.getMessage());
    }
    TestListener.getTest().log(Status.INFO, "📋 Selected by visible text: " + element);

}

@Override
public void switchToWindow(int i) {
    try {
        Set<String> handles = dr.getWindowHandles();
        String[] windows = handles.toArray(new String[0]);
        dr.switchTo().window(windows[i]);
    } catch (Exception e) {
        System.err.println("❌ switchToWindow() failed: " + e.getMessage());
        
        
    }
    TestListener.getTest().log(Status.INFO, "📋 Selected by visible text: " + i);

}


@Override
public WebElement element(Locators type, String value, String textToType) {
    WebElement ele = null;
    try {
        switch (type) {
            case ID:
                ele = dr.findElement(By.id(value));
                break;
            case Name:
                ele = dr.findElement(By.name(value));
                break;
            case XPath:
                ele = dr.findElement(By.xpath(value));
                break;
            case CSSSelector:
                ele = dr.findElement(By.cssSelector(value));
                break;
            case ClassName:
                ele = dr.findElement(By.className(value));
                break;
            case LinkText:
                ele = dr.findElement(By.linkText(value));
                break;
            case PartialLinkText:
                ele = dr.findElement(By.partialLinkText(value));
                break;
            case TagName:
                ele = dr.findElement(By.tagName(value));
                break;
        }

        // Wait for visibility
        new WebDriverWait(dr, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(ele));

        ele.clear();
        ele.sendKeys(textToType);
    } catch (Exception e) {
        System.err.println("❌ element() failed for locator: " + value + " — " + e.getMessage());
    }
    TestListener.getTest().log(Status.INFO, "📋 Selected by visible text: " + value);

    return ele;
    

}}