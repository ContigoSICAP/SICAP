package com.sicap.clientes.commands;

import com.sicap.clientes.dao.TarjetaDAO;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.TarjetasUtil;
import com.sicap.clientes.vo.TarjetasVO;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.commons.fileupload.*;

public class CommandProcesaCargaLoteTarjetas {
    
    private static Logger myLogger = Logger.getLogger(CommandActualizaCicloApertura.class);
    
    public int procesaCargaLote(FileItem file, HttpServletRequest request) throws Exception {
        
        BitacoraUtil bitacora = new BitacoraUtil(0, request.getRemoteUser(), "CommandProcesaCargaLoteTarjetas");
        int duplicadas = 0;
        TarjetaDAO tarjetaDAO = new TarjetaDAO();
        TarjetasVO tarjetaVO = null;
        String usuario = request.getRemoteUser();
        Object valor = null;
        valor = request.getAttribute("banco");
        int banco = Integer.parseInt(String.valueOf(valor));
        valor = request.getAttribute("loteBanco");
        String loteBanco = String.valueOf(valor);
        String loteSicap = "";
        int llave = 0;
        Connection con = null;
        int resp = 0;
        try {
            con = ConnectionManager.getMySQLConnection();
            con.setAutoCommit(false);
            List lstTarjetas = new TarjetasUtil().procesaArchivoTarjetasBanamex(file);
            bitacora.registraEventoString("banco="+banco+", archivo="+file.getName()+" totalTarjetas="+lstTarjetas.size());
            //List duplicadas = new ArrayList();
            tarjetaVO = new TarjetasVO(null, request.getRemoteUser(), banco, loteBanco, loteSicap, lstTarjetas.size(), file.getName());
            llave = tarjetaDAO.insertDoctoTarjeta(tarjetaVO, con);
            if(llave > 0){
                for (int i = 0; i < lstTarjetas.size(); i++) {
                    TarjetasVO tarjeta = (TarjetasVO)lstTarjetas.get(i);
                    tarjeta.setCarga(llave);
                    tarjeta.setBanco(banco);
                    resp = tarjetaDAO.insertMovtoTarjeta(tarjeta, con);
                    if(resp == 0)
                        duplicadas++;
                }
                if(duplicadas == 0)
                    con.commit();
                else
                    con.rollback();
            }
        } catch (Exception e) {
            myLogger.error("Error ", e);
        } finally{
            if(con!=null){
                con.close();
            }
        }
        return duplicadas;
    }
}
