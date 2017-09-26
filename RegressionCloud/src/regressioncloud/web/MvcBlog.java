package com.regressioncloud.web;

import java.util.Map;

public class MvcBlog extends ModelFactory {

	private Map<String, Object> qMap;
	private JSONQuery jq;
	private String json;
	private String elementID;

	public MvcBlog() {
	}

	@Override
	public String elementResponse(String elementID, String json, JSONQuery jq){
		this.elementID = elementID;
		this.jq = jq;
		this.json = json;
		this.qMap = this.jq.getContent().getQueriesMap(elementID);

		//System.out.println("elementId = " + elementID);
		//System.out.println(this.qMap.isEmpty());
		switch (elementID) {
		      case "divBanner": 
		    	  return divBanner();
		      case "divAdSpace": 
		    	  return divAdSpace();
		      case "divMain": 
		    	  return divMain();
		      case "divRightAd": 
		    	  return divRightAd();
		      case "divFooter": 
		    	  return divFooter();
		      default        : 
		    	  return "";
		    }
	}
	
	public void setPersist(String args) {
	}

	public String getPersist() {
		return "";
	}

	public String divBanner() {
		return "";
	}

	public String divAdSpace() {
		return "";
	}

	public String divLeftNav() {
		return "";
	}

	public String divMain() {
		return "html/Blog.html";
	}

	public String divRightAd() {
		String widget = "";
		if (this.qMap.get("widget") != null){
			widget = (String)this.qMap.get("widget");
			}

		switch (widget) {
			default        : 
				return "jsp/rightAdBlog.jsp";
		}
	}


	public String divFooter() {
		return "html/Footer.html";
	}
	
}
