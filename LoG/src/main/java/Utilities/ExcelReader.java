package Utilities;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.poi.ss.usermodel.CellType;


public class ExcelReader {
	
	 static XSSFSheet sheet;
		static XSSFWorkbook workbook;
		static XSSFCell cell;
		static XSSFRow row;
		static String fileName;
		
		public ExcelReader(String fileName) {
			// TODO Auto-generated constructor stub
			this.fileName=fileName;
		}
	
	/*
	 *  This function returns the list of Row number 'rowNumber',
	 *  @Parameter fileName - Name of the input Excel file XLSX
	 *  @Parameter rowNumber - Number of the row that we want to get
	 *  @Parameter lastColumn - Number of the last column
	 *  Example: rowNumber > 3 for 'EmployeeExport' test data
	 */
	public static List<String> getRow(XSSFSheet sheet, int rowNumber, int lastColumn) throws Exception {


		List<String> rowData = new ArrayList<>();
		XSSFRow row =  sheet.getRow(rowNumber);
		if(row!=null){
			int lastCellNumber = row.getLastCellNum();
			int columnsToTruncate = lastCellNumber - lastColumn ;
			if(columnsToTruncate < 0)
				columnsToTruncate = 0;
			for (int i=columnsToTruncate;i<lastColumn+columnsToTruncate;i++) {
				if(i==(lastColumn-1)){
					System.out.println("No column");
				}
				XSSFCell cell =  row.getCell(i);
				if(cell!=null){
					String str;
					FormulaEvaluator evaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
					DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
					df.setMaximumFractionDigits(6);
					switch (cell.getCellTypeEnum()){
					case FORMULA:
						// Assuming we are only getting doubles after formula evaluation
						// If this is not the case we have to move the logic to the outside of this switch 
						CellValue cellVal = evaluator.evaluate(cell);
						str = df.format(cellVal.getNumberValue());
						break;
					case STRING:
						str = Utils.checkIfStringIsDate(cell.getStringCellValue());						
						break;
					case BOOLEAN:
						str = String.valueOf(cell.getBooleanCellValue());
						break;
					case NUMERIC:
						if(HSSFDateUtil.isCellDateFormatted(cell)){
							str = Utils.convertDateToString(cell.getDateCellValue().toString());
						}else{
//							str = String.valueOf((double)cell.getNumericCellValue());
							str = df.format(cell.getNumericCellValue());
						}
						//str = String.valueOf((double)cell.getNumericCellValue());
						break;
					default:
						str = String.valueOf(cell.getStringCellValue());
						break;
					}

					rowData.add(str);
				}
				else{
					rowData.add(" ");
				}
			}
		}
		return rowData;
	}


	/*
	 *  This function returns the list of Row number 'rowNumber',
	 *  @Parameter fileName - Name of the input Excel file XLSX
	 *  @Parameter rowNumber - Number of the row that we want to get
	 *  Example: rowNumber > 3 for 'EmployeeExport' test data
	 */
	public static List<String> getRow(String fileName, int rowNumber) throws Exception {
		File file = new File(fileName);
		InputStream book1 = new FileInputStream(file);
		XSSFWorkbook wb = new XSSFWorkbook(book1);
		XSSFSheet sheet1 = wb.getSheetAt(0);

		List<String> rowData = new ArrayList<>();

		org.apache.poi.ss.usermodel.Row row = (org.apache.poi.ss.usermodel.Row) sheet1.getRow(rowNumber-1);

		Iterator<Cell> column = row.cellIterator();
		while (column.hasNext()) {
			Cell cell = (org.apache.poi.ss.usermodel.Cell) column.next();
			String str;
			switch (cell.getCellTypeEnum()){

			case STRING:
				str = cell.getStringCellValue();
				break;
			case BOOLEAN:
				str = String.valueOf(cell.getBooleanCellValue());
				break;
			case NUMERIC:
				if(HSSFDateUtil.isCellDateFormatted(cell)){
					str = cell.getDateCellValue().toString();
				}else{
					str = String.valueOf(cell.getNumericCellValue());
				}
				break;

			default:
				str = String.valueOf(cell.getStringCellValue());

			}

			rowData.add(str);
		}
		return rowData;
	}

	/*
	 *  This function returns the values of column names,
	 *  Assuming that the column names are in the 4th row of given excel sheet
	 *  @Parameter fileName - Name of the input Excel file XLSX
	 */
	public static List<String> getColumnNames(XSSFSheet sheet) throws Exception {
		String [] keystoIgnore = null;
		return getColumnNames(sheet,keystoIgnore);
	}

