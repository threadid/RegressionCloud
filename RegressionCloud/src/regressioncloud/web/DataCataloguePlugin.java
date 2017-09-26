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

public class DataCataloguePlugin extends AjaxModelFactory{

	private WebAppSettings webSettings;
	private String datePattern = "yyyy-mm-dd hh:mm:ss:SS";
	private String yearPattern = "yyyy";
	private SimpleDateFormat timeStamp = new SimpleDateFormat(datePattern);
	private SimpleDateFormat yearOnly = new SimpleDateFormat(yearPattern);
	private PGSQLService PSel;

	private static String HTML_DEFAULT = "";
	private static String JSON_DEFAULT = "{\"catalogue\":{\"series\":,\"source\":,\"sourcenote\":,\"sourceuri\":,\"provider\":,\"serieskey\":,\"seriesvalue\":,\"seriesnote\":,\"seriessource\":}}";
	private static String RC_DATA_SERIES_CATALOGUE_SQL = "SELECT * FROM rc_data_series_catalogue;";
	private static String RC_DATA_SOURCE_CATALOGUE_SQL = "SELECT * FROM rc_data_source_catalogue;";
	private static String RC_DATA_SOURCE_NOTE_SQL = "SELECT * FROM rc_data_source_note;";
	private static String RC_DATA_SOURCE_URI_REFERENCE_SQL = "SELECT * FROM rc_data_source_uri_reference;";
	private static String RC_DATA_PROVIDER_CATALOGUE_SQL = "SELECT * FROM rc_data_provider_catalogue;";
	private static String RC_DATA_SERIES_KEY_SQL = "SELECT * FROM rc_data_series_key;";
	private static String RC_DATA_SERIES_VALUE_SQL = "SELECT * FROM rc_data_series_value;";
	private static String RC_DATA_SERIES_NOTE_SQL = "SELECT * FROM rc_data_series_note;";
	private static String RC_SERIES_SOURCE_SQL = "SELECT * FROM rc_series_source;";
	
	public DataCataloguePlugin() {
		this.webSettings = WebAppSettings.getObject();
	}
	
	@Override
	public String getHTMLResponse () {
		String htmlResult = HTML_DEFAULT;
		StringBuilder htmlSerializer = new StringBuilder();
		htmlSerializer.append(htmlResult);
		htmlSerializer.append(dataCatalogueList());
		htmlResult = htmlSerializer.toString();
//		System.out.println(">>" + htmlResult + "<<");
		return htmlResult;
	}

	private String dataCatalogueList() {
		StringBuilder htmlResultBuilder = new StringBuilder();
		ResultSet queryResult = null;

		try {
			openDataService();
			this.PSel.setPgSQLStatement(RC_DATA_SERIES_CATALOGUE_SQL);
			queryResult = getDataFromService();
			if (!(queryResult instanceof ResultSet))
			{
				return HTML_DEFAULT; 
			}
	    } catch (NullPointerException npe) {
			return HTML_DEFAULT; 
		} catch (Exception e) {
			return HTML_DEFAULT; 
		}

		try {
			while (queryResult.next()) {
				htmlResultBuilder.append("<div><table>");
				htmlResultBuilder.append("<tr><td><a href=# onclick=\"showKeys('"+queryResult.getString(1).trim()+"', '"+queryResult.getString(3).trim()+"' );return false;\" title=\"" + queryResult.getString(3).trim() + "\">");
				htmlResultBuilder.append(queryResult.getString(2).trim() + "</a>");
				htmlResultBuilder.append("</td></tr>");
			}
		} catch (SQLException sqle) {
			System.out.println("SQLException>" + sqle + "<");
			return JSON_DEFAULT; 
		}
				
		//System.out.println("After while next  = " + timeStamp.format(Calendar.getInstance(Locale.US).getTimeInMillis()));

		htmlResultBuilder.append("</table></div>");

		closeDataService();
        
		return htmlResultBuilder.toString();
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
		jsonSerializer.insert(jsonSerializer.indexOf("\"series\":")+9, jsonSeries());
		jsonSerializer.insert(jsonSerializer.indexOf("\"serieskey\":")+12, jsonSeriesKey());
		jsonSerializer.insert(jsonSerializer.indexOf("\"seriesvalue\":")+14, jsonSeriesValue());
		jsonSerializer.insert(jsonSerializer.indexOf("\"seriesnote\":")+13, jsonSeriesNote());
		jsonSerializer.insert(jsonSerializer.indexOf("\"source\":")+9, jsonSource());
		jsonSerializer.insert(jsonSerializer.indexOf("\"sourcenote\":")+13, jsonSourceNote());
		jsonSerializer.insert(jsonSerializer.indexOf("\"sourceuri\":")+12, jsonSourceURI());
		jsonSerializer.insert(jsonSerializer.indexOf("\"provider\":")+11, jsonProvider());
		jsonSerializer.insert(jsonSerializer.indexOf("\"seriessource\":")+15, jsonSeriesSource());
		jsonResult = jsonSerializer.toString();

//		System.out.println("jsonResult>>" + jsonResult + "<<");

		return jsonResult;
	
	}

