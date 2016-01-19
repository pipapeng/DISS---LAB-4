package de.tuhh.diss.plotbot;

import de.tuhh.diss.plotter.Plotter;
import de.tuhh.diss.plotter.PlotterInterface;
import lejos.nxt.Button;
import lejos.nxt.LCD;

public class Plotbot{
	
	private static int choice;
	
	private static PlotterInterface plotter;
	private static UserInterface userInterface;
	
	
	public static void main(String[] args)
	{
		plotter = new Plotter();
		userInterface = new UserInterface();
		
		do{
			choice = userInterface.mainMenu();
			
			switch(choice){
			
			case 1:
				
			case 2:
				
			}
			
		}while(choice != 0);
		
		plotter.shutDown();
		userInterface.shutDown();
		
		


		//Button.ESCAPE.waitForPressAndRelease();
	}
		
	
	
}
