<%-- 
    Document   : capturaControlDePagos
    Created on : 14/03/2012, 12:36:24 PM
    Author     : Alex
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.sicap.clientes.util.ClientesConstants"%>
<%@page import="com.sicap.clientes.dao.ControlPagosDAO"%>
<%@page import="com.sicap.clientes.vo.ControlPagosVO"%>
<%@page import="com.sicap.clientes.util.FormatUtil"%>
<%@page import="com.sicap.clientes.dao.SucursalDAO"%>
<%@page import="com.sicap.clientes.dao.cartera.CreditoCartDAO"%>
<%@page import="com.sicap.clientes.vo.cartera.CreditoCartVO"%>
<%@page import="com.sicap.clientes.vo.SaldoHistoricoVO"%>
<%@page import="java.util.Date"%>
<%@page import="com.sicap.clientes.vo.cartera.TablaAmortVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.sicap.clientes.dao.cartera.TablaAmortDAO"%>
<%@ page import="java.util.Vector"%>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%>
<%@ page import="com.sicap.clientes.vo.GrupoVO"%>

<%
int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
GrupoVO grupo = (GrupoVO)session.getAttribute("GRUPO");
Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
int i = 0;
int t = 0;
Date fecha = new Date();
long lnMilisegundos = fecha.getTime();
Date fechaDia = new java.sql.Date(lnMilisegundos);
CreditoCartVO credito = new CreditoCartDAO().getCreditoClienteSol(grupo.idGrupo, idCiclo);
ArrayList<TablaAmortVO> listTabla = new TablaAmortDAO().getTablaAmortizacion(grupo);
String sucursal = new SucursalDAO().getSucursalNombre(credito.getNumSucursal());
ArrayList<ControlPagosVO> listPagos = new ControlPagosDAO().getControlPagos(grupo);
int numPagos = listTabla.size();
if(listPagos.size() != 0)
    numPagos = listPagos.size();
%>

