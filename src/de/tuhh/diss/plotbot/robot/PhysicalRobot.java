package de.tuhh.diss.plotbot.robot;

import de.tuhh.diss.plotbot.exceptions.MotorException;
import de.tuhh.diss.plotbot.exceptions.OutOfWorkspaceException;
import de.tuhh.diss.plotbot.utilities.Calc;
import lejos.nxt.Button;
import lejos.nxt.LCD;

public class PhysicalRobot implements RobotInterface{

	///////////////////////////////////////////////////////
	//	VARIABLES
	///////////////////////////////////////////////////////
	public static final PhysicalRobot ROBOT = new PhysicalRobot(); //TODO: unstatic
	
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
	public void movePenToInSteps(int xStart, int yStart, int xTarget, int yTarget, int steps) throws OutOfWorkspaceException{
		int startAngle =(int) Math.round(Calc.getAnglePen(getArmLength(), xStart));
		int endAngle =(int) Math.round(Calc.getAnglePen(getArmLength(), xTarget));
		int angleDifference = endAngle - startAngle;
		double angleStep = angleDifference / steps;		
		double fromAngle = startAngle;
		double toAngle = startAngle + angleStep;
		double timePerStep = Math.abs(angleStep / getArmRotationSpeed());
		double yDevianceAngle;
		double yDevianceStep = (yTarget - yStart) / steps; 
		double yStep;
		double necessaryWheelspeed;
		double wheelAngle;
		
		for(int it = 0; it < steps; it++){
			//TODO: Fix this wenn winkel rechts von 90 dann falsches vorzeichen
			yDevianceAngle = Calc.getYCenterToPen(getArmLength(), fromAngle) - Calc.getYCenterToPen(getArmLength(), toAngle);
			yStep = yDevianceAngle + yDevianceStep;
			
			wheelAngle = Math.round((yStep*360/(56*Math.PI)));
			//LCD.drawString("wheelA: " + String.valueOf(wheelAngle*84), 0, 4);
			necessaryWheelspeed = wheelAngle / timePerStep;
			LCD.drawString("motspd: " + String.valueOf(Math.round(necessaryWheelspeed)*84), 0, 4);
			
			moveArmTo(toAngle, true);
			//setWheelSpeed((int) Math.round(necessaryWheelspeed));
			LCD.drawString("yStep: " + String.valueOf(yStep), 0, 5);
			
			moveWheels(yStep);
			waitForArm();
			waitForWheels();
			
			fromAngle = toAngle;
			toAngle = toAngle + angleStep;
		}
	}
	

	// Julius Versuch
	
	/** Keine Steps. Durch Funktion direkte Geschwindigkeitsanpassung
	 */
	public void movePenJulius1(int xTarget, int yTarget) throws OutOfWorkspaceException{
		double startAngle = ARM.getAngle();
		double endAngle = Calc.getAnglePen(ArmModule.ARMLENGTH, xTarget);
		double yNow;
		double yDeviance;
		double yDevianceAngle;
		double ySum;
		double necessaryWheelspeed;
				
				yNow = WHEELS.getYCenter() + Calc.getYCenterToPen(ArmModule.ARMLENGTH, startAngle);
				yDeviance = yTarget - yNow;
				yDevianceAngle = Calc.getYCenterToPen(ArmModule.ARMLENGTH, startAngle) - Calc.getYCenterToPen(getArmLength(), endAngle);
				ySum = yDevianceAngle + yDeviance;
				
				LCD.drawString(String.valueOf(ySum), 0, 6);
				Button.ENTER.waitForPressAndRelease();
				moveArmTo(endAngle, true);
				moveWheels(ySum, true);
				
				
				do{
					necessaryWheelspeed = ARM.getRotationSpeed()*ArmModule.ARMLENGTH*Math.sin(Math.toRadians(ARM.getAngle())); // -90 ersetzen
					WHEELS.setWheelSpeed(necessaryWheelspeed);
				}while(ArmModule.isMoving()); // isMoving ins Interface //TODO: ERROR !!!!

//				isMoving
//
//				public boolean isMoving()
//				This method returns true if the motor is attempting to rotate. The return value may not correspond to the actual motor movement.
//				For example, If the motor is stalled, isMoving() will return true. 
//				After flt() is called, this method will return false even though the motor axle may continue to rotate by inertia. If the motor is stalled, isMoving() will return true. . A stall can be detected by calling isStalled();
//				Specified by:
//				isMoving in interface BaseMotor
//				Returns:
//				true iff the motor is attempting to rotate.
				waitForArm();
				waitForWheels();
	}
	
