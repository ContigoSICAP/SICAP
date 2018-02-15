<%-- 
    Document   : capturaEmpleoGrupal
    Created on : 2/10/2012, 11:15:04 AM
    Author     : Alex
--%>

<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.util.SolicitudUtil"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.EmpleoVO"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="java.util.TreeMap"%>
<jsp:directive.page import="com.sicap.clientes.vo.DireccionVO"/>
<html>
    <head>
        <title>Actividad Economica</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script>

            function guardaEmpleoCliente(Antempleo){
                document.getElementById("boton").disabled = true;
                window.document.forma.command.value = "guardaEmpleoClienteGrupal";
               if ( window.document.forma.razonSocial.value==''){
                    alert('Debe introducir el Nombre de la actividad');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if ( window.document.forma.sector.value==''||window.document.forma.sector.value==0){
                    alert('Debe introducir el Nombre del sector');
                    
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if ( window.document.forma.ubicacionNegocio.value==''||window.document.forma.ubicacionNegocio.value==0){
                    alert('Debe introducir la Ubicaci\u00f3n F\u00edsica');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if ( window.document.forma.idColonia.value==0){
                    alert('Debe indicar la colonia');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if ( window.document.forma.calle.value==''){
                    alert('Debe capturar la Calle');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if ( window.document.forma.numeroExterior.value==''){
                    alert('Debe capturar el Número exterior');
                    document.getElementById("boton").disabled = false; 
                    return false;
                }
                if ( window.document.forma.telefono.value==''){
                    alert('Debe capturar el Telefono');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                 if(Antempleo!=0){
                     if ( window.document.forma.antEmpleo.value==0){
                         alert('Debe capturar indicar la Antigüedad en el empleo');
                         document.getElementById("boton").disabled = false;
                         return false;
                     }
                 }
                if(Antempleo==0){
                    if ( window.document.forma.fechaInicioNeg.value!=''){
			if (esPosterior(window.document.forma.fechaInicioNeg.value,'Hoy')||!esFechaValida(window.document.forma.fechaInicioNeg,false) ){
				alert("La Fecha de Inicio del Negocio es inv\u00e1lida");
                                document.getElementById("boton").disabled = false;
				return false;
			}
                    }else{
			alert("Debe capturar la Fecha de Inicio del Negocio");
                        document.getElementById("boton").disabled = false;
			return false;
                    }
                }
                if ( window.document.forma.sueldoMensual.value=='' ||window.document.forma.sueldoMensual.value==0|| !esFormatoMoneda(window.document.forma.sueldoMensual.value)){
                    alert('El Sueldo mensual es inválido');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.numeroEmpleados.value ==''){
                    alert('Debe capturar el Numero de Empleados')
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (!esEntero(window.document.forma.numeroEmpleados.value))
                {
                    alert('El numero de Empleados debe ser un entero')
                    document.getElementById("boton").disabled = false;
                    return false;                    
                }
		
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }

        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <%
        Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
        ClienteVO cliente = (ClienteVO) session.getAttribute("CLIENTE");
        EmpleoVO empleoCliente = new EmpleoVO();
        DireccionVO direccion = new DireccionVO();
        int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
        int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
        SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
        if (cliente.solicitudes[indiceSolicitud].empleo != null) {
            empleoCliente = cliente.solicitudes[indiceSolicitud].empleo;
            if (empleoCliente != null && empleoCliente.direccion != null) {
                direccion = empleoCliente.direccion;
            }
        }
        
        TreeMap catExpLaboral = CatalogoHelper.getCatalogo(ClientesConstants.CAT_ANT_EMPLEO);
        TreeMap catActividades = CatalogoHelper.getCatalogoActividades();
        TreeMap catUbicacionNegocio = CatalogoHelper.getCatalogo("c_ubicaciones_negocio");
    %>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <jsp:include page="menuIzquierdo.jsp" flush="true"/>
    <CENTER>
        <form name="forma" action="controller" method="post">
            <input type="hidden" name="command" value="">
            <input type="hidden" name="idColonia" value="<%=HTMLHelper.displayField(direccion.idColonia)%>">
            <input type="hidden" name="asentamientoCP" value="<%=HTMLHelper.displayField(direccion.asentamiento_cp)%>">
            <input type="hidden" name="idCliente" value="<%=cliente.idCliente%>">
            <input type="hidden" name="idSolicitud" value="<%=cliente.solicitudes[indiceSolicitud].idSolicitud%>">
            <table border="0" width="100%">
                <tr>
                    <td align="center">	   

                        <h3>Actividad Economica</h3>
                        <%=HTMLHelper.displayNotifications(notificaciones)%>
                        <table width="100%" border="0" cellpadding="0">
                            <tr>
                                <td width="50%" align="right">Actividad</td>
                                <td width="50%">
                                    <input type="hidden" name="razonSocialAnterior" value="<%=HTMLHelper.displayField(empleoCliente.razonSocial)%>">
                                    <input type="text" name="razonSocial" size="40" maxlength="70" value="<%=HTMLHelper.displayField(empleoCliente.razonSocial)%>">
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Sector</td>
                                <td width="50%">
                                    <input type="hidden" name="tipoSectorAnterior" value="<%=HTMLHelper.displayField(empleoCliente.tipoSector)%>">
                                    <select name="sector" size="1" >
                                        <%=HTMLHelper.displayCombo(catActividades, empleoCliente.tipoSector)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Ubicaci&oacute;n F&iacute;sica</td>
                                <td width="50%">
                                    <input type="hidden" name="ubicacionNegocioAnterior" value="<%=HTMLHelper.displayField(empleoCliente.ubicacionNegocio)%>">
                                    <select name="ubicacionNegocio" size="1" >
                                        <%=HTMLHelper.displayCombo(catUbicacionNegocio, empleoCliente.ubicacionNegocio)%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="100%" align="center" colspan="2"><a href="#" onClick="ayudaCodigoPostal()">Ayuda CP</a></td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Estado</td>
                                <td width="50%">  
                                    <input type="hidden" name="estadoAnterior" value="<%=HTMLHelper.displayField(direccion.estado)%>">
                                    <input type="text" name="estado" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.estado)%>" readonly>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Delegación/Municipio</td>
                                <td width="50%">  
                                    <input type="hidden" name="municipioAnterior" value="<%=direccion.municipio%>">
                                    <input type="text" name="municipio" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.municipio)%>" readonly>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Colonia</td>
                                <td width="50%">  
                                    <input type="hidden" name="coloniaAnterior" value="<%=HTMLHelper.displayField(direccion.colonia)%>">
                                    <input type="text" name="colonia" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.colonia)%>" readonly>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">C&oacute;digo postal</td>
                                <td width="50%">  
                                    <input type="hidden" name="cpAnterior" value="<%=HTMLHelper.displayField(direccion.cp)%>">
                                    <input type="text" name="cp" size="6" maxlength="6" value="<%=HTMLHelper.displayField(direccion.cp)%>" readonly>
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Calle</td>
                                <td width="50%">
                                    <input type="hidden" name="calleAnterior" value="<%=HTMLHelper.displayField(direccion.calle)%>">
                                    <input type="text" name="calle" size="30" maxlength="60" value="<%=HTMLHelper.displayField(direccion.calle)%>">
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">N&uacute;mero exterior</td>
                                <td width="50%">
                                    <input type="hidden" name="numeroExteriorAnterior" value="<%=HTMLHelper.displayField(direccion.numeroExterior)%>">
                                    <input type="text" name="numeroExterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(direccion.numeroExterior)%>">
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">N&uacute;mero interior</td>
                                <td width="50%">  
                                    <input type="hidden" name="numeroInteriorAnterior" value="<%=HTMLHelper.displayField(direccion.numeroInterior)%>">
                                    <input type="text" name="numeroInterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(direccion.numeroInterior)%>">
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Tel&eacute;fono</td>
                                <td width="50%">
                                    <input type="text" name="telefono" size="10" maxlength="10" value="<%=HTMLHelper.displayField(empleoCliente.telefono)%>">
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">Referencia</td>
                                <td width="50%">
                                    <input type="textarea" name="referencia" size="50" maxlength="100" value="<%=HTMLHelper.displayField(empleoCliente.referencia)%>">
                                </td>
                            </tr>
                            <%if(empleoCliente.antEmpleo!=0){%>
                            <tr>
                                <td width="50%" align="right">Antigüedad en el empleo</td>
                                <td width="50%">
                                    <select name="antEmpleo" size="1" >
                                        <%=HTMLHelper.displayCombo(catExpLaboral, empleoCliente.antEmpleo)%>
                                    </select>
                                </td>
                            </tr>
                            <%}else{%>
                            <tr>
                                <td width="50%" align="right">Fecha de inicio del Negocio</td>
                                <td width="50%">
                                    <input type="text" name="fechaInicioNeg" id="fechaInicioNeg" size="10" maxlength="10" value="<%=HTMLHelper.displayField(empleoCliente.fechaInicioNeg)%>" >(dd/mm/aaaa)                                
                                </td>
                            </tr>
                            <%}%>
                            <tr>
                                <td width="50%" align="right">Ingreso mensual</td>
                                <td width="50%">
                                    <input type="text" name="sueldoMensual" size="10" maxlength="9" value="<%=HTMLHelper.displayField(empleoCliente.sueldoMensual)%>">
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" align="right">N&uacute;mero de Empleados</td>
                                <td width="50%">  
                                    <input type="text" name="numeroEmpleados" size="10" maxlength="10" value="<%=empleoCliente.numeroEmpleados%>">
                                </td>
                            </tr>
                            <tr>
                                <%if (solicitud.desembolsado == ClientesConstants.DESEMBOLSADO || solicitud.desembolsado == ClientesConstants.CANCELADO) {%>
                                <td align="center" colspan="2">
                                    <br><input disabled type="button" id="boton" value="Enviar" onClick="guardaEmpleoCliente(<%=empleoCliente.antEmpleo%>)">
                                </td>
                                <%} else {%>
                                <td align="center" colspan="2">
                                    <br><input type="button" id="boton" value="Enviar" onClick="guardaEmpleoCliente(<%=empleoCliente.antEmpleo%>)">
                                </td>
                                <%}%>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </form>
    </CENTER>
    <jsp:include page="footer.jsp" flush="true"/>
</body>
</html>
