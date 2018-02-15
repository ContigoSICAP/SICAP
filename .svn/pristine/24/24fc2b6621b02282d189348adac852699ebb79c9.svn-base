/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.SaldoFondeadorDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.vo.CatalogoVO;
import com.sicap.clientes.vo.ParametroVO;
import com.sicap.clientes.vo.SaldoFondeadorVO;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author avillanueva
 */
public class CommandSaldoFondeador implements Command{
    
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandSaldoFondeador.class);
    
    public CommandSaldoFondeador(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        
        CatalogoDAO catalogoDAO = new CatalogoDAO();
        SaldoFondeadorDAO saldoFonDAO = new SaldoFondeadorDAO();
        ArrayList<CatalogoVO> arrCatFondeador = new ArrayList<CatalogoVO>();
        ArrayList<SaldoFondeadorVO> arrFondeador = new ArrayList<SaldoFondeadorVO>();
        double saldoIni = 0, saldoFin = 0;
        try {
            arrCatFondeador = catalogoDAO.getCatalogoFondeadores();
            arrFondeador = new ArrayList<SaldoFondeadorVO>();
            for (CatalogoVO fondeador : arrCatFondeador) {
                saldoIni = saldoFonDAO.montoSaldo(1, fondeador.getId());
                saldoFin = saldoFonDAO.montoSaldo(2, fondeador.getId());
                arrFondeador.add(new SaldoFondeadorVO(fondeador.getId(), fondeador.getDescripcion(), 
                        (saldoIni == -1 ? saldoFonDAO.montoSaldo(3, fondeador.getId()): saldoIni),//SALDO INICIAL
                        (saldoFin == -1 ? Convertidor.stringToDouble(CatalogoHelper.getParametro("SALDO_FONDEADOR_"+fondeador.getId())): saldoFin),//SALDO FINAL
                        saldoFonDAO.montoDispersion(fondeador.getId()),//DISPERSIONES
                        saldoFonDAO.montoCancelacion(fondeador.getId()),//CANCELACIONES
                        //saldoFonDAO.montoCobranza(fondeador.getId())));//COBRANZA
                        0));//COBRANZA
            }
            request.setAttribute("SaldoFondeadores", arrFondeador);
        } catch (Exception e) {
            myLogger.error("execute", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
    
}