	private String jsonSeries() {
		StringBuilder jsonResultBuilder = new StringBuilder();
		ResultSet queryResult = null;
		try {
			openDataService();
			this.PSel.setPgSQLStatement(RC_DATA_SERIES_CATALOGUE_SQL);
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
		jsonResultBuilder.append("{\"id\":\"series_id\",\"label\":\"Series ID\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"series_name\",\"label\":\"Series Name\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"series_description\",\"label\":\"Series Description\",\"type\":\"string\"}]");
		jsonResultBuilder.append(",\"rows\":[");
		
		//System.out.println("Before while next  = " + timeStamp.format(Calendar.getInstance(Locale.US).getTimeInMillis()));
		try {
			while (queryResult.next()) {
				jsonResultBuilder.append("{\"c\":[");
				jsonResultBuilder.append("{\"v\":\"" + queryResult.getString(1).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(2).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(3).trim() + "\"}");
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

	private String jsonSeriesKey() {
		StringBuilder jsonResultBuilder = new StringBuilder();
		ResultSet queryResult = null;
		try {
			openDataService();
			this.PSel.setPgSQLStatement(RC_DATA_SERIES_KEY_SQL);
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
		jsonResultBuilder.append("{\"id\":\"series_id\",\"label\":\"Series ID\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"series_key_seq\",\"label\":\"Key Sequence\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"series_key_name\",\"label\":\"Key\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"series_key_description\",\"label\":\"Key Description\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"series_key_type\",\"label\":\"Key Type\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"series_key_context\",\"label\":\"Key Context\",\"type\":\"string\"}]");
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

	private String jsonSeriesValue() {
		StringBuilder jsonResultBuilder = new StringBuilder();
		ResultSet queryResult = null;
		try {
			openDataService();
			this.PSel.setPgSQLStatement(RC_DATA_SERIES_VALUE_SQL);
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
		jsonResultBuilder.append("{\"id\":\"series_id\",\"label\":\"Series ID\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"series_value_seq\",\"label\":\"Value Sequence\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"series_value_name\",\"label\":\"Value Name\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"series_value_description\",\"label\":\"Value Description\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"series_value_type\",\"label\":\"Value Type\",\"type\":\"string\"}]");
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

	private String jsonSeriesNote() {
		StringBuilder jsonResultBuilder = new StringBuilder();
		ResultSet queryResult = null;
		try {
			openDataService();
			this.PSel.setPgSQLStatement(RC_DATA_SERIES_NOTE_SQL);
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
		jsonResultBuilder.append("{\"id\":\"series_id\",\"label\":\"Series ID\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"series_note_seq\",\"label\":\"Note Sequence\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"series_note\",\"label\":\"Series Note\",\"type\":\"string\"}]");
		jsonResultBuilder.append(",\"rows\":[");
		
		//System.out.println("Before while next  = " + timeStamp.format(Calendar.getInstance(Locale.US).getTimeInMillis()));
		try {
			while (queryResult.next()) {
				jsonResultBuilder.append("{\"c\":[");
				jsonResultBuilder.append("{\"v\":\"" + queryResult.getString(1).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(2).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(3).trim() + "\"}");
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

	private String jsonSource() {
		StringBuilder jsonResultBuilder = new StringBuilder();
		ResultSet queryResult = null;
		try {
			openDataService();
			this.PSel.setPgSQLStatement(RC_DATA_SOURCE_CATALOGUE_SQL);
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
		jsonResultBuilder.append("{\"id\":\"source_id\",\"label\":\"Source ID\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"provider_id\",\"label\":\"Provider ID\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"source_name\",\"label\":\"Source\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"source_description\",\"label\":\"Source Description\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"source_citation\",\"label\":\"Source Citation\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"reference_page_uri\",\"label\":\"Source URL\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"reference_layout_uri\",\"label\":\"Source File Information\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"reference_method_uri\",\"label\":\"Source Method\",\"type\":\"string\"}]");
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
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(8).trim() + "\"}");
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

	private String jsonSourceNote() {
		StringBuilder jsonResultBuilder = new StringBuilder();
		ResultSet queryResult = null;
		try {
			openDataService();
			this.PSel.setPgSQLStatement(RC_DATA_SOURCE_NOTE_SQL);
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
		jsonResultBuilder.append("{\"id\":\"source_id\",\"label\":\"Source ID\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"source_note_seq\",\"label\":\"Note Sequence\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"source_note\",\"label\":\"Source Note\",\"type\":\"string\"}]");
		jsonResultBuilder.append(",\"rows\":[");
		
		//System.out.println("Before while next  = " + timeStamp.format(Calendar.getInstance(Locale.US).getTimeInMillis()));
		try {
			jsonResultBuilder.append("{\"c\":[{\"v\":\"\"}]},");
			
			while (queryResult.next()) {
				jsonResultBuilder.append("{\"c\":[");
				jsonResultBuilder.append("{\"v\":\"" + queryResult.getString(1).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(2).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(3).trim() + "\"}");
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

	private String jsonProvider() {
		StringBuilder jsonResultBuilder = new StringBuilder();
		ResultSet queryResult = null;
		try {
			openDataService();
			this.PSel.setPgSQLStatement(RC_DATA_PROVIDER_CATALOGUE_SQL);
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
		jsonResultBuilder.append("{\"id\":\"provider_id\",\"label\":\"Provider ID\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"provider_name\",\"label\":\"Provider\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"provider_domain\",\"label\":\"Domain\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"reference_provider_uri\",\"label\":\"Provider URL\",\"type\":\"string\"}]");
		jsonResultBuilder.append(",\"rows\":[");
		
		//System.out.println("Before while next  = " + timeStamp.format(Calendar.getInstance(Locale.US).getTimeInMillis()));
		try {
			while (queryResult.next()) {
				jsonResultBuilder.append("{\"c\":[");
				jsonResultBuilder.append("{\"v\":\"" + queryResult.getString(1).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(2).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(3).trim() + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(4).trim() + "\"}");
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
	private String jsonSourceURI() {
		StringBuilder jsonResultBuilder = new StringBuilder();
		ResultSet queryResult = null;
		try {
			openDataService();
			this.PSel.setPgSQLStatement(RC_DATA_SOURCE_URI_REFERENCE_SQL);
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
		jsonResultBuilder.append("{\"id\":\"source_id\",\"label\":\"Source ID\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"reference_file_uri\",\"label\":\"Source File URL\",\"type\":\"string\"}]");
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

	private String jsonSeriesSource() {
		StringBuilder jsonResultBuilder = new StringBuilder();
		ResultSet queryResult = null;
		try {
			openDataService();
			this.PSel.setPgSQLStatement(RC_SERIES_SOURCE_SQL);
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
		jsonResultBuilder.append("{\"id\":\"series_id\",\"label\":\"Series ID\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"source_id\",\"label\":\"Source ID\",\"type\":\"string\"}]");
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
