package Utilities;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import TestLib.Automation_properties;




public class MailTestSuite {
	public static int rowID;
	private static final String classItrComboFilePath = System.getProperty("user.dir")+"\\TestLogs\\classIteratorMappings.properties";
	private static final String CorrelationMapFilePath =FileSystems.getDefault().getPath(System.getProperty("user.dir"), "TestLogs","Correlation.properties").toString() + File.separator;
	public static String generateExecutionHTML() throws Exception{	
		
		ReadTestNGData testNGObj = new ReadTestNGData(TestLib.Automation_properties.getTestNGFilePath());
		HashMap<String, String> tcIds = testNGObj.getAllTestCaseIDs();
		HashMap<String, String> tcDescs = testNGObj.getAllTestDescriptions();
		HashMap<String, String> modules = testNGObj.getAllModules();
		HashMap<String, String> priorities = testNGObj.getAllPriorities();
		//HashMap<String, String> correlationIDs = getCorrelationMap();
		boolean isDataProvider = false;

		
		File file = new File(System.getProperty("user.dir")+"/target/surefire-reports/testng-results.xml");
		//file = new File(System.getProperty("user.dir")+"/test-output/testng-results.xml");
		if(!file.exists())
		{
			file = new File(System.getProperty("user.dir")+"/test-output/testng-results.xml");
		}
		String values;
		String Exetime = "";
		String starttime="";
		String endtime="";

		if(file.getName().endsWith(".xml")){
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);

			String suiteName = "suite";
			String testName = "test";
			String className = "class";
			String testmethod="test-method";
			String teststatus="status";
			StringBuilder contentBuilder = new StringBuilder();
			contentBuilder.append("<tr><td align='middle' style='background:#f3f3f; border:1px solid #b6b6b6; font:normal 15px Calibri; color:#color#;'>#TestID#</td>");
			//contentBuilder.append("<td align='center' valign='middle' style='background:#f3f3f; border:1px solid #b6b6b6; font:normal 15px Calibri; color:#color#;'>#Priority#</td>");
			//contentBuilder.append("<td align='center' valign='middle' style='background:#f3f3f; border:1px solid #b6b6b6; font:normal 15px Calibri; color:#color#;'>#Module#</td>");
			contentBuilder.append("<td valign='middle' style='background:#f3f3f; border:1px solid #b6b6b6; font:normal 15px Calibri; color:#color#;'>#TestScriptName#</td>");
			contentBuilder.append("<td valign='middle' style='background:#f3f3f; border:1px solid #b6b6b6; font:normal 15px Calibri; color:#color#;'> #TestDesc#</td>");
			contentBuilder.append("<td align='center' valign='middle' style='background:#f3f3f; border:1px solid #b6b6b6; font:normal 15px Calibri; color:#color#;'>#Status#</td>");
			//contentBuilder.append("<td align='center' valign='middle' style='background:#f3f3f; border:1px solid #b6b6b6; font:normal 15px Calibri; color:#color#;'>#CorrelationID#</td>");
			contentBuilder.append("<td align='center' valign='middle' style='background:#f3f3f; border:1px solid #b6b6b6; font:normal 15px Calibri; color:#color#;'>#Remarks#</td>");
			contentBuilder.append("<td align='center' valign='middle' style='background:#f3f3f; border:1px solid #b6b6b6; font:normal 15px Calibri; color:#color#;'>#Exetime#</td>");
			//contentBuilder.append("<td align='center' valign='middle' style='background:#f3f3f; border:1px solid #b6b6b6; font:normal 15px Calibri; color:#color#;'><a href=\""+System.getProperty("user.dir")+"/TestLogs/ExtentReport/#reportName#.html\" target=\"_blank\">DetailReport</a></td></tr>");
			contentBuilder.append("<td align='center' valign='middle' style='background:#f3f3f; border:1px solid #b6b6b6; font:normal 15px Calibri; color:#color#;'><a href=\"ExtentReport/#reportName#.html\" target=\"_blank\">DetailReport</a></td></tr>");
			String Row=contentBuilder.toString();
			String exceptionname="";
			String dynamicRow;

			NodeList resultNode = doc.getElementsByTagName("testng-results");

			for (int i = 0; i < resultNode.getLength(); i++) 
			{
				Node rNode = resultNode.item(i);
				Element resultElement = (Element) rNode;

				String skipped = resultElement.getAttribute("skipped");
				String failed = resultElement.getAttribute("failed");
				String total = resultElement.getAttribute("total");
				String passed = resultElement.getAttribute("passed");

				System.out.println("No of test skipped: "+skipped);
				System.out.println("No of test failed: "+failed);
				System.out.println("No of test total: "+total);
				System.out.println("No of test passed: "+passed);

				NodeList sList = doc.getElementsByTagName(suiteName);
				for (int temp = 0; temp < sList.getLength(); temp++) 
				{
					Node suiteNode = sList.item(temp);

					Element suiteElement = (Element) suiteNode;	
					Exetime = HTMLPreparation.MilliSecondsToMinutes(Long.parseLong(suiteElement.getAttribute("duration-ms")));
					String Suite = suiteElement.getAttribute("name");
					starttime = suiteElement.getAttribute("started-at");
					endtime = suiteElement.getAttribute("finished-at");
					System.out.println("Exetime: "+Exetime);
					System.out.println("Suite Name: "+Suite);
					generateHTMLReport.starttime=starttime;
					generateHTMLReport.endtime=endtime;
					generateHTMLReport.Exetime=suiteElement.getAttribute("duration-ms");
					NodeList nList = suiteElement.getElementsByTagName(testName);
					for (int k = 0; k < nList.getLength(); k++) 
					{

						dynamicRow=Row;
						Node testNode = nList.item(k);
						Element tableElement = (Element) testNode;	
						String testscriptname =null;
						NodeList testList = tableElement.getElementsByTagName(className);
						for (int j = 0; j < testList.getLength(); j++) 
						{
							testscriptname = tableElement.getAttribute("name");
							String testexetime = HTMLPreparation.MilliSecondsToMinutes(Long.parseLong(tableElement.getAttribute("duration-ms")));
							System.out.println("Test Script name: "+testscriptname);
							System.out.println("Test Script Exetime: "+testexetime);

							Node classNode = testList.item(j);
							Element classElement = (Element) classNode;				         
							String execClassName = classElement.getAttribute("name");
							//Class<?> classObj = Class.forName(execClassName);

							
							

							System.out.println("Test class name: "+execClassName);

							if(isDataProvider == false){
								
								
								NodeList testmethodList = tableElement.getElementsByTagName(testmethod);
								loopmethod:
									for (int l = 0; l < testmethodList.getLength(); l++) 
									{
										Node testmethodNode = testmethodList.item(l);
										Element testmethodElement = (Element) testmethodNode;				         
										String testmethodname1 = testmethodElement.getAttribute("name");
										System.out.println("Test Method name: "+testmethodname1);
										values=testmethodElement.getAttribute(teststatus);

										System.out.println("Value of K: "+k);
										String testids = tcIds.get(testscriptname);
										String testdesc = tcDescs.get(testscriptname);
										String priority = priorities.get(testscriptname);
										String module = modules.get(testscriptname);
									//	String CorrelationID = correlationIDs.get(execClassName);
										int TCcount=1;
										if(testids.contains(","))
										{	
										TCcount = testids.split(",").length;
										}
										//dynamicRow=dynamicRow.replace("#TestScriptName#", testscriptname).replace("#Exetime#", testexetime).replace("#TestID#", testids).replace("#Priority#", priority).replace("#TestDesc#", testdesc).replace("#Module#", module).replace("#CorrelationID#", CorrelationID);
										//dynamicRow=dynamicRow.replace("#TestScriptName#", testscriptname).replace("#Exetime#", testexetime).replace("#TestID#", testids).replace("#Priority#", priority).replace("#TestDesc#", testdesc).replace("#Module#", module).replace("#reportName#", testscriptname);
										dynamicRow=dynamicRow.replace("#TestScriptName#", testscriptname).replace("#Exetime#", testexetime).replace("#TestID#", testids).replace("#TestDesc#", testdesc).replace("#reportName#", testscriptname);
										if(values.equals("PASS")){
											System.out.println("Test :"+testmethodElement.getAttribute("signature")+" is passed");
											if(l==testmethodList.getLength()-1){
												dynamicRow=dynamicRow.replaceAll("#color#","green").replace("#Remarks#","").replace("#Status#", "PASS");
												dynamicRow=dynamicRow.replaceAll("#color#","green").replace("#Status#", "PASS");
												StatusMail.totalTCcount =StatusMail.totalTCcount+TCcount;
												StatusMail.passTCcount =StatusMail.passTCcount+TCcount;
											}
										}
										else{
											NodeList exceptionList = testmethodElement.getElementsByTagName("exception");
											for (int m = 0; m < exceptionList.getLength(); m++) 
											{
												Node exceptionNode = exceptionList.item(m);
												Element exceptionElement = (Element) exceptionNode;				         
												exceptionname = exceptionElement.getAttribute("class");
												exceptionname =generateHTMLReport.exception(exceptionname);
												HashMap<String,String> logMap=null;
												//exceptionname="Failed to Complete while "+logMap.get("CONTEXT")+"<br/> <label for=\"show\">[Show]</label><label for=\"hide\">/ [Hide]</label><input type=radio id=\"show\" name=\"group\"/><input type=radio id=\"hide\" name=\"group\"/><div id=\"message\">Last Successful ACTION : "+logMap.get("ACTION")+"<br/> EXCEPTION : "+(logMap.containsKey("EXCEPTION")?logMap.get("EXCEPTION"):"")+"<br/> JAVA Exception: "+(logMap.containsKey("JAVAEXCEPTION")?logMap.get("JAVAEXCEPTION"):"")+"</div>";
												System.out.println("Exception :"+ generateHTMLReport.exception(exceptionname));
											}
											System.out.println("Test :"+testmethodElement.getAttribute("signature")+" is failed");
											System.out.println("Test :"+classElement.getAttribute("name")+" is failed");
											dynamicRow=dynamicRow.replaceAll("#color#","red").replace("#Remarks#",exceptionname).replace("#Status#","FAIL");
											dynamicRow=dynamicRow.replaceAll("#color#","red").replace("#Status#","FAIL");
											StatusMail.totalTCcount =StatusMail.totalTCcount+TCcount;
											StatusMail.failTCcount =StatusMail.failTCcount+TCcount;
											break loopmethod;
										}
									}	
								}
							
						}					
						System.out.println(i+" records inserted");  
						HTMLPreparation.htmlContent=HTMLPreparation.htmlContent.replace("#Row#", dynamicRow+"#Row#");		
					}
				}
			}
		}

