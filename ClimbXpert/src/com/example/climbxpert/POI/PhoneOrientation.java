package com.example.climbxpert.POI;

public class PhoneOrientation {

	//TODO consider changing the tolerances to be external information - will allow adjusting tolerance according to usage
	//TODO should the standing location (LatLng for standing) be part of the orientations?
	
	
	// Gravity orientation
	public float xGravity;
	public float yGravity;
	public float zGravity;
	// the tolerance define the allowed differentiation to estimate Gravity proximity  
	public float GravityTolerance;
	
	// Magnetic orientation
	public float xMagnetic;
	public float yMagnetic;
	public float zMagnetic;
	//the tolerance define the allowed differentiation to estimate Magnetic proximity  
	public float MagneticTolerance;
	
	
	
	/***
	 * Check if the given two sets of 3D coordinates are in proximity.
	 * If all matching coordinate values differences are within the tolerance 
	 * range then the coordinates are considered within proximity.   
	 * @param x1	First Coordinate X value
	 * @param y1	First Coordinate Y value
	 * @param z1	First Coordinate Z value
	 * @param x2	Second Coordinate X value
	 * @param y2	Second Coordinate Y value
	 * @param z2	Second Coordinate Z value
	 * @param tolerance		The tolerance for the proximity
	 * @return True if both sets are within proximity, false otherwise.
	 */
	private boolean CheckProximity(float x1, float y1, float z1, float x2, float y2, float z2, float tolerance)
	{
		return Math.abs(x1-x2) < Math.abs(tolerance) && 
				Math.abs(y1-y2) < Math.abs(tolerance) && 
				Math.abs(z1-z2) < Math.abs(tolerance); 
	}
	
	/***
	 * Checking proximity to the Magnetic orientation of this Orientation
	 * @param x		X orientation
	 * @param y		Y orientation
	 * @param z		Z orientation
	 * @return	True if coordinates are within the magnetic proximity
	 */
	public boolean CheckMagneticProximity(float x, float y, float z)
	{
		return CheckProximity(xMagnetic, yMagnetic, zMagnetic, x, y, z, MagneticTolerance);
	}
	
	
	/***
	 * Checking proximity to the Gravity orientation of this Orientation
	 * @param x		X orientation
	 * @param y		Y orientation
	 * @param z		Z orientation
	 * @return	True if coordinates are within the gravity proximity
	 */
	public boolean CheckGravityProximity(float x, float y, float z)
	{
		return CheckProximity(xGravity, yGravity, zGravity, x, y, z, GravityTolerance);
	}
}
