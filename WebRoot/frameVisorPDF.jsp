<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%

String command = request.getParameter("command");
String orientacion = request.getParameter("orientacionVisor");
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
%>
<html>
  <head>
    <title>pdf.html</title>
	
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="this is my page">
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1">
    
  	</head>
<%if ( orientacion!=null && orientacion.equals("H") ){%>
		<FRAMESET rows="50%,50%" framespacing="0" >
	<%}else{%>
		<FRAMESET cols="50%,50%" framespacing="0">
	<%}%>
  	<FRAME NAME="indice" SRC="controller?command=<%=command%>&idSolicitud=<%=idSolicitud%>">
  	<FRAME NAME="principal" SRC="muestraPDFEmbebido.jsp?idSolicitud=<%=idSolicitud%>&tipoArchvio=2" marginheight="0" marginwidth="0" scrolling="no">
  	<NOFRAMES>
    	<P>Lo siento, pero sólo podrá ver esta página si tu navegador tiene la capacidad de visualizar marcos.</P>
  	</NOFRAMES>
	</FRAMESET>

</html>
