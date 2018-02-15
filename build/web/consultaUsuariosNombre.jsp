<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<html>
<head>
<title> Usuarios</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>

   function buscaUsuarios(){
		window.document.forma.command.value='buscaUsuarios';
		if ( window.document.forma.usuario.value==''){
			alert('Debe introducir un usuario');
			return false;
		}
		window.document.forma.submit();
	}
	
	function obtenUsuario(usuario){
	    window.document.forma.command.value='obtenUsuario';
	    window.document.forma.nombreUsuario.value=usuario;
		window.document.forma.submit();
	}
	
	function redireccionMenuAdmin(){
		window.document.forma.command.value = 'administracionUsuarios';
		window.document.forma.submit();
	}
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<%	
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
UsuarioVO[] usuario =  (UsuarioVO[])request.getAttribute("USUARIOS");
%>

<body leftmargin="0" topmargin="0" onload="javascript:window.document.forma.usuario.focus();">
<jsp:include page="header.jsp" flush="true"/>
<center>

<h2>Administraci&oacute;n de Usuarios</h2>
<h3>B&uacute;squeda de Usuarios</h3>

<form name="forma" action="admin" method="post">
<input type="hidden" name="command" value="buscaUsuarios">
<input type="hidden" name="nombreUsuario" value="">
<table border="0" width="100%" cellspacing="0">
	<tr>
  		<td width="100%" align="center" colspan="2"><%=HTMLHelper.displayNotifications(notificaciones)%><br></td>
	</tr>
	
	<tr>
	  	<td width="50%" align="right">Usuario</td>
	  	<td width="50%">  
		  	<input type="text" name="usuario" size="15" maxlength="50">
	  	</td>
	</tr>
<tr>
  <td colspan="2" align="center"><br><input type="button" value="Enviar" onClick="buscaUsuarios()"> <input type="button" value="Regresar" onClick="redireccionMenuAdmin()"></td>
</tr>	
</table>
</form>
</center>

<table border="1" width="70%" cellspacing="0" align="center" cellpadding="7">

<%if(usuario!=null && usuario.length!=0){ %>  <% 
          for(int i=0;i<usuario.length;i++){ %> 
         <td align="center">
         	<a href="#" onClick="obtenUsuario('<%= usuario[i].nombre%>')"><b><%= usuario[i].nombre%></b></a></td>
             <%if(((i+1)%3)==0){%>
             <tr>
             <%}
         }
      }%>  
</table>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>