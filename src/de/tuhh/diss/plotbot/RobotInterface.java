package de.tuhh.diss.plotbot;

public interface RobotInterface {
	
	public void stopAll();
	
	/////** ARM **/////
	public int getArmLength();
	public int getArmAngle();
	public int getMaxArmAngle();
	public int getArmRotationalSpeed();
	public int getMaxFeed();
	public void setArmSpeed(int speed) throws IndexOutOfBoundsException;
	public void moveArmTo(int angle) throws MotorsHasBeenStoppedException;
	public void moveArmTo(int armAngle, boolean immediateReturn) throws MotorsHasBeenStoppedException;
	public void waitForArm();
	public void stopArm();
	
	/////** PEN **/////	
	public void setPen(boolean IO);
	public void stopPen();
	
	/////** Wheels **/////	
	public int getFeed();
	public void setWheelSpeed(int speed) throws IndexOutOfBoundsException;
	public void moveWheels(int length) throws MotorsHasBeenStoppedException;
	public void moveWheelsForward() throws MotorsHasBeenStoppedException;
	public void moveWheelsBackward() throws MotorsHasBeenStoppedException;
	public void waitForWheels();
	public void stopWheels();	
}
