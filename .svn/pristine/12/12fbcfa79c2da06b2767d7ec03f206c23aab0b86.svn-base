<%@page import="com.sicap.clientes.dao.SucursalDAO"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="java.util.*"%>
<%
    UsuarioVO usuario = (UsuarioVO) session.getAttribute("USUARIO");
    boolean SucursalesIxaya = CatalogoHelper.esSucursaldeIxaya(usuario.idSucursal);
%>
<html>
    <head>
        <title>Clientes</title>

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

            function registrarCliente(externo) {
                document.getElementById("boton").disabled = true;
                <%if (SucursalesIxaya) {%>
                    alert('El movimiento se debe de realizar en el sistema JUCAVI');
                    return false;
                <%}%>
                window.document.forma.command.value = 'registraClienteCR';
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
                //if(aPaterno=='NO PROPORCIONADO' && aMaterno=='NO PROPORCIONADO')
                if (aPaterno == 'X' && aMaterno == 'X')
                {
                    alert("Proporcione almenos un apellido");
                    document.getElementById("boton").disabled = false;
                    window.document.forma.aPaterno.focus();                    
                    return false;
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
    <%
        Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");

        TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
        TreeMap catOperaciones = CatalogoHelper.getCatalogoOperaciones(usuario, false);
        ClienteVO cliente = new ClienteVO();
        ClienteVO existeRFC = new ClienteVO();
        TreeMap catEstados = CatalogoHelper.getCatalogo(ClientesConstants.CAT_ESTADOS);
        DireccionVO direccion = new DireccionVO();
        boolean mismaSucursal = false;
        String nombreSucursal = "";

        if ((ClienteVO) request.getAttribute("RFC_ENCONTRADO") != null) {
            existeRFC = (ClienteVO) request.getAttribute("RFC_ENCONTRADO");
            SucursalDAO sucursalDAO = new SucursalDAO();
            nombreSucursal = sucursalDAO.getSucursalNombre(existeRFC.idSucursal);
            Set set = catSucursales.keySet();
            Iterator llaves = set.iterator();
            Object key = null;
            while (llaves.hasNext()) {
                key = llaves.next();
                if (existeRFC.idSucursal == (Integer)key){
                    mismaSucursal = true;
                }
            }
        }

        int operacion = 3;
        int entidadNacimiento = 0;
        if (request.getParameter("operacion") != null) {
            operacion = Integer.parseInt(request.getParameter("operacion"));
        }
        if (request.getParameter("entidadNacimiento") != null) {
            entidadNacimiento = Integer.parseInt(request.getParameter("entidadNacimiento"));
        }

        if ((ClienteVO) request.getAttribute("ayudarfc") != null) {
            cliente = (ClienteVO) request.getAttribute("ayudarfc");
            session.setAttribute("ayudarfc", cliente);
        }
        boolean esExterno = false;
        if (usuario.identificador.equals("E")) {
            esExterno = true;
        }

        String nombre = request.getParameter("nombre");
        String aPaterno = request.getParameter("aPaterno");
        String aMaterno = request.getParameter("aMaterno");
        if (nombre == null && aPaterno == null && aMaterno == null) {
            nombre = request.getParameter("nombreS");
            aPaterno = request.getParameter("apellidoPaterno");
            aMaterno = request.getParameter("apellidoMaterno");
        }
    %>

    <body leftmargin="0" topmargin="0" onload="javascript:window.document.forma.nombre.focus();">
        <jsp:include page="header.jsp" flush="true"/>
    <center>
        <h2>Administraci&oacute;n de Clientes</h2> 
        <h3>Alta de Clientes</h3>

        <form name="forma" action="controller" method="post">
            <input type="hidden" name="command" value="generaRFC">
            <input type="hidden" name="idCliente" value="">
            <input type="hidden" name="idSolicitud" value="1">
            <input type="hidden" name="idLocalidad" value="<%=direccion.idLocalidad%>">
            <input type="hidden" name="idColonia" value="<%=HTMLHelper.displayField(direccion.idColonia)%>">
            <input type="hidden" name="asentamientoCP" value="<%=HTMLHelper.displayField(direccion.asentamiento_cp)%>">

            <table border="0" cellspacing="0" width="100%">


                <tr>
                    <td width="50%" align="right">Nombres</td>
                    <td width="50%">  
                        <input type="text" name="nombre" size="45" maxlength="70" value="<%=HTMLHelper.displayField(nombre)%>" onKeyPress="return submitenter(this, event)">
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Apellido paterno</td>
                    <td width="50%">  
                        <input type="text" name="aPaterno" size="45" maxlength="70" value="<%=HTMLHelper.displayField(aPaterno)%>" onKeyPress="return submitenter(this, event)">
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Apellido materno</td>
                    <td width="50%">  
                        <input type="text" name="aMaterno" size="45" maxlength="70" value="<%=HTMLHelper.displayField(aMaterno)%>" onKeyPress="return submitenter(this, event)">
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Fecha de nacimiento</td>
                    <td width="50%">  
                        <input type="text" id= "fechaNacimiento" name="fechaNacimiento" size="10" maxlength="10" value="<%=HTMLHelper.displayField(request.getParameter("fechaNacimiento"))%>" onKeyPress="return submitenter(this, event)">(dd/mm/aaaa)
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
                        <select name="sucursal" size="1" onKeyPress="return submitenter(this, event)">
                            <%=HTMLHelper.displayCombo(catSucursales, 0)%>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Tipo de operaci&oacute;n</td>
                    <td width="50%">  
                        <select name="operacion" size="1" onKeyPress="return submitenter(this, event)">
                            <%=HTMLHelper.displayCombo(catOperaciones, operacion)%>
                        </select>
                    </td>
                </tr>

                <tr>
                    <td align="center" colspan="2">
                        <br><input type="button" id="boton" value="Registrar Cliente" onClick="registrarCliente(<%=esExterno%>)">
                    </td>
                </tr>

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
                               <!--Si el cliente tiene la misma sucursal que el usuario mostrar la sigueinte cadena, si no solo el mensaje de la sucursal en la que se encuentra -->
                               <% if (mismaSucursal){ %>
                                <td width="20%" id="td"><a href="#" onClick="consultaCliente(<%=existeRFC.idCliente%>)"><%=HTMLHelper.displayField(existeRFC.rfc)%></a></td>
                               <% } else { %>
                                <td width="20%" id="td"><%=HTMLHelper.displayField(existeRFC.rfc)%></td>
                               <% } %>
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