	/** Mit Steps. Fuer jeden Step Geschwindigkeitsanpassung durch Funktion
	 * 
	 * Der Arm wird mit konstanter geschwindigkeit gestartet,
	 * 
	 * durch die Forschleife wird bei jedem durchlauf eine distanz in einer berechneten geschwindigkeit durchlaufen
	 * summe der distanzen ergibt die gesamt distanz
	 */
	public void movePenJulius2(int xTarget, int yTarget, int steps) throws OutOfWorkspaceException{
		double startAngle = ARM.getAngle();
		double endAngle = Calc.getAnglePen(ArmModule.ARMLENGTH, xTarget);
		double yNow;
		double yDeviance;
		double yDevianceAngle;
		double ySum;
		double yStep;
		double necessaryWheelspeed;
				
				yNow = WHEELS.getYCenter() + Calc.getYCenterToPen(ArmModule.ARMLENGTH, startAngle);
				yDeviance = yTarget - yNow;
				yDevianceAngle = Calc.getYCenterToPen(ArmModule.ARMLENGTH, startAngle) - Calc.getYCenterToPen(getArmLength(), endAngle);
				ySum = yDevianceAngle + yDeviance;
				yStep = ySum/steps;
				
				moveArmTo(endAngle, true);
				
				for(int i1 = 1; i1 == steps; i1++){
					necessaryWheelspeed = ARM.getRotationSpeed()*ArmModule.ARMLENGTH*Math.sin(Math.toRadians(ARM.getAngle())); // -90 ersetzen
					WHEELS.setWheelSpeed(necessaryWheelspeed);
					moveWheels(yStep, true);
					WHEELS.setWheelSpeed(necessaryWheelspeed);
					waitForWheels();
				}
				waitForArm();
	}
	

	/** Mit Steps. 
	 * Die Bewegung wird in Abschnitte unterteilt.
	 * Fuer jedes Bewegungsdreieck wird die y distanz und der winnkel berechnet
	 * berechneter winkel und y distanz werden nacheinander gefahren
	 * Geschwindigkeiten bleiben konstant
	 */
	public void movePenJulius3(int xTarget, int steps) throws OutOfWorkspaceException{
		double startAngle = ARM.getAngle();
		double endAngle = Calc.getAnglePen(ArmModule.ARMLENGTH, xTarget);
		double angleDifference = endAngle - startAngle;
		double stepAngle = angleDifference/steps;
		double sector = Math.sqrt(2)*ArmModule.ARMLENGTH*Math.sqrt(1-Math.cos(Math.toRadians(stepAngle))); // stepAngle = Degree? --   c^2 = 2a^2(1-cos(gamma)) 
		double gamma;
		double yDevianceAngle;
		double toAngle;
		
		for(int i1=1; i1 == steps; i1++){
			gamma = i1*stepAngle;
			yDevianceAngle = Math.sin(Math.toRadians(gamma))*sector;
			toAngle = (i1*stepAngle)+startAngle;
			moveArmTo(toAngle, false);
			moveWheels(yDevianceAngle, false);
		}
	}
	
	
	/** Mit Steps. Die Bewegung wird in Abschnitte unterteilt. Fuer jedes Bewegungsdreieck wird die y distanz und der winnkel berechnet
	 * berechneter winkel und y distanz wird gleichzeitig gefahren
	 * die benoetigte Geschwindigkeit wird fuer jeden durchlauf der for schleife neu berechnet
	 */
	public void movePenJulius4(int xTarget, int steps) throws OutOfWorkspaceException{
		double startAngle = ARM.getAngle();
		double endAngle = Calc.getAnglePen(ArmModule.ARMLENGTH, xTarget);
		double angleDifference = endAngle - startAngle;
		double stepAngle = angleDifference/steps;
		double sector = Math.sqrt(2)*ArmModule.ARMLENGTH*Math.sqrt(1-Math.cos(Math.toRadians(stepAngle))); // stepAngle = Degree? --   c^2 = 2a^2(1-cos(gamma)) 
		double gamma;
		double necessaryWheelspeed;
		double toAngle;
		double yDevianceAngle;
		
		for(int i1=1; i1 == steps; i1++){
			
			toAngle = (i1*stepAngle)+startAngle;
			gamma = i1*stepAngle;
			yDevianceAngle = Math.sin(Math.toRadians(gamma))*sector;
			necessaryWheelspeed = ARM.getRotationSpeed()*ArmModule.ARMLENGTH*Math.sin(Math.toRadians((i1-0.5)*stepAngle)); // -90 ersetzen
			WHEELS.setWheelSpeed(necessaryWheelspeed);
			moveArmTo(toAngle, true);
			moveWheels(yDevianceAngle, true);
			WHEELS.setWheelSpeed(necessaryWheelspeed);
			waitForWheels();
		}
			waitForArm();
	}
	
	
	
	
	// Julius Versuch Ende
	

