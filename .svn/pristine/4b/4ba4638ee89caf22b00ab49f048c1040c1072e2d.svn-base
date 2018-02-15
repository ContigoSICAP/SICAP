/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.commands;

import com.jspsmart.upload.File;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.MigracionInformacionDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.TablaAmortizacionHelper;
import com.sicap.clientes.helpers.cartera.TablaAmortHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.RFCUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.CicloMigracionVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.ComisionVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.MigracionInformacionVO;
import com.sicap.clientes.vo.ReferenciaGeneralVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.TablaAmortizacionVO;
import com.sicap.clientes.vo.TasaInteresVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;

public class CommandImportacionInformacion {
    
    private MigracionInformacionDAO infoDAO = new MigracionInformacionDAO();
    private int origen = 0;
    
    public boolean proesarInformacion(File myFile, HttpServletRequest request, String ruta) throws ClientesException, Exception{
        
        /*ESTO ES PARA NORMALIZAR A LAOS CLIENTES MEDIANTE EL RFC
        boolean procesado = true;
        return procesado;*/
        boolean procesado = true;
        int numTipo = Integer.parseInt(request.getParameter("tipo").substring(0, 2));
        origen = Integer.parseInt(request.getParameter("origen").substring(0, 2));
        try {
            switch (numTipo){
                case 1:
                    //migraTasas();
                    break;
                case 2:
                    //migraSucursales();
                    break;
                case 3:
                    //migraAsesores();
                    break;
                case 4:
                    migraGrupos();
                    break;
                case 5:
                    migraClientes();
                    break;
                case 6:
                    migraSolicitudes();
                    break;
                case 7:
                    migraCiclos();
                    break;
                case 8:
                    migraIntegrantes();
                    break;
                case 9:
                    //migraPagos();
                    break;
                case 10:
                    migraSaldos();
                    break;
                case 11:
                    //migraNegocio();
                    break;
                case 12:
                    calculaRFC();
                    break;
                case 13:
                    migraSaldosDesembolso();
                    break;
            }
        } catch (Exception e) {
            Logger.debug("Expecion dentro de CommandImportacionInformacion "+e);
        }
        
        return procesado;
    }
    
    private void migraClientes() throws ClientesException{
        
        System.out.println("Clientes");
        int idTabla = infoDAO.findID("en_numcliente", "d_clientes");
        System.out.println("idTabla "+idTabla);
        String query = "";
        //int idMigraDupl = 0;
        int idCliente = 0;
        int sucMigra = 0, estado = 0, municipio = 0, conteoRFC = 0;
        String rfc = "";
        MigracionInformacionVO bean = null;
        int conteo = 0;
        ArrayList<ClienteVO> listClientes = new ArrayList<ClienteVO>();
        ClienteVO clienteDupl = new ClienteVO();
        do{
            listClientes = infoDAO.getArrClientesLimpio(conteo, origen);
            if(!listClientes.isEmpty()){
                for (ClienteVO cliente : listClientes) {
                    query = "INSERT INTO d_clientes(en_estatus,en_entidad_nac,en_fecha_captura,en_fecharegistro_ibs,en_numgrupo,en_nivel_estudios,en_curp,en_email,en_numcliente,en_numsucursal,en_rfc,"+
                            "en_nombre,en_primer_apellido,en_segundo_apellido,en_nombre_completo,en_fecha_nac,en_sexo,en_nacionalidad,en_tipo_id,en_numero_identificacion,en_edo_civil,"+
                            "en_dependientes_economicos,en_id_migracion,en_origenmigracion) VALUES";
                    //rfc = stringCellValue;
                    idCliente = infoDAO.findClientes(cliente.getNombre().replace(" ", "%"), cliente.getaPaterno(), cliente.getaMaterno(), cliente.getFechaNacimiento()+"");
                    //idCliente = 0;
                    if(idCliente != 0){
                        //idMigraDupl = infoDAO.findIdClienteDupli(idCliente);
                        clienteDupl = infoDAO.findIdClienteDupli(idCliente);
                        //if(idMigraDupl != 0){
                        if(clienteDupl.getIdMigracion() != 0){
                            if(clienteDupl.getOrigenMigracion() == 1)
                                infoDAO.updateIdMigracion("UPDATE d_clientes SET en_id_migracion="+cliente.getIdCliente()+",en_origenmigracion=2 WHERE en_numcliente='"+idCliente+"';");
                            else
                                System.out.println("***********************Cliente: "+idCliente+"_"+cliente.getNombre()+" "+cliente.getaPaterno()+" "+cliente.getaMaterno()+" "+cliente.getFechaNacimiento()+" 1°ID "+clienteDupl.getIdMigracion()+" 2°ID "+cliente.getIdCliente());
                        }else{
                            infoDAO.updateIdMigracion("UPDATE d_clientes SET en_id_migracion="+cliente.getIdCliente()+",en_origenmigracion="+origen+" WHERE en_numcliente='"+idCliente+"';");
                        }
                    } else {
                        idTabla++;
                        query += "(1,0,CONCAT(CURDATE(),' ',CURTIME()),'0000-00-00 00:00:00',0,0,'','',";
                        rfc = cliente.getRfc();
                        if(infoDAO.findRFC(rfc.substring(0, 10)) && rfc.length() == 10){
                            rfc +="_01";
                            if(infoDAO.findRFC(rfc)){
                                conteoRFC = Integer.parseInt(rfc.substring(11, 13));
                                conteoRFC++;
                                rfc = rfc.substring(0, 10)+"_"+FormatUtil.completaCadena(conteoRFC+"", '0', 2, "L");
                            }
                        }
                        query += ""+idTabla+","+infoDAO.findIdSucursal(cliente.getSucursal(), origen) +",'"+rfc+"','"+cliente.getNombre()+"','"+cliente.getaPaterno()+"','"+cliente.getaMaterno()+"','"+cliente.getNombre()+" "+cliente.getaPaterno()+" "+cliente.getaMaterno()+"','"+cliente.getFechaNacimiento()+"',";
                        if(cliente.getTipoSexo().equals("Femenino") || cliente.getTipoSexo().equals("F"))
                            query += "1,1,";
                        else if(cliente.getTipoSexo().equals("Masculino") || cliente.getTipoSexo().equals("M"))
                            query += "2,1,";
                        else
                            query += "0,1,";
                        if(cliente.getTipoDoc().equals("IFE"))
                            query += "1,'"+cliente.getNumeroIdentificacion()+"',";
                        else if(cliente.getTipoDoc().equals("Otro"))
                            query += "3,'"+cliente.getNumeroIdentificacion()+"',";
                        else
                            query += "0,'"+cliente.getNumeroIdentificacion()+"',";
                        if(cliente.getTipoEdoCivil().equals("Casado") || cliente.getTipoEdoCivil().equals("Casada"))
                            query += "1,";
                        else if(cliente.getTipoEdoCivil().equals("Divorciado(a)") || cliente.getTipoEdoCivil().equals("Divorciada"))
                            query += "4,";
                        else if(cliente.getTipoEdoCivil().equals("Soltero") || cliente.getTipoEdoCivil().equals("Soltera"))
                            query += "2,";
                        else if(cliente.getTipoEdoCivil().equals("Union Libre"))
                            query += "3,";
                        else if(cliente.getTipoEdoCivil().equals("Viudo(a)") || cliente.getTipoEdoCivil().equals("Viuda"))
                            query += "5,";
                        else
                            query += "0,";
                        if(cliente.getDependientesEconomicos() == 0)
                            query += "1,"+cliente.getIdCliente()+","+origen+");";
                        else if(cliente.getDependientesEconomicos() == 1)
                            query += "2,"+cliente.getIdCliente()+","+origen+");";
                        else if(cliente.getDependientesEconomicos() == 2)
                            query += "3,"+cliente.getIdCliente()+","+origen+");";
                        else if(cliente.getDependientesEconomicos() > 2)
                            query += "4,"+cliente.getIdCliente()+","+origen+");";
                        infoDAO.insertRegistros(query);
                        query = "INSERT INTO d_direcciones(di_numsolicitud,di_tabla,di_indice_tabla,di_numdireccion,di_numlocalidad,di_numcliente,di_calle,di_numero_ext,di_numero_int,di_numcolonia,di_situacion_vivienda,di_ant_domicilio) VALUES";
                        query += "(0,'d_clientes',1,1,0,";
                        query += idTabla+",'"+cliente.getCalle()+"','"+cliente.getNumext().replace("  ", " ")+"','"+cliente.getNumint().replace("  ", " ")+"',";
                        estado = obtenNumeroEstado(cliente.getEstado());
                        municipio = infoDAO.findMunicipio(estado, cliente.getMunicipio());
                        if(municipio != 0)
                            bean = infoDAO.findColonia(estado, municipio, cliente.getColonia(), true);
                        else
                            bean = infoDAO.findColonia(estado, municipio, cliente.getColonia(), false);
                        query += bean.id+",";
                        if(cliente.getSituacionViv().equals("Propia"))
                            query += "1,";
                        else if (cliente.getSituacionViv().equals("Renta"))
                            query += "2,";
                        else if (cliente.getSituacionViv().equals("Familiar") || cliente.getSituacionViv().equals("Familia"))
                            query += "3,";
                        else if (cliente.getSituacionViv().equals("Otra") || cliente.getSituacionViv().equals("Otros") || cliente.getSituacionViv().equals("Hipotecada"))
                            query += "5,";
                        else
                            query += "0,";
                        query += "'0/"+cliente.getAntiguedadViv()+"');";
                        infoDAO.insertRegistros(query);
                        query = "INSERT INTO d_telefonos(te_numdireccion,te_numtelefono,te_tipotelefono,te_numcliente,te_telefono) VALUES";
                        query += "(1,1,1,"+idTabla+",'"+cliente.getTelefono()+"')";
                        infoDAO.insertRegistros(query);
                    }
                }
            }
            conteo+=500;
        } while(listClientes.size() != 0);
    }
    
