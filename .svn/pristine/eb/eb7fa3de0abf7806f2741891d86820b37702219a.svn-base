<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.vo.UsuarioVO"%>
<%@ page import="com.sicap.clientes.vo.SucursalVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.dao.*"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="com.sicap.clientes.vo.DireccionVO"%>
<%
    String usuario = "";
    String password = "";
    String command = "";
    String identificador = "";
    int flag = 0;
    int idSucursal = 0;
    UsuarioVO usuVO = (UsuarioVO) request.getAttribute("MODIFICACION_USUARIO");
    SucursalVO sucursal = (SucursalVO) request.getAttribute("SUCURSAL");

    DireccionVO direccion = new DireccionVO();


    if (sucursal == null) {
        sucursal = new SucursalVO();
    }
    TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_BANCOS);
    TreeMap catRegion = CatalogoHelper.getCatalogo(ClientesConstants.CAT_REGIONES);
    Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
    String[] roles = new UsuarioDAO().getRoles();

    if (sucursal.idSucursal == 0) {
        identificador = request.getParameter("identificador");
        identificador = "nuevo";
        //flag = 1;


    } else {
        identificador = sucursal.identificador;
        direccion.estado = sucursal.estado;
        direccion.municipio = sucursal.municipio;
        direccion.colonia = sucursal.colonia;
        direccion.cp = String.valueOf(sucursal.cp);
    }
