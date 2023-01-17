package Utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

class DbDetails {
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private ResultSetMetaData rsmd = null;
	public Connection getConn() {
		return conn;
	}
	public void setConn(Connection conn) {
		this.conn = conn;
	}
	public Statement getStmt() {
		return stmt;
	}
	public void setStmt(Statement stmt) {
		this.stmt = stmt;
	}
	public ResultSet getRs() {
		return rs;
	}
	public void setRs(ResultSet rs) {
		this.rs = rs;
	}
	public ResultSetMetaData getRsmd() {
		return rsmd;
	}
	public void setRsmd(ResultSetMetaData rsmd) {
		this.rsmd = rsmd;
	}
}