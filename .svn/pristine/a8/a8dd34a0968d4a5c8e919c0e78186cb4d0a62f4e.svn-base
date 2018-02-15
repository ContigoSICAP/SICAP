package com.sicap.clientes.commands;

import com.sicap.clientes.dao.ArchivoAsociadoDAO;
import com.sicap.clientes.dao.CreditoDAO;
import com.sicap.clientes.dao.DireccionGenericaDAO;
import com.sicap.clientes.dao.EmpleoDAO;
import com.sicap.clientes.dao.EventosPagosGrupalDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.InterCicloHelper;
import com.sicap.clientes.helpers.SeguroHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SeguroConstantes;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.SucursalVO;
import java.util.Date;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class CommandObtenInterCiclo implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandObtenInterCiclo.class);

    public CommandObtenInterCiclo(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        HttpSession session = request.getSession();
        GrupoVO grupo = new GrupoVO();
        IntegranteCicloVO[] integrantesCicloVO = null;
        IntegranteCicloVO[] integrantesIntercicloVO = null;
        SolicitudVO solicitudes[] = null;
        IntegranteCicloVO elementos[] = null;
        CreditoVO informacionCrediticia = null;
        SaldoIBSVO Saldo = new SaldoIBSVO();
        SolicitudDAO solicituddao = new SolicitudDAO();
        int semDisp = 0;
        int semDispDes = 0;
        boolean intercicloAnt = false;

        int i = 0;
        try {
            Vector<Notification> notificaciones = new Vector<Notification>();
            TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            grupo = (GrupoVO) session.getAttribute("GRUPO");
            integrantesCicloVO = new IntegranteCicloDAO().getIntegrantes(grupo.idGrupo, idCiclo);
            integrantesIntercicloVO = new IntegranteCicloDAO().getIntegrantesNuevoInterCiclo(grupo.getIdGrupo(), idCiclo);
            Saldo = new SaldoIBSDAO().getSaldos(grupo.idGrupo, idCiclo);
            semDisp = new InterCicloHelper().semanaDispersion(Saldo);
            if (integrantesIntercicloVO != null) {
                elementos = new IntegranteCicloVO[integrantesCicloVO.length + integrantesIntercicloVO.length];
                for (i = 0; i < integrantesCicloVO.length; i++) {
                    if (integrantesCicloVO[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_REGULAR) {
                        informacionCrediticia = new CreditoDAO().getCredito(integrantesCicloVO[i].idCliente, integrantesCicloVO[i].idSolicitud, 2);
                        if (informacionCrediticia != null) {
                            integrantesCicloVO[i].aceptaRegular = informacionCrediticia.aceptaRegular;
                        } else if (integrantesCicloVO[i].getIdSolicitud() == 1) {
                            integrantesCicloVO[i].calificacion = ClientesConstants.CALIFICACION_CIRCULO_SIN_CONSULTA;
                        } else {
                            integrantesCicloVO[i].calificacion = ClientesConstants.CALIFICACION_CIRCULO_NA;
                        }
                    }else if (integrantesCicloVO[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_BUENA) {
                        informacionCrediticia = new CreditoDAO().getCredito(integrantesCicloVO[i].idCliente, integrantesCicloVO[i].idSolicitud, 2);
                        if (informacionCrediticia != null) {
                            integrantesCicloVO[i].aceptaRegular = informacionCrediticia.aceptaRegular;
                        }
                    }
                    elementos[i] = (IntegranteCicloVO) integrantesCicloVO[i];
                }
                for (int j = 0; j < integrantesIntercicloVO.length; j++) {
                    myLogger.debug(integrantesIntercicloVO[j].toString());
                    informacionCrediticia = new CreditoDAO().getCredito(integrantesIntercicloVO[j].idCliente, integrantesIntercicloVO[j].idSolicitud, 2);
                    if (informacionCrediticia != null) {
                        integrantesIntercicloVO[j].calificacion = informacionCrediticia.comportamiento;
                        integrantesIntercicloVO[j].calificacionAutomatica = informacionCrediticia.comportamiento;
                        if (informacionCrediticia.calificacionMesaControl != 0) {
                            integrantesIntercicloVO[j].calificacion = informacionCrediticia.calificacionMesaControl;
                        }
                        integrantesIntercicloVO[j].aceptaRegular = informacionCrediticia.aceptaRegular;
                    } else if (integrantesIntercicloVO[j].getIdSolicitud() == 1) {
                        integrantesIntercicloVO[j].calificacion = ClientesConstants.CALIFICACION_CIRCULO_SIN_CONSULTA;
                    } else {
                        integrantesIntercicloVO[j].calificacion = ClientesConstants.CALIFICACION_CIRCULO_NA;
                    }
                    if (grupo.ciclos[idCiclo - 1].getEstatusIC() == ClientesConstants.CICLO_AUTORIZADO) {
                        integrantesIntercicloVO[j].seguro = new SegurosDAO().compruebaSeguro(integrantesIntercicloVO[j]);
                        integrantesIntercicloVO[j].empleo = new EmpleoDAO().compruebaEmpleo(integrantesIntercicloVO[j]);
                        integrantesIntercicloVO[j].grupo = new IntegranteCicloDAO().getIntegranteActivo(integrantesIntercicloVO[j].idCliente);
                    } else if (grupo.ciclos[idCiclo - 1].getEstatusIC() == ClientesConstants.CICLO_DESEMBOLSADO) {
                        grupo.ciclos[idCiclo - 1].tablaAmortInterciclo = new TablaAmortizacionDAO().getElementos(grupo.idGrupo, idCiclo, ClientesConstants.AMORTIZACION_INTERCICLO);
                    }
                    solicitudes = solicituddao.getSolicitudes(integrantesIntercicloVO[j].idCliente);
                    for (int y = 0; y < solicitudes.length; y++) {
                        if (solicitudes[y].documentacionCompleta == 1 && (solicitudes[y].desembolsado == ClientesConstants.DESEMBOLSADO)) {
                            integrantesIntercicloVO[j].RenovacionDoc = 0;
                            if (FechasUtil.obtenAniosDiferencia(solicitudes[y].fechaFirma, new Date()) == 1) {
                                integrantesIntercicloVO[j].RenovacionDoc = 1;
                            }
                        }
                    }
                    if (integrantesIntercicloVO[j].RenovacionDoc == 0 && (solicitudes[solicitudes.length - 1].documentacionCompleta > 0 || solicitudes.length == 1)) {
                        integrantesIntercicloVO[j].RenovacionDoc = 1;
                    }                    
                    if (integrantesIntercicloVO[j].RenovacionDoc == 1 && solicitudes[solicitudes.length - 1].documentacionCompleta > 0) {
                        integrantesIntercicloVO[j].DocCompletos = 1;
                    }
                    elementos[i + j] = (IntegranteCicloVO) integrantesIntercicloVO[j];
                }
                grupo.ciclos[idCiclo - 1].integrantes = elementos;
                for (int a = 0; grupo.ciclos[idCiclo - 1].integrantes != null && a < grupo.ciclos[idCiclo - 1].integrantes.length; a++) {
                    grupo.ciclos[idCiclo - 1].integrantes[a].montoDesembolso = ClientesUtil.calculaMontoSinComision(grupo.ciclos[idCiclo - 1].integrantes[a].monto, grupo.ciclos[idCiclo - 1].integrantes[a].comision, catComisionesGrupal);
                }
            } else {
                grupo.ciclos[idCiclo - 1].integrantes = integrantesCicloVO;
            }
            if (integrantesIntercicloVO != null) {
                semDispDes = new InterCicloHelper().semanaDispersionDesembolso(Saldo);
                for (IntegranteCicloVO integrantesIntercicloVO1 : integrantesIntercicloVO) {
                    if (integrantesIntercicloVO1.getSemDisp() != semDispDes) {
                        intercicloAnt = true;
                    }
                }
            }
            if (intercicloAnt) {
                if (semDisp == ClientesConstants.DISPERSION_SEMANA_4 && grupo.ciclos[idCiclo - 1].estatusIC != ClientesConstants.CICLO_DISPERSADO) {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "El Interciclo cuenta con solicitudes anteriores no dispersadas para volverlas a ingresar es necesario guardar la solcitud de nuevo"));
                }

            }
            // Calculamos de monto con seguro financiado para Interciclo Agosto 2017
            int integrantesLenght = grupo.ciclos[idCiclo - 1].integrantes.length; // evitas un calculo en el for
            for (int k = 0; k < integrantesLenght; k++) {
                // Calculamos el monto con seguro financiado Agosto 2017
                IntegranteCicloVO integrante = grupo.ciclos[idCiclo - 1].integrantes[k];
                if (integrante.montoConSeguro == 0) {
                    integrante.montoConSeguro = integrante.monto;
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
            }
            grupo.ciclos[idCiclo - 1].direccionReunion = new DireccionGenericaDAO().getDireccion(grupo.ciclos[idCiclo - 1].idDireccionReunion);
            grupo.ciclos[idCiclo - 1].direccionAlterna = new DireccionGenericaDAO().getDireccion(grupo.ciclos[idCiclo - 1].idDireccionAlterna);
            grupo.ciclos[idCiclo - 1].tablaAmortizacion = new TablaAmortizacionDAO().getElementos(grupo.idGrupo, grupo.ciclos[idCiclo - 1].idCiclo, ClientesConstants.AMORTIZACION_GRUPAL);
            grupo.ciclos[idCiclo - 1].referencia = new ReferenciaGeneralDAO().getReferencia(grupo.idGrupo, grupo.ciclos[idCiclo - 1].idCiclo, 'G');
            grupo.ciclos[idCiclo - 1].eventosDePago = new EventosPagosGrupalDAO().getEventosPagos(grupo.idGrupo, grupo.ciclos[idCiclo - 1].idCiclo);
            grupo.ciclos[idCiclo - 1].archivosAsociados = new ArchivoAsociadoDAO().getArchivosTipo(grupo.idGrupo, grupo.ciclos[idCiclo - 1].idCiclo);

            session.setAttribute("GRUPO", grupo);
            request.setAttribute("semanaDisp", semDisp);
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
