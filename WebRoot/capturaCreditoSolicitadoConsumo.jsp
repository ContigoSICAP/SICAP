<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>
<%
Calendar cal = Calendar.getInstance();
UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
Date[] fechasInhabiles = (Date[])session.getAttribute("INHABILES");
boolean diainhabil = FechasUtil.esDiaInhabil( cal.getTime(), fechasInhabiles);
TreeMap catDestinoCredito = CatalogoHelper.getCatalogo(ClientesConstants.CAT_DESTINOS_CONSUMO);
TreeMap catMarca = CatalogoHelper.getCatalogo(ClientesConstants.CAT_DESTINO_MARCA);
TreeMap catProducto = CatalogoHelper.getCatalogo(ClientesConstants.CAT_DESTINO_PRODUCTO);
TreeMap catEstatus = null;
TreeMap catCausaRechazoCliente = CatalogoHelper.getCatalogo(ClientesConstants.CAT_CAUSA_CLIENTE_RECHAZO_CREDITO);
TreeMap catComisiones = null;
TreeMap catTasas = null;
TreeMap catCausaRechazo = CatalogoHelper.getCatalogo(ClientesConstants.CAT_CAUSA_RECHAZO_COMITE);
boolean permiteEditar = false;
boolean esReestructura = false;
Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
SolicitudVO solicitud = new SolicitudVO();
ScoringVO score = new ScoringVO();
SellFinanceVO sellFinance = new SellFinanceVO();
DecisionComiteVO decisionComite = null;

if ( cliente.solicitudes!=null && idSolicitud>0 ){
	solicitud = cliente.solicitudes[indiceSolicitud];
	    if(solicitud.reestructura==1){
	       esReestructura = true;
	    }
	if ( solicitud.tipoOperacion==ClientesConstants.MAX_ZAPATOS )
		permiteEditar = true;
	catTasas = CatalogoHelper.getCatalogoTasas(solicitud.tipoOperacion);
	catComisiones = CatalogoHelper.getCatalogoComisiones(solicitud.tipoOperacion);
	catEstatus = CatalogoHelper.getCatalogoEstatus(solicitud.estatus);
	if(solicitud.decisionComite!=null){
		decisionComite = solicitud.decisionComite;
	}else{
		decisionComite = new DecisionComiteVO();
	}
	if ( solicitud.scoring!=null )
		score = solicitud.scoring;
	if(solicitud.tipoOperacion == ClientesConstants.SELL_FINANCE && solicitud.sellFinance != null)
		sellFinance=solicitud.sellFinance;
}

TreeMap catDesembolso = CatalogoHelper.getCatalogoDesembolso(request, solicitud);
TreeMap catFrecuenciasPago = CatalogoHelper.getCatalogoFrecuenciasPago(solicitud.tipoOperacion);
boolean esExterno = false;
if(usuario.identificador.equals("E"))
	esExterno = true;
