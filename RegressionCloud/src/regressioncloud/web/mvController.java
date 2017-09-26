package com.regressioncloud.web;

import javax.servlet.ServletContext;

import java.io.Serializable;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.regressioncloud.util.WebAppSettings;

public class mvController implements Serializable {

	private static final long serialVersionUID = -885856382691961120L;
	private ModelFactory responseObj;
	private JSONQuery jq;
	private String content = "";
	private ServletContext servletContext = null;
	private WebAppSettings webSettings = null;
	private SEOServletMap seoMap = null;
	
	public mvController() {
	}

	public void setContent(String content) {
		this.content = content;
//		System.out.println("this.content = " + this.content);
		Gson gson=new Gson(); 
		try {		
		if(this.content != null && !this.content.isEmpty()){
			jq = gson.fromJson(this.content, JSONQuery.class);
		} else {
			this.content = "{\"content\":{\"contentType\":\"24QaYfU0\",\"elements\":[{\"elementId\":\"\",\"queries\":[{\"key\":\"\",\"value\":\"\"}]}]}}";
			jq = gson.fromJson(this.content, JSONQuery.class);
		}
		} catch (NullPointerException npe) {
			this.content = "{\"content\":{\"contentType\":\"24QaYfU0\",\"elements\":[{\"elementId\":\"\",\"queries\":[{\"key\":\"\",\"value\":\"\"}]}]}}";
			jq = gson.fromJson(this.content, JSONQuery.class);
		} catch (JsonSyntaxException jse) {
			this.content = "{\"content\":{\"contentType\":\"24QaYfU0\",\"elements\":[{\"elementId\":\"\",\"queries\":[{\"key\":\"\",\"value\":\"\"}]}]}}";
			jq = gson.fromJson(this.content, JSONQuery.class);
		}

		
		try {
			//System.out.println("jq.getContent().getContentType() = " + jq.getContent().getContentType());
			this.responseObj = ModelFactory.getResponseFactory(jq.getContent().getContentType());
		} catch (NullPointerException e) {
			this.responseObj = ModelFactory.getResponseFactory("24QaYfU0");
			}
	}

	public String getContent() {
		return content;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		this.webSettings = WebAppSettings.buildObject(this.servletContext);
		seoMap = SEOServletMap.getObject(servletContext);
	}

	public ServletContext getServletContext() {
		return servletContext;
	}
	
	public String getSEOPath(){
		String seoPath = "";
//		System.out.println("getSEOPath::this.content "+this.content);
		if (seoMap.getServletPath(this.content) != null) {
			seoPath = seoMap.getServletPath(this.content);
		}
		return seoPath;
	}
	
	public String elementResponse(String elementId ) {
//		System.out.println("elementId = " + elementId);
		try {
			return responseObj.elementResponse(elementId, this.content, this.jq);
		} catch (NullPointerException npe) {
			return "";
		}
	}

	
}
