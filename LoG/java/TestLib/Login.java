package TestLib;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
  

public class Login {
	static String browserName = "GC";
	static Automation_properties automation_properties = Automation_properties.getInstance();
	static WebDriver driver;

	  
	public static void signIn(String browser) throws Exception
	{
		if(BaseDriver.getDriver()==null)
		{
		Automation_properties.setInstance(null);
		automation_properties = Automation_properties.getInstance();
		System.out.println(automation_properties.getInstance().getProperty(automation_properties.BASEURL));
		driver=BaseDriver.StartBrowser(browser,automation_properties.getInstance().getProperty(automation_properties.BASEURL));
		Driver.getLogger().info("Logged in with User: ");
		}
		
	}
	
	public static void signIn(String browser, String Device) throws Exception
	{
		if(BaseDriver.getDriver()==null)
		{
		Automation_properties.setInstance(null);
		automation_properties = Automation_properties.getInstance();
		System.out.println(automation_properties.getInstance().getProperty(automation_properties.BASEURL));
		driver=BaseDriver.StartBrowser(browser,automation_properties.getInstance().getProperty(automation_properties.BASEURL),"mobile",Device);
		Driver.getLogger().info("Logged in with User: ");
		}
		
	}  
	public static void openwebsite(String url) throws Exception{
		if(BaseDriver.getDriver()==null)
		{
			Automation_properties.setInstance(null);
		automation_properties = Automation_properties.getInstance();	
		driver=BaseDriver.StartBrowser(System.getProperty("browser",automation_properties.getInstance().getProperty(automation_properties.BROWSER)),url);
		Driver.getLogger().info("Logged in with User: ");
		}
		
	}
	public static void mobilesignIn(String Device) throws Exception
	{
		if(BaseDriver.getDriver()==null)
		{
		Automation_properties.setInstance(null);
		automation_properties = Automation_properties.getInstance();
		System.out.println(automation_properties.getProperty(automation_properties.BASEURL));
		if(Device.equalsIgnoreCase("andriod"))
		driver=BaseDriver.StartMobileBrowser(Device,automation_properties.getProperty(automation_properties.BASEURL),automation_properties.getProperty(automation_properties.DeviceName),automation_properties.getProperty(automation_properties.UDID));
		if(Device.equalsIgnoreCase("ios"))
			driver=BaseDriver.StartMobileBrowser(Device,automation_properties.getProperty(automation_properties.BASEURL),automation_properties.getProperty(automation_properties.DeviceName),automation_properties.getProperty(automation_properties.PlatformVersion));
			
		Driver.getLogger().info("Logged in with User: ");
		}
		
	}
	
	
	public static void signIn() throws Exception
	{
			if(BaseDriver.getDriver()==null)
			{
				Automation_properties.setInstance(null);
			automation_properties = Automation_properties.getInstance();	
			driver=BaseDriver.StartBrowser(System.getProperty("browser",automation_properties.getInstance().getProperty(automation_properties.BROWSER)),System.getProperty("url",automation_properties.getInstance().getProperty(automation_properties.BASEURL)));
			Driver.getLogger().info("Logged in with User: ");
			}
		
	}
	
	
	public static void signInOxo() throws Exception
	{
			if(BaseDriver.getDriver()==null)
			{
			Automation_properties.setInstance(null);
			automation_properties = Automation_properties.getInstance();
			System.out.println(automation_properties.getInstance().getProperty(automation_properties.BASEURL));
			driver=BaseDriver.StartBrowser(automation_properties.getInstance().getProperty(automation_properties.BROWSER),automation_properties.getInstance().getProperty(automation_properties.BASEURL));
			Driver.getLogger().info("Logged in with User: ");
			}
		
	}
	
	public static void signInRevolon() throws Exception
	{
			if(BaseDriver.getDriver()==null)
			{
				Automation_properties.setInstance(null);
				automation_properties = Automation_properties.getInstance();
			driver=BaseDriver.StartBrowser(automation_properties.getInstance().getProperty(automation_properties.BROWSER),automation_properties.getInstance().getProperty(automation_properties.BASEURL));
			Driver.getLogger().info("Logged in with User: ");
			}
		
	}
	
	

	public static void logginIn(String username,String password) throws Exception { 
		Common.textBoxInput("id","strUserLogin",username);
		Common.textBoxInput("id","strUserPwd",password);
		Common.clickElement("id", "btnLogin");
	}

	
	}
