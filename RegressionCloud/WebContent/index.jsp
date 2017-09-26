<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

 <jsp:useBean id="rcGet" scope="page"
    class="com.regressioncloud.web.mvController">
	<jsp:setProperty name="rcGet" property="*"/>
 </jsp:useBean> 

<html>
<head>
<title>Regression Cloud</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="description" content="Exploring the art of presenting information visually and interactively to reveal trends and patterns hidden in the data.">
<%rcGet.setServletContext(application.getContext("/"+application.getServletContextName()));%> 
<%
if(rcGet.getContent() == null || rcGet.getContent().isEmpty()){
	if (request.getAttribute("content") != null && !request.getAttribute("content").toString().isEmpty()) {
		rcGet.setContent(request.getAttribute("content").toString());
	} else {
		rcGet.setContent("{\"content\":{\"contentType\":\"24QaYfU0\",\"elements\":[{\"elementId\":\"divMain\",\"queries\":[{\"key\":\"widget\",\"value\":\"j2IaY4I9\"}]},{\"elementId\":\"divRightAd\",\"queries\":[{\"key\":\"widget\",\"value\":\"j2IaY4I9\"}]}]}}");
	}
}
%>
<script type="text/javascript" src="ScriptLibrary/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="ScriptLibrary/jquery-ui-1.10.4.custom.min.js"></script>
<script type="text/javascript" src="ScriptLibrary/jquery.json-2.4.min.js"></script>
<script type="text/javascript" src="https://www.google.com/jsapi?autoload={'modules':[{'name':'visualization','version':'1.1','packages':['corechart','table','treemap','controls','motionchart']}]}"></script>
<script type="text/javascript" src="ScriptLibrary/helperFunctions.js"></script>
<script type="text/javascript" src="ScriptLibrary/ajaxControler.js"></script>
<link href="ScriptLibrary/rcStyles.css" type="text/css" rel="stylesheet">
<link href="ScriptLibrary/jquery-ui-1.10.4.custom.min.css" type="text/css" rel="stylesheet">
<link rel="shortcut icon" type="image/png" href="images/favicon.ico" />
</head>
<body>
<!-- DO NOT MOVE! The following AllWebMenus linking code section must always be placed right AFTER the BODY tag-->
<!-- ******** BEGIN ALLWEBMENUS CODE FOR rcMenuBar ******** -->
<script type="text/javascript">var MenuLinkedBy="AllWebMenus [4]",awmMenuName="awmAnchor-rcMenuBar",awmBN="932";</script><script charset="UTF-8" src="ScriptLibrary/rcMenuBar.js" type="text/javascript"></script><script type="text/javascript">awmBuildMenu();</script>
<!-- ******** END ALLWEBMENUS CODE FOR rcMenuBar ******** -->
<!-- $(document).ready(function(){seoURI('<%=rcGet.getSEOPath()%>',$.parseJSON('<%=rcGet.getContent()%>'))}); -->
<script>var objContent = contentJSONClass();objContent.setContent($.parseJSON('<%=rcGet.getContent()%>'));</script>	
<div id="divCenter" class="cCenter" style="Z-INDEX: 100; POSITION: relative; LEFT: 1px; MIN-HEIGHT: 100%">
  <script>
      $(window).resize(function(){
      	onCenter(3);
      });
  </script>

 <div id="divPost" style="display: none;">
    <form id="viewForm" action="<%=request.getContextPath()%>/index.jsp" method="post">
       <input type="submit" name="Enter" value="controller"> 
       <input id="content" type="hidden" name="content" value='{"content":{"contentType":"24QaYfU0","elements":[{"elementId":"divMain","queries":[{"key":"widget","value":"j2IaY4I9"}]},{"elementId":"divRightAd","queries":[{"key":"widget","value":"j2IaY4I9"}]}]}}'/>
     </form>
 </div>

 <div id="divAdSpace" class="cAdSpace" style="Z-INDEX: 1; LEFT: 1px; WIDTH: 1104px; HEIGHT: 100%">
    <%=rcGet.elementResponse("divAdSpace")%>
 </div>
 <div id="divBanner" class="cBanner" style="Z-INDEX: 1; LEFT: 1px; WIDTH: 1104px; HEIGHT: 100%; MIN-HEIGHT: 100px;">
	<div class="logo" style="width:170px; height:90px; FLOAT: left">
	<%@ include file="svg/Logo5.svg" %>
	</div>
	<div class="logo" style="width:300px; height:90px; FLOAT: left">
	<%@ include file="svg/MastHead.svg" %>
	</div>
	
	<div style="height:90px; FLOAT: left">
	<%=rcGet.elementResponse("divBanner")%>
	</div>	
</div>
      
 <div id="awmAnchor-rcMenuBar" class="cawmAnchor-rcMenuBar" style="Z-INDEX: 1; LEFT: 1px; WIDTH: 1104px; HEIGHT: 37px">
	<script type="text/javascript">onCenter(2);</script>
 </div>

 <div id="divCols" class="cdivCols" style=" Z-INDEX: 1; WIDTH: 1104px; HEIGHT: 100%">
 
 <div id="divMain" class="cMain" style="Z-INDEX: 1; FLOAT: left; WIDTH: 800px; ">
 <jsp:include page='<%=rcGet.elementResponse("divMain")%>'   flush='true'></jsp:include>
 </div>
 
 <div id="divRightAd" class="cRightAd" style="Z-INDEX: 1; FLOAT: left; WIDTH: 300px; ">
 <!--%=rcGet.elementResponse("divRightAd")%-->
  <jsp:include page='<%=rcGet.elementResponse("divRightAd")%>'   flush='true'></jsp:include>
 </div>

 <div id="divFooter" class="cFooter" style="Z-INDEX: -1; LEFT: 1px; WIDTH: 1104px; HEIGHT: 100%; clear:both; overflow:hidden;">
<jsp:include page='<%=rcGet.elementResponse("divFooter")%>'   flush='true'></jsp:include>
 </div>
</div>
</div>
</body>
</html>