    private void migraSolicitudes() throws ClientesException, Exception{
        
        System.out.println("Solicitudes");
        String query = "";
        int idCliente = 0, idSolcitud = 0;;
        int conteo = 0;
        ArrayList<SolicitudVO> listSolicitudes = new ArrayList<SolicitudVO>();
        do{
            listSolicitudes = infoDAO.getArrSolicitudesLimpio(conteo, origen);
            if(!listSolicitudes.isEmpty()){
                for (SolicitudVO solicitud : listSolicitudes) {
                    idCliente = infoDAO.findIdCliente(solicitud.getIdCliente(), origen);
                    if(infoDAO.findIdSolicitud(idCliente, solicitud.getIdSolicitud(), origen)==0){
                        query = "INSERT INTO d_solicitudes(so_numcredito_ibs,so_numcuenta_ibs,so_estatus,so_numsucursal,so_numoperacion,so_reestructura,so_solicitud_reestructura,so_nummedio,so_numejecutivo,so_fuente,so_plazo_solicitado,so_frecpago_solicitada,so_destino_credito,so_monto_propuesto,so_plazo_propuesto,so_frecpago_propuesta,so_cuota,so_desembolsado,so_tasa_calculada,so_numcheque,so_numcliente,so_numsolicitud,so_id_migracion,so_fecha_firma,so_monto_solicitado,so_fecha_captura, so_origenmigracion) VALUES";
                        query += "(0,0,2,0,3,0,0,0,0,0,16,3,1,0,0,0,0,2,0,0,";
                        query += idCliente+",";
                        idSolcitud = infoDAO.findSolicitud(idCliente);
                        idSolcitud = idSolcitud+1;
                        query += idSolcitud+","+solicitud.getIdSolicitud()+",";
                        query += "'"+Convertidor.dateToString(solicitud.getFechaFirma(), "yyyy-mm-dd")+"',";
                        query += solicitud.getMontoSolicitado()+",CONCAT(CURDATE(),' ',CURTIME()),"+origen+")";
                        try {
                            infoDAO.insertRegistros(query);
                        } catch (Exception e) {
                            System.out.println("**********************Problema "+solicitud.getIdCliente());
                        }
                    } else{
                        System.out.println("Ya existe "+solicitud.getIdCliente()+" "+solicitud.getIdSolicitud());
                    }
                }
            }
            conteo+=500;
        } while(listSolicitudes.size() != 0);
    }
    
    private void migraGrupos() throws ClientesException, Exception{
        
        System.out.println("Grupos");
        String query = "";
        String rfc = "";
        int idSucursal = 0;
        int conteo = 0;
        int idTabla = infoDAO.findID("gr_numgrupo", "d_grupos");
        ArrayList<GrupoVO> listGrupo = new ArrayList<GrupoVO>();
        do{
            listGrupo = infoDAO.getArrGruposLimpio(conteo, origen);
            if(!listGrupo.isEmpty()){
                for (GrupoVO grupo : listGrupo) {
                    idTabla++;
                    query = "INSERT INTO d_grupos(gr_numgrupo_ibs,gr_estatus,gr_numoperacion,gr_calificacion,gr_numgrupo_original,gr_numciclo_original,gr_fecha_formacion,gr_fecha_captura,gr_fecharegistro_ibs,gr_numsucursal,gr_numgrupo,gr_nombre,gr_id_migracion,gr_rfc,gr_origenmigracion) VALUES";
                    query += "(0,1,3,'A',0,0,CURDATE(),CONCAT(CURDATE(),' ',CURTIME()),'0000-00-00 00:00:00',";
                    idSucursal = infoDAO.findIdSucursal(grupo.getSucursal(), origen);
                    query += idSucursal+",";
                    if(origen == 1){
                        if(grupo.getSucursal()==143 || grupo.getSucursal()==404 || grupo.getSucursal()==137 || grupo.getSucursal()==160 || grupo.getSucursal()==407){
                            query += idTabla+",'"+grupo.getNombre()+" CR',";
                            /*if(infoDAO.findGrupo(grupo.getNombre().replace(" ", "%"), idSucursal) != 0){
                                query += idTabla+",'"+grupo.getNombre()+" CR',";
                            }else {
                                query += idTabla+",'"+grupo.getNombre()+"',";
                            }*/
                        } else {
                            query += idTabla+",'"+grupo.getNombre()+"',";
                        }
                    } else if(origen == 2){
                        if(grupo.getSucursal() == 0){//PONER LAS SUCURSALES DE FUSION
                            query += idTabla+",'"+grupo.getNombre()+" CD',";
                        } else{
                            query += idTabla+",'"+grupo.getNombre()+"',";
                        }
                    }
                    rfc = grupo.getNombre().substring(0, 2);
                    query += "'"+grupo.getRefGrupo()+"','G"+FormatUtil.completaCadena(idTabla+"", '0', 6, "L")+FormatUtil.completaCadena(idSucursal+"", '0', 3, "L")+rfc+"',"+origen+")";
                    try {
                        infoDAO.insertRegistros(query);
                    } catch (Exception e) {
                        Logger.debug("**************GRUPO DUPLICADO "+grupo.getNombre()+" "+grupo.getRefGrupo());
                    }
                }
            }
            conteo+=500;
        } while(listGrupo.size() != 0);
    }
    
