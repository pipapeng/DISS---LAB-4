package de.tuhh.diss.plotbot;

import de.tuhh.diss.plotbot.robot.PhysicalRobot;
import lejos.nxt.Button;
import lejos.nxt.LCD;

public class Testbot {
	
	Testbot(){		// hier ist n arsch voll platz fuer testkram (einfach in main "Testbot" statt "Plotbot" aufrufen)
		
		LCD.drawString("Hello", 0, 0);
		

		new PhysicalRobot();


		Button.ESCAPE.waitForPressAndRelease();
	}
}
