package Utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import TestLib.Automation_properties;

public class generateHTMLReport {

	final public static Logger logger = Logger.getLogger(generateHTMLReport.class);	

	public static String starttime;
	public static String endtime;
	public static String Exetime;	
	public static int DBRowID;

	public static int totExe_time=0;

	public static String consol_testPassRate,consol_testPassPercentage;

	public static String exception(String exceptionname)
	{
		String exceptionString=null;
		switch(exceptionname)
		{
		case "org.openqa.selenium.remote.UnreachableBrowserException":
			exceptionString="Browser became unresponsive and not responding";
			break;
		case "java.lang.AssertionError":
			exceptionString="Data validation failure";
			break;
		case "java.io.IOException":
			exceptionString="Unable to perform read/write on test data file";
			break;
		case "org.openqa.selenium.UnhandledAlertException":
			exceptionString="Unhandled exception causing script to fail";
			break;
		case "java.no.file.NoSuchFileException":
			exceptionString="Trying to access a file that does not exist";
			break;
		case "org.openqa.selenium.WebDriverException":   
			exceptionString="Unable to invoke Web driver component/action";
			break;
		case "org.openqa.selenium.InvalidSelectorException":
			exceptionString="InvalidSelectorException";
			break;
		case "org.openqa.selenium.TimeoutException":  
			exceptionString="TimeoutException";
			break;
		case "org.openqa.selenium.NoSuchWindowException":
			exceptionString="Window cannot be found Exception";
			break;
		case "org.openqa.selenium.ElementNotVisibleException":
			exceptionString="Null pointer Exception";
			break;
		case "org.openqa.selenium.NullPointerException":
			exceptionString="Null pointer Exception";
			break;
		case "java.lang.NullPointerException":
			exceptionString="Java Null pointer Exception";
			break;
		default :
			exceptionString="Unknown exception";

		}		
		return exceptionString;

	}

