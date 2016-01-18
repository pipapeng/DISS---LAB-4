package de.tuhh.diss.plotbot;

import lejos.nxt.Motor;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.LCD;
import lejos.nxt.Button;


public class PhysicalRobot implements RobotInterface{

	private static final int armGearRatio = 85;
	private static final int armLength = 80;
	private static final int armMinAngle = 0;
	private static final int armMaxAngle = 110;
	private static final int penAngleUp = 0;
	private static final int penAngleDown = 180;
	private static final int wheelGearRatio = 5;
	private static final int wheelDiameter = 56;
	private static final int minFeed = 0;
	private static final int maxFeed = 230 - armLength;
	private static final int distanceLightSensor = 105;
	private static final double factorLight = 1.05;

	private final float armMaxSpeed;
	private final float armMinSpeed;
	private final float penMaxSpeed;
	private final float penMinSpeed;
	private final float wheelMaxSpeed;
	private final float wheelMinSpeed;
	
	private int bearingPlay;
	private boolean penDown;
	
	private TouchSensor touchArm;
	private TouchSensor touchPen;
	private LightSensor light;
	
	private NXTRegulatedMotor motorArm;
	private NXTRegulatedMotor motorPen;
	private NXTRegulatedMotor motorWheels;
	
	public PhysicalRobot() throws MotorsHasBeenStoppedException{
		
		touchArm = new TouchSensor(SensorPort.S1);
		touchPen = new TouchSensor(SensorPort.S2);
		light = new LightSensor(SensorPort.S3);
		
		motorPen = Motor.B;
		penMaxSpeed = motorPen.getMaxSpeed();
		penMinSpeed = 0;
		calibrationPen();
		
		motorArm = Motor.A;
		armMaxSpeed = motorArm.getMaxSpeed();
		armMinSpeed = 0;
		calibrationArm();
		
		motorWheels = Motor.C;
		wheelMaxSpeed = motorWheels.getMaxSpeed();
		wheelMinSpeed = -wheelMaxSpeed;
		calibrationWheels();
	}
	
	public void stopAll(){
		stopArm();
		stopPen();
		stopWheels();
	}
	
	
	/////** ARM **/////
	
	public int getArmAngle(){
		
		return motorArm.getPosition() / armGearRatio;
	}
	
	public int getArmLength(){
	
		return armLength;
	}
	
	public int getMaxArmAngle(){
		
		return armMaxAngle;
	}
		
	public int getArmRotationalSpeed(){
		
		return motorArm.getRotationSpeed();
	}
	
	public void setArmSpeed(int speed) throws IndexOutOfBoundsException {
		
		if (speed>=armMinSpeed && speed<=armMaxSpeed){
			motorArm.setSpeed(speed);
		}
		else{
			throw new IndexOutOfBoundsException();
		}
	}
	
	
	//TODO: was passiert wenn schon da?
	public void moveArmTo(int armAngle) throws MotorsHasBeenStoppedException{
		
		if (armAngle>=armMinAngle && armAngle<=armMaxAngle){
			
			int motorAngle = armAngle * armGearRatio;
			
			if (motorArm.getPosition() != motorAngle){
				if(motorArm.getPosition() < motorAngle){
					motorAngle = motorAngle + bearingPlay;
				}
				if(motorArm.getPosition() > motorAngle){
					motorAngle = motorAngle - bearingPlay;	
				}
			}
		}
		else{
			stopAll();
			throw new MotorsHasBeenStoppedException();
		}
	}
	
	public void moveArmTo(int armAngle, boolean immediateReturn) throws MotorsHasBeenStoppedException{
		
		if (armAngle>=armMinAngle && armAngle<=armMaxAngle){
			
			int motorAngle = armAngle * armGearRatio;
			
			if (motorArm.getPosition() != motorAngle){
				if(motorArm.getPosition() < motorAngle){
					motorAngle = motorAngle + bearingPlay;
				}
				if(motorArm.getPosition() > motorAngle){
					motorAngle = motorAngle - bearingPlay;	
				}
				motorArm.rotateTo(motorAngle, true);
			}
		}
		else{
			stopAll();
			throw new MotorsHasBeenStoppedException();
		}
	}
	
