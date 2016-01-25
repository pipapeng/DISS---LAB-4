package de.tuhh.diss.plotbot.plotter;

import de.tuhh.diss.plotbot.robot.RobotInterface;


public class Plotter implements PlotterInterface{

	RobotInterface robot;
	
	public Plotter(RobotInterface robot){
		this.robot = robot;
	}

	@Override
	public int getMinSizeRectangle() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public int getMaxSizeRectangle() {
		// TODO Auto-generated method stub
		return 200;
	}

	@Override
	public int getMinSizeString() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public int getMaxSizeString() {
		// TODO Auto-generated method stub
		return 150;
	}
	
	@Override
	public void plotRectangle(double size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void plotString(double size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopImmediatly() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutDown() {					// brauch man das?
		// TODO Auto-generated method stub
		
	}

	
}