%>
<html>
    <head>
        <title> Alta/Modificaci&oacute;n Sucursales</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">

        <script>
            function validaCamposSucursales(){
                document.getElementById("boton").disabled = true;
	        window.document.forma.command.value = 'altaSucursal';
	
                if(window.document.forma.sucursal.value==0){
                    alert("Seleccione el Nombre de Sucursal");
                    document.getElementById("boton").disabled = false;
                    window.document.forma.sucursal.focus();
                    return false;
                }
                if(window.document.forma.cp.value==''){
                    alert("Ingrese el Codigo Postal de la Sucursal");
                    document.getElementById("boton").disabled = false;
                    window.document.forma.cp.focus();
                    return false;
                }
		
                if(window.document.forma.calle.value==''){
                    alert("Ingrese la Calle de la Sucursal");
                    document.getElementById("boton").disabled = false;
                    window.document.forma.calle.focus();
                    return false;
                }
                if(window.document.forma.numero.value=='' ){
                    alert("Ingrese el Numero de la Sucursal");
                    document.getElementById("boton").disabled = false;
                    window.document.forma.numero.focus();
                    return false;
                }
                if(isNaN(window.document.forma.numero.value) ){
                    alert("Ingrese solo numeros en el Numero de la Sucursal");
                    window.document.forma.numero.value='';
                    document.getElementById("boton").disabled = false;
                    window.document.forma.numero.focus();
                    return false;
                }      
                        
              
                if(window.document.forma.telefono.value==''){
                    alert("Ingrese  el Telefono de la Sucursal");
                    document.getElementById("boton").disabled = false;
                    window.document.forma.telefono.focus();
                    return false;
                }
                        
                if(isNaN(window.document.forma.telefono.value)){
                    alert("Ingrese solo numeros en el Telefono de la Sucursal");
                    document.getElementById("boton").disabled = false;
                    window.document.forma.telefono.focus();
                    return false;
                }
                        
                if(window.document.forma.representante.value==''){
                    alert("Ingrese el representante de la Sucursal");
                    document.getElementById("boton").disabled = false;
                    window.document.forma.representante.focus();
                    return false;
                }
                if(window.document.forma.banco.value==0){
                    alert("Ingrese el banco de operaciones de la Sucursal");
                    document.getElementById("boton").disabled = false;
                    window.document.forma.banco.focus();
                    return false;
                }  
                if('<%=identificador%>'=='nuevo')
                {
                    if(!window.document.forma.identificador[0].checked && !window.document.forma.identificador[1].checked)
                    {
                        alert("Ingrese el tipo de Sucursal");
                        document.getElementById("boton").disabled = false;
                        window.document.forma.sucursal.focus();
                        return false;
                    }
                }
			
		
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
	
            function redireccionMenuAdmin(){
                document.getElementById("boton").disabled = true;
                window.document.forma.command.value = 'administracionSucursales';
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
        </script>
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center>
        <h2>Alta / Modificaci&oacute;n de Sucursales</h2> 
    </center>
    <form method="post" name="forma" action="admin">
        <input type="hidden" name="command" value="">
        <input type="hidden" name="idColonia" value="<%=HTMLHelper.displayField(direccion.idColonia)%>">
        <input type="hidden" name="idEstado" value="<%=HTMLHelper.displayField(direccion.numestado)%>">
        <input type="hidden" name="idMunicipio" value="<%=HTMLHelper.displayField(direccion.numMunicipio)%>">
        <input type="hidden" name="asentamientoCP" value="<%=HTMLHelper.displayField(direccion.asentamiento_cp)%>">





        <table border="0" cellspacing="5" align="center" style="width: 658px; height: 377px;" width="658" height="377">
            <tr>
                <td align="center" colspan="2">
                    <%=HTMLHelper.displayNotifications(notificaciones)%><br>
                </td>
            </tr>
            <tr>
                <th align="right">Nombre Sucursal:</th>
                <td align="left"><input type="text" name="sucursal" value = "<%=HTMLHelper.displayField(sucursal.nombre)%>" <%if (command.equalsIgnoreCase("modificaUsuario") || usuVO != null) {%>readonly<%}%>></td>
            </tr>
            <tr>
                <td width="100%" align="center" colspan="2" id="td"><a href="#" onClick="ayudaCodigoPostal()">Ayuda CP</a></td>
            </tr>
            <tr>
                <td width="50%" align="right">Estado</td>
                <td width="50%">  
                    <%if (flag == 1) {%>
                    <input type="text" name="estado" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.estado)%>" readonly="readonly" class="soloLectura">
                    <%} else {%>
                    <input type="text" name="estado" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.estado)%>" readonly="readonly" class="soloLectura">
                    <%}%>
                </td>
            </tr>
            <tr>
                <td width="50%" align="right">Municipio</td>
                <td width="50%">  
                    <input type="text" name="municipio" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.municipio)%>" readonly="readonly" class="soloLectura">
                </td>
            </tr>
            <tr>
                <td width="50%" align="right">Colonia</td>
                <td width="50%">  
                    <input type="text" name="colonia" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.colonia)%>" readonly="readonly" class="soloLectura">
                </td>
            </tr>
            <tr>
                <td width="50%" align="right">C&oacute;digo postal</td>
                <td width="50%">  
                    <input type="text" name="cp" size="6" maxlength="6" value="<%=HTMLHelper.displayField(direccion.cp)%>" readonly="readonly" class="soloLectura">
                </td>
            </tr>
            <tr>
                <th align="right">Calle:</th>
                <td align="left"><input type="text" name="calle" value = "<%=HTMLHelper.displayField(sucursal.direccion_calle)%>" <%if (command.equalsIgnoreCase("modificaUsuario") || usuVO != null) {%>readonly<%}%>></td>
            </tr>

            <tr>
                <th align="right">Número:</th>
                <td align="left"><font face="Arial Black"><input type="text" name="numero" value="<%=HTMLHelper.displayField(sucursal.numero)%>" <%if (command.equalsIgnoreCase("modificaUsuario") || usuVO != null) {%> readonly <%}%>></font></td>
            </tr>	
            <tr>
                <th align="right">Telefono:</th>
                <td align="left"><input type="text"  name="telefono" size="20" maxlength="10" value = "<%=HTMLHelper.displayField(sucursal.telefono)%>" <%if (command.equalsIgnoreCase("modificaUsuario") || usuVO != null) {%>readonly<%}%>></td>
            </tr>
            <tr>
                <th align="right">Tipo Sucursal:</th>
                <td width="50%">  
                    <%= HTMLHelper.identificadorUsuario(identificador)%>
                </td>
            </tr>
            <tr>
                <th align="right">Representante Legal:</th>
                <td align="left"><input type="text" name="representante" value = "<%=HTMLHelper.displayField(sucursal.representante)%>"></td>
            </tr>
            <tr>
                <th align="right">Banco Operaci&oacute;n:</th>
                <td width="50%">  
                    <select name="banco" size="1">
                        <%=HTMLHelper.displayCombo(catSucursales, sucursal.idBanco)%>
                    </select>
                </td>
            </tr>
            <tr>
                <th align="right">Codigo Contable</th>
                <td align="left"><input type="text" name="codigo" value = "<%=HTMLHelper.displayField(sucursal.codigo)%>"></td>
            </tr>
            <tr>
                <th align="right">Region:</th>
                <td width="50%">  
                    <select name="region" size="1">
                        <%=HTMLHelper.displayCombo(catRegion, sucursal.idRegion)%>
                    </select>
                </td>
            </tr>
            <tr>
                <td align="center" colspan="2"><br><input type="button" id="boton" value="Aceptar" onclick="validaCamposSucursales()"> <input type="button" id="boton" value="Regresar" onClick="redireccionMenuAdmin()"><br><br></td>
            </tr>


        </table>

    </form>
    <jsp:include page="footer.jsp" flush="true"/>
</body>
</html>