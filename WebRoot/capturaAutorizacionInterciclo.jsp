<%-- 
    Document   : capturaAutorizacionInterciclo
    Created on : 18/06/2015, 05:04:10 PM
    Author     : avillanueva
--%>

<%@page import="com.sicap.clientes.dao.BitacoraCicloDAO"%>
<%@page import="com.sicap.clientes.helpers.GrupoHelper"%>
<%@page import="com.sicap.clientes.dao.SucursalDAO"%>
<%@page import="com.sicap.clientes.util.GrupoUtil"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.sicap.clientes.vo.CicloGrupalVO"%>
<%@page import="com.sicap.clientes.vo.DireccionGenericaVO"%>
<%@page import="com.sicap.clientes.vo.GrupoVO"%>
<%@page import="java.util.Vector"%>
<%@page import="com.sicap.clientes.util.FormatUtil"%>
<%@page import="com.sicap.clientes.util.ClientesUtil"%>
<%@page import="com.sicap.clientes.util.ClientesConstants"%>
<%@page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>

<%
Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
//Date[] fechasInhabiles = (Date[])session.getAttribute("INHABILES");
//boolean diainhabil = FechasUtil.esDiaInhabil( cal.getTime(), fechasInhabiles);
boolean sinConsulta = false;
boolean tieneAtrasos = false;
boolean tieneMultas = false;
boolean aplicaInterciclo = false;
GrupoVO grupo = new GrupoVO();
double total = 0;
double totalConComision = 0;
// Agregamos para calcular la suma total del monto con seguro financiado
double totalConSegugoFinanciado = 0;
double totalCostoSeguro = 0;
double totalRefinanciado = 0;
double totalVencido = 0;
BitacoraCicloDAO bitacoraCicloDAO = new BitacoraCicloDAO();
//TablaAmortDAO tablaDao = new TablaAmortDAO();
//SaldoIBSDAO saldoDao= new SaldoIBSDAO();
DireccionGenericaVO direccion = new DireccionGenericaVO();
CicloGrupalVO ciclo = new CicloGrupalVO();
TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
//TreeMap catMontosMaximosCiclo = CatalogoHelper.getCatalogo(ClientesConstants.CAT_MAXIMOS_POR_CICLO);
TreeMap catEstatusInterciclo = CatalogoHelper.getCatalogoEstatusCiclo(true, 0);
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
int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
int numIntegrantes = 0;
//int segurosCompletos = 1;
int idComision=0;
int plazo=0;
int tasa = 0;
int idBanco = 0;
int fondeador = 0;
int despacho = 0;
int opcion = 0;
int estatus = 0;
//String campSeguro = "";
//ArchivoAsociadoVO fldSeguro = null;
//ArchivoAsociadoVO fldDocums = null;
String bloqueo = "";
if ( session.getAttribute("GRUPO")!=null ){
    grupo = (GrupoVO)session.getAttribute("GRUPO");
    tasa = GrupoUtil.asignaTasaGrupal(grupo, false);
    idBanco = new SucursalDAO().getSucursal(grupo.sucursal).idBanco;
    if ( idCiclo!=0 ){
        ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
        if ( ciclo!=null && ciclo.direccionReunion!=null ){
            direccion=ciclo.direccionReunion;
        }
        if ( ciclo!=null && ciclo.integrantes!=null ){
            numIntegrantes = ciclo.integrantes.length;
// cuando ya existe el ciclo obtener tasa, plazo y comision
            plazo = ciclo.plazo;
            idComision = ciclo.comision;
        }
        if(ciclo.saldo!=null){
            fondeador = ciclo.getFondeador();
            despacho = ciclo.saldo.getNumDespacho();
        }
        for(int i=0; i<ciclo.integrantes.length; i++){
            if(ciclo.integrantes[i].esInterciclo == 1){
                aplicaInterciclo = true;
            }
        }
        if ( request.getAttribute("CICLO")!=null ){
            ciclo = (CicloGrupalVO)request.getAttribute("CICLO");
            if ( ciclo!=null && ciclo.direccionReunion!=null )
                direccion=ciclo.direccionReunion;
            if ( ciclo.integrantes!=null )
                numIntegrantes = ciclo.integrantes.length;
        }
        catEjecutivos = CatalogoHelper.getCatalogoEjecutivosComercial(grupo.sucursal, "A");
        catAperturador = CatalogoHelper.getCatalogoAperturador(grupo.sucursal, "A");
        if(ciclo.getSemDisp() == 2){
            estatus = ciclo.getEstatusIC();
        } else if (ciclo.getSemDisp() == 4){
            estatus = ciclo.getEstatusIC2();
        }
    }
}
String desembolsoOk = "NO";
if ( request.getAttribute("VALIDACION")!=null )
    desembolsoOk = (String)request.getAttribute("VALIDACION");
