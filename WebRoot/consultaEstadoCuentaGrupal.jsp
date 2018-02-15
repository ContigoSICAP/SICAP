<%@page import="com.sicap.clientes.vo.cartera.CreditoCartVO"%>
<%@page import="com.sicap.clientes.dao.cartera.CreditoCartDAO"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.util.inffinix.FormatInffinixUtil"%>
<%@ page import="java.sql.Date"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="com.sicap.clientes.vo.cartera.TablaAmortVO"%>
<%@ page import="com.sicap.clientes.dao.cartera.TablaAmortDAO"%>

<%
    int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
    GrupoVO grupo = (GrupoVO) session.getAttribute("GRUPO");
    CicloGrupalVO ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
    TreeMap estatusCredito = CatalogoHelper.getCatalogoEstatusCredito();
    TreeMap tasas = CatalogoHelper.getCatalogoTasas(grupo.idOperacion);
    TreeMap operaciones = CatalogoHelper.getCatalogo("C_OPERACIONES");
//	TreeMap catBancos = CatalogoHelper.getCatalogoBancosAfirme();
    TreeMap catBancos = CatalogoHelper.getCatalogo("C_BANCOS");
    SaldoIBSVO saldo = new SaldoIBSVO();
    Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
    TablaAmortVO[] tablaAmort = null;
    TablaAmortDAO tablaAmortDAO = new TablaAmortDAO();
    Date fechaUltimoPago = null;
    int noPagos = 0;
    int numGrupo = ciclo.idGrupo;
    int numSolicitud = idCiclo;
    CreditoCartDAO credito = new CreditoCartDAO();
    CreditoCartVO creditoVO = new CreditoCartVO();
    creditoVO = credito.getCreditoClienteSol(numGrupo, numSolicitud);
    if (ciclo.pagos != null) {
        if (ciclo.pagos.length > 0) {
            noPagos = ciclo.pagos.length - 1;
            fechaUltimoPago = ciclo.pagos[noPagos].fechaPago;
        }
    }
    if (noPagos > 0) {
        noPagos = noPagos + 1;
    }

    if (ciclo.saldo != null) {
        saldo = ciclo.saldo;
    }
    double tasaIva = 0.00;
    if (Convertidor.esFronterizo(grupo.sucursal)) {
        tasaIva = ClientesConstants.TASA_IVA_FRONTERIZO;
    } else {
        tasaIva = ClientesConstants.TASA_IVA_NO_FRONTERIZO;
    }

    double saldoVigente = saldo.getCapitalSigAmortizacion() + saldo.getInteresSigAmortizacion() + saldo.getIvaSigAmortizacion();
    double pagoCorriente = 0;
    double interesMora = saldo.getSaldoMora() + saldo.getSaldoIVAMora();
    double multa = saldo.getSaldoMulta() + saldo.getSaldoIVAMulta();
    double interesVencido = 0.00;
    double ivaInteresVencido = 0.00;

    if (ciclo.idCreditoIBS != 0) {
        if (ciclo.saldo.getEstatus() < ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO) {
            interesVencido = ciclo.saldo.getSaldoInteresVencido();
            ivaInteresVencido = ciclo.saldo.getIvaInteresVencido();
        } else {
            interesVencido = ciclo.saldo.getInteresVencido();
            ivaInteresVencido = ciclo.saldo.getIvaInteresVencido();
        }
        tablaAmort = tablaAmortDAO.getElementos(ciclo.idGrupo, ciclo.idCreditoIBS, 0);
        for (int j = 1; ciclo.tablaAmortizacion != null && j < ciclo.tablaAmortizacion.length; j++) {
            ciclo.tablaAmortizacion[j].abonoCapital = tablaAmort[j - 1].abonoCapital;
            ciclo.tablaAmortizacion[j].saldoCapital = tablaAmort[j - 1].saldoCapital;
            ciclo.tablaAmortizacion[j].interes = tablaAmort[j - 1].interes;
            ciclo.tablaAmortizacion[j].ivaInteres = tablaAmort[j - 1].ivaInteres;
            ciclo.tablaAmortizacion[j].montoPagar = tablaAmort[j - 1].montoPagar;
            ciclo.tablaAmortizacion[j].montoPagado = tablaAmort[j - 1].totalPagado;
            ciclo.tablaAmortizacion[j].capitalAnticipado = tablaAmort[j-1].capitalAnticipado;
            ciclo.tablaAmortizacion[j].incrementoCapital = tablaAmort[j-1].incrementoCapital;
        }

    } else {
        interesVencido = ciclo.saldo.getSaldoInteresVencido();
        ivaInteresVencido = ciclo.saldo.getIvaInteresVencido();
    }

    if (saldoVigente < saldo.getSaldoTotalAlDia()) {
        pagoCorriente = saldoVigente + saldo.getTotalVencido();
    } else {
        pagoCorriente = saldo.getSaldoTotalAlDia() + saldo.getTotalVencido();
    }
