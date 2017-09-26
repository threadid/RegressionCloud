	function classRateSummary() {

		var jsonSum;
		
        function rateSummary() {       
        }
    
        rateSummary.getSummary = function(rateDataTable, rateFact, rateFactLabel, rateFactCol, rateFactYear) {
            var wdt = rateDataTable;
		    wdt.sort([
		        {column: colID(wdt, 'sumlev')}
		        ,{column: colID(wdt, 'estyear')}
		        ,{column: colID(wdt, 'estdata')}
		        ,{column: rateFactCol, desc:  true}]);
		    dView = new google.visualization.DataView(wdt);
		    dView.setRows(dView.getFilteredRows([
		        {column: colID(dView, 'sumlev'), value: '010'}
		        ,{column: colID(dView, 'estdata'), value: rateFact}
		    	,{column: colID(dView, 'estyear'), value: rateFactYear}]));
		    
		    jsonSum = {"facts":{
						"fyear":{"y":rateFactYear}
						,"fname":{"n":rateFactLabel,"f":rateFact}
						,"fsum":{"c":0,"v":0}
						,"fmin":{"v":0,"stn":""}
						,"fmax":{"v":0,"stn":""}
						,"fmed":{"v":0}
						,"fmean":{"v":0}
	    			   }};
		    
		    jsonSum.facts.fsum.v = dView.getValue(0, rateFactCol);
		    jsonSum.facts.fmin.v = dView.getValue(0, colID(dView, 'pmin'));
		    jsonSum.facts.fmax.v = dView.getValue(0, colID(dView, 'pmax'));
		    jsonSum.facts.fmed.v = dView.getValue(0, colID(dView, 'pmedian'));
		    jsonSum.facts.fmean.v = dView.getValue(0, colID(dView, 'pmean'));

		    sumView = new google.visualization.DataView(wdt);
		    sumView.setRows(sumView.getFilteredRows([
		         {column: colID(sumView, 'sumlev'), value: '040'}
		         ,{column: colID(sumView, 'estdata'), value: rateFact}
		         ,{column: colID(sumView, 'estyear'), value: rateFactYear}]));
		    sumView.setColumns([colID(sumView, 'state'), colID(sumView, 'stname'), rateFactCol]);
		            jsonSum.facts.fsum.c = sumView.getNumberOfRows();
		            jsonSum.facts.fmax.stn = sumView.getValue(0, 1);
		            jsonSum.facts.fmin.stn = sumView.getValue(sumView.getNumberOfRows()-1, 1);
		};

        rateSummary.drawSummary = function(sumArticle) {
            $('#rateTabTitle').find('.factYear').text(jsonSum.facts.fyear.y);
            $('#rateTabTitle').find('.factName').text(jsonSum.facts.fname.n);
            $('.'+sumArticle+'_div.title').find('.factYear').text(jsonSum.facts.fyear.y);
            $('.'+sumArticle+'_div.title').find('.factName').text(jsonSum.facts.fname.n);
            $('#'+sumArticle).find('.factYear').text(jsonSum.facts.fyear.y);
            $('#'+sumArticle).find('.factName').text(jsonSum.facts.fname.n);
            $('#'+sumArticle).find('.fsum').text(factFormat(jsonSum.facts.fsum.v, 2));
            $('#'+sumArticle).find('.fmin').text(factFormat(jsonSum.facts.fmin.v, 2));
            $('#'+sumArticle).find('.fminst').text(jsonSum.facts.fmin.stn);
            $('#'+sumArticle).find('.fmax').text(factFormat(jsonSum.facts.fmax.v, 2));
            $('#'+sumArticle).find('.fmaxst').text(jsonSum.facts.fmax.stn);
            $('#'+sumArticle).find('.fmaxdesc').text('highest ');
            $('#'+sumArticle).find('.fmindesc').text('lowest ');
            $('#'+sumArticle).find('.fmed').text(factFormat(jsonSum.facts.fmed.v, 2));
            $('#'+sumArticle).find('.fmean').text(factFormat(jsonSum.facts.fmean.v, 2));
         };	
        return rateSummary;
	}
