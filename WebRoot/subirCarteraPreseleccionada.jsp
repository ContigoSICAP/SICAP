<%@page import="java.util.Vector"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.sicap.clientes.dao.CatalogoDAO"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>   
        <title>Actualizar Cartera Preseleccionada</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script>

            function regresar(){
                window.document.encForm.command.value='administracionFondeadores';
                //action="admin"
                //window.document.encForm.action='admin';
                window.document.encForm.enctype = 'application/x-www-form-urlencoded';
                window.document.encForm.submit();
            }
            
            function procesarSinRechazos(){
                
                var combo = window.document.encForm.idFondeadorCombo;
                var selected = combo.options[combo.selectedIndex].text;
                
                
                 window.document.getElementById("boton").disabled = true;
                 window.document.getElementById("botonSin").disabled = true;
                if(combo.value == 0){
                   alert("Seleccione un fondeador");
                   window.document.getElementById("boton").disabled = false;
                   window.document.getElementById("botonSin").disabled = false;
                   return false;
               }
               
               if (confirm("¿Está seguro de que desea asignar cartera preseleccionada a "+selected+" SIN Archivo de rechazados?")) {
                    window.document.encForm.command.value='asignarCarteraPreSinRechazos';
                    window.document.encForm.enctype = 'application/x-www-form-urlencoded'
                    window.document.encForm.submit();
                } else {
                    window.document.getElementById("boton").disabled = false;
                    window.document.getElementById("botonSin").disabled = false;
                }
                
                
                
            }
            
            function procesar() {
                var combo = window.document.encForm.idFondeadorCombo;
                var selected = combo.options[combo.selectedIndex].text;

                window.document.getElementById("boton").disabled = true;
                window.document.getElementById("botonSin").disabled = true;
                if(combo.value == 0){
                   alert("Seleccione un fondeador");
                   window.document.getElementById("boton").disabled = false;
                   window.document.getElementById("botonSin").disabled = false;
                   return false;
               }
                if (window.document.encForm.file1.value == '') {
                    alert("Debe especificar al menos un archivo a cargar");
                    window.document.getElementById("boton").disabled = false;
                    window.document.getElementById("botonSin").disabled = false;
                    return false;
                }
                

                if (confirm("¿Está seguro de que desea asignar cartera preseleccionada a "+selected+" \ncon el arcivo de cartera rechazada: " + window.document.encForm.file1.value + " ?")) {
                    //window.document.encForm.command.value = 'asignaCarteraPreseleccion';//Command para ejecutar actualizacioon decartera pre
                    //window.document.encForm.enctype = 'application/x-www-form-urlencoded';
                    window.document.encForm.action='procesaCarteraPreseleccionada.jsp'//redireccionar a otro jsp
                    window.document.encForm.submit();
                } else {
                    window.document.getElementById("boton").disabled = false;
                    window.document.getElementById("botonSin").disabled = false;
                }
            }

            function nuevoArchivo() {
                
                window.document.encForm.file1.value = '';
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
<%
Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
CatalogoDAO catalogoDao = new CatalogoDAO();

TreeMap catFondeador = CatalogoHelper.getCatalogoFondeadorPreseleccion();
int idFondeador = 0;

%>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <center>
            <form name="encForm" method="post" action="admin" enctype="multipart/form-data">
                <input type="hidden" name="command" value="">
                <table border="0" width="100%">
                    <tr>
                        <td align="center" width="75%">
                            <h3>Actualizar Cartera Preseleccionada</h3>
                            <%=HTMLHelper.displayNotifications(notificaciones)%>			
                            <table border="0" cellpadding="0" width="100%">
                                <tr>
                                    <td align="right" >Fondeador a preseleccionar cartera</td>
                                    <td width="50%">
                                        <select name="idFondeadorCombo" size="1" >
                                            <%= HTMLHelper.displayCombo(catFondeador, idFondeador)%>
                                        </select>
                                    </td>
                                </tr>
                                <tr><br/></tr>
                               
                                <tr>
                                    <td align="center" colspan="3">						
                                        <input type="file" name="file1" size="50">
                                    </td>
                                </tr>
                                <tr>
                                    <td align="center" colspan="3">
                                        <%if(!catalogoDao.ejecutandoCierre()){%>
                                        <br><input type="button" onclick="procesar()" value="Upload" id="boton">
                                        <input type="button" onclick="nuevoArchivo()" value="Nuevo archivo" id="botonNuevo">
                                        <br /><br /><input type="button" onclick="procesarSinRechazos()" value="Sin Rechazos" id="botonSin">

                                        <%}%>
                                       				
                                    </td>
                                </tr>
                                <tr>
                                    <td align="center" colspan="3">
                                        <br /><br /><input type="button" value="Regresar" onclick="regresar();"><br><br>					
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

