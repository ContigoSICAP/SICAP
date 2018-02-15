<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<html>
<head>
<title>Menú principal administración</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function altaRepresentantes(){
		window.document.forma.command.value='altaRepresentantes';
		window.document.forma.submit();
	}

	function modificarRepresentantes(){
		window.document.forma.command.value='modificarRepresentantes';
		window.document.forma.submit();
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<%


%>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<center>
    <h2>Administraci&oacute;n de representantes</h2> 
    <h3>Opciones</h3>
<form name="forma" action="admin" method="post">
<input type="hidden" name="command" value="">
<table border="0" width="100%" cellspacing="0">
	<tr>
  		<td width="100%" align="center" colspan="2"><br><a href="#" onClick="altaRepresentantes()">Alta Convenios</a><br><br></td>
	</tr>
	<tr>
  		<td width="100%" align="center" colspan="2"><br><a href="#" onClick="modificarRepresentantes()"> Modificaci&oacute;n Convenios</a><br><br></td>
	</tr>
</table>
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>