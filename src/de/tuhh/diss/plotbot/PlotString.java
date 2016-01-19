package de.tuhh.diss.plotbot;

import de.tuhh.diss.exceptions.MotorException;
import de.tuhh.diss.robot.RobotInterface;

public class PlotString {

	public RobotInterface robot;
	private static final double scale = 1/6;
	private static final int start = 230;
	private static final double center = 1/2;
	
	public double PlotString(int size){
	robot = PlotRectangle.getRobot();
	double nextY;
	
	nextY = getSpacingFrameToString(size, start);
	nextY = drawT(size, nextY);
	nextY = getSpacingStringToString(size, nextY);
	nextY = drawU(size, nextY);
	nextY = getSpacingStringToString(size, nextY);
	nextY = drawH(size, nextY);
	nextY = getSpacingStringToString(size, nextY);
	nextY = drawH(size, nextY);
	nextY = getSpacingFrameToString(size, nextY);
	
	return nextY;
	}
	
	private double getSpacingFrameToString(int size, double startY){
		return startY-2*scale*size;
	}
	
	private double getSpacingStringToString(int size, double startY){
		return startY-3*scale*size;
	}
	
	private double drawT(int size, double startY){
	double widthY = 4*scale*size;
	double endY = startY-widthY;
	
		try {
		new PlotVerticalLine(robot, center*size, startY, -widthY);
	} catch (MotorException e) {
		e.printStackTrace();
	}
	try {
		new PlotHorizontalLine(robot, center*size, startY-widthY*center, -size);
	} catch (MotorException e) {
		e.printStackTrace();
	}
	
	return endY;
	}
	
	private double drawU(int size, double startY){
		double widthY = 3*scale*size;
		double endY = startY-widthY;
		
		try {
			new PlotHorizontalLine(robot, center*size, startY, -size);
		} catch (MotorException e) {
			e.printStackTrace();
		}
		try {
			new PlotVerticalLine(robot, -center*size, startY, -widthY);
		} catch (MotorException e) {
			e.printStackTrace();
		}
		try {
			new PlotHorizontalLine(robot, -center*size, startY-widthY, size);
		} catch (MotorException e) {
			e.printStackTrace();
		}
		
		return endY;
		}
	
	private double drawH(int size, double startY){
		double widthY = 3*scale*size;
		double endY = startY-widthY;
		
		try {
			new PlotHorizontalLine(robot,  center*size, startY, -size);
		} catch (MotorException e) {
			e.printStackTrace();
		}
		try {
			new PlotVerticalLine(robot, 0, startY, widthY);
		} catch (MotorException e) {
			e.printStackTrace();
		}
		try {
			new PlotHorizontalLine(robot, -center*size, startY-widthY, size);
		} catch (MotorException e) {
			e.printStackTrace();
		}
		
		return endY;
		}	
	
	
}
