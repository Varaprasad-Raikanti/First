package Utilities;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import TestLib.Automation_properties;
import TestLib.Common;
import TestLib.Driver;

public class DB {
	private static Map<String,DbDetails> dbMap = new HashMap<>();
	
	private static Connection getConn() {
		if(dbMap.containsKey(Common.getCLassName()))
			return dbMap.get(Common.getCLassName()).getConn();
		return null;
	}

	private static void setConn(Connection conn) {
		if(dbMap.containsKey(Common.getCLassName())){
			dbMap.get(Common.getCLassName()).setConn(conn);
		}else{
			DbDetails obj = new DbDetails();
			obj.setConn(conn);
			dbMap.put(Common.getCLassName(), obj);
		}
	}
	private static Statement getStmt() {
		if(dbMap.containsKey(Common.getCLassName()))
			return dbMap.get(Common.getCLassName()).getStmt();
		return null;
	}

	private static void setStmt(Statement stmt) {
		if(dbMap.containsKey(Common.getCLassName())){
			dbMap.get(Common.getCLassName()).setStmt(stmt);
		}else{
			DbDetails obj = new DbDetails();
			obj.setStmt(stmt);
			dbMap.put(Common.getCLassName(), obj);
		}
	}
	private static ResultSet getRs() {
		if(dbMap.containsKey(Common.getCLassName()))
			return dbMap.get(Common.getCLassName()).getRs();
		return null;
	}

	private static void setRs(ResultSet rs) {
		if(dbMap.containsKey(Common.getCLassName())){
			dbMap.get(Common.getCLassName()).setRs(rs);
		}else{
			DbDetails obj = new DbDetails();
			obj.setRs(rs);
			dbMap.put(Common.getCLassName(), obj);
		}
	}
	private static ResultSetMetaData getRsmd() {
		if(dbMap.containsKey(Common.getCLassName()))
			return dbMap.get(Common.getCLassName()).getRsmd();
		return null;
	}

	private static void setRsmd(ResultSetMetaData rsmd) {
		if(dbMap.containsKey(Common.getCLassName())){
			dbMap.get(Common.getCLassName()).setRsmd(rsmd);
		}else{
			DbDetails obj = new DbDetails();
			obj.setRsmd(rsmd);
			dbMap.put(Common.getCLassName(), obj);
		}
	}
	public static void conDB() throws ClassNotFoundException, SQLException{
		try {
			String db_connect_string =Automation_properties.getInstance().getProperty(Automation_properties.CONN_STR);
			Driver.getLogger().info("Connection String for " + Common.getCLassName() +" is " + db_connect_string);
			//Class.forName(HA.TestAutomation.HATF_properties.getInstance().getProperty(HATF_properties.SQLJAVADriver));
			setConn(DriverManager.getConnection(db_connect_string));
			Driver.getLogger().info("DB Connection Success");
			setStmt(getConn().createStatement());
		}
		catch(Exception e){
			e.printStackTrace();
			Driver.getLogger().error(e);
		}
	}

	public static void conDB(String db_connect_string, String dataset) throws ClassNotFoundException, SQLException{
		try{
			setConn(DriverManager.getConnection(db_connect_string));
			Driver.getLogger().info("DB Connection Success");
			setStmt(getConn().createStatement());
		}
		catch(Exception e){
			e.printStackTrace();
			Driver.getLogger().error(e);
		}
	}

	public static String QueryDB(String queryString) throws ClassNotFoundException, SQLException{
		String Horizon=null;

		try{

			conDB();
			//queryString = "Select PARAM_VAL from HOST_BPM_CONSTANTS WHERE PARAM_KEY='Horizon_DataLoad'";
			setRs(getStmt().executeQuery(queryString));
			setRsmd(getRs().getMetaData());
			// int dbcols = rsmd.getColumnCount();


			while (getRs().next()) {

				Driver.getLogger().info(getRs().getString(1));    		

				Horizon = getRs().getString(1);
			}

		}
		catch(Exception e){
			e.printStackTrace();
			Driver.getLogger().error(e);
		}
		finally{
			getRs().close();                       // Close the ResultSet                  
			getStmt().close();   				   // Close the Statement
		}
		return Horizon;

	}

	public static String getDBdata(String query) throws ClassNotFoundException, SQLException{
		String Horizon=null;

		try{

			conDB();

			setRs(getStmt().executeQuery(query));
			setRsmd(getRs().getMetaData());
			// int dbcols = rsmd.getColumnCount();

			while (getRs().next()) {	     	

				Horizon = getRs().getString(1);
			}

		}
		catch(Exception e){
			e.printStackTrace();
			Driver.getLogger().error(e);
		}
		finally{
			getRs().close();                       // Close the ResultSet                  
			getStmt().close();   				   // Close the Statement
		}
		return Horizon;

	}

	public static String queryDBData(String query) throws ClassNotFoundException, SQLException{
		String Horizon=null;

		try{

			setRs(getStmt().executeQuery(query));
			setRsmd(getRs().getMetaData());
			// int dbcols = rsmd.getColumnCount();

			while (getRs().next()) {	     	

				Horizon = getRs().getString(1);
			}

		}
		catch(Exception e){
			e.printStackTrace();
			Driver.getLogger().error(e);
		}
		finally{
			getRs().close();                       // Close the ResultSet                  
			getStmt().close();   				   // Close the Statement
		}
		return Horizon;

	}




	
	public static void exeDBmultiline(String[] query) throws ClassNotFoundException, SQLException{
		try{
			for(String queryString: query)
			{
				int rs  =getStmt().executeUpdate(queryString);
				Driver.getLogger().info(rs);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			Driver.getLogger().error(e);
		}
		finally{
			//rs.close();                       // Close the ResultSet                  
			getStmt().close();   				   // Close the Statement
		}
	}

	public static ResultSet executeQuery(String datafile, String dataset,String queryString ) throws SQLException, ClassNotFoundException {
		conDB(datafile, dataset);
		return getStmt().executeQuery(queryString);
	}
	
	public static void closeAll()
	{
		try {
			//rs.close();
			getStmt().close();
			getConn().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Driver.getLogger().error(e);
		}

	}

	public static void runQuery(String datafile, String dataset,String[] query) throws ClassNotFoundException, SQLException
	{
		DB.conDB(datafile, dataset);
		DB.exeDBmultiline(query);
		Driver.getLogger().info("Query------------"+query);
		DB.closeAll();
	}

	public static String getQueriedData(String datafile, String dataset,String query) throws ClassNotFoundException, SQLException
	{
		DB.conDB(datafile, dataset);
		String row=DB.queryDBData(query);
		Driver.getLogger().info("Query------------"+query);
		DB.closeAll();
		return row;
	}

	public static void conDB(String connString) throws ClassNotFoundException, SQLException{
		try{
			Class.forName(Automation_properties.getInstance().getProperty(Automation_properties.SQLJAVADriver));
			setConn(DriverManager.getConnection(connString));
			Driver.getLogger().info("DB Connection Success");
			setStmt(getConn().createStatement());
		}
		catch(Exception e){
			e.printStackTrace();
			Driver.getLogger().error(e);
		}
	}



}
