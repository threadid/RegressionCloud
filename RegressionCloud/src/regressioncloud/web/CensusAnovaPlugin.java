package com.regressioncloud.web;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.regressioncloud.util.WebAppSettings;
import com.regressioncloud.pgsql.PGSQLService;

public class CensusAnovaPlugin extends AjaxModelFactory{

	private WebAppSettings webSettings = WebAppSettings.getObject();
	private String datePattern = "yyyy-mm-dd hh:mm:ss:SS";
	private String yearPattern = "yyyy";
	private SimpleDateFormat timeStamp = new SimpleDateFormat(datePattern);
	private SimpleDateFormat yearOnly = new SimpleDateFormat(yearPattern);
	private PGSQLService PSel;

	private static String ANOVA_SQL = "SELECT * FROM anova(?,?,?,?,?,?,?);";
	private static String REGRESSION_DATA_SQL = "SELECT * FROM census_regr_data(?,?,?,?,?,?,?);";
	
	public CensusAnovaPlugin() {
	}
	
	@Override
	public String getHTMLResponse () {
		String htmlObject = "<p></p>";
		String htmlFile = "";
		
		try {
			htmlFile = (String)this.jq.getContent().getQueriesMap("tabCensusAnova").get("Resource");
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
		String jsonResult = "{\"stats\":{\"anova\":,\"regrdata\":}}";
		StringBuilder jsonSerializer = new StringBuilder();
		jsonSerializer.append(jsonResult);
		jsonSerializer.insert(jsonSerializer.indexOf("\"anova\":")+8, jsonANOVA());
		jsonSerializer.insert(jsonSerializer.indexOf("\"regrdata\":")+11, jsonRegrData());
		jsonResult = jsonSerializer.toString();
		
		//System.out.println(">>" + jsonResult + "<<");

		return jsonResult;
	
	}

	private String jsonANOVA() {
		StringBuilder jsonResultBuilder = new StringBuilder();
		ResultSet queryResult = null;
		try {
			openDataService();
			buildANOVA_SQL();
			queryResult = getDataFromService();
			if (!(queryResult instanceof ResultSet))
			{
				return "{\"stats\":{\"anova\":{},\"regrdata\":{}}"; 
			}
	    } catch (NullPointerException npe) {
			return "{\"stats\":{\"anova\":{},\"regrdata\":{}}"; 
		} catch (Exception e) {
			return "{\"stats\":{\"anova\":{},\"regrdata\":{}}"; 
		}
		
		jsonResultBuilder.append("{\"cols\":[");
		jsonResultBuilder.append("{\"id\":\"y\",\"label\":\"Dependent Variable\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"x\",\"label\":\"Independent Variable\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"y-label\",\"label\":\"Dependent Variable\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"x-label\",\"label\":\"Independent Variable\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"sumlev\",\"label\":\"Summary Level\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"yestyear\",\"label\":\"Dependent Variable Year\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"xestyear\",\"label\":\"Independent Variable Year\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"n\",\"label\":\"Observations\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"df\",\"label\":\"Degrees of Freedom\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"dfr\",\"label\":\"Degrees of Freedom Regression\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"dfe\",\"label\":\"Degrees of Freedom Residual\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"rss\",\"label\":\"Regression Sum of Squares\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"sse\",\"label\":\"Sum of Squares Error\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"tss\",\"label\":\"Total Sum of Squares\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"mrss\",\"label\":\"Mean Sum of Squares Regression\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"msse\",\"label\":\"Mean Sum of Squares Error\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"f\",\"label\":\"F-Statistic\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"p\",\"label\":\"p-Value\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"sgnf\",\"label\":\"Significance\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"rsq\",\"label\":\"R-Squared\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"arsq\",\"label\":\"Adjusted R2\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"see\",\"label\":\"Standard Error Estimate\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"a\",\"label\":\"Intercept\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"b\",\"label\":\"Slope\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"ase\",\"label\":\"Standard Error\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"bse\",\"label\":\"Standard Error\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"atv\",\"label\":\"T-Value\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"btv\",\"label\":\"T-Value\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"ap\",\"label\":\"P-Value\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"bp\",\"label\":\"P-Value\",\"type\":\"number\"}]");
		jsonResultBuilder.append(",\"rows\":[");
		
		//System.out.println("Before while next  = " + timeStamp.format(Calendar.getInstance(Locale.US).getTimeInMillis()));
		try {
			while (queryResult.next()) {
				jsonResultBuilder.append("{\"c\":[");
				jsonResultBuilder.append("{\"v\":\"" + queryResult.getString(1).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(2).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(3).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(4).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(5).trim() + "\"}");
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
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(23).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(24).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(25).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(26).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(27).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(28).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(29).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(30).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(31).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(32).trim() + "}");
				jsonResultBuilder.append("]},");
			}
			jsonResultBuilder.deleteCharAt(jsonResultBuilder.length()-1);
		} catch (SQLException sqle) {
			System.out.println("SQLException>" + sqle + "<");
			return "{\"stats\":{\"anova\":{},\"regrdata\":{}}"; 
		}
				
		//System.out.println("After while next  = " + timeStamp.format(Calendar.getInstance(Locale.US).getTimeInMillis()));

		closeDataService();
        
		jsonResultBuilder.append("],\"p\":null}");
		
		return jsonResultBuilder.toString();
		
	}
	
