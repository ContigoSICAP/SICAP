<%@page import="com.sicap.clientes.dao.CicloGrupalDAO"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.helpers.ArchivosAsociadosHelper"%>
<%@ page import="com.sicap.clientes.helpers.GeneraContratoHelper"%>
<%@ page import="com.sicap.clientes.dao.CreditoDAO"%>
<%
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
String mensajeDoc ="";
SolicitudVO solicitud = new SolicitudVO();
if ( cliente.solicitudes!=null && indiceSolicitud!=-1 ){
	solicitud = cliente.solicitudes[indiceSolicitud];
	if(solicitud.amortizacion!=null && solicitud.amortizacion.length!=0 && solicitud.tipoOperacion!=ClientesConstants.GRUPAL && solicitud.tipoOperacion!=ClientesConstants.REESTRUCTURA_GRUPAL){
		String resp = GeneraContratoHelper.makeContract(cliente, idSolicitud);
		int index = resp.indexOf("<center><b>PAGARÉ</b></center>");
		String temp = "<body>" + resp.substring(0,index) + "</body>";
		session.setAttribute("VIVIENDA",temp);
		temp = "<body>" + resp.substring(index) + "</body>";
		session.setAttribute("PAGARE",temp);
		temp = GeneraContratoHelper.makeOrdenPago( cliente, idSolicitud );
		session.setAttribute("ORDENPAGO",temp);
	}
        
}
    if(session.getAttribute("MENSAJEDOC")!=null)
        mensajeDoc = (String)session.getAttribute("MENSAJEDOC");
    
    if(indiceSolicitud  == (cliente.solicitudes.length - 1)){
        if(cliente.solicitudes.length > 1) {
            CreditoDAO creditoDAO = new CreditoDAO();
            cliente.solicitudes[indiceSolicitud - 1].buroCredito = creditoDAO.getCredito(cliente.idCliente, idSolicitud -1, ClientesConstants.BURO_CREDIT0);
            cliente.solicitudes[indiceSolicitud - 1].circuloCredito = creditoDAO.getCredito(cliente.idCliente, idSolicitud -1, ClientesConstants.CIRCULO_CREDIT0);
        }
    }
    
