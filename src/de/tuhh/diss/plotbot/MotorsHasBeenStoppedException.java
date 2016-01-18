package de.tuhh.diss.plotbot;

public class MotorsHasBeenStoppedException extends Exception{

	public String getMessage(){
		
		return ("One Motor went or will go out of his Boundries! ALL Motors has been stopped!");
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private static final long serialVersionUID = 1L;
}
