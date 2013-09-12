package com.example.climbxpert.POI;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

public class StandLocation {
	
	// Latitude and longitude for the standing location
	public LatLng StandInMap;
	
	// The orientation from the stand location to the routes
	public PhoneOrientation StandOrientation;
	
	//TODO 	consider defining a set of orientations for a given location that 
	//		will enable viewing routes in several directions from the same standing location
	
	
	// The list of ClimbRoutes that are visible in the location within the orientation.
	public ArrayList<ClimbRoute> climbingRoutes;	
	
	
}
