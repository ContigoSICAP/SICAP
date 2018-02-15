<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.util.*"%>

<%  

Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");

%>

<html>
<head>
<title>Autentificaci&oacute;n</title>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>

  <table border="0" cellspacing="5" align="center" >
    <tr>
      <td align="center" colspan="2"><br><br><%=HTMLHelper.displayNotifications(notificaciones)%></td>
    </tr>
  </table>

</body>
</html>
