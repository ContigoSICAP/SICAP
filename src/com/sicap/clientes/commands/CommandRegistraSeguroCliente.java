package com.sicap.clientes.commands;

import com.sicap.clientes.dao.AseguradosDAO;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.BeneficiarioDAO;
import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.DecisionComiteDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.SeguroHelper;
import com.sicap.clientes.helpers.TablaAmortizacionHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SeguroConstantes;
import com.sicap.clientes.util.SegurosUtil;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.SegurosVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.SucursalVO;
import java.sql.Savepoint;

public class CommandRegistraSeguroCliente implements Command {

    private String siguiente;

    public CommandRegistraSeguroCliente(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        
        Notification notificaciones[] = new Notification[1];
        HttpSession session = request.getSession();
        SegurosVO seguroCapturado = new SegurosVO();
        SegurosVO seguroActual = new SegurosVO();
        SegurosDAO seguroDAO = new SegurosDAO();
        AseguradosDAO asegurDAO = new AseguradosDAO();
        BeneficiarioDAO benefiDAO = new BeneficiarioDAO();
        CatalogoDAO catalogoDAO = new CatalogoDAO();
        int i = 0;
        int numSeguro = 0;
        String fecNacimiento = "";
        Connection conn = null;
        Savepoint save = null;
        try {
            conn = ConnectionManager.getMySQLConnection();
            conn.setAutoCommit(false);
            save = conn.setSavepoint("BackSeguro");
            int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
            ClienteVO cliente = (ClienteVO) session.getAttribute("CLIENTE");
            int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
            int idCliente = cliente.idCliente;
            SucursalVO sucursalVo = new SucursalVO();
            SucursalDAO sucursalDao = new SucursalDAO();
            SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
            seguroActual = seguroDAO.getSeguros(idCliente, idSolicitud);
            BitacoraUtil bitutil = new BitacoraUtil(idCliente, request.getRemoteUser(), "CommandRegistraSeguroCliente");
            seguroCapturado = SeguroHelper.getSeguroVO(seguroCapturado, request);
            if(seguroActual!=null){
                numSeguro = seguroActual.numSeguro;
            } else {
                numSeguro = (seguroDAO.numSeguro(idCliente))+1;
            }
            seguroCapturado.numSeguro=numSeguro;
            solicitud.seguro = seguroCapturado;
            sucursalVo = sucursalDao.getSucursal(cliente.idSucursal);
            SeguroHelper.calculaPrimaTotal(solicitud,sucursalVo);
            if(seguroActual!=null){
                seguroDAO.updateSeguro(conn, save, seguroCapturado);
            }
            else {
                seguroDAO.addSeguro(conn, save, seguroCapturado);
            }            
            //PROCESA ASEGURADOS
            if(seguroCapturado!=null){
                asegurDAO.deleteAsegurados(conn, save, seguroCapturado);
            }
            for (i = 0; solicitud.seguro.asegurados != null && i < solicitud.seguro.asegurados.size(); i++) {
                solicitud.seguro.asegurados.get(i).setNumAsegurado(i+1);
                asegurDAO.addAsegurados(conn, save, seguroCapturado, solicitud.seguro.asegurados.get(i)); 
                fecNacimiento = solicitud.seguro.asegurados.get(i).getFecNacimiento().substring(8,10)+"/"+solicitud.seguro.asegurados.get(i).getFecNacimiento().substring(5, 7)+"/"+solicitud.seguro.asegurados.get(i).getFecNacimiento().substring(0, 4);
                solicitud.seguro.asegurados.get(i).setFecNacimiento(fecNacimiento);
            }
            //PROCESA BENEFICIARIOS
            if(seguroCapturado!=null){
                benefiDAO.deleteBeneficiario(conn, save, seguroCapturado);
            }
            for (i = 0; solicitud.seguro.beneficiarios != null && i < solicitud.seguro.beneficiarios.size(); i++) {
                solicitud.seguro.beneficiarios.get(i).setNumBeneficiario(i+1);
                benefiDAO.addBeneficiario(conn, save, seguroCapturado, solicitud.seguro.beneficiarios.get(i));
                fecNacimiento = solicitud.seguro.beneficiarios.get(i).getFechaNacimiento().substring(8, 10)+"/"+solicitud.seguro.beneficiarios.get(i).getFechaNacimiento().substring(5, 7)+"/"+solicitud.seguro.beneficiarios.get(i).getFechaNacimiento().substring(0, 4);
                solicitud.seguro.beneficiarios.get(i).setFechaNacimiento(fecNacimiento);
            }
            //RECALCULA LA TABLA DE AMORTIZACION DEL CLIENTE
            
            TablaAmortizacionHelper.generaTablaAmortizacion(cliente, solicitud);
            notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron actualizados correctamente");            
            cliente.solicitudes[indiceSolicitud] = solicitud;          
            bitutil.registraEvento(solicitud.seguro);
            
            // Checamos si aplicamos el calculo de seguro financiado
            boolean aplicaSegFin = SegurosUtil.validarFechaDeadLineSegF(solicitud.seguro.fechaCaptura);
            if (aplicaSegFin && solicitud.seguro.contratacion == SeguroConstantes.CONTRATACION_SI) {
                // De acuerdo al tipo de seguro seleccionado obtenemos el costo del seguro
                double costoSeguroContratado = SeguroHelper.getCostoSeguro(solicitud.seguro.modulos, sucursalVo);
                // RGN-001 Monto con Seguro Financiado = Monto a desembolsar + Costo del Seguro Contratado.
                double montoConSeguro = solicitud.decisionComite.montoAutorizado + costoSeguroContratado;
                solicitud.decisionComite.montoConSeguro = montoConSeguro;
                // Actualizamos los montos con seguro en las tablas d_integrantes_ciclo y d_decision_comite
                IntegranteCicloDAO integranteCicloDAO = new IntegranteCicloDAO();
                integranteCicloDAO.updateMontoConSeguro(conn, montoConSeguro, idCliente, idSolicitud, solicitud.idCiclo);
                DecisionComiteDAO decisionComiteDAO = new DecisionComiteDAO();
                decisionComiteDAO.updateMontoConSeguro(conn, montoConSeguro, idCliente, idSolicitud);
            } else {
                // Si aplica seg financiado pero eligio no contratar y anteriormente tenia contratacion
                if (aplicaSegFin && solicitud.seguro.contratacion == SeguroConstantes.CONTRATACION_NO) {
                    // Actualizamos los montos con seguro en las tablas d_integrantes_ciclo y d_decision_comite a cero
                    IntegranteCicloDAO integranteCicloDAO = new IntegranteCicloDAO();
                    integranteCicloDAO.updateMontoConSeguro(conn, SeguroConstantes.COSTO_SEG_OP_NO, idCliente, idSolicitud, solicitud.idCiclo);
                    DecisionComiteDAO decisionComiteDAO = new DecisionComiteDAO();
                    decisionComiteDAO.updateMontoConSeguro(conn, SeguroConstantes.COSTO_SEG_OP_NO, idCliente, idSolicitud);
                }
            }
            conn.commit();
            request.setAttribute("NOTIFICACIONES", notificaciones);
            session.setAttribute("CLIENTE", cliente);            
        } catch (Exception e) {
            try {
                conn.rollback(save);
                e.printStackTrace();
            } catch (SQLException se1) {
                com.sicap.clientes.util.Logger.debug("Problema dentro de Rollback Seguro");
                com.sicap.clientes.util.Logger.debug(se1.getMessage());
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se2) {
                com.sicap.clientes.util.Logger.debug("Problema de conexion");
            }
        }
        return siguiente;
    }
}