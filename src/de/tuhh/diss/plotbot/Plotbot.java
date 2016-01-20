package de.tuhh.diss.plotbot;

import de.tuhh.diss.plotter.Plotter;
import de.tuhh.diss.plotter.PlotterInterface;
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;

public class Plotbot implements ButtonListener{
	
	public static void main(String[] args){
		new Plotbot();
	}
	
	
	private int choice;
	
	private PlotterInterface plotter;
	private UserInterface userInterface;
	
	
	public Plotbot(){
		
		userInterface = new UserInterface();
		plotter = new Plotter();
		
		do{
			Button.ESCAPE.addButtonListener(this);
			choice = userInterface.mainMenu();
			int size = -1;
			
			switch(choice){
			
			case 1:
				
				size = userInterface.sizeMenu(plotter.getMinSizeRectangle(), plotter.getMaxSizeRectangle());
				
				if(size != -1){
					userInterface.plotInProgress();
					plotter.plotRectangle(size);
					userInterface.plotComplete();
				}
				break;
				
			case 2:
				
				size = userInterface.sizeMenu(plotter.getMinSizeString(), plotter.getMaxSizeString());
				
				if(size != -1){
					userInterface.plotInProgress();
					plotter.plotString(size);
					userInterface.plotComplete();
				}
				break;
				
			case 3:
				
				break;
				
			default:
				
				choice = 3;
				break;
			}
		}while(choice != 3);
		
		plotter.shutDown();
		userInterface.shutDown();
	}

	public void buttonPressed(Button b) {

		plotter.stopImmediatly();
		userInterface.stopedImmediatly();
		
		//TODO: jump zu caseende
	}

	public void buttonReleased(Button b) {
		
	}	
}
