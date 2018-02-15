<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.SolicitudVO"%>
<%@ page import="com.sicap.clientes.vo.ReferenciaComercialVO"%>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<%@ page import="com.sicap.clientes.util.SolicitudUtil"%>
<%@ page import="java.util.TreeMap"%>
<html>
<head>
<title>Referencia Comercial</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function guardaReferenciaComercial(){
		var form = window.document.forma;
		if ( form.nombre.value=='' || !esTexto( "^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$",form.nombre.value) ){
			alert('Debe introducir un nombre válido');
			return false;
		}
		if ( form.telefono.value=='' || !esTelefonoValido(form.telefono.value) ){
			alert('Debe introducir un Teléfono válido (incluir clave LADA)');
			return false;
		}
		if ( form.horarioLlamada.value=='' ){
			alert('Debe introducir una Hora de llamada válida');
			return false;
		}
		if ( form.tiempoConocimiento.value=='' ){
			alert('Debe indicar Hace cuánto tiempo le conoce');
			return false;
		}
		if ( form.conocimientoOcupacion.value=='' ){
			alert('Debe indicar el conocimiento del negocio');
			return false;
		}
		if ( form.relacion.value=='' ){
			alert('Debe indicar la relación con el cliente');
			return false;
		}
		if ( form.tipoProducto.value=='' ){
			alert('Debe indicar el producto que se provee');
			return false;
		}
		if ( form.frecuenciaSurtido.value=='' ){
			alert('Debe indicar la frecuencia de surtido');
			return false;
		}
		if ( form.cantidadSurtido.value=='' ){
			alert('Debe indicar la cantidad de surtido');
			return false;
		}
		if ( form.statusVentas.value=='' ){
			alert('Debe indicar cómo considera sus ventas');
			return false;
		}
		if ( form.tipoPago.value=='' ){
			alert('Debe indicar si le vende a crédito o contado');
			return false;
		}
		if ( form.calidadPago.value=='' ){
			alert('Debe indicar si el pago es puntual');
			return false;
		}
		if ( form.razonAtraso.value=='' ){
			alert('Debe indicar el comportamiento en caso de atraso');
			return false;
		}
		if ( form.cantidadPersonal.value=='' ){
			alert('Debe indicar si es la única persona que atiende el negocio');
			return false;
		}
		if ( form.recomendacionCredito.value=='' ){
			alert('Debe indicar si le recomendaria para un crédito');
			return false;
		}
		if ( form.descripcionCliente.value=='' ){
			alert('Debe indicar cómo le considera');
			return false;
		}
		if ( form.disponibilidadRespaldo.value=='' ){
			alert('Debe indicar si aceptaría ser su fiador o aval');
			return false;
		}
		if ( form.calificacionCliente.value==0 ){
			alert('Debe seleccionar la calificación');
			return false;
		}
		if ( form.fechaRealizacionConsulta.value=='' || !esFechaValida(forma.fechaRealizacionConsulta) ){
			alert('Debe introducir una fecha de realización de consulta válida');
			return false;
		}
		window.document.forma.submit();
	}

</script>

<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<%
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
int idReferenciaComercial = HTMLHelper.getParameterInt(request, "idReferenciaComercial");
TreeMap catCalificacion = CatalogoHelper.getCatalogo(ClientesConstants.CAT_COMPORTAMIENTO_CLIENTE);
ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
SolicitudVO solicitud = new SolicitudVO();
ReferenciaComercialVO referencia = new ReferenciaComercialVO();
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);

if ( idSolicitud!=0 ){
	solicitud = cliente.solicitudes[indiceSolicitud];
	if ( solicitud.referenciasComerciales!=null && idReferenciaComercial!=0 )
		referencia = solicitud.referenciasComerciales[idReferenciaComercial-1];
}

%>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<center>
<form action="controller" method="post" name="forma">
<input type="hidden" name="command" value="guardaReferenciaComercial">
<input type="hidden" name="idSolicitud" value="<%=idSolicitud%>">
<input type="hidden" name="idReferenciaComercial" value="<%=idReferenciaComercial%>">
<table border="0" width="100%">
	<tr>
	<td align="center">	   