%>
<html>
<head>
<title>Alta/Modificaci&oacute;n de cliente</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function guardaCreditoSolicitadoConsumo(){

		window.document.forma.command.value='guardaCreditoSolicitadoConsumo';

		if ( window.document.forma.desembolsado.value==2 ){
			<%if ( diainhabil ){%>
				alert("El desembolso no puede efectuarse debido a que es fecha de día inhábil");
				return false;
			<%}%>
		}

		if ( window.document.forma.montoSolicitado.value!='' ){
			if ( !esMontoValido(window.document.forma.montoSolicitado.value, <%=solicitud.tipoOperacion%>) ){
				alert("El monto es inv\u00e1lido");
				return false;
			}
		}else{
			alert("Debe capturar el monto");
			return false;
		}
		if ( window.document.forma.plazoSolicitado.value!='' ){
			if ( !esPlazoValido(window.document.forma.plazoSolicitado.value, <%=solicitud.tipoOperacion%>) ){
				alert("El plazo es inv\u00e1lido");
				return false;
			}
		}else{
			alert("Debe capturar el plazo");
			return false;
		}
		if ( window.document.forma.frecuenciaPagoSolicitada.value==0 ){
			alert('Debe seleccionar La Frecuencia de Pago');
			return false;
	    }
		if ( window.document.forma.cuota.value!='' ){
			if ( !esFormatoMoneda(window.document.forma.cuota.value) ){
				alert("El monto es inv\u00e1lido");
				return false;
			}
		}
		if( <%=solicitud.tipoOperacion%>!=<%=ClientesConstants.SELL_FINANCE%>){
			if ( window.document.forma.destinoCredito.value==0 ){
				alert('Debe seleccionar el Destino del Cr\u00e9dito');
				return false;
			}
		}else{
			if ( window.document.forma.marca.value==0 ){
				alert('Debe seleccionar la marca del producto');
				return false;
			}
			if ( window.document.forma.producto.value==0 ){
				alert('Debe seleccionar producto');
				return false;
			}
			if ( window.document.forma.desembolsado.value==<%=ClientesConstants.DESEMBOLSADO%> && window.document.forma.estatus.value==<%=ClientesConstants.SOLICITUD_AUTORIZADA%>){
				if(window.document.forma.numeroFactura.value==''){
					alert('Debe ingresar numero de folio');
					return false;
				}
			}
		}

		if(window.document.forma.estatus.value==0){
			alert("Debe seleccionar el estatus de la solicitud");
			return false;
		}if(window.document.forma.estatus.value==<%=ClientesConstants.SOLICITUD_RECHAZADA%> && window.document.forma.desembolsado.value==<%=ClientesConstants.DESEMBOLSADO%>){
			alert("El estatus de desembolso no es v\u00e1lido");
			return false;
		}
		
		window.document.forma.submit();
	}

	function asignaValor(valor, control){
		document.getElementById(control).value = valor;
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css"></head>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<CENTER>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="">
<input type="hidden" name="idCliente" value="<%=cliente.idCliente%>">
<input type="hidden" name="idSolicitud" value="<%=solicitud.idSolicitud%>">

<!-- INICIO NUEVO CODIGO -->

<!-- FIN NUEVO CODIGO -->
<table border="0" width="100%">
	<tr>
	<td align="center">
<h3>Cr&eacute;dito solicitado</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table border="0" cellpadding="0" width="100%">
	<tr>
  	<td width="50%" align="right">Monto solicitado</td>
  	<td width="50%">
	<%if ( permiteEditar || esReestructura) {%>
		<input type="text" name="montoSolicitado" size="10" maxlength="12" value="<%=HTMLHelper.formatoMonto(solicitud.montoSolicitado)%>" >
	<%}else{%>	
  		<input type="text" name="montoSolicitado" size="10" maxlength="12" value="<%=HTMLHelper.formatoMonto(score.monto)%>" readonly="readonly">
  	<%} %>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Plazo</td>
  	<td width="50%"> 
	<%if ( permiteEditar ){%>
  		<input type="text" name="plazoSolicitado" size="5" maxlength="2" value="<%=HTMLHelper.displayField(solicitud.plazoSolicitado)%>">(Periodos)
	<%}else{%>
		<input type="text" name="plazoSolicitado" size="5" maxlength="2" value="<%=HTMLHelper.displayField(score.plazo)%>" readonly="readonly">(Periodos)
	<%}%>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Frecuencia de pago</td>
  	<td width="50%">  
  	<select name="frecuenciaPagoSolicitada" size="1">
	<%if ( permiteEditar ){%>
			<%=HTMLHelper.displayCombo(catFrecuenciasPago,cliente.solicitudes[indiceSolicitud].frecuenciaPagoSolicitada)%>
	<%}else{%>
			<%=HTMLHelper.displayCombo(catFrecuenciasPago,score.periodicidad)%>
	<%}%>
	</select>
  	</td>
  	<tr>
  	<td width="50%" align="right">Amortizaci&oacute;n</td>
  	<td width="50%">  
  	<input type="text" name="cuota" size="30" value="<%=HTMLHelper.formatoMonto(cliente.solicitudes[indiceSolicitud].cuota)%>">
  	</td>
	</tr>
	<%if( solicitud.tipoOperacion==ClientesConstants.SELL_FINANCE ) {%>
	<tr>
	<td width="50%" align="right">Plan</td>
  	<td width="50%">  
  	<select name="marca" size="1">
			<%=HTMLHelper.displayCombo(catMarca,sellFinance.idMarca)%>
	</select>
  	</td>
	</tr>
	<tr>
	<td width="50%" align="right">Tipo de Convenio</td>
  	<td width="50%">  
  	<select name="producto" size="1">
			<%=HTMLHelper.displayCombo(catProducto,sellFinance.idProducto)%>
	</select>
  	</td>
	</tr>
	<%if(solicitud.decisionComite!=null){%>
	<tr>
	<td width="50%" align="right">N&uacute;mero de Folio</td>
  	<td width="50%">
  	<%
  		if(sellFinance.numeroFactura==null){
	  		sellFinance.numeroFactura = "";
  		}
  	%>
  	<input type="text" size="7" name="numeroFactura" value="<%=sellFinance.numeroFactura%>" <%if(solicitud.estatus != ClientesConstants.SOLICITUD_AUTORIZADA ){%>readonly="readonly" class="soloLectura"<%}%> maxlength="7">
  	</tr>
	<%}%>
	<%}else{%>
	<tr>
  	<td width="50%" align="right">Destino del cr&eacute;dito</td>
  	<td width="50%">
<%if ( !permiteEditar ){%>
  	<input type="text" size="30" value="<%=catDestinoCredito.get(score.destinoCredito)%>" readonly="readonly">
  	<input type="hidden" name="destinoCredito" value="<%=score.destinoCredito %>">
<%}else{ %>
	<select name="destinoCredito">
	<%=HTMLHelper.displayCombo(catDestinoCredito, solicitud.destinoCredito)%>
	</select>
<%} %>
  	</td>
	</tr>
	<tr>
	<td width="50%" align="right">N&uacute;mero de cheque / Orden de Pago</td>
  	<td width="50%">
  	<%
  		if(cliente.solicitudes[indiceSolicitud].numCheque==null){
	  		cliente.solicitudes[indiceSolicitud].numCheque = "";
  		}
  	%>
  	<input type="text" size="13" name="numeroCheque" value="<%=cliente.solicitudes[indiceSolicitud].numCheque%>" readonly="readonly" maxlength="13">
	</tr>
	<%}%>
	<tr>
    <td width="50%" align="right">Estatus desembolso</td>
    <td width="50%">
    	<select name="desembolsado" id="desembolsado">
    		<%=HTMLHelper.displayCombo(catDesembolso,solicitud.desembolsado)%>
    	</select>
    </td>
  	</tr>
  	
  	<tr>
    <td width="50%" align="right">Estatus solicitud</td>
    <td width="50%">
    	<select name="estatus" id="estatus">
    		<%=HTMLHelper.displayCombo(catEstatus,solicitud.estatus)%>
    	</select>
    </td>
  	</tr>
  	<tr>
  		<td width="50%" align="right">Causa de rechazo</td>
  		<td width="50%"><input type="text" size="30" class="soloLectura" value="<%=HTMLHelper.getDescripcion(catCausaRechazo,decisionComite.causaRechazo)%>" readonly="readonly"></td>
  	</tr>
  	<tr>
    	<td width="50%" align="right">Cliente rechaza condiciones</td>
    	<td width="50%"><input type="text" size="50" class="soloLectura" name="motivoRechazoCliente" id="motivoRechazoCliente" value ="<%=HTMLHelper.getDescripcion(catCausaRechazoCliente,decisionComite.motivoRechazoCliente)%>" readonly="readonly"/></td>
	</tr>
	<tr>
	    <td width="50%" align="right">Monto a desembolsar $</td>
    	<td width="50%"><input type="text" size="10" class="soloLectura" maxlength="12" name="montoSinComision" value="<%=HTMLHelper.formatoMonto(ClientesUtil.calculaMontoSinComision(decisionComite.montoAutorizado, decisionComite.comision,catComisiones))%>" readonly="readonly"></td>
  	</tr>
	<tr>
	   <td width="50%" align="right">Monto con comisión $</td>
	  	<td width="50%"><input type="text" size="10" class="soloLectura" maxlength="12" name="montoAutorizado" value="<%=HTMLHelper.formatoMonto(decisionComite.montoAutorizado)%>" readonly="readonly"></td>
	</tr>
	<tr>
	    <td width="50%" align="right">Plazo autorizado</td>
	    <td width="50%"><input type="text" class="soloLectura" name="plazoAutorizado" size="8" maxlength="2" value="<%=HTMLHelper.displayField(decisionComite.plazoAutorizado)%>" readonly="readonly"></td>
  	</tr>
	<tr>
	    <td width="50%" align="right">Comisi&oacute;n %</td>
		<td width="50%"><input type="text" class="soloLectura" name="comision" size="12" value="<%=CatalogoHelper.getDescripcionComision(decisionComite.comision, catComisiones)%>" readonly="readonly"></td>
	</tr>
	<tr>
	    <td width="50%" align="right">Tasa</td>
		<td width="50%"><input type="text" class="soloLectura" name="tasa" size="12" value="<%=CatalogoHelper.getDescripcionTasa(decisionComite.tasa, catTasas)%>" readonly="readonly"></td>
	</tr>
	<tr>
	    <td width="50%" align="right">Frecuencia de pagos</td>
    	<td width="50%"><input type="text" class="soloLectura" name="frecuenciaPago" id="frecuenciaPago" value="<%=HTMLHelper.getDescripcion(catFrecuenciasPago,decisionComite.frecuenciaPago)%>" readonly="readonly"></td>
	</tr>
  	<tr>
  		<td width="50%" align="right" valign="center">Comentarios</td>
  		<td width="50%">
			<textarea name="comentariosAnt" class="soloLectura" rows="4" cols="35" readonly="readonly"><%=HTMLHelper.displayField(solicitud.comentarios)%></textarea><br>
			<textarea name="comentariosNvo" rows="4" cols="35"></textarea>
  		</td>
  	</tr>
  	<%
  	if(SolicitudUtil.regenaracionValida(solicitud)){
  	%>
  	<tr>
	    <td width="50%" align="right">Regenera tabla de amortización<br></td>
    	<td width="50%"><%=HTMLHelper.displayCheck("regeneraTabla", solicitud.tipoOperacion==ClientesConstants.SELL_FINANCE)%>&nbsp;&nbsp;(Seleccione para actualizar las tablas a la fecha de desembolso)</td>
    </tr>
	<%}%>
	<tr>
		<td align="center" colspan="2">
			<br><input type="button" value="Enviar" <%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO || (esExterno && solicitud.estatus != ClientesConstants.SOLICITUD_AUTORIZADA)){%>disabled="disabled"<%}%> onClick="guardaCreditoSolicitadoConsumo()">
		</td>
	</tr>
</table> 
</td>
	</tr>
</table>
<!-- INICIO NUEVO CODIGO -->

<!-- FIN NUEVO CODIGO -->

<br>
</form>
</CENTER>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>