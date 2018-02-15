package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.DecisionComiteDAO;
import com.sicap.clientes.dao.DireccionGenericaDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.DireccionHelper;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.SeguroHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.FondeadorUtil;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SeguroConstantes;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.ReferenciaGeneralVO;
import com.sicap.clientes.vo.SucursalVO;
import java.util.Arrays;
import org.apache.log4j.Logger;

public class CommandGuardaCicloGrupal implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandGuardaCicloGrupal.class);

    public CommandGuardaCicloGrupal(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        //Notification notificaciones[] = new Notification[1];
        Vector<Notification> notificaciones = new Vector<Notification>();
        HttpSession session = request.getSession();
        GrupoVO grupo = new GrupoVO();
        CicloGrupalVO cicloAnterior = new CicloGrupalVO();
        CicloGrupalVO ciclo = new CicloGrupalVO();
        ReferenciaGeneralVO pagoReferenciaVO = new ReferenciaGeneralVO();
        CicloGrupalDAO ciclodao = new CicloGrupalDAO();
        DireccionGenericaDAO direcciondao = new DireccionGenericaDAO();
        IntegranteCicloDAO integrantesdao = new IntegranteCicloDAO();
        ReferenciaGeneralDAO pagoReferenciaDAO = new ReferenciaGeneralDAO();
        SaldoIBSDAO saldosDAO = new SaldoIBSDAO();
        Connection conn = null;
        String validacionDesembolso = "NO";
        Calendar cal = Calendar.getInstance();
        try {
            TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
            conn = ConnectionManager.getMySQLConnection();
            conn.setAutoCommit(false);
            grupo = (GrupoVO) session.getAttribute("GRUPO");
            // Para el monto con seguro necesitamos comparar el monto previo del integrante
            IntegranteCicloVO[] integrantesBckup = null;
            BitacoraUtil bitutil = new BitacoraUtil(grupo.idGrupo, request.getRemoteUser(), "CommandGuardaCicloGrupal");
            int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            if (grupo.ciclos != null) {
                cicloAnterior = grupo.ciclos[idCiclo - 1];
                integrantesBckup = Arrays.copyOf(cicloAnterior.integrantes, cicloAnterior.integrantes.length);
            }
            
            if (idCiclo == 0) {
                ciclo = GrupoHelper.getCicloGrupalVO(ciclo, request);
                ciclo.direccionReunion = DireccionHelper.getDireccionGenericaVO(ciclo.direccionReunion, request);
                ciclo = GrupoHelper.getIntegrantesCicloRenovacion(request, ciclo);
                //ciclo.plazo = 16;
                ciclo.tasa = HTMLHelper.getParameterInt(request, "idTasa");
                // validación si existe ciclo activo grupal
                if (ciclodao.getCiclo(grupo.idGrupo) == null) {
                    if (!grupo.calificacion.equals("B")) {
                        if (SolicitudUtil.procesaRenovaciones(conn, ciclo.integrantes, ciclo.tasa)) {
                            ciclo.idDireccionReunion = direcciondao.addDireccion(conn, ciclo.direccionReunion);
                            ciclo.estatus = ClientesConstants.ESTATUS_CAPTURADO;
                            ciclo.monto = 0.0;
                            ciclo.montoConComision = 0.0;
                            ciclo.idTipoCiclo = ClientesConstants.CICLO_NATURAL;
                            ciclodao.addCicloGrupal(conn, ciclo);
                            // Calculamos montos nuevos, montos con seguro en caso de que tenga Agosto 2017
                            for (int i = 0; i < ciclo.integrantes.length; i++) {
                                // Cuenta con monto con seguro financiado y el monto cambio?
                                if (integrantesBckup[i].montoConSeguroTemp > 0) {
                                    ciclo.integrantes[i].montoConSeguro = ciclo.integrantes[i].monto + integrantesBckup[i].costoSeguro;
                                    // Si cambiaron los montos valor anterior valor actual realizamos update
                                    if (integrantesBckup[i].monto != ciclo.integrantes[i].monto) {
                                        // Actualizamos los montos con seguro en las tablas d_integrantes_ciclo y d_decision_comite
                                        int idCliente = ciclo.integrantes[i].idCliente;
                                        int idSolicitud = ciclo.integrantes[i].idSolicitud;
                                        IntegranteCicloDAO integranteCicloDAO = new IntegranteCicloDAO();
                                        integranteCicloDAO.updateMontoConSeguro(conn, ciclo.integrantes[i].montoConSeguro, idCliente, idSolicitud, idCiclo);
                                        DecisionComiteDAO decisionComiteDAO = new DecisionComiteDAO();
                                        decisionComiteDAO.updateMontoConSeguro(conn, ciclo.integrantes[i].montoConSeguro, idCliente, idSolicitud);
                                    }
                                } else {
                                    // No cuenta con seguro por eso solo se le asignamos el monto
                                    ciclo.integrantes[i].montoConSeguro = ciclo.integrantes[i].monto;
                                }
                                // Solo si tiene un seguro contratado se muestra el costo del seguro
                                if (integrantesBckup[i].segContratado == SeguroConstantes.CONTRATACION_SI) {
                                    // Agregamos el costo del seguro Agosto 2017
                                    int idSucursal = integrantesBckup[i].idSucursal;
                                    int tipoSeguro = integrantesBckup[i].tipoSeguro;
                                    SucursalVO sucursalVo = new SucursalDAO().getSucursal(idSucursal);
                                    double costoSeguroContratado = SeguroHelper.getCostoSeguro(tipoSeguro, sucursalVo);
                                    ciclo.integrantes[i].costoSeguro = costoSeguroContratado;
                                    // como esta parte esta en request mantenemos los datos
                                    ciclo.integrantes[i].segContratado = integrantesBckup[i].segContratado;
                                    ciclo.integrantes[i].idSucursal = integrantesBckup[i].idSucursal;     
                                    ciclo.integrantes[i].tipoSeguro = integrantesBckup[i].tipoSeguro;
                                }
                            }
                            for (int i = 0; i < ciclo.integrantes.length; i++) {
                                ciclo.monto += ciclo.integrantes[i].monto;
                                ciclo.integrantes[i].monto = ClientesUtil.calculaMontoConComision(ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal);
                                ciclo.montoConComision += ciclo.integrantes[i].montoConSeguro;
                                integrantesdao.addIntegrante(conn, idGrupo, ciclo.idCiclo, ciclo.integrantes[i]);
                            }

                            validacionDesembolso = "OK";
                            pagoReferenciaVO.numcliente = grupo.idGrupo;
                            pagoReferenciaVO.numSolicitud = ciclo.idCiclo;
                            String referencia = pagoReferenciaDAO.getReferencia(ciclo.idGrupo, ciclo.idCiclo, 'G');
                            if (referencia == null) {
                                pagoReferenciaVO.referencia = ClientesUtil.makeReferenciaGrupal(grupo, ciclo.idCiclo);
                                pagoReferenciaDAO.addReferencia(conn, pagoReferenciaVO);
                            } else {
                                pagoReferenciaVO.referencia = ClientesUtil.makeReferenciaGrupal(grupo, 99);
                                pagoReferenciaDAO.updateReferencia(conn, referencia, pagoReferenciaVO.referencia);
                            }
                            conn.commit();
                            myLogger.debug("Usuario : " + request.getRemoteUser() + ", ciclo.idGrupo: " + ciclo.idGrupo + ", ciclo.idCiclo: " + ciclo.idCiclo);
                            GrupoHelper.obtenTablaAmortizacion(grupo, ciclo, cal.getTime());
                            ciclodao.updateCiclo(ciclo);
                            //notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente");
                            notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente"));
                            bitutil.registraEvento(ciclo);
                            for(int i = 0; i < ciclo.integrantes.length; i++){
                                bitutil.registraEvento(ciclo.integrantes[i]);
                            }
                            //Agrega ciclo recien creado al arreglo de ciclos en cesion del grupo
                            //grupo.ciclos = GrupoUtil.addCiclo(grupo, ciclo);
                        } else {
                            //notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "El monto a desembolsar no es valido");
                            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "El monto a desembolsar no es valido"));
                        }
                    } else {
                        //notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Calificacion 'B' no valida para renovacion");
                        notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Calificacion 'B' no valida para renovacion"));
                    }
                } else {
                    //notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Ya existe un ciclo activo para el grupo");
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Ya existe un ciclo activo para el grupo"));
                }
                bitutil.registraEvento(ciclo);
                for(int i = 0; i < ciclo.integrantes.length; i++){
                    bitutil.registraEvento(ciclo.integrantes[i]);
                }
            } else {
                int cicloAntLiq = saldosDAO.cicloAntLiquidado(idGrupo);
                if (cicloAntLiq == 0) {
                    ciclo = GrupoHelper.getCicloGrupalVO(cicloAnterior, request);
                    if (ciclo.estatus != 2) {
                        if (!grupo.calificacion.equals("B")) {
                            ciclo.direccionReunion = DireccionHelper.getDireccionGenericaVO(ciclo.direccionReunion, request);
                            direcciondao.updateDireccion(conn, ciclo.direccionReunion);
                            ciclo.integrantes = GrupoHelper.getIntegrantesCiclo(request);
                            //ciclo.plazo = 16;
                            ciclo.tasa = HTMLHelper.getParameterInt(request, "idTasa");
                            //ciclo.integrantes = integrantesdao.getIntegrantes(idGrupo, idCiclo);
                            for (int i = 0; i < ciclo.integrantes.length; i++) {
                                String nombre = "desembolso" + i;
                                if (!GrupoHelper.existeIntegrante(ciclo, ciclo.integrantes[i])) {
                                    if (HTMLHelper.getCheckBox(request, nombre)) {
                                        integrantesdao.addIntegrante(conn, idGrupo, ciclo.idCiclo, ciclo.integrantes[i]);
                                        myLogger.debug("Se agrega al grupo el integrante: "+ciclo.integrantes[i].idCliente+": "+ciclo.integrantes[i].nombre );
                                    }
                                } else {
                                    if (!HTMLHelper.getCheckBox(request, nombre)) {
                                        myLogger.debug("Se Elimina del grupo el integrante: "+ciclo.integrantes[i].idCliente+": "+ciclo.integrantes[i].nombre );
                                        integrantesdao.deleteIntegrante(idGrupo, idCiclo, ciclo.integrantes[i].idCliente);
                                        integrantesdao.deleteIntegranteODS(idGrupo, idCiclo, ciclo.integrantes[i].idCliente);
                                    }
                                }
                            }
                            ciclo = GrupoHelper.getIntegrantesCicloRenovacion(request, ciclo);
                            SolicitudUtil.procesaRenovaciones(conn, ciclo.integrantes, ciclo.tasa);
                            // Calculamos montos nuevos, montos con seguro en caso de que tenga Agosto 2017
                            for (int i = 0; i < ciclo.integrantes.length; i++) {
                                // Cuenta con monto con seguro financiado y el monto cambio?
                                if (integrantesBckup[i].montoConSeguroTemp > 0) {
                                    ciclo.integrantes[i].montoConSeguro = ciclo.integrantes[i].monto + integrantesBckup[i].costoSeguro;
                                    ciclo.integrantes[i].montoConSeguroTemp = ciclo.integrantes[i].montoConSeguro;
                                    // Si cambiaron los montos valor anterior valor actual realizamos update
                                    if (integrantesBckup[i].monto != ciclo.integrantes[i].monto) {
                                        // Actualizamos los montos con seguro en las tablas d_integrantes_ciclo y d_decision_comite
                                        int idCliente = ciclo.integrantes[i].idCliente;
                                        int idSolicitud = ciclo.integrantes[i].idSolicitud;
                                        IntegranteCicloDAO integranteCicloDAO = new IntegranteCicloDAO();
                                        integranteCicloDAO.updateMontoConSeguro(conn, ciclo.integrantes[i].montoConSeguro, idCliente, idSolicitud, idCiclo);
                                        DecisionComiteDAO decisionComiteDAO = new DecisionComiteDAO();
                                        decisionComiteDAO.updateMontoConSeguro(conn, ciclo.integrantes[i].montoConSeguro, idCliente, idSolicitud);
                                    }
                                } else {
                                    // No cuenta con seguro por eso solo se le asignamos el monto
                                    ciclo.integrantes[i].montoConSeguro = ciclo.integrantes[i].monto;
                                }
                                // Solo si tiene un seguro contratado se muestra el costo del seguro
                                if (integrantesBckup[i].segContratado == SeguroConstantes.CONTRATACION_SI) {
                                    // Agregamos el costo del seguro Agosto 2017
                                    int idSucursal = integrantesBckup[i].idSucursal;
                                    int tipoSeguro = integrantesBckup[i].tipoSeguro;
                                    SucursalVO sucursalVo = new SucursalDAO().getSucursal(idSucursal);
                                    double costoSeguroContratado = SeguroHelper.getCostoSeguro(tipoSeguro, sucursalVo);
                                    ciclo.integrantes[i].costoSeguro = costoSeguroContratado;
                                    // como esta parte esta en request mantenemos los datos
                                    ciclo.integrantes[i].segContratado = integrantesBckup[i].segContratado;
                                    ciclo.integrantes[i].idSucursal = integrantesBckup[i].idSucursal;     
                                    ciclo.integrantes[i].tipoSeguro = integrantesBckup[i].tipoSeguro;
                                }
                            }
                            
                            ciclo.monto = 0.0;
                            ciclo.montoConComision = 0.0;
                            for (int i = 0; i < ciclo.integrantes.length; i++) {
                                ciclo.monto += ciclo.integrantes[i].monto;
                                ciclo.integrantes[i].monto = ClientesUtil.calculaMontoConComision(ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal);
                                ciclo.montoConComision += ciclo.integrantes[i].montoConSeguro;
                            }
                            conn.commit();
                            /*ASIGNACION DE FONDEADOR AUTOMATICO
                            ciclo.fondeador = FondeadorUtil.asignaFondeador(ciclo);*/
                            ciclodao.updateCiclo(ciclo);
                            //Nuevo Arreglo con lista actualizada ADD o DELETE

                            if (ciclo.estatus == ClientesConstants.CICLO_AUTORIZADO) {
                                myLogger.debug("Usuario: " + request.getRemoteUser() + ", ciclo.idGrupo: " + ciclo.idGrupo + ", ciclo.idCiclo " + ciclo.idCiclo);
                                GrupoHelper.obtenTablaAmortizacion(grupo, ciclo, cal.getTime());
                            }
                            validacionDesembolso = "OK";
                            GrupoUtil.setCiclo(grupo, ciclo, idCiclo);
                            //notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron actualizados correctamente");
                            notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron actualizados correctamente"));

                        } else {
                            //notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Calificacion 'B' no valida para renovacion");
                            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Calificacion 'B' no valida para renovacion"));
                        }
                    } else {
                        if (ciclo.estatusT24 == 3 || request.isUserInRole("ANALISIS_CREDITO")) {
                            ciclo.estatus = 2;
                            ciclodao.updateCiclo(ciclo);
                            GrupoUtil.setCiclo(grupo, ciclo, idCiclo);
                            //notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron actualizados correctamente");
                            notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron actualizados correctamente"));
                        } else {
                            //notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Ciclo no liquidado no es posible cerrarlo.");
                            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Ciclo no liquidado no es posible cerrarlo."));
                        }
                    }
                    bitutil.registraEvento(ciclo);
                    for(int i = 0; i < ciclo.integrantes.length; i++){
                        bitutil.registraEvento(ciclo.integrantes[i]);
                    }
                } else {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Hay ciclo pendientes por liquidar."));
                }

            }

            // Aquí cae validación si existe ciclo activo grupal
            request.setAttribute("NOTIFICACIONES", notificaciones);
            //Actualiza el objeto cliente
            request.setAttribute("VALIDACION", validacionDesembolso);
            request.setAttribute("CICLO", ciclo);
            session.setAttribute("GRUPO", grupo);

        } catch (ClientesDBException dbe) {
            myLogger.error("ClientesDBException", dbe);
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new CommandException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                throw new CommandException(sqle.getMessage());
            }
        }
        return siguiente;
    }
}
