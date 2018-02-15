<%-- 
    Document   : menuIzquierdoRapido
    Created on : 22/03/2016, 12:52:16 PM
    Author     : avillanueva
--%>

<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.SolicitudVO"%>
<%@ page import="com.sicap.clientes.vo.UsuarioVO"%>
<%@ page import="com.sicap.clientes.util.SolicitudUtil"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<%
    int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
    ClienteVO cliente = (ClienteVO) session.getAttribute("CLIENTE");
    int indiceSolicitud = -1;
    SolicitudVO solicitud = null;
    UsuarioVO usuario = (UsuarioVO) session.getAttribute("USUARIO");
    if (idSolicitud == 0) {
        if (request.getAttribute("ID_SOLICITUD") != null) {
            idSolicitud = ((Integer) request.getAttribute("ID_SOLICITUD")).intValue();
        }
    }
    indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
    if (indiceSolicitud >= 0) {
        solicitud = cliente.solicitudes[indiceSolicitud];
    }
%>
<script>
    function consultaSolicitudes() {
        window.document.forma.command.value = 'consultaSolicitudes';
        window.document.forma.target = "_top";
        window.document.forma.submit();
    }
    function capturaRapida() {
        window.document.forma.command.value = 'consultaSolicitudCliente';
        window.document.forma.submit();
    }
    function datosCalificacionCredito() {
        window.document.forma.command.value = 'capturaDatosCredito';
        if (window.document.forma.idSolicitud.value == 0) {
            alert('Es necesario guardar la información previamente');
            return false;
        }
        window.document.forma.submit();
    }
    function archivosAsociados() {
        window.document.forma.command.value = 'consultaArchivosAsociados';
        if (window.document.forma.idSolicitud.value == 0) {
            alert('Es necesario guardar la información previamente');
            return false;
        }
        window.document.forma.submit();
    }
</script>
<style>
    .mennu {
        FONT-WEIGHT: bold; 
        FONT-SIZE: 12px; 
        COLOR: #006633; 
        FONT-FAMILY: Geneva, Arial, Helvetica, sans-serif; 
        TEXT-DECORATION: none; 
    }
    .mennu a:hover {
        FONT-WEIGHT: bold; 
        FONT-SIZE: 12px; 
        COLOR: #FFA125; 
        FONT-FAMILY: Geneva, Arial, Helvetica, sans-serif; 
        TEXT-DECORATION: underline
    }
    .mennu .maintitle {
        FONT-WEIGHT: bold; 
        FONT-SIZE: 16px; 
        COLOR: #006633; 
        FONT-FAMILY: Geneva, Arial, Helvetica, sans-serif; 
        TEXT-DECORATION: none
    }
    .mennu TABLE {
        FONT-SIZE: 12px;
    }
    .mennu tabla {
        BORDER-RIGHT: #ff0000 1px solid; 
        BORDER-TOP: #ff0000 1px solid; 
        FONT-SIZE: 12px; 
        BORDER-LEFT: #ff0000 1px solid; 
        BORDER-BOTTOM: #ff0000 1px solid
    }
    .mennu .ligaRojo {
        FONT-WEIGHT: bold; 
        FONT-SIZE: 12px; 
        COLOR: red; 
        FONT-FAMILY: Geneva, Arial, Helvetica, sans-serif; 
        TEXT-DECORATION: none
    }
    .mennu .campo {
        Background-color: white; 
        bgColor: white; 
        align: center;
        text-align: center;
    }
</style>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Menu Izquiero Rapido</title>
    </head>
    <body>
        <form name="formaOculta" action="controller" method="post">
            <input type="hidden" name="command" value="">
            <input type="hidden" name="idCiclo" value="<%=cliente.solicitudes[indiceSolicitud].idCiclo%>">
            <input type="hidden" name="idGrupo" value="<%=solicitud.idGrupo%>">
        </form>
        <div class="mennu">
            <table cellSpacing=0 cellPadding=0>
                <tr>
                    <td bgColor="#009861">
                        <table cellSpacing=1 cellPadding=5 width=0 border=0>
                            <tr>
                                <td class=campo ><a href="<%=request.getContextPath()%>" target="_top"> Inicio</a></td>
                                <td class=campo ><a href="#" onClick="consultaSolicitudes()"> Solicitudes</a></td>
                                <td class=campo ><a href="#" onClick="capturaRapida()"> Datos Captura R&aacute;pida</a></td>
                                <td class=campo ><a href="#" onClick="datosCalificacionCredito()"> Calificaci&oacute;n Crediticia</a></td>
                                <td class=campo ><a href="#" onClick="archivosAsociados()"> Archivos asociados</a></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </div>
    </body>
</html>
