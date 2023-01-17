package TestLib;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

import Utilities.log4j;



public class Sync  {

	final public static log4j logger = Driver.getLogger();
	public static int waitSecond = 30;
	public static WebDriverWait wait; 
	public static final int miniSliceOfWaitSeconds = 10;
	static {
		WebDriver webDriver = Common.getDriver();
		if(webDriver == null){
			Driver.getLogger().info("This might be an api case which never initialized a driver. Hence skipping web driver wait initialization to prevent ExceptionInInitializerError");
		}else{
			wait = new WebDriverWait(Common.getDriver(), waitSecond);
		}		
	}
	/**
	 * it will wait to load the Page completely for a given time;
	 * 
	 * @param waitSeconds
	 */
	public static void waitPageLoad(long time) {
		waitPageLoad();
	}
	public static void waitPageLoad()  {
		try{
			Common.getDriver().manage().timeouts()
			.pageLoadTimeout(30, TimeUnit.SECONDS);}catch(Exception e){
				Driver.getLogger().info("waitPageLoad  is throwing exception ");
			}
		logger.info("Sync waitPageLoad Completed successfully");
	}
	
	

	/**
	 * set ImplicitWait time.
	 * 
	 * @param waitSeconds
	 */
	public static void waitImplicit(long waitSeconds) {
		Common.getDriver().manage().timeouts().implicitlyWait(waitSeconds, TimeUnit.SECONDS);
		Sync.logger.info("Sync waitImplicit Completed successfully");
	}

	/**
	 * waitAlert: it will Wait for alert
	 * 
	 * @param waitSeconds
	 */
	public static void waitAlert(long waitSeconds) {
		long startTime = System.currentTimeMillis();
		WebDriverWait syncWait = new WebDriverWait(Common.getDriver(), waitSeconds);
		syncWait.withTimeout(Sync.waitSeconds,TimeUnit.SECONDS);
		syncWait.pollingEvery(300, TimeUnit.MICROSECONDS);
		syncWait.until(ExpectedConditions.alertIsPresent());

		long endTime = System.currentTimeMillis();
		Driver.getLogger().info(" Time taken to iwaitAlert   is  " + ((endTime - startTime)) + "  milliseconds");

		Sync.logger.info("Sync waitAlert Completed successfully");
	}

	/**
	 * waitElementinvisible:
	 * 
	 * @param waitSeconds
	 *            -- provide wait in seconds.
	 * @param elemfindBy
	 *            -- provide Element findType with
	 *            id/name/className/linkText/xpath.
	 * @param elemfindText
	 *            -- provide Element findText
	 */
	public static void waitElementInvisible(String elemfindBY, String elemfindText) {
		waitElementInvisible(Sync.waitSeconds, elemfindBY, elemfindText);
	}
	
