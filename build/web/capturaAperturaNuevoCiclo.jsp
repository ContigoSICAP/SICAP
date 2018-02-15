<%@page import="com.sicap.clientes.dao.SaldoIBSDAO"%>
<%@page import="com.sicap.clientes.dao.cartera.TablaAmortDAO"%>
<%@page import="com.sicap.clientes.dao.BitacoraCicloDAO"%>
<%@page import="com.sicap.clientes.dao.IntegranteCicloDAO"%>
<%@page import="com.sicap.clientes.dao.DireccionGenericaDAO"%>
<%@page import="com.sicap.clientes.dao.CicloGrupalDAO"%>
<%@page import="com.sicap.clientes.dao.GrupoDAO"%>
<%@page import="java.util.ArrayList"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.vo.GrupoVO"%>
<%@ page import="com.sicap.clientes.vo.CicloGrupalVO"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<%@ page import="com.sicap.clientes.util.ClientesUtil"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="java.util.Vector"%>
<%@ page import="com.sicap.clientes.util.GrupoUtil"%>
<%@ page import="com.sicap.clientes.vo.DireccionGenericaVO"%>
<%@ page import="com.sicap.clientes.util.FormatUtil"%>
<jsp:directive.page import="com.sicap.clientes.vo.IntegranteCicloVO"/>

<%
Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
GrupoVO grupo = new GrupoVO();
double total = 0;
double totalConComision = 0;
boolean sinConsulta = false;
DireccionGenericaVO direccion = new DireccionGenericaVO();
CicloGrupalVO ciclo = new CicloGrupalVO();
CicloGrupalVO cicloAnterior = new CicloGrupalVO();
IntegranteCicloDAO integranteDao = new IntegranteCicloDAO();
BitacoraCicloDAO bitacoraCicloDao = new BitacoraCicloDAO();
TablaAmortDAO tablaDao = new TablaAmortDAO();
SaldoIBSDAO saldoDao= new SaldoIBSDAO();
TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
TreeMap catEstatus = CatalogoHelper.getCatalogoEstatusNuevoCiclo();
TreeMap catEstatusVIP = CatalogoHelper.getCatalogoEstatusNuevoCicloVIP();
TreeMap catDiasReunon = CatalogoHelper.getCatalogoDiasReunion();
TreeMap catHorasReunion = CatalogoHelper.getCatalogoHorasReunion();
TreeMap catEjecutivos = new TreeMap();
TreeMap catCoordinadores = catSucursales;//new TreeMap();
TreeMap catRoles = CatalogoHelper.getCatalogoRolesGrupo();//new TreeMap();
TreeMap catTasasGrupal = CatalogoHelper.getCatalogoTasas(ClientesConstants.GRUPAL);
TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
TreeMap catPlazos = CatalogoHelper.getCatalogoPlazos();
TreeMap catBancos = CatalogoHelper.getCatalogoBancosDispersion();
TreeMap catAperturador = new TreeMap();
TreeMap catGarantia = CatalogoHelper.getCatalogoGarantiaGrupal();
int otraFinanciera 	= HTMLHelper.getParameterInt(request, "otraFinanciera");
int numIntegrantes = 0;
int tasa = 0;
int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
grupo = new GrupoDAO().getGrupo(idGrupo);
catEjecutivos = CatalogoHelper.getCatalogoEjecutivosComercial(grupo.sucursal, "A");
catAperturador = CatalogoHelper.getCatalogoAperturador(grupo.sucursal, "A");
tasa = GrupoUtil.asignaTasaGrupal(grupo, false);
ciclo.integrantesArray = (ArrayList<IntegranteCicloVO>)request.getAttribute("INTEGRANTES_NUEVO_CICLO");
ciclo.estatus = 3;
ciclo.idTipoCiclo = 1;
ciclo.idCiclo = (new CicloGrupalDAO().getCicloActual(idGrupo))+1;
ciclo.bancoDispersion = new GrupoDAO().getNumBanco(grupo.sucursal);
cicloAnterior = new CicloGrupalDAO().getCicloApertura(idGrupo, ciclo.idCiclo-1);
String bloqueo = "";
boolean tieneAtrasos = false;
boolean tieneMultas = false;

