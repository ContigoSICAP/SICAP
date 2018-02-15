/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sicap.clientes.commands;

import com.sicap.clientes.dao.SaldoFondeadorDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FondeadorUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.SaldoFondeadorVO;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author avillanueva
 */
public class CommandCobranzaFondeador implements Command{
    
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandCobranzaFondeador.class);
    
    public CommandCobranzaFondeador(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        
        Vector<Notification> notificaciones = new Vector<Notification>();
        HttpSession session = request.getSession();
        SaldoFondeadorDAO saldoFondDAO = new SaldoFondeadorDAO();
        ArrayList<SaldoFondeadorVO> arrLista = null;
        FondeadorUtil fondeadorUtil = new FondeadorUtil();
        int respuesta = 0;
        
        int idFondeador = 0;
        try {
            if(request.getParameter("command").equals("buscaCobranza")){
                idFondeador = HTMLHelper.getParameterInt(request, "idFondeador");
                arrLista = saldoFondDAO.getListaCobranza(idFondeador);
                if(arrLista.isEmpty())
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Sin registro de Cobranza"));
            }else if(request.getParameter("command").equals("ingresaCobranza")){
                BitacoraUtil bitacora = new BitacoraUtil(idFondeador, request.getRemoteUser(), "CommandCobranzaFondeador");
                DecimalFormat num = new DecimalFormat("#######.00");
                SaldoFondeadorVO saldoFondVO = new SaldoFondeadorVO();
                double montoCobranza = HTMLHelper.getParameterDouble(request, "montoCobranza");
                idFondeador = HTMLHelper.getParameterInt(request, "idFondeador");
                saldoFondVO.setTipoMov(ClientesConstants.COBRANZA);
                saldoFondVO.setMonto(montoCobranza);
                saldoFondVO.setNumFondeador(idFondeador);
                saldoFondVO.setNumCliente(0);
                saldoFondVO.setNumCredito(0);
                saldoFondVO.setNumTransaccion(0);
                saldoFondVO.setOrigen(0);
                respuesta = fondeadorUtil.actualizaSaldoFondeador(saldoFondVO);
                bitacora.registraEvento(saldoFondVO);
                arrLista = saldoFondDAO.getListaCobranza(idFondeador);
            }
            request.setAttribute("listaCobranza", arrLista);
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch (ClientesDBException dbe) {
            myLogger.error("ClientesDBException", dbe);
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }    
}
