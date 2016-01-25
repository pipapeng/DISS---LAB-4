package de.tuhh.diss.plotbot.utilities;

import de.tuhh.diss.plotbot.exceptions.OutOfWorkspaceException;

public class CoordTrans {

	//TODO: aufräumen
	
//	static public double getXPositionPen(int armLength, double angle){
//		return (armLength * Math.cos(Math.toRadians(angle)));
//	}
//	
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
}



