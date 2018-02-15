<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>

<%
	int idSolicitud  = HTMLHelper.getParameterInt(request,"idSolicitud");
	ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
	int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
	SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
	DecisionComiteVO decisionComite = solicitud.decisionComite;
	TreeMap catDecision   = CatalogoHelper.getCatalogo(ClientesConstants.CAT_DECISION_COMITE);
	TreeMap catCausaRechazo = CatalogoHelper.getCatalogo(ClientesConstants.CAT_CAUSA_RECHAZO_COMITE);
	TreeMap catCausaRechazoCliente = CatalogoHelper.getCatalogo(ClientesConstants.CAT_CAUSA_CLIENTE_RECHAZO_CREDITO);
	TreeMap catFrecuenciaPagos  = CatalogoHelper.getCatalogoFrecuenciasPago(solicitud.tipoOperacion);
	TreeMap catMotivoConidcionamiento = CatalogoHelper.getCatalogo(ClientesConstants.C_MOTIVO_CONDICIONAMIENTO);	

	TreeMap catTasas = CatalogoHelper.getCatalogoTasas(solicitud.tipoOperacion);
	TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(solicitud.tipoOperacion);

	Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");

	if( decisionComite==null ){
		decisionComite = new DecisionComiteVO();
		decisionComite.montoSinComision = solicitud.montoSolicitado;
	}else if ( decisionComite.montoSinComision==0 && decisionComite.decisionComite!=ClientesConstants.CREDITO_RECHAZADO ){
		decisionComite.montoSinComision = ClientesUtil.calculaMontoSinComision(decisionComite.montoAutorizado, decisionComite.comision, catComisiones, solicitud.tipoOperacion);
	}
%>
<html>
<head>
<title>Autorizaci&oacute;n de cr&eacute;dito</title>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
<script language="Javascript" src="./js/functions.js"></script>

<script type="text/javascript">
<!--

	function guardaDecisionComite(){
		window.document.forma.command.value='guardaAutorizacionMaxZapatos';
		window.document.forma.idSolicitud.value='<%=idSolicitud%>';
		if(window.document.forma.decisionComite.value=='0'){
			alert("Debe seleccionar la decisión del comité")
			return false;
		}
		if(window.document.forma.fechaRealizacion.value=='' || esPosterior(window.document.forma.fechaRealizacion.value, 'Hoy') ){
			alert("Favor de capturar la Fecha de autorización")
			return false;
		}else if(!esFechaValida(window.document.forma.fechaRealizacion,false)){
			alert("La fecha de autorización es inválida, favor de revisar.")
			return false;
		}
		if(window.document.forma.decisionComite.value=='<%=ClientesConstants.CREDITO_RECHAZADO%>'){
			if( !(window.document.forma.causaRechazo.value=='0' ^ window.document.forma.motivoRechazoCliente.value=='0') ){
				alert("Debe seleccionar una opción para [Causa del rechazo] o para [Cliente rechaza condiciones]. Sólo esta permitido usar uno de los dos campos")
				return false;
		    }
		}else{
			if ( window.document.forma.montoSinComision.value!='' ){
				if ( !esMontoValido(window.document.forma.montoSinComision.value, <%=solicitud.tipoOperacion%>) ){
					alert("El monto es inválido");
					return false;
				}
			}else{
				alert("Debe capturar el monto");
				return false;
			}
			if ( window.document.forma.plazoAutorizado.value!='' ){
				if ( !esPlazoValido(window.document.forma.plazoAutorizado.value, <%=solicitud.tipoOperacion%>) ){
					alert("El plazo es inválido");
					return false;
				}
			}else{
				alert("Debe capturar el plazo");
				return false;
			}
			if(window.document.forma.comision.value=='0'){
				alert("Debe seleccionar una comision.")
				return false;
		    }
		    if(window.document.forma.tasa.value=='0'){
				alert("Debe seleccionar una tasa.")
				return false;
		    }
		    if(window.document.forma.frecuenciaPago.value=='0'){
				alert("Debe seleccionar una frecuencia de pago.")
				return false;
		    }
		}
		if(window.document.forma.decisionComite.value=='<%=ClientesConstants.CREDITO_CONDICIONADO%>'){
			if(window.document.forma.motivoCondicionamiento.value=='0'){
			alert("Debe seleccionar un motivo del condicionamiento.")
			return false;
		    }
		}
		window.document.forma.submit();
	}	


function comiteAprueba(){
	if(window.document.forma.decisionComite.value=='<%=ClientesConstants.CREDITO_RECHAZADO%>'){
 		window.document.forma.detalleMotivoRechazoCliente.disabled=false;
		window.document.forma.causaRechazo.disabled=false;
		window.document.forma.motivoRechazoCliente.disabled=false;	
	}
	else{
		window.document.forma.detalleMotivoRechazoCliente.disabled=true;
		window.document.forma.causaRechazo.disabled=true;
		window.document.forma.motivoRechazoCliente.disabled=true;
	}
	if(window.document.forma.decisionComite.value=='<%=ClientesConstants.CREDITO_CONDICIONADO%>'){
 		window.document.forma.motivoCondicionamiento.disabled=false;
	}
	else{
		window.document.forma.motivoCondicionamiento.disabled=true;
	}
 }

 -->
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<CENTER>
<form action="controller" method="post" name="forma">
<input type="hidden" name="command" value="">
<input type="hidden" name="idSolicitud" value="<%=solicitud.idSolicitud%>">
<input type="hidden" name="idCliente" value="<%=solicitud.idCliente%>">

