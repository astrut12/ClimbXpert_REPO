package com.example.climbxpert;

public class MathOrientation {

	
	
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
	
}
