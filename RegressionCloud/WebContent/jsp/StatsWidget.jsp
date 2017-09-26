    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <script type="text/javascript" src="ScriptLibrary/visualChart.js"></script>
	<script type="text/javascript" src="ScriptLibrary/CensusYears.js"></script>
    <div id="statTabs" style="visibility: hidden">
        <ul>
            <li><a href="#tabCensusAnova" class='tab'><span>ANOVA SQL</span></a></li>
            <li><a href="#tabCensusAnovaR" class='tab'><span>ANOVA R</span></a></li>
        </ul>
        <div id="tabCensusAnova">
             <jsp:include page="../html/CensusAnova.html" flush="true" />
        </div>
        <div id="tabCensusAnovaR">
             <jsp:include page="../html/CensusAnovaR.html" flush="true" />
        </div>
    </div>
    <script type="text/javascript">
	var objCensusYears = classCensusYears();
	$("#statTabs").css("visibility","visible");
    $("#statTabs").tabs({create: function( event, ui ) {objCensusYears.reqCensusYears();}});
	seoURIReplace('analysis/anova-us-population-birth-death-migration-change-rate',{});

    $(document).ajaxSuccess(function(event, xhr, settings) {
    	if (typeof settings.data == 'undefined') {return false;}
    	if (typeof settings.mimeType == 'undefined') {return false;}
        var query = settings.data;
        var mimeType = settings.mimeType;
	    if (query.match(/z1I8Ocke/) && query.match(/classCensusYears/)){
	        if(mimeType.match(/application\/json/)){
	            var jsonCensusYears = $.parseJSON(xhr.responseText);
	            objCensusYears.setYearsDataTable(jsonCensusYears.initial.years);
	            objCensusYears.setCLabTable(jsonCensusYears.initial.columns);
	            initTabCensusAnova();
	            }
	    }
	});
    </script>
    
    