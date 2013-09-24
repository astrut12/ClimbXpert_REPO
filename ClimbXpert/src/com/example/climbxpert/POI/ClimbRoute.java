package com.example.climbxpert.POI;

import com.example.climbxpert.MathOrientation;


public class ClimbRoute {

	public enum ROUTE_LEVEL{
		LEVEL_5A,
		LEVEL_5A_P,
		LEVEL_5B,
		LEVEL_5B_P,
		LEVEL_5C,
		LEVEL_5C_P,
		LEVEL_6A,
		LEVEL_6A_P,
		LEVEL_6B,
		LEVEL_6B_P,
		LEVEL_6C,
		LEVEL_6C_P,
		LEVEL_7A,
		LEVEL_7A_P,
		LEVEL_7B,
		LEVEL_7B_P,
		LEVEL_7C,
		LEVEL_7C_P,
		LEVEL_8A,
		LEVEL_8A_P,
		LEVEL_8B,
		LEVEL_8B_P,
		LEVEL_8C,
		LEVEL_8C_P,
	}
	
	// The route's level
	public ROUTE_LEVEL level;
	
	// Information about the specific route
	public String info;
	
	// The route's ID
	public int ID;
	
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
