<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%
    Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
    ClienteVO clientes[] = (ClienteVO[]) request.getAttribute("CLIENTES_POR_RFC");
    UsuarioVO usuario = (UsuarioVO) session.getAttribute("USUARIO");
    boolean SucusalCapRapida = CatalogoHelper.esSucursalCapRapida(usuario.idSucursal);
    boolean SucursalesIxaya = CatalogoHelper.esSucursaldeIxaya(usuario.idSucursal);
%>
<html>
    <head>
        <title>Alta Clientes</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script>

            function submitenter(myfield, e) {
                var keycode;
                if (window.event)
                    keycode = window.event.keyCode;
                else if (e)
                    keycode = e.which;
                else
                    return true;

                if (keycode == 13) {
                    buscar();
                } else
                    return true;
            }

            function buscar() {
                document.getElementById("botonBuscar").disabled = true;
                if (window.document.forma.rfc.value == '' && window.document.forma.idCte.value == '' && window.document.forma.apellidoPaterno.value == '' && window.document.forma.apellidoMaterno.value == '' && window.document.forma.nombreS.value == '') {
                    alert('Introduzca un valor para su búsqueda por favor');
                    document.getElementById("botonBuscar").disabled = false;
                    return false;
                }
                if (window.document.forma.idCte.value != '' && !esEntero(window.document.forma.idCte.value)) {
                    alert('Introduzca un n\u00famero de cliente v\u00e1lido por favor');
                    document.getElementById("botonBuscar").disabled = false;
                    return false;
                }
                window.document.forma.command.value = 'buscaClientePorRFC';
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }

            function consultaCliente(idCliente) {
                document.getElementById("td").disabled = true;
                window.document.forma.command.value = 'consultaCliente';
                window.document.forma.idCliente.value = idCliente;
                window.document.forma.idSolicitud.value = 1;
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }

            function nuevoCliente() {
                document.getElementById("botonNuevoCliente").disabled = true;
                <%if (SucursalesIxaya) {%>
                    alert('El movimiento se debe de realizar en el sistema JUCAVI');
                    return false;
                <%}%>
                window.document.forma.command.value = 'nuevoCliente';
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
            function nuevoClienteCR() {
                document.getElementById("botonNuevoCliente").disabled = true;
                <%if (SucursalesIxaya) {%>
                    alert('El movimiento se debe de realizar en el sistema JUCAVI');
                    return false;
                <%}%>
                window.document.forma.command.value = 'nuevoClienteCapturaRapida';
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
            
            function reporteCartera() {
                window.document.forma.command.value = 'reporteCartera';
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }

            function menuReportes() {
                document.getElementById("td").disabled = true;
                window.document.forma.command.value = 'menuReportes';
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
            function reporteSolicitudEstatus() {
                document.getElementById("td").disabled = true;
                window.document.forma.command.value = 'buscaSolicitudEstatus';
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
            function reporteActulizacionDoc(){
                document.getElementById("td").disabled = true;
                window.document.forma.command.value = 'buscaActualizacionDoc';
                document.body.style.cursor = "wait";
                window.document.forma.submit();
                
            }

            function admonChequera() {
                document.getElementById("td").disabled = true;
                window.document.forma.command.value = 'admonChequera';
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }

            function cambioPasswordUsuario() {
                window.document.forma.command.value = 'cambioPasswordUsuario';
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }

        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <body leftmargin="0" topmargin="0" onload="javascript:window.document.forma.rfc.focus();">
        <jsp:include page="header.jsp" flush="true"/>
        <jsp:include page="menuInicio.jsp" flush="true"/>
    <center>
        <h2>Administraci&oacute;n de Clientes</h2> 
        <h3>B&uacute;squeda por RFC</h3>
        <form name="forma" action="controller" method="post">
            <input type="hidden" name="idCliente" value="">
            <input type="hidden" name="idSolicitud" value="">


            <table border="0" width="100%" callpadding="0" cellspacing="0">
                <tr>
                    <td width="50%" align="right">RFC</td>
                    <td width="50%">
                        <input type="text" name="rfc" size="13" maxlength="13" value="<%=HTMLHelper.displayField(request.getParameter("rfc"))%>" onKeyPress="return submitenter(this, event)">
                    </td>
                </tr>
                <tr>	
                    <td width="50%" align="right">Cliente</td>
                    <td width="50%">
                        <input type="text" name="idCte" size="13" maxlength="13" value="<%=HTMLHelper.displayField(request.getParameter("idCte"))%>" onKeyPress="return submitenter(this, event)">
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Apellido Paterno</td>
                    <td width="50%">
                        <input type="text" name="apellidoPaterno" size="25" maxlength="30" value="<%=HTMLHelper.displayField(request.getParameter("apellidoPaterno"))%>" onKeyPress="return submitenter(this, event)">
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Apellido Materno</td>
                    <td width="50%">
                        <input type="text" name="apellidoMaterno" size="25" maxlength="30" value="<%=HTMLHelper.displayField(request.getParameter("apellidoMaterno"))%>" onKeyPress="return submitenter(this, event)">
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Nombre(s)</td>
                    <td width="50%">
                        <input type="text" name="nombreS" size="25" maxlength="30" value="<%=HTMLHelper.displayField(request.getParameter("nombreS"))%>" onKeyPress="return submitenter(this, event)">
                    </td>
                </tr>


                <tr>
                    <td align="center" colspan="2">
                        <br><input type="button" id="botonBuscar" value="Buscar..." onClick="buscar()">
                    </td>
                </tr>	

                <%
                    if (request.getParameter("command") != null && request.getParameter("command").equals("buscaClientePorRFC") && clientes == null) {
                %>
                
                        <%if(SucusalCapRapida){%>
                            <tr>
                                <td align="center" colspan="2">
                                    <br><input type="button" id ="botonNuevoCliente" value="Nuevo Cliente" onClick="nuevoClienteCR()">
                                </td>
                            </tr>
                        <%}else{%>
                            <tr>
                                <td align="center" colspan="2">
                                    <br><input type="button" id ="botonNuevoCliente" value="Nuevo Cliente" onClick="nuevoCliente()">
                                </td>
                            </tr>
                            <%}%>
                <%}%>
                        
                <tr>
                    <td align="center" colspan="2">
                        <br><%=HTMLHelper.displayNotifications(notificaciones)%>
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="2" id="td">
                        <a href="<%=request.getContextPath()%>"></a><a href="#" onClick="menuReportes()"> Consulta men&uacute; de reportes</a>
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="2" id="td">
                        <a href="<%=request.getContextPath()%>"></a><a href="#" onClick="reporteSolicitudEstatus()"> Consulta de Solicitudes por Estatus</a>
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="2" id="td">
                        <a href="<%=request.getContextPath()%>"></a><a href="#" onClick="reporteActulizacionDoc()"> Consulta de Actualizaci&oacute;n de Documentos</a>
                    </td>
                </tr>
            </table>
            <%
                if (clientes != null && clientes.length > 0) {
            %>
            <table border="0" width="50%" cellspacing="3" cellpadding="0" >
                <tr bgcolor="#009865">
                    <td width="10%" align="center" class="whitetext">N&uacute;mero</td>
                    <td width="20%" align="center" class="whitetext">RFC</td>
                    <td width="70%" align="center" class="whitetext">Nombre</td>
                </tr>
                <%
                    for (int i = 0; i < clientes.length; i++) {
                        ClienteVO cliente = clientes[i];
                %>

                <tr>
                    <td width="10%"><%=cliente.idCliente%></td>
                    <td width="20%" id="td"><a href="#" onClick="consultaCliente(<%=cliente.idCliente%>)"><%=cliente.rfc%></a></td>
                    <td width="70%"><%=ClienteHelper.getNombreCompleto(cliente)%></td>
                </tr>

                <%
                    }
                %>
            </table>
            <%
                }
            %>
            <input type="hidden" name="command" value="buscaClientePorRFC">
        </form>
    </center>

    <jsp:include page="footer.jsp" flush="true"/></body>
</html>