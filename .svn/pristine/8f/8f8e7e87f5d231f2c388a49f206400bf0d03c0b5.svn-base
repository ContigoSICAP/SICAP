<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.ReporteVO"%>
<%@ page import="com.sicap.clientes.vo.UsuarioVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>
<html>
    <head>
        <title>Reporte de solicitudes</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script>

            function consultaCliente(idCliente) {
                window.document.forma.command.value = 'consultaCliente';
                window.document.forma.idCliente.value = idCliente;
                window.document.forma.idSolicitud.value = 1;
                window.document.forma.submit();
            }

            function validaSolicitudConsumo() {
                window.document.forma.command.value = 'consultaSolicitudesConsumo';
            <%if (!request.isUserInRole("ANALISIS_CREDITO")) {%>
                if (window.document.forma.sucursal.value == 0) {
                    alert("Debe seleccionar la sucursal");
                    return false;
                }
            <%}%>

                if (window.document.forma.fechaInicio.value != '') {
                    if (!esFechaValida(window.document.forma.fechaInicio, false)) {
                        alert("Debe ingresar una fecha inicial v\u00e1lida");
                        return false;
                    }
                    if (window.document.forma.fechaFin.value == '') {
                        alert("Debe ingresar una fecha final");
                        return false;
                    }
                }
                if (window.document.forma.fechaFin.value != '') {
                    if (!esFechaValida(window.document.forma.fechaFin, false)) {
                        alert("Debe ingresar una fecha final v\u00e1lida");
                        return false;
                    }
                    if (window.document.forma.fechaInicio.value == '') {
                        alert("Debe ingresar una fecha inicial");
                        return false;
                    }
                }
                //Verificar plazo
                if (window.document.forma.fechaInicio.value != '' && window.document.forma.fechaFin.value != '') {
                    if (!esRangoFechaValido(window.document.forma.fechaInicio.value, window.document.forma.fechaFin.value, 8)) {
                        alert("El rango de fechas establecido no es v\u00e1lido");
                        return false;
                    }
                }

                window.document.forma.submit();
            }


            function asignaValor(valor, control) {
                document.getElementById(control).value = valor;
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css"></head>
        <%
            Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
            //TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
            UsuarioVO usuario = (UsuarioVO) session.getAttribute("USUARIO");
            TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
            SortedMap catSucOrderLlave = catSucursales;
            SortedMap<Object, Object> catSucOrderAlfa = new TreeMap<Object, Object>();
            Calendar c1 = Calendar.getInstance();
            Date fechaDia = Convertidor.toSqlTimeStamp(c1.getTime());
            for (Iterator iter = catSucOrderLlave.keySet().iterator(); iter.hasNext();) {
                Object obj = iter.next();
                catSucOrderAlfa.put(catSucOrderLlave.get(obj), obj);
            }

            TreeMap catEstatusSolicitud = CatalogoHelper.getCatalogo(ClientesConstants.CAT_ESTATUS_SOLICITUD);
            TreeMap catProducto = CatalogoHelper.getCatalogoOperaciones(usuario, false);
            TreeMap catOperaciones = CatalogoHelper.getCatalogo(ClientesConstants.CAT_OPERACIONES);
            TreeMap catDesembolso = CatalogoHelper.getCatalogoDesembolso();
            ReporteVO[] reportes = (ReporteVO[]) request.getAttribute("REPORTES");
            String dictamen = null;
            int idSucursal = HTMLHelper.getParameterInt(request, "sucursal");
            int idEstatus = HTMLHelper.getParameterInt(request, "estatus");
            int idProducto = HTMLHelper.getParameterInt(request, "producto");
            java.sql.Timestamp fecha_comparacion = FechasUtil.subtractHours(Convertidor.toSqlTimeStamp(fechaDia), 18);
        %>

    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <form name="forma" action="controller" method="post">
            <input type="hidden" name="command" value="">
            <input type="hidden" name="idCliente" value="">
            <input type="hidden" name="idSolicitud" value="">
            <input type="hidden" name="idGrupo" value="">
            <input type="hidden" name="idCiclo" value="">
            <input type="hidden" name="idOperacion" value="">

            <!-- INICIO NUEVO CODIGO -->
            <table border="0" width="100%">
                <tr height="70">
                    <td align="center">
                        <!-- FIN NUEVO CODIGO -->
                        <h3>Reporte de solicitudes</h3>
                    </td>
                </tr>
                <tr>
                    <td align="center">
                        <%=HTMLHelper.displayNotifications(notificaciones)%>
                        <table border="0" cellpadding="0" width="70%">
                            <tr>
                                <td width="50%" align="right" valign="top">Sucursal</td>
                                <td width="50%" align="left"valign="top">
                                    <select name="sucursal" id="sucursal">
                                        <%=HTMLHelper.displayComboAlfabetico((TreeMap) catSucOrderAlfa, idSucursal)%>
                                    </select>
                                </td>
                            </tr>
                            <%
                                String fecInicio = "";
                                if (request.getParameter("fechaInicio") != null) {
                                    fecInicio = request.getParameter("fechaInicio");
                                }
                            %>
                            <tr>
                                <td width="50%" valign="top" align="right">Fecha inicial</td>
                                <td width="50%">
                                    <input type="text" name="fechaInicio" id="fechaInicio" size="10" maxlength="10" value = "<%=fecInicio%>">
                                </td>
                            </tr>
                            <%
                                String fecFin = "";
                                if (request.getParameter("fechaFin") != null) {
                                    fecFin = request.getParameter("fechaFin");
                                }
                            %>
                            <tr>
                                <td width="50%" align="right" valign="top">Fecha final</td>
                                <td width="50%" valign="top">
                                    <input type="text" name="fechaFin" id="fechaFin" size="10" maxlength="10" value = "<%= fecFin%>">
                                </td>
                            </tr>
                            <tr>
                                <td valign="top" colspan="3" align="center">
                                    <br><input type="button" value="Mostrar" onClick="validaSolicitudConsumo()"><br><br>
                                </td>
                            </tr>
                        </table>
                        <table border="1" cellpadding="0" cellspacing="0" width="90%">
                            <%
                                if (reportes != null) {
                            %>		
                            <tr>
                                <td width="5%"  align="center" style="font-weight: bold;font-size: 11px">No. cliente</td>
                                <td width="8%" align="center" style="font-weight: bold;font-size: 11px">RFC</td>
                                <td width="5%" align="center" style="font-weight: bold;font-size: 11px">No. Solicitud</td>
                                <td width="22%" align="center" style="font-weight: bold;font-size: 11px">Nombre completo</td>
                                <td width="10%" align="center" style="font-weight: bold;font-size: 11px">Producto</td>
                                <td width="10%" align="center" style="font-weight: bold;font-size: 11px">Estatus desembolso</td>
                                <td width="8%" align="center" style="font-weight: bold;font-size: 11px">Fecha de Captura</td>
                                <%
                                    if (idSucursal == 0) {
                                %>
                                <td width="10%" align="center" style="font-weight: bold;font-size: 11px">Sucursal</td>
                                <%
                                    }
                                %>
                                <td width="5%" align="center" style="font-weight: bold;font-size: 11px">Origen</td>
                                <td width="21%" align="center" style="font-weight: bold;font-size: 11px">Ejecutivo</td>
                            </tr>
                            <%
                                for (int i = 0; i < reportes.length; i++) {
                                    String desembolso = HTMLHelper.getDescripcion(catDesembolso, reportes[i].idEstatusDesembolso);
                                    String estatus = HTMLHelper.getDescripcion(catEstatusSolicitud, reportes[i].idEstatusSolicitud);
                            %>
                            <tr>
                                <td width="5%"  align="center" style="font-size: 12px"><%= reportes[i].idCliente%></td>
                                <td width="8%"  align="center" style="font-size: 12px"><a href="#" onClick="consultaCliente(<%=reportes[i].idCliente%>)"><%=reportes[i].rfc%></a></td>
                                <td width="5%" align="center" style="font-size: 12px"><%= HTMLHelper.displayField(reportes[i].idSolicitud)%></td>
                                <td width="22%" align="center" style="font-size: 12px"><%= HTMLHelper.displayField(reportes[i].nombre)%></td>
                                <td width="10%" align="center" style="font-size: 12px"><%= HTMLHelper.displayField(reportes[i].producto)%></td>
                                <td width="10%" align="center" style="font-size: 12px"><%= HTMLHelper.displayField(desembolso)%></td>
                                <td width="8%" align="center" style="font-size: 12px"><%=HTMLHelper.displayField(reportes[i].fechaCaptura)%></td>
                                <%
                                    if (idSucursal == 0) {
                                %>
                                <td width="10%"  align="center" style="font-size: 12px"><%=HTMLHelper.displayField(reportes[i].nombreSucursal)%></td>
                                <%
                                    }
                                %>
                                <td width="5%"  align="center" style="font-size: 12px"><%=HTMLHelper.displayField(reportes[i].origen, "N/D")%></td>
                                <td width="21%"  align="center" style="font-size: 12px"><%=HTMLHelper.displayField(reportes[i].ejecutivo, "N/D")%></td>
                            </tr>
                            <%
                                    }
                                }else{
                                    %>
                                    <br><br><br><br><br><br><br>
                            <%
                                }
                            %>
                        </table>
                        <!-- INICIO NUEVO CODIGO -->
                    </td>
                </tr>
            </table>
            <!-- FIN NUEVO CODIGO -->
            <br>
        </form>
        <jsp:include page="footer.jsp" flush="true"/>
    </body>
</html>