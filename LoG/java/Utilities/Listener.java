package Utilities;

import static com.jayway.restassured.RestAssured.given;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.nio.file.FileSystems;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.codehaus.jettison.json.JSONObject;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;



import com.google.common.io.Files;
import com.jayway.restassured.response.Response;

import TestLib.Automation_properties;
import TestLib.Common;
import TestLib.Driver;
import TestLib.Retry;
import models.admin.HealthCheck;


public class Listener implements  ISuiteListener,ITestListener {
	final public static log4j logger = Driver.getLogger();
	final public static Marker Suite=MarkerManager.getMarker("Suite");
	int executionstatus;
	public HashMap<String,String> startTime = new HashMap<String, String>();
	public HashMap<String,Integer> dataProviderCounter = new HashMap<String, Integer>();
	public Set<String> tcNames = new HashSet<String>(); 
	DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	String Exception ;
	static int rowID = 0, ID = 0;
	String StartTime, EndTime;
	int tcount=0;
	private List<String> failedCases = new ArrayList<String>();
	int numberOfPassedTest=0;
	int numberOfFailedTest=0;
	int numberOfSkipedTest=0;
	int totalNumberOfTestCases = 0;
	private GenerateFailedTestNG failedTestNG;

	public static volatile int sessionCount=1;
	public static int totalTestCases=0;
	 public static Map<String,HealthCheck> healthCheck=new HashMap<String,HealthCheck>();
	    public static boolean doHealthCheck=false;

	
	// This belongs to ISuiteListener and will execute before the Suite start
	@Override
	public synchronized void onStart(ISuite arg0) {
		System.setProperty("java.awt.headless", "false");
		StartTime = dateTimeFormat.format(new Date());
		logger.info(Suite,"***********************Suite Execution Started***********************");
		logger.info(Suite,"Suited Name	: "+arg0.getXmlSuite().getName());
		logger.info(Suite,"Suited Started : "+Instant.now());
		System.out.println("Parameter from xml file is : " + arg0.getParameter("configFile"));
		System.out.println("Parameter from jenkins file is : " + arg0.getParameter("configFile"));
		if(arg0.getParameter("configFile")!=null && !arg0.getParameter("configFile").equals("${property}")){
			String configFile=arg0.getParameter("configFile");
			Automation_properties.createInstance(configFile);
			System.out.println("Property file to read :::::::::::::"+ configFile);
		}
		if(arg0.getParameter("mailid")!=null){
			Automation_properties.getInstance().setProperty("Exestartmail", arg0.getParameter("mailid"));;
			Automation_properties.getInstance().setProperty("Exemail", arg0.getParameter("mailid"));;
		}
		Automation_properties.setTestNG(arg0.getXmlSuite().getFileName());
		try {
			ReadTestNGXMLFile readTestNGObj = new ReadTestNGXMLFile(Automation_properties.getTestNGFilePath());
			if(!arg0.getName().equalsIgnoreCase("ReRun"))
				failedTestNG=new GenerateFailedTestNG(readTestNGObj.getThreadCount());
			System.out.println("TestNG :::::::::::::"+Automation_properties.getTestNGFilePath());
			totalTestCases=readTestNGObj.getAllTestCaseNames().size();
			tcount = readTestNGObj.getThreadCount();
			cleanUpBeforeExecution(readTestNGObj); 
			File correlationID=new File(FileSystems.getDefault().getPath(System.getProperty("user.dir"), "TestLogs","Correlation.properties").toString() + File.separator);
			correlationID.createNewFile();
			try{
			//	mail.exemail();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			writeSummaryTable(true);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
		}
	}


	private void cleanUpBeforeExecution(ReadTestNGXMLFile readTestNGObj) throws Exception {
		try {
			BackupUtils.cleanAutomationEnvironment();
			String downloadFilepath = System.getProperty("user.dir") + "/src/HA/TestData/" + readTestNGObj.getModuleName()
			      + "/Downloads";
			System.out.println(downloadFilepath);
			File file = new File(downloadFilepath);
			if(file.exists())
				if(file.isDirectory())
					FileUtils.cleanDirectory(file);
				else 
					FileUtils.forceDelete(file);
		} catch (java.lang.Exception e) {
			Driver.getLogger().info("Error while clearing downloads. Ignoring and continuing. Exception is ");
		}
	}


	// This belongs to ISuiteListener and will execute, once the Suite is finished
	@Override
	public synchronized void onFinish(ISuite arg0) {
		EndTime = dateTimeFormat.format(new Date());
		logger.info(Suite,"***********************Suite Execution Finished***********************");
		logger.info(Suite,"Suited Name	: "+arg0.getXmlSuite().getName());
		logger.info(Suite,"Suit Execution Finised	: "+Instant.now());
		try {
			Map<String,ISuiteResult> suiteResults=arg0.getResults();
			for(ISuiteResult sr:suiteResults.values()){
				ITestContext tc=sr.getTestContext();
				numberOfFailedTest+=tc.getFailedTests().getAllResults().size();
				numberOfPassedTest+=tc.getPassedTests().getAllResults().size();
				numberOfSkipedTest+=tc.getFailedTests().getAllResults().size()==0?tc.getSkippedTests().getAllResults().size():0;
				totalNumberOfTestCases = numberOfFailedTest + numberOfPassedTest+numberOfSkipedTest;
			}
			Iterator<String> failedIterator = failedCases.iterator();
			while(failedIterator.hasNext()){
				String testScriptName= failedIterator.next() ;
				File file=new File(System.getProperty("user.dir")+"/TestLogs/logs/"+testScriptName+".log");
				File renamefile=new File(System.getProperty("user.dir")+"/TestLogs/logs/failed_"+testScriptName+".log");
				if(file.exists()){
					try {
						Files.copy(file, renamefile);
						file.deleteOnExit();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			 if(doHealthCheck) {
	                StatusMail.sendHealthCheckReport();
	            }
//			System.out.println("GetClassNameHitCount"+HA.SeleniumLib.Common.getClassNameHitCount);
//			System.out.println("GetClassNameMetrics"+HA.SeleniumLib.Common.getClassNameMetrics);
			logger.info(Suite,"===============================================");
			logger.info(Suite,arg0.getName());
			logger.info(Suite,"Total test run: "+totalNumberOfTestCases +", Tests Passed: "+numberOfPassedTest+", Tests Failed: "+numberOfFailedTest+", Tests Skipped: "+numberOfSkipedTest);
			logger.info(Suite,"===============================================");
			LogManager.shutdown();
			if(failedTestNG!=null)
				failedTestNG.close();
		} catch (java.lang.Exception e) {
			// TODO Auto-generated catch block
			logger.info(Suite,"Exception encountered while updating the passed count.");
			e.printStackTrace();
		}
	}
	

	@Override
	public synchronized void onStart(ITestContext arg0) {
		Properties prop=new Properties();
		Thread.currentThread().setName(arg0.getCurrentXmlTest().getClasses().get(0).getName());
		logger.info(Suite,"***********************Test Execution Started***********************");
		logger.info(Suite,"Test Name	    : "+arg0.getCurrentXmlTest().getName());
		logger.info(Suite,"Test Started	: "+ Instant.now());
		executionstatus = 0;
		//arg0.getSuite().getXmlSuite().setThreadCount(NodeInfoFromGrid.getNumberOfNodes());
		try
		{
//		setClassName(arg0.getName());
		tcNames.add(getClassName());
		if(tcount==1)
		{
			VideoCapture.startVideoCapture();
		}
		Date date = new Date();
		startTime.put(getClassName(), dateTimeFormat.format(date));
		
		Driver.getLogger().info("About to begin executing Test: " + arg0.getName());
		}
		catch(Exception e){
			Driver.getLogger().info("Exception Occured in On Start TestContext");
			e.printStackTrace();
		}
	}
	
//	public void setClassName(String Name){
//		ClassName = Name;
//	}
	
	public String getClassName(){
		String[] className = Common.getCLassName().split("\\.");
		return className[className.length - 1];
	}

	@Override
	public synchronized void onFinish(ITestContext arg0) {
		logger.info(Suite,"***********************Test Execution Ended***********************");
		logger.info(Suite,"Test Name	    : "+arg0.getCurrentXmlTest().getName());
		logger.info(Suite,"Test Ended	: "+ Instant.now());
		try{
			//Printing the browser console logs to the debug log file
//			Common.getbrowserlog();
			ReadTestNGXMLFile readTestNGObj = new ReadTestNGXMLFile(Automation_properties.getTestNGFilePath());
			String className=arg0.getCurrentXmlTest().getClasses().get(0).getName();
			
		}
		catch(Exception e){
			e.printStackTrace();
			Driver.getLogger().info("Issue while unMapping tenant in Listener class");
		}
		String status = "Passed";
		Driver.getLogger().info("About to end executing Test: " + arg0.getName() + " with executionStatus: "+executionstatus);
		String videopath=null;
		if(tcount==1)
		{
		videopath = VideoCapture.stopVideoCapture(arg0.getName());
		}
		System.out.println("executionstatus : " +executionstatus);

		//Retry code start
		Iterator<ITestResult> skippedTestCases =arg0.getSkippedTests().getAllResults().iterator();
		while (skippedTestCases.hasNext()) {
			ITestResult skippedTestCase = skippedTestCases.next();
			ITestNGMethod method = skippedTestCase.getMethod();
			if (arg0.getFailedTests().getResults(method).size() > 1) {
				Driver.getLogger().info("failed test case remove as dup:" + skippedTestCase.getTestClass().toString());
				skippedTestCases.remove();
			} else {

				if (arg0.getPassedTests().getResults(method).size() > 0) {
					Driver.getLogger().info("failed test case remove as pass retry:" + skippedTestCase.getTestClass().toString());
					skippedTestCases.remove();
				}
			}
		}
		boolean result = (arg0.getFailedTests().getAllResults().isEmpty()) && (arg0.getSkippedTests().getAllResults().isEmpty());
		//retry code end
		if(result&& tcount==1 )
		{
			if(videopath!=null)
			{
				File f = new File(videopath);
				f.delete();
			}
		}	
	
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
	}

	@Override
	public void onTestFailure(ITestResult arg0) {
		executionstatus = arg0.getStatus();
		String loggerKey = arg0.getTestClass().getRealClass().getName();
		if(failedTestNG!=null)
			failedTestNG.addTestCase(loggerKey);
		failedCases.add(loggerKey);
		System.out.println("executionstatus : " +executionstatus);
		Driver.getLogger().info("Test Failed: " + arg0.getName());
		Driver.getLogger().info("Test Status: Test Failed");
		logger.info(Suite,"*********************Test Failed**********************");
		//logger.info(Suite,"Exception Trace :",arg0.getThrowable());
		//Retry code start
		if (arg0.getMethod().getRetryAnalyzer() != null) {
			Retry retryAnalyzer = (Retry)arg0.getMethod().getRetryAnalyzer();

			if(retryAnalyzer.isRetryAvailable()) {
				arg0.getTestContext().getFailedTests().removeResult(arg0.getMethod());
			} else {
				arg0.setStatus(ITestResult.SKIP);
			}
			Reporter.setCurrentTestResult(arg0);
		}
		
		//Retry code end
		if(Automation_properties.getInstance().getProperty("FailureMail")!=null)
		{
			HTMLPreparation.setFailedTestcaseDetails(arg0);
			try {
				//mailGeneration.sendMail("failure");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void onTestSkipped(ITestResult arg0) {
//		executionStatus.put(getClassName(), arg0.getStatus());
		executionstatus = arg0.getStatus();
		if (arg0.getMethod().getRetryAnalyzer() != null) {
			Retry retryAnalyzer = (Retry)arg0.getMethod().getRetryAnalyzer();

			if(retryAnalyzer.isRetryAvailable()) {
				arg0.getTestContext().getSkippedTests().removeResult(arg0.getMethod());
			} else {
				arg0.setStatus(ITestResult.SKIP);
			}
			Reporter.setCurrentTestResult(arg0);
		}
		try{
			Exception = arg0.getThrowable().getCause().toString();	
		}
		catch(Exception e){
			Driver.getLogger().info("Exception occured in reading the test case exception: ", e);
		}
	
		System.out.println("executionstatus : " +executionstatus);
		Driver.getLogger().info("Test Skipped: " + arg0.getInstanceName());
	}

	@Override
	public synchronized void onTestStart(ITestResult arg0) {
//		executionStatus.put(getClassName(), arg0.getStatus());
		Thread.currentThread().setName(arg0.getTestContext().getCurrentXmlTest().getClasses().get(0).getName());
		executionstatus = arg0.getStatus();
		System.out.println("executionstatus : " +executionstatus);
		Driver.getLogger().info("Test Started: " + arg0.getName());
	}

	@Override
	public void onTestSuccess(ITestResult arg0) {
		Driver.getLogger().info("Test Passed: " + arg0.getName());
	}

	
	
	public void writeSummaryTable(boolean isInsert) throws Exception{
		try
		{
		JSONObject json = new JSONObject(); 
		ReadTestNGXMLFile readTestNGObj = new ReadTestNGXMLFile(Automation_properties.getTestNGFilePath());
		String browserName=Automation_properties.getInstance().getProperty(Automation_properties.BROWSER);
		try{
			tcNames = readTestNGObj.getAllTestCaseNames();
		}
		catch(Exception e){
			Driver.getLogger().info("Exception occured while reading test script names from TestNG");
		}
		String version = "";
		if(browserName.equals("IE"))
		{
			ArrayList<String> output = new ArrayList<String>();
			Process p = Runtime.getRuntime().exec("reg query \"HKLM\\Software\\Microsoft\\Internet Explorer\" /v Version");
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()),8*1024);
			String s = null;
			System.out.println("Here is the standard output of the command:\n");	
			while ((s = stdInput.readLine()) != null)
				output.add(s);
			String internet_explorer_value = (output.get(2));
			version = internet_explorer_value.trim().split("   ")[2];
			version=version.trim().split("\\.")[1];
			System.out.println(version);
		}
		json.put("TableName", "automationexecutionsummary");
		if(isInsert)
			json.put("UpdateOrInsert", "1");
		else
			json.put("UpdateOrInsert", "2");
		json.put("Environment", Automation_properties.getInstance().getProperty(Automation_properties.BASEURL));
		json.put("OS", System.getProperty("os.name"));
		json.put("Browser", browserName+version);
		json.put("ExeStartTime", dateTimeFormat.format(new Date()));
		json.put("MachineName", InetAddress.getLocalHost().getHostName());
		json.put("Module", readTestNGObj.getModuleName());
		json.put("TotalTestsCount", readTestNGObj.getAllTestCaseIds().split(",").length+"");
		//json.put("SummaryFlag", mail.SUMMARYFLAG+"");
		Driver.getLogger().info("Json Object for Summary table : "+json.toString());
		
		}
		catch(Exception e){
			Driver.getLogger().info("Exception Occured while Writing to DB");
			e.printStackTrace();
		}
	}

}