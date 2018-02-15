package com.sicap.clientes.commands;

import com.sicap.clientes.dao.BitacoraCicloDAO;
import java.util.Date;
import java.util.TreeMap;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.DireccionGenericaDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.DireccionHelper;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.BitacoraCicloVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.ReferenciaGeneralVO;
import com.sicap.clientes.vo.SolicitudVO;
import org.apache.log4j.Logger;

public class CommandGuardaAperturaCiclo implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandGuardaAperturaCiclo.class);

    public CommandGuardaAperturaCiclo(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        Vector<Notification> notificaciones = new Vector<Notification>();
        GrupoVO grupo = new GrupoVO();
        CicloGrupalVO ciclo = new CicloGrupalVO();
        SolicitudVO solicitudes[] = null;
        ReferenciaGeneralVO pagoReferenciaVO = new ReferenciaGeneralVO();
        BitacoraCicloVO bitacoraCiclo = new BitacoraCicloVO();
        CicloGrupalDAO ciclodao = new CicloGrupalDAO();
        DireccionGenericaDAO direcciondao = new DireccionGenericaDAO();
        IntegranteCicloDAO integrantesdao = new IntegranteCicloDAO();
        ReferenciaGeneralDAO pagoReferenciaDAO = new ReferenciaGeneralDAO();
        BitacoraCicloDAO bitacoraCicloDao = new BitacoraCicloDAO();
        SolicitudDAO solicituddao = new SolicitudDAO();
        String validacionDesembolso = "NO";
        myLogger.debug("Variables inicializadas");
        try {
            TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
            int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
            myLogger.debug("Obteniendo grupo");
            grupo = new GrupoDAO().getGrupo(idGrupo);
            BitacoraUtil bitutil = new BitacoraUtil(grupo.idGrupo, request.getRemoteUser(), "CommandGuardaAperturaCiclo");
            myLogger.debug("Obteniendo ciclo");
            ciclo = GrupoHelper.getCicloGrupalApertura(ciclo, request);
            ciclo.idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            ciclo.direccionReunion = DireccionHelper.getDireccionGenericaVO(ciclo.direccionReunion, request);
            ciclo.integrantesArray = GrupoHelper.getIntegrantesCicloApertura(request);
            GrupoUtil.recalculaTasaComisionApertura(ciclo, grupo);
            if (!ciclodao.existeCiclo(ciclo.idGrupo, ciclo.idCiclo)){
                if (!grupo.calificacion.equals("B")) {
                    myLogger.debug("El equipo no tiene calificación B");
                    ciclo.idDireccionReunion = direcciondao.addDireccionApertura(ciclo.direccionReunion);
                    ciclo.idTipoCiclo = ClientesConstants.CICLO_NATURAL;
                    ciclo.estatus = HTMLHelper.getParameterInt(request, "estatus");
                    bitacoraCiclo.setIdEquipo(idGrupo);
                    bitacoraCiclo.setIdCiclo(ciclo.idCiclo);
                    bitacoraCiclo.setEstatus(ciclo.estatus);
                    bitacoraCiclo.setIdComentario(bitacoraCicloDao.getNumComentario(idGrupo, ciclo.idCiclo)+1);
                    bitacoraCiclo.setComentario(HTMLHelper.getParameterString(request, "comentario"));
                    bitacoraCiclo.setUsuarioComentario(request.getRemoteUser());
                    if(ciclo.estatus==3||ciclo.estatus==10){
                        bitacoraCiclo.setUsuarioAsignado(request.getRemoteUser());
                    }
                    else if(ciclo.estatus==7){
                        bitacoraCiclo.setUsuarioAsignado("arodriguez");
                    }
                    myLogger.info("Insertando registro de bitácora ciclo");
                    bitacoraCicloDao.insertaBitacoraCiclo(null, bitacoraCiclo);
                    myLogger.info("Insertando ciclo");
                    ciclodao.addCicloApertura(ciclo);
                    ciclo.monto = 0.0;
                    ciclo.montoConComision = 0.0;
                    for (IntegranteCicloVO integrante : ciclo.integrantesArray){
                        System.out.println("integrante "+integrante.idCliente+"_"+integrante.idSolicitud+" "+integrante.monto);
                        ciclo.monto +=integrante.monto;
                        integrante.monto = ClientesUtil.calculaMontoConComision(integrante.monto, integrante.comision, catComisionesGrupal);
                        ciclo.montoConComision += integrante.monto;
                        myLogger.info("Verifica si tiene solicitudes vencidas");
                        solicitudes = solicituddao.getSolicitudes(integrante.idCliente);
                        for (int y = 0; y < solicitudes.length; y++) {
                            if (solicitudes[y].documentacionCompleta == 1 && (solicitudes[y].desembolsado == ClientesConstants.DESEMBOLSADO)) {
                                integrante.RenovacionDoc = 0;
                                if (FechasUtil.obtenAniosDiferencia(solicitudes[y].fechaFirma, new Date()) == 1) {
                                    myLogger.info("encontro solicitud con atiguedad de un año");
                                    integrante.RenovacionDoc = 1;                                
                                }
                            }
                        }
                        if (integrante.RenovacionDoc == 0 && solicitudes[solicitudes.length - 1].documentacionCompleta > 0) {
                                integrante.RenovacionDoc = 1;
                        }
                        if(integrante.RenovacionDoc == 1&& (solicitudes[solicitudes.length-1].documentacionCompleta>0 || solicitudes.length == 1)){
                            myLogger.info("La solictud esta marcada con los documentos completos");
                            integrante.DocCompletos = 1;
                        }
                        myLogger.info("Insertando integrante");                        
                        integrantesdao.addIntegranteApertura(idGrupo, ciclo.idCiclo, integrante);
                    }
                    validacionDesembolso = "OK";
                    pagoReferenciaVO.numcliente = grupo.idGrupo;
                    pagoReferenciaVO.numSolicitud = ciclo.idCiclo;
                    myLogger.debug("Obteniendo referencia");
                    String referencia = pagoReferenciaDAO.getReferencia(ciclo.idGrupo, ciclo.idCiclo, 'G');
                    if (referencia == null) {
                        pagoReferenciaVO.referencia = ClientesUtil.makeReferenciaGrupal(grupo, ciclo.idCiclo);
                        pagoReferenciaDAO.addReferenciaApertura(pagoReferenciaVO);
                    }
                    else {
                        pagoReferenciaVO.referencia = ClientesUtil.makeReferenciaGrupal(grupo, 99);
                        pagoReferenciaDAO.updateReferenciaApertura(referencia, pagoReferenciaVO.referencia);
                    }
                    myLogger.debug("Obteniendo tabla de amortización");
                    GrupoHelper.obtenTablaAmortizacion(grupo, ciclo, new Date()); //SE DEBE MODIFICAR PARA LA COMISION
                    myLogger.info("Actualizando ciclo");
                    ciclodao.updateCicloApertura(ciclo);
                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente"));
                    myLogger.info("Insertando registro en bitácora");
                    bitutil.registraEvento(ciclo);
                    for (int i=0; i<ciclo.integrantesArray.size();i++){
                        bitutil.registraEvento(ciclo.integrantesArray.get(i));                                      
                    }
                    
                }else {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Calificacion 'B' no valida para renovacion"));
                }
            }
            else {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Este ciclo ya fue generado anteriormente"));
            }
            request.setAttribute("NOTIFICACIONES", notificaciones);
            request.setAttribute("VALIDACION", validacionDesembolso);
            request.setAttribute("INTEGRANTES_CICLO", ciclo.integrantesArray);
            request.setAttribute("recienGruadado", true);
        }
        catch (ClientesDBException dbe) {
            myLogger.error("CommandGuardaAperturaCiclo",dbe);
            throw new CommandException(dbe.getMessage());
        }
        catch (Exception e) {
            myLogger.error("CommandGuardaAperturaCiclo",e);
            throw new CommandException(e.getMessage());
        } 
        return siguiente;
    }
}