<%@page import="com.sicap.clientes.vo.ColoniaVO"%>
<%@page import="com.sicap.clientes.vo.ColoniaVO"%>
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
    String nombre = "";
    String dir = null;
    String representante = null;
    int idBanco = 0;
    String password = "";
    String command = "";
    String identificador = "";
    int idsucursal = 0;
    String calle = null;
    String numero = "";
    String telefono = "";
    int estatus = 0;
    int region = 0;
    int codigo = 0;

    int idSucursal = 0;
    UsuarioVO usuVO = (UsuarioVO) request.getAttribute("MODIFICACION_USUARIO");
    SucursalVO[] sucursal = (SucursalVO[]) request.getAttribute("SUCURSAL");


    ColoniaDAO colDAO = new ColoniaDAO();
    ColoniaVO coloniaVO = null;

    coloniaVO = colDAO.getColonia(sucursal[0].idColonia);

    DireccionVO direccion = new DireccionVO();

    if (sucursal == null) {
        sucursal = new SucursalVO[1];
    }
    nombre = sucursal[0].nombre;
    dir = sucursal[0].direccion_calle;
    representante = sucursal[0].representante;
    idBanco = sucursal[0].idBanco;
    idsucursal = sucursal[0].idSucursal;
    estatus = sucursal[0].estatus;
    region = sucursal[0].idRegion;
    codigo = sucursal[0].codigo;
    TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_BANCOS);
    TreeMap catRegion = CatalogoHelper.getCatalogo(ClientesConstants.CAT_REGIONES);
    Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
    identificador = "nuevo";
    calle = sucursal[0].direccion_calle;
    numero = sucursal[0].numero;
    telefono = sucursal[0].telefono;

    direccion.estado = sucursal[0].estado;
    direccion.municipio = sucursal[0].municipio;
    direccion.colonia = sucursal[0].colonia;
    direccion.cp = String.valueOf(sucursal[0].cp);


