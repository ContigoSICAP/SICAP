<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page language="java" import="com.sicap.clientes.vo.ClienteVO" %>
<%@ page language="java" import="java.util.TreeMap" %>
<%@ page language="java" import="com.sicap.clientes.vo.DireccionVO" %>
<%@ page language="java" import="com.sicap.clientes.vo.TelefonoVO" %>
<%@ page language="java" import="com.sicap.clientes.helpers.CatalogoHelper" %>
<%@ page language="java" import="com.sicap.clientes.helpers.ClienteHelper" %>
<%@ page language="java" import="com.sicap.clientes.helpers.HTMLHelper" %>
<%@ page language="java" import="com.sicap.clientes.util.ClientesConstants" %>
<%@ page language="java" import="com.sicap.clientes.util.Notification" %>
<%@ page language="java" import="com.sicap.clientes.vo.ConyugeVO" %>
<%
//String path = request.getContextPath();
//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    ClienteVO cliente = (ClienteVO) session.getAttribute("CLIENTE");
    TreeMap catTiposIdentificacion = CatalogoHelper.getCatalogo(ClientesConstants.CAT_TIPO_IDENTIFICACION);
    TreeMap catDepEconomicos = CatalogoHelper.getCatalogo(ClientesConstants.CAT_DEP_ECONOMICOS);
    TreeMap catGrupos = CatalogoHelper.getCatalogoGruposSucursal(cliente.idSucursal);
    TreeMap catEstadoCivil = CatalogoHelper.getCatalogo(ClientesConstants.CAT_ESTADO_CIVIL);
    TreeMap catNivelEstudios = CatalogoHelper.getCatalogo(ClientesConstants.CAT_NIVELES_ESTUDIO);
    TreeMap catFormaIngreso = CatalogoHelper.getCatalogo(ClientesConstants.CAT_FORMA_INGRESO);
    TreeMap catSector = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SECTORES);
    TreeMap catDependencia = CatalogoHelper.getCatalogo(ClientesConstants.CAT_DEPENDENCIAS);
    TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
    DireccionVO direccion = new DireccionVO();
    TelefonoVO telefonoPrincipal = new TelefonoVO();
    TelefonoVO telefonoRecados = new TelefonoVO();
    TelefonoVO telefonoCelular = new TelefonoVO();
    ConyugeVO conyuge = new ConyugeVO();
    if (cliente.direcciones != null) {
        direccion = cliente.direcciones[0];
        telefonoPrincipal = ClienteHelper.getTelefono(direccion.telefonos, ClientesConstants.TELEFONO_PRINCIPAL);
        telefonoRecados = ClienteHelper.getTelefono(direccion.telefonos, ClientesConstants.TELEFONO_RECADOS);
        telefonoCelular = ClienteHelper.getTelefono(direccion.telefonos, ClientesConstants.TELEFONO_CELULAR);
    }
    if (cliente.conyuge != null) {
        conyuge = cliente.conyuge;
    }
    Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <title>Mantenimiento de Clientes</title>

        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">    
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">
        <!--
        <link rel="stylesheet" type="text/css" href="styles.css">
        -->
        <script language="javascript" src="./js/functions.js"></script>
        <script>
	
            function regresarVentana(){
                document.getElementById("td").disabled = true;
                window.document.forma.command.value= 'consultaCliente';
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
	
            function guardaCambiosCliente(){
		document.getElementById("boton").disabled = true;	
                if(validarCamposCliente() && validarCamposDireccion() && validarCamposConyuge())
                {
                    window.document.forma.command.value= 'guardaCambiosCliente';
                    document.body.style.cursor = "wait";
                    window.document.forma.submit();
                }
            }
		
            function validarCamposCliente()
            {
                if(window.document.forma.nombre.value=='' || window.document.forma.aPaterno.value=='' || window.document.forma.aMaterno.value=='')
                {
                    alert('Debe ingresar el nombre completo del cliente');
                    return false;
                }
                if(window.document.forma.dependientesEconomicos.value=='0')
                {
                    alert('Debe seleccionar el número de dependientes económicos');
                    return false;
                }
                if(window.document.forma.tipoIdentificacion.value=='0')
                {
                    alert('Debe seleccionar el tipo de identificación');
                    return false;
                }
                if(window.document.forma.numeroIdentificacion.value=='')
                {
                    alert('Debe ingresar el número de identificación');
                    return false;
                }
                if(window.document.forma.estadoCivil.value=='0')
                {
                    alert('Debe seleccionar el estado civil');
                    return false;
                }
            <%if (request.isUserInRole("admin")) {%>
        if(window.document.forma.idSucursal.value=='0')
        {
            alert('Debe indicar la sucursal');
            return false;
        }
            <%}%>
        return true;
    }
		
    function validarCamposDireccion()
    {
        if(window.document.forma.cp.value=='')
        {
            alert('Debe ingresar el código postal');
            return false;
        }
        if(window.document.forma.estado.value=='')
        {
            alert('Debe seleccionar el estado');
            return false;
        }
        if(window.document.forma.municipio.value=='')
        {
            alert('Debe seleccionar el municipio');
            return false;
        }
        if(window.document.forma.colonia.value=='')
        {
            alert('Debe ingresar la colonia');
            return false;
        }
        if(window.document.forma.calle.value=='')
        {
            alert('Debe seleccionar la calle');
            return false;
        }
        if(window.document.forma.numeroExterior.value=='')
        {
            alert('Debe seleccionar el número exterior');
            return false;
        }
        if(window.document.forma.telefonoPrincipal.value=='')
        {
            alert('Debe seleccionar el teléfono del cliente');
            return false;
        }
        if(window.document.forma.localidad.value=='')
        {
            alert('Debe seleccionar la licalidad de la dirección');
            return false;
        }
        return true;
    }
		
    function validarCamposConyuge()
    {
        if(window.document.forma.nombreConyuge.value=='' && window.document.forma.aPaternoConyuge.value=='' && window.document.forma.aMaternoConyuge.value=='')
        {
            if(window.document.forma.direccionDomicilioConyuge.value!='' || window.document.forma.telefonoDomicilioConyuge.value!='')
            {
                alert('Falta ingresar el nombre del cónyuge');
                return false;
            }
            return true;
        }
        else
        {
            if(window.document.forma.nombreConyuge.value!='' && window.document.forma.aPaternoConyuge.value!='' && window.document.forma.aMaternoConyuge.value!='')
            {
                if(window.document.forma.direccionDomicilioConyuge.value=='')
                {
                    alert('Debe ingresar la dirección del domicilio del cónyuge');
                    return false;
                }
                if(window.document.forma.telefonoDomicilioConyuge.value=='')
                {
                    alert('Debe ingresar el teléfono del domicilio del cónyuge');
                    return false;					
                }
                return true;
            }
            else
            {
                alert('Falta ingresar el nombre completo del cónyuge');
                return false;
            }
        }
        return true;
    }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <center>
            <form name="forma" action="controller" method="post">
                <input type="hidden" name="command" value="">
                <input type="hidden" name="rfc" value="<%=cliente.rfc%>">
                <input type="hidden" name="idLocalidad" value="<%=direccion.idLocalidad%>">
                <input type="hidden" name="asentamientoCP" value="<%=HTMLHelper.displayField(direccion.asentamiento_cp)%>">
                <input type="hidden" name="idColonia" value="<%=HTMLHelper.displayField(direccion.idColonia)%>">
                <input type="hidden" name="idCliente" value="<%=cliente.idCliente%>">
                <table border="0" width="100%">
                    <tr>
                        <td align="center">
                            <h2>Mantenimiento de clientes</h2>
                            <%=HTMLHelper.displayNotifications(notificaciones)%>
                        </td>
                    </tr>
                </table>
                <table border="0" cellpadding="0" width="100%">
                    <tr>
                        <td align="center" colspan="3">
                            <br/>
                            <b>Informaci&oacute;n cliente</b>
                            <br/>
                        </td>
                    </tr>
                    <tr>
                        <td align="left" colspan="3">
                            <%if(request.isUserInRole("SOPORTE_OPERATIVO")){%>
                            Nombres <input type="text" name="nombre" size="35" maxlength="70" value="<%= HTMLHelper.displayField(cliente.nombre)%>"> Apellido paterno <input type="text" name="aPaterno" size="35" maxlength="70" value="<%= HTMLHelper.displayField(cliente.aPaterno)%>"> Apellido materno <input type="text" name="aMaterno" size="35" maxlength="70" value="<%= HTMLHelper.displayField(cliente.aMaterno)%>">
                            <%} else {%>
                            Nombres <input type="text" name="nombre" size="35" maxlength="70" value="<%= HTMLHelper.displayField(cliente.nombre)%>" disabled> Apellido paterno <input type="text" name="aPaterno" size="35" maxlength="70" value="<%= HTMLHelper.displayField(cliente.aPaterno)%>" disabled> Apellido materno <input type="text" name="aMaterno" size="35" maxlength="70" value="<%= HTMLHelper.displayField(cliente.aMaterno)%>" disabled>
                            <%}%>
                        </td>
                    </tr>
                    <tr>
                        <td align="left" colspan="3">
                            Grupo <select name="idGrupo" size="1"><option value="0">Seleccione...</option><%=HTMLHelper.displayCombo(catGrupos, cliente.idGrupo)%></select> Dependientes econ&oacute;micos<select name="dependientesEconomicos" size="1"> <%=HTMLHelper.displayCombo(catDepEconomicos, cliente.dependientesEconomicos)%></select> Nivel de estudios <select name="nivelEstudios" size="1"><%=HTMLHelper.displayCombo(catNivelEstudios, cliente.nivelEstudios)%></select>
                        </td>
                    </tr>
                    <tr>
                        <td align="left" colspan="3">
                            Tipo de identificaci&oacute;n <select name="tipoIdentificacion" size="1"><%=HTMLHelper.displayCombo(catTiposIdentificacion, cliente.tipoIdentificacion)%></select> N&uacute;mero de identificaci&oacute;n <input type="text" name="numeroIdentificacion" size="30" maxlength="30" value="<%=HTMLHelper.displayField(cliente.numeroIdentificacion)%>"> Estado civil <select name="estadoCivil" size="1"><%=HTMLHelper.displayCombo(catEstadoCivil, cliente.estadoCivil)%></select> Correo electr&oacute;nico <input type="text" name="correoElectronico" size="30" maxlength="50" value="<%=HTMLHelper.displayField(cliente.correoElectronico)%>">
                        </td>
                    </tr>
                    <%if (request.isUserInRole("admin")) {%>
                    <tr>
                        <td>
                            Sucursal <select name="idSucursal" size="1"><%=HTMLHelper.displayCombo(catSucursales, cliente.idSucursal)%></select>
                        </td>
                    </tr>
                    <%}%>
                    <tr>
                        <td align="center" colspan="1">
                            <br/>
                            <b>Direcci&oacute;n cliente</b>
                            <br/>
                        </td>
                    </tr>
                    <tr>
                        <td align="left" colspan="2">
                            C&oacute;digo postal <input type="text" name="cp" size="6" maxlength="6" value="<%=HTMLHelper.displayField(direccion.cp)%>"> <a href="#" onClick="ayudaCodigoPostal()"> Ayuda CP</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Estado <input type="text" name="estado" size="20" maxlength="100" value="<%=HTMLHelper.displayField(direccion.estado)%>"> Municipio <input type="text" name="municipio" size="30" maxlength="100" value="<%=HTMLHelper.displayField(direccion.municipio)%>"> Colonia <input type="text" name="colonia" size="30" maxlength="100" value="<%=HTMLHelper.displayField(direccion.colonia)%>">
                        </td>
                    </tr>
                    <tr>
                        <td align="left" colspan="2">
                            Localidad <input type="text" name="localidad" size="30" maxlength="80" value="<%=HTMLHelper.displayField(direccion.localidad)%>"> <a href="#" onClick="ayudaLocalidad()"> Ayuda Localidad</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Calle <input type="text" name="calle" size="30" maxlength="60" value="<%=HTMLHelper.displayField(direccion.calle)%>"> N&uacute;mero exterior <input type="text" name="numeroExterior" size="3" maxlength="10" value="<%=HTMLHelper.displayField(direccion.numeroExterior)%>"> N&uacute;mero interior <input type="text" name="numeroInterior" size="3" maxlength="10" value="<%=HTMLHelper.displayField(direccion.numeroInterior)%>">
                        </td>
                    </tr>
                    <tr>
                        <td align="left" colspan="2">
                            Tel&eacute;fono <input type="text" name="telefonoPrincipal" size="10" maxlength="10" value="<%=HTMLHelper.displayField(telefonoPrincipal.numeroTelefono)%>"> Tel&eacute;fono recados <input type="text" name="telefonoRecados" size="10" maxlength="10" value="<%=HTMLHelper.displayField(telefonoRecados.numeroTelefono)%>"> Tel&eacute;fono celular <input type="text" name="telefonoCelular" size="10" maxlength="10" value="<%=HTMLHelper.displayField(telefonoCelular.numeroTelefono)%>">
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1" align="center">
                            <br/>
                            <b>C&oacute;nyuge</b>
                            <br/>
                        </td>
                    </tr>
                    <tr>
                        <td align="left" colspan="3">
                            Nombre(s) <input type="text" name="nombreConyuge" size="35" maxlength="70" value="<%=HTMLHelper.displayField(conyuge.nombre)%>"> Apellido paterno <input type="text" name="aPaternoConyuge" size="35" maxlength="70" value="<%=HTMLHelper.displayField(conyuge.aPaterno)%>"> Apellido materno <input type="text" name="aMaternoConyuge" size="35" maxlength="70" value="<%=HTMLHelper.displayField(conyuge.aMaterno)%>">
                        </td>
                    </tr>
                    <tr>
                        <td align="left" colspan="3">
                            Direcci&oacute;n domicilio <input type="text" name="direccionDomicilioConyuge" size="45" maxlength="80" value="<%=HTMLHelper.displayField(conyuge.direccionDomicilio)%>"> Tel&eacute;fono domicilio <input type="text" name="telefonoDomicilioConyuge" size="15" maxlength="15" value="<%=HTMLHelper.displayField(conyuge.telefonoDomicilio)%>"> Tel&eacute;fono celular <input type="text" name="telefonoCelularConyuge" size="15" maxlength="15" value="<%=HTMLHelper.displayField(conyuge.telefonoCelular)%>">
                        </td>
                    </tr>
                    <tr>
                        <td align="left" colspan="3">
                            Direcci&oacute;n trabajo <input type="text" name="direccionTrabajoConyuge" size="45" maxlength="80" value="<%=HTMLHelper.displayField(conyuge.direccionTrabajo)%>"> Tel&eacute;fono trabajo <input type="text" name="telefonoTrabajoConyuge" size="15" maxlength="15" value="<%=HTMLHelper.displayField(conyuge.telefonoTrabajo)%>">
                        </td>
                    </tr>
                    <tr>
                        <td align="left" colspan="2">
                            Sueldo mensual <input type="text" name="sueldoconyuge" size="8" maxlength="8" value="<%=HTMLHelper.displayField(conyuge.sueldoMensual)%>"> Forma ingreso <select name="formaingresoConyuge" size="1"><%=HTMLHelper.displayCombo(catFormaIngreso, conyuge.formaIngreso)%></select> Sector <select name="tiposectorConyuge" size="1"><%=HTMLHelper.displayCombo(catSector, conyuge.tipoSector)%></select>
                        </td>
                    </tr>
                    <tr>
                        <td align="left" colspan="1">
                            Dependencia <select name="dependenciaConyuge" size="1"><%=HTMLHelper.displayCombo(catDependencia, conyuge.dependencia)%></select>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" align="center">
                            <br><input type="button" id="boton" value="Guardar" onClick="guardaCambiosCliente()">
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" align="center" id="td">
                            <br><a href="#" onClick="regresarVentana()">Regresar</a>
                        </td>
                    </tr>
                </table>
            </form>
        </center>
    </body>
</html>