<%@page contentType="text/html" pageEncoding="iso-8859-1"%>
<%@ page import="com.sicap.clientes.helpers.PrimeraMigracionHelper"%>
<!DOCTYPE html>
<%
    PrimeraMigracionHelper pcHelper = new PrimeraMigracionHelper();
    pcHelper.primeraCarga();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
    </body>
</html>
