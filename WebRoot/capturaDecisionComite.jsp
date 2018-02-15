<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>

<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>
<%@page import="com.sicap.clientes.dao.SucursalDAO"%>
<%@page import="com.sicap.clientes.dao.CicloGrupalDAO"%>
<%@page import="com.sicap.clientes.dao.IntegranteCicloDAO"%>
<%@page import="com.sicap.clientes.helpers.InterCicloHelper"%>

<%
	Calendar cal = Calendar.getInstance();
	String titulo = "";
	String nombreFecha = "";
	String txtComplemento = " del comit&eacute;";
	int idSolicitud  = HTMLHelper.getParameterInt(request,"idSolicitud");
        int semDisp = 0;
        int estatus = 0;
	ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
	Date[] fechasInhabiles = (Date[])session.getAttribute("INHABILES");
	boolean diainhabil = FechasUtil.esDiaInhabil( cal.getTime(), fechasInhabiles);
	boolean esMesaControl = request.isUserInRole("ANALISIS_CREDITO");
	int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
	SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
	DecisionComiteVO decisionComite = solicitud.decisionComite;
        SaldoIBSVO saldoICVO = new SaldoIBSVO();  
        CicloGrupalVO ciclo = null;
	ScoringVO score = new ScoringVO();
	TreeMap catDesembolso = CatalogoHelper.getCatalogoDesembolso(request, solicitud);
	TreeMap catDecision   = CatalogoHelper.getCatalogo(ClientesConstants.CAT_DECISION_COMITE);
	TreeMap catCausaRechazo = CatalogoHelper.getCatalogo(ClientesConstants.CAT_CAUSA_RECHAZO_COMITE);
	TreeMap catCausaRechazoCliente = CatalogoHelper.getCatalogo(ClientesConstants.CAT_CAUSA_CLIENTE_RECHAZO_CREDITO);
	TreeMap catFrecuenciaPagos  = CatalogoHelper.getCatalogoFrecuenciasPago(solicitud.tipoOperacion);
	TreeMap catMotivoConidcionamiento = CatalogoHelper.getCatalogo(ClientesConstants.C_MOTIVO_CONDICIONAMIENTO);	
	TreeMap catTasas = CatalogoHelper.getCatalogoTasas(solicitud.tipoOperacion);	
	TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(solicitud.tipoOperacion);	
        TreeMap catModulos = CatalogoHelper.getCatalogoModulos();
	Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
        InterCicloHelper ICHelper = new InterCicloHelper();
        
        if(solicitud.seguro==null){
            solicitud.seguro= new SegurosVO();
        }
        
        // Calculo del costo del seguro
        SucursalVO sucursalVo = new SucursalDAO().getSucursal(cliente.idSucursal);
        double costoSeguroContratado = SeguroHelper.getCostoSeguro(solicitud.seguro.modulos, sucursalVo);
        
        IntegranteCicloVO integranteDAO = new IntegranteCicloDAO().getIntegranteCiclo(cliente.idCliente, idSolicitud);
        if(integranteDAO!=null){
            ciclo = new CicloGrupalDAO().getCiclo(integranteDAO.idGrupo,integranteDAO.idCiclo);        
        }

	if(decisionComite ==null){
		decisionComite = new DecisionComiteVO();	
		    if(solicitud!=null && solicitud.reestructura==1){
		       double montoAutorizado = SolicitudUtil.calculaMontoAutorizadoReestructuraIndividual(solicitud);
		       decisionComite.montoAutorizado = montoAutorizado; 		     
		     }		   		   
      }
	if ( solicitud.tipoOperacion==ClientesConstants.CONSUMO || solicitud.tipoOperacion==ClientesConstants.CREDIHOGAR){
		
		if ( solicitud.scoring!=null ){
			score = solicitud.scoring;
			if ( decisionComite.montoAutorizado==0 ){
				decisionComite.montoAutorizado = ClientesUtil.calculaMontoConComision(score.monto, score.comision, catComisiones);
				decisionComite.plazoAutorizado = score.plazo;
				decisionComite.comision = score.comision;
				decisionComite.tasa = score.tasa;
				Logger.debug("\n\n"+decisionComite.toString());
			}
		}
		titulo = "Autorizaci&oacute;n";
		nombreFecha = "autorizaci&oacute;n";
		txtComplemento = "";
	}else if ( solicitud.tipoOperacion==ClientesConstants.MICROCREDITO ){
		titulo = "Decisi&oacute;n comit&eacute;";
		nombreFecha = "ralizaci&oacute;n del comit&eacute;";
	}else if ( solicitud.tipoOperacion==ClientesConstants.GRUPAL || solicitud.tipoOperacion==ClientesConstants.REESTRUCTURA_GRUPAL){
		titulo = "Autorizaci&oacute;n";
		nombreFecha = "autorizaci&oacute;n";
		txtComplemento = "";
		if(solicitud.desembolsado==0)
			solicitud.desembolsado=1;
	}
	
	if ( decisionComite.plazoAutorizado==0 )
		decisionComite.plazoAutorizado=16;

	double montoMaximo = SolicitudUtil.montoMaximoSolicitud(cliente);
        double montoMaximoPosible = ClientesUtil.calculaMontoEscalera(solicitud.idCliente, solicitud.idSolicitud);
        double montoMaxicoIC = 0;
        if(solicitud.subproducto == ClientesConstants.ID_INTERCICLO){
            montoMaxicoIC = ClientesUtil.calculaMaxMontoIC(solicitud.idCliente);
            montoMaximoPosible= montoMaxicoIC;
            saldoICVO = ICHelper.validaSolicitudIC(solicitud,cliente);
            ciclo = new CicloGrupalDAO().getCiclo(saldoICVO.getIdClienteSICAP(),saldoICVO.getIdSolicitudSICAP());  
            semDisp = ICHelper.semanaDispersion(saldoICVO);
            if (semDisp == ClientesConstants.DISPERSION_SEMANA_2)
                estatus = ciclo.estatusIC;
            if (semDisp == ClientesConstants.DISPERSION_SEMANA_4)
                estatus = ciclo.estatusIC2;
        }
        
        if(decisionComite.decisionComite == 0)
        {
            decisionComite.decisionComite = 1;
        }
        if(decisionComite.frecuenciaPago == 0)
        {
            decisionComite.frecuenciaPago = 3;
        }
        if ( decisionComite.multa == 0)
        {
            decisionComite.multa =100;
        }
