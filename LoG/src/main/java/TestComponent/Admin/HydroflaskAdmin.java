package TestComponent.Admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import TestLib.Common;
import TestLib.Login;
import TestLib.Sync;
import Utilities.ExcelReader;
import Utilities.ExtenantReportUtils;
import Utilities.Listener;
import Utilities.StatusMail;
import models.admin.HealthCheck;

//@Listeners(Listener.class)
public class HydroflaskAdmin {

	Map<String, Map<String, String>> data = new HashMap<>();
	static ExtenantReportUtils report;
	static Map<String, Map<String,String> > cronJobData=new HashMap<String, Map<String,String>>();
	static Map<String, Map<String,String> > orderData=new HashMap<String, Map<String,String>>();
	static Map<String, Map<String,String> > importOrder=new HashMap<String, Map<String,String>>();
	static Map<String, Map<String,String> > exportOrder=new HashMap<String, Map<String,String>>();
	static Map<String, Map<String,String> > indexMangment=new HashMap<String, Map<String,String>>();
	static  Map<String,String> GoogleInsight=new HashMap<String,String>();
	static  Map<String,Boolean> helthCheckStatus=new HashMap<String,Boolean>();
	
	
	HealthCheck health=new HealthCheck();
	
	String datafile = "DryBar//DryBarTestData.xlsx";	
	
	@Test(retryAnalyzer = Utilities.RetryAnalyzer.class)
	
	public void createAccount() throws Exception {

		try {
	      AdminLogin();
			//dateValidaiton();
	        health.setSiteName("hydroflask");
	       
	       //CronJob Status
	        navigateCronTaskList();
	        GetCronTaskStatus();
	        cronJobStatus();
	        
	        
	       //ExportExecutionlog
	        navigateExportExecutionLog();
	        GetOrderExportStatus();
	        OrderExportProcess();
	        
	        
	        //OrderImport
	        navigateImportExecutionLog();
	        GetOrderImportStatus();
	        OrderImportProcess();
	         
	         //Index management
	        navigateIndexManagement();
	        GetIndexManagement();
	        IndexMangementStatus();
	        
	        //OrdersInformation
	       /* navigateOrders();
	        Thread.sleep(2000);
	        GetOrderStatus();
	        OrderCronStatus();*/
	       
	        //Processing Order
	        navigateOrders();
	        /* GetOrderStatus("Processing","Canada");
		       System.out.println(OrderCronStatus());*/
		       
		       GetOrderStatus("Processing","Default Store View");
		       OrderCronStatus();
		       
		       GetOrderStatus("Pending","Default Store View");
		       OrderCronStatus();
		       
		      /* GetOrderStatus("Pending","Canada");
		       System.out.println(OrderCronStatus());
		       
		       GetOrderStatus("Pending","Default Store View");
		       System.out.println(OrderCronStatus());*/
		       
		      //Googleinsight
	       checkGoogleInsights("https://www.hydroflask.com/");
	        //Listener.healthCheck.put("Hydroflask", health);
	       // System.out.println(Listener.healthCheck);
	        
		}
		catch (Exception e) {
			
			Assert.fail(e.getMessage(), e);
		} 
		finally {
			Listener.healthCheck.put("Hydroflask", health);
			StatusMail.sendHealthCheckReport();
		}
	}
	
public HydroflaskAdmin() {
	
		
	/*	excelData = new ExcelReader(datafile);
		data = excelData.getExcelValue();
		this.data = data;*/
		if (Utilities.TestListener.report == null) {
			report = new ExtenantReportUtils("Hydro");
			report.createTestcase("HydroTestCases");
		} else {
			this.report = Utilities.TestListener.report;
		}
	}


