package de.tuhh.diss.plotbot.robot;

import de.tuhh.diss.plotbot.exceptions.OutOfWorkspaceException;
import de.tuhh.diss.plotbot.robot.*;
import de.tuhh.diss.plotbot.utilities.Calc;
import lejos.nxt.LCD;

public class DrawControlChris {
	
	///////////////////////////////////////////////////////
	//	VARIABLES
	///////////////////////////////////////////////////////
	
	/**
	 * Tolerance that is used for reaching a target
	 */
	private final int TARGET_TOLERANCE = 3;
	
	/**
	 * Instance of the robot
	 */
	PhysicalRobot robot = PhysicalRobot.ROBOT;
	
	/**
	 *  Current x position of the pen
	 */
	private double xNow;
	
	/**
	 * Current y position of the pen
	 */
	private double yNow;
	
	
	
	///////////////////////////////////////////////////////
	//	METHODS
	///////////////////////////////////////////////////////		
	
	/** 
	 *  Moves the pen to a certain position 
	 *  by moving arm and wheels at the same time continously.
	 *  
	 *  This is only meant for moving the pen to a position
	 *  or drawing a vertical line, horizontal lines can not 
	 *  be plotted with this.
	 * 
	 * @param xTarget x coordinate of the target
	 * @param yTarget y coordinate of the target
	 */
	public void movePenTo(double xTarget, double yTarget) throws OutOfWorkspaceException{
		double distanceToTravel = yTarget - robot.getYCenter() - getYCenterToPen();
		
		robot.moveArmTo(getArmAngleForX(xTarget));
		robot.moveWheels(distanceToTravel);
	}
	
	
	/** 
	 * Moves the pen to a certain target in steps
	 * 
	 * By using small steps the pen will move in 
	 * a straight line (in theory :P)
	 *  
	 * @param xTarget target x-position of pen
	 * @param yTarget target y-position of pen
	 * @param stepSize the millimeters taken per step
	 * @throws OutOfWorkspaceException
	 */
	//Bewege Arm solang auf ziel zu bis innerhalb Toleranz (Alle Referenzen ueber Motor) NUR X WIRD BETRACHTET -> NUR HORIZONTAL
	public void movePenToInStepsV1(double xTarget, double yTarget, int stepSize) throws OutOfWorkspaceException{
		double nextX, yCorrection, nextWheelSpeed;
		
		while(!xIsReached(xTarget)){
			nextX = getNext(xTarget, xNow, stepSize);
			yCorrection = getYCorrection(nextX);
			nextWheelSpeed = getNextWheelSpeed(nextX - xNow, yCorrection);
			
			printDebugMsg(nextX, 0, nextWheelSpeed, yCorrection);
			
			robot.setWheelSpeed(nextWheelSpeed);
			robot.moveArmTo(getArmAngleForX(nextX),true);
			robot.moveWheels(yCorrection);
			robot.waitForArm();
		}
		
		updateXNow();
		updateYNow();
	}
	
	/**
	 * Calculates the next coordinate in a certain direction 
	 * 
	 * Returns current position + stepSize until the remaining distance to the target
	 * is smaller than stepSize, then returns the target itself
	 * 
	 * @param x_yTarget x or y coordinate of the target
	 * @param x_yNow current x or y coordinate 
	 * @param stepSize the millimeters taken per step 
	 * @return next x or y coordinate
	 * @throws OutOfWorkspaceException
	 */
	public double getNext(double x_yTarget, double x_yNow, int stepSize) throws OutOfWorkspaceException{
		//if moving left or down
		if(x_yTarget - x_yNow < 0){
			stepSize = -stepSize;
		}
		
		if (Math.abs(x_yTarget - x_yNow) >= Math.abs(stepSize)){
			return x_yNow + stepSize;
		} else {
			return x_yTarget;
		}
	}
	
