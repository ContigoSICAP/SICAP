<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<html>
    <head>
        <title>Consulta de solicitudes</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script>
            function mostrarReporte() {
                window.document.forma.command.value = 'consultaReporteSolicitudes';
                window.document.forma.submit();
            }

            function mostrarBuroInterno() {
                window.document.forma.command.value = 'consultaBuroInterno';
                window.document.forma.submit();
            }

            function reporteCartera() {
                window.document.forma.command.value = 'reporteCartera';
                window.document.forma.submit();
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css"></head>
        <%
            Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
            ClienteVO clientes[] = (ClienteVO[]) request.getAttribute("CLIENTES_POR_RFC");
            UsuarioVO usuario = (UsuarioVO) session.getAttribute("USUARIO");
        %>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <form name="forma" action="controller" method="post">
            <input type="hidden" name="command" value="">
            <input type="hidden" name="idCliente" value="">
            <input type="hidden" name="idSolicitud" value="">

            <!-- INICIO NUEVO CODIGO -->
            <table border="0" width="100%">
                <tr>
                    <td align="center">
                        <!-- FIN NUEVO CODIGO -->
                        <h3>Men&uacute; de reportes</h3>
                        <%=HTMLHelper.displayNotifications(notificaciones)%>
                        <table border="0" cellpadding="0" width="100%">
                            <% if (clientes == null && usuario.identificador.equals("I")) {%>
                            <tr>
                                <td align="center">
                                    <br><a href="<%=request.getContextPath()%>"></a><a href="#" onClick="reporteCartera()"> Reporte de cartera</a>
                                </td>
                            </tr>
                            <%}%>	
                            <tr>
                                <td align="center">
                                    <br><a href="<%=request.getContextPath()%>"></a><a href="#" onclick="mostrarReporte()"> Reporte Solicitudes</a>
                                </td>
                            </tr>
                            <tr>
                                <td align="center">
                                    <br><a href="<%=request.getContextPath()%>"></a><a href="#" onclick="mostrarBuroInterno()"> Buro Interno</a>
                                </td>
                            </tr>
                        </table>
                        <!-- INICIO NUEVO CODIGO -->
                    </td>
                </tr>
            </table>
            <!-- FIN NUEVO CODIGO -->
            <br>
        </form>
        <jsp:include page="footer.jsp" flush="true"/></body>
</html>