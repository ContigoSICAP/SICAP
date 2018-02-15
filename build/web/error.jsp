<%@ page isErrorPage="true" %>
<%@ page import="org.apache.log4j.Logger"%>
<html>
<%!
    Logger myLogger = Logger.getLogger("error.jsp");
    
%>
<%myLogger.error("error.jsp", exception);%>
<head>
<title>Problemas con la aplicación</title>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<center>
<br><br><br>
<h5>Sucedió un error en la aplicación, para reportarlo a Help Desk haga <a href="mailto:helpedk@creditofirme.com">clic aqui</a> o llame al (01) 55 52280000.</h5>
<br>
<a href="<%=request.getContextPath()%>">Inicio</a>
<br><br>
</center>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>