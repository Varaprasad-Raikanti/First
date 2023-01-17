package Utilities;

import java.io.File;
import java.io.IOException;
import java.text.CharacterIterator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringEscapeUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import TestLib.Common;
import TestLib.Driver;



public class Utils {
	
	public static String getEmailid() {
		String Email=null;
		try {
			long currentTimestamp = System.currentTimeMillis();
			 Email ="newuser"+currentTimestamp+"@gmail.com";
		}

		catch (Exception e) {

			e.printStackTrace();
		}
		return Email;
	}

	public static String getDateTime() {
		String sDateTime = "";
		String strDate = null;
		try {

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");

			SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

			Date now = new Date();

			strDate = sdfDate.format(now);

			String strTime = sdfTime.format(now);

			strTime = strTime.replace(":", "-");

			sDateTime = strDate + "_" + strTime;
		}

		catch (Exception e) {

			e.printStackTrace();
		}
		return sDateTime;
	}

	public  static boolean equalLists(List<String> one, List<String> two){     
		if (one == null && two == null){
			return true;
		}

		if((one == null && two != null) 
				|| one != null && two == null
				|| one.size() != two.size()){
			return false;
		}

		//to avoid messing the order of the lists we will use a copy
		//as noted in comments by A. R. S.
		one = new ArrayList<String>(one); 
		two = new ArrayList<String>(two);   

		Collections.sort(one);
		Collections.sort(two);      
		return one.equals(two);

	}
	public static boolean valueNULL(String value){
		if(value==null || value.equals("null")){
			return true;
		}else{
			return false;
		}
	}

	public static boolean valueNOTNULL(String value){
		if(value!=null && !value.equals("null")){
			return true;
		}else{
			return false;
		}
	}

	public static boolean isValueTrue(String value){
		if(valueNOTNULL(value) && (value.equalsIgnoreCase("TRUE") ||value.equalsIgnoreCase("YES"))){
			return true;
		}else if (valueNOTNULL(value) && (value.equalsIgnoreCase("FALSE") ||value.equalsIgnoreCase("NO"))){
			return false;
		}else{
			return false;
		}

	}

	public static boolean valueNOTNULL(List<String> values){
		if(values!=null && !values.equals("null") && values.size()>0){
			if(values.size()==1 && values.get(0).equals("null")){
				return false;
			}
			return true;
		}else{
			return false;
		}
	}

	public static boolean isValueExists(String exists){
		if(isValueTrue(exists) || (!(valueNOTNULL(exists))))
			return true;
		return false;
	}

	public static String returnListString(List<String> values){
		String listValue = "";
		for(int i=0;i<values.size();i++){
			if(!(i==values.size()-1))
				listValue = listValue+values.get(i)+"||@@||";
			else
				listValue = listValue+values.get(i);
		}
		return listValue.trim();
	}

	public static String returnListStringForTextBoxes(String elemfindBY,String elemfindText) throws Exception{
		List<WebElement> options = Common.findElements(elemfindBY, elemfindText);
		ArrayList<String> opts = new ArrayList<String>();
		for(WebElement opt:options){
			opts.add(opt.getAttribute("value"));
		}
		return returnListString(opts);
	}

	public static String returnListStringForSelectBoxes(String elemfindBY,String elemfindText) throws Exception{
		List<WebElement> options =Common.findElements(elemfindBY, elemfindText);
		ArrayList<String> opts = new ArrayList<String>();
		for(int i =0;i<options.size();i++){
			Select sel = new Select(options.get(i));
			opts.add(sel.getFirstSelectedOption().getText());
		}	      
		return returnListString(opts);
	}
	public static String replaceSpeacialCharacters(String text){
		StringBuilder s = new StringBuilder(text.length());
		CharacterIterator it = new StringCharacterIterator(text);
		for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
			if (ch == '\'') {
				s.append("\\\'");

			}
			else
				s.append(ch);
		}

