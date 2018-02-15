<%-- 
    Document   : capturaCicloAutorizado
    Created on : 29/09/2015, 01:19:10 PM
    Author     : avillanueva
--%>

<%@page import="com.sicap.clientes.util.SegurosUtil"%>
<%@page import="com.sicap.clientes.util.Convertidor"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.sicap.clientes.util.FechasUtil"%>
<%@page import="com.sicap.clientes.dao.BitacoraCicloDAO"%>
<%@page import="com.sicap.clientes.dao.cartera.TablaAmortDAO"%>
<%@page import="com.sicap.clientes.util.GrupoUtil"%>
<%@page import="com.sicap.clientes.util.FormatUtil"%>
<%@page import="com.sicap.clientes.util.ClientesUtil"%>
<%@page import="com.sicap.clientes.helpers.GrupoHelper"%>
<%@page import="com.sicap.clientes.util.ClientesConstants"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@page import="com.sicap.clientes.vo.DireccionGenericaVO"%>
<%@page import="com.sicap.clientes.vo.GrupoVO"%>
<%@page import="com.sicap.clientes.vo.CicloGrupalVO"%>
<%@page import="com.sicap.clientes.vo.ArchivoAsociadoVO"%>
<%@page import="java.util.Vector"%>
<%@page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@page contentType="text/html" pageEncoding="iso-8859-1"%>
<%
Calendar cal = Calendar.getInstance();
Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
TreeMap catEstatus = null;
TreeMap catDiasReunon = CatalogoHelper.getCatalogoDiasReunion();
TreeMap catHorasReunion = CatalogoHelper.getCatalogoHorasReunion();
TreeMap catAperturador = new TreeMap();
TreeMap catEjecutivos = new TreeMap();
TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
TreeMap catCoordinadores = catSucursales;
TreeMap catTasasGrupal = CatalogoHelper.getCatalogoTasas(ClientesConstants.GRUPAL);
TreeMap catPlazos = CatalogoHelper.getCatalogoPlazos();
TreeMap catBancos = CatalogoHelper.getCatalogoBancosDispersion();
TreeMap catGarantia = CatalogoHelper.getCatalogoGarantiaGrupal();
TreeMap catFondeador = CatalogoHelper.getCatalogoFondeador();
TreeMap catDespachos = CatalogoHelper.getCatalogoDespachos();
TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
TreeMap catRoles = CatalogoHelper.getCatalogoRolesGrupo();
CicloGrupalVO ciclo = new CicloGrupalVO();
ArchivoAsociadoVO fldSeguro = null;
GrupoVO grupo = new GrupoVO();
DireccionGenericaVO direccion = new DireccionGenericaVO();
BitacoraCicloDAO bitacoraCicloDAO = new BitacoraCicloDAO();
String campSeguro = "";
String bloqueo = "";
String desembolsoOk = "NO";
String colorCalif = "";
String medio = "";
int tasa = 0;
int plazo = 0;
int fondeador = 0;
int despacho = 0;
int opcion = 0;
int numIntegrantes = 0;
int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
int idComision = 0;
boolean sinConsulta = false;
boolean isDesembolsadoFlag = false;
boolean ActivaGarantia = false;
boolean otraFinanciera =false; 
boolean esMesaControl = request.isUserInRole("ANALISIS_CREDITO");
boolean diainhabil = FechasUtil.esDiaInhabil( cal.getTime(), (Date[])session.getAttribute("INHABILES"));
double total = 0;
double totalConComision = 0;
// Agregamos para calcular la suma total del monto con seguro financiado
double totalConSegugoFinanciado = 0;
double totalCostoSeguro = 0;
// Listo para desembolso? omite la peticion de pedir ficha
boolean desembolsoSegFin;
        
if(session.getAttribute("GRUPO")!=null){
    grupo = (GrupoVO)session.getAttribute("GRUPO");
    tasa = GrupoUtil.asignaTasaGrupal(grupo, false);
    if(idCiclo!=0){
        ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
        if(ciclo!=null && ciclo.direccionReunion!=null)
            direccion = ciclo.direccionReunion;
        if(ciclo!=null && ciclo.integrantes!=null){
            numIntegrantes = ciclo.integrantes.length;
            plazo = ciclo.plazo;
            idComision = ciclo.comision;
        }
        if(ciclo.saldo != null) {
            fondeador = ciclo.getFondeador();
            despacho = ciclo.saldo.getNumDespacho();
        }
        for(int i=0; i<ciclo.integrantes.length; i++){
            if(ciclo.integrantes[i].seguro == 1)
                campSeguro = "si";
        }
        if(ciclo.archivosAsociados != null){
            for(int i=0; i<ciclo.archivosAsociados.length; i++){
                if(ciclo.archivosAsociados[i].tipo == ClientesConstants.ARCHIVO_TIPO_SEGURO)
                    fldSeguro = ciclo.archivosAsociados[i];
            }
        }
        isDesembolsadoFlag = GrupoHelper.isDesembolsado(ciclo.integrantes);
        opcion = GrupoHelper.muestraOpcionesGrupal(ciclo, desembolsoOk);
        catEstatus = CatalogoHelper.getCatalogoEstatusCiclo(false, ciclo.estatus);
        catEjecutivos = CatalogoHelper.getCatalogoEjecutivosComercial(grupo.sucursal, "A");
        catAperturador = CatalogoHelper.getCatalogoAperturador(grupo.sucursal, "A");
    }
}
otraFinanciera = CatalogoHelper.esSucursalPiloto3(grupo.sucursal);
    if(otraFinanciera){
        for (int i=0; i <ciclo.integrantes.length;i++){
            if(ciclo.integrantes[i].aceptaRegular == ClientesConstants.AUTORIZACION_OTRA_FINANCIERA){
                ActivaGarantia = true;
                break;
            }
        }
    }
