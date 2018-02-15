<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.SolicitudVO"%>
<%@ page import="com.sicap.clientes.vo.InformacionFinancieraVO"%>
<html>
<head>
<title>informacionfinanciera</title>
<script language="Javascript" src="./js/functions.js"></script>
<script type="text/javascript">

	function calculaUtilidadBruta (){
		var ventas = window.document.forma.ventas.value;
		var costoVentas = window.document.forma.costoVentas.value;
		var utilidadBruta;
		
		if ( !esFormatoMoneda(ventas) ){
			alert('El monto de [Ventas] es inválido');
			return false;
		}
		if ( !esFormatoMoneda(costoVentas) ){
			alert('El monto de [Costo de ventas] es inválido');
			return false;
		}
		utilidadBruta = ventas - costoVentas;
		utilidadBruta = formateaADosDecimales(utilidadBruta);
		window.document.forma.utilidadBruta.value = utilidadBruta;
	}

    function calculaUtilidadNegocio(){
        var utilidadBruta = window.document.forma.utilidadBruta.value;
		var gastosOperacion = window.document.forma.gastosOperacion.value;
		var utilidadNegocio;
		
		if ( !esFormatoMoneda(gastosOperacion) ){
			alert('El monto de [Gastos de operación e impuestos] es inválido');
			return false;
		}
		utilidadNegocio = utilidadBruta - gastosOperacion;
		utilidadNegocio = formateaADosDecimales(utilidadNegocio);
		window.document.forma.utilidadNegocio.value = utilidadNegocio;
    }

    function calculaUtilidadNetaFamilia(){
        var otrosIngresosFamilia = window.document.forma.otrosIngresosFamilia.value;
		var gastosFamilia = window.document.forma.gastosFamilia.value;
		var utilidadNetaFamilia;
		
		if ( !esFormatoMoneda(otrosIngresosFamilia) ){
			alert('El monto de [Otros ingresos familiares] es inválido');
			return false;
		}
		if ( !esFormatoMoneda(gastosFamilia) ){
			alert('El monto de [Gastos familiares] es inválido');
			return false;
		}
		utilidadNetaFamilia = otrosIngresosFamilia - gastosFamilia;
		utilidadNetaFamilia = formateaADosDecimales(utilidadNetaFamilia);
		window.document.forma.utilidadNetaFamilia.value = utilidadNetaFamilia;
		calculaUtilidadUnidadFamilia();
    }

    function calculaUtilidadUnidadFamilia(){
        var utilidadNegocio = window.document.forma.utilidadNegocio.value;
		var utilidadNetaFamilia = window.document.forma.utilidadNetaFamilia.value;
		var utilidadUnidadFamilia;

		utilidadUnidadFamilia = parseFloat(utilidadNegocio) + parseFloat(utilidadNetaFamilia);
		utilidadUnidadFamilia = formateaADosDecimales(utilidadUnidadFamilia);
		window.document.forma.utilidadUnidadFamilia.value = utilidadUnidadFamilia;
    }

    function calculaActivoCorriente(){
        var efectivoNegocio = window.document.forma.efectivoNegocio.value;
		var cuentasCorrientesAhorros = window.document.forma.cuentasCorrientesAhorros.value;
		var cuentasCobrar = window.document.forma.cuentasCobrar.value;
		var inventarios = window.document.forma.inventarios.value;
		var activoCorriente;

        if ( !esFormatoMoneda(efectivoNegocio) ){
			alert('El monto de [Efectivo negocio] es inválido');
			return false;
		}
		if ( !esFormatoMoneda(cuentasCorrientesAhorros) ){
			alert('El monto de [Cuentas corrientes y de ahorros] es inválido');
			return false;
		}
		if ( !esFormatoMoneda(cuentasCobrar) ){
			alert('El monto de [Cuentas por cobrar] es inválido');
			return false;
		}
		if ( !esFormatoMoneda(inventarios) ){
			alert('El monto de [Inventarios] es inválido');
			return false;
		}
		activoCorriente = parseFloat(efectivoNegocio) + parseFloat(cuentasCorrientesAhorros)+parseFloat(cuentasCobrar) + parseFloat(inventarios);
		activoCorriente = formateaADosDecimales(activoCorriente);
		window.document.forma.activoCorriente.value = activoCorriente;
    }

    function calculaActivoFijo(){
        var inmuebles = window.document.forma.inmuebles.value;
		var maquinariaEquipo = window.document.forma.maquinariaEquipo.value;
		var vehiculos = window.document.forma.vehiculos.value;
		var activoFijo;

        if ( !esFormatoMoneda(inmuebles) ){
			alert('El monto de [Local, terrenos, casa] es inválido');
			return false;
		}
		if ( !esFormatoMoneda(maquinariaEquipo) ){
			alert('El monto de [Maquinaria, herramientas y equipo empresa] es inválido');
			return false;
		}
		if ( !esFormatoMoneda(vehiculos) ){
			alert('El monto de [Vehiculos] es inválido');
			return false;
		}

		activoFijo = parseFloat(inmuebles) + parseFloat(maquinariaEquipo)+parseFloat(vehiculos);
		activoFijo = formateaADosDecimales(activoFijo);
		window.document.forma.activoFijo.value = activoFijo;
    }

    function calculaTotalOtrosActivos(){
        var otrosActivos = window.document.forma.otrosActivos.value;

        if ( !esFormatoMoneda(otrosActivos) ){
			alert('El monto de [Otros activos] es inválido');
			return false;
		}

		window.document.forma.totalOtrosActivos.value = otrosActivos;
		calculaTotalActivo();
    }

    function calculaTotalActivo(){
        var activoCorriente = window.document.forma.activoCorriente.value;
		var activoFijo = window.document.forma.activoFijo.value;
		var otrosActivos = window.document.forma.otrosActivos.value;
		var totalActivo;

		totalActivo = parseFloat(activoCorriente) + parseFloat(activoFijo)+parseFloat(otrosActivos);
		totalActivo = formateaADosDecimales(totalActivo);
		window.document.forma.totalActivo.value = totalActivo;
    }

    function calculaTotalPasivo(){
        var pasivoCortoPlazo = window.document.forma.pasivoCortoPlazo.value;
		var pasivoLargoPlazo = window.document.forma.pasivoLargoPlazo.value;
		var totalPasivo;

		if ( !esFormatoMoneda(pasivoCortoPlazo) ){
			alert('El monto de [Pasivo a corto plazo] es inválido');
			return false;
		}
		if ( !esFormatoMoneda(pasivoLargoPlazo) ){
			alert('El monto de [Pasivo a largo plazo] es inválido');
			return false;
		}

		totalPasivo = parseFloat(pasivoCortoPlazo) + parseFloat(pasivoLargoPlazo);
		totalPasivo = formateaADosDecimales(totalPasivo);
		window.document.forma.totalPasivo.value = totalPasivo;
    }

    function calculaTotalCapitalContable(){
        var capitalAportado = window.document.forma.capitalAportado.value;
		var utilidad = window.document.forma.utilidad.value;
		var totalCapitalContable;

		if ( !esFormatoMoneda(capitalAportado) ){
			alert('El monto de [Capital aportado] es inválido');
			return false;
		}
		if ( !esFormatoMoneda(utilidad) ){
			alert('El monto de [Utilidad] es inválido');
			return false;
		}

		totalCapitalContable = parseFloat(capitalAportado) + parseFloat(utilidad);
		totalCapitalContable = formateaADosDecimales(totalCapitalContable);
		window.document.forma.totalCapitalContable.value = totalCapitalContable;
		calculaTotalPasivoMasCapitalContable();
		calculaMargenUtilidadOperativa();
		calculaMargenUtilidadNetaUnidadFamiliar();
		calculaIndiceLiquidez();
		calculaRotacionInventarios();
		calculaRotacionCuentasCobrar();
		calculaRotacionCuentasPagar();
    }

    function calculaTotalPasivoMasCapitalContable(){
        var totalPasivo = window.document.forma.totalPasivo.value;
		var totalCapitalContable = window.document.forma.totalCapitalContable.value;
		var totalPasivoMasCapitalContable;

		totalPasivoMasCapitalContable = parseFloat(totalPasivo) + parseFloat(totalCapitalContable);
		totalPasivoMasCapitalContable = formateaADosDecimales(totalPasivoMasCapitalContable);
		window.document.forma.totalPasivoMasCapitalContable.value = totalPasivoMasCapitalContable;
    }

    function calculaMargenUtilidadOperativa(){
        var utilidadNegocio = window.document.forma.utilidadNegocio.value;
		var ventas = window.document.forma.ventas.value;
		var margenUtilidadOper;
		if ( ventas>0 )
			margenUtilidadOper = utilidadNegocio / ventas;
		else
			margenUtilidadOper = 0;
		margenUtilidadOper = margenUtilidadOper*100;
		margenUtilidadOper = Math.round( margenUtilidadOper );
		window.document.forma.margenUtilidadOper.value = margenUtilidadOper;
    }

    function calculaMargenUtilidadNetaUnidadFamiliar(){
        var utilidadUnidadFamilia = window.document.forma.utilidadUnidadFamilia.value;
		var ventas = window.document.forma.ventas.value;
		var margenUtilidadNeta;

		if ( ventas>0 )
			margenUtilidadNeta = utilidadUnidadFamilia / ventas;
		else
			margenUtilidadNeta = 0;
		//margenUtilidadNeta = utilidadUnidadFamilia / ventas;
		margenUtilidadNeta = margenUtilidadNeta*100;
		margenUtilidadNeta = Math.round( margenUtilidadNeta );
		window.document.forma.margenUtilidadNeta.value = margenUtilidadNeta;
    }

    function calculaIndiceLiquidez(){
        var activoCorriente = window.document.forma.activoCorriente.value;
		var pasivoCortoPlazo = window.document.forma.pasivoCortoPlazo.value;
		var indiceLiquidez;

		if ( pasivoCortoPlazo>0 )
			indiceLiquidez = activoCorriente / pasivoCortoPlazo;
		else
			indiceLiquidez = 0;
		//indiceLiquidez = activoCorriente / pasivoCortoPlazo;
		indiceLiquidez = indiceLiquidez*100;
		indiceLiquidez = Math.round( indiceLiquidez );
		window.document.forma.indiceLiquidez.value = indiceLiquidez;
    }

    function calculaRotacionInventarios(){
        var inventarios = window.document.forma.inventarios.value;
		var costoVentas = window.document.forma.costoVentas.value;
		var rotacionInventarios;

		if ( costoVentas>0 )
			rotacionInventarios = inventarios / costoVentas * 30;
		else
			rotacionInventarios = 0;
		//rotacionInventarios = inventarios / costoVentas * 30;
		rotacionInventarios = Math.round( rotacionInventarios );
		window.document.forma.rotacionInventarios.value = rotacionInventarios;
    }

    function calculaRotacionCuentasCobrar(){
        var cuentasCobrar = window.document.forma.cuentasCobrar.value;
		var ventas = window.document.forma.ventas.value;
		var cuentasCobrar;

		if ( ventas>0 )
			cuentasCobrar = cuentasCobrar / ventas * 30;
		else
			cuentasCobrar = 0;
		//cuentasCobrar = cuentasCobrar / costoVentas * 30;
		cuentasCobrar = Math.round( cuentasCobrar );
		window.document.forma.rotacionCuentasCobrar.value = cuentasCobrar;
    }

    function calculaRotacionCuentasPagar(){
        var totalPasivo = window.document.forma.totalPasivo.value;
		var costoVentas = window.document.forma.costoVentas.value;
		var rotacionCuentasPagar;

		if ( costoVentas>0 )
			rotacionCuentasPagar = totalPasivo / costoVentas * 30;
		else
			rotacionCuentasPagar = 0;
		//rotacionCuentasPagar = totalPasivo / costoVentas * 30;
		rotacionCuentasPagar = Math.round( rotacionCuentasPagar );
		window.document.forma.rotacionCuentasPagar.value = rotacionCuentasPagar;
    }

	function guardaInformacionFinanciera(){
		
		calculaUtilidadBruta();
		calculaUtilidadNegocio();
		calculaUtilidadNetaFamilia();
		calculaActivoCorriente();
		calculaActivoFijo();
		calculaTotalOtrosActivos();
		calculaTotalPasivo();
		calculaTotalCapitalContable();
		
		window.document.forma.submit();
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css"></head>
<%
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
SolicitudVO solicitud = new SolicitudVO();
InformacionFinancieraVO informacion = new InformacionFinancieraVO();

if ( cliente.solicitudes!=null && idSolicitud>0 ){
	solicitud = cliente.solicitudes[indiceSolicitud];
	if ( solicitud.informacion!=null )
		informacion = solicitud.informacion;
}
%>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<CENTER>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="guardaInformacionFinanciera">
<input type="hidden" name="idCliente" value="<%=cliente.idCliente%>">
<input type="hidden" name="idSolicitud" value="<%=idSolicitud%>">
<table border="0" width="100%">
	<tr>
	<td align="center">
<h3>Informaci&oacute;n Financiera</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table width="100%" border="0" cellpadding="0">
  <tr>
    <td width="50%" align="right">Venta $</td>
    <td width="50%">
      <input type="text" name="ventas" size="12" maxlength="11" value="<%=informacion.ventas%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Costo de venta $</td>
    <td width="50%">
      <input type="text" name="costoVentas" size="12" maxlength="11" value="<%=informacion.costoVentas%>" onblur="calculaUtilidadBruta()">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Utilidad bruta $</td>
    <td width="50%">
      <input type="text" name="utilidadBruta" size="12" maxlength="11" value="<%=informacion.utilidadBruta%>" readonly>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Gastos de operaci&oacute;n e impuestos $</td>
    <td width="50%">
      <input type="text" name="gastosOperacion" size="12" maxlength="11" value="<%=informacion.gastosOperacion%>" onblur="calculaUtilidadNegocio()">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Utilidad del negocio $</td>
    <td width="50%">
      <input type="text" name="utilidadNegocio" size="12" maxlength="11" value="<%=informacion.utilidadNegocio%>" readonly>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Otros ingresos familiares $</td>
    <td width="50%">
      <input type="text" name="otrosIngresosFamilia" size="12" maxlength="11" value="<%=informacion.otrosIngresosFamilia%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Gastos familiares $</td>
    <td width="50%">
      <input type="text" name="gastosFamilia" size="12" maxlength="11" value="<%=informacion.gastosFamilia%>" onblur="calculaUtilidadNetaFamilia()">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Utilidad neta de la familia $</td>
    <td width="50%">
      <input type="text" name="utilidadNetaFamilia" size="12" maxlength="11" value="<%=informacion.utilidadNetaFamilia%>" readonly>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Utilidad de la unidad familiar $</td>
    <td width="50%">
      <input type="text" name="utilidadUnidadFamilia" size="12" maxlength="11" value="<%=informacion.utilidadUnidadFamilia%>" readonly>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Efectivo negocio $</td>
    <td width="50%">
      <input type="text" name="efectivoNegocio" size="12" maxlength="11" value="<%=informacion.efectivoNegocio%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Cuentas corrientes y de ahorros</td>
    <td width="50%">
      <input type="text" name="cuentasCorrientesAhorros" size="12" maxlength="11" value="<%=informacion.cuentasCorrientesAhorros%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Cuentas por cobrar</td>
    <td width="50%">
      <input type="text" name="cuentasCobrar" size="12" maxlength="11" value="<%=informacion.cuentasCobrar%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Inventarios</td>
    <td width="50%">
      <input type="text" name="inventarios" size="12" maxlength="11" value="<%=informacion.inventarios%>" onblur="calculaActivoCorriente()">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Activo corriente</td>
    <td width="50%">
      <input type="text" name="activoCorriente" size="12" maxlength="11" value="<%=informacion.activoCorriente%>" readonly>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Local, terrenos, casa</td>
    <td width="50%">
      <input type="text" name="inmuebles" size="12" maxlength="11" value="<%=informacion.inmuebles%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Maquinaria, herramientas y equipo empresa $</td>
    <td width="50%">
      <input type="text" name="maquinariaEquipo" size="12" maxlength="11" value="<%=informacion.maquinariaEquipo%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Veh&iacute;culos</td>
    <td width="50%">
      <input type="text" name="vehiculos" size="12" maxlength="11" value="<%=informacion.vehiculos%>" onblur="calculaActivoFijo()">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Activo fijo</td>
    <td width="50%">
      <input type="text" name="activoFijo" size="12" maxlength="11" value="<%=informacion.activoFijo%>" readonly>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Otros activos $</td>
    <td width="50%">
      <input type="text" name="otrosActivos" size="12" maxlength="11" value="<%=informacion.otrosActivos%>" onblur="calculaTotalOtrosActivos()">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Total otros activos</td>
    <td width="50%">
      <input type="text" name="totalOtrosActivos" size="12" maxlength="11" value="<%=informacion.totalOtrosActivos%>" readonly>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Total activo</td>
    <td width="50%">
      <input type="text" name="totalActivo" size="12" maxlength="11" value="<%=informacion.totalActivo%>" readonly>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Pasivo a corto plazo</td>
    <td width="50%">
      <input type="text" name="pasivoCortoPlazo" size="12" maxlength="11" value="<%=informacion.pasivoCortoPlazo%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Pasivo a largo plazo</td>
    <td width="50%">
      <input type="text" name="pasivoLargoPlazo" size="12" maxlength="11" value="<%=informacion.pasivoLargoPlazo%>" onblur="calculaTotalPasivo()">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Total pasivo</td>
    <td width="50%">
      <input type="text" name="totalPasivo" size="12" maxlength="11" value="<%=informacion.totalPasivo%>" readonly>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Capital aportado</td>
    <td width="50%">
      <input type="text" name="capitalAportado" size="12" maxlength="11" value="<%=informacion.capitalAportado%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Utilidad</td>
    <td width="50%">
      <input type="text" name="utilidad" size="12" maxlength="11" value="<%=informacion.utilidad%>" onblur="calculaTotalCapitalContable()">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Total capital contable</td>
    <td width="50%">
      <input type="text" name="totalCapitalContable" size="12" maxlength="11" value="<%=informacion.totalCapitalContable%>" readonly>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Total pasivo m&aacute;s capital contable</td>
    <td width="50%">
      <input type="text" name="totalPasivoMasCapitalContable" size="12" maxlength="11" value="<%=informacion.totalPasivoMasCapitalContable%>" readonly>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Margen de utilidad Operativa</td>
    <td width="50%">
      <input type="text" name="margenUtilidadOper" size="12" maxlength="11" value="<%=informacion.margenUtilidadOper%>" readonly>
      (%) </td>
  </tr>
  <tr>
    <td width="50%" align="right">Margen de utilidad Neta Unidad Familiar</td>
    <td width="50%">
      <input type="text" name="margenUtilidadNeta" size="12" maxlength="11" value="<%=informacion.margenUtilidadNeta%>" readonly>
      (%) </td>
  </tr>
  <tr>
    <td width="50%" align="right">Indice de liquidez</td>
    <td width="50%">
      <input type="text" name="indiceLiquidez" size="12" maxlength="11" value="<%=informacion.indiceLiquidez%>" readonly>
      (%) </td>
  </tr>
  <tr>
    <td width="50%" align="right">Rotaci&oacute;n de Inventarios</td>
    <td width="50%">
      <input type="text" name="rotacionInventarios" size="12" maxlength="11" value="<%=informacion.rotacionInventarios%>" readonly>
      (Veces) </td>
  </tr>
  <tr>
    <td width="50%" align="right">Rotaci&oacute;n de Capital de Trabajo solicitado</td>
    <td width="50%">
      <input type="text" name="rotacionCapitalTrabajoSol" size="12" maxlength="11" value="<%=informacion.rotacionCapitalTrabajoSol%>">
      (Veces) </td>
  </tr>
  <tr>
    <td width="50%" align="right">Rotaci&oacute;n de Cuentas por cobrar</td>
    <td width="50%">
      <input type="text" name="rotacionCuentasCobrar" size="12" maxlength="11" value="<%=informacion.rotacionCuentasCobrar%>" readonly>
      (Veces) </td>
  </tr>
  <tr>
    <td width="50%" align="right">Rotaci&oacute;n de Cuentas por pagar</td>
    <td width="50%">
      <input type="text" name="rotacionCuentasPagar" size="12" maxlength="11" value="<%=informacion.rotacionCuentasPagar%>" readonly>
      (Veces) </td>
  </tr>
  <tr>
  	<%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
		<td align="center" colspan="2">
			<br><input disabled type="button" value="Enviar" onClick="guardaInformacionFinanciera()">
		</td>
	<%}else{ %>
		<td align="center" colspan="2">
			<br><input type="button" value="Enviar" onClick="guardaInformacionFinanciera()">
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
