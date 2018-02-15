<%@page import="com.sicap.clientes.dao.BitacoraCicloDAO"%>
<%@page import="com.sicap.clientes.dao.DireccionGenericaDAO"%>
<%@page import="com.sicap.clientes.dao.IntegranteCicloDAO"%>
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
double totalConSegugoFinanciado = 0;
double totalCostoSeguro = 0;
boolean esMesaControl = request.isUserInRole("ANALISIS_CREDITO");
boolean sinConsulta = false;
boolean nuevo = false;
DireccionGenericaVO direccion = new DireccionGenericaVO();
CicloGrupalVO ciclo = new CicloGrupalVO();
IntegranteCicloDAO integranteDao = new IntegranteCicloDAO();
BitacoraCicloDAO bitacoraCicloDao = new BitacoraCicloDAO();
TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
TreeMap catEstatus = null;
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
ArrayList <IntegranteCicloVO> integrantesNuevos = (ArrayList<IntegranteCicloVO>)request.getAttribute("INTEGRANTES_NUEVOS");
//int otraFinanciera 	= HTMLHelper.getParameterInt(request, "otraFinanciera");
    int numIntegrantes = 0;
    int tasa = 0;
    int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
    int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
    int muestraNuevosIntegrantes = HTMLHelper.getParameterInt(request, "mostrarNuevos");
    boolean sucursalAceptaRegular = false;
    boolean otraFinanciera =false;
    boolean ActivaGarantia = false;
    grupo = new GrupoDAO().getGrupo(idGrupo);
    catEjecutivos = CatalogoHelper.getCatalogoEjecutivosComercial(grupo.sucursal, "A");
    catAperturador = CatalogoHelper.getCatalogoAperturador(grupo.sucursal, "A");
    tasa = GrupoUtil.asignaTasaGrupal(grupo, false);
    ciclo = new CicloGrupalDAO().getCicloApertura(idGrupo, idCiclo);
    catEstatus = CatalogoHelper.getCatalogoEstatusCicloApertura(ciclo.estatus);
    
//ArrayList<IntegranteCicloVO> integrantesCiclo = new IntegranteCicloDAO().getIntegrantesApertura(idGrupo, idCiclo);
    ArrayList<IntegranteCicloVO> integrantesCiclo = (ArrayList<IntegranteCicloVO>) request.getAttribute("INTEGRANTES_CICLO");
//if (request.getParameter("command").equals("consultaCicloApertura") && (ciclo.estatus == ClientesConstants.CICLO_APERTURA||ciclo.estatus == ClientesConstants.CICLO_RECHAZADO) && integrantesNuevos != null){
    if ((ciclo.estatus == ClientesConstants.CICLO_APERTURA || ciclo.estatus == ClientesConstants.CICLO_RECHAZADO) && integrantesNuevos != null && muestraNuevosIntegrantes == 1) {
        for (IntegranteCicloVO integrante : integrantesNuevos) {
            integrantesCiclo.add(integrante);
        }
    }
    sucursalAceptaRegular = CatalogoHelper.esSucursalAceptaRegular(grupo.sucursal);
    
    otraFinanciera = CatalogoHelper.esSucursalPiloto3(grupo.sucursal);
    if(otraFinanciera){
        for (int i=0; i <integrantesCiclo.size();i++){
            if(integrantesCiclo.get(i).aceptaRegular == ClientesConstants.AUTORIZACION_OTRA_FINANCIERA){
                ActivaGarantia = true;
                break;
            }
        }
    }
    
    direccion = new DireccionGenericaDAO().getDireccion(ciclo.idDireccionReunion);
    String color = "#FFFFFF";
    String bloqueo = "";
String consultaCC = "";
 if (request.getAttribute("recienGruadado")!= null)
     nuevo = true;


