<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>

<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>

<%
	int idObligado= HTMLHelper.getParameterInt(request,"idObligado");
	int idSolicitud  = HTMLHelper.getParameterInt(request,"idSolicitud");
	ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
	int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
	SolicitudVO solicituds = cliente.solicitudes[indiceSolicitud];
	SolicitudVO [] solicitud = cliente.solicitudes;
	TreeMap catComportamiento = CatalogoHelper.getCatalogo(ClientesConstants.CAT_COMPORTAMIENTO_CLIENTE);
	CreditoVO buroCredito =  new CreditoVO();
	CreditoVO circuloCredito = new CreditoVO();
	String modificaObligadoCredito ="INSERTA";
	
	if(idObligado!=0){
		if( solicitud[indiceSolicitud].obligadosSolidarios[idObligado-1].buroCredito!=null ){
			buroCredito=solicitud[indiceSolicitud].obligadosSolidarios[idObligado-1].buroCredito;
		}
		if( solicitud[indiceSolicitud].obligadosSolidarios[idObligado-1].circuloCredito!=null ){
			circuloCredito=solicitud[indiceSolicitud].obligadosSolidarios[idObligado-1].circuloCredito;
			modificaObligadoCredito ="ACTUALIZA";			
		}
	}
	else{
		if( solicitud[indiceSolicitud].buroCredito!=null ){
			buroCredito = solicitud[indiceSolicitud].buroCredito;
		}
		if( solicitud[indiceSolicitud].circuloCredito!=null ){
			circuloCredito =solicitud[indiceSolicitud].circuloCredito;
			modificaObligadoCredito ="ACTUALIZA";
		}
	}
	
	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
%>
<html>
<head>
<title>Calificaci&oacute;n crediticia obligados solidarios</title>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
<script language="Javascript" src="./js/functions.js"></script>
<script type="text/javascript">
<!--
	function guardaDatosCredito(){
		var action  = window.document.forma.modificaObligadoCredito.value;
		if ( window.document.forma.fechaConsulta.value!='' ){
			if ( !esFechaValida(window.document.forma.fechaConsulta,false) ){
				alert("La Fecha de consulta de bur\u00f3 es inv\u00e1lida");
				return false;
			}
		}else{
			alert("Debe capturar la Fecha de consulta de bur\u00f3");
			return false;
		}
		if ( window.document.forma.comportamiento.value==0 ){
			alert("Debe capturar el comportamiento del Bur\u00f3 de Cr\u00e9dito");
			return false;
		}
		if ( window.document.forma.descripcion.value==''){
		   alert('Debe introducir una descripci\u00f3n de la calificaci\u00f3n del Bur\u00f3 de Cr\u00e9dito');
		   return false;
			}
		if ( window.document.forma.fechaConsultaCirculo.value!='' ){
			if ( !esFechaValida(window.document.forma.fechaConsultaCirculo,false) ){
				alert("La Fecha de consulta de circulo es inv\u00e1lida");
				return false;
			}
		}else{
			alert("Debe capturar la Fecha de consulta de círculo");
			return false;
		}
		if ( window.document.forma.comportamientoCirculo.value==0 ){
			alert("Debe capturar el comportamiento del Circulo de Cr\u00e9dito");
			return false;
		}
		if ( window.document.forma.descripcionCirculo.value==''){
		   alert('Debe introducir una descripci\u00f3n de la calificaci\u00f3n del Circulo de Cr\u00e9dito');
		   return false;
		}
		if(action=='INSERTA')
			window.document.forma.command.value="guardaDatosCreditoObligado";
		else
			window.document.forma.command.value="actualizaDatosCreditoObligado";		
		window.document.forma.submit();
	}

	function datosCreditoObligadosSolidarios(){
		if ( window.document.forma.idSolicitud.value==0 ){
			alert('Es necesario guardar la información previamente');
			return false;
		}
		params ="?command=consultaCreditoObligadosSolidarios";
		url = "/CEC/controller";
		abreVentana(url + params,'scrollbars=yes',750 ,400 ,true, 0, 0);
	}
	
	function consultaBuro(){
		params ="?persona=obligado&command=consultaBuroCredito&idSolicitud="+<%=idSolicitud%>+"&idObligado="+<%=idObligado%>;
		url = "/CEC/controller";
		abreVentana(url + params,'scrollbars=yes',900 ,500 ,true, 0, 0);
	}

	function consultaCirculo(){
		params ="?persona=obligado&command=consultaCirculoDeCredito&idSolicitud="+<%=idSolicitud%>+"&idObligado="+<%=idObligado%>;
		url = "/CEC/controller";
		abreVentana(url + params,'scrollbars=yes',900 ,500 ,true, 0, 0);
	}
	
