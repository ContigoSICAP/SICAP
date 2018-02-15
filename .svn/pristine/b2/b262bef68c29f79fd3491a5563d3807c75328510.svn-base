<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<html>
    <head>
        <title> busquedaSucursal</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">

        <script>

            function buscaSucursal(){
                window.document.forma.command.value='buscaSucursal';
                if ( window.document.forma.sucursal.value==''){
                    alert('Debe introducir un Nombre');
                    return false;
                }
                window.document.forma.submit();
            }
	
            function obtenUsuario(sucursal){
                window.document.forma.command.value='modificaSucursal';
                window.document.forma.nombreSucursal.value=sucursal;
                window.document.forma.submit();
            }
	
            function redireccionMenuAdmin(){
                window.document.forma.command.value = 'administracionSucursales';
                window.document.forma.submit();
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <%
        Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
        SucursalVO[] sucursal = (SucursalVO[]) request.getAttribute("SUCURSAL");
    %>

    <body leftmargin="0" topmargin="0" onload="javascript:window.document.forma.sucursal.focus();">
        <jsp:include page="header.jsp" flush="true"/>
    <center>

        <h2>Modificaci&oacute;n de Sucursal<br></h2>
        <h3>B&uacute;squeda de Sucursal<br></h3>

        <form name="forma" action="admin" method="post">
            <input type="hidden" name="command" value="buscaSucursal">
            <input type="hidden" name="nombreSucursal" value="">
            <table border="0" width="100%" cellspacing="0">
                <tr>
                    <td width="100%" align="center" colspan="2"><%=HTMLHelper.displayNotifications(notificaciones)%><br></td>
                </tr>

                <tr>
                    <td width="50%" align="right">Sucursal: <br></td>
                    <td width="50%">  
                        <input type="text" name="sucursal" size="15" maxlength="50">
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center"><br><input type="button" value="Enviar" onClick="buscaSucursal()"> <input type="button" value="Regresar" onClick="redireccionMenuAdmin()"></td>
                </tr>	
            </table>
        </form>
    </center>

    <table border="1" width="70%" cellspacing="0" align="center" cellpadding="7">

        <%
            if (sucursal != null && sucursal.length != 0) {
                for (int i = 0; i < sucursal.length; i++) {
                    if (sucursal[i].idSucursal != 33) {%> 
                        <td align="center">
                        <a href="#" onClick="obtenUsuario('<%= sucursal[i].nombre%>')"><b><%= sucursal[i].nombre%></b></a></td>
                        <%if (((i + 1) % 3) == 0) {%>
                        <tr>
                        <%}
                    }
                 }
            }%>  
    </table>
    <jsp:include page="footer.jsp" flush="true"/></body>
</html>