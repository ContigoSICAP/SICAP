<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.util.SolicitudUtil"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.SolicitudVO"%>
<%@ page import="com.sicap.clientes.vo.ExpedienteVO"%>
<%@ page import="com.sicap.clientes.helpers.ExpedienteHelper"%>

<html>
<head>
<title>Check List Expediente Operativo y Legal</title>
<script language="Javascript" src="./js/functions.js"></script>
<script type="text/javascript">

	function guardaExpedienteOperativoLegal(){
		window.document.forma.command.value='guardaExpedienteOperativoLegal';
		window.document.forma.submit();
	}


</script>

<link href="./css/BEtext.css" rel="stylesheet" type="text/css"></head>

<% Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");

ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];

ExpedienteVO expediente = new ExpedienteVO();
if(solicitud.expediente != null)
	expediente = solicitud.expediente;

ExpedienteHelper buscaChecked = new ExpedienteHelper();
%>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<CENTER>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="guardaExpedienteOperativoLegal">
<input type="hidden" name="idSolicitud" value="<%=idSolicitud%>">
<table border="0" width="100%">
	<tr>
	<td align="center">
 <%=HTMLHelper.displayNotifications(notificaciones)%>
 <h3>Check-List<br>Expediente Operativo y Legal</h3>
    
 <table>
	
<tr>
  <td align="left"><b>N&uacute;mero</b></td>
  <td align="center"><b>Descripci&oacute;n</b></td>
  <td align="right"><b>Integraci&oacute;n</b></td>
</tr>

<tr>
  <td alingn="center"><b>01</b></td>
  <td align="left"><b>Solicitud e Información Diversa</b></td>
  <td></td>
</tr>

<tr>
  <td></td>
  <td align="left">Solicitud de Cr&eacute;dito</td>
  <td align="center"><input type="checkbox" name="solicitudcredito" value="si" <%= buscaChecked.buscaCheck(expediente.solicitudcredito)%> ></td>
</tr>

<tr>
  <td></td>
  <td align="left">Identificaci&oacute;n Titular</td>
  <td align="center"><input type="checkbox" name="idtitular" value="si" <%= buscaChecked.buscaCheck(expediente.idtitular)%>></td>
</tr>

<tr>
  <td></td>
  <td align="left">Identificaci&oacute;n Obligado Solidario</td>
  <td align="center"><input type="checkbox" name="idsolidario" value="si" <%= buscaChecked.buscaCheck(expediente.idsolidario)%>></td>
</tr>

<tr>
  <td></td>
  <td align="left">Identificaci&oacute;n Aval</td>
  <td align="center"><input type="checkbox" name="idaval" value="si" <%= buscaChecked.buscaCheck(expediente.idaval)%>></td>
</tr>

<tr>
  <td></td>
  <td align="left">Comprobante de Domicilio</td>
  <td align="center"><input type="checkbox" name="compdomicilio" value="si" <%= buscaChecked.buscaCheck(expediente.compdomicilio)%>></td>
</tr>

<tr>
  <td alingn="center"><b>02</b></td>
  <td align="left"><b>Documentos de Operaci&oacute;n</b></td>
  <td></td>
</tr>


<tr>
  <td></td>
  <td align="left">Autorizaci&oacute;n de SIC</td>
  <td align="center"><input type="checkbox" name="autorizasic" value="si" <%= buscaChecked.buscaCheck(expediente.autorizasic)%>></td>
</tr>

<tr>
  <td></td>
  <td align="left">Consulta Bur&oacute; Titular</td>
  <td align="center"><input type="checkbox" name="consultatitular" value="si" <%= buscaChecked.buscaCheck(expediente.consultatitular)%>></td>
</tr>

<tr>
  <td></td>
  <td align="left">Consulta Bur&oacute; Obligado Solidario</td>
  <td align="center"><input type="checkbox" name="consultasolidario" value="si" <%= buscaChecked.buscaCheck(expediente.consultasolidario)%>></td>
</tr>

<tr>
  <td></td>
  <td align="left">Consulta Bur&oacute; Aval(es)</td>
  <td align="center"><input type="checkbox" name="consultaavales" value="si" <%= buscaChecked.buscaCheck(expediente.consultaavales)%>></td>
</tr>

<tr>
  <td></td>
  <td align="left">Consulta C&iacute;rculo Titular</td>
  <td align="center"><input type="checkbox" name="consultacirctitular" value="si" <%= buscaChecked.buscaCheck(expediente.consultacirctitular)%>></td>
</tr>

<tr>
  <td></td>
  <td align="left">Consulta C&iacute;rculo Obligado Solidario</td>
  <td align="center"><input type="checkbox" name="consultacircsolidario" value="si" <%= buscaChecked.buscaCheck(expediente.consultacircsolidario)%>></td>
</tr>

<tr>
  <td></td>
  <td align="left">Consulta C&iacute;rculo Aval(es)</td>
  <td align="center"><input type="checkbox" name="consultacircavales" value="si" <%= buscaChecked.buscaCheck(expediente.consultacircavales)%>></td>
</tr>

