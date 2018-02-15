package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.dao.PagoDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;

public class CommandEliminaCicloGrupal implements Command {

    private String siguiente;

    public CommandEliminaCicloGrupal(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        Notification notificaciones[] = new Notification[1];
        CicloGrupalVO ciclo = new CicloGrupalVO();
        CicloGrupalDAO cicloDao = new CicloGrupalDAO();
        ReferenciaGeneralDAO referenciaDao = new ReferenciaGeneralDAO();
        IntegranteCicloDAO integrantesDao = new IntegranteCicloDAO();
        SolicitudDAO solicitudes = new SolicitudDAO();
        OrdenDePagoDAO ordenesPago = new OrdenDePagoDAO();
        SaldoIBSVO saldosVO = new SaldoIBSVO();
        SaldoIBSDAO saldosDAO = new SaldoIBSDAO();
        TablaAmortizacionDAO tablaAmort = new TablaAmortizacionDAO();
        CreditoCartVO credito = new CreditoCartVO();
        CreditoCartDAO creditoDAO = new CreditoCartDAO();
        TablaAmortDAO tablaCartera = new TablaAmortDAO();
        PagoDAO pagoCartera = new PagoDAO();

        try {
            ciclo.idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
            ciclo.idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            BitacoraUtil bitutil = new BitacoraUtil(ciclo.idGrupo, request.getRemoteUser(), "CommandEliminaCicloGrupal");
            IntegranteCicloVO[] integrantes = integrantesDao.getIntegrantes(ciclo.idGrupo, ciclo.idCiclo);
            saldosVO = saldosDAO.getSaldos(ciclo.idGrupo, ciclo.idCiclo);
            String referencia = "";
            if(saldosVO.getReferencia() != null)
                referencia = saldosVO.getReferencia();
            if(saldosVO.getReferencia() != null && request.getParameter("credito") != null){
                referencia = "";
                saldosDAO.deleteSaldosIBS(saldosVO.getCredito(), false);
                saldosDAO.deleteSaldosIBS(saldosVO.getCredito(), true);
            }

            if (referencia.equals("")) {
                boolean isDesembolsado = GrupoHelper.isDesembolsado(integrantes);
                cicloDao.deleteCicloCredito(ciclo);
                referenciaDao.deleteReferencia(ciclo);
                bitutil.registraEvento(ciclo);
                tablaAmort.delTablaAmortizacion(ciclo.idGrupo, ciclo.idCiclo);
                if (!isDesembolsado) {
                    notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "El ciclo se eliminó correctamente");
                } else {
                    credito = creditoDAO.getCreditoClienteSol(ciclo.idGrupo, ciclo.idCiclo);
                    System.out.println("credito "+credito);
                    if (credito != null){
                        creditoDAO.deleteCreditoCiclo(credito);
                        tablaCartera.delTablaAmortizacion(credito.getNumCliente(), credito.getNumCredito(), false);
                        tablaCartera.delTablaAmortizacion(credito.getNumCliente(), credito.getNumCredito(), true);
                        //BORRAR LOS PAGOS APLICADOS AL NUEVO CICLO COMO GARANTIA
                        //pagoCartera.deletePagoCiclo(credito.getReferencia());
                    }
                    for (int i = 0; i < integrantes.length; i++) {
                        IntegranteCicloVO integrante = new IntegranteCicloVO();
                        integrante = integrantes[i];
                        solicitudes.updateSolicitudDesembolsada(integrante.idCliente, integrante.idSolicitud);
                        ordenesPago.deleteOrdenPago(integrante.idCliente, integrante.idSolicitud);
                    }
                    notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "El ciclo DESEMBOLSADO se eliminó correctamente");
                }
            } else {
                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "El ciclo no se puede eliminar debido a que se encuentra Activo en Cartera");
            }
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch (ClientesDBException dbe) {
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
}
