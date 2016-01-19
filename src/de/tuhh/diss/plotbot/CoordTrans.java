package de.tuhh.diss.plotbot;

import de.tuhh.diss.exceptions.OutOfWorkspaceException;

public class CoordTrans {
	//fickhen
	static public double getXPositionPen(int armLength, double angle){
		return (armLength * Math.cos(Math.toRadians(angle)));
	}
	
	static public double getYPositionPen(int armLength, double feed, double angle){
		return (feed + armLength * Math.sin(Math.toRadians(angle)));
	}
	
	static public double getYCenterToPen(int armLength, double angle){
		return (armLength * Math.sin(Math.toRadians(angle)));
	}
	
	static public double getAnglePen(int armLength, double x) throws OutOfWorkspaceException{
		if(Math.abs(x) <= armLength){
			return Math.toDegrees(Math.acos(x/armLength));
		} else {
			throw new OutOfWorkspaceException();
		}
	}
	
	static public double getFeed(int armLength, int maxFeed, double x, double y) throws OutOfWorkspaceException{	
		
		double feed = (y-armLength*Math.sin(Math.toRadians(getAnglePen(armLength, x))));
		
		if(feed >= 0 && feed <= maxFeed){
			return feed;
		} else {
			throw new OutOfWorkspaceException();
		}
	}
}



