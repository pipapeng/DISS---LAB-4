package de.tuhh.diss.plotbot.plotter;

import de.tuhh.diss.plotbot.exceptions.MotorException;
import de.tuhh.diss.plotbot.exceptions.OutOfWorkspaceException;
import de.tuhh.diss.plotbot.robot.RobotInterface;

public abstract class PlotLines {
	
	private static final int AMOUNTOFSTEPS = 10;
	private static final int MINSIZE = 10;
	private static final int MAXSIZE = 230;
	
	protected static final int START = 230;
	protected static final double CENTER =	1/2;
	
	
	protected RobotInterface robot;
	
	
	
	public PlotLines(RobotInterface robot){
		
		this.robot = robot;
	}
	
	public int getMinSize() {
		
		return MINSIZE;
	}

	public int getMaxSize() {

		return MAXSIZE;
	}
	
	protected void plotHorizontalLine(double xStart, double yStart, double length) throws MotorException{
		
		robot.movePenToLennart(xStart, yStart);
		robot.setPen(true);
		robot.movePenHorizontalLennart(xStart, yStart, length, AMOUNTOFSTEPS);
		robot.setPen(false);
	}
	
	protected void plotVerticalLine(double xStart, double yStart, double length) throws MotorException, OutOfWorkspaceException{
		
		robot.movePenToLennart(xStart, yStart);
		robot.setPen(true);
		robot.movePenVerticalLennart(xStart, yStart, length);
		robot.setPen(false);
	}
	
}
