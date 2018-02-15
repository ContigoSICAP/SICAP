<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.util.SolicitudUtil"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.vo.ViviendaVO"%>
<%@ page import="java.util.TreeMap"%>
<html>
<head>
<title>Vivienda cliente</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function guardaViviendaCliente(){
		window.document.forma.command.value = "guardaViviendaCliente";
		window.document.forma.submit();
	}

	function guardaEmpleoCliente1(){
	
		if ( window.document.forma.razonSocial.value==''){
		   alert('Debe introducir un Nombre o Razón Social v\u00e1lido');
		   return false;
		}
		//if ( window.document.forma.rfc.value=='' || !esRFCPersonaFisica(window.document.forma.rfc.value)){
		   //alert('Debe introducir un RFC v\u00e1lido');
		   //return false;
		//}
		if ( window.document.forma.actividad.value==0 ){
			alert('Debe seleccionar Actividad');
			return false;
		}
		//if ( window.document.forma.sector.value==0 ){
			//alert('Debe seleccionar un Sector');
			//return false;
		//}
		if ( window.document.forma.calle.value==''){
		   alert('Debe introducir una Calle');
		   return false;
		}
		if ( window.document.forma.numeroExterior.value=='' || !esEntero(window.document.forma.numeroExterior.value)){
			alert('Debe introducir un N\u00famero exterior de Domicilio');
			return false;
		}
		//if ( window.document.forma.telefono.value=='' || !esEntero(window.document.forma.telefono.value)){
		   //alert('Debe introducir un Tel\u00e9fono');
		   //return false;
		//}
		if ( window.document.forma.fechaVisita.value!='' ){
			if ( !esFechaValida(window.document.forma.fechaVisita,false) ){
				alert("La Fecha de visita al negocio es inv\u00e1lida");
				return false;
			}
		}else{
			alert("Debe capturar la Fecha de visita al negocio");
			return false;
		}    
		if ( window.document.forma.tiempoExperiencia.value=='' || !esMesesAnios(window.document.forma.tiempoExperiencia.value)){
		   alert('El tiempo de Experiencia es inv\u00e1lido');
		   return false;
		}
		if ( window.document.forma.empleados.value=='' || !esEntero(window.document.forma.empleados.value)){
		   alert('Debe introducir un Número de empleados');
		   return false;
		}
		if ( window.document.forma.ventasContado.value=='' || !esFormatoPorcentaje(window.document.forma.ventasContado.value)){
		   alert('El porcentaje de Contado de Ventas es inválido');
		   return false;
		}
		if ( window.document.forma.ventasCredito.value=='' || !esFormatoPorcentaje(window.document.forma.ventasCredito.value)){
		   alert('El porcentaje de Crédito de Ventas es inválido');
		   return false;
		}
		if ( window.document.forma.comprasContado.value=='' || !esFormatoPorcentaje(window.document.forma.comprasContado.value)){
		   alert('El porcentaje de Contado de Compras es inválido');
		   return false;
		}
		if ( window.document.forma.comprasCredito.value=='' || !esFormatoPorcentaje(window.document.forma.comprasCredito.value)){
		   alert('El porcentaje de Crédito de Compras es inválido');
		   return false;
		}
		if ( window.document.forma.situacionLocal.value==0 ){
			alert('Debe seleccionar la situación del local');
			return false;
		}
		if ( window.document.forma.entornoNegocio.value==0 ){
			alert('Debe seleccionar el entorno del negocio');
			return false;
		}
		if ( window.document.forma.registrosContables.value==0 ){
			alert('Debe seleccionar los registros contables');
			return false;
		}
		if ( window.document.forma.autorizacionesNegocio.value==0 ){
			alert('Debe seleccionar las autorizaciones del negocio');
			return false;
		}
		window.document.forma.submit();
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<%
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
ViviendaVO vivienda = new ViviendaVO();
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
if ( cliente.solicitudes[indiceSolicitud].vivienda!=null  ){
	vivienda = cliente.solicitudes[indiceSolicitud].vivienda;
}
TreeMap catTipoVivienda = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SITUACION_VIVIENDA);
TreeMap catNivelVivienda = CatalogoHelper.getCatalogo(ClientesConstants.CAT_NIVEL_VIVIENDA);
TreeMap catCalifZona = CatalogoHelper.getCatalogo(ClientesConstants.CAT_CALIF_ZONA);
TreeMap catTamPisos = CatalogoHelper.getCatalogo(ClientesConstants.CAT_PISOS_VIV);
TreeMap catTamCuartos = CatalogoHelper.getCatalogo(ClientesConstants.CAT_CUARTOS_VIV);
TreeMap catCaractFachada = CatalogoHelper.getCatalogo(ClientesConstants.CAT_CARACT_FACHADA);
TreeMap catCaractTecho = CatalogoHelper.getCatalogo(ClientesConstants.CAT_CARACT_TECHO);
TreeMap catTiempoResid = CatalogoHelper.getCatalogo(ClientesConstants.CAT_TIEMPO_RESIDENCIA);
%>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<center>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="">
<input type="hidden" name="idCliente" value="<%=cliente.idCliente%>">
<input type="hidden" name="idSolicitud" value="<%=cliente.solicitudes[indiceSolicitud].idSolicitud%>">

