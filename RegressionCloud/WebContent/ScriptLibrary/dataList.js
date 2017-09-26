/**
 * 
 */
function classSelData(){
	
	var metaDataTable;
	var rangeDataTable;
	
	function selData() {
		
	}
	
	selData.setMeta = function(jsonChartData) {

		if (typeof metaDataTable == 'undefined') {
			metaDataTable = new google.visualization.DataTable(jsonChartData.meta.keys);
		} else {
			var jsonSeries = jsonChartData.meta.series;
			if (!this.hasSeriesMeta(jsonSeries)) {
				for (var i=0;i<jsonChartData.meta.keys.rows.length;i++){
					metaDataTable.addRow(jsonChartData.meta.keys.rows[i].c);
				}
			}
		}
	};

	selData.getMeta = function() {
		return metaDataTable;
	};
	    
	selData.hasSeriesMeta = function(newSeries) {
		if (typeof metaDataTable == 'undefined') {return false;}
		var rowIdx = metaDataTable.getFilteredRows([{column: colID(metaDataTable, 'series'), value: newSeries}]);
		if (rowIdx==0){return false;}
		return true;
	};

	selData.setRange = function(jsonChartData) {

		if (typeof rangeDataTable == 'undefined') {
			rangeDataTable = new google.visualization.DataTable(jsonChartData.range.data);
		} else {
			var jsonSeries = jsonChartData.range.series;
			var jsonCname = jsonChartData.range.cname;
			if (!this.hasSeriesRange(jsonSeries, jsonCname)) {
				for (var i=0;i<jsonChartData.range.data.rows.length;i++){
					rangeDataTable.addRow(jsonChartData.range.data.rows[i].c);
				}
			}
		}
	};

	selData.getRange = function() {
		return rangeDataTable;
	};
	
	selData.hasSeriesRange = function(newSeries, newCname) {
		if (typeof rangeDataTable == 'undefined') {return false;}
		var rowIdx = rangeDataTable.getFilteredRows([
													{column: colID(rangeDataTable, 'series'), value: newSeries}
													,{column: colID(rangeDataTable, 'cname'), value: newCname}
													]);
		if (rowIdx==0){return false;}
		return true;
	};
	
	selData.seriesKeys = function (series) {
		var fieldSet = "<fieldset>";
		fieldSet+="<label for='sKeyCtrl'>Series Keys</label>";
		fieldSet+="<select name='sKeyCtrl' id='sKeyCtrl' onchange='showMeta(\""+series+"\",this.options[this.selectedIndex].value);return false;'>";
		var wdt=metaDataTable;
		wdt.sort([
				{column: colID(wdt, 'series')}
				,{column: colID(wdt, 'kseq')}
				]);
		var colView = new google.visualization.DataView(wdt);
		colView.setRows(colView.getFilteredRows([
						{column: colID(colView, 'series'), value: series}]));
		for (var i=0;i<colView.getNumberOfRows();i++) {
			fieldSet+="<option value='"+colView.getValue(i, colID(colView, 'cname'))+"'";
			if (i==0) {
				fieldSet+=' selected="selected" ';
			}
			fieldSet+=">"+colView.getValue(i, colID(colView, 'cdesc'))+"</option>";
		}
		fieldSet+="</select>";		
		fieldSet+="</fieldset>";	
		return fieldSet;
	};
	
	selData.seriesMeta = function (series, cname) {
		var fieldSet = "<fieldset id='fsMeta'>";
		var wdt=metaDataTable;
		wdt.sort([
				{column: colID(wdt, 'series')}
				,{column: colID(wdt, 'cname')}
				]);
		var colView = new google.visualization.DataView(wdt);
		colView.setRows(colView.getFilteredRows([
						{column: colID(colView, 'series'), value: series}
						,{column: colID(colView, 'cname'), value: cname}]));
		if (colView.getNumberOfRows()>0) {
			var ctype = colView.getValue(0, colID(colView, 'ctype'));
			if (ctype=='int') {
				fieldSet+="<label for='sKeyMin'>Minimum</label>";
				fieldSet+="<input type='text' name='sKeyMin' value='"+colView.getValue(0, colID(colView, 'min'))+"' readonly='readonly' ><br>";
				fieldSet+="<label for='sKeyMax'>Maximum</label>";
				fieldSet+="<input type='text' name='sKeyMax' value='"+colView.getValue(0, colID(colView, 'max'))+"' readonly='readonly' ><br>";
			}
			fieldSet+="<label for='sKeyCnt'>Unique Count</label>";
			fieldSet+="<input type='text' name='sKeyCnt' value='"+colView.getValue(0, colID(colView, 'count'))+"' readonly='readonly' ><br>";
		}
		fieldSet+="</fieldset>";
		return fieldSet;
	};
	
	selData.seriesRange = function (series, cname) {
		fieldSet = "<fieldset>";
		fieldSet+="<label for='sKeyVals'>Key Values</label>";
		fieldSet+="<select name='sKeyVals' id='sKeyVals' >";
		var wdt=rangeDataTable;
		wdt.sort([
				{column: colID(wdt, 'series')}
				,{column: colID(wdt, 'cname')}
				,{column: colID(wdt, 'val')}
				]);
		var colView = new google.visualization.DataView(wdt);
		colView.setRows(colView.getFilteredRows([
						{column: colID(colView, 'series'), value: series}
						,{column: colID(colView, 'cname'), value: cname}]));
		for (var i=0;i<colView.getNumberOfRows();i++) {
			fieldSet+="<option value='"+colView.getValue(i, colID(colView, 'val'))+"'";
			fieldSet+=">"+colView.getValue(i, colID(colView, 'val'))+"</option>";
		}
		fieldSet+="</select>";
		fieldSet+="</fieldset>";
		return fieldSet;
	};
	
	return selData;
	
}