if ( cicloAnterior!=null){
        ciclo.asesor = cicloAnterior.asesor;
	ciclo.coordinador = cicloAnterior.coordinador;
	ciclo.diaReunion = cicloAnterior.diaReunion;
	ciclo.direccionReunion = cicloAnterior.direccionReunion;
	ciclo.horaReunion = cicloAnterior.horaReunion;
	ciclo.multaFalta = cicloAnterior.multaFalta;
	ciclo.multaRetraso = cicloAnterior.multaRetraso;
        ciclo.plazo = cicloAnterior.plazo;        
        if (cicloAnterior.direccionReunion!=null){
            direccion = new DireccionGenericaDAO().getDireccion(cicloAnterior.idDireccionReunion);
        }
        if(tablaDao.getAtrasos(cicloAnterior.idGrupo, cicloAnterior.idCreditoIBS)>3){
            tieneAtrasos = true;
        }
        tieneMultas = saldoDao.tieneMultas(cicloAnterior.idGrupo, cicloAnterior.idCreditoIBS);
}

boolean esMesaControl = request.isUserInRole("ANALISIS_CREDITO");
boolean isOtraFinancieraFlag =  (otraFinanciera==ClientesConstants.CICLO_OTRA_FINANCIERA ? true : false );

if (isOtraFinancieraFlag)
	ciclo.idTipoCiclo = otraFinanciera;