<table border="0" width="100%">
	<tr>
	<td align="center">
<h3>Autorizaci&oacute;n de cr&eacute;dito</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table border="0" cellPadding=0 width="100%">
  <tr>
    <td width="50%" align="right">Fecha de autorizaci&oacute;n</td>
    <td colspan="3" width="50%">
        <input type="text" name="fechaRealizacion" size="10" maxlength="10" id="fechaRealizacion" value="<%=HTMLHelper.displayField(decisionComite.fechaRealizacion)%>" />(dd/mm/aaaa)
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Fecha de captura</td>
    <td colspan="3">
    	<input type="text" name="fechaCaptura" size="10" maxlength="10" id="fechaCaptura"  value="<%=HTMLHelper.displayField(decisionComite.fechaCaptura, new java.util.Date())%>" readOnly="readOnly"/>(dd/mm/aaaa)
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Decisi&oacute;n</td>
    <td colspan="3"><select name="decisionComite" id="decisionComite" onClick="comiteAprueba();"><%=HTMLHelper.displayCombo(catDecision,decisionComite.decisionComite)%></select></td>
  </tr>
  <tr>
    <td width="50%" align="right">Causa del rechazo</td>
    <td colspan="3"><select name="causaRechazo" id="causaRechazo"><%=HTMLHelper.displayCombo(catCausaRechazo,decisionComite.causaRechazo)%> </select></td>
  </tr>
  <tr>
    <td width="50%" align="right">Cliente rechaza condiciones</td>
    <td width="16%"><select  name="motivoRechazoCliente"  id="motivoRechazoCliente"> <%=HTMLHelper.displayCombo(catCausaRechazoCliente,decisionComite.motivoRechazoCliente)%></select></td>
  </tr>
  <tr>
    <td width="50%" align="right">Motivo del rechazo</td>
    <td width="41%">
    	<input type="text" name="detalleMotivoRechazoCliente" size="20" maxlength="25" value="<%=HTMLHelper.displayField(decisionComite.detalleMotivoRechazoCliente)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Monto línea $</td>
    <td colspan="3"><input type="text" size="10" maxlength="12" name="montoSinComision" value="<%=HTMLHelper.formatoMonto(decisionComite.montoSinComision)%>"></td>
  </tr>
  <tr>
    <td width="50%" align="right">Monto con comisión $</td>
    <td colspan="3"><input type="text" size="10" maxlength="12" name="montoAutorizado" value="<%=HTMLHelper.formatoMonto(decisionComite.montoAutorizado)%>" readonly="readonly" class="soloLectura"></td>
  </tr>
  <tr>
    <td width="50%" align="right">Plazo autorizado</td>
    <td colspan="3"><input type="text" name="plazoAutorizado" size="5" maxlength="2" value="2" class="soloLectura" readonly="readonly"> (Meses)
  </tr>
  <tr>
    <td width="50%" align="right">Comisi&oacute;n %</td>
	<td colspan="3"><select name="comision"><%=HTMLHelper.displayComboComisiones(catComisiones,decisionComite.comision)%></select></td>
  </tr>
  <tr>
    <td width="50%" align="right">Tasa</td>
	<td colspan="3"><select name="tasa"><%=HTMLHelper.displayComboTasas(catTasas,decisionComite.tasa)%></select></td>
  </tr>
  <tr>
    <td width="50%" align="right">Frecuencia de pagos</td>
    <td colspan="3"><select name="frecuenciaPago" id="frecuenciaPago" ><%=HTMLHelper.displayCombo(catFrecuenciaPagos,decisionComite.frecuenciaPago)%></select></td>
  </tr>
  <tr>
    <td width="50%" align="right">Motivo condicionamiento pagos</td>
    <td colspan="3"><select name="motivoCondicionamiento" id="motivoCondicionamiento"><%=HTMLHelper.displayCombo(catMotivoConidcionamiento,decisionComite.motivoCondicionamiento)%></select></td>
  </tr>  
  <tr>
    <td width="50%" align="right">Comentarios</td>
    <td colspan="3"><textarea name="comentariosComite" cols="35" id="comentariosComite"><%=HTMLHelper.displayField(decisionComite.comentariosComite)%></textarea></td>
  </tr>
  <tr>
	<td colspan="8" align="center">
  	<%if( solicitud.disposiciones!=null ){ %>
		<br><input disabled type="button" name="" value="Enviar" onClick="guardaDecisionComite()" >
  	<%}else{ %>
 		<br><input type="button" name="" value="Enviar" onClick="guardaDecisionComite()" >
  	<%} %>
  	</td>
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
