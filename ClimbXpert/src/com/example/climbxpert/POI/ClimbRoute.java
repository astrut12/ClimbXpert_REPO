package com.example.climbxpert.POI;

import com.example.climbxpert.MathOrientation;


public class ClimbRoute {

	// Route Rank
	public String rank;
	
	// Route Name
	public String name;
		
	// Information about the specific route
	public String info;

	// The route's ID
	public int rid;

	// The orientation azimuth should be between 0 to 360
	public float azimuth;
	
	// The tilt controls the gravity orientation (0 for horizontal)
	public float tilt;
	
	
	public ClimbRoute(String r_name, String r_info,String r_rank, int r_id, float r_azimuth, float r_tilt) {
		rank = r_rank;
		name = r_name;
		info = r_info;
		rid = r_id;
		azimuth = r_azimuth;
		tilt = r_tilt;
	}
	
	public ClimbRoute() {
		
	}

	public float getTiltDifference(float compareTilt)
	{
		return MathOrientation.getAngleDifference(tilt, compareTilt);
	}
	
	
	public float getAzimuthDifference(float compareAzimuth)
	{
		return MathOrientation.getAngleDifference(azimuth, compareAzimuth);
	}
	
	
}
