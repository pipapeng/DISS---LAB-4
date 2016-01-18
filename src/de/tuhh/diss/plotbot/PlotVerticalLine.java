package de.tuhh.diss.plotbot;

public class PlotVerticalLine extends PlotLine{
	
	public PlotVerticalLine(RobotInterface robot,int xStart, int yStart, int length) throws MotorsHasBeenStoppedException{
		super(robot,xStart,yStart);
		robot.setPen(true);
		super.moveToCoord(xStart,yStart+length);
		robot.setPen(false);
	}
}
