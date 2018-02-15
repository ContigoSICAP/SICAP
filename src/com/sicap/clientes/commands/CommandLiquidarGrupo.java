package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.dao.LiquidacionGruposDAO;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommandLiquidarGrupo implements Command {

    private String siguiente;

    public CommandLiquidarGrupo(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        LiquidacionGruposDAO liquidacionDao = new LiquidacionGruposDAO();
        Notification notificaciones[] = new Notification[1];
        int numGrupo = 0;
        int numCiclo = 0;
        int estatusLiquidacion = 0;
        int estatusCierre = 0;
        int numCredito = 0;
        int origenMigracion = 0;
        Date fecha = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        String referencia = null;
        String liquidar = null;
        String cerrar = null;
        String origen = null;
        //String idMigracion = null;
        String fechaActual = formatoFecha.format(fecha);
        boolean existeGrupo = false;
               
        try {
            numGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
            numCiclo = HTMLHelper.getParameterInt(request, "numCiclo");
            liquidar = HTMLHelper.getParameterString(request, "liquidar");
            cerrar = HTMLHelper.getParameterString(request, "cerrar");
            origen = HTMLHelper.getParameterString(request, "origen");
            BitacoraUtil bitutil = new BitacoraUtil(numGrupo, request.getRemoteUser(), "CommandLiquidarGrupo");
            estatusLiquidacion = liquidacionDao.getEstatusCredito(numGrupo, numCiclo);
            estatusCierre = liquidacionDao.getEstatusCiclo(numGrupo, numCiclo);
            //idMigracion = liquidacionDao.getIdMigracion(numGrupo, numCiclo);
            origenMigracion = liquidacionDao.getOrigenMigracion(numGrupo, numCiclo);
            existeGrupo = liquidacionDao.existeGrupo(numGrupo, numCiclo);
            System.out.println("origenMigracion: "+origenMigracion);
            System.out.println("existeGrupo: "+existeGrupo);
            System.out.println("estatusLiquidacion: "+estatusLiquidacion);
            System.out.println("estatusCierre: "+estatusCierre);
            System.out.println("origen: "+origen);
            
            if ((!existeGrupo)||(estatusLiquidacion==0)){
                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Error: El equipo o el ciclo no existen");
            }
            else{
                if ((origen.equals("cr"))&& (origenMigracion!=1)){
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Error: El equipo no proviene de Crediequipos");
                }
                else if ((origen.equals("cx"))&& (origenMigracion!=2)){
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Error: El equipo no proviene de Credex");
                }
                else if ((origen.equals("fc"))&& (origenMigracion!=0)){
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Error: El equipo no proviene de Fincontigo");
                }
                else {
                    if (liquidar.equals("on") && cerrar.equals("on")){
                        if (estatusLiquidacion==3 && estatusCierre==2){
                            notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Error: El equipo ya está liquidado y cerrado");
                        }
                        else if (estatusLiquidacion==3 && estatusCierre==1){
                            notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Error: El equipo ya está liquidado");
                        }
                        else if (estatusCierre==2 && estatusLiquidacion==8){
                            notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Error: El ciclo ya está cerrado");
                        }
                        else if (estatusLiquidacion==8 && estatusCierre==1){
                            if (origen.equals("cr")||origen.equals("cx")){
                                liquidacionDao.updateEstatusCreditoCR(fechaActual, numGrupo, numCiclo);
                                liquidacionDao.updateEstatusCiclo(numGrupo);
                                notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "El equipo se liquidó y se cerró correctamente");
                                bitutil.registraEventoString("Grupo= "+numGrupo+" Ciclo= "+numCiclo+" "+" Origen= "+origen+" "+notificaciones[0].text);
                                
                            }
                            else if (origen.equals("fc")){
                                referencia = liquidacionDao.getReferencia(numGrupo, numCiclo);
                                numCredito = liquidacionDao.getCredito(numGrupo, numCiclo);
                                liquidacionDao.deleteSaldos(numGrupo, numCiclo);
                                liquidacionDao.insertSaldos(numGrupo, numCiclo);
                                liquidacionDao.deleteCredito(numGrupo, numCiclo);
                                liquidacionDao.insertCredito(numGrupo, numCiclo);
                                liquidacionDao.deleteTablaAmortizacion(numGrupo, numCredito);
                                liquidacionDao.insertTablaAmortizacion(numGrupo, numCredito);
                                liquidacionDao.deletePagos(referencia);
                                liquidacionDao.deletePagosODS(referencia);
                                liquidacionDao.insertPagos(referencia);
                                liquidacionDao.updateEstatusCreditoFC(numGrupo, numCiclo);
                                liquidacionDao.updateEstatusCiclo(numGrupo);
                                notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "El equipo se liquidó y se cerró correctamente");
                                bitutil.registraEventoString("Grupo= "+numGrupo+" Ciclo= "+numCiclo+" "+" Origen= "+origen+" "+notificaciones[0].text);
                            }
                        }
                    }
                    else if (liquidar.equals("on") && cerrar.equals("")){
                        if (estatusLiquidacion==3){
                            notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Error: El equipo ya está liquidado");
                        }
                        else {
                            if (origen.equals("cr")||origen.equals("cx")){
                                liquidacionDao.updateEstatusCreditoCR(fechaActual, numGrupo, numCiclo);
                                notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "El equipo se liquidó correctamente");
                                bitutil.registraEventoString("Grupo= "+numGrupo+" Ciclo= "+numCiclo+" "+" Origen= "+origen+" "+notificaciones[0].text);
                            }
                            else if (origen.equals("fc")){
                                referencia = liquidacionDao.getReferencia(numGrupo, numCiclo);
                                numCredito = liquidacionDao.getCredito(numGrupo, numCiclo);
                                liquidacionDao.deleteSaldos(numGrupo, numCiclo);
                                liquidacionDao.insertSaldos(numGrupo, numCiclo);
                                liquidacionDao.deleteCredito(numGrupo, numCiclo);
                                liquidacionDao.insertCredito(numGrupo, numCiclo);
                                liquidacionDao.deleteTablaAmortizacion(numGrupo, numCredito);
                                liquidacionDao.insertTablaAmortizacion(numGrupo, numCredito);
                                liquidacionDao.deletePagos(referencia);
                                liquidacionDao.deletePagosODS(referencia);
                                liquidacionDao.insertPagos(referencia);
                                liquidacionDao.updateEstatusCreditoFC(numGrupo, numCiclo);
                                notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "El equipo se liquidó correctamente");
                                bitutil.registraEventoString("Grupo= "+numGrupo+" Ciclo= "+numCiclo+" "+" Origen= "+origen+" "+notificaciones[0].text);
                            }
                        }
                    }
                    else if (liquidar.equals("") && cerrar.equals("on")){
                        if (estatusCierre==2){
                            notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Error: El ciclo ya está cerrado");
                        }
                        else {
                            if (origen.equals("cr")||origen.equals("cx")){
                                liquidacionDao.updateEstatusCiclo(numGrupo);
                                notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "El equipo se cerró correctamente");
                                bitutil.registraEventoString("Grupo= "+numGrupo+" Ciclo= "+numCiclo+" "+" Origen= "+origen+" "+notificaciones[0].text);
                            }
                            else if (origen.equals("fc")){
                                liquidacionDao.updateEstatusCiclo(numGrupo);
                                notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "El equipo se cerró correctamente");
                                bitutil.registraEventoString("Grupo= "+numGrupo+" Ciclo= "+numCiclo+" "+" Origen= "+origen+" "+notificaciones[0].text);
                            }
                        }
                    }
                }
            }
            request.setAttribute("NOTIFICACIONES", notificaciones);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
}