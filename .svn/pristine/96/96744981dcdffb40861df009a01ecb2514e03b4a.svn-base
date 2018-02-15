<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<html>
<head>
<title>Envio de archivo de Pagos Referenciados</title>
<script language="Javascript"></script>
<script>

	function cargarArchivo(){
		if ( window.document.encForm.pagosReferenciadosHSBC.value=='' && window.document.encForm.pagosReferenciadosBanorte.value==''){
			alert("Debe especificar un archivo a cargar");
			return false;
		}
		window.document.encForm.action="procesaArchivoPagosReferenciados.jsp";
		window.document.encForm.target="_self";
		window.document.encForm.submit();
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
<form name="encForm" method="post" action="controller" enctype="multipart/form-data">
<input type="hidden" name="tipo" value="0">
<table border="0" width="100%">
	<tr>

		<td align="center">
<!-- FIN NUEVO CODIGO -->
<h3>Archivo pagos referenciados</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
			<table>
				<tr>
					<td colspan="2" align="center">Archivo de pagos referenciados</td>
				</tr>
				<tr>
					<td>Archivo de pagos de HSBC<br></td>
					<td><input type="file" name="pagosReferenciadosHSBC" size="35"></td>
				</tr>

				<tr>
					<td>Archivo de pagos Banorte</td>
					<td><input type="file" name="pagosReferenciadosBanorte" size="35"></td>
				</tr>

				<tr>
				</tr>
				<tr>
					<td colspan="2" align="center"><br><input type="button" onclick="cargarArchivo()" value="Cargar archivo"></td>
				</tr>

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