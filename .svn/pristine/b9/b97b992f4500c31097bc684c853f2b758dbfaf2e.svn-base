<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<html>
    <head>
        <title>Eliminación de ciclo grupal</title>
        <script>
            function eliminaCiclo(){
                if ( window.document.forma.idGrupo.value=='' || window.document.forma.idCiclo.value=='' ){
                    alert("Debe especificar un grupo y ciclo");
                    return false;
                }
                window.document.forma.submit();
            }
        </script>

        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>

    <%Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");%>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center><h3>Eliminación de ciclo grupal<br></h3></center>

    <form name="forma" action="admin" method="POST">
        <input type="hidden" name="command" value="eliminarCiclo">
        <center><%=HTMLHelper.displayNotifications(notificaciones)%></center>
        <div><table border="0" width="100%" cellspacing="0" align="center" height="10%">
                <tr>
                    <td width="50%" height="10%" align="right">Grupo<br></td>
                    <td width="50%"><input type="text" name="idGrupo" size="10" maxlength="10"></td>
                </tr>
                <tr>
                    <td width="50%" height="10%" align="right">Ciclo<br></td>
                    <td width="50%"><input type="text" name="idCiclo" size="10" maxlength="10"></td>
                </tr>
                <%if(request.isUserInRole("manager")){%>
                <tr>
                    <td width="50%" height="10%" align="right"><input type="checkbox" name="credito" size="10" maxlength="10"/><br></td>
                    <td width="50%">Credito Creado</td>
                </tr>
                <%}%>
                <tr>
                    <td colspan="2" align="center"><input type="button" value="Elimina Ciclo" onclick="eliminaCiclo();"></td>
                </tr>
            </table>
        </div>
    </form>

    <%@include file="footer.jsp"%></body>
</html>

