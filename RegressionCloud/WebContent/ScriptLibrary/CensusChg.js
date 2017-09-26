	function classChgSummary() {

		var jsonSum;
		
        function chgSummary() {       
        }
    
        chgSummary.getSummary = function(chgDataTable, chgFact, chgFactLabel, chgFactCol, chgFactYear) {
            var wdt = chgDataTable;
		    wdt.sort([
		        {column: colID(wdt, 'sumlev')}
		        ,{column: colID(wdt, 'estyear')}
		        ,{column: colID(wdt, 'estdata')}
		        ,{column: chgFactCol, desc:  true}]);
		    dView = new google.visualization.DataView(wdt);
		    dView.setRows(dView.getFilteredRows([
		        {column: colID(dView, 'sumlev'), value: '010'}
		        ,{column: colID(dView, 'estdata'), value: chgFact}
		    	,{column: colID(dView, 'estyear'), value: chgFactYear}]));
		    
		    jsonSum = {"facts":{
						"fyear":{"y":chgFactYear}
						,"fname":{"n":chgFactLabel,"f":chgFact}
						,"fsum":{"c":0,"v":0}
						,"fmin":{"v":0,"stn":""}
						,"fmax":{"v":0,"stn":""}
						,"fmed":{"v":0}
						,"fmean":{"v":0}
						,"fdeath":{"v":0}
						,"fbirth":{"v":0}
						,"fnetmig":{"v":0}
						,"fnetchg":{"v":0}
	    			   }};
		    
		    jsonSum.facts.fsum.v = dView.getValue(0, chgFactCol);
		    jsonSum.facts.fmin.v = dView.getValue(0, colID(dView, 'pmin'));
		    jsonSum.facts.fmax.v = dView.getValue(0, colID(dView, 'pmax'));
		    jsonSum.facts.fmed.v = dView.getValue(0, colID(dView, 'pmedian'));
		    jsonSum.facts.fmean.v = dView.getValue(0, colID(dView, 'pmean'));

		    dView = new google.visualization.DataView(wdt);
		    dView.setRows(dView.getFilteredRows([
		                         		        {column: colID(dView, 'sumlev'), value: '010'}
		                         		        ,{column: colID(dView, 'estdata'), value: 'births'}
		                         		    	,{column: colID(dView, 'estyear'), value: chgFactYear}]));
		    jsonSum.facts.fbirth.v = dView.getValue(0, colID(dView, 'psum'));

		    dView = new google.visualization.DataView(wdt);
		    dView.setRows(dView.getFilteredRows([
			                         		        {column: colID(dView, 'sumlev'), value: '010'}
			                         		        ,{column: colID(dView, 'estdata'), value: 'deaths'}
			                         		    	,{column: colID(dView, 'estyear'), value: chgFactYear}]));
		    jsonSum.facts.fdeath.v = dView.getValue(0, colID(dView, 'psum'));

		    dView = new google.visualization.DataView(wdt);
		    dView.setRows(dView.getFilteredRows([
			                         		        {column: colID(dView, 'sumlev'), value: '010'}
			                         		        ,{column: colID(dView, 'estdata'), value: 'netmig'}
			                         		    	,{column: colID(dView, 'estyear'), value: chgFactYear}]));
		    jsonSum.facts.fnetmig.v = dView.getValue(0, colID(dView, 'psum'));
		    		    
		    dView = new google.visualization.DataView(wdt);
		    dView.setRows(dView.getFilteredRows([
			                         		        {column: colID(dView, 'sumlev'), value: '010'}
			                         		        ,{column: colID(dView, 'estdata'), value: 'npopchg'}
			                         		    	,{column: colID(dView, 'estyear'), value: chgFactYear}]));
		    jsonSum.facts.fnetchg.v = dView.getValue(0, colID(dView, 'psum'));
		    		    
		    sumView = new google.visualization.DataView(wdt);
		    sumView.setRows(sumView.getFilteredRows([
		         {column: colID(sumView, 'sumlev'), value: '040'}
		         ,{column: colID(sumView, 'estdata'), value: chgFact}
		         ,{column: colID(sumView, 'estyear'), value: chgFactYear}]));
		    sumView.setColumns([colID(sumView, 'state'), colID(sumView, 'stname'), chgFactCol]);
		            jsonSum.facts.fsum.c = sumView.getNumberOfRows();
		            jsonSum.facts.fmax.stn = sumView.getValue(0, 1);
		            jsonSum.facts.fmin.stn = sumView.getValue(sumView.getNumberOfRows()-1, 1);
		};

        chgSummary.drawSummary = function(sumArticle) {
            $('#chgTabTitle').find('.factYear').text(jsonSum.facts.fyear.y);
            $('#chgTabTitle').find('.factName').text(jsonSum.facts.fname.n);
            $('.'+sumArticle+'_div.title').find('.factYear').text(jsonSum.facts.fyear.y);
            $('.'+sumArticle+'_div.title').find('.factName').text(jsonSum.facts.fname.n);
            $('#'+sumArticle).find('.factYear').text(jsonSum.facts.fyear.y);
            $('#'+sumArticle).find('.factName').text(jsonSum.facts.fname.n);
            $('#'+sumArticle).find('.fsum').text(factFormat(jsonSum.facts.fsum.v, 2));
            $('#'+sumArticle).find('.fmin').text(factFormat(jsonSum.facts.fmin.v, 2));
            $('#'+sumArticle).find('.fminst').text(jsonSum.facts.fmin.stn);
            $('#'+sumArticle).find('.fmax').text(factFormat(jsonSum.facts.fmax.v, 2));
            $('#'+sumArticle).find('.fmaxst').text(jsonSum.facts.fmax.stn);
            if(jsonSum.facts.fname.f=='deaths'){
                $('#'+sumArticle).find('.fmaxdesc').text('greatest decrease');
                $('#'+sumArticle).find('.fmindesc').text('lowest decrease');
            } else {
                $('#'+sumArticle).find('.fmaxdesc').text('greatest increase');
                $('#'+sumArticle).find('.fmindesc').text('lowest increase');
            }
            if(jsonSum.facts.fmin.v<0){
                $('#'+sumArticle).find('.fmindesc').text('greatest decrease');
            }
            $('#'+sumArticle).find('.fmed').text(factFormat(jsonSum.facts.fmed.v, 2));
            $('#'+sumArticle).find('.fmean').text(factFormat(jsonSum.facts.fmean.v, 2));
            $('#'+sumArticle).find('.fbirth').text(factFormat(jsonSum.facts.fbirth.v, 2));
            $('#'+sumArticle).find('.fdeath').text(factFormat(jsonSum.facts.fdeath.v, 2));
            $('#'+sumArticle).find('.fnetmig').text(factFormat(jsonSum.facts.fnetmig.v, 2));
            $('#'+sumArticle).find('.fnetchg').text(factFormat(jsonSum.facts.fnetchg.v, 2));
         };	
        return chgSummary;
	}
