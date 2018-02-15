<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
%>
<html>
  <head>
    <title>embed.html</title>
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="this is my page">
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1">
	<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
  </head>
  <body topmargin="0" leftmargin="0" rightmargin="0">
  <table width="100%" height="100%" cellpadding="0" cellspacing="0">
  	<tr>
  		<td width="100%"> 
  			<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#006633">
				<tr>
				<td>
	  			<table width="100%" border="0" cellpadding="0" cellspacing="1">
					<tr>
                                            <td bgcolor="white" align="center"><a href="muestraPDFEmbebido.jsp?tipoArchvio=2&idSolicitud=<%=idSolicitud%>">Autorización SIC</a></td>					
                                            <td bgcolor="white" align="center"><a href="muestraPDFEmbebido.jsp?tipoArchvio=3&idSolicitud=<%=idSolicitud%>">Solicitud</a></td>					
					<%--<td bgcolor="white" align="center"><a href="muestraPDFEmbebido.jsp?tipoArchvio=9&idSolicitud=<%=idSolicitud%>">Solicitud subsidio CONAVI</a></td>--%>
					</tr>
				</table> 
				</td>
				</tr>
			</table>
  		</td>
  	</tr>
  	<tr>
  		<td width="100%" height="100%">
<% 			if ( request.getParameter("tipoArchvio")!=null ){%>
  			<embed height="100%" width="100%" src="http://<%=request.getServerName()%>:<%=request.getLocalPort()+request.getContextPath()%>/muestraContenidoPDF.jsp?idSolicitud=<%=idSolicitud%>&tipoArchivo=<%=request.getParameter("tipoArchvio")%>"></embed>
<%			}else{%>
			&nbsp;
<%			}%>
  		</td>
  	</tr>
  </table>
    
  </body>
</html>
