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
TreeMap catClificacionSIC = CatalogoHelper.getCatalogo(ClientesConstants.CAT_COMPORTAMIENTO_CLIENTE);
TreeMap catTipoCuanta = CatalogoHelper.getCatalogo(ClientesConstants.CAT_TIPO_CUENTA);
TreeMap catAntCuenta = CatalogoHelper.getCatalogo(ClientesConstants.CAT_ANT_CUENTA);
TreeMap catBusquedas = CatalogoHelper.getCatalogo(ClientesConstants.CAT_NUM_BUSQUEDA_CTA);
TreeMap catDictamen = CatalogoHelper.getCatalogoDictamen();
TreeMap catDictamenCapPago = CatalogoHelper.getCatalogoDictamenCapPago();
TreeMap catGenero = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SEXO);
TreeMap catEstadoCivil = CatalogoHelper.getCatalogo(ClientesConstants.CAT_ESTADO_CIVIL);
TreeMap catTipoContrato = CatalogoHelper.getCatalogo(ClientesConstants.CAT_TIPOS_CONTRATO);
TreeMap catVivienda = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SITUACION_VIVIENDA);
TreeMap catDepEconom = CatalogoHelper.getCatalogo(ClientesConstants.CAT_DEP_ECONOMICOS);

TreeMap catNivelVivienda = CatalogoHelper.getCatalogo(ClientesConstants.CAT_NIVEL_VIVIENDA);
TreeMap catZona = CatalogoHelper.getCatalogo(ClientesConstants.CAT_CALIF_ZONA);
TreeMap catPisosVivienda = CatalogoHelper.getCatalogo(ClientesConstants.CAT_PISOS_VIV);
TreeMap catHabitVivienda = CatalogoHelper.getCatalogo(ClientesConstants.CAT_CUARTOS_VIV);
TreeMap catFachada = CatalogoHelper.getCatalogo(ClientesConstants.CAT_CARACT_FACHADA);
TreeMap catTecho = CatalogoHelper.getCatalogo(ClientesConstants.CAT_CARACT_TECHO);

TreeMap catNumEmpleados = CatalogoHelper.getCatalogo(ClientesConstants.CAT_NUM_EMPLEADOS);
TreeMap catJornada = CatalogoHelper.getCatalogo(ClientesConstants.CAT_JORNADAS);
//TreeMap catPlazoContrato = CatalogoHelper.getCatalogo(ClientesConstants.CAT_PLAZOS_CONTRATO);

TreeMap catReferencia = CatalogoHelper.getCatalogoCalifReferencia();
TreeMap catDestinos = CatalogoHelper.getCatalogo(ClientesConstants.CAT_DESTINOS_CONSUMO);

TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(solicitud.tipoOperacion);
TreeMap catTasas = CatalogoHelper.getCatalogoTasas(solicitud.tipoOperacion);

ScoringVO scoring = new ScoringVO();
if ( idSolicitud==0 )
	idSolicitud = 1;
if ( cliente.solicitudes[indiceSolicitud].scoring!=null ){
	scoring = cliente.solicitudes[indiceSolicitud].scoring;
} 

catTipoContrato.remove(4);
//String path = request.getContextPath();
//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
  <head>
    <title>Calcula scoring</title>
    <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    <script language="Javascript" src="./js/functions.js"></script>
	<script>

	function obtenScoreConsumo(){
		window.document.forma.command.value='obtenScoreConsumo';
		window.document.forma.submit();
	}

	</script>
  </head>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="obtenScoreConsumo">
<input type="hidden" name="idCliente" value="<%=cliente.idCliente%>">
<input type="hidden" name="idSolicitud" value="<%=idSolicitud%>">

