<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.SolicitudVO"%>
<%@ page import="com.sicap.clientes.vo.ReferenciaPersonalVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>

<html>
<head>
<title>Rferencia Personal</title>

<%
	int idReferencia = HTMLHelper.getParameterInt(request,"idReferencia");
	int idSolicitud  = HTMLHelper.getParameterInt(request,"idSolicitud");
	ReferenciaPersonalVO referenciaPersonal = new ReferenciaPersonalVO();
	ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
	int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
	String modificaReferenciasPersonal = "ACTUALIZA"; 
	SolicitudVO solicitud = new SolicitudVO();
	if ( idSolicitud!=0 ){
		solicitud = cliente.solicitudes[indiceSolicitud];
		if(solicitud.referenciasPersonales!=null && idReferencia>0){
			referenciaPersonal = solicitud.referenciasPersonales[idReferencia-1];			
		}
		else{
			referenciaPersonal = new ReferenciaPersonalVO();
			modificaReferenciasPersonal = "INSERTA";
		}
	}
	
	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
%>

<script language="Javascript" src="./js/functions.js">
<!-- 

 -->
 </script>

<script type="text/javascript">
<!--
	function guardaReferenciaPersonal(){
		var action  = window.document.forma.modificaObligadoCredito.value;
		if ( window.document.forma.fechaRealizacionConsulta.value!='' ){
			if ( !esFechaValida(window.document.forma.fechaRealizacionConsulta,false) ){
				alert("La Fecha de Realización de la consulta es inválida");
				return false;
			}
		}
		else{
			alert("Debe capturar la Fecha de Realización  de la solicitud");
			return false;
		}
		
		if(action=='INSERTA')
			window.document.forma.command.value="capturaReferenciaPersonal";
		else{
			window.document.forma.command.value="modificaRefPer";
		}
		window.document.forma.submit();
	}
//-->
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<center>
<form action="controller" method="post" name="forma">
<input type="hidden" name="command" value="">
<input type="hidden" name="idSolicitud" value="<%=idSolicitud%>">
<input type="hidden" name="idReferencia" value="<%=idReferencia%>">
<input type="hidden" name="idCliente" value="<%=cliente.idCliente%>">
<input type="hidden" name="modificaObligadoCredito" value="<%=modificaReferenciasPersonal%>">
<table border="0" width="100%">
	<tr>
	<td align="center">
<h3>Referencia Personal </h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table width="100%" border="0" cellpadding="0">
  <tr>

    <td width="50%" align="right">Nombre</td>
    <td width="50%">
      <input type="text" name="nombre" size="45" maxlength="180" value="<%=HTMLHelper.displayField(referenciaPersonal.nombre) %>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Tel&eacute;fono</td>
    <td width="50%">
      <input type="text" name="telefono" size="15" maxlength="45" value="<%=HTMLHelper.displayField(referenciaPersonal.telefono) %>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;A qu&eacute; hora se puede llamar ?</td>
    <td width="50%">
      <input type="text" name="horarioLlamada" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referenciaPersonal.horarioLlamada) %>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Qu&eacute; relaci&oacute;n tiene con &eacute;l? (amigo, vecino, pariente)</td>
    <td width="50%">
      <input type="text" name="relacion" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referenciaPersonal.relacion) %>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Hace cu&aacute;nto tiempo conoce al Sr. (a) </td>
    <td width="50%">
      <input type="text" name="tiempoConocimiento" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referenciaPersonal.tiempoConocimiento) %>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Sabe a qu&eacute; se dedica? </td>
    <td width="50%">
      <input type="text" name="conocimientoOcupacion" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referenciaPersonal.conocimientoOcupacion) %>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Ha visitado alguna vez su negocio? </td>
    <td width="50%">
      <input type="text" name="visitaNegocio" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referenciaPersonal.visitaNegocio) %>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;D&oacute;nde vende? (en un establecimiento, en su casa, en un tianguis)</td>
    <td width="50%">
      <input type="text" name="dondeVende" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referenciaPersonal.dondeVende) %>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Sabe cu&aacute;nto tiempo tiene operando su negocio?</td>
    <td width="50%">
      <input type="text" name="tiempoOperacion" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referenciaPersonal.tiempoOperacion) %>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;C&oacute;mo considera sus ventas?</td>
    <td width="50%">
      <input type="text" name="statusVentas" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referenciaPersonal.statusVentas) %>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Vive con su esposa e hijos?</td>
    <td width="50%">
      <input type="text" name="conQuienVive" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referenciaPersonal.conQuienVive) %>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;En donde vive? &iquest;Tiene casa propia? </td>
    <td width="50%">
      <input name="conocimientoVivienda" type="text" id="conocimientoVivienda" value="<%=HTMLHelper.displayField(referenciaPersonal.conocimientoVivienda) %>"" maxlength="45" size="45">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Conoce usted alg&uacute;n problema que pueda impedir que el cliente pague?</td>
    <td width="50%">
      <input name="impedimentoPago" type="text" id="impedimentoPago" value="<%=HTMLHelper.displayField(referenciaPersonal.impedimentoPago) %>" maxlength="45" size="45">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Alguna vez le ha prestado dinero? (si no le ha prestado dinero, &iquest;Por qu&eacute;?)</td>
    <td width="50%">
      <input name="prestamoDinero" type="text" id="prestamoDinero" value="<%=HTMLHelper.displayField(referenciaPersonal.prestamoDinero) %>" maxlength="45" size="45">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Lo recomendar&iacute;a para alg&uacute;n cr&eacute;dito?</td>
    <td width="50%">
      <input name="recomendacionCredito" type="text" id="recomendacionCredito" value="<%=HTMLHelper.displayField(referenciaPersonal.recomendacionCredito) %>" maxlength="45" size="45">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Usted le prestar&iacute;a dinero?</td>
    <td width="50%">
      <input name="prestriaDinero" type="text" id="prestriaDinero"  value="<%=HTMLHelper.displayField(referenciaPersonal.prestriaDinero) %>" maxlength="45" size="45">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Si &eacute;l se lo solicitara usted ser&iacute;a su fiador o aval?</td>
    <td width="50%">
      <input type="text" name="disponibilidadRespaldo" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referenciaPersonal.disponibilidadRespaldo) %>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;C&oacute;mo lo considera como persona? (Responsable, Honesto, Incumplido)</td>
    <td width="50%">
      <input type="text" name="descripcionCliente" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referenciaPersonal.descripcionCliente) %>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Calificaci&oacute;n</td>
    <td width="50%">
      <select name="calificacionCliente" size="1">
        <option selected value="0">Seleccione...</option>
        <option value="1">Buena</option>
        <option value="2">Mala</option>
      </select>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Fecha de realizaci&oacute;n de la consulta</td>
    <td width="50%">
      <input name="fechaRealizacionConsulta" type="text" id="fechaRealizacionConsulta" value="<%=HTMLHelper.displayField(referenciaPersonal.fechaRealizacionConsulta)%>" size="12" maxlength="10">
      (dd/mm/aaaa) </td>
  </tr>
  <tr>
  	 <%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
		<td align="center" colspan="2">
			<br><input disabled type="button" value="Enviar" onClick="guardaReferenciaPersonal()">
		</td>
	  <%}else{ %>
	  	<td align="center" colspan="2">
			<br><input type="button" value="Enviar" onClick="guardaReferenciaPersonal()">
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
