package de.tuhh.diss.exceptions;

public class MotorException extends Exception{

	public String getMessage(){
		
		return ("One Motor went or will go out of its Boundries! ALL Motors have been stopped!");
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private static final long serialVersionUID = 1L;
}
