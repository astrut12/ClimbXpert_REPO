package com.example.climbxpert.POI;

import com.example.climbxpert.MathOrientation;


public class ClimbRoute {

	// Route Rank
	public String rank;
	
	// Information about the specific route
	public String info;
	
	// The route's ID
	public int rid;
	
	// The route image resource ID
	public int imageRscID;
	
	
	// The orientation azimuth should be between 0 to 360
	public float azimuth;
	
	// The tilt controls the gravity orientation (0 for horizontal)
	public float tilt;
	
	
	public float getTiltDifference(float compareTilt)
	{
		return MathOrientation.getAngleDifference(tilt, compareTilt);
	}
	
	
	public float getAzimuthDifference(float compareAzimuth)
	{
		return MathOrientation.getAngleDifference(azimuth, compareAzimuth);
	}
	
	
}