	public static String generateHTML() throws Exception{	


		File file = new File(System.getProperty("user.dir")+"\\target\\surefire-reports\\testng-results.xml");
		// file = new File(System.getProperty("user.dir")+"\\test-output\\testng-results.xml");
		if(!file.exists())
		{
			file = new File(System.getProperty("user.dir")+"/test-output/testng-results.xml");
		}
		String values,strtestHTML = "";

		if(file.getName().endsWith(".xml")){

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);

			String suiteName = "suite";
			String testName = "test";
			String className = "class";
			String testmethod="test-method";

			String teststatus="status";

			String browser,strstepHTML,exceptionname="";


			NodeList resultNode = doc.getElementsByTagName("testng-results");

			for (int i = 0; i < resultNode.getLength(); i++) 
			{
				Node rNode = resultNode.item(i);

				Element resultElement = (Element) rNode;

				String skipped = resultElement.getAttribute("skipped");
				String failed = resultElement.getAttribute("failed");
				String total = resultElement.getAttribute("total");
				String passed = resultElement.getAttribute("passed");
				/*PieChartGenerator pie=new PieChartGenerator();
				pie.setTestPassed(passed);
				pie.setTestSkiped(skipped);
				pie.setTestFailed(failed);*/
				
				NodeList sList = doc.getElementsByTagName(suiteName);
				for (int temp = 0; temp < sList.getLength(); temp++) 
				{
					Node suiteNode = sList.item(temp);

					Element suiteElement = (Element) suiteNode;	
					Exetime = suiteElement.getAttribute("duration-ms");
					String Suite = suiteElement.getAttribute("name");
					starttime = suiteElement.getAttribute("started-at");
					endtime = suiteElement.getAttribute("finished-at");

					NodeList nList = suiteElement.getElementsByTagName(testName);


					for (int k = 0; k < nList.getLength(); k++) 
					{


						Node testNode = nList.item(k);

						Element tableElement = (Element) testNode;	


						NodeList testList = tableElement.getElementsByTagName(className);
						for (int j = 0; j < testList.getLength(); j++) 
						{

							String testscriptname = tableElement.getAttribute("name");
							String testexetime = tableElement.getAttribute("duration-ms");
							long minutes = TimeUnit.MILLISECONDS.toMinutes(Long.valueOf(testexetime).longValue());
							if(minutes<1){
								minutes = TimeUnit.MILLISECONDS.toSeconds(Long.valueOf(testexetime).longValue());
								//testexetime=String.valueOf(minutes)+" Secs";
								testexetime="1 Mins";
							}
							else{
								testexetime=String.valueOf(minutes)+" Mins";
							}


							Node classNode = testList.item(j);
							Element classElement = (Element) classNode;				         
							String classname = classElement.getAttribute("name");


							NodeList testmethodList = tableElement.getElementsByTagName(testmethod);
							loopmethod:
								for (int l = 0; l < testmethodList.getLength(); l++) 
								{


									Node testmethodNode = testmethodList.item(l);
									Element testmethodElement = (Element) testmethodNode;				         
									String testmethodname1 = testmethodElement.getAttribute("name");
									browser=getCDataFromElement(testmethodElement);
									System.out.println(browser);
									values=testmethodElement.getAttribute(teststatus);

									ArrayList<String> stringList = new ArrayList<String>();
									stringList =generateHTMLReport.getTCdetails(k);
									System.out.println("Value of K: "+k);
									String testids = stringList.get(0);
									String testdesc = stringList.get(1);
									String priority = stringList.get(2);
									String module = stringList.get(3);
									String[] TCcount = testids.split(",");

									if(values.equals("PASS")){
										if(l==testmethodList.getLength()-1){
											strstepHTML = reporterpass();				
											strstepHTML=strstepHTML.replace("#ScriptName#", testscriptname).replace("#Exetime#", testexetime).replace("#TestID#", testids).replace("#Priority#", priority).replace("#TestDesc#", testdesc).replace("#Module#", module).replace("#reportName#", testscriptname);
											strtestHTML=strtestHTML+strstepHTML;
										}

									}else{

										NodeList exceptionList = testmethodElement.getElementsByTagName("exception");
										for (int m = 0; m < exceptionList.getLength(); m++) 
										{
											Node exceptionNode = exceptionList.item(m);
											Element exceptionElement = (Element) exceptionNode;				         
											exceptionname = exceptionElement.getAttribute("class");

											exceptionname =exception(exceptionname);

										}

										
										strstepHTML = reporterfail();
										strstepHTML=strstepHTML.replace("#Exception#", exceptionname).replace("#ScriptName#", testscriptname).replace("#Exetime#", testexetime).replace("#TestID#", testids).replace("#Priority#", priority).replace("#TestDesc#", testdesc).replace("#Module#", module).replace("#reportName#", testscriptname);;
										strtestHTML=strtestHTML+strstepHTML;
										StatusMail.totalTCcount =StatusMail.totalTCcount+TCcount.length;
										StatusMail.failTCcount =StatusMail.failTCcount+TCcount.length;
									
										break loopmethod;
									}								

								}	

						}					

					}

				}
			}

		}
		testPassPercentage();
		return strtestHTML;
	}

	public static String reporterfail(){
		String strHTML = "";
		strHTML = strHTML + "<TR>";
		strHTML = strHTML + "<TD bgcolor='#F3F3FF' height=50px ALIGN='Center'><font face=Calibri size=3 color=red>#TestID#</font></TD> ";
		strHTML = strHTML + "<TD bgcolor='#F3F3FF' height=50px ALIGN='Center'><font face=Calibri size=3 color=red>#Priority#</font></TD>";
		strHTML = strHTML + "<TD bgcolor='#F3F3FF' height=50px ALIGN='Center'><font face=Calibri size=3 color=red>#Module#</font></TD>";
		strHTML = strHTML + "<TD bgcolor='#F3F3FF' height=50px><font face=Calibri size=3 color=red>#ScriptName#</font></TD> ";
		strHTML = strHTML + "<TD bgcolor='#F3F3FF' height=50px><font face=Calibri size=3 color=red>#TestDesc#</font></TD>";
		strHTML = strHTML + "<TD bgcolor='#F3F3FF' height=50px ALIGN='Center'><font face=Calibri size=3 color=red>Fail</font></TD> ";
		strHTML = strHTML + "<TD bgcolor='#F3F3FF' height=50px ALIGN='Center'><font face=Calibri size=3 color=red>#Exception#</font></TD> ";
		strHTML = strHTML + "<TD bgcolor='#F3F3FF' height=50px ALIGN='Center'><font face=Calibri size=3 color=red>#Exetime#</font></TD>";
		strHTML = strHTML + "<TD bgcolor='#F3F3FF' height=50px ALIGN='Center'><font face=Calibri size=3 color=red><a href=\"D:/Lotus/Automation%20Code/HoT-digital/TestLogs/ExtenantReport/#reportName#.html\">Detail Report</a></font></TD>";
		strHTML = strHTML + "</TR>";		
		return strHTML;

	}

	public static String reporterpass(){
		String strHTML = "";
		strHTML = strHTML + "<TR>";
		strHTML = strHTML + "<TD bgcolor='#F3F3FF' height=50px ALIGN='Center'><font face=Calibri size=3 color=green>#TestID#</font></TD> ";
		strHTML = strHTML + "<TD bgcolor='#F3F3FF' height=50px ALIGN='Center'><font face=Calibri size=3 color=green>#Priority#</font></TD>";
		strHTML = strHTML + "<TD bgcolor='#F3F3FF' height=50px ALIGN='Center'><font face=Calibri size=3 color=green>#Module#</font></TD>";
		strHTML = strHTML + "<TD bgcolor='#F3F3FF' height=50px><font face=Calibri size=3 color=green>#ScriptName#</font></TD> ";
		strHTML = strHTML + "<TD bgcolor='#F3F3FF' height=50px><font face=Calibri size=3 color=green>#TestDesc#</font></TD>";
		strHTML = strHTML + "<TD bgcolor='#F3F3FF' height=50px ALIGN='Center'><font face=Calibri size=3 color=green>Pass</font></TD> ";
		strHTML = strHTML + "<TD bgcolor='#F3F3FF' height=50px ALIGN='Center'><font face=Calibri size=3 color=red></font></TD> ";
		strHTML = strHTML + "<TD bgcolor='#F3F3FF' height=50px ALIGN='Center'><font face=Calibri size=3 color=green>#Exetime#</font></TD>";
		strHTML = strHTML + "<TD bgcolor='#F3F3FF' height=50px ALIGN='Center'><font face=Calibri size=3 color=red><a href=\"D:/Lotus/Automation%20Code/HoT-digital/TestLogs/ExtenantReport/#reportName#.html\">Detail Report</a></font></TD>";
		strHTML = strHTML + "</TR>";		
		return strHTML;

	}

	public static ArrayList<String> getTCdetails(int i) throws Exception{


		ArrayList<String> strList = new ArrayList<String>();

		File file = new File(TestLib.Automation_properties.getTestNGFilePath());

		if(file.getName().endsWith(".xml")){

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);

			String suiteName = "suite";
			String testName = "test";

			NodeList sList = doc.getElementsByTagName(suiteName);
			for (int temp = 0; temp < sList.getLength(); temp++) 
			{
				Node suiteNode = sList.item(temp);
				Element suiteElement = (Element) suiteNode;	
				NodeList nList = suiteElement.getElementsByTagName(testName);
				Node testNode = nList.item(i);
				Element tableElement = (Element) testNode;	
				String testids = tableElement.getAttribute("testid");
				String testdesc = tableElement.getAttribute("description");
				String priority = tableElement.getAttribute("priority");
				strList.add(testids);
				strList.add(testdesc);
				strList.add(priority);

				NodeList testList = tableElement.getElementsByTagName("classes");
				for (int j = 0; j < testList.getLength(); j++) 
				{
					Node classNode = testList.item(j);
					Element classElement = (Element) classNode;			
					NodeList testmethodList = classElement.getElementsByTagName("class");

					for (int l = 0; l < testmethodList.getLength(); l++) 
					{
						Node testmethodNode = testmethodList.item(l);
						Element testmethodElement = (Element) testmethodNode;				         
						String classname = testmethodElement.getAttribute("name");
						System.out.println(classname);							
						String[] module = classname.split("\\.");
						strList.add(module[1]);
						strList.add(classname);
					}
				}
			}
		}
		return strList;
	}

	public static String getCDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			System.out.println("CData is : "+cd.getData());
			return cd.getData();
		}
		return "Mankoo123";
	}

	public static void testPassPercentage(){
		String passRate =null;
		Float pcount,totcount;
		pcount = (float) StatusMail.passTCcount;
		totcount = (float) StatusMail.totalTCcount;
		try{

			passRate = String.format("%.0f", 100*(pcount/totcount));

			if(StatusMail.passTCcount==StatusMail.totalTCcount){

				StatusMail.testPassPercentage = " - All tests passed";
				StatusMail.testPassRate = passRate+"% ("+StatusMail.passTCcount+"/"+StatusMail.totalTCcount+")";

			}else if(StatusMail.failTCcount==StatusMail.totalTCcount) {

				StatusMail.testPassPercentage = " - All tests failed";
				StatusMail.testPassRate = passRate+"% ("+StatusMail.passTCcount+"/"+StatusMail.totalTCcount+")";

			}else{

				StatusMail.testPassPercentage = " - "+StatusMail.failTCcount+" out of "+StatusMail.totalTCcount+" tests failed ";
				StatusMail.testPassRate = passRate+"% ("+StatusMail.passTCcount+"/"+StatusMail.totalTCcount+")";

			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e);
		}



	}

	


	public static void copyFiletoDest(File source, File dest)	throws IOException {
		FileUtils.copyFile(source, dest);
		//Driver.getLogger().info("Copy success");
	}




	public static String extracthtml_details(String fileloc) throws Exception{

		String s="Manoj";

		FileReader fr=new FileReader(fileloc);
		BufferedReader br= new BufferedReader(fr);
		String content="";
		while((s=br.readLine())!=null)
		{

			content=content+s;

		} 
		System.out.println("Mnakoo"+content);
		br.close();

		String Str = new String(content);
		String arr[]=Str.split("<body>", 2);
		System.out.println("Mank text: "+arr[1]);

		String rawbody = arr[1];
		String arrbody[]=rawbody.split("</body>", 2);
		String body=arrbody[0];

		System.out.println("Mank text: "+body);
		return body;	
	}

	public static String gethtmlfiles(String path) throws Exception{
		String strHTML="";

		//path = "\\C:\\QA\\Mchiruvella\\05-03-2015"; 

		String files;
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles(); 

		for (int i = 0; i < listOfFiles.length; i++) 
		{

			if (listOfFiles[i].isFile()) 
			{
				files = listOfFiles[i].getName();
				if(files.endsWith(".html")){
					System.out.println(files);
					strHTML=strHTML+extracthtml_details(path+"\\"+files);

				}
			}
		}

		return strHTML;

	}



}
