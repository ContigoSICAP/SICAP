<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<html>
<head>
<title>Administracion Seguros</title>
<script>
	function generaOrdenSeguro(){
                document.getElementById("td").disabled = true;
		window.document.forma.command.value='generaOrdenSeguro';
		document.body.style.cursor = "wait";
                window.document.forma.submit();
	}
        function consultaOrdenesSeguro(){
                window.document.forma.command.value='consultaOrdenPagoSeguros';
                window.document.forma.submit();
        }

</script>

<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<center>
	<h3>Administración de Seguros<br></h3>
<form name="forma" action="controller" method="POST">
<input type="hidden" name="command" value="">

<div><table border="0" width="100%" cellspacing="0" align="center" height="10%">
	<tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="generaOrdenSeguro()">Genera Orden de Pago</a><br><br></td>
	</tr>
	<tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="consultaOrdenesSeguro()">Consulta Órdenes de Pago para Seguro</a><br><br></td>
	</tr>
</table></div>
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>