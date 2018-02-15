<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="java.util.TreeMap"%>
<html>
<head>
<title>Modificacion de Banco de Orden de Pago</title>
<script>
  function eliminaCiclo(){
      if ( window.document.forma.banco.value=='' || window.document.forma.idOrdenPago.value=='' ){
          alert("Debe especificar orden de pago y banco");
          return false;
      }
      window.document.forma.submit();
  }
</script>

<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>

<%	TreeMap catBancos     = CatalogoHelper.getCatalogo("C_BANCOS");
	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES"); %>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<center><h3>Modificación de Banco en Orden de Pago<br></h3></center>

<form name="forma" action="admin" method="POST">
<input type="hidden" name="command" value="modificaBancoOrdenPago">
<center><%=HTMLHelper.displayNotifications(notificaciones)%></center>
<div><table border="0" width="100%" cellspacing="0" align="center" height="10%">
            <tr>
    <td width="49%" align="right"><strong>Banco</strong></td>
    <td width="51%">
    	<select name="banco" id="banco" readOnly="readOnly">
    			<%=HTMLHelper.displayCombo(catBancos, 2)%>
    	</select>
    </td>
            </tr>
            <tr>
                        <td width="50%" height="10%" align="right">Orden de Pago<br></td>
                        <td width="50%"><input type="text" name="idOrdenPago" size="20" maxlength="20"></td>
            </tr>

            <tr>
                        <td colspan="3" height="10%" align="center"><input type="button" value="Cambia Banco" onclick="eliminaCiclo();"></td>
                        <td colspan="3" height="10%" align="center"><br></td>
            </tr>
            </table>
</div>
</form>

<%@include file="footer.jsp"%></body>
</html>

