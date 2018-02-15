<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>

<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>

<%
	UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
	TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
	TreeMap catBancos = CatalogoHelper.getCatalogoBancosCheques();
	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
%>
<html>
<head>
<title>Captura de cheques</title>
<script language="Javascript" src="./js/functions.js"></script>
<script type="text/javascript">
<!--
	
	function guardaChequera(){
		if ( window.document.forma.sucursal.value==0){
			alert('Debe seleccionar una sucursal');
			return false;
		}
		
		if ( window.document.forma.banco.value==0){
			alert('Debe seleccionar un banco');
			return false;
		}
		
		if ( window.document.forma.chequeinicial.value==''){
			alert('Ingrese un número de cheque inicial');
			return false;
		}else if ( !esEntero(window.document.forma.chequeinicial.value) ){
			alert('Ingrese un número de cheque válido');
			return false;
		}

		if ( window.document.forma.chequefinal.value==''){
			alert('Ingrese un número de cheque final');
			return false;
		}else if ( !esEntero(window.document.forma.chequefinal.value) ){
			alert('Ingrese un número de cheque válido');
			return false;
		}
		
		window.document.forma.command.value='guardaChequera';
		window.document.forma.submit();
		
	}

//-->

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<form action="controller" method="post" name="forma">
<input type="hidden" name="command" value="">
<table border="0" width="100%">
	<tr>
		<td align="center">
<h3>Captura de cheques<br></h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table  border="0" cellpadding="0" width="100%">
  <tr>
    <td width="50%" align="right">Sucursal</td>
    <td width="50%"><select name="sucursal"><%=HTMLHelper.displayCombo(catSucursales,0)%></select></td>
  </tr>
  <tr>
    <td width="50%" align="right">Banco</td>
    <td width="50%"><select name="banco"><%=HTMLHelper.displayCombo(catBancos,0)%></select></td>    
  </tr>
  <tr>
    <td width="50%" align="right">Cheque Inicial</td>
    <td width="50%">
        <input type="text" name="chequeinicial" size="7" maxlength="7" value="" />
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Cheque Final</td>
    <td width="50%"><input type="text" name="chequefinal" size="7" maxlength="7" value="" /></td>
  </tr>

  <tr>
  	<td colspan="2" align="center">
  		<br>
  		<input type="button" name="" value="Enviar" onClick="guardaChequera();" >
  		<br><br>
  	</td>
  </tr>

  <tr>
	<td valign="top" colspan = "3" align="center">&nbsp;
		<br><a href="<%=request.getContextPath() %>">Inicio</a>
	</td>
  </tr>
  
 </table>
		</td>
	</tr>
</table>
</form>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>
