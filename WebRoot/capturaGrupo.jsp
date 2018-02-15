<%@page import="java.util.Vector"%>
<%@page import="com.sicap.clientes.dao.SolicitudDAO"%>
<%@page import="com.sicap.clientes.dao.BitacoraCicloDAO"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.vo.UsuarioVO"%>
<%@ page import="com.sicap.clientes.vo.GrupoVO"%>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="com.sicap.clientes.util.GrupoUtil"%>
<%@ page import="com.sicap.clientes.vo.SaldoIBSVO"%>

<%
    //Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
    Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
    UsuarioVO usuario = (UsuarioVO) session.getAttribute("USUARIO");
    TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
    TreeMap catEstatus = CatalogoHelper.getCatalogoEstatusCiclo(true, 0);
    TreeMap catIgnorarAlertasFuturas = CatalogoHelper.getCatalogoIgnorarAlertasFuturas();
//TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
    TreeMap catCalificacionGrupal = null;
    TreeMap catTipogrupal = CatalogoHelper.getCatalogo(ClientesConstants.CAT_TIPOGRUPAL);
    TreeMap tasas = new TreeMap();

    GrupoVO grupo = new GrupoVO();
    SaldoIBSVO saldoIC = new SaldoIBSVO(); 
    BitacoraCicloDAO bitacoraCicloDao = new BitacoraCicloDAO();
    SolicitudDAO solicitudDao = new SolicitudDAO(); 
    int idGrupo = 0;
    boolean aperturaCiclo = false;
    boolean otraFinanciera = false;
    if (request.getParameter("command").equals("capturaGrupo")) {
        session.removeAttribute("GRUPO");
    }

    if (session.getAttribute("GRUPO") != null) {
        grupo = (GrupoVO) session.getAttribute("GRUPO");
        idGrupo = grupo.idGrupo;
        aperturaCiclo = GrupoUtil.aplicaApertura(grupo);
    }
    if(session.getAttribute("SALDOIC")!=null){
        saldoIC  = (SaldoIBSVO) session.getAttribute("SALDOIC");
    }
    boolean cicloActivo = GrupoUtil.tieneCicloActivo(grupo);
    int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
    boolean isMesaControl = request.isUserInRole("ANALISIS_CREDITO");
    boolean isRestructuraFlag = grupo.idOperacion == ClientesConstants.REESTRUCTURA_GRUPAL;
    boolean adminAlertas = request.isUserInRole("ADMINISTRADOR_ALERTAS");
    boolean muestraIC = solicitudDao.getGrupoIC(idGrupo);
    boolean SucursalesIxaya = CatalogoHelper.esSucursaldeIxaya(usuario.idSucursal);
    if (grupo.otraFinaciera == 1){
        otraFinanciera = true;
    }
    
    String seguro = "";
    if (grupo != null && grupo.ciclos != null && grupo.ciclos.length > 0) {
        tasas = CatalogoHelper.getCatalogoTasas(grupo.idOperacion);
    }
    if (GrupoUtil.decideCatalogo(grupo)== ClientesConstants.CATALOGO_TASA_NUEVA){
        catCalificacionGrupal = CatalogoHelper.getCatalogoTasasNuevas();
    }
    else {
        catCalificacionGrupal = CatalogoHelper.getCatalogoCalificacionGrupal();
    }

%>