%>
<html>
<head>
<title>Alta/Modificaci&oacute;n de cliente</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>

    function heredarArchivos(tipo){
            document.getElementById("boton01").disabled = true;
            window.document.HeredaArchivosForm.action="heredaArchivosAsociados.jsp?";
            window.document.HeredaArchivosForm.target="_self";
            window.document.HeredaArchivosForm.submit();
		
    }	
    function cargarArchivos(tipo){
            document.getElementById("boton").disabled = true;
		if ( validaArchivos(tipo) ){
			window.document.encForm.action="guardaArchivosAsociados.jsp";
			window.document.encForm.target="_self";
			window.document.encForm.submit();
		}
	}
        function EnviaraMesa(){
            document.getElementById("boton").disabled = true;
            window.document.forma.command.value = 'cambiaEstatusCliente';
            window.document.forma.submit();
        }

	function muestraArchivo(tipo){
		window.document.encForm.action="muestraArchivoAsociado.jsp";
		window.document.encForm.target="_SE";
		window.document.encForm.tipo.value=tipo;
		window.document.encForm.submit();
	}

	function muestraDocumento(tipo){
		if(tipo=='PDF'){
			window.document.encForm.action="muestraContratoPDF.jsp";
		}else{
			window.document.encForm.action="muestraDocumento.jsp";
		}
		
		window.document.encForm.target="_self";
		window.document.encForm.tipo.value=tipo;
		window.document.encForm.submit();
	}

	function validaArchivos(tipo){
		if(tipo==1){
			if ( window.document.encForm.imagen.value=='' 
					&& window.document.encForm.autorizacion.value==''
					&& window.document.encForm.primerobligado.value=='' 
					&& window.document.encForm.solicitud.value==''
					&& window.document.encForm.documentos.value==''){
				alert("Debe especificar al menos un archivo a cargar");
				return false;
			}
		}
		if(tipo==2){
			if ( window.document.encForm.imagen.value=='' 
					&& window.document.encForm.autorizacion.value==''
					&& window.document.encForm.primerobligado.value=='' 
					&& window.document.encForm.segundoobligado.value=='' 
					&& window.document.encForm.solicitud.value==''
					&& window.document.encForm.documentos.value==''){
				alert("Debe especificar al menos un archivo a cargar");
				return false;
			}
		}
		if(tipo==3){
			if ( window.document.encForm.autorizacion.value==''
//					&& window.document.encForm.primerobligado.value=='' 
					&& window.document.encForm.solicitud.value==''){
				alert("Debe especificar al menos un archivo a cargar");
				return false;
			}
		}
		if(tipo==4){
			if (  window.document.encForm.imagenvivienda.value==''
					&& window.document.encForm.imagenvivienda2.value==''
					&& window.document.encForm.imagenvivienda3.value==''
					&& window.document.encForm.certificado.value=='' 
					&& window.document.encForm.subsidio.value==''){
				alert("Debe especificar al menos un archivo a cargar");
				return false;
			}
		}
		if(tipo==21){
			if ( window.document.encForm.autorizacion.value==''
					&& window.document.encForm.primerobligado.value==''
					&& window.document.encForm.solicitud.value==''
					&& window.document.encForm.imagenvivienda.value==''
					&& window.document.encForm.imagenvivienda2.value==''
					&& window.document.encForm.imagenvivienda3.value==''
					&& window.document.encForm.certificado.value=='' 
					&& window.document.encForm.subsidio.value==''){
				alert("Debe especificar al menos un archivo a cargar");
				return false;
			}
		}
	
	return true;
	}

	function visorPDF(orientacion){
                document.getElementById("boton").disabled = true;
		window.document.forma.target="_top";
		window.document.forma.action="frameVisorPDF.jsp";
		window.document.forma.orientacionVisor.value=orientacion;
		window.document.forma.command.value="consultaArchivosAsociados";
		document.body.style.cursor = "wait";
                window.document.forma.submit();
	}
        
        function closeVisor(){           
            if ( window.document.forma.action ==''){
               alert('Debe primero antes abrir un visor');
               return false;
            }           
            window.document.forma.target="_top";
            window.document.forma.action='';
            window.document.forma.command.value="consultaArchivosAsociados";
            window.document.forma.submit();
      }


</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<%if( solicitud.estatus == ClientesConstants.SOLICITUD_NUEVA || solicitud.estatus == ClientesConstants.SOLICITUD_EN_ANALISIS
     || solicitud.estatus == ClientesConstants.SOLICITUD_PENDIENTE || solicitud.estatus == ClientesConstants.SOLICITUD_REVALORACION){%>
    <jsp:include page="menuIzquierdoRapido.jsp" flush="true"/>
<%}else{%>
    <jsp:include page="menuIzquierdo.jsp" flush="true"/> 
<%}%>
<center>
<!-- INICIO NUEVO CODIGO -->
<form name="encForm" method="post" action="" enctype="multipart/form-data">
<input type="hidden" name="idCliente" value="<%=cliente.idCliente%>">
<input type="hidden" name="idSolicitud" value="<%=solicitud.idSolicitud%>">
<input type="hidden" name="idSolicitud2" value="<%=idSolicitud %>">
<input type="hidden" name="indiceSolicitud" value="<%=indiceSolicitud %>">
<input type="hidden" name="tipo" value="0">

<table border="0" width="100%">
	<tr>
	<td align="center">