	public static List<String> getColumnNames(XSSFSheet sheet,String ... keysToIgnore) throws Exception {	
		return getColumnNames(sheet,3,keysToIgnore);
	}

	public static List<String> getColumnNames(XSSFSheet sheet,int startingRowIndex,String ... keysToIgnore) throws Exception {	

		ArrayList<String> keysToIgnoreList = new ArrayList<String>();
		if(keysToIgnore!=null){
			for(int i=0;i<keysToIgnore.length;i++){
				keysToIgnoreList.add(keysToIgnore[i].toLowerCase());
			}
		}
		List<String> columnNames = new ArrayList<>();

		XSSFRow row =  sheet.getRow(startingRowIndex);

		Iterator column = row.cellIterator();
		while (column.hasNext()) {
			XSSFCell cell = (XSSFCell) column.next();

			String cellValue = cell.getStringCellValue();
			cellValue = Utils.makeTemporaryStringAdjustments(cellValue);
			//if(!cellValue.equalsIgnoreCase("notes")){
			//if(!keysToIgnoreList.contains((cellValue.toLowerCase()))){
			columnNames.add(cellValue);
			//}
			cellValue = cellValue.replaceAll("‚", ",");
			cellValue = cellValue.replaceAll(",", ",");
		}
		return columnNames;
	}

	/*
	 *  This function returns the values of column names,
	 *  Assuming that the column names are in the 4th row of given excel sheet
	 *  @Parameter fileName - Name of the input Excel file XLSX
	 *  @Return int - lastColumn number of the excel sheet
	 */
	public static int getLastColumnNumber(String fileName) throws Exception {
		File file = new File(fileName);
		InputStream book1 = new FileInputStream(file);
		XSSFWorkbook wb = new XSSFWorkbook(book1);
		XSSFSheet sheet1 = wb.getSheetAt(0);

		org.apache.poi.ss.usermodel.Row row = (org.apache.poi.ss.usermodel.Row) sheet1.getRow(3);
		return row.getLastCellNum();
	}

	/*
	 *  This function returns the values of column names,
	 *  Assuming that the column names are in the 4th row of given excel sheet
	 *  @Parameter fileName - Name of the input Excel file XLSX
	 *  @Return int - lastRow number of the excel sheet
	 */
	public static int getLastRowNumber(String fileName) throws Exception {
		//File file = new File(fileName);
		//	InputStream book1 = new FileInputStream(file);
		XSSFWorkbook wb = new XSSFWorkbook(fileName);
		XSSFSheet sheet1 = wb.getSheet("Employee Report");
		System.out.println("No of rows are "+sheet1.getLastRowNum());
		System.out.println("No of rows are "+sheet1.getPhysicalNumberOfRows());
		return sheet1.getPhysicalNumberOfRows();
	}

	public static XSSFSheet getSheet(String fileName,String sheetName) throws IOException{
		XSSFWorkbook wb = new XSSFWorkbook(fileName);
		XSSFSheet sheet1 = wb.getSheet(sheetName);
		wb.close();
		return sheet1;
	}

	public static XSSFSheet getSheet(String fileName) throws IOException{
		File file = new File(fileName);
		if(!file.exists()){
			return null;
		}
		XSSFWorkbook wb = new XSSFWorkbook(fileName);
		XSSFSheet sheet1 = wb.getSheetAt(0);
		wb.close();
		return sheet1;
	}
	
