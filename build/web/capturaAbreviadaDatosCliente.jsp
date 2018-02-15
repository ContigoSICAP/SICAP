<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.TreeMap"%>
<%
	boolean esMicrocredito = false;
	SolicitudVO solicitud = new SolicitudVO();
	NegocioVO negocio = new NegocioVO();
	DireccionVO direccion = new DireccionVO();
	
	ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
	ClienteVO clienteAyuda = (ClienteVO)session.getAttribute("ayudarfc");
	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
	TreeMap catEstadoCivil = CatalogoHelper.getCatalogo(ClientesConstants.CAT_ESTADO_CIVIL);
	TreeMap catSituacionVivienda = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SITUACION_VIVIENDA);
	TreeMap catSituacionLocal = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SITUACIONES_LOCAL);
	int idSolicitud = 0;
	int indiceSolicitud = 0;
	if ( request.getAttribute("ID_SOLICITUD")!=null ){
		idSolicitud = ((Integer)request.getAttribute("ID_SOLICITUD")).intValue();
		indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
	}else{
		indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, HTMLHelper.getParameterInt(request, "idSolicitud"));
	}
	solicitud = cliente.solicitudes[indiceSolicitud];
	if(cliente.direcciones!=null)
		direccion = cliente.direcciones[0];
	if ( clienteAyuda != null ){
		cliente.nombre = clienteAyuda.nombre;
		cliente.aPaterno = clienteAyuda.aPaterno;
		cliente.aMaterno = clienteAyuda.aMaterno;
		cliente.fechaNacimiento = clienteAyuda.fechaNacimiento;
	}
	if ( solicitud.tipoOperacion==ClientesConstants.MICROCREDITO ){
		esMicrocredito = true;
		if ( solicitud.negocio!=null ){
			negocio = solicitud.negocio;
		}
	}
%>

