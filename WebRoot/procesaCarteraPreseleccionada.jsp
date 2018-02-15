<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%>
<%@ page import="java.util.*"%><%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.commands.*"%>
<%@ page import="com.sicap.clientes.dao.PagoDAO"%>
<jsp:useBean id="mySmartUpload" scope="page" class="com.jspsmart.upload.SmartUpload" />
<%@page import="com.sicap.clientes.dao.CatalogoDAO"%>
<%
    Logger.debug("Inicio Procesar cartera");
    CommandAsignaCarteraPre cmdAsigCarterPre = new CommandAsignaCarteraPre();
    
    CommandProcesarPagoReferenciado cmdPagoRef = new CommandProcesarPagoReferenciado();
    ArrayList<PagoVO> pagos;
    //Se Carga el archivo   
    mySmartUpload.initialize(pageContext);
    mySmartUpload.upload();
     
    //Notification notificaciones[] = new Notification[1];
    Vector<Notification> notificaciones = new Vector<Notification>();
    CatalogoDAO catalogoDAO = new CatalogoDAO();
     Logger.debug("Request OBject:"+ request);
    if(!new CatalogoDAO().ejecutandoCierre()){
        Logger.debug("Request idFondeadorCombo:"+ request.getParameter("idFondeadorCombo"));
        Logger.debug("idFondeadoreRequesMusamsatupload: "+ mySmartUpload.getRequest().getParameter("idFondeadorCombo") );
        for (int i = 0; i < mySmartUpload.getFiles().getCount(); i++) {
            Logger.debug("Lectura Archivos");
            com.jspsmart.upload.File myFile = mySmartUpload.getFiles().getFile(i);
            //Se verifica que exista el archivo	
            cmdAsigCarterPre.procesaArchivo(myFile,request, mySmartUpload.getRequest());
            
            
            
        }
        
    } else {
        notificaciones.addElement(new Notification(ClientesConstants.ERROR_LEVEL, "Cierre en Ejecucion. No se puede procesar las peticion"));
        //notificaciones[0] = new Notification(ClientesConstants.ERROR_LEVEL, "Cierre en Ejecucion. No se puede procesar las peticion");
        request.setAttribute("NOTIFICACIONES", notificaciones);
    }
    
%>
<jsp:forward page="subirCarteraPreseleccionada.jsp"></jsp:forward>