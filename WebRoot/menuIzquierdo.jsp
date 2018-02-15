<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.SolicitudVO"%>
<%@ page import="com.sicap.clientes.vo.UsuarioVO"%>
<%@ page import="com.sicap.clientes.util.SolicitudUtil"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<%

int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
int indiceSolicitud = -1;
SolicitudVO solicitud = null;
boolean autorizadorConsumo = false;
UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");

if ( idSolicitud==0 ){
	if ( request.getAttribute("ID_SOLICITUD")!=null )
		idSolicitud = ((Integer)request.getAttribute("ID_SOLICITUD")).intValue();
}
indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
if ( indiceSolicitud>=0 ){
	solicitud = cliente.solicitudes[indiceSolicitud];
}else{

}
int tipoOperacion = solicitud.tipoOperacion;

if(tipoOperacion == ClientesConstants.GRUPAL || tipoOperacion == ClientesConstants.REESTRUCTURA_GRUPAL)
	session.setAttribute("idSolicitud",idSolicitud);
	
if ( request.isUserInRole("AUTORIZADOR_CONSUMO") )
	autorizadorConsumo=true;

%>

<script type="text/javascript">

    function capturaExpedienteOperativoLegal(){
    
	    window.document.forma.command.value='capturaExpedienteOperativoLegal';
		window.document.forma.submit();
	}
	
	function capturaExpedienteComunal(){
    
	    window.document.forma.command.value='capturaExpedienteComunal';
		window.document.forma.submit();
	}
	
	function capturaExpedienteDescuentoNomina(){
    
	    window.document.forma.command.value='capturaExpedienteDescuentoNomina';
		window.document.forma.submit();
	}
    
	function consultaSolicitudes(){
		window.document.forma.command.value='consultaSolicitudes';
                window.document.forma.target="_top";
		window.document.forma.submit();
	}

	function datosGenerales(){
<%	if ( solicitud.tipoOperacion==ClientesConstants.CONSUMO || solicitud.tipoOperacion==ClientesConstants.CREDIHOGAR){%>
		window.document.forma.command.value='datosGeneralesClienteConsumo';
<%	}else if ( solicitud.tipoOperacion==ClientesConstants.VIVIENDA ){%>
		window.document.forma.command.value='datosGeneralesClienteVivienda';
<%	}else if ( solicitud.tipoOperacion==ClientesConstants.SELL_FINANCE ){%>
		window.document.forma.command.value='datosGeneralesClienteDescuento';
<%	}else{%>
		window.document.forma.command.value='datosGeneralesCliente';
<%	}%>
		window.document.forma.submit();
	}

	function datosObligadosSolidarios(){
		window.document.forma.command.value='consultaObligadosSolidarios';
		if ( window.document.forma.idSolicitud.value==0 ){
			alert('Es necesario guardar la información previamente');
			return false;
		}
		window.document.forma.submit();
	}

	function datosCalificacionCredito(){
		window.document.forma.command.value='capturaDatosCredito';
		if ( window.document.forma.idSolicitud.value==0 ){
			alert('Es necesario guardar la información previamente');
			return false;
		}
		window.document.forma.submit();
	}

	function capturaAbreviadaDatosCliente(){
		window.document.forma.command.value='capturaAbreviadaDatosCliente';
		if ( window.document.forma.idSolicitud.value==0 ){
			alert('Es necesario guardar la información previamente');
			return false;
		}
		window.document.forma.submit();
	}

	function capturaPropuestaComiteCredito(){
		window.document.forma.command.value='capturaPropuestaComiteCredito';
		window.document.forma.submit();
	}

	function capturaCreditoSolicitado(){
		window.document.forma.command.value='capturaCreditoSolicitadoConsumo';
		window.document.forma.submit();
	}
	function capturaCreditoSolicitadoDescuento(){
		window.document.forma.command.value='capturaCreditoSolicitadoDescuento';
		window.document.forma.submit();
	}

	function capturaDecisionComiteCredito(){
		<%if ( solicitud.tipoOperacion==ClientesConstants.CONSUMO || solicitud.tipoOperacion==ClientesConstants.VIVIENDA || solicitud.tipoOperacion==ClientesConstants.CREDIHOGAR){%>
			window.document.forma.command.value='autorizacionCreditoConsumo';
		<%}else if(solicitud.tipoOperacion==ClientesConstants.SELL_FINANCE){%>
			window.document.forma.command.value='autorizacionSellFinance';
		<%}else{%>
			window.document.forma.command.value='decisionComiteCredito';
		<%}%>
		if ( window.document.forma.idSolicitud.value==0 ){
			alert('Es necesario guardar la información previamente');
			return false;
		}
		window.document.forma.submit();
	}

	function capturaArrendatarioDomicilio(){
<%	if ( solicitud.tipoOperacion==ClientesConstants.CONSUMO || solicitud.tipoOperacion==ClientesConstants.VIVIENDA || solicitud.tipoOperacion==ClientesConstants.CREDIHOGAR || solicitud.tipoOperacion==ClientesConstants.SELL_FINANCE || solicitud.tipoOperacion==ClientesConstants.MAX_ZAPATOS){%>
		window.document.forma.command.value='capturaArrendatarioDomicilioConsumo';
<%	}else{%>
		window.document.forma.command.value='capturaArrendatarioDomicilio';
<%	}%>
		if ( window.document.forma.idSolicitud.value==0 ){
			alert('Es necesario guardar la información previamente');
			return false;
		}
		window.document.forma.submit();
	}

	function capturaArrendatarioLocal(){
		window.document.forma.command.value='capturaArrendatarioLocal';
		if ( window.document.forma.idSolicitud.value==0 ){
			alert('Es necesario guardar la información previamente');
			return false;
		}
		window.document.forma.submit();
	}

	function capturaConyuge(){
	<%	if ( solicitud.tipoOperacion==ClientesConstants.VIVIENDA ){%>
			window.document.forma.command.value='capturaConyugeVivienda';
	<%	}else{%>
			window.document.forma.command.value='capturaConyuge';
	<%}%>
	window.document.forma.submit();
	}

	function capturaNegocioCliente(){
		window.document.forma.command.value='capturaNegocioCliente';
		if ( window.document.forma.idSolicitud.value==0 ){
			alert('Es necesario guardar la información previamente');
			return false;
		}
		window.document.forma.submit();
	}

	function capturaEconomiaObligados(){
		window.document.forma.command.value='capturaEconomiaObligados';
		if ( window.document.forma.idSolicitud.value==0 ){
			alert('Es necesario guardar la información previamente');
			return false;
		}
		window.document.forma.submit();
	}

	function capturaInformacionFinanciera(){
		window.document.forma.command.value='capturaInformacionFinanciera';
		if ( window.document.forma.idSolicitud.value==0 ){
			alert('Es necesario guardar la información previamente');
			return false;
		}
		window.document.forma.submit();
	}

	function referenciasComerciales(){
		window.document.forma.command.value='consultaReferenciasComerciales';
		if ( window.document.forma.idSolicitud.value==0 ){
			alert('Es necesario guardar la información previamente');
			return false;
		}
		window.document.forma.submit();
	}

	function referenciasPersonales(){
		window.document.forma.command.value='consultaReferenciasPersonales';
		if ( window.document.forma.idSolicitud.value==0 ){
			alert('Es necesario guardar la información previamente');
			return false;
		}
		window.document.forma.submit();
	}

	function archivosAsociados(){
		window.document.forma.command.value='consultaArchivosAsociados';
		if ( window.document.forma.idSolicitud.value==0 ){
			alert('Es necesario guardar la información previamente');
			return false;
		}
		window.document.forma.submit();
	}

	function capturaEmpleoCliente(){
		<%	if ( solicitud.tipoOperacion==ClientesConstants.VIVIENDA ){%>
				window.document.forma.command.value='capturaEmpleoClienteVivienda';
		<%	}else if ( solicitud.tipoOperacion==ClientesConstants.SELL_FINANCE ){%>
				window.document.forma.command.value='capturaEmpleoClienteDescuento';
		<%	}else if ( solicitud.tipoOperacion==ClientesConstants.GRUPAL ){%>
				window.document.forma.command.value='capturaEmpleoClienteGrupal';
		<%	}else{%>
				window.document.forma.command.value='capturaEmpleoCliente';
		<%}%>
		if ( window.document.forma.idSolicitud.value==0 ){
			alert('Es necesario guardar la información previamente');
			return false;
		}
		window.document.forma.submit();
	}

	function capturaViviendaCliente(){
		window.document.forma.command.value='capturaViviendaCliente';
		if ( window.document.forma.idSolicitud.value==0 ){
			alert('Es necesario guardar la información previamente');
			return false;
		}
		window.document.forma.submit();
	}

	function capturaCapacidadPago(){
		window.document.forma.command.value='capturaCapacidadPago';
		if ( window.document.forma.idSolicitud.value==0 ){
			alert('Es necesario guardar la información previamente');
			return false;
		}
		window.document.forma.submit();
	}

	function capturaReferenciaLaboral(){
		window.document.forma.command.value='capturaReferenciaLaboral';
		if ( window.document.forma.idSolicitud.value==0 ){
			alert('Es necesario guardar la información previamente');
			return false;
		}
		window.document.forma.submit();
	}

	function scoreConsumo(){
		window.document.forma.command.value='scoreConsumo';
		window.document.forma.submit();
	}
	
	function calculaCapacidad(){
		window.document.forma.command.value='calculaCapacidad';
		window.document.forma.submit();
	}
	
	function capturaSeguro(){
		window.document.forma.command.value='capturaSeguro';
		if ( window.document.forma.idSolicitud.value==0 ){
			alert('Es necesario guardar la información previamente');
			return false;
		}
		window.document.forma.submit();
	}
	
	function capturaReferenciasCrediticias(){
		window.document.forma.command.value='capturaReferenciasCrediticias';
		if ( window.document.forma.idSolicitud.value==0 ){
			alert('Es necesario guardar la información previamente');
			return false;
		}
		window.document.forma.submit();
	}

	function capturaDatosCredito(){
		window.document.forma.command.value='capturaDatosCreditoVivienda';
		if ( window.document.forma.idSolicitud.value==0 ){
			alert('Es necesario guardar la información previamente');
			return false;
		}
		window.document.forma.submit();
	}
	
	function estadoCuenta(){
		window.document.forma.command.value='consultaEstadoCuenta';
		window.document.forma.submit();
	}	

	function capturaLimitesCredito(){
		window.document.forma.command.value='capturaLimitesCredito';
		window.document.forma.submit();
	}
	
	function capturaOtrosDomicilios(){
		window.document.forma.command.value='otrosDomicilios';
		window.document.forma.submit();
	}

	function capturaAutorizacionMaxZapatos(){

		window.document.forma.command.value='autorizacionMaxZapatos';
		if ( window.document.forma.idSolicitud.value==0 ){
			alert('Es necesario guardar la información previamente');
			return false;
		}
		window.document.forma.submit();
	}

	function consultaDisposiciones(){

		window.document.forma.command.value='consultaDisposiciones';
		if ( window.document.forma.idSolicitud.value==0 ){
			alert('Es necesario guardar la información previamente');
			return false;
		}
		window.document.forma.submit();
	}
        
        function consultaODP(){
		window.document.forma.command.value='consultaODP';
		if ( window.document.forma.idSolicitud.value==0 ){
			alert('Es necesario guardar la información previamente');
			return false;
		}
		window.document.forma.submit();
	}
        
        function goCicloGrupal(){
            <%if(solicitud.desembolsado == ClientesConstants.DESEMBOLSADO){%>
                window.document.formaOculta.command.value='consultaCicloGrupal';
            <%}else{%>
                window.document.formaOculta.command.value='consultaCicloApertura';
            <%}%>
            window.document.formaOculta.target="_top";
            window.document.formaOculta.submit();
        }
