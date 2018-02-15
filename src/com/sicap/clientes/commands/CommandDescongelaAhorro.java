package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.exceptions.ClientesException;
import java.util.ArrayList;

import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;

//import com.afirme.clientes.dao.CicloGrupalDAO;
//import com.afirme.clientes.dao.EjecutivoCreditoDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
//import com.afirme.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
//import com.afirme.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.cartera.TransaccionesHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
//import com.afirme.clientes.vo.CicloGrupalVO;
//import com.afirme.clientes.vo.EjecutivoCreditoVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.SaldoIBSVO;
//import com.afirme.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import com.sicap.clientes.vo.cartera.DescongelaAhorroVO;

public class CommandDescongelaAhorro implements Command {

    private String siguiente;
    private static org.apache.log4j.Logger myLogger = org.apache.log4j.Logger.getLogger(CommandDescongelaAhorro.class);

    public CommandDescongelaAhorro(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        Notification notificaciones[] = new Notification[1];
        //ArrayList<DescongelaAhorroVO> listaMontoCongelado = null;
        CreditoCartVO creditoVO = new CreditoCartVO();
        SaldoIBSVO saldoVO = new SaldoIBSVO();
        CreditoCartDAO creditoDAO = new CreditoCartDAO();
        GrupoVO grupoCiclo = new GrupoVO();
        GrupoDAO grupoDao = new GrupoDAO();
        CatalogoDAO catalogoDAO = new CatalogoDAO();
        BitacoraUtil bitutil;
        TransaccionesHelper transacHelper = new TransaccionesHelper();
        int grupo = 0;
        int ciclo = 0;
        int contador = 0;
        int resultadoUpdate = 0;
        int numeroGrupo = 0;
        double montoCongelado = 0;
        String referencia = "";

        try {
            if (!catalogoDAO.ejecutandoCierre()) {
                //grupoCiclo.idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
                grupo = HTMLHelper.getParameterInt(request, "idGrupo");
                ciclo = HTMLHelper.getParameterInt(request, "idCiclo");
                bitutil = new BitacoraUtil(grupo, request.getRemoteUser(), "CommandDescongelaAhorro");
                contador = HTMLHelper.getParameterInt(request, "numGrupo");
                montoCongelado = HTMLHelper.getParameterDouble(request, "montoNDescongelar");

                if (contador == 0) {
                    grupoCiclo = grupoDao.getGrupo(grupo);
                    creditoVO = creditoDAO.getCreditoClienteSol(grupo, ciclo);
                    myLogger.debug("Descongelando monto de grupo: " + grupo + " ciclo: " + ciclo + " monto: " + montoCongelado);
                    if (creditoVO.getNumSolicitud() == 0) {
                        myLogger.debug("Grupo no encontrado");
                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Grupo o ciclo no encontrado");
                    } else {
                        if (creditoVO.getMontoCuenta() != 0) {
                            request.setAttribute("MONTODESCONGELADO", creditoVO);
                            request.setAttribute("DATOSGRUPO", grupoCiclo);
                            myLogger.debug("Grupo Encontrado");
                            notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Grupo y Ciclo encontrados");
                            //request.setAttribute("NOTIFICACIONES", notificaciones);
                        } else {
                            myLogger.debug("Grupo no encontrado");
                            notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Credito vigente sin monto a descongelar");
                            //request.setAttribute("NOTIFICACIONES", notificaciones);
                        }
                    }
                    request.setAttribute("NOTIFICACIONES", notificaciones);

                } else {
                    referencia = HTMLHelper.getParameterString(request, "numReferencia");
                    creditoVO = creditoDAO.getCreditoReferencia(referencia);
                    creditoVO.setMontoCuentaCongelada(creditoVO.getMontoCuentaCongelada() - montoCongelado);
                    saldoVO = new SaldoIBSDAO().getSaldo(creditoVO.getNumCliente(), creditoVO.getNumSolicitud(), referencia);
                    transacHelper.aplicaPagoGarantia(creditoVO, saldoVO.getFechaGeneracion(), montoCongelado);
                    resultadoUpdate = creditoDAO.updatePagoCredito(creditoVO);
                    bitutil.registraEvento(creditoVO.toString());
                    notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Terminado el proceso de busqueda, el sistema actualiza el campo monto_cuenta_congelado");
                }
            } else {
                notificaciones[0] = new Notification(ClientesConstants.ERROR_LEVEL, "Cierre en Ejecucion. No se puede procesar las peticion");
            }
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch (ClientesDBException dbe) {
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            myLogger.error("execute", e);
            throw new CommandException(e.getMessage());
        }

        return siguiente;
    }
}