	public void movePenToLennart(double xTarget, double yTarget) throws MotorException{
		
		try{
			double targetAngle = Calc.getAnglePen(ArmModule.getArmLength(), xTarget);
			double yOfTargetAngle = Calc.getYCenterToPen(ArmModule.getArmLength(), targetAngle);
			double currentYPos = this.getYCenter();
			double distance = yTarget - yOfTargetAngle - currentYPos;
			
			this.moveArmTo(targetAngle, true);
			this.moveWheels(distance, true);
			
			this.waitForArm();
			this.waitForWheels();
		} catch(OutOfWorkspaceException e){
			
			this.stopAllMotors();
			throw new MotorException();
		}
	}
	
	public void movePenVerticalLennart(double xStart, double yStart, double length) throws MotorException, OutOfWorkspaceException{
		
		this.movePenToLennart(xStart, yStart);
		moveWheels(length);
	}
	
	public void movePenHorizontalLennart(double xStart, double yStart, double length, int amountOfSteps) throws MotorException{
		
		try{
			int armLength = ArmModule.getArmLength();
			int tolerance = 5;			//TODO Konstante machen
			double angleStepTarget;
			boolean targetReached = false;
			
			amountOfSteps = Math.abs(amountOfSteps);
			
			movePenToLennart(xStart, yStart);
			
			do{
				double angleCurrent = this.getArmAngle();
				double yCurrent = this.getYCenter() + Calc.getYCenterToPen(armLength, angleCurrent);
				double angleTarget = Calc.getAnglePen(armLength, xStart + length);
				
				double yDelta = Calc.getYCenterToPen(armLength, angleTarget) - Calc.getYCenterToPen(armLength, angleCurrent);	
				double yStep = yDelta / amountOfSteps;
				
				for(int i = 1; i == amountOfSteps; i++){
					
					angleStepTarget = Calc.getAnglePenOfY(armLength, yCurrent + i * yStep); 
					this.moveArmTo(angleStepTarget, true);
					this.moveWheels(yStep, true);
					
					this.waitForWheels();
					this.waitForArm();
				}
	
				//TODO Methode zu double aendern
				double armAngle = this.getArmAngle();
				targetReached = Calc.targetReachedSufficently((int) Calc.getXPositionPen(armLength, armAngle),(int) (this.getYCenter() + Calc.getYCenterToPen(armLength, armAngle)), (int) (xStart  + length), (int) yStart, tolerance);
				
				amountOfSteps = 1;		// TODO evtl aendern
			} while(!targetReached);
		} catch(OutOfWorkspaceException e){
			
			this.stopAllMotors();
			throw new MotorException();
		}
	}
	
