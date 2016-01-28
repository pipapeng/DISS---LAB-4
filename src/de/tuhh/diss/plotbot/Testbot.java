package de.tuhh.diss.plotbot;

import de.tuhh.diss.plotbot.exceptions.MotorException;
import de.tuhh.diss.plotbot.exceptions.OutOfWorkspaceException;
import de.tuhh.diss.plotbot.robot.*;
import lejos.nxt.Button;
import lejos.nxt.LCD;

public class Testbot {
	PhysicalRobot robot;
	
	
	Testbot(){		// hier ist n arsch voll platz fuer testkram (einfach in main "Testbot" statt "Plotbot" aufrufen)
		
		LCD.drawString("Hello", 0, 0);
		

		robot = PhysicalRobot.ROBOT;
		robot.calibrateMotors();
		
		
		try {
			//CHRIS
			robot.movePenTo(-60, 50);
			robot.waitForArm();
			robot.setPen(true);
			robot.movePenToInStepsV1(60, 50, 4);
			Button.ENTER.waitForPressAndRelease();
			robot.setPen(false);
			robot.movePenTo(0, 100);
			robot.setPen(true);
			robot.movePenToInStepsV1(50, 100, 4);
//			robot.setPen(true);
//			drawChris.movePenToInStepsV3(-50, 50, 5);
//			robot.setPen(false);
//			drawChris.movePenTo(0, 80);
//			robot.setPen(true);
//			drawChris.movePenToInStepsV2(-50, 80, 5);
//			robot.setPen(false);
//			drawChris.movePenTo(0, 110);
//			robot.setPen(true);
//			drawChris.movePenToInStepsV1(-50, 110, 5);
//			robot.setPen(false);
			
			//JULIUS
//			LCD.drawString("Julius Start", 0, 6);
//			Button.ENTER.waitForPressAndRelease();
//			robot.movePenTo(0, 50);
//			robot.setPen(true);
//			PhysicalRobot.ROBOT.movePenJulius1(-50, 50);
//			LCD.drawString("1", 0, 6);
//			Button.ENTER.waitForPressAndRelease();
//			robot.setPen(false);
//			robot.movePenTo(0, 80);
//			robot.setPen(true);
//			PhysicalRobot.ROBOT.movePenJulius2(-50, 80, 10);
//			LCD.drawString("2", 0, 6);
//			Button.ENTER.waitForPressAndRelease();
//			robot.setPen(false);
//			robot.movePenTo(0, 110);
//			robot.setPen(true);
//			PhysicalRobot.ROBOT.movePenJulius3(-50, 10);
//			LCD.drawString("3", 0, 6);
//			Button.ENTER.waitForPressAndRelease();
//			robot.setPen(false);
//			robot.movePenTo(0, 140);
//			robot.setPen(true);
//			PhysicalRobot.ROBOT.movePenJulius4(-50, 10);
//			LCD.drawString("4", 0, 6);
//			Button.ENTER.waitForPressAndRelease();
//			robot.setPen(false);
			
//			//LENNART
//			try {
//				robot.movePenToLennart(0, 50);
//				robot.setPen(true);
//				robot.movePenHorizontalLennart(0, 50, -50, 10);
//				robot.setPen(false);
//				robot.setPen(true);
//				robot.movePenVerticalLennart(-50, 50, 50);
//				robot.setPen(false);
//				robot.setPen(true);
//				robot.movePenHorizontalLennart(-50, 100, 50, 10);
//				robot.setPen(false);
//				robot.setPen(true);
//				robot.movePenVerticalLennart(0, 100, -50);
//				robot.setPen(false);
//			} catch (MotorException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
		} catch (OutOfWorkspaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			

			// TODO Auto-generated catch block

		

		
		Button.ESCAPE.waitForPressAndRelease();
	}
}
