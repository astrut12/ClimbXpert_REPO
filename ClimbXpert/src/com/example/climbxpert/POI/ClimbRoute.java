package com.example.climbxpert.POI;

public class ClimbRoute {
	
	public enum ROUTE_LEVEL{
		LEVEL_5A,
		LEVEL_5A_P,
		LEVEL_5B,
		LEVEL_5B_P,
		LEVEL_5C,
		LEVEL_5C_P,
		LEVEL_6A,
		LEVEL_6A_P,
		LEVEL_6B,
		LEVEL_6B_P,
		LEVEL_6C,
		LEVEL_6C_P,
		LEVEL_7A,
		LEVEL_7A_P,
		LEVEL_7B,
		LEVEL_7B_P,
		LEVEL_7C,
		LEVEL_7C_P,
		LEVEL_8A,
		LEVEL_8A_P,
		LEVEL_8B,
		LEVEL_8B_P,
		LEVEL_8C,
		LEVEL_8C_P,
	}
	
	// The route's level
	public ROUTE_LEVEL level;
	
	// Information about the specific route
	public String info;
	
	// The route's ID
	public int ID;
	
	//TODO define a bitmap to represent the route on an image
	
	//TODO define a method to define the clickable area for the route 
	
	
	
}

