/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.commands;

import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.vo.SaldoIBSVO;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Alex
 */
public class CommandBuscaCarteraCedida implements Command {
    
    private String siguiente;
    
    public CommandBuscaCarteraCedida(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        
        ArrayList<SaldoIBSVO> saldos = new ArrayList<SaldoIBSVO>();
        SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
        try {
            int sucursal = HTMLHelper.getParameterInt(request, "sucursal");
            saldos = saldoDAO.getAplicarCarteraCedida(sucursal);
            request.setAttribute("SALDOSCARTERA", saldos);
        } catch (ClientesDBException dbe) {
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        }
        
        return siguiente;
    }
}
