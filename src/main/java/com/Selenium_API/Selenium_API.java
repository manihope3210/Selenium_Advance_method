package com.Selenium_API;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.Enum.Browser;
import com.Enum.Locators;

public interface Selenium_API {

	void setup(String url) ;

	void setupBrowser(Browser BrowserName,String url);
	
	void close();
	
	void quit();
	
	WebElement element(Locators type,String value);

	void click(WebElement ele);
	
	void type(WebElement ele,String testData);

	void typeAndEnter(WebElement ele, String testData,Keys Keys);

	void switchToWindow(int i);
	
	void selectValue(WebElement ele , String value);
	
	void selectText(WebElement ele , String value);

	void selectIndex(WebElement ele , String value);
	
	void Screenss() throws Exception;




	
}
