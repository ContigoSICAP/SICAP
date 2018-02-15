package com.sicap.clientes.commands;

import java.util.ArrayList;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.CreditoDAO;
import com.sicap.clientes.dao.InformacionCrediticiaDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.ScoringUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.InformacionCrediticiaVO;
import com.sicap.clientes.vo.IntegranteCicloVO;

public class CommandRenovacionGrupal implements Command {

    private String siguiente;

    public CommandRenovacionGrupal(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        //Notification notificaciones[] = new Notification[1];
        HttpSession session = request.getSession();
        IntegranteCicloVO[] integrantesCicloAnterior = null;
        ArrayList<IntegranteCicloVO> arrayTemporal = new ArrayList<IntegranteCicloVO>();
        IntegranteCicloVO[] nuevosIntegrantes = null;
        IntegranteCicloVO[] integrantesNuevoCiclo = null;
        GrupoVO grupo = new GrupoVO();

        try {
            grupo = (GrupoVO) session.getAttribute("GRUPO");
            int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            int idOperacion = HTMLHelper.getParameterInt(request, "idOperacion");
            integrantesCicloAnterior = new IntegranteCicloDAO().getIntegrantes(grupo.idGrupo, grupo.ciclos[grupo.ciclos.length - 1].idCiclo);
            TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(idOperacion);
            if (idCiclo == 0) {
                nuevosIntegrantes = new IntegranteCicloDAO().getIntegrantesNuevoCiclo(idGrupo, idOperacion);
                if (nuevosIntegrantes != null) {
                    //************************************************************************************
                    //comparo ambos integrantes y descarto solicitudes ya creadas
                    CicloGrupalVO temporal = new CicloGrupalVO();
                    temporal.integrantes = nuevosIntegrantes;
                    for (int i = 0; i < integrantesCicloAnterior.length; i++) {
                        if (!GrupoHelper.existeIntegrante(temporal, integrantesCicloAnterior[i])) {
                            arrayTemporal.add(integrantesCicloAnterior[i]);
                        }
                    }
                    integrantesCicloAnterior = new IntegranteCicloVO[arrayTemporal.size()];
                    for (int i = 0; i < integrantesCicloAnterior.length; i++) {
                        integrantesCicloAnterior[i] = (IntegranteCicloVO) arrayTemporal.get(i);
                    }
                    //************************************************************************************
                    integrantesNuevoCiclo = new IntegranteCicloVO[integrantesCicloAnterior.length + nuevosIntegrantes.length];
                } else {
                    integrantesNuevoCiclo = new IntegranteCicloVO[integrantesCicloAnterior.length];
                }

                for (int i = 0; i < integrantesCicloAnterior.length; i++) {
                    integrantesNuevoCiclo[i] = integrantesCicloAnterior[i];
                    //Calculo su monto a desembolsar quitandole la comision de Decicion Comite
                    integrantesNuevoCiclo[i].monto = ClientesUtil.calculaMontoSinComision(integrantesNuevoCiclo[i].monto, integrantesNuevoCiclo[i].comision, catComisiones);

                    //CALIFICACION
                    //solicitud = new SolicitudDAO().getSolicitud(integrantesNuevoCiclo[i].idCliente, integrantesNuevoCiclo[i].idSolicitud);
                    integrantesNuevoCiclo[i].calificacion = ClientesConstants.CALIFICACION_CIRCULO_BUENA;
                    integrantesNuevoCiclo[i].idSolicitud = 0;
                    integrantesNuevoCiclo[i].rol = 0;
                    //Obtenie Comision del integrante
                    integrantesNuevoCiclo[i].comision = GrupoUtil.obtieneComisionIntegrante(integrantesNuevoCiclo[i], grupo);
                    //Ajusto nuevo monto a nueva Comision
                    integrantesNuevoCiclo[i].monto = ClientesUtil.calculaMontoConComision(integrantesNuevoCiclo[i].monto, integrantesNuevoCiclo[i].comision, catComisiones);
                    CreditoVO informacionCrediticia = new CreditoDAO().getCredito(integrantesNuevoCiclo[i].idCliente, integrantesNuevoCiclo[i].idSolicitud, 2);
                    if(informacionCrediticia != null && informacionCrediticia.calificacionMesaControl > ClientesConstants.CALIFICACION_CIRCULO_BUENA)
                            integrantesNuevoCiclo[i].calificacion = informacionCrediticia.calificacionMesaControl;
                }
                for (int i = integrantesCicloAnterior.length; nuevosIntegrantes != null && i < integrantesNuevoCiclo.length; i++) {
                    integrantesNuevoCiclo[i] = nuevosIntegrantes[i - integrantesCicloAnterior.length];
                    //Calculo su monto a desembolsar quitandole la comision de Decicion Comite
                    integrantesNuevoCiclo[i].monto = ClientesUtil.calculaMontoSinComision(integrantesNuevoCiclo[i].monto, integrantesNuevoCiclo[i].comision, catComisiones);

                    /*if( integrantesNuevoCiclo[i].calificacion==0 )
                    integrantesNuevoCiclo[i].calificacion=ClientesConstants.CALIFICACION_CIRCULO_NA;*/
//					VERIFICANDO CONSULTA A CIRCULO
                    InformacionCrediticiaVO conusltaCrediticia = new InformacionCrediticiaDAO().getLastInfoCrediticia(integrantesNuevoCiclo[i].idCliente);
                    if (integrantesNuevoCiclo[i].idSolicitud == 1) {
                        //VERIFICANDO CALIFICACION MESA DE CONTROL SI NO LA CALCULA DE ACUERDO AL RESULTADO XML
                        CreditoVO informacionCrediticia = new CreditoDAO().getCredito(integrantesNuevoCiclo[i].idCliente, integrantesNuevoCiclo[i].idSolicitud, 2);
                        if (informacionCrediticia != null) {
                            if (informacionCrediticia.calificacionMesaControl != 0) {
                                integrantesNuevoCiclo[i].calificacion = informacionCrediticia.calificacionMesaControl;
                            } else if (conusltaCrediticia != null) {
                                integrantesNuevoCiclo[i].calificacion = informacionCrediticia.comportamiento;
                                integrantesNuevoCiclo[i].calificacionAutomatica = informacionCrediticia.comportamiento;
                            } else {
                                integrantesNuevoCiclo[i].calificacion = ScoringUtil.getCalificacionCirculo(conusltaCrediticia);
                            }
                        } else {
                            integrantesNuevoCiclo[i].calificacion = ScoringUtil.getCalificacionCirculo(conusltaCrediticia);
                        }
                    } else {
                        integrantesNuevoCiclo[i].calificacion = ClientesConstants.CALIFICACION_CIRCULO_BUENA;
                        CreditoVO informacionCrediticia = new CreditoDAO().getCredito(integrantesNuevoCiclo[i].idCliente, integrantesNuevoCiclo[i].idSolicitud, 2);
                        if(informacionCrediticia != null && informacionCrediticia.calificacionMesaControl > ClientesConstants.CALIFICACION_CIRCULO_BUENA)
                            integrantesNuevoCiclo[i].calificacion = informacionCrediticia.calificacionMesaControl;
                    }

                    //Obtenie Comision del integrante
                    integrantesNuevoCiclo[i].comision = GrupoUtil.obtieneComisionIntegrante(integrantesNuevoCiclo[i], grupo);

                    //Ajusto nuevo monto a nueva Comision
                    integrantesNuevoCiclo[i].monto = ClientesUtil.calculaMontoConComision(integrantesNuevoCiclo[i].monto, integrantesNuevoCiclo[i].comision, catComisiones);
                }
            }
            //Calculo el siclo sigueinte y se manda por request // Contemplar catalogo matriz.
            request.setAttribute("ID_CICLO", new CicloGrupalDAO().getNext(idGrupo));
            request.setAttribute("INTEGRANTES_NUEVO_CICLO", integrantesNuevoCiclo);
            session.setAttribute("INTEGRANTES_CICLO_SESION", integrantesNuevoCiclo);
            //Actualiza el cliente en sesion
            //session.setAttribute("CLIENTE", cliente);
        } catch (ClientesDBException dbe) {
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
}
