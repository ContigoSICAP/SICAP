<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>

<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>

<%
    boolean isMesaControl = false;
    boolean muestraEstatus = false;
    boolean aceptaRegular = false;
    boolean otraFinanciera = false;
    boolean bloqueRegular = false;
    boolean otraFinChckDis = false;
    //session.setAttribute("persona","cliente");
    int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
    ClienteVO cliente = (ClienteVO) session.getAttribute("CLIENTE");
    int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
    SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
    SolicitudVO[] solicitudes = cliente.solicitudes;
    TreeMap catComportamiento = CatalogoHelper.getCatalogo(ClientesConstants.CAT_COMPORTAMIENTO_CLIENTE);
    TreeMap catTipoCuenta = CatalogoHelper.getCatalogo(ClientesConstants.CAT_TIPO_CUENTA);
    TreeMap catAntCuenta = CatalogoHelper.getCatalogo(ClientesConstants.CAT_ANT_CUENTA);
    TreeMap catBusquedaCuenta = CatalogoHelper.getCatalogo(ClientesConstants.CAT_NUM_BUSQUEDA_CTA);
    TreeMap catEstatusSol = CatalogoHelper.getCatalogoEstatusSolcitud(false);
    CreditoVO buroCredito = new CreditoVO();
    CreditoVO circuloCredito = new CreditoVO();
    String modificaObligadoCredito = "INSERTA";

    if (solicitudes[indiceSolicitud].buroCredito != null) {
        buroCredito = solicitudes[indiceSolicitud].buroCredito;
    }
    if (solicitudes[indiceSolicitud].circuloCredito != null) {
        circuloCredito = solicitudes[indiceSolicitud].circuloCredito;
        modificaObligadoCredito = "ACTUALIZA";
        
    }

    if (request.isUserInRole("ANALISIS_CREDITO")) {
        isMesaControl = true;
    }
//	boolean existe = ArchivosAsociadosHelper.existe(solicitudes[indiceSolicitud].archivosAsociados, ClientesConstants.ARCHIVO_TIPO_AUTORIZACION);

    Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
    if (solicitud.estatus == ClientesConstants.SOLICITUD_EN_ANALISIS || solicitud.estatus == ClientesConstants.SOLICITUD_REVALORACION) {
        muestraEstatus = true;
    }
    aceptaRegular = CatalogoHelper.esSucursalAceptaRegular(cliente.idSucursal);
    if(CatalogoHelper.esSucursalPiloto3(cliente.idSucursal)){
         if (solicitud.estatus == ClientesConstants.SOLICITUD_PREAPROBADA || 
             solicitud.estatus == ClientesConstants.SOLICITUD_RECHAZADA||
             solicitud.estatus == ClientesConstants.SOLICITUD_AUTORIZADA || 
             solicitud.estatus == ClientesConstants.SOLICITUD_CAPTURADO){
             otraFinanciera = true;             
         }
    }
    
    boolean permiteAutorizarPorExcepcion = false;
    //Permite Actualizar regular piloto 1
    boolean permiteAutorizarRegularP1 = false; 
    if(modificaObligadoCredito == "ACTUALIZA"){
        
        //Verificamos si la sucursal permite realizar autorizacion por excepcion
        boolean sucursalAutExp = CatalogoHelper.sucursalPermiteAutorizacionPorExcepcion(cliente.idSucursal);
        boolean fechaConsultaMayorFechaAutExcep = (CreditoHelper.obtenerFechaAutorizadosPorExcepcion().before(circuloCredito.fechaConsulta))?true:false;
        boolean clienteRechazado = CreditoHelper.obtenerCalificacionCredito(circuloCredito) == ClientesConstants.CALIFICACION_CIRCULO_MALA && solicitudes[indiceSolicitud].estatus == ClientesConstants.SOLICITUD_RECHAZADA;
        permiteAutorizarPorExcepcion = sucursalAutExp && fechaConsultaMayorFechaAutExcep && isMesaControl && clienteRechazado;
        
        permiteAutorizarRegularP1 = sucursalAutExp && circuloCredito.comportamiento == ClientesConstants.CALIFICACION_CIRCULO_REGULAR && fechaConsultaMayorFechaAutExcep && isMesaControl && circuloCredito.aceptaRegular==1 && (solicitudes[indiceSolicitud].estatus == ClientesConstants.SOLICITUD_PREAPROBADA || solicitudes[indiceSolicitud].estatus == ClientesConstants.SOLICITUD_AUTORIZADA);
    }