int validaCiclo = -1;
if ( request.getAttribute("ID_CICLO")!=null )
    validaCiclo = (Integer)request.getAttribute("ID_CICLO");
// Se verifica si viene de otra financiera JBL SEP/10
boolean isDesembolsadoFlag = GrupoHelper.isDesembolsado(ciclo.integrantes);
boolean isRefinaceoFlag	= ( ciclo.idTipoCiclo==ClientesConstants.CICLO_TIPO_REFINANCIAMIENTO ? true : false );
//boolean isOtraFinancieraFlag =  (otraFinanciera==ClientesConstants.CICLO_OTRA_FINANCIERA ? true : false );
boolean isOtraFinancieraFlag =  false;
if( !isDesembolsadoFlag )
    ciclo.tasa = tasa;
else
    tasa = ciclo.tasa;
//boolean isConfirmadorDesembolso = request.isUserInRole("CONFIRMACION_DESEMBOLSO_GRUPAL");
boolean esMesaControl = request.isUserInRole("ANALISIS_CREDITO");
if(aplicaInterciclo)
    opcion = GrupoHelper.muestraOpcionesGrupal( ciclo, desembolsoOk );
String medio = "";
int esInterciclo = 0;
if(ciclo.estatusIC == ClientesConstants.CICLO_DESEMBOLSADO)
    esInterciclo = 1;
