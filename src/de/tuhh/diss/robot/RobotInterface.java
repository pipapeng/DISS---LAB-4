package de.tuhh.diss.robot;

import de.tuhh.diss.exceptions.MotorException;

public interface RobotInterface {
	
	public void stopAllMotors();
	public void movePenTo(int xTarget, int yTarget);
	
	/////** ARM **/////
	public int getArmLength();
	public int getArmAngle();
	public int getArmMaxAngle();
	public int getArmRotationSpeed();
	public int getMaxFeed();
	public void setArmSpeed(int speed) throws IndexOutOfBoundsException;
	public void moveArmTo(int angle) throws MotorException;
	public void moveArmTo(int armAngle, boolean immediateReturn) throws MotorException;
	public void waitForArm();
	public void stopArm();
	
	/////** PEN **/////	
	public void setPen(boolean IO);
	public void stopPen();
	
	/////** Wheels **/////	
	public int getFeed();
	public void setWheelSpeed(int speed) throws IndexOutOfBoundsException;
	public void moveWheels(int length) throws MotorException;
	public void moveWheelsForward() throws MotorException;
	public void moveWheelsBackward() throws MotorException;
	public void waitForWheels();
	public void stopWheels();	
}
