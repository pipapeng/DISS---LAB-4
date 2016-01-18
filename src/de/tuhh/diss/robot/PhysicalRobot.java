package de.tuhh.diss.robot;

import de.tuhh.diss.plotbot.CoordTrans;
import de.tuhh.diss.plotbot.MotorException;
import de.tuhh.diss.plotbot.OutOfWorkspaceException;
import de.tuhh.diss.plotbot.RobotInterface;
import de.tuhh.diss.robot.*;


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
	
	public PhysicalRobot() throws MotorException{
		
		arm = new ArmModule();
		pen = new PenModule();
		wheels = new WheelsModule();
	}
	
	public void stopAllMotors(){
		stopArm();
		stopPen();
		stopWheels();
	}
	
	public void movePenTo(int xTarget, int yTarget){
		int yCenterToPen = CoordTrans.getYCenterToPen(getArmLength(), getArmAngle());
		int yCenterOfRobot = getFeed() - getArmLength();
		int distanceToTravel = yTarget - yCenterOfRobot - yCenterToPen;
		
		try{
			moveArmTo(CoordTrans.getAngle(getArmLength(), xTarget));
			moveWheels(distanceToTravel);
		} catch (MotorException motorException) {
			motorException.getMessage();
		} catch (OutOfWorkspaceException oowException) {
			
		}
	}
	
	
	/////////////////
	/////  ARM
	/////////////////
	
	public int getArmAngle(){return arm.getArmAngle();}
	public int getArmLength(){return arm.getArmLength();}
	public int getArmMaxAngle(){return arm.getArmMaxAngle();}
	public int getArmRotationSpeed(){return arm.getRotationSpeed();}
	
	public void setArmSpeed(int speed) throws IndexOutOfBoundsException {arm.setArmSpeed(speed);}
	public void moveArmTo(int targetAngle) throws MotorException{arm.moveArmTo(targetAngle);}
	public void moveArmTo(int targetAngle, boolean immediateReturn) throws MotorException{arm.moveArmTo(targetAngle, immediateReturn);}
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
	
	public int getFeed(){return wheels.getFeed();}
	public int getMaxFeed(){return wheels.getMaxFeed();}

	public void setWheelSpeed(int speed) throws IndexOutOfBoundsException {wheels.setWheelSpeed(speed);}
	public void moveWheels(int distance) throws MotorException {wheels.moveWheels(distance);}
	public void moveWheelsForward() throws MotorException{wheels.moveWheelsForward();}
	public void moveWheelsBackward() throws MotorException{wheels.moveWheelsBackward();}
	public void waitForWheels(){wheels.waitForWheels();}
	public void stopWheels(){wheels.stopWheels();}
}