<tr>
  <td></td>
  <td align="left">Consulta Interna Titular</td>
  <td align="center"><input type="checkbox" name="consultaintertitular" value="si" <%= buscaChecked.buscaCheck(expediente.consultaintertitular)%>></td>
</tr>

<tr>
  <td></td>
  <td align="left">Consulta Interna Obligado Solidario</td>
  <td align="center"><input type="checkbox" name="consultaintersolidario" value="si" <%= buscaChecked.buscaCheck(expediente.consultaintersolidario)%>></td>
</tr>

<tr>
  <td></td>
  <td align="left">Consulta Interna Aval(es)</td>
  <td align="center"><input type="checkbox" name="consultainteravales" value="si" <%= buscaChecked.buscaCheck(expediente.consultainteravales)%>></td>
</tr>

<tr>
  <td></td>
  <td align="left">Formato de Evaluaci&oacute;n</td>
  <td align="center"><input type="checkbox" name="formatoevaluacion" value="si" <%= buscaChecked.buscaCheck(expediente.formatoevaluacion)%>></td>
</tr>

<tr>
  <td></td>
  <td align="left">Perfil de Operaciones</td>
  <td align="center"><input type="checkbox" name="perfiloperaciones" value="si" <%= buscaChecked.buscaCheck(expediente.perfiloperaciones)%>></td>
</tr>

<tr>
  <td alingn="center"><b>03</b></td>
  <td align="left"><b>Dictamen y An&aacute;lisis de Cr&eacute;dito</b></td>
  <td></td>
</tr>

<tr>
  <td></td>
  <td align="left">Formatos de Verificaci&oacute;n Referencias</td>
  <td align="center"><input type="checkbox" name="formatoref" value="si" <%= buscaChecked.buscaCheck(expediente.formatoref)%>></td>
</tr>

<tr>
  <td alingn="center"><b>04</b></td>
  <td align="left"><b>Expediente Hist&oacute;rico</b></td>
  <td></td>
</tr>

<tr>
  <td></td>
  <td align="left">Formatos de Aplicaci&oacute;n de Cr&eacute;dito</td>
  <td align="center"><input type="checkbox" name="formatocredito" value="si" <%= buscaChecked.buscaCheck(expediente.formatocredito)%>></td>
</tr>

<tr>
  <td></td>
  <td align="left">Bit&aacute;cora de Cobranza</td>
  <td align="center"><input type="checkbox" name="bitacoracobranza" value="si" <%= buscaChecked.buscaCheck(expediente.bitacoracobranza)%>></td>
</tr>


<tr>
  <td></td>
  <td align="left">Tabla de Amortizaci&oacute;n</td>
  <td align="center"><input type="checkbox" name="tablaamort" value="si" <%= buscaChecked.buscaCheck(expediente.tablaamort)%>></td>
</tr>

<tr>
<td>&nbsp;</td>
</tr>
<tr>
<td></td>
<td><h3>Expediente Legal</h3></td>
</tr>


<tr>
  <td align="left"><b>N&uacute;mero</b></td>
  <td align="center"><b>Descripci&oacute;n</b></td>
  <td align="right"><b>Integraci&oacute;n</b></td>
</tr>


<tr>
  <td alingn="center"><b>01</b></td>
  <td align="left"><b>Resoluci&oacute;n y Contratos de Cr&eacute;dito</b></td>
  <td></td>
</tr>

<tr>
  <td></td>
  <td align="left">Pagar&eacute;</td>
  <td align="center"><input type="checkbox" name="pagare" value="si" <%= buscaChecked.buscaCheck(expediente.pagare)%>></td>
</tr>

<tr>
  <td></td>
  <td align="left">Contrato de Cr&eacute;dito</td>
  <td align="center"><input type="checkbox" name="contratocredito" value="si" <%= buscaChecked.buscaCheck(expediente.contratocredito)%>></td>
</tr>

<tr>
  <td></td>
  <td align="left">Factura del Bien en Garantía</td>
  <td align="center"><input type="checkbox" name="factgarantia" value="si" <%= buscaChecked.buscaCheck(expediente.factgarantia)%>></td>
</tr>

<tr>
  <td></td>
  <td align="left">Copia Formato de Seguro (opc) </td>
  <td align="center"><input type="checkbox" name="formatoseguro" value="si" <%= buscaChecked.buscaCheck(expediente.formatoseguro)%>></td>
</tr>

<tr>
  <td alingn="center"><b>02</b></td>
  <td align="left"><b>Recuperaci&oacute;n Factura del Bien Garant&iacute;a</b></td>
  <td></td>
</tr>

<tr>
  <td></td>
  <td align="left">Factura del Bien en Garant&iacute;a</td>
  <td align="center"><input type="checkbox" name="facturabiengarantia" value="si" <%= buscaChecked.buscaCheck(expediente.facturabiengarantia)%>></td>
</tr>
	
<tr>
<td>&nbsp;</td>
</tr>
	
<tr>
<%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
  <td></td>
  	<td align="center">
  	<input disabled type="button" value="Enviar" onClick="guardaExpedienteOperativoLegal()">
  </td>
<%}else{ %>
  <td></td>
  	<td align="center">
  	<input type="button" value="Enviar" onClick="guardaExpedienteOperativoLegal()">
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
