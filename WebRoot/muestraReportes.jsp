<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %><%
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server

	StringBuffer output = new StringBuffer();
	String respuesta = (String)request.getAttribute("saldosSucursal");
	output.append(respuesta);
	
	response.setContentType("application/csv");
	response.setHeader("Content-Disposition","attachment; filename=\"SaldosSucursal.csv\"");
	response.setHeader("cache-control", "no-cache");
	out.println(output.toString());
%>