<html>
    <head>
        <title>Captura de Plan de Pagos</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script language="Javascript" src="./js/functionsGrupal.js"></script>
        <script type="text/javascript">
            function grabarControl(){
                alert("Entra para rabar");
                window.document.forma.command.value='registraControlPagos';
                window.document.forma.submit();
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css"/>
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <form name="forma" action="controller" method="POST">
            <input type="hidden" name="command" value=""/>
            <table border="0" width="100%" align="center">
                <tr align="center">
                    <td>
                        <h3>Control de Pagos y Garant&iacute;as Semanales</h3>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table border="2" rules="rows">
                            <tr>
                                <td width="120">Nombre del Grupo:</td>
                                <td><input type="text" readonly="true" size="40" class="soloLectura" value="<%= HTMLHelper.displayField(grupo.nombre) %>"/></td>
                                <td width="190">Monto Desembolsado:</td>
                                <td><input type="text" readonly="true" size="10" class="soloLectura" value="<%= HTMLHelper.formatCantidad(credito.getMontoDesembolsado()) %>"/></td>
                            </tr>
                            <tr>
                                <td width="120">Asesor:</td>
                                <td><input type="text" readonly="true" size="40" class="soloLectura" value="<%= HTMLHelper.displayField(credito.getNumEjecutivo()) %>"/></td>
                                <td width="190">Monto de Garant&iacute;a (10%): </td>
                                <td><input type="text" readonly="true" size="10" class="soloLectura" value="<%= HTMLHelper.formatCantidad((credito.getMontoDesembolsado()*0.10)) %>"/></td>
                            </tr>
                            <tr>
                                <td width="120">Supervisor:</td>
                                <td><input type="text" readonly="true" size="40" name="supervisor" id="supervisor" class="soloLectura" value="<%= HTMLHelper.displayField(credito.getNumEjecutivo()) %>"/></td>
                                <td width="190">Fecha a Depositar (dd/mm/aaaa):</td>
                                <td><input type="text" name="fechaDeposito" id="Fecha1" size="10" value="<%= HTMLHelper.displayField(fechaDia) %>"/></td>
                            </tr>
                            <tr>
                                <td width="120">Sucursal:</td>
                                <td><input type="text" size="40" readonly="true" class="soloLectura" value="<%= HTMLHelper.displayField(sucursal) %>"/></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr><td>&nbsp;</td></tr>
                <tr>
                    <td>
                        <table border="1">
                            <thead>
                                <tr align="center">
                                    <td width="5">No. Pago</td>
                                    <td>Fecha de Reuni&oacute;n Seg&uacute;n Calendario</td>
                                    <td>Fecha Real de D&iacute;a de Reuni&oacute;n (A)</td>
                                    <td>Fecha de Pago del Grupo (B)</td>
                                    <td>Diferencia en Dias (A-B)</td>
                                    <td>Pago Normal Monto de Ficha (C)</td>
                                    <td>Monto Pagado en Cajas (D)</td>
                                    <td>Diferencia (C-D)</td>
                                    <td>Garant&iacute;as Semanal</td>
                                    <td>Saldo Garant&iacute;as (=Al 10% mas el Garant&iacute;a Semanal)</td>
                                </tr>
                            </thead>
                            <tbody>
                                <%for(i=0; i<numPagos; i++){
                                    if(listPagos.size()>i){//Llena los datos con la tabla de control si no con la de amortizacion
                                %>
                                <tr align="center">
                                    <td><input type="text" name="numPago" id="numPago" readonly="true" size="2" value="<%=HTMLHelper.displayField(listPagos.get(i).getNumPago())%>"/></td>
                                    <td><input type="text" name="fecReunion" id="fecReunion" readonly="true" value="<%=HTMLHelper.displayField(listPagos.get(i).getFechaReunion())%>"/></td>
                                    <td><input type="text" name="fecReal" id="fecReal" readonly="true" value="<%=HTMLHelper.displayField(listPagos.get(i).getFechaReal())%>"/></td>
                                    <td><input type="text" name="fecPago" id="fecPago" value="<%=HTMLHelper.displayField(listPagos.get(i).getFechaPago())%>"/></td>
                                    <td><input type="text" name="difDiasFechas" id="difDiasFechas" readonly="true" value="<%=HTMLHelper.displayField(listPagos.get(i).getDifDiasFechas())%>"/></td>
                                    <td><input type="text" name="montoPagoTab" id="montoPagoTab" value="<%=HTMLHelper.displayField(listPagos.get(i).getMontoFichas())%>"/></td>
                                    <td><input type="text" name="montoPagoCajas" id="montoPagoCajas" value="<%=HTMLHelper.displayField(listPagos.get(i).getMontoPagoCajas())%>"/></td>
                                    <td><input type="text" name="difMontos" id="difDiasFechas" value="<%=HTMLHelper.displayField(listPagos.get(i).getDifMonto())%>"/></td>
                                    <td><input type="text" name="ahorros" id="ahorros" value="<%=HTMLHelper.displayField(listPagos.get(i).getAhorroSemanal())%>"/></td>
                                    <td><input type="text" name="saldoAhorro" id="saldoAhorro" value="<%=HTMLHelper.displayField(listPagos.get(i).getSaldoAhorro())%>"/></td>
                                </tr>
                                <%
                                    } else{
                                %>
                                <tr align="center">
                                    <td><input type="text" name="numPago" id="numPago" readonly="true" size="2" value="<%=HTMLHelper.displayField(listTabla.get(t).getNumPago())%>"/></td>
                                    <td><input type="text" name="fecReunion" id="fecReunion" readonly="true" value="<%=HTMLHelper.displayField(listTabla.get(t).getFechaPago())%>"/></td>
                                    <%//System.out.println(listTabla.get(t).getStatus()+" "+listTabla.get(t).getFechaPago());
                                    
                                    if(listTabla.get(t).getStatus()==0){%>
                                    <td><input type="text" name="fecReal" id="fecReal" readonly="true" value="<%=HTMLHelper.displayField("")%>"/></td>
                                    <td><input type="text" name="fecPago" id="Fecha2" readonly="true" value="<%=HTMLHelper.displayField("")%>"/></td>
                                    <%}else{%>
                                    <td><input type="text" name="fecReal" id="fecReal" readonly="true" value="<%=HTMLHelper.displayField(fechaDia)%>"/></td>
                                    <td><input type="text" name="fecPago" id="Fecha2" value="<%=HTMLHelper.displayField(fechaDia)%>"/></td>
                                    <%}%>
                                    <td><input type="text" name="difDiasFechas" id="difDiasFechas" readonly="true" value="<%=HTMLHelper.displayField(0)%>"/></td>
                                    <td><input type="text" name="montoPagoTab" id="montoPagoTab" readonly="true" value="<%=HTMLHelper.formatCantidad(listTabla.get(t).getMontoPagar())%>"/></td>
                                    <td><input type="text" name="montoPagoCajas" id="montoPagoCajas" value="<%=HTMLHelper.displayField(0)%>"/></td>
                                    <td><input type="text" name="difMontos" id="difDiasFechas" readonly="true" value="<%=HTMLHelper.displayField(0)%>"/></td>
                                    <td><input type="text" name="ahorros" id="ahorros" value="<%=HTMLHelper.displayField(credito.getMontoCuenta()-credito.getMontoCuentaCongelada())%>"/></td>
                                    <td><input type="text" name="saldoAhorro" id="saldoAhorro" value="<%=HTMLHelper.formatCantidad(credito.getMontoCuenta())%>"/></td>
                                </tr>
                                <%
                                    }
                                    t++;
                                  }
                                %>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <tr><td>&nbsp;</td></tr>
                <tr><td><input type="button" value="Enviar" onclick="grabarControl()"</td></tr>
            </table>
        </form>
        <jsp:include page="footer.jsp" flush="true"/>
    </body>
</html>