//-->
</script>
</head>
<body leftmargin="0" topmargin="0">
<form action="controller" method="post" name="forma">
<input type="hidden" name="command" value="">
<input type="hidden" name="idSolicitud" value="<%=solicitud[indiceSolicitud].idSolicitud%>">
<input type="hidden" name="idObligado" value="<%=HTMLHelper.getParameterInt(request,"idObligado")%>">
<input type="hidden" name="idCliente" value="<%=solicitud[indiceSolicitud].idCliente%>">
<input type="hidden" name="modificaObligadoCredito" value="<%=modificaObligadoCredito%>">
<center>

<table border="0" width="100%">
	<tr>
	<td align="center">
<h3>Calificaci&oacute;n crediticia obligados solidarios</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table  border="0" cellpadding="0" width="100%">
  <tr>
    <td colspan="2" align="center" height="25"><b>Bur&oacute; de cr&eacute;dito</b></td>
  </tr>
  <tr>
    <td width="50%" align="right">Fecha de Consulta</td>
    <td width="50%">
        <input type="text" name="fechaConsulta" size="10" maxlength="10" id="fechaConsulta" value="<%=HTMLHelper.displayField(buroCredito.fechaConsulta)%>" />
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Fecha de captura de los resultados</td>
    <td width="50%"><input type="text" name="fechaCaptura" size="10" maxlength="10" id="fechaCaptura"  value="<%=HTMLHelper.displayField(buroCredito.fechaCaptura, new java.util.Date())%>" readonly="readonly"/></td>
  </tr>
  <tr>
    <td width="50%" align="right">Comportamiento</td>
    
    <td width="50%"><select name="comportamiento" id="comportamiento"><%=HTMLHelper.displayCombo(catComportamiento,buroCredito.comportamiento)%></select></td>
  </tr>
  <tr>
    <td width="50%" align="right">Descripci&oacute;n de la calificaci&oacute;n</td>
    <td width="50%"><input type="text" name="descripcion" maxlength="100" id="descripcion" value="<%=HTMLHelper.displayField(buroCredito.descripcion)%>" /></td>
  </tr>
<%if( CreditoHelper.puedeConsultarInfoCrediticia(request, indiceSolicitud, cliente, ClientesConstants.SOCIEDAD_BURO) ){ %>
  <tr>
  	<td colspan="2" align="center"><input type="button" name="" value="Consultar Buró de Crédito" onClick="consultaBuro();"><br></td>
  </tr>
<%}%>
  <tr>
    <td colspan="2" align="center" height="25"><b>C&iacute;rculo de cr&eacute;dito</b></td>
  </tr>
  <tr>
    <td width="50%" align="right">Fecha de consulta</td>
    <td width="50%"><input type="text" name="fechaConsultaCirculo" size="10" maxlength="10" id="fechaConsultaCirculo" value="<%=HTMLHelper.displayField(circuloCredito.fechaConsulta)%>"/></td>
  </tr>
  <tr>
    <td width="50%" align="right">Fecha de captura de los resultados</td>
    <td width="50%"><input type="text" name="fechaCapturaCirculo" size="10" maxlength="10" id="fechaCapturaCirculo" value="<%=HTMLHelper.displayField(circuloCredito.fechaCaptura, new java.util.Date())%>" readonly="readonly"/></td>
  </tr>
  <tr>
    <td width="50%" align="right">Comportamiento del cliente</td>
    <td width="50%"><select name="comportamientoCirculo" id="comportamientoCirculo"><%=HTMLHelper.displayCombo(catComportamiento,buroCredito.comportamiento)%></select></td>
  </tr>
  <tr>
    <td width="50%" align="right">Descripci&oacute;n de la calificaci&oacute;n</td>
    <td width="50%"><input type="text" name="descripcionCirculo" maxlength="100" id="descripcionCirculo" value="<%=HTMLHelper.displayField(circuloCredito.descripcion)%>"/></td>
  </tr>
<%if( CreditoHelper.puedeConsultarInfoCrediticia(request, indiceSolicitud, cliente, ClientesConstants.SOCIEDAD_CIRCULO) ){%>
  <tr>
  	<td colspan="2" align="center"><input type="button" name="" value="Consultar Circulo de Crédito" onClick="consultaCirculo();"><br></td>
  </tr>
<%}%>
  <tr>
  	<td colspan="2" align="center">
  		<br>
  		<%if( solicituds.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
  			<input disabled type="button" name="" value="Enviar" onClick="guardaDatosCredito();" >
  		<%}else{ %>
  			<input type="button" name="" value="Enviar" onClick="guardaDatosCredito();" >
  		<%}%>
		<input type="button" value="Cerrar" onClick="javascript:window.close();">
  	</td>
  </tr>
 </table>
 	</td>
	</tr>
</table>
</center>
</form>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>