	public void waitForArm(){
		motorArm.waitComplete();
	}
		
	public void stopArm(){
		
		motorArm.stop();
	}
	
	
	/////** PEN **/////	
	
	public void setPen(boolean IO){
		
		if(IO == true){
			if(penDown == false){
				motorPen.rotateTo(penAngleDown);
				penDown = true;
			}
		}
		else{
			if(penDown == true){
				motorPen.rotateTo(penAngleUp);
				penDown = false;
			}
		}
	}
	
	public void stopPen(){
		
		motorPen.stop();
	}
	
	
	/////** Wheels **/////
	
	public int getFeed(){
		
		return (int) ((wheelDiameter/(2*wheelGearRatio)) * Math.toRadians(motorWheels.getPosition()));
	}
	
	public int getMaxFeed(){
		
		return maxFeed;
	}

	public void setWheelSpeed(int speed) throws IndexOutOfBoundsException{

		if (speed>=wheelMinSpeed && speed<=wheelMaxSpeed){
			motorWheels.setSpeed(speed);
		}
		else{
			throw new IndexOutOfBoundsException();
		}
	}
	
	public void moveWheels(int length) throws MotorsHasBeenStoppedException{
		if ((getFeed() + length) <= maxFeed && (getFeed() + length) >= minFeed){
			motorWheels.rotate((int) (length*360*wheelGearRatio/(2*wheelDiameter*Math.PI)));
		}
		else
			stopAll();
			throw new MotorsHasBeenStoppedException();
	}
	
	//TODO: Eigentlich Listener noetig
	public void moveWheelsForward() throws MotorsHasBeenStoppedException{
		if(getFeed() <= maxFeed)
			motorWheels.forward();
		else
			stopAll();
			throw new MotorsHasBeenStoppedException();
	}

	//TODO: Eigentlich Listener noetig
	public void moveWheelsBackward() throws MotorsHasBeenStoppedException{
		if(getFeed() >= minFeed)
			motorWheels.backward();
		else
			stopAll();
			throw new MotorsHasBeenStoppedException();
	}
	
	public void waitForWheels(){
		motorWheels.waitComplete();
	}
	
	public void stopWheels(){
		motorWheels.stop();
	}

	
	/////** Calibration **/////
	
	private void calibrationArm(){
		motorArm.setSpeed((int)(armMaxSpeed*.75));
		while (!touchArm.isPressed()){
			motorArm.backward();	
		}
		motorArm.stop();
		motorArm.resetTachoCount();
		bearingPlay = 6;
	}
	
	private void calibrationPen(){
		motorPen.setSpeed(penMaxSpeed/5);
		while (!touchPen.isPressed()){
			motorPen.forward();
		}
		motorPen.stop();
		motorPen.resetTachoCount();
		penDown = false;
	}
	
	private void calibrationWheels() throws MotorsHasBeenStoppedException{
		
		motorArm.setSpeed(armMaxSpeed);
		moveArmTo(armMaxAngle/2);
		
		LCD.drawString("Calibration:", 0, 1);
		LCD.drawString("Light sensor", 0, 2);
		LCD.drawString("'dark'", 0, 3);
		LCD.drawString("Press ESC!", 0, 5);
		Button.ESCAPE.waitForPressAndRelease();
		LCD.clear();
		
		int dark = light.getNormalizedLightValue();
		light.setLow(dark);
		
		motorWheels.setSpeed(wheelMaxSpeed/5);
		while (light.getNormalizedLightValue() <= (factorLight*dark)){
			motorWheels.forward();
		}
		motorWheels.stop();
		light.setHigh(light.getNormalizedLightValue());
		
		moveWheels(distanceLightSensor - armLength);
		motorWheels.resetTachoCount();
	}	
}