    private void migraCiclos() throws ClientesException, Exception{
        
        System.out.println("Ciclos Grupales");
        String query = "", query1 = "";
        int conteo = 0;
        int idGrupo = 0, numCiclo = 0, idTabla = 0, estado = 0, municipio = 0, idSucursal = 0;
        ArrayList<CicloMigracionVO> listCiclos = new ArrayList<CicloMigracionVO>();
        GrupoVO grupoVO = new GrupoVO();
        MigracionInformacionVO bean = null;
        do{
            listCiclos = infoDAO.getArrCiclosLimpio(conteo, origen);
            if(!listCiclos.isEmpty()){
                for (CicloMigracionVO ciclo : listCiclos) {
                    try {
                        query = "INSERT INTO d_ciclos_grupales(ci_tipo_ciclo,ci_numcredito_ibs,ci_numcuenta_ibs,ci_estatus,ci_desembolsado,ci_coordinador,ci_multa_retraso,ci_multa_falta,ci_tasa,ci_monto_refinanciado,ci_estatus_revision_monitor,ci_conseguro,ci_numdireccion_alterna,ci_numgrupo,ci_numciclo,ci_dia_reunion,ci_hora_reunion,ci_ejecutivo,ci_monto,ci_monto_con_comision,ci_fecha_captura,ci_plazo,ci_numdireccion_reunion,ci_id_migracion,ci_origenmigracion) VALUES";
                        query += "(1,0,0,1,3,0,0,0,";
                        grupoVO = infoDAO.getNombreGrupoCR(ciclo.getReferencia(), origen);
                        idSucursal = infoDAO.findIdSucursal(grupoVO.getSucursal(), origen);
                        //PONER LAS SUCURSALES DE FUSION
                        if(origen == 1){
                            if(grupoVO.getSucursal()==143 || grupoVO.getSucursal()==404 || grupoVO.getSucursal()==137 || grupoVO.getSucursal()==160 || grupoVO.getSucursal()==407){
                                idGrupo = infoDAO.findGrupo(grupoVO.getNombre()+" CR", idSucursal);
                            }else{
                                idGrupo = infoDAO.findGrupo(grupoVO.getNombre(), idSucursal);
                            }
                            query += "17,";
                        } else if(origen == 2){
                            idGrupo = infoDAO.findIdGrupo(grupoVO.getIdGrupoOriginal(), origen);
                            if(ciclo.getTasa() == 3)//PROXIMA 3.075
                                query += "14,";
                            else if(ciclo.getTasa() == 3.5)
                                query += "11,";
                            else if(ciclo.getTasa() == 4)
                                query += "16,";
                            else if(ciclo.getTasa() == 4.5)
                                query += "1,";
                            else if(ciclo.getTasa() == 5)
                                query += "5,";
                            else if(ciclo.getTasa() == 5.5)
                                query += "2,";
                            else
                                query += "17,";
                        }
                        query += "0,0,0,0,";
                        if(idGrupo != 0){
                            numCiclo = infoDAO.findCiclo(idGrupo);
                            numCiclo++;
                            query += idGrupo+","+numCiclo+",";
                            if(ciclo.getDiaReunion().equals("Lunes"))
                                query += 1+",";
                            else if(ciclo.getDiaReunion().equals("Martes"))
                                query += 2+",";
                            else if(ciclo.getDiaReunion().equals("Miercoles"))
                                query += 3+",";
                            else if(ciclo.getDiaReunion().equals("Jueves"))
                                query += 4+",";
                            else if(ciclo.getDiaReunion().equals("Viernes"))
                                query += 5+",";
                            else
                                query += 0+",";
                            if(ciclo.getHoraReunion().equals("Lunes"))
                                query += 1+",";
                            else if(ciclo.getHoraReunion().equals("Martes"))
                                query += 2+",";
                            else if(ciclo.getHoraReunion().equals("Miercoles"))
                                query += 3+",";
                            else if(ciclo.getHoraReunion().equals("Jueves"))
                                query += 4+",";
                            else if(ciclo.getHoraReunion().equals("Viernes"))
                                query += 5+",";
                            else
                                query += 0+",";
                            query += infoDAO.findIdEjecutivo(ciclo.getEjecutivo(), origen)+",";
                            query += ciclo.getCapital()+","+ciclo.getCapital()+",";
                            query += "CONCAT('"+ciclo.getFechaCaptura()+"',' ','23:59:59'),";
                            query += ciclo.getPlazo()+",";
                            idTabla = infoDAO.findID("dg_numdireccion", "d_direcciones_genericas");
                            idTabla += 1;
                            if(origen == 1)
                                query += idTabla+",'"+ciclo.getReferencia()+"',"+origen+")";
                            else if(origen == 2)
                                query += idTabla+",'"+ciclo.getNumCiclo()+"',"+origen+")";
                            query1 = "INSERT INTO d_direcciones_genericas(dg_numdireccion,dg_calle,dg_numero_ext,dg_numero_int,dg_fecha_captura,dg_numcolonia) VALUES("+idTabla+",'"+ciclo.getCalle()+"',";
                            query1 += "'"+ciclo.getNumExt()+"',";
                            query1 += "'"+ciclo.getNumInt()+"',CONCAT(CURDATE(),' ',CURTIME()),";
                            estado = obtenNumeroEstado(ciclo.getEstado());
                            municipio = infoDAO.findMunicipio(estado, ciclo.getMunicipio());
                            if(municipio != 0)
                                bean = infoDAO.findColonia(estado, municipio, ciclo.getColonia(), true);
                            else
                                bean = infoDAO.findColonia(estado, municipio, ciclo.getColonia(), false);
                            query1 += bean.id+")";
                            try {
                                infoDAO.insertRegistros(query1);
                                infoDAO.insertRegistros(query);
                                if(origen == 2){
                                    if(ciclo.getTasa() == 3)//PROXIMA 3.075
                                        infoDAO.updateIdMigracion("UPDATE d_grupos SET gr_calificacion='TTT' WHERE gr_numgrupo='"+idGrupo+"';");
                                    else if(ciclo.getTasa() == 3.5)
                                        infoDAO.updateIdMigracion("UPDATE d_grupos SET gr_calificacion='AAA' WHERE gr_numgrupo='"+idGrupo+"';");
                                    else if(ciclo.getTasa() == 4)
                                        infoDAO.updateIdMigracion("UPDATE d_grupos SET gr_calificacion='T' WHERE gr_numgrupo='"+idGrupo+"';");
                                    else if(ciclo.getTasa() == 4.5)
                                        infoDAO.updateIdMigracion("UPDATE d_grupos SET gr_calificacion='AA' WHERE gr_numgrupo='"+idGrupo+"';");
                                    else if(ciclo.getTasa() == 5)
                                        infoDAO.updateIdMigracion("UPDATE d_grupos SET gr_calificacion='A' WHERE gr_numgrupo='"+idGrupo+"';");
                                    else if(ciclo.getTasa() == 5.5)
                                        infoDAO.updateIdMigracion("UPDATE d_grupos SET gr_calificacion='RG' WHERE gr_numgrupo='"+idGrupo+"';");
                                    else
                                        infoDAO.updateIdMigracion("UPDATE d_grupos SET gr_calificacion='A1' WHERE gr_numgrupo='"+idGrupo+"';");
                                }
                            } catch (Exception e) {
                                Logger.debug("**************CICLO "+ciclo.getReferencia());
                            }
                        } else
                            Logger.debug("************** SIN GRUPO "+ciclo.getReferencia());
                    } catch (Exception e) {
                        System.out.println("************** SIN GRUPO "+ciclo.getReferencia());
                    }
                }
            }
            conteo+=500;
        } while(listCiclos.size() != 0);
    }
    
