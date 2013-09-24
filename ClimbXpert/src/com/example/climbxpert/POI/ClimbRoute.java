package com.example.climbxpert.POI;


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
	
	//TODO define a bitmap to represent the route on an image
	
	//TODO define a method to define the clickable area for the route 
	
	
	// The orientation azimuth should be between 0 to 360
	public float azimuth;
	
	// The tilt controls the gravity orientation (0 for horizontal)
	public float tilt;
	
	
	public float getTiltDifference(float compareTilt)
	{
		return getAngleDifference(tilt, compareTilt);
	}
	
	
	public float getAzimuthDifference(float compareAzimuth)
	{
		return getAngleDifference(azimuth, compareAzimuth);
	}
	
	/**
	 * Calculates the difference in degrees between two degrees.
	 * It calculates the shortest angle difference between the two angles in regard to the first angle.
	 * Result is between -180 and 180.
	 * @param a1	First angle to compare
	 * @param a2	Second angle to compare
	 * @return The difference between the two angles.
	 */
	private float getAngleDifference(float a1, float a2)
	{
				
		//aligning angles to  0
		float angle = normalizeAngle(a2) - normalizeAngle(a1);
		
		if (angle > 180)
		{
			return -(360 - angle);
		}
		else
		{
			return angle;
		}
		
	}
	
	/**
	 * Calculates the equivalent angle in the range 0:360  
	 * @param angle The angle to normalize
	 * @return the equivalent angle within 0:360 range
	 */
	private float normalizeAngle(float angle)
	{
		//non integer part + normalized part
		float inRange = (angle - (int)angle) + ((int)angle)%360;
		
		return (inRange>=0 ? inRange : 360+inRange );
		
	}
}
