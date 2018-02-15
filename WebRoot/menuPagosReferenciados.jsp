<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<html>
<head>
<title>Menú principal administración</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function recibirPagos(){
            window.document.forma.command.value='recibirPagosReferenciados.jsp';
            window.document.forma.submit();
	}
        
	function reprocesarPagos(){
            window.document.forma.command.value='reprocesarPagos.jsp';
            window.document.forma.submit();
	}
        function buscaPagos(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='buscaPagosManual';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
        }
        function buscaPagosEliminar(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='buscaPagos';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
	}
        function menuPagosPaynet(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='menuPagosPaynet';
            document.body.style.cursor = "wait";
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
    <h2>Pagos Referenciados</h2> 
    <h3>Opciones</h3>
<form name="forma" action="admin" method="post">
<input type="hidden" name="command" value="">
<table border="0" width="100%" cellspacing="0">
        <%if(request.isUserInRole("ADM_PAGOS")){%>
            <tr>
                <td width="100%" align="center" colspan="2"><br><a href="recibirPagosReferenciados.jsp" onClick="">Recibir Pagos</a><br><br></td>
            </tr>
            <tr>
                <td width="100%" align="center" colspan="2"><br><a href="reprocesarPagos.jsp" onClick="">Aclaraciones</a><br><br></td>
            </tr>
            <tr>
                <td width="100%" align="center" colspan="2"><br><a href="generaArchivoPagosIBS.jsp" onClick="">Exportar Archivo pagos</a><br><br></td>
            </tr>
            <tr>
                <td width="100%" align="center" colspan="2"><br><a href="descongelaAhorro.jsp" onClick="">Descongelar Garant&iacute;a Grupal</a><br><br></td>
            </tr>
            <tr>
                <td width="100%" align="center" colspan="2"><br><a href="descongelarPagoGarantiaIndividual.jsp" onClick="">Descongelar Garant&iacute;a Individual</a><br><br></td>
            </tr>
            <tr>
                <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="buscaPagos()">Ingreso de Pagos Manaules</a><br><br></td>
            </tr>
            <tr>
                <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="buscaPagosEliminar()">Eliminacion de Pagos</a><br><br></td>
            </tr>
        <%}if(request.isUserInRole("ADM_PAGOS_MESA_CONTROL")){%>
            <tr>
                <td width="100%" align="center" colspan="2"><br><a href="condonacionGrupal.jsp" onClick="">Condonacion Multa</a><br><br></td>
            </tr>
            <tr>
                <td width="100%" align="center" colspan="2"><br><a href="condonacionInteresGrupal.jsp" onClick="">Condonacion Interés grupal</a><br><br></td>
            </tr>
            <tr>
                <td width="100%" align="center" colspan="2"><br><a href="quitaCapitalGrupal.jsp" onClick="">Quita Capital grupal</a><br><br></td>
            </tr>
        <%}%>
        <tr>
            <td width="100%" align="center" colspan="2"><br><a href="castigoGrupal.jsp" onClick="">Castigo Grupal</a><br><br></td>
	</tr>
        <tr>
            <td width="100%" align="center" colspan="2"><br><a href="#" onClick="menuPagosPaynet()">Pagos Paynet</a><br><br></td>
	</tr>
</table>
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>