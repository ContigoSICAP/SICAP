package com.sicap.clientes.commands;

import com.sicap.clientes.dao.BitacoraCicloDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.CreditoDAO;
import com.sicap.clientes.dao.DireccionGenericaDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.ScoreCirculoCreditoDAO;
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
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.BitacoraCicloVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import java.util.ArrayList;
import java.util.TreeMap;

public class CommandGuardaOtrosDatos implements Command {

    private String siguiente;

    public CommandGuardaOtrosDatos(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        //Notification notificaciones[] = new Notification[1];
        Vector<Notification> notificaciones = new Vector<Notification>();
        HttpSession session = request.getSession();
        GrupoVO grupo = new GrupoVO();
        CicloGrupalVO ciclo = new CicloGrupalVO();
        BitacoraCicloVO bitacoraVO = new BitacoraCicloVO();
        ArrayList<IntegranteCicloVO> integrantesCiclo = new ArrayList<IntegranteCicloVO>();
        CicloGrupalDAO ciclodao = new CicloGrupalDAO();
        DireccionGenericaDAO direcciondao = new DireccionGenericaDAO();
        BitacoraCicloDAO bitacoraCicloDAO = new BitacoraCicloDAO();
        Connection conn = null;

        try {
            conn = ConnectionManager.getMySQLConnection();
            conn.setAutoCommit(false);
            grupo = (GrupoVO) session.getAttribute("GRUPO");
            BitacoraUtil bitutil = new BitacoraUtil(grupo.idGrupo, request.getRemoteUser(), "CommandGuardaOtrosDatos");
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");

            CicloGrupalVO temporal = GrupoHelper.getCicloGrupalVO(ciclo, request);
            ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
            ciclo.direccionReunion = DireccionHelper.getDireccionGenericaVO(ciclo.direccionReunion, request);
            direcciondao.updateDireccion(conn, ciclo.direccionReunion);

            ciclo.horaReunion = temporal.horaReunion;
            /*
             * Se retira al asesor ya que el cambio se realizara por reasignacion cartera JBL DIC/13
             */
//			ciclo.asesor = temporal.asesor;
            ciclo.horaReunion = temporal.horaReunion;
            ciclo.diaReunion = temporal.diaReunion;
            ciclo.multaFalta = temporal.multaFalta;
            ciclo.multaRetraso = temporal.multaRetraso;
            if (temporal.estatus == ClientesConstants.CICLO_RECHAZADO && ciclo.estatus == ClientesConstants.CICLO_AUTORIZADO) {
                ciclo.estatus = temporal.estatus;
                bitacoraVO.setIdEquipo(ciclo.idGrupo);
                bitacoraVO.setIdCiclo(idCiclo);
                bitacoraVO.setEstatus(ciclo.estatus);
                bitacoraVO.setIdComentario(bitacoraCicloDAO.getNumComentario(ciclo.idGrupo, idCiclo) + 1);
                bitacoraVO.setComentario(HTMLHelper.getParameterString(request, "comentario"));
                bitacoraVO.setUsuarioComentario(request.getRemoteUser());
                bitacoraVO.setUsuarioAsignado(bitacoraCicloDAO.getUsuarioEstatus(ciclo.idGrupo, idCiclo, ClientesConstants.CICLO_ANALISIS));
                bitacoraCicloDAO.insertaBitacoraCiclo(conn, bitacoraVO);
                integrantesCiclo = new IntegranteCicloDAO().getIntegrantesApertura(ciclo.idGrupo, idCiclo);
                TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
                ScoreCirculoCreditoDAO scoreDAO = new ScoreCirculoCreditoDAO();
                for (IntegranteCicloVO integrantes : integrantesCiclo) {
                    integrantes.monto = ClientesUtil.calculaMontoSinComision(integrantes.monto, integrantes.comision, catComisiones);
                    integrantes.calificacion = ClientesConstants.CALIFICACION_CIRCULO_BUENA;
                    integrantes.comision = GrupoUtil.obtieneComisionIntegrante(integrantes, grupo);
                    integrantes.monto = ClientesUtil.calculaMontoConComision(integrantes.monto, integrantes.comision, catComisiones);
                    //CreditoVO informacionCrediticia = new CreditoDAO().getCredito(integrantes.idCliente, integrantes.idSolicitud, 2);
                    CreditoVO informacionCrediticia = new CreditoDAO().getCredito(integrantes.idCliente, integrantes.idSolicitud, 2);
                    if (informacionCrediticia != null) {
                        integrantes.calificacion = informacionCrediticia.comportamiento;
                        integrantes.calificacionAutomatica = informacionCrediticia.comportamiento;
                        if (informacionCrediticia.calificacionMesaControl != 0) {
                            integrantes.calificacion = informacionCrediticia.calificacionMesaControl;
                        }
                        integrantes.aceptaRegular = informacionCrediticia.aceptaRegular;
//                        else
//                            integrantes.calificacion = ClientesConstants.CALIFICACION_CIRCULO_NA;
                    } else {
                        if (integrantes.getIdSolicitud() == 1) {
                            integrantes.calificacion = ClientesConstants.CALIFICACION_CIRCULO_SIN_CONSULTA;
                        } else {
                            integrantes.calificacion = ClientesConstants.CALIFICACION_CIRCULO_NA;
                        }
                    }
                    integrantes.scoreFico = scoreDAO.getValorScore(integrantes.idCliente);
                    /*if(informacionCrediticia != null && informacionCrediticia.calificacionMesaControl > ClientesConstants.CALIFICACION_CIRCULO_BUENA){
                     integrantes.calificacion = informacionCrediticia.calificacionMesaControl;
                     } else {
                     conusltaCrediticia = new InformacionCrediticiaDAO().getLastInfoCrediticia(integrantes.idCliente);
                     //integrantes.calificacion = ScoringUtil.getCalificacionCirculo(conusltaCrediticia);
                     }*/
                }
                request.setAttribute("INTEGRANTES_CICLO", integrantesCiclo);
                siguiente = "/capturaCicloApertura.jsp";
            }
            //GrupoUtil.setCiclo(grupo, ciclo, idCiclo);
            ciclodao.updateCiclo(conn, ciclo);
            new SaldoIBSDAO().updateOtrosDatos(conn, ciclo.idGrupo, ciclo.idCiclo, HTMLHelper.getParameterInt(request, "fondeador"), HTMLHelper.getParameterInt(request, "numDespacho"));
            conn.commit();
            ciclo.saldo.setFondeador(HTMLHelper.getParameterInt(request, "fondeador"));
            ciclo.saldo.setNumDespacho(HTMLHelper.getParameterInt(request, "numDespacho"));

            notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente"));
            bitutil.registraEvento(ciclo);

            // Aquí cae validación si existe ciclo activo grupal
            request.setAttribute("NOTIFICACIONES", notificaciones);
            //Actualiza el objeto cliente
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
