package com.sicap.clientes.commands;

import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.OrdenesDePagoHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.OrdenDePagoVO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CommandGeneraOrdenesPago implements Command {

    private String siguiente;

    public CommandGeneraOrdenesPago(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        Vector<Notification> notificaciones = new Vector<Notification>();
        Connection conn = null;
        try {
            int banco = HTMLHelper.getParameterInt(request, "banco");
            int sucursal = HTMLHelper.getParameterInt(request, "sucursal");
            int estatus = HTMLHelper.getParameterInt(request, "estatus");
            Date fechaIni = Convertidor.toSqlDate(new java.util.Date());
            Date fechaFin = Convertidor.toSqlDate(new java.util.Date());
            ArrayList<OrdenDePagoVO> arrOrdenes = new ArrayList<OrdenDePagoVO>();
            ArrayList<OrdenDePagoVO> arrOdpEnviadas = new ArrayList<OrdenDePagoVO>();
            HttpSession session = request.getSession();
            java.util.Date[] fechasInhabiles = (java.util.Date[]) session.getAttribute("INHABILES");
            List file = null;
            String filename = null;
            conn = ConnectionManager.getMySQLConnection();
            conn.setAutoCommit(false);
            boolean paroEnvio = true;
            OrdenDePagoVO evaluaODP = new OrdenDePagoVO();
            if (request.getParameter("command").equals("buscaOrdenesPagos")) {
                if (banco == 10) {
                    if (!request.getParameter("fechaInicial").equals("")) {
                        fechaIni = Convertidor.stringToSqlDate(request.getParameter("fechaInicial"));
                    }
                    if (!request.getParameter("fechaFinal").equals("")) {
                        fechaFin = Convertidor.stringToSqlDate(request.getParameter("fechaFinal"));
                    }
                    arrOrdenes = new OrdenDePagoDAO().getOrdenesPago(banco, sucursal, fechaIni, fechaFin);
                    //notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "");
                    notificaciones.add(new Notification(ClientesConstants.INFO_LEVEL, ""));
                }
                if (arrOrdenes.isEmpty()) {
                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "No se encontraron Registros"));
                }
            } else if (request.getParameter("command").equals("generaOrdenesPagos")) {
                arrOrdenes = new OrdenesDePagoHelper().getOrdenesPago(request);
                Date fecha = Convertidor.toSqlDate(new java.util.Date());
                OrdenDePagoDAO ordenDAO = new OrdenDePagoDAO();
                IntegranteCicloDAO clienteDAO = new IntegranteCicloDAO();
                int numEnvioDia = 0, numEnvioClie = 0, envioIni = 0, envioFin = 0;
                String referencia = "";
                int res = 0;
                List<OrdenDePagoVO> list = new ArrayList<OrdenDePagoVO>();
                OrdenesDePagoHelper oPagoHelp = null;
                double importeArchivo = 0.00;
                List saveFileList = null;
                File saveFile;
                int j = 1;
                if (arrOrdenes.isEmpty()) {
                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "No se encontraron Registros"));
                    paroEnvio = false;
                } else {
                    for (OrdenDePagoVO ordenVO : arrOrdenes) {
                        evaluaODP = ordenDAO.getOrdenDePagoReferencia(ordenVO.getReferencia());
                        if (evaluaODP.getEstatus() != ClientesConstants.OP_DESEMBOLSADO && evaluaODP.getEstatus() != ClientesConstants.OP_SOLICITA_CANCELACION && evaluaODP.getEstatus() != ClientesConstants.OP_ACTUALIZA_NOMBRE && evaluaODP.getEstatus() != ClientesConstants.OP_SEGURO) {
                            arrOdpEnviadas.add(new OrdenDePagoVO(ordenVO.getIdOrdenPago(), ordenVO.getIdCliente(), ordenVO.getIdSolicitud(), ordenVO.getIdOperacion(), ordenVO.getIdSucursal(), ordenVO.getNombre(), ordenVO.getMonto(),
                                    ordenVO.getReferencia(), ordenVO.getNombreArchivo(), ordenVO.getUsuario(), evaluaODP.getFechaCaptura(), evaluaODP.getFechaEnvio(), ordenVO.getIdBanco(), evaluaODP.getEstatus(),
                                    ordenVO.getNomSucursal(), ordenVO.getProducto(), ordenVO.getDescEstatus(), ordenVO.getGrupo(), evaluaODP.getNumEnvio()));
                            paroEnvio = false;
                        }
                    }
                }
                if (paroEnvio) {
                    switch (banco) {
                        case 10://TELECOM
                            Logger.debug("Procesando Layout Telecom");
                            numEnvioDia = ordenDAO.getEnvioDiaTelecom(fecha);
                            numEnvioClie = ordenDAO.getEnvioClienteTelecom() + 1;
                            envioIni = numEnvioClie;
                            for (OrdenDePagoVO ordenVO : arrOrdenes) {
                                ordenVO.setFecha(fecha);
                                ordenVO.setIdOperacion(numEnvioDia + 1);
                                ordenVO.setIdOrdenPago(numEnvioClie);
                                referencia = ClientesUtil.makeReferencia(ordenVO.getIdSucursal(), ordenVO.getIdCliente(), ordenVO.getIdSolicitud());
                                referencia = "100" + "09" + referencia.substring(3, 13);
                                ordenVO.setReferencia(referencia);
                                res = ordenDAO.setOrdenTelecom(ordenVO);
                                clienteDAO.updateEnvioTelecom(ordenVO);
                                if (res == 1) {
                                    list.add(ordenVO);
                                    importeArchivo += ordenVO.getMonto();
                                    envioFin = numEnvioClie;
                                    numEnvioClie++;
                                }
                            }
                            oPagoHelp = new OrdenesDePagoHelper(list, numEnvioDia + 1, importeArchivo);
                            file = oPagoHelp.creaOrdenDePagoTelecom(envioIni, envioFin);
                            filename = oPagoHelp.getFileNameTelecom(numEnvioDia + 1);
                            break;
                        case 2://BANORTE
                            Logger.debug("Procesando Layout Banorte");
                            for (OrdenDePagoVO ordenVO : arrOrdenes) {
                                ordenVO.setUsuario(request.getRemoteUser());
                                ordenVO.setFechaEnvio(Convertidor.toSqlTimeStamp(new java.util.Date()));
                                cambioEstatus(ordenVO, banco);
                                list.add(ordenVO);
                            }
                            oPagoHelp = new OrdenesDePagoHelper(list);
                            file = oPagoHelp.creaOrdenDePagoBanorte();
                            numEnvioDia = oPagoHelp.envio;
                            filename = oPagoHelp.getFileName();
                            break;
                        case 3://BANCOMER
                            Logger.debug("Procesando Layout Bancomer");
                            for (OrdenDePagoVO ordenVO : arrOrdenes) {
                                if (ordenVO.getEstatus() == ClientesConstants.OP_ACTUALIZA_NOMBRE) {
                                    list.add(new OrdenDePagoVO(ordenVO.getIdOrdenPago(), ordenVO.getIdCliente(), ordenVO.getIdSolicitud(), ordenVO.getIdSucursal(), ordenVO.getNombre(), ordenVO.getMonto(), ordenVO.getReferencia(),
                                            request.getRemoteUser(), Convertidor.toSqlTimeStamp(new java.util.Date()), ordenVO.getEstatus(), ordenVO.getNomSucursal(), ordenVO.getGrupo(), ordenVO.getNombres(), ordenVO.getApaterno(),
                                            ordenVO.getAmaterno(), ordenVO.getIdOficTelecom(), ordenVO.getNomOficTelecom(), ordenVO.getIdentificacion()));
                                    list.add(new OrdenDePagoVO(ordenVO.getIdOrdenPago(), ordenVO.getIdCliente(), ordenVO.getIdSolicitud(), ordenVO.getIdSucursal(), ordenVO.getNombre(), ordenVO.getMonto(), ordenVO.getReferencia(),
                                            request.getRemoteUser(), Convertidor.toSqlTimeStamp(new java.util.Date()), 0, ordenVO.getNomSucursal(), ordenVO.getGrupo(), ordenVO.getNombres(), ordenVO.getApaterno(),
                                            ordenVO.getAmaterno(), ordenVO.getIdOficTelecom(), ordenVO.getNomOficTelecom(), ordenVO.getIdentificacion()));
                                } else {
                                    ordenVO.setUsuario(request.getRemoteUser());
                                    ordenVO.setFechaEnvio(Convertidor.toSqlTimeStamp(new java.util.Date()));
                                    cambioEstatus(ordenVO, banco);
                                    list.add(ordenVO);
                                }
                            }
                            oPagoHelp = new OrdenesDePagoHelper(list, banco);
                            file = oPagoHelp.creaOrdenDePagoBancomer();
                            numEnvioDia = oPagoHelp.envio;
                            filename = oPagoHelp.getFileName("OPN");
                            break;
                        case 12://BANAMEX
                            Logger.debug("Procesando Layout Banamex");
                            for (OrdenDePagoVO ordenVO : arrOrdenes) {
                                if (ordenVO.getEstatus() == ClientesConstants.OP_ACTUALIZA_NOMBRE) {
                                    list.add(new OrdenDePagoVO(ordenVO.getIdOrdenPago(), ordenVO.getIdCliente(), ordenVO.getIdSolicitud(), ordenVO.getIdSucursal(), ordenVO.getNombre(), ordenVO.getMonto(), ordenVO.getReferencia(),
                                            request.getRemoteUser(), Convertidor.toSqlTimeStamp(new java.util.Date()), ordenVO.getEstatus(), ordenVO.getNomSucursal(), ordenVO.getGrupo(), ordenVO.getNombres(), ordenVO.getApaterno(),
                                            ordenVO.getAmaterno(), ordenVO.getIdOficTelecom(), ordenVO.getNomOficTelecom(), ordenVO.getIdentificacion()));
                                    list.add(new OrdenDePagoVO(ordenVO.getIdOrdenPago(), ordenVO.getIdCliente(), ordenVO.getIdSolicitud(), ordenVO.getIdSucursal(), ordenVO.getNombre(), ordenVO.getMonto(), ordenVO.getReferencia(),
                                            request.getRemoteUser(), Convertidor.toSqlTimeStamp(new java.util.Date()), 0, ordenVO.getNomSucursal(), ordenVO.getGrupo(), ordenVO.getNombres(), ordenVO.getApaterno(),
                                            ordenVO.getAmaterno(), ordenVO.getIdOficTelecom(), ordenVO.getNomOficTelecom(), ordenVO.getIdentificacion()));
                                } else {
                                    ordenVO.setUsuario(request.getRemoteUser());
                                    ordenVO.setFechaEnvio(Convertidor.toSqlTimeStamp(new java.util.Date()));
                                    cambioEstatus(ordenVO, banco);
                                    list.add(ordenVO);
                                }
                            }
                            oPagoHelp = new OrdenesDePagoHelper(list, banco);
                            file = oPagoHelp.creaOrdenDePagoBanamex();
                            numEnvioDia = oPagoHelp.envio;
                            filename = oPagoHelp.getFileName("OBanamex");
                            break;
                        case 16://SCOTIA
                            Logger.debug("Procesando Layout Scotia");
                            for (OrdenDePagoVO ordenVO : arrOrdenes) {
                                if (ordenVO.getEstatus() == ClientesConstants.OP_ACTUALIZA_NOMBRE) {
                                    list.add(new OrdenDePagoVO(ordenVO.getIdOrdenPago(), ordenVO.getIdCliente(), ordenVO.getIdSolicitud(), ordenVO.getIdSucursal(), ordenVO.getNombre(), ordenVO.getMonto(), ordenVO.getReferencia(),
                                            request.getRemoteUser(), Convertidor.toSqlTimeStamp(new java.util.Date()), ordenVO.getEstatus(), ordenVO.getNomSucursal(), ordenVO.getGrupo(), ordenVO.getNombres(), ordenVO.getApaterno(),
                                            ordenVO.getAmaterno(), ordenVO.getIdOficTelecom(), ordenVO.getNomOficTelecom(), ordenVO.getIdentificacion()));
                                    list.add(new OrdenDePagoVO(ordenVO.getIdOrdenPago(), ordenVO.getIdCliente(), ordenVO.getIdSolicitud(), ordenVO.getIdSucursal(), ordenVO.getNombre(), ordenVO.getMonto(), ordenVO.getReferencia(),
                                            request.getRemoteUser(), Convertidor.toSqlTimeStamp(new java.util.Date()), 0, ordenVO.getNomSucursal(), ordenVO.getGrupo(), ordenVO.getNombres(), ordenVO.getApaterno(),
                                            ordenVO.getAmaterno(), ordenVO.getIdOficTelecom(), ordenVO.getNomOficTelecom(), ordenVO.getIdentificacion()));
                                }
                                else if(ordenVO.getEstatus() == ClientesConstants.OP_SOLICITA_CANCELACION){
                                    ordenVO.setUsuario(request.getRemoteUser());
                                    cambioEstatus(ordenVO, banco);
                                    list.add(ordenVO);
                                }
                                else {
                                    ordenVO.setUsuario(request.getRemoteUser());
                                    ordenVO.setFechaEnvio(Convertidor.toSqlTimeStamp(new java.util.Date()));
                                    cambioEstatus(ordenVO, banco);
                                    list.add(ordenVO);
                                }
                            }
                            oPagoHelp = new OrdenesDePagoHelper(list, banco);
                            file = oPagoHelp.creaOrdenDePagoScotia();
                            numEnvioDia = oPagoHelp.envio;
                            filename = oPagoHelp.getFileName("ODPScotia");
                            break;
                        case 24://Santander
                            estatus = arrOrdenes.get(0).getEstatus();
                            if (estatus == ClientesConstants.OP_DESEMBOLSADO) {
                                Logger.debug("Procesando Layout Santander");
                                for (OrdenDePagoVO ordenVO : arrOrdenes) {
                                    ordenVO.setUsuario(request.getRemoteUser());
                                    ordenVO.setFechaEnvio(Convertidor.toSqlTimeStamp(new java.util.Date()));
                                    cambioEstatus(ordenVO, banco);
                                    list.add(ordenVO);
                                }
                                oPagoHelp = new OrdenesDePagoHelper(list, banco);
                                file = oPagoHelp.creaOrdenDePagoSantander(fechasInhabiles);
                                numEnvioDia = oPagoHelp.envio;
                                filename = oPagoHelp.getFileName("ODPSantander");
                            } else if (estatus == ClientesConstants.OP_ACTUALIZA_NOMBRE || estatus == ClientesConstants.OP_SOLICITA_CANCELACION) {
                                Logger.debug("Procesando Layout Cancelacion/Modificacion Santander");
                                for (OrdenDePagoVO ordenVO : arrOrdenes) {
                                    ordenVO.setUsuario(request.getRemoteUser());
                                    ordenVO.setFechaEnvio(Convertidor.toSqlTimeStamp(new java.util.Date()));
                                    cambioEstatus(ordenVO, banco);
                                    list.add(ordenVO);
                                }
                                oPagoHelp = new OrdenesDePagoHelper(list, banco);
                                file = oPagoHelp.creaCsvSantander();
                                numEnvioDia = oPagoHelp.envio;
                                filename = oPagoHelp.getFileNameCSV("ODPSantander");

                            }
                            break;
                    }
                    saveFile = new File(ClientesConstants.RUTA_BASE_ARCHIVOS + "\\EnvioOrdenesDePago\\" + filename);
                    saveFileList = (List) file;
                    FileWriter fWrite = new FileWriter(saveFile);
                    BufferedWriter buffer = new BufferedWriter(fWrite);
                    PrintWriter pWrite = new PrintWriter(buffer);
                    if (!saveFileList.isEmpty()) {
                        Iterator i = file.iterator();
                        while (i.hasNext()) {
                            String linea = (String) i.next();
                            pWrite.write(linea + "\r\n");
                        }
                        pWrite.close();
                    }
                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Ordenes Generadas"));
                    if (banco != 10) {
                        if (ordenDAO.updateStatusFromList(conn, list, numEnvioDia, "CommandGeneraOrdenesPago")) {
                            conn.commit();
                        }
                    }
                }
            }
            if (!paroEnvio) {
                if (!arrOrdenes.isEmpty()) {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_LEVEL, "Envio con OPD's ya enviados"));
                }
                siguiente = "/generaOrdenesPago.jsp";
                request.setAttribute("ORDENESPAGO", arrOdpEnviadas);
            } else {
                request.setAttribute("ORDENESPAGO", arrOrdenes);
            }

            request.setAttribute("NOTIFICACIONES", notificaciones);
            request.setAttribute("sucursal", sucursal);
            request.setAttribute("FILE", file);
            request.setAttribute("FILENAME", filename);
            request.setAttribute("tipo", request.getParameter("tipo"));
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

    private OrdenDePagoVO cambioEstatus(OrdenDePagoVO orden, int banco) {

        if (orden.getEstatus() == ClientesConstants.OP_DESEMBOLSADO) {
            orden.setEstatus(ClientesConstants.OP_DISPERSADA);
        } else if (orden.getEstatus() == ClientesConstants.OP_SOLICITA_CANCELACION) {
            orden.setEstatus(ClientesConstants.OP_CANCELADA);
        } else if (orden.getEstatus() == ClientesConstants.OP_SEGURO) {
            orden.setEstatus(ClientesConstants.OP_SEGURO_ENVIADO);
        }
        return orden;
    }

}
