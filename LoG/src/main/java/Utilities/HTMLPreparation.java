package Utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import org.testng.ITestResult;
import TestLib.Automation_properties;
import TestLib.Driver;
import models.admin.HealthCheck;

public class HTMLPreparation {
    public static String mailTemplatePath=System.getProperty("user.dir")+"/src/test/resources/MailTemplates/";
    public static String htmlContent;
    public static String module;
    public static ITestResult test;
    public static String parseHTMLfile(String filePath)
    {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(filePath));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Driver.getLogger().info("File Parsed");
        return contentBuilder.toString();
    }
    public static String prepmail(String fileName) throws Exception{
        FileWriter fWriter = null;
        BufferedWriter writer = null;
        String strDate = HTML.GetDateTime();
        String sFilename = fileName+ strDate+ ".html";
        String htmlreportpath =System.getProperty("user.dir") + "/test-output/" + File.separator + sFilename;
        try {
            fWriter = new FileWriter(htmlreportpath);
            writer = new BufferedWriter(fWriter);
            writer.write(htmlContent);;
            writer.close();
            System.out.println("MAil Body prepared");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("HTML Report: "+htmlreportpath);
        return htmlreportpath;
    }
    public static String generateHealthReport() throws Exception{
        String filePath="";
        return filePath;
    }


    public static String generateMail(String mailType) throws Exception {
        String filePath;
        switch(mailType)
        {
            case "start" :
            {
                filePath=mailTemplatePath+"ExecutionStartMail.html";
                htmlContent= parseHTMLfile(filePath).replace("#Browser#", Automation_properties.getInstance().getProperty("BROWSER") );
    
                fillCommonDetails();
                //.generateStartMail();
                return prepmail("Execution_StartMail");
            }
            case "exectionReport":
            {
                filePath=mailTemplatePath+"employ.html";
                htmlContent= parseHTMLfile(filePath).replace("#URL#", Automation_properties.getInstance().getProperty("URL") );
                htmlContent= parseHTMLfile(filePath).replace("#Designation#", Automation_properties.getInstance().getProperty("Designation") );
                 //htmlContent= parseHTMLfile(filePath).replace("#Browser#", Automation_properties.getInstance().getProperty("BROWSER") );
                htmlContent= parseHTMLfile(filePath).replace("#Employee Name#", Automation_properties.getInstance().getProperty("EmployeeName") );
                fillCommonDetails();
                System.out.println("Module Name:"+ HTMLPreparation.module);
                String module=generateHTMLReport.getTCdetails(0).get(4).split("TestExecute.")[1].split("\\.")[0]+" "+ TestLib.Automation_properties.getInstance().getProperty("BROWSER");
                String ModuleName=generateHTMLReport.getTCdetails(0).get(4).split("TestExecute.")[1].split("\\.")[0];
                System.out.println("Mankoo@@:"+generateHTMLReport.getTCdetails(0).get(4).split("TestExecute.")[1]);
    
                //    mailGeneration.subject+=": "+module+" Module Results"+.generateExecutionHTML();;
                MailTestSuite.generateExecutionHTML();
                HTML.htmlreportpath=prepmail("Execution_report");
                return HTML.htmlreportpath;
            }
            case "healthReport":
            {
                filePath=mailTemplatePath+"HealthCheckreport.html";
                htmlContent= parseHTMLfile(filePath);    
                updateHealthCheckContent();
                HTML.htmlreportpath=prepmail("HealthCheck_report");
                return HTML.htmlreportpath;
            }
            case "failure":
            {
                filePath=mailTemplatePath+"ExecutionFail.html";
                //    mailGeneration.subject="Test case Failed";
                //    mailGeneration.to= TestLib.Automation_properties.getInstance().getProperty("FailureMail");
                htmlContent= parseHTMLfile(filePath);
                fillCommonDetails();
                String testDetails = test.getTestClass().toString();
                String testName=testDetails.split("HA.TestExecute.")[1];
                String Module= testName.split("\\.")[0];
                String testScriptName =(testName.split("\\.")[1]).split("]")[0];
                String testDuration = MilliSecondsToMinutes(test.getStartMillis()-test.getEndMillis());
                String Remarks=test.getThrowable().toString();
                Remarks=Remarks.split("\\:")[0];
                htmlContent=htmlContent.replace("#Module#",Module).replace("#Test Script Name#",testScriptName).replace("#Test Duration#",testDuration).replace("#Remarks#",generateHTMLReport.exception(Remarks));
                return prepmail("TC_Failed");
            }
        }
        return null;
    }

    public static void fillCommonDetails() throws UnknownHostException
    {
        htmlContent=htmlContent.replace("#URL#",Automation_properties.getInstance().getProperty("BASEURL")).replace("#OS#",System.getProperty("os.name")).replace("#ExecutionMachine#",InetAddress.getLocalHost().getHostName());
    }

    public static void setFailedTestcaseDetails(ITestResult testCase) {
        test=testCase;
    }
    
    public static void updateHealthCheckContent() {
        HealthCheck healthCheck=new HealthCheck();
        healthCheck=Utilities.Listener.healthCheck.get("Hydroflask");
        htmlContent=htmlContent.replace("##Site##",healthCheck.getSiteName())
                .replace("##CronJob##", healthCheck.CronJob)
                .replace("##OrderExport##", healthCheck.OrderExport)
                .replace("##OrderImport##", healthCheck.OrderImport)
                .replace("##IndexManagement##", healthCheck.IndexManagement)
                .replace("##OrderTrackingStatus##", healthCheck.OrderTrackingStatus)
                .replace("##Previous##", String.valueOf(healthCheck.OrderTrackingPrevious))
                .replace("##Current##", String.valueOf(healthCheck.OrderTrackingCurrent))
                .replace("##GoogleInsightWeb##", String.valueOf(healthCheck.GoogleInsightWeb))
                .replace("##GoogleInsightMobile##", String.valueOf(healthCheck.GoogleInsightMobile));
    }
    public static String MilliSecondsToMinutes(long testexetime)
    {
        String time;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(testexetime);
        if(minutes<1){
            minutes = TimeUnit.MILLISECONDS.toSeconds(testexetime);
            time="1 Mins";
        }
        else{
            time=String.valueOf(minutes)+" Mins";
        }
        return time;
    }
}