%>
<html>
<head>
<title><%=titulo%> de cr&eacute;dito</title>

<script type="text/javascript">

	function guardaDecisionComite(tipoper){
                document.getElementById("boton").disabled = true;
		window.document.forma.command.value='capturaDecisionComite';
		window.document.forma.idSolicitud.value='<%=idSolicitud%>';
                var montoSinComision = parseFloat (window.document.forma.montoSinComision.value);
		var montoIngresado = parseFloat (window.document.forma.montoAutorizado.value);
		var montoMaximo = parseFloat (<%=montoMaximo%>);
		if(window.document.forma.decisionComite.value=='0'){
			alert("Debe seleccionar la decisión del comité , favor de revisar.")
                        document.getElementById("boton").disabled = false;
			return false;
		}
		if(window.document.forma.fechaRealizacion.value==''){
			alert("Favor de capturar la Fecha de realización del comité.")
                        document.getElementById("boton").disabled = false;
			return false;
		}else if(!esFechaValida(window.document.forma.fechaRealizacion,false) || esPosterior(window.document.forma.fechaRealizacion.value, 'Hoy') ){
			alert("La fecha de realización del comité es inválida, favor de revisar.")
                        document.getElementById("boton").disabled = false;
			return false;
		}
		if(window.document.forma.decisionComite.value=='2'){
			if( !(window.document.forma.causaRechazo.value=='0' ^ window.document.forma.motivoRechazoCliente.value=='0') ){
				alert("Debe seleccionar una opción para [Causa del rechazo] o para [Cliente rechaza condiciones]. Sólo esta permitido usar uno de los dos campos")
                                document.getElementById("boton").disabled = false;
				return false;
		    }
		    window.document.forma.numCheque.value=0;
		    window.document.forma.desembolsado.value=0;
		}else{
			if ( window.document.forma.desembolsado.value==0 ){
				alert("Debe indicar si el crédito fue desembolsado")
                                document.getElementById("boton").disabled = false;
				return false;
			}
			if ( window.document.forma.desembolsado.value==2 && tipoper == 3){
				alert("El desembolso de este individuo debe realizarse mediante el módulo de desembolso grupal")
                                document.getElementById("boton").disabled = false;
				return false;
			}
			
			if ( window.document.forma.desembolsado.value==2 ){
			<%	if ( diainhabil ){%>
					alert("El desembolso no puede efectuarse debido a que es fecha de día inhábil")
                                        document.getElementById("boton").disabled = false;
					return false;
			<%	}%>
			}
			
			if ( window.document.forma.montoSinComision.value!=''){                                                              
                               <%if(!request.isUserInRole("ANALISIS_CREDITO")){%>                            
                                if ( !esMontoValido(window.document.forma.montoSinComision.value, <%=solicitud.tipoOperacion%>) ){
					alert("El monto es inválido");
                                        document.getElementById("boton").disabled = false;
					return false;
				} else if (window.document.forma.montoSinComision.value><%=montoMaximoPosible%>){
                                    alert("El monto a desembolsar supera el máximo posible");
                                    document.getElementById("boton").disabled = false;
                                    return false;
                                }
                            <%}%>
			}else{
				alert("Debe capturar el monto");
                                document.getElementById("boton").disabled = false;
				return false;
			}

			if ( window.document.forma.plazoAutorizado.value!='' ){
				if ( !esPlazoValido(window.document.forma.plazoAutorizado.value, <%=solicitud.tipoOperacion%>) ){
					alert("El plazo es inválido");
                                        document.getElementById("boton").disabled = false;
					return false;
				}
			}else{
				alert("Debe capturar el plazo");
                                document.getElementById("boton").disabled = false;
				return false;
			}
			<%if( solicitud.tipoOperacion!=ClientesConstants.GRUPAL  || esMesaControl ){%>
			 /*
                         if(window.document.forma.comision.value=='0'){
				alert("Debe seleccionar una comision.");
                                document.getElementById("boton").disabled = false;
				return false;
		         }*/
		    <%}else if(solicitud.tipoOperacion!=ClientesConstants.GRUPAL ){%>
		    
		    if(window.document.forma.tasa.value=='0'){
				alert("Debe seleccionar una tasa.");
                                document.getElementById("boton").disabled = false;
				return false;
		    }
		    <%}%>
		    if(window.document.forma.frecuenciaPago.value=='0'){
				alert("Debe seleccionar una frecuencia de pago.");
                                document.getElementById("boton").disabled = false;
				return false;
		    }
		    if ( window.document.forma.desembolsado.value==2 ){
		    	res = confirm('Está seguro que desea registrar el desembolso del crédito con la fecha de hoy ?');
                        document.getElementById("boton").disabled = false;
		    	if ( !res ) return res;
		    }
		}
		if(window.document.forma.decisionComite.value=='3'){
			if(window.document.forma.motivoCondicionamiento.value=='0'){
			alert("Debe seleccionar un motivo del condicionamiento.");
                        document.getElementById("boton").disabled = false;
			return false;
		    }
		}
                /*if ( montoSinComision>parseFloat(40000) ){
                    alert("El monto máximo de crédito es de $ 40,000.00");
                    document.getElementById("boton").disabled = false;
                    return false;
                }*/
                if(<%=solicitud.subproducto%>==<%=ClientesConstants.ID_INTERCICLO%>&&montoSinComision > <%=montoMaxicoIC%>){
                    alert("El monto para Inter-Ciclo es inválido");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
		if ( montoIngresado > montoMaximo && montoMaximo!=0 ){
			if ( confirm('Está seguro de autorizar esta cantidad que supera el 30% extra del crédito anterior?, Cantidad máxima: $'+ montoMaximo ) )
                                    document.body.style.cursor = "wait";
                                    window.document.forma.submit();
		}else{
			document.body.style.cursor = "wait";
                        window.document.forma.submit();	
		}
	}


function comiteAprueba(){
	if(window.document.forma.decisionComite.value=='2'){
 		window.document.forma.detalleMotivoRechazoCliente.disabled=false;
		window.document.forma.causaRechazo.disabled=false;
		window.document.forma.motivoRechazoCliente.disabled=false;	
	}
	else{
		window.document.forma.detalleMotivoRechazoCliente.disabled=true;
		window.document.forma.causaRechazo.disabled=true;
		window.document.forma.motivoRechazoCliente.disabled=true;
	}
	if(window.document.forma.decisionComite.value=='3'){
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
<table border="0" cellpadding="0" width="100%">
  <tr>
    <td width="50%" align="right">Fecha de <%=nombreFecha%></td>
    <td colspan="2">
        <input type="text" name="fechaRealizacion" id="fechaRealizacion" size="10" maxlength="10" id="fechaRealizacion" value="<%=HTMLHelper.displayField(decisionComite.fechaRealizacion, new java.util.Date())%>"/>(dd/mm/aaaa)
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Fecha de captura de resultados<%=txtComplemento%></td>
    <td colspan="3">
    	<input type="text" name="fechaCaptura" size="10" maxlength="10" id="fechaCaptura"  value="<%=HTMLHelper.displayField(decisionComite.fechaCaptura, new java.util.Date())%>" readOnly="readOnly"/ class="soloLectura">(dd/mm/aaaa)
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Número de cheque / Orden de Pago</td>
    <td colspan="3">
    	<input type="text" name="numCheque" size="13" maxlength="13" value="<%=HTMLHelper.displayField(solicitud.numCheque)%>" readOnly="readOnly" class="soloLectura">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Estatus desembolso</td>
    <td colspan="3">
    	<select name="desembolsado">
    		<%=HTMLHelper.displayCombo(catDesembolso,solicitud.desembolsado)%>
    	</select>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Fecha de desembolso</td>
    <td colspan="3">
    	<input type="text" name="fechaDesembolso" size="10" maxlength="10" value="<%=HTMLHelper.displayField(solicitud.fechaDesembolso)%>" readOnly="readOnly"/ class="soloLectura">(dd/mm/aaaa)
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Decisi&oacute;n<%=txtComplemento%></td>
    
    <td colspan="3" ><select name="decisionComite" id="decisionComite" onClick="comiteAprueba();"><%=HTMLHelper.displayCombo(catDecision,decisionComite.decisionComite)%></select></td>
  </tr>
  <tr>
    <td width="50%" align="right">Causa del rechazo</td>
    <td colspan="3"><select name="causaRechazo" id="causaRechazo"><%=HTMLHelper.displayCombo(catCausaRechazo,decisionComite.causaRechazo)%> </select></td>
  </tr>
  <tr>
    <td width="50%" align="right">Cliente rechaza condiciones</td>
    <td><select  name="motivoRechazoCliente"  id="motivoRechazoCliente"> <%=HTMLHelper.displayCombo(catCausaRechazoCliente,decisionComite.motivoRechazoCliente)%></select></td>
  </tr>
  <tr>
    <td width="50%" align="right">Motivo del rechazo</td>
    <td>
    	<input type="text" name="detalleMotivoRechazoCliente" size="20" maxlength="25" value="<%=HTMLHelper.displayField(decisionComite.detalleMotivoRechazoCliente)%>">
    </td>
  </tr>
	<tr>
		<td width="50%" align="right">Monto a desembolsar $</td>
		<td colspan="3"><input type="text" size="10" maxlength="12" name="montoSinComision" value="<%=HTMLHelper.formatoMonto(ClientesUtil.calculaMontoSinComision(decisionComite.montoAutorizado, decisionComite.comision,catComisiones))%>"></td>
	</tr>
	<tr>
		<td width="50%" align="right">Monto con Seguro Financiado $</td>
		<td colspan="3"><input type="text" size="10" maxlength="12" name="montoAutorizado" id="montoAutorizado" value="<%=HTMLHelper.formatoMonto(decisionComite.montoConSeguro)%>" readonly="readonly" class="soloLectura"></td>
	</tr>
        <% if (decisionComite.montoConSeguro > 0) {%>
        <tr>
		<td width="50%" align="right">Seguro Financiado</td>
		<td colspan="3"><%=HTMLHelper.getDescripcion(catModulos, solicitud.seguro.modulos)%></td>
	</tr>
	
	<tr>
		<td width="50%" align="right">Monto de Seguro $</td>
		<td colspan="3"><%=HTMLHelper.formatoMonto(costoSeguroContratado)%></td>
	</tr>
        <%}%>        
	<%if(decisionComite.montoRefinanciado != 0){%>
		<tr>
			<td width="50%" align="right">Monto Refinanciado $</td>
			<td colspan="3"><input type="text" size="10" maxlength="12" name="montoRefinanciado" id="montoRefinanciado" value="<%=HTMLHelper.formatoMonto(decisionComite.montoRefinanciado)%>" disabled="disabled"></td>
		</tr>
		<tr>
			<td width="50%" align="right">Monto Total $</td>
			<td colspan="3"><input type="text" size="10" maxlength="12" name="montoTotal" id="montoTotal" value="<%=HTMLHelper.formatoMonto(decisionComite.montoAutorizado+decisionComite.montoRefinanciado)%>" disabled="disabled"></td>
		</tr>
	<%}%>
  <tr>
    <td width="50%" align="right">Plazo autorizado</td>
    <td colspan="3"><input type="text" name="plazoAutorizado" size="5" maxlength="2" value="<%=HTMLHelper.displayField(decisionComite.plazoAutorizado)%>">
<% 	if ( solicitud.tipoOperacion==ClientesConstants.GRUPAL || solicitud.tipoOperacion==ClientesConstants.REESTRUCTURA_GRUPAL){%>
		(Semanas)
<%} else{%>
  		(Meses)
<%} %>
 </tr>
<%if ( solicitud.reestructura!=0 && request.isUserInRole("DESEMBOLSO_REESTRUCTURA") ){ %>
  <tr>
    <td width="50%" align="right">Fecha valor</td>
    <td colspan="3" width="50%">
        <input type="text" name="fechaValor" size="10" maxlength="10" id="fechaValor" value="<%=HTMLHelper.displayField(decisionComite.fechaValor)%>" />(dd/mm/aaaa)
    </td>
  </tr>
<%} %>
 <%if( solicitud.tipoOperacion==ClientesConstants.GRUPAL ){%>
<%-- Se quita como parte del requerimiento Seguro Financiado
 	<%if(esMesaControl){%>
 		<tr>
		   	<td width="50%" align="right">Comisi&oacute;n</td>
			<td colspan="3"><select name="comision" ><%=HTMLHelper.displayComboComisiones(catComisiones,decisionComite.comision)%></select></td>
		</tr>
	 <%}else{%>
	 	<tr>
		    <td width="50%" align="right">Comisi&oacute;n</td>
		    <td colspan="3"><input type="hidden" name="comision" value="<%=decisionComite.comision%>">
		    <%=CatalogoHelper.getDescripcionComision(decisionComite.comision, catComisiones)%>
	  	</tr>
	 <%}%>
--%>
  <tr>
    <td width="50%" align="right">Tasa</td>
    <td colspan="3"><input type="hidden" name="tasa" value="<%=decisionComite.tasa%>">
    <%=CatalogoHelper.getDescripcionTasa(decisionComite.tasa, catTasas)%>
  </tr>
<%}else{%>
        <%-- Se quita como parte del requerimiento Seguro Financiado
	<tr>
	   <td width="50%" align="right">Comisi&oacute;n %</td>
		<td colspan="3"><select name="comision" ><%=HTMLHelper.displayComboComisiones(catComisiones,decisionComite.comision)%></select></td>
	</tr>
        --%>
	<tr>
	   <td width="50%" align="right">Tasa</td>
		<td colspan="3"><select name="tasa" ><%=HTMLHelper.displayComboTasas(catTasas,decisionComite.tasa)%></select></td>
	</tr>
<%}%>
  <tr>
    <tr>
    <td width="50%" align="right">Frecuencia de pagos</td>
    
    <td colspan="3"><select name="frecuenciaPago" id="frecuenciaPago" ><%=HTMLHelper.displayCombo(catFrecuenciaPagos,decisionComite.frecuenciaPago)%></select></td>
  </tr>
	<tr>
		<td width="50%" align="right">Multa</td>
		<td colspan="3"><input type="text" size="10" maxlength="7" name="multa" value="<%=HTMLHelper.formatoMonto(decisionComite.multa)%>"></td>
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
  	<td colspan="4" align="center">
  	<%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO || solicitud.enGarantia == 1){
            if(request.isUserInRole("ANALISIS_CREDITO") && solicitud.tipoOperacion != ClientesConstants.MICROCREDITO){%>
                <br><input type="button" id="boton" name="" value="Enviar" onClick="guardaDecisionComite(<%=solicitud.tipoOperacion%>)" >
            <%}else{%>
                <br><input disabled type="button" id="boton" name="" value="Enviar" onClick="guardaDecisionComite()" >
            <%}%>
        <%}else if (solicitud.subproducto == ClientesConstants.ID_INTERCICLO && !request.isUserInRole("ANALISIS_CREDITO")){
                 if(estatus==ClientesConstants.CICLO_APERTURA||estatus==ClientesConstants.CICLO_RECHAZADO){%>
                 <br><input type="button" id="boton" name="" value="Enviar" onClick="guardaDecisionComite(<%=solicitud.tipoOperacion%>)" >
                 <%}%>
  	<%}else if ((integranteDAO == null || request.isUserInRole("ANALISIS_CREDITO")) && solicitud.desembolsado!=ClientesConstants.CANCELADO){ %>
		<br><input type="button" id="boton" name="" value="Enviar" onClick="guardaDecisionComite(<%=solicitud.tipoOperacion%>)" >
  	<%}else if (ciclo!=null){
                if(ciclo.estatus==ClientesConstants.CICLO_APERTURA||estatus==ClientesConstants.CICLO_RECHAZADO){%>
                <br><input type="button" id="boton" name="" value="Enviar" onClick="guardaDecisionComite(<%=solicitud.tipoOperacion%>)" >
                <%}%>
        <%}%>
  	</td>
 </tr>
</table>
		</td>
	</tr>
</table>
</form>
</CENTER>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>
