package de.tuhh.diss.plotbot;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;

public class UserInterface implements ButtonListener{

	private int curserPosition;
	private int repeatsLeft;
	private int repeatsRight;
	private int maxSize;
	private int size;
	
	private boolean mainMenu;
	private boolean sizeMenu;
	private boolean stopImmediatly;
	
	public UserInterface(){
		
		curserPosition = 1;
		repeatsLeft = 0;
		repeatsRight = 0;
		stopImmediatly = false;
		
		LCD.drawString("Welcome to", 3, 0);
		LCD.drawString("Plotbot!", 4, 1);
	}
	
	public int mainMenu() {
		
		mainMenu = true;
		
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
		mainMenu = false;
		return curserPosition;
	}
	
	public int sizeMenu(int maxSize){
		
		this.maxSize = maxSize; 
		sizeMenu = true;
		
		LCD.clear();
		LCD.drawString("** Size menu **", 0, 0);
		LCD.drawString("Max Size:", 0, 2);
		LCD.drawString(Integer.toString(maxSize), 0, 3);
		
		LCD.drawString("Size:", 0, 5);
		LCD.drawString(Integer.toString(size), 7, 5);
		
		// nochmal oder reicht das einmal?
		Button.LEFT.addButtonListener(this);
		Button.RIGHT.addButtonListener(this);
		
		
		Button.ENTER.waitForPressAndRelease();
		sizeMenu = false;
		return size;
	}
	
	public void plotInProgress(){
		
		Button.ESCAPE.addButtonListener(this);
		
		LCD.clear();
		LCD.drawString("Plot in progress", 0, 1);
		LCD.drawString("...", 0, 2);
		
		LCD.drawString("Press Escape", 0, 4);
		LCD.drawString("to stop", 0, 5);
	}
	
	public void shutDown(){

		LCD.clear();
		LCD.drawString("Shut down", 0, 2);
		LCD.drawString("Bye Bye...", 0, 4);
		Button.ESCAPE.waitForPressAndRelease();		// time delay
	}
	
	public void buttonPressed(Button b) {
		
	}

	public void buttonReleased(Button b) {

		if(b.getId() == Button.ID_RIGHT){
			
			if(mainMenu == true){
				incrementCurserPosition();
			}
			if(sizeMenu == true){
				incrementSize();
				sizeMenu(maxSize);
			}
		}
		
		if(b.getId() == Button.ID_LEFT){
			
			if(mainMenu == true){
				decrementCurserPosition();
			}
			if(sizeMenu == true){
				decrementSize();
			}
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
	
	private void incrementSize(){
		
		repeatsRight = 0;
		repeatsLeft++;
		if(repeatsLeft > 10){
			size++;
		}
		else{
			size = size + 10;
		}
	}
	
	private void decrementSize(){
		
		repeatsLeft = 0;
		repeatsRight++;
		if(repeatsRight > 10){
			size++;
		}
		else{
			size = size + 10;
		}
	}
}
