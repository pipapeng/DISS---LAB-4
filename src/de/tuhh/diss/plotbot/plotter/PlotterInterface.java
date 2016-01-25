package de.tuhh.diss.plotbot.plotter;

import de.tuhh.diss.plotbot.exceptions.MotorException;

public interface PlotterInterface {
	
	public int getMinSizeRectangle();
	public int getMaxSizeRectangle();
	public int getMinSizeString();
	public int getMaxSizeString();

	public void plotRectangle(double size) throws MotorException;
	public void plotString(double size) throws MotorException;
	
	public void stopImmediatly();	//TODO: evtl. entfernen
}
