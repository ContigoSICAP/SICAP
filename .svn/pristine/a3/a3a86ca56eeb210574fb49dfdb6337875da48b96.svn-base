<%-- 
    Document   : buscaAuditor
    Created on : 19/02/2015, 10:56:14 AM
    Author     : avillanueva
--%>

<%@page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.sicap.clientes.vo.AuditoresVO"%>
<%@page import="com.sicap.clientes.util.Notification"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%
Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
ArrayList<AuditoresVO> arrAuditor = new ArrayList<AuditoresVO>();
int numAuditores = 0;
if(request.getAttribute("AUDITORES") != null){
    arrAuditor = (ArrayList<AuditoresVO>) request.getAttribute("AUDITORES");
    numAuditores = arrAuditor.size();
}
%>
<html>
    <head>
        <title>Buqueda Auditores</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
        <script>
            function buscaAuditor(){
                window.document.forma.command.value = 'buscaAuditorModificar';
                deshabilita();
                if (window.document.forma.idAuditor.value=='' && window.document.forma.nombre.value=='' && window.document.forma.apPaterno.value==''
                         && window.document.forma.apMaterno.value=='' && window.document.forma.rfc.value==''){
                    alert("Ingrese datos para su busqueda");
                    habilita();
                    return false;
                }
                if (!document.getElementById("idAuditor").disabled && window.document.forma.idAuditor.value==0){
                    alert("Ingrese un numero valido");
                    window.document.forma.idAuditor.value = '';
                    habilitaCampos();
                    habilita();
                    return false;
                }
                window.document.forma.submit();
            }
            function consultaAuditor(numAuditor){
                deshabilita();
                window.document.forma.command.value = 'consultaDatosAuditor';
		window.document.forma.idAuditor.value = numAuditor;
                window.document.forma.submit();
            }
            function regresar(){
                deshabilita();
                window.document.forma.command.value = 'auditores';
                window.document.forma.submit();
            }
            function buscaID(){
                document.getElementById("nombre").disabled = true;
                document.getElementById("apPaterno").disabled = true;
                document.getElementById("apMaterno").disabled = true;
                document.getElementById("rfc").disabled = true;
            }
            function buscaNombre(){
                document.getElementById("idAuditor").disabled = true;
                document.getElementById("rfc").disabled = true;
            }
            function buscaRFC(){
                document.getElementById("idAuditor").disabled = true;
                document.getElementById("nombre").disabled = true;
                document.getElementById("apPaterno").disabled = true;
                document.getElementById("apMaterno").disabled = true;
            }
            function habilitaCampos(){
                document.getElementById("idAuditor").disabled = false;
                document.getElementById("nombre").disabled = false;
                document.getElementById("apPaterno").disabled = false;
                document.getElementById("apMaterno").disabled = false;
                document.getElementById("rfc").disabled = false;
            }
            function habilita(){
                document.getElementById("Buscar").disabled = false;
                document.getElementById("Regresar").disabled = false;
                document.body.style.cursor = "default";
            }
            function deshabilita(){
                document.getElementById("Buscar").disabled = true;
                document.getElementById("Regresar").disabled = true;
                document.body.style.cursor = "wait";
            }
        </script>
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center>
        <h2>Busqueda Auditores</h2>
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
                <td align="left"><input type="text" id="idAuditor" name="idAuditor" value="" size="5" onclick="buscaID()"></td>
            </tr>
            <tr>
                <th align="right">RFC:</th>
                <td align="left"><input type="text" id="rfc" name="rfc" value=""  maxlength="13" onclick="buscaRFC()"></td>
            </tr>
            <tr>
                <th align="right">Nombre(s):</th>
                <td align="left"><input type="text" id="nombre" name="nombre" value="" onclick="buscaNombre()"></td>
            </tr>
            <tr>
                <th align="right">A. Paterno:</th>
                <td align="left"><input type="text" id="apPaterno" name="apPaterno" value="" onclick="buscaNombre()"></td>
            </tr>
            <tr>
                <th align="right">A. Materno:</th>
                <td align="left"><input type="text" id="apMaterno" name="apMaterno" value="" onclick="buscaNombre()"></td>
            </tr>
            <tr>
                <td align="center" colspan="2">
                    <br>
                    <input type="button" name="Buscar" id="Buscar" value="Buscar" onclick="buscaAuditor()">
                    <input type="button" name="Regresar" id="Regresar" value="Regresar" onClick="regresar()">
                    <br><br></td>
            </tr>
        </table>
        <%if(numAuditores > 0){%>
        <center><h2>Lista Auditores Encontrados</h2></center>
        <table border="1" align="center" width="50%">
            <tr>
                <td align="center"><b>No</b></td>
                <td align="center"><b>Nombre</b></td>
                <td align="center"><b>RFC</b></td>
                <td align="center"><b>Estatus</b></td>
            </tr>
            <%for (AuditoresVO auditor : arrAuditor) {%>
            <tr>
                <td align="center"><%=HTMLHelper.displayField(auditor.getNumAuditor())%></td>
                <td align="left"><a href="#" onclick="consultaAuditor(<%=auditor.getNumAuditor()%>)"><%=HTMLHelper.displayField(auditor.getNombre()+" "+auditor.getApPaterno()+" "+auditor.getApMaterno())%></a></td>
                <td align="center"><%=HTMLHelper.displayField(auditor.getRfc())%></td>
                <td align="center"><%=HTMLHelper.displayField(auditor.getEstatus() == 1 ? "Activo" : "Baja")%></td>
            </tr>
            <%}%>
        </table>
        <%}%>
    </form>
    <jsp:include page="footer.jsp" flush="true"/>
</body>
</html>