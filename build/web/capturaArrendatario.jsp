<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.util.SolicitudUtil"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.SolicitudVO"%>
<%@ page import="com.sicap.clientes.vo.ArrendatarioVO"%>
<%@ page import="java.util.TreeMap"%>

<html>
<head>
<title>arrendatariodomicilio</title>
<script language="Javascript" src="./js/functions.js"></script>
<script type="text/javascript">

	function guardaArrendatario(){

		if ( window.document.forma.nombreArrendatario.value=='' || !esTexto( "^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$",window.document.forma.nombreArrendatario.value)){
				   alert('Debe introducir un Nombre v\u00e1lido');
				   return false;
				}
		if ( window.document.forma.telefonoArrendatario.value=='' || !esTelefonoValido(window.document.forma.telefonoArrendatario.value)){
				   alert('Debe introducir un Tel\u00e9fono (incluir clave LADA)');
				   return false;
				}
		if ( window.document.forma.horaLlamadaArrendatario.value==''){
		   alert('Debe introducir una hora de llamada');
		   return false;
		}
		if ( window.document.forma.tiempoConocimiento.value==''){
				   alert('Debe introducir hace cuanto conoce al arrendatario');
				   return false;
				}
		if ( window.document.forma.conocimientoOcupacion.value==''){
				   alert('Debe introducir si conoce la ocupación del arrendatario');
				   return false;
				}
		if ( window.document.forma.conocimientoVivienda.value==''){
				   alert('Debe introducir si sabe con quien o con cuantas personas vive el arrendatario');
				   return false;
				}
		if ( window.document.forma.relacionArrendatario.value==''){
				   alert('Debe introducir que relación tiene con el arrendatario');
				   return false;
				}
		if ( window.document.forma.inmuebleRenta.value==''){
				   alert('Debe introducir que le renta');
				   return false;
				}
		if ( window.document.forma.tiempoRentaArrendatario.value==''){
				   alert('Debe introducir desde hace cuanto tiempo le renta');
				   return false;
				}
		if ( window.document.forma.duracionContrato.value==''){
				   alert('Debe introducir la duración de su contrato');
				   return false;
				}
		if ( window.document.forma.existenciaContrato.value==''){
				   alert('Debe introducir si tiene un contrato');
				   return false;
				}
		if ( window.document.forma.puntualidadPago.value==''){
				   alert('Debe introducir si le paga puntual');
				   return false;
				}
		if ( window.document.forma.conductaAtraso.value==''){
				   alert('Debe introducir el motivo del retraso');
				   return false;
				}
		if ( window.document.forma.planRentaFutura.value==''){
				   alert('Debe introducir si tiene pensado seguir rentandole');
				   return false;
				}
		if ( window.document.forma.reconmendacionCredito.value==''){
				   alert('Debe introducir si lo recomendaria para un crédito');
				   return false;
				}
		if ( window.document.forma.descripcionCliente.value==''){
				   alert('Debe introducir como lo considera como persona');
				   return false;
				}
		if ( window.document.forma.disponibilidadRespaldo.value==''){
				   alert('Debe introducir si seria su fiador o aval');
				   return false;
				}
		if ( window.document.forma.calificacionCliente.value==0 ){
					alert('Debe seleccionar la calificación');
					return false;
				    }
		if ( window.document.forma.fechaRealizacionConsulta.value!='' ){
			if ( !esFechaValida(window.document.forma.fechaRealizacionConsulta,false) ){
				alert("La Fecha de realización de la consulta es inv\u00e1lida");
				return false;
			}
		}else{
			alert("Debe capturar la Fecha de realización de la consulta");
			return false;
		}    
		window.document.forma.submit();
	}


</script>

<link href="./css/BEtext.css" rel="stylesheet" type="text/css"></head>

<%
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
TreeMap catCalificacion = CatalogoHelper.getCatalogo(ClientesConstants.CAT_COMPORTAMIENTO_CLIENTE);
ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
SolicitudVO solicitud = new SolicitudVO();
ArrendatarioVO arrendatario = new ArrendatarioVO();


int tipoArrendatario = 0;
tipoArrendatario = ((Integer)request.getAttribute("TIPO_ARRENDATARIO")).intValue();
if ( cliente.solicitudes!=null && idSolicitud>0 ){
	solicitud = cliente.solicitudes[indiceSolicitud];
	if ( solicitud.arrendatarioDomicilio!=null && tipoArrendatario==ClientesConstants.ARRENDATARIO_DOMICILIO )
		arrendatario = solicitud.arrendatarioDomicilio;
	if ( solicitud.arrendatarioLocal!=null && tipoArrendatario==ClientesConstants.ARRENDATARIO_LOCAL )
		arrendatario = solicitud.arrendatarioLocal;
}

%>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<center>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="guardaArrendatario">
<input type="hidden" name="idSolicitud" value="<%=idSolicitud%>">
<input type="hidden" name="tipoArrendatario" value="<%=tipoArrendatario%>">
<table border="0" width="100%">
	<tr>
	<td align="center">

<%if ( tipoArrendatario==ClientesConstants.ARRENDATARIO_DOMICILIO ){%>
	<h3>Arrendatario Domicilio</h3>
<%}else{%>
	<h3>Arrendatario Local</h3>
<%}%>
<%=HTMLHelper.displayNotifications(notificaciones)%>