	public long dateValidaiton(String dateValue) throws ParseException
	{	
		//String dateValue="Oct 20, 2020 8:16:17 AM";
		SimpleDateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
		Date date = format.parse(dateValue);
		Date todayDate = new Date();
		long diffInMillies = Math.abs(todayDate.getTime() - date.getTime());
	    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	    System.out.println(diff);
	    return diff;
		
	}
	public void checkGoogleInsights(String url) throws Exception
	{
		try {
		Common.getDriver().get("https://developers.google.com/speed/pagespeed/insights/");
		Common.textBoxInput("xpath", "//input[@name='url']", url);
		Common.actionsKeyPress(Keys.ENTER);
		Common.isElementVisibleOnPage(60, "xpath", "//*[@id=\"page-speed-insights\"]/div[2]/div[2]/div[2]/div[1]/div[1]/div/div[1]/a/div[2]");
		String mobile=Common.getText("xpath", "//*[@id=\"page-speed-insights\"]/div[2]/div[2]/div[2]/div[1]/div[1]/div/div[1]/a/div[2]");
		Common.clickElement("xpath", "//div[text()='Desktop']");
		String desktop=Common.getText("xpath", "//*[@id=\"page-speed-insights\"]/div[2]/div[2]/div[2]/div[2]/div[1]/div/div[1]/a/div[2]");
		System.out.println("mobile=======>"+mobile);
		System.out.println("mobile=======>"+desktop);
		health.setGoogleInsightMobile(Integer.parseInt(mobile));
		health.setGoogleInsightWeb(Integer.parseInt(desktop));
		GoogleInsight.put("Mobile", mobile);
		GoogleInsight.put("Desktop", desktop);
		}
		catch(Exception e)
		{
			health.setGoogleInsightMobile(0);
			health.setGoogleInsightWeb(0);
		}
	}
	public void AdminLogin() throws Exception
	{
		Common.textBoxInput("id", "username", "manojk");
		Common.textBoxInput("id", "login", "b{?e\\Gm=c8qDH9p!");
		Common.clickElement("xpath", "//button[@class='action-login action-primary']");
		Sync.waitElementPresent("xpath", "//span[text()='Dashboard']");
	}
	
	public void GetCronTaskStatus() throws Exception
	{	
		Thread.sleep(5000);
		List<WebElement> rows=Common.findElements("xpath", "//*[@id='container']/div/div[3]/table/tbody/tr[*]");
		Map<String, Map<String,String> > cronData=new HashMap<String, Map<String,String>>();
		if(rows.size()<1)
		{
			Thread.sleep(5000);
			rows=Common.findElements("xpath", "//*[@id='container']/div/div[3]/table/tbody/tr[*]");	
		}
		for(int i=1;i<=rows.size();i++)
		{
			Map<String,String> data=new HashMap();
			List<WebElement> columns=Common.findElements("xpath", "//*[@id='container']/div/div[3]/table/tbody/tr["+i+"]/td");
			for(int j=2;j<=columns.size();j++)
			{
				String header=Common.getText("xpath", "//*[@id='container']/div/div[3]/table/thead/tr/th["+j+"]/span");
				String value=Common.getText("xpath", "//*[@id='container']/div/div[3]/table/tbody/tr["+i+"]/td["+j+"]");
				if(!data.containsKey(header))
				{
					data.put(header, value);
				}
			}
			cronData.put(data.get("Job Code"), data);
			
		}
		cronJobData=cronData;
		System.out.println(cronData);
	}
		
		public boolean cronJobStatus()
		{
			boolean status=true;
			
			Set<String> keys=cronJobData.keySet();
			//DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
			//Date date = format.parse(string);
			for(String key:keys)
			{
				String statusValue=cronJobData.get(key).get("Status");
				if(!statusValue.equalsIgnoreCase("pending") || statusValue.equalsIgnoreCase("success"))
				{
					status=false;
					break;
				}
			}
			helthCheckStatus.put("cronJobStatus", status);
			if(status)
			{	
			health.setCronJob("Passed");
			}
			else {
				health.setCronJob("Failed");
			}
			return status;
			
		}
		public void GetOrderExportStatus() throws Exception
		{	
			Thread.sleep(5000);
			List<WebElement> rows=Common.findElements("xpath", "//*[@id='xtento_orderexport_log_grid_table']/tbody/tr[*]");
			Map<String, Map<String,String> > cronData=new HashMap<String, Map<String,String>>();
			for(int i=1;i<=rows.size();i++)
			{
				Map<String,String> data=new HashMap();
				List<WebElement> columns=Common.findElements("xpath", "//*[@id='xtento_orderexport_log_grid_table']/tbody/tr["+i+"]/td");
				for(int j=2;j<=columns.size();j++)
				{
					String header=Common.getText("xpath", "//*[@id='xtento_orderexport_log_grid_table']/thead/tr/th["+j+"]/span");
					String value=Common.getText("xpath", "//*[@id='xtento_orderexport_log_grid_table']/tbody/tr["+i+"]/td["+j+"]");
					data.put(header, value);
				}
				cronData.put(data.get("Export Type"), data);
				
			}
			exportOrder=cronData;
		System.out.println(cronData);
	}
		
