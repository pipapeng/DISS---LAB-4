package de.tuhh.diss.plotbot.robot;

import de.tuhh.diss.plotbot.exceptions.OutOfWorkspaceException;
import de.tuhh.diss.plotbot.utilities.Calc;
import lejos.nxt.LCD;



public class PhysicalRobot implements RobotInterface{

	///////////////////////////////////////////////////////
	//	VARIABLES
	///////////////////////////////////////////////////////
	public static final PhysicalRobot ROBOT = new PhysicalRobot();
	
	private final ArmModule ARM;
	private final PenModule PEN;
	private final WheelsModule WHEELS;
	
	
	///////////////////////////////////////////////////////
	//	METHODS
	///////////////////////////////////////////////////////	
	
	private PhysicalRobot(){
			PEN = new PenModule();
			ARM = new ArmModule();
			WHEELS = new WheelsModule();
	}
	
	public RobotInterface getPhysicalRobot(){
		return ROBOT;
	}
	
	/** Calls the calibration methods of every module
	 *  
	 *  @return true if calibration succeeded, false if calibration failed
	 */
	public boolean calibrateMotors(){
		try{
			PEN.calibrateMotorPen();
			ARM.calibrateMotorArm();
			WHEELS.calibrateMotorWheels();
			return true;
			
		} catch (OutOfWorkspaceException e) {
			stopAllMotors();
			return false;
		}
	}
	
	/** Stops all motors at once
	 * 
	 */
	public void stopAllMotors(){
		stopArm();
		stopPen();
		stopWheels();
	}
	
	/** Moves the pen to a certain position 
	 *  by moving arm and wheels at the same time continously
	 * 
	 * @param xTarget x coordinate of the target
	 * @param yTarget y coordinate of the target
	 */
	public void movePenTo(int xTarget, int yTarget) throws OutOfWorkspaceException{
		double yCenterToPen = Calc.getYCenterToPen(getArmLength(), getArmAngle());
		double yCenterOfRobot = getYCenter();
		double distanceToTravel = yTarget - yCenterOfRobot - yCenterToPen;
		
		moveArmTo((int)Calc.getAnglePen(getArmLength(), xTarget), true);
		moveWheels(distanceToTravel);
	}
	
	/** Moves the pen to a certain target in steps
	 *  by using small steps the pen will move in a straight line (in theory :P)
	 *  
	 * @param xTarget target x-position of pen
	 * @param yTarget target y-position of pen
	 * @param steps the amount of steps the movement shall be divided in
	 * @throws OutOfWorkspaceException
	 */
	public void movePenToInSteps(int xTarget, int yTarget, int steps, int style) throws OutOfWorkspaceException{
		double startAngle = ARM.getAngle();
		double endAngle = Calc.getAnglePen(ArmModule.ARMLENGTH, xTarget);
		double angleToTarget = endAngle - startAngle;
		double angleStep = angleToTarget / steps;		
		double fromAngle = startAngle;
		double toAngle = startAngle + angleStep;
		double timePerStep = Math.abs(angleStep / getArmRotationSpeed());
		double yNow = WHEELS.getYCenter() + Calc.getYCenterToPen(ArmModule.ARMLENGTH, startAngle);
		double yDevianceStep = (yTarget - yNow) / steps; 
		double yDevianceAngle;
		double yStep;
		double necessaryWheelspeed;
		double wheelAngle;
		
		//int style     // 1: Aktualisiere alles jeden Schritt ueber den Motor,
						//	  teile jeden Schritt die noch uebrigen Distanzen erneut auf 
						//    (Wahrscheinlich werden die Schritte hier immer groesser)
						//
						// 2: Ermittelt am Ende die Abweichung vom Ziel und schlaegt Error oben drauf
						//	  hoert erst auf wenn er am Ziel ist
		switch (style){
		case 1:
			for(int it = 0; it < steps; it++){
				//set up next distance in Y
				yNow = WHEELS.getYCenter() + Calc.getYCenterToPen(ArmModule.ARMLENGTH, fromAngle);
				LCD.drawString("yNow: " + String.valueOf(yNow), 0, 4);
				//yDevianceStep = (yTarget - yNow) / (steps - it);
				yDevianceAngle = Calc.getYCenterToPen(ArmModule.ARMLENGTH, fromAngle) - Calc.getYCenterToPen(getArmLength(), toAngle);
				yStep = yDevianceAngle + yDevianceStep;
				
				//set up next wheel speed needed
				wheelAngle = yStep * 360/(WheelsModule.WHEELDIAMETER * Math.PI);
				necessaryWheelspeed = wheelAngle / timePerStep;
				//setWheelSpeed(necessaryWheelspeed);
				
				//move arm and wheels
				waitForArm();
				waitForWheels();
				moveArmTo(toAngle, true);
				moveWheels(yStep, true);

				//set up next angle
				fromAngle = ARM.getAngle();
				angleToTarget = endAngle - fromAngle;
				angleStep = angleToTarget / (steps - it);
				toAngle = fromAngle + angleStep;
			} 
			break;
				
		case 2:
			//TODO: needs finishing
			int currentX = (int) Math.round(Calc.getXPositionPen(ArmModule.ARMLENGTH, ARM.getAngle()));
			int currentY = (int) Math.round(yNow);
			double distanceToCoverX;
			double distanceToCoverY;
			double maxDistance;
			double stepsNeeded;
			while(Calc.targetReachedSufficently(currentX, currentY, xTarget, yTarget, 3) != true){
				distanceToCoverX = xTarget - currentX;
				distanceToCoverY = yTarget - currentY;
				maxDistance = Math.max(distanceToCoverX, distanceToCoverY);
				stepsNeeded = maxDistance / steps;
				
				//set up next distance in Y
				
				yDevianceAngle = Calc.getYCenterToPen(ArmModule.ARMLENGTH, fromAngle) - Calc.getYCenterToPen(getArmLength(), toAngle);
				yStep = yDevianceAngle + yDevianceStep;
				
				//need to use default wheelspeed here
				
				//move arm and wheels
				waitForArm();
				waitForWheels();
				moveArmTo(toAngle, true);
				moveWheels(yStep, true);
				
				//set up next angle
				fromAngle = ARM.getAngle();
				angleToTarget = endAngle - fromAngle;
				angleStep = angleToTarget / steps;
				toAngle = fromAngle + angleStep;
				
				//calculate current coordinates
				currentY = (int) Math.round((WHEELS.getYCenter() + Calc.getYCenterToPen(ArmModule.ARMLENGTH, fromAngle)));
				currentX = (int) Math.round(Calc.getXPositionPen(ArmModule.ARMLENGTH, ARM.getAngle()));
			} 
			break;
		}
	}
	
	
	/////////////////
	/////  ARM
	/////////////////
	
