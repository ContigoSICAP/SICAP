<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.SolicitudVO"%>
<%@ page import="com.sicap.clientes.vo.PagoVO"%>
<%@ page import="com.sicap.clientes.vo.SaldoIBSVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.util.inffinix.FormatInffinixUtil"%>
<%@ page import="java.sql.Date"%>
<%@ page import="java.util.TreeMap"%>
<%
	ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
	int indiceSolicitud = ((Integer)request.getAttribute("indiceSolicitud")).intValue();
	SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
	TreeMap estatusCredito = CatalogoHelper.getCatalogoEstatusCredito();
	TreeMap tasas = CatalogoHelper.getCatalogoTasas(solicitud.tipoOperacion);
	TreeMap operaciones = CatalogoHelper.getCatalogo("C_OPERACIONES");
//	TreeMap catBancos = CatalogoHelper.getCatalogoBancosAfirme();
	TreeMap catBancos = CatalogoHelper.getCatalogo("C_BANCOS");
	
	Date fechaUltimoPago = null;
	if (solicitud == null) {
		solicitud = new SolicitudVO();
		solicitud.saldo = new SaldoIBSVO();
		solicitud.pagos = new PagoVO[1];
		solicitud.referencia = "";
	}
	if (solicitud.saldo == null)
		solicitud.saldo = new SaldoIBSVO();
	if (solicitud.referencia == null)
		solicitud.referencia = "";
	int longPagos = 0;
	int cantPagos = 0;
	if(solicitud.pagos!=null){
		if (solicitud.pagos.length > 0){		
			longPagos = solicitud.pagos.length-1;
			cantPagos = solicitud.pagos.length;
			fechaUltimoPago = solicitud.pagos[longPagos].fechaPago;
		}
	}
	double saldoVigente = solicitud.saldo.getCapitalSigAmortizacion()+ solicitud.saldo.getInteresSigAmortizacion() + solicitud.saldo.getIvaSigAmortizacion();
	double pagoCorriente = 0;
	double interesMora = solicitud.saldo.getSaldoMora() + solicitud.saldo.getSaldoIVAMora();
	double multa = solicitud.saldo.getSaldoMulta() + solicitud.saldo.getSaldoIVAMulta();
	double interesVencido = 0.00;
	double tasaIva = 0.00;
        if(solicitud.saldo.getReferencia()!=null){
            tasaIva = solicitud.saldo.getTasaIVA();
        }
	
	if ( solicitud.idCreditoIBS != 0) {
		if (solicitud.saldo.getEstatus() < ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO) {
			interesVencido = solicitud.saldo.getSaldoInteresVencido();
		} else {
			interesVencido = solicitud.saldo.getInteresVencido();
		}
	} else
		interesVencido = solicitud.saldo.getSaldoInteresVencido();
		
	if(saldoVigente < solicitud.saldo.getSaldoTotalAlDia())
		pagoCorriente = saldoVigente + solicitud.saldo.getTotalVencido();
	else
		pagoCorriente = solicitud.saldo.getSaldoTotalAlDia() + solicitud.saldo.getTotalVencido();
	Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
%>
<html>
	<head>
		<title>Consulta de estado de cuenta</title>
		<script language="Javascript" src="./js/functions.js"></script>
		<script type="text/javascript">
	function ayudaConsultaPagosManuales(idCliente, idSolicitud, referencia){
		params ="?command=consultaPagosManuales&idCliente=" + idCliente +"&idSolicitud=" + idSolicitud + "&referencia=" + referencia;
		url = "/CEC/controller";
		abreVentana(url + params,'scrollbars=yes',screen.availWidth ,screen.availHeight ,true, 0, 0);
	}