%>
<html>
    <head>
        <title>Autorizaci&oacute;n Interciclo</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script language="Javascript" src="./js/functionsGrupal.js"></script>
        <script language="Javascript" src="./js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript">
            
    function guardaCicloGrupal(integrantes){
        alert("guardaCicloGrupal");
    }
    
    function desembolsaIntericlo(integrantes){
        desHabilitaBoton();
        window.document.forma.command.value = 'desembolsaInterciclo';
        window.document.forma.submit();
    }
    
    function confirmaDesembolsaCicloGrupal(integrantes){
        desHabilitaBoton();
        window.document.forma.command.value = 'confirmaDispersionInterciclo';
        res = confirm('La información que está apunto de guardar no podrá ser modificada. ¿Esta seguro de que desea registrar el ciclo con los datos actuales?');
        if ( !res ){
            habilitaBoton();
            return res;
        }
        window.document.forma.submit();
    }
    
    function regresaAGrupo(){
        desHabilitaBoton();
        window.document.forma.command.value = 'consultaDetalleGrupo';
        window.document.forma.action="controller";
        window.document.forma.submit();
    }
    
    function consultaPagosComunales(){
        document.getElementById("td").disabled = true;
        window.document.forma.command.value='validarPagoComunal';
        window.document.forma.submit();
    }
    
    function muestraDocumento(tipo){
        window.document.forma.command.value='muestraDocumento';
        window.document.forma.tipo.value=tipo;
        window.document.forma.submit();
    }
    
    function muestraDocumentoIC(tipo){
        window.document.forma.command.value='muestraDocumentoIC';
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
        alert("guardaOtrosDatos");
    }
    
    function habilitaCierreCiclo(){
        alert("habilitaCierreCiclo");
    }
    
    function habilitaBoton(){
        document.getElementById("td").disabled = false;
        if(document.getElementById("btn_desembolsar") != null)
            document.getElementById("btn_desembolsar").disabled = false;
        if(document.getElementById("btn_confirmaDesembolsar") != null)
            document.getElementById("btn_confirmaDesembolsar").disabled = false;
        document.getElementById("btn_regresar").disabled = false;
        document.getElementById("btn_imprimir").disabled = false;
        document.body.style.cursor = "default";
    }
    
    function desHabilitaBoton(){
        document.getElementById("td").disabled = true;
        if(document.getElementById("btn_desembolsar") != null)
            document.getElementById("btn_desembolsar").disabled = true;
        if(document.getElementById("btn_confirmaDesembolsar") != null)
            document.getElementById("btn_confirmaDesembolsar").disabled = true;
        document.getElementById("btn_regresar").disabled = true;
        document.getElementById("btn_imprimir").disabled = true;
        document.body.style.cursor = "wait";
    }
    
    function refinanciaCiclo(){
        alert("refinanciaCiclo");
    }
    
    function desembolsaCicloGrupalProg(){
        alert("desembolsaCicloGrupalProg");
    }
    
    function capturaControlPagos(){
        alert("capturaControlPagos");
    }
    
    function cargarArchivos(tipo){
        alert("cargarArchivos");
    }
    
    function descargaFichaSeguro(){
        alert("descargaFichaSeguro");
    }
    
    function days(date){
        alert("days");
    }
    
    function resetDate(){
        alert("resetDate");
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
        alert("validaMonto");
    }
    
    function validaMontoIntegrantes(integrantes){
        alert("validaMontoIntegrantes");
    }
    
    function paraDesembolso(){
        document.getElementById("btn_paraDesembolso").disabled = true;
        document.body.style.cursor = "wait";
        window.document.forma.command.value = 'peticionDesembolsoInterciclo';
        var fechaPart = window.document.forma.fechaDispersion.value.split('/');
        var fechaIngresada = new Date(fechaPart[2],fechaPart[1]-1,fechaPart[0]);
        if(fechaIngresada.getDay() != window.document.forma.diaReunion.value){
            alert("El dia de reunion no coincide con la fecha seleccionada");
            document.getElementById("btn_paraDesembolso").disabled = false;
            document.body.style.cursor = "default";
            return false;
        }
        if(window.document.forma.comentario.value == ""){
            alert("Favor de ingresar un comentario");
            document.getElementById("btn_paraDesembolso").disabled = false;
            document.body.style.cursor = "default";
            return false;
        }
        window.document.forma.submit();
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
                    }else {
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
                    <h3>Autorizaci&oacute;n Inter-Ciclo</h3>
                    <%=HTMLHelper.displayNotifications(notificaciones)%>
                    <br>
                    <form name="forma" action="controller" method="post">
                        <input type="hidden" name="command" value="">
                        <input type="hidden" name="idCliente" value="">
                        <input type="hidden" name="idCiclo" value="<%=idCiclo%>">
                        <input type="hidden" name="tipo" value="">
                        <input type="hidden" name="semDisp" value="<%=ciclo.getSemDisp()%>">
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
                                <td width="50%" align="right">Estatus Inter-Ciclo</td>
                                <td width="50%">  
                                    <select name="estatusIC" id="estatusIC" size="1" disabled="disabled">
                                        <%=HTMLHelper.displayCombo(catEstatusInterciclo, estatus)%>
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
                                        <%= HTMLHelper.displayCombo(catPlazos, plazo)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Fecha de Dispersión</td>
                                <td><input type="text" name="fechaDispersion" id="fechaDispersion" size="10%" maxlength="10" value="<%=HTMLHelper.displayField(ciclo.fechaDispersion)%>" readonly="readonly">
                                </td>
                            <tr>
                                <td width="50%" align="right">Dispersar por</td>
                                <td width="50%">  
                                    <select name="bancoDispersion" size="1">
                                        <%=HTMLHelper.displayCombo(catBancos, ciclo.bancoDispersion)%>
                                    </select>
                                </td>
                            </tr>
                            <%if(CatalogoHelper.esEquipoAutorizadoGarantia(grupo.idGrupo)){%>
                            <tr>
                                <td width="50%" align="right">% Garant&iacute;a</td>
                                <td width="50%">  
                                    <select name="garantia" size="1" disabled>
                                        <%=HTMLHelper.displayCombo(catGarantia, ciclo.garantia)%>
                                    </select>
                                </td>
                            </tr>
                            <%}%>
                            <tr>
                                <td width="50%" align="right">Fondeador</td>
                                <td><select name="fondeador" id="fondeador" size="1" disabled>
                                        <%= HTMLHelper.displayCombo(catFondeador, ciclo.fondeador)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Despacho</td>
                                <td><select name="numDespacho" id="numDespacho" size="1" onKeyPress="return submitenter(this,event)" disabled>
                                        <%= HTMLHelper.displayCombo(catDespachos, despacho)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" align="center"><br>
                                    <table border="1" width="90%">
                                        <tr>
                                            <td align="center"></td>
                                            <td align="center">No.</td>
                                            <td align="center">Cliente</td>
                                            <td align="center">Solicitud</td>
                                            <td align="center">Nombre</td>
                                            <td align="center">Monto a desembolsar</td>
                                            <td align="center">Monto con Seguro Financiado</td>
                                            <td align="center">Monto de Seguro</td>
                                            <td align="center">Calificación</td>
                                            <td align="center">Medio Cobro</td>
                                            <td align="center">Referencia Cobro</td>
                                            <td align="center">Rol</td>
                                        <%if(opcion == 0){%>
                                            <td align="center">Seguro</td>
                                            <td align="center">Actividad</td>
                                        <%}%>
                                        </tr>
                                        <%for ( int i=0 ; ciclo.integrantes!=null && i<ciclo.integrantes.length ; i++ ){
                                            	String estilo = "";                                                
                                                    if(ciclo.integrantes[i].estatus==2){ //Esta cancelado
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
                                                <%if ( ciclo.integrantes[i].esInterciclo==1&& ciclo.integrantes[i].semDisp ==ciclo.getSemDisp()){%>
                                                <%=HTMLHelper.displayCheck("desembolso"+i,true)%>
                                                <%}%>
                                            </td>
                                            <td align="center"> 
                                                <%=i+1%></td>
                                            <td align="center" id="td">
                                                <input type="hidden" name="idCliente<%=i%>" value="<%=ciclo.integrantes[i].idCliente%>">
                                                <a href="#" onClick="consultaCliente(<%=ciclo.integrantes[i].idCliente%>)"><%=ciclo.integrantes[i].idCliente%></a>
                                            </td>
                                            <td align="center"> 
                                                <input type="hidden" name="idSolicitud<%=i%>" value="<%=ciclo.integrantes[i].idSolicitud%>">
                                                <%=ciclo.integrantes[i].idSolicitud%>
                                            </td>
                                            <td align="center">
                                                <input type="hidden" name="nombre<%=i%>" id="nombre<%=i%>" value="<%=ciclo.integrantes[i].nombre%>">
                                                <%=ciclo.integrantes[i].nombre%>
                                            </td>
                                            <td align="right"> 
                                                <input type="hidden" name="monto<%=i%>" id="monto<%=i%>" value="<%=HTMLHelper.formatoMonto( ClientesUtil.calculaMontoSinComision( ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal ) )%>">
                                                <%=HTMLHelper.formatoMonto( ClientesUtil.calculaMontoSinComision( ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal ) )%>
                                            </td>
                                            <td align="right">
                                                <input type="hidden" name="comision<%=i%>" value="<%=ciclo.integrantes[i].comision%>">
                                                <%=HTMLHelper.formatoMonto(ciclo.integrantes[i].montoConSeguro)%>
                                            </td>
                                            <td align="right"><%=HTMLHelper.formatoMonto(ciclo.integrantes[i].costoSeguro)%></td>
                                            <%if ( ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_BUENA || ciclo.integrantes[i].calificacion == 0 ){ %>
                                            <input type="hidden" name="calificacion<%=i%>" id="calificacion<%=i%>" value="<%=ciclo.integrantes[i].calificacion%>">
                                            <td BGCOLOR="#33FF33"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
                                            <%} %>
                                            <% if ( ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_REGULAR ){ 
                                                if (ciclo.integrantes[i].aceptaRegular >0){%>
                                                <input type="hidden" name="calificacion<%=i%>" id="calificacion<%=i%>" value="<%=ciclo.integrantes[i].calificacion%>">
                                                <input type="hidden" name="aceptaRegular<%=i%>" id="aceptaRegular<%=i%>" value="<%=ciclo.integrantes[i].aceptaRegular%>">
                                                <td BGCOLOR="#ffff00"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
                                                <%}else{%>
                                                <input type="hidden" name="calificacion<%=i%>" id="calificacion<%=i%>" value="<%=ciclo.integrantes[i].calificacion%>">
                                                <td BGCOLOR="#BDBDBA"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
                                                <%}%>
                                            <%} %>
                                            <% if ( ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_MALA ){ %>
                                            <input type="hidden" name="calificacion<%=i%>" id="calificacion<%=i%>" value="<%=ciclo.integrantes[i].calificacion%>">
                                            <td BGCOLOR="#FF0000"><center><b>&nbsp;</b></center></td>
                                            <%} %>
                                            <%if ( ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_NA ){ %>
                                            <input type="hidden" name="calificacion<%=i%>" id="calificacion<%=i%>" value="<%=ciclo.integrantes[i].calificacion%>">
                                            <td BGCOLOR="#33FF99"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
                                            <%} %>
                                            <%if ( ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_SIN_CONSULTA ){ sinConsulta=true;%>
                                            <input type="hidden" name="calificacion<%=i%>" id="calificacion<%=i%>" value="<%=ciclo.integrantes[i].calificacion%>">
                                            <td BGCOLOR="#FFEBCD"><center><font style="font-style: oblique;">Sin consulta</font></center></td>
                                            <%} %>
                                            <%if (ciclo.idCiclo > 0){
                                                medio = (ciclo.integrantes[i].medioCobro==1 ? "TAR" : "ODP");
                                            }else{
                                                medio = (ciclo.integrantes[i].medioDisp==1 ? "TAR" : "ODP");
                                            }%>
                                            <td align="center">
                                                <input type="hidden" name="medioCobro<%=i%>" value="<%=ciclo.integrantes[i].medioCobro%>">
                                                <%=HTMLHelper.displayField(medio)%>
                                            </td>
                                            <td align="center">
                                                <input type="hidden" name="cheque<%=i%>" value="<%=ciclo.integrantes[i].numCheque%>">
                                                <%=HTMLHelper.displayField(ciclo.integrantes[i].numCheque )%>
                                            </td>
                                            <td align="center">
                                                <select name="rol<%=i%>" id="rol<%=i%>" size="1" value="<%=ciclo.integrantes[i].rol%>" >
                                                    <%=HTMLHelper.displayCombo(catRoles, ciclo.integrantes[i].rol)%>
                                                </select>
                                            </td>
                                        <%if(opcion == 0){%>
                                            <td align="center">
                                                <input type="hidden" name="conseguro<%=i%>" value="<%=ciclo.integrantes[i].seguro%>">
                                                <%=(ciclo.integrantes[i].seguro > 0 ?"OK" : "--")%>
                                            </td>
                                            <td align="center">
                                                <input type="hidden" name="conempleo<%=i%>" value="<%=ciclo.integrantes[i].empleo%>">
                                                <%=(ciclo.integrantes[i].empleo != 0 ?"OK" : "--")%>
                                            </td>
                                        <%}%>
                                        </tr>
                                        <%
                                            total += FormatUtil.redondeaMoneda(ClientesUtil.calculaMontoSinComision( ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal ));
                                            totalConComision += ciclo.integrantes[i].monto;
                                            totalConSegugoFinanciado += ciclo.integrantes[i].montoConSeguro;
                                            totalCostoSeguro += ciclo.integrantes[i].costoSeguro;
                                            if( isRefinaceoFlag ){
                                                totalRefinanciado += ciclo.integrantes[i].monto+ciclo.integrantes[i].montoRefinanciado;
                                                totalVencido += ciclo.integrantes[i].montoRefinanciado;
                                            }
                                            numIntegrantes = i;
                                        }%>
                                        <tr>
                                            <td colspan="5" align="center">
                                                Total
                                            </td>
                                            <td align="right"><input type="hidden" name="montoTotal" value="<%=total%>">
                                                <%=HTMLHelper.formatoMonto(total)%>
                                            </td>
                                            <td align="right"><%=HTMLHelper.formatoMonto(totalConSegugoFinanciado)%></td>
                                            <td align="right"><%=HTMLHelper.formatoMonto(totalCostoSeguro)%></td>
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                        <%if(opcion == 0){%>
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                        <%}%>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr><td>&nbsp;</td></tr>
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
                                        <tr id="semana2" onClick="cambiarDisplay('semana2_link')" style="cursor: pointer;">
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
                                        <tr id="semana4" onClick="cambiarDisplay('semana4_link')" style="cursor: pointer;">
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
                                            <td colspan="2" align="center"><textarea name="ultimoComentario" rows="1" cols="100" readonly="readonly" class="soloLectura"><%=bitacoraCicloDAO.getUltimoComentario(grupo.idGrupo, ciclo.idCiclo) %></textarea>
                                                <a href="#" onClick="ayudaHistorialComentarios(<%=grupo.idGrupo%>, <%=ciclo.idCiclo%>)"><img name="imagen" alt="Historial" src="images/history_icon.png"></a>
                                            </td>                        
                                        </tr>                    
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" align="center">
                                    <h4 id="expanderHead" style="cursor:pointer;">Agregar Comentario <span id="expanderSign">+</span></h4>
                                    <div id="expanderContent" style="display:none">
                                        <textarea name="comentario" rows="5" cols="100" placeholder="Escribe tu comentario..."></textarea>
                                        <br>
                                        <br>
                                        <input type="button" id="boton_agrega_comentario" value="Agrega Comentario" onClick="agregaComentario()"/>
                                        <br>
                                    </div>
                                </td>
                            </tr>
                            <br>
                            <tr>
                                <td align="center" colspan="2">
                                    <%if(estatus == ClientesConstants.CICLO_AUTORIZADO){%>
                                    <input type="button" id="btn_paraDesembolso" value="Petici&oacute;n Desembolso" onClick="paraDesembolso()">
                                    <%}else if(esMesaControl && estatus == ClientesConstants.CICLO_PARADESEMBOLSAR){%>
                                    <br><input type="button" id="btn_desembolsar" value="Desembolsar" onClick="desembolsaIntericlo(<%=numIntegrantes+1%>)">
                                    <%}else if(esMesaControl && estatus == ClientesConstants.CICLO_DESEMBOLSO){%>
                                    <br><input type="button" id="btn_confirmaDesembolsar" value="Confirma Desembolso" onClick="confirmaDesembolsaCicloGrupal(<%=numIntegrantes+1%>)">
                                    <%}%>
                                    <input type="button" id="btn_regresar" value="Regresar" onClick="regresaAGrupo()">
                                </td>
                            </tr>
                            <tr>
                                <td align="center" colspan="2">
                                    <br><INPUT TYPE="button" id="btn_imprimir" value="Imprimir" onClick="window.print()">
                                </td>
                            </tr>
                        </table>
                    </form>
                </td>
            </tr>
        </table>
    </body>
</html>
