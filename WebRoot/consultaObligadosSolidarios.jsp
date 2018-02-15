<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.SolicitudVO"%>
<%@ page import="com.sicap.clientes.vo.ObligadoSolidarioVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%

Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
String[] comandos = {"capturaObligadoSolidario", "modificaObligadoSolidario"};
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
if ( solicitud.tipoOperacion==ClientesConstants.CONSUMO || solicitud.tipoOperacion==ClientesConstants.VIVIENDA || solicitud.tipoOperacion==ClientesConstants.CREDIHOGAR || solicitud.tipoOperacion==ClientesConstants.MAX_ZAPATOS)
	comandos = new String []{"capturaObligadoSolidarioConsumo", "modificaObligadoSolidarioConsumo"};
%>
<html>
<head>
<title>Obligados Solidarios</title>
<script language="Javascript" src="js/functions.js"></script>
<script>

	function capturarObligadoSolidario(){
		window.document.forma.idObligado.value=0;
		window.document.forma.command.value='<%=comandos[0]%>';
		window.document.forma.submit();
	}

	function modificaObligadoSolidario(idObligado){
		window.document.forma.command.value='<%=comandos[1]%>';
		window.document.forma.idObligado.value=idObligado;
		window.document.forma.submit();
	}

	function capturaEconomiaObligado(idObligado){
		window.document.forma.command.value='capturaEconomiaObligado';
		window.document.forma.idObligado.value=idObligado;
		window.document.forma.submit();
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css"></head>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<center>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="">
<input type="hidden" name="idSolicitud" value="<%=solicitud.idSolicitud%>">
<input type="hidden" name="idObligado" value="0">

<!-- INICIO NUEVO CODIGO -->

<!-- FIN NUEVO CODIGO -->
<!-- INICIO DEL CODIGO ANTERIOR -->
<table border="0" width="100%">
	<tr>
	<td align="center">	   

<h3>Obligados Solidarios</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table border="0" cellpadding="0" width="600" >
	<tr bgcolor="#009865" class="whitetext">
  	<td width="90%" align="center">Nombre</td>
  	<td width="10%" align="center">Acci&oacute;n</td>
	</tr>
<%
if ( solicitud.obligadosSolidarios!=null ){
	for ( int i=0 ; i<solicitud.obligadosSolidarios.length ; i++ ){
		ObligadoSolidarioVO obligado = solicitud.obligadosSolidarios[i];
%>
	<tr>
  		<td width="90%" align="center"><%=HTMLHelper.displayField(obligado.nombre+" "+obligado.aPaterno+" "+obligado.aMaterno) %></td>
<%
		if ( request.getParameter("command").equals("capturaEconomiaObligados") || request.getParameter("command").equals("guardaEconomiaObligadoSolidario") ){ //guardaEconomiaObligadoSolidario
%>
  		<td width="10%" align="center"><a href='#' onClick='capturaEconomiaObligado(<%=obligado.idObligado%>)'>Modificar</a></td>
<%
		}else{
%>
  		<td width="10%" align="center"><a href='#' onClick='modificaObligadoSolidario(<%=obligado.idObligado%>)'>Modificar</a></td>
<%
		}
%>
	</tr>

<%
	}
}else{
%>
	<tr>
  	<td width="90%" align="center">&nbsp;</td>
  	<td width="10%" align="center">&nbsp;</td>
	</tr>
<% } %>
</table>
<%
		if ( !request.getParameter("command").equals("capturaEconomiaObligados") && !request.getParameter("command").equals("guardaEconomiaObligadoSolidario") ){
%>
<table>
	<tr>
	 <%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
		<td align="center">
			<br><input disabled type="button" value="Nuevo obligado solidario" onClick="capturarObligadoSolidario()">
		</td>
	 <%}else{ %>
	 	<td align="center">
			<br><input type="button" value="Nuevo obligado solidario" onClick="capturarObligadoSolidario()">
		</td>
	 <%} %>
	</tr>
</table>
<%
	}
%>
</td>
	</tr>
</table>
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>
