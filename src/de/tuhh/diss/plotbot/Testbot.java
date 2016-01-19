package de.tuhh.diss.plotbot;


import de.tuhh.diss.exceptions.MotorException;
import de.tuhh.diss.robot.*;
import lejos.nxt.Button;
import lejos.nxt.LCD;

public class Testbot {
	public static void main(String[] args)
	{
		LCD.drawString("Hello", 0, 0);
		

		RobotInterface robot = new PhysicalRobot();


		//Button.ESCAPE.waitForPressAndRelease();
	}
}
