package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CalificacionAsesoresDAO;
import com.sicap.clientes.dao.EjecutivoCreditoDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CalificacionAsesorVO;
import com.sicap.clientes.vo.EjecutivoCreditoVO;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

public class CommandCalificacionAsesores implements Command {
    
    private String siguiente;
    
    public CommandCalificacionAsesores(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest req) throws CommandException {
        
        Notification mensaje[] = new Notification[1];
        String mesCalif = CatalogoHelper.getParametro("MES_CIERRE_RATING");
        String periodo = "";
        int mes = -1;
        Calendar fechaIni = Calendar.getInstance();
        Calendar fechaFin = Calendar.getInstance();
        Calendar fechaAntIni = Calendar.getInstance();
        Calendar fechaAntFin = Calendar.getInstance();
        Date fechaActual = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strFechaIni = "";
        String strFechaFin = "";
        String strFechaAntIni = "";
        String strFechaAntFin = "";
        BitacoraUtil bitacora = new BitacoraUtil(0, req.getRemoteUser(), "CommandGuardaCarteraCedida");
        Connection con = null;
        Savepoint save = null;
        CalificacionAsesoresDAO calfiDAO = new CalificacionAsesoresDAO();
        boolean error = false;
        int asesor = 0;
        int tipoAsesor = 0;
        int origAsesor = 0;
        int origTipo = 0;
        EjecutivoCreditoDAO asesorDAO = new EjecutivoCreditoDAO();
        int priValor = 0;
        int segValor = 0;
        int terValor = 0;
        int cuaValor = 0;
        int quiValor = 0;
        int sexValor = 0;
        int sepValor = 0;
        int octValor = 0;
        double dpriValor = 0;
        double dsegValor = 0;
        double dterValor = 0;
        boolean inserta = false;
        ArrayList<CalificacionAsesorVO> arrCreditos = null;
        ArrayList<Integer> arrClientes = null;
        ArrayList<Integer> arrClienAnte = null;
        CalificacionAsesorVO seguro = null;
        int grupo = 0;
        
        try {
            mes = obtenNumeroMes(mesCalif);
            fechaFin.set(Calendar.MONTH, mes);
            fechaIni.set(Calendar.MONTH, mes);
            fechaFin.set(Calendar.DATE, fechaFin.getActualMaximum(Calendar.DATE));
            fechaIni.set(Calendar.DATE, fechaIni.getActualMinimum(Calendar.DATE));
            strFechaFin = sdf.format(fechaFin.getTime());
            strFechaIni = sdf.format(fechaIni.getTime());
            periodo = strFechaFin.substring(2, 4)+strFechaFin.substring(5, 7);
            fechaActual = fechaIni.getTime();
            long tiempoActual = fechaActual.getTime();
            long unDia = 1 * 24 * 60 * 60 * 1000;
            fechaActual = new Date(tiempoActual - unDia);
            fechaAntFin.setTime(fechaActual);
            fechaAntIni.setTime(fechaActual);
            fechaAntIni.set(Calendar.DATE, fechaAntIni.getActualMinimum(Calendar.DATE));
            strFechaAntIni = sdf.format(fechaAntIni.getTime());
            strFechaAntFin = sdf.format(fechaAntFin.getTime());
            con = ConnectionManager.getMySQLConnection();
            con.setAutoCommit(false);
            save = con.setSavepoint("BackSaldo");
            arrCreditos = calfiDAO.getGruposAsesor(strFechaFin);
            if(!calfiDAO.insertGruoposTotales(con, save, periodo, strFechaFin))
                error = true;
            if(!calfiDAO.insertGruoposVigentesTotales(con, save, periodo, strFechaFin))
                error = true;
            if(!calfiDAO.insertGruoposTotalesCiclo(con, save, periodo, strFechaFin))
                error = true;
            if(!calfiDAO.insertGruoposVigentesTotalesCiclo(con, save, periodo, strFechaFin))
                error = true;
            if(!calfiDAO.insertClientesInicioMes(con, save, periodo, strFechaAntFin))
                error = true;
            if(!calfiDAO.insertClientesFinMes(con, save, periodo, strFechaFin))
                error = true;
            if(!calfiDAO.insertTotGruposRenovar(con, save, periodo, strFechaAntIni, strFechaAntFin))
                error = true;
            if(!calfiDAO.insertTotGruposRenovados(con, save, periodo, strFechaIni, strFechaFin))
                error = true;
            if(!calfiDAO.insertInicioCartera(con, save, periodo, strFechaAntFin))
                error = true;
            if(!calfiDAO.insertFinCartera(con, save, periodo, strFechaFin))
                error = true;
            if(!calfiDAO.insertMontoDesembolsado(con, save, periodo, strFechaIni,strFechaFin))
                error = true;
            if(!calfiDAO.insertInicioMora(con, save, periodo, strFechaAntFin))
                error = true;
            if(!calfiDAO.insertFinalMora(con, save, periodo, strFechaFin))
                error = true;
            if(!calfiDAO.insertFichasRecuperar(con, save, periodo, strFechaIni, strFechaFin))
                error = true;
            if(!calfiDAO.insertFichasRecuperadas(con, save, periodo, strFechaIni, strFechaFin))
                error = true;
            if(!calfiDAO.insertSegurosInicial(con, save, periodo, strFechaAntFin))
                error = true;
            if(!calfiDAO.insertSegurosFinal(con, save, periodo, strFechaFin))
                error = true;
            for (CalificacionAsesorVO credito : arrCreditos) {
                if(asesor!=credito.getIdAsesor()){
                    if(inserta){
                        calfiDAO.insertRegistroCampo(con, save, periodo, credito.getIdSucursal(), asesor, "gpopropio", priValor);
                        calfiDAO.insertRegistroCampo(con, save, periodo, credito.getIdSucursal(), asesor, "gpoherasesor", segValor);
                        calfiDAO.insertRegistroCampo(con, save, periodo, credito.getIdSucursal(), asesor, "gpoheraperturador", terValor);
                        calfiDAO.insertRegistroCampo(con, save, periodo, credito.getIdSucursal(), asesor, "morapropia", dpriValor);
                        calfiDAO.insertRegistroCampo(con, save, periodo, credito.getIdSucursal(), asesor, "moraheredada", dsegValor);
                        calfiDAO.insertRegistroCampo(con, save, periodo, credito.getIdSucursal(), asesor, "moracedida", dterValor);
                    }
                    priValor = 0;
                    segValor = 0;
                    terValor = 0;
                    dpriValor = 0;
                    dsegValor = 0;
                    dterValor = 0;
                    asesor = credito.getIdAsesor();
                    tipoAsesor = asesorDAO.tipoEjecutivo(asesor);
                }
                inserta = true;
                origAsesor = calfiDAO.getOrigenCredito(credito.getIdCredito());
                if(asesor==origAsesor)
                    priValor++;
                else{
                    origTipo = asesorDAO.tipoEjecutivo(origAsesor);
                    if(origTipo==ClientesConstants.TIPO_EJECUTIVO_ASESOR)
                        segValor++;
                    else if(origTipo==ClientesConstants.TIPO_EJECUTIVO_ASESOR_APERTURADOR)
                        terValor++;
                }
                if(asesor!=origAsesor)
                    dsegValor += credito.getMora();
                else
                    dpriValor += credito.getMora();
            }
            arrCreditos = calfiDAO.getOrigenClientes(strFechaIni, strFechaFin);
            asesor = 0;
            inserta = false;
            for (CalificacionAsesorVO credito : arrCreditos) {
                System.out.println(credito.getIdAsesor()+" "+credito.getIdCredito()+" "+credito.getIdGupo()+" "+credito.getIdCiclo());
                if(asesor!=credito.getIdAsesor()){
                    if(inserta){
                        calfiDAO.insertRegistroCampo(con, save, periodo, credito.getIdSucursal(), asesor, "clientesnuevos", priValor);
                        calfiDAO.insertRegistroCampo(con, save, periodo, credito.getIdSucursal(), asesor, "clientesotrogrupo", segValor);
                        calfiDAO.insertRegistroCampo(con, save, periodo, credito.getIdSucursal(), asesor, "clientesrenovados", terValor);
                        calfiDAO.insertRegistroCampo(con, save, periodo, credito.getIdSucursal(), asesor, "clientesperdidos", cuaValor);
                        calfiDAO.insertRegistroCampo(con, save, periodo, credito.getIdSucursal(), asesor, "gporenovadopropio", quiValor);
                        calfiDAO.insertRegistroCampo(con, save, periodo, credito.getIdSucursal(), asesor, "gporenovadootro", sexValor);
                        calfiDAO.insertRegistroCampo(con, save, periodo, credito.getIdSucursal(), asesor, "seguropropio", sepValor);
                        calfiDAO.insertRegistroCampo(con, save, periodo, credito.getIdSucursal(), asesor, "seguroheredado", octValor);
                    }
                    priValor = 0;
                    segValor = 0;
                    terValor = 0;
                    cuaValor = 0;
                    quiValor = 0;
                    sexValor = 0;
                    sepValor = 0;
                    octValor = 0;
                    asesor = credito.getIdAsesor();
                }
                inserta = true;
                arrClientes = calfiDAO.getIntegrantesGrupo(credito.getIdGupo(), credito.getIdCiclo());
                for (Integer integrante : arrClientes) {
                    grupo = calfiDAO.getOrigenCliente(credito.getIdCredito(), integrante);
                    if(grupo==0)
                        priValor++;
                    else if(grupo!=credito.getIdGupo())
                        segValor++;
                    else if(grupo==credito.getIdGupo())
                        terValor++;
                }
                if((credito.getIdCiclo()-1)!=0){
                    arrClienAnte = calfiDAO.getIntegrantesGrupo(credito.getIdGupo(), credito.getIdCiclo()-1);
                    for (Integer integrante : arrClienAnte) {
                        if(arrClientes.indexOf(integrante)==-1)
                            cuaValor++;
                    }
                    grupo = calfiDAO.getCicloAnterior(credito.getIdGupo(), credito.getIdCiclo()-1);
                    if(grupo == asesor)
                        quiValor++;
                    else
                        sexValor++;
                } else{
                    quiValor++;
                }
                arrClientes = calfiDAO.getIntegrantesSeguro(credito.getIdGupo(), credito.getIdCiclo());
                for (Integer asegurado : arrClientes) {
                    seguro = calfiDAO.getSeguro(credito.getIdCredito(), asegurado);
                    if(seguro==null)
                        sepValor++;
                    else{
                        origAsesor = calfiDAO.getOrigenCredito(seguro.getIdCredito());
                        if(asesor!=origAsesor)
                            sepValor++;
                        else
                            octValor++;
                    }
                }
            }
            
            con.commit();
            if(error)
                mensaje[0] = new Notification(ClientesConstants.ERROR_LEVEL, "ERROR DENTRO DEL PROCESO EN BD");
            else{
                calfiDAO.updateParametro(obtenNombreMes(mes+1));
                mensaje[0] = new Notification(ClientesConstants.INFO_LEVEL, "El mes de "+mesCalif+" fue Calificado");
            }
        } catch (Exception e) {
            try {
                con.rollback(save);
                mensaje[0] = new Notification(ClientesConstants.ERROR_LEVEL, "ERROR DURANTE DEL PROCESO");
                Logger.debug("Problema dentro de CommandCalificacionAsesores");
                e.printStackTrace();
            } catch (SQLException se1) {
                Logger.debug("Problema dentro de Rollback");
                Logger.debug(se1.getMessage());
            }
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se2) {
                Logger.debug("Problema de conexion");
            }
        }
        req.setAttribute("NOTIFICACIONES", mensaje);
        return siguiente;
    }
    
    private int obtenNumeroMes(String strMes){
        
        int numMes = -1;
        if(strMes.equals("ENERO"))
            numMes = 0;
        else if(strMes.equals("FEBRERO"))
            numMes = 1;
        else if(strMes.equals("MARZO"))
            numMes = 2;
        else if(strMes.equals("ABRIL"))
            numMes = 3;
        else if(strMes.equals("MAYO"))
            numMes = 4;
        else if(strMes.equals("JUNIO"))
            numMes = 5;
        else if(strMes.equals("JULIO"))
            numMes = 6;
        else if(strMes.equals("AGOSTO"))
            numMes = 7;
        else if(strMes.equals("SEPTIEMBRE"))
            numMes = 8;
        else if(strMes.equals("OCTUBRE"))
            numMes = 9;
        else if(strMes.equals("NOVIEMBRE"))
            numMes = 10;
        else if(strMes.equals("DICIEMBRE"))
            numMes = 11;
        
        return numMes;
    }
    
    private String obtenNombreMes(int inMes){
        
        String nomMes = "";
        switch (inMes){
            case 12:
                nomMes = "ENERO";
                break;
            case 1:
                nomMes = "FEBRERO";
                break;
            case 2:
                nomMes = "MARZO";
                break;
            case 3:
                nomMes = "ABRIL";
                break;
            case 4:
                nomMes = "MAYO";
                break;
            case 5:
                nomMes = "JUNIO";
                break;
            case 6:
                nomMes = "JULIO";
                break;
            case 7:
                nomMes = "AGOSTO";
                break;
            case 8:
                nomMes = "SEPTIEMBRE";
                break;
            case 9:
                nomMes = "OCTUBRE";
                break;
            case 10:
                nomMes = "NOVIEMBRE";
                break;
            case 11:
                nomMes = "DICIEMBRE";
                break;
        }
        
        return nomMes;
    }
    
}
