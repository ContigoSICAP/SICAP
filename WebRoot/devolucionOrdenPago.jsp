<%@page import="com.sicap.clientes.dao.CatalogoDAO"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="java.util.*"%>
<html>
    <head>
        <title>Devolucion Orden de Pago Grupal</title>
        <link href="./css/cupertino/jquery-ui-1.8.6.custom.css" rel="stylesheet" type="text/css">
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
        <script language="Javascript" src="./js/functions.js"></script>
        <script type="text/javascript" src="./js/jquery-1.4.2.min.js" ></script>
        <script type="text/javascript" src="./js/jquery-ui-1.8.6.custom.min.js" ></script>
        <script language="Javascript"></script>
        <script>
            $(document).ready(function() {

                $("#Fecha").datepicker({
                    dateFormat: 'yy-mm-dd',
                    changeMonth: true,
                    changeYear: true
                });

            });
            function submitenter(myfield, e) {
                var keycode;
                if (window.event)
                    keycode = window.event.keyCode;
                else if (e)
                    keycode = e.which;
                else
                    return true;

                if (keycode == 13) {
                    ConsultarPagosReprocesar();
                } else
                    return true;
            }


            function aceptarPago() {
                disableEdit();
                if (window.document.forma.idGrupo.value == '' || window.document.forma.idCiclo.value == '') {
                    alert("Debe especificar un grupo y ciclo");
                    enableEdit();
                    return false;
                }
                if (window.document.forma.ordenPago.value == '') {
                    alert('La orden de pago es invalida');
                    enableEdit();
                    return false;
                }
                window.document.forma.submit();
            }

            function redireccionMenuAdmin() {
                window.document.forma.command.value = 'ordenesDePago';
                window.document.forma.submit();
            }
            
            function disableEdit() {
                document.body.style.cursor = "wait";
                document.getElementById("botonAceptaPago").disabled = true;
                document.getElementById("botonRegresar").disabled = true;
            }

            function enableEdit() {
                document.body.style.cursor = "default";
                document.getElementById("botonAceptaPago").disabled = false;
                document.getElementById("botonRegresar").disabled = false;
            }
        </script>
    </head>

    <%Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
    CatalogoDAO catalogoDao = new CatalogoDAO();
    %>

    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center><h3>Pago Manual Grupal<br></h3></center>

    <form name="forma" action="admin" method="POST">
        <input type="hidden" name="command" value="devolucionOrden">
        <center><%=HTMLHelper.displayNotifications(notificaciones)%></center>
        <div><table border="0" width="100%" cellspacing="0" align="center" height="10%">
                <tr>
                    <td width="50%" height="10%" align="right">Grupo<br></td>
                    <td width="50%"><input type="text" name="idGrupo" size="10" maxlength="10"></td>
                </tr>

                <tr>
                    <td width="50%" height="10%" align="right">Ciclo<br></td>
                    <td width="50%"><input type="text" name="idCiclo" size="3" maxlength="3"></td>
                </tr>
                <tr>
                    <td width="50%" height="10%" align="right">Orden de Pago Cancelada<br></td>
                    <td width="50%"><input type="text" name="ordenPago" size="19" maxlength="18"></td>
                </tr>
                <tr>
                <br>
                <br>
                </tr>
                <tr>
                    <td colspan="3" height="10%" align="center">
                        <%if(!catalogoDao.ejecutandoCierre()){%>
                        <input type="button" id="botonAceptaPago" value="Aceptar Pago" onclick="aceptarPago()">
                        <%}%>
                    </td>
                </tr>
                <tr>
                    <td colspan="3" height="10%" align="center">
                        <input type="button" id="botonRegresar" value="Regresar" onClick="redireccionMenuAdmin()">
                    </td>
                </tr>
            </table>
        </div>
    </form>

    <%@include file="footer.jsp"%></body>
</html>

</html>