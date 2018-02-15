package com.sicap.clientes.commands;

import java.util.ArrayList;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.CreditoDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.InformacionCrediticiaDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.ScoringUtil;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.GrupoVO;

import com.sicap.clientes.vo.InformacionCrediticiaVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.SolicitudVO;
import java.util.Date;
import java.util.Vector;

public class CommandAperturaNuevoCiclo implements Command {

    private String siguiente;

    public CommandAperturaNuevoCiclo(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        Vector<Notification> notificaciones = new Vector<Notification>();
        ArrayList<IntegranteCicloVO> integrantesCicloActual = new ArrayList<IntegranteCicloVO>();
        ArrayList<IntegranteCicloVO> integrantesNuevoCiclo = new ArrayList<IntegranteCicloVO>();
        GrupoVO grupo = new GrupoVO();
        SolicitudVO solicitudes[] = null;
        try {
            int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
            int idCiclo = new CicloGrupalDAO().getCicloActual(idGrupo);
            grupo = new GrupoDAO().getGrupo(idGrupo);
            if(!CatalogoHelper.esSucursaldeIxaya(grupo.sucursal)){
                integrantesCicloActual = new IntegranteCicloDAO().getIntegrantesCicloActual(grupo.idGrupo, idCiclo);
                TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(grupo.idOperacion);
                integrantesNuevoCiclo = new IntegranteCicloDAO().getNuevosIntegrantes(idGrupo);
                SolicitudDAO solicituddao = new SolicitudDAO();
                for (IntegranteCicloVO integranteNuevo : integrantesNuevoCiclo) {
                    integranteNuevo.monto = ClientesUtil.calculaMontoSinComision(integranteNuevo.monto, integranteNuevo.comision, catComisiones);
                    CreditoVO informacionCrediticia = new CreditoDAO().getCredito(integranteNuevo.idCliente, integranteNuevo.idSolicitud, 2);
                    if (informacionCrediticia != null) {
                        integranteNuevo.calificacion = informacionCrediticia.comportamiento;
                        integranteNuevo.calificacionAutomatica = informacionCrediticia.comportamiento;
                        if (informacionCrediticia.calificacionMesaControl != 0) {
                            integranteNuevo.calificacion = informacionCrediticia.calificacionMesaControl;
                        }
                        integranteNuevo.aceptaRegular = informacionCrediticia.aceptaRegular;
                    } else if (integranteNuevo.getIdSolicitud() == 1) {
                        integranteNuevo.calificacion = ClientesConstants.CALIFICACION_CIRCULO_SIN_CONSULTA;
                    } else {
                        integranteNuevo.calificacion = ClientesConstants.CALIFICACION_CIRCULO_NA;
                    }
                    integranteNuevo.comision = GrupoUtil.obtieneComisionIntegrante(integranteNuevo, grupo);
                    integranteNuevo.monto = ClientesUtil.calculaMontoConComision(integranteNuevo.monto, integranteNuevo.comision, catComisiones);
                    solicitudes = solicituddao.getSolicitudes(integranteNuevo.idCliente);
                        for (int y = 0; y < solicitudes.length; y++) {
                            if (solicitudes[y].documentacionCompleta == 1 && (solicitudes[y].desembolsado == ClientesConstants.DESEMBOLSADO)) {
                                integranteNuevo.RenovacionDoc = 0;
                                if (FechasUtil.obtenAniosDiferencia(solicitudes[y].fechaFirma, new Date()) == 1) {
                                    integranteNuevo.RenovacionDoc = 1;                                
                                }
                            }
                        }
                        if (integranteNuevo.RenovacionDoc==0 && (solicitudes[solicitudes.length - 1].documentacionCompleta>0|| solicitudes.length == 1)){
                            integranteNuevo.RenovacionDoc = 1;
                        }
                        if(integranteNuevo.RenovacionDoc == 1&& solicitudes[solicitudes.length-1].documentacionCompleta>0){
                            integranteNuevo.DocCompletos = 1;
                        }
                }
                for (IntegranteCicloVO integrantesActual : integrantesCicloActual) {
                    if (!GrupoHelper.existeNuevoIntegrante(integrantesNuevoCiclo, integrantesActual)) {
                        integrantesActual.monto = ClientesUtil.calculaMontoSinComision(integrantesActual.monto, integrantesActual.comision, catComisiones);
                        integrantesActual.calificacion = ClientesConstants.CALIFICACION_CIRCULO_BUENA;
                        integrantesActual.idSolicitud = 0;
                        integrantesActual.rol = 0;
                        integrantesActual.comision = GrupoUtil.obtieneComisionIntegrante(integrantesActual, grupo);
                        integrantesActual.monto = ClientesUtil.calculaMontoConComision(integrantesActual.monto, integrantesActual.comision, catComisiones);
                        CreditoVO informacionCrediticia = new CreditoDAO().getCredito(integrantesActual.idCliente, integrantesActual.idSolicitud, 2);
                        if (informacionCrediticia != null && informacionCrediticia.calificacionMesaControl > ClientesConstants.CALIFICACION_CIRCULO_BUENA) {
                            integrantesActual.calificacion = informacionCrediticia.calificacionMesaControl;
                        }
                        solicitudes = solicituddao.getSolicitudes(integrantesActual.idCliente);
                        for (int y = 0; y < solicitudes.length; y++) {
                            if (solicitudes[y].documentacionCompleta == 1 && (solicitudes[y].desembolsado == ClientesConstants.DESEMBOLSADO)) {
                                integrantesActual.RenovacionDoc = 0;
                                if (FechasUtil.obtenAniosDiferencia(solicitudes[y].fechaFirma, new Date()) == 1) {
                                    integrantesActual.RenovacionDoc = 1;                                
                                }
                            }
                        }
                        if (integrantesActual.RenovacionDoc==0 && (solicitudes[solicitudes.length - 1].documentacionCompleta>0 || solicitudes.length == 1)){
                            integrantesActual.RenovacionDoc = 1;
                        }
                        if(integrantesActual.RenovacionDoc == 1&& solicitudes[solicitudes.length-1].documentacionCompleta>0){
                            integrantesActual.DocCompletos = 1;
                        }

                        integrantesNuevoCiclo.add(integrantesActual);
                    }
                }
                request.setAttribute("INTEGRANTES_NUEVO_CICLO", integrantesNuevoCiclo);
            }else{
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "El movimiento se debe de realizar en el sistema JUCAVI"));
                request.setAttribute("NOTIFICACIONES", notificaciones);
                siguiente = "/capturaGrupo.jsp";
            }
        } catch (ClientesDBException dbe) {
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
}
