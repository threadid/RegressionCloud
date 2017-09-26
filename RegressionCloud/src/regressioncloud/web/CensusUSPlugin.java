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

public class CensusUSPlugin extends AjaxModelFactory{

	private WebAppSettings webSettings = WebAppSettings.getObject();
	private String datePattern = "yyyy-mm-dd hh:mm:ss:SS";
	private String yearPattern = "yyyy";
	private SimpleDateFormat timeStamp = new SimpleDateFormat(datePattern);
	private SimpleDateFormat yearOnly = new SimpleDateFormat(yearPattern);
	private PGSQLService PSel;
	
	private static String POPULATION_SQL = "SELECT * FROM census_population(?,?);";

	public CensusUSPlugin() {
	}
	
	@Override
	public String getHTMLResponse () {
		String htmlObject = "<p></p>";
		String htmlFile = "";
		
		try {
			htmlFile = (String)this.jq.getContent().getQueriesMap("tabCensusPop").get("Resource");
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
			buildPOPULATION_SQL();
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
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(10).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(11).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(12).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(13).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(14).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(15).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(16).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(17).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(18).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(19).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(20).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(21).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(22).trim() + "}");
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
		
		//System.out.println(">>" + jsonResult + "<<");

		closeDataService();
		 
        return jsonResult;
	
	}

	@Override
	public String getSVGResponse () {

		String svgObject = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><svg></svg>";
		String svgFile = "";
		
		try {
			svgFile = (String)this.jq.getContent().getQueriesMap("popUSMap_div").get("Resource");
	    } catch (NullPointerException npe) {
			return svgObject = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><svg></svg>";
	    } catch (Exception e) {
			return svgObject = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><svg></svg>";
		}
		//System.out.println("svgFile==null" + (svgFile==null));
		//System.out.println("svgFile" + svgFile);
		

		try {
			String cpath = webSettings.getServerPath("svg/" + svgFile);
			//System.out.println("cpath==null" + cpath==null);
			//System.out.println("cpath" + cpath);
			FileInputStream fis = new FileInputStream(cpath);
			InputStreamReader isr = new InputStreamReader(fis);
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

	private void buildPOPULATION_SQL () {

  		String fromDate="";
  		String toDate="";

  		//System.out.println(yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis()));
  		
  		this.PSel.setPgSQLStatement(POPULATION_SQL);
		
		try {
			fromDate = (String)this.jq.getContent().getQueriesMap("tabCensusPop").get("FromDate");
			toDate = (String)this.jq.getContent().getQueriesMap("tabCensusPop").get("ToDate");
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
