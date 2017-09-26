
    function USAMapMenter_evt(evt) 
    {        
    	var ref = evt.data.sel;
    	var st = evt.data.st;
	
        var dl = $(ref).find('.'+st+'datalabel').text();
        var dv = $(ref).find('.'+st+'datavalue').text();
        var mx = parseFloat($(ref).find('.'+st+'text').attr('x'))+15;
        var my = parseFloat($(ref).find('.'+st+'text').attr('y'))-15;
        var mxs = mx + 2; 
        var mys = my + 2;
        var dt = 'm '+mx+','+my+' l 25,-25 h -85,0 v 0,-70 h 250,0 v 0,70 h -135,0  l -55,25';
        var ds = 'm '+mxs+','+mys+' l 25,-25 h -85,0 v 0,-70 h 250,0 v 0,70 h -135,0  l -55,25';
        $(ref).find('.shadowpath').attr('d', ds);
        $(ref).find('.toolpath').attr('d', dt);
        $(ref).find('.tooltiptext').attr('x', mx-50);
        $(ref).find('.tooltiptext').attr('y', my-75);
        $(ref).find('.tooltipstate').attr('x', mx-50);
        $(ref).find('.tooltipstate').attr('y', my-75);
        $(ref).find('.tooltiplabel').attr('x', mx-50);
        $(ref).find('.tooltiplabel').attr('y', my-55);
        $(ref).find('.tooltipvalue').attr('x', mx-50);
        $(ref).find('.tooltipvalue').attr('y', my-35);
        $(ref).find('.tooltipstate').text(st);
        $(ref).find('.tooltiplabel').text(dl);
        $(ref).find('.tooltipvalue').text(dv);
        var toolTip = $(ref).find('.tooltip').detach();
        toolTip.appendTo( $(ref).find('.'+st+'state') );
        var svgState =  $(ref).find('.'+st+'state').detach();
        svgState.appendTo( $(ref).find('.USAcountry') );
        $(ref).find('.tooltip').attr('display', '');
    }

    function USAMapMleave_evt(evt) 
    {
    	var ref = evt.data.sel;
        $(ref).find('.tooltip').attr('display', 'none');
        var toolTip = $(ref).find('.tooltip').detach();
        toolTip.appendTo( $(ref).find('.USAcountry') );
     }
            
	function classDrawVisual() {
    
        var popDataTable;
        var popFactYear;
        var popFact;
        var popFactCol;
        var popFactLabel;
        var svgMap;
           
        function drawVisual() {       
        }
    
        drawVisual.setPopDataTable = function(jsonChartData) {

        	var elapse = new Date();
        	
        	if (typeof popDataTable == 'undefined') {
        		popDataTable = new google.visualization.DataTable(jsonChartData);
        	} else {
        		var jsonYear = jsonChartData.rows[0].c[2].v;
        		if (!this.hasYear(jsonYear)) {
        			for (var i=0;i<jsonChartData.rows.length;i++){
        				var rowArray =  new Array();
        				rowArray = jsonChartData.rows[i].c;
            			popDataTable.addRow(jsonChartData.rows[i].c);
        			}
            	}
            }

        	var formatter = new google.visualization.NumberFormat({pattern:'#,###.##'});

        	var cl = jsonChartData.rows[0].c.length;
    		for (var c=0;c<cl;c++) {
    			if (jsonChartData.cols[c].type == 'number'){
    	        	formatter.format(popDataTable, c);
    			}
    		}
        };

        drawVisual.getPopDataTable = function() {
            return popDataTable;
        };
        
        drawVisual.setPopFact = function(fact) {
        	popFact = fact;
        	if (typeof popDataTable == 'object') {
                popFactCol = colID(popDataTable, 'estval');
            }
        	popFactLabel = objCensusYears.cLabel(popFact);
        }; 
           
        drawVisual.getPopFact = function() {
            return popFact;
        };
        
        drawVisual.getPopFactCol = function() {
            return popFactCol;
        };
        
       drawVisual.getPopFactLabel = function() {
            return popFactLabel;
        };
        
        drawVisual.setPopFactYear = function(factYear) {
            popFactYear = factYear.toString();
        }; 
           
        drawVisual.getPopFactYear = function() {
            return popFactYear;
        };
        
        drawVisual.inLineSVG = function(mapDiv, svgXMLObject) {
        	svgMap = svgXMLObject;
        };

        drawVisual.hasYear = function(newYear) {
        	if (typeof popDataTable == 'undefined') {return false;}
        	var rowIdx = popDataTable.getFilteredRows([{column: colID(popDataTable, 'estyear'), value: newYear.toString()}]);
        	if (rowIdx==0){return false;}
        	return true;
        };

        drawVisual.PaintMapUS = function (mapDiv) {          
            $('.'+mapDiv+'.title').text('Map of United States - ' + popFactLabel + ' by State ' + popFactYear);
            $('#' + mapDiv).append(svgMap.documentElement);
            var wdt = popDataTable;
            wdt.sort([
                {column: colID(wdt, 'sumlev')}
                ,{column: colID(wdt, 'estyear')}
                ,{column: colID(wdt, 'estdata')}
                ,{column: popFactCol, desc:  true}]);
            wdt.setColumnLabel(popFactCol, popFactLabel);
            var mapView = new google.visualization.DataView(wdt);
            mapView.setRows(mapView.getFilteredRows([
                {column: colID(mapView, 'sumlev'), value: '040'}
                ,{column: colID(mapView, 'estdata'), value: popFact}
            	,{column: colID(mapView, 'estyear'), value: popFactYear}]));
            mapView.setColumns([
                colID(mapView, 'state')
                , popFactCol
                , colID(mapView, 'percentile')
                ,{calc:calcABSVal, type:'number', label:'Absolute Value'+popFactLabel}]);
	            function calcABSVal(dataTable, rowNum){
	            	return Math.abs(dataTable.getValue(rowNum, popFactCol)); 
	            }
            var colordt = mapView.toDataTable();
            colordt.sort([{column: 3, asc:  true}]);
            mapView = new google.visualization.DataView(colordt);
            mapView.setColumns([0,1,2,{calc:colorP, type:'number'}]);
	            function colorP(dataTable, rowNum){
	            	return Math.abs(rowNum/dataTable.getNumberOfRows())*100;
	            	}
            var estCount = mapView.getNumberOfRows();
            if (estCount > 0) {
                for (var i=0; i<estCount; i++){
                    decile = Math.floor(mapView.getValue(i, 3)/10);
                    decile = (decile>10)?10:decile;
                    if (mapView.getValue(i, 1)<0){
                        $('#' + mapDiv + ' svg').find('.'+mapView.getValue(i, 0)+'state').css("fill", colorGrade.gradient.red[decile]);
                    } else {
                        $('#' + mapDiv + ' svg').find('.'+mapView.getValue(i, 0)+'state').css("fill", colorGrade.gradient.green[decile]);
                    }
        
                    $('#' + mapDiv + ' svg').find('.'+mapView.getValue(i, 0)+'datalabel').text(mapView.getColumnLabel(1));
                    var dValue = mapView.getFormattedValue(i, 1);
                    $('#' + mapDiv + ' svg').find('.'+mapView.getValue(i, 0)+'datavalue').text(dValue);
        
                    var ref = '#' + mapDiv + ' svg';
                    $('#' + mapDiv + ' svg').find('.'+mapView.getValue(i, 0)+'state')
                    	.on("mouseenter",{sel:ref, st:mapView.getValue(i, 0)},USAMapMenter_evt);
                    
                    $('#' + mapDiv + ' svg').find('.'+mapView.getValue(i, 0)+'state')
                	.on("mouseleave",{sel:ref},USAMapMleave_evt);
                }
            }
        };

        drawVisual.drawDistChart = function(distDiv){
            $('.'+distDiv+'.title').text(popFactLabel + ' by State ' + popFactYear);
            var colChartOptions = {
                    hAxis: {
                            title: 'States'
                            ,titleTextStyle: {color: 'darkblue', bold: true}
                            ,showTextEvery: 1
                            ,slantedText: true
                            ,slantedTextAngle: 90
                            ,textStyle: {fontSize: 11}
                            }
            		,vAxis: {
            				titleTextStyle: {color: 'darkblue', bold: true}
                            ,textStyle: {fontSize: 11}
           				    }
                    ,vAxes: {
                		 0:{title: popFactLabel}
                		}
                    ,backgroundColor: '#FFFFEB'
                  };
    
            wdt = popDataTable;
            wdt.sort([
                {column: colID(wdt, 'sumlev')}
                ,{column: colID(wdt, 'estyear')}
                ,{column: colID(wdt, 'estdata')}
                ,{column: popFactCol, asc:  true}]);
            wdt.setColumnLabel(popFactCol, popFactLabel);
            var colView = new google.visualization.DataView(wdt);
            colView.setRows(colView.getFilteredRows([
                {column: colID(colView, 'sumlev'), value: '040'}
                ,{column: colID(colView, 'estdata'), value: popFact}
            	,{column: colID(colView, 'estyear'), value: popFactYear}]));
            colView.setColumns([
                                colID(colView, 'state')
                                , popFactCol]);
            var colChart = new google.visualization.ColumnChart(document.getElementById(distDiv));
            colChart.draw(colView, colChartOptions);
        };
    
        drawVisual.drawCombChart = function(distDiv){
            $('.'+distDiv+'.title').text(popFactLabel + ' by State and Cumulative ' + popFactYear);
            var comboChartOptions = {
                    hAxis: {
                            title: 'States'
                            ,titleTextStyle: {color: 'darkblue', bold: true}
                            ,showTextEvery: 1
                            ,slantedText: true
                            ,slantedTextAngle: 90
                            ,textStyle: {fontSize: 11}
                            }
            		,vAxis: {
            				titleTextStyle: {color: 'darkblue', bold: true}
                            ,textStyle: {fontSize: 11}
           				    }
                    ,series: {
                	  		0:{type: 'bars', targetAxisIndex:0}
                	  		,1:{type: 'line', targetAxisIndex:1}
              				}
                    ,vAxes: {
                    		0:{title: popFactLabel}
                    		,1:{title:'Cumulative '+popFactLabel, titleTextStyle: {color: '#E01E00'}}
                    		}
                    ,backgroundColor: '#FFFFEB'
                  };
    
            wdt = popDataTable;
            wdt.sort([
                {column: colID(wdt, 'sumlev')}
                ,{column: colID(wdt, 'estyear')}
                ,{column: colID(wdt, 'estdata')}
                ,{column: popFactCol, asc:  true}]);
            wdt.setColumnLabel(popFactCol, popFactLabel);
            var colView = new google.visualization.DataView(wdt);
            colView.setRows(colView.getFilteredRows([
                {column: colID(colView, 'sumlev'), value: '040'}
                ,{column: colID(colView, 'estdata'), value: popFact}
            	,{column: colID(colView, 'estyear'), value: popFactYear}]));
            colView.setColumns([
                                colID(colView, 'state')
                                , popFactCol
                                , {calc:sumCumVal, type:'number', label:'Cumulative '+popFactLabel}]);
            	var cumVal = 0;
                function sumCumVal(dataTable, rowNum){
                    cumVal = cumVal + dataTable.getValue(rowNum, popFactCol);
                    return {v:cumVal, f:factFormat(cumVal, 2)};
                }
            var comboChart = new google.visualization.ComboChart(document.getElementById(distDiv));
            comboChart.draw(colView, comboChartOptions);
        };
    
        drawVisual.drawHistogram = function(histDiv, intervals) {       
          $('.'+histDiv+'.title').text('Distribution of ' + popFactLabel + ' '  + popFactYear + ' - Paramters for ' + popFactLabel + ' '  + popFactYear);
          var colChartOptions = {
        		  chartArea: {
        			  	top:30
        			  	,left:30
        		  }
        		  ,vAxis: {
                        title: 'Intervals'
                        ,titleTextStyle: {color: 'darkblue', bold: true}
                        ,showTextEvery: 1
                        ,textPosition: 'none'
                        ,textStyle: {}
                        }
                  ,hAxis: {
                        title: 'States'
                        ,titleTextStyle: {color: 'darkblue', bold: true}
                        ,textStyle: {fontSize: 11}
                        ,slantedText: true
                        ,slantedTextAngle: 90
                            }
                  ,backgroundColor: '#FFFFEB'
                };

                var jsonHist = {
                  "cols":
                      [
                      {"id":"","label":"Interval","pattern":"","type":"string"},
                      {"id":"","label":"States","pattern":"","type":"number"},
                      {"id":"","label":"IntRangeLow","pattern":"","type":"number"},
                      {"id":"","label":"IntRangeHigh","pattern":"","type":"number"}
                      ]
                      ,
                  "rows":
                      [
                      ]
                      ,
                      "p":null
                      };

            for (var i=0;i<intervals;i++) {
                jsonHist.rows.push(                       
                            {"c":
                            [
                            {"v":"","f":null},
                            {"v":0,"f":null},
                            {"v":0,"f":null},
                            {"v":0,"f":null}
                            ]
                            });
                }
          
            var wdt = popDataTable;
            wdt.sort([
                  {column: colID(wdt, 'sumlev')}
                  ,{column: colID(wdt, 'estyear')}
                  ,{column: colID(wdt, 'estdata')}
                  ,{column: popFactCol, desc:  true}]);
            colView = new google.visualization.DataView(wdt);
            colView.setRows(colView.getFilteredRows([
                    {column: colID(colView, 'sumlev'), value: '010'}
                    ,{column: colID(colView, 'estdata'), value: popFact}
            		,{column: colID(colView, 'estyear'), value: popFactYear}]));
            
            var intRange = colView.getValue(0, colID(colView, 'prange')) / intervals;
            var intLow = colView.getValue(0, colID(colView, 'pmin'));
            var intHigh = colView.getValue(0, colID(colView, 'pmin')) + intRange;
    
            for (var i=0;i<intervals;i++) {
                jsonHist.rows[i].c[0].v = "     " + factFormat(intLow, 2) + " to " + factFormat(intHigh, 2);
                jsonHist.rows[i].c[2].v = intLow;
                jsonHist.rows[i].c[3].v = intHigh;
                intLow = intHigh+.0001;
                intHigh = intHigh+intRange;
            }
            colView = new google.visualization.DataView(wdt);
            colView.setRows(colView.getFilteredRows([
                {column: colID(wdt, 'sumlev'), value: '040'}
                ,{column: colID(wdt, 'estdata'), value: popFact}
            	,{column: colID(wdt, 'estyear'), value: popFactYear}]));
            colView.setColumns([popFactCol]);
    
            for (var i=0;i<colView.getNumberOfRows();i++) {
                for (var ii=0;ii<intervals;ii++) {
                    if (jsonHist.rows[ii].c[2].v <= colView.getValue(i, 0)) {
                        if (colView.getValue(i, 0) <= jsonHist.rows[ii].c[3].v) {
                            jsonHist.rows[ii].c[1].v = jsonHist.rows[ii].c[1].v + 1;
                            break;
                        }
                    }
                }
            }
            var colChart = new google.visualization.BarChart(document.getElementById(histDiv));
            wdt = new google.visualization.DataTable(jsonHist);
            var colView = new google.visualization.DataView(wdt);
            colView.setRows(colView.getFilteredRows([{column: 1, minValue: 1}]));
            colView.setColumns([0, 1]);
            colChart.draw(colView, colChartOptions);
        };
    
        drawVisual.drawParamTab = function(paramDiv){
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
            
            var jsonPopParams = {
              "cols":
                  [
                  {"id":"","label":"Parameter(N)","pattern":"","type":"string"},
                  {"id":"","label":"Value","pattern":"","type":"number"}
                  ]
                  ,
                  "rows":
                  [
                    {"c":
                    [
                    {"v":"N","f":null},
                    {"v":0,"f":null}
                    ]
                    },
                    {"c":
                    [
                    {"v":"Sum","f":null},
                    {"v":0,"f":null}
                    ]
                    },
                    {"c":
                    [
                    {"v":"Min","f":null},
                    {"v":0,"f":null}
                    ]
                    },
                    {"c":
                    [
                    {"v":"Max","f":null},
                    {"v":0,"f":null}
                    ]
                    },
                    {"c":
                    [
                    {"v":"Range","f":null},
                    {"v":0,"f":null}
                    ]
                    },
                   {"c":
                    [
                    {"v":"Mean","f":null},
                    {"v":0,"f":null}
                    ]
                    },
                   {"c":
                    [
                    {"v":"Median","f":null},
                    {"v":0,"f":null}
                    ]
                    },
                   {"c":
                    [
                    {"v":"Variance","f":null},
                    {"v":0,"f":null}
                    ]
                    },
                   {"c":
                    [
                    {"v":"Standard Dev","f":null},
                    {"v":0,"f":null}
                    ]
                    },
                   {"c":
                    [
                    {"v":"Skewness","f":null},
                    {"v":0,"f":null}
                    ]
                    },
                   {"c":
                    [
                    {"v":"Kurtosis","f":null},
                    {"v":0,"f":null}
                    ]
                    }]
                  ,
                  "p":null
                  };
        
            jsonPopParams.rows[0].c[1].f = dView.getFormattedValue(0, colID(dView, 'pn'));
            jsonPopParams.rows[1].c[1].f = dView.getFormattedValue(0, colID(dView, 'psum'));
            jsonPopParams.rows[2].c[1].f = dView.getFormattedValue(0, colID(dView, 'pmin'));
            jsonPopParams.rows[3].c[1].f = dView.getFormattedValue(0, colID(dView, 'pmax'));
            jsonPopParams.rows[4].c[1].f = dView.getFormattedValue(0, colID(dView, 'prange'));
            jsonPopParams.rows[5].c[1].f = dView.getFormattedValue(0, colID(dView, 'pmean'));
            jsonPopParams.rows[6].c[1].f = dView.getFormattedValue(0, colID(dView, 'pmedian'));
            jsonPopParams.rows[7].c[1].f = dView.getFormattedValue(0, colID(dView, 'pvariance'));
            jsonPopParams.rows[8].c[1].f = dView.getFormattedValue(0, colID(dView, 'pstndev'));
            jsonPopParams.rows[9].c[1].f = dView.getFormattedValue(0, colID(dView, 'pskew'));
            jsonPopParams.rows[10].c[1].f = dView.getFormattedValue(0, colID(dView, 'pkurt'));
                
            var tblChart = new google.visualization.Table(document.getElementById(paramDiv));
            wdt = new google.visualization.DataTable(jsonPopParams);
            var tblView = new google.visualization.DataView(wdt);
            tblChart.draw(tblView, tabOpt);
        };
    
        drawVisual.drawTabData = function(tabDiv) {
            $('.'+tabDiv+'.title').text('Tabular Data for ' + popFactLabel + ' By State ' + popFactYear);
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
            var wdt = popDataTable;
            wdt.sort([
              {column: colID(wdt, 'sumlev')}
              ,{column: colID(wdt, 'estyear')}
              ,{column: colID(wdt, 'estdata')}
              ,{column: popFactCol, desc:  true}]);
            wdt.setColumnLabel(popFactCol, popFactLabel);
            var tblView = new google.visualization.DataView(wdt);
            tblView.setRows(tblView.getFilteredRows([
                {column: colID(wdt, 'sumlev'), value: '040'}
                ,{column: colID(wdt, 'estdata'), value: popFact}
            	,{column: colID(wdt, 'estyear'), value: popFactYear}]));
            tblView.setColumns([colID(wdt, 'stname'), popFactCol, colID(wdt, 'percentile')]);
            var tblChart = new google.visualization.Table(document.getElementById(tabDiv));
            tblChart.draw(tblView, tabOpt);
        };

        return drawVisual;
    
    } //classDrawVisual
    
    
    