		public void GetOrderImportStatus() throws Exception
		{	
			Thread.sleep(5000);
			List<WebElement> rows=Common.findElements("xpath", "//*[@id='xtento_trackingimport_log_grid_table']/tbody/tr[*]");
			Map<String, Map<String,String> > cronData=new HashMap<String, Map<String,String>>();
			for(int i=1;i<=rows.size();i++)
			{
				Map<String,String> data=new HashMap();
				List<WebElement> columns=Common.findElements("xpath", "//*[@id='xtento_trackingimport_log_grid_table']/tbody/tr["+i+"]/td");
				for(int j=2;j<=columns.size();j++)
				{
					String header=Common.getText("xpath", "//*[@id='xtento_trackingimport_log_grid_table']/thead/tr/th["+j+"]/span");
					String value=Common.getText("xpath", "//*[@id='xtento_trackingimport_log_grid_table']/tbody/tr["+i+"]/td["+j+"]");
					data.put(header, value);
				}
				cronData.put(data.get("Export Type"), data);
				
			}
			importOrder=cronData;
		System.out.println(importOrder);
	}
		
		public boolean OrderExportProcess()
		{
			boolean status=true;
			
			Set<String> keys=exportOrder.keySet();
			//DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
			//Date date = format.parse(string);
			for(String key:keys)
			{
				String statusValue=exportOrder.get(key).get("Result");
				if(! statusValue.equalsIgnoreCase("SUCCESS"))
				{
					status=false;
					break;
				}
			}
			helthCheckStatus.put("orderExport", status);
			if(status)
			{	
			health.setOrderExport("Passed");
			}
			else {
				health.setOrderExport("Failed");
			}
			return status;
			
		}
		
		public boolean OrderImportProcess()
		{
			boolean status=true;
			
			Set<String> keys=importOrder.keySet();
			//DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
			//Date date = format.parse(string);
			for(String key:keys)
			{
				String statusValue=importOrder.get(key).get("Result");
				if(! statusValue.equalsIgnoreCase("SUCCESS"))
				{
					status=false;
					break;
				}
			}
			helthCheckStatus.put("orderExport", status);
			if(status)
			{	
			health.setOrderImport("Passed");
			}
			else {
				health.setOrderImport("Failed");
			}
			return status;
			
		}
		
		public boolean OrderCronStatus() throws ParseException
		{
			boolean status=true;
			String date=Common.getText("xpath", "//*[@id='container']/div/div[4]/table/tbody/tr[1]/td[4]/div");
			if(dateValidaiton(date)>5)
			{
				return false;
			}
		
			Set<String> keys=orderData.keySet();
			//DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
			//Date date = format.parse(string);
			if(keys.size()>0) {
			for(String key:keys)
			{
				String statusValue=orderData.get(key).get("Status");
				if(!statusValue.equalsIgnoreCase("Processing") || statusValue.equalsIgnoreCase("success"))
				{
					status=false;
					break;
				}
				
			}}
			if(status)
			{	
			health.setOrderTrackingStatus("Passed");
			}
			else {
				health.setOrderTrackingStatus("Failed");
			}
			helthCheckStatus.put("OrderCronStatus", status);
			return status;
			
		}
		
		
		public boolean IndexMangementStatus()
		{
			boolean status=true;
			
			Set<String> keys=indexMangment.keySet();
			//DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
			//Date date = format.parse(string);
			for(String key:keys)
			{
				String statusValue=indexMangment.get(key).get("Status");
				if(! statusValue.equalsIgnoreCase("READY"))
				{
					status=false;
					break;
				}
			}
			helthCheckStatus.put("IndexManagement", status);
			if(status)
			{	
			health.setIndexManagement("Passed");
			}
			else {
				health.setIndexManagement("Failed");
			}
			return status;
			
		}
		
