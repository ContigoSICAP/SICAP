<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" errorPage="error.jsp"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>   
    <title>Consulta Pago Comunal</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>
	function recibir(){
		if (esEntero(window.document.forma.idGrupo.value)){
			if (esEntero(window.document.forma.idCiclo.value)){
				window.document.forma.command.value='validarPagoComunal';
				window.document.forma.submit();
			}else{
				alert('El ciclo debe ser un numero');
			}			
		}
		else
		{	
			alert('El grupo debe ser un numero');
		}
	}
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
    
<%
	ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
  	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
   
  	
%>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<center>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="">
<input type="hidden" name="idCliente" value="">
<input type="hidden" name="idSolicitud" value="">

<table border="0" width="100%">
	<tr>
		
		<td align="center" width="75%">
			<h3>Consulta Pagos Comunales</h3>
			<%=HTMLHelper.displayNotifications(notificaciones)%>
			
		</td>
	</tr>
</table>
</form>
</center> 
  </body>
</html>
