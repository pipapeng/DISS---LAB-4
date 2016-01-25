package de.tuhh.diss.plotbot.plotter;

import de.tuhh.diss.plotbot.exceptions.MotorException;
import de.tuhh.diss.plotbot.robot.PhysicalRobot;
import de.tuhh.diss.plotbot.robot.RobotInterface;

public class PlotRectangle {
	public static RobotInterface robot;
	private static final double scale = 1/6;
	private static final int start = 230;
	private static final double center = 1/2;
	
	public PlotRectangle(int size, boolean square) throws MotorException{
	robot = new PhysicalRobot();	//TODO: Hier kann kein neuer robot erzeugt werden ! Muss übergeben werden
	int height, width;
	if (square==true){
	height = size;
	width = size;
	}else{
	height = (int) ((10*scale)*size);
	width = (int) ((26*scale)*size);
	}	
	draw(height, width);
	}
	
	private void draw(int h, int w){
	try {
		new PlotVerticalLine(robot, (int) (-h*center), start, -w);
	} catch (MotorException e) {
		e.printStackTrace();
	}
	try {
		new PlotHorizontalLine(robot, (int) (-h*center), start-w, h);
	} catch (MotorException e) {
		e.printStackTrace();
	}
	try {
		new PlotVerticalLine(robot, (int) (h*center), start-w, w);
	} catch (MotorException e) {
		e.printStackTrace();
	}
	try {
		new PlotHorizontalLine(robot, (int) (h*center), start, -h);
	} catch (MotorException e) {
		e.printStackTrace();
	}
	}
	
	public static RobotInterface getRobot(){
		return robot;
	}
}