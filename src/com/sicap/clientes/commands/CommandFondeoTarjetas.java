package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.TarjetaDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.TarjetaHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.TarjetasVO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class CommandFondeoTarjetas implements Command{
    
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandActualizaCicloApertura.class);
    
    public CommandFondeoTarjetas(String siguiente) {
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
        Date fechaInicio = Convertidor.toSqlDate(new java.util.Date());
        Date fechaFin = Convertidor.toSqlDate(new java.util.Date());
        try {
            int banco = HTMLHelper.getParameterInt(request, "banco");
            int sucursal = HTMLHelper.getParameterInt(request, "sucursal");
            int estatus = HTMLHelper.getParameterInt(request, "estatus");
            ArrayList<CicloGrupalVO> ciclosDis = new ArrayList<CicloGrupalVO>();
            ArrayList<TarjetasVO> arrTarjetasTem = new ArrayList<TarjetasVO>();
            if(request.getParameter("command").equals("buscaTarjetaFondeo")){
                if(!request.getParameter("fechaInicial").equals(""))
                    fechaInicio = Convertidor.stringToSqlDate(request.getParameter("fechaInicial"));
                if(!request.getParameter("fechaFinal").equals(""))
                    fechaFin = Convertidor.stringToSqlDate(request.getParameter("fechaFinal"));
                ciclosDis = new CicloGrupalDAO().getCiclosFechaDispercion(fechaInicio, fechaFin, sucursal);
                for (CicloGrupalVO ciclos : ciclosDis) {
                    arrTarjetasTem = tarjetaDAO.selectFondeoTarjeta(banco, ciclos.getIdGrupo(), ciclos.getIdCiclo(), estatus);
                    for (TarjetasVO tarjeta : arrTarjetasTem) {
                        arrTarjetas.add(new TarjetasVO(tarjeta.getCarga(), tarjeta.getFechaCaptura(), tarjeta.getUsuario(), tarjeta.getBanco(), tarjeta.getTarjeta(), tarjeta.getIdCliente(),
                                tarjeta.getEnvio(), tarjeta.getFechaEnvio(), tarjeta.getArchivo(), tarjeta.getIdSolicitud(), tarjeta.getSucursalVO(), tarjeta.getNombre(),
                                tarjeta.getMonto(), tarjeta.getEstatus(), tarjeta.getGrupo(), tarjeta.getDescEstatus()));
                    }
                }
                if (arrTarjetas.isEmpty()) {
                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "No se encontraron Registros"));
                }
            } else if(request.getParameter("command").equals("generaFondeoTarjetas")){
                arrTarjetas = new TarjetaHelper().getTarjetasFondeo(request);
                /*for (TarjetasVO tarjeta : arrTarjetas) {
                    System.out.println("TARJETA "+tarjeta.getTarjeta()+" "+tarjeta.clienteVO.getIdCliente()+" "+tarjeta.clienteVO.getNombre()+" "+tarjeta.clienteVO.getRfc()+" "+tarjeta.getSucursalVO().getNombre());
                }*/
                switch (banco){
                    case 12://BANAMEX
                        myLogger.debug("Procesando Layout Dispersion BANAMEX");
                        envio = tarjetaDAO.getEnvioFondeoTarjeta(new java.util.Date());
                        file = tarjetaHelper.creaArchivoFondeoBanamex(arrTarjetas, envio, banco);
                        filename = tarjetaHelper.getFileName("TDBanamex", envio);
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
                siguiente = "/generaFondeoTarjeta.jsp";
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
