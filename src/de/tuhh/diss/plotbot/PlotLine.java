package de.tuhh.diss.plotbot;

import de.tuhh.diss.exceptions.MotorException;
import de.tuhh.diss.exceptions.OutOfWorkspaceException;
import de.tuhh.diss.robot.RobotInterface;

public abstract class PlotLine {
	
	private static final int speed = 450;
	
	protected RobotInterface robot;

	public PlotLine(RobotInterface robot, int xStart, int yStart) throws MotorException{
		
		this.robot = robot; 
		moveToCoord(xStart, yStart);
	}

	protected void moveToCoord(int x, int y) throws MotorException{
		
		robot.setWheelSpeed(speed);
		robot.setArmSpeed(speed);
		
		try {
			robot.moveArmTo(CoordTrans.getAngle(robot.getArmLength(), x), true);
			robot.moveWheels(CoordTrans.getFeed(robot.getArmLength(), robot.getMaxFeed(), x, y) - CoordTrans.getYPosition(robot.getArmLength(), robot.getFeed(), robot.getArmAngle()));
		} catch (OutOfWorkspaceException e) {
			robot.stopAllMotors();
			throw new MotorException();
		}
		
				
		robot.waitForArm();
	}
}
