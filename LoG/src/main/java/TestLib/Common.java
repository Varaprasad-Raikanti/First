package TestLib;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

//import javax.activity.InvalidActivityException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import TestLib.Driver;
import TestLib.Sync;
import Utilities.ExtenantReportUtils;


public class Common {

	private static String os;
	public static boolean[] user = { false, false, false, false, false, false, false, false, false, false };
	private static boolean headlessBrowser = false;
	static Long waitTime=(long) 30;


	public static String browserName() {
		return System.getProperty("browser");
	}
	public static String getOs() {
		return os;
	}

	public static void setOs(String operatingSystem) {
		os = operatingSystem;
	}
	public static String getDownloadPath() {
		String downloadFilepath;
		String[] classname = getCLassName().split("\\.");
		String downloadPath = classname[classname.length - 1];
		downloadFilepath = FileSystems.getDefault().getPath(System.getProperty("user.dir"), "src","Downloads", downloadPath).toString() + File.separator;
		Driver.getLogger().info("Download Path: {}", downloadFilepath);
		return downloadFilepath;
	}

	public static boolean isHeadlessBrowser() {
		return headlessBrowser;
	}

	public static void setHeadlessBrowser(boolean isHeadlessBrowser) {
		headlessBrowser = isHeadlessBrowser;
	}


	public static String getCLassName() {
		String className = Thread.currentThread().getName();
		if (className.startsWith("TestNG") || className.startsWith("main")) {
			Thread.currentThread().setName(getClassNameFromStackTrace());
		}
		return Thread.currentThread().getName();
	}

