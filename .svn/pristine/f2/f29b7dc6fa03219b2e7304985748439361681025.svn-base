<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<html>
<head>
<title>Actualiza Seguros</title>
<script language="Javascript"></script>
<script>

	function actualizaSeguros(){
		document.getElementById("boton").disabled = true;
		alert('Va a actualizar los seguros de todos los clientes');
		window.document.forma.command.value = 'actualizaSeguros';
		window.document.forma.action="controller";
		document.body.style.cursor = "wait";
                window.document.forma.submit();
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<%
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
%>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>

<!-- INICIO NUEVO CODIGO -->
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="">

<table border="0" width="100%">
	<tr>

		<td align="center">
<!-- FIN NUEVO CODIGO -->
<h3>Actualiza Seguros</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
			<table>
				<tr>
					<td align="center" colspan="2">
			<br><input id="boton" type="button" value="Enviar" onClick="actualizaSeguros()">
		</td>
				
				

			</table>
<!-- INICIO NUEVO CODIGO -->
		</td>
	</tr>
</table>
</form>
<!-- FIN NUEVO CODIGO -->
<jsp:include page="footer.jsp" flush="true"/>

</body>
</html>