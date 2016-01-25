package de.tuhh.diss.plotbot.exceptions;

public class OutOfWorkspaceException extends Exception{

	public String getMessage(){
		return ("Out of Workspace!");
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private static final long serialVersionUID = 1L;
}
