package de.tuhh.diss.plotbot.robot;

import de.tuhh.diss.plotbot.exceptions.MotorException;
import de.tuhh.diss.plotbot.exceptions.OutOfWorkspaceException;
import de.tuhh.diss.plotbot.utilities.CoordTrans;
import lejos.nxt.LCD;
import lejos.util.Delay;



public class PhysicalRobot implements RobotInterface{

	///////////////////////////////////////////////////////
	//	VARIABLES
	///////////////////////////////////////////////////////
	
	private ArmModule arm;
	private PenModule pen;
	private WheelsModule wheels;
	
	
	///////////////////////////////////////////////////////
	//	METHODS
	///////////////////////////////////////////////////////	
	
	public PhysicalRobot(){
			pen = new PenModule();
			arm = new ArmModule();
			wheels = new WheelsModule();
	}
	
	/** Calls the calibration methods of every module
	 *  
	 *  @return true if calibration succeded, false if calibration failed
	 */
	public boolean calibrateMotors(){
		try{
			pen.calibrateMotorPen();
			arm.calibrateMotorArm();
			wheels.calibrateMotorWheels();
			return true;
			
		} catch (OutOfWorkspaceException e) {
			stopAllMotors();
			LCD.drawString(e.getMessage(), 0, 5);
			LCD.drawString("Calibration failed!", 0, 6);
			Delay.msDelay(2000);
			return false;
		}
	}
	
	/** Stops all motors at once
	 * 
	 */
	public void stopAllMotors(){
		stopArm();
		stopPen();
		stopWheels();
	}
	
	/** Moves the pen to a certain position 
	 *  by moving arm and wheels at the same time continously
	 * 
	 * @param xTarget x coordinate of the target
	 * @param yTarget y coordinate of the target
	 */
	public void movePenTo(double xTarget, double yTarget){
		double yCenterToPen = CoordTrans.getYCenterToPen(getArmLength(), getArmAngle());
		double yCenterOfRobot = getYCenter();
		double distanceToTravel = yTarget - yCenterOfRobot - yCenterToPen;
		
		try{
			moveArmTo((int)CoordTrans.getAnglePen(getArmLength(), xTarget), true);
			moveWheels(distanceToTravel);
		} catch (OutOfWorkspaceException oowException) {
			
		}
	}
	
	/** Moves the pen to a certain target in steps
	 *  by using small steps the pen will move in a straight line (in theory :P)
	 *  
	 *  
	 * @param xStart current x-position of pen
	 * @param yStart current y-position of pen
	 * @param xTarget target x-position of pen
	 * @param yTarget target y-position of pen
	 * @param steps the amount of steps the movement shall be divided in
	 * @throws OutOfWorkspaceException
	 */
	public void movePenToInSteps(double xStart, double yStart, double xTarget, double yTarget, int steps) throws OutOfWorkspaceException{
		int startAngle =(int) Math.round(CoordTrans.getAnglePen(getArmLength(), xStart));
		int endAngle =(int) Math.round(CoordTrans.getAnglePen(getArmLength(), xTarget));
		int angleDifference = endAngle - startAngle;
		int angleStep = angleDifference / steps;		
		int fromAngle = startAngle;
		int toAngle = startAngle + angleStep;
		double timePerStep = angleStep / getArmRotationSpeed();
		double yDevianceAngle;
		double yDevianceStep = (yTarget - yStart) / steps; 
		double yStep;
		double necessaryWheelspeed;
		
		for(int it = 0; it == steps; it++){
			yDevianceAngle = CoordTrans.getYCenterToPen(getArmLength(), toAngle) - CoordTrans.getYCenterToPen(getArmLength(), fromAngle);
			yStep = yDevianceAngle + yDevianceStep;
			necessaryWheelspeed = yStep / timePerStep;
			
			moveArmTo(toAngle);
			setWheelSpeed((int) Math.round(necessaryWheelspeed));
			moveWheels(Math.round(yStep));
			waitForArm();
			waitForWheels();
			
			fromAngle = toAngle;
			toAngle = toAngle + angleStep;
		}
	}
	
	
	/////////////////
	/////  ARM
	/////////////////
	
	public int getArmLength(){
		return ArmModule.getArmLength();
	}
	
	public int getArmMinAngle(){
		return arm.getArmMinAngle();
	}
	
	public int getArmMaxAngle(){
		return arm.getArmMaxAngle();
	}
	
	public double getArmAngle(){
		return arm.getAngle();
	}
	
	public double getArmRotationSpeed(){
		return arm.getRotationSpeed();
	}
	
	public void setArmSpeed(int speed) throws IndexOutOfBoundsException{
		arm.setArmSpeed(speed);
	}
	
	public void moveArmTo(double targetAngle) throws OutOfWorkspaceException{
			arm.moveArmTo(targetAngle);
	}
	
	public void moveArmTo(double targetAngle, boolean immediateReturn) throws OutOfWorkspaceException{
			arm.moveArmTo(targetAngle, immediateReturn);
	}
	
	public void waitForArm(){
		arm.waitForArm();
	}
	
	public void stopArm(){
		arm.stopArm();
	}
	
	
	/////////////////
	/////  PEN
	/////////////////
	
	public void setPen(boolean down){
		pen.setPen(down);
	}
	
	public void stopPen(){
		pen.stopPen();
	}
	
	
	/////////////////
	/////  WHEELS
	/////////////////
	
	public int getMaxFeed(){
		return wheels.getYCenterMax();
	}
	
	public double getYCenter(){
		return wheels.getYCenter();
	}

	public void setWheelSpeed(double speed) throws IndexOutOfBoundsException{
		wheels.setWheelSpeed(speed);
	}
	
	public void moveWheels(double distance){
		try {
			wheels.moveWheels(distance);
		} catch (OutOfWorkspaceException e) {
			stopAllMotors();
			LCD.drawString("OoW in moveWheels()", 0, 8);
		}
	}
	
	public void moveWheelsForward(){
		try {
			wheels.moveWheelsForward();
		} catch (OutOfWorkspaceException e) {
			stopAllMotors();
			LCD.drawString("OoW in moveWheelsForward()", 0, 8);
		}
	}
	
	public void moveWheelsBackward(){
		try {
			wheels.moveWheelsBackward();
		} catch (OutOfWorkspaceException e) {
			stopAllMotors();
			LCD.drawString("OoW in moveWheelsBackward()", 0, 8);
		}
	}
	
	public void waitForWheels(){
		wheels.waitForWheels();
	}
	
	public void stopWheels(){
		wheels.stopWheels();
	}
}
