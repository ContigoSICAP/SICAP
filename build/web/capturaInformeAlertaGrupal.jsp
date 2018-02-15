<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.TreeMap"%>
<%@page import="java.util.Vector"%>
<%
Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");

int indice = -1;
int identificador = -1;

TreeMap catProblemasGrupo = CatalogoHelper.getCatalogoProblemasGrupo();
TreeMap ProblemasAsesor = CatalogoHelper.getCatalogoProblemasAsesor();
//TreeMap ProblemasNegocio = CatalogoHelper.getCatalogoProblemasNegocio();
TreeMap catProblemasPersonales = CatalogoHelper.getCatalogoProblemasPersonales();
TreeMap catProblemasOtros = CatalogoHelper.getCatalogoProblemasOtros();
TreeMap catProblemasSolucion = CatalogoHelper.getCatalogoPropuestasSolucion();


ReporteVisitaGrupalVO reporteVisita = new ReporteVisitaGrupalVO(); 
GrupoVO grupo = new GrupoVO();
CicloGrupalVO ciclo = new CicloGrupalVO();

int existeArchivoAsociado = 0;
int operacion = 0;
int idAlerta = -1;
UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
//GRUPO
if ( session.getAttribute("GRUPO")!=null )
	grupo = (GrupoVO)session.getAttribute("GRUPO");
//CICLO
if ( request.getAttribute("CICLO_EVENTOS_PAGO")!=null )
	ciclo = (CicloGrupalVO)request.getAttribute("CICLO_EVENTOS_PAGO");
//Reporte Visita
if ( request.getAttribute("REPORTE_VISITA_GRUPAL")!=null )
	reporteVisita = (ReporteVisitaGrupalVO)request.getAttribute("REPORTE_VISITA_GRUPAL");


if ( request.getAttribute("OPERACION")!=null )
	operacion = (Integer)request.getAttribute("OPERACION");
//ID DE LA OPERACION
if( request.getAttribute("IDALERTA")!=null )
	idAlerta = (Integer)request.getAttribute("IDALERTA");

if( request.getAttribute("ESTATUS_ARCHIVO")!=null )
	existeArchivoAsociado = (Integer)request.getAttribute("ESTATUS_ARCHIVO");

System.out.println("existeArchivoAsociado "+existeArchivoAsociado);


indice = idAlerta;
identificador = ciclo.eventosDePago[idAlerta].identificador;	
boolean soloLecturaFlag = (operacion==1 ? true:false);

System.out.println("solo lectura "+soloLecturaFlag);

if(!soloLecturaFlag)
	soloLecturaFlag = (existeArchivoAsociado==0 ? true:false);

