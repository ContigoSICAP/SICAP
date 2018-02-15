<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.TreeMap"%>
<html>
<head>
<title>Buro Interno</title>
<script>

     function submitenter(myfield,e){
        var keycode;
          if (window.event) 
              keycode = window.event.keyCode;
          else if (e) 
             keycode = e.which;
          else 
              return true;

         if (keycode == 13){
             buscar();
           }else
              return true;
           }

	function buscar(){
		if( window.document.forma.idCte.value=='' && window.document.forma.apellidoPaterno.value=='' && window.document.forma.apellidoMaterno.value=='' && window.document.forma.nombre.value==''){
			alert('Introduzca un valor para su búsqueda por favor');
			return false;
		}
		if(window.document.forma.idCte.value!='' && !esEntero(window.document.forma.idCte.value)){
			alert('Introduzca un n\u00famero de cliente v\u00e1lido por favor');
			return false;
		}
		window.document.forma.command.value='buscaIncidenciaBuro';
		window.document.forma.submit();
	}

	function consultaCliente(idCliente){
		window.document.forma.command.value='modificaIncidencia';
		window.document.forma.idCliente.value=idCliente;
		window.document.forma.idSolicitud.value=1;
		window.document.forma.submit();
	}
	
	function nuevaIncidencia(){
		if( window.document.forma.idCte.value==''){
			//alert('Para Ingresar una nueva Incidencia el campo N\u00famero Cliente es requerido');
			$.prompt('Para Ingresar una nueva Incidencia el campo N\u00famero Cliente es requerido');
			return false;
		}
		window.document.forma.command.value='altaIncidencia';
		window.document.forma.submit();
	}

	function reporteCartera(){
		window.document.forma.command.value='reporteCartera';
		window.document.forma.submit();
	}
	
	function menuReportes(){
		window.document.forma.command.value='menuReportes';
		window.document.forma.submit();
	}

	function admonChequera(){
		window.document.forma.command.value='admonChequera';
		window.document.forma.submit();
	}

	function altaIncidencia(){
		window.document.forma.command.value='consultaGrupo';
		window.document.forma.submit();
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<%
	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
	BuroInternoVO clientes[] = (BuroInternoVO[])request.getAttribute("CLIENTES_BURO_INTERNO");
	TreeMap catEstatus = CatalogoHelper.getCatalogoEstatusBuroInterno();
	TreeMap catMotivo = CatalogoHelper.getCatalogoMotivo();

%>

<body leftmargin="0" topmargin="0" onload="javascript:window.document.forma.idCte.focus();">
<jsp:include page="header.jsp" flush="true"/>
<center>
    <h2>B&uacute;squeda Bur&oacute; Interno</h2> 
<form name="forma" action="controller" method="post">
<input type="hidden" name="idCliente" value="">
<input type="hidden" name="idSolicitud" value="">


	<table border="0" width="100%" callpadding="0" cellspacing="0"> 
	<tr>	
	<td width="50%" align="right">N&uacute;mero Cliente</td>
		<td width="50%">
	 <input type="text" name="idCte" size="13" maxlength="13" value="<%=HTMLHelper.displayField(request.getParameter("idCte"))%>" onKeyPress="return submitenter(this,event)">
	  	</td>
	</tr>
	<tr>
	  	<td width="50%" align="right">Apellido Paterno</td>
	  	<td width="50%">
		  	<input type="text" name="apellidoPaterno" size="25" maxlength="30" value="<%=HTMLHelper.displayField(request.getParameter("apellidoPaterno"))%>" onKeyPress="return submitenter(this,event)">
	  	</td>
	</tr>
	<tr>
	  	<td width="50%" align="right">Apellido Materno</td>
	  	<td width="50%">
		  	<input type="text" name="apellidoMaterno" size="25" maxlength="30" value="<%=HTMLHelper.displayField(request.getParameter("apellidoMaterno"))%>" onKeyPress="return submitenter(this,event)">
	  	</td>
	</tr>
	<tr>
	  	<td width="50%" align="right">Nombre(s)</td>
	  	<td width="50%">
		  	<input type="text" name="nombre" size="25" maxlength="30" value="<%=HTMLHelper.displayField(request.getParameter("nombre"))%>" onKeyPress="return submitenter(this,event)">
	  	</td>
	</tr>
	
	
     <tr>
		<td align="center" colspan="2">
			<br><input type="button" value="Buscar..." onClick="buscar()">
		</td>
	 </tr>
	 
	 <tr>
		<td>&nbsp;&nbsp;</td>
	</tr>
	 <tr>
		<td valign="top" colspan = "3" align="center">&nbsp;
			<a href="<%=request.getContextPath() %>">Inicio</a>
		</td>
	</tr>	
	
<%
if ( request.getParameter("command")!=null && request.getParameter("command").equals("buscaIncidenciaBuro") && clientes==null ){
%>
	
	
	  <tr>
		<td align="center" colspan="2">
			<br><input type="button" value="Ingresar Incidencia" onClick="nuevaIncidencia()">
		</td>
	 </tr>
<%}%>	
	
	<tr>
		<td align="center" colspan="2">
			<br><%=HTMLHelper.displayNotifications(notificaciones)%>
		</td>
	</tr>
		</table>
<%
if ( clientes!=null && clientes.length>0 ){
%>
	<table border="0" width="50%" cellspacing="3" cellpadding="0" >
		<tr bgcolor="#009865">
			<td width="10%" align="center" class="whitetext">N&uacute;mero</td>
			<td width="45%" align="center" class="whitetext">Nombre</td>
			<td width="20%" align="center" class="whitetext">Status</td>
			<td width="25%" align="center" class="whitetext">Motivo</td>
		</tr>
<%
	for ( int i=0 ; i<clientes.length ; i++ ){
		BuroInternoVO cliente = clientes[i];
%>

		<tr>
			<td width="10%" align="center"><a href="#" onClick="consultaCliente(<%=cliente.numCliente%>)"><%=cliente.numCliente%></a></td>
			<td width="45%" align="center"><%=cliente.nombre+" "+cliente.apaterno+" "+cliente.amaterno%></td>
			<td width="20%" align="center"><%=HTMLHelper.getDescripcion(catEstatus, cliente.estatus)%></td>
			<td width="25%" align="center"><%=HTMLHelper.getDescripcion(catMotivo, cliente.motivoIngreso)%></td>
		</tr>

<%		
	}
%>
	</table>
<%
}
%>
<input type="hidden" name="command" value="buscaIncidenciaBuro">
</form>
</center>

<jsp:include page="footer.jsp" flush="true"/></body>
</html>