%>
<html>
<head>
<title>Apertura de Ciclo Grupal</title>
<script language="Javascript" src="./js/functions.js"></script>
<script language="Javascript" src="./js/functionsGrupal.js"></script>
<script language="Javascript" src="./js/jquery-1.4.2.min.js"></script>
<script type="text/javascript">
<!--
	
                function guardaCicloApertura(integrantes){
                    
                    //DESCOMENTAR PARA LA PARTE DE CAPTURA DE 48 HORAS
                    /*var diaActual = new Date();
                    var fechaTemp = window.document.forma.fechaDispersion.value;
                    var diaDisp = new Date(fechaTemp.substring(6,10), fechaTemp.substring(3,5)-1, fechaTemp.substring(0,2));
                    var difDias = diaDisp.getTime()-diaActual.getTime();
                    var dias = Math.floor(difDias/(1000*60*60*24));*/
                    disableEdit();
                    window.document.forma.command.value = 'guardaAperturaCiclo';
                    window.document.forma.action="controller";                    
                    var noIntegrantes = cuentaChecked(integrantes);
                    /*
                    if (dias < 1){
                        alert('La fecha de Dispersion debe de contar con 48 hrs de anticipacion');
                        enableEdit();
                        return false;
                    }*/
                    if ( window.document.forma.plazo.value==0 ){
                        alert('Debe indicar el tipo de plazo');
                        enableEdit();
			return false;
                    }
                    if ( noIntegrantes>40 ){
			alert("El grupo debe contar con un máximo de 40 integrantes");
                        enableEdit();
			return false;
                    }
                    if ( window.document.forma.idColonia.value==0 ){
			alert("Debe capturar la dirección");
                        enableEdit();
			return false;
                    }
                    if ( window.document.forma.calle.value=='' ){
			alert("Debe capturar la calle");
                        enableEdit();
			return false;
                    }
                    if ( window.document.forma.numeroExterior.value=='' ){
			alert("Debe capturar el número exterior");
                        enableEdit();
			return false;
                    }
                    if ( window.document.forma.diaReunion.value==0 ){
			alert('Debe indicar el día de reunión');
                        enableEdit();
			return false;
                    }
                    if ( window.document.forma.horaReunion.value==0 ){
			alert('Debe indicar la hora de reunión');
                        enableEdit();
			return false;
                    }
                    if ( window.document.forma.asesor.value==0 ){
			alert('Debe indicar el ejecutivo');
                        enableEdit();
			return false;
                    }
                    if ( window.document.forma.coordinador.value==0 ){
			alert('Debe indicar el coordinador');
                        enableEdit();
			return false;
                    }
                    if ( !esFormatoMoneda(window.document.forma.multaRetraso.value) || window.document.forma.multaRetraso.value<=0 ){
			alert('El monto de la multa por retraso es inválida');
                        enableEdit();
			return false;
                    }
                    if ( !esFormatoMoneda(window.document.forma.multaFalta.value) || window.document.forma.multaFalta.value<=0  ){
			alert('El monto de la multa por falta es inválida');
                        enableEdit();
			return false;
                    }
                    if ( window.document.forma.fechaDispersion.value=='' ){
			alert("Debe capturar la fecha de dispersión");
                        enableEdit();
			return false;
                    }
                    if ( window.document.forma.bancoDispersion.value==0 ){
			alert('Debe indicar el banco para dispersar');
                        enableEdit();
			return false;
                    }
                    if(window.document.forma.comentario.value==""){
                        if(window.document.forma.estatus.value!=3){
                            alert("Debe capturar un comentario");
                            enableEdit();
                            return false;
                        }
                        else {
                            window.document.forma.comentario.value="Ciclo en Apertura";
                        }
                    }
                    <%if(catAperturador.size()>1&&ciclo.idCiclo==1){%>
                        if(window.document.forma.aperturador.value==0 ){
                            if(!confirm("No se indicado el Asesor Aperturador. ¿Desea continuar?")){
                                enableEdit();
                                window.document.forma.aperturador.focus();
                                return false;
                            }
                        }
                    <%}%>
                    if ( window.document.forma.garantia.value==0 ){
			alert('Debe indicar la garantia inicial');
                        enableEdit();
			return false;
                    }
                    <%for(int i=0;i<ciclo.integrantesArray.size(); i++){%>
                            if(document.getElementById("idSolicitud<%=i%>").value==0&&document.getElementById("desembolso<%=i%>").checked==true){
                                alert ("No es posible crear un ciclo con solicitud cero");
                                enableEdit();
                                return false;
                            }
                            else if (<%=integranteDao.getCalificacionIntegrante(ciclo.integrantesArray.get(i).idCliente, ciclo.integrantesArray.get(i).idSolicitud)== 2%>&&document.getElementById("desembolso"+<%=i%>).checked==true){
                                alert ("No es posible ingresar un cliente con solicitud rechazada");
                                enableEdit();
                                return false;
                            }
                            else if (<%=integranteDao.esSolicitudActiva(ciclo.integrantesArray.get(i).idCliente, ciclo.integrantesArray.get(i).idSolicitud, idGrupo)%>&&document.getElementById("desembolso"+<%=i%>).checked==true){
                                alert ("El cliente <%=ciclo.integrantesArray.get(i).nombre%> tiene solicitud activa en otro equipo");
                                enableEdit();
                                return false;
                            } else if(document.getElementById("idSolicitud<%=i%>").value<2&&document.getElementById("desembolso<%=i%>").checked==true&&window.document.forma.estatus.value==10){
                                alert("No es posible crear un ciclo con clientes nuevos para el estatus seleccionado");
                                enableEdit();
                                return false;
                            }
                    <%}%>
                    if(window.document.forma.estatus.value!=3){
                        if( !validaRol(integrantes) ){
                            enableEdit();
                            return false;
                        }
                        if(window.document.forma.estatus.value==10){
                            if( !minimoIntegrantesVIP(integrantes)){
                                enableEdit();
                                return false;
                            }
                        } else {
                            if( !minimoIntegrantes(<%=grupo.idOperacion%>, <%=ciclo.idTipoCiclo%>, integrantes, <%=esMesaControl%>)){
                                enableEdit();
                                return false;
                            }
                        }
                    }
                    if( !validaMontosMaximos(integrantes) ){
                        enableEdit();
                        return false;
                    }
                    if(window.document.forma.estatus.value!=3){
                        if (!confirm('La información que está apunto de guardar será enviada para su análisis y no podrá ser modificada. ¿Está seguro de cambiar el estatus del ciclo con los datos actuales?')){
                            enableEdit();
                            return false;
                        }
                    } else {
                        if (!confirm('¿Está seguro de guardar el ciclo con los datos actuales?')){
                            enableEdit();
                            return false;
                        }
                    }
                    document.body.style.cursor = "wait";
                    window.document.forma.submit();
                }


                function regresaAGrupo(){
                    disableEdit();
                    window.document.forma.command.value = 'consultaDetalleGrupo';
                    window.document.forma.action="controller";
                    document.body.style.cursor = "wait";
                    window.document.forma.submit();
                }

                function consultaCliente(idCliente){
                    window.document.forma.command.value='consultaCliente';
                    window.document.forma.idCliente.value=idCliente;
                    document.body.style.cursor = "wait";
                    window.document.forma.submit();
                }
	
                function cuentaChecked(integrantes){
                    var numchecked = 0;
                    for ( var i=0; i < integrantes ; i++ ){
			var nombreDinamico = "desembolso"+i;
			var elemento = document.getElementById(nombreDinamico);
			if ( elemento.checked == true )
				numchecked++;
                    }
		
                    return numchecked;
                }

                function abilitaBoton(){
                    if( window.document.forma.estatus.value==2 || window.document.forma.estatus.value==3)
			window.document.forma.guardaCiclo.disabled=true;
                    else
			window.document.forma.guardaCiclo.disabled=false;
                }
        
                function cursorDefault(){
                    document.body.style.cursor = "default";
                }
        
                function disableEdit(){
                    document.getElementById("botonEnviar").disabled = true;
                    document.getElementById("botonRegresar").disabled = true;
                }
        
                function enableEdit(){
                    document.getElementById("botonEnviar").disabled = false;
                    document.getElementById("botonRegresar").disabled = false;
                }
                
                function days(date){
                    var day = date.getDay();
                    var diaHabilitado = window.document.forma.diaReunion.value;
                    return [(day==diaHabilitado),""];
                }
                
                function resetDate(){
                    window.document.forma.fechaDispersion.value='';
                }
                function ayudaHistorialComentarios(numEquipo, numCiclo) {
                    params = "?command=historialComentarios"+"&idGrupo="+numEquipo+"&idCiclo="+numCiclo;
                    url = "/CEC/controller";            
                    abreVentana(url + params, 'scrollbars=yes', 1000, 250, true, 0, 0);
                }
                
