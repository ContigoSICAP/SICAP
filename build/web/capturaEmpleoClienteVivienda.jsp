<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.util.SolicitudUtil"%>
<%@ page import="com.sicap.clientes.util.Convertidor"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="java.util.TreeMap"%>
<jsp:directive.page import="com.sicap.clientes.vo.DireccionVO"/>
<html>
<head>
<title>Empleo cliente</title>

<%
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
EmpleoVO empleoCliente = new EmpleoVO();
DireccionVO direccion = new DireccionVO();
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
if ( cliente.solicitudes[indiceSolicitud].empleo!=null  ){
	empleoCliente = cliente.solicitudes[indiceSolicitud].empleo;
	if ( empleoCliente!=null && empleoCliente.direccion!=null )
		direccion = empleoCliente.direccion;
}
TreeMap catExpLaboral = CatalogoHelper.getCatalogo(ClientesConstants.CAT_ANT_EMPLEO);
TreeMap catPlazosCont = CatalogoHelper.getCatalogo(ClientesConstants.CAT_PLAZOS_CONTRATO);
TreeMap catFteOtrosIngresos = CatalogoHelper.getCatalogo(ClientesConstants.CAT_FTE_OTROS_ING);
TreeMap catFormaIngreso = CatalogoHelper.getCatalogo(ClientesConstants.CAT_FORMA_INGRESO);
TreeMap catSector = CatalogoHelper.getCatalogo(ClientesConstants.CAT_TIPO_SECTOR);
TreeMap catDependencias = CatalogoHelper.getCatalogo(ClientesConstants.CAT_DEPENDENCIAS);
double sueldo = Convertidor.stringToDouble(CatalogoHelper.getParametro(ClientesConstants.PARAMETRO_SUELDO_VIVIENDA));
%>
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function guardaEmpleoCliente(tipoOper){
		
		window.document.forma.command.value = "guardaEmpleoClienteVivienda";
		if ( window.document.forma.razonSocial.value==''){
		   alert('Debe introducir el Nombre de la empresa');
		   return false;
		}
		if ( window.document.forma.idColonia.value==0){
		   alert('Debe indicar la colonia');
		   return false;
		}
		if ( window.document.forma.calle.value==''){
		   alert('Debe capturar la Calle');
		   return false;
		}
		if ( window.document.forma.numeroExterior.value==''){
		   alert('Debe capturar el N�mero exterior');
		   return false;
		}
		if ( window.document.forma.area.value==''){
		   alert('Debe capturar el �rea / Departamento / Secci�n');
		   return false;
		}
		if ( window.document.forma.puesto.value==''){
		   alert('Debe capturar el Puesto');
		   return false;
		}
		if ( window.document.forma.telefono.value==''){
		   alert('Debe capturar el Telefono');
		   return false;
		}
		if ( window.document.forma.plazoContrato.value==0){
		   alert('Debe indicar el Tipo contrato');
		   return false;
		}

		if ( tipoOper == 4){
			if ( window.document.forma.formaingreso.value==0){
			   alert('Debe indicar la Forma de ingreso');
			   return false;
			}
			
			if ( window.document.forma.tiposector.value==0){
			   alert('Debe indicar el tipo de sector');
			   return false;
			}
			
			if ( window.document.forma.tiposector.value==1 && window.document.forma.dependencia.value==0 ){
		   		alert('Debe indicar la dependencia');
		   		return false;
		    }

		}else{
			if ( window.document.forma.tipoContrato.value==0){
			   alert('Debe indicar el Tipo ingreso');
			   return false;
			}
		}
		
		if ( window.document.forma.antEmpleo.value==0){
		   alert('Debe indicar la Antig�edad en el empleo');
		   return false;
		}
		if ( window.document.forma.sueldoMensual.value=='' || !esFormatoMoneda(window.document.forma.sueldoMensual.value)){
		   alert('El Sueldo mensual es inv�lido');
		   return false;
		}

		if (tipoOper == 4){
			if (window.document.forma.sueldoMensual.value > <%=sueldo%>){
			   alert('El Sueldo del cliente excede el monto m�ximo permitido');
			   return false;
			}
		}
				
		window.document.forma.submit();
	}

	function enable(){
		if(window.document.forma.tiposector.value=='1'){
	 		window.document.forma.dependencia.disabled=false;
		}
		else{
			window.document.forma.dependencia.disabled=true;
		}

	 }

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<CENTER>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="">
<input type="hidden" name="idColonia" value="<%=HTMLHelper.displayField(direccion.idColonia)%>">
<input type="hidden" name="asentamientoCP" value="<%=HTMLHelper.displayField(direccion.asentamiento_cp)%>">
<input type="hidden" name="idCliente" value="<%=cliente.idCliente%>">
<input type="hidden" name="idSolicitud" value="<%=cliente.solicitudes[indiceSolicitud].idSolicitud%>">
<table border="0" width="100%">
	<tr>
	<td align="center">	
