package de.tuhh.diss.plotbot.plotter;

import de.tuhh.diss.plotbot.exceptions.OutOfWorkspaceException;
import de.tuhh.diss.plotbot.robot.RobotInterface;

public abstract class PlotLine {

	protected RobotInterface robot;

	public PlotLine(RobotInterface robot, double xStart, double yStart){
		
		this.robot = robot; 
		try {
			robot.movePenTo((int) (xStart),(int) (yStart)); //TODO int anpassen
		} catch (OutOfWorkspaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
