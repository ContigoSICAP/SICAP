<%@page import="com.sicap.clientes.util.Notification"%>
<%@page import="com.sicap.clientes.dao.SaldoIBSDAO"%>
<%@page import="com.sicap.clientes.dao.cartera.TablaAmortDAO"%>
<%@page import="com.sicap.clientes.vo.IntegranteCicloVO"%>
<%@page import="com.sicap.clientes.dao.BitacoraCicloDAO"%>
<%@page import="com.sicap.clientes.vo.BitacoraCicloVO"%>
<%@page import="com.sicap.clientes.vo.ArchivoAsociadoVO"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.vo.GrupoVO"%>
<%@ page import="com.sicap.clientes.vo.CicloGrupalVO"%>
<%@ page import="com.sicap.clientes.dao.SucursalDAO"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.sicap.clientes.util.ClientesUtil"%>
<%@ page import="com.sicap.clientes.util.GrupoUtil"%>
<%@ page import="com.sicap.clientes.util.FechasUtil"%>
<%@ page import="com.sicap.clientes.vo.DireccionGenericaVO"%>
<%@ page import="com.sicap.clientes.util.FormatUtil"%>

<%
Calendar cal = Calendar.getInstance();
Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
int semDisp = (Integer)request.getAttribute("semanaDisp");
Date[] fechasInhabiles = (Date[])session.getAttribute("INHABILES");
//BitacoraCicloVO bitacoraCiclo = (BitacoraCicloVO)request.getAttribute("COMENTARIO");
//System.out.println("COMENTARIO: "+ bitacoraCiclo.getComentario());
boolean diainhabil = FechasUtil.esDiaInhabil( cal.getTime(), fechasInhabiles);
boolean sinConsulta = false;
GrupoVO grupo = new GrupoVO();
double total = 0;
double totalConComision = 0;
double totalConSegugoFinanciado = 0;
double totalCostoSeguro = 0;
int cantIntegrantesIC = 0;

BitacoraCicloDAO bitacoraCicloDao = new BitacoraCicloDAO();
TablaAmortDAO tablaDao = new TablaAmortDAO();
SaldoIBSDAO saldoDao= new SaldoIBSDAO();
DireccionGenericaVO direccion = new DireccionGenericaVO();
CicloGrupalVO ciclo = new CicloGrupalVO();

TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
//TreeMap catEstatusIC = CatalogoHelper.getCatalogoEstatusInterciclo();
TreeMap catEstatusIC = null;
TreeMap catDiasReunon = CatalogoHelper.getCatalogoDiasReunion();
TreeMap catHorasReunion = CatalogoHelper.getCatalogoHorasReunion();
TreeMap catEjecutivos = new TreeMap();
TreeMap catCoordinadores = catSucursales;//new TreeMap();
TreeMap catRoles = CatalogoHelper.getCatalogoRolesGrupo();//new TreeMap();
TreeMap catTasasGrupal = CatalogoHelper.getCatalogoTasas(ClientesConstants.GRUPAL);
TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
TreeMap catFondeador = CatalogoHelper.getCatalogoFondeador();
TreeMap catDespachos = CatalogoHelper.getCatalogoDespachos();
TreeMap catPlazos = CatalogoHelper.getCatalogoPlazos();
TreeMap catBancos = CatalogoHelper.getCatalogoBancosDispersion();
TreeMap catAperturador = new TreeMap();
TreeMap catGarantia = CatalogoHelper.getCatalogoGarantiaGrupal();
int idCiclo 		= HTMLHelper.getParameterInt(request, "idCiclo");
int i = 0; //se inicializo el indice del for de los clientes aqui para conservar los datos del mismo
//int otraFinanciera 	= HTMLHelper.getParameterInt(request, "otraFinanciera");
int otraFinanciera 	= 3;
int numIntegrantes = 0;
// el campo de seguros completos se deja como 1 para pasar la validacion JBL- SEP/10
int segurosCompletos = 1;
int idComision=0;
int plazo=0;
int tasa = 0;
int idBanco = 0;
int fondeador = 0;
int despacho = 0;
String campSeguro = "";
int estatusIC =0;
boolean sucursalAceptaRegular = false;
ArchivoAsociadoVO fldSeguro = null;
//ArchivoAsociadoVO fldDocums = null;
String bloqueo = "";
if ( session.getAttribute("GRUPO")!=null ){
        grupo = (GrupoVO)session.getAttribute("GRUPO");
        tasa = GrupoUtil.asignaTasaGrupal(grupo, false);
	idBanco = new SucursalDAO().getSucursal(grupo.sucursal).idBanco;

	catEjecutivos = CatalogoHelper.getCatalogoEjecutivosComercial(grupo.sucursal, "A");
        catAperturador = CatalogoHelper.getCatalogoAperturador(grupo.sucursal, "A");
}
String desembolsoOk = "NO";
if ( request.getAttribute("VALIDACION")!=null )
	desembolsoOk = (String)request.getAttribute("VALIDACION");

