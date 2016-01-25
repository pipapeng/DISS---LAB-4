package de.tuhh.diss.plotbot.alternatePlotter;

public class Line extends Shape{

	private double xStart, xEnd, yStart, yEnd;
	
	public Line(double xStart, double yStart, double xEnd, double yEnd){
		this.xStart = xStart;
		this.xEnd = xEnd;
		this.yStart = yStart;
		this.yEnd = yEnd;
		
		this.height = getHeight();
		this.width = getWidth();
	}
	
	public void plot(){
		
	}
	
	public double getMinX(){
		return Math.min(xStart, xEnd);
	};
	
	public double getMaxX(){
		return Math.max(xStart, xEnd);
	}
	
	public double getMinY(){
		return Math.min(yStart, yEnd);
	}
	
	public double getMaxY(){
		return Math.max(yStart, yEnd);
	}
	
	public double getLength(){
		double xDiff = xEnd - xStart;
		double yDiff = yEnd - yStart;
		
		return Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));
	}
}
