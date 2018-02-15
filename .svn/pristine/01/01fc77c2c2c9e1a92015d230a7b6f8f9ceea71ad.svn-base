<%@page import="com.sicap.clientes.vo.BuroInternoVO"%>
<%@page import="com.sicap.clientes.dao.SolicitudDAO"%>
<%@page import="com.sicap.clientes.dao.IntegranteCicloDAO"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="java.util.TreeMap"%>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.SolicitudVO"%>
<%@ page import="com.sicap.clientes.vo.UsuarioVO"%>
<%@ page import="com.sicap.clientes.vo.SaldoIBSVO"%>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.util.SolicitudUtil"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.vo.BuroInternoVO"%>
<%@ page import="com.sicap.clientes.dao.BuroInternoDAO"%>

<%
    boolean estatusSolicitud = false;
    boolean bBuroInterno = false;
    int nuevaSolicitud = 0;
    UsuarioVO usuario = (UsuarioVO) session.getAttribute("USUARIO");
    ClienteVO cliente = (ClienteVO) session.getAttribute("CLIENTE");
    BuroInternoVO BuroInterno = new BuroInternoVO();
    IntegranteCicloDAO integranteCicloDAO = new IntegranteCicloDAO();
    TreeMap estatusCredito = CatalogoHelper.getCatalogoEstatusCredito();
    estatusSolicitud = new SolicitudDAO().getEstatusSolicitud(cliente.idCliente);
    nuevaSolicitud = new SolicitudUtil().validaSolicitudNueva(cliente.idCliente, request);
    int activoTCI = 0;
    activoTCI = new IntegranteCicloDAO().getIntegranteTCI(cliente.idCliente);
    BuroInterno = new BuroInternoDAO().buscaCliente(cliente.idCliente);
    if(BuroInterno==null){
        bBuroInterno = true;
    }
    
%>

