<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.util.SolicitudUtil"%>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.SolicitudVO"%>
<%@ page import="com.sicap.clientes.vo.ArrendatarioVO"%>
<html>
<head>
<title>arrendatariodomicilio</title>
<script language="Javascript" src="./js/functions.js"></script>
<script type="text/javascript">

	function guardaArrendatario(){

		if ( window.document.forma.nombreArrendatario.value=='' || !esTexto( "^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$",window.document.forma.nombreArrendatario.value)){
		   alert('Debe introducir un Nombre v\u00e1lido');
		   return false;
		}
		if ( window.document.forma.direccion.value=='' ){
		   alert('Debe capturar la dirección del arrendatario');
		   return false;
		}
		if ( window.document.forma.telefonoArrendatario.value=='' || !esTelefonoValido(window.document.forma.telefonoArrendatario.value)){
		   alert('Debe introducir un Tel\u00e9fono (incluir clave LADA)');
		   return false;
		}
		if ( window.document.forma.relacionArrendatario.value==''){
		   alert('Debe indicar la relación con el arrendatario');
		   return false;
		}
    
		window.document.forma.submit();
	}


</script>

<link href="./css/BEtext.css" rel="stylesheet" type="text/css"></head>

<%
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
SolicitudVO solicitud = new SolicitudVO();
ArrendatarioVO arrendatario = new ArrendatarioVO();


int tipoArrendatario = 0;
tipoArrendatario = ((Integer)request.getAttribute("TIPO_ARRENDATARIO")).intValue();
if ( cliente.solicitudes!=null && idSolicitud>0 ){
	solicitud = cliente.solicitudes[indiceSolicitud];
	if ( solicitud.arrendatarioDomicilio!=null && tipoArrendatario==ClientesConstants.ARRENDATARIO_DOMICILIO )
		arrendatario = solicitud.arrendatarioDomicilio;
	if ( solicitud.arrendatarioLocal!=null && tipoArrendatario==ClientesConstants.ARRENDATARIO_LOCAL )
		arrendatario = solicitud.arrendatarioLocal;
}

%>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<CENTER>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="guardaArrendatarioConsumo">
<input type="hidden" name="idSolicitud" value="<%=idSolicitud%>">
<input type="hidden" name="tipoArrendatario" value="<%=tipoArrendatario%>">
<table border="0" width="100%">
	<tr>
	<td align="center">
<%if ( tipoArrendatario==ClientesConstants.ARRENDATARIO_DOMICILIO ){%>
	<h3>Arrendatario Domicilio</h3>
<%}else{%>
	<h3>Arrendatario Local</h3>
<%}%>
<%=HTMLHelper.displayNotifications(notificaciones)%>

<table width="100%" border="0" cellpadding="0">
  <tr>
    <td width="50%" align="right">Nombre</td>
    <td width="50%">
      <input name="nombreArrendatario" type="text" value="<%=HTMLHelper.displayField(arrendatario.nombre)%>" size="40" maxlength="100">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Direcci&oacute;n</td>
    <td width="50%">
      <input name="direccion" type="text" value="<%=HTMLHelper.displayField(arrendatario.direccion)%>" size="20" maxlength="20">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Tel&eacute;fono (No celular)</td>
    <td width="50%">
      <input name="telefonoArrendatario" type="text" value="<%=HTMLHelper.displayField(arrendatario.telefono)%>" size="10" maxlength="10">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Relacion</td>
    <td width="50%">
      <input name="relacionArrendatario" type="text" value="<%=HTMLHelper.displayField(arrendatario.relacion)%>" size="20" maxlength="20">
    </td>
  </tr>
  <tr>
  	<%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
		<td align="center" colspan="2">
			<br><input disabled type="button" value="Enviar" onClick="guardaArrendatario()">
		</td>
	<%}else{ %>
		<td align="center" colspan="2">
			<br><input type="button" value="Enviar" onClick="guardaArrendatario()">
		</td>
	<%}%>
	</tr>
</table>
</td>
	</tr>
</table>
</form>
</CENTER>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>