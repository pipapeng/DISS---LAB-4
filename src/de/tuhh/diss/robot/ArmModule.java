package de.tuhh.diss.robot;

import de.tuhh.diss.exceptions.OutOfWorkspaceException;
import lejos.nxt.Button;
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
	private int bearingPlay = 6;	//half of the total bearing play. 		nomma MESSEN!!!
	private int armMinAngle; //TODO: Messen
	private int rest = armMinAngle - bearingPlay;	//TODO: umbenennen
	private int armMaxAngle; //TODO: bearingPlay
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
	public int getArmMaxAngle(){return armMaxAngle;}
	public int getRotationSpeed(){return motorArm.getRotationSpeed() / ARMGEARRATIO;}
	
	public double getAngle(){
		
		if(armBehindMotor == true){
			return rest - bearingPlay + (motorArm.getPosition() / ARMGEARRATIO) ;
		}
		else{
			return rest + bearingPlay + (motorArm.getPosition() / ARMGEARRATIO) ;
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
		//TODO: If targetAngle==getAngle -> throw exception ?! 	
		if (targetAngle != getAngle() && targetAngle >= armMinAngle && targetAngle <= armMaxAngle){
				
			if(getAngle() < targetAngle){
				targetAngle = targetAngle + bearingPlay;
				armBehindMotor = true;
			}
			else{
				targetAngle = targetAngle - bearingPlay;
				armBehindMotor = false;
			}
				
			motorArm.rotateTo((int) Math.round(targetAngle * ARMGEARRATIO));
		}
		else{
			throw new OutOfWorkspaceException();
		}
	}
		
	
	public void moveArmTo(double targetAngle, boolean immediateReturn) throws OutOfWorkspaceException{
			
		if (targetAngle != getAngle() && targetAngle >= armMinAngle && targetAngle <= armMaxAngle){
			
			if(getAngle() < targetAngle){
				targetAngle = targetAngle + bearingPlay;
				armBehindMotor = true;
			}
			else{
				targetAngle = targetAngle - bearingPlay;
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
		boolean repeat = false;
		do{
		LCD.clear();
		LCD.drawString("Calibrating arm ...", 0, 0); //TODO: Lennart...
		motorArm.setSpeed((int)(ARMMAXSPEED*.75));
		motorArm.backward();
		
		while (!sensorArm.isPressed()){
		}
		stopArm();
		
		motorArm.resetTachoCount();
		armBehindMotor = false;
		motorArm.rotateTo(100*ARMGEARRATIO,true);
		LCD.drawString("Press Escape!", 0, 2);
		Button.ESCAPE.waitForPressAndRelease();
		stopArm();
		
		double angleDifference = motorArm.getPosition()/ARMGEARRATIO;
		LCD.drawString("angleToMin: "+String.valueOf(angleDifference), 0, 1);
		
		
		LCD.drawString("Arm OK?: NO", 0, 2);
		LCD.drawString("Press Enter", 0, 3);
		while (Button.ENTER.isDown() == false){
			if(Button.RIGHT.isDown() || Button.LEFT.isDown()){
				LCD.drawString("Arm OK?: YES", 0, 2);
				repeat = true;
			}
		}
		
		armMaxAngle = (int) (90 + angleDifference);
		armMinAngle = (int) (90 - angleDifference);
		
		LCD.drawString("Min:"+String.valueOf(armMinAngle), 0, 4);
		LCD.drawString("Max:"+String.valueOf(armMaxAngle), 0, 5);
		
		}while(repeat == false);
		
		LCD.drawString("Arm calibrated successfully!", 0, 1); //TODO: Lennart..
	}
}