<html>
    <head>
        <title>Solicitudes por cliente</title>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
        <script language="Javascript" src="./js/functions.js"></script>
        <script>

            function consultaSolicitud(idSolicitud) {
                document.getElementById("td").disabled = true;
                window.document.forma.target = "_top";
                window.document.forma.command.value = 'consultaSolicitudCliente';
                window.document.forma.idSolicitud.value = idSolicitud;
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }


            function registraReestructura() {
                document.getElementById("td").disabled = true;
                window.document.forma.command.value = 'registraReestructura';
                if (confirm('¿ Esta usted seguro que desea reestructurar ?')) {
                    document.body.style.cursor = "wait";
                    window.document.forma.submit();
                } else {
                    return false;
                }
            }

            function registraSolicitud() {
                document.getElementById("td").disabled = true;
                <%if (CatalogoHelper.esSucursaldeIxaya(cliente.idSucursal)) {%>
                    alert('El movimiento se debe de realizar en el sistema JUCAVI');
                    return false;
                <%}%>
                window.document.forma.command.value = 'registraSolicitud';
                window.document.forma.idSolicitud.value = 0;
                var opcion = window.document.forma.producto.options[window.document.forma.producto.selectedIndex];
                if (window.document.forma.producto.value == 0) {
                    alert('Debe seleccionar un producto para la nueva solicitud');
                    window.document.forma.producto.focus();
                } else {
                    if (confirm('¿ Esta usted seguro de que desea registrar una nueva solicitud al cliente para el producto : ' + opcion.text + ' ?')) {
                        if ( window.document.forma.producto.value != 3) {
                            alert('La sucursal no está autorizada para registrar el producto');
                            return false;
                        } else if (<%=activoTCI == 1%>) {
                            if (<%=request.isUserInRole("ANALISIS_CREDITO")%>) {
                                if (confirm("El cliente esta dentro de TCI. ¿Desea generar una nueva solicitud?")) {
                                    document.body.style.cursor = "wait";
                                    window.document.forma.submit();
                                }
                            } else {
                                alert("El cliente se encuentra dentro de TCI");
                                return false;
                            }
                        } else if (<%=!estatusSolicitud%>) {
                            if (<%=request.isUserInRole("ANALISIS_CREDITO")%>) {
                                if (confirm("El cliente tiene solicitudes activas. ¿Desea generar una nueva solicitud?")) {
                                    document.body.style.cursor = "wait";
                                    window.document.forma.submit();
                                }
                            } else {
                                alert("No es posible tener dos solicitudes activas");
                                return false;
                            }
                        } else {
                            document.body.style.cursor = "wait";
                            window.document.forma.submit();
                        }

                    }
                    else {
                        return false;
                    }
                }
            }
            
            function mantenimientoCliente(idCliente) {
                document.getElementById("td").disabled = true;
                window.document.forma.command.value = 'mantenimientoCliente';
                window.document.forma.idCliente.value = idCliente;
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }

            function nuevaConsulta() {
                document.getElementById("td").disabled = true;
                window.document.forma.target = "_top";
                window.document.forma.command.value = 'inicio';
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }

        </script>
    </head>
    <%

        TreeMap catProductos = CatalogoHelper.getCatalogoOperaciones(usuario, false);
        TreeMap catProductosEtiqueta = CatalogoHelper.getCatalogoOperaciones(usuario, true);
        TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
        TreeMap catDesembolso = CatalogoHelper.getCatalogoDesembolso();
        Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
        boolean mostrarBotonReestructura = false;
        
        
    %>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center>
        <form name="forma" action="controller" method="post">
            <input type="hidden" name="command" value="">
            <input type="hidden" name="idSolicitud" value="">
            <input type="hidden" name="idCliente" value="">
            <table border="0" width="100%">
                <tr>
                    <td valign="top">
                    </td>
                    <td align="center">

                        <h3>Alta/Modificaci&oacute;n de cliente</h3>
                        <%=HTMLHelper.displayNotifications(notificaciones)%>
                        <table border="1" cellpadding="2" cellspacing="0" width="80%">
                            <tr>
                                <td width="20%">N&uacute;mero de cliente</td>
                                <td id="td"><b><a href="#" onClick="mantenimientoCliente(<%=cliente.idCliente%>);"><%=cliente.idCliente%></a></b></td>
                            </tr>
                            <tr>
                                <td>RFC</td>
                                <td id="td"><b><a href="#" onClick="mantenimientoCliente(<%=cliente.idCliente%>);"><%=cliente.rfc%></a></b></td>
                            </tr>
                            <tr>
                                <td>Nombre de cliente</td>
                                <td><b><%=HTMLHelper.displayField(cliente.nombreCompleto)%></b></td>
                            </tr>
                            <tr>
                                <td>Sucursal</td>
                                <td><b><%=HTMLHelper.getDescripcion(catSucursales, cliente.idSucursal)%></b></td>
                            </tr>
                        </table>
                        <br><br>
                        <%if (cliente.solicitudes != null && cliente.solicitudes.length > 0) { %>
                        <table border="1" cellpadding="1" cellspacing="0" width="80%">
                            <tr align="center" class="titulos">
                                <td>No. Solicitud</td>
                                <td>Producto</td>
                                <td>Estatus</td>
                                <td>Orden de Pago</td>
                                <td>Fecha</td>
                                <td>Monto</td>
                                <td>Saldo Vigente</td>
                                <td>Saldo Vencido</td>
                                <td colspan="2">Equipo</td>
                                <td>Ciclo</td>
                                <td>Cr&eacute;dito</td>
                                <td>Accion</td>
                            </tr>
                            <%
                                for (int i = 0; i < cliente.solicitudes.length; i++) {
                                    SolicitudVO solicitud = cliente.solicitudes[i];

                                    if (solicitud.saldo != null && solicitud.saldo.getSaldoTotalAlDia() > 0 && request.isUserInRole("ORIGINACION_REESTRUCTURA")) {
                                        mostrarBotonReestructura = true;
                                    }
                                    boolean esReestructura = false;
                                    boolean bloquearReestructura = true;
                                    if (solicitud.reestructura == 1) {
                                        esReestructura = true;
                                        mostrarBotonReestructura = false;
                                    }
                            %>
                            <tr>
                                <td align="center"><%=solicitud.idSolicitud%></td>
                                <td><%=HTMLHelper.getDescripcion(catProductosEtiqueta, solicitud.tipoOperacion)%></td>
                                <td><%=HTMLHelper.getDescripcion(catDesembolso, solicitud.desembolsado)%></td>
                                <%		if (solicitud.ordenPago != null && solicitud.ordenPago.getEstatus() > 0) {%>			
                                <td align="center"><%=solicitud.ordenPago.getDescEstatus()%></td>
                                <%		} else { %>
                                <td align="center">N/D</td>
                                <%		} %>
                                <%		if (solicitud.decisionComite != null) {%>
                                <td align="center"><%=HTMLHelper.displayField(solicitud.decisionComite.fechaCaptura)%></td>
                                <td align="right"><%=HTMLHelper.formatCantidad(solicitud.decisionComite.montoAutorizado)%></td>
                                <%		} else {%>
                                <td align="center">N/D</td>
                                <td align="center">N/D</td>
                                <%}%>
                                <td align="center"><%=HTMLHelper.formatCantidad(solicitud.saldoVigente)%></td>
                                <td align="center"><%=HTMLHelper.formatCantidad(solicitud.saldoVencido)%></td>

                                <td align="left" style="border-right: white"><%=HTMLHelper.displayField(solicitud.idGrupo)%></td>
                                <td align="left" style="border-left: white"><%=HTMLHelper.displayField(solicitud.nombreGrupo)%></td>
                                <td align="center"><%=HTMLHelper.displayField(solicitud.idCiclo)%></td>
                                <td align="center"><%=HTMLHelper.displayField(CatalogoHelper.getDescripcionEstatusCredito(solicitud.estatusCiclo, estatusCredito))%></td>
                                <td align="center" id="td"><a href="#" onClick="consultaSolicitud(<%=solicitud.idSolicitud%>)">Consultar</a></td>
                            </tr>
                            <%
                                    }
                                }
                            %>
                        </table>
                        <table width="50%" border="0">
                            <tr>
                                <td colspan="2" align="center">
                                    <br>
                                </td>
                            </tr>
                            
                            <tr>
                                <% if (bBuroInterno){ 
                                    if(nuevaSolicitud==ClientesConstants.SOLICITUD_PERMITIDA){%>                                    
                                        <td width="50%" align="left">
                                            Tipo de operaci&oacute;n
                                            <select name="producto" size="1">
                                                <%=HTMLHelper.displayCombo(catProductos, 3)%>
                                            </select>
                                         </td>
                                            <td width="50%" align="center" id="td">
                                            <input type="button" value="Nueva solicitud" onClick="registraSolicitud()">
                                            </td>
                                        <% if (mostrarBotonReestructura) { %>
                                            <td width="50%" align="center" id="td">
                                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="Reestructurar" onClick="registraReestructura()">
                                            </td>
                                        <% }%>
                                    <%}else if(nuevaSolicitud==ClientesConstants.SOLICITUD_FUERA_RANG0){%>
                                        <td colspan="2" width="50%" align="center">
                                            <font color ="red"> La captura de una nueva solicitud se encuentra fuera de rango
                                        </td>
                                    <%}else if(nuevaSolicitud==ClientesConstants.SOLICITUD_MORA){%>
                                        <td colspan="2" width="50%" align="center">
                                            <font color ="red"> El cliente esta en un equipo no vigente
                                        </td>
                                    <%}%>
                                <%}else {%>
                                <td colspan="2" width="50%" align="center">
                                    <font color ="red"> El cliente se encuentra en el Bur&oacute; Interno
                                </td>
                                
                                <%}%>
                            </tr>
                            
                            <tr>
                                <td colspan="2" width="50%" align="center">
                                    &nbsp;<br><br>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" width="50%" align="center" id="td">
                                    <input type="button" value="Nueva consulta" onClick="nuevaConsulta()">
                                </td>
                            </tr>
                        </table>
            </table>

        </form>
    </center>
    <jsp:include page="footer.jsp" flush="true"/>
</body>
</html>