	public static void waitElementInvisible(long maxWaitInSeconds, String elemfindBY, String elemfindText) {
		long startTime = System.currentTimeMillis();
		WebDriverWait syncWait = new WebDriverWait(Common.getDriver(), maxWaitInSeconds);
		wait.withTimeout(maxWaitInSeconds,TimeUnit.SECONDS);
		syncWait.pollingEvery(300, TimeUnit.MICROSECONDS);
		switch (elemfindBY) {
		case "id":
			syncWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(elemfindText)));
			break;
		case "name":
			syncWait.until(ExpectedConditions.invisibilityOfElementLocated(By.name(elemfindText)));
			break;
		case "className":
			syncWait.until(ExpectedConditions.invisibilityOfElementLocated(By.className(elemfindText)));
			break;
		case "linkText":
			syncWait.until(ExpectedConditions.invisibilityOfElementLocated(By.linkText(elemfindText)));
			break;
		case "xpath":
			syncWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(elemfindText)));
			break;
		case "css":
			syncWait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(elemfindText)));
			break;
		case "pLinkText":
			syncWait.until(ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText(elemfindText)));
			break;
		case "tagName":
			syncWait.until(ExpectedConditions.invisibilityOfElementLocated(By.tagName(elemfindText)));
			break;
		}

		long endTime = System.currentTimeMillis();
		Driver.getLogger().info(" Time taken to waitElementInvisible  with locator " + elemfindText
				+ "    with attrubuteBy  " + elemfindBY + "    is  " + ((endTime - startTime)) + "  milliseconds");

		Sync.logger.info("Sync waitElementInvisible completed successfully");
	}
	

	/**
	 * waitElementinvisible:
	 * 
	 * @param waitSeconds
	 *            -- provide wait in seconds.
	 * @param elemfindBy
	 *            -- provide Element findType with
	 *            id/name/className/linkText/xpath.
	 * @param elemfindText
	 *            -- provide Element findText
	 * @throws Exception 
	 */

	public static void waitElementVisible(String elemfindBY, String elemfindText) throws Exception {
		long startTime = System.currentTimeMillis();
		waitForWebElement(
				ExpectedConditions.visibilityOfElementLocated(returnByLocator(elemfindBY, elemfindText)));
		long endTime = System.currentTimeMillis();
		Driver.getLogger().info(" Time taken to waitElementVisible  with locator " + elemfindText + "    with attrubuteBy  "
				+ elemfindBY + "    is  " + ((endTime - startTime)) + "  milliseconds");
		Driver.getLogger().info("Sync waitElementVisible completed successfully");
	}
	
	public static void waitForAlert(Integer timeOut) {
		new WebDriverWait(Common.getDriver(),timeOut).ignoring(NoAlertPresentException.class).until(ExpectedConditions.alertIsPresent());
	}
	


	/**
	 * waitElementClickable: it will wait until element clickable
	 * 
	 * @param waitSeconds---set
	 *            waiting Time
	 * @param elemfindBy
	 *            --- set findBy like..id,name,className,xpath
	 * @param elemfindText--set
	 *            findText like Consolidation...etc
	 */
	public static void waitElementClickable(long waitSeconds, String elemfindBY, String elemfindText) {
		waitElementClickable(waitSeconds,returnByLocator(elemfindBY, elemfindText));
	}
	public static void waitElementVisible(long waitSeconds, String elemfindBY, String elemfindText) {
		
		waitElementVisible(waitSeconds,returnByLocator(elemfindBY, elemfindText));
	}
	
	

	public static void waitElementClickable(long waitSeconds, By by) {
		long startTime = System.currentTimeMillis();

		if(Common.browserType().equalsIgnoreCase("EDGE")){
			waitForWebElement(waitSeconds, ExpectedConditions.presenceOfElementLocated(by));
		}else{
		waitForWebElement(waitSeconds,
				ExpectedConditions.elementToBeClickable(by));
		}

		long endTime = System.currentTimeMillis();
		Driver.getLogger().info(" Time taken to waitElementClickable  with locator " + by
				+ "    with attrubuteBy  " + by + "    is  " + ((endTime - startTime)) + "  milliseconds");
		Sync.logger.info("Sync waitElementClickable completed successfully");
	}
	public static void waitElementVisible(long waitseconds,By by){
		long startTime = System.currentTimeMillis();

		if(Common.browserType().equalsIgnoreCase("EDGE")){
			waitForWebElement(waitseconds, ExpectedConditions.visibilityOfElementLocated(by));
		}else{
		waitForWebElement(waitseconds,
				ExpectedConditions.visibilityOfElementLocated(by));
		}

		long endTime = System.currentTimeMillis();
		Driver.getLogger().info(" Time taken to waitElementClickable  with locator " + by
				+ "    with attrubuteBy  " + by + "    is  " + ((endTime - startTime)) + "  milliseconds");
		Sync.logger.info("Sync waitElementVisible completed successfully");
	}

	public static void waitElementClickable(String elemfindBY, String elemfindText) {
		waitElementClickable(Sync.waitSeconds, elemfindBY, elemfindText);
	}
	

	/**
	 * it will wait for frame
	 * 
	 * @param waitSeconds
	 *            --- set waitSeconds
	 * @param frame
	 *            --- set frameName/frameId
	 */

	public static void waitFrame(long waitSeconds, String frame) throws Exception {
		long startTime = System.currentTimeMillis();
		WebDriverWait syncWait = new WebDriverWait(Common.getDriver(), Sync.waitSeconds);
		syncWait.withTimeout(waitSeconds,TimeUnit.SECONDS);
		syncWait.pollingEvery(300, TimeUnit.MICROSECONDS);
		syncWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frame));

		long endTime = System.currentTimeMillis();
		Driver.getLogger().info(" Time taken to waitFrame    is  " + ((endTime - startTime)) + "  milliseconds");
		Sync.logger.info("Sync waitFrame completed successfully");
	}

	/**
	 * it will wait for frame
	 * 
	 * @param waitSeconds
	 *            --- set waitSeconds
	 * @param frame
	 *            --- set frameName/frameId
	 */
	public static void waitFrame(long waitSeconds, String frame, String findBy) throws Exception {
		long startTime = System.currentTimeMillis();
		WebDriverWait syncWait = new WebDriverWait(Common.getDriver(), Sync.waitSeconds);
		syncWait.withTimeout(waitSeconds,TimeUnit.SECONDS);
		syncWait.pollingEvery(300, TimeUnit.MICROSECONDS);
		switch (findBy.toLowerCase()) {
		case "id":
			syncWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id(frame)));
			break;
		case "name":
			syncWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name(frame)));
			break;
		case "class":
			syncWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.className(frame)));
			break;
		case "css":
			syncWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.cssSelector(frame)));
			break;
		case "xpath":
			syncWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath(frame)));
			break;
		}
		long endTime = System.currentTimeMillis();
		Driver.getLogger().info(" Time taken to waitFrame    is  " + ((endTime - startTime)) + "  milliseconds");
		Sync.logger.info("Sync waitFrame completed successfully");
	}

	/**
	 * it will wait for frame
	 * 
	 * @param frame
	 *            --- set frameName/frameId
	 */
	public static void waitFrame(String frame) throws Exception {
		long startTime = System.currentTimeMillis();
		WebDriverWait syncWait = new WebDriverWait(Common.getDriver(), Sync.waitSeconds);
		syncWait.pollingEvery(300, TimeUnit.MICROSECONDS);
		syncWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frame));

		long endTime = System.currentTimeMillis();
		Driver.getLogger().info(" Time taken to waitFrame    is  " + ((endTime - startTime)) + "  milliseconds");
		Sync.logger.info("Sync waitFrame completed successfully");
	}

	/**
	 * it will wait for Text present in a element
	 * 
	 * @param waitSeconds
	 *            --- set set waiting Time
	 * @param elemfindBy
	 *            --- set findBy like..id,name,className,xpath
	 * @param elemfindText---set
	 *            findText like Consolidation...etc
	 * @param text
	 *            --- set text
	 */
	public static void waitTextPresent(long maxWaitSeconds, String elemfindBY, String elemfindText, String text)
			throws Exception {
		long startTime = System.currentTimeMillis();

		waitForWebElementText(maxWaitSeconds,
				ExpectedConditions.textToBePresentInElementLocated(returnByLocator(elemfindBY, elemfindText), text));

		long endTime = System.currentTimeMillis();
		Driver.getLogger().info(" Time taken to waitTextPresent  with locator " + elemfindText + "    with attrubuteBy  "
				+ elemfindBY + "    is  " + ((endTime - startTime)) + "  milliseconds");

		Sync.logger.info("Sync waitTextPresent completed successfully");
	}
	
	
	/**
	 * it will wait for Text present in a element
	 * 
	 * @param elemfindBy
	 *            --- set findBy like..id,name,className,xpath
	 * @param elemfindText---set
	 *            findText like Consolidation...etc
	 * @param text
	 *            --- set text
	 */
	public static void waitTextPresent(String elemfindBY, String elemfindText, String text) throws Exception {
		waitTextPresent(Sync.waitSeconds, elemfindBY, elemfindText, text);
	}
	


	/**
	 * it will wait for waitElementPresent
	 * 
	 * @param waitSeconds---set
	 *            waiting Time
	 * @param elemfindBy
	 *            --- set findBy like..id,name,className,xpath
	 * @param elemfindText--set
	 *            findText like Consolidation...etc
	 */
	public static void waitElementPresent(long waitSeconds, String elemfindBY, String elemfindText) throws Exception {
		waitElementPresent(elemfindBY,elemfindText);
	}
	
	
	public static void waitElementPresent(String elemfindBY, String elemfindText) throws Exception {
		long startTime = System.currentTimeMillis();
		WebDriverWait syncWait = new WebDriverWait(Common.getDriver(), Sync.waitSeconds);
		syncWait.pollingEvery(300, TimeUnit.MICROSECONDS);
		switch (elemfindBY) {
		case "id":
			syncWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id(elemfindText)));
			break;
		case "name":
			syncWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.name(elemfindText)));
			break;
		case "className":
			syncWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className(elemfindText)));
			break;
		case "linkText":
			syncWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.linkText(elemfindText)));
			break;
		case "xpath":
			syncWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(elemfindText)));
			break;
		case "css":
			syncWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(elemfindText)));
			break;
		case "pLinkText":
			syncWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.partialLinkText(elemfindText)));
			break;
		case "tagName":
			syncWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName(elemfindText)));
			break;
		}

		long endTime = System.currentTimeMillis();
		Driver.getLogger().info(" Time taken to waitElementPresent  with locator " + elemfindText + "    with attrubuteBy  "
				+ elemfindBY + "    is  " + ((endTime - startTime)) + "  milliseconds");

		Sync.logger.info("Sync waitElementPresent completed successfully");
	}
	
	

	/**
	 * it will wait for waitElementPresent
	 * 
	 * @param elemfindText
	 *            send id of the elemnet to be present
	 * @throws Exception
	 */
	public static void waitElementPresent(String elemfindText) throws Exception {
		long startTime = System.currentTimeMillis();
		WebDriverWait syncWait = new WebDriverWait(Common.getDriver(), Sync.waitSeconds);
		syncWait.pollingEvery(300, TimeUnit.MICROSECONDS);
		syncWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id((elemfindText))));

		long endTime = System.currentTimeMillis();
		Driver.getLogger().info(" Time taken to waitElementPresent  with locator " + elemfindText + "     is  "
				+ ((endTime - startTime)) + "  milliseconds");

		Sync.logger.info("Sync waitElementPresent completed successfully");

	}

	/**
	 * it will wait for document ready state
	 * 
	 * @throws Exception
	 */
	public static void waitForLoad() throws Exception {
		long startTime = System.currentTimeMillis();
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
		WebDriverWait syncWait = new WebDriverWait(Common.getDriver(), 30);
		syncWait.until(pageLoadCondition);
		long endTime = System.currentTimeMillis();
		Driver.getLogger().info(" Time taken to waitForLoad    is  " + ((endTime - startTime)) + "  milliseconds");

	}
	
	
	
	/**
	 * it will wait for waitForNumberOfWindowsToEqual more than one provide by
	 * user
	 * 
	 * @param numberOfWindows
	 *            pass the no.of windows expecting
	 * @throws Exception
	 */
	public static void waitForNumberOfWindowsToEqual(final int numberOfWindows) throws Exception {
		long startTime = System.currentTimeMillis();
		new WebDriverWait(Common.getDriver(), 120) {
		}.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return (driver.getWindowHandles().size() == numberOfWindows);
			}
		});
		long endTime = System.currentTimeMillis();
		Driver.getLogger().info(
				" Time taken to waitForNumberOfWindowsToEqual    is  " + ((endTime - startTime)) + "  milliseconds");

	}

	/**
	 * it will wait for waitForNumberOfWindowsToEqual more than one provide by
	 * user for a specific time
	 * 
	 * @param delay
	 *            pass delay time
	 * @param numberOfWindows
	 *            pass the no.of windows expecting
	 * @throws Exception
	 */
	public static void waitForNumberOfWindowsToEqual(int delay, final int numberOfWindows) throws Exception {

		// Making a new expected condition
		new WebDriverWait(Common.getDriver(), delay) {
		}.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return (driver.getWindowHandles().size() == numberOfWindows);
			}
		});

	}
	
	public static void waitPresenceOfElementLocated(String elemfindBY, String elemfindText) 
	{
		long startTime = System.currentTimeMillis();

		try 
		{
			waitForWebElement(
					ExpectedConditions.presenceOfElementLocated(returnByLocator(elemfindBY, elemfindText)));
		} 
		catch (Exception e) 
		{
			//Driver.getLogger().exception("waitPresenceOfElementLocated is throwing Exception need to fix this issue");
		}

		long endTime = System.currentTimeMillis();
		Driver.getLogger().info(" Time taken to waitPresenceOfElementLocated  with locator " + elemfindText
				+ "    with attrubuteBy  " + elemfindBY + "    is  " + ((endTime - startTime)) + "  milliseconds");

		Driver.getLogger().info("Sync presenceOfElementLocated completed successfully for the element: " + elemfindBY
				+ "with text:  " + elemfindText);

		// wait.until(ExpectedConditions.presenceOfElementLocated(By.id("whatever")));
	}
	


	public static void processSync(long time) 
	{
		try 
		{
			Thread.sleep(time);
			Driver.getLogger().info("processSync passed  time " + (time) + "seconds");
		} 
		catch (Exception e) 
		{
			//Driver.getLogger().error("processSync failed with below error: " + e);
		}
	}

	
	

	

	public static void waitForElementToRefresh(WebElement element) throws Exception {
		long startTime = System.currentTimeMillis();
		WebDriverWait syncWait = new WebDriverWait(Common.getDriver(), Sync.waitSeconds);
		syncWait.withTimeout(waitSeconds,TimeUnit.SECONDS);
		syncWait.pollingEvery(300, TimeUnit.MICROSECONDS);

		syncWait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(element)));

		long endTime = System.currentTimeMillis();
		Driver.getLogger().info(" Time taken to waitForElementToRefresh  with locator     is  " + ((endTime - startTime))
				+ "  milliseconds");

		Driver.getLogger().info("Sync waitForElementToRefresh completed successfully for the element: ");
	}

	
	public static void waitForElementToAvoidStaleness(WebElement element) throws Exception {
		long startTime = System.currentTimeMillis();
		WebDriverWait syncWait = new WebDriverWait(Common.getDriver(), Sync.waitSeconds);
		syncWait.withTimeout(waitSeconds,TimeUnit.SECONDS);
		syncWait.pollingEvery(300, TimeUnit.MICROSECONDS);
		syncWait.until(ExpectedConditions.stalenessOf(element));
		long endTime = System.currentTimeMillis();
		Driver.getLogger().info(" Time taken to waitForElementToRefresh  with locator     is  " + ((endTime - startTime))
				+ "  milliseconds");

		Driver.getLogger().info("Sync waitForElementToRefresh completed successfully for the element: ");
	}

	public static void waitForElementToAvoidStaleness(long waitseconds,WebElement element) throws Exception {
		long startTime = System.currentTimeMillis();
		WebDriverWait syncWait = new WebDriverWait(Common.getDriver(), waitseconds);
		syncWait.withTimeout(waitSeconds,TimeUnit.SECONDS);
		syncWait.pollingEvery(300, TimeUnit.MICROSECONDS);
		syncWait.until(ExpectedConditions.stalenessOf(element));
		long endTime = System.currentTimeMillis();
		Driver.getLogger().info(" Time taken to waitForElementToRefresh  with locator     is  " + ((endTime - startTime))
				+ "  milliseconds");

		Driver.getLogger().info("Sync waitForElementToRefresh completed successfully for the element: ");
	}


	public static void waitForAttributeTextToChange(String elemfindBY, String elemfindText, String attribute,
			String textToBe) throws Exception {
		waitForAttributeTextToChange(Sync.waitSeconds, elemfindBY, elemfindText, attribute, textToBe);
	}
	


	public static void waitForAttributeTextToChange(long waitSeconds, String elemfindBY, String elemfindText,
			String attribute, String textToBe) throws Exception {
		long startTime = System.currentTimeMillis();
//		WebDriverWait wait = new WebDriverWait(Common.getDriver(), waitSeconds);
//		By by = returnByLocator(elemfindBY, elemfindText);

		waitForIterations(5, waitSeconds,
				ExpectedConditions.attributeToBe(returnByLocator(elemfindBY, elemfindText), attribute, textToBe));
		// wait.until(ExpectedConditions.attributeToBe(by, attribute,textToBe));
		long endTime = System.currentTimeMillis();
		Driver.getLogger().info(" Time taken to waitForAttributeTextToChange  with locator " + elemfindText
				+ "    with attrubuteBy  " + elemfindBY + "    is  " + ((endTime - startTime)) + "  milliseconds");
		Driver.getLogger().info("Sync waitForAttributeTextToChange completed successfully for the element:    " + textToBe);
	}
	
	
	public static void waitForAttributeTextToChange( By by,String attribute,
			String textToBe) throws Exception {
		long startTime = System.currentTimeMillis();
//		WebDriverWait wait = new WebDriverWait(Common.getDriver(), waitSeconds);		

		waitForIterations(5, waitSeconds,ExpectedConditions.attributeToBe(by, attribute,textToBe));
		long endTime = System.currentTimeMillis();
		Driver.getLogger().info(" Time taken to waitForAttributeTextToChange  with locator " + by.toString()+ "    is  " + ((endTime - startTime)) + "  milliseconds");
		Driver.getLogger().info("Sync waitForAttributeTextToChange completed successfully for the element:    " + textToBe);
	}





	
	private static By returnByLocator(String elemfindBY, String elemfindText) {
		By by = null;
		switch (elemfindBY) {
		case "id":
			by = By.id(elemfindText);
			// Sync.logger.info("Sync presenceOfElementLocated completed
			// successfully for the element: "+elemfindBY +"with text:
			// "+elemfindText);
			break;

		case "name":
			by = By.name(elemfindText);
			// Sync.logger.info("Sync presenceOfElementLocated completed
			// successfully");
			break;
		case "className":
			by = By.className(elemfindText);
			// Sync.logger.info("Sync presenceOfElementLocated completed
			// successfully");
			break;
		case "linkText":
			by = By.linkText(elemfindText);
			// Sync.logger.info("Sync presenceOfElementLocated completed
			// successfully");
			break;
		case "xpath":
			by = By.xpath(elemfindText);
			// Sync.logger.info("Sync presenceOfElementLocated completed
			// successfully");
			break;
		case "css":
			by = By.cssSelector(elemfindText);
			// Sync.logger.info("Sync presenceOfElementLocated completed
			// successfully");
			break;
		case "pLinkText":
			by = By.partialLinkText(elemfindText);
			// Sync.logger.info("Sync presenceOfElementLocated completed
			// successfully");
			break;
		case "tagName":
			by = By.tagName(elemfindText);
			// Sync.logger.info("Sync presenceOfElementLocated completed
			// successfully");
			break;
		}
		return by;

	}
	
	public static void waitForIterationsUntilRefresh(int iterationCount, By by, Function<? super WebDriver, WebElement> isTrue) {
		WebDriverWait syncWait = new WebDriverWait(Common.getDriver(), Sync.waitSeconds);
		syncWait.withTimeout(waitSeconds,TimeUnit.SECONDS);
		syncWait.pollingEvery(300, TimeUnit.MICROSECONDS);
		int breakCount = 0;
		while (breakCount < iterationCount) {
			try {
				syncWait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(by)));
				syncWait.until(isTrue);
				break;
			} catch (Exception e) {
				Driver.getLogger().info(" May Be Sliced Approach should be applied for "+by+"  to make page title locator  visible");
				e.printStackTrace();
			}
			breakCount++;
		}

	}

	public static void waitAndSwitchToFrame(String elemfindBY, String elemfindText) {
		if(elemfindText.equals("topnavFrameId")){
			return;
		}
		By by = returnByLocator(elemfindBY, elemfindText);
		WebDriverWait syncWait = new WebDriverWait(Common.getDriver(), Sync.waitSeconds);
		syncWait.withTimeout(waitSeconds,TimeUnit.SECONDS);
		syncWait.pollingEvery(300, TimeUnit.MICROSECONDS);
		syncWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(by));
	}
	

	public static void waitForIterationsUntilClickable(int iterationCount, String elemfindBY, String elemfindText)
			throws Exception {
		By by = returnByLocator(elemfindBY, elemfindText);
		waitAndReturnWebElement(ExpectedConditions.elementToBeClickable(by));
		// waitForIterations(iterationCount,Sync.waitSeconds,ExpectedConditions.elementToBeClickable(by));
	}
	



	private static void waitForIterations(ExpectedCondition<Boolean> isTrue) {
		waitForIterations(1, Sync.waitSeconds, isTrue);
	}

	private static void waitForIterations(int iterationCount, long maxWaitForSeconds, final ExpectedCondition<Boolean> isTrue) {
		FluentWait<WebDriver> wait = new WebDriverWait(Common.getDriver(), maxWaitForSeconds)
				.ignoring(Exception.class)
				.pollingEvery(Sync.miniSliceOfWaitSeconds,TimeUnit.MILLISECONDS);
		try {
			Sync.waitImplicit(1);
			wait.until(driver -> wait(isTrue));
		}finally{
			Common.implicitWait();
		}
	}

	private static boolean wait(ExpectedCondition<Boolean> isTrue) {
		Sync.waitImplicit(miniSliceOfWaitSeconds);
		Driver.getLogger().info("Started WAIT method");
		WebDriverWait syncWait = new WebDriverWait(Common.getDriver(), 10);
		syncWait.withTimeout(Sync.miniSliceOfWaitSeconds,TimeUnit.MILLISECONDS);
		syncWait.pollingEvery(300, TimeUnit.MICROSECONDS);
		boolean ele = syncWait.until(isTrue);
		Driver.getLogger().info("Ended WAIT method");
		return ele;
	}

