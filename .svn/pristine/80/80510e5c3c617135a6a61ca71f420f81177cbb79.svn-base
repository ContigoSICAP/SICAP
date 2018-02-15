<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>

<%
Notification notificaciones[] = (Notification[])request.getAttribute("NOTIFICACIONES");
ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];

%>

<html>
<head>
<title>Disposiciones</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function muestraPagareMaxzapatos(idDisposicion){
		params ="?command=muestraPagareMaxzapatos&idDisposicion="+idDisposicion+"&idSolicitud="+<%=idSolicitud%>;
		url = "/CEC/controller";
		abreVentana(url + params,'scrollbars=yes,titlebar=no',400 ,500 ,true, 0, 0);
	}

	function capturaDisposicion(){
		window.document.forma.command.value="capturaDisposicion";
		window.document.forma.submit();
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css"></head>


<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<CENTER>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="guardaDisposiciones">
<input type="hidden" name="idCliente" value="<%=cliente.idCliente%>">
<input type="hidden" name="idSolicitud" value="<%=solicitud.idSolicitud%>">

<table border="0" width="100%">
	<tr>
		<td align="center">
<h3>Disposiciones</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table border="1" cellpadding="0" width="60%">
	<tr>
		<td align="center" width="10%">Número</td>
		<td align="center" width="30%">Fecha</td>
		<td align="center" width="30%">Monto</td>
		<td align="center" width="30%">Pagaré</td>
	</tr>
<%if ( solicitud.disposiciones!=null && solicitud.disposiciones.length>0 ){ %>

<%for( int i=0 ; i<solicitud.disposiciones.length ; i++ ){%>
	<tr>
		<td align="center"><%=solicitud.disposiciones[i].idDisposicion%></td>
		<td align="center"><%=Convertidor.dateToString(solicitud.disposiciones[i].fechaCaptura, ClientesConstants.FORMATO_CORTO_FECHA_HORA) %></td>
		<td align="right"><%=solicitud.disposiciones[i].monto%></td>
		<td align="center"><a href="#" onclick="muestraPagareMaxzapatos(<%=solicitud.disposiciones[i].idDisposicion%>)">Ver pagaré</a></td>
	</tr>
<%}%>

<%}%>
</table> 
<br><input type="button" value="Nueva disposición" onClick="capturaDisposicion()">
<!-- INICIO NUEVO CODIGO -->
		</td>
	</tr>
</table>
<!-- FIN NUEVO CODIGO -->

<br>
</form>
</CENTER>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>