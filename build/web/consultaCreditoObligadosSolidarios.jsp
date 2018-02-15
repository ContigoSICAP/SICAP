<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.SolicitudVO"%>
<%@ page import="com.sicap.clientes.vo.ObligadoSolidarioVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>

<html>
<head>
<title>Clientes</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function capturarObligadoSolidario(){
		window.document.forma.command.value='capturaCreditoObligadosSolidarios';
		window.document.forma.idObligado.value=idObligado;
		window.document.forma.submit();
	}

	function capturaCreditoObligadosSolidarios(idObligado){
		window.document.forma.command.value='capturaCreditoObligadosSolidarios';
		window.document.forma.idObligado.value=idObligado;
		window.document.forma.submit();
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<%
	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
	ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
	int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
	int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
	SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
%>

<body leftmargin="0" topmargin="0">
<center>
    <h3>Calificacion crediticia obligados solidarios</h3>

	<%=HTMLHelper.displayNotifications(notificaciones)%>

<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="">
<input type="hidden" name="idSolicitud" value="<%=solicitud.idSolicitud%>">
<input type="hidden" name="idObligado" value="0">
<table border="0" cellpadding="0" width="600" >
	<tr  bgcolor="#009865" class="whitetext">
  	<td width="90%" align="center">Nombre</td>
  	<td width="10%" align="center">Acci&oacute;n</td>
	</tr>
<% if ( solicitud.obligadosSolidarios!=null ){
	for ( int i=0 ; i<solicitud.obligadosSolidarios.length ; i++ ){
		ObligadoSolidarioVO obligado = solicitud.obligadosSolidarios[i];
%>
	<tr>
  		<td width="90%" align="center"><%=HTMLHelper.displayField(obligado.nombre+" "+obligado.aPaterno+" "+obligado.aMaterno) %></td>
  		<td width="10%" align="center"><a href='#' onClick='capturaCreditoObligadosSolidarios(<%=obligado.idObligado%>)'>Modificar</a></td>
	</tr>
<%
	}
%>
<% }%>
	<tr>
  	<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="2" align="center"><input type="button" value="Cerrar" onClick="javascript:window.close();"></td>
	</tr>
</table>

<br>
</form>
</center>

</html>
