<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.dao.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.ArchivosAsociadosHelper"%>
<%@ page import="org.apache.log4j.Logger"%>
<%@ page import="com.sicap.clientes.util.FechasUtil"%>
<%@ page import="java.util.Date"%>
<%!   
//Logger myLogger = Logger.getLogger("heredaArchivosAsociados.jsp");
%>
<%
    Notification notificaciones[] = new Notification[1];
    ArrayList<Notification> array = new ArrayList<Notification>();
    
    ClienteVO cliente = (ClienteVO) session.getAttribute("CLIENTE");
    SolicitudVO solicitud = new SolicitudVO();
   
    ArchivoAsociadoVO[] archivosAHeredar = null;
    
    int idSolicitud;
    int indiceSolicitud;
    String ruta = null;
    
    idSolicitud = Integer.parseInt(request.getParameter("idSolicitud"));
    indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
    
    if(cliente.solicitudes.length > 1)
    {   
        archivosAHeredar  = new ArchivoAsociadoDAO().getArchivos(cliente.idCliente, idSolicitud - 1);

        for(int x = 0; x < archivosAHeredar.length; x++)
        {
            String nombreArchivo = archivosAHeredar[x].nombre;
            int idCliente = cliente.idCliente;
            ruta = ArchivosAsociadosHelper.getRutaArchivo(idCliente, (idSolicitud - 1));

            String rutaOrigen = ruta + nombreArchivo;
            String rutaDestino = ArchivosAsociadosHelper.getRutaArchivo(idCliente,idSolicitud);
            String nombreArchivoDestino  = "";

            //idSolicitud
            if(archivosAHeredar[x].tipo == ClientesConstants.ARCHIVO_TIPO_SOLICITUD)
            {
                String ext = ArchivosAsociadosHelper.getExtension(archivosAHeredar[x].nombre);
                String prefijo = String.valueOf(cliente.idCliente) + "_" + String.valueOf(idSolicitud) + "_";


                File directorio = new File(rutaDestino);
                if (!directorio.exists()) {
                    directorio.mkdir();
                }
                ArchivoAsociadoVO archivoAInsertar = new ArchivoAsociadoVO();
                archivoAInsertar.idCliente = cliente.idCliente;
                archivoAInsertar.idSolicitud = idSolicitud;
                archivoAInsertar.tipoCliente = "I";
                archivoAInsertar.fechaCaptura = new Timestamp(System.currentTimeMillis());
                archivoAInsertar.nombre = prefijo + "solicitud" + "." + ext;
                archivoAInsertar.tipo = ClientesConstants.ARCHIVO_TIPO_SOLICITUD;

                ArchivosAsociadosHelper.copyFile_Java7(rutaOrigen, rutaDestino + "\\" + archivoAInsertar.nombre);
                //getArchivoIndividual(int idCliente, int idCiclo, String tipoCliente, int tipoArchivo) 
                ArchivoAsociadoVO archivoAComprobarExistencia = new ArchivoAsociadoDAO().getArchivoIndividual(idCliente, idSolicitud, "I", ClientesConstants.ARCHIVO_TIPO_SOLICITUD); 

                if (archivoAComprobarExistencia == null) {
                    new ArchivoAsociadoDAO().addArchivo(cliente.idCliente, idSolicitud, archivoAInsertar);
                } else {
                     new ArchivoAsociadoDAO().updateArchivo(cliente.idCliente, idSolicitud, archivoAInsertar);
                }


            }
            if(archivosAHeredar[x].tipo == ClientesConstants.ARCHIVO_TIPO_AUTORIZACION)
            {
                String ext = ArchivosAsociadosHelper.getExtension(archivosAHeredar[x].nombre);
                String prefijo = String.valueOf(cliente.idCliente) + "_" + String.valueOf(idSolicitud) + "_";


                File directorio = new File(rutaDestino);
                if (!directorio.exists()) {
                    directorio.mkdir();
                }
                ArchivoAsociadoVO archivoAInsertar = new ArchivoAsociadoVO();
                archivoAInsertar.idCliente = cliente.idCliente;
                archivoAInsertar.idSolicitud = idSolicitud;
                archivoAInsertar.tipoCliente = "I";
                archivoAInsertar.fechaCaptura = new Timestamp(System.currentTimeMillis());
                archivoAInsertar.nombre = prefijo + "autorizacion" + "." + ext;
                archivoAInsertar.tipo = ClientesConstants.ARCHIVO_TIPO_AUTORIZACION;

                ArchivosAsociadosHelper.copyFile_Java7(rutaOrigen, rutaDestino + "\\" + archivoAInsertar.nombre);
                ArchivoAsociadoVO archivoAComprobarExistencia = new ArchivoAsociadoDAO().getArchivoIndividual(idCliente, idSolicitud, "I", ClientesConstants.ARCHIVO_TIPO_AUTORIZACION);
                if (archivoAComprobarExistencia == null) {
                    new ArchivoAsociadoDAO().addArchivo(cliente.idCliente, idSolicitud, archivoAInsertar);
                } else {
                    new ArchivoAsociadoDAO().updateArchivo(cliente.idCliente, idSolicitud, archivoAInsertar);
                }

            }

            //getRutaArchivo
        }


    } 
    cliente.solicitudes[indiceSolicitud].archivosAsociados =  new ArchivoAsociadoDAO().getArchivos(cliente.idCliente, idSolicitud);
    
    notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Se han heredado correctamente los archivos");
   
    session.setAttribute("CLIENTE", cliente);
    request.setAttribute("NOTIFICACIONES", notificaciones);
%>


<jsp:forward page="archivosAsociados.jsp"><jsp:param value="<%=idSolicitud%>" name="idSolicitud"/></jsp:forward>