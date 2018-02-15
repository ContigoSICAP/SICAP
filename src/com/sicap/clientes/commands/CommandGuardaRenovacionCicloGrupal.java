package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.DireccionGenericaDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.DireccionHelper;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.ReferenciaGeneralVO;

public class CommandGuardaRenovacionCicloGrupal implements Command {

    private String siguiente;

    public CommandGuardaRenovacionCicloGrupal(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        //Notification notificaciones[] = new Notification[1];
        Vector<Notification> notificaciones = new Vector<Notification>();
        HttpSession session = request.getSession();
        GrupoVO grupo = new GrupoVO();
        CicloGrupalVO ciclo = new CicloGrupalVO();
        ReferenciaGeneralVO pagoReferenciaVO = new ReferenciaGeneralVO();
        CicloGrupalDAO ciclodao = new CicloGrupalDAO();
        DireccionGenericaDAO direcciondao = new DireccionGenericaDAO();
        IntegranteCicloDAO integrantesdao = new IntegranteCicloDAO();
        ReferenciaGeneralDAO pagoReferenciaDAO = new ReferenciaGeneralDAO();
        String validacionDesembolso = "NO";
        Date fechaUltCred = null;

        Connection conn = null;
        try {
            TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
            conn = ConnectionManager.getMySQLConnection();
            conn.setAutoCommit(false);
            grupo = (GrupoVO) session.getAttribute("GRUPO");
            BitacoraUtil bitutil = new BitacoraUtil(grupo.idGrupo, request.getRemoteUser(), "CommandGuardaRenovacionCicloGrupal");
            ciclo = GrupoHelper.getCicloGrupalVO(ciclo, request);
            ciclo.direccionReunion = DireccionHelper.getDireccionGenericaVO(ciclo.direccionReunion, request);
            GrupoHelper.getIntegrantesCicloRenovacion(request, ciclo);
            int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
//			int plazo = 16; //FIJO POR EL MOMENTO, PENDIENTE POR OBTENER DE LOS CLIENTES
            int tasa = HTMLHelper.getParameterInt(request, "idTasa");
            // validación si existe ciclo activo grupal
            if ((ciclodao.getCiclo(grupo.idGrupo)) == null) {
                //RECALCULA CALIFICACION DEL GRUPO DEPENDIENDO AL CICLO  NUMERO DE INTEGRANTES
                GrupoUtil.recalculaTasaComision(conn, ciclo, grupo);
                tasa = ciclo.tasa;
                //grupo.calificacion = GrupoUtil.AsiganaNuevaCalificacion(ciclo.tasa);
                if (!grupo.calificacion.equals("B")) {
                    //GENERACION DE SOLICITUDES Y AUTORIZACION DE SOLICITUDES PARA CLIENTES RENOVACION
                    if (SolicitudUtil.procesaRenovaciones(conn, ciclo.integrantes, tasa)) {
                        ciclo.idDireccionReunion = direcciondao.addDireccion(conn, ciclo.direccionReunion);
                        ciclo.estatus = ClientesConstants.ESTATUS_CAPTURADO;
                        ciclo.idTipoCiclo = ClientesConstants.CICLO_NATURAL;
                        ciclodao.addCicloGrupal(conn, ciclo);
                        ciclo.monto = 0.0;
                        ciclo.montoConComision = 0.0;
                        //AGREGAR INTEGRANTES
                        for (int i = 0; i < ciclo.integrantes.length; i++) {
                            ciclo.monto += ciclo.integrantes[i].monto;
                            ciclo.integrantes[i].monto = ClientesUtil.calculaMontoConComision(ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal);
                            ciclo.montoConComision += ciclo.integrantes[i].monto;
                            integrantesdao.addIntegrante(conn, idGrupo, ciclo.idCiclo, ciclo.integrantes[i]);
                        }
                        validacionDesembolso = "OK";

                        pagoReferenciaVO.numcliente = grupo.idGrupo;
                        pagoReferenciaVO.numSolicitud = ciclo.idCiclo;

                        Logger.debug("Ciclo : " + ciclo.toString());
                        String referencia = pagoReferenciaDAO.getReferencia(ciclo.idGrupo, ciclo.idCiclo, 'G');
                        if (referencia == null) {
                            pagoReferenciaVO.referencia = ClientesUtil.makeReferenciaGrupal(grupo, ciclo.idCiclo);
                            pagoReferenciaDAO.addReferencia(conn, pagoReferenciaVO);
                        } else {
                            pagoReferenciaVO.referencia = ClientesUtil.makeReferenciaGrupal(grupo, 99);
                            pagoReferenciaDAO.updateReferencia(conn, referencia, pagoReferenciaVO.referencia);
                        }

                        conn.commit();
                        System.out.println("_______________________________"+request.getRemoteUser()+" CommandGuardaRenovacionCicloGrupal"+" ciclo.idGrupo "+ciclo.idGrupo+" ciclo.idCiclo "+ciclo.idCiclo);
                        GrupoHelper.obtenTablaAmortizacion(grupo, ciclo, new Date()); //SE DEBE MODIFICAR PARA LA COMISION
                        ciclodao.updateCiclo(ciclo);
                        //notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente");
                        notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente"));
                        bitutil.registraEvento(ciclo);

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
            // Agrega ciclo recien creado al arreglo de ciclos en cesion del grupo
            grupo.ciclos = GrupoUtil.addCiclo(grupo, ciclo);
            request.setAttribute("NOTIFICACIONES", notificaciones);
            //Actualiza el objeto cliente
            request.setAttribute("VALIDACION", validacionDesembolso);
            request.setAttribute("CICLO", ciclo);
            session.setAttribute("GRUPO", grupo);

        } catch (ClientesDBException dbe) {
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
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
