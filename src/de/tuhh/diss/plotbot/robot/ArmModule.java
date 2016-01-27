package de.tuhh.diss.plotbot.robot;

import de.tuhh.diss.plotbot.UserInterface;
import de.tuhh.diss.plotbot.exceptions.OutOfWorkspaceException;
import de.tuhh.diss.plotbot.robot.ShittyMotor;
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
	
	private boolean anglesAreSet = true;
	private int slackAngle = 730; //TODO: needs to be measured!!!
	private int motorAngleToMiddle = 4700;
	private int angleToMiddle;
	private int armMinAngle;
	private int armMaxAngle; 
	
	private ShittyMotor motorArm;
	private TouchSensor sensorArm;
	private TachoMotorPort port = MotorPort.A;
	

	///////////////////////////////////////////////////////
	//	METHODS
	///////////////////////////////////////////////////////	

	public ArmModule(){
		motorArm = new ShittyMotor(port, slackAngle);
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
	
	public static boolean isMoving(){   //TODO: REMOVE !
		return Motor.A.isMoving();
	}
	
	/**
	 * Returns the speed the motor is set to
	 * @return motor speed
	 */
	public int getMotorSpeed(){
		return motorArm.getSpeed();
	}
	
	/**
	 * This only works while the arm is actually moving and
	 * returns the current speed
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
	//TODO: Make settable for motor or arm !
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
	 * @throws OutOfWorkspaceException 
	 */
	//TODO: Calibrate slack and calibrate middle angle manually !
	public void calibrateMotorArm() throws OutOfWorkspaceException{
		
		LCD.clear();
		LCD.drawString("Calibrating arm ...", 0, 0);
		
		//move arm all the way to the right and reset motorAngle
		setArmSpeed((ARMMOTORMAXSPEED*.9)/ARMGEARRATIO);
		motorArm.backward();
		while (!sensorArm.isPressed()){
		}
		stopArm();
		motorArm.resetTachoCount();
					
		if(anglesAreSet == false){
			calibrateMotorAngles();
		} else {
			angleToMiddle = motorAngleToMiddle/ARMGEARRATIO;
			LCD.drawString("aToMid: " + angleToMiddle, 0, 7);
			armMaxAngle = (90 + angleToMiddle);
			armMinAngle = (90 - angleToMiddle);
			motorArm.rotateTo(motorAngleToMiddle, false);
		}
		

		
		LCD.clear();
		LCD.drawString("Min: " + String.valueOf(armMinAngle), 0, 4);
		LCD.drawString("Max: " + String.valueOf(armMaxAngle), 0, 5);
		LCD.drawString("Arm calibration", 0, 0);
		LCD.drawString("successful!", 0, 1);
		
		setArmSpeed((ARMMOTORMAXSPEED*.9)/ARMGEARRATIO);
		Delay.msDelay(2000);	
	}	
	
	public void calibrateMotorAngles(){
		
		setArmSpeed((ARMMOTORMAXSPEED*.6)/ARMGEARRATIO);
		//move to mid
		for(int i = 1 ; i < 8; i++){
			LCD.clear(i);
		}
		
		// Set where the middle is
		LCD.drawString("Move Robot from", 0, 2);
		LCD.drawString("the right into", 0, 3);
		LCD.drawString("mid position", 0, 4);
		LCD.drawString("Then press ENTER", 0, 6);
			
		do{
			while(Button.LEFT.isDown()){
				motorArm.forward();
			}
			while(Button.RIGHT.isDown()){
				motorArm.backward();
			}
			motorArm.stop();
			LCD.drawString("aToMidwSl: " + motorArm.getTachoCount(), 0, 7);
		} while(!Button.ENTER.isDown());
		int midAngleWithSlack = motorArm.getTachoCount();
		
		for(int i = 1 ; i < 8; i++){
			LCD.clear(i);
		}
		
		Delay.msDelay(1000);
		setArmSpeed((ARMMOTORMAXSPEED*.3)/ARMGEARRATIO);
		
		LCD.drawString("Move arm motor", 0, 2);
		LCD.drawString("to the right", 0, 3);
		LCD.drawString("until arm moves", 0, 4);
		LCD.drawString("Then press ENTER", 0, 6);
			
		do{
			while(Button.LEFT.isDown()){
				motorArm.forward();
			}
			while(Button.RIGHT.isDown()){
				motorArm.backward();
			}
			motorArm.stop();
			LCD.drawString("slack: " + (midAngleWithSlack - motorArm.getTachoCount()), 0, 7);
		} while(!Button.ENTER.isDown());
		int midAngleWithoutSlack = motorArm.getTachoCount();
		
		// Letzte gemessene Mitte minus "neue" Mitte (anderer anschlag)  
		int slack = midAngleWithSlack - midAngleWithoutSlack;
		motorArm.setSlackAngle(slack);
		
		angleToMiddle = motorAngleToMiddle/ARMGEARRATIO;
		LCD.drawString("aToMid: " + angleToMiddle, 0, 7);
		armMaxAngle = (90 + angleToMiddle);
		armMinAngle = (90 - angleToMiddle);
	}

	
}

