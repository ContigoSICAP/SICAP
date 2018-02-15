<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.SolicitudVO"%>
<%@ page import="com.sicap.clientes.vo.ObligadoSolidarioVO"%>
<%@ page import="com.sicap.clientes.vo.EconomiaObligadoVO"%>
<html>
<head>
<title>Obligado Solidario</title>
<script language="Javascript" src="./js/functions.js"></script>

<script type="text/javascript">
<!--
	function guardaEconomiaObligadoSolidario(){
	
		if ( window.document.forma.ocupacion.value==0 ){
			alert('Debe seleccionar la ocupación');
			return false;
	    }
		if ( window.document.forma.frecuenciaIngresos.value==0 ){
			alert('Debe seleccionar la frecuencia de ingresos');
			return false;
	    }
		if ( window.document.forma.empresa.value==''){
		   alert('Debe introducir el nombre de la empresa');
		   return false;
		}
		if ( window.document.forma.tipoContrato.value==0 ){
			alert('Debe seleccionar el tipo de contrato');
			return false;
	    }
		if ( window.document.forma.salario.value=='' || !esFormatoMoneda(window.document.forma.salario.value) ){
		   alert('Debe introducir el salario');
		   return false;
		}
		//if ( window.document.forma.pasivosFamiliares.value=='' || !esEntero(window.document.forma.pasivosFamiliares.value)){
		   //alert('Debe introducir los pasivos familiares');
		   //return false;
		//}
		//if ( window.document.forma.activosFamiliares.value=='' || !esEntero(window.document.forma.activosFamiliares.value)){
		   //alert('Debe introducir los activos familiares');
		   //return false;
		//}
		//if ( window.document.forma.ingresosFamiliares.value=='' || !esEntero(window.document.forma.ingresosFamiliares.value)){
		   //alert('Debe introducir los ingresos familiares');
		   //return false;
		//}
		//if ( window.document.forma.gastosFamiliares.value=='' || !esEntero(window.document.forma.gastosFamiliares.value)){
		   //alert('Debe introducir los gastos familiares');
		   //return false;
		//}
		window.document.forma.submit();
	}

//-->
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css"></head>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<center>
<%
SolicitudVO solicitud = new SolicitudVO();
ObligadoSolidarioVO obligado = new ObligadoSolidarioVO();
EconomiaObligadoVO economia = new EconomiaObligadoVO();
ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
int idObligado = HTMLHelper.getParameterInt(request, "idObligado");
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
TreeMap catOcupaciones = CatalogoHelper.getCatalogo(ClientesConstants.CAT_OCUPACIONES);
TreeMap catFrecuenciaIngresos = CatalogoHelper.getCatalogo(ClientesConstants.CAT_FRECUENCIA_INGRESOS);
TreeMap catTiposContratos = CatalogoHelper.getCatalogo(ClientesConstants.CAT_CONTRATOS);
if ( idSolicitud!=0 ){
	solicitud = cliente.solicitudes[indiceSolicitud];
	if ( idObligado!=0 ){
		obligado = solicitud.obligadosSolidarios[idObligado-1];
		if ( obligado.economia!=null )
			economia = obligado.economia;
	}
}
%>

<h3>Obligado Solidario</h3>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="guardaEconomiaObligadoSolidario">
<input type="hidden" name="idCliente" value="<%=cliente.idCliente%>">
<input type="hidden" name="idSolicitud" value="<%=idSolicitud%>">
<input type="hidden" name="idObligado" value="<%=idObligado%>">

<table width="100%" border="0" cellpadding="0">
  <tr>
    <td width="50%" align="right">Ocupaci&oacute;n</td>
    <td width="50%">
      <select name="ocupacion" size="1">
        <%=HTMLHelper.displayCombo(catOcupaciones, economia.ocupacion)%>
      </select>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Frecuencia de ingresos</td>
    <td width="50%">
      <select name="frecuenciaIngresos" size="1">
        <%=HTMLHelper.displayCombo(catFrecuenciaIngresos, economia.frecuenciaIngresos)%>
      </select>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Empresa donde trabaja </td>
    <td width="50%">
      <input type="text" name="empresa" size="45" maxlength="60" value="<%=HTMLHelper.displayField(economia.empresa)%>">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Tipo de contrato</td>
    <td width="50%">
      <select name="tipoContrato" size="1">
        <%=HTMLHelper.displayCombo(catTiposContratos, economia.tipoContrato)%>
      </select>
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Salario - $</td>
    <td width="50%">
      <input type="text" name="salario" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(economia.salario)%>">
       </td>
  </tr>
  <tr>
    <td width="50%" align="right">Pasivos familiares - $</td>
    <td width="50%">
      <input type="text" name="pasivosFamiliares" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(economia.pasivosFamiliares)%>">
       </td>
  </tr>
  <tr>
    <td width="50%" align="right">Activos familiares - $</td>
    <td width="50%">
      <input type="text" name="activosFamiliares" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(economia.activosFamiliares)%>">
       </td>
  </tr>
  <tr>
    <td width="50%" align="right">Ingresos familiares - $</td>
    <td width="50%">
      <input type="text" name="ingresosFamiliares" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(economia.ingresosFamiliares)%>">
       </td>
  </tr>
  <tr>
    <td width="50%" align="right">Gastos familiares - $</td>
    <td width="50%">
      <input type="text" name="gastosFamiliares" size="10" maxlength="10" value="<%=HTMLHelper.formatoMonto(economia.gastosFamiliares)%>">
       </td>
  </tr>
  <tr>
  	<%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
		<td align="center" colspan="2">
			<br><input disabled type="button" value="Enviar" onClick="guardaEconomiaObligadoSolidario()">
		</td>
    <%}else{ %>
    	<td align="center" colspan="2">
			<br><input type="button" value="Enviar" onClick="guardaEconomiaObligadoSolidario()">
		</td>
    <%}%>
	</tr>
</table>
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>
