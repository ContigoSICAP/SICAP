package com.sicap.clientes.util.inffinix;

import java.io.File;
import java.io.FilenameFilter;

public class FiltroInffinix implements FilenameFilter{
	
	String extension = null;
	
	FiltroInffinix(String extension){
		this.extension = extension;
	}
	
	public boolean accept(File file, String extension){
		return (extension.endsWith(this.extension));
	}
}
