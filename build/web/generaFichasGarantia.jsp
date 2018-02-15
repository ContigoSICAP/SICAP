<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%><%@ page import="com.sicap.clientes.vo.*"%><%@ page import="com.sicap.clientes.util.*"%><%@ page import="com.sicap.clientes.helpers.*"%><%
//ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
//GrupoVO   grupo   = null;

    int idOperacion = HTMLHelper.getParameterInt(request, "idOperacion");
    int id = -1;
    Object objeto = null;
    GrupoVO grupo = null;

    try {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"FichaGarantia.pdf\"");
        response.setHeader("cache-control", "no-cache");
        GeneraFichaGarantiaHelper doc = new GeneraFichaGarantiaHelper();

        if (idOperacion == 3) {
            id = HTMLHelper.getParameterInt(request, "idCiclo");
            objeto = (GrupoVO) session.getAttribute("GRUPO");
            grupo = (GrupoVO) objeto;
        } else {
            id = HTMLHelper.getParameterInt(request, "idSolicitud");
            objeto = (ClienteVO) session.getAttribute("CLIENTE");
        }
        
            doc.pdfWriter(request, response, objeto, id);
        
        return;
    } catch (Exception e) {
        Logger.debug("Exception en fichasGarantia : " + e.getMessage());
        e.printStackTrace();
    }%>