package de.tuhh.diss.plotbot;

import de.tuhh.diss.exceptions.MotorException;
import de.tuhh.diss.robot.RobotInterface;

public class PlotString {

	public RobotInterface robot;
	private static final double scale = 1/6;
	private static final int start = 230;
	private static final double center = 1/2;
	
	public PlotString(int size){
	robot = PlotRectangle.getRobot();
	drawT(size);
	drawU(size);
	drawH1(size);
	drawH2(size);
	}
	
	private void drawT(int s){
	try {
		new PlotVerticalLine(robot, center*s, start-2*scale*s, -4*scale*s);
	} catch (MotorException e) {
		e.printStackTrace();
	}
	try {
		new PlotHorizontalLine(robot, (int) (center*s), (int) (start-4*scale*s), -s);
	} catch (MotorException e) {
		e.printStackTrace();
	}
	}
	
	private void drawU(int s){
		try {
			new PlotHorizontalLine(robot, (int) (center*s), (int) (start-9*scale*s), -s);
		} catch (MotorException e) {
			e.printStackTrace();
		}
		try {
			new PlotVerticalLine(robot, (int) (-center*s), (int) (start-9*scale*s), (int) (-center*s));
		} catch (MotorException e) {
			e.printStackTrace();
		}
		try {
			new PlotHorizontalLine(robot, (int) (-center*s), (int) (start-12*scale*s), s);
		} catch (MotorException e) {
			e.printStackTrace();
		}
		}
	
	private void drawH1(int s){
		try {
			new PlotHorizontalLine(robot, (int) (center*s), (int) (start-15*scale*s), -s);
		} catch (MotorException e) {
			e.printStackTrace();
		}
		try {
			new PlotVerticalLine(robot, 0, (int) (start-15*scale*s), (int) (-3*scale*s));
		} catch (MotorException e) {
			e.printStackTrace();
		}
		try {
			new PlotHorizontalLine(robot, (int) (-center*s), (int) (start-18*scale*s), s);
		} catch (MotorException e) {
			e.printStackTrace();
		}
		}	
	
	private void drawH2(int s){
		try {
			new PlotHorizontalLine(robot, (int) (center*s), (int) (start-21*scale*s), -s);
		} catch (MotorException e) {
			e.printStackTrace();
		}
		try {
			new PlotVerticalLine(robot, 0, (int) (start-21*scale*s), (int) (-3*scale*s));
		} catch (MotorException e) {
			e.printStackTrace();
		}
		try {
			new PlotHorizontalLine(robot, (int) (-center*s), (int) (start-24*scale*s), s);
		} catch (MotorException e) {
			e.printStackTrace();
		}
		}
	
	
}
