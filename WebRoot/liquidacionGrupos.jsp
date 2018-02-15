<%@page import="com.sicap.clientes.vo.SaldoIBSVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.TreeMap"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <title>Liquidacion de Equipos</title>
        <script>
            function liquidarGrupo(){
                document.getElementById("td").disabled = true;
                if ( window.document.forma.idGrupo.value=='' || window.document.forma.numCiclo.value=='' ){
                    alert("Debe especificar número de equipo y ciclo");
                    return false;
                }
                else if( !esEntero(window.document.forma.idGrupo.value)|| !esEntero(window.document.forma.numCiclo.value) ){
                    alert('Número de equipo o ciclo inválidos');
                    return false;
                }
                document.body.style.cursor = "wait";
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
            <input type="hidden" name="command" value="liquidarGrupo"/>
            <table border="0" width="25%" align="center" cellpadding="5">
                <tr>
                    <td align="center" colspan="2"><h3>Liquidación de Equipos</h3>
                        <%=HTMLHelper.displayNotifications(notificaciones)%></td>
                </tr>
                <tr>
                    <td align="right">Origen:</td>
                    <td align="left">
                        <select name="origen">
                            <option value="cx">Credex</option>
                            <option value="cr">Crediequipos</option>                            
                            <option value="fc">Fincontigo</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">Número de Equipo</td>
                    <td align="left"><input size="15" type="text" name="idGrupo"/></td>
                </tr>
                <tr>
                    <td align="right">Ciclo:</td>
                    <td align="left"><input size="15" type="text" name="numCiclo"/></td>
                </tr>
                <tr>
                    <td width="50%" align="right">Liquidar
                    </td>
                    <td width="50%" height="10%" align="left">
                        <input type="checkbox" name="liquidar" size="10" maxlength="10" checked/>
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Cerrar ciclo
                    </td>
                    <td width="50%" height="10%" align="left">
                        <input type="checkbox" name="cerrar" size="10" maxlength="10" checked/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center" id="td"><input type="button" value="Actualizar" onClick="liquidarGrupo()"/></td>
                </tr>
            </table>
          </form>
        <jsp:include page="footer.jsp" flush="true"/>
    </body>
</html>