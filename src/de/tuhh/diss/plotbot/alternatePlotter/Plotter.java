package de.tuhh.diss.plotbot.alternatePlotter;

import de.tuhh.diss.plotbot.robot.RobotInterface;


public class Plotter{

	RobotInterface robot;
	
	public Plotter(RobotInterface robot){
		this.robot = robot;
	}

	public void plot(Shape shape){
		shape.plot();
	}
}
