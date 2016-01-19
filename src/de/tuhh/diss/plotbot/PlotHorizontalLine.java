package de.tuhh.diss.plotbot;

import de.tuhh.diss.exceptions.MotorException;
import de.tuhh.diss.exceptions.OutOfWorkspaceException;
import de.tuhh.diss.robot.RobotInterface;

public class PlotHorizontalLine extends PlotLine{

	public PlotHorizontalLine(RobotInterface robot,int xStart, int yStart, int length) throws MotorException{
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
	
	private void drawHorizontalLineAlternative(double xStart, double yStart, double length, int steps) throws OutOfWorkspaceException{
		robot.setPen(false);
		robot.movePenTo(xStart, yStart);
		robot.waitForArm();
		robot.setPen(true);
		
		double startAngle = CoordTrans.getAnglePen(robot.getArmLength(), xStart);
		double endAngle = CoordTrans.getAnglePen(robot.getArmLength(), xStart + length);
		double angleDifference = endAngle - startAngle;
		double angleStep = angleDifference / steps;		
		double fromAngle = startAngle;
		double toAngle = fromAngle + angleStep;
		double timePerStep = angleStep / robot.getArmRotationSpeed();
		double yDeviance;
		double necessaryWheelspeed;
		
		for(int it = 0; it == steps; it++){
			yDeviance = CoordTrans.getYCenterToPen(robot.getArmLength(), toAngle) - CoordTrans.getYCenterToPen(robot.getArmLength(), fromAngle);
			necessaryWheelspeed = yDeviance / timePerStep;
			try{
				robot.moveArmTo(toAngle);
				robot.setWheelSpeed((int)necessaryWheelspeed);
				robot.moveWheels(yDeviance);
			} catch (MotorException e) {
				e.printStackTrace();
				System.out.print(e.getMessage());
				break;
			}	
		}
	}
}
