package com.regressioncloud.web;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Locale;

//import org.w3c.dom.NodeList;



import java.util.Map;

import com.regressioncloud.util.WebAppSettings;
import com.regressioncloud.pgsql.PGSQLService;

public class DataSelectPlugin extends AjaxModelFactory{

	private WebAppSettings webSettings;
	private String datePattern = "yyyy-mm-dd hh:mm:ss:SS";
	private String yearPattern = "yyyy";
	private SimpleDateFormat timeStamp = new SimpleDateFormat(datePattern);
	private SimpleDateFormat yearOnly = new SimpleDateFormat(yearPattern);
	private PGSQLService PSel;

	private static String HTML_DEFAULT = "";
	private static String JSON_META_DEFAULT = "{\"meta\":{\"series\":,\"keys\":}}";
	private static String JSON_RANGE_DEFAULT = "{\"range\":{\"series\":,\"cname\":,\"data\":}}";
	private static String GOOGLE_JSON_DEFAULT = "{\"cols\":[{\"id\":\"def\",\"label\":\"Default\",\"type\":\"string\"}],\"rows\":[{\"c\":[{\"v\":\"\"}]}],\"p\":null}";
	private static String RC_DATA_SERIES_META_SQL = "SELECT * FROM series_meta(?, ?);";
	private static String RC_DATA_SERIES_RANGE_SQL = "SELECT * FROM series_range(?, ?);";
	private static String RC_DATA_SERIES_KEYS_SQL = "SELECT series_key_seq "
							+",series_key_name "
							+",series_key_description "
							+",series_key_type "
							+"FROM rc_data_series_key "
							+"WHERE series_id = ? "
							+"GROUP BY series_key_seq "
							+",series_key_name "
							+",series_key_description "
							+",series_key_type "
							+"ORDER BY series_key_seq;";

	public DataSelectPlugin() {
		this.webSettings = WebAppSettings.getObject();
	}
	
	@Override
	public String getHTMLResponse () {
		String htmlResult = HTML_DEFAULT;
		return htmlResult;
	}

	@Override
	public String getXMLResponse () {
		return "<?xml version='1.0' encoding='UTF-8'?><xmlpayload></xmlpayload>";
	}

	@Override
	public String getJSONResponse () {		
		StringBuilder jsonSerializer = new StringBuilder();
		String jsonResult = "";
		String series = "";
		String cname = "";
		String element = getElement();
		switch (element) {
			case "selDataMeta":
				jsonResult = JSON_META_DEFAULT;
				jsonSerializer.append(jsonResult);
				series = getSeries("selDataMeta");
				if (!(series=="")) {
					jsonSerializer.insert(jsonSerializer.indexOf("\"series\":")+9, "\""+series+"\"");
					ListIterator<Object> keyList = getKeys(RC_DATA_SERIES_KEYS_SQL).listIterator();
					jsonSerializer.insert(jsonSerializer.indexOf("\"keys\":")+7, jsonKey(keyList));
				} 
				break;
			case "selDataRange":
				jsonResult = JSON_RANGE_DEFAULT;
				jsonSerializer.append(jsonResult);
				series = getSeries("selDataRange");
				cname = getCname();
				if (!(series=="")) {
					jsonSerializer.insert(jsonSerializer.indexOf("\"series\":")+9, "\""+series+"\"");
					jsonSerializer.insert(jsonSerializer.indexOf("\"cname\":")+8, "\""+cname+"\"");
					jsonSerializer.insert(jsonSerializer.indexOf("\"data\":")+7, jsonRange(series, cname));
				}
				break;
			default:
				jsonResult = JSON_META_DEFAULT;
				break;
		}
		
		jsonResult = jsonSerializer.toString();

//		System.out.println(">>" + jsonResult + "<<");

		return jsonResult;
	
	}
	