		public void GetIndexManagement() throws Exception
		{	
			Thread.sleep(3000);
			List<WebElement> rows=Common.findElements("xpath", "//*[@id='gridIndexer_table']/tbody/tr[*]");
			Map<String, Map<String,String> > cronData=new HashMap<String, Map<String,String>>();
			for(int i=1;i<=rows.size();i++)
			{
				Map<String,String> data=new HashMap();
				List<WebElement> columns=Common.findElements("xpath", "//*[@id='gridIndexer_table']/tbody/tr["+i+"]/td");
				for(int j=2;j<=columns.size();j++)
				{
					String header=Common.getText("xpath", "//*[@id='gridIndexer_table']/thead/tr/th["+j+"]/span");
					String value=Common.getText("xpath", "//*[@id='gridIndexer_table']/tbody/tr["+i+"]/td["+j+"]");
					data.put(header, value);
				}
				cronData.put(data.get("Indexer"), data);
				
			}
			indexMangment=cronData;
		System.out.println(cronData);
	}
	
		public void GetOrderStatus() throws Exception
		{	
			Thread.sleep(5000);
			List<WebElement> rows=Common.findElements("xpath", "//*[@id='container']/div/div[4]/table/tbody/tr[*]");
			Map<String, Map<String,String> > cronData=new HashMap<String, Map<String,String>>();
			for(int i=1;i<=rows.size();i++)
			{
				Map<String,String> data=new HashMap();
				List<WebElement> columns=Common.findElements("xpath", "//*[@id='container']/div/div[4]/table/tbody/tr["+i+"]/td");
				for(int j=2;j<=columns.size();j++)
				{
					String header=Common.getText("xpath", "//*[@id='container']/div/div[4]/table/thead/tr/th["+j+"]/span");
					String value=Common.getText("xpath", "//*[@id='container']/div/div[4]/table/tbody/tr["+i+"]/td["+j+"]");
					data.put(header, value);
				}
				cronData.put(data.get("Purchase Point"), data);
				
			}
		System.out.println(cronData);
		orderData=cronData;
	}
	
