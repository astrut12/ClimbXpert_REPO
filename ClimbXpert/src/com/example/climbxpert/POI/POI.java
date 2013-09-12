package com.example.climbxpert.POI;

import java.util.ArrayList;
import java.util.List;

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
	
	// The picture representing the POI
	//TODO: define the format for the picture (is it a picture object or path to stored picture)

	// Latitude and Longitude of the car navigation location
	public LatLng carNavigation;
	
	// List of standing location in the POI area
	public ArrayList<StandLocation> standLocationList;

	
	
	public POI(int arg_id, String arg_name, String arg_info, LatLng arg_carNavigation, List<StandLocation> arg_standLocationList)
	{
		ID = arg_id;
		name = arg_name;
		info = arg_info;
		carNavigation = arg_carNavigation;
		if (null != arg_standLocationList)
		{
			standLocationList = new ArrayList<StandLocation>(arg_standLocationList);
		}
		else
		{
			standLocationList = new ArrayList<StandLocation>();
		}
	}
}
