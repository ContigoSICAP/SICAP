<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>   
    <title>Carga Archivo Paynet</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>
    function recibir(){
        window.document.getElementById("boton").disabled = true;
        if ( window.document.encForm.file1.value=='' ){
            alert("Debe especificar al menos un archivo a cargar");
            window.document.getElementById("boton").disabled = false;
            return false;
        }
        if (confirm("¿Desea cargar el archivo: "+window.document.encForm.file1.value+" ?")){
            window.document.encForm.action="cargaConfirmPagosPaynet.jsp";
            window.document.encForm.submit();
        }
        else{
            window.document.getElementById("boton").disabled = false;
        }
    }
    function nuevoArchivo(){
        window.document.getElementById("boton").disabled = false;
        window.document.encForm.file1.value='';
    }
    function regresar() {
        window.document.encForm.action = "menuPagosPaynet.jsp";
        window.document.encForm.submit();
    }
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<%
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
%>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<center>
<form name="encForm" method="post" action="" ENCTYPE="multipart/form-data">
<table border="0" width="100%">
    <tr>
        <td align="center" width="75%">
            <h3>Carga Archivo Confirmaci&oacute;n</h3>
            <%=HTMLHelper.displayNotifications(notificaciones)%>			
            <table border="0" cellpadding="0" width="100%">
                <tr>
                    <td>
                        <br><br>
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="2">						
                        <input type="file" name="file1" size="50">
                    </td>
                </tr>
                <tr>
                    <td align="center">
                        <br><input type="button" onclick="recibir()" value="Upload" id="boton">					
                        <input type="button" onclick="nuevoArchivo()" value="Nuevo archivo" id="botonNuevo"><br><br>
                        <input type="button" onclick="regresar()" value="Regresar" id="botonRegresar"><br><br>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</form>
</center>
</body>
</html>

