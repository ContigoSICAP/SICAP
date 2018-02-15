<%-- 
    Document   : manCierre
    Created on : 4/10/2011, 09:26:56 AM
    Author     : Alex
--%>

<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <title>Mantenimiento de Cierre</title>
        <script>
            function ejecutaClase(){
                window.document.forma.command.value = 'ejecutaClases';
		window.document.forma.submit();
	 }
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
                    <td align="center" colspan="2"><h3>Ajuste de Informacion</h3>
                        <%=HTMLHelper.displayNotifications(notificaciones)%></td>
                </tr>
                <!--<tr>
                    <td align="right">Grupo:</td>
                    <td align="left"><input type="text" name="idGrupo" value="0"/></td>
                </tr>
                <tr>
                    <td align="right">Ciclo:</td>
                    <td align="left"><input type="text" name="numCiclo" value="0"/></td>
                </tr>
                <tr>
                    <td align="right">Fecha Ajuste</td>
                    <td colspan="2" align="center"><input type="text" name="Fecha" id="Fecha" value="" onKeyPress="return submitenter(this,event)"/></td>
                </tr>-->
                <tr>
                    <td colspan="2" align="center"><input type="button" value="Ejecutar" onClick="ejecutaClase()"/></td>
                </tr>
            </table>
        </form>
        <jsp:include page="footer.jsp" flush="true"/>
    </body>
</html>
