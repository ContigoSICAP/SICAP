package com.sicap.clientes.commands;

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

public class CommandConsultaSolicitudes implements Command {

    private String siguiente;

    public CommandConsultaSolicitudes(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        try {
            int sucursal = HTMLHelper.getParameterInt(request, "sucursal");
            int estatus = HTMLHelper.getParameterInt(request, "estatus");
            int producto = HTMLHelper.getParameterInt(request, "producto");
            Date fechaInicio = Convertidor.stringToSqlDate(request.getParameter("fechaInicio"));
            Date fechaFin = Convertidor.stringToSqlDate(request.getParameter("fechaFin"));
            if (fechaInicio == null) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, -7);
                fechaInicio = new Date(cal.getTimeInMillis());
            }
            if (fechaFin == null) {
                Calendar cal = Calendar.getInstance();
                fechaFin = new Date(cal.getTimeInMillis());
            }
            ReporteDAO reporteDAO = new ReporteDAO();
            ReporteVO[] reportesVO = null;

            if (producto == ClientesConstants.GRUPAL) {
                reportesVO = reporteDAO.getCiclosPorSucursalEstatus(sucursal, estatus, producto, fechaInicio, fechaFin);
            } else {
                reportesVO = reporteDAO.getSolicitudesPorSucursalEstatus(sucursal, estatus, producto, fechaInicio, fechaFin);
            }

//            reportesVO = new ReporteUtil().obtenerDictamen(reportesVO);
//            reportesVO = new ReporteUtil().obtenerUltimosUsuarios(reportesVO);
            request.setAttribute("REPORTES", reportesVO);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return siguiente;
    }
}
