package de.tuhh.diss.plotbot;

import de.tuhh.diss.robot.RobotInterface;
import de.tuhh.diss.robot.PhysicalRobot;
import lejos.nxt.Button;
import lejos.nxt.LCD;

public class Plotbot {
	
	private static int curserPosition;
	private static boolean quit;
	
	
	public static void main(String[] args){
		
		quit = false;
		curserPosition = 0;
		
		LCD.drawString("Welcome to", 0, 0);
		LCD.drawString("Plotbot!", 0, 1);
	
		do{
			
			mainMenu();
		}while(quit != true);
		
		LCD.drawString("Plotbot!", 0, 1);
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
	
	private static void mainMenu(){
		LCD.clear();
		LCD.drawString("*** Main menu ***", 0, 0);
		LCD.drawString("Select shape!", 0, 3);
		
		LCD.drawString("Rectangle", 3, 4);
		LCD.drawString("String", 3, 5);
		LCD.drawString("Quit Programm", 3, 6);
		
		if(curserPosition <= 3 && curserPosition >= 1){
			
			LCD.drawString("X", 0, curserPosition + 3);
		}
	}	
	
	
	
}