	/**
	 * 
	 * @param distanceToGoX
	 * @param distanceToGoY
	 * @return
	 */
	public double getNextWheelSpeed(double distanceToGoX, double distanceToGoY){
		double armSpeed = robot.getArmMotorSpeed() / ArmModule.ARMGEARRATIO;
		double angleToGo = getAngleToTarget(xNow + distanceToGoX);
		double timeNeededForX = Math.abs(angleToGo) / armSpeed;
		double revolutionsWheelNeeded = distanceToGoY / WheelsModule.WHEELDIAMETER;
		double revolutionsMotorNeeded = revolutionsWheelNeeded * WheelsModule.WHEELGEARRATIO;
		
		return revolutionsMotorNeeded * 360 / timeNeededForX;
	}
	
	
	//Variante fuer y Abweichungen mit einbeziehen AUCH HIER NUR FUER HORIZONTALE LINIEN
	public void movePenToInStepsV2(double xTarget, double yTarget, int stepSize) throws OutOfWorkspaceException{
		double nextX, nextY, yCorrection, nextWheelSpeed;

		
		while(!xyIsReached(xTarget, yTarget)){
			nextX = getNext(xTarget, xNow, stepSize);
			nextY = getNext(yTarget, yNow, stepSize);
			yCorrection = getYCorrection(nextX);
			nextWheelSpeed = getNextWheelSpeed(nextX - xNow, (nextY - yNow) + yCorrection);
			
			printDebugMsg(nextX, nextY, nextWheelSpeed, yCorrection);
			
			robot.setWheelSpeed(nextWheelSpeed);
			robot.moveArmTo(getArmAngleForX(nextX),true);
			robot.moveWheels(nextY + yCorrection);
			robot.waitForArm();
		}
		
		updateXNow();
		updateYNow();
	}
	
	
	// Variante tasächlich Laenge wird zerlegt HIERMIT KANN MAN AUCH DIAGONAL ... THEORETISCH
	public void movePenToInStepsV3(double xTarget, double yTarget, int stepSize) throws OutOfWorkspaceException{
		double nextX, nextY, yCorrection, nextWheelSpeed;

		while(!xyIsReached(xTarget, yTarget)){
			nextX = getNextX(xTarget, yTarget, stepSize);
			nextY = getNextY(xTarget, yTarget, stepSize);
			yCorrection = getYCorrection(nextX);
			nextWheelSpeed = getNextWheelSpeed(nextX - xNow, (nextY - yNow) + yCorrection);
			
			printDebugMsg(nextX, nextY, nextWheelSpeed, yCorrection);
			
			robot.setWheelSpeed(nextWheelSpeed);
			robot.moveArmTo(getArmAngleForX(nextX),true);
			robot.moveWheels(nextY + yCorrection);
			robot.waitForArm();
		}
		
		updateXNow();
		updateYNow();
	}
	
	
	public double getNextX(double xTarget, double yTarget, int stepSize){
		double distanceX = xTarget - xNow;
		double distanceY = yTarget - yNow;
		double totalDistance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
		double alpha = Math.atan2(distanceX, distanceY);
		
		//if moving left
		if(distanceX < 0){
			stepSize = -stepSize;
		}
		
		if(Math.abs(totalDistance) >= Math.abs(stepSize)){
				return xNow + (stepSize * Math.cos(alpha));
		} else {
				return xNow + (totalDistance * Math.cos(alpha));
		}
	}
	
	public double getNextY(double xTarget, double yTarget, int stepSize){
		double distanceX = xTarget - xNow;
		double distanceY = yTarget - yNow;
		double totalDistance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
		double alpha = Math.atan2(distanceX, distanceY);
		
		//if moving down
		if(distanceY < 0){
			stepSize = -stepSize;
		}
		
		if(Math.abs(totalDistance) >= Math.abs(stepSize)){
				return yNow + (stepSize * Math.sin(alpha));
		} else {
				return yNow + (totalDistance * Math.sin(alpha));
		}
	}
	
	
	
