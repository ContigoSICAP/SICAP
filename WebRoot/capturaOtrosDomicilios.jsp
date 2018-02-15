<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.TreeMap"%>
<%
	SolicitudVO solicitud = new SolicitudVO();
	DireccionVO[] direccion = new DireccionVO[3];
	for ( int i=0 ; i<direccion.length ; i++ ) {
		direccion[i] = new DireccionVO();
	}
	ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
	
	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
	TreeMap catTipoVivienda = CatalogoHelper.getCatalogo(ClientesConstants.CAT_TIPO_VIVIENDA);

	int idSolicitud = 0;
	int indiceSolicitud = 0;
	if ( request.getAttribute("ID_SOLICITUD")!=null ){
		idSolicitud = ((Integer)request.getAttribute("ID_SOLICITUD")).intValue();
		indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
	}else{
		indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, HTMLHelper.getParameterInt(request, "idSolicitud"));	
	}
	int j=1;
	solicitud = cliente.solicitudes[indiceSolicitud];
	if(cliente.direcciones!=null){
		for ( int i=0 ; i<cliente.direcciones.length ; i++ ) {
			if(cliente.direcciones[i]!=null){	
				if(cliente.direcciones[i].idDireccion != 1 && j!=3){
					direccion[j] = cliente.direcciones[i];
					j++;
				}
			}	
		}
	}
%>

<html>
<head>
<title>Alta/Modificaci&oacute;n de cliente</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function guardaOtrosDomicilios(){
		window.document.forma.command.value='guardaOtrosDomicilios';
		
		if ( window.document.forma.colonia.value==0 ){
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
		if ( window.document.forma.situacionVivienda.value==0 ){
			alert('Debe seleccionar el tipo de domicilio');
			return false;
		}
		
		if(window.document.forma.coloniaTerceraDir.value!=0){
			if ( window.document.forma.situacionViviendaTerceraDir.value==0 ){
				alert('Debe seleccionar el tipo de domicilio de la segunda direccion');
				return false;
			}
			if ( window.document.forma.situacionViviendaTerceraDir.value==window.document.forma.situacionVivienda.value ){
				alert('No puede seleccionar el mismo tipo de domicilio para las dos direcciones, elija diferentes para ambos.');
				return false;
			}
		}
		
		window.document.forma.submit();

	}
	function ayudaCodigoPostalTerceraDireccion(){
		params ="?command=buscaCodigoPostalSegundaDir";
		url = "/CEC/controller";
		abreVentana(url + params,'scrollbars=yes',550 ,210 ,true, 0, 0);
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
<input type="hidden" name="idColonia" value="<%=HTMLHelper.displayField(direccion[1].idColonia)%>">
<input type="hidden" name="asentamientoCP" value="<%=HTMLHelper.displayField(direccion[1].asentamiento_cp)%>">
<input type="hidden" name="idColoniaTerceraDir" value="<%=HTMLHelper.displayField(direccion[2].idColonia)%>">
<input type="hidden" name="asentamientoCPTerceraDir" value="<%=HTMLHelper.displayField(direccion[2].asentamiento_cp)%>">

<table border="0" width="100%">
	<tr>
	<td align="center">
<h3>Otros domicilios</h3>
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
  	<td width="100%" align="center" colspan="2"><a href="#" onClick="ayudaCodigoPostal()">Ayuda CP</a></td>
	</tr>
	<tr>
  	<td width="50%" align="right">Estado</td>
  	<td width="50%">  
  	<input type="text" name="estado" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion[1].estado)%>" readonly>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Municipio</td>
  	<td width="50%">  
  	<input type="text" name="municipio" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion[1].municipio)%>" readonly>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Colonia</td>
  	<td width="50%">  
  	<input type="text" name="colonia" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion[1].colonia)%>" readonly>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">C&oacute;digo postal</td>
  	<td width="50%">  
  	<input type="text" name="cp" size="6" maxlength="6" value="<%=HTMLHelper.displayField(direccion[1].cp)%>" readonly>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Calle</td>
  	<td width="50%">  
  	<input type="text" name="calle" size="30" maxlength="60" value="<%=HTMLHelper.displayField(direccion[1].calle)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">N&uacute;mero exterior</td>
  	<td width="50%">  
  	<input type="text" name="numeroExterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(direccion[1].numeroExterior)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">N&uacute;mero interior</td>
  	<td width="50%">  
  	<input type="text" name="numeroInterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(direccion[1].numeroInterior)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Tipo de domicilio</td>
  	<td width="50%">  
  	<select name="situacionVivienda" size="1">
			<%=HTMLHelper.displayCombo(catTipoVivienda,direccion[1].idDireccion)%>
	</select>
  	</td>
	</tr>
	<tr>
  	<td width="100%" align="center" colspan="2"><a href="#" onClick="ayudaCodigoPostalTerceraDireccion()">Ayuda CP</a></td>
	</tr>
	<tr>
  	<td width="50%" align="right">Estado</td>
  	<td width="50%">  
  	<input type="text" name="estadoTerceraDir" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion[2].estado)%>" readonly>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Municipio</td>
  	<td width="50%">  
  	<input type="text" name="municipioTerceraDir" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion[2].municipio)%>" readonly>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Colonia</td>
  	<td width="50%">  
  	<input type="text" name="coloniaTerceraDir" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion[2].colonia)%>" readonly>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">C&oacute;digo postal</td>
  	<td width="50%">  
  	<input type="text" name="cpTerceraDir" size="6" maxlength="6" value="<%=HTMLHelper.displayField(direccion[2].cp)%>" readonly>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Calle</td>
  	<td width="50%">  
  	<input type="text" name="calleTerceraDir" size="30" maxlength="60" value="<%=HTMLHelper.displayField(direccion[2].calle)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">N&uacute;mero exterior</td>
  	<td width="50%">  
  	<input type="text" name="numeroExteriorTerceraDir" size="10" maxlength="10" value="<%=HTMLHelper.displayField(direccion[2].numeroExterior)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">N&uacute;mero interior</td>
  	<td width="50%">  
  	<input type="text" name="numeroInteriorTerceraDir" size="10" maxlength="10" value="<%=HTMLHelper.displayField(direccion[2].numeroInterior)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Tipo de domicilio</td>
  	<td width="50%">  
  	<select name="situacionViviendaTerceraDir" size="1">
			<%=HTMLHelper.displayCombo(catTipoVivienda,direccion[2].idDireccion)%>
	</select>
  	</td>
	</tr>
	<tr>
		<td align="center" colspan="2">
		<%if (solicitud.desembolsado!=ClientesConstants.DESEMBOLSADO){%>		
			<br><input type="button" value="Enviar" onClick="guardaOtrosDomicilios()">
		<%}else{%>
			<br><input type="button" value="Enviar" onClick="guardaOtrosDomicilios()" disabled="disabled">
		<%}%>
		</td>
	</tr>
</table>
		</td>
	</tr>
</table>	
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>