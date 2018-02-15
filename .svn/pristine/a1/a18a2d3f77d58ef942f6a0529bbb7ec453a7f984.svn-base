<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.SolicitudVO"%>
<%@ page import="com.sicap.clientes.vo.ObligadoSolidarioVO"%>
<%@ page import="com.sicap.clientes.vo.DireccionVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>

<html>
<head>
<title>Clientes</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function guardaDatosOblogadoSolidario(){
		window.document.forma.command.value='guardaDatosObligadoSolidario';
		if ( window.document.forma.rfc.value=='' || !esRFCPersonaFisica(window.document.forma.rfc.value)){
		   alert('Debe introducir un RFC v\u00e1lido');
		   return false;
		}
		if ( !esHomoclaveValida(window.document.forma.rfc.value) ){
			alert('La homoclave del RFC es inv·lida');
			return false;
		}
		if ( window.document.forma.nombre.value=='' || !esTexto( "^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$",window.document.forma.nombre.value)){
		   alert('Debe introducir un Nombre v\u00e1lido');
		   return false;
		}
		if ( window.document.forma.aPaterno.value=='' || !esTexto( "^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$",window.document.forma.aPaterno.value)){
		   alert('Debe introducir un Apellido Paterno v\u00e1lido');
		   return false;
		}
		if ( window.document.forma.aMaterno.value=='' || !esTexto( "^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$",window.document.forma.aMaterno.value)){
		   alert('Debe introducir un Apellido Materno v\u00e1lido');
		   return false;
		}
		if ( window.document.forma.idColonia.value==0 ){
			alert("Debe capturar la direcci\u00f3n");
			return false;
		}
		if ( window.document.forma.calle.value==''){
		   alert('Debe introducir una Calle');
		   return false;
		}
		if ( window.document.forma.numeroExterior.value=='' || !esEntero(window.document.forma.numeroExterior.value)){
		   alert('Debe introducir un N\u00famero exterior de Domicilio');
		   return false;
		}
		if ( !esEntero(window.document.forma.telefono.value) ){
		   alert('Debe introducir un telefono v\u00e1lido');
		   return false;
		}
		if ( window.document.forma.fechaNacimiento.value!='' ){
			if ( !esFechaValida(window.document.forma.fechaNacimiento,false) ){
				alert("La Fecha de nacimiento es inv\u00e1lida");
				return false;
			}else if ( !esEdadValida(window.document.forma.fechaNacimiento.value) ){
				alert("La edad est· fuera del rango permitido");
				return false;
			}
		}else{
			alert("Debe capturar la Fecha de nacimiento");
			return false;
		}

		if ( window.document.forma.empresa.value==''){
		   alert('Debe introducir el nombre de la empresa');
		   return false;
		}
		if ( window.document.forma.puesto.value==''){
		   alert('Debe introducir el puesto');
		   return false;
		}
		if ( window.document.forma.telefonoExt.value==''){
		   alert('Debe introducir el telÈfono del trabajo');
		   return false;
		}
		if ( !esFormatoMoneda(window.document.forma.sueldoMensual.value) || esNegativo(window.document.forma.sueldoMensual.value) ){
		   alert('Debe introducir un sueldo mensual v·lido');
		   return false;
		}
		if ( window.document.forma.direccionTrabajo.value==''){
		   alert('Debe introducir la direccion del trabajo');
		   return false;
		}

		if ( window.document.forma.antiguedadDomicilio.value=='' || !esMesesAnios(window.document.forma.antiguedadDomicilio.value)){
		   alert('La Antig¸edad en el Domicilio es inv\u00e1lida');
		   return false;
		}
		window.document.forma.submit();
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css"></head>
<%
	int idObligado = HTMLHelper.getParameterInt(request, "idObligado");
	int idSolicitud  = HTMLHelper.getParameterInt(request,"idSolicitud");
	ObligadoSolidarioVO obligado = null;
	SolicitudVO solicitud = null;

	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
	ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
	int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
	solicitud = cliente.solicitudes[indiceSolicitud];
	if ( idObligado!=0 ){
		obligado = cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1];
		if ( obligado.direccion==null )
			obligado.direccion = new DireccionVO();
	}else if ( request.getAttribute("OBLIGADO_TEMPORAL")!=null ){
		Logger.debug("Obtiene el objeto ObligadoSolidarioVO temporal");
		obligado = (ObligadoSolidarioVO)request.getAttribute("OBLIGADO_TEMPORAL");
	}else{
		obligado = new ObligadoSolidarioVO();
		obligado.direccion = new DireccionVO();
	}
%>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<center>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="">
<input type="hidden" name="idCliente" value="<%=cliente.idCliente%>">
<input type="hidden" name="idSolicitud" value="<%=cliente.solicitudes[indiceSolicitud].idSolicitud%>">
<input type="hidden" name="idObligado" value="<%=obligado.idObligado%>">
<input type="hidden" name="idColonia" value="<%=HTMLHelper.displayField(obligado.direccion.idColonia)%>">
<input type="hidden" name="asentamientoCP" value="<%=HTMLHelper.displayField(obligado.direccion.asentamiento_cp)%>">
<table border="0" width="100%">
	<tr>
	<td align="center">	
