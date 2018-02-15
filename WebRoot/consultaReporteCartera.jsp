<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.vo.UsuarioVO"%>
<%@ page import="java.util.*"%>
<html>
    <head>
        <title>Alta Clientes</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script>

            function buscar() {
                if (window.document.forma.sucursal.value == 0) {
                    alert('Debe seleccionar una sucursal');
                    return false;
                } else {
                    window.document.forma.command.value = 'respuestaReportes';
                    window.document.forma.sucursal.value = window.document.forma.sucursal.value;
                    window.document.forma.submit();
                }
            }

        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <%
        UsuarioVO usuario = (UsuarioVO) session.getAttribute("USUARIO");
        TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
        //TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
        Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
        int sucursal = 0;
        if (request.getParameter("sucursal") != null) {
            sucursal = Integer.parseInt(request.getParameter("sucursal"));
        }
    %>

    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center>
        <h2>Reporte de cartera<br></h2> 

        <form name="forma" action="controller" method="post">

            <table border="0" width="100%" cellspacing="0">
                <tr>
                    <td width="50%" align="right">Sucursal</td>
                    <td width="50%" align="left">  
                        <select name="sucursal" size="1">
                            <%=HTMLHelper.displayCombo(catSucursales, sucursal)%>
                        </select>
                    </td>
                <tr>
                    <td align="center" colspan="2">
                        <br><input type="button" value="Consultar" onClick="buscar()">
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="2">
                        <br><%=HTMLHelper.displayNotifications(notificaciones)%>
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="2" valign="bottom">
                        <a href="<%=request.getContextPath()%>"> Inicio</a>
                    </td>
                </tr>
            </table>

            <input type="hidden" name="command" value="reporte">
        </form>
    </center>

    <jsp:include page="footer.jsp" flush="true"/></body>
</html>