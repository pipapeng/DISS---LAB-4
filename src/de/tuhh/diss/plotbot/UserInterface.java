package de.tuhh.diss.plotbot;

import de.tuhh.diss.robot.RobotInterface;
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;

public class UserInterface implements ButtonListener{

	private int curserPosition;	
	
	public UserInterface(){
		
		curserPosition = 1;
		
		LCD.drawString("Welcome to", 3, 0);
		LCD.drawString("Plotbot!", 4, 1);
	}
	
	public int mainMenu() {
		LCD.clear();
		LCD.drawString("** Main menu **", 0, 0);
		LCD.drawString("Select shape!", 0, 2);
		
		LCD.drawString("Rectangle", 3, 4);
		LCD.drawString("String", 3, 5);
		LCD.drawString("Quit Program", 3, 6);
		
		LCD.drawString("X", 0, curserPosition + 3);
		
		Button.LEFT.addButtonListener(this);
		Button.RIGHT.addButtonListener(this);
		
		
		Button.ENTER.waitForPressAndRelease();
		return curserPosition;
	}
	
	public void shutDown(){

		LCD.clear();
		LCD.drawString("Shut down", 0, 2);
		LCD.drawString("Bye Bye...", 0, 4);
	}
	
	public void buttonPressed(Button b) {
		
	}

	public void buttonReleased(Button b) {

		if(b.getId() == Button.ID_RIGHT){
			
			incrementCurserPosition();
		}
		
		if(b.getId() == Button.ID_LEFT){
			
			decrementCurserPosition();
		}
	}
	
	private void incrementCurserPosition(){
		curserPosition++;
		if(curserPosition > 3){
			curserPosition = 1;
		}
	}
	
	private void decrementCurserPosition(){
		curserPosition--;
		if(curserPosition < 1){
			curserPosition = 3;
		}
	}	
}
