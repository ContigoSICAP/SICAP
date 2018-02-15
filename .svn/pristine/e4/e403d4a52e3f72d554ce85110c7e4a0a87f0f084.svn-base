<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="java.util.*"%>
<%
    UsuarioVO usuario = (UsuarioVO) session.getAttribute("USUARIO");
%>
<html>
    <head>
        <title>Clientes</title>

        <script>

            function submitenter(myfield,e){
                var keycode;
                if (window.event) keycode = window.event.keyCode;
                else if (e) keycode = e.which;
                else return true;

                if (keycode == 13){
                    registrarCliente();
                }else
                    return true;
            }

            function registrarCliente(externo){
                document.getElementById("boton").disabled = true;
                window.document.forma.command.value='registraCliente';
                if ( window.document.forma.nombre.value=='' || !esTexto( "^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$",window.document.forma.nombre.value)){
                    alert("Ingrese el nombre");
                    window.document.forma.nombre.focus();
                    return false;
                }
		
                if ( window.document.forma.aPaterno.value=='' || !esTexto( "^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$",window.document.forma.aPaterno.value) ){
                    alert("Ingrese un apellido Paterno v·lido");
                    window.document.forma.aPaterno.focus();
                    return false;
                }

                if ( window.document.forma.aMaterno.value=='' || !esTexto( "^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$",window.document.forma.aMaterno.value)){
                    alert("Ingrese un apellido Materno v·lido");
                    window.document.forma.aMaterno.focus();
                    return false;
                }
                var aPaterno = trim(window.document.forma.aPaterno.value.toUpperCase());
                var aMaterno = trim(window.document.forma.aMaterno.value.toUpperCase());
                //if(aPaterno=='NO PROPORCIONADO' && aMaterno=='NO PROPORCIONADO')
                if(aPaterno=='X' && aMaterno=='X')
                {
                    alert("Proporcione almenos un apellido");
                    window.document.forma.aPaterno.focus();
                    return false;
                }
		
                if ( window.document.forma.fechaNacimiento.value!='' ){
                    if ( !esFechaValida(window.document.forma.fechaNacimiento,false) ){
                        alert("La Fecha de nacimiento es inv·lida");
                        return false;
                    }else if ( !esEdadValida(window.document.forma.fechaNacimiento.value, window.document.forma.operacion.value) ){
                        alert("La edad del cliente est· fuera del rango permitido");
                        return false;
                    }
                }else{
                    alert("Debe capturar la Fecha de nacimiento");
                    return false;
                }
			
                if ( window.document.forma.entidadNacimiento.value==0 ){
                    alert('Debe seleccionar la entidad de nacimiento');
                    return false;
                }	
			
                if ( window.document.forma.sucursal.value==0 ){
                    alert('Debe seleccionar una sucursal');
                    return false;
                }
                if ( window.document.forma.operacion.value==0 ){
                    alert('Debe seleccionar un tipo de operaciÛn');
                    return false;
                }else if ( window.document.forma.operacion.value=='<%=ClientesConstants.CONSUMO%>' || window.document.forma.operacion.value=='<%=ClientesConstants.VIVIENDA%>' || window.document.forma.operacion.value=='<%=ClientesConstants.CREDIHOGAR%>'  && !externo ) {
                    window.document.forma.command.value='registraClienteConsumo';
                }else if ( window.document.forma.operacion.value=='<%=ClientesConstants.SELL_FINANCE%>'){
                    window.document.forma.command.value='registraClienteDescuento';
                }
                
                
                /*if(aPaterno=='NO PROPORCIONADO' && esTexto( "^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$",window.document.forma.aMaterno.value))
                {
                    window.document.forma.aPaterno.value = window.document.forma.aMaterno.value.toUpperCase();
                    window.document.forma.aMaterno.value = 'NO PROPORCIONADO';
                    //alert("hice el cambio\n aPaterno:"+window.document.forma.aPaterno.value+"\n aMaterno:"+window.document.forma.aMaterno.value);
                        
                }*/
                    
                document.body.style.cursor = "wait";
                window.document.forma.submit();	
            }

            function seleccionar(){
                window.opener.document.forma.rfc.value = window.document.formarfc.rfc.value;
                window.close();
            }
	
            function consultaCliente(idCliente){
                document.getElementById("td").disabled = true;
                window.document.forma.command.value='consultaCliente';
                window.document.forma.idCliente.value=idCliente;
                window.document.forma.idSolicitud.value=1;
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

        if ((ClienteVO) request.getAttribute("RFC_ENCONTRADO") != null) {
            existeRFC = (ClienteVO) request.getAttribute("RFC_ENCONTRADO");
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
            <input type="hidden" name="idSolicitud" value="">

            <table border="0" cellspacing="0" width="100%">


                <tr>
                    <td width="50%" align="right">Nombres</td>
                    <td width="50%">  
                        <input type="text" name="nombre" size="45" maxlength="70" value="<%=HTMLHelper.displayField(nombre)%>" onKeyPress="return submitenter(this,event)">
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Apellido paterno</td>
                    <td width="50%">  
                        <input type="text" name="aPaterno" size="45" maxlength="70" value="<%=HTMLHelper.displayField(aPaterno)%>" onKeyPress="return submitenter(this,event)">
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Apellido materno</td>
                    <td width="50%">  
                        <input type="text" name="aMaterno" size="45" maxlength="70" value="<%=HTMLHelper.displayField(aMaterno)%>" onKeyPress="return submitenter(this,event)">
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Fecha de nacimiento</td>
                    <td width="50%">  
                        <input type="text" id= "fechaNacimiento" name="fechaNacimiento" size="10" maxlength="10" value="<%=HTMLHelper.displayField(request.getParameter("fechaNacimiento"))%>" onKeyPress="return submitenter(this,event)">(dd/mm/aaaa)
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Entidad de nacimiento</td>
                    <td width="50%">  
                        <select name="entidadNacimiento" size="1" onKeyPress="return submitenter(this,event)">
                            <%=HTMLHelper.displayCombo(catEstados, entidadNacimiento)%>
                        </select>
                    </td>
                </tr>

                <tr>
                    <td width="50%" align="right">Sucursal</td>
                    <td width="50%">  
                        <select name="sucursal" size="1" onKeyPress="return submitenter(this,event)">
                            <%=HTMLHelper.displayCombo(catSucursales, 0)%>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Tipo de operaci&oacute;n</td>
                    <td width="50%">  
                        <select name="operacion" size="1" onKeyPress="return submitenter(this,event)">
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
                                <td width="70%" align="center" class="whitetext">Nombre</td>
                            </tr>

                            <tr>			
                                <td width="10%"><%=existeRFC.idCliente%></td>
                                <td width="20%" id="td"><href="#" onClick="consultaCliente(<%=existeRFC.idCliente%>)"><%=HTMLHelper.displayField(existeRFC.rfc)%></td>
                                <td width="70%"><%=ClienteHelper.getNombreCompleto(existeRFC)%></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table> 
        </form>
    </center>
    <jsp:include page="footer.jsp" flush="true"/>
</body>
</html>