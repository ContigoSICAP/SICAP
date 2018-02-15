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

public class CommandConsultaSolicitudesEstatus implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandConsultaSolicitudesEstatus.class);

    public CommandConsultaSolicitudesEstatus(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        try {
            int sucursal = HTMLHelper.getParameterInt(request, "sucursal");
            int estatus = HTMLHelper.getParameterInt(request, "estatusSolicitud");
            Date fechaInicio = Convertidor.stringToSqlDate(request.getParameter("fechaInicio"));
            Date fechaFin = Convertidor.stringToSqlDate(request.getParameter("fechaFin"));
            int numCliente = HTMLHelper.getParameterInt(request, "numCliente");            
            ReporteDAO reporteDAO = new ReporteDAO();
            ReporteVO[] reportesVO = reporteDAO.getSolicitudesEstatus(sucursal, estatus, numCliente, fechaInicio, fechaFin);
            for (int i = 0; i < reportesVO.length; i++) {
                if (reportesVO[i].calificacionCC !=99 && reportesVO[i].calificacionMesa !=99) {
                    reportesVO[i].calificacion = reportesVO[i].calificacionCC;
                    if (reportesVO[i].calificacionMesa != 0) {
                        reportesVO[i].calificacion = reportesVO[i].calificacionMesa;
                    }
                } else if (reportesVO[i].idSolicitud == 1) {
                    reportesVO[i].calificacion = ClientesConstants.CALIFICACION_CIRCULO_SIN_CONSULTA;
                } else {
                    reportesVO[i].calificacion = ClientesConstants.CALIFICACION_CIRCULO_NA;
                }
            }
            request.setAttribute("REPORTES", reportesVO);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return siguiente;
    }

}
