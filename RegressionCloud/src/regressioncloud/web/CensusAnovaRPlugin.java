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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import com.regressioncloud.util.WebAppSettings;
import com.regressioncloud.pgsql.PGSQLService;
import com.regressioncloud.rserve.RANOVA_lm;
import com.regressioncloud.rserve.RService;
import com.regressioncloud.rserve.RSummary_lm;

public class CensusAnovaRPlugin extends AjaxModelFactory{

	private WebAppSettings webSettings = WebAppSettings.getObject();
	private String datePattern = "yyyy-mm-dd hh:mm:ss:SS";
	private String yearPattern = "yyyy";
	private SimpleDateFormat timeStamp = new SimpleDateFormat(datePattern);
	private SimpleDateFormat yearOnly = new SimpleDateFormat(yearPattern);
	private PGSQLService PSel;
	private Map<String, Object> regressionMap = new LinkedHashMap<String, Object>();
	private Map<String, Object> labelsMap = new HashMap<String, Object>();
	private Map<String, Object> anovaMap = null;
	private List<String> anovaCmd = null;
	private Map<String, Object> summaryMap = null;
	private List<String> summaryCmd = null;

	private static String REGRESSION_DATA_SQL = "SELECT * FROM census_regr_data(?,?,?,?,?,?,?);";
	private static String COLUMN_LABELS = "SELECT * FROM column_labels(?);";
	
	public CensusAnovaRPlugin() {
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
		getRegressionMap();
		getLabelsMap();
		getANOVAMap();
		getSummaryMap();
		String jsonResult = "{\"stats\":{\"anova\":,\"regrdata\":}}";
		StringBuilder jsonSerializer = new StringBuilder();
		jsonSerializer.append(jsonResult);
		jsonSerializer.insert(jsonSerializer.indexOf("\"anova\":")+8, jsonANOVA());
		jsonSerializer.insert(jsonSerializer.indexOf("\"regrdata\":")+11, jsonRegrData());
		jsonResult = jsonSerializer.toString();
		
		//System.out.println(">>" + jsonResult + "<<");

		return jsonResult;
	
	}
	
	private void getRegressionMap() {

		ResultSet queryResult = null;
		try {
			openDataService();
			buildREGRESSION_DATA_SQL();
			queryResult = getDataFromService();
			if (!(queryResult instanceof ResultSet))
			{
				this.regressionMap.put("", ""); 
			}
	    } catch (NullPointerException npe) {
	    	this.regressionMap.put("", ""); 
		} catch (Exception e) {
			this.regressionMap.put("", ""); 
		}

		try {
			while (queryResult.next()) {
				Map<String, String> rowMap = new HashMap<String, String>();
				rowMap.put("yname", queryResult.getString(1));
				rowMap.put("xname", queryResult.getString(2));
				rowMap.put("sumlev", queryResult.getString(3));
				rowMap.put("yestyear", queryResult.getString(4));
				rowMap.put("xestyear", queryResult.getString(5));
				rowMap.put("state", queryResult.getString(6));
				rowMap.put("stname", queryResult.getString(7));
				rowMap.put("y", queryResult.getString(8));
				rowMap.put("x", queryResult.getString(9));

				this.regressionMap.put(queryResult.getString(1)+
						queryResult.getString(2) +
						queryResult.getString(3) +
						queryResult.getString(4) +
						queryResult.getString(5) +
						queryResult.getString(6) 	
						, rowMap);
			}
			queryResult.close();
		} catch (SQLException se) {
			System.out.println("census_regr_data() failed SQLException " +  se.getMessage());
			se.printStackTrace();
		} catch (NullPointerException npe) {
			System.out.println("census_regr_data()  failed NullPointerException " +  npe.getMessage());
			npe.printStackTrace();
		} catch (Exception e) {
			System.out.println("census_regr_data()  failed Exception " +  e.getMessage() + e.getLocalizedMessage()  );
			e.printStackTrace();
		}
		closeDataService();
	}
	
