package com.example.climbxpert.POI;

public class PhoneOrientation {

	//TODO should the standing location (LatLng for standing) be part of the orientations?
	
	
	// The orientation azimuth should be between 0 to 360
	public float azimuth;
	
	// The tilt controls the gravity orientation (0 for horizontal)
	public float tilt;
	
	//TODO implement functions that calculate the difference of tilt and azimuth from a given value
	//		notice that this requires identifying cases of 360->0 switches etc.
	
	public float getTiltDifference(float compareTilt)
	{
		return 0;
	}
}
