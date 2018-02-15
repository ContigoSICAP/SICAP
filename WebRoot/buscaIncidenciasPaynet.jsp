<%@page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<html>
    <head>
        <title>Incidencias Transacciones Paynet</title>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">

        <script>
            function muestraIncidenciasPay() {
                window.document.forma.command.value = 'incidenciasPaynet';
                window.document.forma.submit();
            }
            function regresar() {
                window.document.forma.action = "menuPagosPaynet.jsp";
                window.document.forma.submit();
            }
        </script>
    </head>

<%
%>

    <body leftmargin="0" topmargin="0">
        <br><jsp:include page="header.jsp" flush="true"/>
    <center>
        <h2>Busca Tarjetas para Dispersi&oacute;n</h2>     
        <form name="forma" action="admin" method="post">
            <input type="hidden" name="command" value="">
            <table border="0" width="100%" cellspacing="0">
            </table>
            <table width="80%" border="0" cellspacing="7">
                <tr>
                    <td align="right"><strong>Fecha Dispersi&oacute; Inicial</strong></td>
                    <td><input name="fechaInicial" type="text" id="fechaInicial">dd/mm/yyyy</td>
                </tr>
                <tr>
                    <td align="right"><strong>Fecha Dispersi&oacute; Final</strong></td>
                    <td><input name="fechaFinal" type="text" id="fechaFinal">dd/mm/yyyy</td>
                </tr>
                <tr>
                    <td align="center" colspan="2">
                        <input name="envia" type="button" value="Enviar" onClick="muestraIncidenciasPay()">
                        <input type="button" onclick="regresar()" value="Regresar" id="botonRegresar">
                    </td>
                </tr>
            </table>
        </form>
    </center>
    <jsp:include page="footer.jsp" flush="true"/></body>
</html>