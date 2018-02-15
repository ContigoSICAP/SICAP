<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.sicap.clientes.dao.CatalogoDAO"%>
<%@ page import="org.apache.log4j.Logger"%>
<%! public static Logger myLogger = Logger.getLogger("recibirPAgosReferenciados");%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>   
        <title>Pagos Referenciados</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script>

            function redireccionMenuAdmin() {
                
                window.document.forma.command.value = 'menuPagosReferenciados';
                window.document.forma.submit();
            }

            function recibir() {

                window.document.getElementById("boton").disabled = true;
                if (window.document.encForm.file1.value == '') {
                    alert("Debe especificar al menos un archivo a cargar");
                    window.document.getElementById("boton").disabled = false;
                    return false;
                }

                if (confirm("¿Está seguro de que desea procesar los pagos del archivo: " + window.document.encForm.file1.value + " ?")) {
                    window.document.encForm.action = "procesaPagosReferenciadosSeguros.jsp";
                    window.document.encForm.submit();
                } else {
                    window.document.getElementById("boton").disabled = false;
                }
            }

            function nuevoArchivo() {
                window.document.getElementById("boton").disabled = false;
                window.document.encForm.file1.value = '';
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
<%
Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
CatalogoDAO catalogoDao = new CatalogoDAO();
DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
Date date = new Date();
myLogger.debug("Sesion: "+request.getRequestedSessionId());
myLogger.debug("User: "+request.getUserPrincipal());
myLogger.debug("IP: "+request.getRemoteAddr());
myLogger.debug("Hora: "+dateFormat.format(date));
myLogger.debug("Browser: "+request.getHeader("user-agent"));
%>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <center>
            <form name="encForm" method="post" action="" ENCTYPE="multipart/form-data">
                <table border="0" width="100%">
                    <tr>
                        <td align="center" width="75%">
                            <h3>Recibir Pagos Referenciados</h3>
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
                                        <%if(!catalogoDao.ejecutandoCierre()){%>
                                        <br><input type="button" onclick="recibir()" value="Upload" id="boton">
                                        <%}%>
                                        <input type="button" onclick="nuevoArchivo()" value="Nuevo archivo" id="botonNuevo"><br><br>					
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

