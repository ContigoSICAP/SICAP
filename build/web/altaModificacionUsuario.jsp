<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.vo.UsuarioVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.dao.*"%>
<%@ page import="java.util.TreeMap"%>
<%
    String usuario = "";
    String password = "";
    String nombreCompleto = "";
    String command = "";
    String identificador = "";
    int idSucursal = 0;
    //perrito
    UsuarioVO usuVO = (UsuarioVO) request.getAttribute("MODIFICACION_USUARIO");
    TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
    Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
    String[] roles = new UsuarioDAO().getRoles();
    if (usuVO == null) {
        command = "altaUsuario";
        usuario = request.getParameter("username");
        password = request.getParameter("password");
        nombreCompleto = request.getParameter("nombreCompleto");
        identificador = request.getParameter("identificador");
        if (usuario == null) {
            usuario = "";
            password = "";
            nombreCompleto = "";
            identificador = "nuevo";
        }
    } else {
        command = "modificaUsuario";
        usuario = usuVO.nombre;
        idSucursal = usuVO.idSucursal;
        identificador = usuVO.identificador;
        nombreCompleto = usuVO.nombreCompleto;
        //password = usuVO.password;
    }
%>
<html>
    <head>
        <title> Alta/Modificaci&oacute;n usuario</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
        <script>
            function validaCamposUsuario(){
                document.getElementById("boton").disabled = true;
                if(window.document.forma.idSucursal.value==0){
                    alert("Seleccione la sucursal del usuario");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if ( window.document.forma.nombreCompleto.value=='' || !esTexto( "^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$",window.document.forma.nombreCompleto.value)){
		   alert('Debe introducir un nombre completo v\u00e1lido');
                   document.getElementById("boton").disabled = false;
		   return false;
		}
                if(trim(window.document.forma.username.value)==''){
                    alert("Ingrese el nombre de usuario");
                    window.document.forma.username.focus();
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if('<%=command%>' == 'altaUsuario'){
                    if('<%=identificador%>'=='nuevo')
                    if(!window.document.forma.identificador[0].checked && !window.document.forma.identificador[1].checked){
                        alert("Ingrese el tipo de identificador de usuario");
                        window.document.forma.username.focus();
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                    if(trim(window.document.forma.password.value)==''){
                        alert("Ingrese el password de usuario");
                        window.document.forma.password.focus();
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                    if(trim(window.document.forma.confirmPassword.value)==''){
                        alert("Confirme el password de usuario");
                        window.document.forma.confirmPassword.focus();
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                    if(trim(window.document.forma.password.value)!='' && trim(window.document.forma.confirmPassword.value) != trim(window.document.forma.password.value)){
                        alert("El password no coincide, favor de ingresarlo correctamente");
                        window.document.forma.confirmPassword.focus();
                        document.getElementById("boton").disabled = false;
                        return false;
                    }
                }
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
	
            function redireccionMenuAdmin(){
                window.document.forma.command.value = 'administracionUsuarios';
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
        </script>
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center>
        <h2>Alta / Modificaci&oacute;n de Usuarios</h2> 
    </center>
    <form method="post" name="forma" action="admin">
        <input type="hidden" name="command" value="<%=command%>">
        <table border="0" cellspacing="5" align="center" >
            <tr>
                <td align="center" colspan="2">
                    <%=HTMLHelper.displayNotifications(notificaciones)%><br>
                </td>
            </tr>
            <tr>
                <th align="right">Sucursal:</th>
                <td width="50%">  
                    <select name="idSucursal" size="1">
                        <%=HTMLHelper.displayCombo(catSucursales, idSucursal)%>
                    </select>
                </td>
            </tr>
            <tr>
                <th align="right">Nombre Completo:</th>
                <td align="left"><input type="text" name="nombreCompleto" value = "<%=nombreCompleto%>" size="45" ></td>
            </tr>
            <tr>
                <th align="right">Usuario:</th>
                <td align="left"><input type="text" name="username" value = "<%=usuario%>" <%if (command.equalsIgnoreCase("modificaUsuario") || usuVO != null) {%>readonly<%}%>></td>
            </tr>
            <tr>
                <th align="right">Password:</th>
                <td align="left"><input type="password" name="password" value = "<%=password%>"/></td>

            </tr>
            <tr>
                <th align="right">Confirmaci&oacute;n password:</th>
                <td align="left"><input type="password" name="confirmPassword" /></td>
            </tr>
            <tr>
                <th align="right">Identificador usuario:</th>
                <td align="left"><%=HTMLHelper.identificadorUsuario(identificador)%></td>
            </tr>
            <tr>
                <td align="center" colspan="2"><br><input type="button" id="boton" value="Aceptar" onclick="validaCamposUsuario()"> <input type="button" id="boton" value="Regresar" onClick="redireccionMenuAdmin()"><br><br></td>
            </tr>
        </table>
        <table border="1" cellspacing="0" align="center" width="30%">
            <tr>
                <td align="center" colspan="2"><h3><br>Roles</h3></td>
            </tr>
            <%for (int i = 0; i < roles.length; i++) {
            %>
            <tr>
                <th align="left" width="92%"><%=roles[i]%></th>
                <%if (usuVO != null) {%>
                <th align="center" width="8%"><input value="<%=roles[i]%>" type=checkbox name="<%=roles[i]%>" 
                                                     <%
                                                         for (int j = 0; usuVO.roles != null && j < usuVO.roles.length; j++) {
                                                             if (roles[i].equalsIgnoreCase(usuVO.roles[j])||roles[i].equalsIgnoreCase("AUXILIAR_CREDITO")) {
                                                     %>
                                                     
                                                     checked="yes"
                                                     <%                              }
                                                         }
                                                     %>
                                                     /></th>

                <%
                } else {
                %>
                <th align="center" width="8%"><input  value="<%=roles[i]%>" type=checkbox 
                                                      <%
                                                          if (roles[i].equalsIgnoreCase("AUXILIAR_CREDITO")) {

                                                      %>
                                                      checked="yes"
                                                      <%    }
                                                      %>

                                                      name="<%=roles[i]%>"/></th>
                    <%
                        }%>
            </tr>
            <%
                }
            %>
        </table>
    </form>

</body>
</html>