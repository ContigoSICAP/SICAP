<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>

<html>
<head>
<title>Genera archivo Círculo de Crédito</title>

<script>

	function cargarArchivos(){
		if ( window.document.encForm.periodo.value==''){
			alert("Debe especificar un periodo a reportar");
			return false;
		}
		window.document.encForm.action="cargaCirculoCredito.jsp?periodo="+window.document.encForm.periodo.value;
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
<jsp:include page="header.jsp" flush="true"/>
</td></tr>
</table>

    <center><h3>Actualización información Círculo de crédito</h3></center>

<center><div align="right"> 
 
</div><form name="encForm" method="post" action="" enctype="multipart/form-data"><div align="right"> 
 
</div><table border="0" width="100%" cellspacing="0" align="center" height="10%">

	<tr>
		<td width="50%" height="10%" align="right">Periodo a reportar (AAAAMMDD)<br></td>
		<td width="50%"><input type="text" name="periodo" size="8" maxlength="8"></td>
	</tr>

	<tr>
		<td colspan="3" height="10%" align="center"><input type="button" value="Generar archivo" onClick="cargarArchivos()"></td>
	</tr>
	</table>


</form>
</center>

<%@include file="footer.jsp"%>
</body>
</html>