<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>

<%@ page import="com.sicap.clientes.vo.UsuarioVO"%>
<%@ page import="com.sicap.clientes.vo.LoteChequesVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>

<%
	UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
	TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
//	TreeMap catSucursalesAll = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
	TreeMap catBancos = CatalogoHelper.getCatalogoBancosCheques();
	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
	LoteChequesVO[] lotes = (LoteChequesVO[])request.getAttribute("LOTES");
%>
<html>
<head>
<title>Captura de cheques</title>
<script language="Javascript" src="./js/functions.js"></script>
<script type="text/javascript">
	
	function consultaChequeras(){
		if ( window.document.forma.sucursal.value==0){
			alert('Debe seleccionar una sucursal');
			return false;
		}
		
		window.document.forma.command.value='obtieneChequeras';
		window.document.forma.submit();
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<form action="controller" method="post" name="forma">
<input type="hidden" name="command" value="">


<table border="0" width="100%">
	<tr>
		<td colspan="2" align="center">
		<h3>Consulta de chequeras<br></h3>
		<%=HTMLHelper.displayNotifications(notificaciones)%>
		</td>
	</tr>
  <tr>
    <td width="50%" align="right">Sucursal</td>
    <td width="50%" align="left"><select name="sucursal"><%=HTMLHelper.displayCombo(catSucursales,0)%></select></td>
  </tr>
  <tr>
  	<td colspan="2" align="center">
  		<br>
  		<input type="button" name="" value="Consultar" onClick="consultaChequeras();" >
  		<br><br>
  	</td>
  </tr>
<tr>
<tr>
	<td colspan="2" align="center">
		<br><a href="<%=request.getContextPath() %>">Inicio</a>
	</td>
</tr>

<%
if ( lotes!=null && lotes.length > 0){
%>
	<tr>
	<td colspan="2" align="center">
		<table border="0" cellpadding="0" width="400" >
		<tr bgcolor="#009865" class="whitetext">
		  	<td width="20%" align="center">Lote</td>
		  	<td width="100%" align="center">Sucursal</td>
		  	<td width="100%" align="center">Banco</td>
		  	<td width="60%" align="center">Cheque Inicial</td>
		  	<td width="60%" align="center">Cheque Final</td>  	
		  	<td width="60%" align="center">Fecha alta</td>  	  	
		</tr>
<%
	for ( int i=0 ; i<lotes.length ; i++ ){
		LoteChequesVO lote = lotes[i];
%>
		<tr>
	  		<td width="20%" align="center"><%=HTMLHelper.displayField(lote.idLote) %></td>
	  		<td width="100%" align="center"><%=HTMLHelper.getDescripcion(catSucursales,lote.idSucursal) %></td>
	  		<td width="100%" align="center"><%=HTMLHelper.getDescripcion(catBancos, lote.idBanco) %></td>
	  		<td width="60%" align="center"><%=HTMLHelper.displayField(lote.numChequeIni) %></td>
	  		<td width="60%" align="center"><%=HTMLHelper.displayField(lote.numChequeFin) %></td>
	  		<td width="60%" align="center"><%=HTMLHelper.displayField(lote.fechaAlta) %></td>
		</tr>
<%
	}%>
		</table>
	</td>
	</tr>
<%}%>

</table>
</form>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>
