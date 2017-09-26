package com.regressioncloud.web;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

public class JSONQuery {
	
	private Content content;
	
	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public static class Content{
		private String contentType;
		private List<contentElement> elements;

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public List<contentElement> getElements() {
			return elements;
		}

		public void setElements(List<contentElement> elements) {
			this.elements = elements;
		}

		public Map<String, Object> getQueriesMap(String elementId) {
			Map<String,Object> qm = new HashMap<String,Object>();
			Iterator<contentElement> ei = elements.iterator();
			contentElement e = new contentElement();
			contentQuery q = new contentQuery();
			while (ei.hasNext()){
				e = ei.next();
				if (e.getElementId().matches(elementId)) {
					Iterator<contentQuery> qi = e.getQueries().iterator();
					while (qi.hasNext()){
						q = qi.next();
						qm.put(q.getKey(), q.getValue());
					}
				}
			}
			return qm;
		}

	} 

	public static class contentElement{
		private String elementId;
		private List<contentQuery> queries;

		public String getElementId() {
			return elementId;
		}

		public void setElementId(String elementId) {
			this.elementId = elementId;
		}

		public List<contentQuery> getQueries() {
			return queries;
		}

		public void setQueries(List<contentQuery> queries) {
			this.queries = queries;
		}
	}
	
	public static class contentQuery {
		private String key;
		private String value;
		
		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
