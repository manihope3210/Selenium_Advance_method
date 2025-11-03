package com.SeleniumBase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.Enum.Browser;
import com.Enum.Locators;
import com.Selenium_API.Selenium_API;
import com.google.common.io.Files;

public class Base implements Selenium_API {

	long timeOut = 10;
	long maxWaitTime =30;
	WebDriver dr;
	WebDriverWait wait = null;

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
	            dr = new ChromeDriver();
	            break;
	        case Edge:
	            dr = new EdgeDriver();
	            break;
	        case Firefox:
	            dr = new FirefoxDriver();
	            break;
	        case InternetExplorer:
                dr = new org.openqa.selenium.ie.InternetExplorerDriver();
                break;

	        default:
	            System.err.println("❌ Invalid browser name: " + browserName);
	            return;
	    }

	    dr.manage().window().maximize();
		dr.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeOut));
	    dr.get(url);
		wait = new WebDriverWait(dr, Duration.ofSeconds(maxWaitTime));

	}


	@Override
	public void close() {
     dr.close();
	}

	@Override
	public void quit() {
		dr.quit();
	}

	@Override
	public WebElement element(Locators string, String value) {
		switch (string) {
		case ID:
			return dr.findElement(By.id(value));
		case Name:
			return dr.findElement(By.name(value));
		case ClassName:
			return dr.findElement(By.className(value));
		case TagName:
			return dr.findElement(By.tagName(value));
		case LinkText:
			return dr.findElement(By.linkText(value));
		case PartialLinkText:
			return dr.findElement(By.partialLinkText(value));
		case XPath:
			return dr.findElement(By.xpath(value));
		case CSSSelector:
			return dr.findElement(By.cssSelector(value));
		default:
			break;
		}
		
		return null;
	}

	@Override
	public void click(WebElement ele) {
		WebElement element = wait.withMessage("Element is not clickable").until(ExpectedConditions.elementToBeClickable(ele));
		element.click();
	}

	@Override
	public void type(WebElement ele,String testData) {
		WebElement element = wait.until(ExpectedConditions.visibilityOf(ele));
		element.clear();
		element.sendKeys(testData);

	}
	@Override
	public void typeAndEnter(WebElement ele,String testData, Keys key) {
		WebElement element = wait.until(ExpectedConditions.visibilityOf(ele));
		element.clear();
		element.sendKeys(testData,key);

	}

	@Override
	public void switchToWindow(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectValue(WebElement ele, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectText(WebElement ele, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectIndex(WebElement ele, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Screenss() throws Exception {
		File src = ((TakesScreenshot) dr).getScreenshotAs(OutputType.FILE);
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File dest = new File("./Pic/img_" + timestamp + ".png");
		Files.copy(src, dest);		
	}



}