<table cellspacing="1" cellpadding="0" width="90%" bgcolor="#009861">
  <tr>
    <td width="50%" valign="top">
			<table bgcolor="white" width="100%">
				<tr>
					<td colspan="2" align="center" height="30">
						Nuevo cálculo
					</td>
				</tr>
				<tr>
					<td width="50%" align="right">Sexo</td>
					<td width="50%">
						<select name="genero"><%=HTMLHelper.displayCombo(catGenero, scoring.genero)%></select>
					</td>
				</tr>
				<tr>
					<td width="50%" align="right">Edad (a&ntilde;os)</td>
					<td width="50%">
						<input type="text" name="anios" value="<%=scoring.edad%>" size="6">
					</td>
				</tr>
				<tr>
					<td width="50%" align="right">Estado civil</td>
					<td width="50%">
						<select name="catEstadoCivil">
						<%=HTMLHelper.displayCombo(catEstadoCivil, scoring.estadoCivil)%>
						</select>
					</td>
				</tr>
				<tr>
					<td width="50%" align="right">N&uacute;mero de dependientes econ&oacute;micos<br></td>
					<td width="50%">
						<select name="dependientesEconomicos">
						<%=HTMLHelper.displayCombo(catDepEconom, scoring.dependientesEconomicos)%>
						</select>
					</td>
				</tr>
				<tr>
					<td width="50%" align="right">Tipo de vivienda</td>
					<td width="50%">
						<select name="nivelVivienda">
						<%=HTMLHelper.displayCombo(catNivelVivienda, scoring.nivelVivienda)%>
						</select>
					</td>
				</tr>
				<tr>
					<td width="50%" align="right">Antig&uuml;edad laboral (a&ntilde;os)<br></td>
					<td width="50%">
						<input type="text" name="antLaboralAnios" value="<%=scoring.antLaboral%>" size="6">
					</td>
				</tr>
				<tr>
					<td width="50%" align="right">Arraigo de la empresa (a&ntilde;os)</td>
					<td width="50%">
						<input type="text" name="arraigoEmpresaAnios" value="<%=scoring.arraigoEmpresa%>" size="6">
					</td>
				</tr>
				<tr>
					<td width="50%" align="right">Tiempo de residencia (a&ntilde;os)<br></td>
					<td width="50%">
						<input type="text" name="tiempoResidenciaAnios" value="<%=scoring.tiempoResidencia%>" size="6">
					</td>
				</tr>
				<tr>
					<td width="50%" align="right">Tipo de contrato<br></td>
					<td width="50%">
						<select name="tipoContrato">
						<%=HTMLHelper.displayCombo(catTipoContrato, scoring.tipoContrato)%>
						</select>
					</td>
				</tr>
				<tr>
					<td width="50%" align="right">Vivienda habitual<br></td>
					<td width="50%">
						<select name="situacionVivienda">
						<%=HTMLHelper.displayCombo(catVivienda, scoring.situacionVivienda)%>
						</select>
					</td>
				</tr>
				<tr>
					<td width="50%" align="right">Puntuación</td>
					<td width="50%">
						<input type="text" readonly="readonly" size="7" value="<%=FormatUtil.formateaPorcentaje(scoring.puntuacion)%>" class="soloLectura">
					</td>
				</tr>
				<tr>
					<td width="50%" align="right">Tramo</td>
					<td width="50%">
						<input type="text" readonly="readonly" size="7" value="<%=scoring.tramo%>" class="soloLectura">
					</td>
				</tr>
				<tr>
					<td width="50%" align="right">Dictamen</td>
					<td width="50%">
						<input type="text" readonly="readonly" value="<%=HTMLHelper.getDescripcion(catDictamen,scoring.dictamenFinal)%>" class="soloLecturaRojo" size="11">
					</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%" colspan="2" height="100%">&nbsp;</td>
				</tr>
			</table>
	 	</td>
		<!-- COLUMNA 2  -->	 	
		<td width="50%" bgcolor="white" valign="top">
		<table border="0" cellpadding="0" width="100%">
			<tr>
				<td colspan="2" align="center" height="30">SIC</td>
			<tr>
				<td colspan="2" align="center">
					<table>
						<tr>
			  <td width="50%" align="right">Calificaci&oacute;n SIC</td>
			  <td width="50%">
			  	<select name="calificacionSIC">
			  	<%=HTMLHelper.displayCombo(catClificacionSIC, scoring.calificacionSIC)%>
			  	</select></td>
			</tr>
			<tr>
			  <td width="50%" align="right">Tipo de cuenta<br></td>
			  <td width="50%">
			  	<select name="tipoCuenta">
			  	<%=HTMLHelper.displayCombo(catTipoCuanta, scoring.tipoCuenta)%>
			  	</select>
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Antig&uuml;edad de la cuenta</td>
			  <td width="50%">
			  	<select name="antCuenta">
			  	<%=HTMLHelper.displayCombo(catAntCuenta, scoring.antCuenta)%>
			  	</select>
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Numero de busqueda en los &uacute;ltimos 6 meses</td>
			  <td width="50%">
			  	<select name="numBuquedas">
			  	<%=HTMLHelper.displayCombo(catBusquedas, scoring.numBuquedas)%>
			  	</select>
			  </td>
			</tr>
    	</table>
    </td>
  </tr>
  <tr>
    <tr>
		<td colspan="2" align="center" height="30">Perfil de cliente</td>
			<tr>
				<td colspan="2" align="center">
    	<table>
			<!--tr>
			  <td width="50%" align="right">Tipo de contrato<br></td>
			  <td width="50%">
			  	<select name="tipoContrato">
			  	<%=HTMLHelper.displayCombo(catTipoContrato, scoring.tipoContrato)%>
			  	</select>
			  </td>
			</tr-->
			<tr>
			  <td width="50%" align="right">Importe alquiler/ hipoteca<br></td>
			  <td width="50%">
			  	<input type="text" name="alquilerHipoteca" size="12" maxlength="9" value="<%=HTMLHelper.formatoMonto(scoring.alquilerHipoteca)%>">
			  </td>
			</tr>
    	</table>
    </td>
  </tr>
    <tr>
		<td colspan="2" align="center" height="30">Capacidad de pago</td>
			<tr>
				<td colspan="2" align="center"> 
    	<table width="100%">
			<tr>
			  <td width="50%" align="right">Ingresos n&oacute;mina</td>
			  <td width="50%">
			  	<input type="text" name="ingresosNomina" size="12" maxlength="9" value="<%=HTMLHelper.formatoMonto(scoring.ingresosNomina)%>">
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Otros no comprobables</td>
			  <td width="50%">
			  	<input type="text" name="otrosNoComprobables" size="12" maxlength="9" value="<%=HTMLHelper.formatoMonto(scoring.otrosNoComprobables)%>">
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Otros ingresos<br></td>
			  <td width="50%">
			  	<input type="text" name="otrosIngresos" size="12" maxlength="9" value="<%=HTMLHelper.formatoMonto(scoring.otrosIngresos)%>">
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Renta de vivienda<br></td>
			  <td width="50%">
			  	<input type="text" name="rentaVivienda" size="12" maxlength="9" value="<%=HTMLHelper.formatoMonto(scoring.rentaVivienda)%>">
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Pago de deuda<br></td>
			  <td width="50%">
			  	<input type="text" name="pagoDeuda" size="12" maxlength="9" value="<%=HTMLHelper.formatoMonto(scoring.pagoDeuda)%>">
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Otros gastos<br></td>
			  <td width="50%">
			  	<input type="text" name="otrosGastos" size="12" maxlength="9" value="<%=HTMLHelper.formatoMonto(scoring.otrosGastos)%>">
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Disponible mensual<br></td>
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
  <tr>
		<td colspan="2" align="center" height="30">Domicilio</td>
			<tr>
				<td colspan="2" align="center"> 
    	<table>
			<tr>
			  <td width="50%" align="right">Calificaci&oacute;n de la zona</td>
			  <td width="50%">
			  	<select name="calificacionZona">
			  	<%=HTMLHelper.displayCombo(catZona, scoring.calificacionZona)%>
			  	</select>
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Toma&ntilde;o de la vivienda (pisos)<br></td>
			  <td width="50%">
			  	<select name="pisosVivienda"><%=HTMLHelper.displayCombo(catPisosVivienda, scoring.pisosVivienda)%></select>
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Tama&ntilde;o de la vivienda (cuartos)<br></td>
			  <td width="50%">
			  	<select name="habitacionesVivienda"><%=HTMLHelper.displayCombo(catHabitVivienda, scoring.habitacionesVivienda)%></select>
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Caracteristicas de la fachada<br></td>
			  <td width="50%">
			  	<select name="caracteristicasFachada">
			  	<%=HTMLHelper.displayCombo(catFachada, scoring.caracteristicasFachada)%>
			  	</select>
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Caracteristicas del techo<br></td>
			  <td width="50%">
			  	<select name="caracteristicasTecho">
			  	<%=HTMLHelper.displayCombo(catTecho, scoring.caracteristicasTecho)%>
			  	</select>
			  </td>
			</tr>
    	</table>
    </td>
  </tr>
	<tr>
		<td colspan="2" align="center" height="30">Empleo</td>
			<tr>
				<td colspan="2" align="center">
    	<table>
			<tr>
			  <td width="50%" align="right">N&uacute;mero de empleados</td>
			  <td width="50%">
			  	<select name="numeroEmpleados">
			  	<%=HTMLHelper.displayCombo(catNumEmpleados, scoring.numeroEmpleados)%>
			  	</select>
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Tipo de contrato<br></td>
			  <td width="50%">
			  	<select name="jornada">
			  	<%=HTMLHelper.displayCombo(catJornada, scoring.jornada)%>
			  	</select>
			  </td>
			</tr>
    	</table>
    </td>
  </tr>
	<tr>
		<td colspan="2" align="center" height="30">Referencias</td>
			<tr>
				<td colspan="2" align="center">
    	<table>
			<tr>
			  <td width="50%" align="right">Referencia personal 1</td>
			  <td width="50%">
			  	<select name="referencia1">
			  	<%=HTMLHelper.displayCombo(catReferencia, scoring.referencia1)%>
			  	</select>
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Referencia personal 2<br></td>
			  <td width="50%">
			  	<select name="referencia2">
			  	<%=HTMLHelper.displayCombo(catReferencia, scoring.referencia2)%>
			  	</select>
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Referencia laboral<br></td>
			  <td width="50%">
			  	<select name="referenciaLaboral">
			  	<%=HTMLHelper.displayCombo(catReferencia, scoring.referenciaLaboral)%>
			  	</select>
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Referencia arrendador<br></td>
			  <td width="50%">
			  	<select name="referenciaArrendador">
			  	<%=HTMLHelper.displayCombo(catReferencia, scoring.referenciaArrendador)%>
			  	</select>
			  </td>
			</tr>
    	</table>
    </td>
  </tr>
	<tr>
		<td colspan="2" align="center" height="30">Destino del cr&eacute;dito</td>
			<tr>
				<td colspan="2" align="center">
    	<table>
			<tr>
			  <td width="50%" align="right">Destino del cr&eacute;dito</td>
			  <td width="50%">
			  	<select name="destinoCredito">
			  	<%=HTMLHelper.displayCombo(catDestinos, scoring.destinoCredito)%>
			  	</select>
			  </td>
			</tr>
    	</table>
    </td>
  </tr>
	<tr>
		<td colspan="2" align="center" height="30">Perfil de la operaci&oacute;n</td>
			<tr>
				<td colspan="2" align="center"> 
    	<table width="100%">
			<tr>
			  <td width="50%" align="right">Monto</td>
			  <td width="50%">
			  	<input type="text" name="monto" size="12" maxlength="9" value="<%=HTMLHelper.formatoMonto(scoring.monto)%>">
			  </td>
			</tr>
			<tr>
			  <td width="50%" align="right">Plazo</td>
			  <td width="50%">
			  	<input type="text" name="plazo" size="12" maxlength="2" value="<%=HTMLHelper.displayField(scoring.plazo)%>">
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
			<tr>
			  <td width="50%" align="right">Total de coso financiero<br></td>
			  <td width="50%">
			  	<input type="text" name="comision" size="12" maxlength="9" value="<%=HTMLHelper.formatoMonto(scoring.totalCostofinanciero)%>" readonly="readonly" class="soloLectura">
			  </td>
			</tr>
    	</table>
    </td>
  </tr>
</table>
		</td>
	</tr>
</table>
<table border="0" width="90%">
	<tr>
		<td align="center" colspan="2" bgcolor="white">
			  <%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
			  <br><input disabled type="button" name="" value="Enviar" onClick="obtenScoreConsumo();" >
			  <%}else{%>
			  <br><input type="button" name="" value="Enviar" onClick="obtenScoreConsumo();" >
			  <%}%>
		</td>
	</tr>
</table>
</form>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>
