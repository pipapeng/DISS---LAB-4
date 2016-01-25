package de.tuhh.diss.plotbot.plotter;

import de.tuhh.diss.plotbot.exceptions.MotorException;
import de.tuhh.diss.plotbot.robot.RobotInterface;

public class PlotTUHH {

	private static final double CENTER = 1/2;
	private static final double SCALE = 1/6;
	
	private static final int START = 230;
	private static final int MINSIZE = 10;
	private static final int MAXSIZE = (int) (START*(26*SCALE));
	
	private RobotInterface robot;
	
	
	
	public PlotTUHH(RobotInterface robot){
		this.robot = robot;
	}
	
	public int getMinSize(){
		return MINSIZE;
	}
	
	public int getMaxSize(){
		return MAXSIZE;
	}
	
	public double plot(double size) throws MotorException{
		
		double nextY;
		
		nextY = getSpacingFrameToString(size, START);
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
	
	private double getSpacingFrameToString(double size, double startY){
		return startY-2*SCALE*size;
	}
	
	private double getSpacingStringToString(double size, double startY){
		return startY-3*SCALE*size;
	}
	
	private double drawT(double size, double startY) throws MotorException{
		double widthY = 4*SCALE*size;
		double endY = startY-widthY;
	
		new PlotVerticalLine(robot, CENTER*size, startY, -widthY);
		new PlotHorizontalLine(robot, CENTER*size, startY-widthY*CENTER, -size);
			
		return endY;
	}
	
	private double drawU(double size, double startY) throws MotorException{
		
		double widthY = 3*SCALE*size;
		double endY = startY-widthY;
		
		new PlotHorizontalLine(robot, CENTER*size, startY, -size);
		new PlotVerticalLine(robot, -CENTER*size, startY, -widthY);
		new PlotHorizontalLine(robot, -CENTER*size, startY-widthY, size);
	
		return endY;
	}
	
	private double drawH(double size, double startY) throws MotorException{
		double widthY = 3*SCALE*size;
		double endY = startY-widthY;
		
		new PlotHorizontalLine(robot,  CENTER*size, startY, -size);
		new PlotVerticalLine(robot, 0, startY, widthY);
		new PlotHorizontalLine(robot, -CENTER*size, startY-widthY, size);
	
		return endY;
	}		
}