	private String jsonRegrData() {
		StringBuilder jsonResultBuilder = new StringBuilder();
		ResultSet queryResult = null;
		try {
			openDataService();
			buildREGRESSION_DATA_SQL();
			queryResult = getDataFromService();
			if (!(queryResult instanceof ResultSet))
			{
				return "{\"stats\":{\"anova\":{},\"regrdata\":{}}"; 
			}
	    } catch (NullPointerException npe) {
			return "{\"stats\":{\"anova\":{},\"regrdata\":{}}"; 
		} catch (Exception e) {
			return "{\"stats\":{\"anova\":{},\"regrdata\":{}}"; 
		}
		
		jsonResultBuilder.append("{\"cols\":[");
		jsonResultBuilder.append("{\"id\":\"yname\",\"label\":\"Dependent Variable\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"xname\",\"label\":\"Independent Variable\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"sumlev\",\"label\":\"Summary Level\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"yestyear\",\"label\":\"Dependent Variable Year\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"xestyear\",\"label\":\"Independent Variable Year\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"state\",\"label\":\"State\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"stname\",\"label\":\"State\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"y\",\"label\":\"y Value\",\"type\":\"number\"},");
		jsonResultBuilder.append("{\"id\":\"x\",\"label\":\"x Value\",\"type\":\"number\"}]");
		jsonResultBuilder.append(",\"rows\":[");
		
		//System.out.println("Before while next  = " + timeStamp.format(Calendar.getInstance(Locale.US).getTimeInMillis()));
		try {
			while (queryResult.next()) {
				jsonResultBuilder.append("{\"c\":[");
				jsonResultBuilder.append("{\"v\":\"" + queryResult.getString(1).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(2).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(3).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(4).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(5).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(6).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(7).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(8).trim() + "}");
				jsonResultBuilder.append(",{\"v\":" + queryResult.getString(9).trim() + "}");
				jsonResultBuilder.append("]},");
			}
			jsonResultBuilder.deleteCharAt(jsonResultBuilder.length()-1);
		} catch (SQLException sqle) {
			System.out.println("SQLException>" + sqle + "<");
			return "{\"stats\":{\"anova\":{},\"regrdata\":{}}"; 
		}
				
		//System.out.println("After while next  = " + timeStamp.format(Calendar.getInstance(Locale.US).getTimeInMillis()));

		closeDataService();
        
		jsonResultBuilder.append("],\"p\":null}");
	
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

	protected void buildANOVA_SQL() {

  		String yFromDate="";
  		String yToDate="";
  		String xFromDate="";
  		String xToDate="";
  		String yVariable="";
  		String xVariable="";
  			
  		//System.out.println(yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis()));
  		
		this.PSel.setPgSQLStatement(ANOVA_SQL);

		try {
			yFromDate = (String)this.jq.getContent().getQueriesMap("tabCensusAnova").get("yFromDate");
			yToDate = (String)this.jq.getContent().getQueriesMap("tabCensusAnova").get("yToDate");
			xFromDate = (String)this.jq.getContent().getQueriesMap("tabCensusAnova").get("xFromDate");
			xToDate = (String)this.jq.getContent().getQueriesMap("tabCensusAnova").get("xToDate");
			yVariable= (String)this.jq.getContent().getQueriesMap("tabCensusAnova").get("yVar");
			xVariable= (String)this.jq.getContent().getQueriesMap("tabCensusAnova").get("xVar");
	    } catch (NullPointerException npe) {
	    	yFromDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis()).toString();
	    	yToDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis()).toString();
	    	xFromDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis()).toString();
	    	xToDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis()).toString();
	    } catch (Exception e) {
	    	yFromDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis()).toString();
	    	yToDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis()).toString();
	    	xFromDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis()).toString();
	    	xToDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis()).toString();
		}
		
	    this.PSel.setParamString(1, yVariable);
	    this.PSel.setParamString(2, xVariable);
	    this.PSel.setParamString(3, "040");
	    this.PSel.setParamString(4, yFromDate);
	    this.PSel.setParamString(5, yToDate);
	    this.PSel.setParamString(6, xFromDate);
	    this.PSel.setParamString(7, xToDate);
	    
	}

	protected void buildREGRESSION_DATA_SQL() {

  		String yFromDate="";
  		String yToDate="";
  		String xFromDate="";
  		String xToDate="";
  		String yVariable="";
  		String xVariable="";

  		//System.out.println(yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis()));
  		
  		this.PSel.setPgSQLStatement(REGRESSION_DATA_SQL);

		try {
			yFromDate = (String)this.jq.getContent().getQueriesMap("tabCensusAnova").get("yFromDate");
			yToDate = (String)this.jq.getContent().getQueriesMap("tabCensusAnova").get("yToDate");
			xFromDate = (String)this.jq.getContent().getQueriesMap("tabCensusAnova").get("xFromDate");
			xToDate = (String)this.jq.getContent().getQueriesMap("tabCensusAnova").get("xToDate");
			yVariable= (String)this.jq.getContent().getQueriesMap("tabCensusAnova").get("yVar");
			xVariable= (String)this.jq.getContent().getQueriesMap("tabCensusAnova").get("xVar");
	    } catch (NullPointerException npe) {
	    	yFromDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis()).toString();
	    	yToDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis()).toString();
	    	xFromDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis()).toString();
	    	xToDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis()).toString();
	    } catch (Exception e) {
	    	yFromDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis()).toString();
	    	yToDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis()).toString();
	    	xFromDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis()).toString();
	    	xToDate = yearOnly.format(Calendar.getInstance(Locale.US).getTimeInMillis()).toString();
		}
		
	    this.PSel.setParamString(1, yVariable);
	    this.PSel.setParamString(2, xVariable);
	    this.PSel.setParamString(3, "040");
	    this.PSel.setParamString(4, yFromDate);
	    this.PSel.setParamString(5, yToDate);
	    this.PSel.setParamString(6, xFromDate);
	    this.PSel.setParamString(7, xToDate);
	    
	}

}
