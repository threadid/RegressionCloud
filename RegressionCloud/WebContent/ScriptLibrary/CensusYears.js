	function classCensusYears() {

		var censusYearsDataTable;
		var censusCLabTable;

        function censusYears() {       
        }

        censusYears.reqCensusYears = function(){
        	var mimeType = "application/json";
        	var qs = [jq('', '')];
        	var es = [je('classCensusYears',qs)];
        	var ajaxQuery =  'content='+escape($.toJSON(jc('z1I8Ocke',es)));
        	ajaxReq(ajaxQuery, mimeType);
        };
        
        censusYears.setYearsDataTable = function(jsonYearsData) {
       		censusYearsDataTable = new google.visualization.DataTable(jsonYearsData);
        };

        censusYears.getYearsDataTable = function() {
            return censusYearsDataTable;
        };
        
        censusYears.setCLabTable = function(jsonCLab) {
        	censusCLabTable = new google.visualization.DataTable(jsonCLab);
        };

        censusYears.getCLabTable = function() {
            return censusCLabTable;
        };
        
        censusYears.hasYear = function(newYear, newData) {
        	if (typeof censusYearsDataTable == 'undefined') {return false;}
        	var cy = censusYearsDataTable.getFilteredRows([
                       {column: colID(censusYearsDataTable, 'estyear'), value: newYear.toString()}
                       ,{column: colID(censusYearsDataTable, 'estdata'), value: newData.toString()}]);
        	if (cy==0){return false;}
        	return true;
        };

        censusYears.getData = function(newYear){
        	var jyd = {y:'',d:''};
        	var jd = {data:[]};
        	if (typeof censusYearsDataTable == 'undefined') {
        		jd.data[0] = jyd;
        		return jd;}
            wdt = censusYearsDataTable;
            wdt.sort([
                {column: colID(wdt, 'estyear')}
                ,{column: colID(wdt, 'estdata')}]);
            var yView = new google.visualization.DataView(wdt);
            yView.setRows(yView.getFilteredRows([{column: colID(yView, 'estyear'), value: newYear}]));
            for (var i=0;i<yView.getNumberOfRows();i++) {
            	jyd = {d:''};
            	jyd.y = yView.getValue(i, colID(yView, 'estyear'));
            	jyd.d = yView.getValue(i, colID(yView, 'estdata'));
            	jd.data.push(jyd);            	
            }
            return jd;
        }
        
        censusYears.getYears = function(newData){
        	var jyd = {y:'',d:''};
        	var jd = {data:[]};
        	if (typeof censusYearsDataTable == 'undefined') {
        		jd.data[0] = jyd;
        		return jd;}
            wdt = censusYearsDataTable;
            wdt.sort([
                {column: colID(wdt, 'estdata')}
                ,{column: colID(wdt, 'estyear')}]);
            var yView = new google.visualization.DataView(wdt);
            yView.setRows(yView.getFilteredRows([{column: colID(yView, 'estdata'), value: newData}]));
            for (var i=0;i<yView.getNumberOfRows();i++) {
            	jyd = {y:'',d:''};
            	jyd.y = yView.getValue(i, colID(yView, 'estyear'));
            	jyd.d = yView.getValue(i, colID(yView, 'estdata'));
            	jd.data.push(jyd);            	
            }
            return jd;
        }
        
        censusYears.minYear = function(estdata) {
        	var cd = new Date();
        	var cdy = cd.getFullYear()-1;
        	if (typeof censusYearsDataTable == 'undefined') {return cdy;}
            wdt = censusYearsDataTable;
            wdt.sort([{column: colID(censusYearsDataTable, 'estdata')}
        			,{column: colID(censusYearsDataTable, 'estyear')}]);
            var yView = new google.visualization.DataView(wdt);
            yView.setRows(yView.getFilteredRows([{column: colID(yView, 'estdata'), value: estdata}]));
        	var my = yView.getValue(0,colID(yView, 'estyear'));
        	return my;
        };

        censusYears.maxYear = function(estdata) {
        	var cd = new Date();
        	var cdy = cd.getFullYear()-1;
        	if (typeof censusYearsDataTable == 'undefined') {return cdy;}
            wdt = censusYearsDataTable;
            wdt.sort([{column: colID(censusYearsDataTable, 'estdata')}
        			,{column: colID(censusYearsDataTable, 'estyear'), desc: true}]);
            var yView = new google.visualization.DataView(wdt);
            yView.setRows(yView.getFilteredRows([{column: colID(yView, 'estdata'), value: estdata}]));
        	var my = yView.getValue(0, colID(yView, 'estyear'));
        	return my;
        };
        
        censusYears.cLabel = function(estdata) {
            var yView = new google.visualization.DataView(censusCLabTable);
            yView.setRows(yView.getFilteredRows([{column: colID(yView, 'estdata'), value: estdata}]));
        	var lab = yView.getValue(0,colID(yView, 'estdatalabel'));
        	return lab;
        };

        return censusYears;
	}