%>
<html>
    <head>
        <title>Consulta de estado de cuenta grupal</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script type="text/javascript">

            function regresaAGrupos(semDisp, estatusIC, estatusIC2) {
                if(semDisp == 2){
                    window.document.forma.command.value = 'consultaIntercicloAutorizado';
                    if(estatusIC > 2 && estatusIC < 10){
                        window.document.forma.command.value = 'consultaCicloGrupalIC';
                    } else if (estatusIC==1||estatusIC==0){
                        window.document.forma.command.value = 'consultaCicloGrupal';
                    }                        
                } else if(semDisp == 4){
                    window.document.forma.command.value = 'consultaIntercicloAutorizado';
                    if(estatusIC2 > 2 && estatusIC2 < 10){
                        window.document.forma.command.value = 'consultaCicloGrupalIC';
                    } else if (estatusIC2==1||estatusIC2==0){
                        window.document.forma.command.value = 'consultaCicloGrupal';
                    }
                }else 
                    window.document.forma.command.value = 'consultaCicloGrupal';
                window.document.forma.submit();
            }

            function ayudaConsultaPagosManuales(idCliente, idSolicitud, referencia) {
                params = "?command=consultaPagosManualesGrupal&idGrupo=" + idCliente + "&idCiclo=" + idSolicitud + "&referencia=" + referencia;
                url = "/CEC/controller";
                abreVentana(url + params, 'scrollbars=yes', screen.availWidth, screen.availHeight, true, 0, 0);
            }

            function usarSaldoFavor() {
                if (confirm("¿Deseas autorizar el saldo a favor de este ciclo?")) {
                    window.document.forma.command.value = 'usoSaldoFavor';
                    window.document.forma.submit();
                }
            }

            function CierraCiclo() {
                if (confirm("¿Deseas cerrar el ciclo?")) {
                    window.document.getElementById("botonCerrar").disabled = true;
                    window.document.forma.command.value = 'cierraCicloLiquidado';
                    window.document.forma.submit();
                }
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true" />
        <form name="forma" action="controller" method="POST">
            <input type="hidden" name="command"
                   value="consultaEstadoCuentaGrupal">
            <input type="hidden" name="idGrupo" value="<%=grupo.idGrupo%>">
            <input type="hidden" name="idCiclo" value="<%=ciclo.idCiclo%>">
            <table border="0" width="100%">
                <tr>
                    <td align="center">
                        <br>
                        <h3>
                            Consulta de estado de cuenta grupal
                        </h3>
                        <%=HTMLHelper.displayNotifications(notificaciones)%>
                        <table border="1" cellpadding="0" cellspacing="0" width="85%">
                            <tr>
                                <td width="15%" align="left">
                                    Sucursal
                                </td>
                                <td width="70%" align="left">
                                    <%=HTMLHelper.displayField(saldo.getNombreSucursal())%>
                                </td>
                            </tr>
                            <tr>
                                <td width="15%" align="left">
                                    Número de grupo
                                </td>
                                <td width="70%" align="left">
                                    <%=HTMLHelper.displayField(grupo.idGrupo)%>
                                </td>
                            </tr>
                            <tr>
                                <td width="15%" align="left">
                                    Número de ciclo
                                </td>
                                <td width="70%" align="left">
                                    <%=HTMLHelper.displayField(ciclo.idCiclo)%>
                                </td>
                            </tr>
                            <tr>
                                <td width="15%" align="left">
                                    Grupo
                                </td>
                                <td width="70%" align="left">
                                    <%=HTMLHelper.displayField(grupo.nombre)%>
                                </td>
                            </tr>
                            <tr>
                                <td width="15%" align="left">
                                    RFC
                                </td>
                                <td width="70%" align="left">
                                    <%=HTMLHelper.displayField(grupo.rfc)%>
                                </td>
                            </tr>
                            <tr>
                                <td width="15%" align="left">
                                    Producto
                                </td>
                                <td width="70%" align="left">
                                    <%=HTMLHelper.displayField((String) operaciones.get(ciclo.saldo.getIdProducto()))%>
                                </td>
                            </tr>
                            <tr>
                                <td width="15%" align="left">
                                    Referencia
                                </td>
                                <td width="70%" align="left">
                                    <%=HTMLHelper.displayField(ciclo.referencia)%>
                                </td>
                            </tr>
                            <tr>
                                <td width="15%" align="left">
                                    Estatus del cr&eacute;dito
                                </td>
                                <td width="70%" align="left">
                                    <%=HTMLHelper.displayField(CatalogoHelper.getDescripcionEstatusCredito(ciclo.saldo.getEstatus(), estatusCredito))%>
                                </td>
                            </tr>
                        </table>
                        <br>
                        <h2>
                            Saldo al d&iacute;a <%=HTMLHelper.displayField(ciclo.saldo.getFechaGeneracion())%>
                        </h2>
                        <br>
                        <h3>
                            Resumen
                        </h3>
                        <table width="65%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td bgcolor="#009861">
                                    <table width="100%" border="0" cellpadding="0" cellspacing="1">
                                        <tr bgcolor="FFFFFF">
                                            <td width="35%" align="right">
                                                Monto del Cr&eacute;dito
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.formatCantidad(ciclo.monto, false)%>
                                            </td>
                                            <td width="35%" align="right">
                                                Comisi&oacute;n
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.formatCantidad(ciclo.tablaAmortizacion[0].comisionInicial, false)%>
                                            </td>
                                        </tr>
                                        <tr bgcolor="FFFFFF">
                                            <td width="35%" align="right">
                                                Monto del Cr&eacute;dito con Seguro
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.formatCantidad(ciclo.saldo.getMontoCredito(), false)%>
                                            </td>
                                            <td width="35%" align="right">
                                                Pago anticipado inicial
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.formatCantidad(creditoVO.getMontoCuentaCongelada(), false)%>
                                            </td>
                                        </tr>
                                        <tr bgcolor="FFFFFF">
                                            <td width="35%" align="right">
                                                IVA comisi&oacute;n
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.formatCantidad(ciclo.tablaAmortizacion[0].ivaComision, false)%>
                                            </td>
                                            <td width="35%" align="right">
                                                Tasa mensual
                                            </td>
                                            <td width="15%" align="right">
                                                <%=CatalogoHelper.getDescripcionTasa(ciclo.tasa, tasas)%>
                                            </td>
                                        </tr>
                                        <tr bgcolor="FFFFFF">
                                            <td width="35%" align="right">
                                                Plazo
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.displayField(ciclo.saldo.getNumeroCuotas())%>
                                            </td>
                                            <td width="35%" align="right">
                                                Periodicidad
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.displayField(ciclo.saldo.getPeriodicidad())%>
                                            </td>
                                        </tr>
                                        <tr bgcolor="FFFFFF">
                                            <td width="35%" align="right">
                                                Fecha desembolso
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.displayField(FormatInffinixUtil.formatFechaInffinix(ciclo.saldo.getFechaDesembolso()))%>
                                            </td>
                                            <td width="35%" align="right">
                                                Fecha vencimiento
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.displayField(FormatInffinixUtil.formatFechaInffinix(ciclo.saldo.getFechaVencimiento()))%>
                                            </td>
                                        </tr>
                                        <tr bgcolor="FFFFFF">
                                            <td width="35%" align="right">
                                                Pagos realizados
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.displayField(noPagos)%>
                                            </td>
                                            <td width="35%" align="right">
                                                Monto pagado
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.formatCantidad(ciclo.saldo.getMontoTotalPagado(), false)%>
                                            </td>
                                        </tr>
                                        <tr bgcolor="FFFFFF">
                                            <td width="35%" align="right">
                                                Tasa IVA
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.displayField(tasaIva * 100 + "%")%>
                                            </td>
                                            <td width="35%" align="right"> 
                                                Saldo a favor 
                                            </td>
                                            <%
                                                if (request.isUserInRole("CORPORATIVO")) {
                                            %>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.formatCantidad(ciclo.saldo.getSaldoBucket(), false)%>
                                            </td>
                                            <%
                                            } else {
                                            %>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.formatCantidad(creditoVO.getMontoCuentaCongelada(), false)%>
                                            </td>
                                            <%
                                                }
                                            %>
                                        </tr>
                                        <tr bgcolor="FFFFFF">
                                            <td width="35%" align="right">
                                                Fecha &uacute;ltimo pago
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.displayField(fechaUltimoPago)%>
                                            </td>
                                            <td width="35%" align="right">
                                                Fecha siguiente pago
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.displayField(FormatInffinixUtil.formatFechaInffinix(ciclo.saldo.getFechaSigAmortizacion()))%>
                                            </td>
                                        </tr>
                                        <tr bgcolor="FFFFFF">
                                            <td width="35%" align="right">
                                                D&iacute;as mora
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.displayField(ciclo.saldo.getDiasMora())%>
                                            </td>
                                            <td width="35%" align="right">
                                                Siguiente pago
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.formatCantidad(FormatUtil.formatSaldo(pagoCorriente), false)%>
                                            </td>
                                        </tr>
                                        <tr bgcolor="FFFFFF">
                                            <td width="35%" align="right">
                                                Saldo vencido
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.formatCantidad(ciclo.saldo.getTotalVencido(), false)%>
                                            </td>
                                            <td width="35%" align="right">
                                                Saldo a liquidar
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.formatCantidad(FormatUtil.formatSaldo(Math.ceil(ciclo.saldo.getSaldoConInteresAlFinal())), false)%>
                                            </td>
                                        </tr>
                                        <tr bgcolor="FFFFFF">
                                            <td width="35%" align="right">&nbsp;</td>
                                            <td width="15%" align="right">&nbsp;</td>
                                            <td width="35%" align="right">
                                                Saldo al d&iacute;a
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.formatCantidad(FormatUtil.formatSaldo(Math.ceil(ciclo.saldo.getSaldoTotalAlDia())), false)%>
                                            </td>
                                        </tr>

                                        <tr bgcolor="FFFFFF">
                                            <td width="35%" align="right">
                                                Capital Vencido
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.formatCantidad(ciclo.saldo.getCapitalVencido(), false)%>
                                            </td>
                                            <td width="35%" align="right">
                                                Interés Vencido (incluye IVA)
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.formatCantidad(FormatUtil.formatSaldo(Convertidor.getMontoIva(interesVencido, saldo.getIdSucursal(), 0)), false)%>
                                            </td>
                                        </tr>
                                        <tr bgcolor="FFFFFF">
                                            <td width="35%" align="right">
                                                Interés Moratorio (Incluye IVA)
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.formatCantidad(FormatUtil.formatSaldo(interesMora), false)%>
                                            </td>
                                            <td width="35%" align="right">
                                                Multa (Incluye IVA)
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.formatCantidad(FormatUtil.formatSaldo(multa), false)%>
                                            </td>
                                        </tr>
                                        <tr bgcolor="FFFFFF">
                                            <td width="35%" align="right">
                                                Interés Moratorio Pagado(Incluye IVA)
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.formatCantidad(FormatUtil.formatSaldo(ciclo.saldo.getMoratorioPagado()), false)%>
                                            </td>
                                            <td width="35%" align="right">
                                                Multa pagada(Incluye IVA)
                                            </td>
                                            <td width="15%" align="right">
                                                <%=HTMLHelper.formatCantidad(FormatUtil.formatSaldo(ciclo.saldo.getMultaPagada() + ciclo.saldo.getIvaMultaPagado()), false)%>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>

                        <br>
                        <h3>
                            Tabla de amortizaci&oacute;n
                        </h3>
                        <table width="78%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td bgcolor="#009861">
                                    <table width="100%" border="0" cellpadding="0" cellspacing="1">
                                        <tr bgcolor="FFFFFF">
                                            <td width="7%" align="center">
                                                No. de pago
                                            </td>
                                            <td width="7%" align="center">
                                                Fecha
                                            </td>
                                            <td width="7%" align="center">
                                                Saldo inicial
                                            </td>
                                            <td width="7%" align="center">
                                                Abono de capital
                                            </td>
                                            <td width="7%" align="center">
                                                Saldo de capital
                                            </td>
                                            <td width="7%" align="center">
                                                Comis&oacute;n inicial
                                            </td>
                                            <td width="7%" align="center">
                                                IVA de comisi&oacute;n
                                            </td>
                                            <td width="7%" align="center">
                                                Intereses
                                            </td>
                                            <td width="7%" align="center">
                                                IVA de inter&eacute;s
                                            </td>
                                            <td width="7%" align="center">
                                                Monto a pagar
                                            </td>
                                            <td width="7%" align="center">
                                                Monto pagado
                                            </td>
                                            <td width="8%" align="center">
                                                Incremento Capital&nbsp;
                                            </td>
                                            <td width="8%" align="center">
                                                Decremento Capital&nbsp;
                                            </td>
                                        </tr>
                                        <%
                                            for (int i = 0; ciclo.tablaAmortizacion != null && i < ciclo.tablaAmortizacion.length; i++) {
                                        %>
                                        <tr bgcolor="FFFFFF">
                                            <td width="10%" align="center">
                                                <%=HTMLHelper.displayField(ciclo.tablaAmortizacion[i].numPago)%>
                                            </td>
                                            <td width="10%" align="center">
                                                <%=HTMLHelper.displayField(ciclo.tablaAmortizacion[i].fechaPago)%>
                                            </td>
                                            <td width="10%" align="center">
                                                    <%=HTMLHelper.formatCantidad(ciclo.tablaAmortizacion[i].saldoInicial, false)%>
                                            </td>
                                            <td width="10%" align="center">
                                                <%=HTMLHelper.formatCantidad(ciclo.tablaAmortizacion[i].abonoCapital, false)%>
                                            </td>
                                            <td width="10%" align="center">
                                                <%=HTMLHelper.formatCantidad(ciclo.tablaAmortizacion[i].saldoCapital, false)%>
                                            </td>
                                            <td width="10%" align="center">
                                                <%=HTMLHelper.formatCantidad(ciclo.tablaAmortizacion[i].comisionInicial, false)%>
                                            </td>
                                            <td width="10%" align="center">
                                                <%=HTMLHelper.formatCantidad(ciclo.tablaAmortizacion[i].ivaComision, false)%>
                                            </td>
                                            <td width="10%" align="center">
                                                <%=HTMLHelper.formatCantidad(ciclo.tablaAmortizacion[i].interes, false)%>
                                            </td>
                                            <td width="10%" align="center">
                                                <%=HTMLHelper.formatCantidad(ciclo.tablaAmortizacion[i].ivaInteres, false)%>
                                            </td>
                                            <td width="10%" align="center">
                                                <%=HTMLHelper.formatCantidad(ciclo.tablaAmortizacion[i].montoPagar, false)%>
                                            </td>
                                            <td width="10%" align="center">
                                                <%=HTMLHelper.formatCantidad(ciclo.tablaAmortizacion[i].montoPagado, false)%>
                                            </td>
                                            <td width="10%" align="center">
                                                <%=HTMLHelper.formatCantidad(ciclo.tablaAmortizacion[i].incrementoCapital, false)%>
                                            </td>
                                            <td width="10%" align="center">
                                                <%=HTMLHelper.formatCantidad(ciclo.tablaAmortizacion[i].capitalAnticipado, false)%>
                                            </td>
                                        </tr>
                                        <%
                                            }
                                        %>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td width="100%" align="center" colspan="2"></td>
                            </tr>
                        </table>


                        <%if (ciclo.idCreditoIBS == 0 && ciclo.idCuentaIBS == 0) {%>
                        <br>
                        <h3>
                            Pagos concentradora
                        </h3>
                        <table width="65%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td bgcolor="#009861">
                                    <table width="100%" border="0" cellpadding="0" cellspacing="1">
                                        <tr bgcolor="FFFFFF">
                                            <td width="35%" align="center">
                                                Fecha
                                            </td>
                                            <td width="35%" align="center">
                                                Monto
                                            </td>
                                            <td width="35%" align="center">
                                                Sts Ref
                                            </td>
                                            <td width="35%" align="center">
                                                Banco
                                            </td>
                                        </tr>
                                        <%
                                            for (int i = 0; ciclo.pagosConcentradora != null && i < ciclo.pagosConcentradora.length; i++) {
                                        %>
                                        <tr bgcolor="FFFFFF">
                                            <td width="35%" align="center">
                                                <%=HTMLHelper.displayField(ciclo.pagosConcentradora[i].fecha)%>
                                            </td>
                                            <td width="15%" align="center">
                                                <%=HTMLHelper.formatCantidad(ciclo.pagosConcentradora[i].deposito, false)%>
                                            </td>
                                            <td width="35%" align="center">
                                                <%=HTMLHelper.displayField(ciclo.pagosConcentradora[i].stsRef)%>
                                            </td>
                                            <td width="15%" align="center">
                                                <%=HTMLHelper.displayField(ciclo.pagosConcentradora[i].descripcionBanco)%>
                                            </td>
                                        </tr>
                                        <%
                                            }
                                        %>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td width="100%" align="center" colspan="2"></td>
                            </tr>
                        </table>



                    </td>
                </tr>
                <tr>
                    <td align="center">
                        <br>
                        <br>
                        <a href="javascript:history.back(1)">Regresar</a>
                    </td>
                </tr>
                <tr>
                    <td width="100%" align="center" colspan="2">
                        <br>
                        <a href="#"	onClick="ayudaConsultaPagosManuales(<%=grupo.idGrupo%>,<%=request.getParameter("idCiclo")%>, '<%=ciclo.referencia%>')">Consulta de aplicaci&oacute;n de pagos e intereses</a>
                    </td>
                </tr>
            </table>
            <%} else if (ciclo.idCreditoIBS != 0) {%>
            <table border="0" width="100%">
                <tr>
                    <td align="center">
                        <br>
                        <h2>Tabla de movimientos</h2>
                        <table border="1" cellpadding="0" cellspacing="0" width="90%">
                            <tr>
                                <%-- 							
                                                                                                <td width="15%" align="center">Fecha dep&oacute;sito</td>
                                                                                                <td width="15%" align="center">Fecha aplicaci&oacute;n</td>
                                                                                                <td width="15%" align="center">Monto</td>
                                                                                                <td width="15%" align="center">Tipo movimiento</td>
                                                                                                <td width="15%" align="center">Banco</td>
                                                                                                <td width="15%" align="center">Saldo</td>
                                --%>							
                                <td width="15%" align="center">Fecha dep&oacute;sito</td>
                                <td width="15%" align="center">Monto</td>
                                <td width="15%" align="center">Banco</td>
                            </tr>
                            <%-- Se retira la referencia a IBS JBL JUL/10 							
                                                                                    <%for(int i = 0;ciclo.pagosIBS !=null && i < ciclo.pagosIBS.length; i++){
                                                                                    String fechaDeposito = FechasUtil.formatDateSlashes(ciclo.pagosIBS[i].getFechaDeposito(),2);
                                                                                    String fechaTransaccion = FechasUtil.formatDateSlashes(ciclo.pagosIBS[i].getFechaTransaccion(),2);
                                                                                    
                                                                                    %>
                            --%>
                            <%for (int i = 0; ciclo.pagos != null && i < ciclo.pagos.length; i++) {
                                    String fechaDeposito = Convertidor.dateToString(ciclo.pagos[i].fechaPago);
                                    String fechaTransaccion = Convertidor.dateToString(ciclo.pagos[i].fechaPago);
                            %>
                            <tr>
                                <%-- 
                                                                                                <td width="15%" align="center"><%= HTMLHelper.displayField(fechaDeposito)%></td>
                                                                                                <td width="15%" align="center"><%= HTMLHelper.displayField(fechaTransaccion)%></td>
                                                                                                <td width="15%" align="center"><%= HTMLHelper.displayField(ciclo.pagosIBS[i].getMontoTransaccion())%></td>
                                                                                                <td width="15%" align="center"><%= HTMLHelper.displayField(ciclo.pagosIBS[i].getTipoTransaccion())%></td>
                                                                                                <td width="15%" align="center"><%= HTMLHelper.getDescripcion(catBancos, ciclo.pagosIBS[i].getCodigoBanco())%></td>
                                                                                                <td width="15%" align="center"><%= HTMLHelper.displayField(ciclo.pagosIBS[i].getSaldoCuenta())%></td>
                                      JBL SEP/10   --%>
                                <td width="15%" align="center"><%= HTMLHelper.displayField(fechaDeposito)%></td>
                                <td width="15%" align="center"><%= HTMLHelper.displayField(ciclo.pagos[i].monto)%></td>
                                <td width="15%" align="center"><%= HTMLHelper.getDescripcion(catBancos, ciclo.pagos[i].bancoReferencia)%></td>
                            </tr>
                            <%}%>
                        </table>
                        <br>
                    </td>
                </tr>
                <tr>
                    <td width="100%" align="center" colspan="2">
                        <br>
                        <a href="#"	onClick="ayudaConsultaPagosManuales(<%=request.getParameter("idGrupo")%>,<%=request.getParameter("idCiclo")%>, '<%=ciclo.referencia%>')">Consulta de aplicaci&oacute;n de pagos e intereses</a>
                    </td>
                </tr>
                <%if (request.isUserInRole("ADM_PAGOS")) {%> 
                <tr>
                    <td width="100%" align="center" colspan="2">
                        <br/>
                        <a href="#"	onClick="usarSaldoFavor()">Permitir uso de Saldo a Favor</a>
                        <input type="hidden" name="idGrupoSF" value="<%=request.getParameter("idGrupo")%>">
                        <input type="hidden" name="numCicloSF" value="<%=request.getParameter("idCiclo")%>">
                        <br/>
                        <br/>
                    </td>
                </tr>
                <%}%>
                <tr>
                    <td width="100%" align="center" colspan="2">
                        <input type="button" value="Regresar" onClick="regresaAGrupos(<%=ciclo.semDisp%>,<%=ciclo.estatusIC%>,<%=ciclo.estatusIC2%>)">
                        <%if (ciclo.saldo.getEstatus() == 3 && ciclo.estatus == ClientesConstants.CICLO_DISPERSADO && request.isUserInRole("ADM_PAGOS")) {%>
                        <input type="button" id="botonCerrar" value="Cerrar Ciclo" onClick="CierraCiclo()">
                        <%}%>
                    </td>
                </tr>
            </table>

            <%}%>			

        </form>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
