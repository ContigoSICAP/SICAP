package com.sicap.clientes.commands;

import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.CreditoDAO;
import com.sicap.clientes.dao.InformacionCrediticiaDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.ScoringUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.InformacionCrediticiaVO;
import org.apache.log4j.Logger;

public class CommandConsultaCicloGrupal implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandConsultaCicloGrupal.class);

    public CommandConsultaCicloGrupal(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        //Notification notificaciones[] = new Notification[1];
        CicloGrupalVO ciclo = new CicloGrupalVO();
        try {
            int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            int idOperacion = HTMLHelper.getParameterInt(request, "idOperacion");

            if (idCiclo == 0) {
                //Asigno ciclo 1 si el request biene en cero es la unica opcion pocible
                ciclo.idGrupo = idGrupo;
                ciclo.idTipoCiclo = ClientesConstants.CICLO_NATURAL;
                if (idOperacion == ClientesConstants.GRUPAL) {
                    HttpSession session = request.getSession();
                    GrupoVO grupo = (GrupoVO) session.getAttribute("GRUPO");
                    //SolicitudVO[] solicitudes = null;
                    ciclo.integrantes = new IntegranteCicloDAO().getIntegrantesNuevoCiclo(idGrupo, idOperacion);
                    ciclo.tablaAmortizacion = new TablaAmortizacionDAO().getElementos(idGrupo, idCiclo, 1);
                    TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(idOperacion);
                    for (int i = 0; ciclo.integrantes != null && i < ciclo.integrantes.length; i++) {
                        myLogger.debug("CLIENTES " + ciclo.integrantes[i].idCliente);
                        //VERIFICANDO CONSULTA CIRCULO
                        InformacionCrediticiaVO conusltaCrediticia = new InformacionCrediticiaDAO().getLastInfoCrediticia(ciclo.integrantes[i].idCliente);
                        //VERIFICANDO CALIFICACION MESA DE CONTROL SI NO LA CALCULA DE ACUERDO AL RESULTADO XML
                        CreditoVO informacionCrediticia = new CreditoDAO().getCredito(ciclo.integrantes[i].idCliente, ciclo.integrantes[i].idSolicitud, 2);
                        if (informacionCrediticia != null) {
                            if (informacionCrediticia.calificacionMesaControl != 0) {
                                ciclo.integrantes[i].calificacion = informacionCrediticia.calificacionMesaControl;
                            } else if (conusltaCrediticia != null) {
                                ciclo.integrantes[i].calificacion = informacionCrediticia.comportamiento;
                                ciclo.integrantes[i].calificacionAutomatica = informacionCrediticia.comportamiento;
                            } else {
                                ciclo.integrantes[i].calificacion = ScoringUtil.getCalificacionCirculo(conusltaCrediticia);
                            }
                        } else {
                            ciclo.integrantes[i].calificacion = ScoringUtil.getCalificacionCirculo(conusltaCrediticia);
                        }

                        if (ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_SIN_CONSULTA && ciclo.integrantes[i].idSolicitud > 1) {
                            ciclo.integrantes[i].calificacion = ClientesConstants.CALIFICACION_CIRCULO_NA;
                        }
                        //Calculo su monto a desembolsar quitandole la comision de Decicion Comite
                        ciclo.integrantes[i].monto = ClientesUtil.calculaMontoSinComision(ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisiones);

                        ciclo.integrantes[i].comision = GrupoUtil.obtieneComisionIntegrante(ciclo.integrantes[i], grupo);

                        //ajusto nuevo monto a nueva Comision
                        ciclo.integrantes[i].monto = ClientesUtil.calculaMontoConComision(ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisiones);

                    }
                    request.setAttribute("ID_CICLO", new CicloGrupalDAO().getNext(idGrupo));
                } else if (idOperacion == ClientesConstants.REESTRUCTURA_GRUPAL) {
                    HttpSession session = request.getSession();
                    GrupoVO grupo = (GrupoVO) session.getAttribute("GRUPO");
                    GrupoUtil.obtieneRestructuraComunal(grupo);
                    ciclo = grupo.ciclos[0];
                    request.setAttribute("CICLO_NUEVO", ciclo.idCiclo);
                    session.setAttribute("INTEGRANTES_CICLO_SESION", ciclo.idCiclo);
                    session.setAttribute("GRUPO", grupo);
                }
            }
            request.setAttribute("CICLO", ciclo);
			//Actualiza el cliente en sesion
            //session.setAttribute("CLIENTE", cliente);
        } catch (ClientesDBException dbe) {
            myLogger.debug("ClientesDBException", dbe);
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            myLogger.debug("Exception", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }

}