%>
<html>
    <head>
        <title>Calificaci&oacute;n crediticia</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script language="Javascript" src="./js/functionsGrupal.js"></script>
        <script language="Javascript" src="./js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript">
        <!--
            function guardaDatosCredito() {
                document.getElementById("boton").disabled = true;
                var action = window.document.forma.modificaObligadoCredito.value;
                var otraFinanciera = false
                <%if (otraFinanciera){%>
                        otraFinanciera = window.document.forma.otraFinancieraChck.checked;
                <%}%>
                if(otraFinanciera){
                    if (window.document.forma.comportamientoCirculo.value == 0 && window.document.forma.estatusSolicitud.value !=<%=ClientesConstants.SOLICITUD_PENDIENTE%>) {
                        alert("Debe Realizar la Consulta a C.C");
                        document.getElementById("boton").disabled = false;
                        return false;
                    } 
                    <%if (muestraEstatus) {%>
                    if (window.document.forma.estatusSolicitud.value !=<%=ClientesConstants.SOLICITUD_PREAPROBADA%>) {
                        alert("Solo se puede autorizar un cliente si se seleccion\u00f3 que pertenece a otra financiera");
                        document.getElementById("boton").disabled = false;
                        return false;                        
                    }
                    <%}else{%>
                    if (window.document.forma.calificacionMesaControlCirculo.value !=1){
                        alert("Solo se puede Calificar como Bueno si se seleciona que es de otra financiera");
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                    <%}%>
                }else {           
            <%if (aceptaRegular) {%>
                if (window.document.forma.comportamientoCirculo.value == 0 && window.document.forma.estatusSolicitud.value !=<%=ClientesConstants.SOLICITUD_PENDIENTE%>) {
                    alert("Debe Realizar la Consulta a C.C");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.calificacionMesaControlCirculo != null){
                    if (window.document.forma.calificacionMesaControlCirculo.value == 3){
                        window.document.forma.aceptaRegular.value =2;
                    }
                    if(window.document.forma.calificacionMesaControlCirculo.value == 1){
                        window.document.forma.aceptaRegular.value =0;
                    }
                    if (window.document.forma.calificacionMesaControlCirculo.value == 2){
                        alert("No se puede calificar como Malo un Cliente Regular");
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                    if (window.document.forma.calificacionMesaControlCirculo.value == 4||window.document.forma.calificacionMesaControlCirculo.value == 0){
                        alert("No se puede asignar esa calificaci\u00f3n");
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                }
                <%if (muestraEstatus) {%>
                if (window.document.forma.comportamientoCirculo.value == <%=ClientesConstants.CALIFICACION_CIRCULO_MALA%> && window.document.forma.estatusSolicitud.value == <%=ClientesConstants.SOLICITUD_PREAPROBADA%>) {
                    alert("No se puede aceptar una solicitud con calificacion mala de circulo de credito");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.estatusSolicitud.value ==<%=ClientesConstants.SOLICITUD_RECHAZADA%> && window.document.forma.comportamientoCirculo.value != <%=ClientesConstants.CALIFICACION_CIRCULO_MALA%>) {
                    alert("No se puede rechazar una solicitud que no tiene una calificacion mala");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                <%}%>                
            <%}%>
                }
            <%if (muestraEstatus) {%>
                if (window.document.forma.estatusSolicitud.value == 0) {
                    alert("Debe selecionar un estatus");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.estatusSolicitud.value == <%=ClientesConstants.SOLICITUD_PREAPROBADA%>) {
                    if (window.document.forma.comportamientoCirculo.value == 0) {
                        alert("Debe Realizar la Consulta a C.C");
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                }
                if (window.document.forma.comentario.value == '') {
                    alert("Debe ingresar un comentario");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.estatusSolicitud.value ==<%=ClientesConstants.SOLICITUD_PENDIENTE%>) {
                    if (action == 'INSERTA')
                        window.document.forma.command.value = "guardaDatosCredito";
                    else
                        window.document.forma.command.value = "actualizaDatosCredito";
                    document.body.style.cursor = "wait";
                    window.document.forma.submit();
                    return true;
                }
            <%if (!aceptaRegular) {%>
                if (window.document.forma.estatusSolicitud.value ==<%=ClientesConstants.SOLICITUD_RECHAZADA%> && (window.document.forma.calificacionMesaControlCirculo.value == 1 || window.document.forma.calificacionMesaControlCirculo.value == 4)) {
                    alert("No se puede rechazar una solicitud que tiene una Buena calificacion de mesa de control");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.estatusSolicitud.value ==<%=ClientesConstants.SOLICITUD_PREAPROBADA%> && window.document.forma.calificacionMesaControlCirculo.value == 2) {
                    alert("No se puede aceptar una solicitud que tiene una Mala calificacion de mesa de control");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.calificacionMesaControlCirculo.value == 0 && window.document.forma.comportamientoCirculo.value == <%=ClientesConstants.CALIFICACION_CIRCULO_MALA%>){
                    alert("Debe Realizar una consulta de Mesa de control");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
            <%}%>

            <%}%>
            
            <% if (solicitudes[indiceSolicitud].tipoOperacion != ClientesConstants.GRUPAL && solicitudes[indiceSolicitud].tipoOperacion != ClientesConstants.REESTRUCTURA_GRUPAL) { %>
                if (window.document.forma.fechaConsulta.value != '') {
                    if (!esFechaValida(window.document.forma.fechaConsulta, false)) {
                        alert("La Fecha de consulta de bur\u00f3 es inv\u00e1lida");
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                } else {
                    alert("Debe capturar la Fecha de consulta de bur\u00f3");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.comportamiento.value == 0) {
                    alert("Debe capturar el comportamiento del Bur\u00f3 de Cr\u00e9dito");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
            <%}%>
                if (window.document.forma.fechaConsultaCirculo.value != '') {
                    if (!esFechaValida(window.document.forma.fechaConsultaCirculo, false)) {
                        alert("La Fecha de consulta de circulo es inv\u00e1lida");
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                } else {
                    alert("Debe capturar la Fecha de consulta de círculo");
                    document.getElementById("boton").disabled = false;
                    return false;
                }

            <%if (solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.GRUPAL || solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.REESTRUCTURA_GRUPAL) {
                                if (!isMesaControl) {%>
                if (window.document.forma.comportamientoCirculo.value == 0) {
                    alert("Debe capturar el comportamiento del Circulo de Cr\u00e9dito");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
            <%}
                                        } else {%>
                if (window.document.forma.comportamientoCirculo.value == 0) {
                    alert("Debe capturar el comportamiento del Circulo de Cr\u00e9dito");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
            <%}%>
                if (action == 'INSERTA')
                    window.document.forma.command.value = "guardaDatosCredito";
                else
                    window.document.forma.command.value = "actualizaDatosCredito";
                document.body.style.cursor = "wait";
                window.document.forma.submit();

            }

            function datosCreditoObligadosSolidarios() {
                document.getElementById("boton").disabled = true;
                if (window.document.forma.idSolicitud.value == 0) {
                    alert('Es necesario guardar la información previamente');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                params = "?command=consultaCreditoObligadosSolidarios&idSolicitud=" +<%=idSolicitud%>;
                url = "/CEC/controller";
                abreVentana(url + params, 'scrollbars=yes', 750, 400, true, 0, 0);
            }

    function consultaBuro()         {
                params = "?persona=cliente&command=consultaBuroCredito&idSolicitud=" +<%=idSolicitud%>;
                url = "/CEC/controller";
                abreVentana(url + params, 'scrollbars=yes', 900, 500, true, 0, 0);
            }

            function consultaCirculo()         {
                params = "?persona=cliente&command=consultaCirculoDeCredito&idSolicitud=" +<%=idSolicitud%>;
                url = "/CEC/controller";
                abreVentana(url + params, 'scrollbars=yes', 900, 500, true, 0, 0);
            }
            
            function agregaComentario() {
                if (window.document.forma.comentario.value == "") {
                    alert("No has ingresado comentarios")
                    return false;
                } else if (!confirm("¿Deseas agregar el comentario?")) {
                    return false;
                } else {
                    window.document.getElementById("boton_agrega_comentario").disabled = true;
                    window.document.forma.command.value = 'agregaComentarioCiclo';
                    window.document.forma.submit();
                }
            }
            function ayudaHistorialComentarios() {
                params = "?command=historialComentariosCliente&idCliente=" +<%=solicitudes[indiceSolicitud].idCliente%> + "&idSolicitud=" +<%=solicitudes[indiceSolicitud].idSolicitud%>;
                url = "/CEC/controller";
                abreVentana(url + params, 'scrollbars=yes', 1000, 250, true, 0, 0);
            }

        //-->

        </script>
        <script type="text/javascript">
    $(document).ready(function () {
        $("#expanderHead").click(function () {
            $("#expanderContent").slideToggle();
            if ($("#expanderSign").text() == "+") {
                $("#expanderSign").html("-")
            } else {
                $("#expanderSign").text("+")
            }
        });
    });
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <%if (solicitud.estatus == ClientesConstants.SOLICITUD_NUEVA || solicitud.estatus == ClientesConstants.SOLICITUD_EN_ANALISIS
            || solicitud.estatus == ClientesConstants.SOLICITUD_PENDIENTE || solicitud.estatus == ClientesConstants.SOLICITUD_REVALORACION) {%>
        <jsp:include page="menuIzquierdoRapido.jsp" flush="true"/>
        <%} else {%>
        <jsp:include page="menuIzquierdo.jsp" flush="true"/> 
        <%}%>
    <CENTER>
        <form action="controller" method="post" name="forma">
            <input type="hidden" name="command" value="">
            <input type="hidden" name="idSolicitud" value="<%=solicitudes[indiceSolicitud].idSolicitud%>">
            <input type="hidden" name="idCliente" value="<%=solicitudes[indiceSolicitud].idCliente%>">
            <input type="hidden" name="modificaObligadoCredito" value="<%=modificaObligadoCredito%>">
            <input type="hidden" name="permiteAutorizarPorExcepcion" value="<%=permiteAutorizarPorExcepcion%>">
            <input type="hidden" name="permiteAutorizarRegularP1" value="<%=permiteAutorizarRegularP1%>">
            <table border="0" width="100%">
                <tr>
                    <td align="center">	 
                        <h3>Calificaci&oacute;n crediticia</h3>
                        <%=HTMLHelper.displayNotifications(notificaciones)%>
                        <%if(circuloCredito.aceptaRegular==1){%>
                        <font color="red"><b>Candidato Sin Calificación por parte de Mesa de Control</b></font>
                        <%}%>
                        <table  border="0" cellpadding="0" width="100%">
                            <% if (solicitudes[indiceSolicitud].tipoOperacion != ClientesConstants.GRUPAL && solicitudes[indiceSolicitud].tipoOperacion != ClientesConstants.REESTRUCTURA_GRUPAL) {%>
                            <tr>
                                <td colspan="2" align="center" height="25"><b>Bur&oacute; de cr&eacute;dito</b></td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Fecha de Consulta</td>
                                <td width="50%">
                                    <input type="text" name="fechaConsulta" size="10" maxlength="10" value="<%=HTMLHelper.displayField(buroCredito.fechaConsulta)%>" />
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Fecha de captura de los resultados</td>
                                <td width="50%"><input type="text" name="fechaCaptura" size="10" maxlength="10" value="<%=HTMLHelper.displayField(buroCredito.fechaCaptura, new java.util.Date())%>" readonly="readonly"/></td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Comportamiento</td>
                                <td width="50%"><select name="comportamiento"><%=HTMLHelper.displayCombo(catComportamiento, buroCredito.comportamiento)%></select></td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Descripci&oacute;n de la calificaci&oacute;n</td>
                                <td width="50%"><input type="text" name="descripcion" maxlength="100" value="<%=HTMLHelper.displayField(buroCredito.descripcion)%>" /></td>
                            </tr>
                            <%if (CreditoHelper.puedeConsultarInfoCrediticia(request, indiceSolicitud, cliente, ClientesConstants.SOCIEDAD_BURO)) {%>
                            <tr>
                                <td colspan="2" align="center"><input type="button" name="" value="Consultar Buró de Crédito" onClick="consultaBuro();"><br></td>
                            </tr>
                            <%}%>
                            <%}%>
                            <tr>
                                <td colspan="2" align="center" height="25"><b>C&iacute;rculo de cr&eacute;dito</b></td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Fecha de consulta</td>
                                <td width="50%"><input type="text" name="fechaConsultaCirculo" size="10" maxlength="10" value="<%=HTMLHelper.displayField(circuloCredito.fechaConsulta)%>"/></td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Fecha de captura de los resultados</td>
                                <td width="50%"><input type="text" name="fechaCapturaCirculo" size="10" maxlength="10" value="<%=HTMLHelper.displayField(circuloCredito.fechaCaptura, new java.util.Date())%>" readonly="readonly"/></td>
                            </tr>
                            <% if (solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.GRUPAL || solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.REESTRUCTURA_GRUPAL) {%>
                            <tr>
                                <td width="50%" align="right">Comportamiento del cliente</td>
                                <td width="50%"><input type="text" name="aliasComportamientoCirculo" size="10" maxlength="10" value="<%=HTMLHelper.getDescripcion(catComportamiento, circuloCredito.comportamiento)%>" readonly="readonly" class="sololectura"/></td>
                                <td width="50%"><input type="hidden" name="comportamientoCirculo" value="<%=circuloCredito.comportamiento%>"/></td>
                            </tr>
                                <% if(circuloCredito.aceptaRegular == ClientesConstants.AUTORIZADO_POR_EXCEPCION || circuloCredito.aceptaRegular == ClientesConstants.AUTORIZADO_POR_EXCEPCION_REGULAR){%>
                                    <tr>
                                        <td width="50%" align="right">Calificacion mesa de control(Autorizaci&oacute;n por Excepci&oacute;n) </td>
                                        <td width="50%"><select name="calificacionMesaControlCirculo" disabled="true" ><%=HTMLHelper.displayCombo(catComportamiento, circuloCredito.calificacionMesaControl)%></select></td>
                                    </tr>     
                                <%}else if (!aceptaRegular || (aceptaRegular && (circuloCredito.comportamiento == ClientesConstants.CALIFICACION_CIRCULO_REGULAR || circuloCredito.comportamiento == ClientesConstants.CALIFICACION_CIRCULO_NA) && circuloCredito.aceptaRegular!=1 && circuloCredito.aceptaRegular!=ClientesConstants.AUTORIZADO_POR_EXCEPCION_REGULAR) || 
                                        permiteAutorizarPorExcepcion || permiteAutorizarRegularP1) {%>
                                    <tr>
                                        <td width="50%" align="right">Calificacion mesa de control <%if(permiteAutorizarPorExcepcion || permiteAutorizarRegularP1){%> (Autorización por Excepción) <%}%></td>
                                        <td width="50%"><select name="calificacionMesaControlCirculo" ><%=HTMLHelper.displayCombo(catComportamiento, circuloCredito.calificacionMesaControl)%></select></td>
                                    </tr>
                                <%}

                                if (aceptaRegular){
                                    if(circuloCredito.aceptaRegular==1){%>
                                     <tr>
                                        <td width="50%" align="right">Cliente Regular Aceptado</td>
                                        <td width="50%"><%=HTMLHelper.displayCheckBool("Regular",true, true)%></td>
                                        <td width="50%"><input type="hidden" name="aceptaRegular" value="<%=circuloCredito.aceptaRegular%>"/></td>
                                    </tr>
                                    <%}else{%>
                                        <tr>
                                            <td width="50%"><input type="hidden" name="aceptaRegular" value="0"/></td>
                                        </tr>
                                    <%}%>
                                <%}%>
                            <%} else {%>
                            <tr>
                                <td width="50%" align="right">Comportamiento del cliente</td>
                                <td width="50%"><select name="comportamientoCirculo" ><%=HTMLHelper.displayCombo(catComportamiento, circuloCredito.comportamiento)%></select></td>
                            </tr>
                            <%}%>
                            <tr>
                                <td width="50%" align="right">Descripci&oacute;n de la calificaci&oacute;n</td>
                                <td width="50%"><input type="text" name="descripcionCirculo" size="70" maxlength="100" value="<%=HTMLHelper.displayField(circuloCredito.descripcion)%>"/></td>
                            </tr>
                            <%if(otraFinanciera && request.isUserInRole("ANALISIS_CREDITO")){%>
                            <tr>
                                <td width="50%" align="right">Otra Financiera</td>
                                <td> <%=HTMLHelper.displayCheck("otraFinancieraChck",(circuloCredito.aceptaRegular==6)?true:false, (solicitud.desembolsado != ClientesConstants.DESEMBOLSADO && request.isUserInRole("ANALISIS_CREDITO"))?false:true)%>
                                    </td>
                            </tr>
                            <%}%>
                            <%if (muestraEstatus) {%>
                            <tr>
                                <td width="50%" align="right">Selecione Estatus</td>
                                <td width="50%"><select name="estatusSolicitud" ><%=HTMLHelper.displayCombo(catEstatusSol, 0)%></select></td>
                            </tr>
                            <%}%>
                            <tr>
                                <td colspan="2" align="center">
                                    <br>
                                    <table border="1" width="40%">
                                        <tr>
                                            <td colspan="2" align="center">
                                                <a href="#" onClick="ayudaHistorialComentarios();"><img name="imagen" alt="Historial" src="images/history_icon.png"></a>
                                            </td>                        
                                        </tr>                    
                                    </table>
                                </td>
                            </tr>
                            <%if (muestraEstatus) {%>
                            <tr>
                                <td colspan="2" align="center">
                                    <h4 id="expanderHead" style="cursor:pointer;">
                                        Agregar Comentario <span id="expanderSign">+</span>
                                    </h4>
                                    <div id="expanderContent" style="display:none">
                                        <textarea name="comentario" rows="5" cols="60" placeholder="Escribe tu comentario..."></textarea>
                                    </div>
                                </td>
                            </tr> 
                            <%}%>
                            <%if (CreditoHelper.puedeConsultarInfoCrediticia(request, indiceSolicitud, cliente, ClientesConstants.SOCIEDAD_CIRCULO)) {%>
                            <tr>
                                <td align="center" colspan="2"> <br>
                                    <input type="button" name="" value="Consultar Circulo de Crédito" onClick="consultaCirculo();" >
                                    <br><br>
                                </td>
                            </tr>
                            <%}%>

                            <% if (solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.CONSUMO || solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.VIVIENDA || solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.CREDIHOGAR || solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.SELL_FINANCE) {%>
                            <tr>
                                <td colspan="2" align="center" height="25"><b>Cuenta de referencia</b></td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Tipo de cuenta</td>
                                <td width="50%"><select name="tipoCuenta"><%=HTMLHelper.displayCombo(catTipoCuenta, buroCredito.tipoCuenta)%></select></td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Antigüedad de la cuenta</td>
                                <td width="50%"><select name="antCuenta"><%=HTMLHelper.displayCombo(catAntCuenta, buroCredito.antCuenta)%></select></td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">N&uacute;mero de busquedas de la cuenta</td>
                                <td width="50%"><select name="numBusquedaCuenta"><%=HTMLHelper.displayCombo(catBusquedaCuenta, buroCredito.numBusquedaCuenta)%></select></td>
                            </tr>
                            <%} %>
                            <tr>
                                <td colspan="2" align="center">
                                    <br>
                                    <%if (solicitud.desembolsado == ClientesConstants.DESEMBOLSADO || !request.isUserInRole("ANALISIS_CREDITO")) { %>
                                    <input disabled type="button" id="boton" name="" value="Enviar" onClick="guardaDatosCredito();" >
                                    <%} else { %>
                                    <input type="button" id="boton" name="" value="Enviar" onClick="guardaDatosCredito();" >
                                    <%}%>
                                    <br><br>
                                    <%if (cliente.solicitudes[indiceSolicitud].obligadosSolidarios != null && cliente.solicitudes[indiceSolicitud].obligadosSolidarios.length != 0) {%>
                                    <input type="button" id="boton" name="" value="Obligados solidarios" onClick="datosCreditoObligadosSolidarios();" >
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