	private String getSeries(String elementID) {
		
		String series="";
		
		try {
			series = (String)this.jq.getContent().getQueriesMap(elementID).get("series");
	    } catch (NullPointerException npe) {
			series="";
	    } catch (Exception e) {
			series="";
		}
		
		return series;
	}
	
	private String getCname() {
		
		String cname="";
		
		try {
			cname = (String)this.jq.getContent().getQueriesMap("selDataRange").get("cname");
	    } catch (NullPointerException npe) {
	    	cname="";
	    } catch (Exception e) {
	    	cname="";
		}
		
		return cname;
	}
	
	private String getElement() {
		
		String element="";
		
		try {
			element = (String)this.jq.getContent().getElements().get(0).getElementId();
	    } catch (NullPointerException npe) {
	    	element="";
	    } catch (Exception e) {
	    	element="";
		}
		
		return element;
	}
	
	private ArrayList<Object> getKeys(String sql){
		ArrayList<Object> keyList = new ArrayList<Object>();
		
		ResultSet queryResult = null;
		try {
			openDataService();
			buildKEYS_SQL(sql);
			queryResult = getDataFromService();
			if (!(queryResult instanceof ResultSet))
			{
				return keyList; 
			}
		} catch (NullPointerException npe) {
			return keyList; 
		} catch (Exception e) {
			return keyList; 
		}
		
		try {
			while (queryResult.next()) {
				Map<String, String> keyMap= new HashMap<String, String>();
				keyMap.put("kseq", queryResult.getString(1).trim());
				keyMap.put("cname", queryResult.getString(2).trim());
				keyMap.put("cdesc", queryResult.getString(3).trim());
				keyMap.put("ctype", queryResult.getString(4).trim());
				keyList.add(keyMap);
			}
		} catch (SQLException sqle) {
			System.out.println("SQLException>" + sqle + "<");
			return keyList; 
		}
		
		closeDataService();
		
		return keyList;
	}
	
