<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.TreeMap"%>
<%

Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
String nombre = "";
CicloGrupalVO ciclo = null;
TreeMap motivosEliminacion = CatalogoHelper.getCatalogoMotivosEliminacionAlerta();
int idAlerta = -1;
GrupoVO grupo = new GrupoVO();
boolean sessionActivaFlag = false;
if ( session.getAttribute("GRUPO")!=null ){
		grupo = (GrupoVO)session.getAttribute("GRUPO");
	}
if ( request.getAttribute("CICLO_EVENTOS_PAGO")!=null ){
	ciclo = (CicloGrupalVO)request.getAttribute("CICLO_EVENTOS_PAGO");
	nombre = (String)request.getAttribute("NOMBRE");	
}
if ( request.getAttribute("IDALERTA")!=null )
	idAlerta = (Integer)request.getAttribute("IDALERTA");	

if( nombre==null || nombre=="" ){
	nombre = grupo.nombre;
	//sessionActivaFlag = false;
}

sessionActivaFlag = HTMLHelper.esUsuarioDF(usuario.sucursales);
boolean adminAlertas = request.isUserInRole("ADMINISTRADOR_ALERTAS");
%>
<html>
<head>
<script language="Javascript" src="./js/functions.js"></script>
<script>
	function consultaInformeCobranzaGrupal(numeroAlerta, idAlerta){
		window.document.forma.command.value = "consultaInformeCobranzaGrupal";
		window.document.forma.numeroAlerta.value = numeroAlerta;
		window.document.forma.idAlerta.value = idAlerta;
		window.document.forma.submit();
	}
	function consultaInformeCobranza(idCliente, idAlerta, opcion){
		window.document.forma.command.value = "consultaInformeCobranza";
		window.document.forma.tipoOperacionCliente.value = opcion;
		window.document.forma.idCliente.value = idCliente;
		window.document.forma.idAlerta.value = idAlerta;
		window.document.forma.submit();
	}
		
	function consultaInformeVisita(numeroAlerta, idAlerta, opcion){
		window.document.forma.command.value = "consultaInformeVisita";
		window.document.forma.tipoOperacionCliente.value = opcion;
		window.document.forma.numeroAlerta.value = numeroAlerta;
		window.document.forma.idAlerta.value = idAlerta;
		window.document.forma.submit();
	}
	
	function eliminaAlerta(numeroAlerta){
		var nombreDinamico = "motivoEliminar"+numeroAlerta;
		var motivo = document.getElementById(nombreDinamico);
		
		window.document.forma.command.value = "eliminaAlerta";
		window.document.forma.numeroAlerta.value = numeroAlerta;
		
		if( motivo.value == 0 ){
			alert('Debe seleccionar un motivo');
			return false;
		}
		var comentario = prompt("Ingrese un comentario","");
		if(comentario==null || comentario=="" ){
			alert('Debe agregar un comentario');
			return false;
		}
		window.document.forma.comentarioEliminacion.value = comentario;
		res = confirm('Se eliminara los archivos y reportes de esta alerta. ¿Esta seguro de que desea eliminar la alerta grupal? ');
    	if ( !res ) return res;
		window.document.forma.submit();
		
	}
	
	function consultaDetalleGrupo(){
		window.document.forma.command.value = 'consultaGrupo';
		window.document.forma.submit();
	}
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuInicio.jsp" flush="true"/>
<center>
<table border="0" width="100%">
	<tr>
	<td align="center">

<form name="forma" method="post" action="controller">
<input type="hidden" name="command" value="">
<input type="hidden" name="comentarioEliminacion" value="">
<input type="hidden" name="idGrupo" value="<%=ciclo.idGrupo%>">
<input type="hidden" name="idCiclo" value="<%=ciclo.idCiclo%>">
<input type="hidden" name="idCliente" value="">
<input type="hidden" name="tipoOperacionCliente" value="">
<input type="hidden" name="idAlerta" value="">
<input type="hidden" name="numeroAlerta" value="">
<input type="hidden" name="nombreGrupo" value="<%=nombre%>">

