<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>

<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>
<jsp:directive.page import="com.sicap.clientes.dao.CicloGrupalDAO"/>
<jsp:directive.page import="com.sicap.clientes.dao.IntegranteCicloDAO"/>

<%
	UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
	TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
	TreeMap catMotivosCancelacion = CatalogoHelper.getCatalogo(ClientesConstants.CAT_CANCELACION_CHEQUE);
	ChequeVO cheque = (ChequeVO)request.getAttribute("CHEQUE");
	ClienteVO cliente = (ClienteVO)request.getAttribute("CLIENTE");

	DecisionComiteVO decision = (DecisionComiteVO)request.getAttribute("DECISION");
	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
	int idSucursal = 0;
	int indiceSolicitud = 0;
	String nombreCliente = "";
	Double monto = 0.0;
	int tipoOperacion = 0;
	CicloGrupalVO ciclo = null;
	
	if ( cliente != null){
		idSucursal = cliente.idSucursal;
		nombreCliente = cliente.nombreCompleto;
		monto = decision.montoAutorizado;
		indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, cheque.numSolicitud);
		if ( cliente.solicitudes != null )
			tipoOperacion = cliente.solicitudes[indiceSolicitud].tipoOperacion;
		if( tipoOperacion==ClientesConstants.GRUPAL ){
			IntegranteCicloVO integrante = null;		
			integrante = new IntegranteCicloDAO().getIntegranteCiclo(cliente.solicitudes[indiceSolicitud].idCliente, cliente.solicitudes[indiceSolicitud].idSolicitud);
			ciclo = new CicloGrupalDAO().getCiclo(integrante.idGrupo, integrante.idCiclo);
		}
	}
	
	if (cheque == null)
		cheque = new ChequeVO();
		
