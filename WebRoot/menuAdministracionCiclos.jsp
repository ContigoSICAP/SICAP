<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<html>
    <head>
        <title>Administracion de Ciclos</title>
        <script>
            function eliminacionCiclo(){
                document.getElementById("td").disabled = true;
		window.document.forma.command.value='eliminaCiclo';
		document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
            <center>
                <h3>Administracion de Ciclos<br></h3>
                <form name="forma" action="admin" method="POST">
                    <input type="hidden" name="command" value="">
                    <div>
                        <table border="0" width="100%" cellspacing="0" align="center" height="10%">
                            <tr>
                                <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="eliminacionCiclo()">Eliminación de ciclo</a><br><br></td>
                            </tr>
                            <%--
                            <tr>
                                <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="recalculaCiclo()">Recalcular ciclo</a><br><br></td>
                            </tr>
                            --%>
</table></div>
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>

