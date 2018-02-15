<%@ page import="java.io.*"%><%@ page import="com.sicap.clientes.helpers.*"%><%@ page import="com.sicap.clientes.vo.*"%><%

	ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
	int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
	int tipoArchvio = HTMLHelper.getParameterInt(request, "tipoArchivo");
	//int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
	String ruta = ArchivosAsociadosHelper.getRutaArchivo(cliente.idCliente, idSolicitud);
	String nombreArchivo = ArchivosAsociadosHelper.getNombreArchivo(cliente, idSolicitud, tipoArchvio);
    File f = new File(ruta+nombreArchivo);
    InputStream is = new FileInputStream(f);
    OutputStream os = response.getOutputStream();
    response.setContentType("application/pdf");
    response.setHeader("cache-control", "no-cache");

    int l;
    byte[] b = new byte[1024];
    while(true) {
        l = is.read(b);
        if(l<=0) 
        	break;
        os.write(b, 0, l);
    }
    is.close();%>