    private void migraIntegrantes() throws ClientesException, Exception{
        
        System.out.println("Integrantes Grupales");
        String query = "", query1 = "", query2 = "";
        int conteo = 0;
        int idCliente = 0, idSolicitud = 0, idGrupo = 0, idSucursal = 0, idCiclo = 0;
        ArrayList<IntegranteCicloVO> listIntegrantes = new ArrayList<IntegranteCicloVO>();
        GrupoVO grupoVO = new GrupoVO();
        MigracionInformacionVO bean = null;
        do{
            listIntegrantes = infoDAO.getArrIntegrantesLimpio(conteo, origen);
            if(!listIntegrantes.isEmpty()){
                for (IntegranteCicloVO integrante : listIntegrantes) {
                    query = "INSERT INTO d_integrantes_ciclo(ic_monto_refinanciar,ic_comision,ic_estatus,ic_calificacion,ic_rol,ic_numgrupo,ic_numciclo,ic_numcliente,ic_numsolicitud,ic_monto,ic_fecha_captura) VALUES";
                    query1 = "INSERT INTO d_decision_comite(de_decision_comite,de_causarechazo,de_motivorechazocliente,de_montorefinanciado,de_plazoautorizado,de_comision,de_tasa,de_frecuenciapago,de_motivocondicionamiento,de_multa,de_detallemotivorechazocliente,de_comentarioscomite,de_numcliente,de_numsolicitud,de_montoautorizado,de_fecharealizacion,de_fechacaptura) VALUES";
                    query2 = "INSERT INTO d_ordenes_de_pago(pg_identificador,pg_monto,pg_fecha_envio,pg_fecha_cancelacion,pg_banco,pg_estatus,pg_envio,pg_usuario,pg_numcliente,pg_numsucursal,pg_referencia,pg_nombre_cliente,pg_numsolicitud,pg_fecha_captura) VALUES";
                    
                    query += "(0,1,0,1,0,";
                    query1 += "(1,0,0,0,16,1,17,3,0,0,'','',";
                    query2 += "(0,0,CONCAT(CURDATE(),' ',CURTIME()),CONCAT(CURDATE(),' ',CURTIME()),0,2,0,'sistema',";
                    try {
                        idCliente = infoDAO.findIdCliente(integrante.getIdCliente(), origen);
                        idSolicitud = infoDAO.findIdSolicitud(idCliente, integrante.getIdSolicitud(), origen);
                        grupoVO = infoDAO.getNombreGrupoCR(integrante.getGrupo(), origen);
                        idSucursal = infoDAO.findIdSucursal(grupoVO.getSucursal(), origen);
                        if(origen == 1){
                            if(idSucursal==7 || idSucursal==8 || idSucursal==21 || idSucursal==22 || idSucursal==23){
                                idGrupo = infoDAO.findGrupo(grupoVO.getNombre()+" CR", idSucursal);
                            }else{
                                idGrupo = infoDAO.findGrupo(grupoVO.getNombre(), idSucursal);
                            }
                            idCiclo = infoDAO.findNumCiclo(idGrupo, integrante.getGrupo(), origen);
                        } else if(origen == 2){
                            idGrupo = infoDAO.findIdGrupo(grupoVO.getIdGrupoOriginal(), origen);
                            idCiclo = infoDAO.findNumCiclo(idGrupo, String.valueOf(integrante.getIdCiclo()), origen);
                        }
                        bean = infoDAO.findInfoCliente(idCliente);
                        if(idCliente!=0){
                            query += idGrupo+","+idCiclo+","+idCliente+","+idSolicitud+","+integrante.getMonto()+",CONCAT('"+integrante.getFecha()+"',' ','23:59:59'));";
                            query1 += idCliente+","+idSolicitud+","+integrante.getMonto()+",'"+integrante.getFecha()+"',CONCAT('"+integrante.getFecha()+"',' ','23:59:59'));";
                            query2 += idCliente+","+bean.getId()+",'"+FormatUtil.completaCadena(bean.getId()+"", '0', 3, "L")+FormatUtil.completaCadena(idCliente+"", '0', 7, "L")+FormatUtil.completaCadena(idSolicitud+"", '0', 2, "L")+"9','"+bean.getCampo()+"',";
                            query2 += idSolicitud+",CONCAT('"+integrante.getFecha()+"',' ','23:59:59'));";
                            try {
                                infoDAO.insertRegistros(query);
                                infoDAO.insertRegistros(query1);
                                infoDAO.insertRegistros(query2);
                            } catch (Exception e) {
                                System.out.println("*************** ERROR DE INSERCCION");
                            }
                        } else
                            System.out.println("*************** SIN NUMERO DE CLIENTE "+integrante.getIdCliente()+" "+integrante.getIdSolicitud());   
                    } catch (Exception e) {
                        System.out.println("*************** PROBLEMA CON "+integrante.getIdCliente()+" "+integrante.getIdSolicitud()+" "+integrante.getGrupo());
                    }
                }
            }
            conteo+=500;
        } while(listIntegrantes.size() != 0);
    }
    
