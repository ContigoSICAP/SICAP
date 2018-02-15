<%-- 
    Document   : modificaAuditor
    Created on : 18/02/2015, 12:49:32 PM
    Author     : avillanueva
--%>

<%@page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@page import="com.sicap.clientes.vo.AuditoresVO"%>
<%@page import="com.sicap.clientes.util.Notification"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%
Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
AuditoresVO auditor = (AuditoresVO)request.getAttribute("AUDITOR");
%>
<html>
    <head>
        <title>Modifica Auditor</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
        <script>
            function modificar(numAuditor){
                deshabilita();
                window.document.forma.command.value = 'modificaDatosAuditor';
                if (window.document.forma.nombre.value=='' || !esTexto( "^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$",window.document.forma.nombre.value)){
                    alert("Nombre Incorrecto. Ingrese un nombre valido");
                    window.document.forma.nombre.focus();
                    habilita();
                    return false;
                }
                if (window.document.forma.apPaterno.value=='' || !esTexto( "^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$",window.document.forma.apPaterno.value)){
                    alert("Apellido Paterno Incorrecto. Ingrese un apellido valido");
                    window.document.forma.apPaterno.focus();
                    habilita();
                    return false;
                }
                if (window.document.forma.apMaterno.value=='' || !esTexto( "^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$",window.document.forma.apMaterno.value)){
                    alert("Apellido Materno Incorrecto. Ingrese un apellido valido");
                    window.document.forma.apMaterno.focus();
                    habilita();
                    return false;
                }
                if (window.document.forma.rfc.value=='' || !esRFCPersonaFisica(window.document.forma.rfc.value)){
                    alert("RFC Incorrecto. Ingrese un RFC valido");
                    window.document.forma.rfc.focus();
                    habilita();
                    return false;
                }
                if(confirm("øDesea realizar los cambios?")){
                    window.document.forma.submit();
                }else{
                    habilita();
                }
            }
            function regresar(){
                deshabilita();
                window.document.forma.command.value = 'buscaAuditor';
                window.document.forma.submit();
            }
            function habilita(){
                document.getElementById("Modificar").disabled = false;
                document.getElementById("Regresar").disabled = false;
                document.body.style.cursor = "default";
            }
            function deshabilita(){
                document.getElementById("Modificar").disabled = true;
                document.getElementById("Regresar").disabled = true;
                document.body.style.cursor = "wait";
            }
        </script>
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center>
        <h2>Modifica Auditor</h2>
    </center>
    <form method="post" name="forma" action="admin">
        <input type="hidden" name="command" value="">
        <table border="0" cellspacing="5" align="center">
            <tr>
                <td align="center" colspan="2">
                    <%=HTMLHelper.displayNotifications(notificaciones)%><br>
                </td>
            </tr>
            <tr>
                <th align="right">Numero:</th>
                <td align="left"><input type="text" id="idAuditor" name="idAuditor" value="<%=HTMLHelper.displayField(auditor.getNumAuditor())%>" size="5" readonly="readonly"></td>
            </tr>
            <tr>
                <th align="right">Activo:</th>
                <td align="left"><%=HTMLHelper.radioBoton("estatus",(auditor.getEstatus() == 1 ? true : false),false)%></td>
            </tr>
            <tr>
                <th align="right">Nombre(s):</th>
                <td align="left"><input type="text" id="nombre" name="nombre" value="<%=HTMLHelper.displayField(auditor.getNombre())%>"></td>
            </tr>
            <tr>
                <th align="right">A. Paterno:</th>
                <td align="left"><input type="text" id="apPaterno" name="apPaterno" value="<%=HTMLHelper.displayField(auditor.getApPaterno())%>"></td>
            </tr>
            <tr>
                <th align="right">A. Materno:</th>
                <td align="left"><input type="text" id="apMaterno" name="apMaterno" value="<%=HTMLHelper.displayField(auditor.getApMaterno())%>"></td>
            </tr>
            <tr>
                <th align="right">RFC:</th>
                <%if( request.isUserInRole("AUDITOR_RFC") ){%>
                <td align="left"><input type="text" id="rfc" name="rfc" maxlength="13" value="<%=HTMLHelper.displayField(auditor.getRfc())%>"  maxlength="13"></td>
                <%}else{%>
                <td align="left"><input type="text" id="rfc" name="rfc" maxlength="13" value="<%=HTMLHelper.displayField(auditor.getRfc())%>"  maxlength="13" readonly="readonly"></td>
                <%}%>
            </tr>
            <tr>
                <td align="center" colspan="2">
                    <br>
                    <input type="button" name="Modificar" id="Modificar" value="Modificar" onclick="modificar()">
                    <input type="button" name="Regresar" id="Regresar" value="Regresar" onClick="regresar()">
                    <br><br></td>
            </tr>
        </table>
    </form>
    <jsp:include page="footer.jsp" flush="true"/>
</body>
</html>