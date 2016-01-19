package de.tuhh.diss.plotter;

import de.tuhh.diss.exceptions.MotorException;
import de.tuhh.diss.robot.RobotInterface;

public class PlotVerticalLine extends PlotLine{
	
	public PlotVerticalLine(RobotInterface robot,double xStart, double yStart, double length) throws MotorException{
		super(robot,xStart,yStart);
		robot.setPen(true);
		robot.moveWheels(length);
		robot.setPen(false);
	}
}