<html>
    <head>
        <title>Grupo</title>

        <script type="text/javascript">

            function guardaGrupo() {
                window.document.forma.command.value = 'guardaGrupo';
                if (window.document.forma.nombre.value == '') {
                    alert('Debe capturar el nombre del grupo');
                    return false;
                }
                if (window.document.forma.idSucursal.value == 0) {
                    alert('Debe indicar la sucursal');
                    return false;
                }
                <%if (SucursalesIxaya) {%>
                    alert('El movimiento se debe de realizar en el sistema JUCAVI');
                    return false;
                <%}%>
                if (window.document.forma.fechaFormacion.value != '') {
                    if (!esFechaValida(window.document.forma.fechaFormacion, false)) {
                        alert("La Fecha de formación es inv\u00e1lida");
                        return false;
                    }
                } else {
                    alert("Debe capturar la Fecha de formación");
                    return false;
                }
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
            
            function consultaCliente(idCliente) {
                window.opener.document.forma.command.value = 'consultaCliente';
                window.opener.document.forma.idCliente.value = window.document.forma.idCliente.value;
                window.opener.document.forma.submit();
            }

            function capturaCicloGrupal() {
                document.getElementById("botonNuevoCiclo").disabled = true;
                <%if (SucursalesIxaya) {%>
                    alert('El movimiento se debe de realizar en el sistema JUCAVI');
                    return false;
                <%}%>
            <% if (grupo.ciclos != null && grupo.ciclos.length > 0) {%>
                //window.document.forma.command.value='renuevaCicloGrupal';
                window.document.forma.command.value = 'aperturaNuevoCiclo';
            <%} else {
                if (grupo.idOperacion == ClientesConstants.GRUPAL) {%>
                //window.document.forma.command.value='capturaCicloGrupal';
                window.document.forma.command.value = 'aperturaNuevoCiclo';
            <%} else {%>
                window.document.forma.command.value = 'capturaCicloRestructura';
            <%}
                }%>
                window.document.forma.command.idCiclo = 0;
                if (window.document.forma.idOperacion.value == 5) {
                    //window.document.forma.nuevoCiclo.disabled = true;
                    alert('Por el momento se encuentran suspendidas las reestructuras grupales en SICAP');
                } else {
                    //window.document.forma.nuevoCiclo.disabled = false;
                    document.body.style.cursor = "wait";
                    window.document.forma.submit();
                }

            }

            function capturaCicloApertura() {
                document.getElementById("botonApertura").disabled = true;
                <%if (SucursalesIxaya) {%>
                    alert('El movimiento se debe de realizar en el sistema JUCAVI');
                    return false;
                <%}%>
                window.document.forma.command.value = 'aperturaNuevoCiclo';
                //window.document.forma.command.idCiclo=0;
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
            
            function capturaCicloOtraFinanciera() {
                document.getElementById("botonNuevoCiclo").disabled = true;
                document.getElementById("botonNuevoOtra").disabled = true;
                <%if (SucursalesIxaya) {%>
                    alert('El movimiento se debe de realizar en el sistema JUCAVI');
                    return false;
                <%}%>
            <% if (grupo.ciclos != null && grupo.ciclos.length > 0) {%>
                window.document.forma.command.value = 'renuevaCicloGrupal';
            <%} else {
                if (grupo.idOperacion == ClientesConstants.GRUPAL) {%>
                window.document.forma.command.value = 'capturaCicloGrupal';
            <%} else {%>
                window.document.forma.command.value = 'capturaCicloRestructura';
            <%}
                }%>
                window.document.forma.command.idCiclo = 0;
                window.document.forma.otraFinanciera.value =<%=ClientesConstants.CICLO_OTRA_FINANCIERA%>
                if (window.document.forma.idOperacion.value == 5) {
                    window.document.forma.nuevoCicloOtro.disabled = true;
                    alert('Por el momento se encuentran suspendidas las reestructuras grupales en SICAP');
                } else {
                    window.document.forma.nuevoCicloOtro.disabled = false;
                    alert("otraFinanciera" + window.document.forma.command.value);
                    document.body.style.cursor = "wait";
                    window.document.forma.submit();
                }

            }

            function consultaCicloGrupal(idCiclo, estatus, estatusCiclo, estatusIC, estatusIC2, semDisp, aceptaAdicional) {
                //document.getElementById("td").disabled = true;
                <%if (grupo.idOperacion == ClientesConstants.GRUPAL) {%>
                    if(semDisp == 2){
                        window.document.forma.command.value = 'consultaIntercicloAutorizado';
                        if(estatusIC > 2 && estatusIC < 10){
                            window.document.forma.command.value = 'consultaCicloGrupalIC';
                        } else if (estatusIC==1||estatusIC==0){
                            window.document.forma.command.value = 'consultaCicloGrupal';
                        }                        
                    } else if(semDisp == 4){
                        window.document.forma.command.value = 'consultaIntercicloAutorizado';
                        if(estatusIC2 > 2 && estatusIC2 < 10){
                            window.document.forma.command.value = 'consultaCicloGrupalIC';
                        } else if (estatusIC2==1||estatusIC2==0){
                            window.document.forma.command.value = 'consultaCicloGrupal';
                        }
                    } else {
                        if (estatusCiclo > 2 && estatusCiclo < 10) {
                            window.document.forma.command.value = 'consultaCicloApertura';
                        } else if (estatusCiclo >= 10){
                            window.document.forma.command.value = 'consultaCicloAutorizado';
                            //window.document.forma.command.value = 'consultaCicloGrupal';
                        } else {
                            if (aceptaAdicional > 0 && estatusCiclo == 1){
                                window.document.forma.semAdicional.value = aceptaAdicional;
                                window.document.forma.command.value = 'consultaCicloGrupalAdicional';
                            }else if (estatus == 7) {
                                alert("El ciclo del grupo pertenece a TCI");
                                <%if (request.isUserInRole("admin")) {%>
                                    window.document.forma.command.value = 'consultaCicloGrupal';
                                <%} else {%>
                                    window.document.forma.command.value = 'consultaGrupo';
                                <%}%>
                            } else if (estatus == 8) {
                                alert("El ciclo del equipo no pertenece a CEC");
                                <%if (request.isUserInRole("admin")) {%>
                                    window.document.forma.command.value = 'consultaCicloGrupal';
                                <%} else {%>
                                    window.document.forma.command.value = 'consultaGrupo';
                                <%}%>
                            } else {
                                window.document.forma.command.value = 'consultaCicloGrupal';
                            }
                        }
                    }
              <%} else {%>
                    window.document.forma.command.value = 'consultaCicloRestructura';
                <%}%>
                    window.document.forma.idCiclo.value = idCiclo;
                    document.body.style.cursor = "wait";
                    window.document.forma.submit();
            }
            
            function ConsultaCicloGrupalIC(idCicloIC, estatusIC){
                
                if(estatusIC == <%=ClientesConstants.CICLO_AUTORIZADO%> || estatusIC == <%=ClientesConstants.CICLO_DESEMBOLSO%>){
                    window.document.forma.command.value = 'consultaIntercicloAutorizado';
                } else if (estatusIC == <%=ClientesConstants.CICLO_DISPERSADO%>){
                    window.document.forma.command.value ='consultaIntercicloDispersado';
                }
                else {
                    window.document.forma.command.value = 'consultaCicloGrupalIC';
                }
                window.document.forma.idCiclo.value = idCicloIC;
                window.document.forma.interCiclo.value = "1";
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }

            function regresaAGrupos() {
                window.document.forma.command.value = 'administraGrupos';
                window.document.forma.idGrupo.value = 0;
                window.document.forma.idSucursal.value = 0;
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }

            function regresaABusquedaGrupos() {
                //document.getElementById("boton").disabled = true;
                window.document.forma.command.value = 'administraGrupos';
                window.document.forma.action = "controller";
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }

            function consultaDetalleMonitor(idGrupo, idCiclo) {
                //document.getElementById("boton").disabled = true;
                window.document.forma.command.value = 'consultaDetalleMonitor';
                window.document.forma.idGrupoDetallada.value = idGrupo;
                window.document.forma.idCiclo.value = idCiclo;
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }

            function validaTipo() {
                if (window.document.forma.idOperacion.value == 5) {
                    window.document.forma.boton.disabled = true;
                    alert('Por el momento se encuentran suspendidas las reestructuras grupales en SICAP');
                } else {
                    window.document.forma.boton.disabled = false;
                }

            }

            function capturaCicloRestructura() {
                document.getElementById("botonRestructura").disabled = true;
                window.document.forma.command.value = 'capturaCicloRestructura';
                window.document.forma.idOperacion.value =<%=ClientesConstants.REESTRUCTURA_GRUPAL%>
                window.document.forma.idCiclo.value = 0;
                res = confirm('Esta apunto de generar un nuevo grupo de restructura. ¿Esta seguro de que desea registrar el nuevo grupo?');
                if (!res)
                    return res;
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }


            function descartarAlertas(numeroCiclo) {

                window.document.forma.command.value = "ignorarAlertasFuturas";
                window.document.forma.idCiclo.value = numeroCiclo;

                if (window.document.forma.motivoIgnorarAlertas.value == 0) {
                    alert('Debe seleccionar un motivo');
                    return false;
                }
                var comentario = prompt("Ingrese un comentario", "");
                if (comentario == null || comentario == "") {
                    alert('Debe agregar un comentario');
                    return false;
                }
                window.document.forma.comentarioIgnorarFuturasAlertas.value = comentario;
                res = confirm('¿Esta seguro de ignorar todas las alertas futuras que se generen de este ciclo? ');
                if (!res)
                    return res;
                document.body.style.cursor = "wait";
                window.document.forma.submit();

            }

            function cambiaEstatusCiclo(idCiclo, idEstatus, estatusIC, estatusIC2, idCredito) {
                if (confirm("¿Estás seguro de comenzar el proceso de análisis?")) {
                    if(estatusIC2 != 0 || estatusIC != 0){
                        if(estatusIC2 != 0){
                            window.document.forma.semDisp.value = 4;
                            window.document.forma.estatus.value = estatusIC2;
                            window.document.forma.estatusAsignar.value = estatusIC2;
                        }else if (estatusIC != 0){
                            window.document.forma.semDisp.value = 2;
                            window.document.forma.estatus.value = estatusIC;
                            window.document.forma.estatusAsignar.value = estatusIC;
                            
                        }
                         window.document.forma.idCredito.value = idCredito;
                         window.document.forma.subproducto.value = 1;
                    }else {
                        window.document.forma.estatusAsignar.value = idEstatus;
                    }
                    window.document.forma.idCiclo.value = idCiclo;                    
                    window.document.forma.accion.value = 2;
                    window.document.forma.command.value = 'cambiaEstatusCiclo';
                    window.document.forma.submit();
                }
            }

            function rechazaConsulta() {
                alert("El equipo que deseas consultar está asignado a otro usuario");
                return false;
            }

            function cursorDefault() {
                document.body.style.cursor = "default";
            }

            //-->
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css"></head>
    <body leftmargin="0" topmargin="0" onLoad="cursorDefault()">

        <jsp:include page="header.jsp" flush="true"/>
        <table border="0" width="100%">
            <tr>
                <td align="center">
                    <h3>Alta/Modificaci&oacute;n de Grupo</h3>
                    <%=HTMLHelper.displayNotifications(notificaciones)%>
                    <br>
                    <form name="forma" action="controller" method="post">
                        <input type="hidden" name="command" value=""/>
                        <input type="hidden" name="idGrupo" value="<%=idGrupo%>"/>
                        <input type="hidden" name="idGrupoDetallada" value=""/>
                        <input type="hidden" name="idCiclo" value=""/>
                        <input type="hidden" name ="interCiclo" value ="0"/>
                        <input type="hidden" name="nombreGrupo" value="<%=HTMLHelper.displayField(grupo.nombre)%>"/>
                        <input type="hidden" name="idSolicitud" value="<%=idSolicitud%>"/>
                        <input type="hidden" name="comentarioIgnorarFuturasAlertas" value=""/>
                        <input type="hidden" name="otraFinanciera" value=""/>
                        <input type="hidden" name="campSeguro" value=""/>
                        <input type="hidden" name="estatusAsignar" value=""/>
                        <input type="hidden" name="semDisp" value="0">
                        <input type="hidden" name="subproducto" value="0">
                        <input type="hidden" name="accion" value=""/>
                        <input type="hidden" name="estatus" value="0"/>
                        <input type="hidden" name="idCredito" value="0"/>                        
                        <input type="hidden" name="semAdicional" value="0"/>
                        
                        

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
                                    <input type="text" name="nombre" size="45" maxlength="150" value="<%=HTMLHelper.displayField(grupo.nombre)%>">
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Sucursal</td>
                                <td width="50%">  
                                    <select name="idSucursal" size="1">
                                        <%=HTMLHelper.displayCombo(catSucursales, grupo.sucursal)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Fecha de formaci&oacute;n</td>
                                <td width="50%">  
                                    <input type="text" name="fechaFormacion" id="fechaFormacion" size="10" maxlength="10" value="<%=HTMLHelper.displayField(grupo.fechaFormacion)%>" >(dd/mm/aaaa)
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Tipo de Grupo<br></td>
                                <td width="50%">  
                                    <input type="hidden" name="idOperacion"  value="<%=(grupo.idOperacion == 0 ? 3 : grupo.idOperacion)%>" >
                                    <b><%=(isRestructuraFlag ? "Restructura" : "Grupal")%></b>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Clasificacion comportamiento<br></td>
                                <td width="50%">
                                    <%if (isMesaControl && grupo.idOperacion == ClientesConstants.GRUPAL && idGrupo != 0) {%> 
                                    <select name="calificacionGrupal" size="1">
                                        <%=HTMLHelper.displayCombo(catCalificacionGrupal, grupo.calificacion)%>
                                    </select>
                                    <%} else {%>
                                    <input type="text" name="comportamiento" size="20" maxlength="150" value="<%=HTMLHelper.displayField(grupo.calificacion)%>" readonly="readonly" size="10" class="soloLectura">				
                                    <%}%> 
                                </td>
                            </tr>
                            <%if (isMesaControl && grupo.idOperacion == ClientesConstants.GRUPAL && idGrupo != 0) {%>
                            <tr>
                                <td width="50%" align="right">Otra Financiera<br></td>
                                <td width="50%">   
                                    <%=HTMLHelper.displayCheck("otraFin", otraFinanciera)%>
                                </td>                                
                            </tr>
                            <%}%>
                            <tr>
                                <td align="center" colspan="2">
                                    <br>
                                    <%if (!cicloActivo || isMesaControl) {%><input type="button" id="botonEnv" value="Enviar" onClick="guardaGrupo()" name="boton"><%}%>
                                        <input type="button" id="botonReg" value="Regresar" onClick="regresaABusquedaGrupos()">
                                </td>
                            </tr>
                            <tr>
                                <td colspan="4">&nbsp;</td>
                            </tr>
                            <!--</table>--><br>
                            <br>
                            <br>
                            <%if (grupo != null && grupo.ciclos != null && grupo.ciclos.length > 0) {%>
                            <table border="1" bordercolor="#00003" cellspacing="0">
                                <tr>
                                    <td colspan="1" align="center">Ciclo</td>
                                    <td colspan="1" align="center">Fecha captura</td>
                                    <td colspan="1" align="center">Liquidado</td>
                                    <td colspan="1" align="center">Estatus</td>
                                    <td colspan="1" align="center">Tipo</td>
                                    <td colspan="1" align="center">Historico de alertas</td>
                                    <td colspan="1" align="center">Con Seguro</td>         
                                    <% if (adminAlertas) {%>
                                    <td colspan="1" align="center">Elimina futuras alertas</td>
                                    <% } if ( isMesaControl ){%>
                                    <td colspan="1" align="center">Tasa</td>
                                    <td colspan="1" align="center">Atrasos</td>
                                    <%}%>
                                </tr>
                                <%	for (int i = 0; i < grupo.ciclos.length; i++) {
                                        if (grupo.ciclos[i].estatus == ClientesConstants.ESTATUS_CAPTURADO) {
                                            cicloActivo = true;
                                        }
                                %>
                                <input type="hidden" name="idCiclo<%=i%>" value="<%=i%>"/>
                                <tr>
                                    <%if ( isMesaControl && !request.isUserInRole("ASIGNACION_EQUIPOS") && grupo.ciclos[i].estatus != 10) {%>
                                        <%if (bitacoraCicloDao.getUsuarioAsignado(idGrupo, grupo.ciclos[i].idCiclo).equals(request.getRemoteUser())) {%>
                                            <%if ((grupo.ciclos[i].estatus == 8 || grupo.ciclos[i].estatus == 6)||(grupo.ciclos[i].estatusIC == 8 || grupo.ciclos[i].estatusIC == 6)||(grupo.ciclos[i].estatusIC2 == 8 || grupo.ciclos[i].estatusIC2 == 6)) {%>
                                                <td align="center"><a href="#" onClick="cambiaEstatusCiclo(<%=grupo.ciclos[i].idCiclo%>,<%=grupo.ciclos[i].estatus%>,<%=grupo.ciclos[i].estatusIC%>,<%=grupo.ciclos[i].estatusIC2%>,<%=grupo.ciclos[i].idCreditoIBS%>)"><%=grupo.ciclos[i].idCiclo%></a></td>
                                            <%}else{%>                                    
                                                <td align="center"><a href="#" onClick="consultaCicloGrupal(<%=grupo.ciclos[i].idCiclo%>,<%=grupo.ciclos[i].estatusT24%>,<%=grupo.ciclos[i].estatus%>,<%=grupo.ciclos[i].estatusIC%>,<%=grupo.ciclos[i].estatusIC2%>,<%=grupo.ciclos[i].semDisp%>,<%=grupo.ciclos[i].aceptaAdicional%>)"><%=grupo.ciclos[i].idCiclo%></a></td>
                                            <%}%>
                                        <%} else if (bitacoraCicloDao.getUsuarioAsignado(idGrupo, grupo.ciclos[i].idCiclo).equals("sistema") || grupo.ciclos[i].estatus >= 10 || grupo.ciclos[i].estatus == 1 || grupo.ciclos[i].estatus == 2) {%>
                                            <td align="center"><a href="#" onClick="consultaCicloGrupal(<%=grupo.ciclos[i].idCiclo%>,<%=grupo.ciclos[i].estatusT24%>,<%=grupo.ciclos[i].estatus%>,<%=grupo.ciclos[i].estatusIC%>,<%=grupo.ciclos[i].estatusIC2%>,<%=grupo.ciclos[i].semDisp%>,<%=grupo.ciclos[i].aceptaAdicional%>)"><%=grupo.ciclos[i].idCiclo%></a></td>
                                        <%} else if (!bitacoraCicloDao.getUsuarioAsignado(idGrupo, grupo.ciclos[i].idCiclo).equals("")) {%>
                                            <td align="center"><a href="#" onClick="rechazaConsulta()"><%=grupo.ciclos[i].idCiclo%></a></td>
                                        <%} else {%>
                                            <td align="center"><a href="#" onClick="consultaCicloGrupal(<%=grupo.ciclos[i].idCiclo%>,<%=grupo.ciclos[i].estatusT24%>,<%=grupo.ciclos[i].estatus%>,<%=grupo.ciclos[i].estatusIC%>,<%=grupo.ciclos[i].estatusIC2%>,<%=grupo.ciclos[i].semDisp%>,<%=grupo.ciclos[i].aceptaAdicional%>)"><%=grupo.ciclos[i].idCiclo%></a></td>
                                        <%}%>
                                      <%}else {%>
                                            <td align="center"><a href="#" onClick="consultaCicloGrupal(<%=grupo.ciclos[i].idCiclo%>,<%=grupo.ciclos[i].estatusT24%>,<%=grupo.ciclos[i].estatus%>,<%=grupo.ciclos[i].estatusIC%>,<%=grupo.ciclos[i].estatusIC2%>,<%=grupo.ciclos[i].semDisp%>,<%=grupo.ciclos[i].aceptaAdicional%>)"><%=grupo.ciclos[i].idCiclo%></a></td>
                                        <%}%>
                                    <td align="center"><%=HTMLHelper.displayField(grupo.ciclos[i].fechaCaptura)%></td>
                                    <td align="center"><%if (grupo.ciclos[i].saldo.getEstatus() == 3) {%>SI<%} else {%>NO<%}%></td>
                                    <td align="center"><%=HTMLHelper.getDescripcion(catEstatus, grupo.ciclos[i].estatus)%></td>
                                    <td align="center"><%=grupo.ciclos[i].idTipoCiclo == 2 ? "REFINANCIAMIENTO" : grupo.idOperacion == 5 ? "RESTRUCTURA" : "NATURAL"%></td>
                                    <td align="center" id="boton"><a href="#" onclick="consultaDetalleMonitor(<%=grupo.ciclos[i].idGrupo%>,<%=grupo.ciclos[i].idCiclo%>)">Abrir</a></td>
                                    <%if (grupo.ciclos[i].seguro == 1) {
                                            seguro = "value='si' checked";
                                        } else if (grupo.ciclos[i].seguro == 0) {
                                            seguro = "";
                                        }%>
                                        
                                    <td align="center"><input type="checkbox" name="conSeguro" id="conSeguro" <%=seguro%> disabled/></td>
                                    
                                    <%if (adminAlertas) {%>
                                    <td align="center">
                                        <% if (grupo.ciclos[i].estatus == 1 && grupo.ciclos[i].estatusAlertasPago != 2) {%>
                                        <select name="motivoIgnorarAlertas" id="motivoIgnorarAlertas" size="1" value="0" >
                                            <%=HTMLHelper.displayCombo(catIgnorarAlertasFuturas, 0)%>
                                        </select>
                                        <a href="#" onClick="descartarAlertas(<%=grupo.ciclos[i].idCiclo%>)">Ignorar</a>
                                        <% } else {%>
                                        &nbsp;
                                        <% }%>
                                    </td>
                                    <% } if ( isMesaControl ){%>
                                    <td align="center"> <%=CatalogoHelper.getDescripcionTasa(grupo.ciclos[i].tasa, tasas)%></td>
                                    <td align="center"><%=HTMLHelper.displayField(grupo.ciclos[i].getAtrasos(), true)%></td>
                                    <% } %>
                                   </tr>
                                <%}%>
                            </table>
                            <tr>
                                <%}%>
                                <%--System.out.println("grupo.idGrupo "+grupo.idGrupo+" cicloActivo "+cicloActivo+" grupo.calificacion.equals(B) "+grupo.calificacion.equals("B")+" isRestructuraFlag "+isRestructuraFlag+" CatalogoHelper.esSucursalAutorizadaComunal(grupo.sucursal) "+CatalogoHelper.esSucursalAutorizadaComunal(grupo.sucursal));--%>
                                <%if (grupo.idGrupo != 0 && !grupo.calificacion.equals("B") && !isRestructuraFlag) {%>
                                <td align="center" colspan="2" id="td_nuevo">
                                    <!--<br><input type="button" value="Nuevo ciclo" onClick="capturaCicloGrupal()" name="nuevoCiclo">-->
                                    <%if (!cicloActivo) {%>
                                            <br><input type="button" id="botonNuevoCiclo" value="Nuevo Ciclo" onClick="capturaCicloGrupal()">
                                    <%if (request.isUserInRole("manager")) { %>
                                            <br><br><input type="button" id="botonNuevoOtra" value="Nuevo Ciclo Otra Financiera" onClick="capturaCicloOtraFinanciera()" name="nuevoCicloOtro">
                                    <%}
                                    } else if (aperturaCiclo) {%>
                                            <br><input type="button" id="botonApertura" value="Apertura Ciclo" onClick="capturaCicloApertura()">
                                    <%}%>
                                    <%} else if (grupo.calificacion.equals("B") && cicloActivo && grupo.idGrupo != 0 && grupo.ciclos.length > 0 && grupo.ciclos[grupo.ciclos.length - 1].estatusT24 == 2 && request.isUserInRole("ORIGINACION_REESTRUCTURA")) {%>
                                    <!--<br><input type="button" id="botonRestructura" value="Restructura ciclo activo" onClick="capturaCicloRestructura()">-->
                                    <%}%>
                                </td>
                            </tr>

                    </form>
        </table>
        <br></body>
</html>
