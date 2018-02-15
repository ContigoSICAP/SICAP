<%@ 	page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<html>
    <head>
        <title>Menú Saldo a Favor</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script>
            function devolucionSaldoFavor(){
                document.getElementById("td").disabled = true;
                window.document.forma.command.value="devolucionSaldoFavor";
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
            function cargaArchivo(){
                document.getElementById("td").disabled = true;
                window.document.forma.command.value="cargarArchivoOrdenPago";
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center>
        <h2>Menú Saldo a Favor</h2> 
        <h3>Opciones</h3>
        <form name="forma" action="controller" method="post">
            <input type="hidden" name="command" value="">
            <table border="0" width="100%" cellspacing="0">
                <%if(request.isUserInRole("ADM_PAGOS")){%>
                    <tr>
                        <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="devolucionSaldoFavor()">Devolución de Saldo a Favor</a><br><br></td>
                    </tr>                    
                <%}%>
            </table>
        </form>
    </center>
    <jsp:include page="footer.jsp" flush="true"/></body>
</html>