	public int getArmLength(){
		return ArmModule.ARMLENGTH;
	}
	
	public int getArmMinAngle(){
		return ARM.getArmMinAngle();
	}
	
	public int getArmMaxAngle(){
		return ARM.getArmMaxAngle();
	}
	
	public double getArmAngle(){
		return ARM.getAngle();
	}
	
	public double getArmRotationSpeed(){
		return ARM.getRotationSpeed();
	}
	
	public void setArmSpeed(int speed) throws IndexOutOfBoundsException{
		ARM.setArmSpeed(speed);
	}
	
	public void moveArmTo(double targetAngle) throws OutOfWorkspaceException{
		ARM.moveArmTo(targetAngle);
	}
	
	public void moveArmTo(double targetAngle, boolean immediateReturn) throws OutOfWorkspaceException{
		ARM.moveArmTo(targetAngle, immediateReturn);
	}
	
	public void waitForArm(){
		ARM.waitForArm();
	}
	
	public void stopArm(){
		ARM.stopArm();
	}
	
	
	/////////////////
	/////  PEN
	/////////////////
	
	public void setPen(boolean down){
		PEN.setPen(down);
	}
	
	public void stopPen(){
		PEN.stopPen();
	}
	
	
	/////////////////
	/////  WHEELS
	/////////////////
	
	public int getMaxFeed(){
		return WHEELS.getYCenterMax();
	}
	
	public double getYCenter(){
		return WHEELS.getYCenter();
	}

	public void setWheelSpeed(double speed) throws IndexOutOfBoundsException{
		WHEELS.setWheelSpeed(speed);
	}
	
	public void moveWheels(double distance){
		try {
			WHEELS.moveWheels(distance);
		} catch (OutOfWorkspaceException e) {
			stopAllMotors();
			LCD.drawString("OoW in moveWheels()", 0, 8);
		}
	}
	
	public void moveWheels(double distance, boolean immediateReturn) throws OutOfWorkspaceException{
		try {
			WHEELS.moveWheels(distance, immediateReturn);
		} catch (OutOfWorkspaceException e) {
			stopAllMotors();
			LCD.drawString("OoW in moveWheels()", 0, 8);
		}
	}
	
	public void moveWheelsForward(){
		try {
			WHEELS.moveWheelsForward();
		} catch (OutOfWorkspaceException e) {
			stopAllMotors();
			LCD.drawString("OoW in moveWheelsForward()", 0, 8);
		}
	}
	
	public void moveWheelsBackward(){
		try {
			WHEELS.moveWheelsBackward();
		} catch (OutOfWorkspaceException e) {
			stopAllMotors();
			LCD.drawString("OoW in moveWheelsBackward()", 0, 8);
		}
	}
	
	public void waitForWheels(){
		WHEELS.waitForWheels();
	}
	
	public void stopWheels(){
		WHEELS.stopWheels();
	}
}