<h3>Referencia Comercial</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table width="100%" border="0" cellpadding="0">
  <tr>
    <td width="50%" align="right">Nombre</td>
    <td width="50%">
      <input type="text" name="nombre" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referencia.nombre)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Tel&eacute;fono</td>
    <td width="50%">
      <input type="text" name="telefono" size="15" maxlength="15" value="<%=HTMLHelper.displayField(referencia.telefono)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;A qu&eacute; hora se puede llamar?</td>
    <td width="50%">
      <input type="text" name="horarioLlamada" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referencia.horarioLlamada)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Hace cu&aacute;nto tiempo conoce al Sr.(a)&#63;</td>
    <td width="50%">
      <input type="text" name="tiempoConocimiento" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referencia.tiempoConocimiento)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Sabe usted que negocio tiene el Sr. (a)&#63;</td>
    <td width="50%">
      <input type="text" name="conocimientoOcupacion" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referencia.conocimientoOcupacion)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Qu&eacute; relaci&oacute;n tiene con &eacute;l&#63;</td>
    <td width="50%">
      <input type="text" name="relacion" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referencia.relacion)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">En el caso de ser proveedor de alg&uacute;n producto: &iquest;Qu&eacute; producto le provee&#63;</td>
    <td width="50%">
      <input type="text" name="tipoProducto" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referencia.tipoProducto)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Cu&aacute;ntas veces le surte al mes, o a la semana&#63;</td>
    <td width="50%">
      <input type="text" name="frecuenciaSurtido" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referencia.frecuenciaSurtido)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Qu&eacute; cantidad le surte al mes (en pesos o en producto&#63;</td>
    <td width="50%">
      <input type="text" name="cantidadSurtido" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referencia.cantidadSurtido)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Seg&uacute;n sus pedidos &iquest;c&oacute;mo considera sus ventas&#63;</td>
    <td width="50%">
      <input type="text" name="statusVentas" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referencia.statusVentas)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Le da cr&eacute;dito&#63; O &iquest;Manejan sus cuentas de contado&#63;</td>
    <td width="50%">
      <input type="text" name="tipoPago" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referencia.tipoPago)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">En caso de darle cr&eacute;dito: &iquest;Cu&aacute;ntos d&iacute;as le da para pagar&#63;</td>
    <td width="50%">
      <input type="text" name="diasPago" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referencia.diasPago)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Le paga puntual&#63;</td>
    <td width="50%">
      <input type="text" name="calidadPago" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referencia.calidadPago)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Si se atrasa &iquest;Por qu&eacute; raz&oacute;n&#63;, &iquest;Le avisa del atraso, o usted tiene que cobrarle&#63;</td>
    <td width="50%">
      <input type="text" name="razonAtraso" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referencia.razonAtraso)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Es la &uacute;nica persona que atiende el negocio o ha tratado con alguien m&aacute;s&#63;</td>
    <td width="50%">
      <input type="text" name="cantidadPersonal" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referencia.cantidadPersonal)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Lo recomendar&iacute;a para alg&uacute;n cr&eacute;dito&#63;</td>
    <td width="50%">
      <input type="text" name="recomendacionCredito" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referencia.recomendacionCredito)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;C&oacute;mo lo considera como persona&#63; (Responsable, Honesto, Incumplido)</td>
    <td width="50%">
      <input type="text" name="descripcionCliente" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referencia.descripcionCliente)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">&iquest;Si &eacute;l se lo solicitara usted ser&iacute;a su fiador o aval&#63;</td>
    <td width="50%">
      <input type="text" name="disponibilidadRespaldo" size="45" maxlength="45" value="<%=HTMLHelper.displayField(referencia.disponibilidadRespaldo)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Calificaci&oacute;n</td>
    <td width="50%">
      <select name="calificacionCliente" size="1" >
        <%=HTMLHelper.displayCombo(catCalificacion, referencia.calificacionCliente)%>
      </select>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Fecha de realizaci&oacute;n de la consulta</td>
    <td width="50%">
      <input type="text" name="fechaRealizacionConsulta" size="10" maxlength="10" value="<%=HTMLHelper.displayField(referencia.fechaRealizacionConsulta)%>">
      (dd/mm/aaaa) </td>
  </tr>
  <tr>
  	<%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
		<td align="center" colspan="2">
			<br><input disabled type="button" value="Enviar" onClick="guardaReferenciaComercial()">
		</td>
	<%}else{ %>
		<td align="center" colspan="2">
			<br><input type="button" value="Enviar" onClick="guardaReferenciaComercial()">
		</td>
	<%} %>
	</tr>
</table>

		</td>
	</tr>
</table>
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>
