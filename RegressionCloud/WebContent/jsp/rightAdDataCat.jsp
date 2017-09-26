    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<script type="text/javascript" src="ScriptLibrary/adAcc.js"></script>
    <script type="text/javascript" src="ScriptLibrary/dataList.js"></script>
	<div id='adAcc' class='accRef' >
		<jsp:include page="../html/DataList.html" flush="true" />
	</div>

	<script type="text/javascript">
	$(function() {
		$('#adAcc').css('visibility','visible');
	});
	$(function() {
		$('.accPan').resizable();
		$('.accPan').addClass('isOverflowHidden');
		$('#adAcc').accordion({'heightStyle': 'content', 'collapsible': true, icons: {'header': 'ui-icon-plusthick', 'activeHeader': 'ui-icon-minusthick'}});
	});
	</script>

    