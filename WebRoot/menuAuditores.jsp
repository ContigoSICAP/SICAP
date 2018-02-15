<%-- 
    Document   : menuAuditores
    Created on : 18/02/2015, 12:49:32 PM
    Author     : avillanueva
--%>
<%@page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<html>
    <head>
        <title>Men&uacute; Administraci&oacute;n Auditores</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script>
            function altaAuditor() {
                document.getElementById("td").disabled = true;
                window.document.forma.command.value = "altaAuditor";
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
            function modifAuditor() {
                document.getElementById("td").disabled = true;
                window.document.forma.command.value = "buscaAuditor";
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
            function asignSucAuditor() {
                document.getElementById("td").disabled = true;
                window.document.forma.command.value = "buscaAsignacionAuditor";
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center>
        <h2>Administraci&oacute;n Auditores</h2> 
        <h3>Opciones</h3>
        <form name="forma" action="admin" method="post">
            <input type="hidden" name="command" value="">
            <table border="0" width="100%" cellspacing="0">
                <tr>
                    <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="altaAuditor()">Alta Auditor</a><br><br></td>
                </tr>
                <tr>
                    <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="modifAuditor()">Modificaci&oacute;n Auditor</a><br><br></td>
                </tr>
                <%if( request.isUserInRole("AUDITOR_ASIG_SUCURSAL") ){%>
                <tr>
                    <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="asignSucAuditor()">Asignaci&oacute;n de Sucursales</a><br><br></td>
                </tr>
                <%}%>
            </table>
        </form>
    </center>
    <jsp:include page="footer.jsp" flush="true"/></body>
</html>