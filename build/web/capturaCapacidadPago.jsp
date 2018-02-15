<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.helpers.SolicitudHelper"%>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<%@ page import="com.sicap.clientes.util.SolicitudUtil"%>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.CapacidadPagoVO"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="java.util.TreeMap"%>

<html>
<head>
<title>Capacidad pago</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>


	function calculaDisponibleMensual(){
		var disponibleMensual = 0;
		var ingresos = 0;
		var gastos = 0;
		if ( !esFormatoMoneda(window.document.forma.ingresosNomina.value) ){
			alert('El monto de [Ingresos nómina] es inválido');
			return false;
		}
		if ( !esFormatoMoneda(window.document.forma.otrosNoComprobables.value) ){
			alert('El monto de [Otros ingresos no comprobables] es inválido');
			return false;
		}
		if ( !esFormatoMoneda(window.document.forma.otrosIngresos.value) ){
			alert('El monto de [Otros ingresos] es inválido');
			return false;
		}
		ingresos = parseFloat(window.document.forma.ingresosNomina.value) + parseFloat(window.document.forma.otrosNoComprobables.value) + parseFloat(window.document.forma.otrosIngresos.value);
		if ( !esFormatoMoneda(window.document.forma.rentaVivienda.value) ){
			alert('El monto de [Renta vivienda] es inválido');
			return false;
		}
		if ( !esFormatoMoneda(window.document.forma.pagoDeuda.value) ){
			alert('El monto de [Pago de deuda] es inválido');
			return false;
		}
		if ( !esFormatoMoneda(window.document.forma.otrosGastos.value) ){
			alert('El monto de [Otros gastos] es inválido');
			return false;
		}
		gastos = parseFloat(window.document.forma.rentaVivienda.value) + parseFloat(window.document.forma.pagoDeuda.value) + parseFloat(window.document.forma.otrosGastos.value);
		disponibleMensual = ingresos - gastos;
		window.document.forma.disponibleMensual.value = disponibleMensual;
	}

	function calculaCuotas(){
		
	}

	function guardaCapacidadPago(){
		calculaDisponibleMensual();
		calculaCuotas();
		window.document.forma.command.value = "guardaCapacidadPago";
		window.document.forma.submit();
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<%
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
CapacidadPagoVO capacidadPago = new CapacidadPagoVO();
TreeMap catFuenteOtrosIngresos = CatalogoHelper.getCatalogo(ClientesConstants.CAT_FTE_OTROS_ING);
TreeMap catEstatusAuto = SolicitudHelper.getCatalogoEstatusAuto();
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
if ( cliente.solicitudes[indiceSolicitud].capacidadPago!=null  ){
	capacidadPago = cliente.solicitudes[indiceSolicitud].capacidadPago;
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
<table border="0" width="100%">
	<tr>
	<td align="center">	   

<h3>Capacidad de pago</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table width="100%" border="0" cellpadding="0">
	<tr>
		<td width="50%" align="right">Ingresos nomina</td>
		<td width="50%">
			<input type="text" name="ingresosNomina" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(capacidadPago.ingresosNomina)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Otros ingresos no comprobables</td>
		<td width="50%">
			<input type="text" name="otrosNoComprobables" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(capacidadPago.otrosNoComprobables)%>">
		</td>
	</tr>
		<tr>
		<td width="50%" align="right">Fuente</td>
		<td width="50%">
			<select name="fuenteOtrosIngresos" size="1" >
				<%=HTMLHelper.displayCombo(catFuenteOtrosIngresos, capacidadPago.fuenteOtrosIngresos)%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Otros ingresos</td>
		<td width="50%">
			<input type="text" name="otrosIngresos" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(capacidadPago.otrosIngresos)%>">
		</td>
	</tr>	
	<tr>
		<td width="50%" align="right">Marca y a&ntilde;o del automovil</td>
		<td width="50%">
			<input type="text" name="marcaModeloAuto" size="40" maxlength="40" value="<%=HTMLHelper.displayField(capacidadPago.marcaModeloAuto)%>">
		</td>
	</tr>
	<tr>
		<td width="25%" align="right">Estado del auto</td>
		<td colspan="3">
			<select name="estatusAuto">
				<%=HTMLHelper.displayCombo(catEstatusAuto, capacidadPago.estatusAuto)%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Valor auto</td>
		<td width="50%">
			<input type="text" name="valorAuto" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(capacidadPago.valorAuto)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Renta vivienda</td>
		<td width="50%">
			<input type="text" name="rentaVivienda" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(capacidadPago.rentaVivienda)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Pago de deuda</td>
		<td width="50%">
			<input type="text" name="pagoDeuda" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(capacidadPago.pagoDeuda)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Otros gastos</td>
		<td width="50%">
			<input type="text" name="otrosGastos" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(capacidadPago.otrosGastos)%>" onblur="calculaDisponibleMensual()">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Disponible mensual</td>
		<td width="50%">
			<input type="text" name="disponibleMensual" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(capacidadPago.disponibleMensual)%>" readonly="readonly">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Cuota sobre disponible</td>
		<td width="50%">
			<input type="text" name="cuotaSobreDisponible" size="10" maxlength="10" value="<%=HTMLHelper.displayField(capacidadPago.cuotaSobreDisponible)%>" readonly="readonly">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Cuota sobre ingreso bruto</td>
		<td width="50%">
			<input type="text" name="cuotaSobreIngresoBruto" size="10" maxlength="10" value="<%=HTMLHelper.displayField(capacidadPago.cuotaSobreIngresoBruto)%>" readonly="readonly">
		</td>
	</tr>
	<tr>
	<%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
		<td align="center" colspan="2">
			<br><input type="button" value="Enviar" onClick="guardaCapacidadPago()">
		</td>
	<%}else{ %>
		<td align="center" colspan="2">
			<br><input type="button" value="Enviar" onClick="guardaCapacidadPago()">
		</td>
	<%}%>
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