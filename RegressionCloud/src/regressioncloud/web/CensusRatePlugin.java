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
import java.util.Locale;

import com.regressioncloud.util.WebAppSettings;
import com.regressioncloud.pgsql.PGSQLService;

public class CensusRatePlugin extends AjaxModelFactory{

	private WebAppSettings webSettings = WebAppSettings.getObject();
	private String datePattern = "yyyy-mm-dd hh:mm:ss:SS";
	private String yearPattern = "yyyy";
	private SimpleDateFormat timeStamp = new SimpleDateFormat(datePattern);
	private SimpleDateFormat yearOnly = new SimpleDateFormat(yearPattern);
	private PGSQLService PSel;
	
	private static String RATE_SQL = "SELECT * FROM census_rate(?,?);";

	public CensusRatePlugin() {
	}
	
	@Override
	public String getHTMLResponse () {
		String htmlObject = "<p></p>";
		String htmlFile = "";
		
		try {
			htmlFile = (String)this.jq.getContent().getQueriesMap("tabCensusRate").get("Resource");
	    } catch (NullPointerException npe) {
			return htmlObject = "<p></p>";
	    } catch (Exception e) {
			return htmlObject = "<p></p>";
		}
		
		try {
			String cpath = webSettings.getServerPath("html/" + htmlFile);
			FileInputStream fis = new FileInputStream(cpath);
			InputStreamReader isr = new InputStreamReader(fis);
			StringBuffer buffer = new StringBuffer();
			Reader in = new BufferedReader(isr);
	        int ch;
	        while ((ch = in.read()) > -1) {
	            buffer.append((char)ch);
	        }
	        in.close();
	        htmlObject = buffer.toString();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			return htmlObject = "<p></p>";
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
			return htmlObject = "<p></p>";
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return htmlObject = "<p></p>";
		}
		return htmlObject;
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
			buildRATE_SQL();
			queryResult = getDataFromService();
			if (!(queryResult instanceof ResultSet))
			{
				//System.out.println("Not Connected");
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
		jsonResultBuilder.append("{\"id\":\"sumlev\",\"label\":\"SumLev\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"estdata\",\"label\":\"Estimate\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"estyear\",\"label\":\"Year\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"state\",\"label\":\"State\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"stname\",\"label\":\"State Name\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"estval\",\"label\":\"Estimate\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"percentile\",\"label\":\"Percentile\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"pn\",\"label\":\"N\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"psum\",\"label\":\"Sum\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"pmin\",\"label\":\"Min\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"pmax\",\"label\":\"Max\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"prange\",\"label\":\"Range\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"pmean\",\"label\":\"Mean\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"pmedian\",\"label\":\"Median\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"pvariance\",\"label\":\"Variance\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"pstndev\",\"label\":\"Standard Dev\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"pskew\",\"label\":\"Skewness\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"pkurt\",\"label\":\"Kurtosis\",\"type\":\"number\"}]");
		jsonResultBuilder.append(",\"rows\":[");
		

		//System.out.println("Before while next  = " + timeStamp.format(Calendar.getInstance(Locale.US).getTimeInMillis()));
		try {
			while (queryResult.next()) {
				jsonResultBuilder.append("{\"c\":[");
				jsonResultBuilder.append("{\"v\":\"" + queryResult.getString(1).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(2).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(3).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(6).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(8).trim() + "\"}");
				switch (queryResult.getString(2).trim()) {
					case "rbirth":
						jsonResultBuilder.append(",{\"v\":" + queryResult.getString(10).trim() + "}");
						break;
					case "rdeath":
						jsonResultBuilder.append(",{\"v\":" + queryResult.getString(11).trim() + "}");
						break;
					case "rnatchg":
						jsonResultBuilder.append(",{\"v\":" + queryResult.getString(12).trim() + "}");
						break;
					case "rintmig":
						jsonResultBuilder.append(",{\"v\":" + queryResult.getString(13).trim() + "}");
						break;
					case "rdommig":
						jsonResultBuilder.append(",{\"v\":" + queryResult.getString(14).trim() + "}");
						break;
					case "rnetmig":
						jsonResultBuilder.append(",{\"v\":" + queryResult.getString(15).trim() + "}");
						break;
					case "rnpopchg":
						jsonResultBuilder.append(",{\"v\":" + queryResult.getString(16).trim() + "}");
						break;
					default:
						jsonResultBuilder.append(",{\"v\":0}");
						break;
				}
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(17).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(18).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(19).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(20).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(21).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(22).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(23).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(24).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(25).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(26).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(27).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(28).trim() + "}");
				jsonResultBuilder.append("]},");
			}
			jsonResultBuilder.deleteCharAt(jsonResultBuilder.length()-1);
		} catch (SQLException sqle) {
			System.out.println("SQLException>" + sqle + "<");
			return "{\"cols\":null,\"rows\":null,\"p\":null}"; 
		}
				
		//System.out.println("After while next  = " + timeStamp.format(Calendar.getInstance(Locale.US).getTimeInMillis()));

		jsonResultBuilder.append("],\"p\":null}");

		jsonResult = jsonResultBuilder.toString();
        
        this.PSel.pgsqlClose();
        
        return jsonResult;
	
	}

	@Override
	public String getSVGResponse () {

		//System.out.println("getSVGResponse");
		String svgObject = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><svg></svg>";
		String svgFile = "";
		
		try {
			svgFile = (String)this.jq.getContent().getQueriesMap("rateUSMap_div").get("Resource");
	    } catch (NullPointerException npe) {
			return svgObject = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><svg></svg>";
	    } catch (Exception e) {
			return svgObject = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><svg></svg>";
		}
		
		try {
			String cpath = webSettings.getServerPath("svg/" + svgFile);
			FileInputStream fis = new FileInputStream(cpath);
			InputStreamReader isr = new InputStreamReader(fis);
			String isrEncoding = isr.getEncoding();
			//System.out.println(isrEncoding);
			StringBuffer buffer = new StringBuffer();
			Reader in = new BufferedReader(isr);
	        int ch;
	        while ((ch = in.read()) > -1) {
	            buffer.append((char)ch);
	        }
	        in.close();
	        svgObject = buffer.toString();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
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

	protected void buildRATE_SQL () {

  		String sql="";
  		String fromDate="";
  		String toDate="";
		
  		this.PSel.setPgSQLStatement(RATE_SQL);

  		try {
			fromDate = (String)this.jq.getContent().getQueriesMap("tabCensusRate").get("FromDate");
			toDate = (String)this.jq.getContent().getQueriesMap("tabCensusRate").get("ToDate");
	    } catch (NullPointerException npe) {
	    	fromDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis());
	    	toDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis());
	    } catch (Exception e) {
	    	fromDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis());
	    	toDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis());
		}
		
	    this.PSel.setParamString(1, fromDate);
	    this.PSel.setParamString(2, toDate);

	}

}
