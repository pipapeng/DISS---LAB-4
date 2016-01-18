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
	private static final int PENANGLEDOWN = 180;
	private final float PENMAXSPEED;
	private final float PENMINSPEED = 0;
	
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
		LCD.drawString("Calibrating pen ...", 0, 1);
		motorPen.setSpeed(PENMAXSPEED/5);
		motorPen.forward();
		
		//wait until sensor button is pressed then stop the motor of the pen
		while (!sensorPen.isPressed()){
		}
		stopPen();
		
		motorPen.resetTachoCount();
		penDown = false;
	}
}