    private void migraSaldos() throws ClientesException, Exception{
        
        System.out.println("Saldos");
        String query = "", query1 = "", query2 = "";
        int conteo = 0;
        int inserta = 0;
        int idTabla = infoDAO.findID("ib_credito", "d_saldos");
        int idGrupo = 0, idCiclo = 0, idSucursal = 0, idAsesor = 0, plazo = 0;
        double tasaAnual = 0.0, tasaMenusal = 0.0, pagoFijo = 0.0, capital = 0.0;
        Date fechaDis = new Date();
        String referencia = "";
        ReferenciaGeneralDAO refeDAO = new ReferenciaGeneralDAO();
        ReferenciaGeneralVO pagoReferenciaVO = new ReferenciaGeneralVO();
        Double tasaLogaritmo = 0.0, tasaCalculada = 0.0;
        GrupoVO grupoVO = new GrupoVO();
        CicloGrupalVO cicloVO = new CicloGrupalVO();
        GrupoDAO grupoDAO = new GrupoDAO();
        CicloGrupalDAO cicloDAO = new CicloGrupalDAO();
        IntegranteCicloDAO integranteDAO = new IntegranteCicloDAO();
        TablaAmortizacionDAO tablaDAO = new TablaAmortizacionDAO();
        CreditoCartVO creditoVO = new CreditoCartVO();
        ArrayList<CicloMigracionVO> listSaldos = new ArrayList<CicloMigracionVO>();
        CreditoCartDAO creditoDAO = new CreditoCartDAO();
        SaldoIBSVO saldoVO = new SaldoIBSVO();
        SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
        do{
            listSaldos = infoDAO.getArrSaldosLimpio(conteo, origen);
            if(!listSaldos.isEmpty()){
                for (CicloMigracionVO saldo : listSaldos) {
                    try {
                        grupoVO = infoDAO.getNombreGrupoCR(saldo.getReferencia(), origen);
                        idSucursal = infoDAO.findIdSucursal(grupoVO.getSucursal(), origen);
                        if(origen == 1){
                            if(idSucursal==7 || idSucursal==8 || idSucursal==21 || idSucursal==22 || idSucursal==23){
                                idGrupo = infoDAO.findGrupo(grupoVO.getNombre()+" CR", idSucursal);
                            }else{
                                idGrupo = infoDAO.findGrupo(grupoVO.getNombre(), idSucursal);
                            }
                            idCiclo = infoDAO.findNumCiclo(idGrupo, saldo.getReferencia(), origen);
                        } else if(origen == 2){
                            idGrupo = infoDAO.findIdGrupo(grupoVO.getIdGrupoOriginal(), origen);
                            idCiclo = infoDAO.findNumCiclo(idGrupo, String.valueOf(saldo.getNumCiclo()), origen);
                        }
                        //idSucursal = infoDAO.findNumSucursal(idGrupo);
                        idAsesor = infoDAO.findIdEjecutivo(saldo.getEjecutivo(), origen);
                        if(origen == 1)
                            tasaAnual = 62.086807424;
                        else if(origen == 2){
                            if(saldo.getTasa() == 3)//PROXIMA 3.075
                                tasaAnual = 34.0825;
                            else if(saldo.getTasa() == 3.5)
                                tasaAnual = 38.7931;
                            else if(saldo.getTasa() == 4)
                                tasaAnual = 44.3349;
                            else if(saldo.getTasa() == 4.5)
                                tasaAnual = 49.8768;
                            else if(saldo.getTasa() == 5)
                                tasaAnual = 55.4187;
                            else if(saldo.getTasa() == 5.5)
                                tasaAnual = 60.9606;
                            else
                                tasaAnual = 65.9483;
                        }
                        capital = saldo.getSaldo();
                        pagoFijo = saldo.getCuota();
                        fechaDis = saldo.getFechaCaptura();
                        plazo = saldo.getPlazo();
                        referencia = FormatUtil.completaCadena(String.valueOf(idSucursal), '0', 3, "L");
                        referencia += "9" + FormatUtil.completaCadena(String.valueOf(idGrupo), '0', 6, "L");
                        referencia += FormatUtil.completaCadena(String.valueOf(idCiclo), '0', 2, "L");
                        referencia = ClientesUtil.getDigitoVerificador(referencia);
                        idTabla++;
                        if(refeDAO.getReferenciaGeneral(referencia) == null){
                            pagoReferenciaVO.numcliente = idGrupo;
                            pagoReferenciaVO.numSolicitud = idCiclo;
                            pagoReferenciaVO.referencia = referencia;
                            refeDAO.addReferencia(null, pagoReferenciaVO);
                        }
                        System.out.println("tasaAnual "+tasaAnual+" plazo "+plazo);
                        tasaLogaritmo = TablaAmortizacionHelper.getTasaLogaritmico(capital, pagoFijo, plazo, ClientesConstants.PAGO_SEMANAL, tasaAnual);
                        System.out.println("tasa 1 "+tasaLogaritmo);
                        System.out.println("CAP: "+capital+"pagoFijo: "+pagoFijo+"fechaDis: "+fechaDis+"idSucursal"+idSucursal);
                        tasaCalculada = TablaAmortizacionHelper.calcTasa(ClientesConstants.GRUPAL, capital, pagoFijo, plazo, ClientesConstants.PAGO_SEMANAL, Convertidor.dateToString(fechaDis), idSucursal, tasaLogaritmo);
                        System.out.println("tasa 2 "+ tasaCalculada);
                        grupoVO = grupoDAO.getGrupo(idGrupo);
                        System.out.println("idGrupo "+idGrupo+" idCiclo "+idCiclo);
                        cicloVO = cicloDAO.getCiclo(idGrupo, idCiclo);
                        cicloVO.plazo = plazo;
                        cicloVO.idCreditoIBS = idTabla;
                        cicloVO.referencia = referencia;
                        TablaAmortizacionHelper.insertTablaInsolutoComunal(grupoVO, cicloVO, pagoFijo, ClientesConstants.PAGO_SEMANAL, Convertidor.dateToString(fechaDis), tasaCalculada);
                        TablaAmortHelper.insertTablaInsolutoComunal(grupoVO, cicloVO, pagoFijo, ClientesConstants.PAGO_SEMANAL, Convertidor.dateToString(fechaDis), tasaCalculada);
                        //cicloVO.integrantes = integranteDAO.getIntegrantes(idGrupo, idCiclo);
                        cicloVO.tablaAmortizacion = tablaDAO.getElementos(idGrupo, idCiclo, ClientesConstants.AMORTIZACION_GRUPAL);
                        creditoVO = getCreditInfo(grupoVO, 0.0, cicloVO, saldo.getSaldo());
                        creditoDAO.addCredito(creditoVO);
                        cicloDAO.updateCicloCredito(cicloVO);
                        saldoVO = getSaldoInfo(grupoVO, cicloVO);
                        saldoDAO.addSaldoIBS(saldoVO);
                        grupoDAO.updateGrupoIBS(idTabla, idGrupo);
                        cicloDAO.updateCiclo(null, cicloVO);
                    } catch (Exception e) {
                        System.out.println("*************** Problema con Ciclo "+saldo.getReferencia());
                    }
                }
            }
            conteo+=500;
        } while(listSaldos.size() != 0);
    }
    
