<%@ page language="java" pageEncoding="ISO-8859-1" import="java.util.*" %><%
String       filename = (String)request.getAttribute("FILENAME"); 
List         file     = (List)request.getAttribute("FILE"); 
response.setContentType("text/plain");
response.setHeader("Content-Disposition",
			       "attachment; filename=\""+filename+"\"");
response.setHeader("cache-control", "no-cache");
if( !file.isEmpty() ){
	Iterator i = file.iterator();
	while( i.hasNext() ){
		String linea = (String)i.next();
		out.println( linea );
	}
}%>