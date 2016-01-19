package de.tuhh.diss.plotter;

public interface PlotterInterface {

	// Wuerde vorschlagen Robot in Plotter zu erzeugen (Lennart)
	
	public int getMinSizeRectangle();
	public int getMaxSizeRectangle();
	public int getMinSizeString();
	public int getMaxSizeString();

	public void plotRectangle(double size);
	public void plotString(double size);
	
	public void stopImmediatly();
	public void shutDown();				// brauch man das?
}
