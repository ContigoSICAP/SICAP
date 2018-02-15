package com.sicap.clientes.commands;

import com.sicap.clientes.dao.ArchivoAsociadoDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.CreditoDAO;
import com.sicap.clientes.dao.DireccionGenericaDAO;
import com.sicap.clientes.dao.EmpleoDAO;
import com.sicap.clientes.dao.EventosPagosGrupalDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SaldoT24DAO;
import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.IBSHelper;
import com.sicap.clientes.helpers.SeguroHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.SeguroConstantes;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.SucursalVO;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class CommandObtenCiclo implements Command {
     private static Logger myLogger = Logger.getLogger(CommandObtenCiclo.class);

    private String siguiente;

    public CommandObtenCiclo(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        HttpSession session = request.getSession();
        GrupoVO grupo = new GrupoVO();
        CicloGrupalDAO ciclodao = new CicloGrupalDAO();
        CreditoVO informacionCrediticia = null;
        try {
            TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            if (session.getAttribute("GRUPO") == null) {
                grupo = new GrupoDAO().getGrupo(HTMLHelper.getParameterInt(request, "idGrupo"));
                grupo.ciclos = new CicloGrupalDAO().getCiclos(grupo.idGrupo);
                grupo.ciclos[0] = new CicloGrupalDAO().getCiclo(grupo.idGrupo, idCiclo);
                if (grupo.ciclos[0].idCreditoIBS != 0) {
                    grupo.ciclos[0].saldo = new SaldoIBSDAO().getSaldos(grupo.idGrupo, idCiclo);
                    grupo.ciclos[0].setAtrasos(new TablaAmortDAO().getAtrasos(grupo.idGrupo, grupo.ciclos[0].idCreditoIBS));
                } else {
                    grupo.ciclos[0].saldo = IBSHelper.getSaldosT24ToIBS(new SaldoT24DAO().getSaldosT24ByNumClienteSolicitudProducto(grupo.idGrupo, idCiclo, ClientesConstants.GRUPAL));
                    grupo.ciclos[0].setAtrasos(0);
                }
                if (grupo.ciclos[0].saldo != null) {
                    grupo.ciclos[0].estatusT24 = grupo.ciclos[0].saldo.getEstatus();
                }
            } else {
                grupo = (GrupoVO) session.getAttribute("GRUPO");
            }
            if (idCiclo != 0) {
                grupo.ciclos[idCiclo - 1].integrantes = new IntegranteCicloDAO().getIntegrantes(grupo.idGrupo, idCiclo);
                for (int a = 0; grupo.ciclos[idCiclo - 1].integrantes != null && a < grupo.ciclos[idCiclo - 1].integrantes.length; a++) {
                    if (grupo.ciclos[idCiclo - 1].integrantes[a].calificacion == ClientesConstants.CALIFICACION_CIRCULO_REGULAR) {
                        informacionCrediticia = new CreditoDAO().getCredito(grupo.ciclos[idCiclo - 1].integrantes[a].idCliente, grupo.ciclos[idCiclo - 1].integrantes[a].idSolicitud, 2);
                        if (informacionCrediticia != null) {
                            grupo.ciclos[idCiclo - 1].integrantes[a].aceptaRegular = informacionCrediticia.aceptaRegular;
                        } else {
                            if ( grupo.ciclos[idCiclo - 1].integrantes[a].getIdSolicitud() == 1) {
                                grupo.ciclos[idCiclo - 1].integrantes[a].calificacion  = ClientesConstants.CALIFICACION_CIRCULO_SIN_CONSULTA;
                            } else {
                                grupo.ciclos[idCiclo - 1].integrantes[a].calificacion = ClientesConstants.CALIFICACION_CIRCULO_NA;
                            }
                        }
                    }else if (grupo.ciclos[idCiclo - 1].integrantes[a].calificacion == ClientesConstants.CALIFICACION_CIRCULO_BUENA) {
                        informacionCrediticia = new CreditoDAO().getCredito(grupo.ciclos[idCiclo - 1].integrantes[a].idCliente, grupo.ciclos[idCiclo - 1].integrantes[a].idSolicitud, 2);
                        if (informacionCrediticia != null) {
                            grupo.ciclos[idCiclo - 1].integrantes[a].aceptaRegular = informacionCrediticia.aceptaRegular;
                        }
                    }
                    grupo.ciclos[idCiclo - 1].integrantes[a].montoDesembolso = ClientesUtil.calculaMontoSinComision(grupo.ciclos[idCiclo - 1].integrantes[a].monto, grupo.ciclos[idCiclo - 1].integrantes[a].comision, catComisionesGrupal);
                    if (grupo.ciclos[idCiclo - 1].idCreditoIBS == 0 && grupo.ciclos[idCiclo - 1].desembolsado != 2) {
                        grupo.ciclos[idCiclo - 1].integrantes[a].seguro = new SegurosDAO().compruebaSeguro(grupo.ciclos[idCiclo - 1].integrantes[a]);
                        grupo.ciclos[idCiclo - 1].integrantes[a].empleo = new EmpleoDAO().compruebaEmpleo(grupo.ciclos[idCiclo - 1].integrantes[a]);
                        grupo.ciclos[idCiclo - 1].integrantes[a].grupo = new IntegranteCicloDAO().getIntegranteActivo(grupo.ciclos[idCiclo - 1].integrantes[a].idCliente);
                    }
                     /**
                     * JECB 01/10/2017
                     * En caso de que al menos un integrante del ciclo tenga asignado un monto adicional 
                     * establecemos en True la propiedades tieneAsignadoCreditoAdicional del bean de
                     * CicloGrupalVO
                     */
                    if(!grupo.ciclos[idCiclo - 1].tieneAsignadoCreditoAdicional && grupo.ciclos[idCiclo - 1].integrantes[a].montoAdicional > 0){
                        grupo.ciclos[idCiclo - 1].tieneAsignadoCreditoAdicional = true;
                    }
                    // Calculamos el monto con seguro financiado Agosto 2017
                    IntegranteCicloVO integrante = grupo.ciclos[idCiclo - 1].integrantes[a];
                    if (integrante.montoConSeguro == 0) {
                        integrante.montoConSeguro = integrante.montoDesembolso;
                    } 
                    // Solo si tiene un seguro contratado se muestra el costo del seguro
                    if (integrante.segContratado == SeguroConstantes.CONTRATACION_SI) {
                        // Agregamos el costo del seguro Agosto 2017
                        int idSucursal = integrante.idSucursal;
                        int tipoSeguro = integrante.tipoSeguro;
                        SucursalVO sucursalVo = new SucursalDAO().getSucursal(idSucursal);
                        double costoSeguroContratado = SeguroHelper.getCostoSeguro(tipoSeguro, sucursalVo);
                        integrante.costoSeguro = costoSeguroContratado;
                    }
                    /**
                 * JECB 25/09/2017
                 * Si el ciclo tiene adicionales asignados
                 * obtenemos la semana maxima en la que se disperso el adicional.
                 */
                if(grupo.ciclos[idCiclo - 1].tieneAsignadoCreditoAdicional){
                     CicloGrupalVO tmpCiclo =  new CicloGrupalDAO().getCiclo(grupo.idGrupo, idCiclo);
                     grupo.ciclos[idCiclo - 1].aceptaAdicional = tmpCiclo.aceptaAdicional;
                     myLogger.debug("idCiclo:" + grupo.ciclos[idCiclo - 1].idCiclo + " aceptaAdicional:"+grupo.ciclos[idCiclo - 1].aceptaAdicional);
                }
                }
                grupo.ciclos[idCiclo - 1].direccionReunion = new DireccionGenericaDAO().getDireccion(grupo.ciclos[idCiclo - 1].idDireccionReunion);
                grupo.ciclos[idCiclo - 1].direccionAlterna = new DireccionGenericaDAO().getDireccion(grupo.ciclos[idCiclo - 1].idDireccionAlterna);
                grupo.ciclos[idCiclo - 1].tablaAmortizacion = new TablaAmortizacionDAO().getElementos(grupo.idGrupo, grupo.ciclos[idCiclo - 1].idCiclo, ClientesConstants.AMORTIZACION_GRUPAL);
                grupo.ciclos[idCiclo - 1].referencia = new ReferenciaGeneralDAO().getReferencia(grupo.idGrupo, grupo.ciclos[idCiclo - 1].idCiclo, 'G');
                grupo.ciclos[idCiclo - 1].eventosDePago = new EventosPagosGrupalDAO().getEventosPagos(grupo.idGrupo, grupo.ciclos[idCiclo - 1].idCiclo);
                grupo.ciclos[idCiclo - 1].archivosAsociados = new ArchivoAsociadoDAO().getArchivosTipo(grupo.idGrupo, grupo.ciclos[idCiclo - 1].idCiclo);
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
