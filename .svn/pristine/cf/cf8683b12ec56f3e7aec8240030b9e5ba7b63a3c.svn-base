<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.dao.*"%>
<html>
    <head>
        <title>Reasignaci&oacute;n de Cartera</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <%
            Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
            UsuarioVO usuario = (UsuarioVO) session.getAttribute("USUARIO");
            int idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
            int idEjecutivoOrigen = HTMLHelper.getParameterInt(request, "idEjecutivoOrigen");
            int idEjecutivoDestino = HTMLHelper.getParameterInt(request, "idEjecutivoDestino");
            boolean diaHabil = (Boolean) request.getAttribute("diaHabil");
            String bloqueo = "";
        //int idEjecutivoDestino = HTMLHelper.getParameterInt(request,"idEjecutivoDestino");
        //TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
            TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
            TreeMap catOperaciones = CatalogoHelper.getCatalogo(ClientesConstants.CAT_OPERACIONES);
            TreeMap status = CatalogoHelper.getEstatusSolicitud();
        //if(idSucursal>0) {
            //TreeMap catEjecutivos = CatalogoHelper.getCatalogoEjecutivos(idSucursal);
            TreeMap catEjecutivosOrigen = null;
            TreeMap catEjecutivosDestino = null;
        //}
            SolicitudVO solicitudes[] = null;
            SolicitudVO solicitudCliente = new SolicitudVO();
            ArrayList<CicloGrupalVO> arrEquipos = new ArrayList<CicloGrupalVO>();

            if (request.getAttribute("EJECUTIVOS_ORIGEN") != null) {
                catEjecutivosOrigen = (TreeMap) request.getAttribute("EJECUTIVOS_ORIGEN");
            }
            if (request.getAttribute("EJECUTIVOS_DESTINO") != null) {
                catEjecutivosDestino = (TreeMap) request.getAttribute("EJECUTIVOS_DESTINO");
            }
            if (request.getAttribute("CARTERA_CLIENTES") != null) {
                solicitudes = (SolicitudVO[]) request.getAttribute("CARTERA_CLIENTES");
            }
            if (request.getAttribute("CARTERA_EQUIPOS") != null) {
                arrEquipos = (ArrayList<CicloGrupalVO>) request.getAttribute("CARTERA_EQUIPOS");
            }
            int j = 0;
        %>
        <script type="text/javascript">
        <!--
            function buscaEjecutivos() {
                window.document.forma.command.value = 'buscaEjecutivos';
                window.document.forma.submit();
            }

            function listarSolicitudes() {
                window.document.forma.command.value = 'listarSolicitudes';
                window.document.forma.submit();
            }

            function reasignarCartera() {
                window.document.forma.command.value = 'reasignarCartera';
            <%if (j > 0) {%>
                var pasa = false;
                var verifica = false;
                //window.document.forma.command.value = 'reasignarCarteraEquipos';
                //for(var i = 0; i<window.document.forma.aplicaCartera.length; i++){
                for (var i = 0; i < window.document.forma.numEquipos.value; i++) {
                    if (window.document.forma.numEquipos.value == 1)
                        verifica = window.document.forma.asignaCartera.checked;
                    else
                        verifica = window.document.forma.asignaCartera[i].checked;
                    if (verifica) {
                        pasa = true;
                    }
                }
                if (pasa) {
                    window.document.forma.submit();
                } else {
                    alert("Debe seleccionar algun equipo");
                }
            <%} else {%>
                //window.document.forma.command.value = 'reasignarCartera';
                window.document.forma.submit();
            <%}%>
            }

            function redireccionMenuAdmin() {
                window.document.forma.command.value = 'administracionEjecutivos';
                window.document.forma.submit();
            }

            function listarEquipos() {
                window.document.forma.command.value = 'listarEquipos';
                window.document.forma.submit();
            }

            function seleccionaTodo() {
                var checa = 1;
                if (window.document.forma.checkGeneral.checked == false)
                    checa = 0;
                //for (var i = 0; i<window.document.forma.aplicaCartera.length; i++){
                for (var i = 0; i < window.document.forma.numEquipos.value; i++) {
                    if (window.document.forma.numEquipos.value == 1)
                        window.document.forma.asignaCartera.checked = checa;
                    else
                        window.document.forma.asignaCartera[i].checked = checa;
                }
            }
        //-->
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <form name="forma" action="admin" method="post">
            <input type="hidden" name="command" value="">

            <table border="0" width="80%" align="center">
                <tr>
                    <td align="center">
                        <h3>Reasignaci&oacute;n de Cartera</h3>
                        <%=HTMLHelper.displayNotifications(notificaciones)%>
                        <br>
                        <table width="100%" border="0" cellpadding="0">
                            <tr>
                                <td width="50%" align="right">Sucursal</td>
                                <td width="50%">  
                                    <select name="idSucursal" size="1">
                                        <%=HTMLHelper.displayCombo(catSucursales, idSucursal)%>
                                    </select>
                                </td>										
                            </tr>	
                            <%if (catEjecutivosOrigen != null) {%>					
                            <tr>
                                <td width="50%" align="right">Ejecutivo de origen</td>
                                <td width="50%">  
                                    <select name="idEjecutivoOrigen" size="1">
                                        <%=HTMLHelper.displayCombo(catEjecutivosOrigen, idEjecutivoOrigen)%>
                                    </select>
                                </td>	
                            </tr>
                            <%} else {%>
                            <tr>
                                <td width="50%" align="right">Ejecutivo de origen</td>
                                <td width="50%">  
                                    <select name="idEjecutivoOrigen" size="1">
                                        <option value="0">Seleccione</option>
                                    </select>
                                </td>	
                            </tr>
                            <%}
                        if (catEjecutivosDestino != null) {%>
                            <tr>
                                <td width="50%" align="right">Ejecutivo destino</td>
                                <td width="50%">  
                                    <select name="idEjecutivoDestino" size="1">
                                        <%=HTMLHelper.displayCombo(catEjecutivosDestino, idEjecutivoDestino)%>
                                    </select>
                                </td>	
                            </tr>
                            <%} else {%>
                            <tr>
                                <td width="50%" align="right">Ejecutivo destino</td>
                                <td width="50%">  					 					
                                    <select name="idEjecutivoDestino" size="1">
                                        <option value="0">Seleccione</option>							
                                    </select>
                                </td>
                            </tr>
                            <%}%>
                            <%if (request.isUserInRole("ADM_EJECUTIVOS_ALTAS")) {%>
                            <tr>
                                <td width="50%" align="right"><input type="radio" id="cambioHist" name="cambioHist" value="true"></td>
                                <td width="50%">Cambio Historico</td>
                            </tr>
                            <%} else {%>
                            <input type="hidden" name="cambioHist" value="true">
                            <%}%>
                            <%if (solicitudes != null) {%>
                            <tr>
                                <td colspan="2">
                                    <br>
                                    <table width="100%" border="0">
                                        <tr bgcolor="#009865">
                                            <td class="whitetext" align="center">Cliente</td>
                                            <td class="whitetext" align="center">Solicitud</td>
                                            <td class="whitetext" align="center">Nombre</td>
                                            <td class="whitetext" align="center">Producto</td>
                                            <td class="whitetext" align="center">Estatus</td>
                                            <td class="whitetext" align="center">Fecha</td>
                                            <td class="whitetext" align="center">Reasignar</td>
                                        </tr>
                                        <%for (int i = 0; i < solicitudes.length; i++) {
                                                solicitudCliente = solicitudes[i];
                                        %>
                                        <tr>
                                            <td>
                                                <input type="hidden" name="idCliente" value="<%=solicitudCliente.idCliente%>">
                                                <%=solicitudCliente.idCliente%>
                                            </td>
                                            <td>
                                                <%=solicitudCliente.idSolicitud%>
                                            </td>
                                            <td>
                                                <%ClienteDAO clientedao = new ClienteDAO();
                                            ClienteVO cliente = clientedao.getCliente(solicitudCliente.idCliente);
                                            out.print(cliente.nombreCompleto);%>
                                            </td>
                                            <td>
                                                <%=HTMLHelper.getDescripcion(catOperaciones, solicitudCliente.tipoOperacion)%>
                                            </td>
                                            <td>								
                                                <%=HTMLHelper.getDescripcion(status, solicitudCliente.estatus)%>
                                            </td>
                                            <td><%=HTMLHelper.displayField(solicitudCliente.fechaCaptura)%></td>
                                            <td align="center">
                                                <input type="checkbox" name ="cambioEjecutivo" value="<%=solicitudCliente.idCliente + "," + solicitudCliente.idSolicitud%>">
                                            </td>
                                        </tr>
                                        <%}%>
                                    </table>
                                </td>				
                            </tr>
                            <%}%>
                            <%if (!arrEquipos.isEmpty()) {%>
                            <tr>
                                <td colspan="2">
                                    <br>
                                    <table width="100%" border="0">
                                        <tr bgcolor="#009865">
                                            <td width="2%" align="center"><input type="checkbox" name="checkGeneral" id="checkGeneral" onclick="seleccionaTodo()" checked/></td>
                                            <td class="whitetext" align="center">Sucursal</td>
                                            <td class="whitetext" align="center">No. Equipo</td>
                                            <td class="whitetext" align="center">Ciclo</td>
                                            <td class="whitetext" align="center">Nombre</td>
                                            <td class="whitetext" align="center">Fecha Dispersi&oacute;</td>
                                            <td class="whitetext" align="center">Fecha Vencimiento</td>
                                            <td class="whitetext" align="center">No. Ejecutivo</td>
                                            <td class="whitetext" align="center">Nombre</td>
                                        </tr>
                                        <%for (CicloGrupalVO equipo : arrEquipos) {%>
                                        <tr>
                                            <td align="center"><input type="checkbox" name="asignaCartera" id="aplicaCartera" value="<%=j%>" checked/></td>
                                            <td align="left"><%=HTMLHelper.displayField(equipo.getNombreSucursal())%>&nbsp;&nbsp;</td>
                                            <td align="right"><%=HTMLHelper.displayField(equipo.getIdGrupo(), true)%>&nbsp;&nbsp;</td>
                                            <td align="right"><%=HTMLHelper.displayField(equipo.getIdCiclo(), true)%>&nbsp;&nbsp;</td>
                                            <td align="lefth"><%=HTMLHelper.displayField(equipo.getNombreEquipo())%>&nbsp;&nbsp;</td>
                                            <td align="center"><%=HTMLHelper.displayField(equipo.getFechaDispersion(), 0)%>&nbsp;&nbsp;</td>
                                            <td align="center"><%=HTMLHelper.displayField(equipo.getFechaVencimiento(), 0)%>&nbsp;&nbsp;</td>
                                            <td align="right"><%=HTMLHelper.displayField(equipo.getAsesor(), true)%>&nbsp;&nbsp;</td>
                                            <td align="left"><%=HTMLHelper.displayField(equipo.getNombreEjecutivo())%>&nbsp;&nbsp;</td>
                                        <input type="hidden" name="idGrupo<%=j%>" value="<%=equipo.getIdGrupo()%>"/>
                                        <input type="hidden" name="idCiclo<%=j%>" value="<%=equipo.getIdCiclo()%>"/>
                            </tr>
                            <%  j++;
                                    }%>
                        </table>
                    </td>				
                </tr>
                <%}%>
                <!-- Botones submit -->
                <%if (!diaHabil) {
                            bloqueo = "disabled";
                        }%>
                <tr>
                    <td align="center" colspan="2">
                        <br>
                        <input type="button" id="btnListEjecutivos" value="Listar Ejecutivos" onClick="buscaEjecutivos()" <%=bloqueo%>>&nbsp;
                        <!--<input type="button" value="Listar Solicitudes" onClick="listarSolicitudes()">&nbsp;-->
                        <input type="button" id="btnListEquipos" value="Listar Equipos" onClick="listarEquipos()" <%=bloqueo%>>&nbsp;
                        <input type="button" id="btnReasignar" value="Reasignar" onClick="reasignarCartera()" <%=bloqueo%>>&nbsp;
                        <input type="button" id="btnRegresar" value="Regresar" onClick="redireccionMenuAdmin()">
                    </td>
                </tr>
            </table>
        </td>
    </tr>		
</table>
<input type="hidden" name="numEquipos" id="numEquipos" value="<%=j%>"/>
</form>
<jsp:include page="footer.jsp" flush="true"/>

</body>
</html>