System.out.println("solo lectura "+soloLecturaFlag);
%>
<html>
<head>
<script language="Javascript" src="./js/functions.js"></script>
<script>
	
	function cargarArchivos(){
		if ( window.document.encForm.reporteVisita.value!='' ){
			window.document.encForm.action="guardaArchivosGrupal.jsp";
			window.document.encForm.target="_self";
			window.document.encForm.submit();
		}
	}

	function muestraArchivo(){
		window.document.encForm.action="muestraArchivoAsociadoGrupal.jsp";
		window.document.encForm.target="_SE";
		window.document.encForm.submit();
	}
	
	function guardaInformeVisita(){
		window.document.forma.command.value = 'guardaInformeVisita';
		var problema = 0;
		for ( var i=1; i < 6 ; i=i+1 ){
			if(i!=3){
				var nombreDinamico = "S"+i;
				var elemento = document.getElementById(nombreDinamico);
				if ( elemento.value!=0 ){
					problema = elemento.value;
				}
			}
		}
		if( problema==0 ){
			alert("Seleccione al menos un problema");
			return false;
		}
		if( window.document.forma.S6.value ==0){
			alert("Seleccione una propuesta de pago");
			return false;
		}
		if( window.document.forma.S7.value =='' || window.document.forma.S7.value == null){
			alert("No a ingresado comentarios");
			return false;
		}
		

	var seleccionados = 0;
	for ( var i=0; i < <%=ciclo.integrantes.length%> ; i=i+1 ){
		var cheked = "seleccionado"+i;
		var seleccionadoOk = document.getElementById(cheked);
		if ( seleccionadoOk.checked == true ){
			seleccionados = seleccionados+1;
		}
	}	
	if ( seleccionados==0 ){
		alert("Seleccione al menos un integrante visitado");
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
<input type="hidden" name="idGrupo" value="<%=ciclo.idGrupo%>">
<input type="hidden" name="idCiclo" value="<%=ciclo.idCiclo%>">
<input type="hidden" name="usuario" value="<%=usuario.nombre%>">
<input type="hidden" name="identificadorAlerta" value="<%=idAlerta%>">

<h3>Captura informe de visita alerta grupal</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
			<table><tr><td align="center"><b>Detalle del ciclo</b></td></tr></table>
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
			<b>Resultados obtenidos durante la visita (llenar correctamente)</b>
			<table border="1">
				<tr>
					<td colspan="1" align="right">Problemas con el grupo</td>
					<td colspan="1" align="left">
						<select name="S1" id="S1" size="1" value="<%=reporteVisita.problemasGrupo%>" >
							<%=HTMLHelper.displayCombo(catProblemasGrupo, reporteVisita.problemasGrupo)%>
						</select>
					</td>
				</tr>
				<tr>
					<td colspan="1" align="right">Problemas con el asesor</td>
					<td colspan="1" align="left">
						<select name="S2" id="S2" size="1" value="<%=reporteVisita.problemasAsesor%>" >
							<%=HTMLHelper.displayCombo(ProblemasAsesor, reporteVisita.problemasAsesor)%>
						</select>
					</td>
				</tr>
				<tr>
					<td colspan="1" align="right">Problemas personales</td>
					<td colspan="1" align="left">
						<select name="S4" id="S4" size="1" value="<%=reporteVisita.problemasPersonales%>" >
							<%=HTMLHelper.displayCombo(catProblemasPersonales, reporteVisita.problemasPersonales)%>
						</select>
					</td>
				</tr>
				<tr>
					<td colspan="1" align="right">Otros problemas</td>
					<td colspan="1" align="left">
						<select name="S5" id="S5" size="1" value="<%=reporteVisita.problemasOtros%>" >
							<%=HTMLHelper.displayCombo(catProblemasOtros, reporteVisita.problemasOtros)%>
						</select>
					</td>
				</tr>
				<tr>
					<td colspan="1" align="right">Propuesta de pago</td>
					<td colspan="1" align="left">
						<select name="S6" id="S6" size="1" value="<%=reporteVisita.propuestaSolucion%>" >
							<%=HTMLHelper.displayCombo(catProblemasSolucion, reporteVisita.propuestaSolucion)%>
						</select>
					</td>
				</tr>
				<tr>
					<td colspan="1" align="right">Comentarios</td>
					<td colspan="1" align="left"><textarea name="S7" cols="50"><%=HTMLHelper.displayField(reporteVisita.comentarios)%></textarea></td>
				</tr>
			</table>
			<br>
			<tr><td  align="center"><b>Seleccione integrantes visitados</b></td></tr>
			<table border="1">
				<tr>
					<td colspan="1" align="center"><b>Seleccionar</b></td>
					<td colspan="1" align="center"><b>No. cliente</b></td>
					<td colspan="1" align="center"><b>Nombre</b></td>
					<td colspan="1" align="center"><b>Rol</b></td>
				</tr>
				<%for(int i=0; i<ciclo.integrantes.length; i++){ boolean integranteFlag=HTMLHelper.buscaCliente(reporteVisita.integrantesVisitados,ciclo.integrantes[i]);%>
					<tr>
						<td align="center"><%=HTMLHelper.displayCheck("seleccionado"+i,integranteFlag)%></td>
						<td align="center">
							<input type="hidden" name="idCliente<%=i%>" value="<%=ciclo.integrantes[i].idCliente%>">
							<%=ciclo.integrantes[i].idCliente%>	
						</td>
						<td align="center" <%if(integranteFlag){%>class="soloLecturaRojo"<%}%>>
							<%=ciclo.integrantes[i].nombre%>
						</td>
						<td align="center"><%=HTMLHelper.printRol(ciclo.integrantes[i].rol)%></td>
					</tr>
				<%}%>
			</table>
			<br><br>
			<table>
				<tr>
					<%if(!soloLecturaFlag ){%>
						<td colspan="1" align="right"><input type="button" name="" value="Guardar" onClick="guardaInformeVisita()" ></td>
					<%}%>
					<td colspan="1" align="left"><input type="button" onclick="history.go(-1)" value="Regresar"></td>
				</tr>
			</table>
</form>		
<!-- INICIO NUEVO CODIGO -->
<form name="encForm" method="post" action="" enctype="multipart/form-data">
<input type="hidden" name="identificador" value="<%=identificador%>">
<input type="hidden" name="indice" value="<%=indice%>">
<input type="hidden" name="noCiclo" value="<%=ciclo.idCiclo%>">
<input type="hidden" name="noGrupo" value="<%=ciclo.idGrupo%>">
<input type="hidden" name="tipoArchivo" value="reporteAlertas">
<input type="hidden" name="idNombre" value="reporteAlertas">
		<tr><td  align="center"><b>Adjuntar documento de visita (opcional)</b></td></tr>
		<table border="1">
				<tr>
					<td colspan="1" align="center"><b>Reporte de Visita (PDF)</b></td>
				</tr>
				<tr>
					<td colspan="1" align="center">
						<%=ArchivosAsociadosHelper.getTextoGrupal(ciclo.archivosAsociados, ClientesConstants.ARCHIVO_TIPO_REPORTE_VISITA_GRUPAL, ciclo.eventosDePago[idAlerta].identificador)%>
						<% if(soloLecturaFlag && (ciclo.eventosDePago[idAlerta].estatusVisitaSupervisor==1 || ciclo.eventosDePago[idAlerta].estatusVisitaGerente==1)) {%>
							<input type="file" name="reporteVisita" size="45">
						<%}%>
					</td>
				</tr>
				<% if(soloLecturaFlag && (ciclo.eventosDePago[idAlerta].estatusVisitaSupervisor==1 || ciclo.eventosDePago[idAlerta].estatusVisitaGerente==1)) {%>
					<tr>
						<td colspan="1" align="center">
							<br><input type="button" onclick="cargarArchivos()" value="Cargar archivos">
						</td>
					</tr>
				<%}%>
		</table>
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>