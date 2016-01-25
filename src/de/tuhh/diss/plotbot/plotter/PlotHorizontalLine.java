package de.tuhh.diss.plotbot.plotter;

import de.tuhh.diss.plotbot.exceptions.MotorException;
import de.tuhh.diss.plotbot.exceptions.OutOfWorkspaceException;
import de.tuhh.diss.plotbot.robot.RobotInterface;
import de.tuhh.diss.plotbot.utilities.CoordTrans;

public class PlotHorizontalLine extends PlotLine{

	public PlotHorizontalLine(RobotInterface robot,double xStart, double yStart, double length) throws MotorException{
		super(robot, xStart, yStart);
		robot.setPen(true);
		drawHorizontalLine(xStart,length);
		robot.setPen(false);
	}
	
	private int calcWheelSpeed(int angle){
		
		return (int) (robot.getArmLength() * robot.getArmRotationSpeed() * Math.sin(Math.toRadians(angle)));
	}

	private void drawHorizontalLine(double xStart, double length) throws MotorException{
		
		try {
			//TODO: adjust speed 
			robot.moveArmTo((int)CoordTrans.getAnglePen(robot.getArmLength(), xStart+length), true);
		} catch (OutOfWorkspaceException e) {
			robot.stopAllMotors();
			throw new MotorException();
		}	
		
		while(robot.getArmRotationSpeed() != 0){
			
			robot.setWheelSpeed(calcWheelSpeed((int)robot.getArmAngle()));
			robot.moveWheelsForward();
		}
		robot.stopWheels();
		
//		// old solution
//		
//		int maxArmAngle = robot.getMaxArmAngle();
//		
//		while(robot.getArmRotationalSpeed() != 0){
//			robot.setWheelSpeed(calcWheelSpeed(robot.getArmAngle()));
//			int rotationalSpeed = robot.getArmRotationalSpeed();
//			int armAngle = robot.getArmAngle(); 
//			if((armAngle < .5*maxArmAngle && rotationalSpeed < 0) || (armAngle > .5*maxArmAngle && rotationalSpeed > 0)){
//				robot.moveWheelsForward();
//			}
//			else if((armAngle < .5*maxArmAngle && rotationalSpeed > 0) || (armAngle > .5*maxArmAngle && rotationalSpeed < 0)){
//				robot.moveWheelsBackward();
//			} else
//				robot.stopWheels();
//		}
//		robot.stopWheels();
	}
	

}