String fechasDisp = "\"";
int diasuma = 1;
Date fechaDiaDos = ciclo.fechaDispersion;
fechasDisp += Convertidor.dateToString(ciclo.fechaDispersion,"d/M/yyyy");
do{     
    fechaDiaDos = FechasUtil.getRestarDias(ciclo.fechaDispersion, -diasuma);
    diasuma++;
}while(FechasUtil.esDiaInhabil(fechaDiaDos  ,(Date[])session.getAttribute("INHABILES")));

    fechasDisp += "\",\""+Convertidor.dateToString(fechaDiaDos,"d/M/yyyy");

fechasDisp += "\",\""+Convertidor.dateToString(FechasUtil.getRestarDias(ciclo.fechaDispersion, -7),"d/M/yyyy")+"\"";
// Para validar si requiere ficha de pago
desembolsoSegFin = SegurosUtil.validarNumIntegrantesSegFinanciado(ciclo.integrantes);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <title>Alta de Ciclo Grupal</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script language="Javascript" src="./js/functionsGrupal.js"></script>
        <script language="Javascript" src="./js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript">
            function paraDesembolso(){
                document.getElementById("btn_paraDesembolso").disabled = true;
                document.body.style.cursor = "wait";
		window.document.forma.command.value = 'peticionDesembolso';
                var fechaPart = window.document.forma.fechaDispersion.value.split('/');
		var fechaIngresada = new Date(fechaPart[2],fechaPart[1]-1,fechaPart[0]);
                if(fechaIngresada.getDay() != window.document.forma.diaReunion.value){
                    alert("El dia de reunion no coincide con la fecha seleccionada");
                    document.getElementById("btn_paraDesembolso").disabled = false;
                    document.body.style.cursor = "default";
                    document.getElementById("btn_paraDesembolso").disabled =false;
                    return false;
                }
                if(window.document.forma.idTasa.value == 0){
                    alert("No se puede solicitar desmbolso con tasa 0");
                    document.getElementById("btn_paraDesembolso").disabled = false;
                    document.body.style.cursor = "default";
                    return false;                   
                }
                if(window.document.forma.comentario.value == ""){
                    alert("Favor de ingresar un comentario");
                    document.getElementById("btn_paraDesembolso").disabled = false;
                    document.body.style.cursor = "default";
                    document.getElementById("btn_paraDesembolso").disabled =false;
                    return false;
                }
		window.document.forma.submit();
            }
            function guardaCicloGrupal(integrantes){
                document.getElementById("guardaCiclo").disabled = true;
                document.body.style.cursor = "wait";                
                window.document.forma.command.value = 'guardaCicloAutorizado';
                var fechaPart = window.document.forma.fechaDispersion.value.split('/');
		var fechaIngresada = new Date(fechaPart[2],fechaPart[1]-1,fechaPart[0]);
                if(fechaIngresada.getDay() != window.document.forma.diaReunion.value){
                    alert("El dia de reunion no coincide con la fecha seleccionada");
                    document.getElementById("guardaCiclo").disabled = false;
                    document.body.style.cursor = "default";
                    return false;
                }
                if (window.document.forma.fechaDesembolsoProg.value ==''){
                    alert("Se debe elegir una fecha para el desembolso");
                    document.getElementById("guardaCiclo").disabled = false;
                    document.body.style.cursor = "default";
                    return false;
                }
                if( !validaRolAut(integrantes) ){
                    document.getElementById("guardaCiclo").disabled = false;
                    document.body.style.cursor = "default";
                    return false;
                }
                window.document.forma.submit();
            }
            function desembolsaCicloGrupal(integrantes){
                document.getElementById("btn_desembolsar").disabled = true;
                document.body.style.cursor = "wait";
                window.document.forma.command.value = 'desembolsaCicloGrupal';
                var desembolsos = cuentaDesembolsos(integrantes);
                var fecha = new Date();
		var milisegundos = parseInt(1*24*60*60*1000);
		var fechaPart = window.document.forma.fechaDesembolsoProg.value.split('/');
		var fechaIngresada = new Date(fechaPart[2],fechaPart[1]-1,fechaPart[0]);
		var tiempo = fecha.getTime();
		var total = fecha.setTime(parseInt(tiempo-milisegundos));
		var fechaActual = new Date(total);
		if( !minimoIntegrantes(<%=grupo.idOperacion%>, <%=ciclo.idTipoCiclo%>, integrantes, <%=esMesaControl%>) ){
                    document.getElementById("btn_desembolsar").disabled = false;
                    document.body.style.cursor = "default";
                    return false;
                }
                if (window.document.forma.fechaDesembolsoProg.value ==''){
                    alert("Se debe elegir una fecha para el desembolso");
                    document.getElementById("btn_desembolsar").disabled = false;
                    document.body.style.cursor = "default";
                    return false;
                }
                if(window.document.forma.idTasa.value == 0){
                    alert("No Se puede desmbolsar con tasa 0");
                    document.getElementById("btn_desembolsar").disabled = false;
                    document.body.style.cursor = "default";
                    return false;                   
                }
                if( !validaRol(integrantes) ){
                    document.getElementById("btn_desembolsar").disabled = false;
                    document.body.style.cursor = "default";
                    return false;
                }
                if(fechaActual > fechaIngresada){
                    alert("La fecha de desembolso debe ser mayor a la fecha actual");
                    document.getElementById("btn_desembolsar").disabled = false;
                    document.body.style.cursor = "default";
                    return false;
                }
                if(!esFechaValida(window.document.forma.fechaDesembolsoProg,false) ){
                    alert("La Fecha de Realización de la consulta es inválida");
                    document.getElementById("btn_desembolsar").disabled = false;
                    document.body.style.cursor = "default";
                    return false;
                }
                if(window.document.forma.fechaDesembolsoProg.value != window.document.forma.fechaDispersion.value){
                    if(!confirm("Las Fechas Desembolso y Dispersion son diferente. ¿Desea continuar?")){
                        document.getElementById("btn_desembolsar").disabled = false;
                        document.body.style.cursor = "default";
                        return false;
                    }
                }
                if(fechaIngresada.getDay() != window.document.forma.diaReunion.value){
                    alert("El dia de reunion no coincide con la fecha seleccionada");
                    document.getElementById("btn_desembolsar").disabled = false;
                    document.body.style.cursor = "default";
                    return false;
                }
                if(window.document.forma.fondeador.value == 0){
                    alert("Debe de seleccionar un Fondeador");
                    document.getElementById("btn_desembolsar").disabled = false;
                    document.body.style.cursor = "default";
                    return false;
                }
                if(<%=diainhabil%>){
                    alert("El desembolso no puede efectuarse debido a que es fecha de día inhábil");
                    document.getElementById("btn_desembolsar").disabled = false;
                    document.body.style.cursor = "default";
                    return false;
                }
                window.document.forma.submit();
            }
            function confirmaDesembolsaCicloGrupal(integrantes){
                document.getElementById("btn_confirmaDesembolsar").disabled = true;
                window.document.forma.command.value = 'confirmaDesembolsaCicloGrupal';
                var desembolsos = cuentaDesembolsos(integrantes);
                var fechaPart = window.document.forma.fechaConfirmacion.value.split('/');
		var fechaIngresada = new Date(fechaPart[2],fechaPart[1]-1,fechaPart[0]);
                if(!minimoIntegrantes(<%=grupo.idOperacion%>, <%=ciclo.idTipoCiclo%>, integrantes, <%=esMesaControl%>)){
                    document.getElementById("btn_confirmaDesembolsar").disabled = false;
                    return false;
                }
                if(!validaRol(integrantes)){
                    document.getElementById("btn_confirmaDesembolsar").disabled = false;
                    return false;
                }
                if(window.document.forma.fechaConfirmacion.value ==''){
                    alert("Se debe elegir una fecha para el desembolso");
                    document.getElementById("btn_desembolsar").disabled = false;
                    return false;
                }
                if(window.document.forma.idTasa.value == 0){
                    alert("No Se puede confirmar desmbolso con tasa 0");
                    document.getElementById("btn_confirmaDesembolsar").disabled = false;
                    document.body.style.cursor = "default";
                    return false;                   
                }
                if(window.document.forma.plazo.value==0){
                    alert('Debe indicar el tipo de plazo');
                    document.getElementById("guardaCiclo").disabled = false;
                    return false;
                }
                if(window.document.forma.fechaConfirmacion.value != window.document.forma.fechaDispersion.value){
                    if(!confirm("Las Fechas Desembolso y Dispersion son diferente. ¿Desea continuar?")){
                        document.getElementById("btn_confirmaDesembolsar").disabled = false;
                        return false;
                    }
                }
                if(fechaIngresada.getDay() != window.document.forma.diaReunion.value){
                    alert("El dia de reunion no coincide con la fecha seleccionada");
                    document.getElementById("btn_confirmaDesembolsar").disabled = false;
                    return false;
                }
                if (window.document.forma.fondeador.value == 0){
                    alert("Debe de seleccionar un Fondeador");
                    document.getElementById("btn_confirmaDesembolsar").disabled = false;
                    return false;
                }
                if(<%=diainhabil%>){
                    alert("El desembolso no puede efectuarse debido a que es fecha de día inhábil");
                    document.getElementById("btn_confirmaDesembolsar").disabled = false;
                    return false;
                }
                res = confirm('La información que está apunto de guardar no podrá ser modificada. ¿Esta seguro de que desea registrar el ciclo con los datos actuales?');
                if ( !res ){
                    document.getElementById("btn_confirmaDesembolsar").disabled = false;
                    return res;
                }
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
            function desembolsaCicloGrupalProg(){
                alert("Por favor ingresar la fecha del desembolso y posteriormente pulsar el boton Desembolsar")
                $("#ocultaFechaDesembolso").css("display", "block");
                document.getElementById("boton").disabled = false;
                return false;
            }
            function regresaAGrupo(){
                document.getElementById("boton").disabled = true;
                window.document.forma.command.value = 'consultaDetalleGrupo';
		window.document.forma.action="controller";
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
            function validaFechaDis(){
                var fechaPart = window.document.forma.fechaConfirmacion.value.split('/');
		var fechaIngresada = new Date(fechaPart[2],fechaPart[1]-1,fechaPart[0]);
                var fecha = new Date();
                fecha.setHours(0);
                fecha.setMinutes(0);
                fecha.setSeconds(0);
                fecha.setMilliseconds(0);
                if(fechaIngresada < fecha){
                    document.getElementById("btn_confirmaDesembolsar").disabled = true;
                    alert("La fecha seleccionada es menor a la actual. Seleccione una mayor.");
                    return false;
                } else{
                    document.getElementById("btn_confirmaDesembolsar").disabled = false;
                    return true;
                }
            }
            function habilitada(date){
               var fechasHabilitadas = [<%=fechasDisp%>]; //Formato de fecha: [];
                dmy = date.getDate() + "/" + (date.getMonth()+1) + "/" + date.getFullYear();
                if ($.inArray(dmy, fechasHabilitadas) != -1) {
                        return true;
                } else {
                        return false;
                }
            }
            function muestraDocumento(tipo){
                window.document.forma.command.value='muestraDocumento';
                window.document.forma.tipo.value=tipo;
                window.document.forma.submit();
            }
            function consultaEstadoCuentaGrupal(){
                document.getElementById("td").disabled = true;
                window.document.forma.action="controller";
                window.document.forma.command.value="consultaEstadoCuentaGrupal";
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
            function consultaPagosComunales(){
                document.getElementById("td").disabled = true;
                window.document.forma.command.value='validarPagoComunal';
                window.document.forma.submit();
            }
            function consultaCliente(idCliente){
                document.getElementById("td").disabled = true;
                window.document.forma.command.value='consultaCliente';
                window.document.forma.idCliente.value=idCliente;
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
        function agregaComentario(){
           if(window.document.forma.comentario.value==""){
               alert("No has ingresado comentarios")  
               return false;
           } else if(!confirm("¿Deseas agregar el comentario?")){
               return false;
           } else {
               window.document.getElementById("boton_agrega_comentario").disabled=true;
               window.document.forma.command.value='agregaComentarioCicloAut';
               window.document.forma.submit();
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
            
            function ayudaHistorialComentarios(numEquipo, numCiclo) {
                params = "?command=historialComentarios" + "&idGrupo=" + numEquipo + "&idCiclo=" + numCiclo;
                url = "/CEC/controller";
                abreVentana(url + params, 'scrollbars=yes', 1000, 250, true, 0, 0);
            }
            
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <jsp:include page="header.jsp" flush="true"/>
        <table border="0" width="100%">
            <tr>
                <td align="center">
                    <h3>Alta de Ciclo Grupal</h3>
                    <%=HTMLHelper.displayNotifications(notificaciones)%>
                    <br>
                    <form name="forma" action="controller" method="post">
                        <!--<input type="hidden" name="command" value="guardaCicloGrupal">-->
                        <input type="hidden" name="command" value="">
                        <input type="hidden" name="idGrupo" value="<%=grupo.idGrupo%>">
                        <input type="hidden" name="idCiclo" value="<%=ciclo.idCiclo%>">
                        <input type="hidden" name="idComision" value="<%=idComision%>">
                        <input type="hidden" name="idColonia" value="<%=HTMLHelper.displayField(direccion.idColonia)%>">
                        <input type="hidden" name="asentamientoCP" value="<%=HTMLHelper.displayField(direccion.asentamiento_cp)%>">
                        <input type="hidden" name="saldoT24" value="<%=ciclo.estatusT24%>">
                        <input type="hidden" name="idSucursal" value="<%=grupo.sucursal%>">
                        <input type="hidden" name="numIntegrantes" value="<%=ciclo.integrantes.length%>">
                        <input type="hidden" name="tipo" value="">
                        <input type="hidden" name="idCliente" value="">
                        <table width="100%" border="0" cellpadding="0">
                            <tr>
                                <td width="50%" align="right">N&uacute;mero del grupo </td>
                                <td width="50%">
                                    <input type="text" name="idGrupo" size="10" maxlength="10" value="<%=HTMLHelper.displayField(grupo.idGrupo)%>" readonly="readonly" class="soloLectura">
                                </td>
                                <tr>
                                    <td width="50%" align="right">Nombre del grupo </td>
                                    <td width="50%">
                                        <input type="text" name="nombre" size="45" maxlength="150" value="<%=HTMLHelper.displayField(grupo.nombre)%>" readonly="readonly" class="soloLectura">
                                    </td>
                                </tr>
                                <tr>
                                    <td width="50%" align="right">N&uacute;mero de ciclo</td>
                                    <td width="50%">
                                        <input type="text" name="idCicloRead" size="10" maxlength="10" value="<%=HTMLHelper.displayField(ciclo.idCiclo)%>" readonly="readonly" class="soloLectura">
                                    </td>
                                </tr>
                                <%if(ciclo.estatus != ClientesConstants.CICLO_AUTORIZADO || !esMesaControl){%>
                                <tr>
                                    <td width="100%" align="center" colspan="2"><a href="#" onClick="ayudaCodigoPostal()">Ayuda CP</a></td>
                                </tr>
                                <%}%>
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
                                        <input type="text" name="calle" size="30" maxlength="60" value="<%=HTMLHelper.displayField(direccion.calle)%>" <%if(ciclo.estatus == ClientesConstants.CICLO_AUTORIZADO && esMesaControl){%> readonly="readonly" class="soloLectura"<%}%>>
                                    </td>
                                </tr>
                                
                                <tr>
                                    <td width="50%" align="right">N&uacute;mero exterior</td>
                                    <td width="50%">
                                        <input type="text" name="numeroExterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(direccion.numeroExterior)%>"<%if(ciclo.estatus == ClientesConstants.CICLO_AUTORIZADO &&esMesaControl){%> readonly="readonly" class="soloLectura"<%}%>>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="50%" align="right">N&uacute;mero interior</td>
                                    <td width="50%">
                                        <input type="text" name="numeroInterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(direccion.numeroInterior)%>"<%if(ciclo.estatus == ClientesConstants.CICLO_AUTORIZADO &&esMesaControl){%> readonly="readonly" class="soloLectura"<%}%>>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="50%" align="right">Estatus</td>
                                    <td width="50%">
                                        <%if(ciclo.estatus == ClientesConstants.CICLO_AUTORIZADO && esMesaControl){%> 
                                        <select name="estatus" id="estatus" size="1">
                                            <%=HTMLHelper.displayCombo(catEstatus, ciclo.estatus)%>
                                        </select>
                                        <%}else{%> 
                                        <select name="estatusdis" id="estatus" size="1" disabled>
                                            <%=HTMLHelper.displayCombo(catEstatus, ciclo.estatus)%>
                                        </select>
                                        <input type="hidden" name="estatus" value="<%=ciclo.estatus%>">
                                        <%}%>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="50%" align="right">D&iacute;a de reuni&oacute;n del grupo</td>
                                    <td width="50%">
                                        <%if(ciclo.estatus == ClientesConstants.CICLO_AUTORIZADO && esMesaControl){%> 
                                        <select name="diaReuniondis" size="1" disabled>
                                            <%=HTMLHelper.displayCombo(catDiasReunon, ciclo.diaReunion)%>
                                        </select>
                                        <input type="hidden" name="diaReunion" value="<%=ciclo.diaReunion%>">
                                        <%}else{%> 
                                        <select name="diaReunion" size="1">
                                            <%=HTMLHelper.displayCombo(catDiasReunon, ciclo.diaReunion)%>
                                        </select>
                                        <%}%>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="50%" align="right">Hora de reuni&oacute;n del grupo</td>
                                    <td width="50%"> 
                                        <%if(ciclo.estatus == ClientesConstants.CICLO_AUTORIZADO && esMesaControl){%> 
                                        <select name="horaReuniondis" size="1" disabled>
                                            <%=HTMLHelper.displayCombo(catHorasReunion, ciclo.horaReunion)%>
                                        </select>
                                        <input type="hidden" name="horaReunion" value="<%=ciclo.horaReunion%>">
                                        <%}else{%> 
                                        <select name="horaReunion" size="1">
                                            <%=HTMLHelper.displayCombo(catHorasReunion, ciclo.horaReunion)%>
                                        </select>
                                        <%}%>
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
                                         <%if(ciclo.estatus == ClientesConstants.CICLO_AUTORIZADO && esMesaControl){%>
                                        <select name="asesordis" size="1" disabled>
                                            <option value="0">Seleccione...</option>
                                            <%=HTMLHelper.displayCombo(catEjecutivos, ciclo.asesor)%>
                                        </select>
                                         <input type="hidden" name="asesor" value="<%=ciclo.asesor%>">
                                        <%}else{%>
                                        <select name="asesor" size="1">
                                            <option value="0">Seleccione...</option>
                                            <%=HTMLHelper.displayCombo(catEjecutivos, ciclo.asesor)%>
                                        </select>
                                        <%}%>
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
                                        <input type="text" name="multaRetraso" size="10" maxlength="8" value="<%=HTMLHelper.formatoMonto(ciclo.multaRetraso)%>"<%if(ciclo.estatus == ClientesConstants.CICLO_AUTORIZADO && esMesaControl){%> readonly="readonly" class="soloLectura"<%}%> >
                                    </td>
                                </tr>
                                <tr>
                                    <td width="50%" align="right">Multa por falta</td>
                                    <td width="50%">  
                                        <input type="text" name="multaFalta" size="10" maxlength="8" value="<%=HTMLHelper.formatoMonto(ciclo.multaFalta)%>" <%if(ciclo.estatus == ClientesConstants.CICLO_AUTORIZADO && esMesaControl){%> readonly="readonly" class="soloLectura"<%}%>> 
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
                                    <td><%if(ciclo.estatus == ClientesConstants.CICLO_AUTORIZADO && esMesaControl){%>
                                        <select name="plazo" id="plazo" size="1" onKeyPress="return submitenter(this,event)">
                                            <%= HTMLHelper.displayCombo(catPlazos, plazo)%>
                                        </select>
                                        <%}else{%>
                                        <select name="plazodis" id="plazodis" size="1" disabled>
                                            <%= HTMLHelper.displayCombo(catPlazos, plazo)%>
                                        </select>
                                        <input type="hidden" name="plazo" value="<%=plazo%>">
                                        <%}%>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="50%" align="right">Fecha de Dispersi&oacute;n</td>
                                    <%if(esMesaControl){%>
                                    <td><input type="text" name="fechaDispersion" id="fechaDisp" size="10%" maxlength="10" value="<%=HTMLHelper.displayField(ciclo.fechaDispersion)%>" readonly="readonly" class="soloLectura">
                                    <%}else{%> 
                                    <td><input type="text" name="fechaDispersion" id="fechaDispersionAut" size="10%" maxlength="10" value="<%=HTMLHelper.displayField(ciclo.fechaDispersion)%>" readonly="readonly">
                                    <%}%>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="50%" align="right">Dispersar por</td>
                                    <td width="50%">  
                                         <%if(esMesaControl){%>
                                        <select name="bancoDispersion" size="1">
                                            <%=HTMLHelper.displayCombo(catBancos, ciclo.bancoDispersion)%>
                                        </select>
                                        <%}else{%>
                                        <select name="bancoDispersiondis" size="1" disabled>
                                            <%=HTMLHelper.displayCombo(catBancos, ciclo.bancoDispersion)%>
                                            <input type="hidden" name="bancoDispersion" value="<%=ciclo.bancoDispersion%>">
                                        </select>
                                        <%}%>
                                    </td>
                                </tr>
                                <%if(request.isUserInRole("CAPTURA_GARANTIA") && CatalogoHelper.esEquipoAutorizadoGarantia(grupo.idGrupo)|| (ActivaGarantia && request.isUserInRole("ANALISIS_CREDITO"))) {
                                    if (ciclo.estatus == 1 || ciclo.estatus == 2) {
                                        bloqueo = "disabled";
                                    }%>
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
                                    <%if (request.isUserInRole("ANALISIS_CREDITO") && fondeador == ClientesConstants.ID_FONDEADOR_FOMMUR) {%>
                                    <td><select name="fondeador" id="fondeador" size="1" onKeyPress="return submitenter(this,event)">
                                            <%= HTMLHelper.displayCombo(catFondeador, fondeador)%>
                                        </select>
                                    </td>
                                    <%} else {%>
                                    <td><select name="fondeador" id="fondeador" size="1" onKeyPress="return submitenter(this,event)" onfocus="this.oldvalue=this.value;this.blur();" onchange="this.value=this.oldvalue;">
                                            <%= HTMLHelper.displayCombo(catFondeador, fondeador)%>
                                        </select>
                                    </td>
                                    <%}%>
                                </tr>
                                <tr>
                                    <td width="50%" align="right">Despacho</td>
                                    <%if (request.isUserInRole("ADM_PAGOS_MANAGER")) {%>
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
                                                <td align="center">&nbsp;</td>
                                                <td align="center">No.</td>
                                                <td align="center">Cliente</td>
                                                <td align="center">Solicitud</td>
                                                <td align="center">Nombre</td>
                                                <td align="center">Monto a desembolsar</td>
                                                <td align="center">Monto de Seguro</td>
                                                <td align="center">Monto con Seguro Financiado</td>
                                                <td align="center">Comisi&oacute;n</td>
                                                <td align="center">Calificaci&oacute;n</td>
                                                <td align="center">Medio Cobro</td>
                                                <td align="center">Referencia Cobro</td>
                                                <td align="center">Rol</td>
                                <%if(opcion == 0){%>
                                                <td align="center">Seguro</td>
                                                <td align="center">Actividad</td>
                                                <td align="center">Activo en</td>
                                <%}%>
                                            </tr>
                                <%for (int i = 0; ciclo.integrantes != null && i < ciclo.integrantes.length; i++) {
                                    ciclo.integrantes[i].montoConSeguro = ciclo.integrantes[i].monto + ciclo.integrantes[i].costoSeguro; %>
                                            <tr>
                                                <%if(esMesaControl){%> 
                                                    <td align="center"><%=HTMLHelper.displayCheck("desembolso" + i, true)%></td>
                                                <%}else{%>
                                                    <td align="center"> <input type="checkbox" name="<%="desembol"+i%>" id="<%="desembol"+i%>" disabled checked >
                                                    <input type="hidden" name="<%="desembolso"+i%>" id="<%="desembolso"+i%>" value="si">
                                                <%}%>
                                                <td align="center"><%=i + 1%></td>
                                                <td align="center" id="td"><a href="#" onClick="consultaCliente(<%=ciclo.integrantes[i].idCliente%>)"><%=ciclo.integrantes[i].idCliente%></a></td>
                                                <input type="hidden" name="idCliente<%=i%>" value="<%=ciclo.integrantes[i].idCliente%>">
                                                <td align="center"><%=ciclo.integrantes[i].idSolicitud%></td>
                                                <input type="hidden" name="idSolicitud<%=i%>" value="<%=ciclo.integrantes[i].idSolicitud%>">
                                                <td align="center"><%=ciclo.integrantes[i].nombre%></td>
                                                <input type="hidden" name="nombre<%=i%>" id="nombre<%=i%>" value="<%=ciclo.integrantes[i].nombre%>">
                                    <%if (request.isUserInRole("ANALISIS_CREDITO") && ciclo.desembolsado == 0) {%>
                                                <td align="center"><input type="text" name="monto<%=i%>" id="monto<%=i%>" value="<%=HTMLHelper.formatoMonto(ClientesUtil.calculaMontoSinComision(ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal))%>" size="8" maxlength="8" onchange="validaMonto(this.value)"></td>
                                    <%} else {%>
                                                <td align="right"><%=HTMLHelper.formatoMonto(ClientesUtil.calculaMontoSinComision(ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal))%></td>
                                                <input type="hidden" name="monto<%=i%>" id="monto<%=i%>" value="<%=HTMLHelper.formatoMonto(ClientesUtil.calculaMontoSinComision(ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal))%>">
                                    <%}%>
                                                <td align="right"><%=HTMLHelper.formatoMonto(ciclo.integrantes[i].costoSeguro)%></td>
                                                <td align="right"><%=HTMLHelper.formatoMonto(ciclo.integrantes[i].montoConSeguro)%></td>
                                                <input type="hidden" name="montoComision<%=i%>" value="<%=ciclo.integrantes[i].monto%>">
                                                <input type="hidden" name="oPago<%=i%>" value="<%=ciclo.integrantes[i].ordenPago%>">
                                                <td align="right"><%=CatalogoHelper.getDescripcionComision(ciclo.integrantes[i].comision, catComisionesGrupal)%></td>
                                                <input type="hidden" name="comision<%=i%>" value="<%=ciclo.integrantes[i].comision%>">
                                    <%if (ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_BUENA || ciclo.integrantes[i].calificacion == 0) {
                                        colorCalif = "#33FF33";
                                    }else if(ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_REGULAR) {
                                        if (ciclo.integrantes[i].aceptaRegular>0)
                                            colorCalif = "#ffff00";
                                        else
                                            colorCalif = "#BDBDBA";
                                    }else if(ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_MALA) {
                                        colorCalif = "#FF0000";
                                    }else if(ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_NA) {
                                        colorCalif = "#33FF99";
                                    }else if(ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_SIN_CONSULTA) {
                                        colorCalif = "#FFEBCD";
                                        sinConsulta = true;
                                    }%>
                                    
                                    <%if (ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_BUENA && (ciclo.integrantes[i].aceptaRegular == 3 || ciclo.integrantes[i].aceptaRegular == 4)){%>
                                                <td bgcolor="<%=colorCalif%>"><center><font style="font-style: oblique;">&nbsp;A.E.</font></center></td>
                                     <%}else if(ciclo.integrantes[i].aceptaRegular == ClientesConstants.AUTORIZACION_OTRA_FINANCIERA && otraFinanciera){%>
                                                    <td BGCOLOR="#00E100"><center><font style="font-style: oblique;">&nbsp;O.F.</font></center></td>                                             
                                    <%}else{%>
                                                <td bgcolor="<%=colorCalif%>"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
                                    <%}%>
                                                <input type="hidden" name="calificacion<%=i%>" id="calificacion<%=i%>" value="<%=ciclo.integrantes[i].calificacion%>">
                                                <input type="hidden" name="aceptaRegular<%=i%>" id="aceptaRegular<%=i%>" value="<%=ciclo.integrantes[i].aceptaRegular%>">
                                    <%if (ciclo.idCiclo > 0) {
                                        medio = (ciclo.integrantes[i].medioCobro == 1 ? "TAR" : "ODP");
                                    } else {
                                        medio = (ciclo.integrantes[i].medioDisp == 1 ? "TAR" : "ODP");
                                    }%>
                                                <td align="center"><%=HTMLHelper.displayField(medio)%></td>
                                                <input type="hidden" name="medioCobro<%=i%>" value="<%=ciclo.integrantes[i].medioCobro%>">
                                                <td align="center"><%=HTMLHelper.displayField(ciclo.integrantes[i].numCheque)%></td>
                                                <input type="hidden" name="cheque<%=i%>" value="<%=ciclo.integrantes[i].numCheque%>">
                                                <td align="center">
                                                    <%if(ciclo.estatus == ClientesConstants.CICLO_AUTORIZADO && esMesaControl){%>
                                                    <select name="rol<%=i%>" id="rol<%=i%>" size="1" value="<%=ciclo.integrantes[i].rol%>"  disabled>
                                                        <%=HTMLHelper.displayCombo(catRoles, ciclo.integrantes[i].rol)%>
                                                    </select>
                                                    <input type="hidden" name="<%="rol"+i%>" value="<%=ciclo.integrantes[i].rol%>">
                                                    <%}else {%>
                                                    <select name="rol<%=i%>" id="rol<%=i%>" size="1" value="<%=ciclo.integrantes[i].rol%>">
                                                        <%=HTMLHelper.displayCombo(catRoles, ciclo.integrantes[i].rol)%>
                                                    </select>
                                                    <%}%>
                                                </td>
                                    <%if(opcion == 0){%>
                                                <td align="center"><%=(ciclo.integrantes[i].seguro > 0 ? "OK" : "--")%></td>
                                                <input type="hidden" name="conseguro<%=i%>" value="<%=ciclo.integrantes[i].seguro%>">
                                                <td align="center"><%=(ciclo.integrantes[i].empleo != 0 ? "OK" : "--")%></td>
                                                <input type="hidden" name="conempleo<%=i%>" value="<%=ciclo.integrantes[i].empleo%>">
                                                <td align="center"><%=(ciclo.integrantes[i].grupo != "" ? HTMLHelper.displayField(ciclo.integrantes[i].grupo) : "--")%></td>
                                                <input type="hidden" name="activoen<%=i%>" value="<%=ciclo.integrantes[i].grupo%>">
                                    <%}%>
                                            </tr>
                                    <%
                                    total += FormatUtil.redondeaMoneda(ClientesUtil.calculaMontoSinComision(ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal));
                                    totalConComision += ciclo.integrantes[i].monto;
                                    totalConSegugoFinanciado += ciclo.integrantes[i].montoConSeguro;
                                    totalCostoSeguro += ciclo.integrantes[i].costoSeguro;
                                    numIntegrantes = i;
                                }%>
                                            <tr>
                                                <td colspan="5" align="center">Total</td>
                                                <td align="right"><%=HTMLHelper.formatoMonto(total)%></td>
                                                <input type="hidden" name="montoTotal" value="<%=total%>">
                                                <td align="right"><%=HTMLHelper.formatoMonto(totalCostoSeguro)%></td>
                                                <input type="hidden" name="montoTotalCostoSeguro" value="<%=totalCostoSeguro%>">
                                                <td align="right"><%=HTMLHelper.formatoMonto(totalConSegugoFinanciado)%></td>
                                                <input type="hidden" name="montoTotalConComision" value="<%=totalConComision%>">
                                                <td>&nbsp;</td>
                                                <td>&nbsp;</td>
                                                <td align="center">&nbsp;</td>
                                <%if(opcion == 0){%>
                                                <td align="center">&nbsp;</td>
                                                <td align="center">&nbsp;</td>
                                                <td align="center">&nbsp;</td>
                                <%}%>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <%if(opcion > 0){%>
                                <tr><td>&nbsp;</td></tr>
                                <tr>
                                    <td colspan="2">
                                        <table width="100%" border="2" cellpadding="0" bordercolor="#0040FF">
                                            <tr>
                                    <%if(opcion > 1){%>
                                                <td align="center" id="td"><a href="#" onClick="muestraDocumento('ORDENPAGO_0')">Consulta orden de pago</a></td>
                                    <%}%>
                                                <td align="center" id="td"><a href="#" onClick="muestraDocumento('CONTRATO')">Consulta contrato</a></td>
                                                <td align="center" id="td"><a href="#" onClick="muestraDocumento('CARATULA')">Consulta car&aacute;tula</a></td>
                                                <td align="center" id="td"><a href="#" onClick="muestraDocumento('AVISOPRIVACIDAD')">Consulta Aviso de privacidad</a></td>
                                                <td align="center" id="td"><a href="#" onClick="muestraDocumento('PAGAREGRUPAL_0')">Consulta pagar&eacute; grupal</a></td>
                                                <td align="center" id="td"><a href="#" onClick="muestraDocumento('PAGAREINDIVIDUAL_0')">Consulta pagar&eacute; individual</a></td>
                                                <td align="center" id="td"><a href="#" onClick="muestraDocumento('CONTROLDEPAGOS')">Control de pagos</a></td>
                                                <td align="center" id="td"><a href="generaFichasPagos.jsp?idOperacion=3&idCiclo=<%=idCiclo%>">Fichas de pago</a></td>
                                                <td align="center" id="td"><a href="#" onClick="muestraDocumento('HOJARESUMEN')">Hoja resumen</a></td>
                                                <td align="center" id="td"><a href="#" onClick="muestraDocumento('REGLAMENTO')">Reglamento Interno</a></td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <%} else {
                                    if (ciclo.idCiclo > 0){%>
                                <tr>
                                    <td align="center" colspan="2">
                                        <a href="generaFichasGarantia.jsp?idOperacion=3&idCiclo=<%=idCiclo%>">Ficha pago en garant&iacute;a</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                    </td>
                                </tr>
                                    <%}
                                }%>
                            </tr>
                            <tr>
                                <td colspan="2" align="center">
                                    <br>
                                    <table border="1" width="80%">
                                        <tr>
                                            <td colspan="2" align="center"><textarea name="ultimoComentario" rows="1" cols="100" readonly="readonly" class="soloLectura"><%=bitacoraCicloDAO.getUltimoComentario(grupo.idGrupo, ciclo.idCiclo) %></textarea>
                                                <a href="#" onClick="ayudaHistorialComentarios(<%=grupo.idGrupo%>, <%=ciclo.idCiclo%>)"><img name="imagen" alt="Historial" src="images/history_icon.png"></a>
                                            </td>                        
                                        </tr>                    
                                    </table>
                                </td>
                            </tr>
                                <%if(ciclo.estatus==10&&ciclo.desembolsado==0){%>
                            <tr>
                                <td colspan="2" align="center">
                                    <h4 id="expanderHead" style="cursor:pointer;">Agregar Comentario <span id="expanderSign">+</span></h4>
                                    <div id="expanderContent" style="display:none">
                                        <textarea name="comentario" rows="5" cols="100" placeholder="Escribe tu comentario..."></textarea>
                                        <br>
                                        <br>
                                        <input type="button" id="boton_agrega_comentario" value="Agrega Comentario" onClick="agregaComentario()"/>
                                    </div>
                                </td>
                            </tr>
                                <%}%>
                            <tr>
                                <td align="center" colspan="2"><br>
                                <%if(fldSeguro == null && campSeguro.equals("si") && !desembolsoSegFin){%>
                                    <input type="button" value="Guarda Ficha" onClick="ayudaCargaArchivo()"/>
                                    <input type="hidden" name="tipoArchivo" value="fileFicha"/>
                                    <input type="hidden" name="conSeguro" value="1"/>
                                <%} else if(opcion > 0){%>
                                    <input type="button" value="Documentaci&oacute;n" onClick="ayudaCargaArchivo()"/>
                                    <input type="hidden" name="tipoArchivo" value="fileDocumentacion"/>
                                    <input type="hidden" name="conSeguro" value="0"/>
                                <%} else if(ciclo.estatus == ClientesConstants.CICLO_AUTORIZADO){%>
                                    <input type="button" id="btn_paraDesembolso" value="Petici&oacute;n Desembolso" onClick="paraDesembolso()()">
                                <%}%>
                                    <input type="button" id="boton" value="Regresar" onClick="regresaAGrupo()">
                                    <center id="ocultaFechaDesembolso"><br><input type="text" name="fechaDesembolsoProg" id="fechaDesembolsoProg" size="10" maxlength="10" value="<%=HTMLHelper.displayField(new java.util.Date())%>">(dd/mm/aaaa)</center>
                                </td>
                            </tr>
                                <%if(ciclo.estatus>=ClientesConstants.CICLO_AUTORIZADO){%>
                            <tr>
                                <td align="center" colspan="2"><br>
                                    <%if(ciclo.estatus==ClientesConstants.CICLO_AUTORIZADO){%>
                                    <input type="button" name="guardaCiclo" id="guardaCiclo" value="Guarda Ciclo" onClick="guardaCicloGrupal(<%=numIntegrantes+1%>)">
                                    <%}else if(ciclo.estatus==ClientesConstants.CICLO_PARADESEMBOLSAR && esMesaControl) {
                                        if(!isDesembolsadoFlag){
                                            bloqueo = "";
                                        }else{
                                            bloqueo = "disabled";
                                        }%>
                                    <input type="button" id="btn_desembolsar" value="Desembolsar" onClick="desembolsaCicloGrupal(<%=numIntegrantes+1%>)" <%=bloqueo%>>
                                    <input type="button" id="btn_desembolso_prog" value="Desembolso Programado" onClick="desembolsaCicloGrupalProg()" <%=bloqueo%>>
                                    <%}else if(ciclo.estatus==ClientesConstants.CICLO_DESEMBOLSO&&esMesaControl){%>
                                    <br>Fecha de Dispersi&oacute;n
                                        <%if(request.isUserInRole("FECHA_DISPERSION")){%>
                                    <br><input type="text" name="fechaConfirmacion" id="Fecha1" size="10%" maxlength="10" value="<%=HTMLHelper.displayField(ciclo.fechaDispersion)%>">
                                        <%} else {%>
                                    <br><input type="text" name="fechaConfirmacion" id="Fecha1" size="10%" maxlength="10" value="<%=HTMLHelper.displayField(ciclo.fechaDispersion)%>" onchange="validaFechaDis()">
                                        <%}%>
                                    <br><input type="button" id="btn_confirmaDesembolsar" value="Confirma Desembolso" onClick="confirmaDesembolsaCicloGrupal(<%=numIntegrantes+1%>)">
                                    <%}%>
                                </td>
                            </tr>
                                <%}%>
                            <tr>
                                <td align="center" colspan="2">
                                    <br><input type="button" value="Imprimir" onClick="window.print()">
                                </td>
                            </tr>
                        </table>
                    </form>
                </td>
            </tr>
        </table>
    </body>
</html>
