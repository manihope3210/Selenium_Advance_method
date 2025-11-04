package com.example;

import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.Enum.Browser;
import com.Enum.Locators;
import com.Reports.TestListener;
import com.SeleniumBase.Base;
@Listeners(TestListener.class)
public class LoginWithBase extends Base  {

	    @Test
	    void browserLaunch() throws Exception {
	        setupBrowser(Browser.Firefox, "https://demo.automationtesting.in/Register.html");
	        sendKeys(Locators.XPath, "//input[@placeholder='First Name']", "manikandan");
	        sendKeys(Locators.XPath, "//input[@placeholder='Last Name']", "sakthivel");
	        sendKeys(Locators.XPath, "//textarea[@ng-model='Adress']", "John Smith, 123 Main Street");
	        sendKeys(Locators.XPath, "//input[@ng-model='EmailAdress']", "manitest@gmail.com");
	        sendKeys(Locators.XPath, "//input[@ng-model='Phone']", "1122334455");

	        // ✅ Radio button (use click)
	        click(element(Locators.XPath, "//input[@value='Male']"));

	        // ✅ Checkbox
	        selectCheckboxesById("checkbox1", "checkbox3");

	        // ✅ Language autocomplete
	        WebElement language = element(Locators.XPath, "//div[@id='msdd']");
	        language.click();
	        selectAutoCompleteOptions(
	                Locators.ID, "languageInput",
	                "//ul[contains(@class,'ui-autocomplete')]//a",
	                Arrays.asList("English", "Hindi", "Spanish")
	        );

	        // ✅ Dropdowns
//	        selectByVisibleText(Locators.XPath, "//span[@role='combobox']", "India");
	        selectByValue(Locators.XPath, "//select[@id='Skills']", "Java");
	        selectDropdownOption(By.id("yearbox"), "1997");
	        selectDropdownOption(By.xpath("//select[@placeholder='Month']"), "July");
	        selectDropdownOption(By.id("daybox"), "26");
	        sendKeys(Locators.XPath, "//input[@id='firstpassword']", "0123456");
	        
	        sendKeys(Locators.XPath, "//input[@id='secondpassword']", "0123456");
	        click(element(Locators.XPath, "//button[@id='submitb']"));
	        
	        TestListener.getTest().pass("✅ Registration test completed successfully.");

	    }
	

	
	
	
//	 @Test
	 void my() {
		 setupBrowser(Browser.InternetExplorer, "https://web.whatsapp.com/");
	}
	
	

}