	public void movePenHorizontalLennart2(double xStart, double yStart, double length, int yStep) throws MotorException{
		
		try{
			movePenToLennart(xStart, yStart);
			
			int armLength = ArmModule.getArmLength();
			
			double angleStart = Calc.getAnglePen(armLength, xStart);
			double angleTarget = Calc.getAnglePen(armLength, xStart + length);
			
			double yDelta = Calc.getYCenterToPen(armLength, angleTarget) - Calc.getYCenterToPen(armLength, angleStart);
			
			int amountOfSteps = Math.abs((int) (yDelta / yStep));
			
			yStep = Math.abs(yStep);
			if(yDelta < 0){
				yStep = -yStep;
			}
			
			double angleStepTarget;
			
			for(int i = 1; i == amountOfSteps; i++){
					
				angleStepTarget = Calc.getAnglePenOfY(armLength, yStart + i * yStep); 
				this.moveArmTo(angleStepTarget, true);
				this.moveWheels(yStep, true);
					
				this.waitForWheels();
				this.waitForArm();
			}
			
			double yRest = Calc.getYCenterToPen(armLength, angleTarget) - Calc.getYCenterToPen(armLength, this.getArmAngle());
			this.moveArmTo(angleTarget, true);
			this.moveWheels(yRest, true);
			
			this.waitForWheels();
			this.waitForArm();
			
		} catch(OutOfWorkspaceException e){
			
			this.stopAllMotors();
			throw new MotorException();
		}
	}

	
	/////////////////
	/////  ARM
	/////////////////
	
	public int getArmLength(){
		return ArmModule.ARMLENGTH;
	}
	
	public double getArmMinAngle(){
		return ARM.getArmMinAngle();
	}
	
	public double getArmMaxAngle(){
		return ARM.getArmMaxAngle();
	}
	
	public double getArmAngle(){
		return ARM.getAngle();
	}
	
