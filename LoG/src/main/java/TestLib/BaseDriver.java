package TestLib;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;


public class BaseDriver 
{

	public static WebDriver driver;
	public static String className = Common.getCLassName();
	static Automation_properties automation_properties = Automation_properties.getInstance();
	
	public static WebDriver StartBrowser(String browserName,String URL) throws Exception {
		return StartBrowser(browserName,URL,"","");
		
	}
	
	
		public static WebDriver StartBrowser(String browserName,String URL, String View, String Device) throws InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
		{
			try {
			 if(browserName.equalsIgnoreCase("firefox"))
			{
					WebDriverManager.firefoxdriver().setup();
					
 
					 
					DesiredCapabilities cap = new DesiredCapabilities();
					 cap.setJavascriptEnabled(true);					 
					 
					 
					 FirefoxOptions  options = new FirefoxOptions();
						options.addArguments("--no-sandbox");
						options.addArguments("-disable-dev-shm-usage");
						options.addArguments("--test-type");
						options.addArguments("--enable-video-player-chromecast-support");
						options.addPreference("dom.file.createInChild", true);
						options.addArguments("disable-infobars");
						options.addArguments("--disable-notifications");
						options.addArguments("--disable-popup-blocking");
					driver=new FirefoxDriver(options);
					
				}
			 
			 else if(browserName.equalsIgnoreCase("edge"))
				{
					WebDriverManager.edgedriver().setup();
					 /*DesiredCapabilities cap = new DesiredCapabilities();
					 cap.setJavascriptEnabled(true);
					 FirefoxOptions  opt = new FirefoxOptions();*/
					driver=new EdgeDriver();
					
				}
			else if(browserName.equalsIgnoreCase("safari"))
			{
				DriverManagerType safari = DriverManagerType.SAFARI;
				WebDriverManager.getInstance(safari).setup();
				Class<?> safariClass =  Class.forName(safari.browserClass());
				driver = (WebDriver) safariClass.getDeclaredConstructor().newInstance();
			}

			
			
			else if(browserName.equalsIgnoreCase("chrome"))
			{
				WebDriverManager.chromedriver().setup();
				String 	downloadFilepath = System.getProperty("user.dir")+"\\TestLogs\\Download";
				File dlDirectory = new File(downloadFilepath);
				dlDirectory.mkdir();
				if (!dlDirectory.exists()) {
					dlDirectory.mkdir();
				}else{
					File[] files = dlDirectory.listFiles();
					if(files!=null)
					{
						for (File f : files)
						{
							if(f.delete())
								{
									System.out.println(f.getName()+"  file deleted  "+f.getAbsolutePath());
									//Driver.getLogger().info(f.getName()+"  file deleted  "+f.getAbsolutePath());
								}
								else
								{
									System.out.println("Unable to delete file.....");
								}
							}
						}
					
				}
			
				

				

				
			// Save Chrome Preferences in Hash Map
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			HashMap<String, Object> contentsetting = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
		
			chromePrefs.put("download.default_directory", downloadFilepath);
			contentsetting.put("multiple-automatic-downloads", 1);
	
			chromePrefs.put("safebrowsing.enabled", "true");
			
			chromePrefs.put("download.prompt_for_download", "false");
			chromePrefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
			chromePrefs.put("credentials_enable_service", false);
			chromePrefs.put("password_manager_enabled", false);

			// Save Chrome Opions
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--no-sandbox");
			options.addArguments("-disable-dev-shm-usage");
			options.addArguments("--test-type");
			options.addArguments("--enable-video-player-chromecast-support");
			
			
	
			
			options.setExperimentalOption("useAutomationExtension", false);
	
			options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
			options.addArguments("disable-infobars");
			options.addArguments("--disable-notifications");
			options.addArguments("--disable-popup-blocking");
			if(View.equalsIgnoreCase("mobile")) {
				HashMap<String, String> mobileEmulation = new HashMap<String, String>();
				//mobileEmulation.put("deviceName", "iPhone X");
				mobileEmulation.put("deviceName", Device);
				options.setExperimentalOption("mobileEmulation", mobileEmulation);
				System.out.print("Mobile Device initiated");
			}
			
			
			//options.setCapability("chrome.switches", Arrays.asList("--disable-local-storage"));
			//options.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap);
			// cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			//options.setCapability(ChromeOptions.CAPABILITY, options);
			//options.setCapability("--enable-video-player-chromecast-support", true);
			//DesiredCapabilities handlSSLErr = DesiredCapabilities.chrome ()       
			options.addArguments("ignore-certificate-errors");
					//WebDriver driver = new ChromeDriver (handlSSLErr);
			LoggingPreferences logPrefschrome = new LoggingPreferences();
			logPrefschrome.enable(LogType.BROWSER, Level.ALL);
			//options.setCapability(CapabilityType.LOGGING_PREFS, logPrefschrome);
			options.setExperimentalOption("prefs", chromePrefs);
			driver = new ChromeDriver(options);
			Driver.getLogger().info("Chrome Driver Stared Successfully");
			}
			else{
				WebDriverManager.chromedriver().setup();
				String 	downloadFilepath = System.getProperty("user.dir")+"\\TestLogs\\Download";
				File dlDirectory = new File(downloadFilepath);
				dlDirectory.mkdir();
				if (!dlDirectory.exists()) {
					dlDirectory.mkdir();
				}else{
					File[] files = dlDirectory.listFiles();
					if(files!=null)
					{
						for (File f : files)
						{
							if(f.delete())
								{
									System.out.println(f.getName()+"  file deleted  "+f.getAbsolutePath());
									//Driver.getLogger().info(f.getName()+"  file deleted  "+f.getAbsolutePath());
								}
								else
								{
									System.out.println("Unable to delete file.....");
								}
							}
						}
					
				}
				

				

				
			// Save Chrome Preferences in Hash Map
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			HashMap<String, Object> contentsetting = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
		
			chromePrefs.put("download.default_directory", downloadFilepath);
			contentsetting.put("multiple-automatic-downloads", 1);
	
			chromePrefs.put("safebrowsing.enabled", "true");
			
			chromePrefs.put("download.prompt_for_download", "false");
			chromePrefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
			chromePrefs.put("credentials_enable_service", false);
			chromePrefs.put("password_manager_enabled", false);

			// Save Chrome Opions
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--no-sandbox");
			options.addArguments("-disable-dev-shm-usage");
			options.addArguments("--test-type");
			options.addArguments("--enable-video-player-chromecast-support");
			
			
	
			
			options.setExperimentalOption("useAutomationExtension", false);
	
			options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
			options.addArguments("disable-infobars");
			options.addArguments("--disable-notifications");
			options.addArguments("--disable-popup-blocking");
			if(View.equalsIgnoreCase("mobile")) {
				HashMap<String, String> mobileEmulation = new HashMap<String, String>();
				//mobileEmulation.put("deviceName", "iPhone X");
				mobileEmulation.put("deviceName", Device);
				options.setExperimentalOption("mobileEmulation", mobileEmulation);
				System.out.print("Mobile Device initiated");
			}
			
			
			//options.setCapability("chrome.switches", Arrays.asList("--disable-local-storage"));
			//options.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap);
			// cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			//options.setCapability(ChromeOptions.CAPABILITY, options);
			//options.setCapability("--enable-video-player-chromecast-support", true);
			//DesiredCapabilities handlSSLErr = DesiredCapabilities.chrome ()       
			options.addArguments("ignore-certificate-errors");
					//WebDriver driver = new ChromeDriver (handlSSLErr);
			LoggingPreferences logPrefschrome = new LoggingPreferences();
			logPrefschrome.enable(LogType.BROWSER, Level.ALL);
			//options.setCapability(CapabilityType.LOGGING_PREFS, logPrefschrome);
			options.setExperimentalOption("prefs", chromePrefs);
			driver = new ChromeDriver(options);
			Driver.getLogger().info("Chrome Driver Stared Successfully");
			}
			Thread.sleep(3000);
			System.out.println("URL is=====?"+URL);
			driver.get(URL);
			driver.manage().window().maximize();
			
			}
			catch (Exception e) {
				// TODO: handle exception
			}
			
				
			return driver;
		}
		
