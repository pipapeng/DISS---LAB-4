package de.tuhh.diss.plotter;

public interface PlotterInterface {
	
	public int getMinSizeRectangle();
	public int getMaxSizeRectangle();
	public int getMinSizeString();
	public int getMaxSizeString();

	public void plotRectangle(double size);
	public void plotString(double size);
	
	public void stopImmediatly();
	public void shutDown();
}
