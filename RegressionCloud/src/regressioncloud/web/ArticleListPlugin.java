package com.regressioncloud.web;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.regressioncloud.util.WebAppSettings;
import com.regressioncloud.pgsql.PGSQLService;

public class ArticleListPlugin extends AjaxModelFactory{

	private WebAppSettings webSettings;
	private String datePattern = "yyyy-mm-dd hh:mm:ss:SS";
	private String yearPattern = "yyyy";
	private SimpleDateFormat timeStamp = new SimpleDateFormat(datePattern);
	private SimpleDateFormat yearOnly = new SimpleDateFormat(yearPattern);
	private PGSQLService PSel;
	private String elementID;
	private String arcType;
	private Map<String, Object> qMap;

	private static String HTML_DEFAULT = "<script>"
			+ "$(function() {"
			+ "$( '#blog' ).accordion({'heightStyle': 'content', 'collapsible': true, icons: {'header': 'ui-icon-plusthick', 'activeHeader': 'ui-icon-minusthick'}});"
			+ "});"
			+ "$(function() {"
			+ "$( '.accPan' ).resizable();"
			+ "$( '.accPan' ).addClass('isOverflowHidden');"
			+ "});"
			+ "</script>";
	private static String JSON_DEFAULT = "{\"article\":{\"blog\":}}";
	private static String BLOG_LIST_SQL = "SELECT * FROM blog_list(?);";
	public ArticleListPlugin() {
		this.webSettings = WebAppSettings.getObject();
	}
	
	@Override
	public String getHTMLResponse () {
		String htmlResult = HTML_DEFAULT;
		StringBuilder htmlSerializer = new StringBuilder();
		htmlSerializer.append(htmlResult);
		this.elementID = "AdBlogList";
		this.qMap = this.jq.getContent().getQueriesMap(this.elementID);

		String widget = "";
		if (this.qMap.get("widget") != null){
			widget = (String)this.qMap.get("widget");
		} else {
			return HTML_DEFAULT;
		}

		
		//System.out.println("ModelArticlePlugin-elementID>>"+this.elementID+"<<");
		//System.out.println("ModelArticlePlugin-widget>>"+widget+"<<");
		
		//System.out.println("divRightAd widget = " + widget);
		switch (widget) {
			case "j2IaY4I9": 
				this.arcType = "FROM_MainHome.html_TO_blog_articles";
				htmlSerializer.insert(0, CensusBlogList());
				break;
			case "GbQ8O3E1": 
				this.arcType = "FROM_VisualData.html_TO_blog_articles";
				htmlSerializer.insert(0, CensusBlogList());
				break;
			case "TbZ1MfI4": 
				this.arcType = "FROM_Analysis.html_TO_blog_articles";
				htmlSerializer.insert(0, CensusBlogList());
				break;
			case "W6M0MdQ6": 
				this.arcType = "FROM_Technologies.html_TO_blog_articles";
				htmlSerializer.insert(0, CensusBlogList());
				break;
			case "j0g0M8Mb": 
				this.arcType = "FROM_Terms.html_TO_blog_articles";
				htmlSerializer.insert(0, CensusBlogList());
				break;
			case "T7E0M6Ed": 
				this.arcType = "FROM_Blog.html_TO_blog_articles";
				htmlSerializer.insert(0, CensusBlogList());
				break;
			case "GcI4NaU9": 
				this.arcType = "FROM_DataCatalog.html_TO_blog_articles";
				htmlSerializer.insert(0, CensusBlogList());
				break;
			default        : 
				return HTML_DEFAULT;
		}
		
		htmlResult = htmlSerializer.toString();
		
		//System.out.println("ModelArticle>>" + htmlResult + "<<");

		return htmlResult;
	}
		
	private String CensusBlogList() {
		StringBuilder htmlResultBuilder = new StringBuilder();
		ResultSet queryResult = null;

		String articleID = "";
		if (this.qMap instanceof Map){
			if (this.qMap.get("articleid") != null){
				articleID = (String)this.qMap.get("articleid");
			}
		}
						
		try {
			openDataService();
			buildBLOG_LIST_SQL();
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
		
		htmlResultBuilder.append("<p><table>");
		
		//System.out.println("Before while next  = " + timeStamp.format(Calendar.getInstance(Locale.US).getTimeInMillis()));
		try {
			while (queryResult.next()) {
				if (queryResult.isFirst()) {
					if (articleID == null || articleID.isEmpty() ) {
						htmlResultBuilder.append("<script>getBlog('"+queryResult.getString(1).trim()+"','"+queryResult.getString(3).trim()+"','"+queryResult.getString(4).trim()+"','"+queryResult.getString(5).trim()+"','"+queryResult.getString(6).trim()+"');</script>");
					}
				}
				if (articleID != null && !articleID.isEmpty() ) {
					if (articleID.matches(queryResult.getString(1).trim())) {
						htmlResultBuilder.append("<script>getBlog('"+queryResult.getString(1).trim()+"','"+queryResult.getString(3).trim()+"','"+queryResult.getString(4).trim()+"','"+queryResult.getString(5).trim()+"','"+queryResult.getString(6).trim()+"');</script>");
					}
				}
				htmlResultBuilder.append("<tr><td>");
				htmlResultBuilder.append("<a  href='#' onclick=\"getBlog('"+queryResult.getString(1).trim()+"','"+queryResult.getString(3).trim()+"','"+queryResult.getString(4).trim()+"','"+queryResult.getString(5).trim()+"','"+queryResult.getString(6).trim()+"');return false;\">");
				htmlResultBuilder.append("<span class='ffact'>" + queryResult.getString(2).trim() + "</span>");
				htmlResultBuilder.append("</a></td></tr>");
				htmlResultBuilder.append("<tr><td><hr class='dash'></td></tr>");
			}
		} catch (SQLException sqle) {
			System.out.println("SQLException>" + sqle + "<");
			return HTML_DEFAULT; 
		}
				
		//System.out.println("After while next  = " + timeStamp.format(Calendar.getInstance(Locale.US).getTimeInMillis()));
		htmlResultBuilder.append("</table></p>");
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

	private void buildBLOG_LIST_SQL() {
		
  		this.PSel.setPgSQLStatement(BLOG_LIST_SQL);
	    this.PSel.setParamString(1, this.arcType);

	}

}