//	private static boolean wait(Function<? super WebDriver, WebElement> isTrue) throws Exception {
//		Sync.waitImplicit(miniSliceOfWaitSeconds);
//		Driver.getLogger().info("Started WAIT method");
//		WebDriverWait syncWait = new WebDriverWait(getDriver(), 10);
//		syncWait.withTimeout(Duration.ofSeconds(miniSliceOfWaitSeconds));
//		syncWait.pollingEvery(300, TimeUnit.MICROSECONDS);
//		WebElement ele = syncWait.until(isTrue);
//		Driver.getLogger().info("Ended WAIT method");
//		return ele.isEnabled();
//	}




	public static void waitForCondition(ExpectedCondition<?> isTrue) {
		Sync.waitImplicit(miniSliceOfWaitSeconds);
		Driver.getLogger().info("Started WAIT method");
		Sync.waitImplicit(0);
		WebDriverWait syncWait = new WebDriverWait(Common.getDriver(), 60);
		syncWait.withTimeout(Sync.miniSliceOfWaitSeconds,TimeUnit.MILLISECONDS);
		syncWait.pollingEvery(300, TimeUnit.MICROSECONDS);
		syncWait.until(isTrue);
		Driver.getLogger().info("Ended WAIT method");
		Common.implicitWait();
	}
	
	
	public static void waitForAttributeTextToChange(WebElement ele, String attribute, String textToBe) throws Exception {
		String text = "";
		for(int i=0;i<9;i++){
			try{

				text = ele.getAttribute(attribute);
				Driver.getLogger().info("texttexttextis "+text+"   textToBe  "+textToBe);
				if(text.contains(textToBe)){
					return;
				}


			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public static void waitElementClickable(long waitSeconds, WebElement element)  {
		int waitTime = 500 ;
		int noOFIterations = (int) (waitSeconds/5);
		for(int i=0;i<noOFIterations;i++){
			try{
				// waitForIterations(5,waitSeconds,ExpectedConditions.elementToBeClickable(returnByLocator(elemfindBY,elemfindText)));
				waitForWebElement(waitSeconds,
						ExpectedConditions.elementToBeClickable(element));
				Sync.processSync(waitTime);
				element.isEnabled();
				element.getAttribute("id");
				return;
			}catch(Exception e){
				Driver.getLogger().info("saved from waitElementClickable(long waitSeconds, WebElement element  "+i);
			}
		}

	}


	public static void waitForAttributeTextToChange(WebElement ele, By childElement,String attribute, String textToBe, int iterations) throws Exception {
		String text = "";
		for(int i=0;i<iterations;i++){
			try{

				text = ele.findElement(childElement).getAttribute(attribute);
				if(text.contains(textToBe)){
					return;
				}
				Sync.processSync(500);

			}catch(Exception e){
				e.printStackTrace();
				Sync.processSync(500);
			}
		}

	}



	
	
	public static void scrollDownToView(String elemfindBy, String elemfindText) {
		// TODO Auto-generated method stub

		WebElement ied = null;
		if (elemfindBy.equals("id")) {
			ied = Common.getDriver().findElement(By.id(elemfindText));
		} else if (elemfindBy.equals("name")) {
			ied = Common.getDriver().findElement(By.name(elemfindText));
		} else if (elemfindBy.equals("className")) {
			ied = Common.getDriver().findElement(By.className(elemfindText));
		}  else if (elemfindBy.equals("xpath")) {
			ied = Common.getDriver().findElement(By.xpath(elemfindText));
		}
		((JavascriptExecutor) Common.getDriver()).executeScript("arguments[0].scrollIntoView(true);",ied);

	}
	
	public static void scrollSideToView(String elemfindBy, String elemfindText) {
	if (elemfindBy.equals("id")) {
		((JavascriptExecutor) Common.getDriver()).executeScript(
	    "document.getElementById('"+elemfindText+"').scrollLeft += 250", "");
	} else if (elemfindBy.equals("name")) {
		Driver.getLogger().info("In class NAME");
		((JavascriptExecutor) Common.getDriver()).executeScript("$('."+elemfindText+"').animate({scrollLeft: 1000});");
	}
	}
	
	public static void waitCustomtAlert(long waitSeconds) throws Exception {
		long startTime = System.currentTimeMillis();
		WebDriverWait syncWait = new WebDriverWait(Common.getDriver(), waitSeconds);
		syncWait.withTimeout(waitSeconds,TimeUnit.SECONDS);
		syncWait.pollingEvery(300, TimeUnit.MICROSECONDS);
		syncWait.until(ExpectedConditions.alertIsPresent());

		long endTime = System.currentTimeMillis();
		Driver.getLogger().info(" Time taken to iwaitAlert   is  " + ((endTime - startTime)) + "  milliseconds");

		Sync.logger.info("Sync waitAlert Completed successfully");
	}
	
	
	
	
	public static void implicitWait() {
		waitImplicit(waitSecond);
	}
	
	static long waitSeconds = 30;
	private static int staleEliminatorIteration = 2;

	public static WebElement waitAndReturnWebElement(By by) {
		return waitAndReturnWebElement(ExpectedConditions.elementToBeClickable(by));
	}

	public static WebElement waitAndReturnWebElement(Function<? super WebDriver, WebElement> isTrue) {
		long startTime = System.currentTimeMillis();
		long endTime ;
		try {
			WebElement ele =  waitAndReturnWebElement(waitSeconds, isTrue);
			ele.isEnabled();
			endTime = System.currentTimeMillis();
			Driver.getLogger().info(" Time taken to Function<? super WebDriver, WebElement> isTrue  is  " + ((endTime - startTime)) + " milliseconds");
			return ele;
		} catch (StaleElementReferenceException e) {
			Driver.getLogger().info("This Saved From Stale Element. We need to improve");
			endTime = System.currentTimeMillis();
			Driver.getLogger().info(" Time taken to Function<? super WebDriver, WebElement> isTrue  is  " + ((endTime - startTime)) + " milliseconds");
			return waitAndReturnWebElement(waitSeconds, isTrue);
		} catch (InvalidElementStateException e) {
			endTime = System.currentTimeMillis();
			Driver.getLogger().info(" Time taken to Function<? super WebDriver, WebElement> isTrue  is  " + ((endTime - startTime)) + " milliseconds");
			Driver.getLogger().info("This Saved From Ivalid Element. We need to improve");
			return waitAndReturnWebElement(waitSeconds, isTrue);
		}
	}

	public static boolean waitForWebElement(Function<? super WebDriver, WebElement> isTrue) {
		return waitForWebElement(waitSeconds, isTrue);

	}

	public static boolean waitForWebElement(long maxWaitForSeconds, Function<? super WebDriver, WebElement> isTrue) {	
		try {
			return waitAndReturnWebElement(maxWaitForSeconds, isTrue).isEnabled();
		} catch (StaleElementReferenceException e) {
			Driver.getLogger().info("This Saved From Stale Element. We need to improve");
			return waitAndReturnWebElement(maxWaitForSeconds, isTrue).isEnabled();
		} catch (InvalidElementStateException e) {
			Driver.getLogger().info("This Saved From Ivalid Element. We need to improve");
			return waitAndReturnWebElement(maxWaitForSeconds, isTrue).isEnabled();
		}

	}

	/*******************************************************************************************************
	 * 
	 * 
	 * 
	 * This is the Main Iterator Method which will iterate properly to achieve
	 * Reliability to avoid Stale , Invalid Exceptions. This Iterate for 2 times
	 * only in case of Stale Element, and Invalid State Exceptions It will throw
	 * same exception for FIRST iteration other than Stale, Invalid Exceptions
	 * 
	 * 
	 * @param maxWaitForSeconds
	 * @param isTrue
	 * @return
	 * @throws Exception
	 * 
	 * 
	 * 
	 * 
	 *******************************************************************************************************/

	public static WebElement waitAndReturnWebElement(long maxWaitForSeconds, Function<? super WebDriver, WebElement> isTrue)  {
		long startTime = System.currentTimeMillis();
		long endTime ;
		int i = 0;
		while (i < staleEliminatorIteration) {
			try {
				WebElement element = waitAndReturnWebElement(1, maxWaitForSeconds, isTrue);
				element.isEnabled();	
				endTime = System.currentTimeMillis();
				Driver.getLogger().info(" Time taken to i < staleEliminatorIteration  is  " + ((endTime - startTime)) + " milliseconds");
				return element;
			} catch (StaleElementReferenceException e) {
				Driver.getLogger().info("This Saved From Stale Element. We need to improve");
				++i;				
				return waitAndReturnWebElement(maxWaitForSeconds, isTrue);
			} catch (InvalidElementStateException e) {
				++i;
				Driver.getLogger().info("This Saved From Ivalid Element. We need to improve");
				return waitAndReturnWebElement(maxWaitForSeconds, isTrue);
			} 
		}
		endTime = System.currentTimeMillis();
		Driver.getLogger().info(" Time taken to i < staleEliminatorIteration  is  " + ((endTime - startTime)) + " milliseconds");
		return null;
	}

	/***********************************************************************************************************************
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param maxWaitForSeconds
	 * @param isTrue
	 * @return
	 * @throws Exception
	 * 
	 * 
	 * 
	 * 
	 */
	public static boolean waitForWebElementText(long maxWaitForSeconds, ExpectedCondition<Boolean> isTrue) {		
		int i = 0;
		while (i < staleEliminatorIteration) {
			try {
				return waitAndReturnWebElementText(1, maxWaitForSeconds, isTrue);
			} catch (StaleElementReferenceException e) {
				Driver.getLogger().info("This Saved From Stale Element. We need to improve");
				++i;
				return waitAndReturnWebElementText(1, maxWaitForSeconds, isTrue);
			} catch (InvalidElementStateException e) {
				++i;
				Driver.getLogger().info("This Saved From Ivalid Element. We need to improve");
				return waitAndReturnWebElementText(1, maxWaitForSeconds, isTrue);
			} 
		}
		return false;
	}

	private static boolean waitAndReturnWebElementText(int iterationCount, long maxWaitForSeconds, ExpectedCondition<Boolean> isTrue) {
		FluentWait<WebDriver> wait = new WebDriverWait(Common.getDriver(), maxWaitForSeconds)
				.ignoring(Exception.class)
				.pollingEvery(Sync.miniSliceOfWaitSeconds,TimeUnit.MILLISECONDS);
		try {
			Sync.waitImplicit(1);
			return wait.until(isTrue);
		}finally{
			Common.implicitWait();
		}
	}

	private static WebElement waitAndReturnWebElement(int iterationCount, long maxWaitForSeconds, Function<? super WebDriver, WebElement> isTrue) {
		FluentWait<WebDriver> wait = new WebDriverWait(Common.getDriver(), maxWaitForSeconds)
											.ignoring(Exception.class)
											.pollingEvery(Sync.miniSliceOfWaitSeconds,TimeUnit.MILLISECONDS);
		try {
			Sync.waitImplicit(1);
			return wait.until(isTrue);
		}finally{
			Common.implicitWait();
		}
	}
	
}