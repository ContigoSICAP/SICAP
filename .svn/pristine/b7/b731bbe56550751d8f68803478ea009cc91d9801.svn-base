
package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.vo.BitacoraSalidaCarteraVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

public class BitacoraSalidaCarteraDAO  extends DAOMaster {
    
    private static Logger myLogger = Logger.getLogger(BitacoraSalidaCarteraDAO.class);
    
    
    public void addBitacoraSalida(BitacoraSalidaCarteraVO bitacora) 
            throws ClientesException, SQLException{
         Connection con = null; 
        try {
            con = getConnection();
            //con.setAutoCommit(false);
            addBitacoraSalida( bitacora, con);
            //con.commit();
            
        } catch (NamingException ex) {
            java.util.logging.Logger.getLogger(SaldoIBSDAO.class.getName()).log(Level.SEVERE, null, ex);
        }/*finally{
            try{
            
            if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }*/
         
     }
    
    public int addBitacoraSalida(BitacoraSalidaCarteraVO bitacora,Connection con) throws ClientesException {

        String query = "INSERT INTO "
                + " D_BITACORA_SALIDA_CARTERA_GARANTIZADA  (BI_NUM_GRUPO, BI_NUM_CICLO, BI_FONDEADOR_ORIGEN, BI_FONDEADOR_DESTINO, BI_FECHA_SALIDA, BI_ESTATUS_CARTERA, BI_USUARIO) "
                + " VALUES (?, ?, ?, ?, NOW(), ?, ? );";

        
        int param = 1;
        int res = 0;
        
        try {
            if(con==null){
                con= getConnection();
            }
            PreparedStatement ps = null;
            ps = con.prepareStatement(query);
            ps.setInt(param++, bitacora.getNumGrupo());
            ps.setInt(param++, bitacora.getNumCiclo());
            ps.setInt(param++, bitacora.getFondeadorOrigen());
            ps.setInt(param++, bitacora.getFondeadorDestino());
            ps.setInt(param++, bitacora.getEstatusCartera());
            ps.setString(param++, bitacora.getUsuario());
            
            myLogger.debug("Ejecutando = "+query);
            myLogger.debug("Parametros [ " + bitacora.getNumGrupo() + "," + bitacora.getNumCiclo()+ "," + bitacora.getFondeadorOrigen() + "," + bitacora.getFondeadorDestino() + " ]");
            res = ps.executeUpdate();
            //myLogger.debug("Resultado = "+res);
        } catch (SQLException sqle) {
            myLogger.error("addBitacoraSalida", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addBitacoraSalida", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }
    
    /**
     * 
     * @param listaSaldosSalida         Lista de SaldosIBSVO que ser quitaron como garantia de un fondeador
     * @param fondeadorBloqueOrigen     Idetificador del fondeador al cual se garantizaba
     * @param fondeadorBloqueDestino    Idetificador del fondeador al cual se garantiza = 0 seria sin grantia
     * @param con                       Objeto de conexion
     * @throws ClientesException 
     */
    public void addBitacoraSalida(List<SaldoIBSVO> listaSaldosSalida,int fondeadorBloqueOrigen,int fondeadorBloqueDestino,Connection con) throws ClientesException {

        String query = "INSERT INTO "
                + " D_BITACORA_SALIDA_CARTERA_GARANTIZADA  (BI_NUM_GRUPO, BI_NUM_CICLO, BI_FONDEADOR_ORIGEN, BI_FONDEADOR_DESTINO, BI_FECHA_SALIDA,BI_ESTATUS_CARTERA,BI_USUARIO) "
                + " VALUES (?, ?, ?, ?, NOW(),"+ClientesConstants.ESTATUS_FONDEADOR_CARTERA_SALIENTE+",? );";

        
        
        PreparedStatement ps;
            
        
        try {
            if(con==null){
                con= getConnection();
            }
            
            for (SaldoIBSVO saldoSalida : listaSaldosSalida) {
                ps = con.prepareStatement(query); 
                ps.setInt(1, saldoSalida.getIdClienteSICAP());
                ps.setInt(2, saldoSalida.getIdSolicitudSICAP());
                ps.setInt(3, fondeadorBloqueOrigen);
                ps.setInt(4, fondeadorBloqueDestino);
                ps.setString(5, "sistema");
                
                myLogger.debug("Ejecutando = "+query);
                myLogger.debug("Parametros [ " + saldoSalida.getIdClienteSICAP() + "," + saldoSalida.getIdSolicitudSICAP()+ "," + fondeadorBloqueOrigen + "," +  fondeadorBloqueDestino  + " ]");
                ps.executeUpdate();

            }
             
        } catch (SQLException sqle) {
            myLogger.error("addBitacoraSalida(List<SaldoIBSVO>)", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addBitacoraSalida(List<SaldoIBSVO>)", e);
            throw new ClientesException(e.getMessage());
        } 
    }
    
    public void addBitacoraSalidaCartera(List<SaldoIBSVO> listaSaldosSalida,int fondeadorBloqueOrigen,int fondeadorBloqueDestino,int estatusBitacora,String user,Connection con) throws ClientesException {

        String query = "INSERT INTO "
                + " D_BITACORA_SALIDA_CARTERA_GARANTIZADA  "
                + " (BI_NUM_GRUPO, BI_NUM_CICLO, BI_FONDEADOR_ORIGEN, BI_FONDEADOR_DESTINO, BI_FECHA_SALIDA,BI_ESTATUS_CARTERA,BI_USUARIO) "
                + " VALUES (?, ?, ?, ?, NOW(),?,? );";

        
        
        PreparedStatement ps;
            
        
        try {
            if(con==null){
                con= getConnection();
            }
            
            for (SaldoIBSVO saldoSalida : listaSaldosSalida) {
                ps = con.prepareStatement(query); 
                ps.setInt(1, saldoSalida.getIdClienteSICAP());
                ps.setInt(2, saldoSalida.getIdSolicitudSICAP());
                ps.setInt(3, fondeadorBloqueOrigen);
                ps.setInt(4, fondeadorBloqueDestino);
                ps.setInt(5, estatusBitacora);
                ps.setString(6, user);
                
                myLogger.debug("Ejecutando = "+ps.toString());
                ps.executeUpdate();

            }
             
        } catch (SQLException sqle) {
            myLogger.error("addBitacoraSalidaCartera(List<SaldoIBSVO>)", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addBitacoraSalidaCartera(List<SaldoIBSVO>)", e);
            throw new ClientesException(e.getMessage());
        } 
    }
}
