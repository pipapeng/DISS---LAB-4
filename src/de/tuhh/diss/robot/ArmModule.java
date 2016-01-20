package de.tuhh.diss.robot;

import de.tuhh.diss.exceptions.OutOfWorkspaceException;
import de.tuhh.diss.plotbot.UserInterface;
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
		boolean wantToRepeat = true;
		double angleToMiddle;
		
		do{
			LCD.clear();
			LCD.drawString("Calibrating arm ...", 0, 0); //TODO: Lennart...
		
			//move arm all the way to the right and reset motorAngle
			motorArm.setSpeed((int)(ARMMAXSPEED*.75));
			motorArm.backward();
			while (!sensorArm.isPressed()){
			}
			stopArm();
			motorArm.resetTachoCount();
			armBehindMotor = false;
		
			//move arm left needs to be stopped manually when in the middle
			motorArm.rotateTo(100*ARMGEARRATIO,true);
			LCD.drawString("Press Enter when ", 0, 2);
			LCD.drawString("arm in middle!", 0, 3);
			Button.ESCAPE.waitForPressAndRelease();
			stopArm();
		
			//show the measured angle and ask if user wants to go again
			angleToMiddle = motorArm.getPosition()/ARMGEARRATIO;
			LCD.drawString("Angle: "+String.valueOf(angleToMiddle), 0, 1);
			LCD.drawString("Try Again?", 0, 2);
			LCD.drawString("Choice: ", 0, 3);
			wantToRepeat = UserInterface.chooseYesNo(9, 3);
		}while(wantToRepeat == true);
		
		armMaxAngle = (int) (90 + angleToMiddle);
		armMinAngle = (int) (90 - angleToMiddle);
		LCD.drawString("Min:"+String.valueOf(armMinAngle), 0, 4);
		LCD.drawString("Max:"+String.valueOf(armMaxAngle), 0, 5);
		LCD.drawString("Arm calibrated successfully!", 0, 1); //TODO: Lennart..
	}
	

	
}