%>
<html>
    <head>
        <title> Alta/Modificaci&oacute;n Sucursales</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
        <script>
            function validaCamposSucursales(){
	
                window.document.forma.command.value = 'actualizaSucursal';
	
                if(window.document.forma.sucursal.value==0){
                    alert("Seleccione el nombre de sucursal");
                    return false;
                }
                
                if(window.document.forma.cp.value==''){
                    alert("Ingrese el Codigo Postal de la Sucursal");
                    window.document.forma.cp.focus();
                    return false;
                }
		
                if(window.document.forma.calle.value==''){
                    alert("Ingrese la Calle de la Sucursal");
                    window.document.forma.calle.focus();
                    return false;
                }
                if(window.document.forma.numero.value==''){
                    alert("Ingrese el Numero de la Sucursal");
                    window.document.forma.numero.focus();
                    return false;
                }
                if(window.document.forma.telefono.value==''){
                    alert("Ingrese el Telefono de la Sucursal");
                    window.document.forma.telefono.focus();
                    return false;
                }
                
                if('<%=identificador%>'=='nuevo'){
			
                    if(!window.document.forma.identificador[1].checked && !window.document.forma.identificador[0].checked){
                        alert("Ingrese el tipo de Sucursal");
                        window.document.forma.sucursal.focus();
                        return false;
                    }
                }
                if(window.document.forma.representante.value==''){
                    alert("Ingrese el representante de la Sucursal");
                    window.document.forma.representante.focus();
                    return false;
                }
                if(window.document.forma.banco.value==0){
                    alert("Ingrese el banco de operaciones de la Sucursal");
                    window.document.forma.banco.focus();
                    return false;
                }
            
                window.document.forma.submit();
            }
	
            function redireccionMenuAdmin(){
                window.document.forma.command.value = 'administracionSucursales';
                window.document.forma.submit();
            }
        
        </script>
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center>
        <h2>Modificaci&oacute;n de Sucursales</h2> 
    </center>

    <form method="post" name="forma" action="admin">
        <input type="hidden" name="command" value="">
        <input type="hidden" name="idColonia" value="<%=HTMLHelper.displayField(coloniaVO.idColonia)%>">
        <input type="hidden" name="idEstado" value="<%=HTMLHelper.displayField(coloniaVO.idEstado)%>">
        <input type="hidden" name="idMunicipio" value="<%=HTMLHelper.displayField(coloniaVO.idMunicipio)%>">
        <input type="hidden" name="asentamientoCP" value="<%=HTMLHelper.displayField(coloniaVO.cp)%>">

        <table border="0" cellspacing="5" align="center" >
            <tr>
                <td align="center" colspan="2">
                    <%=HTMLHelper.displayNotifications(notificaciones)%><br>
                </td>
            </tr>
            <tr>
                <th align="right">Nombre Sucursal:</th>
                <td align="left"><input type="text" name="sucursal" value = "<%=HTMLHelper.displayField(nombre)%>" <%if (command.equalsIgnoreCase("modificaUsuario") || usuVO != null) {%>readonly<%}%>> </td>
            </tr>
            <tr>
                <td width="100%" align="center" colspan="2"><a href="#" onClick="ayudaCodigoPostal()">Ayuda CP</a></td>
            </tr>
            <tr>
                <td width="50%" align="right">Estado</td>
                <td width="50%">  
                    <input type="text" name="estado" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.estado)%>" readonly="readonly" class="soloLectura">
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
                <td align="left"><input type="text" name="calle" value = "<%=HTMLHelper.displayField(calle)%>" <%if (command.equalsIgnoreCase("modificaUsuario") || usuVO != null) {%>readonly<%}%>></td>
            </tr>

            <tr>
                <th align="right">Número:</th>
                <td align="left"><input type="text" name="numero" value = "<%=HTMLHelper.displayField(numero)%>" <%if (command.equalsIgnoreCase("modificaUsuario") || usuVO != null) {%>readonly<%}%>></td>
            </tr>	
            <tr>
                <th align="right">Telefono:</th>
                <td align="left"><input type="text" name="telefono" size="20" maxlength="10"  value = "<%=HTMLHelper.displayField(telefono)%>" <%if (command.equalsIgnoreCase("modificaUsuario") || usuVO != null) {%>readonly<%}%>></td>
            </tr>
            <tr>
            <tr>
                <th align="right">Tipo Sucursal:</th>
                <td width="50%">  

                    <%= HTMLHelper.identificadorUsuario(identificador)%>

                </td>
            </tr>
            <tr>
                <th align="right">Representante Legal:</th>
                <td align="left"><input type="text" name="representante" value = "<%=HTMLHelper.displayField(representante)%>"></td>
            </tr>
            <tr>
                <th align="right">Banco Operaci&oacute;n:</th>
                <td width="50%">  
                    <select name="banco" size="1">
                        <%=HTMLHelper.displayCombo(catSucursales, idBanco)%>
                    </select>
                </td>
            </tr>
            <tr>
                <th align="right">Estatus:</th>
                <td width="50%">  
                    <%= HTMLHelper.estatusSucursal(estatus)%>
                </td>
            </tr>
            <tr>
                <th align="right">Codigo Contable</th>
                <td align="left"><input type="text" name="codigo" value = "<%=HTMLHelper.displayField(codigo)%>"></td>
            </tr>
            <tr>
                <th align="right">Region:</th>
                <td width="50%">  
                    <select name="region" size="1">
                        <%=HTMLHelper.displayCombo(catRegion, region)%>
                    </select>
                </td>
            </tr>
            <tr>
                <td align="left"><input type="hidden" name="idsucursal" value = "<%=HTMLHelper.displayField(idsucursal)%>"></td>
            </tr>
            <tr>
                <td align="center" colspan="2"><br>
                    <%if (!nombre.equals("") || nombre != null) {%>
                    <input  type="button" value="Modificar" onclick="validaCamposSucursales()"> 
                    <%    }
                    %>
                    <input type="button" value="Regresar" onClick="redireccionMenuAdmin()"><br><br></td>
            </tr>
        </table>
    </form>
    <jsp:include page="footer.jsp" flush="true"/>
</body>
</html>