%>
<html>
    <head>
        <title>Apertura de Ciclo Grupal</title>
        <script language="Javascript" src="./js/jquery-1.4.2.min.js"></script>
        <script language="Javascript" src="./js/functions.js"></script>
        <script language="Javascript" src="./js/functionsGrupal.js"></script>
        <script type="text/javascript">
        <!--

            function guardaCicloApertura(integrantes) {
                disableEdit();
                window.document.forma.command.value = 'actualizaCicloApertura';
                window.document.forma.action = "controller";
                var noIntegrantes = cuentaChecked(integrantes);
                if(noIntegrantes == -1){
                    alert('Integrante con calificacion mala. Descartar para su envio.');
                    enableEdit();
                    return false;
                }
                if (window.document.forma.plazo.value == 0) {
                    alert('Debe indicar el tipo de plazo');
                    enableEdit();
                    return false;
                }

                if (noIntegrantes > 40) {
                    alert("El grupo debe contar con un m�ximo de 40 integrantes");
                    enableEdit();
                    return false;
                }

                if (window.document.forma.idColonia.value == 0) {
                    alert("Debe capturar la direcci�n");
                    enableEdit();
                    return false;
                }

                if (window.document.forma.calle.value == '') {
                    alert("Debe capturar la calle");
                    enableEdit();
                    return false;
                }
                if (window.document.forma.numeroExterior.value == '') {
                    alert("Debe capturar el n�mero exterior");
                    enableEdit();
                    return false;
                }
                if (<%=esMesaControl%>) {
                    if (window.document.forma.estatus.value != <%=ClientesConstants.CICLO_PENDIENTE%> && window.document.forma.estatus.value != <%=ClientesConstants.CICLO_RECHAZADO%> && window.document.forma.estatus.value != <%=ClientesConstants.CICLO_AUTORIZADO%>) {
                        alert('Estatus no permitido');
                        enableEdit();
                        return false;
                    }
                } else {
            <%if (ciclo.estatus != ClientesConstants.CICLO_PENDIENTE && ciclo.estatus != ClientesConstants.CICLO_RECHAZADO) { %>
                    if (window.document.forma.estatus.value != <%=ClientesConstants.CICLO_APERTURA%> && window.document.forma.estatus.value != <%=ClientesConstants.CICLO_ANALISIS%>) {
                        alert('Estatus no permitido');
                        enableEdit();
                        return false;
                    }
            <%} else {%>
                    if (window.document.forma.estatus.value != <%=ClientesConstants.CICLO_REVALORACION%>) {
                        alert('Estatus no permitido');
                        enableEdit();
                        return false;
                    }
            <%}%>
                }
                if (window.document.forma.diaReunion.value == 0) {
                    alert('Debe indicar el d�a de reuni�n');
                    enableEdit();
                    return false;
                }
                if (window.document.forma.horaReunion.value == 0) {
                    alert('Debe indicar la hora de reuni�n');
                    enableEdit();
                    return false;
                }
                if (window.document.forma.asesor.value == 0) {
                    alert('Debe indicar el ejecutivo');
                    enableEdit();
                    return false;
                }
                if (window.document.forma.coordinador.value == 0) {
                    alert('Debe indicar el coordinador');
                    enableEdit();
                    return false;
                }

                if (!esFormatoMoneda(window.document.forma.multaRetraso.value) || window.document.forma.multaRetraso.value <= 0) {
                    alert('El monto de la multa por retraso es inv�lida');
                    enableEdit();
                    return false;
                }

                if (!esFormatoMoneda(window.document.forma.multaFalta.value) || window.document.forma.multaFalta.value <= 0) {
                    alert('El monto de la multa por falta es inv�lida');
                    enableEdit();
                    return false;
                }
                if (window.document.forma.fechaDispersion.value == '') {
                    alert("Debe capturar la fecha de dispersi�n");
                    enableEdit();
                    return false;
                }
                if (window.document.forma.bancoDispersion.value == 0) {
                    alert('Debe indicar el banco para dispersar');
                    enableEdit();
                    return false;
                }
                if (window.document.forma.garantia.value == 0) {
                    alert('Debe indicar la garantia inicial');
                    enableEdit();
                    return false;
                }
                for (var i = 0; i < integrantes; i++) {
                    if (document.getElementById("idSolicitud" + i).value == 0 && document.getElementById("desembolso" + i).checked == true) {
                        alert("No es posible crear un ciclo con solicitud cero");
                        enableEdit();
                        return false;
                    }
                }

                if (window.document.forma.estatus.value != <%=ClientesConstants.CICLO_RECHAZADO%>) {
            <%for (int i = 0; i < integrantesCiclo.size(); i++) {%>
                    if (<%=integranteDao.getCalificacionIntegrante(integrantesCiclo.get(i).idCliente, integrantesCiclo.get(i).idSolicitud) == 2%> && document.getElementById("desembolso" +<%=i%>).checked == true) {
                        alert("No es posible ingresar un cliente con solicitud rechazada");
                        enableEdit();
                        return false;
                    }
                    else if (<%=integranteDao.esSolicitudActiva(integrantesCiclo.get(i).idCliente, integrantesCiclo.get(i).idSolicitud, idGrupo)%> && document.getElementById("desembolso" +<%=i%>).checked == true) {
                        alert("El cliente <%=integrantesCiclo.get(i).nombre%> tiene solicitud activa en otro equipo");
                        enableEdit();
                        return false;
                    }
            <%}%>
                }
                if (window.document.forma.comentario.value == "") {
                    if (window.document.forma.estatus.value != <%=ClientesConstants.CICLO_APERTURA%>) {
                        alert("Favor de ingresar un comentario");
                        enableEdit();
                        return false;
                    } else {
                        window.document.forma.comentario.value = "Ciclo en Apertura";
                    }
                }
                if (window.document.forma.estatus.value != <%=ClientesConstants.CICLO_APERTURA%> || window.document.forma.estatus.value != <%=ClientesConstants.CICLO_RECHAZADO%>) {
                    if (!validaRol(integrantes)) {
                        enableEdit();
                        return false;
                    }
                    if (!minimoIntegrantes(<%=grupo.idOperacion%>, <%=ciclo.idTipoCiclo%>, integrantes, <%=esMesaControl%>)) {
                        enableEdit();
                        return false;
                    }
                }
                /*if( !validaMontosMaximos(integrantes) ){
                 enableEdit();
                 return false;
                 }*/
                if (window.document.forma.estatus.value == <%=ClientesConstants.CICLO_AUTORIZADO%> && !getCalificacionGrupal(integrantes)) {
                    alert('El cr�dito no puede ser autorizado, verifica la calificaci�n de CC de los integrantes');
                    enableEdit();
                    return false;
                }
                <%if(otraFinanciera &&request.isUserInRole("ANALISIS_CREDITO")){%>
                    if(window.document.forma.garantia.value == 3){
                        if (!confirm("�Esta seguro de selecionar una garantia del 10% para clientes de otra financiera?")) {
                        enableEdit();
                        return false;
                    }
                    }
                <%}%>
                if (window.document.forma.estatus.value !=<%=ciclo.estatus%>) {
                    if (!confirm("�Esta seguro de registrar el ciclo con los datos actuales y cambiar el estatus?")) {
                        enableEdit();
                        return false;
                    }
                } else if (!confirm('�Esta seguro de actualizar los datos del ciclo?')) {
                    enableEdit();
                    return false;
                }
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }

            function muestraIntegrantesNuevos() {
                window.document.forma.command.value = 'agregaIntegrantesNuevos';
                window.document.forma.mostrarNuevos.value = 1;
                window.document.forma.action = "controller";
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
            function regresaAGrupo() {
                document.getElementById("botonRegresar").disabled = true;
                window.document.forma.command.value = 'consultaDetalleGrupo';
                window.document.forma.action = "controller";
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }

            function consultaCliente(idCliente) {
                window.document.forma.command.value = 'consultaCliente';
                window.document.forma.idCliente.value = idCliente;
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }

            function cuentaChecked(integrantes) {
                var numchecked = 0;
                for (var i = 0; i < integrantes; i++) {
                    var nombreDinamico = "desembolso" + i;
                    var nombreDinamicoCC = "calificacion" + i;
                    var elemento = document.getElementById(nombreDinamico);
                    var elementoCC = document.getElementById(nombreDinamicoCC);
                    if (elemento.checked == true){
                        if(elementoCC.value == <%=ClientesConstants.CALIFICACION_CIRCULO_MALA%>){
                            numchecked =-1;
                            break;
                        } else {
                            numchecked++;
                        }
                    }
                }
                return numchecked;
            }

            function cuentaCheckedCC(integrantes) {
                var numchecked = 0;
                for (var i = 0; i < integrantes; i++) {
                    var nombreDinamico = "consCC" + i;
                    var elemento = document.getElementById(nombreDinamico);
                    if (elemento.checked == true)
                        numchecked++;
                }
                return numchecked;
            }

            function abilitaBoton() {
                if (window.document.forma.estatus.value == <%=ClientesConstants.CICLO_CERRADO%> || window.document.forma.estatus.value == <%=ClientesConstants.CICLO_APERTURA%>)
                    window.document.forma.guardaCiclo.disabled = true;
                else
                    window.document.forma.guardaCiclo.disabled = false;
            }

            function cursorDefault() {
                document.body.style.cursor = "default";
            }

            function disableEdit() {
                document.body.style.cursor = "wait";
            <%if (esMesaControl) {%>
                document.getElementById("botonGuardar").disabled = true;
                document.getElementById("botonConsultaCC").disabled = true;
            <%}%>
                document.getElementById("botonRegresar").disabled = true;
            }

            function enableEdit() {
                document.body.style.cursor = "default";
            <%if (esMesaControl) {%>
                document.getElementById("botonGuardar").disabled = false;
                document.getElementById("botonConsultaCC").disabled = false;
            <%}%>
                document.getElementById("botonRegresar").disabled = false;
            }

            function days(date) {
                var day = date.getDay();
                var diaHabilitado = window.document.forma.diaReunion.value;
                return [(day == diaHabilitado), ""];
            }

            function resetDate() {
                window.document.forma.fechaDispersion.value = '';
            }

            function ayudaHistorialComentarios(numEquipo, numCiclo) {
                params = "?command=historialComentarios" + "&idGrupo=" + numEquipo + "&idCiclo=" + numCiclo;
                url = "/CEC/controller";
                abreVentana(url + params, 'scrollbars=yes', 1000, 250, true, 0, 0);
            }

            function consultaCC(integrantes) {
                disableEdit();
                window.document.forma.command.value = 'consultaMasivaCC';
                window.document.forma.action = "controller";
                var noIntegrantes = cuentaCheckedCC(integrantes);
                if (!confirm("�Deseas consultar Informacion Crediticia de los " + noIntegrantes + " integrantes?")) {
                    enableEdit();
                    return false;
                }
                window.document.forma.submit();
            }
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
    <body leftmargin="0" topmargin="0" onLoad="cursorDefault()">
        <jsp:include page="header.jsp" flush="true"/>
        <table border="0" width="100%">
            <tr>
                <td align="center">
                    <h3>Apertura Ciclo Grupal</h3>
                    <%=HTMLHelper.displayNotifications(notificaciones)%>
                    <br>
                    <form name="forma" action="controller" method="post">
                        <input type="hidden" name="command" value="">
                        <input type="hidden" name="idGrupo" value="<%=grupo.idGrupo%>">
                        <input type="hidden" name="idCliente" value="">
                        <input type="hidden" name="idCiclo" value="<%=ciclo.idCiclo%>">
                        <input type="hidden" name="idSucursal" value="<%=grupo.sucursal%>">
                        <input type="hidden" name="numIntegrantes" value="<%=integrantesCiclo.size()%>">
                        <input type="hidden" name="idColonia" value="<%=HTMLHelper.displayField(direccion.idColonia)%>">
                        <input type="hidden" name="asentamientoCP" value="<%=HTMLHelper.displayField(direccion.asentamiento_cp)%>">
                        <input type="hidden" name="idDireccion" value="<%=HTMLHelper.displayField(direccion.idDireccion)%>">
                        <input type="hidden" name="integrantesCiclo" value="<%=integrantesCiclo%>">
                        <input type="hidden" name="mostrarNuevos" value="0">
                        <table width="100%" border="0" cellpadding="0" id="mainTable">
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
                                    <select name="estatus" id="estatus" size="1">
                                        <%=HTMLHelper.displayCombo(catEstatus, ciclo.estatus)%>
                                    </select>
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
                            <tr>
                                <td width="50%" align="right">Aperturador</td>
                                <td width="50%">  
                                    <select name="aperturador" size="1" disabled>
                                        <option value="0">Seleccione...</option>
                                        <%=HTMLHelper.displayCombo(catAperturador, ciclo.aperturador)%>
                                    </select>
                                </td>
                            <input type="hidden" name="aperturador" value=<%=ciclo.aperturador%>>
                            </tr>
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
                                <td><select name="plazo" id="plazo" size="1" onKeyPress="return submitenter(this, event)">
                                        <%= HTMLHelper.displayCombo(catPlazos, ciclo.plazo)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Fecha de Dispersi�n</td>
                                <td><input type="text" name="fechaDispersion" id="fechaDispersion" size="10%" maxlength="10" value="<%=HTMLHelper.displayField(ciclo.fechaDispersion)%>" style="background-color: white" readonly="readonly" class="soloLectura">
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
                            <%if ((request.isUserInRole("CAPTURA_GARANTIA") && CatalogoHelper.esEquipoAutorizadoGarantia(grupo.idGrupo))|| (ActivaGarantia && request.isUserInRole("ANALISIS_CREDITO"))) {
                                    if (ciclo.estatus == ClientesConstants.CICLO_DISPERSADO || ciclo.estatus == ClientesConstants.CICLO_CERRADO) {
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
                            <%} else {%>
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
                                                Monto de Seguro
                                            </td>
                                            <td align="center">
                                                Monto con Seguro Financiado
                                            </td>
                                            <td align="center">
                                                Calificaci&oacute;n
                                            </td>
                                            <td align="center">
                                                Rol
                                            </td>
                                            <td align="center">
                                                Score FICO
                                            </td>
                                            <td align="center">
                                                Cons. CC
                                            </td>
                                            <%if(ciclo.estatus!=ClientesConstants.CICLO_DISPERSADO && ciclo.estatus!=ClientesConstants.CICLO_CERRADO && ciclo.estatus!=ClientesConstants.CICLO_AUTORIZADO && ciclo.estatus!=ClientesConstants.CICLO_PARADESEMBOLSAR&& ciclo.estatus!=ClientesConstants.CICLO_DESEMBOLSO){%>
                                            <td align="center">
                                                Doc. Completa
                                            </td>
                                            <%}%>
                                        </tr>
                                        <%for (int i = 0; integrantesCiclo != null && i < integrantesCiclo.size(); i++) {
                                            integrantesCiclo.get(i).montoConSeguro = integrantesCiclo.get(i).monto + integrantesCiclo.get(i).costoSeguro; %>
                                        <tr>
                                            <td align="center"> 
                                                <%=HTMLHelper.displayCheck("desembolso" + i, true)%>
                                            </td>
                                            <td align="center"> 
                                                <%=i + 1%>
                                            </td>
                                            <td align="center"> 
                                                <input type="hidden" name="idCliente<%=i%>" id="idCliente<%=i%>" value="<%=integrantesCiclo.get(i).idCliente%>">
                                                <a href="#" onClick="consultaCliente(<%=integrantesCiclo.get(i).idCliente%>)"><%=integrantesCiclo.get(i).idCliente%></a>
                                            </td>
                                            <td align="center"> 
                                                <input type="hidden" id="idSolicitud<%=i%>" name="idSolicitud<%=i%>" value="<%=integrantesCiclo.get(i).idSolicitud%>">
                                                <%=integrantesCiclo.get(i).idSolicitud%>
                                            </td>
                                            <td align="center" bgcolor=<%=(integrantesCiclo.get(i).esNuevo == 1 ? "#BDBDBD" : "#FFFFFF")%>>
                                                <input type="hidden" name="nombre<%=i%>" id="nombre<%=i%>" value="<%=integrantesCiclo.get(i).nombre%>">
                                                <input type="hidden" name="esNuevo<%=i%>" id="esNuevo<%=i%>" value="<%=integrantesCiclo.get(i).esNuevo%>">
                                                <%if (integranteDao.getCalificacionIntegrante(integrantesCiclo.get(i).idCliente, integrantesCiclo.get(i).idSolicitud) == 2) {%>
                                                <font color="red"><%=integrantesCiclo.get(i).nombre%></font>
                                                <%} else {%>
                                                <%=integrantesCiclo.get(i).nombre%>
                                                <%}%>
					</td>
					<td align="right">
                                                <input type="hidden" name="montoMaximo<%=i%>" id="montoMaximo<%=i%>" maxlength="10" value="<%=HTMLHelper.formatoMonto(ClientesUtil.calculaMontoEscalera(integrantesCiclo.get(i).idCliente, integrantesCiclo.get(i).idSolicitud) )%>">
                                                <%=HTMLHelper.formatoMonto(ClientesUtil.calculaMontoEscalera(integrantesCiclo.get(i).idCliente, integrantesCiclo.get(i).idSolicitud) )%>
					</td>
					<td align="right">
						<input type="text" name="monto<%=i%>" id="monto<%=i%>" value="<%=HTMLHelper.formatoMonto( ClientesUtil.calculaMontoSinComision( integrantesCiclo.get(i).monto, integrantesCiclo.get(i).comision, catComisionesGrupal ) )%>" size="8" maxlength="9" readonly="readonly" class="soloLectura">
					</td>
                                        <td align="right">
						<input type="hidden" name="costoSeguro<%=i%>" id="costoSeguro<%=i%>" value="<%=integrantesCiclo.get(i).costoSeguro%>" >
						<%=HTMLHelper.formatoMonto(integrantesCiclo.get(i).costoSeguro)%>
					</td>
					<td align="right">
						<input type="hidden" name="montoComision<%=i%>" id="montoComision<%=i%>" value="<%=integrantesCiclo.get(i).montoConSeguro%>" >
						<%=HTMLHelper.formatoMonto(integrantesCiclo.get(i).montoConSeguro)%>
					</td>
                                        <%if ( integrantesCiclo.get(i).calificacion == ClientesConstants.CALIFICACION_CIRCULO_BUENA || integrantesCiclo.get(i).calificacion == 0 ){ %>
						<input type="hidden" name="calificacion<%=i%>" id="calificacion<%=i%>" value="<%=integrantesCiclo.get(i).calificacion%>">
                                                <%if(integrantesCiclo.get(i).aceptaRegular == 3 || integrantesCiclo.get(i).aceptaRegular == 4){%>
                                                    <td BGCOLOR="#00E100"><center><font style="font-style: oblique;">&nbsp;A.E.</font></center></td>
                                                <%}else if(integrantesCiclo.get(i).aceptaRegular == ClientesConstants.AUTORIZACION_OTRA_FINANCIERA && otraFinanciera){%>
                                                    <td BGCOLOR="#00E100"><center><font style="font-style: oblique;">&nbsp;O.F.</font></center></td>
                                                <%}else{%>
                                                    <td BGCOLOR="#00E100"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
                                                <%}
                                        }%>
			
					<% if ( integrantesCiclo.get(i).calificacion == ClientesConstants.CALIFICACION_CIRCULO_REGULAR ){ %>
						<input type="hidden" name="calificacion<%=i%>" id="calificacion<%=i%>" value="<%=integrantesCiclo.get(i).calificacion%>">
                                               <%if(integrantesCiclo.get(i).aceptaRegular>0&&sucursalAceptaRegular){%>
						<td BGCOLOR="#ffff00"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
                                                <input type="hidden" name="aceptaRegular<%=i%>" id="aceptaRegular<%=i%>" value="1">
                                                <%}else{%>
                                                <td BGCOLOR="#BDBDBA"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
                                                <input type="hidden" name="aceptaRegular<%=i%>" id="aceptaRegular<%=i%>" value="0">
                                                <%}%>
					<%} %>
					
					<% if ( integrantesCiclo.get(i).calificacion == ClientesConstants.CALIFICACION_CIRCULO_MALA ){ %>
						<input type="hidden" name="calificacion<%=i%>" id="calificacion<%=i%>" value="<%=integrantesCiclo.get(i).calificacion%>">
						<td BGCOLOR="#FF0000"><center><b>&nbsp;</b></center></td>
					<%} %>
					
					<%if ( integrantesCiclo.get(i).calificacion == ClientesConstants.CALIFICACION_CIRCULO_NA ){ %>
						<input type="hidden" name="calificacion<%=i%>" id="calificacion<%=i%>" value="<%=integrantesCiclo.get(i).calificacion%>">
						<td BGCOLOR="#33FF99"><center><font style="font-style: oblique;">&nbsp;</font></center></td>
					<%} %>
					
					<%if ( integrantesCiclo.get(i).calificacion == ClientesConstants.CALIFICACION_CIRCULO_SIN_CONSULTA ){ sinConsulta=true;%>
						<input type="hidden" name="calificacion<%=i%>" id="calificacion<%=i%>" value="<%=integrantesCiclo.get(i).calificacion%>">
						<td BGCOLOR="#FFEBCD"><center><font style="font-style: oblique;">Sin consulta</font></center></td>
					<%} %>
                                        <td align="center">
						<select name="rol<%=i%>" id="rol<%=i%>" size="1" value="<%=integrantesCiclo.get(i).rol%>" >
						<%=HTMLHelper.displayCombo(catRoles, integrantesCiclo.get(i).rol)%>
						</select>
					</td>
                                        <%color = HTMLHelper.getCellColor(integrantesCiclo.get(i).calificacionAutomatica);%>
					<td align="center" BGCOLOR=<%=color%>>
						<center><b><%=integrantesCiclo.get(i).scoreFico%></b></center>
					</td>
                                        <%if(integrantesCiclo.get(i).consultaCC == 1)
                                            consultaCC = "checked";
                                        else
                                            consultaCC = "disabled";
                                        %>
					<td align="center">
                                                <input type="checkbox" name="consCC<%=i%>" id="consCC<%=i%>" value="si" <%=consultaCC%>>
					</td>
                                        <%if(ciclo.estatus!=ClientesConstants.CICLO_DISPERSADO && ciclo.estatus!=ClientesConstants.CICLO_CERRADO && ciclo.estatus!=ClientesConstants.CICLO_AUTORIZADO && ciclo.estatus!=ClientesConstants.CICLO_PARADESEMBOLSAR&& ciclo.estatus!=ClientesConstants.CICLO_DESEMBOLSO){%>
                                        <td align="center">
                                            <%if(integrantesCiclo.get(i).RenovacionDoc>0){%>
                                                <input type="checkbox" name="Doc<%=i%>" id="Doc<%=i%>" value="si" disabled 
                                                <%if(integrantesCiclo.get(i).DocCompletos>0){%>
                                                    checked
                                                <%}%>
                                                >
                                            <%}%>
                                        </td>
                                        <%}%>
                                </tr>
<%
total += FormatUtil.redondeaMoneda(ClientesUtil.calculaMontoSinComision( integrantesCiclo.get(i).monto, integrantesCiclo.get(i).comision, catComisionesGrupal ));
totalConComision += integrantesCiclo.get(i).monto;
totalConSegugoFinanciado += integrantesCiclo.get(i).montoConSeguro;
totalCostoSeguro += integrantesCiclo.get(i).costoSeguro;
numIntegrantes = i;
}%>
				<tr>
					<td colspan="5" align="center">  
						Total 
					</td>
                                        <td>
						&nbsp;
					</td>
					<td align="right">
						<input type="hidden" name="montoTotal" value="<%=total%>">
						<%=HTMLHelper.formatoMonto(total) %>
					</td>
					<% if(grupo.idOperacion==ClientesConstants.GRUPAL){%>
					<td align="right">
						<%=HTMLHelper.formatoMonto(totalCostoSeguro)%>
					</td>
					<%}%>
					<td align="right">
						<input type="hidden" name="montoTotalConComision" value="<%=totalConComision%>">
						<%=HTMLHelper.formatoMonto(totalConSegugoFinanciado)%>
					</td>
				</tr>
			</table>
		</td>
	</tr>
        <%if((ciclo.estatus == ClientesConstants.CICLO_APERTURA||ciclo.estatus == ClientesConstants.CICLO_RECHAZADO) && integrantesNuevos != null && muestraNuevosIntegrantes==0){%>
        <tr><td colspan="2" align="center"><a href="#" onclick="muestraIntegrantesNuevos();">Agrega Integrantes</a></td></tr>
        <%}%>
        <tr>
            <td align="center" colspan="2">
                <a href="generaFichasPagosSinMonto.jsp?idOperacion=3&idCiclo=<%=idCiclo%>">Ficha de Pago sin Monto</a>&nbsp;&nbsp;&nbsp;&nbsp;
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
        <%if((esMesaControl&&ciclo.estatus==9)||(!esMesaControl&&(ciclo.estatus==3||ciclo.estatus==4||ciclo.estatus==5))||(request.isUserInRole("manager"))){ %>
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
                    <%if (!nuevo){%>
                    <br><input type="button" id="botonGuardar" name="guardaCiclo" value="Guardar Ciclo" onClick="guardaCicloApertura(<%=numIntegrantes + 1%>)">                    
                    <%}if(request.isUserInRole("CONSULTA_CREDITICIA")){ %>
                    <input type="button" id="botonConsultaCC" value="Consulta CC" onClick="consultaCC(<%=numIntegrantes + 1%>)">
                    <%}%>
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
