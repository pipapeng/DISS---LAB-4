package de.tuhh.diss.plotbot.plotter;

import de.tuhh.diss.plotbot.exceptions.MotorException;
import de.tuhh.diss.plotbot.exceptions.OutOfWorkspaceException;
import de.tuhh.diss.plotbot.robot.ArmModule;
import de.tuhh.diss.plotbot.robot.RobotInterface;
import de.tuhh.diss.plotbot.utilities.Calc;
import lejos.nxt.Button;
import lejos.nxt.LCD;

public class PlotRectangle extends PlotLines{
	
	private static final int MINSIZE = 10;
	private static final int MAXSIZE = 120;			// bei workspace von 45 bis 135
	
	
	public PlotRectangle(RobotInterface robot) throws MotorException{
		
		super(robot);
	}
	
	public int getMinSize(){
		
		return MINSIZE;
	}
	
	public int getMaxSize(){
		
		return MAXSIZE;
	}
	
public void plot(double size, boolean square) throws MotorException, OutOfWorkspaceException{
		
		double height = size;
		double width = calcWidth(height, square);
		
		LCD.clear();
		LCD.drawString("REALX: " + Calc.getXPositionPen(ArmModule.getArmLength(), robot.getArmAngle()), 0, 1);
		LCD.drawString("REALy: " + robot.getYCenter()+Calc.getYCenterToPen(ArmModule.getArmLength(), robot.getArmAngle()), 0, 2);
		
		LCD.drawString("XSTART: " + (-1*(height*CENTER)), 0, 4);
		LCD.drawString("YSTART: " + (START), 0, 4);
		LCD.drawString("Length:" + -width, 0, 5);
		LCD.drawString("ENTER", 0, 7);
		Button.ENTER.waitForPressAndRelease();
		LCD.clear(7);
		LCD.drawString("WAIT", 0, 7);
		super.plotVerticalLine((-1*(height*CENTER)), START, -width);
		LCD.clear(7);
		LCD.drawString("DONE - NEXT?", 0, 7);
		LCD.drawString("REALX: " + Calc.getXPositionPen(ArmModule.getArmLength(), robot.getArmAngle()), 0, 1);
		LCD.drawString("REALy: " + robot.getYCenter()+Calc.getYCenterToPen(ArmModule.getArmLength(), robot.getArmAngle()), 0, 2);
		
		Button.ENTER.waitForPressAndRelease();
		LCD.clear();
		LCD.drawString("REALX: " + Calc.getXPositionPen(ArmModule.getArmLength(), robot.getArmAngle()), 0, 1);
		LCD.drawString("REALy: " + robot.getYCenter()+Calc.getYCenterToPen(ArmModule.getArmLength(), robot.getArmAngle()), 0, 2);
		
		LCD.drawString("XSTART: " + -height*CENTER, 0, 4);
		LCD.drawString("YSTART: " + (START-width), 0, 4);
		LCD.drawString("Length:" + height, 0, 5);
		LCD.drawString("ENTER", 0, 7);
		Button.ENTER.waitForPressAndRelease();
		LCD.clear(7);
		LCD.drawString("WAIT", 0, 7);
		super.plotHorizontalLine(-height*CENTER, START-width, height);
		LCD.clear(7);
		LCD.drawString("DONE - NEXT?", 0, 7);
		LCD.drawString("REALX: " + Calc.getXPositionPen(ArmModule.getArmLength(), robot.getArmAngle()), 0, 1);
		LCD.drawString("REALy: " + robot.getYCenter()+Calc.getYCenterToPen(ArmModule.getArmLength(), robot.getArmAngle()), 0, 2);
		
		Button.ENTER.waitForPressAndRelease();
		LCD.clear();
		LCD.drawString("REALX: " + Calc.getXPositionPen(ArmModule.getArmLength(), robot.getArmAngle()), 0, 1);
		LCD.drawString("REALy: " + robot.getYCenter()+Calc.getYCenterToPen(ArmModule.getArmLength(), robot.getArmAngle()), 0, 2);
		
		LCD.drawString("XSTART: " + (height*CENTER), 0, 4);
		LCD.drawString("YSTART: " + (START-width), 0, 4);
		LCD.drawString("Length:" + width, 0, 5);
		LCD.drawString("ENTER", 0, 7);
		Button.ENTER.waitForPressAndRelease();
		LCD.clear(7);
		LCD.drawString("WAIT", 0, 7);
		super.plotVerticalLine(height*CENTER, START-width, width);
		LCD.clear(7);
		LCD.drawString("DONE - NEXT?", 0, 7);
		LCD.drawString("REALX: " + Calc.getXPositionPen(ArmModule.getArmLength(), robot.getArmAngle()), 0, 1);
		LCD.drawString("REALy: " + robot.getYCenter()+Calc.getYCenterToPen(ArmModule.getArmLength(), robot.getArmAngle()), 0, 2);
		
		Button.ENTER.waitForPressAndRelease();
		LCD.clear();
		LCD.drawString("REALX: " + Calc.getXPositionPen(ArmModule.getArmLength(), robot.getArmAngle()), 0, 1);
		LCD.drawString("REALy: " + robot.getYCenter()+Calc.getYCenterToPen(ArmModule.getArmLength(), robot.getArmAngle()), 0, 2);
		
		LCD.drawString("XSTART: " + (height*CENTER), 0, 4);
		LCD.drawString("YSTART: " + (START), 0, 4);
		LCD.drawString("Length:" + -height, 0, 5);
		LCD.drawString("ENTER", 0, 7);
		Button.ENTER.waitForPressAndRelease();
		LCD.clear(7);
		LCD.drawString("WAIT", 0, 7);
		super.plotHorizontalLine(height*CENTER, START, -height);
		LCD.clear(7);
		LCD.drawString("DONE - NEXT?", 0, 7);
		LCD.drawString("REALX: " + Calc.getXPositionPen(ArmModule.getArmLength(), robot.getArmAngle()), 0, 1);
		LCD.drawString("REALy: " + robot.getYCenter()+Calc.getYCenterToPen(ArmModule.getArmLength(), robot.getArmAngle()), 0, 2);
	}
	
	private double calcWidth(double height, boolean square){
		
		if (square){
			return height;
		}else{
			return 2.6*height;
		}	
	}

	
	
}