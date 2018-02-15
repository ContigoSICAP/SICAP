<%@page import="java.util.TreeMap"%>
<%@page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@page import="com.sicap.clientes.util.Notification"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
    <head>   
        <title>Carga Lote de Tarjetas</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script>
            function recibir() {
                desactivar();
                if(window.document.encForm.loteBanco.value == ''){
                    if(!confirm("El campo Lote se encuentra vacío. ¿Desea continuar?")){
                        activar();
                        return false;
                    }
                }
                if (window.document.encForm.file1.value == '') {
                    alert("Debe especificar al menos un archivo a cargar");
                    activar();
                    return false;
                }
                if (confirm("¿Está seguro de que desea procesar el archivo: " + window.document.encForm.file1.value + " ?")) {
                    window.document.encForm.action = "procesaLoteTarjetas.jsp";
                    window.document.encForm.submit();
                } else
                    activar();
            }
            /*function nuevoArchivo() {
                activar();
                window.document.encForm.file1.value = '';
            }*/
            function regresar() {
                window.document.encForm.action = "menuTarjetas.jsp";
                window.document.encForm.submit();
            }
            function activar(){
                document.body.style.cursor = "default";
                window.document.getElementById("boton").disabled = false;
                //window.document.getElementById("botonNuevo").disabled = false;
                window.document.getElementById("botonRegresar").disabled = false;
            }
            function desactivar(){
                document.body.style.cursor = "wait";
                window.document.getElementById("boton").disabled = true;
                //window.document.getElementById("botonNuevo").disabled = true;
                window.document.getElementById("botonRegresar").disabled = true;
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">

    </head>
<%
Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
TreeMap catBancos = CatalogoHelper.getCatalogoBancosDispersionTarjeta();
%>
    <body leftmargin="0" topmargin="0" onLoad="cacheOff()">
        <jsp:include page="header.jsp" flush="true"/>
    <center>
        <form name="encForm" method="post" action="" ENCTYPE="multipart/form-data">
            <table border="0" width="100%">
                <tr>
                    <td align="center" width="75%">
                        <h3>Cargar Lote de Tarjetas</h3>
                        <%=HTMLHelper.displayNotifications(notificaciones)%>			
                        <table border="0" cellpadding="0" width="100%">
                            <tr><td colspan="2"><br><br></td></tr>
                            <tr>
                                <td width="50%" align="right">Banco&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                <td width="50%">  
                                    <select name="banco" size="1">
                                        <%=HTMLHelper.displayCombo(catBancos, 0)%>
                                    </select>
                                    <br>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Lote&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                <td width="50%">  
                                    <input type="text" name="loteBanco" size="20" maxlength="20">
                                </td>
                            </tr>
                            <tr><td colspan="2"><br></td></tr>
                            <tr>
                                <td align="center" colspan="2">						
                                    <input type="file" name="file1" size="50">
                                </td>
                            </tr>
                            <tr>
                                <td align="center" colspan="2">
                                    <br><input type="button" onclick="recibir()" value="Upload" id="boton">
                                    <!--<input type="button" onclick="nuevoArchivo()" value="Nuevo archivo" id="botonNuevo"><br><br>-->
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

