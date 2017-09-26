package com.regressioncloud.web;

import java.sql.ResultSet;

public abstract class AjaxModelFactory extends AjaxController {

	public abstract String getXMLResponse();
	public abstract String getHTMLResponse();
	public abstract String getJSONResponse();
	public abstract String getSVGResponse();
	protected abstract void openDataService();
	protected abstract ResultSet getDataFromService ();
	protected abstract void closeDataService();
	
	public AjaxModelFactory() {
	}

	public static AjaxModelFactory getResponseFactory(String whichPlugin) {
		   switch (whichPlugin) {
		      case "mfldZeM0": // CENSUSUS
		          return new CensusUSPlugin();
		      case "W2Y7N6U3": // CENSUSCHG
		          return new CensusChgPlugin();
		      case "m4MdZ8U0": // CENSUSRATE
		          return new CensusRatePlugin();
		      case "W2g6NbZ9": // CENSUSANOVA
		          return new CensusAnovaPlugin();
		      case "D4V6NeE4": // CENSUSANOVAR
		          return new CensusAnovaRPlugin();
		      case "z1I8Ocke": // CENSUSINITIAL
		          return new CensusInitialPlugin();
		      case "Dac8Oekf": // CENSUSARTICLE
		          return new ArticlePlugin();
		      case "DfYfZ6Q1": // Data Catalogue
		          return new DataCataloguePlugin();
		      case "z6V2M1h4": // Data Select
		          return new DataSelectPlugin();
		      case "zfQ5N7Be": // Blog List
		          return new ArticleListPlugin();
		      case "j5R5NcE5": // Notes
		          return new NoteListPlugin();
		      case "jcYdZ3R9": // Notes
		          return new FRBH15Plugin();
		      default        : 
		          return null;
		   }
		   
	}
}
