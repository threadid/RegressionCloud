<%@ page language="java" contentType="image/svg+xml; charset=UTF-8" pageEncoding="UTF-8"%><jsp:useBean id="mvCAjax" scope="request" class="com.regressioncloud.web.AjaxController"><jsp:setProperty name="mvCAjax" property="*"/></jsp:useBean><%mvCAjax.setServletContext(application.getContext("/"+application.getServletContextName()));%><%=mvCAjax.svgPayLoad()%>