//-->
</script>
<script type="text/javascript">
                $(document).ready(function(){
                    $("#expanderHead").click(function(){
                        $("#expanderContent").slideToggle();
                        if ($("#expanderSign").text() == "+"){
                                $("#expanderSign").html("-")
                        }
                        else {
                                $("#expanderSign").text("+")
                        }
                    });
                });
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0" onLoad="cursorDefault()">
<jsp:include page="header.jsp" flush="true"/>
<table border="0" width="100%">
	<tr>
		<td align="center">
<h3>Apertura Ciclo Grupal</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<br>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="guardaAperturaCiclo">
<input type="hidden" name="idGrupo" value="<%=grupo.idGrupo%>">
<input type="hidden" name="idCliente" value="">
<input type="hidden" name="idCiclo" value="<%=ciclo.idCiclo%>">
<input type="hidden" name="idSucursal" value="<%=grupo.sucursal%>">
<input type="hidden" name="numIntegrantes" value="<%=ciclo.integrantesArray.size()%>">
<input type="hidden" name="integrantesNuevoCiclo" value="<%=ciclo.integrantesArray%>">
<input type="hidden" name="idColonia" value="<%=HTMLHelper.displayField(direccion.idColonia)%>">
<input type="hidden" name="asentamientoCP" value="<%=HTMLHelper.displayField(direccion.asentamiento_cp)%>">

