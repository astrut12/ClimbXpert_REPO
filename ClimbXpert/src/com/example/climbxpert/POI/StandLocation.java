package com.example.climbxpert.POI;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

public class StandLocation {
	
	// Latitude and longitude for the standing location
	public LatLng StandInMap;
	
	//TODO define the information required for orientation calculations
	//TODO 	consider defining a set of orientations for a given location that 
	//		will enable viewing routes in several directions from the same standing location
	
	
	// The list of ClimbRoutes that are visible in the location within the orientation.
	public ArrayList<ClimbRoute> climbingRoutes;	
	
	
}
