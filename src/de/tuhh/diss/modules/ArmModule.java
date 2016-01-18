package de.tuhh.diss.modules;

import de.tuhh.diss.plotbot.MotorException;
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
	private final int ARMLENGTH = 80;
	private final int ARMMINANGLE = 0; //TODO: Messen
	private final int ARMMAXANGLE = 110; //TODO: Messen
	private final float ARMMAXSPEED;
	private final float ARMMINSPEED = 0;
	
	private int bearingPlay = 6;
	private NXTRegulatedMotor motorArm;
	private TouchSensor sensorArm;
	

	///////////////////////////////////////////////////////
	//	METHODS
	///////////////////////////////////////////////////////	

	public ArmModule() throws MotorException{
		motorArm = Motor.A;
		ARMMAXSPEED = motorArm.getMaxSpeed();
		sensorArm = new TouchSensor(SensorPort.S1);
		
		calibrateMotorArm();
	}
	
	public int getArmAngle(){return motorArm.getPosition() / ARMGEARRATIO;}
	public int getArmLength(){return ARMLENGTH;}
	public int getArmMaxAngle(){return ARMMAXANGLE;}
	public int getRotationSpeed(){return motorArm.getRotationSpeed();}
	
	public void setArmSpeed(int speed) throws IndexOutOfBoundsException{
		
		if (speed >= ARMMINSPEED && speed <= ARMMAXSPEED){
			motorArm.setSpeed(speed);
		}
		else{
			throw new IndexOutOfBoundsException();
		}
	}
	
	//TODO: was passiert wenn schon da?
	public void moveArmTo(int targetAngle) throws MotorException{
			
		if (targetAngle >= ARMMINANGLE && targetAngle <= ARMMAXANGLE){
				
			int motorAngle = targetAngle * ARMGEARRATIO;
				
			if (motorArm.getPosition() != motorAngle){
				if(motorArm.getPosition() < motorAngle){
					motorAngle = motorAngle + bearingPlay;
				}
				if(motorArm.getPosition() > motorAngle){
					motorAngle = motorAngle - bearingPlay;	
				}
			}
		}
		else{
			stopArm();
			throw new MotorException();
		}
	}
		
	//TODO: Bearing play untersuchen
	public void moveArmTo(int targetAngle, boolean immediateReturn) throws MotorException{
			
		if (targetAngle >= ARMMINANGLE && targetAngle <= ARMMAXANGLE){
			
			int motorAngle = targetAngle * ARMGEARRATIO;
				
			if (motorArm.getPosition() != motorAngle){
				if(motorArm.getPosition() < motorAngle){
					motorAngle = motorAngle + bearingPlay;
				}
				if(motorArm.getPosition() > motorAngle){
					motorAngle = motorAngle - bearingPlay;	
				}
				motorArm.rotateTo(motorAngle, immediateReturn);
			}
		}
		else{
			stopArm();
			throw new MotorException();
		}
	}		
	
	public void waitForArm(){
		motorArm.waitComplete();
	}
	
	public void stopArm(){
		motorArm.stop();
	}
		
	private void calibrateMotorArm() throws MotorException{
		LCD.drawString("Calibrating arm ...", 0, 1);
		
		motorArm.setSpeed((int)(ARMMAXSPEED*.75));
		motorArm.backward();
		
		while (!sensorArm.isPressed()){
		}
		stopArm();
		motorArm.resetTachoCount();
		
		motorArm.setSpeed(ARMMAXSPEED);
		moveArmTo(ARMMAXANGLE/2);

		LCD.drawString("Arm calibrated successfully!", 0, 2);
	}
}
