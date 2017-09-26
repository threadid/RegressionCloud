package com.regressioncloud.web;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import com.regressioncloud.util.WebAppSettings;
import com.regressioncloud.web.JSONQuery.contentElement;
import com.regressioncloud.web.JSONQuery.contentQuery;
import com.regressioncloud.pgsql.PGSQLService;

public class ArticlePlugin extends AjaxModelFactory{

	private WebAppSettings webSettings;
	private String datePattern = "yyyy-mm-dd hh:mm:ss:SS";
	private String yearPattern = "yyyy";
	private SimpleDateFormat timeStamp = new SimpleDateFormat(datePattern);
	private SimpleDateFormat yearOnly = new SimpleDateFormat(yearPattern);
	private PGSQLService PSel;

	private static String BLOG_POST_DEFAULT = 
			"<h1><span class='btitle ffact'></span></h1>"
			+ "<span class='bdate'></span>   <span class='bline'></span>"
			+ "<hr>"
			+ "<article class='bpost factart'>"
			+ "</article>";
	private static String JSON_DEFAULT = "{\"article\":{\"blog\":}}";
	private static String BLOG_POST_SQL = "SELECT * FROM blog_post(?);";
	public ArticlePlugin() {
		this.webSettings = WebAppSettings.getObject();
	}
	
	@Override
	public String getHTMLResponse () {
		return getBlogPost();
	}

	private String getBlogPost() {
		StringBuilder htmlResultBuilder = new StringBuilder();
		ResultSet queryResult = null;
		try {
			openDataService();
			buildBLOG_POST_SQL();
			queryResult = getDataFromService();
			if (!(queryResult instanceof ResultSet))
			{
				return BLOG_POST_DEFAULT; 
			}
	    } catch (NullPointerException npe) {
			return BLOG_POST_DEFAULT; 
		} catch (Exception e) {
			return BLOG_POST_DEFAULT; 
		}
		
		try {
			while (queryResult.next()) {
				try {
					htmlResultBuilder.append("<h1><span class='btitle ffact'>" + queryResult.getString(1).trim() + "</span></h1>");
				} catch (NullPointerException npe) {
					htmlResultBuilder.append("<h1><span class='btitle ffact'></span></h1>");
				}
				try {
					htmlResultBuilder.append("<span class='bdate'>" + queryResult.getString(2).trim() + "</span>");
				} catch (NullPointerException npe) {
					htmlResultBuilder.append("<span class='bdate'></span>");
				}
				try {
					htmlResultBuilder.append("   <span class='bline'>" + queryResult.getString(3).trim() + "</span>");
				} catch (NullPointerException npe) {
					htmlResultBuilder.append("   <span class='bline'></span>");
				}
				try {
					htmlResultBuilder.append("<hr><article class='bpost factart'>" + queryResult.getString(4).trim() + "</article>");
				} catch (NullPointerException npe) {
					htmlResultBuilder.append("<hr><article class='bpost factart'></article>");
				}
			}
		} catch (SQLException sqle) {
			System.out.println("SQLException>" + sqle + "<");
			return BLOG_POST_DEFAULT; 
		}
				
		closeDataService();

//		System.out.println("htmlblogpost>>" + htmlResultBuilder.toString() + "<<");
		
		return htmlResultBuilder.toString();
		
	}
		
	@Override
	public String getXMLResponse () {
		return "<?xml version='1.0' encoding='UTF-8'?><xmlpayload></xmlpayload>";
	}

	@Override
	public String getJSONResponse () {		
		String jsonResult = JSON_DEFAULT;
		return jsonResult;
	
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

	private void buildBLOG_POST_SQL() {

		String aid="";
		String eid="";
		
		Iterator<contentElement> ei = this.jq.getContent().getElements().iterator();
		contentElement ce = new contentElement();
		if (ei.hasNext()){
			ce = ei.next();
			eid=ce.getElementId();
		} else {
			eid="MainHome.html";
		}

  		try {
			aid = (String)this.jq.getContent().getQueriesMap(eid).get("articleid");
	    } catch (NullPointerException npe) {
	    	aid="1";
	    } catch (Exception e) {
	    	aid="1";
		}
		
  		this.PSel.setPgSQLStatement(BLOG_POST_SQL);
	    this.PSel.setParamString(1, aid);

	}

}
