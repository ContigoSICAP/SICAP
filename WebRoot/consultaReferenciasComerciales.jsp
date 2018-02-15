<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.SolicitudVO"%>
<%@ page import="com.sicap.clientes.vo.ReferenciaComercialVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>

<html>
<head>
<title>Referencias comerciales</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function capturarReferenciaComercial(){
		window.document.forma.idReferenciaComercial.value=0;
		window.document.forma.command.value='capturaReferenciaComercial';
		window.document.forma.submit();
	}

	function modificaReferenciaComercial(idReferenciaComercial){
		window.document.forma.command.value='modificaReferenciaComercial';
		window.document.forma.idReferenciaComercial.value=idReferenciaComercial;
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
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<center>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="">
<input type="hidden" name="idSolicitud" value="<%=solicitud.idSolicitud%>">
<input type="hidden" name="idReferenciaComercial" value="0">

<!-- INICIO NUEVO CODIGO -->
<!-- FIN NUEVO CODIGO -->
<!-- INICIO DEL CODIGO ANTERIOR -->
<table border="0" width="100%">
	<tr>
	<td align="center">
<h3>Referencias comerciales</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table border="0" cellpadding="0" width="600" >
	<tr bgcolor="#009865" class="whitetext">
  	<td width="90%" align="center">Nombre</td>
  	<td width="10%" align="center">Acci&oacute;n</td>
	</tr>
<%
if ( solicitud.referenciasComerciales!=null && solicitud.referenciasComerciales.length!=0 ){
	for ( int i=0 ; i<solicitud.referenciasComerciales.length ; i++ ){
		ReferenciaComercialVO referencia = solicitud.referenciasComerciales[i];
%>
	<tr>
  		<td width="90%" align="center"><%=HTMLHelper.displayField(referencia.nombre)%></td>
  		<td width="10%" align="center"><a href='#' onClick='modificaReferenciaComercial(<%=referencia.idReferencia%>)'>Modificar</a></td>
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
<table>
	<tr>
	 <%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
		<td align="center">
			<br><input disabled type="button" value="Nueva referencia comercial" onClick="capturarReferenciaComercial()">
		</td>
	 <%}else{ %>
	 	<td align="center">
			<br><input type="button" value="Nueva referencia comercial" onClick="capturarReferenciaComercial()">
		</td>
	 <%} %>
	</tr>
</table> 
</td>
	</tr>
</table>
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>
