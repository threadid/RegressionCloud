    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<script type="text/javascript" src="ScriptLibrary/TYCChart.js"></script>
    <div id="H15Tabs" style="visibility: hidden">
        <ul>
            <li><a href="#tabH15W"><span>H15 Weekly</span></a></li>
            <li><a href="#tabH15YC"><span>Yield Curve</span></a></li>
        </ul>
        <div id="tabH15W">
             <jsp:include page="../html/H15W.html" flush="true" />
        </div>
        <div id="tabH15YC">
             <jsp:include page="../html/H15YC.html" flush="true" />
        </div>
    </div>
    <script>
		$("#H15Tabs").css("visibility","visible");
		$("#H15Tabs").tabs({create: function( event, ui ) {loadTreasCharts('w', 100);}});
		seoURIReplace('visual/frb-usgovernment-securities-treasury-constant-maturities-h15',{});
    </script>
    
