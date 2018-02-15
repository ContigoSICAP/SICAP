<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="java.util.TreeMap"%>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.util.SolicitudUtil"%>
<%@ page import="com.sicap.clientes.vo.ScoringVO"%>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<jsp:directive.page import="com.sicap.clientes.util.FormatUtil"/>
<%
ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
int idSolicitud = HTMLHelper.getParameterInt(request,"idSolicitud");
if ( idSolicitud==0 ){
	if ( request.getAttribute("ID_SOLICITUD")!=null )
		idSolicitud = ((Integer)request.getAttribute("ID_SOLICITUD")).intValue();
}

int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
TreeMap catBusquedas = CatalogoHelper.getCatalogo(ClientesConstants.CAT_NUM_BUSQUEDA_CTA);
TreeMap catDictamen = CatalogoHelper.getCatalogoDictamen();
TreeMap catDictamenCapPago = CatalogoHelper.getCatalogoDictamenCapPago();
TreeMap catFrecuenciasPago = CatalogoHelper.getCatalogoFrecuenciasPago(solicitud.tipoOperacion);


//TreeMap catPlazoContrato = CatalogoHelper.getCatalogo(ClientesConstants.CAT_PLAZOS_CONTRATO);


TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(solicitud.tipoOperacion);
TreeMap catTasas = CatalogoHelper.getCatalogoTasas(solicitud.tipoOperacion);

ScoringVO scoring = new ScoringVO();
if ( idSolicitud==0 )
	idSolicitud = 1;
if ( cliente.solicitudes[indiceSolicitud].scoring!=null ){
	scoring = cliente.solicitudes[indiceSolicitud].scoring;
} 

//String path = request.getContextPath();
//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
  <head>
    <title>Calcula scoring</title>
    <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    <script language="Javascript" src="./js/functions.js"></script>
	<script>

	function obtenCalculo(){
		window.document.forma.command.value='obtenCapacidadPago';
		window.document.forma.submit();
	}

	</script>
  </head>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="obtenCalculoDescuentoNom">
<input type="hidden" name="idCliente" value="<%=cliente.idCliente%>">
<input type="hidden" name="idSolicitud" value="<%=idSolicitud%>">

<table cellspacing="1" cellpadding="0" width="100%" bgcolor="white">
  <tr>
    <td width="40%" valign="top">
			<table bgcolor="white" width="100%">
    <tr>
    	<table width="100%">
			<tr>
			  <td colspan ="2" align="center">Capacidad de Pago</td>
			</tr>
			<tr>
			  <td width="50%" align="right">Ingresos n&oacute;mina</td>
			  <td width="50%">
			  	<input type="text" name="ingresosNomina" size="12" maxlength="9" value="<%=HTMLHelper.formatoMonto(scoring.ingresosNomina)%>">
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Disponible<br></td>
			  <td width="50%">
			  	<input type="text" name="disponibleMensual" size="12" maxlength="9" readonly="readonly" value="<%=HTMLHelper.formatoMonto(scoring.disponibleMensual)%>" class="soloLectura">
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Cuota sobre disponible<br></td>
			  <td width="50%">
			  	<input type="text" name="cuotaSobreDisponible" size="12" maxlength="9" readonly="readonly" value="<%=HTMLHelper.formatoMonto(scoring.cuotaSobreDisponible)%>" class="soloLectura">
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Cuota sobre ingreso bruto<br></td>
			  <td width="50%">
			  	<input type="text" name="cuotaSobreIngresoBruto" size="12" maxlength="9" readonly="readonly" value="<%=HTMLHelper.formatoMonto(scoring.cuotaSobreIngresoBruto)%>" class="soloLectura">
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Cobertura de pago<br></td>
			  <td width="50%">
			  	<input type="text" name="capacidadPago" size="12" readonly="readonly" value="<%=HTMLHelper.getDescripcion(catDictamenCapPago,scoring.coberturaPago)%>" class="soloLectura">
			  </td>
			</tr>
    	</table>
    </td>
  </tr>
			</table>
	 	</td>
		<!-- COLUMNA 2  -->	 	
		<td width="40%" bgcolor="white" valign="top">
		<table border="0" cellpadding="0" width="100%">
	<tr>
    	<table width="100%">
			<tr>
			  <td colspan ="2" align="center">Perfil de la operaci&oacute;n</td>
			</tr>
			<tr>
			  <td width="50%" align="right">Monto</td>
			  <td width="50%">
			  	<input type="text" name="monto" size="12" maxlength="9" value="<%=HTMLHelper.formatoMonto(scoring.monto)%>">
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">No. de Pagos</td>
			  <td width="50%">
			  	<input type="text" name="plazo" size="12" maxlength="2" value="<%=HTMLHelper.displayField(scoring.plazo)%>">
			  </td>
			</tr>
			<tr>
  				<td width="50%" align="right">Frecuencia de pago</td>
  				<td width="50%">  
  			<select name="periodicidad" size="1">
				<%=HTMLHelper.displayCombo(catFrecuenciasPago,scoring.periodicidad)%>
			</select>
  			</td>
			</tr>
			<tr>
			  <td width="50%" align="right">Monto con comision</td>
			  <td width="50%">
			  	<input type="text" name="montoConComision" size="12" maxlength="9" value="<%=HTMLHelper.formatoMonto(scoring.montoConComision)%>" readonly="readonly" class="soloLectura">
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Cuota</td>
			  <td width="50%">
			  	<input type="text" name="cuota" size="12" maxlength="9" value="<%=HTMLHelper.formatoMonto(scoring.cuota)%>" readonly="readonly" class="soloLectura">
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Tasa mensual</td>
			  <td width="50%">
			  	<select name="tasa">
			  	<%=HTMLHelper.displayComboTasas(catTasas, scoring.tasa)%>
			  	</select>
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Comisi&oacute;n</td>
			  <td width="50%">
			  	<select name="comision">
			  	<%=HTMLHelper.displayComboComisiones(catComisiones, scoring.comision)%>
			  	</select>
			  </td>
			</tr>
    	</table>
    </td>
  </tr>
    	</table>
    </td>
  </tr>
  <tr>
</table>
<table border="0" width="100%">
	<tr>
		<td align="center" colspan="2" bgcolor="white">
			  <%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
			  <br><input disabled type="button" name="" value="Enviar" onClick="obtenCalculo();" >
			  <%}else{%>
			  <br><input type="button" name="" value="Enviar" onClick="obtenCalculo();" >
			  <%}%>
		</td>
	</tr>
</table>
</form>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>
