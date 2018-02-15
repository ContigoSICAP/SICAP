<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>

<%
	int numeroReferencias = 0;
	int total = 0;
	ReferenciaCrediticiaVO referencias[] = null;
	ReferenciaCrediticiaVO referencia = new ReferenciaCrediticiaVO();
	ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
	int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
	int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
	SolicitudVO solicitud = new SolicitudVO();
	if ( cliente.solicitudes!=null && cliente.solicitudes.length>0 ){
		solicitud = cliente.solicitudes[indiceSolicitud];
		if ( solicitud.referenciasCrediticias!=null ){
			referencias = solicitud.referenciasCrediticias;
			numeroReferencias = referencias.length;
			for (int i=0 ; i<numeroReferencias ; i++ ){
				total += referencias[i].saldo;
			}
		}
	}

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>   
    <title>Captura Referencias Crediticias</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function guardaReferenciasCrediticias(){
		window.document.forma.command.value='guardaReferenciasCrediticias';

		if ( !referenciasCorrectas() ){
			
			return false;
		}

		window.document.forma.submit();
	}

	function referenciasCorrectas(){
		var i=0;
		var numReferencias = 0;
		for ( i ; i<3 ; i++ ){
			if ( validarReferencia(i) ){
				if ( window.document.forma.institucion[i].value=='' ){
					alert("Debe capturar el nombre de la Institución para la referencia "+(i+1));
					return false;
				}
				if ( window.document.forma.numCredito[i].value=='' ){
					alert("Debe capturar el Número de crédito o tarjeta para la referencia "+(i+1));
					return false;
				}
				if ( window.document.forma.plazo[i].value=='' ){
					alert("Debe capturar el Plazo para la referencia "+(i+1));
					return false;
				}
				if ( window.document.forma.saldo[i].value==''  ){
					alert("Debe capturar el Saldo para la referencia "+(i+1));
					return false;
				}else if ( !esFormatoMoneda(window.document.forma.saldo[i].value) ){
					alert("El Saldo para la referencia "+(i+1)+" es incorrecto");
					return false;
				}
				if ( window.document.forma.frecuenciaPago[i].value==''){
					alert("Debe capturar la Frecuencia de pago para la referencia "+(i+1));
					return false;
				}
				numReferencias += 1;				
			}
		}
		if ( numReferencias==0 ){
			alert('La información es invalida');
			return false;
		}
		return true;
	}

	function validarReferencia(indice){
		if (    window.document.forma.institucion[indice].value!='' || 
		        window.document.forma.numCredito[indice].value!='' || 
                forma.plazo[indice].value!='' || 
                forma.saldo[indice].value!='' || 
                forma.frecuenciaPago[indice].value!='' ){
			return true;
		}
		return false;
	}

	function saldosCorrectos(){
		var i = 0;
		var total = 0;
		for ( i ; i<4 ; i++ ){
			if ( window.document.forma.saldo[i].value!='' ){
                total = total + parseInt( window.document.forma.saldo[i].value );
			}
		}
		window.document.forma.porcentajeTotal.value = total;
		if ( total==100 ){
		    return true;
        }
        alert("La suma de los porcentajes debe ser igual a 100");
        return false;
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
    
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<center>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="">
<input type="hidden" name="idCliente" value="<%=cliente.idCliente%>">
<input type="hidden" name="idSolicitud" value="<%=solicitud.idSolicitud%>">
<!-- INICIO DEL CODIGO ANTERIOR -->
<table border="0" width="100%">
	<tr>
	<td align="center">	   

<h3>Referencias crediticias</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table border="0" cellpadding="0" width="100%">
<%
if ( numeroReferencias>0 )
	referencia = referencias[0];
else
	referencia = new ReferenciaCrediticiaVO();
%>
	<tr>
  	<td width="50%" align="center" colspan="2">Referencia 1</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Institici&oacute;n</td>
  	<td width="50%">  
  	<input type="text" name="institucion" size="45" maxlength="100" value="<%=HTMLHelper.displayField(referencia.institucion)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">N&uacute;mero de cr&eacute;dito o tarjeta</td>
  	<td width="50%">  
  	<input type="text" name="numCredito" size="30" maxlength="30" value="<%=HTMLHelper.displayField(referencia.numCredito)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Plazo</td>
  	<td width="50%">  
  	<input type="text" name="plazo" size="30" maxlength="30" value="<%=HTMLHelper.displayField(referencia.plazo)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Saldo</td>
  	<td width="50%">  
  	<input type="text" name="saldo" size="12" maxlength="9" value="<%=HTMLHelper.displayField(referencia.saldo)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Frecuencia de pago</td>
  	<td width="50%">  
  	<input type="text" name="frecuenciaPago" size="30" maxlength="45" value="<%=HTMLHelper.displayField(referencia.frecuenciaPago)%>">
  	</td>
	</tr>
<%
if ( numeroReferencias>1 )
	referencia = referencias[1];
else
	referencia = new ReferenciaCrediticiaVO();
%>
	<tr>
  	<td width="50%" align="center" colspan="2">Referencia 2</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Institici&oacute;n</td>
  	<td width="50%">  
  	<input type="text" name="institucion" size="45" maxlength="100" value="<%=HTMLHelper.displayField(referencia.institucion)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">N&uacute;mero de cr&eacute;dito o tarjeta</td>
  	<td width="50%">  
  	<input type="text" name="numCredito" size="30" maxlength="30" value="<%=HTMLHelper.displayField(referencia.numCredito)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Plazo</td>
  	<td width="50%">  
  	<input type="text" name="plazo" size="30" maxlength="30" value="<%=HTMLHelper.displayField(referencia.plazo)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Saldo</td>
  	<td width="50%">  
  	<input type="text" name="saldo" size="12" maxlength="9" value="<%=HTMLHelper.displayField(referencia.saldo)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Frecuencia de pago</td>
  	<td width="50%">  
  	<input type="text" name="frecuenciaPago" size="30" maxlength="45" value="<%=HTMLHelper.displayField(referencia.frecuenciaPago)%>">
  	</td>
	</tr>
<%
if ( numeroReferencias>2 )
	referencia = referencias[2];
else
	referencia = new ReferenciaCrediticiaVO();
%>
	<tr>
  	<td width="50%" align="center" colspan="2">Referencia 1</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Institici&oacute;n</td>
  	<td width="50%">  
  	<input type="text" name="institucion" size="45" maxlength="100" value="<%=HTMLHelper.displayField(referencia.institucion)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">N&uacute;mero de cr&eacute;dito o tarjeta</td>
  	<td width="50%">  
  	<input type="text" name="numCredito" size="30" maxlength="30" value="<%=HTMLHelper.displayField(referencia.numCredito)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Plazo</td>
  	<td width="50%">  
  	<input type="text" name="plazo" size="30" maxlength="30" value="<%=HTMLHelper.displayField(referencia.plazo)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Saldo</td>
  	<td width="50%">  
  	<input type="text" name="saldo" size="12" maxlength="9" value="<%=HTMLHelper.displayField(referencia.saldo)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Frecuencia de pago</td>
  	<td width="50%">  
  	<input type="text" name="frecuenciaPago" size="30" maxlength="45" value="<%=HTMLHelper.displayField(referencia.frecuenciaPago)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Total</td>
  	<td width="50%">  
  	<input type="text" name="total" size="5" maxlength="3" value="<%=HTMLHelper.displayField(total)%>" readonly="readonly">
  	</td>
	</tr>
	<tr>
	 <%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
		<td align="center" colspan="2">
			<br><input disabled type="button" value="Enviar" onClick="guardaReferenciasCrediticias()">
		</td>
	 <%}else{ %>
	 	<td align="center" colspan="2">
			<br><input type="button" value="Enviar" onClick="guardaReferenciasCrediticias()">
		</td>
	 <%} %>
	</tr>
</table>
	</td>
	</tr>
</table>
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>