<h3>Historico de atrasos y visitas</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
			<table>
				<tr>
					<td  align="center"><%=ciclo.idGrupo%></td>
					<td  align="center"><%=nombre%></td>
				</tr>
			<table border="1">
				<tr>
					<td colspan="1" align="center"><b>Ciclo</b></td>
					<td colspan="1" align="center"><b>No.Pago</b></td>
					<td colspan="1" align="center"><b>Atraso</b></td>
					<td colspan="1" align="center"><b>Visita</b></td>
					<td colspan="1" align="center"><b>Estatus alerta</b></td>
					<td colspan="1" align="center"><b>Fecha ultimo pago</b></td>
					<td colspan="1" align="center"><b>Saldo vencido</b></td>
					<td colspan="1" align="center"><b>Dias de mora</b></td>
					<td colspan="1" align="center"><b>Ejecutivo</b></td>
					<td colspan="1" align="center"><b>Informe de visita</b></td>
					<% if(sessionActivaFlag ) {%>
					<td colspan="1" align="center"><b>Reporte de cobranza</b></td>
					<% }%>
					<% if(adminAlertas) {%>
					<td colspan="1" align="center"><b>Elimina Alerta</b></td>
					<% }%>
				</tr>
<%
if ( ciclo.eventosDePago!=null ){	 

			for ( int i=0 ; i < ciclo.eventosDePago.length ; i++ ){
%>				
				<tr>
					<td align="center"><%=ciclo.eventosDePago[i].numCiclo%></td>
					<td align="center"><%=ciclo.eventosDePago[i].numPago%></td>
					<td align="center"><%=ciclo.eventosDePago[i].numAtrasos%></td>
					<td align="center"><%if( ciclo.eventosDePago[i].estatusVisitaGerente!=0 ){%>Gerente<%}else if(ciclo.eventosDePago[i].estatusVisitaSupervisor!=0){%>Cobrador<%}else if(ciclo.eventosDePago[i].estatusVisitaGestor!=0){%>Gestor<%}else{%>No aplica<%}%></td>
					<td colspan="1" align="center"><%=HTMLHelper.obtieneEstatusAlerta(ciclo.eventosDePago[i])%>&nbsp;</td>
					<td align="center"><%=HTMLHelper.displayField(ciclo.fechaUltimoPago)%>&nbsp;</td>
					<td align="center"><%=HTMLHelper.formatCantidad(ciclo.saldo.getTotalVencido(),false)%>&nbsp;</td>
					<td align="center"><%=HTMLHelper.displayField(ciclo.saldo.getDiasMora(), true)%></td>
					<td align="center"><%=CatalogoHelper.getNombreEjecutivo(grupo.sucursal, ciclo)%></td>
					<td align="center"><%=HTMLHelper.opcionesInformeVisitaGrupal(ciclo, i, 0)%></td>
					<% if(sessionActivaFlag ) {%>
					<td align="center"><%=HTMLHelper.opcionesInformeCobranza(ciclo.eventosDePago[i], i)%></td>
					<% }%>
					<% if(adminAlertas) {%>
					<td align="center">
					<select name="motivoEliminar<%=ciclo.eventosDePago[i].identificador%>" id="motivoEliminar<%=ciclo.eventosDePago[i].identificador%>" size="1" value="0" >
							<%=HTMLHelper.displayCombo(motivosEliminacion, 0)%>
					</select>
					<a href="#" onClick="eliminaAlerta(<%=ciclo.eventosDePago[i].identificador%>)">Eliminar</a></td>
					<% }%>
				</tr>
			<%if(ciclo.eventosDePago[i].estatusReporteCobranza==1 && idAlerta == -1)
					idAlerta = i;
			}}%>
			</table>

			<% if(sessionActivaFlag && idAlerta>-1 ) {%>
			<tr>
				<td align="center"><h4>Sección de seguimiento call center</h4></td>
			</tr>
				<table border="1">
					<tr>
						<td colspan="1" align="center"><b>Integrante</b></td>
						<td colspan="1" align="center"><b>Nombre</b></td>
						<td colspan="1" align="center"><b>Rol</b></td>
						<td colspan="1" align="center"><b>Acciones</b></td>
					</tr>
					<% for(int i=0 ; ciclo.integrantes!=null && i<ciclo.integrantes.length ; i++){%>
						<tr>
							<td align="center"><%=ciclo.integrantes[i].idCliente%></td>
							<td align="center"><%=ciclo.integrantes[i].nombre%></td>
							<td align="center"><%=HTMLHelper.printRol(ciclo.integrantes[i].rol)%></td>
							<td align="center"><%=HTMLHelper.opcionesRegistroAlertaCC(ciclo, ciclo.integrantes[i].idCliente, idAlerta, 0)%></td>
						</tr>
					<%}%>
				</table>
			<%}%>
			<br><br>
			<tr>
				<td colspan="1" align="center">
					<input type="button" onclick="consultaDetalleGrupo()" value="Regresar al grupo">
				</td>
			</tr>
		</table>
	</form>
  </td>
 </tr>
</table>		
</center>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>