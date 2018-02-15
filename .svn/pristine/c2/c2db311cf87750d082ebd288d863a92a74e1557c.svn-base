package com.sicap.clientes.commands;

import com.sicap.clientes.dao.BitacoraSalidaCarteraDAO;
import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FondeadorUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.BitacoraSalidaCarteraVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import java.sql.Connection;
import java.sql.SQLException;
//import com.sun.xml.ws.transport.tcp.io.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class CommandActualizaCarteraGarantia implements Command {
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandActualizaCarteraGarantia.class);

    public CommandActualizaCarteraGarantia(String siguiente) {
        this.siguiente = siguiente;
    }
    
    
    //TODO NOTA: Para mover cartera de bursa primero tiene que pasar a fondeos propios. 11/mayo/2017
    public String execute(HttpServletRequest request) throws CommandException {
        myLogger.info("CommandActualizaCarteraGarantia");
        
        //Validar que no este corriendo
        CatalogoDAO catalogoDAO = new CatalogoDAO();
        SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
        boolean procesandoCierreFond = false;
        boolean procesandoCierreDia = false;
        
        try {
            
            //Ejecucion de procesos de cierre al momento
            procesandoCierreFond = catalogoDAO.ejecutandoCierreFondeadores();
            procesandoCierreDia = catalogoDAO.ejecutandoCierre();
            
            Vector<Notification> notificaciones = new Vector<Notification>();
            
            
            SaldoIBSVO saldo = null;
            
            
            int idEquipo = HTMLHelper.getParameterInt(request, "idEquipo");
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            int idFondeadorNvo=HTMLHelper.getParameterInt(request, "idFondeadorNuevo");
            boolean cumple = false;
            double saldoAcumuladoFondeadorGarantiaAnterior = 0,saldoAcumuladoFondeadorGarantiaNvo = 0;
            
            if(procesandoCierreFond){
                myLogger.debug("El cierre de fondeadores se encuentra en proceso");
                notificaciones.addElement(new Notification(ClientesConstants.INFO_TYPE, "El cierre de fondeadores se encuentra en proceso"));
                request.setAttribute("NOTIFICACIONES", notificaciones);
                return siguiente;
            }else if(procesandoCierreDia){
                myLogger.debug("El cierre de dia se encuentra en proceso");
                notificaciones.addElement(new Notification(ClientesConstants.INFO_TYPE, "El cierre de dia se encuentra en proceso"));
                request.setAttribute("NOTIFICACIONES", notificaciones);
                return siguiente;            
            }
            //Validar si saldo cumple con las reglas del fondeador
            SaldoIBSDAO saldoDao = new SaldoIBSDAO();
            saldo=saldoDao.getSaldos(idEquipo, idCiclo);
            
            myLogger.info("Asignacion manual de cartera");
            myLogger.info(saldo);
            myLogger.info("EQUIPO: "+idEquipo);
            myLogger.info("CICLO: "+idCiclo);
            cumple= FondeadorUtil.isSaldoCandidatoValido(saldo, idFondeadorNvo);
            //TODO insert en transaccion para cumplir conperfil contable
            if(cumple){
                
                int idFondeadorAnterior = saldo.getFondeoGarantia();
                
                //Validar si es de credito real alguno de los saldos en garantia
                
                if(idFondeadorAnterior!= ClientesConstants.ID_FONDEADOR_CREDITO_REAL){
                    saldoAcumuladoFondeadorGarantiaAnterior=Convertidor
                        .stringToDouble(CatalogoHelper.getParametroFondeador("SALDO_GARANTIZADO",idFondeadorAnterior));
                }
                if(idFondeadorNvo != ClientesConstants.ID_FONDEADOR_CREDITO_REAL){
                    saldoAcumuladoFondeadorGarantiaNvo=Convertidor
                        .stringToDouble(CatalogoHelper.getParametroFondeador("SALDO_GARANTIZADO",idFondeadorNvo));
                }
                
                //Para credito real se modifica un poco el proceso
                //Update d_saldos
                saldo.setFondeoGarantia(idFondeadorNvo==ClientesConstants.ID_FONDEADOR_CREDITO_REAL?0:idFondeadorNvo);
                saldoDao.updateFondeoGarantia(saldo);
                               
                //Update a los 2 paramtros en fondeadores de SALDO_GARANTIZADO
                CatalogoDAO catalogoDao= new CatalogoDAO();
                    //Se resta saldo
                String cSaldoAcumuladoFondeadorGarantiaAnterior = new DecimalFormat(ClientesConstants.FORMATO_MONTO)
                        .format( saldoAcumuladoFondeadorGarantiaAnterior-saldo.getSaldoCapital());
                    //Se suma saldo
                String cSaldoAcumuladoFondeadorGarantiaNvo = new DecimalFormat(ClientesConstants.FORMATO_MONTO)
                        .format( saldoAcumuladoFondeadorGarantiaNvo+saldo.getSaldoCapital());
                
                if(idFondeadorNvo!= ClientesConstants.ID_FONDEADOR_CREDITO_REAL){
                    catalogoDao.updateParametroFondeador("SALDO_GARANTIZADO",idFondeadorNvo, cSaldoAcumuladoFondeadorGarantiaNvo);
                }
                if(idFondeadorAnterior != ClientesConstants.ID_FONDEADOR_CREDITO_REAL){
                    catalogoDao.updateParametroFondeador("SALDO_GARANTIZADO",idFondeadorAnterior, cSaldoAcumuladoFondeadorGarantiaAnterior);
                }
                
                //Update bitacora
                BitacoraSalidaCarteraDAO bitacoraSalidaCarteraDAO = new BitacoraSalidaCarteraDAO();
                BitacoraSalidaCarteraVO bitacoraVO = new BitacoraSalidaCarteraVO();
                bitacoraVO.setNumCiclo(idCiclo);
                bitacoraVO.setNumGrupo(idEquipo);
                bitacoraVO.setFondeadorOrigen(idFondeadorAnterior);
                bitacoraVO.setFondeadorDestino(idFondeadorNvo);
                bitacoraVO.setEstatusCartera(ClientesConstants.ESTATUS_FONDEADOR_CARTERA_REASIGNADA);
                bitacoraVO.setUsuario(request.getRemoteUser());
                bitacoraSalidaCarteraDAO.addBitacoraSalida(bitacoraVO);
                
                //Inserta perfiles de entrada y salida PERFIL CONTABLE
                //Se setea en elcampo fondeador para que se inserte en la tabla de transacciones
                //TODO revisar el metodo deinserrcion de Transacccion paraverificarque sirva el seteo del campo de fondeador
                if(idFondeadorNvo==ClientesConstants.ID_FONDEADOR_BURSA ) {
                    //Entra Bursa
                    registraMovimientoCarteraPerfilContable(saldo,ClientesConstants.CARTERA_INGRESO_BURSA,idFondeadorNvo  );
                    
                    //TODO Checar
                    //Actualizar fondeadore otras tres tablas para id BURSA
                     actulizaFondeadorOtrasTablas(saldo, idFondeadorNvo);
                    
                    //Sale Credito Real
                    registraMovimientoCarteraPerfilContable(saldo,ClientesConstants.CARTERA_SALIDA_BURSA, idFondeadorAnterior );
                    
                }else if(idFondeadorAnterior == ClientesConstants.ID_FONDEADOR_BURSA){
                    //Sale Bursa
                    registraMovimientoCarteraPerfilContable(saldo,ClientesConstants.CARTERA_SALIDA_BURSA , idFondeadorAnterior);
                    
                    //TODO
                    //Actulizar fondedaor a Credito Real
                    actulizaFondeadorOtrasTablas(saldo, idFondeadorNvo);
                    
                    //Entra Credito Real
                    registraMovimientoCarteraPerfilContable(saldo,ClientesConstants.CARTERA_INGRESO_BURSA, idFondeadorNvo );
                }
                
                notificaciones.addElement(new Notification(ClientesConstants.INFO_TYPE, "Se actualizo correctamente de id: "
                                                            +idFondeadorAnterior +" a "+idFondeadorNvo));
            
            }else{
                notificaciones.addElement(new Notification(ClientesConstants.ERROR_TYPE, "NO se puede asociar al fondeador con ID: "
                                                            +idFondeadorNvo +" por no cumplir con sus reglas de asignación"));
            }

            
            System.out.println("LISTO");
            
            request.setAttribute("SALDO", saldo);
            request.setAttribute("NOTIFICACIONES", notificaciones);
            
        } catch(ClientesDBException dbe){
            throw new CommandException(dbe.getMessage());
        } catch(Exception e){
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
    
    
    public void registraMovimientoCarteraPerfilContable(SaldoIBSVO saldo, String tipoTrans,int idFondeadorTrans) throws Exception{
        Connection conCart = null;
        try {
            //Insert transaccion de perfil contable
            ArrayList<SaldoIBSVO> lstSaldoPerfil = new ArrayList<SaldoIBSVO>();
            lstSaldoPerfil.add(saldo);
            conCart = ConnectionManager.getCWConnection();
            conCart.setAutoCommit(false);
            //FondeadorUtil.registraPerfilContableCartera(lstSaldoPerfil, conCart, ClientesConstants.CARTERA_FONDEADOR_INGRESO, false);
            //FondeadorUtil.registraPerfilContableCartera(lstSaldoPerfil, conCart, ClientesConstants.CARTERA_FONDEADOR_SALIDA, false);
            FondeadorUtil.registraPerfilContableCartera(lstSaldoPerfil, conCart, tipoTrans, false,idFondeadorTrans);
            conCart.commit();
            
            
        } catch (NamingException ex) {
            java.util.logging.Logger.getLogger(CommandActualizaCarteraGarantia.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(CommandActualizaCarteraGarantia.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            //Cerrar conexion
           
            if (conCart != null) {
                try {
                    conCart.close();
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(CommandActualizaCarteraGarantia.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
    
    }
    
    public void actulizaFondeadorOtrasTablas(SaldoIBSVO saldo, int idFondeador) throws ClientesException{
    
        Connection conCart = null,con = null;
        
        ArrayList<SaldoIBSVO> lstSaldoPerfil = new ArrayList<SaldoIBSVO>();
        lstSaldoPerfil.add(saldo);
        
        try {
            conCart = ConnectionManager.getCWConnection();
            conCart.setAutoCommit(false);
            
            con = ConnectionManager.getMySQLConnection();
            con.setAutoCommit(false);
            
            
            FondeadorUtil.actualizaFondeadorOtrasTablas(lstSaldoPerfil, con, conCart, idFondeador);
            
            
            con.commit();
            conCart.commit();
            
        } catch (NamingException ex) {
            java.util.logging.Logger.getLogger(CommandActualizaCarteraGarantia.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(CommandActualizaCarteraGarantia.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            //Cerrar conexion
           
            if (conCart != null) {
                try {
                    conCart.close();
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(CommandActualizaCarteraGarantia.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(CommandActualizaCarteraGarantia.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            
        }
        
        
        
    
    }
    
}
