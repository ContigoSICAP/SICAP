<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>
<html>
<head>
<title>Alta/Modificaci&oacute;n de cliente</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function guardaPropuestaComiteCredito(montoMaximo){
		window.document.forma.command.value='guardaPropuestaComiteCredito';
		
		
			if ( window.document.forma.montoPropuesto.value!='' ){
			if ( !esMontoValido(window.document.forma.montoPropuesto.value) ){
				alert("El monto es inv\u00e1lido");
				return false;
			}
		}else{
			alert("Debe capturar el monto");
			return false;
		}
		
		
		if ( window.document.forma.plazoPropuesto.value!='' ){
			if ( !esPlazoValido(window.document.forma.plazoPropuesto.value) ){
				alert("El plazo es inv\u00e1lido");
				return false;
			}
		}else{
			alert("Debe capturar el plazo");
			return false;
		}
		
		if ( window.document.forma.frecuenciaPagoPropuesta.value==0 ){
			alert('Debe seleccionar La Frecuencia de Pago');
			return false;
		    }
		if ( window.document.forma.destinoCredito.value==0 ){
			alert('Debe seleccionar el Destino del Cr\u00e9dito');
			return false;
		    }    
		
		if ( window.document.forma.montoPropuesto.value>montoMaximo && montoMaximo!='0.0' ){
			if(<%=request.isUserInRole("ANALISIS_CREDITO")%>){
				if ( confirm('Está seguro desea proponer esa cantidad que supera el 30% extra del crédito anterior' ) )
					window.document.forma.submit();
			}else{
				if ( confirm('El monto propuesto exede en más del 30% el del útimo crédito liquidado y sólo podrá ser autorizado por el área de Análisis de Crédito. Cantidad máxima: $'+ montoMaximo) )	
					window.document.forma.submit();
			}
		}else{
			window.document.forma.submit();	
		}
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css"></head>
<%
TreeMap catDestinoCredito = null;

Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");

int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
SolicitudVO solicitud = new SolicitudVO();
InformacionFinancieraVO informacion = new InformacionFinancieraVO();
if ( cliente.solicitudes!=null && idSolicitud>0 ){
	solicitud = cliente.solicitudes[indiceSolicitud];
  	          if(solicitud!=null && solicitud.reestructura==1){
		        double montoAutorizado = SolicitudUtil.calculaMontoAutorizadoReestructuraIndividual(solicitud);
		        solicitud.montoPropuesto = montoAutorizado; 		     
		       }	
	if ( solicitud.tipoOperacion==ClientesConstants.CONSUMO || solicitud.tipoOperacion==ClientesConstants.CREDIHOGAR || solicitud.tipoOperacion==ClientesConstants.MAX_ZAPATOS)
		catDestinoCredito = CatalogoHelper.getCatalogo(ClientesConstants.CAT_DESTINOS_CONSUMO);
	if ( solicitud.tipoOperacion==ClientesConstants.MICROCREDITO )
		catDestinoCredito = CatalogoHelper.getCatalogo(ClientesConstants.CAT_DESTINOS_MICRO);
	if ( solicitud.informacion!=null )
		informacion = solicitud.informacion;
}
double montoMaximo = SolicitudUtil.montoMaximoSolicitud(cliente);
TreeMap catFrecuenciaPago = CatalogoHelper.getCatalogoFrecuenciasPago(solicitud.tipoOperacion);
%>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<CENTER>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="">
<input type="hidden" name="idCliente" value="<%=cliente.idCliente%>">
<input type="hidden" name="idSolicitud" value="<%=solicitud.idSolicitud%>">


<!-- INICIO NUEVO CODIGO -->

<!-- FIN NUEVO CODIGO -->
<table border="0" width="100%">
	<tr>
	<td align="center">
<h3>Propuesta comit&eacute; cr&eacute;dito</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table border="0" cellpadding="0" width="100%">
	<tr>
  	<td width="50%" align="right">Monto propuesto</td>
  	<td width="50%">  
  	<input type="text" name="montoPropuesto" size="10" maxlength="12" value="<%=HTMLHelper.formatoMonto(cliente.solicitudes[indiceSolicitud].montoPropuesto)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Plazo propuesto</td>
  	<td width="50%">  
  	<input type="text" name="plazoPropuesto" size="5" maxlength="2" value="<%=HTMLHelper.displayField(cliente.solicitudes[indiceSolicitud].plazoPropuesto)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Frecuencia de pago propuesta</td>
  	<td width="50%">  
  	<select name="frecuenciaPagoPropuesta" size="1">
			<%=HTMLHelper.displayCombo(catFrecuenciaPago,cliente.solicitudes[indiceSolicitud].frecuenciaPagoPropuesta)%>
	</select>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Destino del cr&eacute;dito</td>
  	<td width="50%">  
  	<select name="destinoCredito" size="1">
			<%=HTMLHelper.displayCombo(catDestinoCredito,cliente.solicitudes[indiceSolicitud].destinoCredito)%>
	</select>
  	</td>
	</tr>
	<!--tr>
    <td width="50%" align="right">Endeudamiento a futuro Total</td>
    <td width="50%">
      <input type="text" name="endeudamientoFuturoTotal" size="12" maxlength="11" value="<%=informacion.endeudamientoFuturoTotal%>" readonly>
      (%) </td>
	</tr>
	<tr>
    <td width="50%" align="right">Capacidad de Pago (<50%)</td>
    <td width="50%">
      <input type="text" name="capacidadPago" size="12" maxlength="11" value="<%=informacion.capacidadPago%>" readonly>
      (%) </td>
  </tr>
  <tr>
    <td width="50%" align="right">D&iacute;as de venta (<3)</td>
    <td width="50%">
      <input type="text" name="diasVenta" size="12" maxlength="11" value="<%=informacion.diasVenta%>" readonly>
      (Dias) </td>
  </tr-->
	<tr>
	 <%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
		<td align="center" colspan="2">
			<br><input disabled type="button" value="Enviar" onClick="guardaPropuestaComiteCredito()">
		</td>
	 <%}else{ %>
	 	<td align="center" colspan="2">
			<br><input type="button" value="Enviar" onClick="guardaPropuestaComiteCredito(<%=montoMaximo%>)">
		</td>
	 <%} %>
	</tr>
</table> 

		</td>
	</tr>
</table>
<!-- INICIO NUEVO CODIGO -->

<!-- FIN NUEVO CODIGO -->

</form>
</CENTER>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>