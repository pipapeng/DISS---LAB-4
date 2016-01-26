package de.tuhh.diss.plotbot.plotter;

import de.tuhh.diss.plotbot.exceptions.MotorException;
import de.tuhh.diss.plotbot.robot.RobotInterface;

public class PlotRectangle {
	
	
	private static final int START = 230;
	private static final int MINSIZE = 10;
	private static final int MAXSIZE = 170;
	
	private static final double CENTER = 1/2;
	
	private RobotInterface robot;
	
	
	
	public PlotRectangle(RobotInterface robot) throws MotorException{
		this.robot = robot;
	}
	
	public int getMinSize() {
	
		return MINSIZE;
	}

	public int getMaxSize() {

		return MAXSIZE;
	}
	
	public void plot(double size, boolean square) throws MotorException{
		
		double height = size;
		double width = calcWidth(height, square);
		
		new PlotVerticalLine(robot, -height*CENTER, START, -width);
		new PlotHorizontalLine(robot, -height*CENTER, START-width, height);
		new PlotVerticalLine(robot, height*CENTER, START-width, width);
		new PlotHorizontalLine(robot, height*CENTER, START, -height);
	}
	
	private double calcWidth(double height, boolean square){
		
		if (square){
			return height;
		}else{
			return 2.6*height;
		}	
	}

	
	
}