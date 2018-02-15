<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>

<html>
<head>
<title>Genera archivo Buró de Crédito</title>

<script>

	function cargarArchivos(){
		if ( window.document.encForm.cartera.value==''){
			alert("Debe especificar al menos un archivo a cargar");
			return false;
		}
		window.document.encForm.action="generaFichasPagos.jsp";
		window.document.encForm.target="_self";
		window.document.encForm.submit();
	}


</script>

<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>


<body bgcolor="white" bgproperties=fixed>
<table>
<tr>
<td>
<img src="./Images/imagen.gif">
</td></tr>
</table>

    <center><h3>Actualización información Buró de crédito</h3></center>

<center>

<form name="encForm" method="post" action="" enctype="multipart/form-data">

<table border="0" width="100%" cellspacing="0" align="center" height="10%">

	<tr>
		<td colspan="2" height="10%" align="center">Seleccione el archivo a cargar</td>
		<td><input type="file" name="cartera" size="35"></td>
	</tr>

	<tr>
		<td colspan="3" height="10%" align="center"><input type="button" value="Generar archivo" onClick="cargarArchivos()"></td>
	</tr>
	</table>


</form>
</center>

<jsp:include page="footer.jsp" flush="true"/></body>
</html>