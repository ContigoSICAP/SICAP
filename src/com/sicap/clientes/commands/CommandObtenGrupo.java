package com.sicap.clientes.commands;

import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ArchivoAsociadoDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.DireccionGenericaDAO;
import com.sicap.clientes.dao.EventosPagosGrupalDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SaldoT24DAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.IBSHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.helpers.InterCicloHelper;
import com.sicap.clientes.util.AdicionalUtil;
import com.sicap.clientes.vo.SaldoIBSVO;
import org.apache.log4j.Logger;

public class CommandObtenGrupo implements Command {
    private static Logger myLogger = Logger.getLogger(CommandObtenGrupo.class);
    private String siguiente;

    public CommandObtenGrupo(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        HttpSession session = request.getSession();
        GrupoVO grupo = new GrupoVO();
        GrupoDAO grupodao = new GrupoDAO();
        CicloGrupalDAO ciclodao = new CicloGrupalDAO();
        TablaAmortDAO tablaAmortdao = new TablaAmortDAO();
        CicloGrupalDAO cicloGrupalDao = new CicloGrupalDAO();
        SaldoIBSDAO SaldoDAO = new SaldoIBSDAO();
        SaldoIBSVO saldoIC = new SaldoIBSVO();
        SaldoIBSVO saldoAdicional = new SaldoIBSVO();
        int semDispAdi = 0;
        //Agragar validacion para  mostrar liga para interciclo

        try {
            int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
            if (idGrupo != 0) {
                grupo = grupodao.getGrupo(idGrupo);
                grupo.ciclos = ciclodao.getCiclos(grupo.idGrupo);

                for (int i = 0; grupo.ciclos != null && i < grupo.ciclos.length; i++) {
                    grupo.ciclos[i] = new CicloGrupalDAO().getCiclo(idGrupo, grupo.ciclos[i].idCiclo);
                    if (grupo.ciclos[i].idCreditoIBS != 0) {
                        grupo.ciclos[i].saldo = new SaldoIBSDAO().getSaldos(grupo.idGrupo, grupo.ciclos[i].idCiclo);
                        grupo.ciclos[i].setAtrasos(tablaAmortdao.getAtrasos(grupo.idGrupo, grupo.ciclos[i].idCreditoIBS));
                        grupo.ciclos[i].setSemDisp(InterCicloHelper.semanaDispersion(grupo.ciclos[i].saldo, grupo.ciclos[i]));
                        myLogger.debug("grupo.ciclos[i].semDisp "+grupo.ciclos[i].semDisp);
                        if (CatalogoHelper.esSucursalAdicional(grupo.sucursal)&&grupo.ciclos[i].estatus==ClientesConstants.CICLO_DISPERSADO&&grupo.ciclos[i].semDisp==0){
                           //en un furuto generar un utili para las validaciones para el Adicional 
                           saldoAdicional = SaldoDAO.getSaldo(idGrupo, grupo.ciclos[i].idCreditoIBS);
                           myLogger.debug("estatus saldo: "+saldoAdicional.getEstatus());
                           if (saldoAdicional.getEstatus()==ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE ){  
                               semDispAdi = grupo.ciclos[i].getAceptaAdicional();
                               grupo.ciclos[i].setAceptaAdicional(AdicionalUtil.verificaSemanaAdicional(grupo.ciclos[i],saldoAdicional));
                           }else{
                               //JECB 19/07/2017
                               //se establece el campo de acepta adicional para que salga de manera correcta
                               //las solicitudes de credito adicional en el panel de documento
                               grupo.ciclos[i].setAceptaAdicional(AdicionalUtil.verificaSemanaAdicional(grupo.ciclos[i],saldoAdicional));
                           }
                            
                        }
                    } else {
                        grupo.ciclos[i].saldo = IBSHelper.getSaldosT24ToIBS(new SaldoT24DAO().getSaldosT24ByNumClienteSolicitudProducto(idGrupo, grupo.ciclos[i].idCiclo, ClientesConstants.GRUPAL));
                        grupo.ciclos[i].setAtrasos(0);
                    }
                    if (grupo.ciclos[i].saldo != null) {
                        grupo.ciclos[i].estatusT24 = grupo.ciclos[i].saldo.getEstatus();
                    }
                }
                if (CatalogoHelper.esSucursalInterCiclo(grupo.sucursal)) {
                    saldoIC = SaldoDAO.validaInterCiclo(idGrupo);
                }
            }

            session.setAttribute("GRUPO", grupo);
            session.setAttribute("SALDOIC", saldoIC);
        } catch (ClientesDBException dbe) {
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
}