<table width="100%" border="0" cellpadding="0">
  <tr>
    <td width="50%" align="right">Nombre</td>
    <td width="50%">
      <input name="nombreArrendatario" type="text" value="<%=HTMLHelper.displayField(arrendatario.nombre)%>" size="40" maxlength="100">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Tel&eacute;fono</td>
    <td width="50%">
      <input name="telefonoArrendatario" type="text" value="<%=HTMLHelper.displayField(arrendatario.telefono)%>" size="10" maxlength="10">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;A qu&eacute; hora se puede llamar ?</td>
    <td width="50%">
      <input name="horaLlamadaArrendatario" type="text" value="<%=HTMLHelper.displayField(arrendatario.horarioLlamada)%>" size="20" maxlength="20">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Hace cu&aacute;nto tiempo conoce al Sr. (a)?</td>
    <td width="50%">
      <input name="tiempoConocimiento" type="text" value="<%=HTMLHelper.displayField(arrendatario.tiempoConocimiento)%>" size="20" maxlength="20">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Sabe usted a que se dedica el Sr. (a)?</td>
    <td width="50%">
      <input name="conocimientoOcupacion" type="text" value="<%=HTMLHelper.displayField(arrendatario.conocimientoOcupacion)%>" size="20" maxlength="20">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Sabe con quien vive? O &iquest;cu&aacute;ntas personas viven con &eacute;l?</td>
    <td width="50%">
      <input name="conocimientoVivienda" type="text" value="<%=HTMLHelper.displayField(arrendatario.conocimientoVivienda)%>" size="20" maxlength="20">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Qu&eacute; relaci&oacute;n tiene con &eacute;l?</td>
    <td width="50%">
      <input name="relacionArrendatario" type="text" value="<%=HTMLHelper.displayField(arrendatario.relacion)%>" size="20" maxlength="20">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Qu&eacute; le renta?</td>
    <td width="50%">
      <input name="inmuebleRenta" type="text" value="<%=HTMLHelper.displayField(arrendatario.inmuebleRenta)%>" size="20" maxlength="20">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Hace cu&aacute;nto tiempo?</td>
    <td width="50%">
      <input name="tiempoRentaArrendatario" type="text" value="<%=HTMLHelper.displayField(arrendatario.tiempoRenta)%>" size="20" maxlength="20">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Tiene un contrato escrito con &eacute;l? </td>
    <td width="50%">
      <input name="existenciaContrato" type="text" value="<%=HTMLHelper.displayField(arrendatario.existenciaContrato)%>" size="20" maxlength="20">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Por cu&aacute;nto tiempo?</td>
    <td width="50%">
      <input name="duracionContrato" type="text" value="<%=HTMLHelper.displayField(arrendatario.duracionContrato)%>" size="20" maxlength="20">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Le paga puntual?</td>
    <td width="50%">
      <input name="puntualidadPago" type="text" value="<%=HTMLHelper.displayField(arrendatario.puntualidadPago)%>" size="20" maxlength="20">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Si se atrasa &iquest;Por qu&eacute; raz&oacute;n?, &iquest;Le avisa del atraso, o usted tiene que cobrarle?</td>
    <td width="50%">
      <input name="conductaAtraso" type="text" value="<%=HTMLHelper.displayField(arrendatario.conductaAtraso)%>" size="20" maxlength="20">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Tiene usted pensado seguir rent&aacute;ndole su casa o local?</td>
    <td width="50%">
      <input name="planRentaFutura" type="text" value="<%=HTMLHelper.displayField(arrendatario.planRentaFutura)%>" size="20" maxlength="20">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Lo recomendar&iacute;a para alg&uacute;n cr&eacute;dito?</td>
    <td width="50%">
      <input name="reconmendacionCredito" type="text" value="<%=HTMLHelper.displayField(arrendatario.reconmendacionCredito)%>" size="20" maxlength="20">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;C&oacute;mo lo considera como persona? (Responsable, Honesto, Trabajador)</td>
    <td width="50%">
      <input name="descripcionCliente" type="text" value="<%=HTMLHelper.displayField(arrendatario.descripcionCliente)%>" size="20" maxlength="20">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Si &eacute;l se lo solicitara usted ser&iacute;a su fiador o aval?</td>
    <td width="50%">
      <input name="disponibilidadRespaldo" type="text" value="<%=HTMLHelper.displayField(arrendatario.disponibilidadRespaldo)%>" size="20" maxlength="20">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Calificaci&oacute;n</td>
    <td width="50%">
      <select name="calificacionCliente" size="1" >
        <%=HTMLHelper.displayCombo(catCalificacion,arrendatario.calificacionCliente)%>
      </select>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Fecha de realizaci&oacute;n de la consulta</td>
    <td width="50%">
      <input name="fechaRealizacionConsulta" type="text" value="<%=HTMLHelper.displayField(arrendatario.fechaRealizacionConsulta)%>" size="10" maxlength="10">
      (dd/mm/aaaa) </td>
  </tr>
  <tr>
  	<%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
		<td align="center" colspan="2">
			<br><input disabled type="button" value="Enviar" onClick="guardaArrendatario()">
		</td>
	<%}else{ %>
		<td align="center" colspan="2">
			<br><input type="button" value="Enviar" onClick="guardaArrendatario()">
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