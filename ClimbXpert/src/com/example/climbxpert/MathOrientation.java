package com.example.climbxpert;

import com.google.android.gms.maps.model.LatLng;


public class MathOrientation {

	private static final double EARTH_RADIUS = 6371000;
	
	// Adjusts the scaling of distance calculations
	private static final double SCALE_FOR_REAL_DISTANCE_CALCULATION = 0.016434811;
	
	
	/**
	 * Calculate the angle from X and Y coordinates
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 * @return The angle that the coordinates create with the X axis
	 */
	public static double getAngle(double x, double y)
	{
		//TODO Check if there is a simpler way to calculate this
		double Dangle;
		if (x<0)
		{
			if (y>0)
			{ //90:180
				//atan is negative
				Dangle = 180 + Math.toDegrees(Math.atan((y/x)));
			}
			else
			{ //180:270
				Dangle = 180 + Math.toDegrees(Math.atan((y/x)));
			}
			
		}
		else if (x>0)
		{
			if (y>0)
			{ // 0:90
				Dangle = Math.toDegrees(Math.atan((y/x)));
			}
			else
			{ //270:360
				//atan is negative
				Dangle = 360 + Math.toDegrees(Math.atan((y/x)));
			}
		}
		else
		{// x==0 -> need to handed division by zero
			if (y>=0)
			{
				Dangle = 90;
			}
			else
			{
				Dangle = 270;
			}
		}
		
		return Dangle;
	}
	
	/**
	 * Calculates the difference in degrees between two degrees.
	 * It calculates the shortest angle difference between the two angles in regard to the first angle.
	 * Result is between -180 and 180.
	 * @param a1	First angle to compare
	 * @param a2	Second angle to compare
	 * @return The difference between the two angles.
	 */
	public static float getAngleDifference(float a1, float a2)
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
	public static float normalizeAngle(float angle)
	{
		//non integer part + normalized part
		float inRange = (angle - (int)angle) + ((int)angle)%360;
		
		return (inRange>=0 ? inRange : 360+inRange );
		
	}
	
	
	
	public static double calculateLocationsDistance(LatLng loc1, LatLng loc2)
	{
		double x = (loc1.latitude - loc2.latitude) * EARTH_RADIUS;
		double y = (loc1.longitude - loc2.longitude) * EARTH_RADIUS;
		
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) * SCALE_FOR_REAL_DISTANCE_CALCULATION;
	}
	
}
