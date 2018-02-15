
package com.sicap.clientes.commands;

import com.sicap.clientes.dao.FondeadorDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.FondeadorVO;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;


public class CommandAltaFondeador implements Command
{
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandAltaFondeador.class);

    public CommandAltaFondeador(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        
        Notification notificaciones[] = new Notification[1];
        FondeadorVO fondeadorVO = null;
        FondeadorDAO fondeadorDAO = new FondeadorDAO();
        try{
            if(request.getParameter("command").equals("altaFondeador")){
                myLogger.debug("Entra a crear Fondeador()");
                //obtenemos los datos de la pantalla altaLineaCredito
                String nombreFondeador = HTMLHelper.getParameterString(request, "nombre");
                int preSeleccionCartera = HTMLHelper.getParameterInt(request, "preSeleccionCartera");
                int estatus = ClientesConstants.ACTIVO;
                int prioridad = fondeadorDAO.obtenerUltimaPrioridad();
                fondeadorVO = new FondeadorVO(nombreFondeador.toUpperCase(),preSeleccionCartera,estatus,prioridad+1,0);
                //obtenemos los datos para enviarlos a la capa DAO
                int result = fondeadorDAO.insertFondeador(fondeadorVO);
                if(result!=0){
                    //registro en bitacora
                    /*BitacoraUtil bitacora = new BitacoraUtil(idFondeador, request.getRemoteUser(), "CommandAltaLineaCredito");
                    bitacora.registraEventoString("nombre="+lineaCreditoVO.getNombreLineaCredito()+", razonSocial="+lineaCreditoVO.getRazonSocialFondeador()+",fechaVigenciaInicio="+lineaCreditoVO.getFechaVigenciaInicio()+",fechaVigenciaFin"+lineaCreditoVO.getFechaVigenciaFin());
                    */
                    notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente");

                }else{
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Problemas con el Ingreso");
                } 
                request.setAttribute("FONDEADOR", fondeadorVO);
                request.setAttribute("NOTIFICACIONES", notificaciones);
            }else if(request.getParameter("command").equals("desactivarLineaCredito")){
//                try{
//                    /*request.setAttribute("LINEACREDITO", lineaCreditoVO);
//                    request.setAttribute("NOTIFICACIONES", notificaciones);*/
//                    myLogger.debug("Entra desactivarLineaCredito()");
//                 
//                    int idLineaCredito = HTMLHelper.getParameterInt(request, "idLineaCredito");
//                    fondeadorVO = new LineaCreditoVO(idLineaCredito);
//                    fondeadorVO = lineaCreditoDao.obtenerSaldoFechVigencia(fondeadorVO.getIdLineaCredito());
//                    Date fechaActual = new Date();
//                    
//                    if(fondeadorVO!=null){
//                        int saldoFondeador = fondeadorVO.getMontoLineaCredito();
//                        Date fechaVencLinea = fondeadorVO.getFechaVigenciaFin();
//                        
//                        if(saldoFondeador==0){
//                            int resFechas = fechaVencLinea.compareTo(fechaActual);
//                            if(resFechas == -1 ){
//                                myLogger.debug("FechaVigencia es menor?: "+resFechas);
//                                int result = lineaCreditoDao.desactivarLineaCredito(idLineaCredito);
//                                if(result!=0){
//                                    notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Se ha desactivado correctamente");
//                                }
//                                else{
//                                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha podido desactivar la línea de crédito ");
//                                }
//                            }else{
//                                myLogger.debug("No cumple validacion fechas");
//                            } 
//                        }else{
//                            notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "El pagaré aun no esta liquidado");
//                        }
//                        
//                        
//                    }
//                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Problema al desactivar la línea de crédito. Necesita tener saldo en ceros y un pagaré asociado");
//                    
//                }catch (ClientesDBException dbe){
//                    myLogger.error("desactivarLineaCredito", dbe);
//                    throw new CommandException(dbe.getMessage());
//                }
            }
            request.setAttribute("FONDEADOR", fondeadorVO);
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } 
        catch (Exception e) 
        {
            myLogger.error("execute", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
}
