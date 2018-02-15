<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<html>
<head>
<title>Autentificaci&oacute;n</title>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0" onload="javascript:window.document.forma.j_username.focus();">
<jsp:include page="headerLogin.jsp" flush="true"/>
<form method="post" action="j_security_check" name="forma">
  <table border="0" cellspacing="5" align="center" >
    <tr>
      <th align="right">Usuario:</th>
      <td align="left"><input type="text" name="j_username" ></td>
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