		String JsString = s.toString();
		JsString = JsString.replace("/'/g", "\\'");
		return s.toString();
	}
	public static String escapeSpecialCharacters(String text, String whichLangToEscapeIn)
	{
		String escapedString="";
		switch(whichLangToEscapeIn.toLowerCase())
		{
			case "javascript":
				escapedString = StringEscapeUtils.escapeEcmaScript(text);
				break;
			default:
				Driver.getLogger().info("The particular language is not handled. Please do so now..");
				break;
		}
		return escapedString;
		
	}

	public static String getLocalSystemDateFormat(){
		DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
		String pattern       = ((SimpleDateFormat)formatter).toPattern();
		String localPattern  = ((SimpleDateFormat)formatter).toLocalizedPattern();

		System.out.println(pattern);
		System.out.println(localPattern);
		localPattern = localPattern.replace("/", "_");


		DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy");
		Date date1 = new Date();
		Calendar cal=Calendar.getInstance();
		cal.setTime(date1);
		System.out.println( cal.get(Calendar.MONTH));
		System.out.println( cal.get(Calendar.YEAR));
		System.out.println( cal.get(Calendar.DATE));
		System.out.println(dateFormat.format(date1));
		return dateFormat.format(date1);
	}

	public static String convertDateToString(String dateString) throws ParseException{
		// DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy");

		DateFormat originalFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
		DateFormat targetFormat = new SimpleDateFormat("M/d/yyyy");
		Date date = originalFormat.parse(dateString);
		String formattedDate = targetFormat.format(date);  // 20120821
		if(formattedDate.contains("8")){
			System.out.println("Hectic finally");
		}
		return formattedDate;
	}

	public static String checkIfStringIsDate(String dateString) throws ParseException{
		// DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy");

		DateFormat originalFormat = new SimpleDateFormat("M/d/yyyy");
		DateFormat targetFormat = new SimpleDateFormat("M/d/yyyy");
		try{
			Date date = originalFormat.parse(dateString);
			String formattedDate = targetFormat.format(date);  // 20120821
			if(formattedDate.contains("8")){
				System.out.println("Hectic finally");
			}
			return formattedDate;
		}catch(Exception e){
			return dateString;
		}
	}

	public static String makeTemporaryStringAdjustments(String string){
		if(string.contains("Process Status")){
			string = "Process status";
		}

		if(string.contains("Budget Raise Date")){
			string = string.trim();
		}
		return string;
	}

	public static List<String> returnList(String value){
		if(valueNOTNULL(value)){
			return Arrays.asList(value.split("\\|\\|@@\\|\\|"));
		}
		return new ArrayList<>();
	}
	
	public static String getScreenShotBaseLocation(String moduleFolder){
		try{
		System.out.println("");
		String className = Common.getCLassName();
		String [] classNames =  className.split("\\.");
		String screenShotBaseLocation = System.getProperty("user.dir")+"/src/HA/baseline/screenshots/"+moduleFolder+"/";
		java.io.File file = new java.io.File(screenShotBaseLocation);
		if(!file.exists()){
	      boolean result = file.mkdirs();
	      System.out.println("Status = " + result);
		}
		return screenShotBaseLocation;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static String convertDateToMDDYYYY(String value) throws ParseException{
		// DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy");
        if(Utils.valueNOTNULL(value)){
		DateFormat originalFormat = new SimpleDateFormat("M/d/yyyy");
		DateFormat targetFormat = new SimpleDateFormat("M/d/yyyy");	
		Date date = originalFormat.parse(value);
		String formattedDate = targetFormat.format(date);  // 20120821
		return formattedDate;
        }else{
        	return null;
        }

	}
	
	public static List<String> intializeKeysToIgnore(String... keysToIgnore ){
		List<String> keysToIgnoreList = new ArrayList<String>();
		if(keysToIgnore!=null){
			for(int i=0;i<keysToIgnore.length;i++){
				keysToIgnoreList.add(keysToIgnore[i].toLowerCase());
			}
		}
		return keysToIgnoreList;
	}
	
	public static List<String> convertToString(List<String> values){
		List<String> vals = new ArrayList<String>();
		for(int i=0;i<values.size();i++){
			try{
				vals.add(String.valueOf(values.get(i)));
			}catch(Exception e){
				
			}
             
		}
		return vals;
	}
	public static String getModule()
	{
		return getModule(Common.getCLassName());
	}
	public static String getModule(String currentClassname)
	{
		
		String[] clssplit = currentClassname.split("\\.");
		
		String getModule = clssplit[2];
		
		return getModule;
	}
	
	public static String getUserLocation(){
		return System.getProperty("user.dir");
	}
	
	
	public static String getScreenShotLocation(String moduleFolder){
		try{
		System.out.println("");
		String className = Common.getCLassName();
		String [] classNames =  className.split("\\.");
		String screenShotLocation = System.getProperty("user.dir")+"/TestLogs/screenShots/"+moduleFolder+"/"+classNames[classNames.length-1]+"/";
		java.io.File file = new java.io.File(screenShotLocation);
	    if(!file.exists()){
		boolean result = file.mkdirs();
	      System.out.println("Status = " + result);
	    }
		return screenShotLocation;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	

	
}
