    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <script type="text/javascript" src="ScriptLibrary/visualChart.js"></script>
	<script type="text/javascript" src="ScriptLibrary/CensusYears.js"></script>
    <div id="censusTabs" style="visibility: hidden">
        <ul>
            <li><a href="#tabCensusPop" class='tab'><span>US Population</span></a></li>
            <li><a href="#tabCensusChg" class='tab'><span>Change US Population</span></a></li>
            <li><a href="#tabCensusRate" class='tab'><span>Rate of Change</span></a></li>
        </ul>
        <div id="tabCensusPop">
             <jsp:include page="../html/CensusPop.html" flush="true" />
        </div>
        <div id="tabCensusChg">
             <jsp:include page="../html/CensusChg.html" flush="true" />
        </div>
        <div id="tabCensusRate">
             <jsp:include page="../html/CensusRate.html" flush="true" />
        </div>
    </div>
    <script type="text/javascript">
	var objCensusYears = classCensusYears();
	$("#censusTabs").css("visibility","visible");
    $("#censusTabs").tabs({create: function( event, ui ) {objCensusYears.reqCensusYears();}});
	seoURIReplace('visual/dashboard-us-population-birth-death-migration-change-rate',{});

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
				initTabCensusPop();
				}
		}
	});
    </script>
    
    