<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>

<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>

<%
	ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
	int idSolicitud  = HTMLHelper.getParameterInt(request,"idSolicitud");
	int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
	SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
	TreeMap catTasas = CatalogoHelper.getCatalogoTasas(solicitud.tipoOperacion);
	TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(solicitud.tipoOperacion);
	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
	LimiteCreditoVO limite = new LimiteCreditoVO();
	boolean deshabilitarFormulario = false;
	
	if ( !request.isUserInRole("ANALISIS_CREDITO") || (solicitud.estatus==ClientesConstants.SOLICITUD_AUTORIZADA && solicitud.limites!=null) || solicitud.desembolsado==ClientesConstants.DESEMBOLSADO )
		deshabilitarFormulario = true;
	if( solicitud.limites!=null )
		limite = solicitud.limites;
	else
		limite.monto = solicitud.montoSolicitado;
		
	double montoMaximo = SolicitudUtil.montoMaximoSolicitud(cliente);
%>
<html>
<head>
<title>L&iacute;mites</title>
<script language="Javascript" src="./js/functions.js"></script>

<script type="text/javascript">
	function guardaLimites(montoMaximo){	

		if(window.document.forma.tasa.value=='0'){
			alert("Debe seleccionar una tasa.");
			return false;
	    }
		if(window.document.forma.comision.value=='0'){
			alert("Debe seleccionar una comision.");
			return false;
		}
		if ( window.document.forma.montoAutorizado.value!='' ){
			if ( !esMontoValido(window.document.forma.montoAutorizado.value, <%=solicitud.tipoOperacion%>) ){
				alert("El monto es inválido");
				return false;
			}
		}else{
			alert("Debe capturar el monto");
			return false;
		}

	    if ( window.document.forma.montoAutorizado.value>montoMaximo && montoMaximo!='0.0' ){
			if ( confirm('Está seguro de solicitar esa cantidad que supera el 30% extra del crédito anterior?' ) )
				window.document.forma.submit();
		}else{
			window.document.forma.submit();	
		}

	}	
</script>

<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<center>
<form action="controller" method="post" name="forma">
<input type="hidden" name="command" value="guardaLimitesCredito">
<input type="hidden" name="idSolicitud" value="<%=solicitud.idSolicitud%>">

<table border="0" width="100%">
	<tr>
	<td align="center" width="100%">
	<h3>L&iacute;mites</h3>
	<%=HTMLHelper.displayNotifications(notificaciones)%>
<table border="0" cellpadding="0" width="100%">
<tr>
    <td width="50%" align="right">Tasa</td>
	<td width="50%"><select  name="tasa" <%if (deshabilitarFormulario) out.print("disabled");%> ><%=HTMLHelper.displayComboTasas(catTasas,limite.tasa)%></select></td>
	<tr>
    <td width="50%" align="right">Comisi&oacute;n %</td>
	<td width="50%"><select name="comision" <%if (deshabilitarFormulario) out.print("disabled");%> ><%=HTMLHelper.displayComboComisiones(catComisiones,limite.comision)%></select></td>
  </tr>
  <tr>
    <td width="50%" align="right">Monto autorizado $</td>
    <td width="50%"><input type="text" size="10" maxlength="12" name="montoAutorizado" value="<%=HTMLHelper.formatoMonto(limite.monto)%>" <%if (deshabilitarFormulario) out.print("disabled");%>></td>
  </tr>
<tr>
    <td width="50%" align="right">Garant&iacute;a</td>
  <td width="50%"><%=HTMLHelper.displayCheck("garant&iacute;a", limite.garantia, deshabilitarFormulario)%></td>
</tr>
  <tr>
    <td width="50%" align="right">Comentarios</td>
    <td width="50%"><textarea name="comentarios" cols="35" rows="5" <%if (deshabilitarFormulario) out.print("disabled");%>><%=HTMLHelper.displayField(limite.comentarios)%></textarea></td>
  </tr>
  <tr>
  	<td colspan="2" align="center">
  		<br><input type="button" name="" value="Enviar" onClick="guardaLimites(<%=montoMaximo%>)" <%if (deshabilitarFormulario) out.print("disabled");%> >
  	</td>
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
