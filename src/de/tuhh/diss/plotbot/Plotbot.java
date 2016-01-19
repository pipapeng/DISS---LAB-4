package de.tuhh.diss.plotbot;

import de.tuhh.diss.plotter.Plotter;
import de.tuhh.diss.plotter.PlotterInterface;
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;

public class Plotbot implements ButtonListener{
	
	private int choice;
	
	private PlotterInterface plotter;
	private UserInterface userInterface;
	
	
	public static void main(String[] args){
		new Plotbot();
	}
	
	public Plotbot(){
		
		userInterface = new UserInterface();
		plotter = new Plotter();
		
		do{
			Button.ESCAPE.addButtonListener(this);
			choice = userInterface.mainMenu();
			int size = 0;
			
			switch(choice){
			
			
			case 1:
				
				size = userInterface.sizeMenu(plotter.getMinSizeRectangle(), plotter.getMaxSizeRectangle());
				userInterface.plotInProgress();
				plotter.plotRectangle(size);
				userInterface.plotComplete();
				
			case 2:
				
				size = userInterface.sizeMenu(plotter.getMinSizeString(), plotter.getMaxSizeString());
				userInterface.plotInProgress();
				plotter.plotString(size);
				userInterface.plotComplete();
				
			default:
				choice = 0;		//keine Ahnung was sonst
			}
		}while(choice != 0);
		
		plotter.shutDown();
		userInterface.shutDown();
		
		//TODO: time delay

		Button.ESCAPE.waitForPressAndRelease();
	}


	@Override
	public void buttonPressed(Button b) {

		plotter.stopImmediatly();
		userInterface.stopedImmediatly();
		
		//TODO: jump zu caseende
	}


	@Override
	public void buttonReleased(Button b) {
		
	}	
}