    private void migraSaldosDesembolso() throws ClientesException, Exception{
        
        System.out.println("Saldos");
        String query = "", query1 = "", query2 = "";
        int conteo = 0;
        int inserta = 0;
        int idTabla = infoDAO.findID("ib_credito", "d_saldos");
        int idGrupo = 0, idCiclo = 0, idSucursal = 0, idAsesor = 0, plazo = 0;
        double tasaAnual = 0.0, tasaMenusal = 0.0, /*pagoFijo = 0.0,*/ capital = 0.0;
        Date fechaDis = new Date();
        String referencia = "";
        ReferenciaGeneralDAO refeDAO = new ReferenciaGeneralDAO();
        ReferenciaGeneralVO pagoReferenciaVO = new ReferenciaGeneralVO();
        Double tasaLogaritmo = 0.0, tasaCalculada = 0.0;
        GrupoVO grupoVO = new GrupoVO();
        CicloGrupalVO cicloVO = new CicloGrupalVO();
        GrupoDAO grupoDAO = new GrupoDAO();
        CicloGrupalDAO cicloDAO = new CicloGrupalDAO();
        IntegranteCicloDAO integranteDAO = new IntegranteCicloDAO();
        TablaAmortizacionDAO tablaDAO = new TablaAmortizacionDAO();
        CreditoCartVO creditoVO = new CreditoCartVO();
        ArrayList<CicloMigracionVO> listSaldos = new ArrayList<CicloMigracionVO>();
        CreditoCartDAO creditoDAO = new CreditoCartDAO();
        SaldoIBSVO saldoVO = new SaldoIBSVO();
        SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
        do{
            listSaldos = infoDAO.getArrSaldosLimpio(conteo, origen);
            if(!listSaldos.isEmpty()){
                for (CicloMigracionVO saldo : listSaldos) {
                    try {
                        grupoVO = infoDAO.getNombreGrupoCR(saldo.getReferencia(), origen);
                        idSucursal = infoDAO.findIdSucursal(grupoVO.getSucursal(), origen);
                        if(origen == 1){
                            if(idSucursal==7 || idSucursal==8 || idSucursal==21 || idSucursal==22 || idSucursal==23){
                                idGrupo = infoDAO.findGrupo(grupoVO.getNombre()+" CR", idSucursal);
                            }else{
                                idGrupo = infoDAO.findGrupo(grupoVO.getNombre(), idSucursal);
                            }
                        } else if(origen == 2){
                            
                        }
                        //idGrupo = infoDAO.findGrupo(grupoVO.getNombre()+" CR", infoDAO.findIdSucursal(grupoVO.getSucursal()));
                        idCiclo = infoDAO.findNumCiclo(idGrupo, saldo.getReferencia(), origen);
                        idSucursal = infoDAO.findNumSucursal(idGrupo);
                        idAsesor = infoDAO.findIdEjecutivo(saldo.getEjecutivo(), origen);
                        tasaAnual = 62.086807424;
                        capital = saldo.getSaldo();
                        //pagoFijo = saldo.getCuota();
                        fechaDis = saldo.getFechaCaptura();
                        plazo = saldo.getPlazo();
                        referencia = FormatUtil.completaCadena(String.valueOf(idSucursal), '0', 3, "L");
                        referencia += "9" + FormatUtil.completaCadena(String.valueOf(idGrupo), '0', 6, "L");
                        referencia += FormatUtil.completaCadena(String.valueOf(idCiclo), '0', 2, "L");
                        referencia = ClientesUtil.getDigitoVerificador(referencia);
                        idTabla++;
                        if(refeDAO.getReferenciaGeneral(referencia) == null){
                            pagoReferenciaVO.numcliente = idGrupo;
                            pagoReferenciaVO.numSolicitud = idCiclo;
                            pagoReferenciaVO.referencia = referencia;
                            refeDAO.addReferencia(null, pagoReferenciaVO);
                        }
                        Double pagoFijo = TablaAmortizacionHelper.calcPagoUnitario(tasaAnual, capital, plazo, ClientesConstants.PAGO_SEMANAL, Convertidor.dateToString(fechaDis), idSucursal, ClientesConstants.GRUPAL);
                        tasaLogaritmo = TablaAmortizacionHelper.getTasaLogaritmico(capital, pagoFijo, plazo, ClientesConstants.PAGO_SEMANAL, tasaAnual);
                        tasaCalculada = TablaAmortizacionHelper.calcTasa(ClientesConstants.GRUPAL, capital, pagoFijo, plazo, ClientesConstants.PAGO_SEMANAL, Convertidor.dateToString(fechaDis), idSucursal, tasaLogaritmo);
                        grupoVO = grupoDAO.getGrupo(idGrupo);
                        cicloVO = cicloDAO.getCiclo(idGrupo, idCiclo);
                        cicloVO.plazo = plazo;
                        cicloVO.idCreditoIBS = idTabla;
                        cicloVO.referencia = referencia;
                        TablaAmortizacionHelper.insertTablaInsolutoComunal(grupoVO, cicloVO, pagoFijo, ClientesConstants.PAGO_SEMANAL, Convertidor.dateToString(fechaDis), tasaCalculada);
                        //TablaAmortHelper.insertTablaInsolutoComunal(grupoVO, cicloVO, pagoFijo, ClientesConstants.PAGO_SEMANAL, Convertidor.dateToString(fechaDis), tasaCalculada);
                        //cicloVO.integrantes = integranteDAO.getIntegrantes(idGrupo, idCiclo);
                        cicloVO.tablaAmortizacion = tablaDAO.getElementos(idGrupo, idCiclo, ClientesConstants.AMORTIZACION_GRUPAL);
                        //creditoVO = getCreditInfo(grupoVO, 0.0, cicloVO, saldo.getSaldo());
                        //creditoDAO.addCredito(creditoVO);
                        //cicloDAO.updateCicloCredito(cicloVO);
                        //saldoVO = getSaldoInfo(grupoVO, cicloVO);
                        //saldoDAO.addSaldoIBS(saldoVO);
                        //grupoDAO.updateGrupoIBS(idTabla, idGrupo);
                        //cicloDAO.updateCiclo(null, cicloVO);
                    } catch (Exception e) {
                        System.out.println("*************** Problema con Ciclo "+saldo.getReferencia());
                    }
                }
            }
            conteo+=500;
        } while(listSaldos.size() != 0);
    }
    
