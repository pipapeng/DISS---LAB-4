package de.tuhh.diss.plotbot.plotter;

import de.tuhh.diss.plotbot.exceptions.MotorException;
import de.tuhh.diss.plotbot.robot.RobotInterface;

public class PlotRectangle extends PlotLines{
	
	private static final int MINSIZE = 10;
	private static final int MAXSIZE = 120;			// bei workspace von 45° bis 135°
	
	
	public PlotRectangle(RobotInterface robot) throws MotorException{
		
		super(robot);
	}
	
	public int getMinSize(){
		
		return MINSIZE;
	}
	
	public int getMaxSize(){
		
		return MAXSIZE;
	}
	
	public void plot(double size, boolean square) throws MotorException{
		
		double height = size;
		double width = calcWidth(height, square);
		
		super.plotVerticalLine(-height*CENTER, START, -width);
		super.plotHorizontalLine(-height*CENTER, START-width, height);
		super.plotVerticalLine(height*CENTER, START-width, width);
		super.plotHorizontalLine(height*CENTER, START, -height);
	}
	
	private double calcWidth(double height, boolean square){
		
		if (square){
			return height;
		}else{
			return 2.6*height;
		}	
	}

	
	
}