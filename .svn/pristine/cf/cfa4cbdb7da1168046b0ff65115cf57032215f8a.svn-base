<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.ConyugeVO"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>

<html>
<head>
<title>Conyuge</title>
<script language="Javascript" src="./js/functions.js"></script>
<script type="text/javascript">

	function guardaConyuge(){

		if ( window.document.forma.fechaEvaluacion.value!='' ){
			if ( !esFechaValida(window.document.forma.fechaEvaluacion,false) || !esPosterior(window.document.forma.fechaEvaluacion.value, '<%=ClientesConstants.FECHA_LIM_MENOR%>') ){
				alert("La Fecha es inv\u00e1lida");
				return false;
			}
		}else{
			alert("Debe capturar la Fecha de realizaciÛn de la evaluaciÛn");
			return false;
		}    
		if ( window.document.forma.nombre.value=='' || !esTexto( "^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$",window.document.forma.nombre.value)){
				   alert('Debe introducir un Nombre v\u00e1lido');
				   return false;
				}
				
				if ( window.document.forma.aPaterno.value=='' || !esTexto( "^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$",window.document.forma.aPaterno.value)){
				   alert('Debe introducir un Apellido Paterno v\u00e1lido');
				   return false;
				}
		if ( window.document.forma.direccionDomicilio.value==''){
				   alert('Debe introducir un domicilio');
				   return false;
				}
		
		if ( window.document.forma.telefonoDomicilio.value=='' || !esTelefonoValido(window.document.forma.telefonoDomicilio.value)){
				   alert('Debe introducir un Tel\u00e9fono v\u00e1lido (incluir clave LADA)');
				   return false;
				}

		window.document.forma.submit();
	}


</script>

<link href="./css/BEtext.css" rel="stylesheet" type="text/css"></head>
<%
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
ConyugeVO conyuge = new ConyugeVO();
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
if ( cliente.conyuge!=null  )
	conyuge = cliente.conyuge;

%>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<CENTER>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="guardaConyuge">
<input type="hidden" name="idSolicitud" value="<%=idSolicitud%>">
<table border="0" width="100%">
	<tr>
	<td align="center">	
<h3>C&oacute;nyuge</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>

<table width="100%" border="0" cellpadding="0">
  <tr>
    <td width="50%" align="right">Fecha de realizaci&oacute;n de la evaluaci&oacute;n </td>
    <td width="50%">
      <input type="text" name="fechaEvaluacion" size="10" maxlength="10" value="<%=HTMLHelper.displayField(conyuge.fechaEvaluacion)%>">
      (dd/mm/aaaa) </td>
  </tr>

  <tr>
    <td width="50%" align="right">Nombre</td>
    <td width="50%">
      <input type="text" name="nombre" size="40" maxlength="50" value="<%=HTMLHelper.displayField(conyuge.nombre)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Apellido Paterno</td>
    <td width="50%">
      <input type="text" name="aPaterno" size="40" maxlength="50" value="<%=HTMLHelper.displayField(conyuge.aPaterno)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Apellido Materno</td>
    <td width="50%">
      <input type="text" name="aMaterno" size="40" maxlength="50" value="<%=HTMLHelper.displayField(conyuge.aMaterno)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Direcci&oacute;n domicilio</td>
    <td width="50%">
      <input type="text" name="direccionDomicilio" size="40" maxlength="80" value="<%=HTMLHelper.displayField(conyuge.direccionDomicilio)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Tel&eacute;fono domicilio</td>
    <td width="50%">
      <input type="text" name="telefonoDomicilio" size="15" maxlength="15" value="<%=HTMLHelper.displayField(conyuge.telefonoDomicilio)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Direcci&oacute;n del trabajo</td>
    <td width="50%">
      <input type="text" name="direccionTrabajo" size="40" maxlength="80" value="<%=HTMLHelper.displayField(conyuge.direccionTrabajo)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Tel&eacute;fono trabajo</td>
    <td width="50%">
      <input type="text" name="telefonoTrabajo" size="10" maxlength="10" value="<%=HTMLHelper.displayField(conyuge.telefonoTrabajo)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Celular</td>
    <td width="50%">
      <input type="text" name="telefonoCelular" size="10" maxlength="10" value="<%=HTMLHelper.displayField(conyuge.telefonoCelular)%>">
    </td>
  </tr>
  <tr>
  <%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
		<td align="center" colspan="2">
			<br><input disabled type="button" value="Enviar" onClick="guardaConyuge()">
		</td>
  <%}else{ %>
  		<td align="center" colspan="2">
			<br><input type="button" value="Enviar" onClick="guardaConyuge()">
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