		generateHTMLReport.testPassPercentage();
		HTMLPreparation.htmlContent=HTMLPreparation.htmlContent.replace("#Row#","").replace("#TestPassRate#",StatusMail.testPassRate).replace("#ExecutionStartTime#",getFormatedTime(starttime)).replace("#ExecutionEndTime#",getFormatedTime(endtime)).replace("#ExecutionTime#",Exetime);	
		return StatusMail.testPassPercentage;

	}
	
	




	private static String calculateTime(int exeTime) {

		String time;
		if(exeTime>60){
			int hours = (int) Math.floor(exeTime / 60); 
			int minutes = exeTime % 60;
			time= hours+":" +minutes + " Hrs";

		}
		else{
			time=String.valueOf(exeTime)+" Mins";
		}
		return time;

	}

	public static void generateStartMail() throws Exception {
		File fXmlFile = new File(TestLib.Automation_properties.getTestNGFilePath());

		StringBuilder contentBuilder1 = new StringBuilder();
		contentBuilder1.append("<tr><td align='middle' style='background:#f3f3f; border:1px solid #b6b6b6; font:normal 15px Calibri;'>#TestCaseID#</td>");
		contentBuilder1.append("<td align='center' valign='middle' style='background:#f3f3f; border:1px solid #b6b6b6; font:normal 15px Calibri;'>#Priority#</td>");
		contentBuilder1.append("<td align='center' valign='middle' style='background:#f3f3f; border:1px solid #b6b6b6; font:normal 15px Calibri;'>#Module#</td>");
		contentBuilder1.append("<td valign='middle' style='background:#f3f3f; border:1px solid #b6b6b6; font:normal 15px Calibri;'>#TestCaseName#</td>");
		contentBuilder1.append("<td align='center' valign='middle' style='background:#f3f3f; border:1px solid #b6b6b6; font:normal 15px Calibri;'>#Description#</td></tr>");
		String row=contentBuilder1.toString();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("test");
		String module = "";
		String dynamicRow;
		System.out.println("LIST SIZE"+nList.getLength());
		for (int temp =0; temp < nList.getLength(); temp++) 
		{
			dynamicRow=row;
			ArrayList<String> stringList = new ArrayList<String>();
			stringList =generateHTMLReport.getTCdetails(temp);
			System.out.println("Value of temp: "+temp);
			String testids = stringList.get(0);
			String testdesc = stringList.get(1);
			String priority = stringList.get(2);
			module = stringList.get(3);
			String className = stringList.get(4);
			String[] TCcount = testids.split(",");
			dynamicRow=dynamicRow.replace("#TestCaseID#",testids).replace("#Priority#",priority).replace("#Module#",module).replace("#Description#", testdesc).replace("#TestCaseName#", className.split("HA.TestExecute.")[1].split("\\.")[1]);
			System.out.println("Value of temp: "+TCcount);
			HTMLPreparation.htmlContent= HTMLPreparation.htmlContent.replace("#Row#",dynamicRow+"#Row#");
		}
		
		//mailGeneration.subject+=": "+module+" Module Start Mail";
		HTMLPreparation.htmlContent= HTMLPreparation.htmlContent.replace("#Row#","");

		String browserName=TestLib.Automation_properties.getInstance().getProperty(Automation_properties.BROWSER);
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
	}

	public static void createFile() throws Exception{
		Deletefiles.startingwith(System.getProperty("user.dir")+"/TestLogs", "DBFile");
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(System.getProperty("user.dir")+"/TestLogs/DBFile.txt"), "utf-8"))) {
			writer.write(String.valueOf(rowID));
			writer.close();
		}
	}
	
	private static ArrayList<String> getIteratorDataSetNames(String className) throws ParserConfigurationException, SAXException, IOException{
		ArrayList<String> iteratorDataSetNames = new ArrayList<String>();
		String iteratorFileName = getIteratorFileName(className);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(System.getProperty("user.dir")+"/src/HA/TestData/" + Utils.getModule(className) + "/testIterator/"+iteratorFileName);
		NodeList listOfNodes = doc.getElementsByTagName("DataSet");
		for (int i = 0; i < listOfNodes.getLength(); i++) 
		{
			Node dataSet = listOfNodes.item(i);
			Element resultElement = (Element) dataSet;
			iteratorDataSetNames.add(resultElement.getAttribute("name"));
		}
		return iteratorDataSetNames;
	}
	
	private static String getFormatedTime(String time) {
		SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		Date d = null;
		try 
		{
		   d = input.parse(time);

		} 
		catch (ParseException e) 
		{
		   e.printStackTrace();
		}
		String formatted = output.format(d);
		return formatted;
	}
	
	private static String getIteratorFileName(String key) throws IOException{
		Properties prop = new Properties();
		FileInputStream input = new FileInputStream(classItrComboFilePath);
		prop.load(input);
		String value = prop.getProperty(key);
		input.close();
		return value;
	}

	private static HashMap<String, String> getCorrelationMap() throws IOException {
		Properties properties = new Properties();
		FileInputStream corrFile = new FileInputStream(CorrelationMapFilePath);
		properties.load(corrFile);
		HashMap<String, String> CorrelationMap = new HashMap<String, String>();
		for (String key : properties.stringPropertyNames()) {
			CorrelationMap.put(key, properties.get(key).toString());
		}
		corrFile.close();
		return CorrelationMap;
	}

}
class Row
{
	String OS,Browser,Module,Tenant,ExecutionMachine,ExecutionTime,Total_TC,Passed_TC,Percentage,Environment;
}