<table width="100%" border="0" cellpadding="0" id ="table1">
    
	<tr>
		<td width="50%" align="right">N&uacute;mero del grupo </td>
		<td width="50%">
			<input type="text" name="idGrupo" size="10" maxlength="10" value="<%=HTMLHelper.displayField(grupo.idGrupo)%>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Nombre del grupo </td>
		<td width="50%">
			<input type="text" name="nombre" size="45" maxlength="150" value="<%=HTMLHelper.displayField(grupo.nombre)%>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="100%" align="center" colspan="2"><a href="#" onClick="ayudaCodigoPostal()">Ayuda CP</a></td>
	</tr>
	<tr>
		<td width="50%" align="right">Estado</td>
		<td width="50%">  
		<input type="text" name="estado" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.estado)%>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Municipio</td>
		<td width="50%">  
		<input type="text" name="municipio" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.municipio)%>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Colonia</td>
		<td width="50%">  
		<input type="text" name="colonia" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.colonia)%>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">C&oacute;digo postal</td>
		<td width="50%">  
		<input type="text" name="cp" size="6" maxlength="6" value="<%=HTMLHelper.displayField(direccion.cp)%>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Calle</td>
		<td width="50%">  
		<input type="text" name="calle" size="30" maxlength="60" value="<%=HTMLHelper.displayField(direccion.calle)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">N&uacute;mero exterior</td>
		<td width="50%">  
		<input type="text" name="numeroExterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(direccion.numeroExterior)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">N&uacute;mero interior</td>
		<td width="50%">  
		<input type="text" name="numeroInterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(direccion.numeroInterior)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Estatus</td>
		<td width="50%">
                    <%if(request.isUserInRole("AUTORIZACION_EQUIPOS_VIP")&&ciclo.idCiclo>1&&!tieneAtrasos&&!tieneMultas){%>
                    <select name="estatus" id="estatus" size="1">
			<%=HTMLHelper.displayCombo(catEstatusVIP, ciclo.estatus)%>
                    </select>
                    <%} else{%>
                    <select name="estatus" id="estatus" size="1">
			<%=HTMLHelper.displayCombo(catEstatus, ciclo.estatus)%>
                    </select>
                    <%}%>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">D&iacute;a de reuni&oacute;n del grupo</td>
		<td width="50%">  
                    <select name="diaReunion" id="diaReunion" size="1" onchange="resetDate()">
			<%=HTMLHelper.displayCombo(catDiasReunon, ciclo.diaReunion)%>
                        
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Hora de reuni&oacute;n del grupo</td>
		<td width="50%">  
			<select name="horaReunion" size="1">
			<%=HTMLHelper.displayCombo(catHorasReunion, ciclo.horaReunion)%>
			</select>
		</td>
	</tr>
        <%if(ciclo.idCiclo==1){ %>
        <tr>
		<td width="50%" align="right">Aperturador</td>
		<td width="50%">  
			<select name="aperturador" size="1">
			<option value="0">Seleccione...</option>
			<%=HTMLHelper.displayCombo(catAperturador, ciclo.aperturador)%>
			</select>
		</td>
	</tr>
        <%}%>
	<tr>
		<td width="50%" align="right">Ejecutivo</td>
		<td width="50%">  
			<select name="asesor" size="1">
			<option value="0">Seleccione...</option>
			<%=HTMLHelper.displayCombo(catEjecutivos, ciclo.asesor)%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Sucursal</td>
		<td width="50%">  
			<select name="coordinador" size="1">
			<%=HTMLHelper.displayCombo(catCoordinadores, grupo.sucursal)%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Multa por retraso</td>
		<td width="50%">  
			<input type="text" name="multaRetraso" size="10" maxlength="8" value="<%=HTMLHelper.formatoMonto(ciclo.multaRetraso)%>" >
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Multa por falta</td>
		<td width="50%">  
			<input type="text" name="multaFalta" size="10" maxlength="8" value="<%=HTMLHelper.formatoMonto(ciclo.multaFalta)%>" > 
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Tasa</td>
		<td width="50%">  
			<input type="text" name="tasaDescripcion" size="10" maxlength="8" value="<%=CatalogoHelper.getDescripcionTasa(tasa, catTasasGrupal)%>" readonly="readonly" class="soloLectura"> 
			<input type="hidden" name="idTasa" value="<%=tasa%>">
		</td>
	</tr>
        <tr>
		<td width="50%" align="right">Plazo</td>
                <td><select name="plazo" id="plazo" size="1" onKeyPress="return submitenter(this,event)">
                    <%= HTMLHelper.displayCombo(catPlazos, ciclo.plazo)%>
                    </select>
                </td>
	</tr>
        <tr>
		<td width="50%" align="right">Fecha de Dispersión</td>
                <td><input type="text" name="fechaDispersion" id="fechaDispersion" size="10%" maxlength="10" style="background-color: white" readonly="readonly" class="soloLectura">
                </td>
	</tr>
        <tr>
		<td width="50%" align="right">Dispersar por</td>
		<td width="50%">  
			<select name="bancoDispersion" size="1">
			<%=HTMLHelper.displayCombo(catBancos, ciclo.bancoDispersion)%>
			</select>
		</td>
	</tr>
        <%if(request.isUserInRole("CAPTURA_GARANTIA") && CatalogoHelper.esEquipoAutorizadoGarantia(grupo.idGrupo)){
            if(ciclo.estatus == ClientesConstants.CICLO_DISPERSADO || ciclo.estatus == ClientesConstants.CICLO_CERRADO || ciclo.estatus == ClientesConstants.CICLO_AUTORIZADO){
                bloqueo = "disabled";
            }
        %>
	<tr>
		<td width="50%" align="right">% Garant&iacute;a</td>
		<td width="50%">  
			<select name="garantia" size="1" <%=bloqueo%>>
			<%=HTMLHelper.displayCombo(catGarantia, ciclo.garantia)%>
			</select>
		</td>
	</tr>
        <%}else{%>
        <input type="hidden" name="garantia" value="3">
        <%}%>
	<tr>
		<td colspan="2" align="center"><br>
			<table border="1" width="90%">
				<tr>
					<td align="center">  
						
					</td>
					<td align="center">
						No.
					</td>
					<td align="center"> 
						Cliente
					</td>
					<td align="center"> 
						Solicitud
					</td>
					<td align="center">
						Nombre
					</td>
					<td align="center"> 
						Monto maximo posible 
					</td>
					<td align="center">
						Monto a desembolsar
					</td>
					<td align="center">
						Monto con comisión
					</td>
                                        <td align="center">
						Calificaci&oacute;n
					</td>
                                        <td align="center">
						Rol
					</td>
                                        <td align="center">
                                                Doc. Completa
                                        </td>
				</tr>