</script>

<STYLE type="text/css">
  	.mennu {
  	FONT-WEIGHT: bold; 
  	FONT-SIZE: 12px; 
  	COLOR: #006633; 
  	FONT-FAMILY: Geneva, Arial, Helvetica, sans-serif; 
  	TEXT-DECORATION: none; 
  	}
  	.mennu a:hover {
	FONT-WEIGHT: bold; 
	FONT-SIZE: 12px; 
	COLOR: #FFA125; 
	FONT-FAMILY: Geneva, Arial, Helvetica, sans-serif; 
	TEXT-DECORATION: underline
	}
	.mennu .maintitle {
	FONT-WEIGHT: bold; 
	FONT-SIZE: 16px; 
	COLOR: #006633; 
	FONT-FAMILY: Geneva, Arial, Helvetica, sans-serif; 
	TEXT-DECORATION: none
	}
	.mennu TABLE {
	FONT-SIZE: 12px;
	}
	.mennu tabla {
	BORDER-RIGHT: #ff0000 1px solid; 
	BORDER-TOP: #ff0000 1px solid; 
	FONT-SIZE: 12px; 
	BORDER-LEFT: #ff0000 1px solid; 
	BORDER-BOTTOM: #ff0000 1px solid
	}
	.mennu .ligaRojo {
	FONT-WEIGHT: bold; 
	FONT-SIZE: 12px; 
	COLOR: red; 
	FONT-FAMILY: Geneva, Arial, Helvetica, sans-serif; 
	TEXT-DECORATION: none
	}
	.mennu .campo {
	Background-color: white; 
	bgColor: white; 
	align: center;
	text-align: center;
	}
 </STYLE>
 <form name="formaOculta" action="controller" method="post">
     <input type="hidden" name="command" value="">
     <input type="hidden" name="idCiclo" value="<%=cliente.solicitudes[indiceSolicitud].idCiclo%>">
     <input type="hidden" name="idGrupo" value="<%=solicitud.idGrupo%>">
 </form>
