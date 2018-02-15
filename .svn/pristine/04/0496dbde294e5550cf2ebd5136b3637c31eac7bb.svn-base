<%-- 
    Document   : migracionSemanal
    Created on : 2/04/2012, 05:11:55 PM
    Author     : Alex
--%>

<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <title>Migracion Informacion BSC</title>
        <script>
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css"/>
    </head>
    <%
    Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
    %>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <form name="forma" action="admin" method="post">
            <input type="hidden" name="command" value=""/>
            <table border="0" width="20%" align="center">
                <tr>
                    <td align="center" colspan="2"><h3>Migración de Información a BCS</h3>
                        <%=HTMLHelper.displayNotifications(notificaciones)%></td>
                </tr>
            </table>
        </form>
        <jsp:include page="footer.jsp" flush="true"/>
    </body>
</html>
