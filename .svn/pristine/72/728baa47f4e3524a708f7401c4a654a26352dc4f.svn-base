<%@page import="com.sicap.clientes.util.Notification"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.TreeMap"%>
<%@ page contentType="text/html" pageEncoding="iso-8859-1"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<%
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <title>Importaci&oacute;n de Informaci&oacute;n</title>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="./js/functions.js" ></script>
        <script>
            function importar(){
                document.getElementById("boton").disabled = true;
                window.document.forma.action="procesaImportacion.jsp?tipo="+window.document.forma.tipoArchivo.value+"&origen="+window.document.forma.origen.value;
                document.body.style.cursor = "wait";
                window.document.forma.submit();
                /*var nombre = window.document.forma.archivo.value;
                var array = nombre.split(".");
                if(nombre.split(".").length == 2){
                    //if(array[1] == "xls" || array[1] == "xlsx" || array[1] == "ods"){
                    if(array[1] == "xls" || array[1] == "csv" || array[1] == "xlsx"){
                        if(confirm("Desea procesar la información "+window.document.forma.tipoArchivo.value)){
                            window.document.forma.action="procesaImportacion.jsp?tipo="+window.document.forma.tipoArchivo.value;
                            document.body.style.cursor = "wait";
                            window.document.forma.submit();
                        }
                    } else
                        alert("El archivo no contiene tiene un formato permitido");
                } else
                    alert("El archivo contiene varias extenciones");*/
            }
        </script>
    </head>
    <body>
    <center>
        <jsp:include page="header.jsp" flush="true"/>
        <form name="forma" action="" method="post" ENCTYPE="multipart/form-data">
            <input type="hidden" name="command" value=""/>
            <h2>Administraci&oacute;n de Importaci&oacute;n Informaci&oacute;n<br/><br/></h2>
            <table border="0" cellpadding="1" cellspacing="1" width="60%">
                <tr>
                    <td align="center" colspan="2"><%=HTMLHelper.displayNotifications(notificaciones)%></td>
                </tr>
                <tr>
                    <td>
                        <table border="0" cellpadding="0" cellspacing="1" align="center">
                            <tr>
                                <td align="right">Archivo a Importar&nbsp;</td>
                                <td><select name="tipoArchivo" id="tipoArchivo">
                                        <option>Seleccionar...</option>
                                        <option value="01Tasas">Tasas</option>
                                        <option value="02Sucursales">Sucursales</option>
                                        <option value="03Asesores">Asesores</option>
                                        <option value="04Grupos">Grupos</option>
                                        <option value="05Clientes">Clientes</option>
                                        <option value="06Solicitudes">Solicitudes</option>
                                        <option value="07Ciclos Grupales">Ciclos Grupales</option>
                                        <option value="08Integrantes Grupos">Integrantes Grupos</option>
                                        <option value="09Pagos">Pagos</option>
                                        <option value="10Saldos">Saldos</option>
                                        <option value="11Negocio">Actividad Economica</option>
                                        <option value="12RFC">RFC</option>
                                        <option value="13Desembolso">Desembolso</option>
                                    </select>
                                </td>
                            </tr>
                            <tr><td colspan="2">&nbsp;</td></tr>
                            <!--<tr><td colspan="2"><input type="file" name="archivo" size="45"/></td></tr>-->
                            <tr>
                                <td align="right">Origen Informaci&oacute;n</td>
                                <td><select name="origen" id="origen">
                                        <option value="01CreditoReal"/>Cr&eacute;dito Real
                                        <option value="02Credex"/>Credex
                                    </select></td></tr>
                            <tr><td colspan="2">&nbsp;</td></tr>
                            <tr>
                                <td colspan="2" align="Center"><input type="button" id="boton" value="Subir" onclick="importar()"/></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </form>
        <%@include file="footer.jsp"%>
    </center>
    </body>
</html>