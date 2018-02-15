package com.sicap.clientes.helpers.inffinix;


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.TreeMap;

import com.sicap.clientes.dao.CatalogoMapeoDAO;
import com.sicap.clientes.dao.inffinix.ClienteInffinixDAO;
import com.sicap.clientes.dao.inffinix.GrupoInffinixDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.CatalogoMapeoVO;
import com.sicap.clientes.vo.inffinix.ClienteInffinix;
import com.sicap.clientes.vo.inffinix.GrupoInffinixVO;

public class SyncronetHelper{
	
	public static ClienteInffinix[] getClientesEnvio() throws Exception{
		ClienteInffinixDAO dao = new ClienteInffinixDAO();
		ClienteInffinix[] clientes = null;
		clientes = dao.getClientesInfinix();
		if( clientes!=null ) 
			Logger.debug("Clientes encontrados para envio a Syncronet : "+clientes.length);
		return clientes;
	}
	
	public static GrupoInffinixVO[] getGruposEnvio() throws Exception{
		GrupoInffinixDAO grupoDAO = new GrupoInffinixDAO();
		GrupoInffinixVO[] grupos = null;
		grupos = grupoDAO.getGruposInffinix();
		if( grupos!=null ) 
			Logger.debug("Grupos encontrados para envio a Syncronet : "+grupos.length);
		return grupos;
	}

	public static TreeMap getCatalogoMapeo(int idCatalogo) throws ClientesException{
		Hashtable<String, String> catalogo = new Hashtable<String, String>();
		CatalogoMapeoDAO dao = new CatalogoMapeoDAO();
		CatalogoMapeoVO elementos[] = dao.getCatalogoMapeo(idCatalogo);
		for ( int i=0 ; i<elementos.length ; i++ ){
			catalogo.put(new String(elementos[i].codigo),elementos[i].codigoSyncronet);
		}
		return sort(catalogo);
	}

	private static TreeMap sort(Hashtable hash){
        TreeMap<Object, Object> tree = new TreeMap<Object, Object>();
        if (hash != null && hash.size() > 0){
            Enumeration e = hash.keys();
            while (e.hasMoreElements()){
                Object key = e.nextElement();
                Object value = hash.get(key);
                tree.put(key, value);
            }
        }
        return tree;
    }
}