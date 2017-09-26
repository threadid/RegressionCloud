	function classDashBoard() {

		var jsonSum;
		
        function dashBoard() {       
        }
    
        dashBoard.getDashBoard = function(popDataTable, popFact, popFactLabel, popFactCol, popFactYear) {
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
		    
		    chartJson = {"cols":[{"id":"","label":"","type":"string"},{"id":"","label":"","type":"number"}],"rows":[{"c":[{"v":""},{"v":0.00}]},],"p":null};
		    
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

		dashBoard.drawDashBoard = function(sumArticle) {
			
            $('#'+sumArticle).find('.factYear').text(jsonSum.facts.fyear.y);
            $('#'+sumArticle).find('.fsum').text(factFormat(jsonSum.facts.fsum.v, 2));
			
            chartJson = {"cols":[],"rows":[],"p":null};
		    colJson = {"id":"y","label":"Period","type":"string"};
		    chartJson.cols.push(colJson);
		    colJson = {"id":"1","label":"Most Populous State: "+jsonSum.facts.fmax.stn+", "+factFormat(jsonSum.facts.fmax.v, 0),"type":"number"};
		    chartJson.cols.push(colJson);
		    colJson = {"id":"2","label":"Least Populous State: "+jsonSum.facts.fmin.stn+", "+factFormat(jsonSum.facts.fmin.v, 0),"type":"number"};
		    chartJson.cols.push(colJson);
		    colJson = {"id":"3","label":"Mean Population "+factFormat(Math.floor(jsonSum.facts.fmean.v), 0),"type":"number"};
		    chartJson.cols.push(colJson);
		    colJson = {"id":"4","label":"Median Population "+factFormat(jsonSum.facts.fmed.v, 0),"type":"number"};
		    chartJson.cols.push(colJson);
		    rowJson = {"c":[{"v":jsonSum.facts.fyear.y+" US Population"},{"v":jsonSum.facts.fmax.v},{"v":jsonSum.facts.fmin.v},{"v":Math.floor(jsonSum.facts.fmean.v)},{"v":jsonSum.facts.fmed.v}]};
		    chartJson.rows.push(rowJson);
		    barDataTable = new google.visualization.DataTable(chartJson);

		    var formatter = new google.visualization.NumberFormat({pattern:'#,###.##'});
        	var cl = chartJson.rows[0].c.length;
    		for (var c=0;c<cl;c++) {
    			if (chartJson.cols[c].type == 'number'){
    	        	formatter.format(barDataTable, c);
    			}
    		}

	        var barOptions = {
	        		  chartArea: {
	        			  	top:0
	        			  	,left:50
	        			  	,right:0
	        			  	,height:300
	        			  	,width:300
	        		  }
	        		  ,legend: {
	        			  	position: "right"
	        				,alignment: "center"
	        				,textStyle: {
	        					color: 'black', bold: true, fontSize: 16
	        				}
	        		  }
	        		  ,vAxis: {
	                        title: ''
	                        ,titleTextStyle: {color: 'darkblue', bold: true}
	                        ,showTextEvery: 1
	                        ,textPosition: 'none'
	                        ,textStyle: {}
	                        ,baselineColor: '#FFFFEB'
		                    ,gridlines: {color: '#FFFFEB'}	
			                ,minorGridlines: {color: '#FFFFEB'}	
	                        }
	                  ,hAxis: {
	                        title: ''
	                        ,titleTextStyle: {color: 'darkblue', bold: true}
	                        ,textStyle: {fontSize: 11}
	                        ,textPosition: 'none'
	                        ,slantedText: true
	                        ,slantedTextAngle: 90
	                        ,baselineColor: '#FFFFEB'
	                        ,gridlines: {color: '#FFFFEB'}
	                        ,minorGridlines: {color: '#FFFFEB'}
                            }
	                  ,backgroundColor: '#FFFFEB'
	                };
            var bar = new google.visualization.BarChart(document.getElementById(sumArticle + "_bar"));
            bar.draw(barDataTable, barOptions);
			
			
            piePop(jsonSum.facts.f20.st, jsonSum.facts.f20.c, jsonSum.facts.f20.v, jsonSum.facts.fsum.v, factFormat(jsonSum.facts.f20.w*100, 2) + '% of the US population resides in the top ' + jsonSum.facts.f20.c + ' States', sumArticle+'_20');
            piePop(jsonSum.facts.f33.st, jsonSum.facts.f33.c, jsonSum.facts.f33.v, jsonSum.facts.fsum.v, factFormat(jsonSum.facts.f33.w*100, 2) + '% of the US population resides in the top ' + jsonSum.facts.f33.c + ' States', sumArticle+'_30');
            piePop(jsonSum.facts.f50.st, jsonSum.facts.f50.c, jsonSum.facts.f50.v, jsonSum.facts.fsum.v, factFormat(jsonSum.facts.f50.w*100, 2) + '% of the US population resides in the top ' + jsonSum.facts.f50.c + ' States', sumArticle+'_50');
            piePop('Top 25 States (above median)', jsonSum.facts.fmedw.c, jsonSum.facts.fmedw.v, jsonSum.facts.fsum.v, factFormat(jsonSum.facts.fmedw.w*100, 2) + '% of the US population resides in the top ' + jsonSum.facts.fmedw.c + ' States', sumArticle+'_med');
            
            function piePop(st, c, v, us, t, cont) {
            	var cr = 51-c;
	            chartJson = {"cols":[],"rows":[],"p":null};
			    colJson = {"id":"s","label":"States","type":"string"};
			    chartJson.cols.push(colJson);
			    colJson = {"id":"w","label":"Percent","type":"number"};
			    chartJson.cols.push(colJson);
			    rowJson = {"c":[{"v":st},{"v":v}]};
			    chartJson.rows.push(rowJson);
			    rowJson = {"c":[{"v":"Remaining " + cr + " US States"},{"v":us-v}]};
			    chartJson.rows.push(rowJson);
			    pieDataTable = new google.visualization.DataTable(chartJson);

			    var formatter = new google.visualization.NumberFormat({pattern:'#,###.##'});
	        	var cl = chartJson.rows[0].c.length;
	    		for (var c=0;c<cl;c++) {
	    			if (chartJson.cols[c].type == 'number'){
	    	        	formatter.format(pieDataTable, c);
	    			}
	    		}

		        var pieOptions = {
		                title: t
		                ,titleTextStyle: {color: 'black', bold: true, fontSize: 16}
		                ,is3D: true
		                ,colors:['#FF2929','#334BFF']
		                ,backgroundColor: '#FFFFEB'
		              };
	            var pie = new google.visualization.PieChart(document.getElementById(cont));
	            pie.draw(pieDataTable, pieOptions);
            }
			
        };
        
        return dashBoard;
	}
