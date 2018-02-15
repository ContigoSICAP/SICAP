<%@page import="com.sicap.clientes.util.BitacoraUtil"%>
<%@page import="com.jspsmart.upload.SmartUpload"%>
<%@page import="java.io.File"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%><%@ page import="com.sicap.clientes.commands.*"%><jsp:useBean id="mySmartUpload" scope="page" class="com.jspsmart.upload.SmartUpload" /><%@ page import="com.sicap.clientes.util.ClientesConstants"%><%@ page import="com.sicap.clientes.util.Notification"%><%
    boolean proceso = false;
    Notification mensaje[] = new Notification[1];
    mySmartUpload.initialize(pageContext);
    mySmartUpload.upload();
    /*com.jspsmart.upload.File file = mySmartUpload.getFiles().getFile(0);
    String ruta = ClientesConstants.RUTA_BASE_ARCHIVOS+"Migracion\\";
    File directorio = new File(ruta);
    if(!directorio.exists())
        directorio.mkdir();
    file.saveAs(ruta+file.getFileName(), SmartUpload.SAVE_PHYSICAL);
    BitacoraUtil bitacora = new BitacoraUtil(0, request.getRemoteUser(), "Ingreso Informacion Migracion");
    bitacora.registraEventoString("archivo= "+file.getFileName()+", tipo= "+request.getParameter("tipo"));
    proceso = new CommandImportacionInformacion().proesarInformacion(file, request, request.getParameter("tipo"), ruta);
    if(proceso)
        mensaje[0] = new Notification(ClientesConstants.INFO_TYPE, "El archivo "+file.getFileName()+" fue ingresado correctamente.");
    else
        mensaje[0] = new Notification(ClientesConstants.ERROR_TYPE, "Ocurrio un error dentro del archivo "+file.getFileName()+".");*/
    proceso = new CommandImportacionInformacion().proesarInformacion(null, request, "");
    mensaje[0] = new Notification(ClientesConstants.INFO_TYPE, "Termino.");
    request.setAttribute("NOTIFICACIONES", mensaje);
%>
<jsp:forward page="importarInformacion.jsp"></jsp:forward>