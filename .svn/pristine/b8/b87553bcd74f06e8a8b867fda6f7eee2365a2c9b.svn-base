package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CreditoDAO;
import java.sql.Date;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.ReporteDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.ReporteUtil;
import com.sicap.clientes.vo.ReporteVO;
import org.apache.log4j.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LDAVILA
 */
public class CommandActualizacionDocumentos implements Command {
    
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandActualizacionDocumentos.class);
    
    public CommandActualizacionDocumentos (String siguiente) {
        this.siguiente = siguiente;
    }
    
     public String execute(HttpServletRequest request) throws CommandException {
          try {
            int sucursal = HTMLHelper.getParameterInt(request, "sucursal");
            int mes = HTMLHelper.getParameterInt(request, "mes");          
            ReporteDAO reporteDAO = new ReporteDAO();
            ReporteVO[] reportesVO = reporteDAO.getActualizacionDoc(sucursal, mes);
            String filename = "DocumentosVencidos" + sucursal + mes + ".CSV";
            request.setAttribute("FILE", reportesVO);
            request.setAttribute("FILENAME", filename);
            request.setAttribute("REPORTES", reportesVO);
        } catch (Exception exc) {
            myLogger.error("Error en CommandActualizacionDocumentos", exc);
        }
        return siguiente;
    }
    
}
