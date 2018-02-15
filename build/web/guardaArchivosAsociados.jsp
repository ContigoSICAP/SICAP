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
<%!   
Logger myLogger = Logger.getLogger("guardaArchivosAsociados.jsp");
%>
<%
    Notification notificaciones[] = null;
    ArrayList<Notification> array = new ArrayList<Notification>();
    String ruta = null;
    ClienteVO cliente = (ClienteVO) session.getAttribute("CLIENTE");
    SolicitudVO solicitud = new SolicitudVO();
    ArchivoAsociadoVO[] archivos = null;
    int idSolicitud = 0;
    int indiceSolicitud = 0;

// Create a factory for disk-based file items
    DiskFileItemFactory factory = new DiskFileItemFactory();

    ServletContext servletContext = this.getServletConfig().getServletContext();
    File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
    factory.setRepository(repository);

// Create a new file upload handler
    ServletFileUpload upload = new ServletFileUpload(factory);
// Set overall request size constraint
    upload.setSizeMax(ClientesConstants.REQUEST_MAX_SIZE);
    String idSolicitudString = null;
    List<FileItem> items = upload.parseRequest(request);
    Iterator<FileItem> parametersIter = items.iterator();

    while (parametersIter.hasNext()) {
        FileItem parameterItem = parametersIter.next();
        if (parameterItem.isFormField()) {
            if (parameterItem.getFieldName().equals("idSolicitud")) {
                idSolicitudString = parameterItem.getString();
                myLogger.debug("idSolicitud " + idSolicitud);
            }
        }
    }

    if (idSolicitudString != null && !idSolicitudString.equalsIgnoreCase("")) {
        idSolicitud = Integer.parseInt(idSolicitudString);
        indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
        solicitud = cliente.solicitudes[indiceSolicitud];
        archivos = solicitud.archivosAsociados;
    }
    ruta = ArchivosAsociadosHelper.getRutaArchivo(cliente.idCliente, idSolicitud);
    myLogger.debug(ruta);
    File direcotio = new File(ruta);

    Iterator<FileItem> iter = items.iterator();
    FileItem item = null;
    // Select each file
    while (iter.hasNext()) {
        item = iter.next();
        if (!item.isFormField()) {
            // Save it only if this file exists
            if (item.getSize() != 0) {
                int indice = 0;
                if (item.getSize() < ClientesConstants.LONG_MAX_ARCHIVOS) {
                    String ext = ArchivosAsociadosHelper.getExtension(item.getName());
                    if (ArchivosAsociadosHelper.esFormatoValido(item)) {
                        myLogger.debug("Subiendo archivo de longitud : " + item.getSize());
                        ArchivoAsociadoVO archivo = new ArchivoAsociadoVO();
                        archivo.idCliente = cliente.idCliente;
                        archivo.idSolicitud = idSolicitud;
                        archivo.tipoCliente = "I";
                        String prefijo = String.valueOf(cliente.idCliente) + "_" + String.valueOf(idSolicitud) + "_";
                        if (item.getFieldName().equals("imagen")) {
                            indice = ArchivosAsociadosHelper.getIndice(archivos, ClientesConstants.ARCHIVO_TIPO_IMAGEN);
                            archivo.nombre = prefijo + "imagen" + "." + ext;
                            archivo.tipo = ClientesConstants.ARCHIVO_TIPO_IMAGEN;
                        } else if (item.getFieldName().equals("autorizacion")) {
                            indice = ArchivosAsociadosHelper.getIndice(archivos, ClientesConstants.ARCHIVO_TIPO_AUTORIZACION);
                            archivo.nombre = prefijo + "autorizacion" + "." + ext;
                            archivo.tipo = ClientesConstants.ARCHIVO_TIPO_AUTORIZACION;
                        } else if (item.getFieldName().equals("solicitud")) {
                            indice = ArchivosAsociadosHelper.getIndice(archivos, ClientesConstants.ARCHIVO_TIPO_SOLICITUD);
                            archivo.nombre = prefijo + "solicitud" + "." + ext;
                            archivo.tipo = ClientesConstants.ARCHIVO_TIPO_SOLICITUD;
                        } else if (item.getFieldName().equals("amortizacion")) {
                            indice = ArchivosAsociadosHelper.getIndice(archivos, ClientesConstants.ARCHIVO_TIPO_AMORTIZACION);
                            archivo.nombre = prefijo + "amortizacion" + "." + ext;
                            archivo.tipo = ClientesConstants.ARCHIVO_TIPO_AMORTIZACION;
                        } else if (item.getFieldName().equals("primerobligado")) {
                            indice = ArchivosAsociadosHelper.getIndice(archivos, ClientesConstants.ARCHIVO_TIPO_PRIMER_OBLIGADO);
                            archivo.nombre = prefijo + "autorizacion_obligado_uno" + "." + ext;
                            archivo.tipo = ClientesConstants.ARCHIVO_TIPO_PRIMER_OBLIGADO;
                        } else if (item.getFieldName().equals("segundoobligado")) {
                            indice = ArchivosAsociadosHelper.getIndice(archivos, ClientesConstants.ARCHIVO_TIPO_SEGUNDO_OBLIGADO);
                            archivo.nombre = prefijo + "autorizacion_obligado_dos" + "." + ext;
                            archivo.tipo = ClientesConstants.ARCHIVO_TIPO_SEGUNDO_OBLIGADO;
                        } else if (item.getFieldName().equals("imagenvivienda")) {
                            indice = ArchivosAsociadosHelper.getIndice(archivos, ClientesConstants.ARCHIVO_TIPO_FOTO_VIVIENDA);
                            archivo.nombre = prefijo + "imagen_vivienda" + "." + ext;
                            archivo.tipo = ClientesConstants.ARCHIVO_TIPO_FOTO_VIVIENDA;
                        } else if (item.getFieldName().equals("imagenvivienda2")) {
                            indice = ArchivosAsociadosHelper.getIndice(archivos, ClientesConstants.ARCHIVO_TIPO_FOTO_VIVIENDA2);
                            archivo.nombre = prefijo + "imagen_vivienda_2" + "." + ext;
                            archivo.tipo = ClientesConstants.ARCHIVO_TIPO_FOTO_VIVIENDA2;
                        } else if (item.getFieldName().equals("imagenvivienda3")) {
                            indice = ArchivosAsociadosHelper.getIndice(archivos, ClientesConstants.ARCHIVO_TIPO_FOTO_VIVIENDA3);
                            archivo.nombre = prefijo + "imagen_vivienda_3" + "." + ext;
                            archivo.tipo = ClientesConstants.ARCHIVO_TIPO_FOTO_VIVIENDA3;
                        } else if (item.getFieldName().equals("certificado")) {
                            indice = ArchivosAsociadosHelper.getIndice(archivos, ClientesConstants.ARCHIVO_TIPO_CERTIFICADO_VIVIENDA);
                            archivo.nombre = prefijo + "certificado" + "." + ext;
                            archivo.tipo = ClientesConstants.ARCHIVO_TIPO_CERTIFICADO_VIVIENDA;
                        } else if (item.getFieldName().equals("subsidio")) {
                            indice = ArchivosAsociadosHelper.getIndice(archivos, ClientesConstants.ARCHIVO_TIPO_SUBSIDIO_VIVIENDA);
                            archivo.nombre = prefijo + "subsidio" + "." + ext;
                            archivo.tipo = ClientesConstants.ARCHIVO_TIPO_SUBSIDIO_VIVIENDA;
                        } else if (item.getFieldName().equals("documentos")) {
                            indice = ArchivosAsociadosHelper.getIndice(archivos, ClientesConstants.ARCHIVO_TIPO_DOCUMENTOS_OFICIALES);
                            archivo.nombre = prefijo + "documentos_oficiales" + "." + ext;
                            archivo.tipo = ClientesConstants.ARCHIVO_TIPO_DOCUMENTOS_OFICIALES;
                        }

                        if (!direcotio.exists()) {
                            direcotio.mkdirs();
                        }
                        archivo.fechaCaptura = new Timestamp(System.currentTimeMillis());

                        File uploadedFile = new File(ruta + archivo.nombre);
                        item.write(uploadedFile);

                        if (indice == -1) {
                            new ArchivoAsociadoDAO().addArchivo(cliente.idCliente, idSolicitud, archivo);
                        } else {
                            new ArchivoAsociadoDAO().updateArchivo(cliente.idCliente, idSolicitud, archivo);
                        }
                        //Actualizacion de archivo (pendiente)
                        BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, "N/D", "guardaArchivosAsociados.jsp");
                        bitutil.registraEvento(archivo);
                        array.add(new Notification(ClientesConstants.INFO_TYPE, "El archivo " + item.getName() + " fue salvado correctamente."));
                    } else {
                        array.add(new Notification(ClientesConstants.ERROR_TYPE, "El archivo " + item.getName() + " no se encuentra en un formato permitido."));
                    }
                } else {
                    array.add(new Notification(ClientesConstants.ERROR_TYPE, "El archivo " + item.getName() + " excede el tamaño permitido."));
                }
            }
        }
    }

    if (array.size() > 0) {
        notificaciones = new Notification[array.size()];
        for (int i = 0; i < notificaciones.length; i++) {
            notificaciones[i] = (Notification) array.get(i);
        }
    }
    solicitud.archivosAsociados = new ArchivoAsociadoDAO().getArchivos(cliente.idCliente, idSolicitud);
    cliente.solicitudes[indiceSolicitud] = solicitud;
    session.setAttribute("CLIENTE", cliente);
    request.setAttribute("NOTIFICACIONES", notificaciones);
%>

<jsp:forward page="archivosAsociados.jsp"><jsp:param value="<%=idSolicitud%>" name="idSolicitud"/></jsp:forward>
