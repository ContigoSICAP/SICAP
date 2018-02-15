<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.dao.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.ArchivosAsociadosHelper"%>
<%@ page import="org.apache.log4j.Logger"%>
<%!Logger myLogger = Logger.getLogger("guardaArchivosGrupal.jsp");%>
<%
    //Notification notificaciones[] = null;
    //ArrayList<Notification> array = new ArrayList<Notification>();
    Vector<Notification> notificaciones = new Vector<Notification>();
    // ArrayList<Notification> array = new ArrayList<Notification>();
    String ruta = null;

    GrupoVO grupo = new GrupoVO();
    ArchivoAsociadoVO[] archivos = null;
    int identificador = -1;
    int indiceArregloEventos = -1;
    int idGrupo = 0;
    int idCiclo = 0;
    int seguro = 0;
    CicloGrupalVO ciclo = null;
    ArchivoAsociadoVO archivo = null;
    String prefijo = "";
    String tipoCliente = "";
    String idNombre = "";
    int tipo = 0;
    boolean seguros = false;
    

// Create a factory for disk-based file items
    DiskFileItemFactory factory = new DiskFileItemFactory();

    ServletContext servletContext = this.getServletConfig().getServletContext();
    File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
    factory.setRepository(repository);

// Create a new file upload handler
    ServletFileUpload upload = new ServletFileUpload(factory);