<div class="mennu">
 <TABLE cellSpacing=0 cellPadding=0>
  <TR>
    <TD bgColor="#009861">
      <TABLE cellSpacing=1 cellPadding=5 width=0 border=0>
      <TR>
        <TD class=campo ><a href="<%=request.getContextPath()%>" target="_top"> Inicio</a></TD>
        <TD class=campo ><a href="#" onClick="consultaSolicitudes()"> Solicitudes</a></TD>
        <%if ( usuario.identificador.equals("I") ){%>
        	<TD class=campo ><a href="#" onClick="datosGenerales()"> Datos generales</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaOtrosDomicilios()"> Otros domicilios</a></TD>
                <TD class=campo ><a href="#" onClick="capturaEmpleoCliente()"> Actividad Economica</a></TD>
        <%}%>
        <!-- Para Consumo -->
   		<%if ( tipoOperacion==ClientesConstants.CONSUMO){%>
        	<TD class=campo ><a href="#" onClick="capturaAbreviadaDatosCliente()"> Captura abreviada</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaEmpleoCliente()"> Empleo</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaReferenciasCrediticias()"> Referencias crediticias</a></TD>
        	<TD class=campo ><a href="#" onClick="datosCalificacionCredito()"> Calificaci&oacute;n crediticia</a></TD>
        	<TD class=campo ><a href="#" onClick="referenciasPersonales()"> Referencias personales</a></TD>
        	
        </TR>
        <TR>
        	<TD class=campo ><a href="#" onClick="capturaArrendatarioDomicilio()"> Arrendatario domicilio</a></TD>
        	<TD class=campo ><a href="#" onClick="datosObligadosSolidarios()"> Obligados solidarios</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaCreditoSolicitado()"> Cr&eacute;dito solicitado</a></TD>
        	<TD class=campo ><a href="#" onClick="scoreConsumo()"> Score</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaSeguro()"> Seguro de vida</a></TD>
        	<TD class=campo ><a href="#" onClick="archivosAsociados()"> Archivos asociados</a></TD>
        	<%if(solicitud.estatus == ClientesConstants.SOLICITUD_AUTORIZADA){ %>
        	<TD class=campo ><a href="#" onClick="estadoCuenta()"> Estado de cuenta</a></TD>
        	<%}else{%>
        	<TD class=campo >Estado de cuenta</TD>
        	<%} %>
        	<%if (autorizadorConsumo==true){%>
        		<TD class=campo ><a href="#" onClick="capturaDecisionComiteCredito()"> Autorizaci&oacute;n de cr&eacute;dito</a></TD>
        		<TD class=campo >&nbsp;</TD>
        	<%}else{%>
        		<TD class=campo >&nbsp;</TD>
        		<TD class=campo >&nbsp;</TD>
        	<%}%>
        <%}%>
        <!-- Para Credihogar -->
        <%if (tipoOperacion==ClientesConstants.CREDIHOGAR){%>
        	<TD class=campo ><a href="#" onClick="capturaAbreviadaDatosCliente()"> Captura abreviada</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaEmpleoCliente()"> Empleo</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaReferenciasCrediticias()"> Referencias crediticias</a></TD>
        	<TD class=campo ><a href="#" onClick="datosCalificacionCredito()"> Calificaci&oacute;n crediticia</a></TD>
        	<TD class=campo ><a href="#" onClick="referenciasPersonales()"> Referencias personales</a></TD> 
   		</TR>
   		<TR>
        	<TD class=campo ><a href="#" onClick="capturaArrendatarioDomicilio()"> Arrendatario domicilio</a></TD>
        	<TD class=campo ><a href="#" onClick="datosObligadosSolidarios()"> Obligados solidarios</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaCreditoSolicitado()"> Cr&eacute;dito solicitado</a></TD>
        	<TD class=campo ><a href="#" onClick="scoreConsumo()"> Score</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaSeguro()"> Seguro de vida</a></TD>
        	<TD class=campo ><a href="#" onClick="archivosAsociados()"> Archivos asociados</a></TD>
        	<%if(solicitud.estatus == ClientesConstants.SOLICITUD_AUTORIZADA){ %>
        	<TD class=campo ><a href="#" onClick="estadoCuenta()"> Estado de cuenta</a></TD>
        	<%}else{%>
        	<TD class=campo >Estado de cuenta</TD>
        	<%}%>
        	<%if ( autorizadorConsumo==true ){%>
        		<TD class=campo ><a href="#" onClick="capturaDecisionComiteCredito()"> Autorizaci&oacute;n de cr&eacute;dito</a></TD>
        		<TD class=campo >&nbsp;</TD>
        	<%}else{%>
        		<TD class=campo >&nbsp;</TD>
        		<TD class=campo >&nbsp;</TD>
        	<%}%>
        <%}%>
        <!-- Para Vivienda -->
        <%if ( tipoOperacion==ClientesConstants.VIVIENDA ){%>
        	<TD class=campo ><a href="#" onClick="capturaConyuge()"> C&oacute;nyuge</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaDatosCredito()"> Datos del crédito</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaAbreviadaDatosCliente()"> Captura abreviada</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaEmpleoCliente()"> Empleo</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaReferenciasCrediticias()"> Referencias crediticias</a></TD>
        	<TD class=campo ><a href="#" onClick="datosCalificacionCredito()"> Calificaci&oacute;n crediticia</a></TD>
        </TR>
  		<TR>
        	<TD class=campo ><a href="#" onClick="referenciasPersonales()"> Referencias personales</a></TD> 
        	<TD class=campo ><a href="#" onClick="capturaArrendatarioDomicilio()"> Arrendatario domicilio</a></TD>
        	<TD class=campo ><a href="#" onClick="datosObligadosSolidarios()"> Obligados solidarios</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaCreditoSolicitado()"> Cr&eacute;dito solicitado</a></TD>
        	<TD class=campo ><a href="#" onClick="scoreConsumo()"> Score</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaSeguro()"> Seguro de vida</a></TD>
        	<TD class=campo ><a href="#" onClick="archivosAsociados()"> Archivos asociados</a></TD>
        	<%if(solicitud.estatus == ClientesConstants.SOLICITUD_AUTORIZADA){ %>
        	<TD class=campo ><a href="#" onClick="estadoCuenta()"> Estado de cuenta</a></TD>
        	<%}else{%>
        	<TD class=campo >Estado de cuenta</TD>
        	<%}%>
        	<%if ( autorizadorConsumo==true ){%>
        		<TD class=campo ><a href="#" onClick="capturaDecisionComiteCredito()"> Autorizaci&oacute;n de cr&eacute;dito</a></TD>
        		<TD class=campo >&nbsp;</TD>
        	<%}else{%>
        		<TD class=campo >&nbsp;</TD>
        		<TD class=campo >&nbsp;</TD>
        	<%}%>
        <%}%>
        <!-- Para Microcredito -->
		<%if ( tipoOperacion==ClientesConstants.MICROCREDITO ){%>
			<TD class=campo ><a href="#" onClick="capturaAbreviadaDatosCliente()"> Captura abreviada</a></TD> 
        	<TD class=campo ><a href="#" onClick="datosObligadosSolidarios()"> Obligados solidarios</a></TD>
        	<TD class=campo ><a href="#" onClick="datosCalificacionCredito()"> Calificaci&oacute;n crediticia</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaConyuge()"> C&oacute;nyuge</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaNegocioCliente()"> Negocio cliente</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaEconomiaObligados()"> Datos econ&oacute;micos obligados solidarios</a></TD>
       	</TR>
       	<TR>
        	<TD class=campo ><a href="#" onClick="capturaInformacionFinanciera()"> Informaci&oacute;n financiera</a></TD>
        	<TD class=campo ><a href="#" onClick="referenciasPersonales()"> Referencias personales</a></TD>
        	<TD class=campo ><a href="#" onClick="referenciasComerciales()"> Referencias comerciales</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaArrendatarioLocal()"> Arrendatario local</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaArrendatarioDomicilio()"> Arrendatario domicilio</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaPropuestaComiteCredito()"> Propuesta comit&eacute; de cr&eacute;dito</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaExpedienteOperativoLegal()"> Expediente Operativo y Legal</a></TD>
        	<TD class=campo ><a href="#" onClick="archivosAsociados()"> Archivos asociados</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaSeguro()"> Seguro de vida</a></TD>
        	<%if(solicitud.estatus == ClientesConstants.SOLICITUD_AUTORIZADA){ %>
        	<TD class=campo ><a href="#" onClick="estadoCuenta()"> Estado de cuenta</a></TD>
        	<%}else{%>
        	<TD class=campo >Estado de cuenta</TD>
        	<%} %>
       	</TR>
       	<TR>
        	<TD class=campo ><a href="#" onClick="capturaDecisionComiteCredito()"> Decisi&oacute;n comit&eacute; de cr&eacute;dito</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaLimitesCredito()"> Limites de crédito</a></TD>
        	<TD class=campo >&nbsp;</TD>
        	<TD class=campo >&nbsp;</TD>
        	<TD class=campo >&nbsp;</TD>
        	<TD class=campo >&nbsp;</TD>
        	<TD class=campo >&nbsp;</TD>
        	<TD class=campo >&nbsp;</TD>
        	<TD class=campo >&nbsp;</TD>
        	<TD class=campo >&nbsp;</TD>
        <%}%>
        <!-- Para Grupal -->
		<%if ( tipoOperacion==ClientesConstants.GRUPAL ){%>
        	<TD class=campo ><a href="#" onClick="datosCalificacionCredito()"> Calificaci&oacute;n crediticia</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaSeguro()"> Seguro de vida</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaExpedienteComunal()"> Check List Expediente Comunal</a></TD>
        	<TD class=campo ><a href="#" onClick="archivosAsociados()"> Archivos asociados</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaDecisionComiteCredito()"> Autorizaci&oacute;n de cr&eacute;dito</a></TD>
                <TD class=campo ><a href="#" onClick="consultaODP()"> &Oacute;rdenes de pago</a></TD>
                <%if(solicitud.idCiclo != 0){%>
                    <TD class=campo ><a href="#" onClick="goCicloGrupal()"> Ir a Ciclo</a></TD>
                <%}%>
        <%}%>
        <!-- Para Restructura Grupal -->
		<%if ( tipoOperacion==ClientesConstants.REESTRUCTURA_GRUPAL ){%>
        	<TD class=campo ><a href="#" onClick="datosCalificacionCredito()"> Calificaci&oacute;n crediticia</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaExpedienteComunal()"> Check List Expediente Comunal</a></TD>
        	<TD class=campo ><a href="#" onClick="archivosAsociados()"> Archivos asociados</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaDecisionComiteCredito()"> Autorizaci&oacute;n de cr&eacute;dito</a></TD>
        <%}%>
        <!-- Para Sell Finance -->
   		<%if ( tipoOperacion==ClientesConstants.SELL_FINANCE){%>
   			<%if ( usuario.identificador.equals("I") ){%>
	        	<TD class=campo ><a href="#" onClick="capturaEmpleoCliente()"> Empleo</a></TD>
	        	<TD class=campo ><a href="#" onClick="calculaCapacidad()"> Calculo Capacidad</a></TD>
	        	<TD class=campo ><a href="#" onClick="referenciasPersonales()"> Referencias personales</a></TD>
        		<TD class=campo ><a href="#" onClick="capturaExpedienteDescuentoNomina()"> Check List Expediente</a></TD>
	        </TR>
	        <TR>
	        	<TD class=campo ><a href="#" onClick="capturaCreditoSolicitadoDescuento()"> Cr&eacute;dito solicitado</a></TD>
	        	<TD class=campo ><a href="#" onClick="archivosAsociados()"> Archivos asociados</a></TD>
	        	<%if(solicitud.estatus == ClientesConstants.SOLICITUD_AUTORIZADA){ %>
	        	<TD class=campo ><a href="#" onClick="estadoCuenta()"> Estado de cuenta</a></TD>
	        	<%}else{%>
				<TD class=campo >Estado de cuenta</TD>
	        	<%}%>
        		<%if (autorizadorConsumo==true){%>
        			<TD class=campo ><a href="#" onClick="capturaDecisionComiteCredito()"> Autorizaci&oacute;n de cr&eacute;dito</a></TD>
        			<TD class=campo >&nbsp;</TD>
        		<%}else{%>
        			<TD class=campo >&nbsp;</TD>
        			<TD class=campo >&nbsp;</TD>
        		<%}%>
        	<%}else{%>
	        	<TD class=campo ><a href="#" onClick="capturaAbreviadaDatosCliente()"> Captura abreviada</a></TD>
	        	<TD class=campo ><a href="#" onClick="capturaCreditoSolicitado()"> Cr&eacute;dito solicitado</a></TD>
	        	<TD class=campo ><a href="#" onClick="archivosAsociados()"> Archivos asociados</a></TD>
        	<%}%>
        <%}%>
        <!-- Para Max Zapatos -->
   		<%if ( tipoOperacion==ClientesConstants.MAX_ZAPATOS){%>
        	<TD class=campo ><a href="#" onClick="capturaAbreviadaDatosCliente()"> Captura abreviada</a></TD>
        	<TD class=campo ><a href="#" onClick="scoreConsumo()"> Score</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaEmpleoCliente()"> Empleo</a></TD>
			<TD class=campo ><a href="#" onClick="referenciasPersonales()"> Referencias personales</a></TD>
		</TR>
		<TR>
			<TD class=campo ><a href="#" onClick="capturaReferenciasCrediticias()"> Referencias crediticias</a></TD>
			<TD class=campo ><a href="#" onClick="capturaArrendatarioDomicilio()"> Arrendatario domicilio</a></TD>
        	<TD class=campo ><a href="#" onClick="capturaCreditoSolicitado()"> Cr&eacute;dito solicitado</a></TD>
        	<TD class=campo ><a href="#" onClick="consultaDisposiciones()"> Disposiciones</a></TD>
        	<TD class=campo ><a href="#" onClick="archivosAsociados()"> Archivos asociados</a></TD>
        	<%if(solicitud.estatus == ClientesConstants.SOLICITUD_AUTORIZADA){ %>
        	<TD class=campo ><a href="#" onClick="estadoCuenta()"> Estado de cuenta</a></TD>
        	<%}else{%>
        	<TD class=campo >Estado de cuenta</TD>
        	<%}%>
			<%if (autorizadorConsumo==true){%>
	        	<TD class=campo ><a href="#" onClick="datosCalificacionCredito()"> Calificaci&oacute;n crediticia</a></TD>
        		<TD class=campo ><a href="#" onClick="capturaAutorizacionMaxZapatos()"> Autorizaci&oacute;n de cr&eacute;dito</a></TD>
        	<%}%>
        <%}%>
      </tr>
	 </TABLE>
	</TD>
   </TR>
  </TABLE>
</div>