	public int getArmMotorSpeed(){
		return ARM.getMotorSpeed();
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
	
	public void moveWheels(double distance) throws OutOfWorkspaceException{
		WHEELS.moveWheels(distance);
	}
	
	public void moveWheels(double distance, boolean immediateReturn) throws OutOfWorkspaceException{
		WHEELS.moveWheels(distance, immediateReturn);
	}
	
	public void moveWheelsForward() throws OutOfWorkspaceException{
		WHEELS.moveWheelsForward();
	}
	
	public void moveWheelsBackward() throws OutOfWorkspaceException{
		WHEELS.moveWheelsBackward();
	}
	
	public void waitForWheels(){
		WHEELS.waitForWheels();
	}
	
	public void stopWheels(){
		WHEELS.stopWheels();
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	///////////////////////////////////////////////////////
	//	VARIABLES
	///////////////////////////////////////////////////////
	
	/**
	 * Tolerance that is used for reaching a target
	 */
	private final int TARGET_TOLERANCE = 3;
	
	/**
	 * Instance of the robot
	 */
	//private PhysicalRobot robot = PhysicalRobot.ROBOT;
	
	/**
	 *  Current x position of the pen
	 */
	private double xNow;
	
	/**
	 * Current y position of the pen
	 */
	private double yNow;
	
	
	
	///////////////////////////////////////////////////////
	//	METHODS
	///////////////////////////////////////////////////////		
	
	/** 
	 *  Moves the pen to a certain position 
	 *  by moving arm and wheels at the same time continously.
	 *  
	 *  This is only meant for moving the pen to a position
	 *  or drawing a vertical line, horizontal lines can not 
	 *  be plotted with this.
	 * 
	 * @param xTarget x coordinate of the target
	 * @param yTarget y coordinate of the target
	 */
	public void movePenVertical(double xTarget, double yTarget) throws OutOfWorkspaceException{
		double distanceToTravel = yTarget - getYCenter() - getYCenterToPen();
		//moveArmTo(getArmAngleForX(xTarget),true);
		moveWheels(distanceToTravel);
	}
	
	
	/** 
	 * Moves the pen to a certain target in steps
	 * 
	 * By using small steps the pen will move in 
	 * a straight line (in theory :P)
	 *  
	 * @param xTarget target x-position of pen
	 * @param yTarget target y-position of pen
	 * @param stepSize the millimeters taken per step
	 * @throws OutOfWorkspaceException
	 */
	//Bewege Arm solang auf ziel zu bis innerhalb Toleranz (Alle Referenzen ueber Motor) NUR X WIRD BETRACHTET -> NUR HORIZONTAL
	public void movePenToInStepsV1(double xTarget, double yTarget, int stepSize) throws OutOfWorkspaceException{
		double nextX, yCorrection, nextWheelSpeed;
		
		while(!xIsReached(xTarget)){
			updateXNow();
			updateYNow();
			nextX = getNext(xTarget, xNow, stepSize);
			yCorrection = getYCorrection(nextX);
			nextWheelSpeed = getNextWheelSpeed(nextX - xNow, yCorrection);
			
			printDebugMsg(nextX, 0, nextWheelSpeed, yCorrection);
			
			setWheelSpeed((WHEELS.WHEELMOTORMAXSPEED/WheelsModule.WHEELGEARRATIO)/6);
			moveArmTo(getArmAngleForX(nextX),true);
			moveWheels(yCorrection*.9);
			waitForArm();
			updateXNow();
			updateYNow();
		}
		
		updateXNow();
		updateYNow();
	}
	
	/**
	 * Calculates the next coordinate in a certain direction 
	 * 
	 * Returns current position + stepSize until the remaining distance to the target
	 * is smaller than stepSize, then returns the target itself
	 * 
	 * @param x_yTarget x or y coordinate of the target
	 * @param x_yNow current x or y coordinate 
	 * @param stepSize the millimeters taken per step 
	 * @return next x or y coordinate
	 * @throws OutOfWorkspaceException
	 */
	public double getNext(double x_yTarget, double x_yNow, int stepSize) throws OutOfWorkspaceException{
		//if moving left or down
		if(x_yTarget - x_yNow < 0){
			stepSize = -stepSize;
		}
		
		if (Math.abs(x_yTarget - x_yNow) >= Math.abs(stepSize)){
			return x_yNow + stepSize;
		} else {
			return x_yTarget;
		}
	}
	
	/**
	 * 
	 * @param distanceToGoX
	 * @param distanceToGoY
	 * @return
	 */
	public double getNextWheelSpeed(double distanceToGoX, double distanceToGoY){
		double armSpeed = getArmMotorSpeed() / ArmModule.ARMGEARRATIO;
		double angleToGo = getAngleToTarget(xNow + distanceToGoX);
		double timeNeededForX = Math.abs(angleToGo) / armSpeed;
		double revolutionsWheelNeeded = distanceToGoY / WheelsModule.WHEELDIAMETER;
		double revolutionsMotorNeeded = revolutionsWheelNeeded * WheelsModule.WHEELGEARRATIO;
		
		return (revolutionsMotorNeeded * 360 / timeNeededForX);
	}
	
	
	//Variante fuer y Abweichungen mit einbeziehen AUCH HIER NUR FUER HORIZONTALE LINIEN
	public void movePenToInStepsV2(double xTarget, double yTarget, int stepSize) throws OutOfWorkspaceException{
		double nextX, nextY, yCorrection, nextWheelSpeed;

		
		while(!xyIsReached(xTarget, yTarget)){
			updateXNow();
			updateYNow();
			nextX = getNext(xTarget, xNow, stepSize);
			nextY = getNext(yTarget, yNow, stepSize);
			yCorrection = getYCorrection(nextX);
			nextWheelSpeed = getNextWheelSpeed(nextX - xNow, (nextY - yNow) + yCorrection);
			
			printDebugMsg(nextX, nextY, nextWheelSpeed, yCorrection);
			
			setWheelSpeed(nextWheelSpeed);
			moveArmTo(getArmAngleForX(nextX),true);
			moveWheels(nextY + yCorrection);
			waitForArm();
			updateXNow();
			updateYNow();
		}
		
		updateXNow();
		updateYNow();
	}
	
	
	// Variante tasaechlich Laenge wird zerlegt HIERMIT KANN MAN AUCH DIAGONAL ... THEORETISCH
	public void movePenToInStepsV3(double xTarget, double yTarget, int stepSize) throws OutOfWorkspaceException{
		double nextX, nextY, yCorrection, nextWheelSpeed;

		while(!xyIsReached(xTarget, yTarget)){
			updateXNow();
			updateYNow();
			nextX = getNextX(xTarget, yTarget, stepSize);
			nextY = getNextY(xTarget, yTarget, stepSize);
			yCorrection = getYCorrection(nextX);
			nextWheelSpeed = getNextWheelSpeed(nextX - xNow, (nextY - yNow) + yCorrection);
			
			printDebugMsg(nextX, nextY, nextWheelSpeed, yCorrection);
			
			//setWheelSpeed(nextWheelSpeed);
			moveArmTo(getArmAngleForX(nextX),true);
			moveWheels(nextY + yCorrection);
			waitForArm();
			updateXNow();
			updateYNow();
		}
		
		updateXNow();
		updateYNow();
	}
	
	
	public double getNextX(double xTarget, double yTarget, int stepSize){
		double distanceX = xTarget - xNow;
		double distanceY = yTarget - yNow;
		double totalDistance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
		double alpha = Math.atan2(distanceX, distanceY);
		
		//if moving left
		if(distanceX < 0){
			stepSize = -stepSize;
		}
		
		if(Math.abs(totalDistance) >= Math.abs(stepSize)){
				return (xNow + (stepSize * Math.cos(alpha)));
		} else {
				return (xNow + (totalDistance * Math.cos(alpha)));
		}
	}
	
	public double getNextY(double xTarget, double yTarget, int stepSize){
		double distanceX = xTarget - xNow;
		double distanceY = yTarget - yNow;
		double totalDistance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
		double alpha = Math.atan2(distanceX, distanceY);
		
		//if moving down
		if(distanceY < 0){
			stepSize = -stepSize;
		}
		
		if(Math.abs(totalDistance) >= Math.abs(stepSize)){
				return (yNow + (stepSize * Math.sin(alpha)));
		} else {
				return (yNow + ((totalDistance * Math.sin(alpha))));
		}
	}
	
	
	
	/**
	 * Calculates the distance that has to be corrected in y-direction
	 * when the arm moves from where it is to xTarget
	 * 
	 * @param xTarget x-coordinate of the target position
	 * @return the distance that needs to be corrected (deviance * -1)
	 */
	private double getYCorrection(double xTarget){
//		LCD.drawString("AL: " + String.valueOf(ArmModule.ARMLENGTH), 0, 0);
//		LCD.drawString("gAfX: " + String.valueOf(getArmAngleForX(xTarget)), 0, 1);
//		LCD.drawString("sin: " + String.valueOf(Math.sin(getArmAngleForX(xTarget))), 0, 2);
//		LCD.drawString("gAA: " + String.valueOf(getArmAngle()), 0, 3);
//		LCD.drawString("sin2: " + String.valueOf(Math.sin(getArmAngle())), 0, 4);
//		LCD.drawString("No-1: " + String.valueOf(((ArmModule.ARMLENGTH * Math.sin(getArmAngleForX(xTarget))) - (ArmModule.ARMLENGTH * Math.sin(getArmAngle())))), 0, 5);
//		LCD.drawString("-1: " + String.valueOf((-1) * ((ArmModule.ARMLENGTH * Math.sin(getArmAngleForX(xTarget))) - (ArmModule.ARMLENGTH * Math.sin(getArmAngle())))), 0, 6);
//		LCD.drawString("xT: " + String.valueOf(xTarget), 0, 7);
//		Button.ENTER.waitForPressAndRelease();
		return ((-1) * ((ArmModule.ARMLENGTH * Math.sin(Math.toRadians(getArmAngleForX(xTarget)))) - (ArmModule.ARMLENGTH * Math.sin(Math.toRadians(getArmAngle()))))) ;
	}
	
	/**
	 * Returns the angle the arm would be in for a certain x
	 * 
	 * @param x the x-coordinate used for calculation
	 * @return the angle the arm would be in for a certain x
	 */
	public double getArmAngleForX(double x){
		return (Math.toDegrees(Math.acos(x/ArmModule.ARMLENGTH)));
	}
	
	/**
	 * Calculates the total angle that has to be covered  by the arm in order to reach a target
	 * 
	 * @param xTarget x coordinate of the target
	 */
	private double getAngleToTarget(double xTarget){
		double startAngle = getArmAngleForX(xNow);
		double endAngle = getArmAngleForX(xTarget);
		return endAngle - startAngle;
	}
	
	/**
	 * Checks whether a certain point is reached with a certain tolerance
	 * 
	 * @param xTarget x-coordinate of the point in question
	 * @param yTarget y-coordinate of the point in question
	 * @return true if point has been reached, false if not
	 */
	private boolean xyIsReached(double xTarget, double yTarget){
		return ( xIsReached(xTarget) && yIsReached(yTarget) );
	}
	
	/**
	 * Checks whether the x coordinate of a certain point has been reached
	 * 
	 * @param xTarget x-coordinate of the point in question
	 * @return true if x has been reached, false if not
	 */
	private boolean xIsReached(double xTarget){
		updateXNow();
		
		xNow = Math.abs(xNow);
		xTarget = Math.abs(xTarget);
		
		return (xNow >= (xTarget - TARGET_TOLERANCE)) && (xNow <= (xTarget + TARGET_TOLERANCE));
	}
	
	/**
	 * Checks whether the y coordinate of a certain point has been reached
	 * 
	 * @param yTarget y-coordinate of the point in question
	 * @return true if y has been reached, false if not
	 */
	private boolean yIsReached(double yTarget){
		updateYNow();
		
		yNow = Math.abs(yNow);
		yTarget = Math.abs(yTarget);
		
		return (yNow >= (yTarget - TARGET_TOLERANCE)) && (yNow <= (yTarget + TARGET_TOLERANCE));
	}
	
	/**
	 * Updates the current x coordinate of the pen based on the current motor angle
	 */
	private void updateXNow(){
		xNow = (ArmModule.ARMLENGTH * Math.cos(Math.toRadians(getArmAngle())));
	}
	
	/**
	 * Updates the current y coordinate of the pen based on the current motor angle
	 */
	private void updateYNow(){
		yNow = (getYCenter() + getYCenterToPen());
	}
	
	/**
	 * Calculates the distance from the rotational center of the arm to where the pen is currently
	 * 
	 * @return distance from center to pen
	 */
	private double getYCenterToPen(){
		return (ArmModule.ARMLENGTH * Math.sin(Math.toRadians(getArmAngle())));
	}
	
	public void printDebugMsg(double nextX, double nextY, double wheelSpeed, double YCorr){
		LCD.clear();
		LCD.drawString("Moving ....", 0, 0);
		LCD.drawString("xNow: " + String.valueOf(xNow), 0, 1);
		LCD.drawString("nextX: " + String.valueOf(nextX), 0, 2);
		LCD.drawString("yNow: " + String.valueOf(yNow), 0, 3);
		LCD.drawString("nextY: " + String.valueOf(nextY), 0, 4);
		LCD.drawString("YCorr: " + String.valueOf(YCorr), 0, 5);
		LCD.drawString("whlspd: " + String.valueOf(wheelSpeed), 0, 6);
		LCD.drawString("ArmAn: " + String.valueOf(getArmAngle()), 0, 7);
	}
	
	
}
