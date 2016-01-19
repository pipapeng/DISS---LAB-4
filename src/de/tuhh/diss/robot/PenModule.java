package de.tuhh.diss.robot;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.TouchSensor;
import lejos.nxt.SensorPort;

public class PenModule {
	
	///////////////////////////////////////////////////////
	//	VARIABLES
	///////////////////////////////////////////////////////
	
	private static final int PENANGLEUP = 0;
	private static final int PENANGLEDOWN = -180;
	private final float PENMAXSPEED;
	
	private boolean penDown;

	NXTRegulatedMotor motorPen;
	TouchSensor sensorPen;
	
	
	///////////////////////////////////////////////////////
	//	METHODS
	///////////////////////////////////////////////////////		
	
	public PenModule(){
		motorPen = Motor.B;
		PENMAXSPEED = motorPen.getMaxSpeed();
		
		sensorPen = new TouchSensor(SensorPort.S2);
		calibrateMotorPen();
	}
	
	public void setPen(boolean down){
		
		if(down == true){
			if(penDown == false){
				motorPen.rotateTo(PENANGLEDOWN);
				penDown = true;
			}
		} else {
			if(penDown == true){
				motorPen.rotateTo(PENANGLEUP);
				penDown = false;
			}
		}
	}	
	
	public void stopPen(){
		motorPen.stop();
	}
	
	public void calibrateMotorPen(){
		boolean moving = false;
		LCD.drawString("Calibrating pen ...", 0, 0); //TODO: Lennart findet drawString doof!
		motorPen.setSpeed(PENMAXSPEED/5);
		
		while (!sensorPen.isPressed()){
			if(moving == false){
				motorPen.forward();
				moving = true;
			}
		}
		stopPen();
		moving = false;
		
		motorPen.resetTachoCount();
		penDown = false;
		
		motorPen.setSpeed(PENMAXSPEED/2);
		LCD.drawString("Pen calibartion success", 0, 1);
	}
}
