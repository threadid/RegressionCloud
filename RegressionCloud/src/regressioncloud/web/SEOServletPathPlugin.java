package com.regressioncloud.web;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import com.regressioncloud.util.WebAppSettings;
import com.regressioncloud.pgsql.PGSQLService;

public class SEOServletPathPlugin extends AjaxModelFactory{

	private WebAppSettings webSettings = WebAppSettings.getObject();
	private String datePattern = "yyyy-mm-dd hh:mm:ss:SS";
	private String yearPattern = "yyyy";
	private SimpleDateFormat timeStamp = new SimpleDateFormat(datePattern);
	private SimpleDateFormat yearOnly = new SimpleDateFormat(yearPattern);
	private PGSQLService PSel;
	
	private static String PATH_QUERY_SQL = "SELECT * FROM seo_servlet_map('PATH_QUERY');";
	private static String QUERY_PATH_SQL = "SELECT * FROM seo_servlet_map('QUERY_PATH');";
	private static String DEFAULT_JSON = "{\"cols\":null,\"rows\":null,\"p\":null}";

	public SEOServletPathPlugin() {
	}
	
	@Override
	public String getHTMLResponse () {
		String htmlObject = "<p></p>";
		return htmlObject;	
	}

	@Override
	public String getXMLResponse () {
		return "<?xml version='1.0' encoding='UTF-8'?><xmlpayload></xmlpayload>";
	}

	@Override
	public String getJSONResponse () {		
		return DEFAULT_JSON; 
	}

	@Override
	public String getSVGResponse () {
		String svgObject = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><svg></svg>";
		return svgObject;
	}
	
	public Map<String, Object> getPathQueryMap(){
		Map<String, Object> pathQuery = new LinkedHashMap<String, Object>();
		Map<String, Object> seoMap = new LinkedHashMap<String, Object>();

		ResultSet queryResult = null;
		try {
			openDataService();
	  		this.PSel.setPgSQLStatement(PATH_QUERY_SQL);
			queryResult = getDataFromService();
			if (!(queryResult instanceof ResultSet))
			{
				pathQuery = new LinkedHashMap<String, Object>();
				return pathQuery; 
			}
	    } catch (NullPointerException npe) {
			pathQuery = new LinkedHashMap<String, Object>();
			return pathQuery; 
		} catch (Exception e) {
			pathQuery = new LinkedHashMap<String, Object>();
			return pathQuery; 
		}
		
		//System.out.println("Before while next  = " + timeStamp.format(Calendar.getInstance(Locale.US).getTimeInMillis()));
		try {
			while (queryResult.next()) {
				seoMap = new LinkedHashMap<String, Object>();
				seoMap.put("servlet_path", queryResult.getString(1).trim());
				seoMap.put("content_path", queryResult.getString(2).trim());
				seoMap.put("query", queryResult.getString(3).trim());
				seoMap.put("uri", queryResult.getString(4).trim());
				seoMap.put("mime", queryResult.getString(5).trim());
				pathQuery.put(queryResult.getString(2).trim(),seoMap);
			}
		} catch (SQLException sqle) {
			System.out.println("SQLException>" + sqle + "<");
			pathQuery = new LinkedHashMap<String, Object>();
			return pathQuery; 
		}
				
		closeDataService();
		
		return pathQuery;
	}

	public Map<String, Object> getQueryPathMap(){
		Map<String, Object> queryPath = new LinkedHashMap<String, Object>();
		Map<String, Object> seoMap = new LinkedHashMap<String, Object>();

		ResultSet queryResult = null;
		try {
			openDataService();
	  		this.PSel.setPgSQLStatement(QUERY_PATH_SQL);
			queryResult = getDataFromService();
			if (!(queryResult instanceof ResultSet))
			{
				queryPath = new LinkedHashMap<String, Object>();
				return queryPath; 
			}
	    } catch (NullPointerException npe) {
	    	queryPath = new LinkedHashMap<String, Object>();
			return queryPath; 
		} catch (Exception e) {
			queryPath = new LinkedHashMap<String, Object>();
			return queryPath; 
		}
		
		//System.out.println("Before while next  = " + timeStamp.format(Calendar.getInstance(Locale.US).getTimeInMillis()));
		try {
			while (queryResult.next()) {
				seoMap = new LinkedHashMap<String, Object>();
				seoMap.put("servlet_path", queryResult.getString(1).trim());
				seoMap.put("content_path", queryResult.getString(2).trim());
				seoMap.put("query", queryResult.getString(3).trim());
				seoMap.put("uri", queryResult.getString(4).trim());
				seoMap.put("mime", queryResult.getString(5).trim());
				queryPath.put(queryResult.getString(3).trim(),seoMap);
			}
		} catch (SQLException sqle) {
			System.out.println("SQLException>" + sqle + "<");
			queryPath = new LinkedHashMap<String, Object>();
			return queryPath; 
		}
				
		closeDataService();
		
		return queryPath;
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
