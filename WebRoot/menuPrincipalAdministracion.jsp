<%@page import="com.sicap.clientes.dao.CatalogoDAO"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>

<%
CatalogoDAO catalogoDao = new CatalogoDAO();
%>
<html>
<head>
<title>Menú principal administración</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function administracionUsuarios(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='administracionUsuarios';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
        }
	
	function administracionSucursales(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='administracionSucursales';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
	}
	
	function consultaDB(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='ejecutaConsulta';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
	}
	
	function administracionEjecutivos(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='administracionEjecutivos';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
	}
	
	function administracionRepresentantes(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='administracionRepresentantes';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
	}
	
	function liquidacionGrupos(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='liquidacionGrupos';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
	}
	function pagosReferenciados(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='pagosReferenciados';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
	}
	
	
	function consultaReportes(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='consultaReportes';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
	}
	
	function ordenesDePago(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='ordenesDePago';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
	}

	function soporte(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='administracionSoporte';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
	}
	
	function cierreDia(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='cierreDia';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
	}
	
	function cierreDiaMigracion(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='cierreDiaMigracion';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
	}
	
	function cierreContable(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='cierreContable';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
	}

	
	function mayorizacion(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='mayorizacion';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
	}
        
        function ajusteCredito(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='ajusteCredito';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
	}
        
        function caretraCedida(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='caretraCedida';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
	}
        
        function adminClases(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='manClases';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
	}
        function seguros(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='administracionSeguros';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
	}
        function cambioSucursalClientes(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='cambioSucursalClientes';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
	}		
        function saldoFavor(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='saldoFavor';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
        }
        function administracionCiclos(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='administracionCiclos';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
        }
        function tarjetas(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='tarjetas';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
	}
        function administracionCobradores(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='administracionCobradores';
            document.body.style.cursor = "wait";
            window.document.forma.submit();                        
        }
        function auditores(){
                document.getElementById("td").disabled = true;
		window.document.forma.command.value='auditores';
		document.body.style.cursor = "wait";
                window.document.forma.submit();
	}
        function administracionFondeadores(){
                document.getElementById("td").disabled = true;
		window.document.forma.command.value='administracionFondeadores';
		document.body.style.cursor = "wait";
                window.document.forma.submit();
	}
        function cierreDiaBursa(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='cierreDiaBursa';
            document.body.style.cursor = "wait";
            window.document.forma.submit();
	}
        function generarInterfacesBursa(){
            document.getElementById("td").disabled = true;
            window.document.forma.command.value='interfacesBursa';
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
    <h2>Administraci&oacute;n SICAP</h2> 
    <h3>Opciones</h3>
<form name="forma" action="admin" method="post">
<input type="hidden" name="command" value="">
<table border="0" width="100%" cellspacing="0">
    <%if(request.isUserInRole("AUXILIAR_CREDITO")){%>
        <tr>
            <td width="100%" align="center" colspan="2"><br>&nbsp;<br></td>
	</tr>
    <%}if(request.isUserInRole("ADM_SOPORTE")){%> 
	<tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="administracionUsuarios()">Administraci&oacute;n de usuarios</a><br><br></td>
	</tr>
	<tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="administracionSucursales()">Administraci&oacute;n de sucursales</a><br><br></td>
	</tr>
	<tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="soporte()">Administraci&oacute;n Soporte</a><br><br></td>
	</tr>
    <%}if(request.isUserInRole("ADM_CICLOS")){%>
	<tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="administracionCiclos()">Administraci&oacute;n de Ciclos</a><br><br></td>
	</tr>	
    <%}if(request.isUserInRole("ADM_EJECUTIVOS")){%>
	<tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="administracionEjecutivos()">Administraci&oacute;n de ejecutivos</a><br><br></td>
	</tr>
    <%}if(request.isUserInRole("ADM_REPRESENTANTES")){%>
	<tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="administracionRepresentantes()">Administraci&oacute;n de Convenios</a><br><br></td>
	</tr>
    <%}if(request.isUserInRole("ADM_PAGOS")){%> 
	<tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="liquidacionGrupos()">Liquidaci&oacute;n de Equipos</a><br><br></td>
	</tr>
	<tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="pagosReferenciados()">Administraci&oacute;n de Pagos</a><br><br></td>
	</tr>
	<tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="cierreDia()">Cierre de D&iacute;a</a><br><br></td>
	</tr>
	<tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="cierreDiaMigracion()">Cierre de D&iacute;a Migraci&oacute;n</a><br><br></td>
	</tr>
	<tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="cierreContable()">Cierre Contable</a><br><br></td>
	</tr>
	<tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="mayorizacion()">Mayorizacion y Envio</a><br><br></td>
	</tr>
	<tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="saldoFavor()">Saldo a Favor</a><br><br></td>
	</tr>
        <tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="cierreDiaBursa()">Cierre BURSA</a><br><br></td>
	</tr>
        <tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="generarInterfacesBursa()">Generar Interfaces Bursa</a><br><br></td>
	</tr>
        <%if(request.isUserInRole("ADM_PAGOS_MANAGER")){%>
            <!--<tr>
                <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="ajusteCredito()">Ajuste de Cr&eacute;ditos</a><br><br></td>
            </tr>-->
            <tr>
                <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="caretraCedida()">Migraci&oacute;n Cartera Cedida</a><br><br></td>
            </tr>
        <%}%>
    <%}if(request.isUserInRole("ADM_PAGOS_MESA_CONTROL")){%>
            <tr>
                <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="pagosReferenciados()">Administraci&oacute;n de Condonaciones</a><br><br></td>
            </tr>
    <%}if(request.isUserInRole("ADM_REPORTES")){%> 
	<tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="consultaReportes()">Reportes</a><br><br></td>
	</tr>	
	<%}if( request.isUserInRole("ADM_ORDENESPAGO") || request.isUserInRole("CAMBIO_ESTATUS_CHEQUE") || request.isUserInRole("SOPORTE_OPERATIVO") ){%> 
	<tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="ordenesDePago()">Administraci&oacute;n Ordenes de Pagos</a><br><br></td>
	</tr>
        <%}if( request.isUserInRole("ADM_TARJETAS")){%> 
	<tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="tarjetas()">Administraci&oacute;n Tarjetas</a><br><br></td>
	</tr>
	<%}if( request.isUserInRole("ADM_SEGUROS")){%> 
	<tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="seguros()">Administraci&oacute;n de seguros</a><br><br></td>
	</tr>
	<%}if( request.isUserInRole("ADM_CLIENTES")){%> 
	<tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="cambioSucursalClientes()">Cambio de Sucursal Clientes</a><br><br></td>
	</tr>
    <%}if( request.isUserInRole("AUDITOR_ADMIN") ){%> 
	<tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="auditores()">Administraci&oacute;n Auditores</a><br><br></td>
	</tr>
    <%}%>
    <%if( request.isUserInRole("admin") ){%> 
	<!--<tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="consultaDB()">Consultas</a><br><br></td>
	</tr>-->
    <%}if( request.isUserInRole("ADM_COBRADORES")){%>
        <tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="administracionCobradores()">Administraci&oacute;n de Cobradores</a><br><br></td>
	</tr>
    <%}%>
    <%if( request.isUserInRole("ADM_FONDEADORES")){%>
        <tr>
            <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="administracionFondeadores()">Administraci&oacute;n Fondeadores</a><br><br></td>
	</tr>
    <%}%>

</table>
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>