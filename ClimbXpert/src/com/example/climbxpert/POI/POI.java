package com.example.climbxpert.POI;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

// A class representing the Point Of Interest.
// The POI is the general location where the routes are located
public class POI {

	// ID for the POI
	public int ID;
	
	// The displayed name of the POI	
	public String name;
	
	// Information presented about the POI
	public String info;
	
	// The resource ID of the picture representing the POI
	public int imageID;	
	
	// Latitude and Longitude of the car navigation location
	public LatLng carNavigation;
	
	// Latitude and Longitude of the standing location
	public LatLng standLocation;
		
	
	// List of standing location in the POI area
	public ArrayList<ClimbRoute> routes;

	
	
	public POI(int arg_id, String arg_name, String arg_info, int arg_imageID, LatLng arg_carNavigation, 
				LatLng arg_standLocation, ArrayList<ClimbRoute> arg_routes)
	{
		ID = arg_id;
		name = arg_name;
		info = arg_info;
		imageID = arg_imageID;
		carNavigation = arg_carNavigation;
		standLocation = arg_standLocation;
		routes = new ArrayList<ClimbRoute>(arg_routes);
	}
}
