package de.tuhh.diss.plotbot;

import de.tuhh.diss.plotbot.exceptions.OutOfWorkspaceException;
import de.tuhh.diss.plotbot.robot.PhysicalRobot;
import de.tuhh.diss.plotbot.robot.RobotInterface;
import lejos.nxt.Button;
import lejos.nxt.LCD;

public class Testbot {
	
	Testbot(){		// hier ist n arsch voll platz fuer testkram (einfach in main "Testbot" statt "Plotbot" aufrufen)
		
		LCD.drawString("Hello", 0, 0);
		

		RobotInterface robot = new PhysicalRobot();
		robot.calibrateMotors();
		
		
		try {
			
			
			robot.movePenTo(0, 50);
			robot.setPen(true);
			robot.movePenToInSteps(0, 50, -50, 50, 5);
			robot.setPen(false);
			robot.setPen(true);
			robot.movePenTo(-50, 150);
			
			robot.setPen(false);
			robot.setPen(true);
			robot.movePenToInSteps(-50, 50, -50, 100, 20);
			robot.setPen(false);
			
//			robot.waitForArm();
//			robot.waitForWheels();
//			robot.setPen(true);
//			robot.movePenTo(50, 50);
//			robot.waitForArm();
//			robot.waitForWheels();
//			robot.movePenTo(50, 100);
//			robot.waitForArm();
//			robot.waitForWheels();
//			robot.movePenTo(0, 100);
//			robot.waitForArm();
//			robot.waitForWheels();
//			robot.movePenTo(0, 50);
//			robot.waitForArm();
//			robot.waitForWheels();
//			robot.setPen(false);
		} catch (OutOfWorkspaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			

			// TODO Auto-generated catch block

		

		
		Button.ESCAPE.waitForPressAndRelease();
	}
}
