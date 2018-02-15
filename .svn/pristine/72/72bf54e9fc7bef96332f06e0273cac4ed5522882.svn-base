<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%><%@ page import="java.util.*"%><%@ page import="com.sicap.clientes.vo.*"%><%@ page import="com.sicap.clientes.util.*"%><%@ page import="com.sicap.clientes.commands.*"%><%@ page import="com.sicap.clientes.dao.PagoDAO"%><jsp:useBean id="mySmartUpload" scope="page" class="com.jspsmart.upload.SmartUpload" /><%@ page import="org.apache.log4j.Logger"%><%! public static Logger myLogger = Logger.getLogger("recibirPAgosReferenciados");%><%@page import="com.sicap.clientes.dao.CatalogoDAO"%><%
    CommandProcesarPagoReferenciado cmdPagoRef = new CommandProcesarPagoReferenciado();
    ArrayList<PagoVO> pagos;
    //Se Carga el archivo   
    mySmartUpload.initialize(pageContext);
    mySmartUpload.upload();
    myLogger.debug("Inicio Pagos Referenciados");
    Notification notificaciones[] = new Notification[1];
    CatalogoDAO catalogoDAO = new CatalogoDAO();

myLogger.debug("Sesion: "+request.getRequestedSessionId());
myLogger.debug("User: "+request.getUserPrincipal());
myLogger.debug("IP: "+request.getRemoteAddr());
    if(!new CatalogoDAO().ejecutandoCierre()){
        for (int i = 0; i < mySmartUpload.getFiles().getCount(); i++) {
            myLogger.debug("Lectura Archivos");
            com.jspsmart.upload.File myFile = mySmartUpload.getFiles().getFile(i);
            myLogger.debug("Archivo: "+myFile.getFileName());
            //Se verifica que exista el archivo	
            pagos = cmdPagoRef.procesaArchivoReferenciado(myFile, request);
            
            int archivoprocesado = 0;
            if (pagos != null && pagos.size() > 0) {
                        //fechapago = pagos.get(0).fechaPago;
                //bancoreferencia = pagos.get(0).bancoReferencia; 
                archivoprocesado = pagos.get(0).idContable;
            }

            PagoDAO pagocartera = new PagoDAO();
            pagos = pagocartera.getPagoVO(archivoprocesado);

            //Archivo de salida
            response.setContentType("application/csv");
            response.setHeader("Content-Disposition", "attachment; filename=\"Salida.csv\"");
            response.setHeader("cache-control", "no-cache");
            Iterator<PagoVO> it = pagos.iterator();
            int id_consecutivo = 0;
            while (it.hasNext()) {
                if (id_consecutivo > 99) {
                    id_consecutivo = 0;
                }
                PagoVO pago = (PagoVO) it.next();
                out.println(pago.fechaPago.toString() + "," + pago.referencia + "," + ((pago.fechaHora.getTime()) + (id_consecutivo++)) + "," + pago.monto + "," + pago.bancoReferencia + "," + pago.sucursal);

            }

            out.flush();
        }
    } else {
        notificaciones[0] = new Notification(ClientesConstants.ERROR_LEVEL, "Cierre en Ejecucion. No se puede procesar las peticion");
        request.setAttribute("NOTIFICACIONES", notificaciones);
    }
%>
<jsp:forward page="recibirPagosReferenciados.jsp"></jsp:forward>