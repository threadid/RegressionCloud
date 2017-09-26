package com.regressioncloud.pgsql;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.apache.tomcat.jdbc.pool.DataSource;

public class PGSQLService {
	
	// Class Level Variables
	private boolean pgSQLConnectState = false;
	private String pgSQLStatement = "";

	// Class Level Objects
	private Connection objDBConn;
	private PreparedStatement pgSQLPrepared = null;
	private DataSource objDS;
	private ResultSet queryResult;
	
    
    public PGSQLService() {
    }

	public void setPgSQLStatement(String pgSQLStatement) {
		this.pgSQLStatement = pgSQLStatement;

		if (!this.isConnect())
		{
			System.out.println("setPgSQLStatement : Database not connected");
			if (!this.PGSQLConnect()){
				return;
			}
		}
		
		if (pgSQLStatement=="")
		{
			System.out.println("setPgSQLStatement : Missing SQL Statement");
		}
		try {
			this.pgSQLPrepared = this.objDBConn.prepareStatement(this.pgSQLStatement, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
			this.pgSQLPrepared.setFetchDirection(ResultSet.FETCH_UNKNOWN);
			this.pgSQLPrepared.setFetchSize(1000);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    public boolean PGSQLConnect() {
    	if (this.isConnect()) {
			return this.pgSQLConnectState;
		}

    	PGSQLDSBuilder PGSQLDSB = new PGSQLDSBuilder();

    	if (!(PGSQLDSB instanceof PGSQLDSBuilder) ) {
    		System.out.println("Failed DataSourceBuilder Object");
    	}
    	
    	this.objDS = PGSQLDSB.getDS();
    	
    	if (!(objDS instanceof DataSource) ) {
    		System.out.println("Failed DataSource Object");
    	}

		try {
			this.objDBConn = objDS.getConnection();
			this.objDBConn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
				this.pgSQLConnectState = false;
				return this.pgSQLConnectState;
			}
		this.pgSQLConnectState = true;
    	return this.pgSQLConnectState;
    }
    
    public void pgsqlClose() {

    	if (this.queryResult!=null) 
        	try{
        		this.queryResult.close();
        	} catch(SQLException e) {e.printStackTrace();};
    	if (this.pgSQLPrepared!=null) 
        	try{
        		this.pgSQLPrepared.close();
        	} catch(SQLException e) {e.printStackTrace();};
    	if (this.objDBConn!=null) 
        	try{
        		this.objDBConn.close();
        	} catch(SQLException e) {e.printStackTrace();};
    }
    
	public boolean isConnect() {
		try {
			if (this.objDBConn.isClosed()){
				this.pgSQLConnectState = false;
			} else {
				this.pgSQLConnectState = true;
			}
			return this.pgSQLConnectState;
		} catch(SQLException e) {
			this.pgSQLConnectState=false;
			return this.pgSQLConnectState;
		} catch(Exception e) {
			this.pgSQLConnectState=false;
			return this.pgSQLConnectState;
		}
	}

	public void setParamString(int idx, String strParam) {
		try {
			this.pgSQLPrepared.setString(idx, strParam);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	};
	
	public void setParamInt(int idx, int intParam) {
		try {
			this.pgSQLPrepared.setInt(idx, intParam);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	};
	
	public void setParamDouble(int idx, double dblParam) {
		try {
			this.pgSQLPrepared.setDouble(idx, dblParam);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	};
	
	public void setParamDate(int idx, Date dtParam) {
		try {
			this.pgSQLPrepared.setDate(idx, dtParam);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	};
	
	public ResultSet callService() throws SQLException {

		String datePattern = "yyyy-mm-dd hh:mm:ss:SS";
		SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
		//System.out.println("start callService() = " + formatter.format(Calendar.getInstance(Locale.US).getTimeInMillis()));

		if (!this.isConnect())
		{
			System.out.println("Database not connected");
			if (!this.PGSQLConnect()){
				return this.queryResult = null;
			}
		}
		
		if (pgSQLStatement=="")
		{
			System.out.println("Missing SQL Statement");
			return this.queryResult = null;
		}

		try {
						
			//System.out.println(">>" + this.pgSQLPrepared.toString() + "<<");
			this.queryResult = this.pgSQLPrepared.executeQuery();
	
		} catch (SQLException e) {
			System.out.println("Query Failed> " + e);
			e.printStackTrace();
			return this.queryResult = null;
		}

		//System.out.println("after execute sql = " + formatter.format(Calendar.getInstance(Locale.US).getTimeInMillis()));
        
		return this.queryResult;
	}

	public void callServiceUpdate() throws SQLException {
	
		String datePattern = "yyyy-mm-dd hh:mm:ss:SS";
		SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
		//System.out.println("start callService() = " + formatter.format(Calendar.getInstance(Locale.US).getTimeInMillis()));
	
		if (!this.isConnect())
		{
			System.out.println("Database not connected");
			if (!this.PGSQLConnect()){
				return;
			}
		}
		
		if (pgSQLStatement=="")
		{
			System.out.println("Missing SQL Statement");
			return;
		}
	
		try {
			//System.out.println(">>" + this.pgSQLPrepared.toString() + "<<");
			this.pgSQLPrepared.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Query Failed> " + e);
			e.printStackTrace();
			return;
		}
		//System.out.println("after execute sql = " + formatter.format(Calendar.getInstance(Locale.US).getTimeInMillis()));
	}

}

