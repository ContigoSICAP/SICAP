package com.sicap.clientes.commands;

import com.sicap.clientes.dao.TarjetaDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.TarjetaHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.TarjetasVO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class CommandPersonalizacionTarjeta implements Command{
    
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandActualizaCicloApertura.class);
    
    public CommandPersonalizacionTarjeta(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        
        Vector<Notification> notificaciones = new Vector<Notification>();
        ArrayList<TarjetasVO> arrTarjetas = new ArrayList<TarjetasVO>();
        TarjetaDAO tarjetaDAO = new TarjetaDAO();
        boolean paroEnvio = true;
        List file = null;
        String filename = null;
        List saveFileList = null;
        File saveFile;
        TarjetaHelper tarjetaHelper = new TarjetaHelper();
        int envio = 0;
        try {
            int banco = HTMLHelper.getParameterInt(request, "banco");
            int sucursal = HTMLHelper.getParameterInt(request, "sucursal");
            if(request.getParameter("command").equals("buscaClienteTarjeta")){
                arrTarjetas = tarjetaDAO.selectAsignaTarjeta(banco, sucursal);
                /*for (TarjetasVO tarjeta : arrTarjetas) {
                    System.out.println("TARJETA "+tarjeta.getTarjeta()+" "+tarjeta.clienteVO.getIdCliente()+" "+tarjeta.clienteVO.getNombre()+" "+tarjeta.clienteVO.getRfc()+" "+tarjeta.getSucursalVO().getNombre());
                }*/
                if (arrTarjetas.isEmpty()) {
                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "No se encontraron Registros"));
                }
            } else if(request.getParameter("command").equals("generaPersonalizacion")){
                arrTarjetas = new TarjetaHelper().getTarjetas(request);
                /*for (TarjetasVO tarjeta : arrTarjetas) {
                    System.out.println("TARJETA "+tarjeta.getTarjeta()+" "+tarjeta.clienteVO.getIdCliente()+" "+tarjeta.clienteVO.getNombre()+" "+tarjeta.clienteVO.getRfc()+" "+tarjeta.getSucursalVO().getNombre());
                }*/
                switch (banco){
                    case 12://BANAMEX
                        myLogger.debug("Procesando Layout Personalizacion BANAMEX");
                        envio = tarjetaDAO.getEnvioTarjeta(new Date());
                        file = tarjetaHelper.creaArchivoPersonalizacionBanamex(arrTarjetas, envio, banco);
                        filename = tarjetaHelper.getFileName("TPBanamex", envio);
                        break;
                }
                if(file.size() > 0){
                    saveFile = new File(ClientesConstants.RUTA_BASE_ARCHIVOS+"\\EnvioTarjetas\\"+filename);
                    saveFileList = (List)file;
                    FileWriter fWrite = new FileWriter(saveFile);
                    BufferedWriter buffer = new BufferedWriter(fWrite);
                    PrintWriter pWrite = new PrintWriter(buffer);
                    if( !saveFileList.isEmpty() ){
                        Iterator i = file.iterator();
                        while( i.hasNext() ){
                            String linea = (String)i.next();
                            pWrite.write(linea+"\r\n");
                        }
                        pWrite.close();
                    }
                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Layout Generado"));
                } else {
                    paroEnvio = false;
                }
            }
            if(!paroEnvio){
                if (!arrTarjetas.isEmpty())
                    notificaciones.add(new Notification(ClientesConstants.ERROR_LEVEL, "Algunas tarjetas ya fueron procesadas"));
                siguiente = "/generaPersonalizacion.jsp";
                request.setAttribute("TARJETAS", arrTarjetas);
            } else {
                request.setAttribute("TARJETAS", arrTarjetas);
            }

            request.setAttribute("NOTIFICACIONES", notificaciones);
            request.setAttribute("banco", banco);
            request.setAttribute("sucursal", sucursal);
            request.setAttribute("FILE", file);
            request.setAttribute("FILENAME", filename);
        } catch (Exception e) {
            myLogger.error("Error ", e);
        }
        return siguiente;
    }
}
