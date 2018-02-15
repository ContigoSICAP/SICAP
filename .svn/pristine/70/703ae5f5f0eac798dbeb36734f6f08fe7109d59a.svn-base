package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.SaldoT24DAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.vo.SaldoT24VO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import org.apache.log4j.Logger;

public class CommandConsultaReportes implements Command {
    
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandConsultaReportes.class);    
    
    public CommandConsultaReportes(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        try {
            int idSucursal = 0;
            idSucursal = Integer.parseInt(request.getParameter("sucursal"));
            myLogger.debug("Obteniendo saldos para sucursal :" + idSucursal);
//			SaldoT24VO[] saldos = null;
//			SaldoT24DAO saldoDAO = new SaldoT24DAO();
            SaldoIBSVO[] saldos = null;
            SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
            saldos = saldoDAO.getSaldosBySucursal(idSucursal);
            myLogger.debug("Se leyeron :" + saldos.length);
            String output = formatSaldos(saldos);
            request.setAttribute("saldosSucursal", output);
            myLogger.debug("Retornando saldos");
            
        } catch (Exception e) {
            myLogger.error("execute", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;
        
    }
    
    private String formatSaldos(SaldoIBSVO[] saldos) {
        String titulos = "Número Equipo,"
                + "Nombre,"
                //                + "Referencia,"
                + "Número Ciclo,"
                //                + "Producto,"
                + "Sucursal,"
                //                + "RFC,"
                + "No.Cuotas,"
                + "Cuotas restantes,"
                + "Fec.Disposición,"
                + "Fec.Vencimiento,"
                //                + "Monto aprobado,"
                //                + "Comision,"
                + "Desembolso,"
                //                + "Amortización,"
                //                + "Frecuencia,"
                //                + "Interés por Cobrar,"
                //                + "Capital vencido,"
                //                + "Interés vencido,"
                //                + "Interés moratorio,"
                //                + "Multa,"
                + "Total vencido,"
                //                + "Otros rubros,"
                + "Situación crédito,"
                //                + "Fecha incumplimiento,"
                + "Días vencidos,"
                //                + "Días transcurridos,"
                //                + "No.Pagos vencidos,"
                //                + "Total exigible,"
                //                + "Capital pagado,"
                //                + "Interés pagado,"
                //                + "Moratorio pagado,"
                + "Total pagado,"
                //                + "Ejecutivo,"
                //                + "Interés futuro,"
                + "Saldo a favor,"
                + "Saldo del credito\n";
        StringBuffer output = new StringBuffer(titulos);
        try {
            for (int i = 0; i < saldos.length; i++) {
                double comisionIva = FormatUtil.roundDouble(saldos[i].getComision(), 2);
                double multa = FormatUtil.roundDouble(saldos[i].getSaldoMulta() + saldos[i].getSaldoIVAMulta(), 2);
                double totalVencido = FormatUtil.roundDouble(saldos[i].getCapitalVencido() + saldos[i].getSaldoInteresVencido() + saldos[i].getSaldoMora() + multa, 2);
                double desembolso = saldos[i].getMontoDesembolsado();
                //double montoAmortizacion = FormatUtil.roundDouble(saldos[i].montoAprobado / saldos[i].numCuotas, 2);
                double totalExigible = FormatUtil.roundDouble(saldos[i].getSaldoTotalAlDia(), 2);
                double totalPagado = saldos[i].getCapitalPagado() + saldos[i].getInteresNormalPagado() + saldos[i].getIvaInteresNormalPagado() + saldos[i].getMoratorioPagado() + saldos[i].getIvaMoraPagado() + saldos[i].getMultaPagada() + saldos[i].getIvaMultaPagado();
                desembolso = FormatUtil.roundDouble(desembolso, 2);
                totalPagado = FormatUtil.roundDouble(totalPagado, 2);
                output.append(saldos[i].getIdClienteSICAP() + ",");
                output.append(saldos[i].getNombreCliente() + ",");
//                output.append(saldos[i].getReferencia() + ",");
                output.append(saldos[i].getIdSolicitudSICAP() + ",");
//                output.append(saldos[i].getIdProducto() + ",");
                output.append(saldos[i].getNombreSucursal() + ",");
                //output.append(saldos[i].contrato + ",");
//                output.append(saldos[i].getRfc() + ",");
                output.append(saldos[i].getNumeroCuotas() + ",");
                output.append(saldos[i].getNumeroCuotas() - saldos[i].getNumeroCuotasTranscurridas() + ",");
                output.append(Convertidor.dateToString(saldos[i].getFechaDesembolso()) + ",");
                output.append(Convertidor.dateToString(saldos[i].getFechaVencimiento()) + ",");
                output.append(saldos[i].getMontoCredito() + ",");
                //output.append(saldos[i].saldoInsoluto + ",");
//                output.append(comisionIva + ",");
//                output.append(desembolso + ",");
//                output.append(saldos[i].getMontoCredito() + ",");
//                output.append(saldos[i].getPeriodicidad() + ",");
//                output.append(saldos[i].getSaldoConInteresAlFinal() + ",");
//                //output.append(saldos[i].tasaBruta + ",");
//                output.append(saldos[i].getCapitalVencido() + ",");
//				//Se colocó feeVencidos en la columna Interés vencido, la columna fee vencido se renombró por 
//                // Otros rubros, se está enviando cero como valor.
//                output.append(saldos[i].getSaldoInteresVencido() + ",");
//                output.append(saldos[i].getSaldoMora() + saldos[i].getSaldoIVAMora() + ",");
//                output.append(saldos[i].getSaldoMulta() + saldos[i].getSaldoIVAMulta() + ",");
                output.append(saldos[i].getTotalVencido() + ",");
//                output.append(0 + ",");
                String estatus = "";
                switch (saldos[i].getEstatus()) {
                    case 1:
                        estatus = "Vigente";
                        break;
                    case 2:
                        estatus = "Mora";
                        break;
                    case 3:
                        estatus = "Liquidado";
                        break;
                    case 4:
                        estatus = "Vencido";
                        break;
                    default:
                        break;
                }
                output.append(estatus + ",");
//                if (Convertidor.dateToString(saldos[i].getFechaIncumplimiento()).equalsIgnoreCase("29/12/1899")) {
//                    output.append(",");
//                } else {
//                    output.append(Convertidor.dateToString(saldos[i].getFechaIncumplimiento()) + ",");
//                }
                output.append(saldos[i].getDiasMora() + ",");
//                output.append(saldos[i].getDiasTranscurridos() + ",");
//                output.append(saldos[i].getCuotasVencidas() + ",");
//                output.append(totalExigible + ",");
//                output.append(saldos[i].getCapitalPagado() + ",");
//                output.append(saldos[i].getInteresNormalPagado() + ",");
//                output.append(saldos[i].getMoratorioPagado() + ",");
                output.append(saldos[i].getMontoTotalPagado() + ",");
// Se toma la cuenta contable que es donde se esta guardando al ejecutivo				
//                output.append(saldos[i].getCtaContable() + ",");
//                output.append(saldos[i].getInteresSigAmortizacion() + ",");
//				output.append(saldos[i].getSaldoTotalAlDia() + ",");
// Se cambia el salto total al dia por saldo bucket ya que el saldo total ya se refleja en total exigible
                output.append(saldos[i].getSaldoBucket() + ",");
//                if (Convertidor.dateToString(saldos[i].getFechaSigAmortizacion()).equalsIgnoreCase("29/12/1899")) {
//                    output.append("\n");
//                } else {
//                    output.append(Convertidor.dateToString(saldos[i].getFechaSigAmortizacion()) + "\n");
//                }
                output.append(Math.ceil(saldos[i].getSaldoConInteresAlFinal()) + "\n");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();
    }
}
