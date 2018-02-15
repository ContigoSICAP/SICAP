package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.CreditoDAO;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.EmpleoDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.CatalogoVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.DireccionVO;
import com.sicap.clientes.vo.EmpleoVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.UsuarioVO;
import java.sql.Date;
import org.apache.log4j.Logger;

public class CommandRegistraSolicitud implements Command {

    private static Logger myLogger = Logger.getLogger(CommandRegistraSolicitud.class);
    private String siguiente;

    public CommandRegistraSolicitud(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        HttpSession session = request.getSession();
        UsuarioVO usuario = (UsuarioVO) session.getAttribute("USUARIO");
        ClienteVO cliente = new ClienteVO();
        SolicitudVO nuevaSolicitud = new SolicitudVO();
        CatalogoVO localidadVO = null;
        DireccionDAO direccionDAO = new DireccionDAO();
        CatalogoDAO catalogoDAO = new CatalogoDAO();
        CreditoDAO creditoDAO = new CreditoDAO();
        String siguienteConsumo = "/capturaDatosClienteConsumo.jsp";
        String sellFinanceExterno = "/capturaAbreviadaDatosCliente.jsp";
        String siguienteCapturaRapida = "/clienteCapturaRapida.jsp";
        int producto = 0;
        int idSolicitud = 0;

        try {
            producto = HTMLHelper.getParameterInt(request, "producto");
            cliente = (ClienteVO) session.getAttribute("CLIENTE");
            BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandRegistraSolicitud");
            if (producto != 0) {
                nuevaSolicitud = new SolicitudVO();
                nuevaSolicitud.tipoOperacion = producto;
                nuevaSolicitud.fechaCaptura = new Timestamp(System.currentTimeMillis());
                Date fechaHoy = Convertidor.toSqlDate(new java.util.Date());
                
                
                SolicitudVO ultimoDesembolso = obtenerUltimoDesembolso(cliente, cliente.solicitudes.length);
                boolean heredarInformacion = false;

                if (ultimoDesembolso!=null && FechasUtil.inBetweenDays(ultimoDesembolso.fechaDesembolso, fechaHoy) < 365){
                    myLogger.debug("Fecha ultimo desembolso : "+ultimoDesembolso.fechaDesembolso);
                    heredarInformacion=true;
//                    nuevaSolicitud.estatus = ClientesConstants.SOLICITUD_CAPTURADO;
//                    sol.updateSolicitudRenovacion(cliente.idCliente, cliente.solicitudes[indiceSolicitud].idSolicitud);
//                    solicitud.estatus = 1;//para que se guarde en session y se vaya a la ventana correcta
                }else{
                    myLogger.info("NO tiene desembolsos anteriores o es mayor a un año...");
                    CreditoVO circuloUltimoCredito = creditoDAO.getUltimoCredito(cliente.idCliente, ClientesConstants.CIRCULO_CREDIT0, 0);
                    if (circuloUltimoCredito != null
                           && FechasUtil.inBetweenDays(circuloUltimoCredito.fechaConsulta, fechaHoy) < 30) {
                        if(request.isUserInRole("ANALISIS_CREDITO")){
                            myLogger.info("Se queda el estatus como esta: "+nuevaSolicitud.estatus);
                            heredarInformacion=true;
                        }else{
                            myLogger.info("Usuario sucursal....El credito aun esta vigente, utilizar misma solicitud");
                            Notification[] notificaciones = new Notification[1];
                            notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "La consulta de crédito aun esta vigente");
                            request.setAttribute("NOTIFICACIONES", notificaciones);
                            return "/consultaSolicitudesCliente.jsp";
                        }
                        
                    }else{
                        myLogger.debug("Sin Consulta de credito, o consulta mayor a 30 dias");
                        nuevaSolicitud.estatus = ClientesConstants.SOLICITUD_NUEVA;
                        cliente.direcciones = direccionDAO.getDirecciones(cliente.idCliente);
                        if (cliente.direcciones != null) {
                            localidadVO = catalogoDAO.getLocalidad(cliente.direcciones[0].idColonia, cliente.direcciones[0].idLocalidad);
                            if (localidadVO != null) {
                                cliente.direcciones[0].localidad = localidadVO.descripcion;
                            }
                        }
                    }
                }
                
                nuevaSolicitud.idCliente = cliente.idCliente;
                idSolicitud = new SolicitudDAO().addSolicitud(cliente.idCliente, nuevaSolicitud);
                bitutil.registraCambioEstatus(nuevaSolicitud, request);
                bitutil.registraEvento(nuevaSolicitud);

                int n = cliente.solicitudes.length;
                nuevaSolicitud.fechaFirmaConUltimoCambioDoctos = new Date(new java.util.Date().getTime());
                //TCC
                //Se heredan datos
                if(n >= 1 && heredarInformacion)
                {
                    myLogger.info("Hereda Información");
                    EmpleoDAO empleodao = new EmpleoDAO();
                    
                    nuevaSolicitud.fechaFirmaConUltimoCambioDoctos =  com.sicap.clientes.util.SolicitudUtil.DeterminaFechaUltimoCambioEnDocumentacion(cliente);
                    
                    nuevaSolicitud  = com.sicap.clientes.util.SolicitudUtil.heredaDatosDelCicloAnterior(nuevaSolicitud, cliente.solicitudes[n - 1], cliente.idSucursal, nuevaSolicitud.idEjecutivo );
                    int comodin = new SolicitudDAO().updateSolicitud(cliente.idCliente, nuevaSolicitud);
                     
                    EmpleoVO empleoCliente = SolicitudUtil.heredaDatosEmpleoDelCicloAnterior(cliente.idCliente, n);
                    if (empleoCliente != null && empleoCliente.direccion != null) 
                    {
                        empleodao.addEmpleo(null, cliente.idCliente, idSolicitud, empleoCliente);
                        DireccionDAO direcciondao = new DireccionDAO();
                        DireccionVO direccion = empleoCliente.direccion;
                        direcciondao.addDireccion(null, cliente.idCliente, idSolicitud, "d_empleos", 1, direccion);
                        nuevaSolicitud.empleo = empleoCliente;
                    }
                }
                
                SolicitudVO[] solicitudes = new SolicitudVO[n + 1];

                for (int i = 0; i < cliente.solicitudes.length; i++) {
                    solicitudes[i] = cliente.solicitudes[i];
                }

                solicitudes[n] = nuevaSolicitud;
                cliente.solicitudes = solicitudes;

                session.setAttribute("CLIENTE", cliente);
                request.setAttribute("ID_SOLICITUD", new Integer(idSolicitud));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        }
        if (producto == ClientesConstants.CONSUMO || producto == ClientesConstants.VIVIENDA || producto == ClientesConstants.CREDIHOGAR || (producto == ClientesConstants.SELL_FINANCE && usuario.identificador.equals("I"))) {
            return siguienteConsumo;
        } else if (producto == ClientesConstants.SELL_FINANCE && usuario.identificador.equals("E")) {
            return sellFinanceExterno;
        } else if( nuevaSolicitud.estatus == ClientesConstants.SOLICITUD_NUEVA){
            request.setAttribute("idSolicitud", idSolicitud);
            return siguienteCapturaRapida;
        }
        return siguiente;

    }
    
    private SolicitudVO obtenerUltimoDesembolso(ClienteVO cliente, int indiceSolicitud){
        SolicitudVO ultimoDesembolso = null;
        for(int i = indiceSolicitud-1; i>=0; i--){
            if(cliente.solicitudes[i].desembolsado == ClientesConstants.DESEMBOLSADO){
                myLogger.info("indice Ultimo desembolso: "+i);
                myLogger.info("Fecha desembolso: "+cliente.solicitudes[i].fechaDesembolso);
                ultimoDesembolso=cliente.solicitudes[i];
                break;
            }
        }
        return ultimoDesembolso;
    }

}
