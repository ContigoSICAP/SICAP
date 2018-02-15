package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import javax.servlet.http.HttpServletRequest;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.SaldoIBSVO;

public class CommandListaGrupos implements Command {

    private String siguiente;

    public CommandListaGrupos(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        SaldoIBSVO saldoVO = new SaldoIBSVO();
        SaldoIBSDAO saldoIBSDAO = new SaldoIBSDAO();
        Notification notificaciones[] = new Notification[1];
        int idGrupo = 0;
        int idCiclo = 0;
        CicloGrupalVO ciclo = new CicloGrupalVO();
        CicloGrupalDAO cicloDAO = new CicloGrupalDAO();
        GrupoVO grupo = new GrupoVO();
        GrupoDAO grupoDAO = new GrupoDAO();
        int status = ClientesConstants.SITUACION_CREDITO_SALDOST24_VENCIDO;
        try {
            idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
            idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            if (idCiclo > 0 && idGrupo > 0) {
                ciclo = cicloDAO.getCiclo(idGrupo, idCiclo);
                grupo = grupoDAO.getGrupo(idGrupo);
                if (ciclo != null) {
                    saldoVO = saldoIBSDAO.getSaldoStatus(ciclo.idGrupo, ciclo.idCreditoIBS, status);
                }
                if (saldoVO != null) {
                    request.setAttribute("SALDOS", saldoVO);
                    request.setAttribute("CICLO", ciclo);
                    request.setAttribute("GRUPO", grupo);
                    notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Dividendos encontrados");
                    request.setAttribute("NOTIFICACIONES", notificaciones);

                } //fin if
                else {
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "El cr&eacute;dito para el grupo y ciclo indicados no se encuentra vencido");
                    request.setAttribute("NOTIFICACIONES", notificaciones);
                }	//fin else
            } //fin if
            else {
                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha seleccionado un grupo con creditos en mora o vencido");
                request.setAttribute("NOTIFICACIONES", notificaciones);
            }  //fin else
        } catch (ClientesDBException dbe) {
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
}