	private static String getClassNameFromStackTrace() {
		StackTraceElement[] stackTraceElements = new Exception().getStackTrace();
		int i = 0;
		while ((i < stackTraceElements.length)) {
			if (!stackTraceElements[i].getClassName().contains("sun.reflect.NativeMethodAccessorImpl") 
					&& !stackTraceElements[i].getClassName().contains("sun.reflect.NativeConstructorAccessorImpl") 
					&& !stackTraceElements[i].getClassName().contains("sun.reflect.GeneratedMethod"))
				i++;
			else
				break;
		}
		try {
			Class<?> myClass = Class.forName(stackTraceElements[i - 1].getClassName());
			if (myClass.getSuperclass().getName().contains("java.lang.Object")) {
				return stackTraceElements[i - 1].getClassName();
			} else {
				if (myClass.getSuperclass().getName().contains("DataProviderTestBase")) {
					return myClass.getSuperclass().getName();
				}
				if (myClass.getSuperclass().getName().contains("TestBase")) {
					return myClass.getName();
				}
				if (myClass.getSuperclass().getName().contains("ReportingBase")) {
					return myClass.getName();
				}
				return myClass.getSuperclass().getName();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stackTraceElements[i - 1].getClassName();
	}

	/**
	 * returns the browser name based on driver instance
	 * 
	 * @return browser name
	 */

	public static String browserType() {
		if (getDriver() instanceof InternetExplorerDriver)
			return "IE";
		if (getDriver() instanceof ChromeDriver)
			return "GC";
		if (getDriver() instanceof EdgeDriver)
			return "EDGE";
		return "";
	}

	public static void refreshpage() {
		getDriver().navigate().refresh();
		Driver.getLogger().info("Page Refreshed Sucessfully");
	}

	/**
	 * returns the web driver instance
	 * 
	 * @return webdriver
	 */
	public static WebDriver getDriver() {
		return BaseDriver.getDriver();
	}



	public static List<WebElement> findElements(String elemfindBY, String elemfindText) {
		return getDriver().findElements(returnByLocator(elemfindBY, elemfindText));
	}

	public static WebElement findElementBy(String elemfindBY, String elemfindText) {
		return getDriver().findElement(returnByLocator(elemfindBY,elemfindText));
	}

	public static WebElement expandRootElement(WebElement element) {
		return (WebElement) executeJS("return arguments[0].shadowRoot", element);
	}

	public static Object executeJS(String js, Object... args) {
		return ((JavascriptExecutor) getDriver()).executeScript(js, args);
	}

	

	public static WebElement findElement(String elemfindBY, String elemfindText) {
	
		List<WebElement> elements = findElements(elemfindBY, elemfindText);
		for (WebElement element : elements) {
			if (element.isDisplayed() || (browserType().equalsIgnoreCase("EDGE") && element.isEnabled())) {
				return element;
			}
		}
		if (!elements.isEmpty())
			return elements.get(0);
		return findElementBy(elemfindBY, elemfindText);

	}



	public static List<WebElement> isElementPresent(String elemfindBY, String elemfindText) {
		return findElements(elemfindBY, elemfindText);
	}
	
	/**
	 * maximizeImplicitWait: will maximize the currency window and
	 * implicitWait() will helps to delay problems. * implicitWait: will wait
	 * for the respond.
	 * 
	 * @waitTime: provide waiting time in Seconds in HATF_propertices page.
	 * 
	 */
	// **Maximizing Window */
	public static void maximizeImplicitWait() {
		Driver.getLogger().info("Came to maximizeImplicitWait  nethod whcih is siometimes tiking more time to switch");
		java.awt.Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension screenDimensions = new Dimension(ss.width, ss.height);
		Driver.getLogger().info("Time 1");
		try {
			getDriver().manage().window().setSize(screenDimensions);
		} catch (Exception e) {
			Driver.getLogger().info("Window is already Maximized Hence came to catch Exception");
		}
		Driver.getLogger().info("Time 2");
		try {
			getDriver().manage().window().maximize();
		} catch (Exception e) {
			Driver.getLogger().info("Window is already maximised");
		}
		implicitWait();
	}

	/**
	 * implicitWait: will wait for the respond.
	 * 
	 * @waitTime: provide waiting time in Seconds in HATF_propertices page.
	 */
	// ** ImplicitWait */
	public static void implicitWait() {
		Sync.waitImplicit(waitTime);
	}

	/**
	 * switchWindows(): will switch to windows by driver generated id.
	 * 
	 * @Noparameter
	 * 
	 */
	// **Switching Windows*/
	public static void switchWindows() {
		switchWindows(true);
	}

	public static void clickAndtextBoxInput(String elemfindBY, String elemfindText, String value) {
		Sync.waitElementClickable(60, elemfindBY, elemfindText);
		WebElement web = Sync.waitAndReturnWebElement(ExpectedConditions.elementToBeClickable(returnByLocator(elemfindBY, elemfindText)));
		new Actions(getDriver()).moveToElement(web).build().perform();
		web.sendKeys(value);
	}

	public static void switchWindows(boolean maximizeWindow) {
		Set<String> ids = getDriver().getWindowHandles();
		String currentWindowId = null;
	//	Driver.getLogger().info("No Of Window Handles  at start of switchWindows is {}", ids.size());
		for (String id : ids) {
			if (currentWindowId == null) {
				try {
					currentWindowId = getDriver().getWindowHandle();
				} catch (NoSuchWindowException ex) {
					Driver.getLogger().info("getWindowHandle retuned exception , moving to window available");
					ids = getDriver().getWindowHandles();
					if (ids.size() == 1) {
						Driver.getLogger().info("Only One window is existign may be that is Parent Window and swithcing to Parent Window");
						String winHandle = ids.iterator().next();
						getDriver().switchTo().window(winHandle);
						return;
					} else {
						Driver.getLogger().info("No of WidnowHandles in Else condition is : " + ids.size());
						getDriver().switchTo().defaultContent();
						Driver.getLogger().info("Switching to default content as window cannot be determined from multiple child windows");
						return;
					}
				}
			} 

			if (!id.equals(currentWindowId)){
				Driver.getLogger().info("id.equals(currentWindowId)  ELSE condition ins switchWindows");
		//		Driver.getLogger().info("This is the last switching Condition in switchWindows and ID is  {}", id);
				getDriver().switchTo().window(id);
			}
		//	Driver.getLogger().info("Window Details: {}", id);
		}
		if (maximizeWindow)
			maximizeImplicitWait();
	}

	public static String parenWindows() {
		return getDriver().getWindowHandle();
	}

	public static void switchmainWindowsCons() {
		String parentWin = parenWindows();
		Driver.getLogger().info("parentWinparentWinparentWin: {}", parentWin);
		Set<String> ids = getDriver().getWindowHandles();
		Driver.getLogger().info("id size: {}", ids.size());
		for (String id : ids) {
			if (ids.size() > 1 && !id.equals(parentWin)) {
				getDriver().switchTo().window(id);
			}

			if (ids.size() == 1) {
				getDriver().switchTo().window(id);
			}
	//		Driver.getLogger().info("Window Details: {}", id);
		}
	}

	// **Switching Windows*/
	/**
	 * switchWindows:will switch to window by windowName 
	 * 
	 * @param popupName
	 *            -- provide the windowName 
	 */
	public static void switchWindows(String windowName) {
		getDriver().switchTo().window(windowName);
	}

	// **Switching Frame with FrameId */
	/**
	 * switchFrames(id): switch to frame by id
	 * 
	 * @param frameid
	 *            -- provide frameid
	 */
	public static void switchFrames(int frameid) {
		if (frameid > 0) {
			getDriver().switchTo().frame(frameid);
		} else {
			getDriver().switchTo().defaultContent();
		}
		Driver.getLogger().info("Switched to the frame with id : {}", frameid);
	}

	public static void switchFrames(String frameName) {
		getDriver().switchTo().frame(frameName);
		Driver.getLogger().info("Switched to the frame with name : {}", frameName);
	}

	// **Switching Frame with FrameName */
	/**
	 * switchFrames(frameName): switch to frame by frameName or frameC
	 * 
	 * @param frameFindBY
	 *            -- provide frameFindBY
	 * @param frameFindText
	 *            -- provide frameFindText
	 */
	public static void switchFrames(String frameFindBY, String frameFindText)  {
		getDriver().switchTo().frame(findElement(frameFindBY, frameFindText));
		//Driver.getLogger().info("Frame:  {} found successfully.", frameFindText);
	}

	

	/**
	 * txtVerify: will verify the text and return the result in true or false
	 * 
	 * @param text:
	 *            pass expected text.
	 */
	public static boolean txtVerify(String text) {
		return getDriver().findElement(By.cssSelector("BODY")).getText().contains(text);
	}


	public static String getText(String elemfindBY, String elemfindText)  {
		String getText = null;
		getText = findElement(elemfindBY,elemfindText).getText();
		//Driver.getLogger().action("the Text for the given element is : " + getText);
		return getText;
	}


	


	public static String textBoxVerify(String elemfindBY, String elemfindText)  {
		return textBoxVerify(elemfindBY,elemfindText, false);
	}


	public static String textBoxVerify(String elemfindBY, String elemfindText, boolean isDisabled) {
		String value = findElement(elemfindBY,elemfindText).getText();
	//	Driver.getLogger().action("Value is :: " + value);
		if (StringUtils.isNotEmpty(value)) {
			if (!isDisabled)
				Sync.waitElementClickable(elemfindBY,elemfindText);
			String actual = findElement(elemfindBY,elemfindText).getAttribute("value");
			//Driver.getLogger().action("actualText:  " + actual + "  userText:  " + value);
			if (value.equals(actual)) {
			//	Driver.getLogger().action(value + " Text found in text box");
				return "Pass";
			}
		}
		return "Fail";
	}
	
	public static String textBoxVerify(String elemfindBY, String elemfindText, String value) {
		if (StringUtils.isNotEmpty(value)) {
				Sync.waitElementClickable(elemfindBY,elemfindText);
			String actual = findElement(elemfindBY,elemfindText).getAttribute("value");
			if (value.equals(actual)) {
			//	Driver.getLogger().action(value + " Text found in text box");
				return "Pass";
			}
		}
		return "Fail";
	}

	
	

	public static void clickElement(String elemfindBY, String elemfindText)  {
		clickElement(elemfindBY,elemfindText, true);
	}

	public static void clickElementWithoutWait(String elemfindBY, String elemfindText)  {
		clickElementWithoutWait(elemfindBY,elemfindText, true);
	}

	private static void clickElementWithoutWait(String elemfindBY, String elemfindText, boolean retryflag)  {
		try {
			WebElement element = findElement(elemfindBY,elemfindText);
			if (element.isDisplayed() || (browserType().equalsIgnoreCase("EDGE") && element.isEnabled())) {
				try {
					element.click();
				} catch (WebDriverException we) {
					scrollToElementAndClick(element);
				}
			}
			else
				clickOnHiddenElement(element);
		} catch (StaleElementReferenceException e) {
			if (retryflag)
				clickElementWithoutWait(elemfindBY,elemfindText, false);
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	public static void clickElement(String elemfindBY, String elemfindText, boolean retryflag)  {
		clickElementWithoutWait(elemfindBY,elemfindText, retryflag);
	}

	public static void clickElement(By by)  {
		WebElement element = findElement(by);
		if (element.isDisplayed() || (browserType().equalsIgnoreCase("EDGE") && element.isEnabled())) {
			element.click();
		} else
			clickOnHiddenElement(element);
	}

	public static void clickOnHiddenElement(String elemfindBY, String elemfindText)  {
		clickOnHiddenElement(findElement(elemfindBY, elemfindText));
	}

	public static void clickOnHiddenElement(WebElement element) {
		executeJS("var el=arguments[0];  if(document.createEvent){" + "var evObj = document.createEvent('MouseEvents');" + "evObj.initEvent('click', true, false); setTimeout(function() { el.dispatchEvent(evObj); }, 10);} " + "else if(document.createEventObject) { setTimeout(function() {el.fireEvent('onclick');}, 10);}", element);
		Driver.getLogger().info("Click on hidden element completed.");

	}

	public static void clickCheckBox(String elemfindBY, String elemfindText, boolean isChecked)  {
		WebElement checkBox = findElement(elemfindBY,elemfindText);
		if (checkBox.isSelected() != isChecked) {
			checkBox.click();
		}

	}



	public static void clickCheckBox(String elemfindBY, String elemfindText)  {
		WebElement checkBox = findElement(elemfindBY,elemfindText);
		if (!checkBox.isSelected()) {
			checkBox.click();
		}
		Driver.getLogger().info(checkBox.getText() + "Button click success");

	}


	public static void unCheckBox(String elemfindBY, String elemfindText)  {
		Sync.waitElementClickable(elemfindBY,elemfindText);
		WebElement checkBox = findElement(elemfindBY,elemfindText);
		if (checkBox.isSelected()) {
			checkBox.click();
		}
		Driver.getLogger().info(checkBox.getText() + "Button click success");

	}

	public static boolean verifyCheckBox(String elemfindBY, String elemfindText, boolean isChecked)  {
		return verifyCheckBox(elemfindBY, elemfindText, isChecked, false);
	}

	
	public static Boolean verifyCheckBox(String elemfindBY, String elemfindText, boolean isChecked, boolean isDisabled)  {
		if (!isDisabled)
			Sync.waitElementClickable(elemfindBY,elemfindText);
		WebElement checkBox = findElement(elemfindBY,elemfindText);
		if (checkBox.isSelected() != isChecked) {
			return false;
		}
		return true;
	}

	

	public static boolean isCheckBoxSelected(String elemfindBY, String elemfindText)  {
		String iselemCBChecked = findElement(elemfindBY,elemfindText).getAttribute("checked");
		return iselemCBChecked != null && "true".equals(iselemCBChecked);
	}



	
	

	public static void deselectdropdown(String elemfindBY, String elemfindText, String selectvalue, SelectBy selectBy)  {
		Select s = new Select(findElement(elemfindBY, elemfindText));

		if (selectBy == SelectBy.TEXT)
			s.deselectByVisibleText(selectvalue);
		else if (selectBy == SelectBy.VALUE)
			s.deselectByValue(selectvalue);
		else if (selectBy == SelectBy.INDEX)
			s.deselectByIndex(Integer.parseInt(selectvalue));

	}

	public static void dropdown(String elemfindBY, String elemfindText, SelectBy selectBy,String selectvalue)  {
		Select s = new Select(findElement(elemfindBY,elemfindText));
		if (selectBy == SelectBy.TEXT)
			s.selectByVisibleText(selectvalue);
		else if (selectBy == SelectBy.VALUE)
			s.selectByValue(selectvalue);
		else if (selectBy == SelectBy.INDEX)
			s.selectByIndex(Integer.parseInt(selectvalue));

	}

	

	public static boolean verifyDropdown(String elemfindBY, String elemfindText, SelectBy selectBy, String expected)  {
		return verifyDropdown(elemfindBY, elemfindText, selectBy, expected, false);
	}

	

	public static boolean verifyDropdown(String elemfindBY, String elemfindText, SelectBy selectBy, String expected, boolean isDisabled)  {
		//Driver.getLogger().info("Expected : {}", expected);
		if (StringUtils.isNotEmpty(expected)) {
			if (!isDisabled)
				Sync.waitElementClickable(elemfindBY,elemfindText);
			Select s = new Select(findElement(elemfindBY,elemfindText));
			if (selectBy == SelectBy.TEXT) {
				String actual = s.getFirstSelectedOption().getText();
			//	Driver.getLogger().info("Actual: {} Expected: {}", actual, expected);
				if (expected.equals(actual)) {
					Driver.getLogger().info("Verify dropdown successful.");
					return true;
				}
				return false;
			}
		}
		return true;
	}


	
	// **Alerts*/
	/**
	 * alerts: will get the text on alert compare with expected text and click
	 * on OK or Cancel.
	 * 
	 * @param alertActions:
	 *            pass "OK" for OK and Yes. provide cancel or No to dismiss the
	 *            alert.
	 * @param alerttext:
	 *            Expected text on alert.
	 */
	public static void alerts(String alertActions)  {
		Sync.waitAlert(30);
		Alert alert = getDriver().switchTo().alert();
		if ("OK".equalsIgnoreCase(alertActions))
			alert.accept();
		else
			alert.dismiss();
	}

	// **drag and drop**//
	/**
	 * dragdrop: will drag the element from source and drag it on destination on
	 * same page.
	 * 
	 * @param ddSourceeElemfindBy:
	 *            source element find by, like by "id", "name", "className",
	 *            "xpath".
	 * @param ddSourcename:
	 *            source element name
	 * @param ddTargetElemFindBy:
	 *            target element find by, like by "id", "name", "className",
	 *            "xpath".
	 * @param ddTargetsname:
	 *            target element name
	 */
	public static void dragdrop(String ddSourceeElemfindBy, String ddSourcename, String ddTargetElemFindBy, String ddTargetsname)  {
		dragdrop(findElement(ddSourceeElemfindBy, ddSourcename), ddTargetElemFindBy, ddTargetsname);
	}

	

	public static void dragdrop(WebElement ddSourcename, String ddTargetElemFindBy, String ddTargetsname)  {
		Sync.waitPageLoad();
		new Actions(getDriver()).dragAndDrop(ddSourcename, findElement(ddTargetElemFindBy, ddTargetsname)).build().perform();
		Sync.waitPageLoad();
	}

	/**
	 * mouseOver: will get the mouse over action on a element.
	 * 
	 * @param elemfindBY:
	 *            find element by "id", "name", "className", "xpath".
	 * @param elemfindText
	 *            find element text.
	 */
	
	public static void mouseOver(String elemfindBY, String elemfindText)  {
		Actions ac = new Actions(getDriver());
		ac.moveToElement(findElement(elemfindBY,elemfindText));
		ac.perform();
	}

	/**
	 * mouseOverClick: will get the mouse over action on a element and click on
	 * that element.
	 * 
	 * @param elemfindBY:
	 *            element by "id", "name", "className", "xpath".
	 * @param elemfindText:
	 *            element text
	 */
	public static void mouseOverClick(String elemfindBY, String elemfindText)  {
		Actions ac = new Actions(getDriver());
		WebElement tr = findElement(elemfindBY, elemfindText);
		ac.moveToElement(tr).click(tr).build().perform();
	}

	// **Capturing Screenshots**//
	/**
	 * screenShot: will capture the screen shot and save the screen shot a jpg
	 * file.
	 * 
	 * @param saveAs:
	 *            save the file on the specified location
	 * 
	 */

	public static void screenShot(String saveAs) {
		File scrFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir") + "/TestLogs/screenShots/" + (saveAs +Utilities.File.GetDateTime()) + ".jpg"));
		} catch (IOException e) {
		Driver.getLogger().error(e);
		}
		Driver.getLogger().info("screenshot saved successfully");
	}
	
	public static String getscreenShot(String screnShotName) {
		String filePath=System.getProperty("user.dir") + "/TestLogs/screenShots/" + (screnShotName +Utilities.File.GetDateTime()) + ".jpg";
		File scrFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(filePath));
		} catch (IOException e) {
		Driver.getLogger().error(e);
		}
		Driver.getLogger().info("screenshot saved successfully");
		return filePath;
		
	}
	
	public static String getscreenShotPathforReport(String screnShotName) {
		String screenName=screnShotName +Utilities.File.GetDateTime();
		String filePath=System.getProperty("user.dir") + "/TestLogs/ExtentReport/" +screenName+ ".jpg";
		File scrFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(filePath));
		} catch (IOException e) {
		Driver.getLogger().error(e);
		}
		Driver.getLogger().info("screenshot saved successfully");
		return screenName+ ".jpg";
		
	}

	public static void fileUpLoad(String elemfindBY, String elemfindText,String filepath) {
		int retryCount = 0;
		WebElement fileInput = findElement(elemfindBY,elemfindText);
		while (retryCount < 3) {
			Sync.processSync(4000);
			fileInput.sendKeys(filepath);
			retryCount++;
		}
	}


	/**
	 * closeCurrentWindow: will close the currency window.
	 */

	// CloseCurent Window
	public static void closeCurrentWindow() {
		getDriver().close();
		//Driver.getLogger().action("Window Closed Successfully");
	}

	// CloseAllWindows
	/**
	 * closeAll: will closed all the windows and quit the selenium driver.
	 */
	public static void closeAll() {
		        
				getDriver().quit();
				BaseDriver.setDriver(null);
				
	}

	

	/**
	 * quitProcess: will kill the selenium driver based on driver.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */

	public static void quitProcess()  {

		String processName = null;
		String browservar = browserType();
		//Driver.getLogger().info("browservar for quit process is {}", browservar);

		switch (browservar) {
		case "GC":
			getbrowserlog();
			getDriver().quit();
			Driver.getLogger().info("GC Broswerkilled");
			break;
		case "GCHeadless":
			getbrowserlog();
			getDriver().quit();
			Driver.getLogger().info("GCHeadless Broswerkilled");
			break;
		case "FF":
			processName = "firefox.exe"; // firefox process
			Driver.getLogger().info("FF Broswerkilled");
			break;
		default: 
			break;
		}
	}

	private static void getbrowserlog() {
		LogEntries logEntries = getDriver().manage().logs().get(LogType.BROWSER);
		//Driver.getLogger().error("***********************Browser Console Log***********************");
		for (LogEntry entry : logEntries) {
		//	Driver.getLogger().error("{}\t{}\t{}", new Date(entry.getTimestamp()), entry.getLevel(), entry.getMessage());
		}
	}



	public static String getTagName(String elemfindBY, String elemfindText, String tagName) {
		return findElement(elemfindBY, elemfindText).getAttribute(tagName);

	}

	public static int getElementCount(String elemfindBY, String elemfindText) {
		return findElements(elemfindBY, elemfindText).size();
	}

	public enum SelectBy {
		TEXT, VALUE, INDEX
	}

	public static void jsClickElement(String id) {
		//Driver.getLogger().info("JS Click Script: document.getElementById(arguments[0]).click();",id);
		executeJS("document.getElementById('" + id + "').click();");
	}


	

	public static void pressTabKey(String elemfindBY, String elemfindText)  {
		findElement(elemfindBY, elemfindText).sendKeys(Keys.TAB);
	}

	public static void pressTabKey(WebElement element) {
		element.sendKeys(Keys.TAB);
	}

	public static int windowCounts() {
		Set<String> ids = getDriver().getWindowHandles();
		return ids.size();
	}

	public static boolean isAlertPresent() {
		try {
			getDriver().switchTo().alert();
			return true;
		} catch (Exception e) {
			return false;
		}
	}



	public static List<WebElement> getAllOption(String elemfindBY, String elemfindText)  {
		Sync.waitElementClickable(elemfindBY,elemfindText);
		Select select = new Select(findElement(elemfindBY,elemfindText));
		return select.getOptions();
	}



	public static boolean isElementDisplayed(String elemfindBY, String elemfindText)  {
		if (!findElements(elemfindBY,elemfindText).isEmpty()) 
			return findElement(elemfindBY,elemfindText).isDisplayed();
		return false;

	}


	public static boolean isElementEnabled(String elemfindBY, String elemfindText)  {
		if (!findElements(elemfindBY,elemfindText).isEmpty())
			return findElement(elemfindBY,elemfindText).isEnabled();
		return false;
	}

	public static String getValue(String findBY, String findText)  {
		return findElement(findBY, findText).getAttribute("value");
	}


	public static void javascriptclickElement(String elemfindBY, String elemfindText)  {
		try {
			WebElement element = findElement(elemfindBY,elemfindText);
			if (element.isDisplayed() || (browserType().equalsIgnoreCase("EDGE") && element.isEnabled()))
				executeJS("var el=arguments[0]; setTimeout(function() { el.click(); }, 10);", element);
			else
				clickOnHiddenElement(element);
		} catch (Exception e) {
			//Driver.getLogger().exception(elemfindBY,elemfindText.getException(), elemfindBY,elemfindText.getValue());
			throw e;
		}
	}

	public static void actionsKeyPress(Keys keyCode) {
		new Actions(getDriver()).sendKeys(keyCode).build().perform();
	}

	

	public static void doubleClick(String elemfindBY, String elemfindText)  {
	/*	if (browserName().equals("FF")) {
			executeJS("var event = new MouseEvent('dblclick', {\r\n 'view': window,\r\n" + "    'bubbles': true,\r\n" + "    'cancelable': true\r\n" + "  });arguments[0].dispatchEvent(event);", findElement(elemfindBY,elemfindText));
			Actions doubleClick = new Actions(getDriver());
			doubleClick.moveToElement(findElement(elemfindBY,elemfindText)).doubleClick().build().perform();
		} else {*/
			Actions doubleClick = new Actions(getDriver());
			doubleClick.moveToElement(findElement(elemfindBY,elemfindText)).doubleClick().build().perform();
		//}
	}

	public static void switchWindowsAfterClick(String elemfindBY, String elemfindText)  {
		String mainWindow = getDriver().getWindowHandle();
		Set<String> windows = new LinkedHashSet<>();
		windows.add(mainWindow);
		clickElement(elemfindBY, elemfindText);
		Sync.waitPageLoad();
		windows = getDriver().getWindowHandles();
		while (windows.size() <= 1) {
			windows = getDriver().getWindowHandles();
		}
		getDriver().switchTo().window(mainWindow);
		windows.remove(mainWindow);
		for (String id : windows) {
			getDriver().switchTo().window(id);
		}
	}

	public static void switchWindowsSingleClick(String elemfindBY, String elemfindText)  {
		String mainWindow = getDriver().getWindowHandle();
		Set<String> windows = new LinkedHashSet<>();
		windows.add(mainWindow);
		clickElement(elemfindBY, elemfindText);
		Sync.waitPageLoad();
		windows = getDriver().getWindowHandles();
		getDriver().switchTo().window(mainWindow);
		getDriver().close();
		windows.remove(mainWindow);
		for (String id : windows) {
			getDriver().switchTo().window(id);
		}
	}

	
	public static String getCurrentDate() {
		return new SimpleDateFormat("MM/dd/yyyy").format(new Date());
	}


	public static String getSelectedOption(String elemfindBY, String elemfindText)  {
		Select select = new Select(findElement(elemfindBY,elemfindText));
		return select.getFirstSelectedOption().getText();
	}
	
	public static int genrateRandomNumber()
	{
		Random rand = new Random(); 
        int randNumber = rand.nextInt(100000); 
        return randNumber;
	}
	
	public static String genrateRandomEmail(String email)
	{
		return email.split("@")[0]+genrateRandomNumber()+"@"+email.split("@")[1];
	}

	public static boolean isElementClickable(long waitSeconds, By by)  {
		try {
			getDriver().manage().timeouts().implicitlyWait(waitSeconds, TimeUnit.SECONDS);
			Sync.waitElementClickable(waitSeconds, by);
			return true;
		} catch (Exception ex) {
			return false;
		} finally {
			maximizeImplicitWait();
		}
	}

	


	private static By returnByLocator(String elemfindBY, String elemfindText) {
		By by = null;
		switch (elemfindBY) {
		case "id":
			by = By.id(elemfindText);
			break;
		case "name":
			by = By.name(elemfindText);
			break;
		case "className":
			by = By.className(elemfindText);
			break;
		case "linkText":
			by = By.linkText(elemfindText);
			break;
		case "xpath":
			by = By.xpath(elemfindText);
			break;
		case "css":
			by = By.cssSelector(elemfindText);
			break;
		case "pLinkText":
			by = By.partialLinkText(elemfindText);
			break;
		case "tagName":
			by = By.tagName(elemfindText);
			break;	
		default : 
			break;
		}
		return by;

	}

	



	public static void waitAndClick(String elemfindBY, String elemfindText)  {
		Actions action = new Actions(getDriver());
		action.moveToElement(Sync.waitAndReturnWebElement(returnByLocator(elemfindBY,elemfindText))).click().build().perform();
	}

	public static void waitAndClick(By by)  {
		Sync.waitAndReturnWebElement(ExpectedConditions.elementToBeClickable(by)).click();
	//	Driver.getLogger().info("Clicked on {} Successfully", by);
	}

	public static String getTextBoxValue(String elemfindBY, String elemfindText)  {
		Sync.waitElementClickable(elemfindBY, elemfindText);
		return findElement(elemfindBY, elemfindText).getAttribute("value");
	}

	public static void scrollToElementAndClick(String elemfindBY, String elemfindText)  {
		scrollToElementAndClick(returnByLocator(elemfindBY, elemfindText));
	}

	public static void scrollToElementAndClick(By by)  {
		Sync.waitElementClickable(90, by);
		WebElement element = getDriver().findElement(by);
		scrollToElementAndClick(element);
	}

	public static void scrollToElementAndClick(WebElement element) {
		scrollIntoView(element);
		if (!element.isSelected()) {
			executeJS("arguments[0].click();", element);
		}
	}

	public static void scrollToElementAndUnCheck(String elemfindBY, String elemfindText)  {
		scrollToElementAndUnCheck(returnByLocator(elemfindBY, elemfindText));
	}

	public static void scrollToElementAndUnCheck(By by)  {
		Sync.waitElementClickable(90, by);
		WebElement element = getDriver().findElement(by);
		scrollToElementAndUnCheck(element);
	}

	public static void scrollToElementAndUnCheck(WebElement element) {
		scrollIntoView(element);
		if (element.isSelected()) {
			executeJS("arguments[0].click();", element);
		}
	}

	
	public static void javascriptTextBoxInput(String elemfindBY, String elemfindText, String longstring)  {
		WebElement inputField = findElement(elemfindBY,elemfindText);
		if (browserType().equalsIgnoreCase("IE")) {
			executeJS("arguments[0].setAttribute('value', '" + longstring + "')", inputField);
		} else {
			executeJS("arguments[0].innerHTML='" + longstring + "';", inputField);
		}
	}

	public static boolean isValueInDropdown(String elemfindBY, String elemfindText, String value)  {
		Sync.waitElementClickable(elemfindBY, elemfindText);
		Select s = new Select(findElement(elemfindBY, elemfindText));
		List<WebElement> op = s.getOptions();
		for (WebElement w : op) {
			if (w.getText().equals(value))
				return true;
		}
		return false;
	}

	public static int dropdownOptionsCount(String elemfindBY, String elemfindText)  {
		Sync.waitElementClickable(elemfindBY, elemfindText);
		Select s = new Select(findElement(elemfindBY, elemfindText));
		return s.getOptions().size();
	}

	public static boolean isRowPresentInWFPEmpGrid(String searchTexts, String columnIndex, String gridName) {
		return (boolean) executeJS("var searchedRows = [];" + "var searchText = \'" + searchTexts + "\';" + "var gridObj=" + gridName + ";" 
				+ "var searchTextArray = searchText.split('||@@||');" + "for(var i=0; i<searchTextArray.length; i++){" + "searchedRows = gridObj.grid.findCell(searchTextArray[i], " + columnIndex + ");" 
				+ "for(var j=0; j<searchedRows.length; j++){ if(gridObj.grid.cellById(searchedRows[j][0], 2).getTitle() == searchText){" + "return true;} } return false;}");
	}

	public static List<String> getAttribute(String findBy, String findValue, String nameSplitBY, String idSplitBy) {
		List<WebElement> elements = new ArrayList<>();
		List<WebElement> idElements = new ArrayList<>();
		if (findBy.equals("xpath")) {
			elements = getDriver().findElements(By.xpath(findValue));
			idElements = getDriver().findElements(By.xpath(findValue + "//Input"));
		}
		List<String> ar = new ArrayList<>();
		for (int i = 0; i < idElements.size(); i++) {
			String attributeName = null;
			if (nameSplitBY != null)
				attributeName = elements.get(i).getText().split(nameSplitBY)[0];
			else
				attributeName = elements.get(i).getText();
			String attributeId = null;
			if (idSplitBy != null)
				attributeId = idElements.get(i).getAttribute("id").split(idSplitBy)[0];
			else
				attributeId = idElements.get(i).getAttribute("id");
			ar.add(attributeName + "||@@||" + attributeId);
		}
		return ar;
	}


	public static boolean isElementDisplayedonPage(long waitSeconds, String elemfindBY, String elemfindText)  {
		boolean isElementPresent = true;
		try {
			getDriver().manage().timeouts().implicitlyWait(waitSeconds, TimeUnit.SECONDS);
			Sync.waitElementClickable(waitSeconds, elemfindBY,elemfindText);
		} catch (Exception ex) {
			isElementPresent = false;
		} finally {
			maximizeImplicitWait();
		}
		return isElementPresent;
	}

	public static void switchToDefault() {
		getDriver().switchTo().defaultContent();
	}

	


	public static void performScrollClick(String value, By toClick)  {
		performScrollClick(value, toClick, null);
	}

	public static void performScrollClick(String value, By toClick, By toWait)  {
	
				scrollToElementAndClick(toClick);
				if (toWait != null) {
					Sync.waitElementClickable(90, toWait);
				}
			 else {
				scrollToElementAndUnCheck(toClick);
			}
		}
	

	
	
	public static boolean checkBoxIsSelected(String elemfindBY, String elemfindText, boolean isDisabled)  {
		if (!isDisabled)
			Sync.waitElementClickable(elemfindBY, elemfindText);
		return findElement(elemfindBY, elemfindText).isSelected();
	}

	public static WebElement findElement(By by) {
		List<WebElement> elements = findElements(by);
		if (elements.isEmpty())
		//	Driver.getLogger().info("Element with {} not found", by);
		for (WebElement element : elements) {
			if (element.isDisplayed() || (browserType().equalsIgnoreCase("EDGE") && element.isEnabled())) {
				return element;
			}
		}
		return elements.get(0);
	}

	public static List<WebElement> findElements(By by) {
		return getDriver().findElements(by);
	}


	public static String getTextBoxInput(String elemfindBY, String elemfindText) {
		Sync.waitAndReturnWebElement(ExpectedConditions.elementToBeClickable(returnByLocator(elemfindBY, elemfindText)));
		return findElement(elemfindBY, elemfindText).getAttribute("value");
	}


	public static void scrollIntoView(WebElement element) {
		executeJS("arguments[0].scrollIntoView();", element);
	}
	
	public static void scrollIntoView(String elemfindBY, String elemfindText) {
		WebElement element=findElement(elemfindBY, elemfindText);
		executeJS("arguments[0].scrollIntoView();", element);
	}

	public static void scrollTo(WebElement element, int x, int y) {
		executeJS("arguments[0].scrollTo(arguments[1],arguments[2]);", element, x, y);
	}

	
	
	public static void clickWithPostCondition(String findBy, String locator, boolean postShouldDisplay)  {
		Sync.waitElementClickable(60, findBy, locator);
		int attempt = 0;
		while (attempt < 5) {
			try {
				clickElement(findBy, locator);
				Sync.waitPageLoad();
				boolean actualDisplay = isElementDisplayedonPage(5, findBy, locator);
				if (actualDisplay == postShouldDisplay) {
			//		Driver.getLogger().info("Attempt: {} -Post condition for the click successful", attempt);
					break;
				}

			} catch (Exception e) {
				//Driver.getLogger().exception(e);
			}
			attempt++;
		}
	}


	public static List<WebElement> getVisibleElements(String findBy, String locator) {
		List<WebElement> visibleElements = new ArrayList<WebElement>();
		WebDriverWait wait = new WebDriverWait(getDriver(), 60);
		wait.withTimeout(waitTime, TimeUnit.SECONDS);
		wait.ignoring(NoSuchElementException.class, StaleElementReferenceException.class);

		List<WebElement> elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(locator)));

		for (WebElement element : elements) {
			try {
				if (element.isDisplayed() || (browserType().equalsIgnoreCase("EDGE") && element.isEnabled())) {
					visibleElements.add(element);
				}
			} catch (StaleElementReferenceException st) {
				Driver.getLogger().info("getVisibleElements:- Stale Exception Occured..now recovering.. '");
				getVisibleElements(findBy, locator);
			}
		}
		return visibleElements;
	}



	public static void textBoxInputClear(String elemfindBY, String elemfindText)  {
		WebElement web = Sync.waitAndReturnWebElement(ExpectedConditions.elementToBeClickable(returnByLocator(elemfindBY,elemfindText)));
		web.clear();
	}

	public static void switchToTopFrame()  {
		getDriver().switchTo().defaultContent();
		Sync.waitAndSwitchToFrame("id", "topnavFrameId");
	}
	
	public static void textBoxInput(WebElement textbox, String value)  {
	
			textbox.clear();
			textbox.sendKeys(value);
	}

	
	public static String getCurrentDate(String format) {
		String currentDate = new SimpleDateFormat(format).format(new Date());
		//Driver.getLogger().info("Current date is: {}.", currentDate);
		return currentDate;
	}

	public static boolean checkBoxIsSelected(String elemfindBY, String elemfindText)  {
		Sync.waitElementClickable(elemfindBY, elemfindText);
		return findElement(elemfindBY, elemfindText).isSelected();
	}

	public static boolean isElementVisibleOnPage(long waitSeconds, String elemfindBY, String elemfindText)  {

		boolean isElementPresent = true;
		try {
			Sync.waitForWebElement(waitSeconds, ExpectedConditions.elementToBeClickable(returnByLocator(elemfindBY, elemfindText)));
		} catch (Exception ex) {
			isElementPresent = false;
		}
		return isElementPresent;

	}

	public static void rightClick(String elemFindBy, String elemFindText)  {
		Actions action = new Actions(getDriver()).contextClick(findElement(elemFindBy, elemFindText));
		action.build().perform();
	}

	
	public static void clickEscape() {
		Actions action = new Actions(getDriver());
		action.sendKeys(Keys.ESCAPE).build().perform();
	}

	public static String getDropdownSelectedValue(String dropDownId) {
		return (String) executeJS("var indexValue = document.getElementById('" + dropDownId + "').selectedIndex;" + "var textValue = document.getElementById('" + dropDownId + "').options[indexValue].text;" + "return textValue;");
	}

	public static void openUrlInNewTabAlreadyLogin() {
		getDriver().findElement(By.cssSelector("body")).sendKeys(Keys.chord(Keys.CONTROL + "n"));
		executeJS("window.open()");
		List<String> tabs = new ArrayList<String>(getDriver().getWindowHandles());
		getDriver().switchTo().window(tabs.get(1));
		getDriver().get(System.getProperty("url"));
	}

	public static void openNewTab() {
		getDriver().findElement(By.cssSelector("body")).sendKeys(Keys.chord(Keys.CONTROL + "n"));
		executeJS("window.open()");
		List<String> tabs = new ArrayList<String>(getDriver().getWindowHandles());
		getDriver().switchTo().window(tabs.get(1));
	}
	
	public static void switchToSecondTab() {
		WebDriver driver = getDriver();
		List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
	}

	public static void switchToFirstTab() {
		WebDriver driver = getDriver();
		List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(0));
	}

	public static void textBoxInput(String elemfindBY, String elemfindText, String value) throws Exception  {
		try {
			WebElement web;
				web = findElement(elemfindBY,elemfindText);
				if (browserType().equalsIgnoreCase("EDGE"))
					new WebDriverWait(getDriver(), 30).until(ExpectedConditions.presenceOfElementLocated(returnByLocator(elemfindBY,elemfindText)));
				else
					new WebDriverWait(getDriver(), 30).until(ExpectedConditions.elementToBeClickable(web));
				web.clear();
				web.sendKeys(value);
				String entered = web.getAttribute("value");
				if (!entered.equals(value)) {
					web.clear();
					web.sendKeys(value);
				}
			
		} catch (Exception e) {
			throw e;
		}
	}

	public static String getCurrentLocale() {
		return (String) executeJS("window.navigator.userLanguage || window.navigator.language;");
	}

	

	public static void clickElementStale(String elemfindBY, String elemfindText) {
		int timelimit = 0;
		boolean elementavailable = false;
		while (!elementavailable) {
			if (timelimit > 3) {
		//		Driver.getLogger().exception(elemfindBY,elemfindText.getException());
				break;
			}
			try {
				if (isElementDisplayed(elemfindBY,elemfindText)) {
					clickElement(elemfindBY,elemfindText);
					elementavailable = true;
				}
			} catch (Exception e) {
		//		Driver.getLogger().exception(elemfindBY,elemfindText.getException() + "\t\n" + e.getMessage());
			}
			timelimit++;
		}
	}

	

	public static void isElementNotDisplayed(String elemfindBY, String elemfindText)  {
		Sync.waitImplicit(3);
		if (!findElements(elemfindBY, elemfindText).isEmpty()) {
			Driver.getLogger().info("Element is displayed");
			Assert.fail();
		}
		implicitWait();
	}

	
	
	public static String destFilepath(String destFile) {
		String s1 = getDownloadPath() + destFile + ".xlsx";
		File directory = new File(getDownloadPath());
		File[] files = directory.listFiles();
		for (File f : files) {
			Driver.getLogger().info(f.getAbsolutePath());
			if (f.getAbsolutePath().contains(destFile)) {
				s1 = f.getAbsolutePath();
				break;
			}
		}
		return s1;
	}

	
	public static void acceptAlert(long timeOutInSeconds)  {
		try {
			Sync.waitCustomtAlert(timeOutInSeconds);
			if (isAlertPresent()) {
				getDriver().switchTo().alert().accept();
			}
		} catch (Exception e) {
			//Driver.getLogger().exception(e);
		}
		
	}

	

	public static void alert(long timeOutInSeconds, String action){
		try {
			Sync.waitAlert(timeOutInSeconds);
			if (isAlertPresent()) {
				alerts(action);
			}
		} catch (Exception e) {
			Driver.getLogger().info("Exception occured while handling alert. Ignoring..");
		} 
	}

	public static void textBoxInputAndVerify(String elemfindBY, String elemfindText, String value) throws Exception {
		textBoxInput(elemfindBY, elemfindText, value);
		for (int i = 0; i < 5; i++) {
			String verifyResult = textBoxVerify(elemfindBY, elemfindText, value);
			if ("Pass".equalsIgnoreCase(verifyResult)) {
			//	Driver.getLogger().info("Result of text verification: {}", verifyResult);
				break;
			} else {
				findElement(elemfindBY, elemfindText).clear();
				textBoxInput(elemfindBY, elemfindText, value);
			}
		}

	}
	
	public static String getAlertText() {
		Alert alert = getDriver().switchTo().alert();
		return (alert.getText().trim());
	}
   public static String getCurrentURL()
   {
	return  getDriver().getCurrentUrl();
	   
   }
   public static String getPageTitle()
   {
	   
	return  getDriver().getTitle();
	   
   }
   
   public static  void oppenURL(String URL)
   {
	   
      getDriver().get(URL);
	   
   }
   
   public static void validating_URL_PageTitle(String URL,String pagetitle){
	  Assert.assertTrue(getCurrentURL().equals(URL)&&getPageTitle().equals(pagetitle));
	   
	  
   }
   
	public static void dismissAlert() {
		alert(Sync.waitSeconds, "DISMISS");
	}
	
	public static void assertionCheckwithReport(String actualvalue,String expectedvalue,String actualResult,String expectedResult,String FailedMessage)
	{
		try{
		Assert.assertEquals(actualvalue, expectedvalue);
		ExtenantReportUtils.addPassLog(expectedResult, actualResult, Common.getscreenShotPathforReport(expectedResult));
		}catch (Exception |Error e) {
			ExtenantReportUtils.addFailedLog(expectedResult, FailedMessage, Common.getscreenShotPathforReport(expectedResult));
			Assert.fail();
			// TODO: handle exception
		}
		
	}
	
	public static void assertionCheckwithReport(boolean status,String actualResult,String expectedResult,String FailedMessage)
	{
		if(status){
		ExtenantReportUtils.addPassLog(expectedResult, actualResult, Common.getscreenShotPathforReport(expectedResult));
		}
		else{
		ExtenantReportUtils.addFailedLog(expectedResult, FailedMessage, Common.getscreenShotPathforReport(expectedResult));
		Assert.fail();
		}
	}
	
	
	public static void assertionCheckwithReport(boolean status,String description,String expectedResult,String actualResult,String FailedMessage)
	{
		if(status){
		ExtenantReportUtils.addPassLog(description, expectedResult, actualResult, Common.getscreenShotPathforReport(expectedResult));
		}
		else{
		ExtenantReportUtils.addFailedLog(description, expectedResult, FailedMessage, Common.getscreenShotPathforReport(expectedResult));
		Assert.fail();
		}
	}
	
	
	
	
}