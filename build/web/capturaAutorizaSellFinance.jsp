<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>

<%
	String titulo = "";
	String nombreFecha = "";
	String txtComplemento = " del comit&eacute;";
	int idSolicitud  = HTMLHelper.getParameterInt(request,"idSolicitud");
	ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
	int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
	SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
	DecisionComiteVO decisionComite = solicitud.decisionComite;
	ScoringVO score = new ScoringVO();
//	TreeMap catDesembolso = CatalogoHelper.getCatalogoDesembolso();
	TreeMap catDecision   = CatalogoHelper.getCatalogo(ClientesConstants.CAT_DECISION_COMITE);
	TreeMap catCausaRechazo = CatalogoHelper.getCatalogo(ClientesConstants.CAT_CAUSA_RECHAZO_COMITE);
	TreeMap catCausaRechazoCliente = CatalogoHelper.getCatalogo(ClientesConstants.CAT_CAUSA_CLIENTE_RECHAZO_CREDITO);
	TreeMap catFrecuenciaPagos  = CatalogoHelper.getCatalogoFrecuenciasPago(solicitud.tipoOperacion);
	TreeMap catMotivoConidcionamiento = CatalogoHelper.getCatalogo(ClientesConstants.C_MOTIVO_CONDICIONAMIENTO);	
	TreeMap catPlanSellFinance  = CatalogoHelper.getCatalogoPlanesSellFinance();
//    TreeMap catPlanSellFinance  = CatalogoHelper.getCatalogo(ClientesConstants.CAT_DESTINO_MARCA);
	TreeMap catTasas = CatalogoHelper.getCatalogoTasas(solicitud.tipoOperacion);
	TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(solicitud.tipoOperacion);

	Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
/*	if ( cliente.solicitudes[indiceSolicitud].amortizacion!=null && cliente.solicitudes[indiceSolicitud].amortizacion.length>0 )
		catDesembolso = SolicitudHelper.getCatalogoDesembolso(false);
	else
		catDesembolso = SolicitudHelper.getCatalogoDesembolso(true);*/
	if(decisionComite ==null)
		decisionComite = new DecisionComiteVO();
	if(solicitud.sellFinance == null)
		solicitud.sellFinance = new SellFinanceVO();
	if ( solicitud.tipoOperacion!=ClientesConstants.GRUPAL ){
		if ( solicitud.scoring!=null ){
			score = solicitud.scoring;
			if ( decisionComite.montoAutorizado==0 ){
				if ( solicitud.tipoOperacion==ClientesConstants.VIVIENDA ){
					decisionComite.montoAutorizado = ClientesUtil.calculaMontoConComision(score.monto, 1, catComisiones);
					decisionComite.comision = 1;
				}
				else{
					decisionComite.montoAutorizado = ClientesUtil.calculaMontoConComision(score.monto, score.comision, catComisiones);
					decisionComite.comision = score.comision;
				}
				decisionComite.plazoAutorizado = score.plazo;
				//decisionComite.comision = score.comision;
				decisionComite.tasa = score.tasa;
			}
		}
		titulo = "Autorizaci&oacute;n";
		nombreFecha = "autorizaci&oacute;n";
		txtComplemento = "";
	}else{
		titulo = "Autorizaci&oacute;n";
		nombreFecha = "autorizaci&oacute;n";
		txtComplemento = "";
	}
	
	//Validaciones para deshabilitar botón de enviar
	//boolean disabled = false;
	//if( solicitud.tipoOperacion==ClientesConstants.VIVIENDA 
	//	&& !ArchivosAsociadosHelper.existe(  cliente.solicitudes[indiceSolicitud].archivosAsociados, ClientesConstants.ARCHIVO_TIPO_CERTIFICADO_VIVIENDA ))
	//	disabled = true;
	
%>
<html>
<head>
<title><%=titulo%> de cr&eacute;dito</title>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
<script language="Javascript" src="./js/functions.js"></script>

