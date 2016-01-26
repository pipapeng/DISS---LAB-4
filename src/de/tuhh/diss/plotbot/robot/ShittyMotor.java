package de.tuhh.diss.plotbot.robot;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.TachoMotorPort;

/** ShittyMotor extends the NXTRegulatedMotor class of lejos
 *  original methods have been overwritten to take slack into account
 * 
 * @author Chris
 *
 */
public class ShittyMotor extends NXTRegulatedMotor{
	
	///////////////////////////////////////////////////////
	//	VARIABLES
	///////////////////////////////////////////////////////
	
	private int slackAngle;
	private int currentMotorError;
	
	private enum Direction {
		FORWARD, BACKWARD
	}
	
	Direction lastDirection;
	
	
	///////////////////////////////////////////////////////
	//	METHODS
	///////////////////////////////////////////////////////
	
	public ShittyMotor(TachoMotorPort tachoPort, int slackAngle){
		super(tachoPort);
		this.slackAngle = slackAngle;
		lastDirection = Direction.BACKWARD;
	}
	
	public void rotate(int angle){
		
	}
	
	public void rotate(int angle, boolean immediateReturn){
		
	}
	
	public void rotateTo(int limitAngle){
		rotateTo(limitAngle, false);
	}
	
	public void rotateTo(int limitAngle, boolean immediateReturn){
		switch (lastDirection){
			case FORWARD:
		
		
			case BACKWARD:
		}
	}
	
	public int getTachoCount(){
		return 1;
	}
	
	public int getPosition(){
		return 1;
	}
}
