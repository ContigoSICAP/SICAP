<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.TreeMap"%>
<%
    /*CobradorVO cobrador = (CobradorVO)request.getAttribute("COBRADOR");
     if(cobrador==null) 
     cobrador = new CobradorVO();*/
    CobradorVO cobrador = new CobradorVO();
    Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
    SolicitudVO solicitud = new SolicitudVO();
    TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
        <title>Alta Cobradores</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script type="text/javascript">
            function guardaCobradores() {
                if (window.document.forma.idSucursal.value == 0) {
                    alert('Debe seleccionar una sucursal');
                    return false;
                }
                if (window.document.forma.nombre.value == '' || !esTexto("^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$", window.document.forma.nombre.value)) {
                    alert('Debe introducir un Nombre v\u00e1lido');
                    return false;
                }
                if (window.document.forma.aPaterno.value == '' || !esTexto("^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$", window.document.forma.aPaterno.value)) {
                    alert('Debe introducir un Apellido Paterno v\u00e1lido');
                    return false;
                }

                if (window.document.forma.aMaterno.value == '' || !esTexto("^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$", window.document.forma.aMaterno.value)) {
                    alert('Debe introducir un Apellido Materno v\u00e1lido');
                    return false;
                }
                if (window.document.forma.estatus.value == 1) {
                    if (!confirm("Si existe un Cobrador Activo este pasara al estatus de Inactivo. ¿Desea Continuar?")) {
                        return false;
                    }
                }
                window.document.forma.submit();
            }
            function redireccionMenuAdmin() {
                window.document.forma.command.value = 'administracionCobradores';
                window.document.forma.submit();
            }
        </script>
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <table border="0" width="100%">
            <tr>
                <td align="center">
                    <h3>Alta de Cobradores</h3>
                    <table border="0" width="100%">
                        <tr>
                            <td align="center">
                                <%=HTMLHelper.displayNotifications(notificaciones)%>
                                <form name="forma" action="admin" method="POST">
                                    <input type="hidden" name="command" value="guardaCobrador">
                                    <input type="hidden" name="idSolicitud" value="<%=solicitud.idSolicitud%>">
                                    <table width="100%" border="0" cellpadding="0">
                                        <tr>
                                            <td width="50%" align="right">Sucursal</td>
                                            <td width="50%" align="left">
                                                <select name="idSucursal" id="idSucursal" onchange="consultaEjecutivo()">
                                                    <%= HTMLHelper.displayCombo(catSucursales, HTMLHelper.getParameterInt(request, "idSucursal"))%>
                                                </select>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td align="right">Nombre</td>
                                            <td width="50%" align="left">  
                                                <input type="text" id="nombre" name="nombre" size="45" maxlength="70" value="<%=HTMLHelper.displayField(cobrador.getNombre())%>">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td width="50%" align="right">Apellido paterno</td>
                                            <td width="50%" align="left">   
                                                <input type="text" id="aPaterno" name="aPaterno" size="45" maxlength="70" value="<%=HTMLHelper.displayField(cobrador.getaPaterno())%>">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td width="50%" align="right">Apellido materno</td>
                                            <td width="50%" align="left">  
                                                <input type="text" id="aMaterno" name="aMaterno" size="45" maxlength="70" value="<%=HTMLHelper.displayField(cobrador.getaMaterno())%>">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td width="50%" align="right">Estatus</td>
                                            <td width="50%">
                                                <select name="estatus" id="estatus">						
                                                    <%= HTMLHelper.getComboStatusCobrador(cobrador.getEstatus())%>						
                                                </select>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td align="center" colspan="2">
                                                <br><input type="button" value="Enviar" onClick="guardaCobradores()">
                                                <input type="button" value="Regresar" onClick="redireccionMenuAdmin()">
                                            </td>
                                        </tr>
                                    </table>
                                </form>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <jsp:include page="footer.jsp" flush="true"/>
    </body>
</html>
