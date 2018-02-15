<%@page import="java.util.List"%>
<%@page import="com.sicap.clientes.vo.FondeadorVO"%>
<%@page import="java.util.Date"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.vo.LineaCreditoVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.dao.*"%>
<%@ page import="java.util.TreeMap"%>
<%
    String command = "altaFondeador";
    Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
    
    FondeadorDAO fondDAO = new FondeadorDAO();
    List<FondeadorVO> fondeadores = fondDAO.obtenerFondeadores();
%>
<html>
    <head>
        <title>Alta Fondeador</title>
        <script type="text/javascript" src="./js/functions.js"></script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
        <script>
            function validaCamposAltaFondeador()
            {
                if( window.document.forma.nombre.value===''){
                    alert("Ingrese el nombre del Fondeador");
                    window.document.forma.nombre.focus();
                    document.getElementById("botonAlta").disabled = false;
                    return false;
                }
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
	
            function redireccionMenuGestionFondeadores(){
                window.document.forma.command.value = 'administracionFondeadores';
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
            
            
        </script>
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center>
        <h2>Alta Fondeador</h2> 
    </center>
    <form method="post" name="forma" action="admin">
        <input type="hidden" name="command" value="<%=command%>">
        <table border="0" cellspacing="5" align="center" >
            <tr>
                <td align="center" colspan="2">
                    <%=HTMLHelper.displayNotifications(notificaciones)%><br>
                </td>
            </tr>
            <tr>
                <th align="right">Nombre Fondeador:</th>
                <td align="left"><input type="text" name="nombre" maxlength="45" style="text-transform: uppercase" ></td>
            </tr>
            <tr>
                <th align="right">Pre Selección Cartera:</th>
                <td>
                    <input type="radio" name="preSeleccionCartera" value="1">SI
                    <input type="radio" checked="true" name="preSeleccionCartera" value="0">NO
                </td>
            </tr>
            <tr>
                <td align="center" colspan="2"><br><input type="button" id="botonAlta" value="Aceptar" onclick="validaCamposAltaFondeador()"> <input type="button" id="botonRegresar" value="Regresar" onClick="redireccionMenuGestionFondeadores()"><br><br></td>
            </tr>
        </table>
        <center>
            <h2>Lista Fondeadores</h2> 
        </center>
        <table border="2" cellspacing="5" align="center" >
            <tr bgcolor="#00bfff">
                <td class="whitetext" align="center">Nombre</td>
                <td class="whitetext" align="center">Prioridad</td>
                <td class="whitetext" align="center">Revolvente</td>
                <td class="whitetext" align="center">Preseleccion</td>
            </tr>
            
            <%for (FondeadorVO fond : fondeadores) {%>
            <tr>
                <td align="center"><%=fond.getNombre()%></td>
                <td align="center"><%=fond.getPrioridad()%></td>
                <td align="center"><%=(fond.getRevolvente()==1)?"Si":"No"%></td>
                <td align="center"><%=(fond.getPreSeleccionCartera()==1)?"Si":"No"%></td>
            </tr>
            <%}%>
        </table>
    </form>

</body>
</html>
