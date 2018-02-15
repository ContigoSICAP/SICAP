package com.sicap.clientes.helpers;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.vo.DecisionComiteVO;
import com.sicap.clientes.vo.SellFinanceVO;
import com.sicap.clientes.vo.SolicitudVO;

public class DecisionComiteHelper {

    public static DecisionComiteVO getDecisionVO(DecisionComiteVO decisionComiteActualizado, SolicitudVO solicitud, HttpServletRequest request) throws Exception {

        decisionComiteActualizado.idCliente = solicitud.idCliente;
        decisionComiteActualizado.idSolicitud = solicitud.idSolicitud;
        decisionComiteActualizado.fechaRealizacion = new java.sql.Date(HTMLHelper.getParameterDate(request, "fechaRealizacion").getTime());
        decisionComiteActualizado.fechaCaptura = new Timestamp(System.currentTimeMillis());
        decisionComiteActualizado.decisionComite = HTMLHelper.getParameterInt(request, "decisionComite");
        decisionComiteActualizado.causaRechazo = HTMLHelper.getParameterInt(request, "causaRechazo");
        decisionComiteActualizado.motivoRechazoCliente = HTMLHelper.getParameterInt(request, "motivoRechazoCliente");
        decisionComiteActualizado.detalleMotivoRechazoCliente = HTMLHelper.getParameterString(request, "detalleMotivoRechazoCliente");
        decisionComiteActualizado.montoSinComision = HTMLHelper.getParameterDouble(request, "montoSinComision");
        decisionComiteActualizado.montoAutorizado = HTMLHelper.getParameterDouble(request, "montoAutorizado");
        decisionComiteActualizado.plazoAutorizado = HTMLHelper.getParameterInt(request, "plazoAutorizado");
        if (request.getParameter("fechaValor") != null && !request.getParameter("fechaValor").equals("")) {
            decisionComiteActualizado.fechaValor = new java.sql.Date(HTMLHelper.getParameterDate(request, "fechaValor").getTime());
        }
        decisionComiteActualizado.comision = HTMLHelper.getParameterInt(request, "comision");
        decisionComiteActualizado.tasa = HTMLHelper.getParameterInt(request, "tasa");
        decisionComiteActualizado.frecuenciaPago = HTMLHelper.getParameterInt(request, "frecuenciaPago");
        decisionComiteActualizado.motivoCondicionamiento = HTMLHelper.getParameterInt(request, "motivoCondicionamiento");
        decisionComiteActualizado.comentariosComite = HTMLHelper.getParameterString(request, "comentariosComite");
        if (solicitud.tipoOperacion == ClientesConstants.SELL_FINANCE) {
            if (solicitud.sellFinance == null) {
                solicitud.sellFinance = new SellFinanceVO();
            }
            solicitud.sellFinance.idCliente = solicitud.idCliente;
            solicitud.sellFinance.idSolicitud = solicitud.idSolicitud;
            solicitud.sellFinance.idPlan = HTMLHelper.getParameterInt(request, "planSellFinance");
        }
        decisionComiteActualizado.multa = HTMLHelper.getParameterDouble(request, "multa");
        return decisionComiteActualizado;
    }
}
