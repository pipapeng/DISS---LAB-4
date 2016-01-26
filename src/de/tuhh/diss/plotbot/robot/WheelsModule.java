package de.tuhh.diss.plotbot.robot;

import de.tuhh.diss.plotbot.exceptions.OutOfWorkspaceException;
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
		
	public static final int WHEELGEARRATIO = 5;
	public static final int WHEELDIAMETER = 56;
	public static final int YCENTERMIN = -ArmModule.getArmLength();
	public static final int YCENTERMAX = 2300 -ArmModule.getArmLength();
	public static final int DISTANCETOLIGHTSENSOR = 105;
	public static final double FACTORLIGHT = 1.05;
	
	public final float WHEELMOTORMAXSPEED;
	public final float WHEELMOTORMINSPEED = 0;
	
	private NXTRegulatedMotor motorWheels;
	private LightSensor lightSensor;
	
	
	///////////////////////////////////////////////////////
	//	METHODS
	///////////////////////////////////////////////////////		
	
	public WheelsModule(){
		motorWheels = Motor.C;
		WHEELMOTORMAXSPEED = motorWheels.getMaxSpeed();
		lightSensor = new LightSensor(SensorPort.S3);
	}
	
	public int getYCenterMax(){
		return YCENTERMAX;
	}
	
	/** Calculates the distance from y = 0 to where the rotational 
	 *  center of the swivel arm is currently
	 * 
	 * @return y of center
	 */
	public double getYCenter(){
		double circumferenceWheel = Math.PI * WHEELDIAMETER;
		double revolutionsMotor = motorWheels.getTachoCount()/360;
		double revolutionsWheel = revolutionsMotor/WHEELGEARRATIO;  
		
		return revolutionsWheel * circumferenceWheel - ArmModule.getArmLength();
	}
	
	/** Sets the speed of the wheels, will check 
	 *  whether that speed is possible within the boundaries of the motor
	 * 
	 * @param wheelSpeed desired speed for the wheels
	 */
	public void setWheelSpeed(double wheelSpeed){
		double motorSpeed = wheelSpeed * WHEELGEARRATIO;
		
		if (motorSpeed >= WHEELMOTORMINSPEED && motorSpeed <= WHEELMOTORMAXSPEED){
			motorWheels.setSpeed((float)motorSpeed);
		}
		else{
			motorWheels.setSpeed((float)WHEELMOTORMAXSPEED/2);
			LCD.drawString("Wrong wheelspeed,", 0, 1); //TODO:ONLY FOR DEBUG
			LCD.drawString("set default speed", 0, 2); //TODO:ONLY FOR DEBUG
		}
	}
	
	/** Moves the robot by a given distance in y-direction
	 *  distance may also be negative
	 * 
	 * @param distance the distance to be driven
	 * @throws OutOfWorkspaceException thrown if target is not inside workspace
	 */
	public void moveWheels(double distance) throws OutOfWorkspaceException{

		
		if ((getYCenter() + distance) <= YCENTERMAX && (getYCenter() + distance) >= YCENTERMIN){
			
			/* Formula in detail:
			 *  circumferenceWheel = WHEELDIAMETER * Math.PI;
			 *  revolutionsWheelNeeded = distance / circumferenceWheel;
			 *  revolutionsMotorNeeded = revolutionsWheelNeeded * WHEELGEARRATIO;
			 *  motorAngleNeeded = revolutionsMotorNeeded * 360; -> to function
			 */ 
			motorWheels.rotate((int) Math.round((distance*360*WHEELGEARRATIO/(WHEELDIAMETER*Math.PI))));
			
		} else {
			throw new OutOfWorkspaceException();
		}
	}
	
	/** Moves the robot by a given distance in y-direction
	 *  distance may also be negative
	 * 
	 * @param distance the distance to be driven
	 * @param immediateReturn if set true method will return even if rotate is not finished yet
	 * @throws OutOfWorkspaceException thrown if target is not inside workspace
	 */
	public void moveWheels(double distance, boolean immediateReturn) throws OutOfWorkspaceException{

		
		if ((getYCenter() + distance) <= YCENTERMAX && (getYCenter() + distance) >= YCENTERMIN){
			
			/* Formula in detail:
			 *  circumferenceWheel = WHEELDIAMETER * Math.PI;
			 *  revolutionsWheelNeeded = distance / circumferenceWheel;
			 *  revolutionsMotorNeeded = revolutionsWheelNeeded * WHEELGEARRATIO;
			 *  motorAngleNeeded = revolutionsMotorNeeded * 360; -> to function
			 */ 
			motorWheels.rotate((int) Math.round((distance*360*WHEELGEARRATIO/(WHEELDIAMETER*Math.PI))), immediateReturn);
			
		} else {
			throw new OutOfWorkspaceException();
		}
	}
	
	/** Moves forward unless the robot is already outside of the workspace
	 * 
	 * @throws OutOfWorkspaceException
	 */
	public void moveWheelsForward() throws OutOfWorkspaceException{
		if(getYCenter() <= YCENTERMAX)
			motorWheels.forward();
		else
			stopWheels();
			throw new OutOfWorkspaceException();
	}

	/** Moves forward unless the robot is already outside of the workspace
	 * 
	 * @throws OutOfWorkspaceException
	 */
	public void moveWheelsBackward() throws OutOfWorkspaceException{
		if(getYCenter() >= YCENTERMIN)
			motorWheels.backward();
		else
			stopWheels();
			throw new OutOfWorkspaceException();
	}
	
	/** Waits for a motor method to be completed before another can be called
	 * 
	 */
	public void waitForWheels(){
		motorWheels.waitComplete();
	}
	
	public void stopWheels(){
		motorWheels.stop();
	}
	
	//TODO: Zurueckfahren bis Linie gefunden, dann kalibrieren
	public void calibrateMotorWheels() throws OutOfWorkspaceException{
	
		LCD.clear();
		LCD.drawString("Calibrating wheels ...", 0, 0);
		LCD.drawString("Press ENTER when sensor", 0, 1);
		LCD.drawString("is above black bar!", 0, 2);
		Button.ENTER.waitForPressAndRelease();
		LCD.clear(1);
		LCD.clear(2);
		
		int choice = 2;
	
		switch(choice){
			
		case 1:
			
			//Save the current sensor value as dark
			int dark = lightSensor.getNormalizedLightValue();
			lightSensor.setLow(dark);
			
			//Move forward until sensor notices a change of FACTORLIGHT
			motorWheels.resetTachoCount(); // avoids a possible out of bounds error
			motorWheels.setSpeed(WHEELMOTORMAXSPEED/5);
			while (lightSensor.getNormalizedLightValue() <= (FACTORLIGHT * dark)){
				motorWheels.forward();
			}
			motorWheels.stop();
			lightSensor.setHigh(lightSensor.getNormalizedLightValue());
			motorWheels.resetTachoCount(); 
			
			break;
			
		case 2:
			
			int light = lightSensor.getNormalizedLightValue();
			lightSensor.setHigh(light);
			LCD.drawString("light: " + String.valueOf(light), 0, 4);
			
			//Move backward until sensor notices a change of FACTORLIGHT
			motorWheels.setSpeed(WHEELMOTORMAXSPEED/5);
			
			motorWheels.backward();
			while (lightSensor.getNormalizedLightValue() >= (light / FACTORLIGHT)){
			}
			motorWheels.stop();
			
			lightSensor.setLow(lightSensor.getNormalizedLightValue());
			LCD.drawString("light: " + String.valueOf(lightSensor.getNormalizedLightValue()), 0, 5);
			motorWheels.resetTachoCount();
			
			break;
		}
		
		//Move forward so the pen is at y = 0
		moveWheels(DISTANCETOLIGHTSENSOR - ArmModule.getArmLength());
		motorWheels.resetTachoCount();
		
		LCD.clear();
		LCD.drawString("Wheel calibration", 0, 0);
		LCD.drawString("successful!", 0, 1);
	}
}