		public static WebDriver StartMobileBrowser(String Device,String URL, String deviceName, String udid) throws InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, MalformedURLException
		{
			if(Device.equalsIgnoreCase("Andriod"))
			{	
			DesiredCapabilities cap = new DesiredCapabilities();
			cap.setCapability("browserName", "Chrome");
			cap.setCapability("platformVersion","11");
			//cap.setCapability("deviceName","sdk_gphone_x86_arm");
			cap.setCapability("deviceName",deviceName);
			//cap.setCapability("udid","emulator-5554");
			cap.setCapability("udid",udid);
			cap.setCapability("platformName","ANDROID");
			driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), cap);
			driver.get(URL);
			}
			else if(Device.equalsIgnoreCase("ios"))
			{
				DesiredCapabilities cap = new DesiredCapabilities();
				cap.setCapability(MobileCapabilityType.PLATFORM_VERSION, udid);
				//cap.setCapability(MobileCapabilityType.PLATFORM_VERSION, "14.4");
			cap.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
			//cap.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 12");
			cap.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
			cap.setCapability(MobileCapabilityType.BROWSER_NAME, "safari"); 
			URL url = new URL("http://127.0.0.1:4723/wd/hub");

			 

			//  IOSDriver driver = new IOSDriver(url, cap);

			RemoteWebDriver driver = new IOSDriver(new URL("http://127.0.0.1:4723/wd/hub"), cap);
			BaseDriver.driver=driver;
			//driver.get("https://jetrails-stg.hydroflask.com");

			driver.get("https://www.hydroflask.com");
			}
			return driver;
		}
		
		public static void closeBrowser()
		{
			driver.quit();
		}
		
		public static WebDriver getDriver()
		{
			return driver;
			
		}
		
		public static void setDriver(WebDriver webdriver)
		{
			 driver=webdriver;
			
		}
		
		
		
}