
	function classFertChart() {
    
        var popgrowthDT;
        var popfutureDT;
        var usbirthDT;
        var fertcohortDT;
        var fertuswldDT;
        var fertrankDT;
        var fertpieDT;
           
        function fertChart() {       
        }
    
        fertChart.setPopGrowthDT = function(jsonChartData) {
        	
        	popgrowthDT = new google.visualization.DataTable(jsonChartData);

        	var formatter = new google.visualization.NumberFormat({pattern:'#,###.##'});

        	var cl = jsonChartData.rows[0].c.length;
    		for (var c=0;c<cl;c++) {
    			if (jsonChartData.cols[c].type == 'number'){
    	        	formatter.format(popgrowthDT, c);
    			}
    		}
        };

        fertChart.setPopFutureDT = function(jsonChartData) {
        	
        	popfutureDT = new google.visualization.DataTable(jsonChartData);

        	var formatter = new google.visualization.NumberFormat({pattern:'#,###.##'});

        	var cl = jsonChartData.rows[0].c.length;
    		for (var c=0;c<cl;c++) {
    			if (jsonChartData.cols[c].type == 'number'){
    	        	formatter.format(popfutureDT, c);
    			}
    		}
        };

        fertChart.setUSBirthDT = function(jsonChartData) {
        	
        	usbirthDT = new google.visualization.DataTable(jsonChartData);

        	var formatter = new google.visualization.NumberFormat({pattern:'#,###.##'});

        	var cl = jsonChartData.rows[0].c.length;
    		for (var c=0;c<cl;c++) {
    			if (jsonChartData.cols[c].type == 'number'){
    	        	formatter.format(usbirthDT, c);
    			}
    		}
        };

        fertChart.setFertCohortDT = function(jsonChartData) {
        	
        	fertcohortDT = new google.visualization.DataTable(jsonChartData);

        	var formatter = new google.visualization.NumberFormat({pattern:'#,###.##'});

        	var cl = jsonChartData.rows[0].c.length;
    		for (var c=0;c<cl;c++) {
    			if (jsonChartData.cols[c].type == 'number'){
    	        	formatter.format(fertcohortDT, c);
    			}
    		}
        };

        fertChart.setFertUSWLDDT = function(jsonChartData) {
        	
        	fertuswldDT = new google.visualization.DataTable(jsonChartData);

        	var formatter = new google.visualization.NumberFormat({pattern:'#,###.##'});

        	var cl = jsonChartData.rows[0].c.length;
    		for (var c=0;c<cl;c++) {
    			if (jsonChartData.cols[c].type == 'number'){
    	        	formatter.format(fertuswldDT, c);
    			}
    		}
        };

        fertChart.setFertRankDT = function(jsonChartData) {
        	
        	fertrankDT = new google.visualization.DataTable(jsonChartData);

        	var formatter = new google.visualization.NumberFormat({pattern:'#,###.##'});

        	var cl = jsonChartData.rows[0].c.length;
    		for (var c=0;c<cl;c++) {
    			if (jsonChartData.cols[c].type == 'number'){
    	        	formatter.format(fertrankDT, c);
    			}
    		}
        };

        fertChart.setFertPieDT = function(jsonChartData) {
        	
        	fertpieDT = new google.visualization.DataTable(jsonChartData);

        	var formatter = new google.visualization.NumberFormat({pattern:'#,###.##'});

        	var cl = jsonChartData.rows[0].c.length;
    		for (var c=0;c<cl;c++) {
    			if (jsonChartData.cols[c].type == 'number'){
    	        	formatter.format(fertpieDT, c);
    			}
    		}
        };

        fertChart.drawPopGrowth = function(distDiv){
        	
        	var hEvery = 10;
        	var fromYear = '1901';
        	var toYear = '2012';
            var lineChartOptions = {
            		title: 'United States Population Growth 20th Century'
            		,titleTextStyle: {fontSize: 20, bold: true}
                    ,hAxis: {
                            title: 'Year'
                            ,titleTextStyle: {color: 'darkblue', bold: true}
                            ,showTextEvery: hEvery
                            ,slantedText: true
                            ,slantedTextAngle: 45
                            ,textStyle: {fontSize: 11}
                            }
            		,vAxis: {
            				titleTextStyle: {color: 'darkblue', bold: true}
                            ,textStyle: {fontSize: 11}
           				    }
                    ,series: {
                    		0:{type: 'line', targetAxisIndex:0}
	            	  		,1:{type: 'line', targetAxisIndex:1}
            	  			,2:{type: 'bars', targetAxisIndex:0, color: 'darkblue'}
            	  			,2:{type: 'bars', targetAxisIndex:0, color: 'darkblue'}
	          				}
                    ,vAxes: {
                    		0:{title: "US Population"}
	                    	,1:{title: "US Population Growth Rate (Per Thousand)", titleTextStyle: {color: '#E01E00'}}
	                		}
                    ,bar: {groupWidth: 20}
                    ,backgroundColor: '#FFFFEB'
                  };

            wdt = popgrowthDT;
            wdt.sort([
                {column: colID(wdt, 'estyear')}]);
            var fertView = new google.visualization.DataView(wdt);
            fertView.setRows(fertView.getFilteredRows([
                {column: colID(fertView, 'estyear'), minValue: fromYear, maxValue: toYear}]));
            fertView.setColumns([
                                 colID(wdt, 'estyear')
                                  ,colID(fertView, 'pop')
                                 ,colID(fertView, 'popchgrate')
                                 ,{calc:popDbl, type:'number', label:"Population Doubles"}]);
            function popDbl(dataTable, rowNum){
                var yv = dataTable.getValue(rowNum, colID(wdt, 'estyear'));
                var pv = 0;
                if (yv == '1950') {pv=152271417;}
                if (yv == '2008') {pv=304093966;}
                return {v:pv, f:factFormat(pv, 0)};
            }
            var lineChart = new google.visualization.LineChart(document.getElementById(distDiv));
            lineChart.draw(fertView, lineChartOptions);
        };
    
        fertChart.drawUSBirth = function(distDiv){
        	
        	var hEvery = 1;
        	var fromYear = '1996';
        	var toYear = '2012';
            var lineChartOptions = {
            		title: 'United States Birth Rate 1996-2012'
                	,titleTextStyle: {fontSize: 20, bold: true}
                    ,hAxis: {
                            title: 'Year'
                            ,titleTextStyle: {color: 'darkblue', bold: true}
                            ,showTextEvery: hEvery
                            ,slantedText: true
                            ,slantedTextAngle: 45
                            ,textStyle: {fontSize: 11}
                            }
            		,vAxis: {
            				titleTextStyle: {color: 'darkblue', bold: true}
                            ,textStyle: {fontSize: 11}
           				    }
                    ,series: {
	            	  		0:{type: 'line', targetAxisIndex:0}
	            	  		,1:{type: 'line', targetAxisIndex:1}
	          				}
                    ,vAxes: {
	                		 0:{title: "US Births"}
	                    	,1:{title: "US Birth Rate (Per Thousand)", titleTextStyle: {color: '#E01E00'}}
	                		}
                    ,backgroundColor: '#FFFFEB'
                  };

            wdt = usbirthDT;
            wdt.sort([
                {column: colID(wdt, 'estyear')}]);
            var fertView = new google.visualization.DataView(wdt);
            fertView.setRows(fertView.getFilteredRows([
                {column: colID(fertView, 'estyear'), minValue: fromYear, maxValue: toYear}]));
            fertView.setColumns([colID(wdt, 'estyear'),colID(fertView, 'birth'),colID(fertView, 'brate')]);
            var lineChart = new google.visualization.LineChart(document.getElementById(distDiv));
            lineChart.draw(fertView, lineChartOptions);
        };

        fertChart.drawFertCohort = function(distDiv){
        	
        	var hEvery = 1;
            var colChartOptions = {
            		title: 'United States Cohort Fertility 1960, 2009'
                    ,titleTextStyle: {fontSize: 20, bold: true}
                    ,hAxis: {
                            title: 'Cohort Age Group'
                            ,titleTextStyle: {color: 'darkblue', bold: true}
                            ,showTextEvery: hEvery
                            ,slantedText: true
                            ,slantedTextAngle: 45
                            ,textStyle: {fontSize: 11}
                            }
            		,vAxis: {
            				titleTextStyle: {color: 'darkblue', bold: true}
                            ,textStyle: {fontSize: 11}
           				    }
                    ,vAxes: {
                		 0:{title: "US Births (Per 1000 Women)"}
                		}
                    ,backgroundColor: '#FFFFEB'
                  };

            wdt = fertcohortDT;
            wdt.sort([
                {column: colID(wdt, 'agegrp')}]);
            var fertView = new google.visualization.DataView(wdt);
            var colChart = new google.visualization.ColumnChart(document.getElementById(distDiv));
            colChart.draw(fertView, colChartOptions);
        };

        fertChart.drawFertUSWLD = function(distDiv){
        	
        	var hEvery = 5;
            var lineChartOptions = {
            		title: 'US, World Total Fertility 1960-2012'
                	,titleTextStyle: {fontSize: 20, bold: true}
                    ,hAxis: {
                            title: 'Year'
                            ,titleTextStyle: {color: 'darkblue', bold: true}
                            ,showTextEvery: hEvery
                            ,slantedText: true
                            ,slantedTextAngle: 45
                            ,textStyle: {fontSize: 11}
                            }
            		,vAxis: {
            				titleTextStyle: {color: 'darkblue', bold: true}
                            ,textStyle: {fontSize: 11}
           				    }
                    ,series: {
	            	  		0:{type: 'line', targetAxisIndex:0}
	            	  		,1:{type: 'line', targetAxisIndex:0}
	          				}
                    ,vAxes: {
	                		 0:{title: "Total Fertility Per Woman"}
	                		}
                    ,backgroundColor: '#FFFFEB'
                  };

            wdt = fertuswldDT;
            wdt.sort([
                {column: colID(wdt, 'estyear')}]);
            var fertView = new google.visualization.DataView(wdt);
            var lineChart = new google.visualization.LineChart(document.getElementById(distDiv));
            lineChart.draw(fertView, lineChartOptions);
        };

        fertChart.drawFertRank = function(distDiv){
         	var tabOpt = {
         			cssClassNames: {tableRow: 'chartTabRow', oddTableRow: 'chartTabRow', headerRow: 'chartHeadRow' }
			};
            wdt = fertrankDT;
            wdt.sort([
                {column: colID(wdt, 'pctl'), desc: true}
                ,{column: colID(wdt, 'rank'), desc: true}]);
            var fertView = new google.visualization.DataView(wdt);
            var tblChart = new google.visualization.Table(document.getElementById(distDiv));
            tblChart.draw(fertView, tabOpt);
        };

        fertChart.drawFertPie = function(distDiv1, distDiv2){
        	
        	var fromYear1 = '1960';
        	var toYear1 = '1960';
        	var fromYear2 = '2012';
        	var toYear2 = '2012';
            var pieChartOptions1 = {
            		title: 'World Total Fertility Replacement 1960'
                	,titleTextStyle: {fontSize: 20, bold: true}
                    ,is3D: true
                    ,backgroundColor: '#FFFFEB'
                    ,chartArea: {width: 300, left: 50}
                    ,legend: { position:'left'}
                  };
            var pieChartOptions2 = {
            		title: 'World Total Fertility Replacement 2012'
                	,titleTextStyle: {fontSize: 20, bold: true}
                    ,is3D: true
                    ,backgroundColor: '#FFFFEB'
                    ,chartArea: {width: 300, left: 25}
                    ,legend: { position:'left'}
                  };

            wdt = fertpieDT;
            wdt.sort([
                {column: colID(wdt, 'estyear')}
                ,{column: colID(wdt, 'ord')}]);
            var fertView1 = new google.visualization.DataView(wdt);
            fertView1.setRows(fertView1.getFilteredRows([
                {column: colID(fertView1, 'estyear'), minValue: fromYear1, maxValue: toYear1}]));
            fertView1.setColumns([colID(fertView1, 'cat')
                                  ,colID(fertView1, 'cntry')]);
            var pieChart1 = new google.visualization.PieChart(document.getElementById(distDiv1));
            pieChart1.draw(fertView1, pieChartOptions1);
            
            var fertView2 = new google.visualization.DataView(wdt);
            fertView2.setRows(fertView2.getFilteredRows([
                {column: colID(fertView2, 'estyear'), minValue: fromYear2, maxValue: toYear2}]));
            fertView2.setColumns([colID(fertView2, 'cat')
                                  ,colID(fertView2, 'cntry')]);
            var pieChart2 = new google.visualization.PieChart(document.getElementById(distDiv2));
            pieChart2.draw(fertView2, pieChartOptions2);
        };

       return fertChart;
    
    } //classfertChart
    
    
    