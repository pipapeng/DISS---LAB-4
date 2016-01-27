package de.tuhh.diss.plotbot.plotter;

import de.tuhh.diss.plotbot.exceptions.MotorException;
import de.tuhh.diss.plotbot.exceptions.OutOfWorkspaceException;
import de.tuhh.diss.plotbot.robot.RobotInterface;

public class PlotTUHH extends PlotLines{

	private static final double SCALE = 1/6;
	private static final int MINSIZE = 10;
	private static final int MAXSIZE = (int) (START*(26*SCALE));	//TODO: das ist viel zu groﬂ
																	//obere Grenze ist laenge vom rectangle <= start
	
	public PlotTUHH(RobotInterface robot){
		
		super(robot);
	}
	
	public int getMinSize(){
		
		return MINSIZE;
	}
	
	public int getMaxSize(){
		
		return MAXSIZE;
	}
	
	public double plot(double size) throws MotorException, OutOfWorkspaceException{
		
		double nextY;
		
		nextY = getSpacingFrameToString(size, START);
		nextY = drawT(size, nextY);
		nextY = getSpacingStringToString(size, nextY);
		nextY = drawU(size, nextY);
		nextY = getSpacingStringToString(size, nextY);
		nextY = drawH(size, nextY);
		nextY = getSpacingStringToString(size, nextY);
		nextY = drawH(size, nextY);
		nextY = getSpacingFrameToString(size, nextY);
		
		return nextY;
	}
	
	private double getSpacingFrameToString(double size, double startY){
		
		return startY-2*SCALE*size;
	}
	
	private double getSpacingStringToString(double size, double startY){
		
		return startY-3*SCALE*size;
	}
	
	private double drawT(double size, double startY) throws MotorException, OutOfWorkspaceException{
		
		double widthY = 4*SCALE*size;
		double endY = startY-widthY;
	
		super.plotVerticalLine(CENTER*size, startY, -widthY);
		super.plotHorizontalLine(CENTER*size, startY-widthY*CENTER, -size);
			
		return endY;
	}
	
	private double drawU(double size, double startY) throws MotorException, OutOfWorkspaceException{
		
		double widthY = 3*SCALE*size;
		double endY = startY-widthY;
		
		super.plotHorizontalLine(CENTER*size, startY, -size);
		super.plotVerticalLine(-CENTER*size, startY, -widthY);
		super.plotHorizontalLine(-CENTER*size, startY-widthY, size);
	
		return endY;
	}
	
	private double drawH(double size, double startY) throws MotorException, OutOfWorkspaceException{
		
		double widthY = 3*SCALE*size;
		double endY = startY-widthY;
		
		super.plotHorizontalLine( CENTER*size, startY, -size);
		super.plotVerticalLine(0, startY, widthY);
		super.plotHorizontalLine(-CENTER*size, startY-widthY, size);
	
		return endY;
	}		
}
