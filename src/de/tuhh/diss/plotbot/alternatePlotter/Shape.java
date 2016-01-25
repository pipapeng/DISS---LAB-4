package de.tuhh.diss.plotbot.alternatePlotter;

public abstract class Shape {
	
	protected double height;
	protected double width;
	
	Shape(){

	}
	
	public abstract void plot();
	
	public abstract double getMinX();
	
	public abstract double getMaxX();
	
	public abstract double getMinY();
	
	public abstract double getMaxY();
	
	public double getHeight(){
		return getMaxX() - getMinX();
	}
	
	public double getWidth(){
		return getMaxY() - getMinY();
	}
}
