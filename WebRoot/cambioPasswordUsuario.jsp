<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.UsuarioVO" %>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<html>
<head>
<title>Actaliza Usuario</title>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0" onload="javascript:window.document.forma.user_pass.focus();">
<jsp:include page="header.jsp" flush="true"/>
<%
UsuarioVO user = (UsuarioVO)session.getAttribute("USUARIO");
if ( user==null ){
	user = (UsuarioVO)session.getAttribute("USUARIO_CAMBIO_PWD");
}
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
%>

<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="actualizaUsuario">

  <table border="0" cellspacing="5" align="center" >
	
    <tr>
      <td align="center" colspan="2">Introduce la nueva contraseña para el usuario <%=user.nombre%><br><br></td>
    </tr>
     <tr>
        <td align="center" colspan="2"> <b>La contraseña debe cumplir con las siguientes características  </b><br></td>        
    </tr>
    <tr>
        <td aling ="left" colspan="2">
            &nbsp;&nbsp;&diams;	Debe tener una longitud de 8 a 16 caracteres <br>
            &nbsp;&nbsp;&diams;	Debe tener al menos una Letra Mayúscula <br>
            &nbsp;&nbsp;&diams;	Debe tener al menos un Numero<br>
            &nbsp;&nbsp;&diams;	Debe tener cualquiera de los siguientes caracteres especiales: $% *  <br><br>  
        </td>
    </tr>    
    <tr>
      <th align="right">Contraseña:</th>
      <td align="left"><input type="password" name="user_pass"></td>
    </tr>
    <tr>
      <td align="right">Confirma Contraseña:</td>
      <td align="left"><input type="password" name="user_passConf"></td>
    </tr>
    <tr>
      <td align="center" colspan="2"><input type="submit" value="Enviar"></td>
    </tr>   
     <tr>
      <td align="center" colspan="2"><br><br><%=HTMLHelper.displayNotifications(notificaciones)%></td>
    </tr>    
  </table>
</form>
</body>
</html>