		public String GetOrderStatus(String status, String view) throws Exception
		{	
			Thread.sleep(3000);
			String strorevalue="1";
			if(view.equalsIgnoreCase("Default Store View"))	
			{
				strorevalue="1";
			}
			if(view.equalsIgnoreCase("Canada"))	
			{
				strorevalue="3";
			}
			
			Common.clickElement("xpath", "//button[text()='Filters']");
			Common.dropdown("xpath", "//select[@name='store_id']", Common.SelectBy.VALUE, strorevalue);
			//Common.actionsKeyPress(Keys.ARROW_DOWN);
			//Thread.sleep(2000);
			Common.dropdown("xpath", "//select[@name='status']", Common.SelectBy.TEXT, status);
			Common.clickElement("xpath", "//span[text()='Apply Filters']");
			Thread.sleep(5000);
			String date=Common.getText("xpath", "//*[@id='container']/div/div[4]/table/tbody/tr[1]/td[4]/div");
			System.out.println("Date----->"+date);
			if(dateValidaiton(date)>5)
			{
				Common.clickElement("xpath", "//*[@id='container']/div/div[4]/table/thead/tr/th[4]");
				Thread.sleep(4000);
				if(Common.getText("xpath", "//*[@id='container']/div/div[4]/table/tbody/tr[1]/td[4]/div")==date)
				{
					Common.clickElement("xpath", "//*[@id='container']/div/div[4]/table/thead/tr/th[4]");
					Thread.sleep(4000);
				}
			}
			String noOfRecords=Common.getText("xpath", "//*[@id='container']/div/div[3]/div/div[1]/div/div[2]");
			noOfRecords=noOfRecords.split("records found")[0];//.split("records found")[0].trim();
			List<WebElement> rows=Common.findElements("xpath", "//*[@id='container']/div/div[4]/table/tbody/tr[*]");
			
			Map<String, Map<String,String> > cronData=new HashMap<String, Map<String,String>>();
			for(int i=1;i<=rows.size();i++)
			{
				Map<String,String> data=new HashMap();
				List<WebElement> columns=Common.findElements("xpath", "//*[@id='container']/div/div[4]/table/tbody/tr["+i+"]/td");
				for(int j=2;j<=columns.size();j++)
				{
					String header=Common.getText("xpath", "//*[@id='container']/div/div[4]/table/thead/tr/th["+j+"]/span");
					String value=Common.getText("xpath", "//*[@id='container']/div/div[4]/table/tbody/tr["+i+"]/td["+j+"]");
					data.put(header, value);
				}
				cronData.put(data.get("Purchase Point"), data);
				
			}
			 noOfRecords=Common.getText("xpath", "//*[@id='container']/div/div[3]/div/div[1]/div/div[2]");
			noOfRecords=noOfRecords.split("records found")[0];//.split("records found")[0].trim();
		
			System.out.println("no of orders=====>"+noOfRecords);
			noOfRecords=noOfRecords.trim();
			if(status.equalsIgnoreCase("Pending"))
			{
				//health.setOrderTrackingCurrent(Integer.parseInt(noOfRecords));
				health.setOrderTrackingCurrent(300);
			}else {
				health.setOrderTrackingPrevious(250);
			//health.setOrderTrackingPrevious(Integer.parseInt(noOfRecords));
				}
		System.out.println(cronData);
		orderData=cronData;
		return noOfRecords;
	}
	
	public void navigateCronTaskList() throws InterruptedException
	{
	
		Common.clickElement("xpath", "//span[text()='Reports']");
		Thread.sleep(2000);
		Common.clickElement("xpath", "//*[@id='menu-magento-reports-report']/div/ul/li[5]/ul/li[2]/div/ul/li[5]/a");
	}
	
	public void navigateOrders() throws InterruptedException
	{
		Common.clickElement("xpath", "//span[text()='Sales']");
		Thread.sleep(2000);
		Common.clickElement("xpath", "//*[@id='menu-magento-sales-sales']/div/ul/li/ul/li[1]/div/ul/li[1]/a");
	}
	
	public void navigateIndexManagement() throws InterruptedException
	{
		Common.clickElement("xpath", "//span[text()='System']");
		Thread.sleep(2000);
		Common.clickElement("xpath", "//*[@id='menu-magento-backend-system']/div/ul/li[2]/ul/li[1]/div/ul/li[2]/a");
	}
	
	public void navigateExportExecutionLog() throws InterruptedException
	{
		Common.clickElement("xpath", "//span[text()='Sales']");
		Thread.sleep(2000);
		Common.clickElement("xpath", "//*[@id='menu-magento-sales-sales']/div/ul/li/ul/li[2]/div/ul/li[2]/a");
	}
	
	public void navigateImportExecutionLog() throws InterruptedException
	{
		Common.clickElement("xpath", "//span[text()='Sales']");
		Thread.sleep(2000);
		Common.clickElement("xpath", "//*[@id='menu-magento-sales-sales']/div/ul/li/ul/li[3]/div/ul/li[2]/a");
	}
	@AfterTest
	public void clearBrowser()
	{
		//Common.closeAll();

	}
	
	@BeforeTest
	  public void startTest() throws Exception {
		 System.setProperty("configFile", "Hydroflask\\AdminConfig.properties");
		  Login.signIn();
		 
		  
	  }

}
