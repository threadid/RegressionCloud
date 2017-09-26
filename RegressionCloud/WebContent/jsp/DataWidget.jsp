    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <div id="dataCatTabs" style="visibility: hidden">
        <ul>
            <li><a href="#tabRCDataCat" class='tab'><span>Data Catalog</span></a></li>
        </ul>
        <div id="tabRCDataCat">
             <jsp:include page="../html/RCDataCat.html" flush="true" />
        </div>
    </div>
    <script type="text/javascript">
	$("#dataCatTabs").css("visibility","visible");
    $("#dataCatTabs").tabs({create: function( event, ui ) {initTabDataCat();}});
	seoURIReplace('data/regression-cloud-data-series-catalog-offerings',{});
    </script>
    
    