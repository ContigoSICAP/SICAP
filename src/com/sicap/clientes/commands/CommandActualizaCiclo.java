package com.sicap.clientes.commands;

import com.sicap.clientes.dao.ArchivoAsociadoDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.DireccionGenericaDAO;
import com.sicap.clientes.dao.EmpleoDAO;
import com.sicap.clientes.dao.EventosPagosGrupalDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class CommandActualizaCiclo implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandActualizaCiclo.class);

    public CommandActualizaCiclo(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        HttpSession session = request.getSession();
        GrupoVO grupo = new GrupoVO();
        CicloGrupalDAO ciclodao = new CicloGrupalDAO();
        IntegranteCicloVO[] integrantesCicloVO = null;
        IntegranteCicloVO[] integrantesIntercicloVO = null;
        IntegranteCicloVO elementos[] = null;
        try {
            TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
            grupo = (GrupoVO) session.getAttribute("GRUPO");
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            myLogger.debug("Ciclo :" + idCiclo);
            int i = 0;

            if (idCiclo != 0) {
                myLogger.debug("En if");
                int indice = GrupoUtil.getIndiceCiclo(grupo.ciclos, idCiclo);
                grupo.ciclos[indice] = new CicloGrupalDAO().getCiclo(grupo.idGrupo, idCiclo);
                if (grupo.ciclos[indice].estatusIC == ClientesConstants.CICLO_DESEMBOLSO || grupo.ciclos[indice].estatusIC2 == ClientesConstants.CICLO_DESEMBOLSO) {
                    integrantesIntercicloVO = new IntegranteCicloDAO().getIntegrantesNuevoInterCiclo(grupo.getIdGrupo(), idCiclo);
                }
                if (integrantesIntercicloVO != null) {
                    integrantesCicloVO = new IntegranteCicloDAO().getIntegrantes(grupo.idGrupo, idCiclo);
                    elementos = new IntegranteCicloVO[integrantesCicloVO.length + integrantesIntercicloVO.length];
                    for (i = 0; i < integrantesCicloVO.length; i++) {
                        elementos[i] = (IntegranteCicloVO) integrantesCicloVO[i];
                    }
                    for (int j = 0; j < integrantesIntercicloVO.length; j++) {
                        if (integrantesIntercicloVO[j].semDisp == ClientesConstants.DISPERSION_SEMANA_2) {
                            integrantesIntercicloVO[j].tipo = ClientesConstants.TIPO_CLIENTE_INTERCICLO;
                        } else if (integrantesIntercicloVO[j].semDisp == ClientesConstants.DISPERSION_SEMANA_4) {
                            integrantesIntercicloVO[j].tipo = ClientesConstants.TIPO_CLIENTE_INTERCICLO_2;
                        }
                        elementos[i + j] = (IntegranteCicloVO) integrantesIntercicloVO[j];
                    }
                    grupo.ciclos[indice].integrantes = elementos;
                } else {
                    grupo.ciclos[indice].integrantes = new IntegranteCicloDAO().getIntegrantes(grupo.idGrupo, idCiclo);
                }
                if (grupo.ciclos[indice].idCreditoIBS != 0) {
                    grupo.ciclos[indice].saldo = new SaldoIBSDAO().getSaldos(grupo.idGrupo, grupo.ciclos[indice].idCiclo);
                }
                for (int a = 0; grupo.ciclos[indice].integrantes != null && a < grupo.ciclos[indice].integrantes.length; a++) {
                    grupo.ciclos[indice].integrantes[a].montoDesembolso = ClientesUtil.calculaMontoSinComision(grupo.ciclos[indice].integrantes[a].monto, grupo.ciclos[indice].integrantes[a].comision, catComisionesGrupal);
                    if (grupo.ciclos[indice].idCreditoIBS == 0 && grupo.ciclos[indice].desembolsado != 2) {
                        grupo.ciclos[indice].integrantes[a].seguro = new SegurosDAO().compruebaSeguro(grupo.ciclos[indice].integrantes[a]);
                        grupo.ciclos[indice].integrantes[a].empleo = new EmpleoDAO().compruebaEmpleo(grupo.ciclos[indice].integrantes[a]);
                        grupo.ciclos[indice].integrantes[a].grupo = new IntegranteCicloDAO().getIntegranteActivo(grupo.ciclos[indice].integrantes[a].idCliente);
                    }
                }
                grupo.ciclos[indice].direccionReunion = new DireccionGenericaDAO().getDireccion(grupo.ciclos[indice].idDireccionReunion);
                grupo.ciclos[indice].direccionAlterna = new DireccionGenericaDAO().getDireccion(grupo.ciclos[indice].idDireccionAlterna);
                grupo.ciclos[indice].tablaAmortizacion = new TablaAmortizacionDAO().getElementos(grupo.idGrupo, grupo.ciclos[indice].idCiclo, ClientesConstants.AMORTIZACION_GRUPAL);
                grupo.ciclos[indice].referencia = new ReferenciaGeneralDAO().getReferencia(grupo.idGrupo, grupo.ciclos[indice].idCiclo, 'G');
                grupo.ciclos[indice].eventosDePago = new EventosPagosGrupalDAO().getEventosPagos(grupo.idGrupo, grupo.ciclos[indice].idCiclo);
                grupo.ciclos[indice].archivosAsociados = new ArchivoAsociadoDAO().getArchivosTipo(grupo.idGrupo, grupo.ciclos[indice].idCiclo);
            }
            session.setAttribute("GRUPO", grupo);
        } catch (ClientesDBException dbe) {
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }

}