</script>
		<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
	</head>
	<body leftmargin="0" topmargin="0">
		<jsp:include page="header.jsp" flush="true" />
		<jsp:include page="menuIzquierdo.jsp" flush="true" />
		<form name="forma" action="controller" method="POST">
			<input type="hidden" name="command" value="">
			<input type="hidden" name="idCliente" value="<%=cliente.idCliente%>">
			<input type="hidden" name="idSolicitud"	value="<%=request.getParameter("idSolicitud")%>">
			<table border="0" width="100%">
				<tr>
					<td align="center">
						<br>
						<h3>
							Consulta de estado de cuenta
						</h3>
						<%=HTMLHelper.displayNotifications(notificaciones)%>
						<table border="1" cellpadding="0" cellspacing="0" width="85%">
                                                        <tr>
								<td width="15%" align="left">
									Sucursal
								</td>
								<td width="70%" align="left">
									<%=HTMLHelper.displayField(solicitud.saldo.getNombreSucursal())%>
								</td>
							</tr>
							<tr>
								<td width="15%" align="left">
									Número de cliente
								</td>
								<td width="70%" align="left">
									<%=HTMLHelper.displayField(cliente.idCliente)%>
								</td>
							</tr>
							<tr>
								<td width="15%" align="left">
									Número de solicitud
								</td>
								<td width="70%" align="left">
									<%=HTMLHelper.displayField(solicitud.idSolicitud)%>
								</td>
							</tr>
							<tr>
								<td width="15%" align="left">
									Cliente
								</td>
								<td width="70%" align="left">
									<%=HTMLHelper.displayField(cliente.nombreCompleto)%>
								</td>
							</tr>
							<tr>
								<td width="15%" align="left">
									RFC
								</td>
								<td width="70%" align="left">
									<%=HTMLHelper.displayField(cliente.rfc)%>
								</td>
							</tr>
							<tr>
								<td width="15%" align="left">
									Producto
								</td>
								<td width="70%" align="left">
									<%=HTMLHelper.displayField((String)operaciones.get(solicitud.saldo.getIdProducto()))%>
								</td>
							</tr>
							<tr>
								<td width="15%" align="left">
									Referencia
								</td>
								<td width="70%" align="left">
									<%=HTMLHelper.displayField(solicitud.referencia)%>
								</td>
							</tr>
							<tr>
								<td width="15%" align="left">
									Estatus del cr&eacute;dito
								</td>
								<td width="70%" align="left">
									<%=HTMLHelper.displayField((String)estatusCredito.get(solicitud.saldo.getEstatus()))%>
								</td>
							</tr>
						</table>
						<br>
						<h2>
							Saldo al d&iacute;a <%=HTMLHelper.displayField(solicitud.saldo.getFechaGeneracion())%>
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
												<%=HTMLHelper.formatCantidad(solicitud.saldo.getMontoCredito(),	false)%>
											</td>
											<td width="35%" align="right">
												Comisi&oacute;n
											</td>
											<td width="15%" align="right">
												<%=HTMLHelper.formatCantidad(solicitud.amortizacion[0].comisionInicial, false)%>
											</td>
										</tr>
										<tr bgcolor="FFFFFF">
											<td width="35%" align="right">
												IVA comisi&oacute;n
											</td>
											<td width="15%" align="right">
												<%=HTMLHelper.formatCantidad(solicitud.amortizacion[0].ivaComision,false)%>
											</td>
											<td width="35%" align="right">
												Tasa mensual
											</td>
											<td width="15%" align="right">
												<%=CatalogoHelper.getDescripcionTasa(solicitud.decisionComite.tasa, tasas)%>
											</td>
										</tr>
										<tr bgcolor="FFFFFF">
											<td width="35%" align="right">
												Plazo
											</td>
											<td width="15%" align="right">
												<%=HTMLHelper.displayField(solicitud.saldo.getNumeroCuotas())%>
											</td>
											<td width="35%" align="right">
												Periodicidad
											</td>
											<td width="15%" align="right">
												<%=HTMLHelper.displayField(solicitud.saldo.getPeriodicidad())%>
											</td>
										</tr>
										<tr bgcolor="FFFFFF">
											<td width="35%" align="right">
												Fecha desembolso
											</td>
											<td width="15%" align="right">
												<%=HTMLHelper.displayField(FormatInffinixUtil.formatFechaInffinix(solicitud.saldo.getFechaDesembolso()))%>
											</td>
											<td width="35%" align="right">
												Fecha vencimiento
											</td>
											<td width="15%" align="right">
												<%=HTMLHelper.displayField(FormatInffinixUtil.formatFechaInffinix(solicitud.saldo.getFechaVencimiento()))%>
											</td>
										</tr>
										<tr bgcolor="FFFFFF">
											<td width="35%" align="right">
												Pagos realizados
											</td>
											<td width="15%" align="right">
												<%=HTMLHelper.displayField(solicitud.saldo.getNumeroPagosRealizados())%>
											</td>
											<td width="35%" align="right">
												Monto pagado
											</td>
											<td width="15%" align="right">
												<%=HTMLHelper.formatCantidad(solicitud.saldo.getMontoTotalPagado() , false)%>
											</td>
										</tr>
										<tr bgcolor="FFFFFF">
											<td width="35%" align="right">
												Tasa IVA
											</td>
											<td width="15%" align="right">
												<%=HTMLHelper.displayField(tasaIva+"%")%>
											</td>
											<td width="35%" align="right"> 
												Saldo a favor 
											</td>
											<td width="15%" align="right">
												<%=HTMLHelper.formatCantidad(solicitud.saldo.getSaldoBucket() , false)%>
											</td>
										</tr>
										<tr bgcolor="FFFFFF">
											<td width="35%" align="right">
												Fecha &uacute;ltimo pago
											</td>
											<td width="15%" align="right">
												<%=HTMLHelper.displayField(solicitud.saldo.getFechaUltimoPago())%>
											</td>
											<td width="35%" align="right">
												Fecha siguiente pago
											</td>
											<td width="15%" align="right">
												<%=HTMLHelper.displayField(FormatInffinixUtil.formatFechaInffinix(solicitud.saldo.getFechaSigAmortizacion()))%>
											</td>
										</tr>
										<tr bgcolor="FFFFFF">
											<td width="35%" align="right">
												D&iacute;as mora
											</td>
											<td width="15%" align="right">
												<%=HTMLHelper.displayField(solicitud.saldo.getDiasMora())%>
											</td>
											<td width="35%" align="right">
												Siguiente pago
											</td>
											<td width="15%" align="right">
												<%=HTMLHelper.formatCantidad(FormatUtil.formatSaldo(pagoCorriente) , false)%>
											</td>
										</tr>
										<tr bgcolor="FFFFFF">
                                                                                        <td width="35%" align="right">&nbsp;</td>
											<td width="15%" align="right">&nbsp;</td>
											<td width="35%" align="right">
                                                                                            Saldo del cr&eacute;dito
                                                                                        </td>
											<td width="15%" align="right">
                                                                                            <%=HTMLHelper.formatCantidad(FormatUtil.formatSaldo(solicitud.saldo.getSaldoConInteresAlFinal()),false)%>
                                                                                        </td>
										</tr>
										<tr bgcolor="FFFFFF">
											<td width="35%" align="right">
												Saldo vencido
											</td>
											<td width="15%" align="right">
												<%=HTMLHelper.formatCantidad(solicitud.saldo.getTotalVencido(),false)%>
											</td>
											<td width="35%" align="right">
                                                                                                Saldo al d&iacute;a
											</td>
											<td width="15%" align="right">
												<%=HTMLHelper.formatCantidad(FormatUtil.formatSaldo(solicitud.saldo.getSaldoTotalAlDia()),false)%>
											</td>
										</tr>
										<tr bgcolor="FFFFFF">
											<td width="35%" align="right">
												Capital Vencido
											</td>
											<td width="15%" align="right">
												<%=HTMLHelper.formatCantidad(solicitud.saldo.getCapitalVencido(),false)%>
											</td>
											<td width="35%" align="right">
												Interés Vencido (Incluye IVA)
											</td>
											<td width="15%" align="right">
												<%=HTMLHelper.formatCantidad(FormatUtil.formatSaldo(solicitud.saldo.getInteresVencido()+solicitud.saldo.getIvaInteresVencido()),	false)%>
											</td>
										</tr>
										<tr bgcolor="FFFFFF">
											<td width="35%" align="right">
												Interés Moratorio (Incluye IVA)
											</td>
											<td width="15%" align="right">
												<%=HTMLHelper.formatCantidad(FormatUtil.formatSaldo(solicitud.saldo.getSaldoMora() + solicitud.saldo.getSaldoIVAMora()),false)%>
											</td>
											<td width="35%" align="right">
												Multa (Incluye IVA)
											</td>
											<td width="15%" align="right">
												<%=HTMLHelper.formatCantidad(FormatUtil.formatSaldo(solicitud.saldo.getSaldoMulta() + solicitud.saldo.getSaldoIVAMulta()),	false)%>
											</td>
										</tr>
										<tr bgcolor="FFFFFF">
											<td width="35%" align="right">
												Interés Moratorio Pagado(Incluye IVA)
											</td>
											<td width="15%" align="right">
												<%=HTMLHelper.formatCantidad(FormatUtil.formatSaldo(solicitud.saldo.getMoratorioPagado()),false)%>
											</td>
											<td width="35%" align="right">
												Multa pagada(Incluye IVA)
											</td>
											<td width="15%" align="right">
												<%=HTMLHelper.formatCantidad(FormatUtil.formatSaldo(solicitud.saldo.getMultaPagada()+solicitud.saldo.getIvaMultaPagado()),	false)%>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td width="100%" align="center" colspan="2"></td>
							</tr>
							
						</table>
						
						<br>
						<h3>
							Tabla de amortizaci&oacute;n
						</h3>
						<table width="65%" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td bgcolor="#009861">
									<table width="100%" border="0" cellpadding="0" cellspacing="1">
										<tr bgcolor="FFFFFF">
											<td width="10%" align="center">
												No. de pago
											</td>
											<td width="10%" align="center">
												Fecha
											</td>
											<td width="10%" align="center">
												Saldo inicial
											</td>
											<td width="10%" align="center">
												Abono de capital
											</td>
											<td width="10%" align="center">
												Saldo de capital
											</td>
											<td width="10%" align="center">
												Comis&oacute;n inicial
											</td>
											<td width="10%" align="center">
												IVA de comisi&oacute;n
											</td>
											<td width="10%" align="center">
												Intereses
											</td>
											<td width="10%" align="center">
												IVA de inter&eacute;s
											</td>
											<td width="10%" align="center">
												Monto a pagar
											</td>
										</tr>
                                                                                <%for (int i = 0;solicitud.amortizacion!=null && i < solicitud.amortizacion.length; i++){%>
										<tr bgcolor="FFFFFF">
											<td width="10%" align="center">
												<%=HTMLHelper.displayField(solicitud.amortizacion[i].numPago)%>
											</td>
											<td width="10%" align="center">
												<%=HTMLHelper.displayField(solicitud.amortizacion[i].fechaPago)%>
											</td>
											<td width="10%" align="center">
												<%=HTMLHelper.formatCantidad(solicitud.amortizacion[i].saldoInicial, false)%>
											</td>
											<td width="10%" align="center">
												<%=HTMLHelper.formatCantidad(solicitud.amortizacion[i].abonoCapital, false)%>
											</td>
											<td width="10%" align="center">
												<%=HTMLHelper.formatCantidad(solicitud.amortizacion[i].saldoCapital, false)%>
											</td>
											<td width="10%" align="center">
												<%=HTMLHelper.formatCantidad(solicitud.amortizacion[i].comisionInicial, false)%>
											</td>
											<td width="10%" align="center">
												<%=HTMLHelper.formatCantidad(solicitud.amortizacion[i].ivaComision, false)%>
											</td>
											<td width="10%" align="center">
												<%=HTMLHelper.formatCantidad(solicitud.amortizacion[i].interes, false)%>
											</td>
											<td width="10%" align="center">
												<%=HTMLHelper.formatCantidad(solicitud.amortizacion[i].ivaInteres, false)%>
											</td>
											<td width="10%" align="center">
												<%=HTMLHelper.formatCantidad(solicitud.amortizacion[i].montoPagar, false)%>
											</td>
										</tr>
										<%} %>
									</table>
								</td>
							</tr>
							<tr>
								<td width="100%" align="center" colspan="2"></td>
							</tr>	
						</table>
						
						
