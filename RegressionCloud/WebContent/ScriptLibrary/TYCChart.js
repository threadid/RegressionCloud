/**
 * 
 */

function classTreasuryYieldCurveChart() {

// Class Level Variables
	var treasBusDay;
	var treasWeek;
	var treasChart;
	var treasLineView;
	var treasChartOpts;
	var treasDashBoard;
	var treasRangeSlider;
	var treasLineChart;
	var treasDataView;
	var eventHndl_TLC_Ready;
	var eventHndl_TLC_Select;
	var eventHndl_TRS_StateChange;
	var eventHndl_CLC_Ready;
	
	var curveDataTable;
	var curveLineChart;
	var curveOptions;
	var isAnimHalt;
	var DBCallStack = new Array();
	var isTreasLineChartReady=true;

	
	function TYCChart(){};
	
	TYCChart.initReadyEvent = function (event){
		isTreasLineChartReady=true;
        var eDt = treasLineChart.getDataTable().getNumberOfRows();
        document.getElementById("trsSDtLab").innerHTML=$.datepicker.formatDate('MM dd, yy', treasLineChart.getDataTable().getValue(0, colID(treasLineView, 'ratedate')));
        document.getElementById("trsEDtLab").innerHTML=$.datepicker.formatDate('MM dd, yy', treasLineChart.getDataTable().getValue(eDt-1, colID(treasLineView, 'ratedate')));
        var chrtSelect = [{row:1, column:null}];
        chrtSelect[0].row = treasLineChart.getDataTable().getNumberOfRows()-1;
        treasLineChart.getChart().setSelection(chrtSelect);
//        google.visualization.events.trigger(treasLineChart, 'select', null);
		var jsonYCData = TYCChart.getYCJSON();
        var ri = treasLineChart.getDataTable().getNumberOfRows()-1;
		for (var gvi = 1; gvi<12; gvi++){
   			jsonYCData.rows[gvi-1].c[1].v = treasLineChart.getDataTable().getValue(ri, gvi);
   		}
   		var ycChartTitle = "Yield Curve " + $.datepicker.formatDate('MM dd, yy', treasLineChart.getDataTable().getValue(ri, colID(treasLineView, 'ratedate')));
   		TYCChart.loadYieldCurve(jsonYCData, ycChartTitle);
        google.visualization.events.removeListener(eventHndl_TLC_Ready);
	}
	
	TYCChart.rangeStateEvent = function (event){
    	if (isAnimHalt==false){
    		TYCChart.ycAnimToggle();
    	}
        var eDt = treasLineChart.getDataTable().getNumberOfRows();
        document.getElementById("trsSDtLab").innerHTML=$.datepicker.formatDate('MM dd, yy', treasLineChart.getDataTable().getValue(0, colID(treasLineView, 'ratedate')));
        document.getElementById("trsEDtLab").innerHTML=$.datepicker.formatDate('MM dd, yy', treasLineChart.getDataTable().getValue(eDt-1, colID(treasLineView, 'ratedate')));
    }

	TYCChart.setBusDayJSON = function (jsonChartData) {
    	var db = new Date();
    	var jsonTempl = $.parseJSON('{"cols":[{"id":"freq","label":"Reporting Interval","type":"string"},{"id":"ratedate","label":"Rate Date","type":"date"},{"id":"y30","label":"30 Year","type":"number"},{"id":"y20","label":"20 Year","type":"number"},{"id":"y10","label":"10 Year","type":"number"},{"id":"y7","label":"7 Year","type":"number"},{"id":"y5","label":"5 Year","type":"number"},{"id":"y3","label":"3 Year","type":"number"},{"id":"y2","label":"2 Year","type":"number"},{"id":"y1","label":"1 Year","type":"number"},{"id":"m6","label":"6 Month","type":"number"},{"id":"m3","label":"3 Month","type":"number"},{"id":"m1","label":"1 Month","type":"number"}],"rows":null,"p":null}');
    	treasBusDay = new google.visualization.DataTable(jsonTempl);
		for (var i=0;i<jsonChartData.rows.length;i++){
			var rowArray =  new Array();
			rowArray = jsonChartData.rows[i].c;
			var ymdArray = rowArray[1].v.replace("Date(","").replace(")","").split(/[\s,]+/);
			rowArray[1].v = new Date(parseInt(ymdArray[0]), parseInt(ymdArray[1]), parseInt(ymdArray[2]));
			treasBusDay.addRow(rowArray);
		}
   	}
   	
	TYCChart.getBusDayJSON = function () {

        return treasBusDay;

   	}

	TYCChart.setWeekJSON = function (jsonChartData) {
    	var jsonTempl = $.parseJSON('{"cols":[{"id":"freq","label":"Reporting Interval","type":"string"},{"id":"ratedate","label":"Rate Date","type":"date"},{"id":"y30","label":"30 Year","type":"number"},{"id":"y20","label":"20 Year","type":"number"},{"id":"y10","label":"10 Year","type":"number"},{"id":"y7","label":"7 Year","type":"number"},{"id":"y5","label":"5 Year","type":"number"},{"id":"y3","label":"3 Year","type":"number"},{"id":"y2","label":"2 Year","type":"number"},{"id":"y1","label":"1 Year","type":"number"},{"id":"m6","label":"6 Month","type":"number"},{"id":"m3","label":"3 Month","type":"number"},{"id":"m1","label":"1 Month","type":"number"}],"rows":null,"p":null}');
    	treasWeek = new google.visualization.DataTable(jsonTempl);
		for (var i=0;i<jsonChartData.rows.length;i++){
			var rowArray =  new Array();
			rowArray = jsonChartData.rows[i].c;
			var ymdArray = rowArray[1].v.replace("Date(","").replace(")","").split(/[\s,]+/);
			rowArray[1].v = new Date(parseInt(ymdArray[0]), parseInt(ymdArray[1]), parseInt(ymdArray[2]));
			treasWeek.addRow(rowArray);
		}
   	}
   	
	TYCChart.getWeekJSON = function () {

        return treasWeek;

   	}

	TYCChart.DrawTreasChart = function () {          
		
		treasChartOpts = {
              'title': 'Market Yield U.S. Treasury Securities Constant Maturity',
              'titleTextStyle': {color: 'darkblue', bold: true},
              'pointSize': 0,
              'colors' : ['#000000'],
              'backgroundColor' : { 
            	  'fill' : '#FFFFEB' 
            	  },
              'labelStacking': 'vertical',
              'enableInteractivity': true,
              'fontSize': 12,
              'chartArea': {
            	  'left' : '30',
                  'width' : '720',
                  'height' : 'auto'
                  },
              'focusTarget' : 'category',
              'legend': {'position': 'none'},
              'series' : {
               	  0:{color : '#1F1F7A'},
               	  1:{color : '#24248F'},
               	  2:{color : '#2929A3'},
               	  3:{color : '#2E2EB8'},
               	  4:{color : '#3333CC'},
               	  5:{color : '#4747D1'},
               	  6:{color : '#5C5CD6'},
               	  7:{color : '#7070DB'},
               	  8:{color : '#8585E0'},
               	  9:{color : '#9999E6'},
               	  10:{color : '#ADADEB'},
               	  11:{color : '#C2C2F0'}
            	  },
              'hAxis': {
               'viewWindowMode': 'pretty',
               'textPosition': 'out',
               'slantedText' : true,
               'gridlines.color' : '#000000'
               },
              'vAxis': {
               'viewWindowMode': 'pretty',
               'textPosition': 'out',
               'gridlines': {'color': 'blue'}
               }
        };
        
        var treasView = new google.visualization.DataView(treasWeek);
        treasView.setColumns([
                            colID(treasView, 'ratedate')
                            ,colID(treasView, 'y30')
                            ,colID(treasView, 'y20')
                            ,colID(treasView, 'y10')
                            ,colID(treasView, 'y7')
                            ,colID(treasView, 'y5')
                            ,colID(treasView, 'y3')
                            ,colID(treasView, 'y2')
                            ,colID(treasView, 'y1')
                            ,colID(treasView, 'm6')
                            ,colID(treasView, 'm3')
                            ,colID(treasView, 'm1')]);

		treasChart = new google.visualization.LineChart(document.getElementById('h15AllYrs_div'));
        treasChart.draw(treasView, treasChartOpts);
	};
	
    TYCChart.SeekRange = function (range) {
    	var last = treasLineView.getNumberOfRows();
        var ty = $.datepicker.formatDate('yy', treasLineView.getValue(last-1, colID(treasLineView, 'ratedate')));
        var fy = parseInt(ty)-parseInt(range)+1;
        if (range <= 3) {
			if (fy < $.datepicker.formatDate('yy', treasBusDay.getValue(0, colID(treasBusDay, 'ratedate')))){
				fy = $.datepicker.formatDate('yy', treasBusDay.getValue(0, colID(treasBusDay, 'ratedate')));
				ty = parseInt(fy)+parseInt(range)-1;
			}
			if (ty > $.datepicker.formatDate('yy', treasBusDay.getValue(treasBusDay.getNumberOfRows()-1, colID(treasBusDay, 'ratedate')))){
				ty = $.datepicker.formatDate('yy', treasBusDay.getValue(treasBusDay.getNumberOfRows()-1, colID(treasBusDay, 'ratedate')));
			}
        } else {
			if (fy < $.datepicker.formatDate('yy', treasWeek.getValue(0, colID(treasWeek, 'ratedate')))){
				fy = $.datepicker.formatDate('yy', treasWeek.getValue(0, colID(treasWeek, 'ratedate')));
				ty = parseInt(fy)+parseInt(range)-1;
			}
			if (ty > $.datepicker.formatDate('yy', treasWeek.getValue(treasWeek.getNumberOfRows()-1, colID(treasWeek, 'ratedate')))){
				ty = $.datepicker.formatDate('yy', treasWeek.getValue(treasWeek.getNumberOfRows()-1, colID(treasWeek, 'ratedate')));
			}
        }
        TYCChart.DBAddQueue(fy, ty);
    }
    
    TYCChart.SeekFirst = function () {
        var fy = $.datepicker.formatDate('yy', treasLineView.getValue(0, colID(treasLineView, 'ratedate')));
        var ty = $.datepicker.formatDate('yy', treasLineView.getValue(treasLineView.getNumberOfRows()-1, colID(treasLineView, 'ratedate')));
        var range = parseInt(ty)-parseInt(fy)+1;
        if (range<=3){
            fy = $.datepicker.formatDate('yy', treasBusDay.getValue(0, colID(treasBusDay, 'ratedate')));
        } else {
            fy = $.datepicker.formatDate('yy', treasWeek.getValue(0, colID(treasWeek, 'ratedate')));
        }
        ty = parseInt(fy)+parseInt(range)-1;
        TYCChart.DBAddQueue(fy, ty);
    }
    
    TYCChart.SeekLast = function () {
        var fy = $.datepicker.formatDate('yy', treasLineView.getValue(0, colID(treasLineView, 'ratedate')));
        var ty = $.datepicker.formatDate('yy', treasLineView.getValue(treasLineView.getNumberOfRows()-1, colID(treasLineView, 'ratedate')));
        var range = parseInt(ty)-parseInt(fy)+1;
        if (range<=3){
        	ty = $.datepicker.formatDate('yy', treasBusDay.getValue(treasBusDay.getNumberOfRows()-1, colID(treasBusDay, 'ratedate')));
        } else {
        	ty = $.datepicker.formatDate('yy', treasWeek.getValue(treasWeek.getNumberOfRows()-1, colID(treasWeek, 'ratedate')));
        }
    	fy = parseInt(ty)-parseInt(range)+1;
        TYCChart.DBAddQueue(fy, ty);
    }
    
    TYCChart.SeekNext = function (next) {
        var fy = $.datepicker.formatDate('yy', treasLineView.getValue(0, colID(treasLineView, 'ratedate')));
        var ty = $.datepicker.formatDate('yy', treasLineView.getValue(treasLineView.getNumberOfRows()-1, colID(treasLineView, 'ratedate')));
        var range = parseInt(ty)-parseInt(fy)+1;
    	if (parseInt(next)<0) {
            fy = parseInt(fy) - parseInt(range);
            ty = parseInt(ty) - parseInt(range);
    		if (range<=3){
	    		if (fy == $.datepicker.formatDate('yy', treasBusDay.getValue(0, colID(treasBusDay, 'ratedate')))){
	    		}
				if (fy < $.datepicker.formatDate('yy', treasBusDay.getValue(0, colID(treasBusDay, 'ratedate')))){
					fy = $.datepicker.formatDate('yy', treasBusDay.getValue(0, colID(treasBusDay, 'ratedate')));
					ty = parseInt(fy)+parseInt(range)-1;
				}
				if (ty > $.datepicker.formatDate('yy', treasBusDay.getValue(treasBusDay.getNumberOfRows()-1, colID(treasBusDay, 'ratedate')))){
					ty = $.datepicker.formatDate('yy', treasBusDay.getValue(treasBusDay.getNumberOfRows()-1, colID(treasBusDay, 'ratedate')));
				}
    		} else {
	    		if (fy == $.datepicker.formatDate('yy', treasWeek.getValue(0, colID(treasWeek, 'ratedate')))){
	    		}
				if (fy < $.datepicker.formatDate('yy', treasWeek.getValue(0, colID(treasWeek, 'ratedate')))){
					fy = $.datepicker.formatDate('yy', treasWeek.getValue(0, colID(treasWeek, 'ratedate')));
					ty = parseInt(fy)+parseInt(range)-1;
				}
				if (ty > $.datepicker.formatDate('yy', treasWeek.getValue(treasWeek.getNumberOfRows()-1, colID(treasWeek, 'ratedate')))){
					ty = $.datepicker.formatDate('yy', treasWeek.getValue(treasWeek.getNumberOfRows()-1, colID(treasWeek, 'ratedate')));
				}
    		}
    	}
    	if (parseInt(next)>0) {
            fy = parseInt(fy) + parseInt(range);
            ty = parseInt(ty) + parseInt(range);
    		if (range<=3){
	    		if (ty == $.datepicker.formatDate('yy', treasBusDay.getValue(treasBusDay.getNumberOfRows()-1, colID(treasBusDay, 'ratedate')))){
	    		}
				if (ty > $.datepicker.formatDate('yy', treasBusDay.getValue(treasBusDay.getNumberOfRows()-1, colID(treasBusDay, 'ratedate')))){
					ty = $.datepicker.formatDate('yy', treasBusDay.getValue(treasBusDay.getNumberOfRows()-1, colID(treasBusDay, 'ratedate')));
					fy = parseInt(ty)-parseInt(range)+1;
				}
				if (fy < $.datepicker.formatDate('yy', treasBusDay.getValue(0, colID(treasBusDay, 'ratedate')))){
					fy = $.datepicker.formatDate('yy', treasBusDay.getValue(0, colID(treasBusDay, 'ratedate')));
				}
    		} else {
	    		if (ty == $.datepicker.formatDate('yy', treasWeek.getValue(treasWeek.getNumberOfRows()-1, colID(treasWeek, 'ratedate')))){
	    		}
				if (ty > $.datepicker.formatDate('yy', treasWeek.getValue(treasWeek.getNumberOfRows()-1, colID(treasWeek, 'ratedate')))){
					ty = $.datepicker.formatDate('yy', treasWeek.getValue(treasWeek.getNumberOfRows()-1, colID(treasWeek, 'ratedate')));
					fy = parseInt(ty)-parseInt(range)+1;
				}
				if (fy < $.datepicker.formatDate('yy', treasWeek.getValue(0, colID(treasWeek, 'ratedate')))){
					fy = $.datepicker.formatDate('yy', treasWeek.getValue(0, colID(treasWeek, 'ratedate')));
				}
			}
		}
		TYCChart.DBAddQueue(fy, ty);
	}
	
	TYCChart.DBAddQueue = function (fy, ty) {
		var DBArgs = {};
		DBArgs.fy = fy;
		DBArgs.ty = ty;
		DBCallStack.push(DBArgs);
		TYCChart.DBDoQueue();
	}
	
	TYCChart.DBDoQueue = function () {
		if (DBCallStack.length > 0){
			setTimeout(function () {
				var DBArgs = DBCallStack.shift();
				TYCChart.DrawTreasDB(DBArgs.fy, DBArgs.ty);
			}, 0);
		}
	}
    
    TYCChart.DrawTreasDB = function (fy, ty) {
        if (!isTreasLineChartReady) {
			return;
        }
    	
    	if (isAnimHalt==false){
    		TYCChart.ycAnimToggle();
    	}

    	if (parseInt(ty)-parseInt(fy)<=3){
        	treasLineView = new google.visualization.DataView(treasBusDay);
    	} else {
        	treasLineView = new google.visualization.DataView(treasWeek);
    	}
        treasLineView.setRows(treasLineView.getFilteredRows([
             {column: colID(treasLineView, 'ratedate'), minValue: new Date(fy, 0, 1), maxValue: new Date(ty, 11, 31)}]));
        treasLineView.setColumns([
             colID(treasLineView, 'ratedate')
             ,colID(treasLineView, 'y30')
             ,colID(treasLineView, 'y20')
             ,colID(treasLineView, 'y10')
             ,colID(treasLineView, 'y7')
             ,colID(treasLineView, 'y5')
             ,colID(treasLineView, 'y3')
             ,colID(treasLineView, 'y2')
             ,colID(treasLineView, 'y1')
             ,colID(treasLineView, 'm6')
             ,colID(treasLineView, 'm3')
             ,colID(treasLineView, 'm1')]);
        
		treasDashBoard = new google.visualization.Dashboard(document.getElementById('h15DashBoard_div'));

        treasRangeSlider = new google.visualization.ControlWrapper({
          'controlType': 'ChartRangeFilter',
          'containerId': 'h15Ctrl_div',
          'options': {
              'filterColumnLabel': 'Rate Date',
              'ui':{
                  'chartType': 'LineChart',
                  'chartOptions': { 
                       'enableInteractivity': true,
                       'pointSize': 0,
                       'colors' : ['#808080'],
                       'backgroundColor' : {
                      	  'fill' : '#FFFFEB'
                      	  },
                       'chartArea': {
                             'top' : '0',
                             'width' : 'auto',
                             'height' : '40'
                             },
                       'series' : {
                           	  0:{color : '#1F1F7A'},
                           	  1:{color : '#24248F'},
                           	  2:{color : '#2929A3'},
                           	  3:{color : '#2E2EB8'},
                           	  4:{color : '#3333CC'},
                           	  5:{color : '#4747D1'},
                           	  6:{color : '#5C5CD6'},
                           	  7:{color : '#7070DB'},
                           	  8:{color : '#8585E0'},
                           	  9:{color : '#9999E6'},
                           	  10:{color : '#ADADEB'},
                           	  11:{color : '#C2C2F0'}
                           	  },
                       'hAxis': {
                           'textPosition': 'none',
                           'baselineColor': 'none'
                                  }
                       }
                  }
             }
        });
        
        treasLineChart = new google.visualization.ChartWrapper({
            'chartType': 'LineChart',
          'containerId': 'h15Chrt_div',
          'options': {
              'title': 'Market Yield U.S. Treasury Securities Constant Maturity',
              'titleTextStyle': {color: 'darkblue', bold: true},
              'pointSize': 0,
              'colors' : ['#000000'],
              'backgroundColor' : { 
            	  'fill' : '#FFFFEB' 
            	  },
              'labelStacking': 'vertical',
              'enableInteractivity': true,
              'fontSize': 12,
              'chartArea': {
            	  'left' : '30',
                  'width' : '370',
                  'height' : 'auto'
                  },
              'focusTarget' : 'category',
              'legend': {'position': 'none'},
              'series' : {
               	  0:{color : '#1F1F7A'},
               	  1:{color : '#24248F'},
               	  2:{color : '#2929A3'},
               	  3:{color : '#2E2EB8'},
               	  4:{color : '#3333CC'},
               	  5:{color : '#4747D1'},
               	  6:{color : '#5C5CD6'},
               	  7:{color : '#7070DB'},
               	  8:{color : '#8585E0'},
               	  9:{color : '#9999E6'},
               	  10:{color : '#ADADEB'},
               	  11:{color : '#C2C2F0'}
            	  },
              'hAxis': {
               'viewWindowMode': 'pretty',
               'textPosition': 'out',
               'slantedText' : true,
               'gridlines.color' : '#000000'
               },
              'vAxis': {
               'viewWindowMode': 'pretty',
               'textPosition': 'out',
               'gridlines': {'color': 'blue'}
               }
          }
        });

		treasDashBoard.bind(treasRangeSlider, treasLineChart);
		treasDashBoard.draw(treasLineView);
		isTreasLineChartReady=false;
    	eventHndl_TLC_Select = google.visualization.events.addListener(treasLineChart, 'select', TYCChart.yieldCurveEvent);
        eventHndl_TLC_Ready = google.visualization.events.addListener(treasLineChart, 'ready', TYCChart.initReadyEvent);
        eventHndl_TRS_StateChange = google.visualization.events.addListener(treasRangeSlider, 'statechange', TYCChart.rangeStateEvent);
        
    }
    
	TYCChart.getYCJSON = function (){

		var jsonYCData = $.parseJSON("{" +
			    "\"cols\": [" +
	             "{\"id\":\"\",\"label\":\"Maturity\",\"pattern\":\"\",\"type\":\"string\"}," +
	             "{\"id\":\"\",\"label\":\"Yield\",\"pattern\":\"\",\"type\":\"number\"}" +
	           "]," +
	       "\"rows\": [" +
	       		"{\"c\":[{\"v\":\"30 Year\",\"f\":null},{\"v\":0.0,\"f\":null}]}" +
      			",{\"c\":[{\"v\":\"20 Year\",\"f\":null},{\"v\":0.0,\"f\":null}]}" +
       			",{\"c\":[{\"v\":\"10 Year\",\"f\":null},{\"v\":0.0,\"f\":null}]}" +
       			",{\"c\":[{\"v\":\"7 Year\",\"f\":null},{\"v\":0.0,\"f\":null}]}" +
       			",{\"c\":[{\"v\":\"5 Year\",\"f\":null},{\"v\":0.0,\"f\":null}]}" +
       			",{\"c\":[{\"v\":\"3 Year\",\"f\":null},{\"v\":0.0,\"f\":null}]}" +
       			",{\"c\":[{\"v\":\"2 Year\",\"f\":null},{\"v\":0.0,\"f\":null}]}" +
       			",{\"c\":[{\"v\":\"1 Year\",\"f\":null},{\"v\":0.0,\"f\":null}]}" +
       			",{\"c\":[{\"v\":\"6 Month\",\"f\":null},{\"v\":0.0,\"f\":null}]}" +
       			",{\"c\":[{\"v\":\"3 Month\",\"f\":null},{\"v\":0.0,\"f\":null}]}" +
	       		",{\"c\":[{\"v\":\"1 Month\",\"f\":null},{\"v\":0.0,\"f\":null}]}" +
	           "]" +
	     "}");
		
		return jsonYCData;
	}

	TYCChart.ycAnimToggle = function (){
		if ($("#ycAnim").find("span.ui-icon").hasClass("ui-icon-play")) {
			$("#ycAnim").find("span.ui-icon").removeClass("ui-icon-play").addClass("ui-icon-stop");
			isAnimHalt=false;
			TYCChart.animateYieldCurve();
		}
		else{
			$("#ycAnim").find("span.ui-icon").removeClass("ui-icon-stop").addClass("ui-icon-play");
			isAnimHalt=true;
		}	
	}

	TYCChart.animateYieldCurve = function () {

		var jsonYCData = TYCChart.getYCJSON();
		var selRow = [{row:1, column:null}];
		var dataLen = treasLineChart.getDataTable().getNumberOfRows();
		var ri = 0;
		var delay = 100;
		isAnimHalt=false;
		doAnimate = function(callBack)
		{
			animate(callBack);
		};

		animateCallBack = function()
		{
			ri++;
			if (isAnimHalt){
				ri=dataLen;
				}
			if (ri < dataLen) {
				doAnimate(animateCallBack);
			} else {
				$("#ycAnim, span.ui-icon").removeClass("ui-icon-stop").addClass("ui-icon-play");
			}
		};
	
		animate = function(callBack){
			for (var ci = 1; ci<12; ci++){
				jsonYCData.rows[ci-1].c[1].v = treasLineChart.getDataTable().getValue(ri, ci);
			}
    		selRow[0].row = ri;
    		treasLineChart.getChart().setSelection(selRow);
    		var ycChartTitle = "Yield Curve " + $.datepicker.formatDate('MM dd, yy', treasLineChart.getDataTable().getValue(ri, 0));
        	curveOptions.title = ycChartTitle;
			curveDataTable = new google.visualization.DataTable(jsonYCData);
			curveLineChart.draw(curveDataTable, curveOptions);
			setTimeout(callBack, delay);
        };
        doAnimate(animateCallBack);
	}
	
	TYCChart.yieldCurveEvent = function (event) {

            var selectedRates = treasLineChart.getChart().getSelection();
    		var ri = null;
   		    if (selectedRates.length > 0 ){
   		    	if (selectedRates[0].row != null) {
   		    		ri = selectedRates[0].row;
   		    	} else {
   		    		ri = treasLineChart.getDataTable().getNumberOfRows()-1;
   		    	}
   		    } else {
   		    	return;
   		    }

   			var jsonYCData = TYCChart.getYCJSON();
    		if (ri != null) {
    			for (var gvi = 1; gvi<12; gvi++){
    				jsonYCData.rows[gvi-1].c[1].v = treasLineChart.getDataTable().getValue(ri, gvi);
    			}
    		}
    		var ycChartTitle = "Yield Curve " + $.datepicker.formatDate('MM dd, yy', treasLineChart.getDataTable().getValue(ri, 0));
    	    curveDataTable = new google.visualization.DataTable(jsonYCData);
    	    curveOptions.title = ycChartTitle;
    	    curveLineChart.draw(curveDataTable, curveOptions);
   	}

	TYCChart.yieldCurveOptions = function () {
	    curveOptions = {'title':'Yield Curve for Month Day Year',
                'titleTextStyle': {color: 'darkblue', bold: true},
                'enableInteractivity': true,
                'chartArea' : {
                    'left': 30,
                    'width': 280,
                    'height': 'auto'
                    },
                'backgroundColor' : {
                    'fill' : '#FFFFEB'
                    },
                'labelStacking': 'vertical',
                'series' : {
                    0:{color : '#3300FF'}
                    },
                'legend': {'position': 'none'},
                'hAxis': {
                 'textPosition': 'out',
                 'slantedText' : true,
                 'direction' : -1
                 },
                'vAxis': {
                    'textPosition': 'out',
                    'logScale': false,
                    'gridlines': {'color': 'blue', 'count': 6},
                    'baseline': 0
                    }
                 };
	    return curveOptions;
	}
	
	TYCChart.loadYieldCurve = function (jsondata, chartTitle){
	    	curveDataTable = new google.visualization.DataTable(jsondata);
	    	curveOptions=TYCChart.yieldCurveOptions();
	    	curveOptions.title = chartTitle;
	    	curveLineChart = new google.visualization.LineChart(document.getElementById('ycChrt_div'));
	    	curveLineChart.draw(curveDataTable, curveOptions);
	}
	
	return TYCChart;
    
}
