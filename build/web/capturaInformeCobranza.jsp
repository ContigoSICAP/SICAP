<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.TreeMap"%>
<%
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
TreeMap catRoles = CatalogoHelper.getCatalogoRolesGrupo(true);
GrupoVO grupo = new GrupoVO();
CicloGrupalVO ciclo = new CicloGrupalVO();
ReporteCobranzaGrupalVO reporteCobranza = new ReporteCobranzaGrupalVO();
ClienteVO cliente = new ClienteVO();
int operacion = 0;
int idAlerta = -1;
UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
//GRUPO
if ( session.getAttribute("GRUPO")!=null )
	grupo = (GrupoVO)session.getAttribute("GRUPO");
//CICLO
if ( request.getAttribute("CICLO_EVENTOS_PAGO")!=null )
	ciclo = (CicloGrupalVO)request.getAttribute("CICLO_EVENTOS_PAGO");
//Reporte Cobranza
if ( request.getAttribute("REPORTE_COBRANZA")!=null )
	reporteCobranza = (ReporteCobranzaGrupalVO)request.getAttribute("REPORTE_COBRANZA");
//Cliente completo
if ( request.getAttribute("CLIENTE")!=null )
	cliente = (ClienteVO)request.getAttribute("CLIENTE");
//OPERACION CAPTURA O CONSULTA CAHNCE Y SOBRE CHECAR ADELANTE OOOOOOOOOOOOOOOJJJJJJJJJJJJOOOOOOOO
if ( request.getAttribute("OPERACION")!=null )
	operacion = (Integer)request.getAttribute("OPERACION");
//ID DE LA OPERACION
if( request.getAttribute("IDALERTA")!=null )
	idAlerta = (Integer)request.getAttribute("IDALERTA");
	
	
boolean soloLecturaFlag = (operacion==1 ? true:false);
%>
<html>
<head>
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function guardaCuestionario(){
		window.document.forma.command.value = 'guardaCuestionario';
		if ( !esEntero(window.document.forma.P4.value) ){
			alert("Numero de faltas a juntas incorrecto");
			return false;
		}
		if ( window.document.forma.P9.value=='' || window.document.forma.P9.value==null){
			alert("Debe capturar comentarios");
			return false;
		}
		window.document.forma.submit();
	}
	
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuInicio.jsp" flush="true"/>
<center>
<form name="forma" method="post" action="controller">
<input type="hidden" name="command" value="">
<input type="hidden" name="idUnicoAlerta" value="<%=ciclo.eventosDePago[idAlerta].identificador%>">
<input type="hidden" name="numCliente" value="<%=cliente.idCliente%>">
<input type="hidden" name="nombreCliente" value="<%=cliente.nombreCompleto%>">
<input type="hidden" name="idGrupo" value="<%=ciclo.idGrupo%>">
<input type="hidden" name="idCiclo" value="<%=ciclo.idCiclo%>">
<input type="hidden" name="usuario" value="<%=usuario.nombre%>">
<input type="hidden" name="identificadorAlerta" value="<%=idAlerta%>">
<input type="hidden" name="indice" value="">
<input type="hidden" name="tipo" value="">
<input type="hidden" name="tipoOperacionCliente" value="">

