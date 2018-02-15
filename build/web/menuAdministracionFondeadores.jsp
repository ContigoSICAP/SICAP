<%-- 
    Document   : menuAdministracionFondeadores
    Created on : 4/08/2015, 01:27:00 PM
    Author     : avillanueva
--%>

<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<html>
<head>
    <title>Men&uacute; Administraci&oacute;n Fondeadores</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>
    function saldoFondeador(){
        document.getElementById("td").disabled = true;
        window.document.forma.command.value='saldofondeador';
        document.body.style.cursor = "wait";
        window.document.forma.submit();
    }
    function ingresoCobranza(){
        document.getElementById("td").disabled = true;
        window.document.forma.command.value='ingresoCobranza';
        document.body.style.cursor = "wait";
        window.document.forma.submit();
    }
    function asignaCarteraGarantia(){
        document.getElementById("td").disabled = true;
        window.document.forma.command.value='asignaCarteraGarantia';
        document.body.style.cursor = "wait";
        window.document.forma.submit();
    }
    function generarPreseleccion(){
        document.getElementById("td").disabled = true;
        window.document.forma.command.value='generarPreseleccion';
        document.body.style.cursor = "wait";
        window.document.forma.submit();
    }
    function subePreSeleccionada(){
        document.getElementById("td").disabled = true;
        window.document.forma.command.value='subePreSeleccionada';
        document.body.style.cursor = "wait";
        window.document.forma.submit();
    }
    function ingresoLineaCredito(){
        document.getElementById("td").disabled = true;
        window.document.forma.command.value='ingresoLineaCredito';
        document.body.style.cursor = "wait";
        window.document.forma.submit();
    }
    function ingresoPagare(){
        document.getElementById("td").disabled = true;
        window.document.forma.command.value='ingresoPagare';
        document.body.style.cursor = "wait";
        window.document.forma.submit();
    }
    function ingresoAmort(){
        document.getElementById("td").disabled = true;
        window.document.forma.command.value='ingresoAmort';
        document.body.style.cursor = "wait";
        window.document.forma.submit();
    }
    function ingresoDesactLineaCredito(){
        document.getElementById("td").disabled = true;
        window.document.forma.command.value='ingresoDesactLineaCredito';
        document.body.style.cursor = "wait";
        window.document.forma.submit();
    }
    function ingresoPagoPagare(){
        document.getElementById("td").disabled = true;
        window.document.forma.command.value='ingresoPagoPagare';
        document.body.style.cursor = "wait";
        window.document.forma.submit();
    }
    function altaFondeador(){
        document.getElementById("td").disabled = true;
        window.document.forma.command.value='ingresoAltaFondeador';
        document.body.style.cursor = "wait";
        window.document.forma.submit();
    }
    function consultaLineasCredito(){
        document.getElementById("td").disabled = true;
        window.document.forma.command.value='ingresoConsultaLC';
        document.body.style.cursor = "wait";
        window.document.forma.submit();
    }
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<%%>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<center>
    <h2>Administraci&oacute;n Fondeadores</h2> 
    <h3>Opciones</h3>
<form name="forma" action="admin" method="post">
<input type="hidden" name="command" value="">
<table border="0" width="100%" cellspacing="0">
    <tr>
        <td width="100%" align="center" colspan="2"><br><a href="#" onClick="altaFondeador()">Alta de Fondeadores</a><br><br></td>
    </tr>
    <tr>
        <td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="saldoFondeador()">Saldo Fondeadores</a><br><br></td>
    </tr>
    <tr>
        <td width="100%" align="center" colspan="2"><br><a href="#" onClick="ingresoCobranza()">Ingreso Cobranza</a><br><br></td>
    </tr>
    <tr>
        <td width="100%" align="center" colspan="2"><br><a href="#" onClick="asignaCarteraGarantia()">Asignaci&oacute;n Manual de Cartera en Garant&iacute;a</a><br><br></td>
    </tr>
    <tr>
        <td width="100%" align="center" colspan="2"><br><a href="#" onClick="generarPreseleccion()">Ejecutar Preselecci&oacute;n de Cartera en Garant&iacute;a</a><br><br></td>
    </tr>
    <tr>
        <td width="100%" align="center" colspan="2"><br><a href="#" onClick="subePreSeleccionada()">Subir Asignaci&oacute;n/Preselecci&oacute;n  de Cartera en Garant&iacute;a</a><br><br></td>
    </tr>
    <tr>
        <td width="100%" align="center" colspan="2"><br><a href="#" onClick="ingresoLineaCredito()">Alta L&iacute;nea de Cr&eacute;dito</a><br><br></td>
    </tr>
    <tr>
        <td width="100%" align="center" colspan="2"><br><a href="#" onClick="consultaLineasCredito()">Consulta L&iacute;neas de Cr&eacute;dito</a><br><br></td>
    </tr>
    <tr>
        <td width="100%" align="center" colspan="2"><br><a href="#" onClick="ingresoDesactLineaCredito()">Desactivar L&iacute;nea de Cr&eacute;dito</a><br><br></td>
    </tr>
    <tr>
        <td width="100%" align="center" colspan="2"><br><a href="#" onClick="ingresoPagare()">Alta de Pagar&eacute;</a><br><br></td>
    </tr>
    <tr>
        <td width="100%" align="center" colspan="2"><br><a href="#" onClick="ingresoAmort()">Alta de Amortizaci&oacute;n de Pagar&eacute;</a><br><br></td>
    </tr>
     <%if(request.isUserInRole("ADM_PAGOS")){%>
       <tr>
            <td width="100%" align="center" colspan="2"><br><a href="#" onClick="ingresoPagoPagare()">Pago del Capital del Pagar&eacute;</a><br><br></td>
        </tr>
    <%}%>
    
</table>
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>
