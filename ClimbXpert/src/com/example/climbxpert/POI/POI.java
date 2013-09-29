package com.example.climbxpert.POI;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

// A class representing the Point Of Interest.
// The POI is the general location where the routes are located
public class POI implements Parcelable {

	// ID for the POI
	public int pid;
	
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

	
	//TODO remove parameter for route list
	public POI(int arg_id, String arg_name, String arg_info, int arg_imageID, LatLng arg_carNavigation, 
				LatLng arg_standLocation, ArrayList<ClimbRoute> arg_routes)
	{
		pid = arg_id;
		name = arg_name;
		info = arg_info;
		imageID = arg_imageID;
		carNavigation = arg_carNavigation;
		standLocation = arg_standLocation;
		if (arg_routes != null)
		{
			routes = new ArrayList<ClimbRoute>(arg_routes);
		}
		else
		{
			routes = new ArrayList<ClimbRoute>();
		}
	}


	public int getImageId(Context context) {
		String str = "p"+String.valueOf(pid);
		return context.getResources().getIdentifier(str, "drawable", "com.example.climbxpert");
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public POI(Parcel in){
        String[] data = new String[5];

        in.readStringArray(data);
        this.pid = Integer.valueOf(data[0]);
        this.name = data[1];
        this.info = data[2];
        double lon,lat;
        lon = Double.valueOf(data[3]);
        lat = Double.valueOf(data[4]);
        this.carNavigation = new LatLng(lat,lon);
    }


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] {String.valueOf(this.pid), this.name, this.info, String.valueOf(this.carNavigation.longitude),
				String.valueOf(this.carNavigation.latitude)});
		
	}
	 public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
         public POI createFromParcel(Parcel in) {
             return new POI(in); 
         }

         public POI[] newArray(int size) {
             return new POI[size];
         }
     };
}
