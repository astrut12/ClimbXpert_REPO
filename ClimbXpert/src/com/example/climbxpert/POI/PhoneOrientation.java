package com.example.climbxpert.POI;

public class PhoneOrientation {

	//TODO should the standing location (LatLng for standing) be part of the orientations?
	
	
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
	
	private float normalizeAngle(float angle)
	{
		//non integer part + normalized part
		float inRange = (angle - (int)angle) + ((int)angle)%360;
		
		return (inRange>=0 ? inRange : 360+inRange );
		
	}
}
