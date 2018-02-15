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
                window.document.forma.command.value = 'consultaActualizacionDoc';
                if(window.document.forma.sucursal.value == 0 ){
                    alert("Debe seleccionar una sucursal");
                    document.getElementById("boton").disabled = false;
                    return false;          
                }
                if(window.document.forma.mes.value == 0 ){
                    alert("Debe seleccionar un mes de consulta");
                    document.getElementById("boton").disabled = false;
                    return false;          
                }                        
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
            function asignaValor(valor, control) {
                document.getElementById(control).value = valor;
            }
            function muestraReporte(){
		window.document.forma.command.value='generaReporteDocVencidos';	
                window.document.forma.submit();
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css"></head>
        <%
            Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
            //TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
            UsuarioVO usuario = (UsuarioVO) session.getAttribute("USUARIO");
            TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
            TreeMap catMeses = CatalogoHelper.getCatalogoMeses();
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
            int mesbusqueda = request.getParameter("mes")!=null ? Integer.parseInt(request.getParameter("mes")): 0;
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
                        <h3>Consulta de Actualizaci&oacute;n de Documentos</h3>
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
                            <tr>
                                <td width="50%" align="right" valign="top">Mes</td>
                                <td width="50%" align="left"valign="top">                                    
                                    <select name="mes" id="mes">                                        
                                        <%=HTMLHelper.displayCombo((TreeMap) catMeses, mesbusqueda)%>                                        
                                    </select>                                    
                                </td>                   
                               
                            </tr>                            
                       
                            <tr>
                                <td valign="top" colspan="3" align="center">
                                    <br><input type="button" id="boton" value="Mostrar" onClick="validaSolicitudConsumo()"><br><br>
                                </td>
                                
                            </tr>
                            <%
                                if (reportes != null) {
                            %>
                            <tr>
                            <td width="50%" colspan="2" align="center" valign="top">Exportar a CSV                                                                    
                                <a href="#" onClick="muestraReporte()">&nbsp;<img name="imagen" alt="Historial" src="images/icon_excel.gif"></a><br><br>                                 
                                </td> 
                            </tr>
                            <%}%>
                            <tr>
                                <td align="center" colspan="2" valign="bottom">
                                    <a href="<%=request.getContextPath()%>"> Inicio</a>
                                </td>
                            </tr>
                        </table>
                        <table border="1" cellpadding="0" cellspacing="0" width="90%">
                            <%
                                if (reportes != null) {
                            %>		
                            <tr>
                                <td width="5%"  align="center" style="font-weight: bold;font-size: 11px">No. Grupo</td>
                                <td width="8%" align="center" style="font-weight: bold;font-size: 11px">Grupo</td>
                                <td width="5%"  align="center" style="font-weight: bold;font-size: 11px">No. cliente</td>
                                <td width="8%" align="center" style="font-weight: bold;font-size: 11px">RFC</td>
                                <td width="22%" align="center" style="font-weight: bold;font-size: 11px">Nombre completo</td>
                                <td width="5%"  align="center" style="font-weight: bold;font-size: 11px">Asesor</td>
                                <td width="8%" align="center" style="font-weight: bold;font-size: 11px">Fecha de Firma</td>
                                <td width="8%" align="center" style="font-weight: bold;font-size: 11px">Fecha de Vencimiento</td>
                            </tr>
                            <%
                                    for (int i = 0; i < reportes.length; i++) {
                                        String desembolso = HTMLHelper.getDescripcion(catDesembolso, reportes[i].idEstatusDesembolso);
                                        String estatus = HTMLHelper.getDescripcion(catEstatusSolicitud, reportes[i].idEstatusSolicitud);
                            %>
                            <tr>
                                <td width="5%"  align="center" style="font-size: 12px"><%= reportes[i].numGrupo%></td>
                                <td width="5%"  align="center" style="font-size: 12px"><%= reportes[i].nombreGrupo%></td>
                                <td width="5%"  align="center" style="font-size: 12px"><%= reportes[i].idCliente%></td>
                                <td width="8%"  align="center" style="font-size: 12px"><a href="#" onClick="consultaCliente(<%=reportes[i].idCliente%>)"><%=reportes[i].rfc%></a></td>
                                <td width="22%" align="center" style="font-size: 12px"><%= HTMLHelper.displayField(reportes[i].nombre)%></td>
                                <td width="22%" align="center" style="font-size: 12px"><%= HTMLHelper.displayField(reportes[i].ejecutivo)%></td>
                                <td width="8%" align="center" style="font-size: 12px"><%=HTMLHelper.displayField(reportes[i].fechaFirma)%></td>  
                                <td width="8%" align="center" style="font-size: 12px"><%=HTMLHelper.displayField(reportes[i].fechaVencimiento)%></td>  
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