int validaCiclo = -1;
if ( request.getAttribute("ID_CICLO")!=null )
	validaCiclo = (Integer)request.getAttribute("ID_CICLO");

ciclo = grupo.ciclos[idCiclo-1];

// Se verifica si viene de otra financiera JBL SEP/10
boolean isDesembolsadoFlag = GrupoHelper.isDesembolsado(ciclo.integrantes);


if( !isDesembolsadoFlag )
	ciclo.tasa = tasa;
else
	tasa = ciclo.tasa;

// En caso de venir de otra financiera se cambia el tipo de ciclo y asi se le da un trato diferente JBL SEP/10

boolean esMesaControl = request.isUserInRole("ANALISIS_CREDITO");
int opcion = GrupoHelper.muestraOpcionesGrupal( ciclo, desembolsoOk );
String medio = "";
if (semDisp==ClientesConstants.DISPERSION_SEMANA_2){
    estatusIC = ciclo.estatusIC;
}
if (semDisp==ClientesConstants.DISPERSION_SEMANA_4){
    estatusIC = ciclo.estatusIC2;
}
if (estatusIC!=0)
    catEstatusIC = CatalogoHelper.getCatalogoEstatusCicloApertura(estatusIC);

sucursalAceptaRegular = CatalogoHelper.esSucursalAceptaRegular(grupo.sucursal);
%>
<html>
<head>
<title>Alta de Ciclo Grupal</title>
<script language="Javascript" src="./js/functions.js"></script>
<script language="Javascript" src="./js/functionsGrupal.js"></script>
<script language="Javascript" src="./js/jquery-1.4.2.min.js"></script>
<script type="text/javascript">



	function guardaCicloGrupal(integrantes,integrantesIC,estatus){
            
            estatusNuevo = document.getElementById("estatus").value            
            //window.document.forma.action="controller";
            cantIC = 0 ;
            var mala = 0
            inputs= document.getElementsByTagName('input');   
            for(i=0;i<inputs.length;i++){
                if(inputs[i].type == "checkbox"){ //solo si es un checkbox entramos
                    
                    if (inputs[i].checked==1){
                        //desembolso
                        var nombrecheck =inputs[i].name;
                        if (nombrecheck.substring(0, 3) != "Doc"){
                            var elementoCC = document.getElementById("calificacion"+nombrecheck.substr(10));
                            if (elementoCC.value == <%=ClientesConstants.CALIFICACION_CIRCULO_MALA%>){
                                mala =-1;                            
                                break;                            
                            }else
                                cantIC =cantIC +1;
                        }
                    } 
                }
                else if (inputs[i].type === 'button'){// solo si es un boton entramos
                    inputs[i].disabled = true
                }
            }
            if (mala == -1){
            alert("Integrante con calificacion mala. Descartar para su envio");
                for (var i = 0; i < inputs.length; i++) {
                    if (inputs[i].type === 'button') {
                        inputs[i].disabled = false;
                    }
                }
                return false;            
            }
            if (estatusNuevo==0){
                alert("Debe selecionar un estatus");
                for (var i = 0; i < inputs.length; i++) {
                    if (inputs[i].type === 'button') {
                        inputs[i].disabled = false;
                    }
                }
                return false;
            }
            if(<%=estatusIC%>==0){
                alert("Todavia no se inicia el proceso de interciclo para esta semana, favor de ingresar una nueva solicitud de Inter-Ciclo");
                for (var i = 0; i < inputs.length; i++) {
                    if (inputs[i].type === 'button') {
                        inputs[i].disabled = false;
                    }
                }
                return false;
            }

            if (<%=esMesaControl%>) {
                    if (window.document.forma.estatus.value != <%=ClientesConstants.CICLO_PENDIENTE%> && window.document.forma.estatus.value != <%=ClientesConstants.CICLO_RECHAZADO%> && window.document.forma.estatus.value != <%=ClientesConstants.CICLO_AUTORIZADO%>) {
                        alert('Estatus no permitido para un usuario de Mesa de Control');
                        for (var i = 0; i < inputs.length; i++) {
                            if (inputs[i].type === 'button') {
                                    inputs[i].disabled = false;
                                }
                        }
                        return false;
                    }
                } else {
                    <%if (estatusIC != ClientesConstants.CICLO_PENDIENTE && estatusIC != ClientesConstants.CICLO_RECHAZADO) { %>
                        if (window.document.forma.estatus.value != <%=ClientesConstants.CICLO_APERTURA%> && window.document.forma.estatus.value != <%=ClientesConstants.CICLO_ANALISIS%>) {
                            alert('Estatus no permitido');
                            for (var i = 0; i < inputs.length; i++) {
                                if (inputs[i].type === 'button') {
                                    inputs[i].disabled = false;
                                }
                            }
                            return false;
                        }
                    <%} else {%>
                        if (window.document.forma.estatus.value != <%=ClientesConstants.CICLO_REVALORACION%>) {
                            alert('Estatus no permitido');
                            for (var i = 0; i < inputs.length; i++) {
                                if (inputs[i].type === 'button') {
                                    inputs[i].disabled = false;
                                }
                            }
                            return false;
                        }
                     <%}%>
                }
            if (integrantes>35){
		alert("El grupo supera la cantidad permitida de integrantes para Inter-Ciclo");
                for (var i = 0; i < inputs.length; i++) {
                    if (inputs[i].type === 'button') {
                        inputs[i].disabled = false;
                    }
                }
                return false;
            }
            if(cantIC == 0){
                alert("Debe selecionar al menos un integrante");
                for (var i = 0; i < inputs.length; i++) {
                    if (inputs[i].type === 'button') {
                        inputs[i].disabled = false;
                    }
                }
                return false;      
            }
       
            if ( !validaMontoIntegrantes(cantIC) ){
                        document.getElementById("guardaCiclo").disabled = false;
                        return false;
            }
            if (window.document.forma.comentario.value == "") {
                if (window.document.forma.estatus.value != <%=ClientesConstants.CICLO_APERTURA%>) {
                    alert("Favor de ingresar un comentario");
                    for (var i = 0; i < inputs.length; i++) {
                        if (inputs[i].type === 'button') {
                            inputs[i].disabled = false;
                        }
                    }
                    return false;
                }
            }
            
            res = confirm('La información que está apunto de guardar no podrá ser modificada, ¿Esta seguro de que desea registrar el Inter-Ciclo con los datos actuales?');
            if ( !res ){                 
                for (var i = 0; i < inputs.length; i++) {
                    if (inputs[i].type === 'button') {
                        inputs[i].disabled = false;
                    }
                }
                return res;
            }
            window.document.forma.command.value = 'guardaInterCiclo'
            document.body.style.cursor = "wait";
            window.document.forma.submit();
            
	}
        
        function rechazaInterciclo(){    
            var inputs = document.getElementsByTagName("input");
            for (var i = 0; i < inputs.length; i++) {
                if (inputs[i].type === 'button') {
                    inputs[i].disabled = true;
                }
            }
            window.document.forma.command.value = 'rechazaInterCiclo';            
            window.document.forma.action="controller";
            res = confirm('La información que está apunto de guardar no podrá ser modificada ¿Esta seguro de que desea rechazar el Inter-Ciclo?');
            if ( !res ){                 
                for (var i = 0; i < inputs.length; i++) {
                    if (inputs[i].type === 'button') {
                        inputs[i].disabled = false;
                    }
                }
                return res;
            }
            document.body.style.cursor = "wait";
            window.document.forma.submit();          
        }

	function regresaAGrupo(){
                
                var inputs = document.getElementsByTagName("input");
                for (var i = 0; i < inputs.length; i++) {
                    if (inputs[i].type === 'button') {
                        inputs[i].disabled = true;
                    }
                }
		window.document.forma.command.value = 'consultaDetalleGrupo';
		window.document.forma.action="controller";
                document.body.style.cursor = "wait";
		window.document.forma.submit();
	}
	
	function consultaPagosComunales(){
                document.getElementById("td").disabled = true;
		window.document.forma.command.value='validarPagoComunal';
                window.document.forma.submit();
	}

	function muestraDocumento(tipo){
                window.document.forma.command.value='muestraDocumento';
		//window.document.forma.target="_blank";
		window.document.forma.tipo.value=tipo;
                window.document.forma.submit();
                
	}
	
	function consultaCliente(idCliente){
                document.getElementById("td").disabled = true;
		window.document.forma.command.value='consultaCliente';
		window.document.forma.idCliente.value=idCliente;
                document.body.style.cursor = "wait";
		window.document.forma.submit();
	}
	
	function cuentaDesembolsos(integrantes){
		var numchecked = 0;
		for ( var i=0; i < integrantes ; i++ ){
			var nombreDinamico = "desembolso"+i;
			var elemento = document.getElementById(nombreDinamico);
			if ( elemento.checked == true )
				numchecked++;
		}
		
		return numchecked;
	}
	
	function consultaEstadoCuentaGrupal(){
                document.getElementById("td").disabled = true;
		window.document.forma.action="controller";
		window.document.forma.command.value="consultaEstadoCuentaGrupal";
                document.body.style.cursor = "wait";
		window.document.forma.submit();
	}

	function guardaOtrosDatos(){
                document.getElementById("guardaDatosExtra").disabled = true;
		window.document.forma.action="controller";
		window.document.forma.command.value="guardaOtrosDatos";
                document.body.style.cursor = "wait";
		window.document.forma.submit();
	}

	function habilitaCierreCiclo(){
		if( window.document.forma.estatus.value==2 ){
			window.document.forma.guardaCiclo.disabled=false;
			if(<%=isDesembolsadoFlag%> && <%=ciclo.estatus!=2%>)
				window.document.forma.guardaDatosExtra.disabled = true;
		}else{
			window.document.forma.guardaCiclo.disabled=true;
			if(<%=isDesembolsadoFlag%> && <%=ciclo.estatus!=2%>)
				window.document.forma.guardaDatosExtra.disabled = false;
		}
	}
	function habilitaBoton(){
		if( window.document.forma.estatus.value==2 ){
			window.document.forma.guardaCiclo.disabled=true;
			if(<%=isDesembolsadoFlag%> && <%=ciclo.estatus!=2%>)
				window.document.forma.guardaDatosExtra.disabled = false;
		}else{
			window.document.forma.guardaCiclo.disabled=false;
			if(<%=isDesembolsadoFlag%> && <%=ciclo.estatus!=2%>)
				window.document.forma.guardaDatosExtra.disabled = true;
		}
	}

        function capturaControlPagos(){
            window.document.forma.command.value = 'capturaControlPagos';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
        }
        
        function cargarArchivos(tipo){
            window.document.forma.action="guardaArchivosGrupal.jsp";
            window.document.forma.target="_self";
            if(window.document.forma.autorizacion.value != ""){
                if(tipo == "fileDocumentacion" && (window.document.forma.autorizacion.indexOf(".zip") != -1 || window.document.forma.autorizacion.indexOf(".rar") != -1)){
                    document.body.style.cursor = "wait";
                     window.document.forma.submit();
                } else if(tipo == "fileFicha"){
                    document.body.style.cursor = "wait";
                    window.document.forma.submit();
                } else {
                    alert("El Archivo no se encuentra en un formato permitido");
                }
            } else {
                alert("Debe de agregar la ruta del archvo");
            }
            //window.document.forma.submit();
        }
        
        function descargaFichaSeguro(){
            window.document.forma.command.value = 'descargaArchivo';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
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
       function agregaComentario(){
           if(window.document.forma.comentario.value==""){
               alert("No has ingresado comentarios")  
               return false;
           } else if(!confirm("¿Deseas agregar el comentario?")){
               return false;
           } else {
               window.document.getElementById("boton_agrega_comentario").disabled=true;
               window.document.forma.command.value='agregaComentarioCiclo';
               window.document.forma.submit();
           }
       }
       function validaMonto(monto){
           if(monto > 40000){
               alert("El monto no puede ser mayor a $40,000.00");
               window.document.getElementById("guardaCiclo").disabled=true;
           } else {
               window.document.getElementById("guardaCiclo").disabled=false;
           }
       }
       function validaMontoIntegrantes(integrantes){
           for ( var i=0; i < integrantes ; i=i+1 ){
               var nombreDinamico = "monto"+i;
               var nombreDinamico = ""+i;
               var cliente = "idCliente"+i;
               var elemento = document.getElementById(nombreDinamico);
               if(elemento > 40000){
                   alert("El monto del cliente "+cliente+" no puede ser mayor a los $40,000.00");
                   return false;
               }
               return true;
            }
       }
       function cambiarDisplay(id) {
           if (!document.getElementById)
               return false;
           fila = document.getElementById(id);
           if (fila.style.display != "none") {
               fila.style.display = "none"; //ocultar fila
           } else {
               fila.style.display = ""; //mostrar fila
           }
       }
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
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<table border="0" width="100%">
	<tr>
		<td align="center">
<h3>Alta de Inter-Ciclo</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<br>

<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="guardaCicloGrupal">
<input type="hidden" name="idGrupo" value="<%=grupo.idGrupo%>">
<input type="hidden" name="idTipoCiclo" value="<%=ciclo.idTipoCiclo%>">
<input type="hidden" name="idCicloRefinancear" value="<%=ciclo.idCiclo%>" >
<input type="hidden" name="idCliente" value="">
<input type="hidden" name="esNuevo" value="<%=validaCiclo%>" >
<input type="hidden" name="saldoT24" value="<%=ciclo.estatusT24%>">
<input type="hidden" name="idCiclo" <%if(idCiclo==0){%>value="<%=ciclo.idCiclo%>"<%}else{%>value="<%=idCiclo%>"<%}%> >
<input type="hidden" name="idSucursal" value="<%=grupo.sucursal%>">
<input type="hidden" name="tipo" value="">
<input type="hidden" name="numIntegrantes" value="<%=ciclo.integrantes.length%>" >
<input type="hidden" name="idColonia" value="<%=HTMLHelper.displayField(ciclo.direccionReunion.idColonia)%>">
<input type="hidden" name="asentamientoCP" value="<%=HTMLHelper.displayField(ciclo.direccionReunion.asentamiento_cp)%>">
<input type="hidden" name="segurosCompletos" value="<%=segurosCompletos%>">
<input type="hidden" name="desembolsado" value="<%=isDesembolsadoFlag%>">
<input type="hidden" name="idComision" value="<%=ciclo.comision%>">
<input type="hidden" name="semDisp" value="<%=semDisp%>">
<input type="hidden" name="semanaDisp">

<table width="100%" border="0" cellpadding="0">
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
		<td width="50%" align="right">Número de ciclo</td>
		<td width="50%">
			<input type="text" name="idCicloRead" size="10" maxlength="10" value="<% if ( ciclo.idCiclo!=0 ) out.print(HTMLHelper.displayField(ciclo.idCiclo)); %>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="100%" align="center" colspan="2"><a href="#" onClick="ayudaCodigoPostal()">Ayuda CP</a></td>
	</tr>
	<tr>
		<td width="50%" align="right">Estado</td>
		<td width="50%">  
		<input type="text" name="estado" size="40" maxlength="100" value="<%=HTMLHelper.displayField(ciclo.direccionReunion.estado)%>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Municipio</td>
		<td width="50%">  
		<input type="text" name="municipio" size="40" maxlength="100" value="<%=HTMLHelper.displayField(ciclo.direccionReunion.municipio)%>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Colonia</td>
		<td width="50%">  
		<input type="text" name="colonia" size="40" maxlength="100" value="<%=HTMLHelper.displayField(ciclo.direccionReunion.colonia)%>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">C&oacute;digo postal</td>
		<td width="50%">  
		<input type="text" name="cp" size="6" maxlength="6" value="<%=HTMLHelper.displayField(ciclo.direccionReunion.cp)%>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Calle</td>
		<td width="50%">  
		<input type="text" name="calle" size="30" maxlength="60" value="<%=HTMLHelper.displayField(ciclo.direccionReunion.calle)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">N&uacute;mero exterior</td>
		<td width="50%">  
		<input type="text" name="numeroExterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(ciclo.direccionReunion.numeroExterior)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">N&uacute;mero interior</td>
		<td width="50%">  
		<input type="text" name="numeroInterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(ciclo.direccionReunion.numeroInterior)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Estatus Inter-Ciclo</td>
		<td width="50%">  
			<select name="estatus" id="estatus" size="1" >
                            <%if(estatusIC==0){%>
                                <option value="0"> Seleccione...</option>
                            <%}else{%>
                                <%=HTMLHelper.displayCombo(catEstatusIC, estatusIC)%>
                            <%}%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">D&iacute;a de reuni&oacute;n del grupo</td>
		<td width="50%">  
			<select name="diaReunion" size="1">
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
        <tr>
                <td width="50%" align="right">Aperturador</td>
		<td width="50%">  
                    <select name="aperturador" size="1" disabled>
			<option value="0">Seleccione...</option>
			<%=HTMLHelper.displayCombo(catAperturador, ciclo.aperturador)%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Ejecutivo</td>
		<td width="50%">  
			<select name="asesor" size="1">
			<option value="0">Seleccione...</option>
			<%=HTMLHelper.displayCombo(catEjecutivos, ciclo.asesor)%>
			</select>
		</td>
                <input type="hidden" name="aperturador" value=<%=ciclo.aperturador%>>
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
                <td><input type="text" name="fechaDispersion" id="fechaDispersion" size="10%" maxlength="10" value="<%=HTMLHelper.displayField(ciclo.fechaDispersion)%>" readonly="readonly">
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
            if(estatusIC == 1 || estatusIC == 2){
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
            <td width="50%" align="right">Fondeador</td>
            <%if(request.isUserInRole("ANALISIS_CREDITO") && fondeador == ClientesConstants.ID_FONDEADOR_FOMMUR){%>
            <td><select name="fondeador" id="fondeador" size="1" onKeyPress="return submitenter(this,event)">
                    <%= HTMLHelper.displayCombo(catFondeador, ciclo.getFondeador())%>
                </select>
            </td>
            <%} else {%>
            <td><select name="fondeador" id="fondeador" size="1" onKeyPress="return submitenter(this,event)" onfocus="this.oldvalue=this.value;this.blur();" onchange="this.value=this.oldvalue;">
                    <%= HTMLHelper.displayCombo(catFondeador, ciclo.getFondeador())%>
                </select>
            </td>
            <%}%>
	</tr>
        <tr>
            <td width="50%" align="right">Despacho</td>
            <%if(request.isUserInRole("ADM_PAGOS_MANAGER")){%>
            <td><select name="numDespacho" id="numDespacho" size="1" onKeyPress="return submitenter(this,event)">
                    <%= HTMLHelper.displayCombo(catDespachos, despacho)%>
                </select>
            </td>
            <%} else {%>
            <td><select name="numDespacho" id="numDespacho" size="1" onKeyPress="return submitenter(this,event)" disabled>
                    <%= HTMLHelper.displayCombo(catDespachos, despacho)%>
                </select>
            </td>
            <%}%>
	</tr>

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
						Monto a desembolsar
					</td>
					<td align="center">
						Monto con Seguro Financiado
					</td>
                                        <td align="center">
                                                Monto de Seguro
                                        </td>                                        
					<td align="center">
						Comisión
					</td>
					
					<td align="center">
						Calificación
					</td>
					<td align="center">
						Medio Cobro
					</td>
					<td align="center">
						Referencia Cobro
					</td>
					<td align="center">
						Rol
					</td>
                                        <td align="center">
                                                Doc. Completa
                                        </td>                                         
				</tr>
<%for ( i=0 ; ciclo.integrantes!=null && i<ciclo.integrantes.length ; i++ ){ 
	String estilo = "";
	
            if(ciclo.integrantes[i].estatus == 2){ //Esta cancelado
                if(ciclo.integrantes[i].semDisp == 2||ciclo.integrantes[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO)//Interciclo con dispercion Semana 2
                    estilo = "clienteInterCiclo2Cancelado";
                else if(ciclo.integrantes[i].semDisp == 4||ciclo.integrantes[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO_2) //Interciclo con dispercion Semana 4
                    estilo = "clienteInterCicloCancelado";
                else 
                    estilo = "soloLecturaRojo";
            }else{
                if(ciclo.integrantes[i].semDisp == 2||ciclo.integrantes[i].tipo == ClientesConstants.TIPO_CLIENTE_INTERCICLO)//Interciclo con dispercion Semana 2
                    estilo = "clienteInterCiclo2";
                if(ciclo.integrantes[i].semDisp == 4) //Interciclo con dispercion Semana 4
                    estilo = "clienteInterCiclo";
            }
        
%>
				<tr class="<%=estilo%>" >
					<td align="center"> 
                                            <%if(ciclo.integrantes[i].esInterciclo==1&&ciclo.integrantes[i].semDisp ==ciclo.getSemDisp()){                                                
                                                cantIntegrantesIC=+1;%>
                                                <%=HTMLHelper.displayCheck("desembolso"+i,true)%>
                                            <%}%>
					</td>
					<td align="center"> 
						<%=i+1%>
					</td>
					<td align="center" id="td">
                                            <%if(ciclo.integrantes[i].esInterciclo==1){ %>
                                                <input type="hidden" name="idCliente<%=i%>" value="<%=ciclo.integrantes[i].idCliente%>">
                                            <%}%>
                                            <a href="#" onClick="consultaCliente(<%=ciclo.integrantes[i].idCliente%>)"><%=ciclo.integrantes[i].idCliente%></a>
					</td>
					<td align="center">
                                            <%if(ciclo.integrantes[i].esInterciclo==1){ %>
                                                <input type="hidden" name="idSolicitud<%=i%>" value="<%=ciclo.integrantes[i].idSolicitud%>">
                                            <%}%>
                                            <%=ciclo.integrantes[i].idSolicitud%>
					</td>
					<td align="center">
                                            <%if(ciclo.integrantes[i].esInterciclo==1){ %>
                                                <input type="hidden" name="nombre<%=i%>" id="nombre<%=i%>" value="<%=ciclo.integrantes[i].nombre%>">
                                            <%}%>                                            
                                            <%=ciclo.integrantes[i].nombre%>
					</td>
				
					<td align="right"> 
                                            <%if(ciclo.integrantes[i].esInterciclo==1){ %>
                                                <input type="hidden" name="monto<%=i%>" id="monto<%=i%>" value="<%=HTMLHelper.formatoMonto( ClientesUtil.calculaMontoSinComision( ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal ) )%>">
                                            <%}%> 
                                            <%=HTMLHelper.formatoMonto( ClientesUtil.calculaMontoSinComision( ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal ) )%>
					</td>

					<td align="right"> 
                                            <%if(ciclo.integrantes[i].esInterciclo==1){ %>
                                                <input type="hidden" name="montoComision<%=i%>" value="<%=ciclo.integrantes[i].monto%>">
						<input type="hidden" name="oPago<%=i%>" value="<%=ciclo.integrantes[i].ordenPago%>">
                                            <%}%> 
                                            <%=HTMLHelper.formatoMonto(ciclo.integrantes[i].montoConSeguro)%>
					</td>
                                        <td align="right">
						<%=HTMLHelper.formatoMonto(ciclo.integrantes[i].costoSeguro)%>
					</td>
					<td align="right">
                                            <%if(ciclo.integrantes[i].esInterciclo==1){ %>
                                                <input type="hidden" name="comision<%=i%>" value="<%=ciclo.integrantes[i].comision%>">
                                            <%}%>
                                            <%=CatalogoHelper.getDescripcionComision(ciclo.integrantes[i].comision, catComisionesGrupal)%>
					</td>
                                        <%if(ciclo.integrantes[i].esInterciclo==1){ %>
                                            <input type="hidden" name="calificacion<%=i%>" id="calificacion<%=i%>" value="<%=ciclo.integrantes[i].calificacion%>">
                                        <%}%>
					<%if ( ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_BUENA || ciclo.integrantes[i].calificacion == 0 ){ %>
                                                <%if(ciclo.integrantes[i].aceptaRegular == 3 || ciclo.integrantes[i].aceptaRegular == 4){%>
                                                    <td BGCOLOR="#33FF33"><center><font style="font-style: oblique;">&nbsp;A.E.</font></center></td>
                                                 <%}else if(ciclo.integrantes[i].aceptaRegular == ClientesConstants.AUTORIZACION_OTRA_FINANCIERA){%>
                                                    <td BGCOLOR="#00E100"><center><font style="font-style: oblique;">&nbsp;O.F.</font></center></td>
                                                <%}else{%>
                                                    <td BGCOLOR="#33FF33"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
                                                <%}
                                        }%>
			
					<% if ( ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_REGULAR ){ %>
                                            <%if(ciclo.integrantes[i].aceptaRegular>0&&sucursalAceptaRegular){%>
                                                <td BGCOLOR="#ffff00"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
                                            <%}else{%>
						<td BGCOLOR="#BDBDBA"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
                                            <%}%>
					<%} %>
					
					<% if ( ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_MALA ){ %>
						<td BGCOLOR="#FF0000"><center><b>&nbsp;</b></center></td>
					<%} %>
					
					<%if ( ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_NA ){ %>
						<td BGCOLOR="#33FF99"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
					<%} %>
					
					<%if ( ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_SIN_CONSULTA ){ sinConsulta=true;%>
						<td BGCOLOR="#FFEBCD"><center><font style="font-style: oblique;">Sin consulta</font></center></td>
					<%} %>
                                        <%if (ciclo.idCiclo > 0){
                                            medio = (ciclo.integrantes[i].medioCobro==1 ? "TAR" : "ODP");
                                        }else{
                                            medio = (ciclo.integrantes[i].medioDisp==1 ? "TAR" : "ODP");
                                        }%>
					<td align="center">
                                            <%if(ciclo.integrantes[i].esInterciclo==1){ %>
                                                <input type="hidden" name="medioCobro<%=i%>" value="<%=ciclo.integrantes[i].medioCobro%>">
                                            <%}%>
                                            <%=HTMLHelper.displayField(medio)%>
					</td>
					<td align="center">
                                            <%if(ciclo.integrantes[i].esInterciclo==1){ %>
                                               <input type="hidden" name="cheque<%=i%>" value="<%=ciclo.integrantes[i].numCheque%>">
                                            <%}%>
                                            <%=HTMLHelper.displayField(ciclo.integrantes[i].numCheque )%>
					</td>
					<td align="center">
                                            <input type="hidden" name="esInterciclo<%=i%>" value="<%=ciclo.integrantes[i].esInterciclo%>">
                                            <%=HTMLHelper.getDescripcion(catRoles, ciclo.integrantes[i].rol)%>
					</td>
                                        <td align="center">
                                            <%if(ciclo.integrantes[i].RenovacionDoc>0){%>
                                                <input type="checkbox" name="Doc<%=i%>" id="Doc<%=i%>" value="si" disabled 
                                                <%if(ciclo.integrantes[i].DocCompletos>0){%>
                                                    checked
                                                <%}%>
                                                >
                                            <%}%>
                                        </td>
				</tr>

<%
total += FormatUtil.redondeaMoneda(ClientesUtil.calculaMontoSinComision( ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal ));
totalConComision += ciclo.integrantes[i].monto;
totalConSegugoFinanciado += ciclo.integrantes[i].montoConSeguro;
totalCostoSeguro += ciclo.integrantes[i].costoSeguro;
}%>

				<tr>
					<td colspan="5" align="center">  
						Total 
					</td>
					<%if(validaCiclo == 1){%>
					<td>
						&nbsp;
					</td>
					<%}%>
					<td align="right">
						<input type="hidden" name="montoTotal" value="<%=total%>">
						<%=HTMLHelper.formatoMonto(total) %>
					</td>
					<td align="right">
						<input type="hidden" name="montoTotalConComision" value="<%=totalConComision%>">
						<%=HTMLHelper.formatoMonto(totalConSegugoFinanciado) %>
					</td>

					<td align="right">
						<%=HTMLHelper.formatoMonto(totalCostoSeguro) %>
					</td>
					<td>
						&nbsp; 
					</td>

					<td>
						&nbsp;
					</td>
					<td>
						&nbsp;
					</td>
					<td align="center">
						&nbsp;					
					</td>

				</tr>
			</table>
		</td>
	</tr>
        <tr>
            <td colspan="2" align="center">
                <table width="90%" border="2" cellpadding="0" bordercolor="#0040FF">
                    <tr>
                        <td align="center" id="td"><a href="#" onClick="muestraDocumento('CONTRATO')">Consulta contrato</a></td>
                        <td align="center" id="td"><a href="#" onClick="muestraDocumento('CARATULA')">Consulta car&aacute;tula</a></td>
                        <td align="center" id="td"><a href="#" onClick="muestraDocumento('AVISOPRIVACIDAD')">Consulta Aviso de privacidad</a></td>
                        <td align="center" id="td"><a href="#" onClick="muestraDocumento('CONTROLDEPAGOS')">Control de pagos</a></td>
                        <td align="center" id="td"><a href="generaFichasPagos.jsp?idOperacion=3&idCiclo=<%=idCiclo%>">Fichas de pago</a></td>
                        <td align="center" id="td"><a href="#" onClick="consultaEstadoCuentaGrupal()">Estado de cuenta</a></td>
                        <td align="center" id="td"><a href="#" onClick="consultaPagosComunales()"> Consulta pagos comunales</a></td>
                        <td align="center" id="td"><a href="#" onClick="muestraDocumento('HOJARESUMEN')">Hoja resumen</a></td>
                        <td align="center" id="td"><a href="#" onClick="muestraDocumento('REGLAMENTO')">Reglamento Interno</a></td>
                    </tr>
                </table>
            </td>
	</tr>
        <tr><td>&nbsp;</td></tr>
        <tr>
            <td colspan="2" align="center">
                <table width="60%" border="2" cellpadding="0">
                    <tr>
                        <td align="center"><strong><i>Dispersion</i></strong></td>
                        <td colspan="4" align="center"><strong><i>Documentaci&oacute;n</i></strong></td>
                    </tr>
                    <tr id="semana0" onClick="cambiarDisplay('semana0_link')" style="cursor: pointer;">
                        <td colspan="5"><i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Inicial</i></td>
                    </tr>
                    <tr id="semana0_link" onClick="cambiarDisplay('semana0_link')">
                        <td align="center" id="td" colspan="2"><a href="#" onClick="muestraDocumento('ORDENPAGO_0')">Consulta orden de pago</a></td>
                        <td align="center" id="td"><a href="#" onClick="muestraDocumento('PAGAREGRUPAL_0')">Consulta pagaré grupal</a></td>
                        <td align="center" id="td" colspan="2"><a href="#" onClick="muestraDocumento('PAGAREINDIVIDUAL_0')">Consulta pagaré individual</a></td>
                    </tr>
                    <%if(ciclo.estatusIC == ClientesConstants.CICLO_DESEMBOLSO || ciclo.estatusIC == ClientesConstants.CICLO_DISPERSADO){%>
                    <tr id="semana2" onClick="cambiarDisplay('semana2_link')"  style="cursor: pointer;">
                        <td colspan="5"><i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Semana 2</i></td>
                    </tr>
                    <tr><tr id="semana2_link" onClick="cambiarDisplay('semana2_link')" style="display:none">
                        <td align="center" id="td" colspan="2"><a href="#" onClick="muestraDocumento('ORDENPAGO_2')">Consulta orden de pago</a></td>
                        <td align="center" id="td"><a href="#" onClick="muestraDocumento('PAGAREGRUPAL_2')">Consulta pagaré grupal</a></td>
                        <td align="center" id="td"><a href="#" onClick="muestraDocumento('PAGAREINDIVIDUAL_2')">Consulta pagaré individual</a></td>
                        <td align="center" id="td"><a href="#" onClick="muestraDocumento('ADDENDUM_2')">Addendum</a></td>
                    </tr>
                    <%}%>
                    <%if(ciclo.estatusIC2 == ClientesConstants.CICLO_DESEMBOLSO || ciclo.estatusIC2 == ClientesConstants.CICLO_DISPERSADO){%>
                    <tr id="semana4" onClick="cambiarDisplay('semana4_link')"  style="cursor: pointer;">
                        <td colspan="5"><i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Semana 4</i></td>
                    </tr>
                    <tr id="semana4_link" onClick="cambiarDisplay('semana4_link')" style="display:none">
                        <td align="center" id="td" colspan="2"><a href="#" onClick="muestraDocumento('ORDENPAGO_4')">Consulta orden de pago</a></td>
                        <td align="center" id="td"><a href="#" onClick="muestraDocumento('PAGAREGRUPAL_4')">Consulta pagaré grupal</a></td>
                        <td align="center" id="td"><a href="#" onClick="muestraDocumento('PAGAREINDIVIDUAL_4')">Consulta pagaré individual</a></td>
                        <td align="center" id="td"><a href="#" onClick="muestraDocumento('ADDENDUM_4')">Addendum</a></td>
                    </tr>
                    <%}%>
                </table>
            </td>
        </tr>
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
        <%if((esMesaControl && estatusIC == ClientesConstants.CICLO_PROCESO && bitacoraCicloDao.getUsuarioAsignado(ciclo.idGrupo, ciclo.idCiclo).equals(request.getRemoteUser())) || (!esMesaControl && (estatusIC == ClientesConstants.CICLO_APERTURA || estatusIC == ClientesConstants.CICLO_PENDIENTE || estatusIC == ClientesConstants.CICLO_RECHAZADO)) || (request.isUserInRole("manager"))){ %>
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
                    <br><input type="button" id="botonGuardar" name="guardaCiclo" value="Guardar Ciclo" onClick="guardaCicloGrupal(<%=numIntegrantes + 1%>)">

                    <input type="button" id="botonRegresar" value="Regresar" onClick="regresaAGrupo()">
                </td>
            </tr>
            <%} else {%>
            <tr>
                <td align="center" colspan="2">
                    <input type="button" id="botonRegresar" value="Regresar" onClick="regresaAGrupo()">
                </td>
            </tr>
            <%}%>
	
</table>
</form>
		</td>
	</tr>
</table>
</body>
</html>