// Set overall request size constraint
    upload.setSizeMax(ClientesConstants.REQUEST_MAX_SIZE);
    String noCicloString = null;
    String noGrupoString = null;
    String identificadorString = null;
    String indiceString = null;
    String tipoArchivoString = null;
    String idGrupoString = null;
    String idCicloString = null;
    String idNombreString = null;
    String conSeguroString = null;
    String nombreGrupoString = null;
    String semanaAdicional = null;

    List<FileItem> items = upload.parseRequest(request);
    Iterator<FileItem> parametersIter = items.iterator();

    while (parametersIter.hasNext()) {
        FileItem parameterItem = parametersIter.next();
        if (parameterItem.isFormField()) {
            if (parameterItem.getFieldName().equals("noCiclo")) {
                noCicloString = parameterItem.getString();
                //System.out.println("noCicloString " + noCicloString);
            }
            if (parameterItem.getFieldName().equals("noGrupo")) {
                noGrupoString = parameterItem.getString();
                //System.out.println("noGrupoString " + noGrupoString);
            }
            if (parameterItem.getFieldName().equals("identificador")) {
                identificadorString = parameterItem.getString();
                //System.out.println("identificadorString " + identificadorString);
            }
            if (parameterItem.getFieldName().equals("indice")) {
                indiceString = parameterItem.getString();
                //System.out.println("indiceString " + indiceString);
            }
            if (parameterItem.getFieldName().equals("tipoArchivo")) {
                tipoArchivoString = parameterItem.getString();
                System.out.println("tipoArchivoString " + tipoArchivoString);
            }
            if (parameterItem.getFieldName().equals("idGrupo")) {
                idGrupoString = parameterItem.getString();
                //System.out.println("idGrupoString " + idGrupoString);
            }
            if (parameterItem.getFieldName().equals("idCiclo")) {
                idCicloString = parameterItem.getString();
                //System.out.println("idCicloString " + idCicloString);
            }
            if (parameterItem.getFieldName().equals("idNombre")) {
                idNombreString = parameterItem.getString();
                //System.out.println("idNombreString " + idNombreString);
            }
            if (parameterItem.getFieldName().equals("conSeguro")) {
                conSeguroString = parameterItem.getString();
                //System.out.println("conSeguroString " + conSeguroString);
            }
            if (parameterItem.getFieldName().equals("nombreGrupo")) {
                nombreGrupoString = parameterItem.getString();
                //System.out.println("nombreGrupoString " + nombreGrupoString);
            }
            
            if (parameterItem.getFieldName().equals("semanaAdicional")) {
                semanaAdicional = parameterItem.getString();
                //System.out.println("nombreGrupoString " + nombreGrupoString);
            }
        }
    }

    if (idCicloString != null) {
        idCiclo = Integer.parseInt(idCicloString);
        grupo = (GrupoVO) session.getAttribute("GRUPO");
        ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
    }
    //ruta = ArchivosAsociadosHelper.getRutaArchivo(ciclo.idGrupo, ciclo.idCiclo, "G");
    if (identificadorString != null && identificadorString != "") {
        identificador = Integer.parseInt(identificadorString);
        indiceArregloEventos = Integer.parseInt(indiceString);
    }
    if (tipoArchivoString != null && tipoArchivoString.equals("fileFichaSeguros")) {
        seguros = true;
        idGrupo = Integer.parseInt(idGrupoString);
        idCiclo = Integer.parseInt(idCicloString);
        idNombre = idNombreString;
        seguro = Integer.parseInt(conSeguroString);
        ruta = ArchivosAsociadosHelper.getRutaArchivo(idGrupo, idCiclo, "DocumentacionGrupal\\");
        prefijo = idGrupo + "_" + idCiclo + "_" + "fichaSeguros" + ".";
        tipo = ClientesConstants.ARCHIVO_TIPO_SEGURO;
        tipoCliente = "S";
        identificador = 0;
    }
    if (tipoArchivoString != null && tipoArchivoString.equals("fileFichaGarantia")) {
        seguros = true;
        idGrupo = Integer.parseInt(idGrupoString);
        idCiclo = Integer.parseInt(idCicloString);
        idNombre = idNombreString;
        ruta = ArchivosAsociadosHelper.getRutaArchivo(idGrupo, idCiclo, "DocumentacionGrupal\\");
        prefijo = idGrupo + "_" + idCiclo + "_" + "fichaGarantia" + ".";
        tipo = ClientesConstants.ARCHIVO_TIPO_GARANTIA;
        tipoCliente = "G";
        identificador = 0;
    }
    if (tipoArchivoString != null && tipoArchivoString.equals("fileDocOficial")) {
        seguros = true;
        idGrupo = Integer.parseInt(idGrupoString);
        idCiclo = Integer.parseInt(idCicloString);
        idNombre = idNombreString;
        ruta = ArchivosAsociadosHelper.getRutaArchivo(idGrupo, idCiclo, "DocumentacionGrupal\\");
        prefijo = idGrupo + "_" + idCiclo + "_" + "documentosOficiales" + ".";
        tipo = ClientesConstants.ARCHIVO_TIPO_DOCUMENTOS_OFICIALES;
        tipoCliente = "D";
        identificador = 0;
    }
    if (tipoArchivoString != null && tipoArchivoString.equals("fileFichaSegurosInter")) {
        seguros = true;
        idGrupo = Integer.parseInt(idGrupoString);
        idCiclo = Integer.parseInt(idCicloString);
        idNombre = idNombreString;
        ruta = ArchivosAsociadosHelper.getRutaArchivo(idGrupo, idCiclo, "DocumentacionGrupal\\");
        prefijo = idGrupo + "_" + idCiclo + "_" + "fichaSegurosInterciclo" + ".";
        tipo = ClientesConstants.ARCHIVO_TIPO_SEGURO_INTERCICLO;
        tipoCliente = "G";
        identificador = 0;
    }
    if (tipoArchivoString != null && tipoArchivoString.equals("fileFichaGarantiaInter")) {
        seguros = true;
        idGrupo = Integer.parseInt(idGrupoString);
        idCiclo = Integer.parseInt(idCicloString);
        idNombre = idNombreString;
        ruta = ArchivosAsociadosHelper.getRutaArchivo(idGrupo, idCiclo, "DocumentacionGrupal\\");
        prefijo = idGrupo + "_" + idCiclo + "_" + "fichaGarantiaInterciclo" + ".";
        tipo = ClientesConstants.ARCHIVO_TIPO_GARANTIA_INTERCICLO;
        tipoCliente = "G";
        identificador = 0;
    }
    if (tipoArchivoString != null && tipoArchivoString.equals("fileDocOficialInter")) {
        seguros = true;
        idGrupo = Integer.parseInt(idGrupoString);
        idCiclo = Integer.parseInt(idCicloString);
        idNombre = idNombreString;
        ruta = ArchivosAsociadosHelper.getRutaArchivo(idGrupo, idCiclo, "DocumentacionGrupal\\");
        prefijo = idGrupo + "_" + idCiclo + "_" + "documentosOficialesInterciclo" + ".";
        tipo = ClientesConstants.ARCHIVO_TIPO_DOCUMENTOS_OFICIALES_INTERCICLO;
        tipoCliente = "D";
        identificador = 0;
    }
    if (tipoArchivoString != null && tipoArchivoString.equals("reporteAlertas")) {
        idGrupo = Integer.parseInt(noGrupoString);
        idCiclo = Integer.parseInt(noCicloString);
        idNombre = idNombreString;
        ruta = ArchivosAsociadosHelper.getRutaArchivo(ciclo.idGrupo, ciclo.idCiclo, "G");
        prefijo = idGrupo + "_" + idCiclo + "_" + idNombre + ".";
        tipo = ClientesConstants.ARCHIVO_TIPO_REPORTE_VISITA_GRUPAL;
        tipoCliente = "G";
    }
    
    if (tipoArchivoString != null && tipoArchivoString.equals("fileCreditoAdicional")) {
        seguros = true;
        idGrupo = Integer.parseInt(idGrupoString);
        idCiclo = Integer.parseInt(idCicloString);
        idNombre = idNombreString;
        ruta = ArchivosAsociadosHelper.getRutaArchivo(idGrupo, idCiclo, "DocumentacionGrupal\\");
        prefijo = idGrupo + "_" + idCiclo + "_" + ClientesConstants.nombreDocArchivoAdicional + ".";
        prefijo = prefijo.replace("#",semanaAdicional);
        tipo = ClientesConstants.ARCHIVO_TIPO_SOLICITUD_ADICIONAL;
        tipoCliente = "D";
        identificador = Integer.parseInt(semanaAdicional);
    }
    
    if (tipoArchivoString != null && tipoArchivoString.indexOf("fileDocLegalAdicional") > -1) {
        seguros = true;
        idGrupo = Integer.parseInt(idGrupoString);
        idCiclo = Integer.parseInt(idCicloString);
        idNombre = idNombreString;
        ruta = ArchivosAsociadosHelper.getRutaArchivo(idGrupo, idCiclo, "DocumentacionGrupal\\");
        prefijo = idGrupo + "_" + idCiclo + "_" + ClientesConstants.nombreDocLegalesAdicionales + ".";
        String semAdDocLegal = tipoArchivoString.substring(tipoArchivoString.indexOf("_")+1, tipoArchivoString.length());
        prefijo = prefijo.replace("#",semAdDocLegal);
        tipo = ClientesConstants.ARCHIVO_TIPO_DOC_LEGAL_ADICIONAL;
        tipoCliente = "D";
        identificador = Integer.parseInt(semAdDocLegal);
    }
    /*if (ciclo.archivosAsociados != null) {
     System.out.println("ciclo.archivosAsociados.length "+ciclo.archivosAsociados.length);
     archivos = ciclo.archivosAsociados.length;
     }*/

    Iterator<FileItem> iter = items.iterator();
    FileItem item = null;

    myLogger.debug("RUTA: " + ruta);
    File direcotio = new File(ruta);

    while (iter.hasNext()) {
        item = iter.next();
        if (!item.isFormField()) {
            // Save it only if this file exists
            if (item.getSize() != 0) {
                int indice = -1;
                if (item.getSize() < ClientesConstants.LONG_MAX_ARCHIVOS) {
                    //if (myFile.getSize() < ClientesConstants.LONG_MIN_ARCHIVOS) {
                    String ext = ArchivosAsociadosHelper.getExtension(item.getName());
                    if (ArchivosAsociadosHelper.esFormatoValido(item)) {
                        myLogger .debug("Subiendo archivo de longitud : " + item.getSize());
                        archivo = new ArchivoAsociadoVO();
                        archivo.idCliente = idGrupo;
                        archivo.idSolicitud = idCiclo;
                        archivo.consecutivo = identificador;
    //                    archivo.consecutivo = 0;
                        //prefijo = String.valueOf(ciclo.idGrupo) + "_" + String.valueOf(ciclo.idCiclo) + "_" + String.valueOf(identificador) + "_";

                        //indice = ArchivosAsociadosHelper.getIndiceGrupal(archivos, identificador);
                        //archivo.nombre = prefijo + "reporte_de_visita" + "." + myFile.getFileExt();
                        //archivo.tipo = ClientesConstants.ARCHIVO_TIPO_REPORTE_VISITA_GRUPAL;
                        indice = ArchivosAsociadosHelper.getIndiceGrupal(archivos, identificador);
    //                   indice = archivos;
                        archivo.nombre = prefijo + ext;
                        archivo.tipo = tipo;

                        if (!direcotio.exists()) {
                            direcotio.mkdirs();
                        }
                        archivo.fechaCaptura = new Timestamp(System.currentTimeMillis());
                        //archivo.tipoCliente = "G";
                        archivo.tipoCliente = tipoCliente;

                        File uploadedFile = new File(ruta + archivo.nombre);
                        item.write(uploadedFile);
                        if (indice == -1) {
                            new ArchivoAsociadoDAO().addArchivo(idGrupo, idCiclo, archivo);
                        } else {
                            new ArchivoAsociadoDAO().updateArchivo(idGrupo, idCiclo, archivo);
                        }

                        //if( ciclo.eventosDePago[indiceArregloEventos].estatusVisitaGerente == 1 )
                        //	ciclo.eventosDePago[indiceArregloEventos].estatusVisitaGerente = 2;
                        //if( ciclo.eventosDePago[indiceArregloEventos].estatusVisitaSupervisor == 1 )	
                        //	ciclo.eventosDePago[indiceArregloEventos].estatusVisitaSupervisor = 2;
                        //new EventosPagosGrupalDAO().updateMonitor(ciclo.eventosDePago[indiceArregloEventos]);
                        if (seguro == 1) {
                            new CicloGrupalDAO().updateSeguroCiclo(seguro, idGrupo, idCiclo);
                            ciclo.seguro = 1;
                        }/* else {
                            ciclo.archivosAsociados = new ArchivoAsociadoDAO().getArchivos(ciclo.idGrupo, ciclo.idCiclo, "G");
                        }*/
                        ciclo.archivosAsociados = new ArchivoAsociadoDAO().getArchivosTipo(ciclo.idGrupo, ciclo.idCiclo);
                        //BITACORA
                        BitacoraUtil bitutil = new BitacoraUtil(idGrupo, request.getRemoteUser(), "guardaArchivosGrupal.jsp");
                        bitutil.registraEvento(archivo);
                        //count++;
                        //array.add(new Notification( ClientesConstants.INFO_TYPE, "El archivo "+myFile.getFileName()+ " fue salvado correctamente." ));
                        notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "El archivo " + item.getName() + " fue salvado correctamente."));
                        
                        if (tipoArchivoString != null && tipoArchivoString.equals("fileCreditoAdicional")
                                && semanaAdicional != null && !semanaAdicional.isEmpty()) {
                            notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Favor de ingresar de nueva cuenta a la pantalla de Alta de Adicional"));
                        }
                        
                        if (tipoArchivoString != null && tipoArchivoString.indexOf("fileDocLegalAdicional") > -1) {
                            notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Favor de ingresar de nueva cuenta a la pantalla de Alta de Adicional"));
                        }
                        request.setAttribute("ESTATUS_ARCHIVO", 1);
                    } else {
                        //array.add(new Notification( ClientesConstants.ERROR_TYPE, "El archivo "+myFile.getFileName()+ " no se encuentra en un formato permitido." ));
                        notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "El archivo " + item.getName() + " no se encuentra en un formato permitido."));
                    }
                } else {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "El archivo " + item.getName() + " excede el tamaño permitido."));
                }
            }
        }
    }
    //ciclo.archivosAsociados = new ArchivoAsociadoDAO().getArchivosTipo(ciclo.idGrupo, ciclo.idCiclo, ClientesConstants.ARCHIVO_TIPO_SEGURO);
    /*ciclo.archivosAsociados = new ArchivoAsociadoDAO().getArchivosTipo(ciclo.idGrupo, ciclo.idCiclo);
     request.setAttribute("IDGRUPO", ciclo.idGrupo);
     request.setAttribute("NOMBRE", mySmartUpload.getRequest().getParameter("nombreGrupo"));
     request.setAttribute("CICLO_EVENTOS_PAGO", ciclo);
     request.setAttribute("REPORTE_VISITA_GRUPAL", ciclo.eventosDePago[indiceArregloEventos].reporteVisita);	
     request.setAttribute("IDALERTA",indiceArregloEventos);
     GrupoUtil.setCiclo(grupo, ciclo, idCiclo);
     session.setAttribute("GRUPO", grupo);*/
    //request.setAttribute("NOTIFICACIONES", array);
    request.setAttribute("NOTIFICACIONES", notificaciones);
%>
<% if (!seguros) {
        request.setAttribute("IDGRUPO", ciclo.idGrupo);
        request.setAttribute("NOMBRE", nombreGrupoString);
        request.setAttribute("CICLO_EVENTOS_PAGO", ciclo);
        request.setAttribute("REPORTE_VISITA_GRUPAL", ciclo.eventosDePago[indiceArregloEventos].reporteVisita);
        request.setAttribute("IDALERTA", indiceArregloEventos);
        GrupoUtil.setCiclo(grupo, ciclo, idCiclo);
        session.setAttribute("GRUPO", grupo);
%>
<jsp:forward page="capturaInformeAlertaGrupal.jsp"><jsp:param value="<%=identificador%>" name="identificador"/><jsp:param value="<%=idCiclo%>" name="idCiclo"/></jsp:forward>
<%} else {
        GrupoUtil.setCiclo(grupo, ciclo, idCiclo);
        session.setAttribute("GRUPO", grupo);%>
<jsp:forward page="cargaArchivos.jsp"><jsp:param value="<%=identificador%>" name="identificador"/><jsp:param value="<%=idCiclo%>" name="idCiclo"/>
    <jsp:param value="<%=semanaAdicional%>" name="semanaAdicional"/>
</jsp:forward>
<%}%>