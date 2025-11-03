package com.example;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import com.Enum.Browser;
import com.Enum.Locators;
import com.SeleniumBase.Base;

public class LoginWithBase extends Base  {
	@Test
	 void browserLaunch() throws Exception {
		 setupBrowser( Browser.Firefox,"https://www.google.com/");
		 WebElement search = element(Locators.ID, "APjFqb");
		 typeAndEnter(search, "facebook",Keys.ENTER);
		 Thread.sleep(3000);
		 Screenss();
	}
	 @Test
	 void my() {
		 setupBrowser(Browser.InternetExplorer, "https://web.whatsapp.com/");
	}
	
	

}
