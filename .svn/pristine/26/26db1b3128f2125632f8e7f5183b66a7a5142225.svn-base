/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.commands;


import com.sicap.clientes.dao.AmortPagareDAO;
import com.sicap.clientes.dao.PagareDAO;
import com.sicap.clientes.dao.PagoPagareDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.AmortizacionPagareVO;
import com.sicap.clientes.vo.PagareVO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author nmejia
 */
public class CommandAltaAmortPagare implements Command
{
    private final String siguiente;
    private static final Logger myLogger = Logger.getLogger(CommandAltaAmortPagare.class);
    private static final double MAX_MONTO_AMORT = 9.99999999E9;
    
    public CommandAltaAmortPagare(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        
        Notification notificaciones[] = new Notification[1];
        Connection con = null;
        try 
        {
            
            con=ConnectionManager.getMySQLConnection();
            con.setAutoCommit(false);
            
            AmortPagareDAO amortDao = new AmortPagareDAO();
            PagareDAO pagareDao = new PagareDAO();
            int idPagare = HTMLHelper.getParameterInt(request, "idPagare");
            int numPagos = HTMLHelper.getParameterInt(request, "numPago");
            Date fechaPago = HTMLHelper.getParameterDate(request, "fechaPago");
            double capital = HTMLHelper.getParameterDouble(request, "capital");
            int periodo = HTMLHelper.getParameterInt(request, "periodo");
            //obtenemos la tasa de interes del pagare asociado
            PagareVO pagare = pagareDao.obtenerPagare(idPagare);
            
            if(fechaPago.compareTo(pagare.getFechaInicio())>=0){
                
                double montoTotal = numPagos*capital;
                myLogger.info("Monto total de pagare: "+montoTotal);
                
                if(montoTotal<MAX_MONTO_AMORT){
                    
                    double diferencia = (double)pagare.getMontoPagare()-montoTotal;
                    myLogger.debug("Diferencia Total Calculado vs Monto Pagare: "+diferencia);
                    //si la diferencia es menor de 50 centavos el saldo es igual
                    if((diferencia>=0&&diferencia<.5)||(diferencia<=0&&diferencia>(-0.5))){
                        double iva = capital*0.16;
                        List<AmortizacionPagareVO> amortizaciones = new ArrayList<AmortizacionPagareVO>();
                        Calendar fechaCalculada = Calendar.getInstance();
                        fechaCalculada.setTime(fechaPago);

                        int incremento = 1;
                        int periodicidad = 0;

                        switch(periodo){
                            case 0:
                                myLogger.debug("Periodicidad: Semanal");
                                periodicidad = Calendar.WEEK_OF_YEAR;
                                break;
                            case 1:
                                myLogger.debug("Periodicidad: Mensual");
                                periodicidad = Calendar.MONTH;
                                break;
                            case 2:
                                myLogger.debug("Periodicidad: Semestral");
                                periodicidad = Calendar.MONTH;
                                incremento = 6;
                                break;
                            case 3:
                                myLogger.debug("Periodicidad: Pago Unico");
                                numPagos=1;
                                periodicidad = Calendar.DAY_OF_MONTH;
                                break;
                        }

                        for(int i = 1; i<=numPagos; i++){
                            myLogger.debug("Fecha Pagare "+i+": "+fechaCalculada.getTime());
                            AmortizacionPagareVO amortVO = new AmortizacionPagareVO(idPagare, i, fechaCalculada.getTime(), capital, pagare.getTasa(), iva);
                            amortizaciones.add(amortVO);
                            fechaCalculada.add(periodicidad, incremento);
                        }
                        
                        //Le sumamos a la ultima amortizacion la diferencia, para que cuadre correctamente
                        double capitalAmort = amortizaciones.get(amortizaciones.size()-1).getCapital();
                        amortizaciones.get(amortizaciones.size()-1).setCapital(capitalAmort+diferencia);
                        amortizaciones.get(amortizaciones.size()-1).setIva((capitalAmort+diferencia)*0.16);
                        
                        //insertamos datos en la tabla amortizacion
                        int result = amortDao.altaAmortPagare(amortizaciones, con);
                        PagoPagareDAO pagoPagareDao = new PagoPagareDAO();
                        pagoPagareDao.actualizarPagoTotalPagare(idPagare, pagare.getMontoPagare(), con);

                        if(result!=0)
                        {
                            notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Las amortizaciones del pagaré fueron guardadas correctamente");
                        }
                        else
                        {
                            notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Problemas con el Ingreso");
                        } 
                    }else{
                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "La suma de los montos debe ser igual al monto del Pagaré");
                    }
                }else{
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "La suma de los montos de los pagos es demasiado grande, revisar el monto y el número de pagos.");
                }

            }else{
                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "La fecha debe ser mayor a la fecha de inicio del Pagaré");
            }
            con.commit();
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch (SQLException sqlEx) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                myLogger.error(ex.getCause());
            }
        } 
        catch (Exception e) 
        {
            myLogger.error("execute", e);
            throw new CommandException(e.getMessage());
        }finally {
            //Cerrar conexion
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(CommandCierreDiaBursa.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return siguiente;
    }
}