<h3>Empleo</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table width="100%" border="0" cellpadding="0">
	<tr>
		<td width="50%" align="right">Nombre de la empresa</td>
		<td width="50%">
			<input type="text" name="razonSocial" size="40" maxlength="70" value="<%=HTMLHelper.displayField(empleoCliente.razonSocial)%>">
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
		<td width="50%" align="right">Delegaci�n/Municipio</td>
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
			<input type="text" name="calle" size="30" maxlength="60" value="<%=HTMLHelper.displayField(direccion.calle)%>">
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
		<td width="50%" align="right">&Aacute;rea / Departamento / Secci&oacute;n</td>
		<td width="50%">
			<input type="text" name="area" size="40" maxlength="100" value="<%=HTMLHelper.displayField(empleoCliente.area)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Puesto</td>
		<td width="50%">
			<input type="text" name="puesto" size="40" maxlength="100" value="<%=HTMLHelper.displayField(empleoCliente.puesto)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Tel&eacute;fono</td>
		<td width="50%">
			<input type="text" name="telefono" size="10" maxlength="10" value="<%=HTMLHelper.displayField(empleoCliente.telefono)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Ext</td>
		<td width="50%">
			<input type="text" name="extension" size="10" maxlength="5" value="<%=HTMLHelper.displayField(empleoCliente.extension)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Tipo contrato</td>
		<td width="50%">
			<select name="plazoContrato" size="1" >
				<%=HTMLHelper.displayCombo(catPlazosCont, empleoCliente.plazoContrato)%>
			</select>
		</td>
	</tr>

	<tr>
	  	<td width="50%" align="right">Forma ingreso</td>
	  	<td width="50%">  
	  	<select name="formaingreso" size="1">
			<%=HTMLHelper.displayCombo(catFormaIngreso, empleoCliente.formaIngreso)%>
		</select>
	  	</td>
	</tr>

	<tr>
	  	<td width="50%" align="right">Sector</td>
	  	<td width="50%">  
	  	<select name="tiposector" size="1" onClick="enable();">
			<%=HTMLHelper.displayCombo(catSector, empleoCliente.tipoSector)%>
		</select>
	  	</td>
	</tr>

	<tr>
	  	<td width="50%" align="right">Dependencia</td>
	  	<td width="50%">
	  	<select name="dependencia" size="1" style="width: 360">
			<%=HTMLHelper.displayCombo(catDependencias, empleoCliente.dependencia)%>
		</select>
	  	</td>
	</tr>
	
	<tr>
		<td width="50%" align="right">Antig�edad en el empleo</td>
		<td width="50%">
			<select name="antEmpleo" size="1" >
				<%=HTMLHelper.displayCombo(catExpLaboral, empleoCliente.antEmpleo)%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Sueldo mensual fijo</td>
		<td width="50%">
			<input type="text" name="sueldoMensual" size="10" maxlength="9" value="<%=HTMLHelper.formatoMonto(empleoCliente.sueldoMensual)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Otros ingresos</td>
		<td width="50%">
			<input type="text" name="otrosIngresos" size="10" maxlength="9" value="<%=HTMLHelper.formatoMonto(empleoCliente.otrosIngresos)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Fuente de otros ingresos</td>
		<td width="50%">
			<select name="fteOtrosIngresos" size="1" >
				<%=HTMLHelper.displayCombo(catFteOtrosIngresos, empleoCliente.fteOtrosIngresos)%>
			</select>
		</td>
	</tr>
	<tr>
	 <%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
		<td align="center" colspan="2">
			<br><input disabled type="button" value="Enviar" onClick="guardaEmpleoCliente(<%= cliente.solicitudes[indiceSolicitud].tipoOperacion %>)">
		</td>
	 <%}else{ %>
	 	<td align="center" colspan="2">
			<br><input type="button" value="Enviar" onClick="guardaEmpleoCliente(<%= cliente.solicitudes[indiceSolicitud].tipoOperacion %>)">
		</td>
	 <%}%>
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
