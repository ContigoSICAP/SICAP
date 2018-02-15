/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.commands;

import com.sicap.clientes.dao.cartera.SaldoFavorDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.TraspasoSaldoIxayaUtil;
import com.sicap.clientes.vo.ClienteVO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Alex
 */
public class CommandUsoSaldoFavor implements Command{

    private String siguiente;

    public CommandUsoSaldoFavor(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        //HttpSession session = request.getSession();
        Notification mensaje[] = new Notification[1];
        //ClienteVO cliente =(ClienteVO) session.getAttribute("CLIENTE");
        //BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandUsoSaldoFavor");

        int idGrupo = Integer.parseInt(request.getParameter("idGrupoSF"));
        int numCiclo = Integer.parseInt(request.getParameter("numCicloSF"));
        int aplico = 0;

        try {
            System.out.println("*******" + idGrupo + " " + numCiclo);
            SaldoFavorDAO usoSaldo = new SaldoFavorDAO();
            aplico = usoSaldo.aplicaSaldo(idGrupo, numCiclo);
            if (aplico == 1) {
                mensaje[0] = new Notification(ClientesConstants.ERROR_LEVEL, "Saldo a Favor Autorizado");
            } else {
                mensaje[0] = new Notification(ClientesConstants.ERROR_LEVEL, "ERROR: Uso de Saldo a Favor");
            }
            request.setAttribute("NOTIFICACIONES", mensaje);
            //bitutil.registraEvento(mensaje+" "+idGrupo+" "+numCiclo);
        } catch (Exception e) {
            System.out.println("ERROR: Uso de Saldo a Favor\n" + e);
        }
        return siguiente;
    }

}
