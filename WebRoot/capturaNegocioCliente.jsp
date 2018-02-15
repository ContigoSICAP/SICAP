<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.util.SolicitudUtil"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.vo.NegocioVO"%>
<%@ page import="com.sicap.clientes.vo.DireccionVO"%>
<%@ page import="java.util.TreeMap"%>
<html>
<head>
<title>Negocio cliente</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function guardaNegocioCliente(){
	
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
NegocioVO negocioCliente = new NegocioVO();
DireccionVO direccion = new DireccionVO();
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
if ( cliente.solicitudes[indiceSolicitud].negocio!=null  ){
	negocioCliente = cliente.solicitudes[indiceSolicitud].negocio;
	if ( negocioCliente.direccion!=null )
		direccion = negocioCliente.direccion;
}
TreeMap catActividades = CatalogoHelper.getCatalogo(ClientesConstants.CAT_ACTIVIDADES);
TreeMap catSectores = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SECTORES);
TreeMap catSituacionesLocal = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SITUACIONES_LOCAL);
TreeMap catEntornosNegocio = CatalogoHelper.getCatalogo(ClientesConstants.CAT_ENTORNOS_NEGOCIO);
TreeMap catRegistriosContables = CatalogoHelper.getCatalogo(ClientesConstants.CAT_REGISTROS_CONTABLES);
TreeMap catAutorizacionesNegocio = CatalogoHelper.getCatalogo(ClientesConstants.CAT_AUTORIZACIONES_NEGOCIO);

%>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<CENTER>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="guardaNegocioCliente">
<input type="hidden" name="idCliente" value="<%=cliente.idCliente%>">
<input type="hidden" name="idSolicitud" value="<%=cliente.solicitudes[indiceSolicitud].idSolicitud%>">
<input type="hidden" name="idColonia" value="<%=HTMLHelper.displayField(direccion.idColonia)%>">
<input type="hidden" name="asentamientoCP" value="<%=HTMLHelper.displayField(direccion.asentamiento_cp)%>">
<table border="0" width="100%">
	<tr>
	<td align="center">
<h3>Negocio Cliente </h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table width="100%" border="0" cellpadding="0">
  <tr>
    <td width="50%" align="right">Nombre comercial/Raz&oacute;n social</td>
    <td width="50%">
      <input type="text" name="razonSocial" size="40" maxlength="70" value="<%=HTMLHelper.displayField(negocioCliente.razonSocial)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">RFC</td>
    <td width="50%">
      <input type="text" name="rfc" size="13" maxlength="13" value="<%=HTMLHelper.displayField(negocioCliente.rfc)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Actividad</td>
    <td width="50%">
      <select name="actividad" size="1" >
        <%=HTMLHelper.displayCombo(catActividades,negocioCliente.activiad)%>
      </select>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Sector</td>
    <td width="50%">
      <select name="sector" size="1" >
        <%=HTMLHelper.displayCombo(catSectores,negocioCliente.sector)%>
      </select>
    </td>
  </tr>
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
      <input type="text" name="calle" size="35" maxlength="60" value="<%=HTMLHelper.displayField(direccion.calle)%>">
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
  <tr>
    <td width="50%" align="right">Tel&eacute;fono</td>
    <td width="50%">
      <input type="text" name="telefono" size="10" maxlength="10" value="<%=HTMLHelper.displayField(negocioCliente.telefono)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Celular</td>
    <td width="50%">
      <input type="text" name="telefonoCelular" size="10" maxlength="10" value="<%=HTMLHelper.displayField(negocioCliente.telefonoCelular)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Fecha de visita al negocio</td>
    <td width="50%">
      <input type="text" name="fechaVisita" size="10" maxlength="10" value="<%=HTMLHelper.displayField(negocioCliente.fechaVisita)%>">
      (dd/mm/aaaa) </td>
  </tr>
  <tr>
    <td width="50%" align="right">Tiempo de experiencia en el negocio</td>
    <td width="50%">
      <input type="text" name="tiempoExperiencia" size="7" maxlength="7" value="<%=HTMLHelper.displayField(negocioCliente.tiempoExperiencia)%>">
      (mm/aaaa) </td>
  </tr>
  <tr>
    <td width="50%" align="right">N&uacute;mero de empleados</td>
    <td width="50%">
      <input type="text" name="empleados" size="5" maxlength="5" value="<%=HTMLHelper.displayField(negocioCliente.empleados)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Ventas (Porcentaje de Contado)</td>
    <td width="50%">
      <input type="text" name="ventasContado" size="5" maxlength="3" value="<%=HTMLHelper.displayField(negocioCliente.ventasContado)%>">
      (%) </td>
  </tr>
  <tr>
    <td width="50%" align="right">Ventas (Porcentaje de Cr&eacute;dito)</td>
    <td width="50%">
      <input type="text" name="ventasCredito" size="5" maxlength="3" value="<%=HTMLHelper.displayField(negocioCliente.ventasCredito)%>">
      (%) </td>
  </tr>
  <tr>
    <td width="50%" align="right">Compras (Porcentaje de Contado)</td>
    <td width="50%">
      <input type="text" name="comprasContado" size="5" maxlength="3" value="<%=HTMLHelper.displayField(negocioCliente.comprasContado)%>">
      (%) </td>
  </tr>
  <tr>
    <td width="50%" align="right">Compras (Porcentaje de Cr&eacute;dito)</td>
    <td width="50%">
      <input type="text" name="comprasCredito" size="5" maxlength="3" value="<%=HTMLHelper.displayField(negocioCliente.comprasCredito)%>">
      (%) </td>
  </tr>
  <tr>
    <td width="50%" align="right">Situaci&oacute;n del local</td>
    <td width="50%">
      <select name="situacionLocal" size="1" >
        <%=HTMLHelper.displayCombo(catSituacionesLocal,negocioCliente.situacionLocal)%>
      </select>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Entorno del negocio</td>
    <td width="50%">
      <select name="entornoNegocio" size="1" >
        <%=HTMLHelper.displayCombo(catEntornosNegocio,negocioCliente.entornoNegocio)%>
      </select>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Registros contables</td>
    <td width="50%">
      <select name="registrosContables" size="1" >
        <%=HTMLHelper.displayCombo(catRegistriosContables,negocioCliente.registrosContables)%>
      </select>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Autorizaciones del negocio</td>
    <td width="50%">
      <select name="autorizacionesNegocio" size="1" >
        <%=HTMLHelper.displayCombo(catAutorizacionesNegocio,negocioCliente.autorizacionesNegocio)%>
      </select>
    </td>
  </tr>
  <tr>
  	<%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
		<td align="center" colspan="2">
			<br><input disabled type="button" value="Enviar" onClick="guardaNegocioCliente()">
		</td>
	<%}else{ %>
		<td align="center" colspan="2">
			<br><input type="button" value="Enviar" onClick="guardaNegocioCliente()">
		</td>
	<%} %>
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
