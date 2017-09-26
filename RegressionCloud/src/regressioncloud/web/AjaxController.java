package com.regressioncloud.web;

import java.io.Serializable;

import javax.servlet.ServletContext;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.regressioncloud.util.*;

public class AjaxController implements Serializable {

	private static final long serialVersionUID = 528900246948638249L;
	private String content;
	protected JSONQuery jq;
	protected ServletContext servletContext = null;
	private AjaxModelFactory responseObj = null;
	private WebAppSettings webSettings = null;
	
	public AjaxController() {
	}

	public void setContent(String content) {
		this.content = content;

//		System.out.println("setContent" + this.content);
		Gson gson=new Gson(); 
		try {		
		if(this.content != null && !this.content.isEmpty()){
			jq = gson.fromJson(this.content, JSONQuery.class);
		} else {
			this.responseObj = null;
			return;
		}
		} catch (NullPointerException npe) {
			this.responseObj = null;
			return;
		} catch (JsonSyntaxException jse) {
			this.responseObj = null;
			return;
			}
		
		try {
			this.responseObj = AjaxModelFactory.getResponseFactory(jq.getContent().getContentType());
		} catch (NullPointerException e) {
			this.responseObj = null;
			}
}

	public String getContent() {
		return content;
	}

	public void setServletContext(ServletContext servletContext) {
		//System.out.println("setServletContext");
		this.servletContext = servletContext;
		this.webSettings = WebAppSettings.buildObject(this.servletContext);
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public String htmlPayLoad() {
		if (this.responseObj==null) {
			return "";
		}
		this.responseObj.jq = this.jq;
		return this.responseObj.getHTMLResponse();	
		
	}

	public String xmlPayLoad() {
		if (this.responseObj==null) {
			return "";
		}

		this.responseObj.jq = this.jq;
		return this.responseObj.getXMLResponse();	
		
	}

	public String jsonPayLoad() {
		if (this.responseObj==null) {
			return "";
		}
		
		this.responseObj.jq = this.jq;
		return this.responseObj.getJSONResponse();	
		
	}

	public String svgPayLoad() {
		if (this.responseObj==null) {
			return "";
		}
		
		this.responseObj.jq = this.jq;
		return this.responseObj.getSVGResponse();	
		
	}

}
