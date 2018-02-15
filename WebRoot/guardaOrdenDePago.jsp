<jsp:useBean id="mySmartUpload" scope="page" class="com.jspsmart.upload.SmartUpload" />
<%
    Notification notificaciones[] = null;
    ArrayList<Notification> array = new ArrayList<Notification>();
    OrdenesDePagoHelper oPagoHelper = new OrdenesDePagoHelper();
    List list = null;
    OrdenDePagoVO oPago = null;
    String ruta = null;
    // Initialization
    mySmartUpload.initialize(pageContext);
    // Upload	
    mySmartUpload.upload();
    ruta = ClientesConstants.RUTA_BASE_ARCHIVOS + "OrdenesDePago\\";
    File directorio = new File(ruta);
    // Select each file
    for (int i = 0; i < mySmartUpload.getFiles().getCount(); i++) {
    // Retreive the current file
        com.jspsmart.upload.File myFile = mySmartUpload.getFiles().getFile(i);
        // Save it only if this file exists
        if (!myFile.isMissing()) {
            if (true) { //Validar Formato
                if (!directorio.exists()) {
                    directorio.mkdirs();
                }
                myFile.saveAs(ruta + myFile.getFileName(), SmartUpload.SAVE_PHYSICAL);
                list = oPagoHelper.getOrdenPagofromFile(ruta, myFile.getFileName(), request.getRemoteUser());
                //Archivo de salida
                String[] banco = myFile.getFileName().split("_");
                response.setContentType("application/csv");
                response.setHeader("Content-Disposition","attachment; filename=\"Confirmacion ODP banco "+banco[0]+".csv\"");
                response.setHeader("cache-control", "no-cache");
                out.println("Referencia,Monto,Estatus,Descripcion");
                Iterator iter = list.iterator();
                while (iter.hasNext()) {
                    oPago = (OrdenDePagoVO) iter.next();
                    out.println("'"+oPago.getReferencia()+"',"+oPago.getMonto()+","+oPago.getEstatus()+","+oPago.getDescEstatus());
                }
                out.flush();
            } else {
                array.add(new Notification(ClientesConstants.ERROR_TYPE, "El archivo " + myFile.getFileName() + " no se encuentra en un formato permitido."));
            }
        }
    }
    if (array.size() > 0) {
        notificaciones = new Notification[array.size()];
        for (int i = 0; i < notificaciones.length; i++) {
            notificaciones[i] = (Notification) array.get(i);
        }
    }
    request.setAttribute("NOTIFICACIONES", notificaciones);
%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %><%@ page import="java.io.*"%><%@ page import="java.util.*"%><%@ page import="com.sicap.clientes.util.*"%><%@ page import="com.sicap.clientes.helpers.OrdenesDePagoHelper"%><%@ page import="com.sicap.clientes.vo.*"%><jsp:directive.page import="com.jspsmart.upload.SmartUpload"/>