function getBlog(aid,eid,cid,serv,path){
	var mimeType = "text/html";
  	var qs = [jq('articleid', aid)];
  	var es = [je(eid,qs)];
  	var cs = jc(cid,es);
  	var ajaxQuery =  'content='+escape($.toJSON(cs));
  	ajaxReq(ajaxQuery, mimeType, serv, path);
  }
