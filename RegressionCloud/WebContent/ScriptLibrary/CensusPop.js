	function classPopSummary() {

		var jsonSum;
		
        function popSummary() {       
        }
    
        popSummary.getSummary = function(popDataTable, popFact, popFactLabel, popFactCol, popFactYear) {
            var wdt = popDataTable;
		    wdt.sort([
		        {column: colID(wdt, 'sumlev')}
		        ,{column: colID(wdt, 'estyear')}
		        ,{column: colID(wdt, 'estdata')}
		        ,{column: popFactCol, desc:  true}]);
		    dView = new google.visualization.DataView(wdt);
		    dView.setRows(dView.getFilteredRows([
		        {column: colID(dView, 'sumlev'), value: '010'}
		        ,{column: colID(dView, 'estdata'), value: popFact}
		    	,{column: colID(dView, 'estyear'), value: popFactYear}]));
		    
		    jsonSum = {"facts":{
						"fyear":{"y":popFactYear}
		    			,"fname":{"n":popFactLabel}
						,"fsum":{"c":0,"v":0}
						,"fmin":{"v":0,"stn":""}
						,"fmax":{"v":0,"stn":""}
						,"fmed":{"v":0}
						,"fmean":{"v":0}
						,"f20":{"c":0,"w":0,"v":0,"st":""}
						,"f33":{"c":0,"w":0,"v":0,"st":""}
						,"f50":{"c":0,"w":0,"v":0,"st":""}
						,"fmedw":{"c":0,"w":0,"v":0}
	    			   }};
		    
		    var sum = dView.getValue(0, colID(dView, 'psum'));
		    jsonSum.facts.fsum.v = dView.getValue(0, colID(dView, 'psum'));
		    jsonSum.facts.fmin.v = dView.getValue(0, colID(dView, 'pmin'));
		    jsonSum.facts.fmax.v = dView.getValue(0, colID(dView, 'pmax'));
		    jsonSum.facts.fmed.v = dView.getValue(0, colID(dView, 'pmedian'));
		    jsonSum.facts.fmean.v = dView.getValue(0, colID(dView, 'pmean'));
		
		    sumView = new google.visualization.DataView(wdt);
		    sumView.setRows(sumView.getFilteredRows([
		         {column: colID(sumView, 'sumlev'), value: '040'}
		         ,{column: colID(sumView, 'estdata'), value: popFact}
		         ,{column: colID(sumView, 'estyear'), value: popFactYear}]));
		    sumView.setColumns([colID(sumView, 'state'), colID(sumView, 'stname'), popFactCol]);
		            jsonSum.facts.fsum.c = sumView.getNumberOfRows();
		            jsonSum.facts.fmax.stn = sumView.getValue(0, 1);
		            jsonSum.facts.fmin.stn = sumView.getValue(sumView.getNumberOfRows()-1, 1);
		  
		    var cumSum = 0;
		    var states = "";
		    var statenames = "";
		    for (var i=0;i<sumView.getNumberOfRows();i++) {
		    	cumSum = cumSum + sumView.getValue(i, 2);
		    	states = states + sumView.getValue(i, 0); 
		    	statenames = statenames + sumView.getValue(i, 1); 
		    	if ((cumSum/sum)>.20){break;}
		    	states = states + ', '; 
		    	statenames = statenames + ', '; 
		    }
		    jsonSum.facts.f20.c = i+1;
		    jsonSum.facts.f20.w = cumSum/sum;
		    jsonSum.facts.f20.v = cumSum;
		    jsonSum.facts.f20.st = states;
		    
		    cumSum = 0;
		    states = "";
		    statenames = "";
		    for (var i=0;i<sumView.getNumberOfRows();i++) {
		    	cumSum = cumSum + sumView.getValue(i, 2);
		    	states = states + sumView.getValue(i, 0); 
		    	statenames = statenames + sumView.getValue(i, 1); 
		    	if ((cumSum/sum)>.33){break;}
		    	states = states + ', '; 
		    	statenames = statenames + ', '; 
		    }
		    jsonSum.facts.f33.c = i+1;
		    jsonSum.facts.f33.w = cumSum/sum;
		    jsonSum.facts.f33.v = cumSum;
		    jsonSum.facts.f33.st = states;
		
		    cumSum = 0;
		    states = "";
		    statenames = "";
		    for (var i=0;i<sumView.getNumberOfRows();i++) {
		    	cumSum = cumSum + sumView.getValue(i, 2);
		    	states = states + sumView.getValue(i, 0); 
		    	statenames = statenames + sumView.getValue(i, 1); 
		    	if ((cumSum/sum)>.50){break;}
		    	states = states + ', '; 
		    	statenames = statenames + ', '; 
		    }
		    jsonSum.facts.f50.c = i+1;
		    jsonSum.facts.f50.w = cumSum/sum;
		    jsonSum.facts.f50.v = cumSum;
		    jsonSum.facts.f50.st = states;
		
		    cumSum = 0;
		    states = "";
		    statenames = "";
		    for (var i=0;i<sumView.getNumberOfRows();i++) {
		    	cumSum = cumSum + sumView.getValue(i, 2);
		    	states = states + sumView.getValue(i, 0); 
		    	statenames = statenames + sumView.getValue(i, 1); 
		    	if (i+1 >= Math.floor(sumView.getNumberOfRows()/2)){break;}
		    	states = states + ', '; 
		    	statenames = statenames + ', '; 
		    }
		    jsonSum.facts.fmedw.c = i+1;
		    jsonSum.facts.fmedw.w = cumSum/sum;
		    jsonSum.facts.fmedw.v = cumSum;
		};

        popSummary.drawSummary = function(sumArticle) {
            $('#popTabTitle').find('.factYear').text(jsonSum.facts.fyear.y);
            $('.'+sumArticle+'_div.title').text('Observations about the Population of the United States ' + jsonSum.facts.fyear.y);
            $('#'+sumArticle).find('.factYear').text(jsonSum.facts.fyear.y);
            $('#'+sumArticle).find('.fsum').text(factFormat(jsonSum.facts.fsum.v, 2));
            $('#'+sumArticle).find('.fmin').text(factFormat(jsonSum.facts.fmin.v, 2));
            $('#'+sumArticle).find('.fminst').text(jsonSum.facts.fmin.stn);
            $('#'+sumArticle).find('.fmax').text(factFormat(jsonSum.facts.fmax.v, 2));
            $('#'+sumArticle).find('.fmaxst').text(jsonSum.facts.fmax.stn);
            $('#'+sumArticle).find('.fmed').text(factFormat(jsonSum.facts.fmed.v, 2));
            $('#'+sumArticle).find('.fmean').text(factFormat(jsonSum.facts.fmean.v, 2));
            $('#'+sumArticle).find('.f20w').text(factFormat(jsonSum.facts.f20.w*100, 2)+'%');
            $('#'+sumArticle).find('.f20v').text(factFormat(jsonSum.facts.f20.v, 2));
            $('#'+sumArticle).find('.f20c').text(jsonSum.facts.f20.c);
            $('#'+sumArticle).find('.f20').text(jsonSum.facts.f20.st);
            $('#'+sumArticle).find('.f33w').text(factFormat(jsonSum.facts.f33.w*100, 2)+'%');
            $('#'+sumArticle).find('.f33v').text(factFormat(jsonSum.facts.f33.v, 2));
            $('#'+sumArticle).find('.f33c').text(jsonSum.facts.f33.c);
            $('#'+sumArticle).find('.f33').text(jsonSum.facts.f33.st);
            $('#'+sumArticle).find('.f50w').text(factFormat(jsonSum.facts.f50.w*100, 2)+'%');
            $('#'+sumArticle).find('.f50v').text(factFormat(jsonSum.facts.f50.v, 2));
            $('#'+sumArticle).find('.f50c').text(jsonSum.facts.f50.c);
            $('#'+sumArticle).find('.f50').text(jsonSum.facts.f50.st);
            $('#'+sumArticle).find('.fmedw').text(factFormat(jsonSum.facts.fmedw.w*100, 2)+'%');
            $('#'+sumArticle).find('.fmedv').text(factFormat(jsonSum.facts.fmedw.v,2));
            $('#'+sumArticle).find('.fmedc').text(jsonSum.facts.fmedw.c);
            $('#'+sumArticle).find('.fmedw-').text(factFormat((1-jsonSum.facts.fmedw.w)*100, 2)+'%');
            $('#'+sumArticle).find('.fmedv-').text(factFormat(jsonSum.facts.fsum.v-jsonSum.facts.fmedw.v, 2));
            $('#'+sumArticle).find('.fmedc-').text(jsonSum.facts.fsum.c-jsonSum.facts.fmedw.c);
        };
        
        return popSummary;
	}
