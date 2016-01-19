package de.tuhh.diss.robot;

import de.tuhh.diss.exceptions.OutOfWorkspaceException;
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
		
	private static final int WHEELGEARRATIO = 5;
	private static final int WHEELDIAMETER = 56;
	private static final int MINFEED = -ArmModule.getArmLength();
	private static final int MAXFEED = 2300 -ArmModule.getArmLength();
	private static final int DISTANCETOLIGHTSENSOR = 105;
	private static final double FACTORLIGHT = 1.05;
	
	private final float WHEELMAXSPEED;
	private final float WHEELMINSPEED = 0;
	
	NXTRegulatedMotor motorWheels;
	LightSensor lightSensor;
	
	
	///////////////////////////////////////////////////////
	//	METHODS
	///////////////////////////////////////////////////////		
	
	public WheelsModule() throws OutOfWorkspaceException{
		motorWheels = Motor.C;
		WHEELMAXSPEED = motorWheels.getMaxSpeed();
		
		lightSensor = new LightSensor(SensorPort.S3);
		
		calibrateMotorWheels();
	}
	
	public int getMaxFeed(){return MAXFEED;}
	
	public double getYCenter(){  //TODO: ueberpruefen
		
		return WHEELDIAMETER * Math.toRadians(motorWheels.getPosition()) / (2 * WHEELGEARRATIO) - ArmModule.getArmLength();
	}
	
	public void setWheelSpeed(double speed) throws IndexOutOfBoundsException{

		if (speed/WHEELGEARRATIO >= WHEELMINSPEED && speed/WHEELGEARRATIO <= WHEELMAXSPEED){
			motorWheels.setSpeed((float)speed/WHEELGEARRATIO);
		}
		else{
			throw new IndexOutOfBoundsException();
		}
	}
	
	public void moveWheels(double distance) throws OutOfWorkspaceException{
		if ((getYCenter() + distance) <= MAXFEED && (getYCenter() + distance) >= MINFEED){
			motorWheels.rotate((int) Math.round((distance*360*WHEELGEARRATIO/(2*WHEELDIAMETER*Math.PI))));
		}
		else
			throw new OutOfWorkspaceException();
	}
	
	//TODO: Eigentlich Listener noetig
	public void moveWheelsForward() throws OutOfWorkspaceException{
		if(getYCenter() <= MAXFEED)
			motorWheels.forward();
		else
			stopWheels();
			throw new OutOfWorkspaceException();
	}

	//TODO: Eigentlich Listener noetig
	public void moveWheelsBackward() throws OutOfWorkspaceException{
		if(getYCenter() >= MINFEED)
			motorWheels.backward();
		else
			stopWheels();
			throw new OutOfWorkspaceException();
	}
	
	public void waitForWheels(){
		motorWheels.waitComplete();
	}
	
	public void stopWheels(){
		motorWheels.stop();
	}
	
	public void calibrateMotorWheels() throws OutOfWorkspaceException{
		LCD.drawString("Calibrating wheels ...", 0, 1);
		LCD.drawString("Press ESC when Lightsensor", 0, 2);
		LCD.drawString("is above black bar!", 0, 3);
		Button.ESCAPE.waitForPressAndRelease();
		LCD.clear();
		
		int dark = lightSensor.getNormalizedLightValue();
		lightSensor.setLow(dark);
		
		motorWheels.resetTachoCount(); // safety first (evtl. outOFBounds)
		motorWheels.setSpeed(WHEELMAXSPEED/5);
		while (lightSensor.getNormalizedLightValue() <= (FACTORLIGHT*dark)){
			motorWheels.forward();
		}
		motorWheels.stop();
		lightSensor.setHigh(lightSensor.getNormalizedLightValue());
		
		moveWheels(DISTANCETOLIGHTSENSOR - ArmModule.getArmLength());
		motorWheels.resetTachoCount();
		
		LCD.clear();;
		LCD.drawString("Wheel calibration successful!", 0, 1);
	}
}
