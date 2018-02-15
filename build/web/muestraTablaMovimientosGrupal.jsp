<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%>
<%@ page import="com.sicap.clientes.vo.GrupoVO"%>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<jsp:directive.page import="com.sicap.clientes.vo.CicloGrupalVO"/>
<jsp:directive.page import="com.sicap.clientes.util.GrupoUtil"/>
<%
    Integer idCiclo = (Integer) request.getAttribute("idCiclo");
    GrupoVO grupo = (GrupoVO) session.getAttribute("GRUPO");
    CicloGrupalVO ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo.intValue());
    double saldo = 0;
    double cargo = 0;
    double abono = 0;
    String evento = null;
    String fecha = null;
    boolean desplegar = false;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <title>Tabla de Movimientos Grupal</title>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">    
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">
        <!--
        <link rel="stylesheet" type="text/css" href="styles.css">
        -->
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <body leftmargin="0" topmargin="0">
        <form action="">
            <br>
            <center><h1>Tabla de movimientos</h1></center>
            <table border="1" cellpadding="0" cellspacing="0" width="50%" align="center">
                <tr>
                    <td>Equipo</td>
                    <td><%=HTMLHelper.displayField(grupo.idGrupo)%></td>
                </tr>
                <tr>
                    <td>Ciclo</td>
                    <td><%=HTMLHelper.displayField(ciclo.idCiclo)%></td>
                </tr>
                <tr>
                    <td>Nombre</td>
                    <td><%=HTMLHelper.displayField(grupo.nombre)%></td>
                </tr>
            </table>
            <br>
            <table border="1" cellpadding="0" cellspacing="0" width="50%" align="center">
                <tr>
                    <td>Capital Inicial</td>
                    <td align="right"><%=HTMLHelper.formatCantidad(ciclo.saldo.getMontoCredito(), false)%></td>
                    <td>Intereses Iniciales</td>
                    <td align="right"><%=HTMLHelper.formatCantidad((ciclo.saldo.getMontoConIntereses()-ciclo.saldo.getMontoCredito()), false)%></td>
                </tr>
                <tr>
                    <td>Saldo Inicial</td>
                    <td align="right"><%=HTMLHelper.formatCantidad(ciclo.saldo.getMontoConIntereses(), false)%></td>
                    <td>Saldo Restante</td>
                    <td align="right"><%=HTMLHelper.formatCantidad(ciclo.saldo.getSaldoConInteresAlFinal(), false)%></td>
                </tr>
            </table>
            <table border="0" width="100%">
                <tr>
                    <td align="center">
                        <br>
                        <table border="1" cellpadding="0" cellspacing="0" width="90%">
                            <tr>
                                <td width="15%" align="center">Fecha Movimiento</td>
                                <td width="15%" align="center">Evento</td>
                                <td width="15%" align="center">CARGO</td>
                                <td width="15%" align="center">ABONO</td>
                                <td width="15%" align="center">SALDO</td>
                            </tr>
                            <% for (int i = 0; ciclo.eventos != null && i < ciclo.eventos.length; i++) {
                                    desplegar = false;
                                    if (ciclo.eventos[i].tipoEvento.equalsIgnoreCase("DES")) {
                                        fecha = ciclo.eventos[i].fechaFin.toString();
                                        evento = "Desembolso";
                                        cargo = ciclo.eventos[i].monto;
                                        abono = 0;
                                        saldo = ciclo.eventos[i].saldo;
                                        desplegar = true;
                                    } else if (ciclo.eventos[i].tipoEvento.equalsIgnoreCase("INT")) {
                                        fecha = ciclo.eventos[i].fechaFin.toString();
                                        evento = "Intereses";
                                        cargo = ciclo.eventos[i].monto;
                                        abono = 0;
                                        saldo = ciclo.eventos[i].saldo;
                                        desplegar = true;
                                    } else if (ciclo.eventos[i].tipoEvento.equalsIgnoreCase("PAG")) {
                                        fecha = ciclo.eventos[i].fechaFin.toString();
                                        evento = "Aplicacion de pago";
                                        cargo = 0;
                                        abono = ciclo.eventos[i].monto;
                                        saldo = ciclo.eventos[i].saldo;
                                        desplegar = true;
                                    } else if (ciclo.eventos[i].tipoEvento.equalsIgnoreCase("DVO")) {
                                        fecha = ciclo.eventos[i].fechaFin.toString();
                                        evento = "Devolucion ODP";
                                        cargo = 0;
                                        abono = ciclo.eventos[i].monto;
                                        saldo = ciclo.eventos[i].saldo;
                                        desplegar = true;
                                    } else if (ciclo.eventos[i].tipoEvento.equalsIgnoreCase("MUL")) {
                                        fecha = ciclo.eventos[i].fechaFin.toString();
                                        evento = "Multa";
                                        cargo = ciclo.eventos[i].monto;
                                        abono = 0;
                                        saldo = ciclo.eventos[i].saldo;
                                        desplegar = true;
                                    } else if (ciclo.eventos[i].tipoEvento.equalsIgnoreCase("CON")) {
                                        fecha = ciclo.eventos[i].fechaFin.toString();
                                        evento = "Condonacion";
                                        cargo = 0;
                                        abono = ciclo.eventos[i].monto;
                                        saldo = ciclo.eventos[i].saldo;
                                        desplegar = true;
                                    } else if (ciclo.eventos[i].tipoEvento.equalsIgnoreCase("CAN_PAG")) {
                                        fecha = ciclo.eventos[i].fechaFin.toString();
                                        evento = "Cancelaci&oacuten de pago";
                                        cargo = ciclo.eventos[i].monto;;
                                        abono = 0;
                                        saldo = ciclo.eventos[i].saldo;
                                        desplegar = true;
                                    }
                                    if (desplegar) {
                            %>
                            <tr>
                                <td width="15%" align="center"><%= HTMLHelper.displayField(fecha)%></td>
                                <td width="15%" align="center"><%= HTMLHelper.displayField(evento)%></td>
                                <td width="15%" align="center"><%= HTMLHelper.formatCantidad(cargo, false)%></td>
                                <td width="15%" align="center"><%= HTMLHelper.formatCantidad(abono, false)%></td>
                                <td width="15%" align="center"><%= HTMLHelper.formatCantidad(saldo, false)%></td>
                            </tr>
                            <%	}
                                }
                            %>
                        </table>
                        <br>
                    </td>
                </tr>
                <tr>
                    <td align="center">
                        <input type="button" value="Cerrar" onclick="window.close();">
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>