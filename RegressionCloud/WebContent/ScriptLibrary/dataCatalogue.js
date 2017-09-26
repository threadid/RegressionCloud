	function classDataCat() {

		var seriesTable;
		var sourceTable;
		var sourceNoteTable;
		var sourceURITable;
		var providerTable;
		var seriesKeyTable;
		var seriesValueTable;
		var seriesNoteTable;
		var seriesSourceTable;
		
        function seriesCat() {       
        }
    
        seriesCat.setSeriesTable = function(jsonData) {
        	seriesTable = new google.visualization.DataTable(jsonData);
		};

        seriesCat.getSeriesTable = function() {
        	return seriesTable;
		};

        seriesCat.setSourceTable = function(jsonData) {
        	sourceTable = new google.visualization.DataTable(jsonData);
            if (typeof sourceTable == "undefined") {console.log("undefined");}
            console.log(jsonData.cols[2].id);
		};

        seriesCat.getSourceTable = function() {
        	return sourceTable;
		};
		
        seriesCat.setSourceNoteTable = function(jsonData) {
        	sourceNoteTable = new google.visualization.DataTable(jsonData);
		};

        seriesCat.getSourceNoteTable = function() {
        	return sourceNoteTable;
		};
		
        seriesCat.setSourceURITable = function(jsonData) {
        	sourceURITable = new google.visualization.DataTable(jsonData);
		};

        seriesCat.getSourceURITable = function() {
        	return sourceURITable;
		};

        seriesCat.setProviderTable = function(jsonData) {
        	providerTable = new google.visualization.DataTable(jsonData);
		};

        seriesCat.getProviderTable = function() {
        	return providerTable;
		};

        seriesCat.setSeriesKeyTable = function(jsonData) {
        	seriesKeyTable = new google.visualization.DataTable(jsonData);
		};

        seriesCat.getSeriesKeyTable = function() {
        	return seriesKeyTable;
		};

        seriesCat.setSeriesValueTable = function(jsonData) {
        	seriesValueTable = new google.visualization.DataTable(jsonData);
		};

        seriesCat.getSeriesValueTable = function() {
        	return seriesValueTable;
		};

        seriesCat.setSeriesNoteTable = function(jsonData) {
        	seriesNoteTable = new google.visualization.DataTable(jsonData);
		};

        seriesCat.getSeriesNoteTable = function() {
        	return seriesNoteTable;
		};

        seriesCat.setSeriesSourceTable = function(jsonData) {
        	console.log(jsonData);
        	seriesSourceTable = new google.visualization.DataTable(jsonData);
		};

        seriesCat.getSeriesSourceTable = function() {
        	return seriesSourceTable;
		};
		
		seriesCat.getSeries = function() {
			var seriesView = new google.visualization.DataView(seriesTable);
			var seriesAccord = '';
			
			for (var i=0; i<seriesView.getNumberOfRows(); i++) {
				var series_id = seriesView.getValue(i, colID(seriesView, 'series_id'))
				
				seriesAccord = "<article class=''><h2>Series</h2><p>"
				+"<ul class='cCatList'>"
				+"<h2>Values</h2>";
				
				seriesValueTable.sort([
					{column: colID(seriesValueTable, 'series_id')}
					,{column: colID(seriesValueTable, 'series_value_seq')}]);
				var seriesValueView = new google.visualization.DataView(seriesValueTable);
				seriesValueView.setRows(seriesValueView.getFilteredRows([
					{column: colID(seriesValueView, 'series_id'), value: series_id}
					]));;
				for (var iv=0;iv<seriesValueView.getNumberOfRows();iv++) {
					seriesAccord +="<li>Series Value "+seriesValueView.getValue(iv, colID(seriesValueView, 'series_value_seq'))+": <span class=''>"+seriesValueView.getValue(iv, colID(seriesValueView, 'series_value_name'))+"</span></li>";
					seriesAccord +="<li>Value Description: <span class=''>"+seriesValueView.getValue(iv, colID(seriesValueView, 'series_value_description'))+"</span></li>";
					seriesAccord +="<li>Value Type: <span class=''>"+seriesValueView.getValue(iv, colID(seriesValueView, 'series_value_type'))+"</span></li>";
					seriesAccord +="<li>&nbsp;</li>";
				}
				
				seriesAccord +="<h2>Keys</h2>";
					
				seriesKeyTable.sort([
					{column: colID(seriesKeyTable, 'series_id')}
					,{column: colID(seriesKeyTable, 'series_key_seq')}]);
				var seriesKeyView = new google.visualization.DataView(seriesKeyTable);
				seriesKeyView.setRows(seriesKeyView.getFilteredRows([
					{column: colID(seriesKeyView, 'series_id'), value: series_id}
					]));
				for (var ik=0;ik<seriesKeyView.getNumberOfRows();ik++) {
					seriesAccord +="<li>Series Key "+seriesKeyView.getValue(ik, colID(seriesKeyView, 'series_key_seq'))+": <span class=''>"+seriesKeyView.getValue(ik, colID(seriesKeyView, 'series_key_name'))+"</span></li>";
					seriesAccord +="<li>Key Description: <span class=''>"+seriesKeyView.getValue(ik, colID(seriesKeyView, 'series_key_description'))+"</span></li>";
					seriesAccord +="<li>Key Type: <span class=''>"+seriesKeyView.getValue(ik, colID(seriesKeyView, 'series_key_type'))+"</span></li>";
					seriesAccord +="<li>Key Context: <span class=''>"+seriesKeyView.getValue(ik, colID(seriesKeyView, 'series_key_context'))+"</span></li>";
					seriesAccord +="<li>&nbsp;</li>";
				}
				seriesAccord += "</ul></p>";
				seriesAccord +="<h2>Sources</h2>";
				
				seriesSourceTable.sort([
	                      {column: colID(seriesSourceTable, 'series_id')}]);
				var sourceList = new google.visualization.DataView(seriesSourceTable);
				sourceList.setRows(sourceList.getFilteredRows([
	                                                     {column: colID(sourceList, 'series_id'), value: series_id}
	                                                     ]));
				sourceList.setColumns([
	                                 colID(sourceList, 'source_id')
	                                 ]);
				console.log(sourceList.getNumberOfRows());
				var sourceAccord = "";
				for (var ii=0; ii<sourceList.getNumberOfRows(); ii++) {
					var sourceId = sourceList.getValue(ii, colID(sourceList, 'source_id'));
					var sourceView = new google.visualization.DataView(sourceTable);
					console.log(sourceView.getNumberOfRows());
					console.log(sourceId);
					console.log(ii);
					sourceView.setRows(sourceView.getFilteredRows([
				                      {column: colID(sourceView, 'source_id'), value: sourceId}
				                      ]));
					console.log(sourceView.getNumberOfRows());
					console.log(sourceView.getValue(0, colID(sourceView, 'source_id')));
					console.log(sourceView.getValue(0, colID(sourceView, 'source_name')));
					sourceAccord += "<table><tr><td>" 
					+"<a  href='#' onclick=\"if($('#srcid_" + series_id + sourceId + "').hasClass('isHidden')){$('#srcid_" + series_id + sourceId + "').removeClass('isHidden')}else{$('#srcid_" + series_id + sourceId + "').addClass('isHidden')};return false;\">"
					+"<span class='ffact'>" + sourceView.getValue(0, colID(sourceView, 'source_name')) + "</span>"
					+"</a></td></tr>" 
					+"<tr id='srcid_" + series_id + sourceId + "' class='isHidden'><td>";
					sourceAccord += "<ul class='cCatList'>"
//					+"<li>Source: <span class='ffact'>"+sourceView.getValue(0, colID(sourceView, 'source_name'))+"</span></li>"
//					+"<li>Provider: <span class='ffact'>"+sourceView.getFormattedValue(ii, colID(sourceView, 'provider_name'))+"</span></li>"
//					+"<li>Domain: <span class='ffact'>"+sourceView.getFormattedValue(ii, colID(sourceView, 'provider_domain'))+"</span></li>"
					+"<li>Description: <span class=''>"+sourceView.getValue(0, colID(sourceView, 'source_description'))+"</span></li>"
					+"<li>Citation: <span class=''>"+sourceView.getValue(0, colID(sourceView, 'source_citation'))+"</span></li>"
					+"<li>Reference: <a href='"+sourceView.getValue(0, colID(sourceView, 'reference_page_uri'))+"' target='_blank'><span class=''>"+sourceView.getValue(0, colID(sourceView, 'reference_page_uri'))+"</span></a></li>"
					+"<li>Layout: <a href='"+sourceView.getValue(0, colID(sourceView, 'reference_layout_uri'))+"' target='_blank'><span class=''>"+sourceView.getValue(0, colID(sourceView, 'reference_layout_uri'))+"</span></a></li>"
					+"<li>Methodology: <a href='"+sourceView.getValue(0, colID(sourceView, 'reference_method_uri'))+"' target='_blank'><span class=''>"+sourceView.getValue(0, colID(sourceView, 'reference_method_uri'))+"</span></a></li>";
					var sourceURI = new google.visualization.DataView(sourceURITable);
					sourceURI.setRows(sourceURI.getFilteredRows([
		                                                     {column: colID(sourceURI, 'source_id'), value: sourceId}
		                                                     ]));
					sourceURI.setColumns([
		                                 colID(sourceURI, 'reference_file_uri')
		                                 ]);
					for (var iii=0; iii<sourceURI.getNumberOfRows(); iii++) {
						if (iii==0){
							sourceAccord += "<li>Files: <a href='"+sourceURI.getValue(iii, colID(sourceURI, 'reference_file_uri'))+"' target='_blank'><span class=''>"+sourceURI.getValue(iii, colID(sourceURI, 'reference_file_uri'))+"</span></a></li>"
						} else {
							sourceAccord += "<li> <span class=''><a href='"+sourceURI.getValue(iii, colID(sourceURI, 'reference_file_uri'))+"' target='_blank'><span class=''>"+sourceURI.getValue(iii, colID(sourceURI, 'reference_file_uri'))+"</span></a></li>"
						}
					}
					sourceAccord += "</ul></td></tr></table>";
				}
			$('#divSeries').append('<h3>'+seriesView.getFormattedValue(i, colID(seriesView, 'series_name'))
				+' - '+seriesView.getFormattedValue(i, colID(seriesView, 'series_description'))+'</h3>'
				+'<div id="seriesDiv'+i+'" >'
				+'<div>'
				+seriesAccord
				+'</div><div>'
				+sourceAccord
				+'</div>'
				+'</div>');
			}
			$( "#divSeries" ).accordion({
				heightStyle: "content"
				,collapsible: true
				,active: false
			    });
			$(function() {
				$( '.accSource' ).resizable();
				$( '.accSource' ).addClass('isOverflowHidden');
				});
		}
        return seriesCat;
	}
