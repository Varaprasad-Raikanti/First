package Utilities;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xslf.usermodel.XSLFSlideShow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.testng.Assert;

import TestLib.Driver;
import TestLib.Sync;

public class File {


	//public static String targetfilepath=null; 

	public static void htmlFileCompare(String data, String expecteddata) {
		String out=null;
		try{
			String compareresults = System.getProperty("user.dir")+"/TestLogs/Compare Results/";
			String strDate = Utilities.HTML.GetDateTime();
			String targetfilepath=null; 


			targetfilepath = compareresults+"target"+strDate+".html";
			java.io.File file = new java.io.File(targetfilepath);
			file.getParentFile().mkdirs();
			file.createNewFile();
			java.io.FileWriter fw = new java.io.FileWriter(file);
			fw.write(data);
			fw.close();
			System.out.println("Target file :"+targetfilepath);

			ProcessBuilder pb = new ProcessBuilder("java.exe", "-jar", String.format("\"%s\"", System.getProperty("user.dir")+"/Lib/daisydiff.jar"), String.format("\"%s\"", expecteddata), 
					String.format("\"%s\"", targetfilepath), String.format("\"--file=%s\"", compareresults + expecteddata.substring(expecteddata.lastIndexOf("/") + 1, expecteddata.lastIndexOf(".html")) + ".htm"));
			Process shell=pb.start();

			try
			{
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(shell.getInputStream()));
				StringBuilder response=new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					response.append(line + "\n");
				}
				bufferedReader.close();
				if (response.toString().length() > 0) {
					out=response.toString().trim();
				}
			}catch(Exception e){
				out=e.toString();
				Driver.getLogger().error(e);
			}
			System.out.println(out);

		} catch (Exception e) {
			e.printStackTrace();
			Driver.getLogger().error(e);
		}
	}

	
	
	
	public static boolean CompareExcelFiles(String expectedfilepath,String actualfilepath,String range) throws Exception {
		String out=null;
		try{
			String comparatorpath = System.getProperty("user.dir")+"/Lib/Host.SpreadSheet.Comparator.exe";
			String compareresults = System.getProperty("user.dir")+"/TestLogs/Compare Results/";

			System.out.println("Actual File Path "+actualfilepath);
			System.out.println("Expected File Path "+expectedfilepath);
			if (!new java.io.File(expectedfilepath).exists() || !new java.io.File(actualfilepath).exists())
			{
			   Driver.getLogger().info("Given Files not available");
			   Assert.fail();
			}

			ProcessBuilder pb = new ProcessBuilder(comparatorpath, "objSpreadsheet", expectedfilepath, actualfilepath, "Verify", compareresults,range);
			Process shell=pb.start();
			try
			{
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(shell.getInputStream()));
				StringBuilder response=new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					response.append(line + "\n");
				}
				bufferedReader.close();
				
				if (response.toString().length() > 0) {
					out=response.toString().trim();
					
				}
				
				String mismatch=out.split("--")[8];
				System.out.println("888888888888888888  "+mismatch);
				 if(mismatch.equals("False"))
					return true;
				 else
				 {
		           Driver.getLogger().info("Comparator result mismatch  "+ out+"  mismatch value is "+mismatch);
				 }
		
			}catch(Exception e){
				e.printStackTrace();
				out=e.toString();
				Driver.getLogger().error(e);
			}
			Driver.getLogger().info(out);
			

		} catch (Exception e) {
			e.printStackTrace();
			Driver.getLogger().error(e);
			
		}
     return false; 
	}

	public static String GetDateTime()
	{
		String strDate,sDateTime = null;
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

	
	
	
	public static boolean compareImage(String sourceImage, String targetImage) { 
		try {
			String ss= GetDateTime()+".jpg";			
		//	Common.screenShot(targetImage+ss);

			// read buffer data from image files // 
			java.io.File ImageA = new java.io.File(sourceImage);
			java.io.File ImageB = new java.io.File(targetImage+ss);
			System.out.println(targetImage+ss);
			BufferedImage biA = ImageIO.read(ImageA); 
			System.out.println("2");
			DataBuffer dbA = biA.getData().getDataBuffer(); 
			System.out.println("3");
			int sizeA = dbA.getSize(); 
			BufferedImage biB = ImageIO.read(ImageB); 
			DataBuffer dbB = biB.getData().getDataBuffer(); 
			int sizeB = dbB.getSize(); 
			System.out.println("4");
			// compare data-buffer objects // 
			if(sizeA == sizeB) { 
				for(int i=0; i<sizeA; i++) { 
					if(dbA.getElem(i) != dbB.getElem(i)) { 
						Driver.getLogger().error("sourceImage:  "+sourceImage+"\ntargetImage:  "+targetImage+ss+"\nResult:  failed");
						return false; 
					} 
				} 
				Driver.getLogger().info("sourceImage:  "+sourceImage+"\n targetImage:  "+targetImage+ss+"\nResult:  PASSED");
				return true; 
			} 
			else { 
				Driver.getLogger().error("sourceImage:  "+sourceImage+"\n targetImage:  "+targetImage+ss+"\nResult:  failed");
				return false; 
			} 
		} 
		catch (Exception e) { 
			Driver.getLogger().error("Failed to compare image files ...\n"+e);
			return false; 
		} 
	}

	public static boolean compareImages(String file1, String file2) { 
		try {
			java.io.File fileA=new java.io.File(file1);
			java.io.File fileB=new java.io.File(file2);
		// take buffer data from botm image files //
		BufferedImage biA = ImageIO.read(fileA);
		DataBuffer dbA = biA.getData().getDataBuffer();
		int sizeA = dbA.getSize(); 
		BufferedImage biB = ImageIO.read(fileB);
		DataBuffer dbB = biB.getData().getDataBuffer();
		int sizeB = dbB.getSize();
		// compare data-buffer objects //
		if(sizeA == sizeB) {
		for(int i=0; i<sizeA; i++) { 
		if(dbA.getElem(i) != dbB.getElem(i)) {
		return false;
		}
		}
		return true;
		}
		else {
		return false;
		}
		} 
		catch (Exception e) { 
		System.out.println("Failed to compare image files ...");
		return false;
		}
		}
	

	public static void textFileCompare(String actualFile, String expectedFile) throws Exception {
        String compareresults = System.getProperty("user.dir")+"/TestLogs/Compare Results/";
        String strDate = Utilities.HTML.GetDateTime();
		String resultfilepath=null; 
		resultfilepath = compareresults+"TextCompareResults"+strDate+".xls";
		Driver.getLogger().info("In text File Compare method");
		try{
			// Create FileReader & Writer Objects.
            FileReader actualFileReader  = new FileReader(actualFile);
            FileReader expectedFileReader  = new FileReader(expectedFile);
            FileWriter resultFile = new FileWriter(resultfilepath);
            // Create Buffered Object.
            BufferedReader actualFileBufRdr = new BufferedReader(actualFileReader);
            BufferedReader expectedFileBufRdr = new BufferedReader(expectedFileReader);
            BufferedWriter resultFileBufWrtr = new BufferedWriter(resultFile);
           
            // Get the file contents into String Variables.
            String actualFileContent = actualFileBufRdr.readLine();
            String expectedtdFileContent = expectedFileBufRdr.readLine();
           
            // Write the content into the results file.
            resultFileBufWrtr.newLine();
            resultFileBufWrtr.write("  Comparision Results");
            resultFileBufWrtr.newLine();
            resultFileBufWrtr.newLine();
            resultFileBufWrtr.write("  Source	"+actualFile);
            resultFileBufWrtr.newLine();
            resultFileBufWrtr.write("  Target	"+expectedFile);
            resultFileBufWrtr.newLine();
            
            boolean isDifferent = false;
            int lineNumber = 1;
           
            if (actualFileContent != null || expectedtdFileContent != null) {
            	
            	resultFileBufWrtr.newLine();
                resultFileBufWrtr.write("    Line 		");
                resultFileBufWrtr.newLine();
               
                    // Check whether Actual file contains data or not
                    while((actualFileContent!=null)  ){
                           
                            // Check whether Expected file contains data or not
                            if (((expectedtdFileContent )!=null)) {
                                   
                                    // Check whether both the files have same data in the lines
                                    if (!actualFileContent.equals(expectedtdFileContent)) {
                                            resultFileBufWrtr.write(lineNumber+"		Source 		"+actualFileContent);
                                            resultFileBufWrtr.newLine();
                                            resultFileBufWrtr.write("		Target 		"+expectedtdFileContent);
                                            resultFileBufWrtr.newLine();
                                            isDifferent = true;
                                    }
                                    lineNumber = lineNumber+1;
                                    expectedtdFileContent= expectedFileBufRdr.readLine();
                            }
                            else{
                            		resultFileBufWrtr.write(lineNumber+"		Source 		"+actualFileContent);
                            		resultFileBufWrtr.newLine();
                            		resultFileBufWrtr.write("		Target 		"+expectedtdFileContent);
                            		resultFileBufWrtr.newLine();
                            		isDifferent = true;
                                    lineNumber = lineNumber+1;
                            }
                            actualFileContent=actualFileBufRdr.readLine();
                    }
                   
                    // Check for the condition : if Actual File has Data & Expected File doesn't contain data.
                    while ((expectedtdFileContent!=null)&&(actualFileContent==null)) {
                    		resultFileBufWrtr.write(lineNumber+"		Source 		"+actualFileContent);
                    		resultFileBufWrtr.newLine();
                    		resultFileBufWrtr.write("		Target 		"+expectedtdFileContent);
                    		resultFileBufWrtr.newLine();
                    		isDifferent = true;
                            lineNumber = lineNumber+1;
                            expectedtdFileContent= expectedFileBufRdr.readLine();
                    }
            }
            else{
                    // Mention that both the files don't have Data.
            	resultFileBufWrtr.newLine();    
            	resultFileBufWrtr.write("	No Data in both files");
                    resultFileBufWrtr.newLine();
                    isDifferent = true;
            }
           
            // Check is there any difference or not.
            if (isDifferent) {
            	
            }
            else{
                    resultFileBufWrtr.append("		No difference Found");
            }
           
            //Close the streams.
            actualFileReader.close();
            expectedFileReader.close();
            resultFileBufWrtr.close();
            actualFileBufRdr.close();
            expectedFileBufRdr.close();
            Driver.getLogger().info("Result generated successfully");
      }
		catch(Exception e)
		{
			e.printStackTrace();
			Driver.getLogger().error(e);
		}
}
	
	public static String[] readCSVFile(String file) throws Exception{
		BufferedReader CSVFile = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
		  String str="";
		  String dataRow =""; 
		  while ((dataRow = CSVFile.readLine()) != null){
			  str=str.concat(dataRow+"@#@");
		  }
		  CSVFile.close();
		  String[] array = str.split("@#@");
		  return array;
		 }
	public static boolean excelCompare(String expectedfilepath,String actualfilepath,String compareresults) throws Exception {
		String out=null;
		int lineCount = 0;
			try{
				String comparatorpath = System.getProperty("user.dir")+"/Lib/SpreadSheetComparator.exe";
				//String compareresults = System.getProperty("user.dir")+"/TestLogs/Compare Results/";
	
				System.out.println("expectedfilepath:::::::::::"+expectedfilepath);
				System.out.println("actualfilepath::::::::::"+actualfilepath);
				System.out.println("compareresults::::::::"+compareresults);
				ProcessBuilder pb = new ProcessBuilder(comparatorpath, "objSpreadsheet", expectedfilepath, actualfilepath, "Verify", compareresults);
				Process shell=pb.start();
					try
					{
						BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(shell.getInputStream()));
						StringBuilder response=new StringBuilder();
						String line;
						System.out.println("eywirwtrywetyrwetwetyrf"+lineCount);
						while ((line = bufferedReader.readLine()) != null) {
							response.append(line + "\n");
							lineCount++;
						}
						bufferedReader.close();
						if (response.toString().length() > 0) {
							out=response.toString().trim();
							
						}
						
						
						
					}catch(Exception e){
						out=e.toString();
						Driver.getLogger().error(e);
					}
				Driver.getLogger().info(out);
				//			System.out.println(out);
				System.out.println("eywirwtrywetyrwetwetyrf"+lineCount);
			} catch (Exception e) {
				e.printStackTrace();
				Driver.getLogger().error(e);
				
			}
		String[] a =  out.split("Comparison Results:");
		System.out.println("wwrssdtysyudedwedte:::::::::" +a[1].replace("\"", "").trim());
		return fileRead(a[1].replace("\"", "").trim());

	}
	

	public static boolean fileRead(String filepath) throws Exception{		
		/* Get the files to be compared first */
		FileInputStream fileInputStream = new FileInputStream(filepath);
        HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
        HSSFSheet worksheet = workbook.getSheet("Comparision Results");
       // HSSFRow row1 = worksheet.getRow(3);
        Iterator rows = worksheet.rowIterator(); 
        int noOfRows = 0;
        while( rows.hasNext() ) {
            HSSFRow row = (HSSFRow) rows.next();
            noOfRows++;                
        }
        System.out.println("Number of Rows: " + noOfRows);
        if(noOfRows == 5)
        	return true;
        else 
            return false;	
	}


	
	public static void deleteFile(String filePath){
		
		java.io.File file = new java.io.File(filePath);		
		if(file.exists()){
			file.delete();
		}
	}
}//eoc
