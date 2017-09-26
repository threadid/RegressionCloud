package com.regressioncloud.web;

import java.util.Map;

public class MvcStats extends ModelFactory {

	private Map<String, Object> qMap;
	private JSONQuery jq;
	private String json;
	private String elementID;

	MvcStats() {
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
	
	public String divBanner() {
		return "";
	}

	public String divAdSpace() {
		return "";
	}

	public String divMain() {
		String widget = "";
		if (this.qMap.get("widget") != null){
			widget = (String)this.qMap.get("widget");
			}
		//System.out.println("divMain widget = " + widget);
		switch (widget) {
			case "TbZ1MfI4": 
				return "html/Analysis.html";
			case "z5l7NeNf": 
				return "jsp/StatsWidget.jsp";
			default        : 
				return "html/Analysis.html";
		}
	}
 
	public String divRightAd() {
			String widget = "";
			if (this.qMap.get("widget") != null){
				widget = (String)this.qMap.get("widget");
				}

			switch (widget) {
				case "TbZ1MfI4": 
					return "jsp/rightAdBlog.jsp";
				case "z5l7NeNf": 
					return "jsp/rightAdStats.jsp";
				default        : 
					return "jsp/rightAdDefault.jsp";
			}
	}

	public String divFooter() {
		return "html/Footer.html";
	}

}
