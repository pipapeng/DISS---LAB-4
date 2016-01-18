package de.tuhh.diss.robot;

import de.tuhh.diss.exceptions.OutOfWorkspaceException;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class ArmModule{
	
	///////////////////////////////////////////////////////
	//	VARIABLES
	///////////////////////////////////////////////////////
	
	private final int ARMGEARRATIO = 85;
	private static final int ARMLENGTH = 80;
	private final int BEARINGPLAY = 6;	//half of the total bearing play. 		nomma MESSEN!!!
	private final int ARMMINANGLE = 30; //TODO: Messen
	private final int REST = ARMMINANGLE - BEARINGPLAY;	//TODO: umbenennen
	private final int ARMMAXANGLE = 180 - ARMMINANGLE; //TODO: bearingPlay
	private final float ARMMAXSPEED;
	private final float ARMMINSPEED = 0;
	
	private boolean armBehindMotor;
	
	private NXTRegulatedMotor motorArm;
	private TouchSensor sensorArm;
	

	///////////////////////////////////////////////////////
	//	METHODS
	///////////////////////////////////////////////////////	

	public ArmModule() throws OutOfWorkspaceException{
		motorArm = Motor.A;
		ARMMAXSPEED = motorArm.getMaxSpeed();
		
		sensorArm = new TouchSensor(SensorPort.S1);
		
		calibrateMotorArm();
	}
	
	public static int getArmLength(){return ARMLENGTH;}
	public int getArmMaxAngle(){return ARMMAXANGLE;}
	public int getRotationSpeed(){return motorArm.getRotationSpeed() / ARMGEARRATIO;}
	
	public double getAngle(){
		
		if(armBehindMotor == true){
			return REST - BEARINGPLAY + (motorArm.getPosition() / ARMGEARRATIO) ;
		}
		else{
			return REST + BEARINGPLAY + (motorArm.getPosition() / ARMGEARRATIO) ;
		}
	}
	
	public void setArmSpeed(int speed) throws IndexOutOfBoundsException{ //TODO: handle?
		
		if (speed >= ARMMINSPEED && speed <= ARMMAXSPEED){
			motorArm.setSpeed(speed);
		}
		else{
			throw new IndexOutOfBoundsException();
		}
	}
	
	public void moveArmTo(double targetAngle) throws OutOfWorkspaceException{
			
		if (targetAngle != getAngle() && targetAngle >= ARMMINANGLE && targetAngle <= ARMMAXANGLE){
				
			if(getAngle() < targetAngle){
				targetAngle = targetAngle + BEARINGPLAY;
				armBehindMotor = true;
			}
			else{
				targetAngle = targetAngle - BEARINGPLAY;
				armBehindMotor = false;
			}
				
			motorArm.rotateTo((int) Math.round(targetAngle * ARMGEARRATIO));
		}
		else{
			throw new OutOfWorkspaceException();
		}
	}
		
	
	public void moveArmTo(double targetAngle, boolean immediateReturn) throws OutOfWorkspaceException{
			
		if (targetAngle != getAngle() && targetAngle >= ARMMINANGLE && targetAngle <= ARMMAXANGLE){
			
			if(getAngle() < targetAngle){
				targetAngle = targetAngle + BEARINGPLAY;
				armBehindMotor = true;
			}
			else{
				targetAngle = targetAngle - BEARINGPLAY;
				armBehindMotor = false;
			}
				
			motorArm.rotateTo((int) Math.round(targetAngle * ARMGEARRATIO), immediateReturn);
		}
		else{
			throw new OutOfWorkspaceException();
		}
	}		
	
	public void waitForArm(){
		motorArm.waitComplete();
	}
	
	public void stopArm(){
		motorArm.stop();
	}
		
	private void calibrateMotorArm() throws OutOfWorkspaceException{
		LCD.drawString("Calibrating arm ...", 0, 1); //TODO: Lennart...
		
		motorArm.setSpeed((int)(ARMMAXSPEED*.75));
		motorArm.backward();
		
		while (!sensorArm.isPressed()){
		}
		stopArm();
		motorArm.resetTachoCount();
		armBehindMotor = false;
		
		motorArm.setSpeed(ARMMAXSPEED);
		moveArmTo(ARMMAXANGLE/2); 		//TODO: Das muss in calibrateMotorWheels

		LCD.drawString("Arm calibrated successfully!", 0, 2); //TODO: Lennart...
	}
}
