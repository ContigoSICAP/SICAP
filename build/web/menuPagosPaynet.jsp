<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<html>
<head>
    <title>Men&uacute; Pagos Paynet</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>
    function cargaArchivoPaynet(){
        window.document.forma.command.value='cargaArchivoPaynet';
        window.document.forma.submit();
    }
    function buscaIncidencias(){
        window.document.forma.command.value='buscaIncidenciasPaynet';
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
    <h2>Administraci&oacute;n Pagos Paynet</h2> 
<form name="forma" action="admin" method="post">
<input type="hidden" name="command" value="">
<table border="0" width="100%" cellspacing="0">
    <tr>
        <td width="100%" align="center" colspan="2"><br><a href="#" onClick="cargaArchivoPaynet()">Carga Confirmaci&oacute;n Pagos</a><br><br></td>
    </tr>
    <tr>
        <td width="100%" align="center" colspan="2"><br><a href="#" onClick="buscaIncidencias()">Incidencias Paynet</a><br><br></td>
    </tr>
</table>
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>