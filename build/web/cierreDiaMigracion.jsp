<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%>
<%@ page import="com.sicap.clientes.dao.CatalogoDAO"%>
<%@ page import="com.sicap.clientes.vo.ParametroVO"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<jsp:directive.page import="com.sicap.clientes.util.inffinix.InffinixUtil"/>

<%
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
String ultimaFecha = CatalogoHelper.getParametro("FECHA_CIERRE_MIGRACION");

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
<script language="Javascript" src="./js/functions.js"></script>
<script>

   function cierreDia(){
		window.document.forma.command.value='procesaCierreMigracion';
		window.document.forma.submit();
	}
	
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    
    <title>Cierre Dia Manual Migracion</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
<body leftmargin="0" topmargin="0" onload="javascript:window.document.forma.usuario.focus();">
<jsp:include page="header.jsp" flush="true"/>
<center>

<h2>Administraci&oacute;n de Cierre</h2>
<h3>Cierre de Dia Manual Migracion</h3>

<form name="forma" action="admin" method="post">
<input type="hidden" name="command" value="">
<table border="0" width="100%" cellspacing="0">
	<tr>
  		<td width="100%" align="center" colspan="2"><%=HTMLHelper.displayNotifications(notificaciones)%><br></td>
	</tr>
	
	<tr>
	  	<td width="50%" align="right">Fecha Cierre Migracion</td>
	  	<td width="50%">  
			<%=HTMLHelper.displayField(ultimaFecha)%>
	  	</td>
	</tr>
<tr>
  <td colspan="2" align="center"><br><input type="button" value="Cerrar Dia" onClick="cierreDia()"></td>
</tr>	
</table>
</form>
</center>

<jsp:include page="footer.jsp" flush="true"/></body>
  </body>
</html>
