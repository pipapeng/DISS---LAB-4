package de.tuhh.diss.plotbot.plotter;

import de.tuhh.diss.plotbot.exceptions.MotorException;
import de.tuhh.diss.plotbot.exceptions.OutOfWorkspaceException;
import de.tuhh.diss.plotbot.robot.RobotInterface;

public class PlotVerticalLine extends PlotLine{
	
	public PlotVerticalLine(RobotInterface robot,double xStart, double yStart, double length) throws MotorException{
		super(robot,xStart,yStart);
		robot.setPen(true);
		try {
			robot.moveWheels(length);
		} catch (OutOfWorkspaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		robot.setPen(false);
	}
}
