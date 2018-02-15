
package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.PagareDAO;
import com.sicap.clientes.dao.PagoPagareDAO;
import com.sicap.clientes.dao.SaldoFondeadorDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.PagosReferenciadosHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.FondeadorUtil;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.PagareVO;
import com.sicap.clientes.vo.PagoPagareVO;
import com.sicap.clientes.vo.SaldoFondeadorVO;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author nmejia
 */
public class CommandPagoPagare implements Command
{
    private final String siguiente;
    private static final Logger myLogger = Logger.getLogger(CommandPagoPagare.class);

    public CommandPagoPagare(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        String comando = "";
        Notification notificaciones1[] = new Notification[1];
        Vector<Notification> notificaciones = new Vector<Notification>();
        ArrayList<PagoPagareVO> arrPago = new ArrayList<PagoPagareVO>();
        ArrayList<SaldoFondeadorVO> arrLista = null;
        FondeadorUtil fondeadorUtil = new FondeadorUtil();
        int respuesta = 0;
        PagoPagareVO pagoPagare = null;
        PagareDAO pagareDao = new PagareDAO();
        PagoPagareDAO pagoPagareDao = new PagoPagareDAO();
        CatalogoDAO catalogoDAO = new CatalogoDAO();
        SaldoFondeadorDAO saldoFondDao = new SaldoFondeadorDAO();
        double saldoActual = 0.0;
        ArrayList<PagareVO> arrAplicaPago = null;
        Connection cn = null;
        try {
            
            cn = ConnectionManager.getMySQLConnection();
            cn.setAutoCommit(false);
            
            comando = request.getParameter("command");
            if (comando.equals("aplicaPagosPagare")) {
                if (!catalogoDAO.ejecutandoCierre()) {
                //if (catalogoDAO.ejecutandoCierre()) {
                    //obtenemos el pagare seleccionado
                    arrAplicaPago = new PagosReferenciadosHelper().getPagosPagare(request);
                    //obtenemos datos pago capital, fechaPago, referenciaBancaria
                    
                    double pagoCapital = HTMLHelper.getParameterDouble(request, "montoPago");
                    Date fechaPago = HTMLHelper.getParameterDate(request, "fechaPago");
                    String refBancaria = HTMLHelper.getParameterString(request, "refBancaria");
                    
                    //obtenemos el idFondeador
                    int numPagare = arrAplicaPago.get(0).getNumPagare();
                    String nombrePagare = arrAplicaPago.get(0).getNombrePagare();
                    String nombreFondeador = arrAplicaPago.get(0).getNombreFondeador();
                    int idFondeador = pagareDao.getIdFondeador(numPagare,nombreFondeador);
                    
                    //aplicamos el pago a la tabla d_saldos_mov
                    SaldoFondeadorVO saldoFondVO = new SaldoFondeadorVO();
                    saldoFondVO.setTipoMov(ClientesConstants.PAGO_FONDEADOR);
                    saldoFondVO.setMonto(pagoCapital);
                    saldoFondVO.setNumFondeador(idFondeador);
                    saldoFondVO.setNumCliente(0);
                    saldoFondVO.setNumCredito(0);
                    saldoFondVO.setNumTransaccion(0);
                    saldoFondVO.setOrigen(0);
                    
                    String saldoParamFond = null;
                    double saldoFond;
                    
                    //obtenemos pagare para obtener la suma total que debe pagar de las amortizaciones
                    PagareVO pagare = pagareDao.obtenerPagare(numPagare);
                    
                    if(pagoCapital>pagare.getTotalPagar()){
                        notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "La suma de los pagos es mayor al monto total calculado de las amortizaciones o aun no se generan las amortizaciones del pagaré"));
                        request.setAttribute("NOTIFICACIONES", notificaciones);
                        return siguiente;
                    }
                    
                    double restanteTotalPagare = pagare.getTotalPagar()-pagoCapital;
                    
                    pagoPagareDao.actualizarPagoTotalPagare(numPagare, restanteTotalPagare, cn);
                    
                    if(idFondeador == ClientesConstants.ID_FONDEADOR_FOMMUR || idFondeador == ClientesConstants.ID_FONDEADOR_FOMMUR_DOS
                            || idFondeador == ClientesConstants.ID_FONDEADOR_FINAFIN){
                        //obtenemos el saldo tabla c_parametros_fondeadores
                        saldoParamFond = catalogoDAO.getParametro("SALDO_FONDEADOR_" + idFondeador).valor;
                        //convertimos el saldoParamFond a double para operaciones
                        saldoFond = Double.parseDouble(saldoParamFond);

                        if(saldoFond > pagoCapital && pagoCapital > 0 )
                             respuesta = fondeadorUtil.actualizaSaldoFondeadorPagare(saldoFondVO, saldoFond, cn);
                        else
                            respuesta = fondeadorUtil.actualizaSaldoFondeadorPagare(saldoFondVO, saldoFond, cn);
                        if(respuesta ==1)
                            myLogger.debug("Se ha actualizado el saldo");
                        else
                            myLogger.debug("Problema actualizar saldo");
                    }else{
                        //obtenemos el saldo tabla c_parametros_fondeadores
                        saldoParamFond = saldoFondDao.getSaldoFondeadorParam(idFondeador);
                        //convertimos el saldoParamFond a double para operaciones
                        saldoFond = Double.parseDouble(saldoParamFond);
                        //convertimos el saldo actual para enviarlo a la tabla de parametros
                        //aplicamos formato al saldoFondeador
                        DecimalFormat format = new DecimalFormat("#######.00");
                        if(saldoFond >= pagoCapital && pagoCapital > 0 )
                        {
                            saldoActual = saldoFond - pagoCapital;
                            String formatSaldoActual = format.format(saldoActual);
                            //actualizamos el saldoFondeador en la tabla c_parametros_fondeadores
                            catalogoDAO.updateParametroFondeador("SALDO_FONDEADOR",idFondeador, formatSaldoActual, cn);
                        }
                        else if(pagoCapital < 0 )
                        {
                            myLogger.debug("Entra suma de Saldo Fondeador c_paramtros_fondeador");
                            saldoActual = saldoFond + -(pagoCapital);
                            String formatSaldoActual = format.format(saldoActual);
                            //actualizamos el saldoFondeador en la tabla c_parametros_fondeadores
                            catalogoDAO.updateParametroFondeador("SALDO_FONDEADOR",idFondeador, formatSaldoActual, cn);
                            notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "El Pago del Pagaré \t"+nombrePagare+" fue cancelado correctamente"));
                            //registramos evento en bitacora
                            BitacoraUtil bitacora = new BitacoraUtil(idFondeador, request.getRemoteUser(), "CommandPagoPagare");
                            bitacora.registraEventoString("CancelacionPagoDePagare: "+nombrePagare);
                        }else if(pagoCapital>saldoFond){
                            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "La suma de los pagos no debe exeder el total de las lineas de crédito"));
                            request.setAttribute("NOTIFICACIONES", notificaciones);
                            return siguiente;
                        }
                    }

                    //insertamos informacion en tabla d_pagos_manuales
                    String usuario = request.getRemoteUser();
                    //damos formato al pago de capital
                    double pagoCapital1 = FormatUtil.redondeaMoneda(pagoCapital);
                    
                    pagoPagare = new PagoPagareVO(numPagare,fechaPago,pagoCapital1,ClientesConstants.ACTIVO,refBancaria,usuario);
                    boolean result= pagoPagareDao.insertarPagoPagare(pagoPagare, cn);
                    //obtenemos monto del pagare selecionado
                    double montoPagare = arrAplicaPago.get(0).getMonto();
                    
                    /*if(saldoFond > pagoCapital && pagoCapital > 0 )
                    {
                        pagareDao.actualizarMontoPagare(montoPagare, pagoCapital, numPagare);
                        //registramos evento en bitacora
                        BitacoraUtil bitacora = new BitacoraUtil(idFondeador, request.getRemoteUser(), "CommandPagoPagare");
                        bitacora.registraEventoString("ActualizacionMontoPagare: "+nombrePagare);
                    }
                    */
                    //registramos evento en bitacora
                    BitacoraUtil bitacora = new BitacoraUtil(idFondeador, request.getRemoteUser(), "CommandPagoPagare");
                    bitacora.registraEventoString("PagoDePagare: "+nombrePagare);
                    if(result==true)
                        notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "El Pago del Pagaré \t"+nombrePagare+" fue aplicado correctamente"));
                    else
                        notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Problema al aplicar el Pagaré \t"+nombrePagare));
                }
                request.setAttribute("NOTIFICACIONES", notificaciones);
            }
            cn.commit();
        } catch (SQLException sqlEx) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                myLogger.error(ex.getCause());
            }
        } catch (ClientesDBException dbe) {
            myLogger.error("ClientesDBException", dbe);
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new CommandException(e.getMessage());
        }finally {
            //Cerrar conexion
            if (cn != null) {
                try {
                    cn.close();
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(CommandPagoPagare.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return siguiente;
    }
}
