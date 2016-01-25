package de.tuhh.diss.plotbot;

import de.tuhh.diss.plotbot.plotter.Plotter;
import de.tuhh.diss.plotbot.plotter.PlotterInterface;
import de.tuhh.diss.plotbot.robot.PhysicalRobot;
import de.tuhh.diss.plotbot.robot.RobotInterface;
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.util.Delay;

public class Plotbot implements ButtonListener{
	private int choice;
	private boolean calibrated = false;
	
	private RobotInterface robot;
	private PlotterInterface plotter;
	private UserInterface userInterface;
	
	
	public static void main(String[] args){
		new Plotbot();
	}
	
	public Plotbot(){
		
		robot = new PhysicalRobot();
		userInterface = new UserInterface();
		plotter = new Plotter(robot);
		
		Button.ESCAPE.addButtonListener(this);
		
		userInterface.calibrationMenu();
		calibrated = robot.calibrateMotors();
		
		if(calibrated == true){
			do{
				choice = userInterface.mainMenu();
				int size = -1;
				
				switch(choice){
				
				case 1:
					
					size = userInterface.sizeMenu(plotter.getMinSizeRectangle(), plotter.getMaxSizeRectangle());
					
					if(size != -1){
						userInterface.plotInProgress();
						Delay.msDelay(3000);	//TODO: entfernen
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
		}


		plotter.shutDown();
		userInterface.shutDown();
		return;
	}

	public void buttonPressed(Button b) {

		if(userInterface.mainMenuActive == false && userInterface.sizeMenuActive == false){
			plotter.stopImmediatly();
			userInterface.stopedImmediatly();
			//TODO: jump zu caseende
		}

	}

	public void buttonReleased(Button b) {
		
	}	
}
