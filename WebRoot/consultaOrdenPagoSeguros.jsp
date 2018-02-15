<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.OrdenDePagoVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>

<%
    TreeMap catBancos = CatalogoHelper.getCatalogo("c_bancos");
    TreeMap catSucursales = CatalogoHelper.getCatalogo("c_sucursales");
    ArrayList<OrdenDePagoVO> ordenesPagoSeguros = (ArrayList<OrdenDePagoVO>)request.getAttribute("ORDENESDEPAGO");
    Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
%>

<html>
 <head>
<title>Consulta de Ordenes de Pagos de Seguro</title>
<script language="Javascript" src="./js/functions.js"></script>
<script type="text/javascript">
	function consultaOrdenes(){
            if(window.document.forma.fechaInicial.value==""){
                alert("Debe proporcionar fecha inicial");
                return false;
            }
            if(window.document.forma.fechaFinal.value==""){
                alert("Debe proporcionar fecha final");
                return false;
            }
            if(window.document.forma.fechaFinal.value<window.document.forma.fechaInicial.value){
                alert("La fecha inicial no puede ser mayor a la fecha final");
                return false;
            }
            window.document.forma.command.value='consultaOrdenesSeguros';
            window.document.forma.submit();
	}
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">

</head>

<body leftmargin="0" topmargin="0">
    <jsp:include page="header.jsp" flush="true"/>
    <center>
        <h2>Órdenes de Pago para Seguros por Fecha de Creación</h2>
        <%=HTMLHelper.displayNotifications(notificaciones)%>
        <form action="controller" method="post" name="forma">
            <input type="hidden" name="command" value="">
            <table border="0" width="100%" align="center">
                <tr>
                    <td width="50%" align="right"><strong>Fecha Inicial</strong></td>
                    <td width="50%"><input name="fechaInicial" size="12" type="text" id="fechaInicial" style="background-color: white" readonly="readonly" class="soloLectura"></td>
                </tr>
                <tr>
                    <td width="50%" align="right"><strong>Fecha Final</strong></td>
                    <td width="50%"><input type="text" name="fechaFinal" size="12" id="fechaFinal" style="background-color: white" readonly="readonly" class="soloLectura"></td>
                </tr>
  		<tr>
                    <td colspan="2" align="center">
                        <br>
                        <input type="button" name="" value="Consultar" onClick="consultaOrdenes();" >
                        <br>
                        <br>
                    </td>
                </tr>
            </table>
<%if ( ordenesPagoSeguros!=null){%>    
            <table border="0" width="90%" align="center" >
                <tr bgcolor="#009865" class="whitetext">
                    <td align="center">Fecha de Captura</td>
                    <td align="center">Sucursal</td>
                    <td align="center">No. Cliente</td>
                    <td align="center">No. Solicitud</td>
                    <td align="center">Nombre</td>
                    <td align="center">Orden de Pago</td>
                    <td align="center">Monto</td> 
                    <td align="center">Banco</td>                    
                </tr>
                <%for(OrdenDePagoVO ordenPago : ordenesPagoSeguros){%>
                <tr>
                    <td align="center"><%=HTMLHelper.displayField(ordenPago.getFechaCaptura()) %></td>
                    <td align="left"><%=HTMLHelper.getDescripcion(catSucursales, ordenPago.getIdSucursal()) %></td>
                    <td align="right"><%=HTMLHelper.displayField(ordenPago.getIdCliente()) %></td>
                    <td align="center"><%=HTMLHelper.displayField(ordenPago.getIdSolicitud()) %></td>
                    <td align="left"><%=HTMLHelper.displayField(ordenPago.getNombre()) %></td>
                    <td align="center"><%=HTMLHelper.displayField(ordenPago.getReferencia()) %></td>
                    <td align="right"><%=HTMLHelper.formatoMonto(ordenPago.getMonto()) %></td>
                    <td align="center"><%=HTMLHelper.getDescripcion(catBancos, ordenPago.getIdBanco()) %></td>
                </tr>
                <%}%>
            </table>
<%}%>
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>