<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<html>
<head>
<title>Administración de chequera</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>
	function altaChequera(){
            document.getElementById("td").disabled = true;
		window.document.forma.command.value='altaChequera';
		document.body.style.cursor = "wait";
                window.document.forma.submit();
	}

	function consultaChequera(){
            document.getElementById("td").disabled = true;
		window.document.forma.command.value='consultaChequeras';
		document.body.style.cursor = "wait";
                window.document.forma.submit();
	}

	function modificaEstatusCheques(){
            document.getElementById("td").disabled = true;
		window.document.forma.command.value='modificaEstatusCheques';
		document.body.style.cursor = "wait";
                window.document.forma.submit();
	}
	
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<center>
    <h2>Administraci&oacute;n Chequeras</h2> 
    <h3>Opciones</h3>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="">
<table border="0" width="100%" cellspacing="0">
<%if ( request.isUserInRole("ALTA_CHEQUERA") ){%>
	<tr>
  		<td width="100%" align="center" colspan="2"id="td"><br><a href="#" onClick="altaChequera()">Alta de chequera</a><br></td>
	</tr>
	<tr>
  		<td width="100%" align="center" colspan="2"id="td"><br><a href="#" onClick="consultaChequera()">Consulta de chequera</a><br></td>
	</tr>
<%}if ( request.isUserInRole("CAMBIO_ESTATUS_CHEQUE") ){%>
	<tr>
  		<td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="modificaEstatusCheques()">Modificación cheques</a><br></td>
	</tr>
<%} %>	
</table>
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>