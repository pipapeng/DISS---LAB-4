package de.tuhh.diss.plotbot.plotter;

import de.tuhh.diss.plotbot.exceptions.MotorException;
import de.tuhh.diss.plotbot.robot.PhysicalRobot;
import de.tuhh.diss.plotbot.robot.RobotInterface;

public class PlotRectangle {
	private static final double SCALE = 1/6;
	private static final int START = 230;
	private static final double CENTER = 1/2;
	private RobotInterface robot;
	
	
	public PlotRectangle(RobotInterface robot) throws MotorException{
		this.robot = robot;
	}
	
	
	public void drawRectangle(int size, boolean square){
		int width, height;
		
		height = size;
		
		if (square){
		width = size;
		}else{
		width = (int) (2.6*size);
		}	
		draw(width, height);
	}
	
	
	
	private void draw(int height, int width){
	try {
		new PlotVerticalLine(robot, (int) (-height*CENTER), START, -width);
		new PlotHorizontalLine(robot, (int) (-height*CENTER), START-width, height);
		new PlotVerticalLine(robot, (int) (height*CENTER), START-width, width);
		new PlotHorizontalLine(robot, (int) (height*CENTER), START, -height);
	} catch (MotorException e) {
		e.printStackTrace();
	}
	}
}