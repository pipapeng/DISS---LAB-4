package de.tuhh.diss.plotbot;

import de.tuhh.diss.plotbot.exceptions.MotorException;
import de.tuhh.diss.plotbot.exceptions.OutOfWorkspaceException;
import de.tuhh.diss.plotbot.plotter.Plotter;
import de.tuhh.diss.plotbot.plotter.PlotterInterface;
import de.tuhh.diss.plotbot.robot.PhysicalRobot;
import de.tuhh.diss.plotbot.robot.RobotInterface;
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.NXT;


public class Plotbot implements ButtonListener{
	private int choice;
	private boolean calibrated = false;
	
	private RobotInterface robot;
	private PlotterInterface plotter;
	private UserInterface userInterface;
	
	
	public static void main(String[] args){
		new Testbot();
	}
	
	public Plotbot(){
	
		
		try {
			robot = PhysicalRobot.ROBOT;
			userInterface = new UserInterface();
			plotter = new Plotter(robot);
		} catch (MotorException e1) {

			userInterface.motorException();
			userInterface.shutDown();
			NXT.shutDown();
		}

		
		Button.ESCAPE.addButtonListener(this);
		
		calibrate(false);
		
		if(calibrated == true){
			do{
				choice = userInterface.mainMenu();
				int size = -1;
				
				switch(choice){
				
				case 1:
					
					size = userInterface.sizeMenu(plotter.getMinSizeRectangle(), plotter.getMaxSizeRectangle());
					
					if(size != -1){
						
						try {
							userInterface.plotInProgress();
							plotter.plotRectangle(size);
							userInterface.plotComplete();
						} catch (MotorException e) {
							
							userInterface.motorException();
							calibrate(true);
							break;
						} catch (OutOfWorkspaceException e) {
							//TODO: FILL THIS
						}
					}
					break;
					
				case 2:
					
					size = userInterface.sizeMenu(plotter.getMinSizeString(), plotter.getMaxSizeString());
					
					if(size != -1){
						
						try {
							userInterface.plotInProgress();
							plotter.plotString(size);
							userInterface.plotComplete();
						} catch (MotorException e) {

							userInterface.motorException();
							calibrate(true);
							break;
						} catch (OutOfWorkspaceException e) {
							//TODO: FILL THIS
						}
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

		userInterface.shutDown();
		NXT.shutDown();
	}

	public void buttonPressed(Button b) {

		if(userInterface.mainMenuActive == false && userInterface.sizeMenuActive == false){		//TODO: evtl. entfernen
			plotter.stopImmediatly();															//TODO: evtl. entfernen
			userInterface.stopedImmediatly();
			NXT.shutDown();
		}

	}

	public void buttonReleased(Button b) {
		
	}
	
	private void calibrate(boolean recalibration){
		
		userInterface.calibrationMenu(recalibration);
		calibrated = robot.calibrateMotors();
	}
}
