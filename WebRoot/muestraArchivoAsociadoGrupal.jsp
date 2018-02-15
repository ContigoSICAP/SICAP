<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %><%@ page import="com.sicap.clientes.vo.*"%><%@ page import="com.sicap.clientes.helpers.ArchivosAsociadosHelper"%><%@ page import="com.sicap.clientes.util.*"%><jsp:useBean id="mySmartUpload" scope="page" class="com.jspsmart.upload.SmartUpload" /><%
mySmartUpload.initialize(pageContext);
mySmartUpload.upload();
int idCiclo = 0;
CicloGrupalVO ciclo = new CicloGrupalVO();
GrupoVO grupo = new GrupoVO();
if ( mySmartUpload.getRequest().getParameter("noCiclo")!=null){
		idCiclo = Integer.parseInt( mySmartUpload.getRequest().getParameter("noCiclo") );
		grupo = (GrupoVO)session.getAttribute("GRUPO");
		ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
	}
int identificador = Integer.parseInt( mySmartUpload.getRequest().getParameter("identificador") );
String ruta = ArchivosAsociadosHelper.getRutaArchivo(ciclo.idGrupo, ciclo.idCiclo, "G");
String nombreArchivo = ArchivosAsociadosHelper.getNombreArchivoGrupal(ciclo, identificador);
mySmartUpload.downloadFile(ruta+nombreArchivo, "application/octet-stream", nombreArchivo);%>