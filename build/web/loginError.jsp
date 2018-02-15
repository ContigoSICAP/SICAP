<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.UsuarioVO"%>
<%@ page import="com.sicap.clientes.dao.UsuarioDAO"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.util.*"%>

<%
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
UsuarioDAO usuariodao = new UsuarioDAO();
UsuarioVO usuariovo  = new UsuarioVO();
String usuario = request.getParameter("j_username");
usuariovo = usuariodao.getDatosUsuario(usuario);
int num_intentos = 0;

if( usuariovo != null){
	num_intentos = usuariovo.intentosFallidos;
	num_intentos++;
	usuariovo.intentosFallidos = num_intentos;
	if( num_intentos==3 ){
		usuariovo.status="I";
	}
	usuariodao.updateUsuario(usuariovo, usuario);
}
%>

<html>
<head>
<title>Autentificaci&oacute;n</title>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0">
<jsp:include page="headerLogin.jsp" flush="true"/>
<form method="post" action="j_security_check" >
  <table border="0" cellspacing="5" align="center" >
    <tr>
      <td align="center" colspan="2">Usuario o password inv&aacute;lidos, intente nuevamente</td>
    </tr>
    <tr>
      <th align="right">Usuario:</th><%=HTMLHelper.displayNotifications(notificaciones)%>
      <td align="left"><input type="text" name="j_username"></td>
    </tr>
    <tr>
      <th align="right">Password:</th>
      <td align="left"><input type="password" name="j_password"></td>
    </tr>
    <tr>
      <td align="center" colspan="2"><input type="submit" value="Enviar"></td>
    </tr>
  </table>
</form>
</body>
</html>
