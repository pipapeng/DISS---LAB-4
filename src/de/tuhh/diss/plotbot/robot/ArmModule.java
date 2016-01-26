package de.tuhh.diss.plotbot.robot;

import de.tuhh.diss.plotbot.UserInterface;
import de.tuhh.diss.plotbot.exceptions.OutOfWorkspaceException;
import de.tuhh.diss.plotbot.robot.ShittyMotor;
import de.tuhh.diss.plotbot.robot.SlackMotor;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.TachoMotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.util.Delay;

public class ArmModule{
	
	///////////////////////////////////////////////////////
	//	VARIABLES
	///////////////////////////////////////////////////////
	
	public static final int ARMGEARRATIO = 85;
	public static final int ARMLENGTH = 80;
	public static float ARMMOTORMAXSPEED;
	public static float ARMMOTORMINSPEED = 0;
	
	private int slackAngle = 450; //TODO: needs to be measured!!!
	private int armMinAngle;
	private int armMaxAngle; 
	private SlackMotor motorArm;
	private TouchSensor sensorArm;
	private TachoMotorPort port = MotorPort.A;
	

	///////////////////////////////////////////////////////
	//	METHODS
	///////////////////////////////////////////////////////	

	public ArmModule(){
		motorArm = new SlackMotor(port, slackAngle);
		ARMMOTORMAXSPEED = motorArm.getMaxSpeed();
		sensorArm = new TouchSensor(SensorPort.S1);
	}
	
	public static int getArmLength(){
		return ARMLENGTH;
	}
	
	public int getArmMinAngle(){
		return armMinAngle;
	}
	
	public int getArmMaxAngle(){
		return armMaxAngle;
	}
	
	/**
	 * @return the speed of the actual arm, not the motor angle  in degrees/s
	 */
	public int getRotationSpeed(){
		return motorArm.getRotationSpeed()/ARMGEARRATIO;
	}
	
	/** Returns the current angle of the arm
	 *  since the motor angle is zero where the arm hits the touch sensor
	 *  armMinAngle needs to be added to motorArm.getTachoCount()/ARMGEARRATIO
	 * 
	 * @return the actual arm angle, not the motor angle 
	 */
	public double getAngle(){
			return motorArm.getTachoCount()/ARMGEARRATIO + armMinAngle;
	}
	
	/** Sets the speed of the arm. Checks whether the desired speed is 
	 * within the boundaries of the motor, if not it sets a default speed
	 * 
	 * @param armSpeed desired speed that should be set
	 */
	public void setArmSpeed(double armSpeed){
		double armMotorSpeed = armSpeed * ARMGEARRATIO;
		
		if (armMotorSpeed >= ARMMOTORMINSPEED && armMotorSpeed <= ARMMOTORMAXSPEED){
			motorArm.setSpeed((float)armMotorSpeed);
		}
		else{
			motorArm.setSpeed((float)ARMMOTORMAXSPEED/2);
			LCD.drawString("Wrong armspeed,", 0, 1); //TODO:ONLY FOR DEBUG
			LCD.drawString("set default speed", 0, 2); //TODO:ONLY FOR DEBUG
		}
	}
	
	
	/** Moves the arm to a set angle 
	 * 
	 * @param targetAngle the desired target angle
	 * @throws OutOfWorkspaceException thrown when the target is not inside workspace
	 */
	public void moveArmTo(double targetAngle) throws OutOfWorkspaceException{
		double targetMotorAngle = (targetAngle-armMinAngle) * ARMGEARRATIO;
		
		if (targetAngle >= armMinAngle && targetAngle <= armMaxAngle){
			motorArm.rotateTo((int) Math.round(targetMotorAngle));
		}
		else{
			throw new OutOfWorkspaceException();
		}
	}
		
	/** Moves arm to set angle, will be interrupted by new commands
	 *  if immediateReturn is set true
	 *  
	 * @param targetAngle the desired target angle
	 * @param immediateReturn if true motor will stop on new command 
	 * @throws OutOfWorkspaceException thrown when the target is not inside workspace
	 */
	public void moveArmTo(double targetAngle, boolean immediateReturn) throws OutOfWorkspaceException{
		double targetMotorAngle = (targetAngle-armMinAngle) * ARMGEARRATIO;
		
		if (targetAngle >= armMinAngle && targetAngle <= armMaxAngle){
			motorArm.rotateTo((int) Math.round(targetMotorAngle), immediateReturn);
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
	
	/** Calibrates the arm motor and sets armMinAngle and armMaxAngle
	 * 	arm needs to be stopped manually by the user when in the middle
	 * 	which makes the calibration work for different robots
	 */
	//TODO: Calibrate slack and calibrate middle angle manually !
	public void calibrateMotorArm(){
		boolean wantToRepeat = true;
		double angleToMiddle;
		
		do{
			LCD.clear();
			LCD.drawString("Calibrating arm ...", 0, 0);
		
			//move arm all the way to the right and reset motorAngle
			motorArm.setSpeed((int)(ARMMOTORMAXSPEED*.75));
			motorArm.backward();
			while (!sensorArm.isPressed()){
			}
			stopArm();
			motorArm.resetTachoCount();
		
			//move arm left needs to be stopped manually when in the middle
			motorArm.rotateTo(100*ARMGEARRATIO,true);
			LCD.drawString("Press Enter when ", 0, 2);
			LCD.drawString("arm in middle!", 0, 3);
			Button.ENTER.waitForPressAndRelease();
			stopArm();
		
			//show the measured angle and ask if user wants to go again
			angleToMiddle = motorArm.getPosition()/ARMGEARRATIO;
			LCD.drawString("Angle: " + String.valueOf(angleToMiddle), 0, 1);
			LCD.drawString("Try Again?", 0, 2);
			LCD.drawString("Choice: ", 0, 3);
			wantToRepeat = UserInterface.chooseYesNo(9, 3);
		}while(wantToRepeat == true);
		LCD.clear();
		
		armMaxAngle = (int) (90 + angleToMiddle);
		armMinAngle = (int) (90 - angleToMiddle);
		LCD.drawString("Min: " + String.valueOf(armMinAngle), 0, 4);
		LCD.drawString("Max: " + String.valueOf(armMaxAngle), 0, 5);
		LCD.drawString("Arm calibration", 0, 0);
		LCD.drawString("successful!", 0, 1);
		Delay.msDelay(2000);
	}
	

	
}

