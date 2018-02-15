<%@page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<html>
    <head>
        <title>Men&uacute; Administraci&oacute;n Tarjetas</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script>
            function cargaLote() {
                document.getElementById("td").disabled = true;
                window.document.forma.command.value = "cargaLote";
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
            function asignacionTarjeta() {
                document.getElementById("td").disabled = true;
                window.document.forma.command.value = "asignacionTarjeta";
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
            function altaPersonalizar() {
                document.getElementById("td").disabled = true;
                window.document.forma.command.value = "altaPersonalizar";
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
            function dispersaTarjeta() {
                document.getElementById("td").disabled = true;
                window.document.forma.command.value = "buscaTarjetaDispersa";
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center>
        <h2>Administraci&oacute;n Tarjetas</h2> 
        <h3>Opciones</h3>
        <form name="forma" action="admin" method="post">
            <input type="hidden" name="command" value="">
            <table border="0" width="100%" cellspacing="0">
                <tr>
                    <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="cargaLote()">Carga Lote Tarjetas</a><br><br></td>
                </tr>
                <tr>
                    <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="asignacionTarjeta()">Asignaci&oacute;n de Tarjetas a Clientes</a><br><br></td>
                </tr>
                <tr>
                    <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="altaPersonalizar()">Generar Archivo Alta Personalizaci&oacute;n</a><br><br></td>
                </tr>
                <tr>
                    <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="dispersaTarjeta()">Generar Archivo Dispersi&oacute;n</a><br><br></td>
                </tr>
            </table>
        </form>
    </center>
    <jsp:include page="footer.jsp" flush="true"/></body>
</html>