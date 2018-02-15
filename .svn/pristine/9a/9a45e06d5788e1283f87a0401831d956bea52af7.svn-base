<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<html>
<head>
<title>Ordenes de Pago</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function generaOrdenDePago( totalOrdenes ){
		var importe = sumaImportes(totalOrdenes);
		if(window.confirm("¿ Confirma que deseas Crear un Archivo de Dispersión por un Importe de $ "+importe +'?')){
			window.document.forma.command.value='generaOrdenDePago';
			window.document.forma.totalOrdenes.value= totalOrdenes;
			document.getElementById("getFile").disabled = true;		
			window.document.forma.submit();
		}
	}
	
	function calculaImporteArchivo(totalOrdenes){
		var importe = sumaImportes(totalOrdenes);
		importe.toFixed(2);
		document.getElementById("divMessage").innerHTML = '$  '+ importe;
		if( importe > 0 ){
			document.getElementById("getFile").disabled = false;
		}else{
			document.getElementById("getFile").disabled = true;
		}		
	}
	
	function sumaImportes(totalOrdenes){
		var importe = 0;
		var aux     = 0;
		for ( var i=0; i < totalOrdenes ; i++ ){
			var nombreDinamico  = "ordenpago"+i;
			var importeDinamico = "importe"+i;
			var elemento = document.getElementById(nombreDinamico);
			
			if ( elemento.checked==true && elemento.disabled==false){				
				aux = parseFloat(document.getElementById(importeDinamico).value);
				importe = importe+aux;
			}
		}
		return importe;
	}
	function disableCheckBox( totalOrdenes ){
		for ( var i=0; i < totalOrdenes ; i++ ){
			var nombreDinamico  = "ordenpago"+i;
			document.getElementById(nombreDinamico).disabled = true;
		}
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>

<body leftmargin="0" topmargin="0" onload='calculaImporteArchivo()'>
<jsp:include page="header.jsp" flush="true"/>
<center>
    <h2>Administraci&oacute;n de Ordenes de Pago SICAP</h2>     
<form name="forma" action="admin" method="post">
<input type="hidden" name="command" value="">
<input type="hidden" name="totalOrdenes" value="">
<% 
double montoCancelaciones = 0;
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
OrdenDePagoVO ordenesPagos[]  = (OrdenDePagoVO[])request.getAttribute("ORDENESPAGO");
%>
<table border="1" width="90%" align="center" >
<tr>
	<td align="center">
		
	</td>
	<td align="center">
		Cliente
	</td>
	<td align="center"> 
		Solicitud
	</td>
	<td align="center"> 
		Sucursal
	</td>
	<td align="center"> 
		Producto
	</td>
	<td align="center"> 
		Grupo
	</td>
	<td align="center"> 
		Solicitante
	</td>
	<td align="center">
		Importe
	</td>
	<td align="center">
		Fecha Desembolso
	</td>				
	<td align="center">
		Referencia
	</td>
	<td align="center">
		Estatus
	</td>
</tr>

<%for ( int i=0 ; ordenesPagos!=null && i<ordenesPagos.length ; i++ ){ 
String bckground = "#FFFFFF";
String font      = "text";
	
	switch( ordenesPagos[i].getEstatus() ){
		
		case( ClientesConstants.OP_SOLICITA_CANCELACION ):
			bckground="##009865"; 
			font="whitetext";
			montoCancelaciones += ordenesPagos[i].getMonto();
	%>		<input type="hidden" name="idOrdenPagoCancelada<%=i%>" value="<%=ordenesPagos[i].getIdOrdenPago()%>"><%
	 		break;
			
		case( ClientesConstants.OP_ACTUALIZA_NOMBRE ):
			bckground="##009865"; 
			font="whitetext";
	%>		<input type="hidden" name="idOrdenModificada<%=i%>" value="<%=ordenesPagos[i].getIdOrdenPago()%>"><%
			break;
	}
	%>
<tr bgcolor="<%=bckground%>" class="<%=font%>">
	<td align="center"> 
		<% 
			boolean isChecked  = true;
			if( ordenesPagos[i].getEstatus() == 1 ){isChecked=false;}
		%>
		<%=HTMLHelper.displayCheckonClick("ordenpago"+i,isChecked, isChecked, "calculaImporteArchivo("+ordenesPagos.length+")")%>
	</td>
	<td align="center"> 
		<%=ordenesPagos[i].getIdCliente()%>
	</td>
	<td align="center"> 
		<%=ordenesPagos[i].getIdSolicitud()%>
	</td>
	<td align="center"> 
		<%=ordenesPagos[i].getNomSucursal()%>
	</td>
	<td align="center"> 
		<%=ordenesPagos[i].getProducto()%>
	</td>
	<td align="center"> 
		<%=ordenesPagos[i].getGrupo()%>
	</td>
	<td align="center"> 
		<%=ordenesPagos[i].getNombre()%>
	</td>
		<td align="center"> 
		<input type="hidden" id="idOrdenPago" name="idOrdenPago<%=i%>" value="<%=ordenesPagos[i].getIdOrdenPago()%>">
		<input type="hidden" id="importe<%=i%>" value="<%=ordenesPagos[i].getMonto()%>">
		<%=ordenesPagos[i].getMonto()%>
	</td>
		<td align="center">
		<%=ordenesPagos[i].getFechaCaptura()%>
	</td>
		<td align="center"> 		
		<%=ordenesPagos[i].getReferencia()%>
	</td>
		<td align="center"> 
		<%= ordenesPagos[i].getDescEstatus()%>
	</td>
</tr>

<%}%>
</table>

<table width="90%" border="0" cellspacing="2" class="maintitle">
	<tr>
		<td width="87%" align="right"> Importe Dispersiones</td>
  		<td width="13%" align="center" bgcolor="#FB9A33">
  			<div id="divMessage" />
		</td>
	</tr>
	<tr>
		<td width="87%" align="right"> Importe Cancelaciones</td>
  		<td width="13%" align="center" bgcolor="#FB9A33">
  			<%="$ "+montoCancelaciones %>
		</td>
	</tr>
	<tr>
		<td height="46" colspan="2" align="right"><label>
		  <input name="getFile" type="button" class="text" id="getFile" onClick="generaOrdenDePago(<%=ordenesPagos.length%>)" value="Genera Archivo">
	    </label></td>
     </tr>
</table>
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>