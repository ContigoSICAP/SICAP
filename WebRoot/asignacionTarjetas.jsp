<%@page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<html>
    <head>
        <title>Asignaci&oacute;n de Tarjetas</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script>
            function regresar() {
                window.document.forma.action = "menuTarjetas.jsp";
                window.document.forma.submit();
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center>
        <h2>Asignaci&oacute;n de Tarjetas</h2> 
        <h1>En Construcci&oacute;n</h1>
        <form name="forma" action="admin" method="post">
            <input type="hidden" name="command" value="">
            <table border="0" width="100%" cellspacing="0">
                <tr>
                    <td align="center" colspan="2">
                        <input type="button" onclick="regresar()" value="Regresar" id="botonRegresar"><br><br>
                    </td>
                </tr>
            </table>
        </form>
    </center>
    <jsp:include page="footer.jsp" flush="true"/></body>
</html>