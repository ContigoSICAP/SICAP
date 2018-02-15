<%
//<jsp:useBean id="mySmartUpload" scope="page" class="com.jspsmart.upload.SmartUpload" />
    //Notification[] notificaciones = new Notification[1];
    CommandConfirmaPagosPaynet command = new CommandConfirmaPagosPaynet();
    ArrayList<PaynetVO> arrPagosPaynet = new ArrayList<PaynetVO>();
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
    System.out.println("Nombre y Longitud Archivo "+myFile.getName()+" "+myFile.getSize());
    String ext = ArchivosAsociadosHelper.getExtension(myFile.getName());
    System.out.println("ext "+ext);
    if(ext.equals("txt")){
        if(myFile.getSize() != 0){
            arrPagosPaynet = command.procesaCargaConfiracion(myFile, request);
            if(arrPagosPaynet.size() > 0){
                System.out.println("El archivo "+myFile.getName()+" fue cargado correctamente.");
                //Archivo de salida
                response.setContentType("application/csv");
                response.setHeader("Content-Disposition","attachment; filename=\"Coinciliacion de Pagos Paynet.csv\"");
                response.setHeader("cache-control", "no-cache");
                out.println("No Autorizacion,Fecha Pago,Hora Pago,Monto,Referencia Paynet,Referencia SICAP,Tipo Coinciliacion");
                for (PaynetVO paynetVO : arrPagosPaynet) {
                    out.println(paynetVO.getIdPago()+","+paynetVO.getFechaAutPagPay()+","+paynetVO.getHoraPago()+","+paynetVO.getMonto()+","+paynetVO.getReferenciaPay()+","+paynetVO.getReferencia()+"',"+paynetVO.getTipoTran());
                }
                out.flush();
            } else
                System.out.println("Problemas con la carga del archivo "+myFile.getName()+". duplicadas tarjetas duplicadas");
        }
    } else{
        System.out.println("El archivo "+myFile.getName()+" no tiene una extensi&oacute;n valida.");
    }
%>
<%@page import="com.sicap.clientes.vo.PaynetVO"%><%@page import="java.util.ArrayList"%><%@page import="com.sicap.clientes.helpers.ArchivosAsociadosHelper"%><%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%><%@page import="java.io.File"%><%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%><%@page import="org.apache.commons.fileupload.*"%><%@page import="java.util.Iterator"%><%@page import="java.util.List"%><%@page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%><%@page import="com.sicap.clientes.commands.*"%>