	/**
	 * Calculates the distance that has to be corrected in y-direction
	 * when the arm moves from where it is to xTarget
	 * 
	 * @param xTarget x-coordinate of the target position
	 * @return the distance that needs to be corrected (deviance * -1)
	 */
	private double getYCorrection(double xTarget){
		return - ArmModule.ARMLENGTH * (Math.sin(getArmAngleForX(xTarget)) - Math.sin(robot.getArmAngle()));
	}
	
	/**
	 * Returns the angle the arm would be in for a certain x
	 * 
	 * @param x the x-coordinate used for calculation
	 * @return the angle the arm would be in for a certain x
	 */
	private double getArmAngleForX(double x){
		return Math.toDegrees(Math.acos(x/ArmModule.ARMLENGTH));
	}
	
	/**
	 * Calculates the total angle that has to be covered  by the arm in order to reach a target
	 * 
	 * @param xTarget x coordinate of the target
	 */
	private double getAngleToTarget(double xTarget){
		double startAngle = getArmAngleForX(xNow);
		double endAngle = getArmAngleForX(xTarget);
		return endAngle - startAngle;
	}
	
	/**
	 * Checks whether a certain point is reached with a certain tolerance
	 * 
	 * @param xTarget x-coordinate of the point in question
	 * @param yTarget y-coordinate of the point in question
	 * @return true if point has been reached, false if not
	 */
	private boolean xyIsReached(double xTarget, double yTarget){
		return ( xIsReached(xTarget) && yIsReached(yTarget) );
	}
	
	/**
	 * Checks whether the x coordinate of a certain point has been reached
	 * 
	 * @param xTarget x-coordinate of the point in question
	 * @return true if x has been reached, false if not
	 */
	private boolean xIsReached(double xTarget){
		updateXNow();
		
		xNow = Math.abs(xNow);
		xTarget = Math.abs(xTarget);
		
		return (xNow >= (xTarget - TARGET_TOLERANCE)) && (xNow <= (xTarget + TARGET_TOLERANCE));
	}
	
	/**
	 * Checks whether the y coordinate of a certain point has been reached
	 * 
	 * @param yTarget y-coordinate of the point in question
	 * @return true if y has been reached, false if not
	 */
	private boolean yIsReached(double yTarget){
		updateYNow();
		
		yNow = Math.abs(yNow);
		yTarget = Math.abs(yTarget);
		
		return (yNow >= (yTarget - TARGET_TOLERANCE)) && (yNow <= (yTarget + TARGET_TOLERANCE));
	}
	
	/**
	 * Updates the current x coordinate of the pen based on the current motor angle
	 */
	private void updateXNow(){
		xNow = (ArmModule.ARMLENGTH * Math.cos(Math.toRadians(robot.getArmAngle())));
	}
	
	/**
	 * Updates the current y coordinate of the pen based on the current motor angle
	 */
	private void updateYNow(){
		yNow = (robot.getYCenter() + getYCenterToPen());
	}
	
	/**
	 * Calculates the distance from the rotational center of the arm to where the pen is currently
	 * 
	 * @return distance from center to pen
	 */
	private double getYCenterToPen(){
		return (ArmModule.ARMLENGTH * Math.sin(Math.toRadians(robot.getArmAngle())));
	}
	
	private void printDebugMsg(double nextX, double nextY, double wheelSpeed, double YCorr){
		LCD.clear();
		LCD.drawString("Moving ...." + xNow, 0, 0);
		LCD.drawString("xNow: " + xNow, 0, 1);
		LCD.drawString("nextX: " + String.valueOf(nextX), 0, 2);
		LCD.drawString("yNow: " + yNow, 0, 3);
		LCD.drawString("nextY: " + String.valueOf(nextY), 0, 4);
		LCD.drawString("YCorr: " + String.valueOf(YCorr), 0, 5);
		LCD.drawString("whlspd: " + String.valueOf(wheelSpeed), 0, 6);
	}
}
