package com.example.climbxpert.POI;

import android.content.Context;

import com.example.climbxpert.LoggerTools;
import com.example.climbxpert.MathOrientation;
import com.example.climbxpert.R;


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
	
	public int parentPOIid;
	
	public ClimbRoute(String r_name, String r_info,String r_rank, int r_id, float r_azimuth, float r_tilt, int r_pid) {
		rank = r_rank;
		name = r_name;
		info = r_info;
		rid = r_id;
		azimuth = r_azimuth;
		tilt = r_tilt;
		parentPOIid = r_pid;
	}
	
	public ClimbRoute() {
		
	}
	
	public int getImageId(Context context) {
		String str = "p"+String.valueOf(parentPOIid)+"r"+String.valueOf(rid);
		LoggerTools.LogToast(context, "looking for: " + str);
		return context.getResources().getIdentifier(str, "drawable", "com.example.climbxpert");
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
