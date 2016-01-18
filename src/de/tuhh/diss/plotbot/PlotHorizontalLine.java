package de.tuhh.diss.plotbot;

import de.tuhh.diss.plotbot.OutOfWorkspaceException;

public class PlotHorizontalLine extends PlotLine{

	public PlotHorizontalLine(RobotInterface robot,int xStart, int yStart, int length) throws MotorsHasBeenStoppedException{
		super(robot, xStart, yStart);
		robot.setPen(true);
		moveHorizontal(xStart,length);
		robot.setPen(false);
	}
	
	private int calcWheelSpeed(int angle){
		
		return (int) (robot.getArmLength() * robot.getArmRotationalSpeed() * Math.sin(Math.toRadians(angle)));
	}

	private void moveHorizontal(int xStart, int length) throws MotorsHasBeenStoppedException{
		
		try {
			//TODO: adjust speed 
			robot.moveArmTo(CoordTrans.getAngle(robot.getArmLength(), xStart+length), true);
		} catch (OutOfWorkspaceException e) {
			robot.stopAll();
			throw new MotorsHasBeenStoppedException();
		}	
		
		while(robot.getArmRotationalSpeed() != 0){
			
			robot.setWheelSpeed(calcWheelSpeed(robot.getArmAngle()));
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
	
//	public static void drawHorizontalLineAlternative(int xStart, int yStart, int length){
//		boolean lineDone = false;
//		double startAngle = CoordinateTransformer.cartesianToPolar(xStart, yStart, PhysicalRobot.ARMLENGTH_);
//		double endAngle = CoordinateTransformer.cartesianToPolar(xStart + Length, yStart, PhysicalRobot.ARMLENGTH_);
//		int steps = 100;
//		
//		robot.movePenTo(xStart, yStart);
//		robot.waitForArm();
//		robot.waitForRobot();
//		robot.movePenDown();
//		robot.waitForPen();
//	}
	
	
}
