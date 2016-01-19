package de.tuhh.diss.plotbot;


import java.awt.Rectangle;

import de.tuhh.diss.exceptions.MotorException;
import de.tuhh.diss.robot.*;
import lejos.nxt.Button;
import lejos.nxt.LCD;

public class Plotbot {
	public static void main(String[] args)
	{
	
		try{
		PlotRectangle rect = new PlotRectangle(10,true);
		} catch (MotorException e) {
			
		}
		
		Button.ESCAPE.waitForPressAndRelease();
	}
}
