package com.regressioncloud.web;

import java.util.Map;

public abstract class ModelFactory extends mvController {

	public abstract String elementResponse(String elementId, String json, JSONQuery jq);
	
	public ModelFactory() {
	}

	public static ModelFactory getResponseFactory(String whichModel) {
		
	   switch (whichModel) {
	      case "24QaYfU0": 
	          return new MvcHome();
	      case "Gfk9O7Ia": 
	          return new MvcCensus();
	      case "WaE2M6R8": 
	          return new MvcStats();
	      case "TaU9O5Y9": 
	          return new MvcAbout();
	      case "m6J1Mfga": 
	          return new MvcBlog();
	      case "zeM4N5Va": 
	          return new MvcData();
	      default        : 
	          return new MvcHome();
	    }

	}

}
