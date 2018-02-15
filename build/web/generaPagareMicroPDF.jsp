<%@ page import="com.sicap.clientes.vo.ClienteVO"%><%@ page import="com.sicap.clientes.vo.SolicitudVO"%><%@ page import="com.sicap.clientes.helpers.GeneraPagareHelper"%><%@ page import="com.sicap.clientes.helpers.HTMLHelper"%><%@ page import="com.sicap.clientes.util.SolicitudUtil"%><%@ page import="com.sicap.clientes.util.ClientesConstants"%><%
ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
SolicitudVO solicitud = new SolicitudVO();
if( cliente.solicitudes!=null && indiceSolicitud!=-1 ){
    solicitud = cliente.solicitudes[indiceSolicitud];
    if(solicitud.amortizacion!=null && solicitud.amortizacion.length!=0 && solicitud.tipoOperacion!=ClientesConstants.GRUPAL && solicitud.tipoOperacion!=ClientesConstants.REESTRUCTURA_GRUPAL ){
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition","attachment; filename=\"Pagare.pdf\"");
        response.setHeader("cache-control", "no-cache");
        GeneraPagareHelper pagare = new GeneraPagareHelper();
        pagare.doPagarePDF(request, response, cliente, solicitud);
    }
}%>