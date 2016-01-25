package de.tuhh.diss.plotbot.plotter;

import de.tuhh.diss.plotbot.robot.RobotInterface;

public abstract class PlotLine {

	protected RobotInterface robot;

	public PlotLine(RobotInterface robot, double xStart, double yStart){
		
		this.robot = robot; 
		robot.movePenTo(xStart, yStart);
	}
}
