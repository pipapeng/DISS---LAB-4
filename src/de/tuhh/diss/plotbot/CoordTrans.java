package de.tuhh.diss.plotbot;

import de.tuhh.diss.plotbot.OutOfWorkspaceException;

public class CoordTrans {
	
	static public int getXPosition(int armLength, int angle){
		
		return (int) (armLength * Math.cos(Math.toRadians(angle)));
	}
	
	static public int getYPosition(int armLength, int feed, int angle){
		return (int) (feed + armLength * Math.sin(Math.toRadians(angle)));
	}
	
	static public int getAngle(int armLength, int x) throws OutOfWorkspaceException{
		
		if(Math.abs(x) <= armLength){
			
			return (int) Math.toDegrees(Math.cos(x/armLength));
		}
		else{
			throw new OutOfWorkspaceException();
		}
	}
	
	static public int getFeed(int armLength, int maxFeed, int x, int y) throws OutOfWorkspaceException{
		
		int feed = (int) (y-armLength*Math.sin(Math.toRadians(getAngle(armLength, x))));
		
		if(feed >= 0 && feed <= maxFeed){
			return feed;
		}
		else{
			throw new OutOfWorkspaceException();
		}
	}
}