<h3>Obligados Solidarios</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table border="0" cellpadding="0" width="100%" >
	<tr>
  	<td width="50%" align="right">RFC</td>
  	<td width="50%">  
  	<input type="text" name=rfc size="13" maxlength="70" value="<%=HTMLHelper.displayField(obligado.rfc)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Nombre</td>
  	<td width="50%">  
  	<input type="text" name="nombre" size="45" maxlength="70" value="<%=HTMLHelper.displayField(obligado.nombre)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Apellido paterno</td>
  	<td width="50%">  
  	<input type="text" name="aPaterno" size="45" maxlength="70" value="<%=HTMLHelper.displayField(obligado.aPaterno)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Apellido materno</td>
  	<td width="50%">  
  	<input type="text" name="aMaterno" size="45" maxlength="70" value="<%=HTMLHelper.displayField(obligado.aMaterno)%>">
  	</td>
	</tr>

	<tr>
  	<td width="100%" align="center" colspan="2"><a href="#" onClick="ayudaCodigoPostal()">Ayuda CP</a></td>
	</tr>
	<tr>
  	<td width="50%" align="right">Estado</td>
  	<td width="50%">  
  	<input type="text" name="estado" size="40" maxlength="100" value="<%=HTMLHelper.displayField(obligado.direccion.estado)%>" readonly>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Municipio</td>
  	<td width="50%">  
  	<input type="text" name="municipio" size="40" maxlength="100" value="<%=HTMLHelper.displayField(obligado.direccion.municipio)%>" readonly>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Colonia</td>
  	<td width="50%">  
  	<input type="text" name="colonia" size="40" maxlength="100" value="<%=HTMLHelper.displayField(obligado.direccion.colonia)%>" readonly>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">C&oacute;digo postal</td>
  	<td width="50%">  
  	<input type="text" name="cp" size="6" maxlength="6" value="<%=HTMLHelper.displayField(obligado.direccion.cp)%>" readonly>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Calle</td>
  	<td width="50%">  
  	<input type="text" name="calle" size="30" maxlength="60" value="<%=HTMLHelper.displayField(obligado.direccion.calle)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">N&uacute;mero exterior</td>
  	<td width="50%">  
  	<input type="text" name="numeroExterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(obligado.direccion.numeroExterior)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">N&uacute;mero interior</td>
  	<td width="50%">  
  	<input type="text" name="numeroInterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(obligado.direccion.numeroInterior)%>">
  	</td>
	</tr>
	<tr>
	  	<td width="50%" align="right">Tel&eacute;fono casa o recados</td>
	  	<td width="50%">  
	  	<input type="text" name="telefono" size="15" maxlength="15" value="<%=HTMLHelper.displayField(obligado.telefono)%>">
	  	</td>
	</tr>

	<tr>
  	<td width="50%" align="right">Nombre empresa</td>
  	<td width="50%">  
  	<input type="text" name="empresa" size="30" maxlength="70" value="<%=HTMLHelper.displayField(obligado.empresa)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Puesto</td>
  	<td width="50%">  
  	<input type="text" name="puesto" size="30" maxlength="50" value="<%=HTMLHelper.displayField(obligado.puesto)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Tel&eacute;fono/Extensi&oacute;n</td>
  	<td width="50%">  
  	<input type="text" name="telefonoExt" size="20" maxlength="20" value="<%=HTMLHelper.displayField(obligado.telefonoExt)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Sueldo mensual fijo</td>
  	<td width="50%">  
  	<input type="text" name="sueldoMensual" size="15" maxlength="9" value="<%=HTMLHelper.displayField(obligado.sueldoMensual)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Direcci&oacute;n del trabajo</td>
  	<td width="50%">  
  	<input type="text" name="direccionTrabajo" size="45" maxlength="100" value="<%=HTMLHelper.displayField(obligado.direccionTrabajo)%>">
  	</td>
	</tr>

	<tr>
  	<td width="50%" align="right">Fecha de nacimiento</td>
  	<td width="50%">  
  	<input type="text" name="fechaNacimiento" size="10" maxlength="10" value="<%=HTMLHelper.displayField(obligado.fechaNacimiento)%>">(dd/mm/aaaa)
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Antig&uuml;edad en el domicilio </td>
  	<td width="50%">  
  	<input type="text" name="antiguedadDomicilio" size="7" maxlength="7" value="<%=HTMLHelper.displayField(obligado.direccion.antDomicilio)%>"> (mm/aaaa)
  	</td>
	</tr>
	<tr>
	 <%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
		<td align="center" colspan="2">
			<br><input disabled type="button" value="Enviar" onClick="guardaDatosOblogadoSolidario()">
		</td>
	 <%}else{ %>
	 	<td align="center" colspan="2">
			<br><input type="button" value="Enviar" onClick="guardaDatosOblogadoSolidario()">
		</td>
	 <%}%>
	</tr>
</table> 

		</td>
	</tr>
</table>
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>
