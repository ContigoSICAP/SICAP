<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.util.Notification"%>

<html>
<head>
<title>Menú principal administración</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>
    function loadFile(){
        var file = document.getElementById("file").value;
        document.getElementById("getFile").disabled = true;
        document.getElementById("botonRegresar").disabled = true;
        document.body.style.cursor = "wait";
        if( file !="" ){
            if(window.confirm("¿ Deseas Importar el Archivo "+file +' ?')){
                window.document.forma.action="guardaOrdenDePago.jsp";
                window.document.forma.target="_self";
                window.document.forma.submit();
            }	
        }else{
            alert("Debes de Seleccionar el Archivo a Importar");
        }	
    }
    function regresar() {
        window.document.forma.action = "administracionOrdenesDePago.jsp";
        window.document.forma.submit();
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
    <h2>Cargar Respuesta Bancos</h2> 
    <%=HTMLHelper.displayNotifications(notificaciones)%>
<form name="forma" action="admin" method="post" enctype="multipart/form-data">
<input type="hidden" name="command" value="">
<table border="0" width="100%" cellspacing="0">
    <%if(request.isUserInRole("ADM_ORDENESPAGO")){%>
    <tr>
        <td align="right">Selecciona el Archivo a Procesar<br><br></td>
        <td><input type="file" id="file" name="autorizacion" size="35"></td>
    </tr>
    <tr>
        <td align="center" colspan="2">
            <input name="getFile" type="button" class="text" id="getFile" onClick="loadFile()" value="Cargar Archivo">
            <input name="getFile" type="button" class="text" id="botonRegresar" onClick="regresar()" value="Regresar">
        </td>
    </tr>
    <%}%>	
</table>
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>