    private static int obtenNumeroEstado(String mes){
        
        int numero = 0;
        if(mes.equals("AGS"))
            numero = 1;
        if(mes.equals("BC"))
            numero = 2;
        if(mes.equals("BCS"))
            numero = 3;
        if(mes.equals("CAM") || mes.equals("CAMP"))
            numero = 4;
        if(mes.equals("COAH"))
            numero = 5;
        if(mes.equals("COL"))
            numero = 6;
        if(mes.equals("CHIS"))
            numero = 7;
        if(mes.equals("CHIH"))
            numero = 8;
        if(mes.equals("DF"))
            numero = 9;
        if(mes.equals("DGO"))
            numero = 10;
        if(mes.equals("GTO"))
            numero = 11;
        if(mes.equals("GRO"))
            numero = 12;
        if(mes.equals("HGO"))
            numero = 13;
        if(mes.equals("JAL"))
            numero = 14;
        if(mes.equals("MEX"))
            numero = 15;
        if(mes.equals("MICH"))
            numero = 16;
        if(mes.equals("MOR"))
            numero = 17;
        if(mes.equals("NAY"))
            numero = 18;
        if(mes.equals("NL"))
            numero = 19;
        if(mes.equals("OAX"))
            numero = 20;
        if(mes.equals("PUE"))
            numero = 21;
        if(mes.equals("QRO"))
            numero = 22;
        if(mes.equals("QROO") || mes.equals("QR"))
            numero = 23;
        if(mes.equals("SLP"))
            numero = 24;
        if(mes.equals("SIN"))
            numero = 25;
        if(mes.equals("SON"))
            numero = 26;
        if(mes.equals("TAB"))
            numero = 27;
        if(mes.equals("TAMP") || mes.equals("TAMPS"))
            numero = 28;
        if(mes.equals("TLAX"))
            numero = 29;
        if(mes.equals("VER"))
            numero = 30;
        if(mes.equals("YUC"))
            numero = 31;
        if(mes.equals("ZAC"))
            numero = 32;
        
        return numero;
    }
    
    private void calculaRFC() throws ClientesException{
        
        ArrayList<ClienteVO> listClientes = null;
        int conteo = 0;
        String rfc = "", nombre = "", fecNaci = "", paterno = "", materno = "";
        do{
            System.out.println("Conteo "+conteo);
            listClientes = infoDAO.getArrClientes(conteo);
            if(!listClientes.isEmpty()){
                for (ClienteVO cliente : listClientes) {
                    nombre = cliente.getNombre();
                    paterno = cliente.getaPaterno();
                    materno = cliente.getaMaterno();
                    fecNaci = cliente.getFechaNacimiento()+"";
                    if(nombre.substring(1, 2).equals(" ")){
                        nombre = nombre.substring(2, nombre.length());
                    }
                    if(paterno.equals(""))
                        paterno = "X";
                    if(materno.equals(""))
                        materno = "X";
                    rfc = RFCUtil.obtenRFC(nombre, paterno, materno, fecNaci.replace("-", ""));
                    infoDAO.updateRFC("UPDATE clientes SET cl_rfc='"+rfc+"' WHERE cl_numcliente="+cliente.getIdCliente(), origen);
                }
            }
            conteo++;
        } while(listClientes.size() != 0);
        System.out.println("FIN");
    }
    
