package de.tuhh.diss.robot;

import de.tuhh.diss.exceptions.MotorException;
import de.tuhh.diss.exceptions.OutOfWorkspaceException;
import de.tuhh.diss.plotbot.CoordTrans;
import lejos.nxt.LCD;



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
		try {
			pen = new PenModule();
			arm = new ArmModule();
			wheels = new WheelsModule();
		} catch (OutOfWorkspaceException e) {
			stopAllMotors();
		}
	}
	
	public void stopAllMotors(){
		stopArm();
		stopPen();
		stopWheels();
	}
	
	public void movePenTo(double xTarget, double yTarget){
		double yCenterToPen = CoordTrans.getYCenterToPen(getArmLength(), getArmAngle());
		double yCenterOfRobot = getFeed() - getArmLength();
		double distanceToTravel = yTarget - yCenterOfRobot - yCenterToPen;
		
		try{
			moveArmTo((int)CoordTrans.getAnglePen(getArmLength(), xTarget));
			moveWheels(distanceToTravel);
		} catch (MotorException motorException) {
			motorException.getMessage();
		} catch (OutOfWorkspaceException oowException) {
			
		}
	}
	
	
	/////////////////
	/////  ARM
	/////////////////
	
	public int getArmLength(){return arm.getArmLength();}
	public int getArmMaxAngle(){return arm.getArmMaxAngle();}
	public double getArmAngle(){return arm.getAngle();}
	public double getArmRotationSpeed(){return arm.getRotationSpeed();}
	
	public void setArmSpeed(int speed) throws IndexOutOfBoundsException {arm.setArmSpeed(speed);}
	
	public void moveArmTo(double targetAngle) throws MotorException{
		try {
			arm.moveArmTo(targetAngle);
		} catch (OutOfWorkspaceException e) {
			stopAllMotors();
			LCD.drawString("OoW in moveAtmTo(d)", 0, 8);
		}
	}
	
	public void moveArmTo(double targetAngle, boolean immediateReturn) throws MotorException{
		try {
			arm.moveArmTo(targetAngle, immediateReturn);
		} catch (OutOfWorkspaceException e) {
			stopAllMotors();
			LCD.drawString("OoW in moveArmTo(d,b)", 0, 8);
		}
	}
	
	public void waitForArm(){arm.waitForArm();}
	public void stopArm(){arm.stopArm();}
	
	
	/////////////////
	/////  PEN
	/////////////////
	
	public void setPen(boolean down){pen.setPen(down);}
	public void stopPen(){pen.stopPen();}
	
	
	/////////////////
	/////  WHEELS
	/////////////////
	
	public int getMaxFeed(){return wheels.getMaxFeed();}
	public double getFeed(){return wheels.getYCenter();}

	public void setWheelSpeed(double speed) throws IndexOutOfBoundsException {wheels.setWheelSpeed(speed);}
	
	public void moveWheels(double distance) throws MotorException{
		try {
			wheels.moveWheels(distance);
		} catch (OutOfWorkspaceException e) {
			stopAllMotors();
			LCD.drawString("OoW in moveWheels()", 0, 8);
		}
	}
	public void moveWheelsForward() throws MotorException{
		try {
			wheels.moveWheelsForward();
		} catch (OutOfWorkspaceException e) {
			stopAllMotors();
			LCD.drawString("OoW in moveWheelsForward()", 0, 8);
		}
	}
	
	public void moveWheelsBackward() throws MotorException{
		try {
			wheels.moveWheelsBackward();
		} catch (OutOfWorkspaceException e) {
			stopAllMotors();
			LCD.drawString("OoW in moveWheelsBackward()", 0, 8);
		}
	}
	
	public void waitForWheels(){wheels.waitForWheels();}
	public void stopWheels(){wheels.stopWheels();}
}