<!-- FIN NUEVO CODIGO -->
<h3>Archivos asociados</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<%if(!mensajeDoc.equals("") && solicitud.documentacionCompleta==0 && (solicitud.desembolsado != ClientesConstants.DESEMBOLSADO && solicitud.desembolsado != ClientesConstants.CANCELADO)){%>
<b><font color='<%=ClientesConstants.ERROR_COLOR %>'> <%=mensajeDoc%> </font></b><br>
<%}%>
			<table>
				<tr>
					<td colspan="2" align="center" id="boton">
						<a href="#" onClick="visorPDF('H')"> Visor PDF (horizontal)</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<a href="#" onClick="visorPDF('V')"> Visor PDF (vertical)</a><br><br>
                                                <a href="#" onClick="closeVisor()"> Cerrar Visor</a><br><br>
					</td>
				</tr>

<%if ( solicitud.tipoOperacion == ClientesConstants.CONSUMO || solicitud.tipoOperacion == ClientesConstants.MICROCREDITO){%>
				<tr>
					<td colspan="2" align="center">Fotografía de negocio (JPG, GIF, ZIP, RAR)</td>
				</tr>
				<tr>
					<td><%=ArchivosAsociadosHelper.getTexto(solicitud.archivosAsociados, ClientesConstants.ARCHIVO_TIPO_IMAGEN,0, idSolicitud)%></td>
					<td><input type="file" name="imagen" size="45"></td>
				</tr>
				<tr>
					<td colspan="2" align="center">Autorización consulta a buró (PDF)</td>
				</tr>
				<tr>
					<td><%=ArchivosAsociadosHelper.getTexto(solicitud.archivosAsociados, ClientesConstants.ARCHIVO_TIPO_AUTORIZACION,0, idSolicitud)%></td>
					<td><input type="file" name="autorizacion" size="45"></td>
				</tr>
				<tr>
					<td colspan="2" align="center">Autorización consulta a buró 1er. obligado (PDF)</td>
				</tr>
				<tr>
					<td><%=ArchivosAsociadosHelper.getTexto(solicitud.archivosAsociados, ClientesConstants.ARCHIVO_TIPO_PRIMER_OBLIGADO,0, idSolicitud)%></td>
					<td><input type="file" name="primerobligado" size="45"></td>
				</tr>
<%} %>				
<%if ( solicitud.tipoOperacion == ClientesConstants.GRUPAL){%>
				<tr>
					<td colspan="2" align="center">Autorización consulta a buró (PDF)</td>
				</tr>
				<tr>
					<td><%=ArchivosAsociadosHelper.getTexto(solicitud.archivosAsociados, ClientesConstants.ARCHIVO_TIPO_AUTORIZACION,0, idSolicitud)%></td>
					<td><input type="file" name="autorizacion" size="45"></td>
				</tr>
				<tr>
					<td colspan="2" align="center">Solicitud (PDF)</td>
				</tr>
				<tr>
					<td><%=ArchivosAsociadosHelper.getTexto(solicitud.archivosAsociados, ClientesConstants.ARCHIVO_TIPO_SOLICITUD,0, idSolicitud)%></td>
					<td><input type="file" name="solicitud" size="45"></td>
				</tr>
<%} %>				

