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
	function guardaDatosCliente(tipoOperacion){
		window.document.forma.command.value='guardaDatosCreditoVivienda';

		if ( window.document.forma.esquema.value==0 ){
		   alert('Seleccione el esquema crediticio');
		   return false;
		}
		
		if ( window.document.forma.numcredito.value!='' ){
			if ( !esEntero(window.document.forma.numcredito.value) ){
				alert("El número de crédito es inv\u00e1lido");
				return false;
			}
		}else{
			alert("Debe capturar el número de crédito");
			return false;
		}
		
		if ( window.document.forma.valorhabitacional.value!='' ){
			if ( !esFormatoMoneda(window.document.forma.valorhabitacional.value) ){
				alert("El valor habitacional es inv\u00e1lido");
				return false;
			}
		}else{
			alert("Debe capturar el valor habitacional");
			return false;
		}
		
		if ( window.document.forma.valoravaluo.value!='' ){
			if ( !esFormatoMoneda(window.document.forma.valoravaluo.value) ){
				alert("El valor del avalúo es inv\u00e1lido");
				return false;
			}
		}
		
		if ( window.document.forma.derechos.value!='' ){
			if ( !esFormatoMoneda(window.document.forma.derechos.value) ){
				alert("El valor de los derechos es inv\u00e1lido");
				return false;
			}
		}
		
		if ( window.document.forma.impuestos.value!='' ){
			if ( !esFormatoMoneda(window.document.forma.impuestos.value) ){
				alert("El valor de los impuestos es inv\u00e1lido");
				return false;
			}
		}

		if ( window.document.forma.gastos.value!='' ){
			if ( !esFormatoMoneda(window.document.forma.gastos.value) ){
				alert("El valor de los gastos es inv\u00e1lido");
				return false;
			}
		}
		
		if ( window.document.forma.ahorro.value!='' ){
			if ( !esFormatoMoneda(window.document.forma.ahorro.value) ){
				alert("El valor del ahorro previo es inv\u00e1lido");
				return false;
			}
		}else{
			alert('Debe capturar el valor del ahorro previo');
			return false;
		}

		if ( window.document.forma.subtitular.value!='' ){
			if ( !esFormatoMoneda(window.document.forma.subtitular.value) ){
				alert("El valor de subcuenta de vivienda del titular es inv\u00e1lido");
				return false;
			}
		}

	    window.document.forma.submit();
	}


</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<%
	ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
	TreeMap catEsquemaCredito = CatalogoHelper.getCatalogoEsquemaCredito();

	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
	
	SolicitudVO solicitud = new SolicitudVO();
	DireccionVO direccion = new DireccionVO();
	CreditoViviendaVO creditoVivienda = new CreditoViviendaVO();
	DecisionComiteVO decisionComite = new DecisionComiteVO();

	int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
	if ( idSolicitud==0 )
		idSolicitud = ((Integer)request.getAttribute("ID_SOLICITUD")).intValue();
	int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);

	if ( cliente.solicitudes != null && cliente.solicitudes.length>0 ){
		solicitud = cliente.solicitudes[indiceSolicitud];
		if( solicitud.creditoVivienda != null ){
			creditoVivienda = solicitud.creditoVivienda;
			if ( creditoVivienda.direccion != null )
				direccion = creditoVivienda.direccion;
		}else{
			if ( cliente.direcciones != null )
				direccion = cliente.direcciones[0];
		}
		
		if( solicitud.decisionComite != null)
			decisionComite = solicitud.decisionComite;
	}
	
	String numCredito = ClientesUtil.makeReferencia(cliente, idSolicitud);
%>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<center>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="">
<input type="hidden" name="idSolicitud" value="<%=solicitud.idSolicitud%>">
<input type="hidden" name="idColonia" value="<%=HTMLHelper.displayField(direccion.idColonia)%>">
<input type="hidden" name="asentamientoCP" value="<%=HTMLHelper.displayField(direccion.asentamiento_cp)%>">
<input type="hidden" name="idLocalidad" value="<%=HTMLHelper.displayField(direccion.idLocalidad)%>">
<table border="0" width="100%">
	<tr>
	<td align="center">