<table border="0" width="100%">
	<tr>
	<td align="center">	   

<h3>Vivienda</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table width="100%" border="0" cellpadding="0">
	<tr>
		<td width="50%" align="right">Tipo de vivienda</td>
		<td width="50%">
			<select name="tipoVivienda" size="1" >
				<%=HTMLHelper.displayCombo(catTipoVivienda, vivienda.tipoVivienda)%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Importe alquiler/hipoteca</td>
		<td width="50%">
			<input type="text" name="impAlquilerHipoteca" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(vivienda.impAlquilerHipoteca)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Niviel de la vivienda</td>
		<td width="50%">
			<select name="nivelVivienda" size="1" >
				<%=HTMLHelper.displayCombo(catNivelVivienda, vivienda.nivelVivienda)%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Calificacion de la zona</td>
		<td width="50%">
			<select name="zona" size="1" >
				<%=HTMLHelper.displayCombo(catCalifZona, vivienda.zona)%>
			</select>
		</td>
	</tr>

	<tr>
		<td width="50%" align="right">Tamaño de vivienda (pisos)</td>
		<td width="50%">
			<select name="pisos" size="1" >
				<%=HTMLHelper.displayCombo(catTamPisos, vivienda.pisos)%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Tamaño de vivienda (cuartos)</td>
		<td width="50%">
			<select name="cuartos" size="1" >
				<%=HTMLHelper.displayCombo(catTamCuartos, vivienda.cuartos)%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Caracteristicas de la fachada</td>
		<td width="50%">
			<select name="fachada" size="1" >
				<%=HTMLHelper.displayCombo(catCaractFachada, vivienda.fachada)%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Caracteristicas techo</td>
		<td width="50%">
			<select name="techo" size="1" >
				<%=HTMLHelper.displayCombo(catCaractTecho, vivienda.techo)%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Tiempo de residencia</td>
		<td width="50%">
			<select name="tiempoResidencia" size="1" >
				<%=HTMLHelper.displayCombo(catTiempoResid, vivienda.tiempoResidencia)%>
			</select>
		</td>
	</tr>
	<tr>
	 <%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
		<td align="center" colspan="2">
			<br><input disabled type="button" value="Enviar" onClick="guardaViviendaCliente()">
		</td>
	 <%}else{%>
	 	<td align="center" colspan="2">
			<br><input type="button" value="Enviar" onClick="guardaViviendaCliente()">
		</td>
	 <%} %>
	</tr>
</table>

		</td>
	</tr>
</table>
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>
