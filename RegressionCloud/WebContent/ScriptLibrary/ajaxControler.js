function contentJSONClass() {

	var cJSON;
	
	function contentJSON(){}
	
	contentJSON.setContent = function(json){
		cJSON = json;
	};
	
	contentJSON.getContent = function(){
		return cJSON;
	};

	contentJSON.getWidget = function(element){
		if (typeof cJSON == 'undefined') {
			return "";
		}
		for (var i=0;i<cJSON.content.elements.length;i++) {
			if(cJSON.content.elements[i].elementId.localeCompare(element)==0){
				for (var ii=0;ii<cJSON.content.elements[i].queries.length;ii++) {
					if(cJSON.content.elements[i].queries[ii].key.localeCompare('widget')==0){
						return cJSON.content.elements[i].queries[ii].value;
					}
				}
			}
		}
	};

	contentJSON.getKey = function(element, key){
		if (typeof cJSON == 'undefined') {
			return "";
		}
		for (var i=0;i<cJSON.content.elements.length;i++) {
			if(cJSON.content.elements[i].elementId.localeCompare(element)==0){
				for (var ii=0;ii<cJSON.content.elements[i].queries.length;ii++) {
					if(cJSON.content.elements[i].queries[ii].key.localeCompare(key)==0){
						return cJSON.content.elements[i].queries[ii].value;
					}
				}
			}
		}
	};

	return contentJSON;
}


function jc(ct,es){
	if (typeof ct == 'undefined'){ct='';}
	var c = $.parseJSON('{"content":{"contentType":"' + ct + '","elements":[]}}');
	if (typeof es != 'undefined') {
		for (var i=0;i<es.length;i++){
			c.content.elements[i] = es[i];
			}
	}
	return c;
}

function je(ei, qs){
	if (typeof ei == 'undefined'){ei='';}
	var e = $.parseJSON('{"elementId":"' + ei + '","queries":[]}');
	if (typeof qs != 'undefined') {
		for (var i=0;i<qs.length;i++){
			e.queries[i] = qs[i];
			}
	}
	return e;
}

function jq(k, v){
	if (typeof k == 'undefined'){k='';}
	if (typeof v == 'undefined'){v='';}
	var q = $.parseJSON('{"key":"' + k + '","value":"' + v + '"}');
	return q;
}

function ajaxReq(ajaxQuery, mimeType, serv, path){
	var slash = "/";
	if (typeof serv == "undefined"){serv="";path="";slash="";}
	if (typeof path == "undefined"){path="";slash="";}
	if (serv == ""){path="";slash="";}
	if (path == ""){slash="";}
	var ajaxHREF = serv+slash+path;
	
	if((ajaxHREF=="") && mimeType.match(/application\/xml/)){
		ajaxHREF = "jsp/ajaxGetXML.jsp";
		}
	if((ajaxHREF=="") && mimeType.match(/text\/html/)){
		ajaxHREF = "jsp/ajaxGetHTML.jsp";
		}
	if((ajaxHREF=="") && mimeType.match(/application\/json/)){
		ajaxHREF = "jsp/ajaxGetJSON.jsp";
		}
	if((ajaxHREF=="") && mimeType.match(/image\/svg\+xml/)){
		ajaxHREF = "jsp/ajaxGetSVG.jsp";
		}
	$.ajax({
		url: ajaxHREF,
		type: "post",
		data: ajaxQuery,         
		beforeSend: function ( xhr ) {
		    xhr.overrideMimeType(mimeType);
		    },
		success: function(response, textStatus, jqXHR){ 
			},         
		error: function(jqXHR, textStatus, errorThrown){             
			console.log("JSQL ajax error: " + textStatus + ", " + errorThrown);
			console.log(jqXHR.responseText);
			}, 
		complete: function(){
			}
		}); 	
}