<script type="text/javascript">
<!--

	function guardaDecisionComite(){
		window.document.forma.command.value='guardaAutorizacionSellFinance';
		window.document.forma.idSolicitud.value='<%=idSolicitud%>';
		if(window.document.forma.decisionComite.value=='0'){
			alert("Debe seleccionar la decisión del comité , favor de revisar.")
			return false;
		}
		if(window.document.forma.fechaRealizacion.value=='' || esPosterior(window.document.forma.fechaRealizacion.value, 'Hoy') ){
			alert("Favor de capturar la Fecha de realización del comité.")
			return false;
		}else if(!esFechaValida(window.document.forma.fechaRealizacion,false)){
			alert("La fecha de realización del comité es inválida, favor de revisar.")
			return false;
		}
		if(window.document.forma.decisionComite.value=='<%=ClientesConstants.CREDITO_RECHAZADO%>'){
			if(window.document.forma.causaRechazo.value=='0'){
				alert("Debe seleccionar una causa del rechazo.")
				return false;
			}
		}else{/*
			if ( window.document.forma.desembolsado.value==0 ){
				alert("Debe indicar si el crédito fue desembolsado")
				return false;
			}*/
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
		     if(window.document.forma.planSellFinance.value=='0'){
				alert("Debe seleccionar un plan.")
				return false;
		    }
		    /*if ( window.document.forma.desembolsado.value==2 ){
		    	res = confirm('Está seguro que desea registrar el desembolso del crédito con la fecha de hoy ?');
		    	if ( !res ) return res;
		    	}*/
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
<h3><%=titulo%> de cr&eacute;dito</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table border="0" cellPadding=0 width="100%">
  <tr>
    <td width="50%" align="right">Fecha de <%=nombreFecha%></td>
    <td colspan="3" width="50%">
        <input type="text" name="fechaRealizacion" size="10" maxlength="10" id="fechaRealizacion" value="<%=HTMLHelper.displayField(decisionComite.fechaRealizacion)%>" />(dd/mm/aaaa)
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Fecha de captura de resultados<%=txtComplemento%></td>
    <td colspan="3">
    	<input type="text" name="fechaCaptura" size="10" maxlength="10" id="fechaCaptura"  value="<%=HTMLHelper.displayField(decisionComite.fechaCaptura, new java.util.Date())%>" readOnly="readOnly"/>(dd/mm/aaaa)
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Fecha de desembolso</td>
    <td colspan="3">
    	<input type="text" name="fechaDesembolso" size="10" maxlength="10" value="<%=HTMLHelper.displayField(solicitud.fechaDesembolso)%>" readOnly="readOnly"/>(dd/mm/aaaa)
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Decisi&oacute;n<%=txtComplemento%></td>
    
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
    <td width="50%" align="right">Plan Descuento Nómina</td>
    <td width="16%"><select  name="planSellFinance"  id="motivoRechazoCliente"> <%=HTMLHelper.displayComboProductosSellFinance(catPlanSellFinance, solicitud.sellFinance.idPlan)%></select></td>
  </tr>
  <tr>
    <td width="50%" align="right">Monto a desembolsar $</td>
    <td colspan="3"><input type="text" size="10" maxlength="12" name="montoSinComision" value="<%=HTMLHelper.formatoMonto(ClientesUtil.calculaMontoSinComision(decisionComite.montoAutorizado, decisionComite.comision,catComisiones))%>"></td>
  </tr>
  <tr>
    <td width="50%" align="right">Monto con comisión $</td>
    <td colspan="3"><input type="text" size="10" maxlength="12" name="montoAutorizado" value="<%=HTMLHelper.formatoMonto(decisionComite.montoAutorizado)%>" disabled="disabled"></td>
  </tr>
  <tr>
    <td width="50%" align="right">Plazo autorizado</td>
    <td colspan="3"><input type="text" name="plazoAutorizado" size="5" maxlength="2" value="<%=HTMLHelper.displayField(decisionComite.plazoAutorizado)%>">
<% 	if ( solicitud.tipoOperacion==ClientesConstants.GRUPAL ){%>
		(Semanas)
<%} else{%>
  	(Periodos de pago)
<%} %>
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
    <tr>
    <td width="50%" align="right">Frecuencia de pagos</td>
    
    <td colspan="3"><select name="frecuenciaPago" id="frecuenciaPago" ><%=HTMLHelper.displayCombo(catFrecuenciaPagos,decisionComite.frecuenciaPago)%></select></td>
  </tr>
  <tr>
    <td width="50%" align="right">Motivo condicionamiento pagos</td>
    <td colspan="3"><select name="motivoCondicionamiento" id="motivoCondicionamiento"><%=HTMLHelper.displayCombo(catMotivoConidcionamiento,decisionComite.motivoCondicionamiento)%></select></td>
  </tr>  
  <tr>
    <td width="50%" align="right">Comentarios<%=txtComplemento%></td>
    <td colspan="3"><textarea name="comentariosComite" cols="35" id="comentariosComite"><%=HTMLHelper.displayField(decisionComite.comentariosComite)%></textarea></td>
  </tr>
  <tr>
  	<%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
	  	<td colspan="8" align="center">
	 			<br><input disabled type="button" name="" value="Enviar" onClick="guardaDecisionComite()" >
	  	</td>
  	<%}else{ %>
	  	<td colspan="4" align="center">
	 			<br><input type="button" name="" value="Enviar" onClick="guardaDecisionComite()" >
	  	</td>
  	<%} %>
  </tr>
</table>

		</td>
	</tr>
</table>
</form>
</CENTER>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>
