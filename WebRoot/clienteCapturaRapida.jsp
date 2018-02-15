<%@page import="com.sicap.clientes.dao.SucursalDAO"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.sicap.clientes.helpers.*"%>
<%@page import="com.sicap.clientes.util.*"%>
<%@page import="com.sicap.clientes.vo.*"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%
UsuarioVO usuario = (UsuarioVO) session.getAttribute("USUARIO");
ClienteVO cliente = (ClienteVO) session.getAttribute("CLIENTE");
ClienteVO existeRFC = new ClienteVO();
DireccionVO direccion = new DireccionVO();
SolicitudVO solicitud = new SolicitudVO();
String nombreSucursal = "";
Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
TreeMap catOperaciones = CatalogoHelper.getCatalogoOperaciones(usuario, false);
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
        if ((ClienteVO) request.getAttribute("RFC_ENCONTRADO") != null) {
            existeRFC = (ClienteVO) request.getAttribute("RFC_ENCONTRADO");
            SucursalDAO sucursalDAO = new SucursalDAO();
            nombreSucursal = sucursalDAO.getSucursalNombre(existeRFC.idSucursal);
        }
if(idSolicitud == 0){
    idSolicitud = Integer.parseInt(String.valueOf(request.getAttribute("idSolicitud")));
}
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
solicitud = cliente.solicitudes[indiceSolicitud];
if (cliente.direcciones != null) {
    direccion = cliente.direcciones[0];
}
String habilitado = "readonly";
if (request.isUserInRole("SOPORTE_OPERATIVO")) {
    habilitado = "";
}
%>
<html>
    <head>
        <title>Clientes Captura R&aacute;pida</title>
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
                    registrarCliente();
                } else
                    return true;
            }

            function registrarCliente() {
                
                document.getElementById("boton").disabled = true;
                window.document.forma.command.value = 'actualizaClienteCR';
                if (window.document.forma.nombre.value == '' || !esTexto("^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$", window.document.forma.nombre.value)) {
                    alert("Ingrese el nombre");
                    window.document.forma.nombre.focus();
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.aPaterno.value == '' || !esTexto("^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$", window.document.forma.aPaterno.value)) {
                    alert("Ingrese un apellido Paterno v·lido");
                    document.getElementById("boton").disabled = false;
                    window.document.forma.aPaterno.focus();
                    return false;
                }
                if (window.document.forma.aMaterno.value == '' || !esTexto("^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$", window.document.forma.aMaterno.value)) {
                    alert("Ingrese un apellido Materno v·lido");
                    document.getElementById("boton").disabled = false;
                    window.document.forma.aMaterno.focus();
                    return false;
                }
                var aPaterno = trim(window.document.forma.aPaterno.value.toUpperCase());
                var aMaterno = trim(window.document.forma.aMaterno.value.toUpperCase());
                if (aPaterno == 'X' && aMaterno == 'X'){
                    alert("Proporcione almenos un apellido");
                    document.getElementById("boton").disabled = false;
                    window.document.forma.aPaterno.focus();                    
                    return false;
                }
                var nombreSes = "<%=cliente.nombre%>";
                var paternoSes = "<%=cliente.aPaterno%>";
                var maternoSes = "<%=cliente.aMaterno%>";
                var fechaNacSes = "<%=Convertidor.dateToString(cliente.fechaNacimiento)%>";
                if (window.document.forma.nombre.value != nombreSes || window.document.forma.aPaterno.value != paternoSes || window.document.forma.aMaterno.value != maternoSes || window.document.forma.fechaNacimiento.value != fechaNacSes){
                    if (<%=request.isUserInRole("ANALISIS_CREDITO")%>) {
                        if (!confirm("Desea cambiar los datos de nombre del cliente")) {
                            document.getElementById("boton").disabled = false;
                            return false;
                        } else {
                            window.document.forma.autorizacionRFC.value = 1;
                        }
                    } else {
                        alert("No puede realizar cambios de datos en nombre del cliente");
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                }
                if (window.document.forma.fechaNacimiento.value != '') {
                    if (!esFechaValida(window.document.forma.fechaNacimiento, false)) {
                        alert("La Fecha de nacimiento es inv·lida");
                        document.getElementById("boton").disabled = false;
                        return false;
                    } else if (!esEdadValida(window.document.forma.fechaNacimiento.value, window.document.forma.operacion.value)) {
                        alert("La edad del cliente est· fuera del rango permitido");
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                } else {
                    alert("Debe capturar la Fecha de nacimiento");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.cp.value == '') {
                    alert("Debe capturar el Codigo Postal");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.localidad.value == '') {
                    alert("Debe capturar la Localidad");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.calle.value == '') {
                    alert('Debe introducir una Calle');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.numeroExterior.value == '' || !esEntero(window.document.forma.numeroExterior.value)) {
                    alert('Debe introducir un Numero exterior de Domicilio');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.sucursal.value == 0) {
                    alert('Debe seleccionar una sucursal');
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (window.document.forma.operacion.value == 0) {
                    alert('Debe seleccionar un tipo de operaciÛn');
                    document.getElementById("boton").disabled = false;
                    return false;
                } else if (window.document.forma.operacion.value == '<%=ClientesConstants.CONSUMO%>' || window.document.forma.operacion.value == '<%=ClientesConstants.VIVIENDA%>' || window.document.forma.operacion.value == '<%=ClientesConstants.CREDIHOGAR%>' && !externo) {
                    window.document.forma.command.value = 'registraClienteConsumo';
                } else if (window.document.forma.operacion.value == '<%=ClientesConstants.SELL_FINANCE%>') {
                    window.document.forma.command.value = 'registraClienteDescuento';
                }
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }

            function seleccionar() {
                window.opener.document.forma.rfc.value = window.document.formarfc.rfc.value;
                window.close();
            }

            function consultaCliente(idCliente) {
                document.getElementById("td").disabled = true;
                window.document.forma.command.value = 'consultaCliente';
                window.document.forma.idCliente.value = idCliente;
                window.document.forma.idSolicitud.value = 1;
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }


        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>


    <body leftmargin="0" topmargin="0" onload="javascript:window.document.forma.nombre.focus();">
        <jsp:include page="header.jsp" flush="true"/>
        <jsp:include page="menuIzquierdoRapido.jsp" flush="true"/>
    <center>
        <h2>Administraci&oacute;n Clientes Captura R&aacute;pida</h2> 
        <h3>Clientes</h3>
        <form name="forma" action="controller" method="post">
            <input type="hidden" name="command" value="generaRFC">
            <input type="hidden" name="idLocalidad" value="<%=direccion.idLocalidad%>">
            <input type="hidden" name="idSolicitud" value="<%=solicitud.idSolicitud%>">
            <input type="hidden" name="idColonia" value="<%=HTMLHelper.displayField(direccion.idColonia)%>">
            <input type="hidden" name="asentamientoCP" value="<%=HTMLHelper.displayField(direccion.asentamiento_cp)%>">
            <input type="hidden" name="autorizacionRFC" value="0">
            <table border="0" cellspacing="0" width="100%">
                <tr>
                    <td width="50%" align="right">N&uacute;mero de cliente</td>
                    <td width="50%">  
                        <input type="text" name="idCliente" size="10" value="<%=cliente.idCliente%>" readonly>
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">RFC</td>
                    <td width="50%">  
                        <input type="text" name="rfc" size="13" maxlength="13" value="<%=cliente.rfc%>" readonly>
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Nombres</td>
                    <td width="50%">  
                        <input type="text" name="nombre" size="45" maxlength="70" value="<%=HTMLHelper.displayField(cliente.nombre)%>" <%=habilitado%>>
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Apellido paterno</td>
                    <td width="50%">  
                        <input type="text" name="aPaterno" size="45" maxlength="70" value="<%=HTMLHelper.displayField(cliente.aPaterno)%>" <%=habilitado%>>
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Apellido materno</td>
                    <td width="50%">  
                        <input type="text" name="aMaterno" size="45" maxlength="70" value="<%=HTMLHelper.displayField(cliente.aMaterno)%>" <%=habilitado%>>
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Fecha de nacimiento</td>
                    <td width="50%">
                        <% if (habilitado.equals("")){%>
                        <input type="text" id= "fechaNacimiento" name="fechaNacimiento" size="10" maxlength="10" value="<%=HTMLHelper.displayField(cliente.fechaNacimiento)%>" <%=habilitado%>>(dd/mm/aaaa)
                        <% }else {%>
                        <input type="text" id= "fechaNacimiento2" name="fechaNacimiento2" size="10" maxlength="10" value="<%=HTMLHelper.displayField(cliente.fechaNacimiento)%>" disabled>(dd/mm/aaaa)
                        <input type="hidden" id="fechaNacimiento" name="fechaNacimiento" value="<%=HTMLHelper.displayField(cliente.fechaNacimiento)%>">
                        <% } %>
                    </td>
                </tr>
                <tr>
                    <td width="100%" align="center" colspan="2"><a href="#" onClick="ayudaCodigoPostal()">Ayuda CP</a></td>
                </tr>
                <tr>
                    <td width="50%" align="right">Estado</td>
                    <td width="50%">  
                        <input type="text" name="estado" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.estado)%>" readonly>
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Municipio</td>
                    <td width="50%">  
                        <input type="text" name="municipio" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.municipio)%>" readonly>
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Colonia</td>
                    <td width="50%">  
                        <input type="text" name="colonia" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.colonia)%>" readonly>
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">C&oacute;digo postal</td>
                    <td width="50%">  
                        <input type="text" name="cp" size="6" maxlength="6" value="<%=HTMLHelper.displayField(direccion.cp)%>" readonly>
                    </td>
                </tr>
                <tr>
                    <td width="100%" align="center" colspan="2"><a href="#" onClick="ayudaLocalidad()">Ayuda Localidad</a></td>
                </tr>

                <tr>
                    <td width="50%" align="right">Localidad</td>
                    <td width="50%">
                        <input type="text" name="localidad" size="40" maxlength="80" value="<%=HTMLHelper.displayField(direccion.localidad)%>" readonly>
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Calle</td>
                    <td width="50%">  
                        <input type="text" name="calle" size="30" maxlength="60" value="<%=HTMLHelper.displayField(direccion.calle)%>">
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">N&uacute;mero exterior</td>
                    <td width="50%">  
                        <input type="text" name="numeroExterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(direccion.numeroExterior)%>">
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">N&uacute;mero interior</td>
                    <td width="50%">  
                        <input type="text" name="numeroInterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(direccion.numeroInterior)%>">
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Sucursal</td>
                    <td width="50%">  
                        <select disabled name="sucursal" size="1" onKeyPress="return submitenter(this, event)">
                            <%=HTMLHelper.displayCombo(catSucursales, cliente.idSucursal)%>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Tipo de operaci&oacute;n</td>
                    <td width="50%">  
                        <select disabled name="operacion" size="1" onKeyPress="return submitenter(this, event)">
                            <%=HTMLHelper.displayCombo(catOperaciones, solicitud.tipoOperacion)%>
                        </select>
                    </td>
                </tr>
                <%if(request.isUserInRole("ANALISIS_CREDITO") || solicitud.estatus == ClientesConstants.SOLICITUD_NUEVA || solicitud.estatus == ClientesConstants.SOLICITUD_PENDIENTE){%>
                <tr>
                    <td align="center" colspan="2">
                        <br><input type="button" id="boton" value="Actualiza Cliente" onClick="registrarCliente()">
                    </td>
                </tr>
                <%} else {%>
                <tr>
                    <td align="center" colspan="2">
                        <br><input disabled type="button" id="boton" value="Actualiza Cliente" onClick="">
                    </td>
                </tr>
                <%}%>
                <tr>
                    <td align="center" colspan="2">
                        <br><%=HTMLHelper.displayNotifications(notificaciones)%>
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="2">
                        <% 	if (existeRFC.rfc != null) {%>

                        <table border="0" width="50%" cellspacing="3" cellpadding="0" >
                            <tr bgcolor="#009865">
                                <td width="10%" align="center" class="whitetext">N&uacute;mero</td>
                                <td width="20%" align="center" class="whitetext">RFC</td>
                                <td width="50%" align="center" class="whitetext">Nombre</td>
                                <td width="20%" align="center" class="whitetext">Sucursal</td>
                            </tr>

                            <tr>			
                                <td width="10%"><%=existeRFC.idCliente%></td>
                                <td width="20%" id="td"><a href="#" onClick="consultaCliente(<%=existeRFC.idCliente%>)"><%=HTMLHelper.displayField(existeRFC.rfc)%></a></td>
                                <td width="50%"><%=ClienteHelper.getNombreCompleto(existeRFC)%></td>
                                <td width="20"><%=HTMLHelper.displayField(nombreSucursal)%></td>
                            </tr>
                        </table>
                        <% }%>
                    </td>
                </tr>
            </table> 
        </form>
    </center>
    <jsp:include page="footer.jsp" flush="true"/>
</body>
</html>