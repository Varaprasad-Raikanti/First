package Utilities;

import java.io.IOException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import TestLib.Automation_properties;

import com.aventstack.extentreports.markuputils.Markup;

public class ExtenantReportUtils {
	
	static ExtentReports extent;
	static ExtentTest logger;
	
	public ExtenantReportUtils(String reportFileName) {
		if(reportFileName.contains("\\s"))
		reportFileName=reportFileName.split(" ")[0];
		ExtentHtmlReporter htmlReporter=new ExtentHtmlReporter(System.getProperty("user.dir")+"/TestLogs/ExtentReport/"+reportFileName+".html");
		//ExtentHtmlReporter htmlReporter=new ExtentHtmlReporter(System.getProperty("user.dir")+"/TestLogs/ExtenantReport/index.html");
	    extent = new ExtentReports();
	    extent.attachReporter(htmlReporter);
	    extent.setSystemInfo("OS", "windows");
	    extent.setSystemInfo("Environment", "Automation Testing ENV");
	    htmlReporter.config().setChartVisibilityOnOpen(true);
	    htmlReporter.config().setDocumentTitle("Extent Report Demo");
	    htmlReporter.config().setReportName("Test Report");
	    htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
	    htmlReporter.config().setTheme(Theme.STANDARD);
	    htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
	}
	
	public static void  saveTestCase()
	{
		extent.flush();
	}
	public static ExtentTest createTestcase(String TestCaseName)
	{
		logger=extent.createTest(TestCaseName);
		return logger;
	}
	
	public static void addInfoLog(String infoMessage)
	{
		 logger.log(Status.INFO, infoMessage);
	}
	
	public static void addPassLog(String passMessage,String screenShotPath)
	{
		try {
			logger.log(Status.PASS, passMessage, MediaEntityBuilder.createScreenCaptureFromPath(screenShotPath).build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addPassLog(String expectedResult,String actualResult,String screenShotPath)
	{
		try {
			String testResult = "<b style=\"color:green;\">Expected Result:</b> " + expectedResult +" &nbsp;<br/><b style=\"color:green;\">Actual Result&nbsp;&nbsp;:</b>" + actualResult;
			logger.log(Status.PASS, testResult, MediaEntityBuilder.createScreenCaptureFromPath(screenShotPath).build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addPassLog(String description,String expectedResult,String actualResult,String screenShotPath)
	{
		try {
			String testResult = "<b style=\"color:green;\">Description :</b> " + description +" &nbsp;<br/><b style=\"color:green;\">Expected Result:</b> " + expectedResult +" &nbsp;<br/><b style=\"color:green;\">Actual Result&nbsp;&nbsp;:</b>" + actualResult;
			logger.log(Status.PASS, testResult, MediaEntityBuilder.createScreenCaptureFromPath(screenShotPath).build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addFailedLog(String expectedResult,String actualResult,String screenShotPath)
	{
		try {
			String testResult = "<b style=\"color:green;\">Expected Result:</b> " + expectedResult +" &nbsp;<br/><b style=\"color:red;\">Actual Result&nbsp;&nbsp;:</b>" + actualResult;
			logger.log(Status.FAIL, testResult, MediaEntityBuilder.createScreenCaptureFromPath(screenShotPath).build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addFailedLog(String description,String expectedResult,String actualResult,String screenShotPath)
	{
		try {
			String testResult = "<b style=\"color:red;\">Description :</b> " + description +" &nbsp;<br/><b style=\"color:red;\">Expected Result:</b> " + expectedResult +" &nbsp;<br/><b style=\"color:red;\">Actual Result&nbsp;&nbsp;:</b>" + actualResult;
			logger.log(Status.FAIL, testResult, MediaEntityBuilder.createScreenCaptureFromPath(screenShotPath).build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addFailedLog(String failMessage,String screenShotPath)
	{
		try {
			logger.log(Status.FAIL, failMessage, MediaEntityBuilder.createScreenCaptureFromPath(screenShotPath).build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addPassLog(String passMessage)
	{
		logger.log(Status.PASS, passMessage);
	}
	
	public static void addFailedLog(String failMessage)
	{
		logger.log(Status.FAIL, failMessage);
	}
	
	public static void main(String[] args) throws IOException {
		
	ExtentHtmlReporter htmlReporter=new ExtentHtmlReporter("./TestLogs/ExtentReportResults_52.html");
    ExtentReports extent = new ExtentReports();
    extent.attachReporter(htmlReporter);
    extent.setSystemInfo("OS", "windows");
   
    extent.setSystemInfo("Environment", "Automation Testing ENV");
    htmlReporter.config().setChartVisibilityOnOpen(true);
    htmlReporter.config().setDocumentTitle("Extent Report Demo");
    htmlReporter.config().setReportName("Test Report");
    htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
    htmlReporter.config().setTheme(Theme.STANDARD);
   // htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
    
    ExtentTest logger=extent.createTest("LoginTest").assignCategory("GoogleAPP");
    String testResult = "<b style=\"color:green;\">Description :</b> " + "Login application validation"+"<b style=\"color:green;\">Expected Result:</b> " + "User Should Login into revoln application" +" &nbsp;&nbsp;&nbsp;<b style=\"color:red;\">Actual Result&nbsp;&nbsp;:</b>"+" Logined into application";
    
    logger.log(Status.INFO, "Login to Revolon");
    logger.log(Status.PASS, "Title verified");
    extent.setSystemInfo("Browser", "firefox");
    //logger.log(Status.FAIL, "Failed with", MediaEntityBuilder.createScreenCaptureFromPath("../TestLogs/screenShots/TestScreen.jpg").build());
    logger.log(Status.FAIL, testResult, MediaEntityBuilder.createScreenCaptureFromPath("TestScreen.jpg").build());
    extent.flush();
	
	}
	
}