    private static CreditoCartVO getCreditInfo(GrupoVO grupo, Double montoDepositado, CicloGrupalVO ciclo, double montoDesembolso) {
        Calendar cal = Calendar.getInstance();
        CreditoCartVO objlCredit = new CreditoCartVO();
        CreditoCartDAO creditDAO = new CreditoCartDAO();
        Double tasaIva = 0.000;
        Double montoSeguro = 0.00;
        TasaInteresVO tasaInteres = null;
        ComisionVO comision = null;
        //double montoDesembolso = 0.00;
        try {
            TreeMap<Integer, ComisionVO> catComisiones = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
            TreeMap<Integer, TasaInteresVO> catTasas = CatalogoHelper.getCatalogoTasas(ClientesConstants.GRUPAL);
            SucursalVO sucursal = new SucursalDAO().getSucursal(grupo.sucursal);
            if (Convertidor.esFronterizo(grupo.sucursal)) {
                tasaIva = ClientesConstants.TASA_IVA_FRONTERIZO;
            } else {
                tasaIva = ClientesConstants.TASA_IVA_NO_FRONTERIZO;
            }
            /*for (int i = 0; i < ciclo.integrantes.length; i++) {
                montoSeguro += ciclo.integrantes[i].primaSeguro;
                montoDesembolso += ClientesUtil.calculaMontoSinComision(ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisiones);
            }*/
            tasaInteres = catTasas.get(ciclo.tasa);
            comision = catComisiones.get(ciclo.comision);
            objlCredit.setNumCredito(creditDAO.getMaxCredito() + 1);
            objlCredit.setNumCliente(grupo.idGrupo);
            objlCredit.setNumSolicitud(ciclo.idCiclo);
            objlCredit.setReferencia(ciclo.referencia);
            objlCredit.setNumSucursal(grupo.sucursal);
            objlCredit.setFechaDesembolso((ciclo.tablaAmortizacion[0].fechaPago));
            objlCredit.setFechaVencimiento((ciclo.tablaAmortizacion[ciclo.tablaAmortizacion.length - 1].fechaPago));
            objlCredit.setValorCuota(ciclo.tablaAmortizacion[1].montoPagar);
            objlCredit.setNumCuotas(ciclo.tablaAmortizacion.length - 1);
            objlCredit.setPeriodicidad(ClientesConstants.PAGO_SEMANAL);
            objlCredit.setMontoCredito((ciclo.montoConComision));
            objlCredit.setMontoCuenta(montoDepositado);
            objlCredit.setMontoCuentaCongelada(ciclo.monto * 0.10);
            objlCredit.setMontoDesembolsado((montoDesembolso));
            objlCredit.setMontoSeguro((montoSeguro));
            objlCredit.setMontoComision(ciclo.tablaAmortizacion[0].comisionInicial);
            objlCredit.setMontoIvaComision(ciclo.tablaAmortizacion[0].ivaComision);
            objlCredit.setTasaInteres(ciclo.tasaCalculada);
            objlCredit.setTasaInteresSIVA((ciclo.tasaCalculada / (1 + tasaIva)));
            objlCredit.setTasaMora((ciclo.tasaCalculada * ClientesConstants.FACTOR_MORA_GRUPO));
            objlCredit.setTasaComision((100 - ((ciclo.monto * 100) / ciclo.montoConComision)));
            objlCredit.setTasaIVA(tasaIva * 100);
            objlCredit.setNumProducto(ClientesConstants.GRUPAL);
            objlCredit.setFondeador(1);
            objlCredit.setStatus(8);
            if (grupo.idOperacion == ClientesConstants.REESTRUCTURA_GRUPAL) {
                String referenciaAnterior = new ReferenciaGeneralDAO().getReferencia(grupo.idGrupoOriginal, grupo.idCicloOriginal, 'G');
            }
            objlCredit.setNumDocumento((ciclo.referencia));
            objlCredit.setNumEjecutivo(ciclo.asesor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objlCredit;
    }
    
    private static SaldoIBSVO getSaldoInfo(GrupoVO grupo, CicloGrupalVO ciclo) {
        Calendar cal = Calendar.getInstance();
        SaldoIBSVO saldoVO = new SaldoIBSVO();
        Double tasaIva = 0.000;
        try {
            SucursalVO sucursal = new SucursalDAO().getSucursal(grupo.sucursal);
            if (Convertidor.esFronterizo(grupo.sucursal)) {
                tasaIva = ClientesConstants.TASA_IVA_FRONTERIZO;
            } else {
                tasaIva = ClientesConstants.TASA_IVA_NO_FRONTERIZO;
            }
            TreeMap<Integer, ComisionVO> catComisiones = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
            TreeMap<Integer, TasaInteresVO> catTasas = CatalogoHelper.getCatalogoTasas(ClientesConstants.GRUPAL);
            TablaAmortizacionVO[] tablaVO = ciclo.tablaAmortizacion;
            double pagos[] = null;
            double pagoInteresIva = 0;
            pagos = new double[tablaVO.length];
            for (int i = 0; i < tablaVO.length; i++) {
                pagos[i] = tablaVO[i].interes + tablaVO[i].ivaInteres;
                pagoInteresIva = pagoInteresIva + pagos[i];
            }
            saldoVO.setIdClienteSICAP((grupo.idGrupo));
            saldoVO.setCredito((ciclo.idCreditoIBS));
            saldoVO.setIdSolicitudSICAP((ciclo.idCiclo));
            saldoVO.setReferencia(ciclo.referencia);
            saldoVO.setIdSucursal((grupo.sucursal));
            saldoVO.setNombreSucursal(sucursal.nombre);
            saldoVO.setFechaDesembolso(ciclo.tablaAmortizacion[0].fechaPago);
            saldoVO.setFechaVencimiento(ciclo.tablaAmortizacion[ciclo.tablaAmortizacion.length - 1].fechaPago);
            saldoVO.setNumeroCuotas((ciclo.tablaAmortizacion.length - 1));
            saldoVO.setPeriodicidad((ClientesConstants.PAGO_SEMANAL));
            saldoVO.setMontoCredito(ciclo.montoConComision);
            saldoVO.setMontoDesembolsado((ClientesUtil.calculaMontoSinComision(ciclo.montoConComision, ciclo.comision, catComisiones)));
            saldoVO.setMontoSeguro((0));
            saldoVO.setComision((ciclo.tablaAmortizacion[0].comisionInicial));
            saldoVO.setIvaComision((ciclo.tablaAmortizacion[0].ivaComision));
            saldoVO.setTasaInteresSinIVA((ciclo.tasaCalculada / (1 + tasaIva)));
            saldoVO.setTasaMoraSinIVA((ciclo.tasaCalculada * ClientesConstants.FACTOR_MORA_GRUPO / (1 + tasaIva)));
            saldoVO.setTasaIVA(tasaIva * 100);
            saldoVO.setIdProducto((ClientesConstants.GRUPAL));
            saldoVO.setFondeador(1);
            saldoVO.setEstatus(8);
            saldoVO.setNombreCliente(grupo.nombre);
            saldoVO.setRfc(grupo.rfc);
            saldoVO.setFechaEnvio(ciclo.tablaAmortizacion[0].fechaPago);
            saldoVO.setFechaGeneracion(ciclo.tablaAmortizacion[0].fechaPago);
            saldoVO.setHoraGeneracion(1200);
            saldoVO.setNumeroCuotasTranscurridas(0);
            saldoVO.setPlazo(ciclo.plazo);
            saldoVO.setSaldoTotalAlDia(ciclo.montoConComision + ciclo.tablaAmortizacion[1].interes + ciclo.tablaAmortizacion[1].ivaInteres);
            saldoVO.setSaldoCapital(ciclo.montoConComision);
            saldoVO.setSaldoInteres(ciclo.tablaAmortizacion[1].interes);
            saldoVO.setSaldoInteresVigente(ciclo.tablaAmortizacion[1].interes);
            saldoVO.setSaldoInteresVencido(0);
            saldoVO.setSaldoInteresVencido90dias(0);
            saldoVO.setSaldoInteresCtasOrden(0);
            saldoVO.setSaldoIvaInteres(ciclo.tablaAmortizacion[1].ivaInteres);
            saldoVO.setSaldoMora(0);
            saldoVO.setSaldoIVAMora(0);
            saldoVO.setSaldoMulta(0);
            saldoVO.setSaldoIVAMulta(0);
            saldoVO.setCapitalPagado(0);
            saldoVO.setInteresNormalPagado(0);
            saldoVO.setIvaInteresNormalPagado(0);
            saldoVO.setMoratorioPagado(0);
            saldoVO.setIvaMoraPagado(0);
            saldoVO.setMultaPagada(0);
            saldoVO.setIvaMultaPagado(0);
            saldoVO.setComision(ciclo.tablaAmortizacion[0].comisionInicial);
            saldoVO.setIvaComision(ciclo.tablaAmortizacion[0].ivaComision);
            saldoVO.setMontoDesembolsado(ciclo.monto);
            saldoVO.setFechaSigAmortizacion(ciclo.tablaAmortizacion[1].fechaPago);
            saldoVO.setCapitalSigAmortizacion(ciclo.tablaAmortizacion[1].abonoCapital);
            saldoVO.setInteresSigAmortizacion(ciclo.tablaAmortizacion[1].interes);
            saldoVO.setIvaSigAmortizacion(ciclo.tablaAmortizacion[1].ivaInteres);
            saldoVO.setNombreFondeador("");
            saldoVO.setSaldoConInteresAlFinal(ciclo.montoConComision + pagoInteresIva);
            saldoVO.setCapitalVencido(0);
            saldoVO.setInteresVencido(0);
            saldoVO.setIvaInteresVencido(0);
            saldoVO.setTotalVencido(0);
            saldoVO.setFechaIncumplimiento(Convertidor.stringToSqlDate(ClientesConstants.FECHA_DEFECTO_NULO));
            saldoVO.setFechaAcarteraVencida(Convertidor.stringToSqlDate(ClientesConstants.FECHA_DEFECTO_NULO));
            saldoVO.setDiasMora(0);
            saldoVO.setDiasTranscurridos(1);
            saldoVO.setCuotasVencidas(0);
            saldoVO.setNumeroPagosRealizados(0);
            saldoVO.setMontoTotalPagado(0);
            saldoVO.setFechaUltimoPago(Convertidor.stringToSqlDate(ClientesConstants.FECHA_DEFECTO_NULO));
            saldoVO.setBanderaReestructura("N");
            saldoVO.setCreditoReestructurado(0);
            saldoVO.setDiasMoraReestructura(0);
            saldoVO.setTasaPreferencialIVA("");
            saldoVO.setMontoSeguro(0);
            saldoVO.setCuentaBucket(0);
            saldoVO.setSaldoBucket(0);
            saldoVO.setSaldoBonificacionDeIVA(0);
            saldoVO.setBonificacionPagada(0);
            saldoVO.setOrigen(1);
            saldoVO.setCtaContable(ciclo.asesor);
            saldoVO.setMontoConIntereses(java.lang.Math.ceil(ciclo.montoConComision + pagoInteresIva));
            saldoVO.setTasaElegida(Double.parseDouble(catTasas.get(ciclo.tasa).descripcion.replace("%", "")));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return saldoVO;
    }
}