<h3>Captura informe de llamada call center</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
			<tr><td  align="center"><b>Detalle del ciclo</b></td></tr>
			<table border="1">
				<tr>
					<td  colspan="1" align="center"><b>Num grupo</b></td>
					<td  colspan="1" align="center"><b>Nombre</b></td>
					<td  colspan="1" align="center"><b>Num ciclo</b></td>
					<td  colspan="1" align="center"><b>Num atrasos</b></td>
					<td  colspan="1" align="center"><b>Ejecutivo</b></td>
				</tr>
				<tr>
					<td  align="center"><%=ciclo.idGrupo%></td>
					<td  align="center"><%=grupo.nombre%></td>
					<td  align="center"><%=ciclo.idCiclo%></td>
					<td  align="center"><%=ciclo.eventosDePago[idAlerta].numAtrasos%></td>
					<td  align="center"><%=CatalogoHelper.getNombreEjecutivo(grupo.sucursal, ciclo)%></td>
				</tr>	
			</table>
			<br>
			<tr><td  align="center"><b>Detalle del cliente</b></td></tr>
			<table border="1">
				<tr>
					<td colspan="1" align="center"><b>Num Cliente</b></td>
					<td colspan="1" align="center"><b>Nombre</b></td>
					<td colspan="1" align="center"><b>RFC</b></td>
					<td colspan="1" align="center"><b>Sexo</b></td>
					<td colspan="1" align="center"><b>Direccion</b></td>
					<td colspan="1" align="center"><b>Telefonos</b><br>(Particular : Movil : Recados)</td>
				</tr>
				<tr>
					<td colspan="1" align="center"><%=cliente.idCliente%></td>
					<td colspan="1" align="center"><%=cliente.nombreCompleto%></td>
					<td colspan="1" align="center"><%=cliente.rfc%></td>
					<td colspan="1" align="center"><%if(cliente.sexo==1){%>F<%}else{%>M<%}%></td>
					<td colspan="1" align="center"><%=reporteCobranza.direccion%></td>
					<td colspan="1" align="center"><%=reporteCobranza.telefonos%></td>
				</tr>
			</table>
			<br>
			<table border="1">
				<tr>
					<td colspan="1" align="center"><b>Motivos de no contacto</b></td>
				</tr>
				<tr>
					<td colspan="1" align="center">
						No. incorrecto:<input type="radio" value="1"  name="R1" <%=(reporteCobranza.motivoNoContacto==1 ? "checked=\"checked\"":"")%> >&nbsp;
						Ya no vive ahí:<input type="radio" value="2"  name="R1" <%=(reporteCobranza.motivoNoContacto==2 ? "checked=\"checked\"":"")%> >&nbsp;
						Teléfono de sucursal:<input type="radio" value="3"  name="R1" <%=(reporteCobranza.motivoNoContacto==3 ? "checked=\"checked\"":"")%>>&nbsp;
						No lo conocen:<input type="radio" value="4"  name="R1" <%=(reporteCobranza.motivoNoContacto==4 ? "checked=\"checked\"":"")%>>&nbsp;
						Cuelgan:<input type="radio" value="5"  name="R1" <%=(reporteCobranza.motivoNoContacto==5 ? "checked=\"checked\"":"")%>>&nbsp;
						No. no existe:<input type="radio" value="6"  name="R1" <%=(reporteCobranza.motivoNoContacto==6 ? "checked=\"checked\"":"")%>>&nbsp;
						No. suspendido:<input type="radio" value="7"  name="R1" <%=(reporteCobranza.motivoNoContacto==7 ? "checked=\"checked\"":"")%>>&nbsp;
						Mal capturado:<input type="radio" value="8"  name="R1" <%=(reporteCobranza.motivoNoContacto==8 ? "checked=\"checked\"":"")%>>&nbsp;
						N/A:<input type="radio" value="0"  name="R1"  <%=(reporteCobranza.motivoNoContacto==0 ? "checked=\"checked\"":"")%>>						
					</td>
				</tr>
			</table>
			<br>
			<tr><td  align="center"><b>Seccion de preguntas (llenar correctamente)</b></td></tr>
			<table border="1">
				<tr>
					<td colspan="1" align="right">¿Ha dejado de realizar sus pagos?</td>
					<td colspan="1" align="left"><%=HTMLHelper.radioBoton("P1", reporteCobranza.realizaPagos, soloLecturaFlag)%></td>
				</tr>
				<tr>
					<td colspan="1" align="right">¿A quien le entrega sus pagos?<br>(indique nompre y puesto)</td>
					<td colspan="1" align="left">
					<select name="P2" id="P2" size="1" value="<%=reporteCobranza.receptorPagos%>" >
						<%=HTMLHelper.displayCombo(catRoles, reporteCobranza.receptorPagos)%>
					</select>
					 Otro: <input type="text" name="P2_1" size="40" value="<%=HTMLHelper.displayField(reporteCobranza.receptorPagosOtro)%>"></td>
				</tr>
				<tr>
					<td colspan="1" align="right">¿Su asesor acude cada semana a su junta de recuperación?</td>
					<td colspan="1" align="left"><%=HTMLHelper.radioBoton("P3", reporteCobranza.asesorVisitaSemanal, soloLecturaFlag)%></td>
				</tr>
				<tr>
					<td colspan="1" align="right">¿Cuantas veces su asesor ha faltado a las juntas de recuperación?</td>
					<td colspan="1" align="left"><input type="text" name="P4" value="<%=reporteCobranza.numerofaltas%>"></td>
				</tr>
				<tr>
					<td colspan="1" align="right">¿Su asesor acude a las juntas de recuperacion puntualmente?</td>
					<td colspan="1" align="left"><%=HTMLHelper.radioBoton("P5", reporteCobranza.asesorPuntual, soloLecturaFlag)%></td>
				</tr>
				<tr>
					<td colspan="1" align="right">¿Su asesor atiende las necesidades del grupo tales como apoyar<br>la cobranza y los problemas del grupo?</td>
					<td colspan="1" align="left"><%=HTMLHelper.radioBoton("P6", reporteCobranza.asesorProductivo, soloLecturaFlag)%></td>
				</tr>
				<tr>
					<td colspan="1" align="right">¿Su asesor los trata con respeto?</td>
					<td colspan="1" align="left"><%=HTMLHelper.radioBoton("P7", reporteCobranza.asesorRespeta, soloLecturaFlag)%></td>
				</tr>
				<tr>
					<td colspan="1" align="right">¿Su asesor recibe pagos del grupo?</td>
					<td colspan="1" align="left"><%=HTMLHelper.radioBoton("P8", reporteCobranza.asesorRecibePagos, soloLecturaFlag)%></td>
				</tr>
				<tr>
					<td colspan="1" align="right">¿Comentarios?</td>
					<td colspan="1" align="left"><textarea name="P9" cols="50"><%=HTMLHelper.displayField(reporteCobranza.comentarios)%></textarea></td>
				</tr>
			</table>
			<br><br>
			<table>
			<tr>
					<%if(!soloLecturaFlag){%>
						<td colspan="1" align="right"><input type="button" name="" value="Guardar" onClick="guardaCuestionario()" ></td>
					<%}%>
					<td colspan="1" align="left"><input type="button" onclick="history.go(-1)" value="Regresar"></td>
			</tr>
			</table>
			
</form>	
</center>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>