<%if ( solicitud.idCreditoIBS == 0 && solicitud.idCuentaIBS == 0 ) {%>						
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
										<% for (int i = 0;solicitud.pagosConcentradora!=null && i < solicitud.pagosConcentradora.length; i++){%>
										<tr bgcolor="FFFFFF">
											<td width="35%" align="center">
												<%=HTMLHelper.displayField(solicitud.pagosConcentradora[i].fecha)%>
											</td>
											<td width="15%" align="center">
												<%=HTMLHelper.formatCantidad(solicitud.pagosConcentradora[i].deposito, false)%>
											</td>
											<td width="35%" align="center">
												<%=HTMLHelper.displayField(solicitud.pagosConcentradora[i].stsRef)%>
											</td>
											<td width="15%" align="center">
												<%=HTMLHelper.displayField(solicitud.pagosConcentradora[i].descripcionBanco)%>
											</td>
										</tr>
										<%} %>
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
					<td width="100%" align="center" colspan="2">
						<br>
						<a href="#"	onClick="ayudaConsultaPagosManuales(<%=request.getParameter("idCliente")%>,<%=request.getParameter("idSolicitud")%>,'<%=solicitud.referencia%>')">Consulta de aplicaci&oacute;n de pagos e intereses</a>
					</td>
				</tr>
			</table>
			
<%}else if ( solicitud.idCreditoIBS != 0 ){ %>			
			<table border="0" width="100%">
				<tr>
					<td align="center">
						<br>
						<h2>Tabla de movimientos</h2>
						<table border="1" cellpadding="0" cellspacing="0" width="90%">
							<tr>
							
								<td width="15%" align="center">Fecha dep&oacute;sito</td>
<%-- 								<td width="15%" align="center">Fecha aplicaci&oacute;n</td>   --%>
								<td width="15%" align="center">Monto</td>
<%-- 	JBL JUL/10				<td width="15%" align="center">Tipo movimiento</td>      --%>
								<td width="15%" align="center">Banco</td>
<%-- 	JBL JUL/10				<td width="15%" align="center">Saldo</td>				--%>
							</tr>
<%-- Se retira la referencia a IBS JBL JUL/10 							
							<%for(int i=0 ; solicitud.pagosIBS!=null && i<solicitud.pagosIBS.length ; i++ ){
							String fechaDeposito = FechasUtil.formatDateSlashes(solicitud.pagosIBS[i].getFechaDeposito(),2);
							String fechaTransaccion = FechasUtil.formatDateSlashes(solicitud.pagosIBS[i].getFechaTransaccion(),2);
							%>
--%>							
							<%for(int i=0 ; solicitud.pagos!=null && i<solicitud.pagos.length ; i++ ){
							String fechaDeposito = Convertidor.dateToString(solicitud.pagos[i].fechaPago);
							String fechaTransaccion = Convertidor.dateToString(solicitud.pagos[i].fechaPago);
							%>
							<tr>
<%-- Se saca la referencia a IBS JBL-JUL/10
								<td width="15%" align="center"><%= HTMLHelper.displayField(fechaDeposito)%></td>
								<td width="15%" align="center"><%= HTMLHelper.displayField(fechaTransaccion)%></td>
								<td width="15%" align="center"><%= HTMLHelper.displayField(solicitud.pagosIBS[i].getMontoTransaccion())%></td>
								<td width="15%" align="center"><%= HTMLHelper.displayField(solicitud.pagosIBS[i].getTipoTransaccion())%></td>
								<td width="15%" align="center"><%= HTMLHelper.getDescripcion(catBancos, solicitud.pagosIBS[i].getCodigoBanco())%></td>
								<td width="15%" align="center"><%= HTMLHelper.displayField(solicitud.pagosIBS[i].getSaldoCuenta())%></td>
--%>							
								<td width="15%" align="center"><%= HTMLHelper.displayField(fechaDeposito)%></td>
								<td width="15%" align="center"><%= HTMLHelper.displayField(solicitud.pagos[i].monto)%></td>
								<td width="15%" align="center"><%= HTMLHelper.getDescripcion(catBancos, solicitud.pagos[i].bancoReferencia)%></td>
							</tr>
							<%}%>
						</table>
						<br>
					</td>
				</tr>
				<tr>
					<td width="100%" align="center" colspan="2">
						<br>
						<a href="#"	onClick="ayudaConsultaPagosManuales(<%=request.getParameter("idCliente")%>,<%=request.getParameter("idSolicitud")%>,'<%=solicitud.referencia%>')">Consulta de aplicaci&oacute;n de pagos e intereses</a>
					</td>
				</tr>
			</table>
			
<%} %>			
		<center><input type="button" value="Imprimir" onclick="window.print();return false;" /></center>
		</form>
		<jsp:include page="footer.jsp" flush="true" />
	</body>
</html>