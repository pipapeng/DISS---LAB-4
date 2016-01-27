package de.tuhh.diss.plotbot.plotter;

import de.tuhh.diss.plotbot.exceptions.MotorException;
import de.tuhh.diss.plotbot.exceptions.OutOfWorkspaceException;
import de.tuhh.diss.plotbot.robot.RobotInterface;

public class Plotter implements PlotterInterface{
	
	RobotInterface robot;
	PlotTUHH tuhh;
	PlotRectangle rectangle; 
	
	
	public Plotter(RobotInterface robot) throws MotorException{
		
		this.robot = robot;
		tuhh = new PlotTUHH(robot);
		rectangle = new PlotRectangle(robot);	
	}

	public int getMinSizeRectangle() {
	
		return rectangle.getMinSize();
	}

	public int getMaxSizeRectangle() {
		
		return rectangle.getMaxSize();
	}

	public int getMinSizeString() {
		
		return tuhh.getMinSize();
	}

	public int getMaxSizeString() {
		
		return tuhh.getMaxSize();
	}
	
	public void plotRectangle(double size) throws MotorException, OutOfWorkspaceException{
		
		rectangle.plot(size, true);;
	}

	public void plotString(double size) throws MotorException, OutOfWorkspaceException{
		
		double rectangleSize = tuhh.plot(size);
		rectangle.plot(rectangleSize, false);
	}

	public void stopImmediatly() {
		// TODO bauen oder entfernen
		
	}
}
