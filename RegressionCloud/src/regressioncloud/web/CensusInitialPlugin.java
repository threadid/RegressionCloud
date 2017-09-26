package com.regressioncloud.web;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

//import org.w3c.dom.NodeList;



import java.util.Map;

import com.regressioncloud.util.WebAppSettings;
import com.regressioncloud.pgsql.PGSQLService;

public class CensusInitialPlugin extends AjaxModelFactory{

	private WebAppSettings webSettings;
	private String datePattern = "yyyy-mm-dd hh:mm:ss:SS";
	private String yearPattern = "yyyy";
	private SimpleDateFormat timeStamp = new SimpleDateFormat(datePattern);
	private SimpleDateFormat yearOnly = new SimpleDateFormat(yearPattern);
	private PGSQLService PSel;

	private static String JSON_DEFAULT = "{\"initial\":{\"years\":,\"columns\":}}";
	private static String CENSUS_YEARS_SQL = "SELECT estyear, estdata FROM census_coverage GROUP BY estyear, estdata ORDER BY estyear, estdata;";
	private static String CENSUS_COLUMNS_SQL = "SELECT * FROM column_labels('census_pop_est_alldata');";
	
	public CensusInitialPlugin() {
		this.webSettings = WebAppSettings.getObject();
	}
	
	@Override
	public String getHTMLResponse () {
		return "<p></p>";
	}

	@Override
	public String getXMLResponse () {
		return "<?xml version='1.0' encoding='UTF-8'?><xmlpayload></xmlpayload>";
	}

	@Override
	public String getJSONResponse () {		
		String jsonResult = JSON_DEFAULT;
		StringBuilder jsonSerializer = new StringBuilder();
		jsonSerializer.append(jsonResult);
		jsonSerializer.insert(jsonSerializer.indexOf("\"years\":")+8, jsonYears());
		jsonSerializer.insert(jsonSerializer.indexOf("\"columns\":")+10, jsonColumns());
		jsonResult = jsonSerializer.toString();
		
		//System.out.println(">>" + jsonResult + "<<");

		return jsonResult;
	
	}

	private String jsonYears() {
		StringBuilder jsonResultBuilder = new StringBuilder();
		ResultSet queryResult = null;
		try {
			openDataService();
			this.PSel.setPgSQLStatement(CENSUS_YEARS_SQL);
			queryResult = getDataFromService();
			if (!(queryResult instanceof ResultSet))
			{
				return JSON_DEFAULT; 
			}
	    } catch (NullPointerException npe) {
			return JSON_DEFAULT; 
		} catch (Exception e) {
			return JSON_DEFAULT; 
		}
		
		jsonResultBuilder.append("{\"cols\":[");
		jsonResultBuilder.append("{\"id\":\"estyear\",\"label\":\"Estimate Year\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"estdata\",\"label\":\"Estimate Data\",\"type\":\"string\"}]");
		jsonResultBuilder.append(",\"rows\":[");
		
		//System.out.println("Before while next  = " + timeStamp.format(Calendar.getInstance(Locale.US).getTimeInMillis()));
		try {
			while (queryResult.next()) {
				jsonResultBuilder.append("{\"c\":[");
				jsonResultBuilder.append("{\"v\":\"" + queryResult.getString(1).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(2).trim() + "\"}");
				jsonResultBuilder.append("]},");
			}
			jsonResultBuilder.deleteCharAt(jsonResultBuilder.length()-1);
		} catch (SQLException sqle) {
			System.out.println("SQLException>" + sqle + "<");
			return JSON_DEFAULT; 
		}
				
		//System.out.println("After while next  = " + timeStamp.format(Calendar.getInstance(Locale.US).getTimeInMillis()));

		jsonResultBuilder.append("],\"p\":null}");

		closeDataService();
        
		return jsonResultBuilder.toString();
		
	}

	private String jsonColumns() {
		StringBuilder jsonResultBuilder = new StringBuilder();
		ResultSet queryResult = null;
		try {
			openDataService();
			this.PSel.setPgSQLStatement(CENSUS_COLUMNS_SQL);
			queryResult = getDataFromService();
			if (!(queryResult instanceof ResultSet))
			{
				return JSON_DEFAULT; 
			}
	    } catch (NullPointerException npe) {
			return JSON_DEFAULT; 
		} catch (Exception e) {
			return JSON_DEFAULT; 
		}
		
		jsonResultBuilder.append("{\"cols\":[");
		jsonResultBuilder.append("{\"id\":\"estdata\",\"label\":\"Estimate Data\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"estdatalabel\",\"label\":\"Estimate Data Label\",\"type\":\"string\"}]");
		jsonResultBuilder.append(",\"rows\":[");
		
		//System.out.println("Before while next  = " + timeStamp.format(Calendar.getInstance(Locale.US).getTimeInMillis()));
		try {
			while (queryResult.next()) {
				jsonResultBuilder.append("{\"c\":[");
				jsonResultBuilder.append("{\"v\":\"" + queryResult.getString(1).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(2).trim() + "\"}");
				jsonResultBuilder.append("]},");
			}
			jsonResultBuilder.deleteCharAt(jsonResultBuilder.length()-1);
		} catch (SQLException sqle) {
			System.out.println("SQLException>" + sqle + "<");
			return JSON_DEFAULT; 
		}
				
		//System.out.println("After while next  = " + timeStamp.format(Calendar.getInstance(Locale.US).getTimeInMillis()));

		jsonResultBuilder.append("],\"p\":null}");

		closeDataService();

		//System.out.println("jsonlabels>>" + jsonResultBuilder.toString() + "<<");
		
		return jsonResultBuilder.toString();
		
	}

	@Override
	public String getSVGResponse () {

		String svgObject = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><svg></svg>";
		return svgObject;
		
	}

	@Override
	protected void openDataService() {
		switch (webSettings.getDefaultSetting("dataService")) {
              case "awsrds"    : 
            	  return;
		      case "mysql"    : 
		          return;
		      case "postgres"    : 
		    	  this.PSel = new PGSQLService();
		    	  this.PSel.PGSQLConnect();
    	          return;
		      default        : 
		          return ;
		    }
	}

	@Override
	protected ResultSet getDataFromService () {
		ResultSet queryResult = null;
		switch (webSettings.getDefaultSetting("dataService")) {
		      case "awsrds" : 
		          return null;      
		      case "mysql"    : 
		          return null;
		      case "postgres"    : 
		    	  try {
		    		  queryResult = this.PSel.callService();
		    		  return queryResult;
		    	  } catch (SQLException e) {
		    		  System.out.println("failed: callService>" + e);
		    		  return null;
		    	  } 
		      default        : 
		          return null;
		    }
	}

	@Override
	protected void closeDataService() {
		switch (webSettings.getDefaultSetting("dataService")) {
              case "awsrds"    : 
            	  return;
		      case "mysql"    : 
		          return;
		      case "postgres"    : 
		    	  this.PSel.pgsqlClose();
    	          return;
		      default        : 
		          return ;
		    }
	}

}