	private void getLabelsMap() {

		ResultSet queryResult = null;
		try {
			openDataService();
			buildCOLUMN_LABELS();
			queryResult = getDataFromService();
			if (!(queryResult instanceof ResultSet))
			{
				this.labelsMap.put("", ""); 
			}
	    } catch (NullPointerException npe) {
	    	this.labelsMap.put("", ""); 
		} catch (Exception e) {
			this.labelsMap.put("", ""); 
		}

		try {
			while (queryResult.next()) {
				this.labelsMap.put(queryResult.getString(1),queryResult.getString(2));
			}
			queryResult.close();
		} catch (SQLException se) {
			System.out.println("column_labels() failed SQLException " +  se.getMessage());
			se.printStackTrace();
		} catch (NullPointerException npe) {
			System.out.println("column_labels()  failed NullPointerException " +  npe.getMessage());
			npe.printStackTrace();
		} catch (Exception e) {
			System.out.println("column_labels()  failed Exception " +  e.getMessage() + e.getLocalizedMessage()  );
			e.printStackTrace();
		}
		closeDataService();
	}
	
	private String getVector(String vName, String v) {
		StringBuilder vector = new StringBuilder();
		vector.append(vName + " <- c(");
		Iterator<Entry<String, Object>> regrMapIter = regressionMap.entrySet().iterator();
		while (regrMapIter.hasNext()) {
			Map.Entry<String, Object> regrEntry = (Map.Entry<String, Object>) regrMapIter.next();
			Map<String, Object> rowMap = (Map<String, Object>) regrEntry.getValue();
			vector.append(rowMap.get(v).toString()+",");			
		}
		vector.deleteCharAt(vector.length()-1);
		vector.append(")");
		return vector.toString();
	}
	
	private void getANOVAMap(){
		String yVariable="";
		String xVariable=""; 
		try {
			yVariable= (String)this.jq.getContent().getQueriesMap("tabCensusAnovaR").get("yVar");
			xVariable= (String)this.jq.getContent().getQueriesMap("tabCensusAnovaR").get("xVar");
	    } catch (NullPointerException npe) {
		}

		String yvector = getVector(yVariable, "y");
		String xvector = getVector(xVariable, "x");

		RService rs = new RService();
		RANOVA_lm ra = (RANOVA_lm) rs.getRService("RANOVA_lm");
		ra.setyName(yVariable);
		ra.setxName(xVariable);
		ra.setYvector(yvector);
		ra.setXvector(xvector);
		ra.callService();
		this.anovaMap = ra.getResult();
		this.anovaCmd = ra.getRcmd();
	}
	
	private void getSummaryMap(){
		String yVariable="";
		String xVariable=""; 
		try {
			yVariable= (String)this.jq.getContent().getQueriesMap("tabCensusAnovaR").get("yVar");
			xVariable= (String)this.jq.getContent().getQueriesMap("tabCensusAnovaR").get("xVar");
	    } catch (NullPointerException npe) {
		}

		String yvector = getVector(yVariable, "y");
		String xvector = getVector(xVariable, "x");

		RService rs = new RService();
		RSummary_lm ra = (RSummary_lm) rs.getRService("RSummary_lm");
		ra.setyName(yVariable);
		ra.setxName(xVariable);
		ra.setYvector(yvector);
		ra.setXvector(xvector);
		ra.callService();
		this.summaryMap = ra.getResult();
		this.summaryCmd = ra.getRcmd();
	}
	
