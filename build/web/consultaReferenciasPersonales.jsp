<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.SolicitudVO"%>
<%@ page import="com.sicap.clientes.vo.ReferenciaPersonalVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%
	int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
	ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
	int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
	SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
	String commandCaptura = "referenciaPersonal";
	String commandModif = "modificaReferenciaPersonal";
 	if ( solicitud!=null && (solicitud.tipoOperacion==ClientesConstants.CONSUMO || solicitud.tipoOperacion==ClientesConstants.VIVIENDA || solicitud.tipoOperacion==ClientesConstants.CREDIHOGAR || solicitud.tipoOperacion==ClientesConstants.MAX_ZAPATOS || solicitud.tipoOperacion==ClientesConstants.SELL_FINANCE)){
		commandCaptura = "referenciaPersonalConsumo";
		commandModif = "modificaReferenciaPersonalConsumo";
	}
%>
<html>
<head>
<title>Referencias Personales</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function capturarReferenciaPersonal(){
		window.document.forma.idReferencia.value=0;
		window.document.forma.command.value='<%=commandCaptura%>';
		window.document.forma.submit();
	}

	function modificaReferenciaPersonal(idReferencia){
		window.document.forma.command.value='<%=commandModif%>';
		window.document.forma.idReferencia.value=idReferencia;
		window.document.forma.submit();
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<CENTER>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="">
<input type="hidden" name="idSolicitud" value="<%=idSolicitud%>">
<input type="hidden" name="idReferencia" value="0">
<table border="0" width="100%">
	<tr>
	<td align="center">	
<h3>Referencias Personales</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table border="0" cellpadding="0" width="600" >
	<tr  bgcolor="#009865" class="whitetext">
  	<td width="90%" align="center">Nombre</td>
  	<td width="10%" align="center">Acci&oacute;n</td>
	</tr>
<%
if ( solicitud.referenciasPersonales!=null && solicitud.referenciasPersonales.length!=0 ){
	for ( int i=0 ; i<solicitud.referenciasPersonales.length ; i++ ){
		ReferenciaPersonalVO referencia = solicitud.referenciasPersonales[i];
%>
	<tr>
  		<td width="90%" align="center"><%=HTMLHelper.displayField(referencia.nombre)%></td>
  		<td width="10%" align="center"><a href='#' onClick='modificaReferenciaPersonal(<%=referencia.idReferencia%>)'>Modificar</a></td>
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
			<br><input disabled type="button" value="Nueva referencia Personal" onClick="capturarReferenciaPersonal()">
		</td>
	  <%}else{ %>
	  	<td align="center">
			<br><input type="button" value="Nueva referencia Personal" onClick="capturarReferenciaPersonal()">
		</td>
	  <%} %>	  
	</tr>
</table> 
</td>
	</tr>
</table>
</form>
</CENTER>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>