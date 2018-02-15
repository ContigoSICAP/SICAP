
package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.FondeadorDAO;
import com.sicap.clientes.dao.LineaCreditoDAO;
import com.sicap.clientes.dao.PagareDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.FondeadorVO;
import com.sicap.clientes.vo.LineaCreditoVO;
import com.sicap.clientes.vo.PagareVO;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;


public class CommandLineaCredito implements Command
{
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandLineaCredito.class);

    public CommandLineaCredito(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        
        Notification notificaciones[] = new Notification[1];
        LineaCreditoVO lineaCreditoVO = null;
        LineaCreditoDAO lineaCreditoDao = new LineaCreditoDAO();
        FondeadorDAO fondeoDAO = new FondeadorDAO();
        Connection con = null;
        try{
            if(request.getParameter("command").equals("altaLineaCredito")){
                myLogger.debug("Entra altaLineaCredito()");
                //obtenemos los datos de la pantalla altaLineaCredito
                int idFondeador = HTMLHelper.getParameterInt(request, "idFondeador");
                String nombreLineaCredito = HTMLHelper.getParameterString(request, "nombre");
                int montoLineaCredito = HTMLHelper.getParameterInt(request, "monto");
                Date fechaVigenciaInicio = HTMLHelper.getParameterDate(request, "fechaInicio");
                Date fechaVigenciaFin = HTMLHelper.getParameterDate(request, "fechaFin");
                String tasa = HTMLHelper.getParameterString(request, "tasa");

                FondeadorVO fond = fondeoDAO.obtenerFondeador(idFondeador);
                
                int estatus = ClientesConstants.ACTIVO;
                lineaCreditoVO = new LineaCreditoVO(idFondeador,nombreLineaCredito.toUpperCase(),montoLineaCredito,fechaVigenciaInicio,fechaVigenciaFin,tasa.toUpperCase(),fond.getPreSeleccionCartera(),estatus);
                
                con = ConnectionManager.getMySQLConnection();
                con.setAutoCommit(false);
                //obtenemos los datos para enviarlos a la capa DAO
                int result = lineaCreditoDao.altaLineaCredito(lineaCreditoVO, con, false);
                
                if(result!=0){
                    
                    //revisamos parametro saldo_fondeador, si no tiene insertamos el parametro, si tiene lo actualizacmo
                    int tieneParametroSaldo = lineaCreditoDao.fondeadorTieneParametroSaldo(idFondeador);
                    
                    if(tieneParametroSaldo>0){
                        myLogger.info("Actualizamos parametro SALDO_FONDEADOR");
                        CatalogoDAO catalogoDao= new CatalogoDAO();
                        
                        Double saldoFondeadorActual = Convertidor.stringToDouble(CatalogoHelper.getParametroFondeador("SALDO_FONDEADOR",idFondeador,con));
                        
                        String saldoActualizado = new DecimalFormat(ClientesConstants.FORMATO_MONTO).format(saldoFondeadorActual.doubleValue()+((double)montoLineaCredito)); 
                        catalogoDao.updateParametroFondeador("SALDO_FONDEADOR",idFondeador, saldoActualizado , con);
            
                    }else{
                        myLogger.info("Creamos parametro SALDO_FONDEADOR");
                        lineaCreditoDao.altaParamSaldoFondeador(idFondeador, montoLineaCredito, con, false);
                    }
                    con.commit();
                    //registro en bitacora
                    /*BitacoraUtil bitacora = new BitacoraUtil(idFondeador, request.getRemoteUser(), "CommandAltaLineaCredito");
                    bitacora.registraEventoString("nombre="+lineaCreditoVO.getNombreLineaCredito()+", razonSocial="+lineaCreditoVO.getRazonSocialFondeador()+",fechaVigenciaInicio="+lineaCreditoVO.getFechaVigenciaInicio()+",fechaVigenciaFin"+lineaCreditoVO.getFechaVigenciaFin());
                    */
                    notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente");

                }else{
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Problemas con el Ingreso");
                } 
                request.setAttribute("LINEACREDITO", lineaCreditoVO);
                request.setAttribute("NOTIFICACIONES", notificaciones);
            }else if(request.getParameter("command").equals("desactivarLineaCredito")){
                try{
                    /*request.setAttribute("LINEACREDITO", lineaCreditoVO);
                    request.setAttribute("NOTIFICACIONES", notificaciones);*/
                    myLogger.debug("Entra desactivarLineaCredito()");
                 
                    int idLineaCredito = HTMLHelper.getParameterInt(request, "idLineaCredito");

                    lineaCreditoVO = lineaCreditoDao.obtenerLineaCredito(idLineaCredito);
                    PagareDAO pagareDAO = new PagareDAO();
                    List<PagareVO> pagaresLC = pagareDAO.getPagaresLineaCredito(idLineaCredito, -1);
                    
                    double sumaMontosPagare = 0;
                    boolean pagados = true;
                    for(PagareVO pag : pagaresLC){
                        sumaMontosPagare+=pag.getMonto();
                        if(pag.getEstatus()!=ClientesConstants.PAGARE_PAGADO){
                            pagados = false;
                        }
                    }
                    
                    myLogger.info("Suma montos pagare para desactivar: "+sumaMontosPagare);
                    
                    if(pagados){
                        if(sumaMontosPagare==(double)lineaCreditoVO.getMontoLineaCredito()){
                            int result = lineaCreditoDao.desactivarLineaCredito(idLineaCredito);
                            if(result!=0){
                                notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "La Línea de Crédito se ha desactivado correctamente");
                            }
                            else{
                                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Error al desactivar la línea de crédito ");
                            }
                        }else{
                            notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "El monto de la Línea de Crédito aun no esta liquidado, crear pagares para cubrir el monto y liquidarlos");
                        }
                    }else{
                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No todos los pagares estan liquidados");
                    }
                    
                }catch (ClientesDBException dbe){
                    myLogger.error("desactivarLineaCredito", dbe);
                    throw new CommandException(dbe.getMessage());
                }
            }
            request.setAttribute("LINEACREDITO", lineaCreditoVO);
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch (SQLException sqlEx) {
            myLogger.error(sqlEx.getCause());
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
