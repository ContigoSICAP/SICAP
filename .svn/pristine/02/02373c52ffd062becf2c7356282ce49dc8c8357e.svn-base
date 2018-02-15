<%@page import="com.sicap.clientes.helpers.*"%>
<%@page import="com.sicap.clientes.util.*"%>
<%@page contentType="text/html" pageEncoding="iso-8859-1"%>
<%
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
String mesCalif = CatalogoHelper.getParametro("MES_CIERRE_RATING");

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <title>Calificaci&oacute;n Asesores del Mes</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script>
            function calificarMes(){
                window.document.forma.command.value='procesaCalificacion';
                window.document.forma.submit();
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css"/>
    </head>
    <body><center>
        <jsp:include page="header.jsp" flush="true"/>
        <h2>Administraci&oacute;n Calificaci&oacute;n de Asesores</h2>
        <h3>Calificaci&oacute;n de Asesores</h3>
        <form name="forma" action="admin" method="post">
            <input type="hidden" name="command" value=""/>
            <table border="0" width="100%" cellspacing="0">
                <tr>
                    <td align="center"><%=HTMLHelper.displayNotifications(notificaciones)%><br></td>
                </tr>
                <tr>
                    <td align="center">Calificaci&oacute;n del Mes</td>
                </tr>
                <tr>
                    <td align="center"><b><%=HTMLHelper.displayField(mesCalif)%></b></td>
                </tr>
                <tr>
                    <td align="center"><br><input type="button" value="Calificar Mes" onClick="calificarMes()"></td>
                </tr>
            </table>
        </form>
        <jsp:include page="footer.jsp" flush="true"/>
    </center></body>
</html>
