<%@page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@page import="java.util.TreeMap"%>
<%@page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@page import="com.sicap.clientes.vo.ClienteVO"%>
<%@page import="com.sicap.clientes.vo.OrdenDePagoVO"%>
<%@page import="com.sicap.clientes.dao.OrdenDePagoDAO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.sicap.clientes.helpers.HTMLHelper"%>

<%
    ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
    int idSolicitud  = HTMLHelper.getParameterInt(request,"idSolicitud");
    TreeMap catBancos = CatalogoHelper.getCatalogo("c_bancos");
    TreeMap catEstatusODP = CatalogoHelper.getCatalogo("c_estatus_ordenes_pago");
    ArrayList<OrdenDePagoVO> ordenesPago = new ArrayList<OrdenDePagoVO>();
    OrdenDePagoDAO opagoDao = new OrdenDePagoDAO();
    ordenesPago = opagoDao.getOrdenesPagoCliente(cliente.getIdCliente(), idSolicitud);
%>

<html>
    <head>
        <title>Órdenes de Pago por cliente</title>
        <script languaje="Javascript" src="./js/functions.js"></script>
        <script type="text/javascript">

            function muestraDocumento(ref){
                window.document.forma.command.value='muestraDocumento';
                window.document.forma.tipo.value='ORDENPAGOINDIVIDUAL';
                window.document.forma.referencia.value=ref;
                window.document.forma.submit();
            }

        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <jsp:include page="menuIzquierdo.jsp" flush="true"/>
        <center>
            <form name="forma" action="controller" method="post">
                <input type="hidden" name="command" value="">
                <input type="hidden" name="tipo" value="">
                <input type="hidden" name="referencia" value="">
                <input type="hidden" name="idSolicitud" value="<%=idSolicitud%>">
                <table border="0" width="100%">
                    <td valign="top"></td>
                    <td align="center">
                        <h3>Órdenes de Pago por Cliente</h3>
                        <%if ( ordenesPago!=null){%>
                         <table border="0" width="90%" align="center">
                             <tr bgcolor="#009865" class="whitetext">
                                 <td align="center">Fecha de Captura</td>
                                 <td align="center">Nombre</td>
                                 <td align="center">Orden de Pago</td>
                                 <td align="center">Monto</td> 
                                 <td align="center">Banco</td>
                                 <td align="center">Estatus</td>
                             </tr>
                             <%for(OrdenDePagoVO oPago : ordenesPago){%>
                                <tr>
                                    <td align="center"><%=HTMLHelper.displayField(oPago.getFechaCaptura()) %></td>
                                    <td align="left"><%=HTMLHelper.displayField(oPago.getNombre()) %></td>
                                    <td align="center"><a href="#" onClick=muestraDocumento("<%=oPago.getReferencia()%>")><%=HTMLHelper.displayField(oPago.getReferencia())%></a></td>
                                    <td align="right"><%=HTMLHelper.formatoMonto(oPago.getMonto()) %></td>
                                    <td align="center"><%=HTMLHelper.getDescripcion(catBancos, oPago.getIdBanco()) %></td>
                                    <td align="center"><%=HTMLHelper.getDescripcion(catEstatusODP, oPago.getEstatus()) %></td>
                                </tr>
                             <%}%>
                         </table>
                        <%}%>                        
                </tr>
                </table>
            </form>
        </center>
    </body>
</html>
