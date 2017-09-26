/*************************************************************
 * 
 * Common Functions for consistent experience
 * 
 */

function seoURIPush(servPath, query){
	if (typeof servPath == 'undefined'){return;}
	if (typeof query == 'undefined'){query={};}
	document.title = servPath.split("/")[0] + " | " + servPath.split("/").pop().split("-").join(" ");
	if (window.history && window.history.pushState) {
		window.history.pushState(query, "" , servPath.split("/").pop());
	}
}

function seoURIReplace(servPath, query){
	if (typeof servPath == 'undefined'){return;}
	if (typeof query == 'undefined'){query={};}
	document.title = servPath.split("/")[0] + " | " + servPath.split("/").pop().split("-").join(" ");
	if (window.history && window.history.replaceState) {
		window.history.replaceState(query, "" , servPath.split("/").pop());
	}
}

function onCenter(d){
	var dW = $(document).width();
	var bW = $("#awmAnchor-rcMenuBar").width();
	var cL = (dW - bW)/d; 
	if (dW > bW) {
		$("#divCenter").css("left", cL );
		}
}

function logBasen(val, base) {
	  return Math.log(val) / Math.log(base);
	}

function rnd2prec(val, prec){
	var multi = Math.pow( 10, prec );
    return Math.round( val * multi ) / multi;
}

function factFormat(fact, prec){
	var formatted = fact.toFixed(prec).toString().split('.');
	formatted[0] = formatted[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	if (formatted.length>0) {
		if (parseInt(formatted[1])>0){
			return formatted[0]+'.'+formatted[1];
		} else {
			return formatted[0];
		}
	} else {
		return formatted[0];
	}
}

function colID(dataTable, column){
	
	for (var i=0;i<dataTable.getNumberOfColumns();i++){
	       if (dataTable.getColumnId(i) === column) {
	    	   return i;
	    	   break;
	    	   }
       }
}

function minMaxButt(ref){
	if($('.'+ref+'.hide').css('display')=='none'){
		$('.'+ref+'.butt').attr('src','images/minButt.png');
		$('.'+ref+'.hide').css('display','');
	} else {
		$('.'+ref+'.butt').attr('src','images/maxButt.png');
		$('.'+ref+'.hide').css('display','none');
	}
}

function syncSelect(y, x){
    var yv = $( "#"+y+" option:selected" ).val();
    var xv = $( "#"+x+" option:selected" ).val();
    $( "#"+y+" option:disabled" ).prop('disabled', false);
    $( "#"+x+" option:disabled" ).prop('disabled', false);
    $( "#"+y+" option[value='" + xv + "']" ).prop('disabled', true);
    $( "#"+x+" option[value='" + yv + "']" ).prop('disabled', true);
}

var colorGrade = {gradient: {
		'green' :{
			10:'#004C00',
			9:'#005C00',
			8:'#006B00',
			7:'#007A00',
			6:'#008A00',
			5:'#009900',
			4:'#19A319',
			3:'#33AD33',
			2:'#4DB84D',
			1:'#66C266',
			0:'#80CC80'
			},
		'red' :{
		  10:'#800000',
		  9:'#990000',
		  8:'#B20000',
		  7:'#CC0000',
		  6:'#E60000',
		  5:'#FF0000',
		  4:'#FF1919',
		  3:'#FF3333',
		  2:'#FF4D4D',
		  1:'#FF6666',
		  0:'#FF8080'
		  } 
 	}
};