<!-- INICIO DEL CODIGO ANTERIOR -->
<h3>Informaci&oacute;n del Cr&eacute;dito<br></h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table border="0" cellpadding="0" width="100%">
	<tr>
	  	<td width="50%" align="right">N&uacute;mero de cliente<input type="text" name="idCliente" size="10" value="<%=cliente.idCliente%>" readonly></td>
	  	<td width="50%" align="left">RFC<input type="text" name="rfc" size="13" maxlength="13" value="<%=cliente.rfc%>" readonly></td>
	</tr>

	<tr>
	  	<td width="50%" align="right">Tipo de cr&eacute;dito <input type="text" name="tipocredito" size="18" value="Individual" readonly></td>
		<td colspan="2" align="left">Esquema del crédito<select name="esquema" size="1"><%=HTMLHelper.displayCombo(catEsquemaCredito, creditoVivienda.cofinanciado)%></select></td>
	</tr>

	<tr>
  		<td width="50%" align="right">Número de crédito<input type="text" name="numcredito" size="13" maxlength="10" value="<%=HTMLHelper.displayField(numCredito)%>" readonly></td>
  		<td width="50%"	align="left">Tipo de tasa de interés<input type="text" name="tipotasa" size="13" value="Fija" readonly></td>
	</tr>


	<tr>
  		<td width="50%" align="right">Valor solución habitacional<input type="text" name="valorhabitacional" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(creditoVivienda.valorSolucion)%>"></td>
  		<td width="50%" align="left">Valor solución en avalúo<input type="text" name="valoravaluo" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(creditoVivienda.valorAvaluo)%>"></td>
	</tr>

	<tr>
  		<td width="50%" align="right">Derechos<input type="text" name="derechos" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(creditoVivienda.derechos)%>"></td>
  		<td width="50%" align="left">Impuestos<input type="text" name="impuestos" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(creditoVivienda.impuestos)%>"></td>
	</tr>

	<tr>
  		<td width="50%" align="right">Gastos de operación<input type="text" name="gastos" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(creditoVivienda.gastosOperacion)%>"></td>
  		<td width="50%" align="left">Importe del ahorro previo<input type="text" name="ahorro" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(creditoVivienda.ahorro)%>"></td>
	</tr>

	<tr>
  		<td width="50%" align="right">Saldo Subcuenta Titular<input type="text" name="subtitular" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(creditoVivienda.subcuentaTitular)%>"></td>
  		<td width="50%" align="left">CLABE Bancaria<input type="text" name="clavebancaria" size="18" maxlength="18" value="<%=HTMLHelper.displayField(creditoVivienda.CLABEBancaria)%>"></td>
	</tr>

	<tr>
  		<td width="50%" align="right">Monto de crédito autorizado<input type="text" name="montocredito" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(decisionComite.montoAutorizado)%>"></td>
	</tr>

	<tr>
  		<td width="50%" colspan="2" align="center"><br><b>Ubicación en donde se ejerce el crédito</b></td>
	</tr>

	<tr>
  		<td width="100%" align="center" colspan="2"><br><a href="#" onClick="ayudaCodigoPostal()">Ayuda CP</a></td>
	</tr>

	<tr>
  		<td width="50%" align="right">Estado<input type="text" name="estado" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.estado)%>" readonly></td>
  		<td width="50%" align="left">Municipio<input type="text" name="municipio" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.municipio)%>" readonly></td>
	</tr>

	<tr>
  		<td width="50%" align="right">Colonia<input type="text" name="colonia" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.colonia)%>" readonly></td>
  		<td width="50%" align="left">C&oacute;digo postal<input type="text" name="cp" size="6" maxlength="6" value="<%=HTMLHelper.displayField(direccion.cp)%>" readonly></td>
	</tr>

	<tr>
  		<td width="50%" align="right">Calle<input type="text" name="calle" size="30" maxlength="60" value="<%=HTMLHelper.displayField(direccion.calle)%>"></td>
  		<td width="50%" align="left">N&uacute;mero exterior<input type="text" name="numeroExterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(direccion.numeroExterior)%>"></td>
	</tr>

	<tr>
  		<td width="50%" align="right">N&uacute;mero interior<input type="text" name="numeroInterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(direccion.numeroInterior)%>"></td>
	</tr>

	<tr>
		<td width="100%" align="center" colspan="2"><a href="#" onClick="ayudaLocalidad()">Ayuda Localidad</a></td>
	</tr>

	<tr>
  		<td width="50%" align="right">Localidad<input type="text" name="localidad" size="40" maxlength="80" value="<%=HTMLHelper.displayField(direccion.localidad)%>" readonly></td>
	</tr>


	<tr>
	<%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
		<td align="center" colspan="2">
			<br><input disabled type="button" value="Enviar" onClick="guardaDatosCliente(<%=solicitud.tipoOperacion%>)">
		</td>
	<%}else{ %>	
		<td align="center" colspan="2">
			<br><input type="button" value="Enviar" onClick="guardaDatosCliente(<%=solicitud.tipoOperacion%>)">
		</td>
	<%}%>
	</tr>

</table>
</td>
	</tr>
</table>
<!-- FIN DEL CODIGO ANTERIOR -->		
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>