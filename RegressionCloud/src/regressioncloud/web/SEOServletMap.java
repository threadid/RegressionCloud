package com.regressioncloud.web;

import java.util.Map;

import javax.servlet.ServletContext;

public class SEOServletMap {

	// Class Level Objects
	private static SEOServletMap SEOServletMapInstance;
	private static Map<String, Object> pathToQuery;
	private static Map<String, Object> queryToPath;
    
	private SEOServletMap() {
	}

	public static SEOServletMap getObject(ServletContext servletContext) {
		if (!(SEOServletMap.SEOServletMapInstance instanceof SEOServletMap) )
			{
    			synchronized(SEOServletMap.class) {
    				if (!(SEOServletMap.SEOServletMapInstance instanceof SEOServletMap) )
    					{
    					SEOServletMapInstance = new SEOServletMap();
    					initSEOServletMap(servletContext);
    					}
    				}
			}
    	return SEOServletMapInstance;
    }
	
	private static void initSEOServletMap(ServletContext servletContext){
		SEOServletPathPlugin seoPlugin = new SEOServletPathPlugin();
		seoPlugin.setServletContext(servletContext);
		pathToQuery = seoPlugin.getPathQueryMap();
		queryToPath = seoPlugin.getQueryPathMap();
		seoPlugin = null;
	}

	public String getServletPath(String query) {
		Map <String, Object> seoMap;
		String servletPath = "";
		if (queryToPath.containsKey(query)) {
			seoMap = (Map<String, Object>) queryToPath.get(query);
			servletPath = seoMap.get("servlet_path").toString() + "/" + seoMap.get("content_path").toString();
		} 
		return servletPath;
	}

	public String getQuery(String path) {
		Map <String, Object> seoMap;
		String query = "";
		if (pathToQuery.containsKey(path)) {
			seoMap = (Map<String, Object>) pathToQuery.get(path);
			query = seoMap.get("query").toString();
		}
		return query;
	}
	
}