<%for ( int i=0 ; ciclo.integrantesArray!=null && i<ciclo.integrantesArray.size() ; i++ ){%>
				<tr>
					<td align="center"> 
					<%=HTMLHelper.displayCheck("desembolso"+i,true)%>
					</td>
					<td align="center"> 
						<%=i+1%>
					</td>
					<td align="center"> 
                                            <input type="hidden" name="idCliente<%=i%>" id="idCliente<%=i%>" value="<%=ciclo.integrantesArray.get(i).idCliente%>">
						<a href="#" onClick="consultaCliente(<%=ciclo.integrantesArray.get(i).idCliente%>)"><%=ciclo.integrantesArray.get(i).idCliente%></a>
					</td>
					<td align="center"> 
						<input type="hidden" id ="idSolicitud<%=i%>" name="idSolicitud<%=i%>" value="<%=ciclo.integrantesArray.get(i).idSolicitud%>">
						<%=ciclo.integrantesArray.get(i).idSolicitud%>
					</td>
					<td align="center">
						<input type="hidden" name="nombre<%=i%>" id="nombre<%=i%>" value="<%=ciclo.integrantesArray.get(i).nombre%>">
						<%if (integranteDao.getCalificacionIntegrante(ciclo.integrantesArray.get(i).idCliente, ciclo.integrantesArray.get(i).idSolicitud)== 2){%>
                                                    <font color="red"><%=ciclo.integrantesArray.get(i).nombre%></font>
                                                <%} else {%>
                                                    <%=ciclo.integrantesArray.get(i).nombre%>
                                                <%}%>
					</td>
					<td align="right">
                                                <input type="hidden" name="montoMaximo<%=i%>" id="montoMaximo<%=i%>" value="<%--=HTMLHelper.formatoMonto(ClientesUtil.calculaMontoEscalera(ciclo.integrantes[i].idCliente, ciclo.integrantes[i].idSolicitud) )--%>">
						<%=HTMLHelper.formatoMonto( ClientesUtil.calculaMontoEscalera(ciclo.integrantesArray.get(i).idCliente, ciclo.integrantesArray.get(i).idSolicitud ) )%>
                                        </td>
					<td align="right">
						<input type="text" name="monto<%=i%>" id="monto<%=i%>" value="<%=HTMLHelper.formatoMonto( ClientesUtil.calculaMontoSinComision( ciclo.integrantesArray.get(i).monto, ciclo.integrantesArray.get(i).comision, catComisionesGrupal ) )%>" size="8" maxlength="9">
					</td>
					<td align="right">
						<input type="hidden" name="montoComision<%=i%>" id="montoComision<%=i%>" value="<%=ciclo.integrantesArray.get(i).monto%>" >
						<%=HTMLHelper.formatoMonto(ciclo.integrantesArray.get(i).monto)%>
					</td>
                                        <td align="right" BGCOLOR= "<%=HTMLHelper.getColorCalificacion(ciclo.integrantesArray.get(i).calificacion,ciclo.integrantesArray.get(i).aceptaRegular,grupo.sucursal)%>">
                                            <input type="hidden" name="calificacion<%=i%>" id="calificacion<%=i%>" value="<%=ciclo.integrantesArray.get(i).calificacion%>" >
                                            <input type="hidden" name="aceptaRegular<%=i%>" id="aceptaRegular<%=i%>" value="<%=ciclo.integrantesArray.get(i).aceptaRegular%>" >                                            
					</td>
					<td align="center">
						<select name="rol<%=i%>" id="rol<%=i%>" size="1" value="<%=ciclo.integrantesArray.get(i).rol%>" >
						<%=HTMLHelper.displayCombo(catRoles, ciclo.integrantesArray.get(i).rol)%>
						</select>
					</td>
                                        <td align="center">
                                            <%if(ciclo.integrantesArray.get(i).RenovacionDoc>0){%>
                                                <input type="checkbox" name="Doc<%=i%>" id="Doc<%=i%>" value="si" disabled 
                                                <%if(ciclo.integrantesArray.get(i).DocCompletos>0){%>
                                                    checked
                                                <%}%>
                                                >
                                            <%}%>
                                        </td>
                                </tr>
<%
total += FormatUtil.redondeaMoneda(ClientesUtil.calculaMontoSinComision( ciclo.integrantesArray.get(i).monto, ciclo.integrantesArray.get(i).comision, catComisionesGrupal ));
totalConComision += ciclo.integrantesArray.get(i).monto;
numIntegrantes = i;
}%>
				<tr>
					<td colspan="5" align="center">  
						Total 
					</td>
					<td align="right">
						<input type="hidden" name="montoTotal" value="<%=total%>">
						<%=HTMLHelper.formatoMonto(total) %>
					</td>
					<td align="right">
						<input type="hidden" name="montoTotalConComision" value="<%=totalConComision%>">
						<%=HTMLHelper.formatoMonto(totalConComision) %>
					</td>
					<td>
						&nbsp;
					</td>
					<% if(grupo.idOperacion==ClientesConstants.GRUPAL){%>
					<td>
						&nbsp;
					</td>
					<%}%>
				</tr>
			</table>
		</td>
	</tr>
        <%if(bitacoraCicloDao.getUltimoComentario(grupo.idGrupo, ciclo.idCiclo)!=""){%>
        <tr>
            <td colspan="2" align="center">
                <br>
                <table border="1" width="80%">
                    <tr>
                        <td colspan="2" align="center"><textarea name="ultimoComentario" rows="1" cols="100" readonly="readonly" class="soloLectura"><%=bitacoraCicloDao.getUltimoComentario(grupo.idGrupo, ciclo.idCiclo) %></textarea>
                            <a href="#" onClick="ayudaHistorialComentarios(<%=grupo.idGrupo%>, <%=ciclo.idCiclo%>)"><img name="imagen" alt="Historial" src="images/history_icon.png"></a>
                        </td>                        
                    </tr>                    
                </table>
            </td>
        </tr>
        <%}%>
        <tr>
            <td colspan="2" align="center">
                <h4 id="expanderHead" style="cursor:pointer;">
                        Agregar Comentario <span id="expanderSign">+</span>
                    </h4>
                    <div id="expanderContent" style="display:none">
                        <textarea name="comentario" rows="5" cols="100" placeholder="Escribe tu comentario..."></textarea>
                    </div>
            </td>
        </tr>
	<tr>
		<td align="center" colspan="2">
			<%if( !sinConsulta){ %>
				<br><input type="button" id="botonEnviar" name="guardaCiclo" value="Enviar" onClick="guardaCicloApertura(<%=numIntegrantes+1%>)">
			<%}%>
                        <input type="button" id="botonRegresar" value="Regresar" onClick="regresaAGrupo()">
		</td>
	</tr>
</table>
</form>
		</td>
	</tr>
</table>
</body>
</html>