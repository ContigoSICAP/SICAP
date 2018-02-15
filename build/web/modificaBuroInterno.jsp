<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>

<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="java.util.*"%>

<html>
<head>
<title>Modifica Incidencia</title>

<script type="text/javascript">

	function modificaIncidencia(){
		
		window.document.forma.command.value = 'modificaIncidenciaBuroInterno';
				
				if ( window.document.forma.descripcion.value=='' || !esTexto( "^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$",window.document.forma.descripcion.value)){
				   //alert('Debe introducir un Apellido Paterno v\u00e1lido');
				   $.prompt('El campo Descripcion es requerido');
				   return false;
				}
		
		
		window.document.forma.submit();
	}
	
	function buscaIncidencia(){
		window.document.forma.command.value = 'consultaBuroInterno';
		window.document.forma.submit();
	}


</script>

<link href="./css/BEtext.css" rel="stylesheet" type="text/css"></head>
<%
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
BuroInternoVO clientesBuro = (BuroInternoVO)request.getAttribute("CLIENTE_BURO_INTERNO");

TreeMap catSucursales = CatalogoHelper.getCatalogoEstatusBuroInterno();
TreeMap catMotivo = CatalogoHelper.getCatalogoMotivo();
TreeMap catEstados = CatalogoHelper.getCatalogo(ClientesConstants.CAT_ESTADOS);
//ClienteVO cliente2 = new ClienteVO();
//ClienteVO existeRFC = new ClienteVO();
//int i =0;
//i = clientes.nombre;

%>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<CENTER>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="guardaConyuge">
<input type="hidden" name="idSolicitud" value="">
<table border="0" width="100%">
	<tr>
	<td align="center">	
<h3>Modificaci&oacute;n Incidencia</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>

<table width="100%" border="0" cellpadding="0">
  <tr>
    <td width="50%" align="right">Numero Cliente</td>
    <td width="50%">
      <input type="text" name="idCliente" size="10" maxlength="50" value="<%=clientesBuro.numCliente%>" readonly>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Apellido Paterno</td>
    <td width="50%">
      <input type="text" name="aPaterno" size="40" maxlength="50" value="<%=clientesBuro.apaterno%>" readonly>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Apellido Materno</td>
    <td width="50%">
      <input type="text" name="aMaterno" size="40" maxlength="50" value="<%=clientesBuro.amaterno%>" readonly>
    </td>
  </tr>	
  <tr>
    <td width="50%" align="right">Nombre</td>
    <td width="50%">
      <input type="text" name="nombre" size="40" maxlength="50" value="<%=clientesBuro.nombre%>" readonly>
    </td>
  </tr>
  <tr>
  	<td width="50%" align="right">Status</td>
  	<td width="50%">  
   	<select name="status" size="1" >
		<%=HTMLHelper.displayCombo(catSucursales, clientesBuro.estatus)%>
   	</select>
   	</td>
  </tr>
  <tr>
  	<td width="50%" align="right">Motivo</td>
  	<td width="50%">  
   	<select name="motivo" size="1" >
		<%=HTMLHelper.displayCombo(catMotivo, clientesBuro.motivoIngreso)%>
   	</select>
   	</td>
  </tr>
  <tr>
    <td width="50%" align="right"><span style="color:red">* </span>Descripci&oacute;n</td>
    <td width="50%">
      <textarea name="descripcion" cols="30" rows="4"><%=clientesBuro.descripcion%></textarea>
    </td>
  </tr>
  
  <tr>
    <td width="50%" align="right">Fecha de &Uacute;ltima modificaci&oacute;n</td>
    <td width="50%">
      <input type="text" name="fechaUltimaModificacion" id="fechaUltimaModificacion" size="10" maxlength="10" value="<%=HTMLHelper.displayField(clientesBuro.fechaUltimaModificacion)%>">
      (dd/mm/aaaa) </td>
  </tr>
  
  <tr>
    <td width="50%" align="right">Usuario de &Uacute;ltima modificaci&oacute;n</td>
    <td width="50%">
      <input type="text" name="usuarioModificacion" size="40" maxlength="80" value="<%=request.getRemoteUser()%>" readonly>
    </td>
  </tr>
  
  
  <tr>
  		<td align="center" colspan="2">
			<br><input type="button" value="Regresar" onClick="buscaIncidencia()">
				<input type="button" value="Modificar" onClick="modificaIncidencia()">
		</td>
	</tr>
		
</table>
	</td>
	</tr>
</table>
</form>
</CENTER>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>