	private String jsonKey(ListIterator<Object> keyList) {
		String series = getSeries("selDataMeta");
		StringBuilder jsonResultBuilder = new StringBuilder();
		ResultSet queryResult = null;
		try {
			openDataService();
		} catch (NullPointerException npe) {
			return GOOGLE_JSON_DEFAULT; 
		} catch (Exception e) {
			return GOOGLE_JSON_DEFAULT; 
		}
		
		jsonResultBuilder.append("{\"cols\":[");
		jsonResultBuilder.append("{\"id\":\"series\",\"label\":\"Series ID\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"kseq\",\"label\":\"Key Seq\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"cname\",\"label\":\"Column Name\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"cdesc\",\"label\":\"Key Name\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"ctype\",\"label\":\"Data Type\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"min\",\"label\":\"Minimum\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"max\",\"label\":\"Maximum\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"count\",\"label\":\"Unique Count\",\"type\":\"string\"}]");
		jsonResultBuilder.append(",\"rows\":[");

		while (keyList.hasNext()) {
			Map<String, String> keyMap = (Map<String, String>)keyList.next();
		
			try {
				buildMETA_SQL(keyMap.get("cname"));
				queryResult = getDataFromService();
				if (!(queryResult instanceof ResultSet))
				{
					return GOOGLE_JSON_DEFAULT; 
				}
			} catch (NullPointerException npe) {
				return GOOGLE_JSON_DEFAULT; 
			} catch (Exception e) {
				return GOOGLE_JSON_DEFAULT; 
			}
			try {
				while (queryResult.next()) {
					jsonResultBuilder.append("{\"c\":[");
					jsonResultBuilder.append("{\"v\":\"" + series + "\"}");
					jsonResultBuilder.append(",{\"v\":\"" + keyMap.get("kseq") + "\"}");
					jsonResultBuilder.append(",{\"v\":\"" + keyMap.get("cname") + "\"}");
					jsonResultBuilder.append(",{\"v\":\"" + keyMap.get("cdesc") + "\"}");
					jsonResultBuilder.append(",{\"v\":\"" + keyMap.get("ctype") + "\"}");
					jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(1).trim() + "\"}");
					jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(2).trim() + "\"}");
					jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(3).trim() + "\"}");
					jsonResultBuilder.append("]},");
				}
			} catch (SQLException sqle) {
				System.out.println("SQLException>" + sqle + "<");
				return GOOGLE_JSON_DEFAULT; 
			}
		} //while

		jsonResultBuilder.deleteCharAt(jsonResultBuilder.length()-1);
		jsonResultBuilder.append("],\"p\":null}");
		
		closeDataService();
		
		return jsonResultBuilder.toString();
		
	}
	
	private String jsonRange(String series, String cname) {
		
		StringBuilder jsonResultBuilder = new StringBuilder();
		ResultSet queryResult = null;
		try {
			openDataService();
			buildRANGE_SQL();
			queryResult = getDataFromService();
			if (!(queryResult instanceof ResultSet))
			{
				return GOOGLE_JSON_DEFAULT; 
			}
		} catch (NullPointerException npe) {
			return GOOGLE_JSON_DEFAULT; 
		} catch (Exception e) {
			return GOOGLE_JSON_DEFAULT; 
		}
		
		jsonResultBuilder.append("{\"cols\":[");
		jsonResultBuilder.append("{\"id\":\"series\",\"label\":\"Series ID\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"cname\",\"label\":\"Column Name\",\"type\":\"string\"},");
		jsonResultBuilder.append("{\"id\":\"val\",\"label\":\"Unique Values\",\"type\":\"string\"}]");
		jsonResultBuilder.append(",\"rows\":[");
		try {
			while (queryResult.next()) {
				jsonResultBuilder.append("{\"c\":[");
				jsonResultBuilder.append("{\"v\":\"" + series + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + cname + "\"}");
				jsonResultBuilder.append(",{\"v\":\"" + queryResult.getString(1).trim() + "\"}");
				jsonResultBuilder.append("]},");
			}
			jsonResultBuilder.deleteCharAt(jsonResultBuilder.length()-1);
		} catch (SQLException sqle) {
			System.out.println("SQLException>" + sqle + "<");
			return GOOGLE_JSON_DEFAULT; 
		}
		
		jsonResultBuilder.append("],\"p\":null}");
		closeDataService();
		
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
	
	private void buildKEYS_SQL(String sql) {
		
		String series="";
		
		this.PSel.setPgSQLStatement(sql);
		
		try {
			series = (String)this.jq.getContent().getQueriesMap("selDataMeta").get("series");
	    } catch (NullPointerException npe) {
			series="";
	    } catch (Exception e) {
			series="";
		}
		
	    this.PSel.setParamString(1, series);
	}

	private void buildMETA_SQL (String cName) {
		
		String series = "";
		
		this.PSel.setPgSQLStatement(RC_DATA_SERIES_META_SQL);

		try {
			series = (String)this.jq.getContent().getQueriesMap("selDataMeta").get("series");
	    } catch (NullPointerException npe) {
	    	series="";
	    } catch (Exception e) {
	    	series="";
		}
		
	    this.PSel.setParamString(1, series);
	    this.PSel.setParamString(2, cName);

	}
	
	private void buildRANGE_SQL () {
		
		String series = "";
		String cName = "";
		
		this.PSel.setPgSQLStatement(RC_DATA_SERIES_RANGE_SQL);
	
		try {
			series = (String)this.jq.getContent().getQueriesMap("selDataRange").get("series");
			cName = (String)this.jq.getContent().getQueriesMap("selDataRange").get("cname");
	    } catch (NullPointerException npe) {
	    	series="";
	    	cName = "";
	    } catch (Exception e) {
	    	series="";
	    	cName = "";
		}
		
	    this.PSel.setParamString(1, series);
	    this.PSel.setParamString(2, cName);
	    
	}

}
