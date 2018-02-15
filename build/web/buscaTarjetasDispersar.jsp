<%@page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@page import="java.util.TreeMap"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<html>
    <head>
        <title>Busca Tarjetas para Dispersi&oacute;n</title>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">

        <script>
            function muestraClientesTarjetas() {
                window.document.forma.command.value = 'buscaTarjetaFondeo';
                window.document.forma.submit();
            }
            function regresar() {
                window.document.forma.action = "menuTarjetas.jsp";
                window.document.forma.submit();
            }
        </script>
    </head>

<%
TreeMap catSucursales = CatalogoHelper.getCatalogoSucursales();
TreeMap catBancos = CatalogoHelper.getCatalogoBancosDispersionTarjeta();
TreeMap catEstatus    = CatalogoHelper.getCatalogo("C_ESTATUS_ORDENES_PAGO");
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
                    <td width="49%" align="right"><strong>Banco</strong></td>
                    <td width="51%">
                        <select name="banco" id="banco" readOnly="readOnly">
                            <%=HTMLHelper.displayCombo(catBancos, 2)%>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right"><strong>Sucursal</strong></td>
                    <td>
                        <select name="sucursal" size="1">
                            <%=HTMLHelper.displayCombo(catSucursales, 0)%>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right"><strong>Estatus</strong></td>
                    <td>
                        <select name="estatus" id="estatus">
                            <%=HTMLHelper.displayCombo(catEstatus, 0)%>
                        </select>
                    </td>
                </tr>
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
                        <input name="envia" type="button" value="Enviar" onClick="muestraClientesTarjetas()">
                        <input type="button" onclick="regresar()" value="Regresar" id="botonRegresar">
                    </td>
                </tr>
            </table>
        </form>
    </center>
    <jsp:include page="footer.jsp" flush="true"/></body>
</html>