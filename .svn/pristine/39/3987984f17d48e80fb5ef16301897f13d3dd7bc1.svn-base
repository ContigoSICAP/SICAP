<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<%@ page import="com.sicap.clientes.util.SolicitudUtil"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.vo.ReferenciaLaboralVO"%>
<%@ page import="java.util.TreeMap"%>
<html>
<head>
<title>Referencia laboral</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function guardaReferenciaLaboral(){
		if ( window.document.forma.fechaElaboracion.value!='' ){
			if ( !esFechaValida(window.document.forma.fechaElaboracion,false) ){
				alert("La Fecha de elaboración es inv\u00e1lida");
				return false;
			}
		}
		if ( window.document.forma.fechaAltaHacienda.value!='' ){
			if ( !esFechaValida(window.document.forma.fechaAltaHacienda,false) ){
				alert("La Fecha de alta en hacienda es inv\u00e1lida");
				return false;
			}
		}
		if ( window.document.forma.inicioEmpleo.value!='' && !esMesesAnios(window.document.forma.inicioEmpleo.value)){
		   alert('El formato del capo Inicio en empleo es inv\u00e1lido');
		   return false;
		}
		window.document.forma.command.value = "guardaReferenciaLaboral";
		window.document.forma.submit();
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<%
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
ReferenciaLaboralVO referenciaLaboral = new ReferenciaLaboralVO();
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
TreeMap catJornadas = CatalogoHelper.getCatalogo(ClientesConstants.CAT_JORNADAS);
if ( cliente.solicitudes[indiceSolicitud].referenciaLaboral!=null  ){
	referenciaLaboral = cliente.solicitudes[indiceSolicitud].referenciaLaboral;
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
<h3>Referencia Laboral</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table width="100%" border="0" cellpadding="0">
	<tr>
		<td width="50%" align="right">Fecha de visita</td>
		<td width="50%">
			<input type="text" name="fechaElaboracion" size="10" maxlength="10" value="<%=HTMLHelper.displayField(referenciaLaboral.fechaElaboracion)%>">(dd/mm/aaaa)
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Nombre de la empresa</td>
		<td width="50%">
			<input type="text" name="nombreEmpresa" size="40" maxlength="60" value="<%=HTMLHelper.displayField(referenciaLaboral.nombreEmpresa)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Nombre del informante</td>
		<td width="50%">
			<input type="text" name="nombreInformante" size="40" maxlength="80" value="<%=HTMLHelper.displayField(referenciaLaboral.nombreInformante)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Cargo del informante</td>
		<td width="50%">
			<input type="text" name="cargo" size="40" maxlength="60" value="<%=HTMLHelper.displayField(referenciaLaboral.cargo)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Giro de la empresa</td>
		<td width="50%">
			<input type="text" name="giroEmpresa" size="40" maxlength="60" value="<%=HTMLHelper.displayField(referenciaLaboral.giroEmpresa)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">RFC de la empresa</td>
		<td width="50%">
			<input type="text" name="rfc" size="13" maxlength="13" value="<%=HTMLHelper.displayField(referenciaLaboral.rfc)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Direcci&oacute;n proporcionada es verdadera</td>
		<td width="50%">
			<select name="direccionVerdadera" size="1" >
				<option value="0">Seleccione...</option>
<%
String seleccionSi = "";
String seleccionNo = "";
if ( referenciaLaboral.direccionVerdadera==1 ) {
	seleccionSi = "selected";
}else if ( referenciaLaboral.direccionVerdadera==2 ) {
	seleccionNo = "selected";
}
%>
				<option value="1" <%=seleccionSi%> >Si</option>
				<option value="2" <%=seleccionNo%> >No</option>
			</select>
		</td>
	</tr>
	<tr>
	<tr>
		<td width="50%" align="right">Telefono 1</td>
		<td width="50%">
			<input type="text" name="telefono1" size="15" maxlength="15" value="<%=HTMLHelper.displayField(referenciaLaboral.telefono1)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Telefono 2</td>
		<td width="50%">
			<input type="text" name="telefono2" size="15" maxlength="15" value="<%=HTMLHelper.displayField(referenciaLaboral.telefono2)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Inicio de operaciones</td>
		<td width="50%">
			<input type="text" name="inicioOperaciones" size="20" maxlength="20" value="<%=HTMLHelper.displayField(referenciaLaboral.inicioOperaciones)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Numero de empleados</td>
		<td width="50%">
			<input type="text" name="numeroEmpleados" size="5" maxlength="5" value="<%=HTMLHelper.displayField(referenciaLaboral.numeroEmpleados)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Principales productos o servicios</td>
		<td width="50%">
			<input type="text" name="principalesProdsServs" size="40" maxlength="80" value="<%=HTMLHelper.displayField(referenciaLaboral.principalesProdsServs)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Fecha de alta en SHCP</td>
		<td width="50%">
			<input type="text" name="fechaAltaHacienda" size="10" maxlength="10" value="<%=HTMLHelper.displayField(referenciaLaboral.fechaAltaHacienda)%>">(dd/mm/aaaa)
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Puesto</td>
		<td width="50%">
			<input type="text" name="puesto" size="40" maxlength="50" value="<%=HTMLHelper.displayField(referenciaLaboral.puesto)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Descripci&oacute;n del puesto</td>
		<td width="50%">
			<input type="text" name="descPuesto" size="40" maxlength="50" value="<%=HTMLHelper.displayField(referenciaLaboral.descPuesto)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Inicio en empleo</td>
		<td width="50%">
			<input type="text" name="inicioEmpleo" size="7" maxlength="7" value="<%=HTMLHelper.displayField(referenciaLaboral.inicioEmpleo)%>">(mm/aaaa)
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Recomienda para credito</td>
		<td width="50%">
			<select name="reconmendacionCredito" size="1" >
				<option value="0">Seleccione...</option>
<%
seleccionSi = "";
seleccionNo = "";
if ( referenciaLaboral.reconmendacionCredito==1 ) {
	seleccionSi = "selected";
}else if ( referenciaLaboral.reconmendacionCredito==2 ) {
	seleccionNo = "selected";
}
%>
				<option value="1" <%=seleccionSi%> >Si</option>
				<option value="2" <%=seleccionNo%> >No</option>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Perpectiva futuras con el empleado</td>
		<td width="50%">
			<input type="text" name="perspectivasMeses" size="2" maxlength="2" value="<%=HTMLHelper.displayField(referenciaLaboral.perspectivasMeses)%>">meses
			<input type="text" name="perspectivasAnios" size="2" maxlength="2" value="<%=HTMLHelper.displayField(referenciaLaboral.perspectivasAnios)%>">a&ntilde;os
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Jornada</td>
		<td width="50%">
			<select name="jornada" size="1" >
				<%=HTMLHelper.displayCombo(catJornadas, referenciaLaboral.jornada)%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Horario</td>
		<td width="50%">
			<input type="text" name="horario" size="30" maxlength="30" value="<%=HTMLHelper.displayField(referenciaLaboral.horario)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">D&iacute;as de descanso</td>
		<td width="50%">
			<input type="text" name="diasDescanso" size="30" maxlength="30" value="<%=HTMLHelper.displayField(referenciaLaboral.diasDescanso)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Ingreso bruto</td>
		<td width="50%">
			<input type="text" name="salarioFijo" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(referenciaLaboral.salarioFijo)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Otros ingresos</td>
		<td width="50%">
			<input type="text" name="incentivos" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(referenciaLaboral.incentivos)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Seguro m&eacute;dico</td>
		<td width="50%">
			<select name="seguroMedico" size="1" >
				<option value="0">Seleccione...</option>
<%
seleccionSi = "";
seleccionNo = "";
if ( referenciaLaboral.seguroMedico==1 ) {
	seleccionSi = "selected";
}else if ( referenciaLaboral.seguroMedico==2 ) {
	seleccionNo = "selected";
}
%>
				<option value="1" <%=seleccionSi%> >Si</option>
				<option value="2" <%=seleccionNo%> >No</option>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Tipo seguro</td>
		<td width="50%">
			<input type="text" name="tipoSeguro" size="30" maxlength="30" value="<%=HTMLHelper.displayField(referenciaLaboral.tipoSeguro)%>">
		</td>
	</tr>
	<tr>
	 <%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
		<td align="center" colspan="2">
			<br><input disabled type="button" value="Enviar" onClick="guardaReferenciaLaboral()">
		</td>
	 <%}else{ %>
	 	<td align="center" colspan="2">
			<br><input type="button" value="Enviar" onClick="guardaReferenciaLaboral()">
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