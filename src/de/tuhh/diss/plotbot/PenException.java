package de.tuhh.diss.plotbot;

//TODO: einbauen oder loeschen

public class PenException extends Exception{
	
		public String getMessage(){
			return ("Pen is NOT in upper position! Recalibration...");
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		private static final long serialVersionUID = 1L;
		
	}
