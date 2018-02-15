/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.commands;

import com.sicap.clientes.dao.LineaCreditoDAO;
import com.sicap.clientes.dao.PagareDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.LineaCreditoVO;
import com.sicap.clientes.vo.PagareVO;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author nmejia
 */
public class CommandAltaPagare implements Command
{
    private final String siguiente;
    private static final Logger myLogger = Logger.getLogger(CommandAltaPagare.class);

    public CommandAltaPagare(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        
        Notification notificaciones[] = new Notification[1];
        PagareVO pagareVO = null;
        try 
        {
            PagareDAO pagareDao = new PagareDAO();
            LineaCreditoDAO lineaDao = new LineaCreditoDAO();
            int idLineaCredito = HTMLHelper.getParameterInt(request, "idLineaCredito");
            String nombrePagare = HTMLHelper.getParameterString(request, "nombre");
            int montoPagare = HTMLHelper.getParameterInt(request, "monto");
            Date fechaInicio = HTMLHelper.getParameterDate(request, "fechaInicio");
            Date fechaFin = HTMLHelper.getParameterDate(request, "fechaFin");
            //convertimos fechaFin
            
            LineaCreditoVO lineaCreditoVO = lineaDao.obtenerLineaCredito(idLineaCredito);
            int estatus = ClientesConstants.ACTIVO;
            pagareVO = new PagareVO(idLineaCredito,nombrePagare,montoPagare,fechaInicio,fechaFin,lineaCreditoVO.getTasa(),estatus);
            //validamos fecha pagare y monto
            
            request.setAttribute("PAGARE", pagareVO);
            
            int sumPagaresAsociados = lineaDao.getSumPagaresAsociadosLineaCred(idLineaCredito);
            int montoFinalPagare = montoPagare + sumPagaresAsociados;

            if(lineaCreditoVO.getFechaVigenciaFin().compareTo(fechaFin)>=0
                    &&fechaInicio.compareTo(lineaCreditoVO.getFechaVigenciaInicio())>=0){
                
                if(montoFinalPagare<=lineaCreditoVO.getMontoLineaCredito()){
                    //insertamos datos en la tabla lineaCredito
                    int result = pagareDao.altaPagare(pagareVO);

                    if(result!=0)
                    {
                        notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente");
                        request.setAttribute("PAGARE", new PagareVO());
                    }
                    else
                    {
                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Problemas con el Ingreso");
                    }
                }else{
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "La suma de los montos de los pagarés asociados es mayor a la Linea de Credito");
                    request.setAttribute("NOMBRE_PAGARE",nombrePagare);
                }
                
            }else{
                myLogger.debug("Las fechas del pagare deben estar dentro del rango de las fechas de la linea de credito");
                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Las fechas del Pagaré deben estar dentro del rango de las fechas de la Linea de Credito");
            }
      
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