<html>
<head>
<title>Alta/Modificaci&oacute;n de cliente</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function guardaDatosCliente(){
		window.document.forma.command.value='guardaDatosAbreviadaCliente';
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
		if ( window.document.forma.fechaNacimiento.value!='' ){
			if ( !esFechaValida(window.document.forma.fechaNacimiento, false) ){
				alert("La Fecha de nacimiento es inv\u00e1lida");
				return false;
			}else if ( !esEdadValida(window.document.forma.fechaNacimiento.value, 2) ){
				alert("La edad del cliente est· fuera del rango permitido");
				return false;
			}
		}else{
			alert("Debe capturar la Fecha de nacimiento");
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

<%if ( solicitud.tipoOperacion==ClientesConstants.MICROCREDITO ){%>

		if ( window.document.forma.estadoCivil.value==0 ){
			alert('Debe seleccionar Estado Civil');
			return false;
		}
		if ( window.document.forma.situacionVivienda.value==0 ){
			alert('Debe seleccionar Situaci\u00f3n de la Vivienda');
			return false;
		}
		if ( window.document.forma.montoSolicitado.value!='' ){
			if ( !esMontoValido(window.document.forma.montoSolicitado.value, 2) ){
				alert("El monto es inv\u00e1lido");
				return false;
			}
		}else{
			alert("Debe capturar el monto");
			return false;
		}
		if ( window.document.forma.situacionLocal.value==0 ){
			alert('Debe seleccionar la situaciÛn del local');
			return false;
		}
<%}%>
		window.document.forma.submit();

	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<center>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="">
<input type="hidden" name="idSolicitud" value="<%=solicitud.idSolicitud%>">
<input type="hidden" name="idColonia" value="<%=HTMLHelper.displayField(direccion.idColonia)%>">
<input type="hidden" name="asentamientoCP" value="<%=HTMLHelper.displayField(direccion.asentamiento_cp)%>">

<!-- INICIO DEL CODIGO ANTERIOR -->
<table border="0" width="100%">
	<tr>
	<td align="center">
<h3>Alta de cliente</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table border="0" cellpadding="0" width="100%">
	<tr>
  	<td width="50%" align="right">N&uacute;mero de cliente</td>
  	<td width="50%">  
  	<input type="text" name="idCliente" size="10" value="<%=cliente.idCliente%>" readonly class="soloLectura">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">RFC</td>
  	<td width="50%">  
  	<input type="text" name="rfc" size="13" maxlength="13" value="<%=cliente.rfc%>" readonly class="soloLectura">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Nombres</td>
  	<td width="50%">  
  	<input type="text" name="nombre" size="45" maxlength="70" value="<%=HTMLHelper.displayField(cliente.nombre)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Apellido paterno</td>
  	<td width="50%">  
  	<input type="text" name="aPaterno" size="45" maxlength="70" value="<%=HTMLHelper.displayField(cliente.aPaterno)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Apellido materno</td>
  	<td width="50%">  
  	<input type="text" name="aMaterno" size="45" maxlength="70" value="<%=HTMLHelper.displayField(cliente.aMaterno)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Fecha de nacimiento</td>
  	<td width="50%">  
  	<input type="text" name="fechaNacimiento" size="10" maxlength="10" value="<%=HTMLHelper.displayField(cliente.fechaNacimiento)%>">(dd/mm/aaaa)
  	</td>
	</tr>
<%if ( esMicrocredito ){%>
	<tr>
  	<td width="50%" align="right">Edad</td>
  	<td width="50%">  
  	<input type="text" name="edad" size="5" value="<%=FechasUtil.calculaEdad(cliente.fechaNacimiento)%>" readonly="readonly" class="soloLectura">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Estado civil</td>
  	<td width="50%">  
  	<select name="estadoCivil" size="1">
		<%=HTMLHelper.displayCombo(catEstadoCivil,cliente.estadoCivil)%>
	</select>
  	</td>
	</tr>
<%}%>
	<tr>
  	<td width="100%" align="center" colspan="2"><a href="#" onClick="ayudaCodigoPostal()">Ayuda CP</a></td>
	</tr>
	<tr>
  	<td width="50%" align="right">Estado</td>
  	<td width="50%">  
  	<input type="text" name="estado" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.estado)%>" readonly>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Municipio</td>
  	<td width="50%">  
  	<input type="text" name="municipio" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.municipio)%>" readonly>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Colonia</td>
  	<td width="50%">  
  	<input type="text" name="colonia" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.colonia)%>" readonly>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">C&oacute;digo postal</td>
  	<td width="50%">  
  	<input type="text" name="cp" size="6" maxlength="6" value="<%=HTMLHelper.displayField(direccion.cp)%>" readonly>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Calle</td>
  	<td width="50%">  
  	<input type="text" name="calle" size="30" maxlength="60" value="<%=HTMLHelper.displayField(direccion.calle)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">N&uacute;mero exterior</td>
  	<td width="50%">  
  	<input type="text" name="numeroExterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(direccion.numeroExterior)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">N&uacute;mero interior</td>
  	<td width="50%">  
  	<input type="text" name="numeroInterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(direccion.numeroInterior)%>">
  	</td>
	</tr>
<%if ( esMicrocredito ){%>
	<tr>
  	<td width="50%" align="right">Situaci&oacute;n de la vivienda</td>
  	<td width="50%">  
  	<select name="situacionVivienda" size="1">
			<%=HTMLHelper.displayCombo(catSituacionVivienda,direccion.situacionVivienda)%>
	</select>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Monto solicitado</td>
  	<td width="50%">  
  	<input type="text" name="montoSolicitado" size="10" maxlength="11" value="<%=HTMLHelper.formatoMonto(solicitud.montoSolicitado)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Situaci&oacute;n del local</td>
  	<td width="50%">  
  	<select name="situacionLocal" size="1">
		<%=HTMLHelper.displayCombo(catSituacionLocal, negocio.situacionLocal)%>
	</select>
  	</td>
	</tr>
<%}%>
	<tr>
		<td align="center" colspan="2">
		<%if (solicitud.desembolsado!=ClientesConstants.DESEMBOLSADO){%>		
			<br><input type="button" value="Enviar" onClick="guardaDatosCliente()">
		<%}else{%>
			<br><input type="button" value="Enviar" onClick="guardaDatosCliente()" disabled="disabled">
		<%}%>
		</td>
	</tr>
</table>
		</td>
	</tr>
</table>
<!-- FIN DEL CODIGO ANTERIOR -->		
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>