package de.tuhh.diss.exceptions;

public class OutOfWorkspaceException extends Exception{

	public String getMessage(){
		
		return ("Coordinates cannot be reached!");
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private static final long serialVersionUID = 1L;
}
