package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.SeguroHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.SegurosVO;
import com.sicap.clientes.vo.TablaAmortizacionVO;

public class CommandActualizaSeguros extends DAOMaster implements Command {

    private String siguiente;

    public CommandActualizaSeguros(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        Notification notificaciones[] = new Notification[1];
        Connection cn = null;

        /******************
         * 
         *   Actualizar todos los seguros
         *   
         ******/
        try {
            int numgrupo = 0;
            int numciclo = 0;
            int id_cliente = 0;
            int id_solicitud = 0;
            int producto = 0;
            int individuo_o_grupo = 0;
            SegurosDAO irporSeguro = new SegurosDAO();
            SegurosVO seguro = new SegurosVO();

            if (true) {

                cn = getConnection();
                //Connection cn2 = getConnection();
			/*String query = "SELECT * FROM D_SOLICITUDES";
                //String query = "SELECT * FROM D_SOLICITUDES WHERE SO_NUMCLIENTE = 42996 AND SO_NUMSOLICITUD = 1";
                PreparedStatement ps = cn.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                //FSI 	Logger.debug("Ejecutando = "+query);
                int cont = 0;
                if(true){
                while( rs.next()){
                id_cliente = rs.getInt("SO_NUMCLIENTE");
                id_solicitud = rs.getInt("SO_NUMSOLICITUD");
                producto = rs.getInt("SO_NUMOPERACION");
                //					FSI Logger.debug("Obteniendo cliente: "+id_cliente+" solicitud: "+id_solicitud+" y producto: "+producto);
                
                seguro = irporSeguro.getSeguros(id_cliente, id_solicitud);
                
                if(seguro!=null){
                actSeguro(producto,id_cliente, id_solicitud, seguro, numgrupo, numciclo, 1);
                }
                
                notificaciones[0] = new Notification(ClientesConstants.INFO_LEVEL, "Se actualizaron los seguros exitosamente");
                request.setAttribute("NOTIFICACIONES",notificaciones);
                
                cont++;
                //				FSI  Logger.debug("cont: "+cont);
                }  quitar este */


                // AHORA PARA GRUPOSSSSS  

                String query4 = "select * from d_seguros where se_fecha_contratacion is null and se_fecha_vencimiento is null and se_fecha_proximo_vencimiento is null and se_contratacion!=2";
                PreparedStatement ps4 = cn.prepareStatement(query4);
                ResultSet rs4 = ps4.executeQuery();
                int numcliente = 0;
                int numsolicitud = 0;

                while (rs4.next()) {


                    numcliente = rs4.getInt("SE_NUMCLIENTE");
                    numsolicitud = rs4.getInt("SE_NUMSOLICITUD");
                    int id_cliente2 = 0;
                    int id_solicitud2 = 0;
                    int producto2 = 0;
                    //String query2 = "SELECT * FROM D_CICLOS_GRUPALES";
                    //String query2 = "SELECT * FROM d_ciclos_grupales where ci_numgrupo=144 and ci_numciclo=2";
                    //String query2 = "select * from d_seguros where se_fecha_contratacion is null and se_fecha_vencimiento is null and se_fecha_proximo_vencimiento is null and se_contratacion!=2";
                    String query2 = "SELECT * FROM d_integrantes_ciclo where ic_numcliente=" + numcliente + " and ic_numsolicitud=" + numsolicitud + " ";
                    PreparedStatement ps2 = cn.prepareStatement(query2);
                    ResultSet rs2 = ps2.executeQuery();

                    while (rs2.next()) {

                        numgrupo = rs2.getInt("IC_NUMGRUPO");
                        numciclo = rs2.getInt("IC_NUMCICLO");
                        String query3 = "SELECT A.*, B.EN_NOMBRE_COMPLETO, IC_MONTO, C.SO_NUMCHEQUE, C.SO_DESEMBOLSADO FROM"
                                + " D_INTEGRANTES_CICLO A LEFT JOIN D_SOLICITUDES C ON A.IC_NUMCLIENTE = C.SO_NUMCLIENTE AND"
                                + " A.IC_NUMSOLICITUD = C.SO_NUMSOLICITUD, D_CLIENTES B WHERE IC_NUMGRUPO = " + numgrupo + " AND IC_NUMCICLO = " + numciclo + ""
                                + " AND A.IC_NUMCLIENTE = B.EN_NUMCLIENTE ORDER BY A.IC_ROL DESC, IC_MONTO DESC";

                        PreparedStatement ps3 = cn.prepareStatement(query3);
                        ResultSet rs3 = ps3.executeQuery();

                        while (rs3.next()) {
                            id_cliente2 = rs3.getInt("IC_NUMCLIENTE");
                            id_solicitud2 = rs3.getInt("IC_NUMSOLICITUD");
                            producto2 = 3;

                            seguro = irporSeguro.getSeguros(id_cliente2, id_solicitud2);

                            if (seguro != null) {
                                actSeguro(producto2, id_cliente2, id_solicitud2, seguro, numgrupo, numciclo, 2);
                            }

                        }
                    }


                }


            } else {

                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se actualizaron los seguros");
                request.setAttribute("NOTIFICACIONES", notificaciones);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return siguiente;
    }

    public void actSeguro(int producto, int idCliente, int idSolicitud, SegurosVO seguro, int numgrupo, int numciclo, int individuo_o_grupo) throws CommandException {
        //  Connection conn  = null;
        Connection cn2 = null;
        try {

            //SegurosVO seguro = new SegurosVO();

            ClienteDAO irporCliente = new ClienteDAO();
            ClienteVO cliente = new ClienteVO();
            SegurosDAO segurodao = new SegurosDAO();

            SolicitudDAO irporSolicitud = new SolicitudDAO();

            //  Ir por la frecuencia de pago autorizada

            cn2 = getConnection();
            String query = "SELECT * FROM d_decision_comite where de_numcliente = " + idCliente + " and de_numsolicitud = " + idSolicitud + "";
            PreparedStatement ps = cn2.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
//				FSI Logger.debug("Ejecutando = "+query);
            int frecuenciapago = 0;
            if (rs.next()) {
                frecuenciapago = rs.getInt("DE_FRECUENCIAPAGO");
            }

            cliente = irporCliente.getCliente(idCliente);

            int idAmortizacion = 0;
            TablaAmortizacionDAO tablaAmortizacionDAO = new TablaAmortizacionDAO();
            TablaAmortizacionVO[] tablaAmortizacion = null;
            if (producto == 3) {
                idAmortizacion = 1;
                tablaAmortizacion = tablaAmortizacionDAO.getElementos(numgrupo, numciclo, idAmortizacion);
            } else {
                tablaAmortizacion = tablaAmortizacionDAO.getElementos(idCliente, idSolicitud, idAmortizacion);
            }
//				FSI Logger.debug("Amortizacion length: "+tablaAmortizacion.length);			
            //Logger.debug("cliente.solicitudes.length: "+HTMLHelper.displayField(cliente.solicitudes.length));

            //int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);

            if (seguro != null && !(seguro.contratacion == 2)) {

                //Logger.debug("SEGUROes :"+seguro.toString());

                //	 Logger.debug("indiceSolicitud: "+indiceSolicitud);
//	 FSI Logger.debug("frecuenciaPago: "+frecuenciapago);
                //Logger.debug("frecuenciaPago: "+cliente.solicitudes[idSolicitud].decisionComite.frecuenciaPago);
//	 FSI Logger.debug("sumaAsegurada: "+seguro.sumaAsegurada);
//	 FSI Logger.debug("modulos: "+seguro.modulos);	
                seguro.prima = SeguroHelper.getMontoPeriodo(frecuenciapago, seguro.sumaAsegurada, seguro.modulos);

                if (tablaAmortizacion != null && tablaAmortizacion.length != 0) {
                    int plazoTabla = tablaAmortizacion.length;
                    int numeropago = 0;
                    //Posicionarnos en la fecha actual y para adelante en la tabla de amortizacion
                    for (int i = 0; i < tablaAmortizacion.length; i++) {

                        Calendar fechaValor = Calendar.getInstance();
                        Calendar fechaActual = Calendar.getInstance();
                        fechaValor.setTime(tablaAmortizacion[i].fechaPago);
                        fechaValor.set(Calendar.HOUR_OF_DAY, 0);
                        fechaValor.set(Calendar.MINUTE, 0);
                        fechaValor.set(Calendar.SECOND, 0);
                        fechaValor.set(Calendar.MILLISECOND, 0);

                        fechaActual.set(Calendar.HOUR_OF_DAY, 0);
                        fechaActual.set(Calendar.MINUTE, 0);
                        fechaActual.set(Calendar.SECOND, 0);
                        fechaActual.set(Calendar.MILLISECOND, 0);


                        // significa que apenas comienza la amortización
                        //seguro.prima = SeguroHelper.getMontoPeriodo(frecuenciapago, seguro.sumaAsegurada, seguro.modulos);
                        // seguro.saldoInsoluto = seguro.prima * ( (plazoTabla-1) - (numeropago-1));
                        // seguro.saldoActual = seguro.prima; // la prima
                        //seguro.fechaUltimoPago = null; // No se sabe cuándo fue el último pago, acaba de comenzar la amortización  
                        //seguro.estatus = 0; // El proceso diario y mensual actualiza el status 1=vigente 0=no vigente

                        seguro.fechaContratacion = tablaAmortizacion[0].fechaPago;
                        //Logger.debug("fechaContratacion:"+tablaAmortizacion[0].fechaPago);
                        seguro.fechaVencimiento = tablaAmortizacion[plazoTabla - 1].fechaPago; // vencimiento de toodo el seguro?
                        //Logger.debug("fechaVencimiento:"+tablaAmortizacion[plazoTabla-1].fechaPago);
                        //seguro.fechaProximoVencimiento = tablaAmortizacion[numeropago].fechaPago;
                        seguro.fechaProximoVencimiento = new Date(fechaValor.getTimeInMillis());
                        //Logger.debug("fechaProximoVencimiento:"+new Date(fechaValor.getTimeInMillis()));
                        seguro.estatus = 0;
                        seguro.saldoActual = seguro.prima;

                        if (fechaValor.after(fechaActual) || fechaValor.equals(fechaActual)) {
                            seguro.numCuotasTranscurridas = numeropago - 1; // en q numero de pago va
                            //Logger.debug("numCuotasTranscurridas:"+ seguro.numCuotasTranscurridas);
                            //seguro.numCuotasVigentes = 0; // 0 al inicio
                            //seguro.numCuotasVencidas = 0; // va comenzando la amortizacion, no hay cuotas vencidas
                            seguro.numCuotasRestantes = (plazoTabla - 1) - (numeropago - 1);
                            //Logger.debug("numCuotasRestantes:"+seguro.numCuotasRestantes);


                            // este saldo insoluto es para individual
                            if (individuo_o_grupo == 1) {
                                seguro.saldoInsoluto = seguro.prima * ((plazoTabla - 1) - (numeropago - 1));
                            } else if (individuo_o_grupo == 2) {
                                // saldo insoluto para grupal
                                if (numeropago < 2) {
                                    seguro.saldoInsoluto = seguro.prima * 16;
                                } else if (numeropago == 2) {
                                    seguro.saldoInsoluto = seguro.prima * 8;
                                } else if (numeropago > 2) {
                                    seguro.saldoInsoluto = 0;
                                }
                            }
                            //Logger.debug("saldoInsoluto:"+((seguro.prima * ( (plazoTabla-1) - (numeropago-1)))));
                            //Logger.debug("Saldo insoluto: "+seguro.saldoInsoluto);
                            seguro.estatus = 1;
                            break;
                        }
                        numeropago++;
                    }

                } else {
                    Logger.debug("El que sigue, no hay tabla de amortizacion");
                }

                /*Logger.debug("antes del update");
                Logger.debug("seguro.idCliente: "+seguro.idCliente);
                Logger.debug("seguro.idSolicitudf: "+seguro.idSolicitud);
                Logger.debug("seguro.numseguro :"+seguro.numSeguro);
                Logger.debug("asignar a objeto");
                seguro.idCliente = idCliente;
                seguro.idSolicitud = idSolicitud;
                seguro.numSeguro = 1;
                Logger.debug("seguro.idCliente: "+seguro.idCliente);
                Logger.debug("seguro.idSolicitudf: "+seguro.idSolicitud);*/


                segurodao.updateSeguro(seguro);

            } else {
                //Logger.debug("El que sigue, no contrató seguro");	
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
            //throw new CommandException(e.getMessage());
        } finally {
            try {
                if (cn2 != null) {
                    cn2.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new CommandException(e.getMessage());
            }
        }


    }
}