<%if ( solicitud.tipoOperacion == ClientesConstants.CONSUMO || solicitud.tipoOperacion == ClientesConstants.MICROCREDITO){%>

				<tr>
					<td colspan="2" align="center">Autorización consulta a buró 2do. obligado (PDF)</td>
				</tr>
				<tr>
					<td><%=ArchivosAsociadosHelper.getTexto(solicitud.archivosAsociados, ClientesConstants.ARCHIVO_TIPO_SEGUNDO_OBLIGADO,0, idSolicitud)%></td>
					<td><input type="file" name="segundoobligado" size="45"></td>
				</tr>

				<tr>
					<td colspan="2" align="center">Documentos oficiales (PDF)</td>
				</tr>
				<tr>
					<td><%=ArchivosAsociadosHelper.getTexto(solicitud.archivosAsociados, ClientesConstants.ARCHIVO_TIPO_DOCUMENTOS_OFICIALES,0, idSolicitud)%></td>
					<td><input type="file" name="documentos" size="45"></td>
				</tr>

<%} if ( solicitud.tipoOperacion == ClientesConstants.SELL_FINANCE){%> 
				<tr>
					<td colspan="2" align="center">Solicitud (PDF)</td>
				</tr>
				<tr>
					<td><%=ArchivosAsociadosHelper.getTexto(solicitud.archivosAsociados, ClientesConstants.ARCHIVO_TIPO_SOLICITUD,0, idSolicitud)%></td>
					<td><input type="file" name="solicitud" size="45"></td>
				</tr>
				<tr>
					<td colspan="2" align="center">Contrato (PDF)</td>
				</tr>
				<tr>
					<td><%=ArchivosAsociadosHelper.getTexto(solicitud.archivosAsociados, ClientesConstants.ARCHIVO_TIPO_CONTRATO,0, idSolicitud)%></td>
					<td><input type="file" name="contrato" size="45"></td>
				</tr>
				<tr>
					<td colspan="2" align="center">Solicitud Firmada (PDF)</td>
				</tr>
				<tr>
					<td><%=ArchivosAsociadosHelper.getTexto(solicitud.archivosAsociados, ClientesConstants.ARCHIVO_TIPO_SOLICITUD_FIRMADA,0, idSolicitud)%></td>
					<td><input type="file" name="solicitud_firm" size="45"></td>
				</tr>

<%}if ( solicitud.tipoOperacion == ClientesConstants.VIVIENDA || solicitud.tipoOperacion == ClientesConstants.CREDIHOGAR){%>
				<tr>
					<td colspan="2" align="center">Fotografía vivienda (JPG, GIF, ZIP, RAR)</td>
				</tr>
				<tr>
					<td><%=ArchivosAsociadosHelper.getTexto(solicitud.archivosAsociados, ClientesConstants.ARCHIVO_TIPO_FOTO_VIVIENDA,0, idSolicitud)%></td>
					<td><input type="file" name="imagenvivienda" size="45"></td>
				</tr>

				<tr>
					<td colspan="2" align="center">Fotografía vivienda 2 (JPG, GIF, ZIP, RAR)</td>
				</tr>
				<tr>
					<td><%=ArchivosAsociadosHelper.getTexto(solicitud.archivosAsociados, ClientesConstants.ARCHIVO_TIPO_FOTO_VIVIENDA2,0, idSolicitud)%></td>
					<td><input type="file" name="imagenvivienda2" size="45"></td>
				</tr>

				<tr>
					<td colspan="2" align="center">Foto de la acción terminada (JPG, GIF, ZIP, RAR)</td>
				</tr>
				<tr>
					<td><%=ArchivosAsociadosHelper.getTexto(solicitud.archivosAsociados, ClientesConstants.ARCHIVO_TIPO_FOTO_VIVIENDA3,0, idSolicitud)%></td>
					<td><input type="file" name="imagenvivienda3" size="45"></td>
				</tr>

				<tr>
					<td colspan="2" align="center">Certificado vivienda (PDF)</td>
				</tr>
				<tr>
					<td><%=ArchivosAsociadosHelper.getTexto(solicitud.archivosAsociados, ClientesConstants.ARCHIVO_TIPO_CERTIFICADO_VIVIENDA,0, idSolicitud)%></td>
					<td><input type="file" name="certificado" size="45"></td>
				</tr>
				<tr>
					<td colspan="2" align="center">Subsidio vivienda (PDF)</td>
				</tr>
				<tr>
					<td><%=ArchivosAsociadosHelper.getTexto(solicitud.archivosAsociados, ClientesConstants.ARCHIVO_TIPO_SUBSIDIO_VIVIENDA, 0, idSolicitud)%></td>
					<td><input type="file" name="subsidio" size="45"></td>
				</tr>
<%} %>
				<tr>
					<td colspan="2" align="center">&nbsp;</td>
				</tr>
			</table>
			<table>
				<tr>
					<td><%out.println(ArchivosAsociadosHelper.getTexto(solicitud));%></td>
				</tr>
				<tr>
                                    <td colspan="2" align="center">
                                    <%if(!request.isUserInRole("ANALISIS_CREDITO") && indiceSolicitud == (cliente.solicitudes.length - 1))
                                     {
                                        CicloGrupalDAO cicloGrupalDAO = new CicloGrupalDAO();
                                        CicloGrupalVO cicloGrupalVO = cicloGrupalDAO.getCiclo(cliente.solicitudes[indiceSolicitud].idGrupo, cliente.solicitudes[indiceSolicitud].idCiclo);
                                        if(cicloGrupalVO == null){
                                            %>
                                                <br><input type="button" id="boton" onclick="cargarArchivos(<%=solicitud.tipoOperacion%>)" value="Cargar archivos">
                                            <%
                                        }else if(cicloGrupalVO.estatus==ClientesConstants.CICLO_APERTURA || cicloGrupalVO.estatus==ClientesConstants.CICLO_PENDIENTE || cicloGrupalVO.estatus==ClientesConstants.CICLO_RECHAZADO)
                                         {      
                                        %>
                                            <br><input type="button" id="boton" onclick="cargarArchivos(<%=solicitud.tipoOperacion%>)" value="Cargar archivos">
                                        <%
                                         }
                                     }
                                    %>
                                    <%if( solicitud.desembolsado!=ClientesConstants.DESEMBOLSADO || solicitud.tipoOperacion==ClientesConstants.VIVIENDA ||request.isUserInRole("ANALISIS_CREDITO") ){ %>
                                                <% 
                                                    if(solicitud.estatus == 0 || solicitud.estatus == ClientesConstants.SOLICITUD_CAPTURADO || solicitud.estatus == ClientesConstants.SOLICITUD_PREAPROBADA)
                                                    {
                                                        if(indiceSolicitud  == (cliente.solicitudes.length - 1)){
                                                            if(cliente.solicitudes.length > 1) {
                                                        
                                                                if(ArchivosAsociadosHelper.EsPosibleHeredarArchivos(cliente, solicitud, cliente.solicitudes[indiceSolicitud - 1], indiceSolicitud, cliente.solicitudes.length)){ %>
                                                                    &nbsp;<input type="button" id="boton01" onclick="heredarArchivos(<%=solicitud.tipoOperacion%>)" value="Heredar archivos ciclo anterior">
                                                <%
                                                                }
                                                            }
                                                        }
                                                    }
                                                %>
					</td>
				<%}else{ %>
					<td colspan="2" align="center">
						<br><input disabled type="button" id="boton" onclick="cargarArchivos(<%=solicitud.tipoOperacion%>)" value="Cargar archivos">
                                        </td>
				<%}%>
                                <%=ArchivosAsociadosHelper.getBotonCambioEstatus(solicitud.archivosAsociados,idSolicitud,solicitud)%>				
			</table>
	</td>
	</tr>
</table>		
<!-- INICIO NUEVO CODIGO -->
</form>
</center>
<!-- FIN NUEVO CODIGO -->
<jsp:include page="footer.jsp" flush="true"/>
<form name="forma" method="post" action="controller">
    <input type="hidden" name="command" value="">
    <input type="hidden" name="idCliente" value="<%=cliente.idCliente%>">
    <input type="hidden" name="idSolicitud" value="<%=solicitud.idSolicitud%>">
    <input type="hidden" name="indiceSolicitud" value="<%=indiceSolicitud %>">
    <input type="hidden" name="orientacionVisor" value="">
</form>
<form name="HeredaArchivosForm" method="get" action="" enctype="multipart/form-data">
    <input type="hidden" name="idCliente" value="<%=cliente.idCliente%>">
    <input type="hidden" name="idSolicitud" value="<%=solicitud.idSolicitud%>">
    <input type="hidden" name="idSolicitud2" value="<%=idSolicitud %>">
    <input type="hidden" name="indiceSolicitud" value="<%=indiceSolicitud %>">
    <input type="hidden" name="tipo" value="0">
</form>
</body>
</html>