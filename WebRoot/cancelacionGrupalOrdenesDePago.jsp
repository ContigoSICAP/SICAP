<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.OrdenDePagoVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>

<%
    TreeMap catBancos = CatalogoHelper.getCatalogo("c_bancos");
    TreeMap catSucursales = CatalogoHelper.getCatalogo("c_sucursales");
    ArrayList<OrdenDePagoVO> ordenesDePago = (ArrayList<OrdenDePagoVO>)request.getAttribute("ORDENESDEPAGO");
    Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
    String nombreGrupo = (String)request.getAttribute("NOMBREGRUPO");    
%>
<html>
    <head>
        <title>Cancelación Grupal de ODP</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script type="text/javascript">
            function consultaOrdenesPago(){
                if (window.document.forma.numGrupo.value==0||window.document.forma.numGrupo.value==''|| !esEntero(window.document.forma.numGrupo.value)){
                    alert('Número de equipo inválido');
                    return false;
		}
		if (window.document.forma.numCiclo.value==0||window.document.forma.numCiclo.value==''|| !esEntero(window.document.forma.numCiclo.value)){
                    alert('Número de ciclo inválido');
                    return false;
		}
                window.document.forma.command.value='consultaOrdenDePagoGrupal';
		window.document.forma.submit();
            }
            function cancelaOrdenesPago(){
                if(confirm("¿Desea cancelar las órdenes de pago de todos los integrantes del equipo "+window.document.forma.nombreGrupo.value+" ciclo "+window.document.forma.numCiclo.value+"?")){
                    window.document.forma.command.value= 'cancelaOrdenesDePagoGrupal' ;
                    window.document.forma.submit();
                }
                else {
                    return false;
                }
            }
            function nuevaConsulta(){
                window.document.forma.command.value="cancelacionGrupalODP";
                window.document.forma.submit();
            }            
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <center>
            <h2>Cancelación Grupal de Órdenes de Pago</h2>
            <%=HTMLHelper.displayNotifications(notificaciones)%>
            <form action="admin" method="post" name="forma">
                <input type="hidden" name="command" value="">
                <input type="hidden" name="idOrden" value="">
                <input type="hidden" name="idOperacion" value="">
                <%if ( ordenesDePago!=null&&!ordenesDePago.isEmpty() ){
                    int numGrupo = (Integer)request.getAttribute("NUMEROGRUPO");
                    int numCiclo = (Integer)request.getAttribute("NUMEROCICLO");%>
                    <input type="hidden" name="numGrupo" value="<%=numGrupo%>"/>
                    <input type="hidden" name="numCiclo" value="<%=numCiclo%>"/>
                    <input type="hidden" name="nombreGrupo" value="<%=nombreGrupo%>"/>
                    <table border="1" cellpadding="0" cellspacing="0" width="50%">
                        <tr>
                            <td width="15%" align="left">Sucursal</td>
                            <td width="70%" align="left"> <%=HTMLHelper.getDescripcion(catSucursales, ordenesDePago.get(1).getIdSucursal())%></td>
                        </tr>
                        <tr>
                            <td width="15%" align="left">Número de equipo</td>
                            <td width="70%" align="left"> <%=numGrupo%></td>
                        </tr>
                        <tr>
                            <td width="15%" align="left">Número de ciclo</td>
                            <td width="70%" align="left"> <%=numCiclo%></td>
                        </tr>
                        <tr>
                            <td width="15%" align="left">Equipo</td>
                            <td width="70%" align="left"> <%=nombreGrupo%></td>
                        </tr>
                    </table>
                    <table border="0" width="80%" align="center" >
                        <tr bgcolor="#009865" class="whitetext">
                            <%--
                            <td align="center">Sucursal</td>
                            --%>
                            <td align="center">No. Cliente</td>
                            <td align="center">No. Solicitud</td>
                            <td align="center">Nombre</td>
                            <td align="center">Orden de Pago</td>
                            <td align="center">Monto</td>
                            <td align="center">Banco</td>
                        </tr>
                        <%for(OrdenDePagoVO oPago : ordenesDePago){%>
                            <tr>
                                <%--
                                <td align="left"><%=HTMLHelper.getDescripcion(catSucursales, oPago.getIdSucursal()) %></td>
                                --%>
                                <td align="right"><%=HTMLHelper.displayField(oPago.getIdCliente()) %></td>
                                <td align="center"><%=HTMLHelper.displayField(oPago.getIdSolicitud()) %></td>
                                <td align="left"><%=HTMLHelper.displayField(oPago.getNombre()) %></td>
                                <td align="center"><%=HTMLHelper.displayField(oPago.getReferencia()) %></td>
                                <td align="right"><%=HTMLHelper.formatoMonto(oPago.getMonto()) %></td>
                                <td align="center"><%=HTMLHelper.getDescripcion(catBancos, oPago.getIdBanco()) %></td>
                            </tr>
                        <%}%>
                        <br>
                        <br>
                        <tr>
                            <td align="center" colspan="7">
                                <br>
                                <input type="button" id="botonCancelacionGrupal" value="Cancelar órdenes de pago" onClick="cancelaOrdenesPago()">
                                <input type="button" value="Nueva consulta" onClick="nuevaConsulta()">
                            </td>
                        </tr>
                    </table>
                <%} else {%>
                    <table border="0" width="90%" align="center" >
                    <tr>
                        <td width="50%" align="right">Número de Equipo</td>
                        <td width="50%"><input type="text" name="numGrupo" size="7" maxlength="7" value="" /></td>
                    </tr>
                    <tr>
                        <td width="50%" align="right">Número de Ciclo</td>
                        <td width="50%"><input type="text" name="numCiclo" size="5" maxlength="7" value="" /></td>
                    </tr>
                    <tr>
                        <td colspan="2" align="center">
                            <br>
                            <input type="button" name="" value="Consultar" onClick="consultaOrdenesPago();" >
                            <br>
                            <br>
                        </td>
                    </tr>
                </table>
                <%}%>
            </form>
        </center>
        <jsp:include page="footer.jsp" flush="true"/>
    </body>
</html>