%>
<html>
<head>
<title>Captura de cheques</title>
<script language="Javascript" src="./js/functions.js"></script>
<script type="text/javascript">
<!--
	
	function consultaCheque(){
		if ( window.document.forma.sucursal.value==0){
			alert('Debe seleccionar una sucursal');
			return false;
		}
		
		if ( window.document.forma.numcheque.value==''){
			alert('Ingrese un número de cheque');
			return false;
		}

		if ( window.document.forma.idCliente.value==''){
			alert('Ingrese un número de cliente');
			return false;
		}
		
		window.document.forma.command.value='consultaCheque';
		window.document.forma.submit();
		
	}

	function cancelaCheque(){
	   	if ( window.document.forma.motivo.value==0 ){
			alert('Debe seleccionar un motivo para cancelar el cheque');
			return false;
		}

    	var i
    	for (i=0;i<window.document.forma.cancel.length;i++){
       	if (window.document.forma.cancel[i].checked)
          	break;
    	}
    	
    	var cancel =  window.document.forma.cancel[i].value

		if ( cancel == 'desiste' ){
			if ( window.document.forma.motivo.value!=10 && window.document.forma.motivo.value!=11 ){
				alert('Debe seleccionar un motivo válido para cancelar definitivamente el cheque');
				return false;
			}
		}
	   	
	   	res = confirm('Está seguro que desea cancelar el cheque actual ?');
		if ( !res ) return res;
		
		var comando = 'cancelaCheque';
    	<%if( ciclo!=null && ciclo.desembolsado==ClientesConstants.CICLO_DESEMBOLSO_CONFIRMADO ){%>
    		if( cancel == 'desiste'){
    			comando = 'cancelaChequeConfirmado';
    		}
    	<%}%>
		window.document.forma.command.value = comando;
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
<input type="hidden" name="cheque" value="<%=HTMLHelper.displayField(cheque.numCheque)%>">
<input type="hidden" name="lote" value="<%=HTMLHelper.displayField(cheque.numLote)%>">
<input type="hidden" name="cliente" value="<%=HTMLHelper.displayField(cheque.numCliente)%>">
<input type="hidden" name="solicitud" value="<%=HTMLHelper.displayField(cheque.numSolicitud) %>">
<input type="hidden" name="grupo" value="<%=HTMLHelper.displayField(cheque.numGrupo) %>">
<input type="hidden" name="ciclo" value="<%=HTMLHelper.displayField(cheque.numCiclo) %>">
<input type="hidden" name="fecha" value="<%=HTMLHelper.displayField(cheque.fechaAsignacion) %>">
<input type="hidden" name="idSucursal" value="<%=HTMLHelper.displayField(idSucursal) %>">
<input type="hidden" name="tipooperacion" value="<%=HTMLHelper.displayField(tipoOperacion) %>">

<table border="0" width="100%">
	<tr>
		<td align="center">
<h3>Modificaci&oacute;n estatus de cheque<br></h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table border="0" cellpadding="0" width="100%" align="center">
<%if ( cliente == null) {%>
  <tr>
    <td width="50%" align="right">Sucursal</td>
    <td width="50%"><select name="sucursal"><%=HTMLHelper.displayCombo(catSucursales,0)%></select></td>
  </tr>
  <tr>
    <td width="50%" align="right">No. Cheque</td>
    <td width="50%">
        <input type="text" name="numcheque" size="7" maxlength="7" value="" />
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">No. Cliente</td>
    <td width="50%">
        <input type="text" name="idCliente" size="7" maxlength="7" value="" />
    </td>
  </tr>
  <tr>
  	<td colspan="2" align="center">
  		<br>
  		<input type="button" name="" value="Consultar" onClick="consultaCheque();" >
  		<br><br>
  	</td>
  </tr>
<%} 
if ( cliente!=null ){
%>
<tr>
    <td width="50%" align="right">Motivo Cancelación</td>
    <td width="50%"><select name="motivo"><%=HTMLHelper.displayCombo(catMotivosCancelacion,0)%></select></td>
</tr>
<tr>
    <td width="50%" align="right">Comentarios</td>
    <td width="50%"><textarea name="comentarios" cols="35" id="comentarios"></textarea></td>
</tr>

<tr>
  	<td colspan="2" align="center"><br><INPUT TYPE="RADIO" NAME="cancel" VALUE="sustitucion" checked>Por sustitución de cheque<INPUT TYPE="RADIO" NAME="cancel" VALUE="desiste">Cancelaci&oacute;n definitiva de cheque</td>
</tr>

<tr>
 	<td colspan="2" align="center"><br>
  		<input type="button" name="" value="Cancelar Cheque" onClick="cancelaCheque();" >
  	</td>
</tr>
<%} %>
  <tr>
	<td valign="top" colspan = "3" align="center">&nbsp;
		<br><a href="<%=request.getContextPath() %>">Inicio</a>
	</td>
  </tr>
</table>
<%
if ( cliente!=null ){
%>
<table border="0" cellpadding="0" >
<tr bgcolor="#009865" class="whitetext">
  	<td align="center">No.Cheque</td>
  	<td align="center">No.Lote</td>
  	<td align="center">No.Cliente</td>
  	<td align="center">No.Solicitud</td>
  	<td align="center">Nombre</td>  	
  	<td align="center">Monto</td>  	  	
</tr>

<tr>
	<td align="center"><%=HTMLHelper.displayField(cheque.numCheque) %></td>
	<td align="center"><%=HTMLHelper.displayField(cheque.numLote) %></td>
	<td align="center"><%=HTMLHelper.displayField(cheque.numCliente) %></td>
	<td align="center"><%=HTMLHelper.displayField(cheque.numSolicitud) %></td>
	<td align="center"><%=HTMLHelper.displayField(nombreCliente) %></td>
	<td align="center"><%=HTMLHelper.formatoMonto(monto) %></td>
</tr>
</table>
<%}%>
		</td>
	</tr>

</table>


</form>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>
