package de.tuhh.diss.robot;

import de.tuhh.diss.exceptions.MotorException;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.LightSensor;

public class WheelsModule {
	
	///////////////////////////////////////////////////////
	//	VARIABLES
	///////////////////////////////////////////////////////
	
	private final int ARMLENGTH = 80;	
	private static final int WHEELGEARRATIO = 5;
	private static final int WHEELDIAMETER = 56;
	private static final int MINFEED = 0;
	private static final int MAXFEED = 2300;
	private static final int DISTANCETOLIGHTSENSOR = 105;
	private static final double FACTORLIGHT = 1.05;
	
	private final float WHEELMAXSPEED;
	private final float WHEELMINSPEED = 0;
	
	NXTRegulatedMotor motorWheels;
	LightSensor lightSensor;
	
	
	///////////////////////////////////////////////////////
	//	METHODS
	///////////////////////////////////////////////////////		
	
	public WheelsModule() throws MotorException{
		motorWheels = Motor.C;
		WHEELMAXSPEED = motorWheels.getMaxSpeed();
		
		lightSensor = new LightSensor(SensorPort.S3);
		
		calibrateMotorWheels();
	}
	//TODO: Wie wird sichergestellt was der Feed ist, bisher: y Wert zu der Position an der die wheels kalibiert werden ?
	public int getFeed(){return (int) ((WHEELDIAMETER/(2*WHEELGEARRATIO)) * Math.toRadians(motorWheels.getPosition()));} //TODO: überprüfen
	public int getMaxFeed(){return MAXFEED;}
	
	public void setWheelSpeed(int speed) throws IndexOutOfBoundsException{

		if (speed>=WHEELMINSPEED && speed<=WHEELMAXSPEED){
			motorWheels.setSpeed(speed);
		}
		else{
			throw new IndexOutOfBoundsException();
		}
	}
	
	public void moveWheels(int distance) throws MotorException{
		if ((getFeed() + distance) <= MAXFEED && (getFeed() + distance) >= MINFEED){
			motorWheels.rotate((int) (distance*360*WHEELGEARRATIO/(2*WHEELDIAMETER*Math.PI)));
		}
		else
			stopWheels();
			throw new MotorException();
	}
	
	//TODO: Eigentlich Listener noetig
	public void moveWheelsForward() throws MotorException{
		if(getFeed() <= MAXFEED)
			motorWheels.forward();
		else
			stopWheels();
			throw new MotorException();
	}

	//TODO: Eigentlich Listener noetig
	public void moveWheelsBackward() throws MotorException{
		if(getFeed() >= MINFEED)
			motorWheels.backward();
		else
			stopWheels();
			throw new MotorException();
	}
	
	public void waitForWheels(){
		motorWheels.waitComplete();
	}
	
	public void stopWheels(){
		motorWheels.stop();
	}
	
	public void calibrateMotorWheels() throws MotorException{
		LCD.drawString("Calibrating wheels ...", 0, 1);
		LCD.drawString("Press ESC when Lightsensor", 0, 2);
		LCD.drawString("is above black bar!", 0, 3);
		Button.ESCAPE.waitForPressAndRelease();
		LCD.clear();
		
		int dark = lightSensor.getNormalizedLightValue();
		lightSensor.setLow(dark);
		
		motorWheels.setSpeed(WHEELMAXSPEED/5);
		while (lightSensor.getNormalizedLightValue() <= (FACTORLIGHT*dark)){
			motorWheels.forward();
		}
		motorWheels.stop();
		lightSensor.setHigh(lightSensor.getNormalizedLightValue());
		
		moveWheels(DISTANCETOLIGHTSENSOR - ARMLENGTH);
		motorWheels.resetTachoCount();
		LCD.clear();;
		LCD.drawString("Calibration successful!", 0, 1);
	}
}
