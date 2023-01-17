package Utilities;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import TestLib.Automation_properties;
import TestLib.Common;
import TestLib.Driver;

public class TestListener implements  ITestListener ,ISuiteListener{
	final public static log4j logger = Driver.getLogger();
	String StartTime, EndTime;
	public static ExtenantReportUtils report;
	VideoCapture video=new VideoCapture();
	
	//private static List<String> executionDataDirectories = Arrays.asList("TestLogs/logs", "TestLogs/screenShots", "test-output", "TestLogs/videos", "TestLogs/buildlogs", "TestLogs/Listener txt files", "TestLogs/IETraceLogs", "TestLogs/DebugLogs");
	
	private static List<String> executionDataDirectories = Arrays.asList("TestLogs/logs", "TestLogs/screenShots", "test-output", "TestLogs/videos");
	public static void cleanAutomationEnvironment() {
		executionDataDirectories.forEach(eachDirectory -> deleteFilesInDirectory(new File(eachDirectory)));
	}

	public  void onStart(ISuite arg0) 
	{
		cleanAutomationEnvironment();
		System.out.println("Suited Name	: "+arg0.getXmlSuite().getName());
		/*if(report==null)
		report=new ExtenantReportUtils(arg0.getXmlSuite().getName());*/
	}
	
	private static void deleteFilesInDirectory(File directoryPath) {
		try {
			FileUtils.cleanDirectory(directoryPath);
		} catch (IOException e) {
			Driver.getLogger().error("Unable to delete files in directory. Continuing.. \n Error: ");
		}
	}
	

	public void onStart(ITestContext context) {
		String testName=context.getName();
		System.out.println(context.getName());
		
		String videoStatus=Automation_properties.getInstance().getProperty("VideoRecord");
		if(videoStatus!=null)
		{
		if(Automation_properties.getInstance().getProperty("VideoRecord").toLowerCase().equalsIgnoreCase("Yes"))
		{	
		video.startVideoCapture();
		}}
		/*if(report==null)
		{
			report=new ExtenantReportUtils(testName);
		}*/
		
		report=new ExtenantReportUtils(testName);
	}

	
	public void onTestStart(ITestResult result) {
		
		String testName=result.getName();
		System.out.println(result.getName());
		Thread.currentThread().setName(result.getTestContext().getCurrentXmlTest().getClasses().get(0).getName());
		if(report==null)
		{
			report=new ExtenantReportUtils(result.getTestContext().getCurrentXmlTest().getClasses().get(0).getName());
		}
		try {
			if(System.getProperty("browser")==null)
			{report.extent.setSystemInfo("Browser", System.getProperty("browser",Automation_properties.getInstance().getProperty("BROWSER")));}
			else {report.extent.setSystemInfo("Browser", System.getProperty("browser"));}
		    }catch(Exception e)
		    {
		    	report.extent.setSystemInfo("Browser", System.getProperty("browser","chrome"));
		    }
		report.createTestcase(testName);
		
	}

	
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		String testName=result.getName();
		String filePath=Common.getscreenShotPathforReport(testName);
		//report.addPassLog(testName + "Passed");
	   // RESTHelper.updateJIRAStatus("BTY-340,BTY-341,BTY-427,BTY-428,OUS23-707,HFM2-976","Passed");
		//RESTHelper.updateJIRAStatus("HMF2-975","Passed");
	}

	
	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
		String testName=result.getName();
		String filePath=Common.getscreenShotPathforReport(testName);
		//RESTHelper.updateJIRAStatus("HMF2-975","Failed");
		//report.addFailedLog(testName+" Failed",filePath);
		//report.addFailedLog(testName+" Failed");
		//RESTHelper.updateJIRAStatus("BTY-340,BTY-341,BTY-427,BTY-428,OUS23-707,HFM2-976","Failed");
	}

	
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		String testName=result.getName();
		String filePath=Common.getscreenShotPathforReport(testName);
		report.addFailedLog(testName+" Failed",filePath);
	}

	
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		String testName=result.getName();
		String filePath=Common.getscreenShotPathforReport(testName);
		report.addFailedLog(testName+" Failed",filePath);
		//RESTHelper.updateJIRAStatus("BTY-340,BTY-341,BTY-427,BTY-428,OUS23-707,HFM2-976","Failed");
		
	}

	
	
	
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		report.extent.flush();
		String videoStatus=Automation_properties.getInstance().getProperty("VideoRecord");
		if(videoStatus!=null)
		{	
		if(videoStatus.toLowerCase().equalsIgnoreCase("Yes"))
		{	
		video.stopVideoCapture(context.getName());
		}
		}
		
	}

	@Override
	public void onFinish(ISuite suite) {
		// TODO Auto-generated method stub
		report.extent.flush();
	}

	

}
