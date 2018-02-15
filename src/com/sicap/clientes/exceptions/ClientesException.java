package com.sicap.clientes.exceptions;


public class ClientesException extends Exception {

	private String error;
	private Exception e;
	
	/** Creates a new instance of ClientesException */
	public ClientesException() {
	}


	public ClientesException (String e){
		super(e);
		this.error = e;
	}


	public ClientesException (Exception e){
		this.e = e;
	}


	public Exception getException (){
		return e;
	}


	public String toString(){
		if (e != null)
			return e.toString();
		return error;
	}


	public void printStackTrace(java.io.PrintWriter printWriter) {
		super.printStackTrace(printWriter);
	}


	public void printStackTrace(java.io.PrintStream printStream) {
		super.printStackTrace(printStream);
	}


	public void printStackTrace() {

		if (e != null){
			e.printStackTrace();
		}
		super.printStackTrace();

	}


	public String getMessage() {
		return toString();
	}


}
