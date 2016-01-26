package de.tuhh.diss.plotbot.robot;

import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.TachoMotorPort;

/**
 * Special TachoMotor, compensating for a certain mechanical slack.
 * Delegates all calls except rotateTo() and getTachoCount()
 * to a standard leJOS motor. 
 * RotateTo() is overwritten to add some state remembering the direction
 * of last motor movement. If the direction is changed, the motor will
 * add some rotation offset to overcome the initial slack it experiences.
 * 
 * GetTachoCount is adjusted accordingly, so that no bias is experienced, 
 * in spite of the additional movement.
 * 
 * @author Andreas Weigel / Marcus Venzke
 */
public class SlackMotor extends NXTRegulatedMotor {
	private boolean lastFwd;
	private int currOffset;
	private int slackOffset;
	
	/**
	 * Creates an instance of a SlockMotor
	 * 
	 * @param port   MotorPort to which the Motor is connected
	 * @param slackOffset Motorangle that needs to be turns when changing direction after which the arm moves
	 */
	public SlackMotor(TachoMotorPort port, int slackOffset) {
		super(port);
		this.slackOffset = slackOffset;
		this.lastFwd = false;
		this.currOffset = 0;
	}



	public void rotate(int angle) {
		rotateTo(super.getTachoCount() + angle); 
	}
	
	public void rotate(int angle, boolean immediateReturn) {
		rotateTo(super.getTachoCount() + angle, immediateReturn);
	}
	
	public void rotateTo(int limitAngle) {
		rotateTo(limitAngle, false);
	}
	
	/**
	 * Overwritten to compensate for slack when changing direction of movement
	 */
	public void rotateTo(int limitAngle, boolean immediateReturn) {
		int delta = limitAngle - getTachoCount();
		if (delta != 0) {
			if (lastFwd && delta < 0) {
				lastFwd = false;
				super.rotateTo(super.getTachoCount() - slackOffset, false);
				super.rotateTo(limitAngle, immediateReturn);
				currOffset = 0;
			} else if (!lastFwd && delta > 0) {
				lastFwd = true;
				super.rotateTo(getTachoCount() + slackOffset, false);
				super.rotateTo(limitAngle, immediateReturn);
				currOffset = slackOffset;
			} else {
				super.rotateTo(limitAngle + currOffset, immediateReturn);
			}
		}
	}
	
	/**
	 * Overwritten to correct for offset of the tacho count
	 */
	public int getTachoCount() {
		return super.getTachoCount() - currOffset; 
	}
}