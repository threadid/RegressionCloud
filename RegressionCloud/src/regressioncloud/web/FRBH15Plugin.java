package com.regressioncloud.web;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import com.regressioncloud.util.WebAppSettings;
import com.regressioncloud.web.JSONQuery.contentElement;
import com.regressioncloud.pgsql.PGSQLService;

public class FRBH15Plugin extends AjaxModelFactory{

	private WebAppSettings webSettings = WebAppSettings.getObject();
	private String datePattern = "yyyy-mm-dd hh:mm:ss:SS";
	private String yearPattern = "yyyy";
	private SimpleDateFormat timeStamp = new SimpleDateFormat(datePattern);
	private SimpleDateFormat yearOnly = new SimpleDateFormat(yearPattern);
	private PGSQLService PSel;
	
	private static String DEFAULT_JSON = "{\"cols\":null,\"rows\":null,\"p\":null}";
	private static String FRB_H15_SQL = "SELECT * FROM frb_h15_yieldcurve(?,?,?);";
	
	public FRBH15Plugin() {
	}
	
	@Override
	public String getHTMLResponse () {
		return "";
	}

	@Override
	public String getXMLResponse () {
		return "<?xml version='1.0' encoding='UTF-8'?><xmlpayload></xmlpayload>";
	}

	@Override
	public String getJSONResponse () {
		String jsonResult = "";
		StringBuilder jsonResultBuilder = new StringBuilder();
		ResultSet queryResult = null;
		try {
			openDataService();
			buildFRB_H15_SQL();
			queryResult = getDataFromService();
			if (!(queryResult instanceof ResultSet))
			{
				System.out.println("Not Connected");
				return "{\"cols\":null,\"rows\":null,\"p\":null}"; 
			}
	    } catch (NullPointerException npe) {
			System.out.println("Null Pointer Exception");
			return "{\"cols\":null,\"rows\":null,\"p\":null}"; 
		} catch (Exception e) {
			System.out.println("No Results");
			return "{\"cols\":null,\"rows\":null,\"p\":null}"; 
		}
		
		jsonResultBuilder.append("{\"cols\":[");
		jsonResultBuilder.append("{\"id\":\"freq\",\"label\":\"Reporting Interval\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"ratedate\",\"label\":\"Rate Date\",\"type\":\"date\"},");
		jsonResultBuilder.append("{\"id\":\"y30\",\"label\":\"30 Year\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"y20\",\"label\":\"20 Year\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"y10\",\"label\":\"10 Year\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"y7\",\"label\":\"7 Year\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"y5\",\"label\":\"5 Year\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"y3\",\"label\":\"3 Year\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"y2\",\"label\":\"2 Year\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"y1\",\"label\":\"1 Year\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"m6\",\"label\":\"6 Month\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"m3\",\"label\":\"3 Month\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"m1\",\"label\":\"1 Month\",\"type\":\"number\"}]");
		jsonResultBuilder.append(",\"rows\":[");
		
//		System.out.println("Before while next  = " + timeStamp.format(Calendar.getInstance(Locale.US).getTimeInMillis()));

		try {
			while (queryResult.next()) {
				
				int mo = Integer.parseInt( queryResult.getString(3).trim());
				jsonResultBuilder.append("{\"c\":[");
				jsonResultBuilder.append("{\"v\":\"" + (queryResult.getString(1) != null?queryResult.getString(1).trim():null) + "\"}");
				jsonResultBuilder.append(",{\"v\":\"Date(" + queryResult.getString(2).trim());
				jsonResultBuilder.append(", " + Integer.toString(mo-1));
				jsonResultBuilder.append(", " + queryResult.getString(4).trim() + ")\"}");
				jsonResultBuilder.append(",{\"v\":" + (queryResult.getString(5) != null?queryResult.getString(5).trim():null) + "}");
				jsonResultBuilder.append(",{\"v\":" + (queryResult.getString(6) != null?queryResult.getString(6).trim():null) + "}");
				jsonResultBuilder.append(",{\"v\":" + (queryResult.getString(7) != null?queryResult.getString(7).trim():null) + "}");
				jsonResultBuilder.append(",{\"v\":" + (queryResult.getString(8) != null?queryResult.getString(8).trim():null) + "}");
				jsonResultBuilder.append(",{\"v\":" + (queryResult.getString(9) != null?queryResult.getString(9).trim():null) + "}");
				jsonResultBuilder.append(",{\"v\":" + (queryResult.getString(10) != null?queryResult.getString(10).trim():null) + "}");
				jsonResultBuilder.append(",{\"v\":" + (queryResult.getString(11) != null?queryResult.getString(11).trim():null) + "}");
				jsonResultBuilder.append(",{\"v\":" + (queryResult.getString(12) != null?queryResult.getString(12).trim():null) + "}");
				jsonResultBuilder.append(",{\"v\":" + (queryResult.getString(13) != null?queryResult.getString(13).trim():null) + "}");
				jsonResultBuilder.append(",{\"v\":" + (queryResult.getString(14) != null?queryResult.getString(14).trim():null) + "}");
				jsonResultBuilder.append(",{\"v\":" + (queryResult.getString(15) != null?queryResult.getString(15).trim():null) + "}");
				jsonResultBuilder.append("]},");
			}
			jsonResultBuilder.deleteCharAt(jsonResultBuilder.length()-1);
		} catch (SQLException sqle) {
			System.out.println("SQLException>" + sqle + "<");
			return "{\"cols\":null,\"rows\":null,\"p\":null}"; 
		}
//		System.out.println("After while next  = " + timeStamp.format(Calendar.getInstance(Locale.US).getTimeInMillis()));
				
		jsonResultBuilder.append("],\"p\":null}");

		jsonResult = jsonResultBuilder.toString();
		
		closeDataService();
		
		return jsonResult;
	
	}

	@Override
	public String getSVGResponse () {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><svg></svg>";
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
              case "awsrds"    : 
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

	protected void buildFRB_H15_SQL() {
		String eid="";
		String freq="";
  		String fromDate="";
  		String toDate="";
  		
  		this.PSel.setPgSQLStatement(FRB_H15_SQL);
		
		Iterator<contentElement> ei = this.jq.getContent().getElements().iterator();
		contentElement ce = new contentElement();
		if (ei.hasNext()){
			ce = ei.next();
			eid=ce.getElementId();
		} 
		
		try {
			freq = (String)this.jq.getContent().getQueriesMap(eid).get("freq");
			fromDate = (String)this.jq.getContent().getQueriesMap(eid).get("fromdate");
			toDate = (String)this.jq.getContent().getQueriesMap(eid).get("todate");
	    } catch (NullPointerException npe) {
	    	freq="b";
	    	fromDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis());
	    	toDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis());
	    } catch (Exception e) {
	    	freq="b";
	    	fromDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis());
	    	toDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis());
		}
		
	    this.PSel.setParamString(1, freq);
	    this.PSel.setParamString(2, fromDate);
	    this.PSel.setParamString(3, toDate);
	    
	}
}
