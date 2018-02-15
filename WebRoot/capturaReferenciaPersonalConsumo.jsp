<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.SolicitudVO"%>
<%@ page import="com.sicap.clientes.vo.ReferenciaPersonalVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>

<html>
<head>
<title>Referencia Personal</title>

<%
	int idReferencia = HTMLHelper.getParameterInt(request,"idReferencia");
	int idSolicitud  = HTMLHelper.getParameterInt(request,"idSolicitud");
	ReferenciaPersonalVO referenciaPersonal = new ReferenciaPersonalVO();
	ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
	int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
	String modificaReferenciasPersonal = "ACTUALIZA"; 
	SolicitudVO solicitud = new SolicitudVO();
	if ( idSolicitud!=0 ){
		solicitud = cliente.solicitudes[indiceSolicitud];
		if(solicitud.referenciasPersonales!=null && idReferencia>0){
			referenciaPersonal = solicitud.referenciasPersonales[idReferencia-1];			
		}
		else{
			referenciaPersonal = new ReferenciaPersonalVO();
			modificaReferenciasPersonal = "INSERTA";
		}
	}
	
	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
%>

<script language="Javascript" src="./js/functions.js">
<!-- 

 -->
 </script>

<script type="text/javascript">
<!--
	function guardaReferenciaPersonal(){
		var action  = window.document.forma.modificaObligadoCredito.value;
		if ( window.document.forma.nombre.value=='' || !esTexto( "^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$",window.document.forma.nombre.value)){
			alert("Debe capturar un nombre válido para la referencia");
			return false;
		}
		if ( window.document.forma.direccion.value=='' ){
			alert("Debe capturar la direccion de la referencia");
			return false;
		}
		if ( window.document.forma.telefono.value=='' || !esTelefonoValido(window.document.forma.telefono.value)){
		   alert('Debe introducir un Tel\u00e9fono (incluir clave LADA)');
		   return false;
		}
		if ( window.document.forma.relacion.value=='' ){
			alert("Debe capturar la relación de la referencia");
			return false;
		}

		if(action=='INSERTA')
			window.document.forma.command.value="capturaReferenciaPersonalConsumo";
		else{
			window.document.forma.command.value="modificaRefPerConsumo";
		}
		window.document.forma.submit();
	}
//-->
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<CENTER>

<form action="controller" method="post" name="forma">
<input type="hidden" name="command" value="">
<input type="hidden" name="idSolicitud" value="<%=idSolicitud%>">
<input type="hidden" name="idReferencia" value="<%=idReferencia%>">
<input type="hidden" name="idCliente" value="<%=cliente.idCliente%>">
<input type="hidden" name="modificaObligadoCredito" value="<%=modificaReferenciasPersonal%>">

<table border="0" width="100%">
	<tr>
	<td align="center">
<h3>Referencia Personal </h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table width="100%" border="0" cellpadding="0">
  <tr>
    <td width="50%" align="right">Nombre</td>
    <td width="50%">
      <input type="text" name="nombre" size="45" maxlength="180" value="<%=HTMLHelper.displayField(referenciaPersonal.nombre) %>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">DIrecci&oacute;n</td>
    <td width="50%">
      <input type="text" name="direccion" size="45" maxlength="100" value="<%=HTMLHelper.displayField(referenciaPersonal.direccion) %>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Tel&eacute;fono (No celular)</td>
    <td width="50%">
      <input type="text" name="telefono" size="15" maxlength="10" value="<%=HTMLHelper.displayField(referenciaPersonal.telefono) %>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Tel&eacute;fono Celular</td>
    <td width="50%">
      <input type="text" name="tiempoConocimiento" size="15" maxlength="10" value="<%=HTMLHelper.displayField(referenciaPersonal.tiempoConocimiento) %>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Horario Llamada</td>
    <td width="50%">
      <input type="text" name="horarioLlamada" size="30" maxlength="45" value="<%=HTMLHelper.displayField(referenciaPersonal.horarioLlamada) %>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Relaci&oacute;n</td>
    <td width="50%">
      <input type="text" name="relacion" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referenciaPersonal.relacion) %>">
    </td>
  </tr>
  <tr>
  	<%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
		<td align="center" colspan="2">
			<br><input disabled type="button" value="Enviar" onClick="guardaReferenciaPersonal()">
		</td>
	<%}else{ %>
		<td align="center" colspan="2">
			<br><input type="button" value="Enviar" onClick="guardaReferenciaPersonal()">
		</td>
	<%}%>
	</tr>
</table>

		</td>
	</tr>
</table>
</form>
</CENTER>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>
