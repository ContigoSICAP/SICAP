package com.sicap.clientes.helpers;


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.TreeMap;

import com.sicap.clientes.dao.CatalogoMapeoDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.CatalogoMapeoVO;

public class MapeoHelper{


	public static TreeMap getCatalogoMapeo(int idCatalogo) throws ClientesException{

		Hashtable<String,String> catalogo = new Hashtable<String,String>();
		CatalogoMapeoDAO dao = new CatalogoMapeoDAO();

		CatalogoMapeoVO elementos[] = dao.getCatalogoMapeo(idCatalogo);
		for ( int i=0 ; i<elementos.length ; i++ ){
			catalogo.put(new String(elementos[i].codigo),elementos[i].codigoSyncronet);
		}
		return sort(catalogo);

	}



	private static TreeMap sort(Hashtable hash){

        TreeMap tree = new TreeMap ();
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