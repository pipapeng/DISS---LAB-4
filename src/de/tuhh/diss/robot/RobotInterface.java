package de.tuhh.diss.robot;

import de.tuhh.diss.exceptions.MotorException;

public interface RobotInterface {
	
	public void stopAllMotors() throws MotorException;
	public void movePenTo(double xTarget, double yTarget);
	
	/////** ARM **/////
	public int getArmLength();
	public int getArmMaxAngle();
	public double getArmAngle();
	public double getArmRotationSpeed();
	public void setArmSpeed(int speed) throws IndexOutOfBoundsException;
	public void moveArmTo(double angle) throws MotorException;
	public void moveArmTo(double armAngle, boolean immediateReturn) throws MotorException;
	public void waitForArm();
	public void stopArm();
	
	/////** PEN **/////	
	public void setPen(boolean IO);
	public void stopPen();
	
	/////** Wheels **/////	
	public int getMaxFeed();
	public double getFeed();
	public void setWheelSpeed(int speed) throws IndexOutOfBoundsException;
	public void moveWheels(double length) throws MotorException;
	public void moveWheelsForward() throws MotorException;
	public void moveWheelsBackward() throws MotorException;
	public void waitForWheels();
	public void stopWheels();	
}
