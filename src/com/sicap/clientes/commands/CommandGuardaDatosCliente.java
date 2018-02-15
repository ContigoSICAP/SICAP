package com.sicap.clientes.commands;

import com.sicap.clientes.dao.ArchivoAsociadoDAO;
import com.sicap.clientes.dao.BitacoraCicloDAO;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.ClienteIntercicloDAO;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.TelefonoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.ClienteHelper;
import com.sicap.clientes.helpers.DireccionHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.InterCicloHelper;
import com.sicap.clientes.helpers.SolicitudHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.CURPUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.RFCUtil;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.BitacoraCicloVO;
import com.sicap.clientes.vo.BusquedaClientesVO;
import com.sicap.clientes.vo.CatalogoVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteIntercicloVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.DireccionVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.TelefonoVO;
import java.util.Date;
import java.util.logging.Level;
import org.apache.log4j.Logger;

public class CommandGuardaDatosCliente implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandGuardaDatosCliente.class);

    public CommandGuardaDatosCliente(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        Notification notificaciones[] = new Notification[1];
        HttpSession session = request.getSession();
        ClienteVO clienteActualizado = new ClienteVO();
        SolicitudVO solicitud = new SolicitudVO();
        DireccionVO direccion = new DireccionVO();
        DireccionVO direccionAnterior = new DireccionVO();
        TelefonoVO telefonoPrincipal = new TelefonoVO();
        TelefonoVO telefonoRecados = new TelefonoVO();
        TelefonoVO telefonoCelular = new TelefonoVO();
        CicloGrupalVO cicloGrupalVO = new CicloGrupalVO();
        SaldoIBSVO saldoVO = new SaldoIBSVO();
        BitacoraCicloVO bitacoraCicloVO = new BitacoraCicloVO();
        ClienteIntercicloVO clienteICVO = null;
        ClienteIntercicloVO anteriorClienteICVO = new ClienteIntercicloVO();
        ClienteDAO clienteDAO = new ClienteDAO();
        SolicitudDAO solicitudDAO = new SolicitudDAO();
        DireccionDAO direccionDAO = new DireccionDAO();
        TelefonoDAO telefonoDAO = new TelefonoDAO();
        CicloGrupalDAO cicloGrupalDAO = new CicloGrupalDAO();
        BitacoraCicloDAO bitacoraCicloDAO = new BitacoraCicloDAO();
        ClienteIntercicloDAO clienteICDAO = new ClienteIntercicloDAO();
        CatalogoDAO catalogoDAO = new CatalogoDAO();
        Connection conn = null;
        int evalEstatus = 0;
        int evalSemDispe = 0;
        int semanaDisp = 0;
        int autorizacionRFC = 0;
        int resp = 0;
        String notificacionIC = "";
        String fechaNac = "";
        boolean ValidaInterciclo = true;
        String CURP = "";
        int clienteVerfId = 0;
        ClienteVO clienteDuplicado = null;
        String clienteVerfSuc = "";
        SucursalDAO sucursalDAO = null;
        String respuestaWS = "";

        try {
            conn = ConnectionManager.getMySQLConnection();
            conn.setAutoCommit(false);

            int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
            ClienteVO clienteActual = (ClienteVO) session.getAttribute("CLIENTE");
            String cambioDocCompleta = HTMLHelper.getParameterString(request, "DocCompleta");
            
            fechaNac = Convertidor.dateToString(clienteActual.fechaNacimiento);
            int indiceSolicitud = SolicitudUtil.getIndice(clienteActual.solicitudes, idSolicitud);
//			Adiciona la informacion capturada al cliente
            clienteActualizado = ClienteHelper.getVO(clienteActual, request);
            autorizacionRFC = HTMLHelper.getParameterInt(request, "autorizacionRFC");
            if (autorizacionRFC == 1) {
                fechaNac = Convertidor.dateToString(clienteActualizado.fechaNacimiento, ClientesConstants.FORMATO_FECHA_EU);
                fechaNac = fechaNac.replace("-", "");
                if (clienteActualizado.aPaterno.equals("")) {
                    clienteActualizado.aPaterno = "X";
                }
                if (clienteActualizado.aMaterno.equals("")) {
                    clienteActualizado.aMaterno = "X";
                }
                clienteActualizado.rfc = RFCUtil.obtenRFC(clienteActualizado.nombre, clienteActualizado.aPaterno, clienteActualizado.aMaterno, fechaNac);
                ClienteVO clienteVerf = clienteDAO.getClienteRFC(clienteActualizado.rfc.substring(0, 10));
                if (clienteVerf == null
                        || ClientesUtil.comparaDuplicadadRFC(new ClienteVO(0, clienteActualizado.nombre, clienteActualizado.aPaterno, clienteActualizado.aMaterno, clienteActualizado.rfc, clienteActualizado.fechaNacimiento))
                        || (clienteVerf != null && clienteActualizado.idCliente == clienteVerf.idCliente)) {
                    autorizacionRFC = 0;
                } else {
                    autorizacionRFC = 1;
                    clienteDuplicado = clienteDAO.getClienteRFCCompleto(clienteActualizado.getRfc());
                    clienteVerfId = clienteDuplicado.idCliente;
                    sucursalDAO = new SucursalDAO();
                    clienteVerfSuc = sucursalDAO.getSucursalNombre(clienteDuplicado.idSucursal);
                }
            }
//			Persiste los cambios en base de datos
            if (clienteActualizado.aPaterno.equals("X")) {
                clienteActualizado.aPaterno = "";
            }
            if (clienteActualizado.aMaterno.equals("X")) {
                clienteActualizado.aMaterno = "";
            }

            clienteActualizado.nombreCompleto = clienteActualizado.nombre + " " + clienteActualizado.aPaterno + " " + clienteActualizado.aMaterno;
            //if(clientedao.getClienteRFC(clienteActualizado.rfc) != null && clienteActual.rfc == clienteActualizado.rfc){
            respuestaWS = ClientesUtil.comparaDuplicadadJUCAVI(clienteActualizado);
            respuestaWS = "";
            if (respuestaWS.equals("")) {
                if (autorizacionRFC == 0) {
                    if (!clienteActualizado.getCurp().equals("")) {
                        CURP = CURPUtil.generaCURP(clienteActualizado.getNombre(), clienteActualizado.getaPaterno(), clienteActualizado.getaMaterno(), clienteActualizado.getFechaNacimiento(), clienteActualizado.getEntidadNacimiento(), clienteActualizado.getSexo());
                    }
                    myLogger.debug("CURP Cliente " + clienteActualizado.idCliente + " " + CURP);
                    if (clienteActualizado.getCurp().substring(0, 16).equals(CURP) && clienteActualizado.getCurp().substring(16, 18).matches("[0-9]*")) {
                        clienteDAO.updateCliente(conn, clienteActualizado);
                        BitacoraUtil bitutil = new BitacoraUtil(clienteActualizado.idCliente, request.getRemoteUser(), "CommandGuardaDatosCliente");

                        if (idSolicitud == 0) {
                            //Nueva solicitud
                            solicitud = SolicitudHelper.getVO(new SolicitudVO(), request);
                            direccion = DireccionHelper.getVO(new DireccionVO(), request);

                            if (solicitud.subproducto == ClientesConstants.ID_INTERCICLO) {
                                saldoVO = InterCicloHelper.validaSolicitudIC(solicitud, clienteActual);
                                if (saldoVO.getIdSolicitudSICAP() > 0) {
                                    if (InterCicloHelper.validaPorcentajeClientesIC(saldoVO)) {
                                        semanaDisp = InterCicloHelper.semanaDispersion(saldoVO);
                                        if (semanaDisp != 0) {
                                            cicloGrupalVO = cicloGrupalDAO.getCiclo(saldoVO.getIdClienteSICAP(), saldoVO.getIdSolicitudSICAP());
                                            if ((semanaDisp == ClientesConstants.DISPERSION_SEMANA_2 && cicloGrupalVO.estatusIC == 0) || (semanaDisp == ClientesConstants.DISPERSION_SEMANA_4 && cicloGrupalVO.estatusIC2 == 0)) {
                                                bitacoraCicloVO.setIdEquipo(saldoVO.getIdClienteSICAP());
                                                bitacoraCicloVO.setIdCiclo(saldoVO.getIdSolicitudSICAP());
                                                bitacoraCicloVO.setEstatus(ClientesConstants.CICLO_APERTURA);
                                                bitacoraCicloVO.setIdComentario(bitacoraCicloDAO.getNumComentario(saldoVO.getIdClienteSICAP(), saldoVO.getIdSolicitudSICAP()) + 1);
                                                bitacoraCicloVO.setComentario("Inicia apertura de Inter-Ciclo");
                                                bitacoraCicloVO.setUsuarioComentario(request.getRemoteUser());
                                                bitacoraCicloVO.setUsuarioAsignado(request.getRemoteUser());
                                                bitacoraCicloVO.setSemDisp(semanaDisp);
                                                bitacoraCicloDAO.insertaBitacoraCiclo(conn, bitacoraCicloVO);
                                                if (semanaDisp == ClientesConstants.DISPERSION_SEMANA_2) {
                                                    cicloGrupalVO.estatusIC = ClientesConstants.CICLO_APERTURA;
                                                }
                                                if (semanaDisp == ClientesConstants.DISPERSION_SEMANA_4) {
                                                    cicloGrupalVO.estatusIC2 = ClientesConstants.CICLO_APERTURA;
                                                }
                                                cicloGrupalDAO.updateEstatusCicloIC(conn, cicloGrupalVO.idGrupo, cicloGrupalVO.idCiclo, ClientesConstants.CICLO_APERTURA, semanaDisp);
                                            }
                                            if ((semanaDisp == ClientesConstants.DISPERSION_SEMANA_2 && (cicloGrupalVO.estatusIC == ClientesConstants.CICLO_APERTURA || cicloGrupalVO.estatusIC == ClientesConstants.CICLO_RECHAZADO))
                                                    || (semanaDisp == ClientesConstants.DISPERSION_SEMANA_4 && (cicloGrupalVO.estatusIC == ClientesConstants.CICLO_APERTURA || cicloGrupalVO.estatusIC == ClientesConstants.CICLO_RECHAZADO))) {
                                                clienteICVO = new ClienteIntercicloVO();
                                                clienteICVO.setNumGrupo(saldoVO.getIdClienteSICAP());
                                                clienteICVO.setNumCiclo(saldoVO.getIdSolicitudSICAP());
                                                clienteICVO.setNumCliente(solicitud.idCliente);
                                                clienteICVO.setNumSolicitud(solicitud.idSolicitud);
                                                clienteICVO.setSemDispercion(semanaDisp);
                                                clienteICVO.setEstatus(1);
                                                clienteICDAO.addIntegranteInterciclo(conn, clienteICVO);
                                            }

                                        } else {
                                            notificacionIC = "El equipo no esta en una semana valida para Inter-Ciclo";
                                            ValidaInterciclo = false;
                                        }
                                    } else {
                                        notificacionIC = "El con este Cliente se supera la cantidad de clientes de interciclo permitidos para un Equipo";
                                        ValidaInterciclo = false;
                                    }
                                } else {
                                    notificacionIC = "El equipo o el cliente no cumplen con las caracteristicas para Inter-Ciclo";
                                    ValidaInterciclo = false;
                                }
                            }
                            if (!ValidaInterciclo) {
                                solicitud.subproducto = 0;
                            }
                            telefonoPrincipal = ClienteHelper.getTelefonoVO(telefonoPrincipal, ClientesConstants.TELEFONO_PRINCIPAL, request);
                            telefonoRecados = ClienteHelper.getTelefonoVO(telefonoRecados, ClientesConstants.TELEFONO_RECADOS, request);
                            telefonoCelular = ClienteHelper.getTelefonoVO(telefonoCelular, ClientesConstants.TELEFONO_CELULAR, request);

                            //TCC
                            direccionAnterior = DireccionHelper.getVODireccionAnterior(new DireccionVO(), request);
                            if(indiceSolicitud == (clienteActual.solicitudes.length - 1))
                            {
                               //Se heredan datos
                                if(clienteActual.solicitudes.length > 1)
                                {
                                    //clienteActualizado.nombreCompleto = clienteActualizado.nombre + " " + clienteActualizado.aPaterno + " " + clienteActualizado.aMaterno;                                    
                                    
                                    String nombreAnterior = HTMLHelper.getParameterString(request, "nombreAnterior");
                                    String aPaternoAnterior = HTMLHelper.getParameterString(request, "aPaternoAnterior");
                                    String aMaternoAnterior = HTMLHelper.getParameterString(request, "aMaternoAnterior");
                                    String fechaNacimientoAnterior = HTMLHelper.getParameterString(request, "fechaNacimientoAnterior");
                                    int entidadAnterior = HTMLHelper.getParameterInt(request, "entidadNacimientoAnterior");
                                    int sexoAnterior = HTMLHelper.getParameterInt(request, "sexoAnterior");
                                    //if(SolicitudUtil.HayDiferenciaEntreDirecciones(direccion, direccionAnterior) || HayDiferenciaEntreNombresYFN(solicitud, solicitudAnterior))
                                   // String nombreAnteior = request.getParameter("")
                                    if(SolicitudUtil.HayDiferenciaEntreDirecciones(direccion, direccionAnterior) || SolicitudUtil.HayDiferenciaEntreNombresYFN(clienteActualizado, nombreAnterior, aPaternoAnterior, aMaternoAnterior, fechaNacimientoAnterior, entidadAnterior, sexoAnterior))

                                    {
                                        
                                        if(!request.isUserInRole("ANALISIS_CREDITO")){
                                            CicloGrupalVO cgEst = cicloGrupalDAO.getCiclo(clienteActualizado.solicitudes[indiceSolicitud].idGrupo, clienteActualizado.solicitudes[indiceSolicitud].idCiclo);
                                            myLogger.info("Estatuc ciclo grupal1: "+((cgEst != null)?cgEst.estatus:"null"));
                                            if(cgEst == null || (cgEst != null && cgEst.estatus == ClientesConstants.CICLO_APERTURA)){
                                                solicitud.documentacionCompleta = 1;
                                                solicitud.fechaFirmaConUltimoCambioDoctos = solicitud.fechaFirma;

                                                //VALIDAR AQUI SI SE DEBE CONSIDERAR ESTATUS DE GRUPO PARA NO ELIMINAR EL REGISTRO
                                                ArchivoAsociadoDAO archivoDAO = new ArchivoAsociadoDAO();
                                                archivoDAO.deleteArchivoAsociado(clienteActualizado.idCliente, idSolicitud, ClientesConstants.ARCHIVO_TIPO_AUTORIZACION);
                                                clienteActualizado.solicitudes[indiceSolicitud].archivosAsociados = null;   
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    solicitud.documentacionCompleta = 1;
                                    solicitud.fechaFirmaConUltimoCambioDoctos = solicitud.fechaFirma;
                                }
                             }
                            solicitud.estatus = ClientesConstants.ESTATUS_CAPTURADO;
                            idSolicitud = solicitudDAO.addSolicitud(conn, clienteActualizado.idCliente, solicitud);
                            int idDireccion = direccionDAO.addDireccion(conn, clienteActualizado.idCliente, direccion);
                            telefonoDAO.addTelefono(conn, clienteActualizado.idCliente, idDireccion, telefonoPrincipal);
                            telefonoDAO.addTelefono(conn, clienteActualizado.idCliente, idDireccion, telefonoRecados);
                            telefonoDAO.addTelefono(conn, clienteActualizado.idCliente, idDireccion, telefonoCelular);
                            conn.commit();
                            bitutil.registraCambioEstatus(solicitud, request);
                            clienteActualizado.solicitudes = solicitudDAO.getSolicitudes(clienteActualizado.idCliente);
                            notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente");
                            if (!ValidaInterciclo) {
                                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, notificacionIC);
                            }
                        } else {
                            //Actualizar solicitud
                            solicitud = SolicitudHelper.getVO(clienteActual.solicitudes[indiceSolicitud], request);
                            SolicitudVO solicitudOriginal = solicitudDAO.getSolicitud(solicitud.idCliente, idSolicitud);
                            if ((solicitud.getIdGrupo() != solicitudOriginal.getIdGrupo() || solicitud.getSubproducto() != solicitudOriginal.getSubproducto()) && solicitud.subproducto == ClientesConstants.ID_INTERCICLO) {
                                direccion = DireccionHelper.getVO(new DireccionVO(), request);
                                direccionAnterior = DireccionHelper.getVO(new DireccionVO(), request);
                                anteriorClienteICVO = clienteICDAO.getClienteInterCicloPorSolicitud(solicitud);
                                if (anteriorClienteICVO.getNumGrupo() != 0) {
                                    evalSemDispe = anteriorClienteICVO.getSemDispercion();
                                    cicloGrupalVO = cicloGrupalDAO.getCicloApertura(anteriorClienteICVO.getNumGrupo(), anteriorClienteICVO.getNumCiclo());
                                    if (evalSemDispe == ClientesConstants.DISPERSION_SEMANA_2) {
                                        evalEstatus = cicloGrupalVO.estatusIC;
                                    } else if (evalSemDispe == ClientesConstants.DISPERSION_SEMANA_4) {
                                        evalEstatus = cicloGrupalVO.estatusIC2;
                                    }
                                    if (evalEstatus != ClientesConstants.CICLO_APERTURA) {
                                        if (evalEstatus != ClientesConstants.CICLO_RECHAZADO) {
                                            notificacionIC = "No se puede cambiar el equipo del cliente ya que se encuentra en un Inter-Ciclo Activo";
                                            ValidaInterciclo = false;
                                        }
                                    }
                                }
                                if (ValidaInterciclo) {
                                    saldoVO = InterCicloHelper.validaSolicitudIC(solicitud, clienteActual);
                                    if (saldoVO.getIdSolicitudSICAP() > 0) {
                                        if (InterCicloHelper.validaPorcentajeClientesIC(saldoVO)) {
                                            semanaDisp = InterCicloHelper.semanaDispersion(saldoVO);
                                            if (semanaDisp != 0) {
                                                cicloGrupalVO = cicloGrupalDAO.getCiclo(saldoVO.getIdClienteSICAP(), saldoVO.getIdSolicitudSICAP());
                                                evalSemDispe = semanaDisp;
                                                if (evalSemDispe == ClientesConstants.DISPERSION_SEMANA_2) {
                                                    evalEstatus = cicloGrupalVO.estatusIC;
                                                } else if (evalSemDispe == ClientesConstants.DISPERSION_SEMANA_4) {
                                                    evalEstatus = cicloGrupalVO.estatusIC2;
                                                }
                                                if (evalEstatus == 0) {
                                                    bitacoraCicloVO.setIdEquipo(saldoVO.getIdClienteSICAP());
                                                    bitacoraCicloVO.setIdCiclo(saldoVO.getIdSolicitudSICAP());
                                                    bitacoraCicloVO.setEstatus(ClientesConstants.CICLO_APERTURA);
                                                    bitacoraCicloVO.setIdComentario(bitacoraCicloDAO.getNumComentario(saldoVO.getIdClienteSICAP(), saldoVO.getIdSolicitudSICAP()) + 1);
                                                    bitacoraCicloVO.setComentario("Inicia Apertura de Inter-Ciclo");
                                                    bitacoraCicloVO.setUsuarioComentario(request.getRemoteUser());
                                                    bitacoraCicloVO.setUsuarioAsignado(request.getRemoteUser());
                                                    bitacoraCicloVO.setSemDisp(semanaDisp);
                                                    bitacoraCicloDAO.insertaBitacoraCiclo(conn, bitacoraCicloVO);
                                                    if (semanaDisp == ClientesConstants.DISPERSION_SEMANA_2) {
                                                        cicloGrupalVO.estatusIC = ClientesConstants.CICLO_APERTURA;
                                                    }
                                                    if (semanaDisp == ClientesConstants.DISPERSION_SEMANA_4) {
                                                        cicloGrupalVO.estatusIC2 = ClientesConstants.CICLO_APERTURA;
                                                    }
                                                    evalEstatus = ClientesConstants.CICLO_APERTURA;
                                                    cicloGrupalDAO.updateCiclo(cicloGrupalVO);
                                                }
                                                if ((evalEstatus == ClientesConstants.CICLO_APERTURA || evalEstatus == ClientesConstants.CICLO_RECHAZADO)) {
                                                    clienteICVO = new ClienteIntercicloVO();
                                                    clienteICVO.setNumGrupo(saldoVO.getIdClienteSICAP());
                                                    clienteICVO.setNumCiclo(saldoVO.getIdSolicitudSICAP());
                                                    clienteICVO.setNumCliente(solicitud.idCliente);
                                                    clienteICVO.setNumSolicitud(solicitud.idSolicitud);
                                                    clienteICVO.setSemDispercion(semanaDisp);
                                                    clienteICVO.setEstatus(1);
                                                    if (InterCicloHelper.verificaExiteIntegranteIC(clienteICVO)) {
                                                        clienteICDAO.updateIntegranteInterCiclo(conn, clienteICVO);
                                                    } else {
                                                        clienteICDAO.addIntegranteInterciclo(conn, clienteICVO);
                                                    }
                                                    if (anteriorClienteICVO.getNumGrupo() != 0) {
                                                        anteriorClienteICVO.setEstatus(2);
                                                        clienteICDAO.updateIntegranteInterCiclo(conn, anteriorClienteICVO);
                                                    }
                                                } else {
                                                    notificacionIC = "El equipo no esta en un estatus valido para ingresar un cliente";
                                                    ValidaInterciclo = false;
                                                }

                                            } else {
                                                notificacionIC = "El equipo no esta en una semana valida para ingresar a un cliente de Inter-Ciclo";
                                                ValidaInterciclo = false;
                                            }
                                        } else {
                                            notificacionIC = "El equipo supero el m&aacute;ximo de clientes de Inter-Ciclo";
                                            ValidaInterciclo = false;
                                        }
                                    } else {
                                        notificacionIC = "El equipo o el cliente no cumplen con las caracteristicas para Inter-Ciclo";
                                        ValidaInterciclo = false;
                                    }
                                }
                            } else if (solicitudOriginal.subproducto == ClientesConstants.ID_INTERCICLO && solicitud.subproducto != ClientesConstants.ID_INTERCICLO) {
                                saldoVO = InterCicloHelper.validaSolicitudIC(solicitud, clienteActual);
                                cicloGrupalVO = cicloGrupalDAO.getCiclo(saldoVO.getIdClienteSICAP(), saldoVO.getIdSolicitudSICAP());
                                clienteICVO = clienteICDAO.getClienteInterCiclo(saldoVO.getIdClienteSICAP(), saldoVO.getIdSolicitudSICAP(), solicitud.idCliente, solicitud.idSolicitud);
                                if (clienteICVO.getSemDispercion() == ClientesConstants.DISPERSION_SEMANA_2) {
                                    evalEstatus = cicloGrupalVO.estatusIC;
                                } else if (clienteICVO.getSemDispercion() == ClientesConstants.DISPERSION_SEMANA_4) {
                                    evalEstatus = cicloGrupalVO.estatusIC2;
                                }
                                if ((evalEstatus == ClientesConstants.CICLO_APERTURA || evalEstatus == ClientesConstants.CICLO_RECHAZADO)) {
                                    clienteICVO.setEstatus(2);
                                    clienteICDAO.updateIntegranteInterCiclo(conn, clienteICVO);
                                } else {
                                    solicitud.subproducto = ClientesConstants.ID_INTERCICLO;
                                    notificacionIC = "No se puede sacar al usuario de Inter-Ciclo ya que el grupo se encuentra en un estatus no valido";
                                }
                            }
                            if (!ValidaInterciclo) {
                                if (solicitud.getIdGrupo() != solicitudOriginal.getIdGrupo()) {
                                    solicitud.subproducto = solicitudOriginal.subproducto;
                                    solicitud.idGrupo = solicitudOriginal.idGrupo;
                                } else {
                                    solicitud.subproducto = 0;
                                }

                            }
                            
                            if(cambioDocCompleta != null && cambioDocCompleta.equals("si")){
                                solicitud.documentacionCompleta = 1;
                            }else{
                                solicitud.documentacionCompleta = 0;
                            }
                            
                            notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, notificacionIC);
                            direccion = DireccionHelper.getVO(new DireccionVO(), request);
                            direccionAnterior = DireccionHelper.getVODireccionAnterior(new DireccionVO(), request);
                            if(indiceSolicitud == (clienteActual.solicitudes.length - 1))
                            {
                               //Se heredan datos
                                if(clienteActual.solicitudes.length > 1)
                                {
                                    //clienteActualizado.nombreCompleto = clienteActualizado.nombre + " " + clienteActualizado.aPaterno + " " + clienteActualizado.aMaterno;                                    
                                    
                                    String nombreAnterior = HTMLHelper.getParameterString(request, "nombreAnterior");
                                    String aPaternoAnterior = HTMLHelper.getParameterString(request, "aPaternoAnterior");
                                    String aMaternoAnterior = HTMLHelper.getParameterString(request, "aMaternoAnterior");
                                    String fechaNacimientoAnterior = HTMLHelper.getParameterString(request, "fechaNacimientoAnterior");
                                    int entidadAnterior = HTMLHelper.getParameterInt(request, "entidadNacimientoAnterior");
                                    int sexoAnterior = HTMLHelper.getParameterInt(request, "sexoAnterior");
                                    
                                    //if(SolicitudUtil.HayDiferenciaEntreDirecciones(direccion, direccionAnterior) || HayDiferenciaEntreNombresYFN(solicitud, solicitudAnterior))
                                   // String nombreAnteior = request.getParameter("")
                                    if(SolicitudUtil.HayDiferenciaEntreDirecciones(direccion, direccionAnterior) || SolicitudUtil.HayDiferenciaEntreNombresYFN(clienteActualizado, nombreAnterior, aPaternoAnterior, aMaternoAnterior, fechaNacimientoAnterior, entidadAnterior, sexoAnterior))

                                    {
                                        if(!request.isUserInRole("ANALISIS_CREDITO")){
                                            CicloGrupalVO cgEst = cicloGrupalDAO.getCiclo(clienteActualizado.solicitudes[indiceSolicitud].idGrupo, clienteActualizado.solicitudes[indiceSolicitud].idCiclo);
                                            myLogger.info("Estatuc ciclo grupal2: "+((cgEst != null)?cgEst.estatus:"null"));
                                            if(cgEst == null || (cgEst != null && cgEst.estatus == ClientesConstants.CICLO_APERTURA)){
                                                solicitud.documentacionCompleta = 1;
                                                solicitud.fechaFirmaConUltimoCambioDoctos = solicitud.fechaFirma;

                                                //VALIDAR AQUI SI SE DEBE CONSIDERAR ESTATUS DE GRUPO PARA NO ELIMINAR EL REGISTRO
                                                ArchivoAsociadoDAO archivoDAO = new ArchivoAsociadoDAO();
                                                archivoDAO.deleteArchivoAsociado(clienteActualizado.idCliente, idSolicitud, ClientesConstants.ARCHIVO_TIPO_AUTORIZACION);
                                                clienteActualizado.solicitudes[indiceSolicitud].archivosAsociados = null;   
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    solicitud.documentacionCompleta = 1;
                                    solicitud.fechaFirmaConUltimoCambioDoctos = solicitud.fechaFirma;
                                }

                             }
                            solicitudDAO.updateSolicitud(conn, clienteActualizado.idCliente, solicitud);
                            
                            if(solicitud.documentacionCompleta == 1){
                                solicitud.fechaFirmaConUltimoCambioDoctos = solicitud.fechaFirma;
                            }else if(solicitud.documentacionCompleta == 0){
                                solicitud.fechaFirmaConUltimoCambioDoctos = com.sicap.clientes.util.SolicitudUtil.DeterminaFechaUltimoCambioEnDocumentacion(clienteActual);
                            }
                            
                            if (clienteActual.direcciones != null) {

                                direccion = DireccionHelper.getVO(clienteActual.direcciones[0], request);
                                TelefonoVO[] telefonosActuales = telefonoDAO.getTelefonos(clienteActual.idCliente, direccion.idDireccion);
                                boolean existePrincipal = false;
                                boolean existeRecados = false;
                                boolean existeCelular = false;
                                direccionDAO.updateDireccion(conn, clienteActualizado.idCliente, direccion);
                                telefonoPrincipal = ClienteHelper.getTelefonoVO(ClienteHelper.getTelefono(direccion.telefonos, ClientesConstants.TELEFONO_PRINCIPAL), ClientesConstants.TELEFONO_PRINCIPAL, request);
                                telefonoRecados = ClienteHelper.getTelefonoVO(ClienteHelper.getTelefono(direccion.telefonos, ClientesConstants.TELEFONO_RECADOS), ClientesConstants.TELEFONO_RECADOS, request);
                                telefonoCelular = ClienteHelper.getTelefonoVO(ClienteHelper.getTelefono(direccion.telefonos, ClientesConstants.TELEFONO_CELULAR), ClientesConstants.TELEFONO_CELULAR, request);
                                if (telefonosActuales != null) {
                                    for (TelefonoVO tel : telefonosActuales) {
                                        if (tel.idTelefono != 0) {
                                            if (tel.tipoTelefono == ClientesConstants.TELEFONO_PRINCIPAL) {
                                                existePrincipal = true;
                                            } else if (tel.tipoTelefono == ClientesConstants.TELEFONO_RECADOS) {
                                                existeRecados = true;
                                            } else if (tel.tipoTelefono == ClientesConstants.TELEFONO_CELULAR) {
                                                existeCelular = true;
                                            }
                                        }
                                    }
                                }
                                if (existePrincipal) {
                                    telefonoDAO.updateTelefono(conn, clienteActualizado.idCliente, direccion.idDireccion, telefonoPrincipal);
                                } else {
                                    telefonoDAO.addTelefono(conn, clienteActualizado.idCliente, direccion.idDireccion, telefonoPrincipal);
                                }
                                if (existeRecados) {
                                    telefonoDAO.updateTelefono(conn, clienteActualizado.idCliente, direccion.idDireccion, telefonoRecados);
                                } else {
                                    telefonoDAO.addTelefono(conn, clienteActualizado.idCliente, direccion.idDireccion, telefonoRecados);
                                }
                                if (existeCelular) {
                                    telefonoDAO.updateTelefono(conn, clienteActualizado.idCliente, direccion.idDireccion, telefonoCelular);
                                } else {
                                    telefonoDAO.addTelefono(conn, clienteActualizado.idCliente, direccion.idDireccion, telefonoCelular);
                                }

                                /*if ( direccion.telefonos!=null ){
                             telefonodao.updateTelefono(conn, clienteActualizado.idCliente, direccion.idDireccion, telefonoPrincipal);
                             telefonodao.updateTelefono(conn, clienteActualizado.idCliente, direccion.idDireccion, telefonoRecados);
                             telefonodao.updateTelefono(conn, clienteActualizado.idCliente, direccion.idDireccion, telefonoCelular);
                             }else{
                             telefonodao.addTelefono(conn, clienteActualizado.idCliente, direccion.idDireccion, telefonoPrincipal);
                             telefonodao.addTelefono(conn, clienteActualizado.idCliente, direccion.idDireccion, telefonoRecados);
                             telefonodao.addTelefono(conn, clienteActualizado.idCliente, direccion.idDireccion, telefonoCelular);
                             }*/
                            } else {
                                direccion = DireccionHelper.getVO(new DireccionVO(), request);
                                int idDireccion = direccionDAO.addDireccion(conn, clienteActualizado.idCliente, direccion);
                                telefonoPrincipal = ClienteHelper.getTelefonoVO(telefonoPrincipal, ClientesConstants.TELEFONO_PRINCIPAL, request);
                                telefonoRecados = ClienteHelper.getTelefonoVO(telefonoRecados, ClientesConstants.TELEFONO_RECADOS, request);
                                telefonoCelular = ClienteHelper.getTelefonoVO(telefonoCelular, ClientesConstants.TELEFONO_CELULAR, request);
                                telefonoDAO.addTelefono(conn, clienteActualizado.idCliente, idDireccion, telefonoPrincipal);
                                telefonoDAO.addTelefono(conn, clienteActualizado.idCliente, idDireccion, telefonoRecados);
                                telefonoDAO.addTelefono(conn, clienteActualizado.idCliente, idDireccion, telefonoCelular);
                            }

                            //direcciondao.updateDireccion(conn, clienteActualizado.idCliente, direccion);
                            conn.commit();
                            
//                            direccion = DireccionHelper.getVO(new DireccionVO(), request);
//                            direccionAnterior = DireccionHelper.getVODireccionAnterior(new DireccionVO(), request);
//                            if(indiceSolicitud == (clienteActual.solicitudes.length - 1))
//                            {
//                               //Se heredan datos
//                                if(clienteActual.solicitudes.length > 1)
//                                {
//                                    clienteActualizado.nombreCompleto = clienteActualizado.nombre + " " + clienteActualizado.aPaterno + " " + clienteActualizado.aMaterno;                                    
//                                    
//                                    String nombreAnterior = HTMLHelper.getParameterString(request, "nombreAnterior");
//                                    String aPaternoAnterior = HTMLHelper.getParameterString(request, "aPaternoAnterior");
//                                    String aMaternoAnterior = HTMLHelper.getParameterString(request, "aMaternoAnterior");
//                                    String fechaNacimientoAnterior = HTMLHelper.getParameterString(request, "fechaNacimientoAnterior");
//                                    //if(SolicitudUtil.HayDiferenciaEntreDirecciones(direccion, direccionAnterior) || HayDiferenciaEntreNombresYFN(solicitud, solicitudAnterior))
//                                   // String nombreAnteior = request.getParameter("")
//                                    if(SolicitudUtil.HayDiferenciaEntreDirecciones(direccion, direccionAnterior) || SolicitudUtil.HayDiferenciaEntreNombresYFN(clienteActualizado, nombreAnterior, aPaternoAnterior, aMaternoAnterior, fechaNacimientoAnterior))
//
//                                    {
//                                        solicitud.documentacionCompleta = 1;
//                                    }
//                                    else
//                                    {
//                                        solicitud.documentacionCompleta = 0;
//                                    }
//                                }
//
//                             }
                            clienteActualizado.solicitudes[indiceSolicitud] = solicitud;
                            notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron actualizados correctamente");

                            if (!ValidaInterciclo) {
                                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "El equipo o el cliente no cumplen con las caracteristicas para Inter-Ciclo");
                            }
                            if (!notificacionIC.equals("")) {
                                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, notificacionIC);
                            }
                         
 
                        }
                        conn.commit();
                        bitutil.registraEvento(clienteActualizado.toString());
                        bitutil.registraEvento(solicitud.toString());
                        bitutil.registraEvento(direccion.toString());
                        bitutil.registraEvento(telefonoPrincipal.toString());
                        bitutil.registraEvento(telefonoRecados.toString());
                        bitutil.registraEvento(telefonoCelular.toString());
                        //Actualiza la solicitud del objeto cliente			
                        clienteActualizado.direcciones = direccionDAO.getDirecciones(clienteActualizado.idCliente);
                        if (clienteActualizado.direcciones != null) {
                            CatalogoVO localidad = catalogoDAO.getLocalidad(clienteActualizado.direcciones[0].idColonia, clienteActualizado.direcciones[0].idLocalidad);
                            if (localidad != null) {
                                clienteActualizado.direcciones[0].localidad = localidad.descripcion;
                            }
                        }

                        for (int i = 0; i < clienteActualizado.direcciones.length; i++) {
                            clienteActualizado.direcciones[i].telefonos = telefonoDAO.getTelefonos(clienteActualizado.idCliente, clienteActualizado.direcciones[i].idDireccion);
                        }
                        //Actualiza el cliente en sesion
                        session.setAttribute("CLIENTE", clienteActualizado);
                    } else if (!clienteActualizado.getCurp().substring(0, 4).equals(CURP.substring(0, 4))) {
                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Error dentro del segmento Iniciales del Nombre en la CURP");
                        regresarDatosAnterioresCurp(request, clienteActualizado);
                        session.setAttribute("CLIENTE", clienteActualizado);
                    } else if (!clienteActualizado.getCurp().substring(4, 10).equals(CURP.substring(4, 10))) {
                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Error dentro del segmento Fecha Nacimiento en la CURP.");
                        regresarDatosAnterioresCurp(request, clienteActualizado);
                        session.setAttribute("CLIENTE", clienteActualizado);
                    } else if (!clienteActualizado.getCurp().substring(10, 11).equals(CURP.substring(10, 11))) {
                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Error dentro del segmento Sexo en la CURP.");
                        regresarDatosAnterioresCurp(request, clienteActualizado);
                        session.setAttribute("CLIENTE", clienteActualizado);
                    } else if (!clienteActualizado.getCurp().substring(11, 13).equals(CURP.substring(11, 13))) {
                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Error dentro del segmento Entidad de Nacimiento en la CURP.");
                        regresarDatosAnterioresCurp(request, clienteActualizado);
                        session.setAttribute("CLIENTE", clienteActualizado);
                    } else if (!clienteActualizado.getCurp().substring(13, 16).equals(CURP.substring(13, 16))) {
                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Error dentro del segmento Consonantes Internas en la CURP.");
                        regresarDatosAnterioresCurp(request, clienteActualizado);
                        session.setAttribute("CLIENTE", clienteActualizado);
                    } else if (!clienteActualizado.getCurp().substring(16, 18).matches("[0-9]*")) {
                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Error dentro del segmento Homoclave no numerica en la CURP.");
                        regresarDatosAnterioresCurp(request, clienteActualizado);
                        session.setAttribute("CLIENTE", clienteActualizado);
                    }
                } else {
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Ya se encuentra un RFC registrado en la sucursal " + clienteVerfSuc + " con el número de cliente  " + clienteVerfId);
                }
            } else {
                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, respuestaWS);
            }
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch (ClientesDBException dbe) {
            try {
                conn.close();
            } catch (SQLException ex) {
                myLogger.error("ClientesDBException", ex);
            }
            myLogger.error("ClientesDBException", dbe);
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            myLogger.error("ClientesDBException", e);
            throw new CommandException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                myLogger.error("ClientesDBException", sqle);
                throw new CommandException(sqle.getMessage());
            }
        }
        return siguiente;
    }
    
    
    private void regresarDatosAnterioresCurp(HttpServletRequest request, ClienteVO clienteActualizado){
        try {
            myLogger.info("Regresando info anterior...");
            String nombreAnterior = HTMLHelper.getParameterString(request, "nombreAnterior");
            String aPaternoAnterior = HTMLHelper.getParameterString(request, "aPaternoAnterior");
            String aMaternoAnterior = HTMLHelper.getParameterString(request, "aMaternoAnterior");
            Date fechaNacimientoAnterior = HTMLHelper.getParameterDate(request, "fechaNacimientoAnterior");
            int entidadAnterior = HTMLHelper.getParameterInt(request, "entidadNacimientoAnterior");
            int sexoAnterior = HTMLHelper.getParameterInt(request, "sexoAnterior");
            
            
            clienteActualizado.nombre = nombreAnterior;
            clienteActualizado.aPaterno = aPaternoAnterior;
            clienteActualizado.aMaterno = aMaternoAnterior;
            clienteActualizado.fechaNacimiento = new java.sql.Date(fechaNacimientoAnterior.getTime());
            clienteActualizado.entidadNacimiento = entidadAnterior;
            clienteActualizado.sexo = sexoAnterior;
            
        } catch (Exception ex) {
            myLogger.error("Error al regresar datos anteriores", ex);
        }
    }
}
