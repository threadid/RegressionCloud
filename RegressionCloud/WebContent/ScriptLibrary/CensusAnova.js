	function classAnova() {

		var anovaDataTable;
		var regrDataTable;
		
		var yFact;
		var yFactLabel;
		var yYear;
		var xFact;
		var xFactLabel;
		var xYear;

        function anova() {       
        }

        anova.setAnovaDataTable = function(jsonAnovaData) {

        	if (typeof anovaDataTable == 'undefined') {
        		anovaDataTable = new google.visualization.DataTable(jsonAnovaData);
        	} else {
        		var jyy = jsonAnovaData.rows[0].c[5].v;
        		var jxy = jsonAnovaData.rows[0].c[6].v;
        		var js = jsonAnovaData.rows[0].c[4].v;
        		var jyv = jsonAnovaData.rows[0].c[0].v;
        		var jxv = jsonAnovaData.rows[0].c[1].v;
            	var ri = anovaDataTable.getFilteredRows([{column: colID(anovaDataTable, 'yestyear'), value: jyy}
														,{column: colID(anovaDataTable, 'xestyear'), value: jxy}
														,{column: colID(anovaDataTable, 'sumlev'), value: js}
            											,{column: colID(anovaDataTable, 'y'), value: jyv}
            											,{column: colID(anovaDataTable, 'x'), value: jxv}]);
            	if (ri==0) {
                	var rl = jsonAnovaData.rows.length;
        			for (var i=0;i<rl;i++){
        				anovaDataTable.addRow(jsonAnovaData.rows[i].c);
        			}
            	}
            }

    		var snFormatter = new google.visualization.NumberFormat({pattern:'0.###E0'});
        	var dpFormatter = new google.visualization.NumberFormat({pattern:'#,###.####'});
        	var iFormatter = new google.visualization.NumberFormat({pattern:'#,###'});

        	var cl = jsonAnovaData.rows[0].c.length;
    		for (var c=0;c<cl;c++) {
    			if (jsonAnovaData.cols[c].type == 'number'){
    				switch (jsonAnovaData.cols[c].id)
    				{
    				   case "dfr": 
    					   iFormatter.format(anovaDataTable, c);
    				       break;
    				   case "dfe": 
    					   iFormatter.format(anovaDataTable, c);
    				       break;
    				   case "n": 
    					   iFormatter.format(anovaDataTable, c);
    				       break;
    				   case "f": 
    					   dpFormatter.format(anovaDataTable, c);
    				       break;
    				   case "sgnf": 
    					   dpFormatter.format(anovaDataTable, c);
    				       break;
    				   case "see": 
    					   iFormatter.format(anovaDataTable, c);
    				       break;
    				   default: 
    					   snFormatter.format(anovaDataTable, c);
    				       break;
    				}
    			}
    		}
        };

        anova.getAnovaDataTable = function() {
        	return anovaDataTable;
        }
        
        anova.setRegrDataTable = function(jsonRegrData) {

        	if (typeof regrDataTable == 'undefined') {
        		regrDataTable = new google.visualization.DataTable(jsonRegrData);
        	} else {
        		var jyy = jsonRegrData.rows[0].c[3].v;
        		var jxy = jsonRegrData.rows[0].c[4].v;
        		var js = jsonRegrData.rows[0].c[2].v;
        		var jyv = jsonRegrData.rows[0].c[0].v;
        		var jxv = jsonRegrData.rows[0].c[1].v;
            	var ri = regrDataTable.getFilteredRows([{column: colID(regrDataTable, 'yestyear'), value: jyy}
														,{column: colID(regrDataTable, 'xestyear'), value: jxy}
														,{column: colID(regrDataTable, 'sumlev'), value: js}
														,{column: colID(regrDataTable, 'yname'), value: jyv}
														,{column: colID(regrDataTable, 'xname'), value: jxv}]);
            	var rl = jsonRegrData.rows.length;
        		if (ri==0) {
        			for (var i=0;i<rl;i++){
        				regrDataTable.addRow(jsonRegrData.rows[i].c);
        			}
            	}
            }

    		var snFormatter = new google.visualization.NumberFormat({pattern:'0.###E0'});
        	var dpFormatter = new google.visualization.NumberFormat({pattern:'#,###.####'});
        	var iFormatter = new google.visualization.NumberFormat({pattern:'#,###'});

        	var cl = jsonRegrData.rows[0].c.length;
    		for (var c=0;c<cl;c++) {
    			if (jsonRegrData.cols[c].type == 'number'){
    				switch (jsonRegrData.cols[c].id)
    				{
    				   case "y": 
    					   dpFormatter.format(regrDataTable, c);
    				       break;
    				   case "x": 
    					   dpFormatter.format(regrDataTable, c);
    				       break;
    				   default: 
    					   dpFormatter.format(regrDataTable, c);
    				       break;
    				}
    			}
    		}
        };

        anova.getRegrDataTable = function() {
        	return regrDataTable;
        }
        
        anova.setYFact = function(fact, label) {
        	yFact = fact;
        	yFactLabel = label;
        }; 
           
        anova.setXFact = function(fact, label) {
        	xFact = fact;
        	xFactLabel = label;
       }; 
           
       anova.setYFactYear = function(yFactYear) {
           yYear = yFactYear.toString();
       }; 
          
       anova.setXFactYear = function(xFactYear) {
           xYear = xFactYear.toString();
       }; 
          
        anova.getYFactYear = function() {
            return yYear;
        }; 
           
        anova.getXFactYear = function() {
            return xYear;
        }; 
           
        anova.hasYearYX = function(yFactYear, xFactYear, y, x) {
        	if (typeof anovaDataTable == 'undefined') {return false;}
        	if (typeof regrDataTable == 'undefined') {return false;}
        	var ria = anovaDataTable.getFilteredRows([{column: colID(anovaDataTable, 'yestyear'), value: yFactYear.toString()}
        											,{column: colID(anovaDataTable, 'xestyear'), value: xFactYear.toString()}
													,{column: colID(anovaDataTable, 'sumlev'), value: '040'}
													,{column: colID(anovaDataTable, 'y'), value: y}
        											,{column: colID(anovaDataTable, 'x'), value: x}]);
        	var rir = regrDataTable.getFilteredRows([{column: colID(regrDataTable, 'yestyear'), value: yFactYear.toString()}
        											,{column: colID(regrDataTable, 'xestyear'), value: xFactYear.toString()}
        											,{column: colID(regrDataTable, 'sumlev'), value: '040'}
        											,{column: colID(regrDataTable, 'yname'), value: y}
        											,{column: colID(regrDataTable, 'xname'), value: x}]);
        	if (ria==0 || rir==0){return false;}
        	return true;
        };
        
        anova.drawAnova = function(sumArticle) {
            $('#anovaTabTitle').text('Analysis Of Variance for ' + yFactLabel + ' ' + yYear + ' and  ' + xFactLabel + ' '+ xYear);
            $('.'+sumArticle+'_div.title').text('Regression Statistics for ' + yFactLabel + ' ' + yYear + ' and ' + xFactLabel + ' '+ xYear);
		    dView = new google.visualization.DataView(anovaDataTable);
		    dView.setRows(dView.getFilteredRows([
		        {column: colID(dView, 'y'), value: yFact}
		        ,{column: colID(dView, 'x'), value: xFact}
		        ,{column: colID(dView, 'sumlev'), value: '040'}
		    	,{column: colID(dView, 'yestyear'), value: yYear}
		    	,{column: colID(dView, 'xestyear'), value: xYear}]));
            $('#'+sumArticle).find('.yvar').text(yFactLabel);
            $('#'+sumArticle).find('.xvar').text(xFactLabel);
            $('#'+sumArticle).find('.rsq').text(dView.getFormattedValue(0, colID(dView, 'rsq')));
            $('#'+sumArticle).find('.arsq').text(dView.getFormattedValue(0, colID(dView, 'arsq')));
            $('#'+sumArticle).find('.see').text(dView.getFormattedValue(0, colID(dView, 'see')));
//            $('#'+sumArticle).find('.dft').text(dView.getFormattedValue(0, colID(dView, 'n')));
            $('#'+sumArticle).find('.n').text(dView.getFormattedValue(0, colID(dView, 'n')));
            $('#'+sumArticle).find('.dfr').text(dView.getFormattedValue(0, colID(dView, 'dfr')));
            $('#'+sumArticle).find('.dfe').text(dView.getFormattedValue(0, colID(dView, 'dfe')));
            $('#'+sumArticle).find('.rss').text(dView.getFormattedValue(0, colID(dView, 'rss')));
            $('#'+sumArticle).find('.sse').text(dView.getFormattedValue(0, colID(dView, 'sse')));
//            $('#'+sumArticle).find('.tss').text(dView.getFormattedValue(0, colID(dView, 'tss')));
            $('#'+sumArticle).find('.mrss').text(dView.getFormattedValue(0, colID(dView, 'mrss')));
            $('#'+sumArticle).find('.msse').text(dView.getFormattedValue(0, colID(dView, 'msse')));
            $('#'+sumArticle).find('.f').text(dView.getFormattedValue(0, colID(dView, 'f')));
            $('#'+sumArticle).find('.p').text(dView.getFormattedValue(0, colID(dView, 'p')));
//            $('#'+sumArticle).find('.sgnf').text(dView.getFormattedValue(0, colID(dView, 'sgnf')));
            $('#'+sumArticle).find('.sgnf').text('');
            $('#'+sumArticle).find('.a').text(dView.getFormattedValue(0, colID(dView, 'a')));
            $('#'+sumArticle).find('.ase').text(dView.getFormattedValue(0, colID(dView, 'ase')));
            $('#'+sumArticle).find('.atv').text(dView.getFormattedValue(0, colID(dView, 'atv')));
            $('#'+sumArticle).find('.ap').text(dView.getFormattedValue(0, colID(dView, 'ap')));
            $('#'+sumArticle).find('.b').text(dView.getFormattedValue(0, colID(dView, 'b')));
            $('#'+sumArticle).find('.bse').text(dView.getFormattedValue(0, colID(dView, 'bse')));
            $('#'+sumArticle).find('.btv').text(dView.getFormattedValue(0, colID(dView, 'btv')));
            $('#'+sumArticle).find('.bp').text(dView.getFormattedValue(0, colID(dView, 'bp')));
        };

        anova.drawScatter = function(scatterDiv){
            $('.'+scatterDiv+'.title').text('Fitted Regression for ' + yFactLabel + ' ' + yYear + ' and  ' + xFactLabel + ' '+ xYear);
            
            var scatterChartOptions = {
                    hAxis: {
                            title: xFactLabel
                            ,titleTextStyle: {color: 'darkblue', bold: true}
                            ,textStyle: {fontSize: 11}
                            }
                    ,vAxis: {
                            title: yFactLabel
                            ,titleTextStyle: {color: 'darkblue', bold: true}
                            ,textStyle: {fontSize: 11}
                            }
                    ,trendlines: { 0: {} } 
                    ,backgroundColor: '#FFFFEB'
                    };
    
            var wdt = regrDataTable;
            wdt.sort([
                {column: colID(wdt, 'yname')}
                ,{column: colID(wdt, 'xname')}
                ,{column: colID(wdt, 'sumlev')}
                ,{column: colID(wdt, 'yestyear')}
                ,{column: colID(wdt, 'xestyear')}]);
            var colView = new google.visualization.DataView(wdt);
            colView.setRows(colView.getFilteredRows([
                {column: colID(colView, 'yname'), value: yFact}
                ,{column: colID(colView, 'xname'), value: xFact}
                ,{column: colID(colView, 'sumlev'), value: '040'}
            	,{column: colID(colView, 'yestyear'), value: yYear}
            	,{column: colID(colView, 'xestyear'), value: xYear}]));
            colView.setColumns([  colID(colView, 'x')
                                , colID(colView, 'y')]);
            var scatterChart = new google.visualization.ScatterChart(document.getElementById(scatterDiv));
            scatterChart.draw(colView, scatterChartOptions);
        };

        anova.drawTabData = function(tabDiv) {
            $('.'+tabDiv+'.title').text('Tabular Data for Regression ' + yFactLabel + ' ' + yYear + ' and ' + xFactLabel + ' ' + xYear);
        	var tabOpt = {
    				width: '100%'
    				,allowHtml: true
    				,cssClassNames: {
    					tableRow: 'chartTabRow'
    					,oddTableRow: 'chartTabRow'
    					,headerRow: 'chartHeadRow'
        				,tableCell: 'chartTabRow'
            			,headerCell: 'chartHeadRow'
    					}
    				};
            var wdt = regrDataTable;
            wdt.sort([
                {column: colID(wdt, 'yname')}
                ,{column: colID(wdt, 'xname')}
                ,{column: colID(wdt, 'sumlev')}
                ,{column: colID(wdt, 'yestyear')}
                ,{column: colID(wdt, 'xestyear')}]);
   
            var tblView = new google.visualization.DataView(wdt);
            tblView.setRows(tblView.getFilteredRows([
                 {column: colID(tblView, 'yname'), value: yFact}
                 ,{column: colID(tblView, 'xname'), value: xFact}
                 ,{column: colID(tblView, 'sumlev'), value: '040'}
              	,{column: colID(tblView, 'yestyear'), value: yYear}
             	,{column: colID(tblView, 'xestyear'), value: xYear}]));
            tblView.setColumns([    colID(tblView, 'stname')
                                  , colID(tblView, 'y')
                                  , colID(tblView, 'x')]);
            var tblChart = new google.visualization.Table(document.getElementById(tabDiv));
            tblChart.draw(tblView, tabOpt);
        };

        return anova;
	}