	public static String getValue(String DataSet, String key) {
		//Map<String, String> cellVal=new HashMap<>();
		

		try {
			workbook = new XSSFWorkbook(new FileInputStream(new File(System.getProperty("user.dir")+"\\src\\test\\resources\\testData\\"+fileName)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sheet=workbook.getSheetAt(0);
		String data=null;
		int rowNumber;
		int cellNumber;
		for(int i=0;i<sheet.getLastRowNum();i++)
		{
			if(sheet.getRow(i).getCell(0).getStringCellValue().equalsIgnoreCase(DataSet))
			{	rowNumber=i;
				for(int j=0;j<sheet.getRow(0).getLastCellNum();j++)
				{
					if(sheet.getRow(0).getCell(j).getStringCellValue().equalsIgnoreCase(key))
					{
						
						cell=sheet.getRow(rowNumber).getCell(j);
						if(cell.getCellType()==cell.CELL_TYPE_STRING)
						{
							data=cell.getStringCellValue();
						}
						else if(cell.getCellType()==cell.CELL_TYPE_NUMERIC)
						{
							data= String.valueOf(cell.getNumericCellValue());
						}
						else if(cell.getCellType()==cell.CELL_TYPE_BOOLEAN)
						{
							data= String.valueOf(cell.getBooleanCellValue());
						}
						return data;
					}
				}

		}
		} return data;
				
	}
	
	public static Map<String, Map<String, String>> getExcelValue() {
		Map<String, String> cellVal=new HashMap<>();
		Map<String, Map<String, String>> excelData=new HashMap<>();

		try {
			workbook = new XSSFWorkbook(new FileInputStream(new File(System.getProperty("user.dir")+"\\src\\test\\resources\\testData\\"+fileName)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sheet=workbook.getSheetAt(0);
		String data=null;
		int rowNumber;
		int cellNumber;
		System.out.println(sheet.getLastRowNum());
		for(int i=1;i<=sheet.getLastRowNum();i++)
		{
			String dataSet=null;
			try {
			dataSet=sheet.getRow(i).getCell(0).getStringCellValue();
			}catch (Exception e) {
				break;
			}
			if(dataSet.isEmpty())
			{
				break;
			}
			{	
				cellVal=new HashMap<>();
				for(int j=1;j<sheet.getRow(i).getLastCellNum();j++)
				{
						cell=sheet.getRow(i).getCell(j);
						data=null;
						try {
						if(cell.getCellType()==cell.CELL_TYPE_STRING)
						{
							data=cell.getStringCellValue();
						}
						else if(cell.getCellType()==cell.CELL_TYPE_NUMERIC)
						{
							data= String.valueOf((long)(cell.getNumericCellValue()));
						}
						else if(cell.getCellType()==cell.CELL_TYPE_BOOLEAN)
						{
							data= String.valueOf(cell.getBooleanCellValue());
						}
						cellVal.put(sheet.getRow(0).getCell(j).getStringCellValue(), data);
						}
						catch (Exception e) {
							cellVal.put(sheet.getRow(0).getCell(j).getStringCellValue(), null);
						}
					}
					excelData.put(dataSet, cellVal);
				}

		}
		 return excelData;
				
	}
	
	
	public static Map<String, List<Map<String, String>>> getStateAddressValue() {
		Map<String, List<Map<String, String>>> cellVal=new HashMap<String, List<Map<String, String>>>();
		
		
		//Map<String, Map<String, String>> excelData=new HashMap<>();

		try {
			workbook = new XSSFWorkbook(new FileInputStream(new File(System.getProperty("user.dir")+"\\src\\test\\resources\\testData\\"+fileName)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sheet=workbook.getSheetAt(0);
		String data=null;
		int rowNumber;
		int cellNumber;
		System.out.println(sheet.getLastRowNum());
		List<Map<String, String>> values=new LinkedList();
		String state="";
		for(int i=1;i<=sheet.getLastRowNum();i++)
		{
			
		//	state=sheet.getRow(i).getCell(1).getStringCellValue();
			try {
			if(!sheet.getRow(i).getCell(1).getStringCellValue().isEmpty()||!(sheet.getRow(i).getCell(1).getStringCellValue().equals(""))) 
			{
			if(!cellVal.containsKey(sheet.getRow(i).getCell(1).getStringCellValue()) && values.size()>0)
			{   
				cellVal.put(state, values);	
				values=new LinkedList();
			}
			state=sheet.getRow(i).getCell(1).getStringCellValue().trim();
	
			}}
			catch(Exception e)
			{
				return cellVal;
			}
			
			Map<String, String> address=new HashMap<>();
						
	
		
				for(int j=2;j<sheet.getRow(i).getLastCellNum();j++)
				{
						System.out.print(sheet.getRow(i).getLastCellNum());
						cell=sheet.getRow(i).getCell(j);
						data=null;
						try {
						if(cell.getCellType()==cell.CELL_TYPE_STRING)
						{
							data=cell.getStringCellValue();
						}
						else if(cell.getCellType()==cell.CELL_TYPE_NUMERIC)
						{
							data= String.valueOf((long)(cell.getNumericCellValue()));
						}
						else if(cell.getCellType()==cell.CELL_TYPE_BOOLEAN)
						{
							data= String.valueOf(cell.getBooleanCellValue());
						}
						System.out.print(sheet.getRow(0).getCell(j).getStringCellValue());
						address.put(sheet.getRow(0).getCell(j).getStringCellValue().trim(),data.trim());
						
						
						}
						catch (Exception e) {
							address.put(sheet.getRow(0).getCell(j).getStringCellValue(), null);
						}
					}
					values.add(address);
				   	

		}
		 return cellVal;
				
	}

}
