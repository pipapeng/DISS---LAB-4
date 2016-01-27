package de.tuhh.diss.plotbot.robot;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.TouchSensor;
import lejos.nxt.SensorPort;

public class PenModule {
	
	///////////////////////////////////////////////////////
	//	VARIABLES
	///////////////////////////////////////////////////////
	
	public static final int PENANGLEUP = 0;
	public static final int PENANGLEDOWN = -380;
	public final float PENMAXSPEED;
	
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
	}
	
	public boolean penIsDown(){
		return penDown;
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
		LCD.clear();
		LCD.drawString("Calibrating pen ...", 0, 0);
		motorPen.setSpeed(PENMAXSPEED/5);
		
		while (!sensorPen.isPressed()){
			if(motorPen.isMoving() == false){
				motorPen.forward();
			}
		}
		stopPen();
		
		motorPen.resetTachoCount();
		penDown = false;
		
		motorPen.setSpeed(PENMAXSPEED/2);
		LCD.drawString("Pen calibration", 0, 0);
		LCD.drawString("successful!", 0, 1);
	}
}
