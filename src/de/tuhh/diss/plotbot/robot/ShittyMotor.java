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
	Direction desiredDirection;
	
	
	///////////////////////////////////////////////////////
	//	METHODS
	///////////////////////////////////////////////////////
	
	public ShittyMotor(TachoMotorPort tachoPort, int slackAngle){
		super(tachoPort);
		this.lastDirection = Direction.BACKWARD;
		this.currentMotorError = 0;
		this.slackAngle = slackAngle;
	}
	
	public void rotate(int angle){
		rotate(angle, false);
	}
	
	public void rotate(int angle, boolean immediateReturn){
		//Determine desired direction
				
		if(angle == 0){ 
			return; //nothing to do, leave method
					
		} else if (angle > 0) {
			desiredDirection = Direction.FORWARD;
					
		} else if (angle < 0) {
			desiredDirection = Direction.BACKWARD;
		}
				
		//Check where the motor came from and react to it
		switch (lastDirection){
			case FORWARD: 
				if(desiredDirection == Direction.FORWARD){
					super.rotate(angle + currentMotorError, immediateReturn);
					currentMotorError = slackAngle;
					lastDirection = Direction.FORWARD;
					return;
							
				} else if (desiredDirection == Direction.BACKWARD){
					super.rotate(super.getTachoCount() - slackAngle, false);
					super.rotate(angle, immediateReturn);
					currentMotorError = 0;
					lastDirection = Direction.BACKWARD;
					return;
				}
				
			case BACKWARD:
				if(desiredDirection == Direction.FORWARD){
					super.rotate(super.getTachoCount() + slackAngle, false);
					currentMotorError = slackAngle;
					super.rotate(angle + currentMotorError, immediateReturn);
					lastDirection = Direction.FORWARD;
					return;
							
				} else if (desiredDirection == Direction.BACKWARD){
					super.rotate(angle, immediateReturn);
					currentMotorError = 0;
					lastDirection = Direction.BACKWARD;
					return;
				}
		}
	}
	
	public void rotateTo(int limitAngle){
		rotateTo(limitAngle, false);
	}
	
	public void rotateTo(int limitAngle, boolean immediateReturn){
		//Determine desired direction
		int angleToTarget = limitAngle - getTachoCount();
		
		if(angleToTarget == 0){ 
			return; //nothing to do, leave method
			
		} else if (angleToTarget > 0) {
			desiredDirection = Direction.FORWARD;
			
		} else if (angleToTarget < 0) {
			desiredDirection = Direction.BACKWARD;
		}
		
		//Check where the motor came from and react to it
		switch (lastDirection){
			case FORWARD: 
				if(desiredDirection == Direction.FORWARD){
					super.rotateTo(limitAngle + currentMotorError, immediateReturn);
					currentMotorError = slackAngle;
					lastDirection = Direction.FORWARD;
					return;
					
				} else if (desiredDirection == Direction.BACKWARD){
					super.rotateTo(super.getTachoCount() - slackAngle, false);
					super.rotateTo(limitAngle, immediateReturn);
					currentMotorError = 0;
					lastDirection = Direction.BACKWARD;
					return;
				}
		
			case BACKWARD:
				if(desiredDirection == Direction.FORWARD){
					super.rotateTo(super.getTachoCount() + slackAngle, false);
					currentMotorError = slackAngle;
					super.rotateTo(limitAngle + currentMotorError, immediateReturn);
					lastDirection = Direction.FORWARD;
					return;
					
				} else if (desiredDirection == Direction.BACKWARD){
					super.rotateTo(limitAngle, immediateReturn);
					currentMotorError = 0;
					lastDirection = Direction.BACKWARD;
					return;
				}
		}
	}
	
	public int getTachoCount(){
		return super.getTachoCount() - currentMotorError; 
	}
	
	public int getPosition(){
		return super.getPosition() - currentMotorError;
	}
	
	public void setSlackAngle(int slackAngle){
		this.slackAngle = slackAngle;
	}
}
