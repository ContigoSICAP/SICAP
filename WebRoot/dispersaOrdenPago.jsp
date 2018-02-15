<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.TreeMap"%>
<html>
<head>
<title>Ordenes de Pago</title>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">

<script>

	function muestraDesembolsosPendientes(){
            if(window.document.forma.banco.value==10){
                window.document.forma.command.value='buscaOrdenesPagos';
            }else{
                window.document.forma.command.value='muestraDesembolsosPendientes';
                if(window.document.forma.estatus.value == 0&&window.document.forma.banco.value==24){
                    alert("Es necesario seleccionar el Estatus de las ordenes de pago del Banco Santander");
                    return false;
                }
            }
            window.document.forma.submit();
	}

</script>
</head>

<% 
TreeMap catSucursales = CatalogoHelper.getCatalogoSucursales();
//TreeMap catBancos     = CatalogoHelper.getCatalogo("C_BANCOS");
TreeMap catBancos = CatalogoHelper.getCatalogoBancosDispersion();
TreeMap catEstatus    = CatalogoHelper.getCatalogo("C_ESTATUS_ORDENES_PAGO");
%>

<body leftmargin="0" topmargin="0">
<br><jsp:include page="header.jsp" flush="true"/>
<center>
    <h2>Consulta Ordenes de Pago</h2>     
<form name="forma" action="admin" method="post">
<input type="hidden" name="command" value="">
<table border="0" width="100%" cellspacing="0">



</table>
<table width="80%" border="0" cellspacing="7">
  <tr>
    <td width="49%" align="right"><strong>Banco</strong></td>
    <td width="51%">
    	<select name="banco" id="banco" readOnly="readOnly">
    			<%=HTMLHelper.displayCombo(catBancos, 2)%>
    	</select>
    </td>
  </tr>
  <tr>
    <td align="right"><strong>Sucursal</strong></td>
    <td>
		<select name="sucursal" size="1">
			<%=HTMLHelper.displayCombo(catSucursales, 0)%>
		</select>
	</td>
  </tr>
  <tr>
    <td align="right"><strong>Estatus</strong></td>
    <td>
    	<select name="estatus" id="estatus">
    			<%=HTMLHelper.displayCombo(catEstatus, 0)%>
    	</select>
    </td>
  </tr>
  <tr>
      <td align="right"><strong>Fecha Dispersi&oacute; Inicial</strong></td>
    <td><input name="fechaInicial" type="text" id="fechaInicial"> 
      dd/mm/yyyy</td>
  </tr>
  <tr>
    <td align="right"><strong>Fecha Dispersi&oacute; Final</strong></td>
    <td><input type="text" name="fechaFinal" id="fechaFinal">
dd/mm/yyy</td>
  </tr>
  <tr>
      <td colspan="2" align="center"><input name="send" type="button" class="text" id="send" value="Enviar" onClick="muestraDesembolsosPendientes()"></td>
    </tr>
</table>

</form>
</center>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>