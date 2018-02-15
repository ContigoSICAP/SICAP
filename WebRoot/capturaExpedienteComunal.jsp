<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.util.SolicitudUtil"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.SolicitudVO"%>
<%@ page import="com.sicap.clientes.vo.ExpedienteVO"%>
<%@ page import="com.sicap.clientes.helpers.ExpedienteHelper"%>



<html>
<head>
<title>Check List Producto Comunal</title>
<script language="Javascript" src="./js/functions.js"></script>
<script type="text/javascript">

	function guardaExpedienteComunal(){
		window.document.forma.command.value='guardaExpedienteComunal';
		document.body.style.cursor = "wait";
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
 <h3>Check-List<br>Expediente Comunal</h3>
    
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
  <td align="left">Credencial de Elector</td>
  <td align="center"><input type="checkbox" name="idtitular" value="si" <%= buscaChecked.buscaCheck(expediente.idtitular)%> ></td>
</tr>

<tr>
  <td></td>
  <td align="left">Comprobante de Domicilio</td>
  <td align="center"><input type="checkbox" name="compdomicilio" value="si" <%= buscaChecked.buscaCheck(expediente.compdomicilio)%>></td>
</tr>

<tr>
  <td></td>
  <td align="left">Reglamento Interno</td>
  <td align="center"><input type="checkbox" name="reglamentointerno" value="si" <%= buscaChecked.buscaCheck(expediente.reglamentointerno)%>></td>
</tr>

<tr>
  <td></td>
  <td align="left">Acta de Formaci&oacute;n de Grupo</td>
  <td align="center"><input type="checkbox" name="actaformaciongrupo" value="si" <%= buscaChecked.buscaCheck(expediente.actaformaciongrupo)%>></td>
</tr>

<tr>
  <td></td>
  <td align="left">Reporte del SIC</td>
  <td align="center"><input type="checkbox" name="autorizasic" value="si" <%= buscaChecked.buscaCheck(expediente.autorizasic)%>></td>
</tr>

<tr>
  <td></td>
  <td align="left">Anexo B Grupal</td>
  <td align="center"><input type="checkbox" name="anexobgrupal" value="si" <%= buscaChecked.buscaCheck(expediente.anexobgrupal)%>></td>
</tr>

<tr>
<td>&nbsp;</td>
</tr>
	
<tr>
 <%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
  	<td></td>
  	<td align="center"><input disabled type="button" value="Enviar" onClick="guardaExpedienteComunal()"></td>
 <%}else{ %>
 	<td></td>
  <td align="center"><input type="button" value="Enviar" onClick="guardaExpedienteComunal()"></td>
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
