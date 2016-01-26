package de.tuhh.diss.plotbot.utilities;

import de.tuhh.diss.plotbot.exceptions.OutOfWorkspaceException;

public class Calc {

	//TODO: aufraeumen
	
	static public double getXPositionPen(int armLength, double angle){
		return (armLength * Math.cos(Math.toRadians(angle)));
	}
	
//	static public double getYPositionPen(int armLength, double feed, double angle){
//		return (feed + armLength * Math.sin(Math.toRadians(angle)));
//	}
	
	static public double getYCenterToPen(int armLength, double armAngle){
		return (armLength * Math.sin(Math.toRadians(armAngle)));
	}
	
	static public double getAnglePen(int armLength, double x) throws OutOfWorkspaceException{
		return Math.toDegrees(Math.acos(x/armLength));
	}
	
//	static public double getFeed(int armLength, int maxFeed, double x, double y) throws OutOfWorkspaceException{	
//		
//		double feed = (y-armLength*Math.sin(Math.toRadians(getAnglePen(armLength, x))));
//		
//		if(feed >= 0 && feed <= maxFeed){
//			return feed;
//		} else {
//			throw new OutOfWorkspaceException();
//		}
//	}
	
	public static boolean targetReachedSufficently(int xNow, int yNow, int xTarget, int yTarget, int tolerance){
		boolean xOK = false, yOK = false;
		
		xTarget = Math.abs(xTarget);
		yTarget = Math.abs(yTarget);
		xNow = Math.abs(xNow);
		yNow = Math.abs(yNow);
		
		if( (xNow >= (xTarget - tolerance)) && (xNow <= (xTarget + tolerance)) ){
			xOK = true;
		}
		
		if( (yNow >= (yTarget - tolerance)) && (yNow <= (yTarget + tolerance)) ){
			yOK = true;
		}
		
		if(xOK == true && yOK == true){
			return true;
		} else {
			return false;
		}
	}
}



