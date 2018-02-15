<%-- 
    Document   : altaAuditor
    Created on : 18/02/2015, 12:49:32 PM
    Author     : avillanueva
--%>

<%@page import="com.sicap.clientes.vo.AuditoresVO"%>
<%@page import="com.sicap.clientes.util.Notification"%>
<%@page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%
Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
AuditoresVO auditorVO = (AuditoresVO) request.getAttribute("AUDITOR");
if(auditorVO == null){
    auditorVO = new AuditoresVO();
}
%>
<html>
    <head>
        <title> Alta Auditor</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
        <script>
            function registrar(){
                window.document.forma.command.value = 'ingresaAuditor';
                deshabilita();
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
                window.document.forma.submit();
            }
            function regresar(){
                deshabilita();
                window.document.forma.command.value = 'auditores';
                window.document.forma.submit();
            }
            function habilita(){
                document.getElementById("Registrar").disabled = false;
                document.body.style.cursor = "default";
            }
            function deshabilita(){
                document.getElementById("Registrar").disabled = true;
                document.body.style.cursor = "wait";
            }
        </script>
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center>
        <h2>Alta Auditor</h2> 
    </center>
    <form method="post" name="forma" action="admin">
        <input type="hidden" name="command" value="">
        <table border="0" cellspacing="5" align="center">
            <tr>
                <td align="center" colspan="2">
                    <%=HTMLHelper.displayNotifications(notificaciones)%><br>
                </td>
            </tr>
            <%if(auditorVO.getNumAuditor() != 0){%>
            <tr>
                <th align="right">Numero:</th>
                <td align="left"><input type="text" value="<%=HTMLHelper.displayField(auditorVO.getNumAuditor())%>" size="5" disabled></td>
            </tr>
            <%}%>
            <tr>
                <th align="right">Nombre(s):</th>
                <td align="left"><input type="text" name="nombre" value="<%=HTMLHelper.displayField(auditorVO.getNombre())%>"></td>
            </tr>
            <tr>
                <th align="right">A. Paterno:</th>
                <td align="left"><input type="text" name="apPaterno" value="<%=HTMLHelper.displayField(auditorVO.getApPaterno())%>"></td>
            </tr>
            <tr>
                <th align="right">A. Materno:</th>
                <td align="left"><input type="text" name="apMaterno" value="<%=HTMLHelper.displayField(auditorVO.getApMaterno())%>"></td>
            </tr>
            <tr>
                <th align="right">RFC:</th>
                <td align="left"><input type="text" name="rfc" value="<%=HTMLHelper.displayField(auditorVO.getRfc())%>"  maxlength="13"></td>
            </tr>
            <tr>
                <td align="center" colspan="2">
                    <br>
                    <input type="button" name="Registrar" id="Registrar" value="Registrar" onclick="registrar()">
                    <input type="button" name="Regresar" id="Regresar" value="Regresar" onClick="regresar()">
                    <br><br></td>
            </tr>
        </table>
    </form>
    <jsp:include page="footer.jsp" flush="true"/>
</body>
</html>