	private String jsonANOVA() {
  		String yFromDate="";
  		String yToDate="";
  		String xFromDate="";
  		String xToDate="";
		String yVariable="";
		String xVariable="";
		try {
			yFromDate = (String)this.jq.getContent().getQueriesMap("tabCensusAnovaR").get("yFromDate");
			yToDate = (String)this.jq.getContent().getQueriesMap("tabCensusAnovaR").get("yToDate");
			xFromDate = (String)this.jq.getContent().getQueriesMap("tabCensusAnovaR").get("xFromDate");
			xToDate = (String)this.jq.getContent().getQueriesMap("tabCensusAnovaR").get("xToDate");
			yVariable= (String)this.jq.getContent().getQueriesMap("tabCensusAnovaR").get("yVar");
			xVariable= (String)this.jq.getContent().getQueriesMap("tabCensusAnovaR").get("xVar");
	    } catch (NullPointerException npe) {
		}

		StringBuilder jsonResultBuilder = new StringBuilder();
        
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


		jsonResultBuilder.append("{\"c\":[");
		jsonResultBuilder.append("{\"v\":\"" + yVariable + "\"}"); //y
		jsonResultBuilder.append(",{\"v\":\"" + xVariable + "\"}"); //x
		jsonResultBuilder.append(",{\"v\":\"" + this.labelsMap.get(yVariable) + "\"}"); //y-label
		jsonResultBuilder.append(",{\"v\":\"" + this.labelsMap.get(xVariable) + "\"}"); //x-label
		jsonResultBuilder.append(",{\"v\":\"" + "040" + "\"}"); //sumlev
		jsonResultBuilder.append(",{\"v\":\"" + yFromDate + "\"}"); //yestyear
		jsonResultBuilder.append(",{\"v\":\"" + xFromDate + "\"}"); //xestyear
		jsonResultBuilder.append(",{\"v\":" + this.regressionMap.size() + "}"); //n
		jsonResultBuilder.append(",{\"v\":" + this.anovaMap.get("Df1") + "}"); //df
		jsonResultBuilder.append(",{\"v\":" + this.anovaMap.get("Df0") + "}"); //dfr
		jsonResultBuilder.append(",{\"v\":" + this.anovaMap.get("Df1") + "}"); //dfe
		jsonResultBuilder.append(",{\"v\":" + this.anovaMap.get("Sum Sq0") + "}"); //rss
		jsonResultBuilder.append(",{\"v\":" + this.anovaMap.get("Sum Sq1") + "}"); //sse
		jsonResultBuilder.append(",{\"v\":" + "0" + "}"); //tss
		jsonResultBuilder.append(",{\"v\":" + this.anovaMap.get("Mean Sq0") + "}"); //mrss
		jsonResultBuilder.append(",{\"v\":" + this.anovaMap.get("Mean Sq1") + "}"); //msse
		jsonResultBuilder.append(",{\"v\":" + this.anovaMap.get("F value0") + "}"); //f
		jsonResultBuilder.append(",{\"v\":" + this.anovaMap.get("Pr(>F)0") + "}"); //p
		jsonResultBuilder.append(",{\"v\":" + "0" + "}"); //sgnf
		jsonResultBuilder.append(",{\"v\":" + this.summaryMap.get("r.squared0") + "}"); //rsq
		jsonResultBuilder.append(",{\"v\":" + this.summaryMap.get("adj.r.squared0") + "}"); //arsq
		jsonResultBuilder.append(",{\"v\":" + this.summaryMap.get("sigma0") + "}"); //see
		jsonResultBuilder.append(",{\"v\":" + this.summaryMap.get("coefficients0") + "}"); //a
		jsonResultBuilder.append(",{\"v\":" + this.summaryMap.get("coefficients1") + "}"); //b
		jsonResultBuilder.append(",{\"v\":" + this.summaryMap.get("coefficients2") + "}"); //ase
		jsonResultBuilder.append(",{\"v\":" + this.summaryMap.get("coefficients3") + "}"); //bse
		jsonResultBuilder.append(",{\"v\":" + this.summaryMap.get("coefficients4") + "}"); //atv
		jsonResultBuilder.append(",{\"v\":" + this.summaryMap.get("coefficients5") + "}"); //btv
		jsonResultBuilder.append(",{\"v\":" + this.summaryMap.get("coefficients6") + "}"); //ap
		jsonResultBuilder.append(",{\"v\":" + this.summaryMap.get("coefficients7") + "}"); //bv
		jsonResultBuilder.append("]}");
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
			yFromDate = (String)this.jq.getContent().getQueriesMap("tabCensusAnovaR").get("yFromDate");
			yToDate = (String)this.jq.getContent().getQueriesMap("tabCensusAnovaR").get("yToDate");
			xFromDate = (String)this.jq.getContent().getQueriesMap("tabCensusAnovaR").get("xFromDate");
			xToDate = (String)this.jq.getContent().getQueriesMap("tabCensusAnovaR").get("xToDate");
			yVariable= (String)this.jq.getContent().getQueriesMap("tabCensusAnovaR").get("yVar");
			xVariable= (String)this.jq.getContent().getQueriesMap("tabCensusAnovaR").get("xVar");
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

	protected void buildCOLUMN_LABELS() {
  		
  		this.PSel.setPgSQLStatement(COLUMN_LABELS);
	    this.PSel.setParamString(1, "census_pop_est_alldata");
	    
	}


}
