/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mantenimiento;

import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.helpers.IntegrantesHelper;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.ClienteVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author Alex
 */
public class AjustaTipoCliente extends DAOMaster{
    
    String sql = "";
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet res = null;
    Statement st = null;
    
    public boolean updateTipoCliente(){
        
        boolean ejecuto = true;
        try {
            int idCliente = 0;
            Date fechaUltCred = null;
            IntegranteCicloDAO inteDAO = new IntegranteCicloDAO();
            IntegrantesHelper inteHelp = new IntegrantesHelper();
            ArrayList<SaldoIBSVO> arrCiclo = getCiclos();
            //System.out.println("arrCiclo "+arrCiclo.size());
            CicloGrupalVO ciclo = new CicloGrupalVO();
            for (SaldoIBSVO saldo : arrCiclo) {
                List arrInteg = getIntegrantes(saldo.getIdClienteSICAP(), saldo.getIdSolicitudSICAP());
                //System.out.println("arrInteg "+arrInteg.size());
                for (Object cliente : arrInteg) {
                    idCliente = (Integer) cliente;
                    fechaUltCred = inteDAO.getTipoIntegrante((Integer) idCliente);
                    ciclo.idGrupo = saldo.getIdClienteSICAP();
                    ciclo.idCiclo = saldo.getIdSolicitudSICAP();
                    //System.out.println("TIPO "+inteHelp.getTipoCliente(idCliente, saldo.getFechaDesembolso(), fechaUltCred));
                    inteDAO.updateTipoIntegrante(ciclo, idCliente, inteHelp.getTipoCliente(idCliente, saldo.getFechaDesembolso(), fechaUltCred));
                }
            }
        } catch (Exception e) {
            ejecuto = false;
        }
        return ejecuto;
    }
    
    public List<ClienteVO> getIntegrantes(int idGrupo, int idCiclo) throws SQLException{
        
        List integrantes = new ArrayList();
        sql = "SELECT ic_numcliente FROM d_integrantes_ciclo WHERE ic_numgrupo=? AND ic_numciclo=?;";
        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            res = ps.executeQuery();
            while (res.next()) {                
                integrantes.add(res.getInt("ic_numcliente"));
            }
        } catch (Exception e) {
            con.close();
        }
        con.close();
        return integrantes;
    }
    
    public ArrayList<SaldoIBSVO> getCiclos() throws SQLException{
        
        ArrayList<SaldoIBSVO> arrCiclo = new ArrayList<SaldoIBSVO>();
        //sql = "SELECT ib_numclientesicap,ib_numsolicitudsicap,ib_fecha_desembolso FROM d_saldos WHERE ib_fecha_desembolso BETWEEN '2013-08-01' AND '2013-08-08';";
        //sql = "SELECT ib_numclientesicap,ib_numsolicitudsicap,ib_fecha_desembolso FROM d_saldos WHERE ib_fecha_desembolso='2013-07-01';";
        sql = "SELECT ib_numclientesicap,ib_numsolicitudsicap,ib_fecha_desembolso FROM d_saldos WHERE ib_credito=60126 and ib_numclientesicap=7112";
        try {
            con = getConnection();
            st = con.createStatement();
            res = st.executeQuery(sql);
            while (res.next()) {                
                arrCiclo.add(new SaldoIBSVO(res.getInt("ib_numclientesicap"), res.getInt("ib_numsolicitudsicap"), res.getDate("ib_fecha_desembolso")));
            }
        } catch (Exception e) {
            con.close();
        }
        con.close();
        return arrCiclo;
    }
}
