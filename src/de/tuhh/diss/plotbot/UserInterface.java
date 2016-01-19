package de.tuhh.diss.plotbot;

import de.tuhh.diss.robot.RobotInterface;
import lejos.nxt.Button;
import lejos.nxt.LCD;

public class UserInterface {

	private static int curserPosition;
	private static int choice;	
	
	public static void main(String[] args){
		
		choice = 0;
		curserPosition = 0;
		
		LCD.drawString("Welcome to", 0, 0);
		LCD.drawString("Plotbot!", 0, 1);
	
		do{
			
			
			mainMenu();
			
			
			
			
			Button.ENTER.waitForPressAndRelease();
			choice = curserPosition;
			switch(choice){
			
			case 1:
				
			case 2:
				
			}
			
		}while(choice != 3);
		
		LCD.clear();
		LCD.drawString("Bye Bye...", 0, 1);
		Button.ESCAPE.waitForPressAndRelease(); //wait a sec
	}
	
	
	
	private static void incrementCurserPosition(){
		curserPosition++;
		if(curserPosition > 3){
			curserPosition = 1;
		}
	}
	
	private static void decrementCurserPosition(){
		curserPosition--;
		if(curserPosition < 1){
			curserPosition = 3;
		}
	}
	
	private static void mainMenu() {
		LCD.clear();
		LCD.drawString("*** Main menu ***", 0, 0);
		LCD.drawString("Select shape!", 0, 2);
		
		LCD.drawString("Rectangle", 3, 4);
		LCD.drawString("String", 3, 5);
		LCD.drawString("Quit Programm", 3, 6);
		
		Button.LEFT.addButtonListener(new LeftButtonListener());
		Button.RIGHT.addButtonListener(new LeftButtonListener());
		
		
		if(curserPosition <= 3 && curserPosition >= 1){
			
			LCD.drawString("X", 0, curserPosition + 3);
		}
		
		
	}	

}
