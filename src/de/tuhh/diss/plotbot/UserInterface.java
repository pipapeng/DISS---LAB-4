package de.tuhh.diss.plotbot;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.util.Delay;

public class UserInterface implements ButtonListener{

	private static final int REFRESHPERIOD = 250;
	private static final int TIMEDELAY = 2000;
	
	private int curserPosition;
	private int repeatsLeft;
	private int repeatsRight;
	private int minSize;
	private int maxSize;
	private int size;
	
	public boolean mainMenuActive;			//TODO: methode
	public boolean sizeMenuActive;			//TODO: methode
	private boolean sizeMenuInterruption;
	
	
	public UserInterface(){
		
		Button.LEFT.addButtonListener(this);
		Button.RIGHT.addButtonListener(this);
		Button.ENTER.addButtonListener(this);
		Button.ESCAPE.addButtonListener(this);
		
		LCD.drawString("Welcome to", 3, 1);
		LCD.drawString("Plotbot!", 4, 2);
		
		Delay.msDelay(TIMEDELAY);
	}
	
	public void calibrationMenu(){
		LCD.clear();
		LCD.drawString("Robot needs to", 0, 0);
		LCD.drawString("be calibrated.", 0, 1);
		LCD.drawString("Press ENTER", 0, 3);
		LCD.drawString("to start!", 0, 4);
		
		Button.ENTER.waitForPressAndRelease();
		
		LCD.clear();
		LCD.drawString("Calibrating...", 1, 4);
	}
	
	public int mainMenu() {
		
		mainMenuStart();
		
		do{
			for(int i = 4; i <= 6; i++){
				LCD.clear(1, i, 1);
			}
			LCD.drawString("X", 1, curserPosition + 3);
			
			Delay.msDelay(REFRESHPERIOD);
		} while(mainMenuActive == true);
	
		return curserPosition;
	}
	
	private void mainMenuStart(){
		
		mainMenuActive = true;
		curserPosition = 1;
		
		LCD.clear();
		LCD.drawString("** Main menu **", 0, 0);
		LCD.drawString("Select shape!", 0, 2);
		
		LCD.drawString("Rectangle", 3, 4);
		LCD.drawString("String", 3, 5);
		LCD.drawString("Quit Program", 3, 6);
	}
	
	public int sizeMenu(int minSize, int maxSize){
		
		sizeMenuStart(minSize, maxSize);				
		
		do{
			LCD.clear(6, 5, 11);
			LCD.drawString(Integer.toString(size), 6, 5);
		
			Delay.msDelay(REFRESHPERIOD);
		} while(sizeMenuActive == true);
		
		if(sizeMenuInterruption == false){
			return size;
		}
		else{
			return -1;
		}
	}
	
	public void plotInProgress(){
		
		LCD.clear();
		LCD.drawString("Plot in progress", 0, 1);
		LCD.drawString("...", 0, 2);
		
		LCD.drawString("Press Escape", 0, 5);
		LCD.drawString("to stop", 0, 6);
	}
	
	public void plotComplete(){
		
		LCD.clear();
		LCD.drawString("Plot complete!", 0, 3);

		Delay.msDelay(TIMEDELAY);
	}
	
	public void stopedImmediatly(){
		
		LCD.clear();
		LCD.drawString("You have", 0, 1);
		LCD.drawString("pressed ESC", 0, 2);
		
		LCD.drawString("Robot has", 0, 4);
		LCD.drawString("been stopped!", 0, 5);
		
		Delay.msDelay(TIMEDELAY);
	}
	
	public void shutDown(){

		LCD.clear();
		LCD.drawString("Shutting down", 0, 2);
		LCD.drawString("Bye Bye...", 0, 4);
		
		Delay.msDelay(TIMEDELAY);
	}
	
	public void buttonPressed(Button b) {
		
		if(b.getId() == Button.ID_RIGHT){
			
			if(mainMenuActive == true){
				
				incrementCurserPosition();
			}
			if(sizeMenuActive == true){
				
				incrementSize();
			}
		}
		
		if(b.getId() == Button.ID_LEFT){
			
			if(mainMenuActive == true){
				
				decrementCurserPosition();
			}
			if(sizeMenuActive == true){
				
				decrementSize();
			}
		}
		
		if(b.getId() == Button.ID_ENTER){
		
			if(mainMenuActive == true){
				
				mainMenuActive = false;
			}
			if(sizeMenuActive == true){
				
				sizeMenuActive = false;
			}
		}
		
		if(b.getId() == Button.ID_ESCAPE){
			
			if(sizeMenuActive == true){
				
				sizeMenuInterruption = true;
				sizeMenuActive = false;
			}
			
			while(!Button.ESCAPE.isUp()){
				
			}
		}
	}

	public void buttonReleased(Button b) {

		
	}
	
	private void sizeMenuStart(int minSize, int maxSize){
		
		repeatsLeft = 0;
		repeatsRight = 0;
		
		this.minSize = minSize;
		this.maxSize = maxSize;
		
		if(sizeMenuActive == false){
			size = (int) ((minSize + maxSize) / 20) * 10;
		}
		
		sizeMenuActive = true;
		sizeMenuInterruption = false;
		
		LCD.clear();
		LCD.drawString("** Size menu **", 0, 0);
		
		LCD.drawString("Min Size:", 0, 2);
		LCD.drawString(Integer.toString(minSize), 10, 2);
		LCD.drawString("Max Size:", 0, 3);
		LCD.drawString(Integer.toString(maxSize), 10, 3);
		
		LCD.drawString("Size:", 0, 5);
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
		
		repeatsLeft = 0;
		repeatsRight++;
		if(repeatsRight <= 10){
			size++;
		}
		else{
			size = size + 10;
		}
		if(size > maxSize){
			size = maxSize;
		}
	}
	
	private void decrementSize(){
		
		repeatsRight = 0;
		repeatsLeft++;
		if(repeatsLeft <= 10){
			size--;
		}
		else{
			size = size - 10;
		}
		if(size < minSize){
			size = minSize;
		}
	}
	
	/**
	 * Lets the user choose between "Yes" and "No" on a certain position of the screen
	 * 
	 * @param screenColumn 	Column on screen where Yes/No will be placed
	 * @param screenRow		Row on screen where Yes/No will be placed
	 * @return true when selection is "Yes" and false when selection is "No"
	 */
	public static boolean chooseYesNo(int screenColumn, int screenRow){
		int selection = 0;
		LCD.drawString("No", screenColumn, screenRow);
		
		while (!Button.ENTER.isDown()){
			if(Button.RIGHT.isDown() || Button.LEFT.isDown()){
				if(Button.RIGHT.isUp() && Button.LEFT.isUp())
				switch (selection){
					case(0):	
						selection = 1;
						LCD.drawString("Yes", screenColumn, screenRow);
						break;
				
					case(1):
						selection = 0;
						LCD.drawString("No", screenColumn, screenRow);
						break;
				}
			}
		}
		
		if(selection == 1){
			return true;
		} else {
			return false;
		}
	}
	
}
