package Utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HTML {


	public static String strHTMLbody = null;
	public static String sDateTime, sFilename, sBodyText,htmlreportpath,resultmailfile,consolidatedmailreportpath;

	public static void prepmail() throws Exception{

		FileWriter fWriter = null;
		strHTMLbody =htmlheaderbody();
		
		BufferedWriter writer = null;
		String strDate = GetDateTime();

		sFilename = "Execution_Report_" + strDate+ ".html";
		htmlreportpath =System.getProperty("user.dir") + "\\test-output\\" + File.separator + sFilename;
		try {

			//Creating a new FileWriter object with the file location
			fWriter = new FileWriter(htmlreportpath);
			//creating a buffered writer for the file object
			writer = new BufferedWriter(fWriter);
			//Adding the initial HTML tags
			writer.write("<html><head><title>Helenoftroy - Test Automation Execution Report</title></head><body>");
			writer.write(strHTMLbody);
			//Closing the tags
			writer.write("</body></html>");
			//closing the writer object
			writer.close();
			System.out.println("MAil Body prepared");
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	public static String htmlheaderbody() throws Exception{

		String strHTML = "";
		strHTML = strHTML + "<h3 ALIGN='Left'><U><h2> Host Analytics Automation Report</h2> </U></h3><BR>";
		strHTML = strHTML + "<TABLE ALIGN='Left'>";
		//strHTML = strHTML + "<TR><TD height=12px> <b>ProjectName</b></TD><TD><b>:</b></TD><TD  height=12px>Selenium-Java Tool Agnostic Automation</TD></TR>";
		//strHTML = strHTML + "<TR><TD  height=12px> <b>Script location</b></TD><TD><b>:</b></TD><TD  height=12px>"+System.getProperty("user.dir")+"</TD> </TR> ";
		//strHTML = strHTML + "<TR><TD  height=12px> <b>Environment Details</b></TD><TD><b>:</b></TD> <TD  height=12px><u><font color='Blue'>"+HA.TestAutomation.HATF_properties.getInstance().getProperty(HATF_properties.BASEURL)+"</font></u></TD> </TR>  ";
		strHTML = strHTML + "<TR><TD   height=12px> <b> OS </b></TD><TD><b>:</b></TD> <TD height=12px>"+System.getProperty("os.name")+"</TD> </TR> ";
		strHTML = strHTML + "<TR><TD  height=12px> <b> Execution Machine</b></TD><TD><b>:</b></TD> <TD  height=12px>"+ InetAddress.getLocalHost().getHostName()+"</TD></TR> ";
		strHTML = strHTML + "<TR><TD   height=12px> <b> Test Pass Rate </b></TD><TD><b>:</b></TD> <TD height=12px>#TestPassRate#</TD> </TR>";
		strHTML = strHTML + "<TR><TD   height=12px> <b> Execution Start Time</b></TD bgcolor='#F3F3FF'><TD><b>:</b></TD> <TD  height=12px>#StartTime#</TD> </TR>";
		strHTML = strHTML + "<TR><TD  width=200px height=12px> <b> Execution End Time</b></TD><TD><b>:</b></TD> <TD  height=12px>#EndTime#</TD> </TR>";
		strHTML = strHTML + "<TR><TD   height=12px> <b>Execution Time</b></TD><TD><b>:</b></TD> <TD  height=12px> ~ #ExeTime#</TD> </TR> </TABLE>";
		strHTML = strHTML + "<BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR>";
		strHTML = strHTML + "<TABLE>";
		strHTML = strHTML + "<TR>";
		strHTML = strHTML + "<TD bgcolor='#A0CFEC' width=100px height=50px ALIGN='Center'><font face=Arial size=2><b>Test ID</b></font></TD>";
		strHTML = strHTML + "<TD bgcolor='#A0CFEC' width=100px height=50px ALIGN='Center'><font face=Arial size=2><b>Priority</b></font></TD> ";
		strHTML = strHTML + "<TD bgcolor='#A0CFEC' width=100px height=50px ALIGN='Center'><font face=Arial size=2><b>Module</b></font></TD> ";
		strHTML = strHTML + "<TD bgcolor='#A0CFEC' width=100px height=50px ALIGN='Center'><font face=Arial size=2><b>Test Script Name</b></font></TD> ";
		strHTML = strHTML + "<TD bgcolor='#A0CFEC' width=100px height=50px ALIGN='Center'><font face=Arial size=2><b>Description</b></font></TD> ";
		strHTML = strHTML + "<TD bgcolor='#A0CFEC' width=100px height=50px ALIGN='Center'><font face=Arial size=2><b>Status</b></font></TD> ";
		strHTML = strHTML + "<TD bgcolor='#A0CFEC' width=100px height=50px ALIGN='Center'><font face=Arial size=2><b>Remarks</b></font></TD> ";
		strHTML = strHTML + "<TD bgcolor='#A0CFEC' width=100px height=50px ALIGN='Center'><font face=Arial size=2><b>Test Duration</b></font></TD> ";
		strHTML = strHTML + "</TR>";
		//strHTML = strHTML + generateHTMLReport.generateHTML();
		strHTML = strHTML + "</TABLE><BR><BR><BR>";
		//strHTML = strHTML.replace("#StartTime#",timedate.getlocaltime(generateHTMLReport.starttime)).replace("#EndTime#", timedate.getlocaltime(generateHTMLReport.endtime)).replace("#ExeTime#", timedate.getminsectime(generateHTMLReport.Exetime)).replace("#TestPassRate#",mail.testPassRate);
		return strHTML;
	}

	public static String htmlsuitebody(){

		String strHTMLsuite = "";

		strHTMLsuite = strHTMLsuite +"";

		return htmlreportpath;

	}

	public static String GetDateTime()
	{
		String strDate = null;
		try{

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");

			SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

			Date now = new Date();

			strDate = sdfDate.format(now);

			String strTime = sdfTime.format(now);

			strTime = strTime.replace(":", "-");

			sDateTime = strDate + "_" + strTime;}

		catch (Exception e){

			e.printStackTrace();
		} 
		return sDateTime;
	}
	
	public static String consolidatedmailheaderbody() throws Exception{

		String strHTML = "";
		strHTML = strHTML + "<h3 ALIGN='Left'><u><h2> Consolidated Test Automation Execution Report</h2></u></h3><BR>";
		strHTML = strHTML + "<TABLE ALIGN='Left'>";
	//	strHTML = strHTML + "<TR><TD  height=12px> <b>Environment Details</b></TD><TD><b>:</b></TD> <TD  height=12px><u><font color='Blue'>"+HA.TestAutomation.HATF_properties.getInstance().getProperty(HATF_properties.BASEURL)+"</font></u></TD> </TR>  ";

		//strHTML = strHTML + "<TR><TD   height=12px> <b> HAEPM Version </b></TD><TD><b>:</b></TD> <TD height=12px>"+timedate.getbuildtime()+"</TD> </TR>";
		strHTML = strHTML + "<TR><TD   height=12px> <b> Test Pass Rate </b></TD><TD><b>:</b></TD> <TD height=12px>#TestPassRate#</TD> </TR>";
		strHTML = strHTML + "<TR><TD   height=12px> <b>Total Execution Time</b></TD><TD><b>:</b></TD> <TD  height=12px> ~ #ExeTime#</TD> </TR> </TABLE>";
		strHTML = strHTML + "<BR><BR><BR><BR><BR>";
		strHTML = strHTML + "<TABLE>";
		strHTML = strHTML + "<TR>";
		strHTML = strHTML + "<TD bgcolor='#A0CFEC' width=100px height=50px ALIGN='Center'><font face=Arial size=2><b>Module</b></font></TD>";
		strHTML = strHTML + "<TD bgcolor='#A0CFEC' width=100px height=50px ALIGN='Center'><font face=Arial size=2><b>Execution Machine</b></font></TD> ";
		strHTML = strHTML + "<TD bgcolor='#A0CFEC' width=100px height=50px ALIGN='Center'><font face=Arial size=2><b>OS</b></font></TD> ";
		strHTML = strHTML + "<TD bgcolor='#A0CFEC' width=100px height=50px ALIGN='Center'><font face=Arial size=2><b>Execution Time</b></font></TD> ";
		strHTML = strHTML + "<TD bgcolor='#A0CFEC' width=100px height=50px ALIGN='Center'><font face=Arial size=2><b>Total TC Executed</b></font></TD> ";
		strHTML = strHTML + "<TD bgcolor='#A0CFEC' width=100px height=50px ALIGN='Center'><font face=Arial size=2><b>Passed TC</b></font></TD> ";
		strHTML = strHTML + "</TR>";
		//strHTML = strHTML + generateHTMLReport.consolidatedmoduledetails();
		
		//strHTML = strHTML.replace("#TestPassRate#",generateHTMLReport.consol_testPassRate).replace("#ExeTime#", mintohours(generateHTMLReport.totExe_time));
		return strHTML;
	}
	
	public static void consolidatemailHTMLprep() throws Exception{

		FileWriter fWriter = null;

		BufferedWriter writer = null;
		String strDate = GetDateTime();

		resultmailfile = "Consolidated_Report_" + strDate+ ".html";
		consolidatedmailreportpath =System.getProperty("user.dir") + "\\test-output\\" + File.separator + resultmailfile;
		try {

			//Creating a new FileWriter object with the file location
			fWriter = new FileWriter(consolidatedmailreportpath);
			//creating a buffered writer for the file object
			writer = new BufferedWriter(fWriter);
			//Adding the initial HTML tags
			writer.write("<html><head><title>Host Analytics EPM - Test Automation Execution Report</title></head><body>");

			writer.write(consolidatedmailheaderbody());
			//Closing the tags
			writer.write("</body></html>");
			//closing the writer object
			writer.close();
			System.out.println("Consolidated MAil Body prepared");
			System.out.println("Mail HTML path"+consolidatedmailreportpath);
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
	
	public static String mintohours(int mins){
		String hours = null;
		String min = Integer.toString(mins%60);
		if(mins>60){
		hours = Integer.toString(mins/60);		
		hours = hours+" hours "+min+" mins";	
		}else{
			hours = min+" mins";
		}
		
		
		return hours;
		
	}
}

