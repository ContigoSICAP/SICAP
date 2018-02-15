<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
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
                document.getElementById("boton").disabled = true;
                window.document.forma.command.value = 'consultaSolicitudesEstatus';
                if(window.document.forma.sucursal.value == 0 && window.document.forma.estatusSolicitud.value == 0 && window.document.forma.numCliente.value == '' && window.document.forma.fechaInicio.value == '' && window.document.forma.fechaFin.value == '' ){
                    alert("Debe seleccionar Por lo menos un rango de fechas para consultar");
                    document.getElementById("boton").disabled = false;
                    return false;          
                }                
                if (window.document.forma.numCliente.value != '' && !esEntero(window.document.forma.numCliente.value)) {
                    alert('Introduzca un n\u00famero de cliente v\u00e1lido por favor');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if(window.document.forma.numCliente.value == ''){
                    if (window.document.forma.fechaInicio.value == ''&&window.document.forma.fechaFin.value ==''){
                        alert("Es necesario introducir un rango de fechas");
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                    if (window.document.forma.fechaInicio.value != '') {
                        if (!esFechaValida(window.document.forma.fechaInicio, false)) {
                            alert("Debe ingresar una fecha inicial v\u00e1lida");
                            document.getElementById("boton").disabled = false;
                            return false;
                        }
                        if (window.document.forma.fechaFin.value == '') {
                            alert("Debe ingresar una fecha final");
                            document.getElementById("boton").disabled = false;
                            return false;
                        }
                    }
                    if (window.document.forma.fechaFin.value != '') {
                        if (!esFechaValida(window.document.forma.fechaFin, false)) {
                            alert("Debe ingresar una fecha final v\u00e1lida");
                            document.getElementById("boton").disabled = false;
                            return false;
                        }
                        if (window.document.forma.fechaInicio.value == '') {
                            alert("Debe ingresar una fecha inicial");
                            document.getElementById("boton").disabled = false;
                            return false;
                        }
                    }
                    if (window.document.forma.fechaInicio.value != '' && window.document.forma.fechaFin.value != '') {                     
                        if (!esRangoValido(window.document.forma.fechaInicio.value, window.document.forma.fechaFin.value, 7)) {
                            alert("El rango de fechas no puede ser superior a 7 dias ");
                            document.getElementById("boton").disabled = false;
                            return false;
                        }
                    }
                }
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
            
            function ayudaHistorialComentarios(idCliente, idSolicitud) {
                params = "?command=historialComentariosCliente&idCliente="+idCliente+"&idSolicitud="+idSolicitud;
                url = "/CEC/controller";            
                abreVentana(url + params, 'scrollbars=yes', 1000, 250, true, 0, 0);
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
            TreeMap catEstatusSol = CatalogoHelper.getCatalogoEstatusSolcitud(true);
            for (Iterator iter = catSucOrderLlave.keySet().iterator(); iter.hasNext();) {
                Object obj = iter.next();
                catSucOrderAlfa.put(catSucOrderLlave.get(obj), obj);
            }

            TreeMap catEstatusSolicitud = CatalogoHelper.getCatalogo(ClientesConstants.CAT_ESTATUS_SOLICITUD);
            TreeMap catDesembolso = CatalogoHelper.getCatalogoDesembolso();
            ReporteVO[] reportes = (ReporteVO[]) request.getAttribute("REPORTES");
            String dictamen = null;
            String fecFin = "";
            String fecInicio = "";
            int idSucursal = HTMLHelper.getParameterInt(request, "sucursal");
            int idEstatus = HTMLHelper.getParameterInt(request, "estatus");
            int idProducto = HTMLHelper.getParameterInt(request, "producto");
            int numCliente = HTMLHelper.getParameterInt(request, "numCliente");
            fecFin = request.getParameter("fechaFin");
            fecInicio = request.getParameter("fechaInicio");
            java.sql.Timestamp fecha_comparacion = FechasUtil.subtractHours(Convertidor.toSqlTimeStamp(fechaDia), 18);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();
            String FechaHoy = dateFormat.format(cal.getTime());
            String fechaAnterior= "";
            boolean fechaInvalida = false;
            int x=1;
            int j=0;
            do{        
                fechaAnterior = Convertidor.dateToString(FechasUtil.getRestarDias(Convertidor.stringToDate(FechaHoy,"dd/MM/yyyy"),x+j));
                fechaInvalida = FechasUtil.esDiaInhabil(Convertidor.stringToDate(fechaAnterior,"dd/MM/yyyy"),(java.util.Date[])session.getAttribute("INHABILES"));
                if(fechaInvalida)
                    j++;
                else
                    x++;
            }while(x<=3);
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
                        <h3>Reporte de solicitudes por estatus</h3>
                    </td>
                </tr>
                <tr>
                    <td align="center">
                        <%=HTMLHelper.displayNotifications(notificaciones)%>
                        <table border="0" cellpadding="0" width="70%">
                            <tr>
                                <td width="50%" align="right" valign="top">Sucursal</td>
                                <td width="50%" align="left"valign="top">
                                    <%if (request.isUserInRole("ANALISIS_CREDITO")) {%>
                                    <select name="sucursal" id="sucursal">
                                        
                                        <%=HTMLHelper.displayComboAlfabetico((TreeMap) catSucOrderAlfa, idSucursal)%>
                                        
                                    </select>
                                    <%}else{%>
                                    <select disabled name="muestrasucursal" id="muestrasucursal">
                                        
                                        <%=HTMLHelper.displayComboCheck(catSucursales, usuario.sucursales[0].idSucursal)%>
                                        
                                    </select>
                                        <input type="hidden" name="sucursal" id="sucursal" value="<%=usuario.sucursales[0].idSucursal%>">
                                    <%}%>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Selecione Estatus</td>
                                <td width="50%"><select name="estatusSolicitud" ><%=HTMLHelper.displayCombo(catEstatusSol,0)%></select></td>
                            </tr>
                            <tr>
                                <td width="50%" valign="top" align="right">Fecha inicial</td>
                                <%if(fecInicio != null){%>
                                <td width="50%"> <input type="text" name="fechaInicio" id="fechaInicio" size="10" maxlength="10" value = "<%=fecInicio%>">
                                </td>
                                <%} else {%>
                                <td width="50%"> <input  type="text" name="fechaInicio" id="fechaInicio" size="10" maxlength="10" value="<%=fechaAnterior%>"  placeholder="dd/mm/yyyy" ></td>
                            <%}%>
                            </tr>
                            <tr>
                                <td width="50%" align="right" valign="top">Fecha final</td>
                                <%if(fecInicio != null){%>
                                <td width="50%" valign="top">
                                    <input type="text" name="fechaFin" id="fechaFin" size="10" maxlength="10" value = "<%= fecFin%>">
                                </td>
                                <%} else {%>
                                <td width="50%" valign="top">
                                    <input type="text" name="fechaFin" id="fechaFin" size="10" maxlength="10" value="<%=FechaHoy%>" placeholder="dd/mm/yyyy" >
                                </td>
                            <%}%>
                            </tr>                            
                        <tr>
                            <td width="50%" height="10%" align="right">No. Cliente<br></td>
                            <%if(numCliente!=0){%>
                            <td width="50%"><input name="numCliente" size="10" maxlength="10" min="1" value="<%=numCliente%>"></td>
                            <%} else{%>
                            <td width="50%"><input name="numCliente" size="10" maxlength="10" min="1"></td>
                            <%}%>
                        </tr>
                            <tr>
                                <td valign="top" colspan="3" align="center">
                                    <br><input type="button" id="boton" value="Mostrar" onClick="validaSolicitudConsumo()"><br><br>
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
                                <td width="22%" align="center" style="font-weight: bold;font-size: 11px">Estatus Solicitud</td>
                                <td width="10%" align="center" style="font-weight: bold;font-size: 11px">Estatus desembolso</td>
                                <td width="8%" align="center" style="font-weight: bold;font-size: 11px">Fecha de Modificacion</td>
                                <td width="5%" align="center" style="font-weight: bold;font-size: 11px">Calificaci&oacute;n</td>
                                <td width="3%" align="center" style="font-weight: bold;font-size: 11px"></td> 
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
                                <td width="22%" align="center" style="font-size: 12px"><%= HTMLHelper.displayField(reportes[i].descEstatusSolicitud)%></td>
                                <td width="10%" align="center" style="font-size: 12px"><%= HTMLHelper.displayField(desembolso)%></td>
                                <td width="8%" align="center" style="font-size: 12px"><%=HTMLHelper.displayField(reportes[i].fechaHora)%></td>  
                                <td BGCOLOR="<%=HTMLHelper.getColorCalificacion(reportes[i].calificacion,reportes[i].aceptaRegular,reportes[i].idSucursal)%>" width="5%" align="center" style="font-size: 12px">&nbsp;</td>                                  
                                <td><a href="#" onClick="ayudaHistorialComentarios(<%=reportes[i].idCliente%>,<%=reportes[i].idSolicitud%>)"><img name="imagen" alt="Historial" src="images/history_icon.png"></a></td>
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