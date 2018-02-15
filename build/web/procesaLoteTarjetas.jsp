<%@page import="com.sicap.clientes.helpers.ArchivosAsociadosHelper"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="java.io.File"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.*"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Vector"%>
<%@page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%>
<%@page import="com.sicap.clientes.util.*"%>
<%@page import="com.sicap.clientes.commands.*"%>
<%
//<jsp:useBean id="mySmartUpload" scope="page" class="com.jspsmart.upload.SmartUpload" />
    Notification[] notificaciones = new Notification[1];
    DiskFileItemFactory factory = new DiskFileItemFactory();
    ServletContext servletContext = this.getServletConfig().getServletContext();
    File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
    factory.setRepository(repository);
    ServletFileUpload upload = new ServletFileUpload(factory);
    List<FileItem> listFile = upload.parseRequest(request);
    Iterator<FileItem> iter = listFile.iterator();
    FileItem myFile = null;
    while(iter.hasNext()){
        FileItem nomFormulario = iter.next();
        if(nomFormulario.getFieldName().equals("file1"))
            myFile = nomFormulario;
        else
            request.setAttribute(nomFormulario.getFieldName(), nomFormulario.getString());
    }
    //FileItem myFile = iter.next();
    Logger.debug("Nombre y Longitud Archivo "+myFile.getName()+" "+myFile.getSize());
    String ext = ArchivosAsociadosHelper.getExtension(myFile.getName());
    if(ext.equals("xls") || ext.equals("xlsx")){
        if(myFile.getSize() != 0){
            int duplicadas = new CommandProcesaCargaLoteTarjetas().procesaCargaLote(myFile, request);
            if(duplicadas > 0)
                notificaciones[0] = new Notification(ClientesConstants.ERROR_LEVEL, "Problemas con la carga del archivo "+myFile.getName()+". "+duplicadas+" tarjetas duplicadas");
            else
                notificaciones[0] = new Notification(ClientesConstants.INFO_LEVEL, "El archivo "+myFile.getName()+" fue cargado correctamente.");
        }
    } else{
        notificaciones[0] = new Notification(ClientesConstants.ERROR_LEVEL, "El archivo "+myFile.getName()+" no tiene una extensi&oacute;n valida.");
    }
    request.setAttribute("NOTIFICACIONES", notificaciones);
%>
<jsp:forward page="cargaLoteTarjeta.jsp"></jsp:forward>

