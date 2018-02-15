<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>

<%
Notification notificaciones[] = (Notification[])request.getAttribute("NOTIFICACIONES");
ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
DisposicionVO disposicion = (DisposicionVO)request.getAttribute("DISPOSICION");
if ( disposicion==null )
	disposicion = new DisposicionVO();
%>

<html>
<head>
<title>Disposiciones</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function esDisposcionValido(monto){
		
		if ( monto!='' && esFormatoMoneda(monto) && !esNegativo(monto) ){
			if(monto<800 || monto>5000)
				return false;
			return true;
		}else{
			return false;
		}
	}


	function guardaDisposiciones(){
		if ( window.document.forma.monto.value!='' ){
			if ( !esDisposcionValido(window.document.forma.monto.value) ){
				alert("El monto es inv\u00e1lido");
				return false;
			}
		}else{
			alert("Debe capturar el monto");
			return false;
		}
		window.document.forma.submit();
	}


	function muestraPagareMaxzapatos(){
		params ="?command=muestraPagareMaxzapatos&idDisposicion="+<%=disposicion.idDisposicion%>+"&idSolicitud="+<%=idSolicitud%>;
		url = "/CEC/controller";
		abreVentana(url + params,'scrollbars=yes,titlebar=no',400 ,500 ,true, 0, 0);
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css"></head>


<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<CENTER>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="guardaDisposiciones">
<input type="hidden" name="idCliente" value="<%=cliente.idCliente%>">
<input type="hidden" name="idSolicitud" value="<%=solicitud.idSolicitud%>">
<input type="hidden" name="idDisposicion" value="<%=disposicion.idDisposicion%>">

<table border="0" width="100%">
	<tr>
		<td align="center">
<h3>Disposiciones</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table border="0" cellpadding="0" width="100%">
	<tr>
  	<td width="50%" align="right">Monto de la disposición</td>
  	<td width="50%" valign="top">  
  	<input type="text" name="monto" size="10" maxlength="12" value="<%=HTMLHelper.formatoMonto(disposicion.monto)%>">
<%if ( disposicion.tablaAmostizaciones!=null ) {%>
  	&nbsp;&nbsp;<a href="#" onclick="muestraPagareMaxzapatos()">Ver pagaré</a>
<%}%>
  	</td>
	</tr>
	<tr>
  	<td colspan="2" align="center">&nbsp;</td>
	</tr>
	<tr>
		<td align="center" colspan="2">
			<br><input type="button" value="Enviar" onClick="guardaDisposiciones()">
		</td>
	</tr>
</table> 

<!-- INICIO NUEVO CODIGO -->
		</td>
	</tr>
</table>
<!-- FIN NUEVO CODIGO -->

<br>
</form>
</CENTER>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>