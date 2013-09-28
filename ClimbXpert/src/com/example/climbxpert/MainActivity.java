package com.example.climbxpert;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.example.climbxpert.POI.POI;
import com.google.android.gms.maps.model.LatLng;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MainActivity extends Activity {
	
	POI currPoi;
	private ArrayList<POI> arrayList;
	//TODO: create a separate class with the activities id's for better references
	//The ID in intent for activity id
	public final static String ACTIVITY_ID = "activityID";
	
	//This activity's id
	public final static String MAIN_ACTIVITY_ID = "main_activity";
    
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Parse.initialize(this, "Mldts0UdI2e7ndjgYIzcAdFiIRKYRiOhYPrQWzqx", "nyRer8Q1FzdfD2oHp0IekZX4DLAeXXcXciVDgPAf");
        arrayList = new ArrayList<POI>();
		try {
			getPOIfromServer();
		} catch (ParseException e) {
			Log.d("Parse","Did not manage to grab DB");
			e.printStackTrace();
		}
		for(POI poi : arrayList) {
			Log.d("poiObject", poi.name);
		}
    }

	private void getPOIfromServer() throws ParseException {
		
		double lon,lat,stllon,stllat;
		int pid,imgId;
		String name,info;
		POI poi;
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("POI");
		ClimbXpertData.POIList = new ArrayList<POI>();
		
		for(ParseObject po : query.find()) {
			pid = po.getInt("pid");
			name = po.getString("name");
			info = po.getString("info");
			lon = po.getDouble("lon");
			lat = po.getDouble("lat");
			stllon = po.getDouble("STLlon");
			stllat = po.getDouble("STLlat");
			imgId = po.getInt("imgId");
			poi = new POI(pid, name, info, imgId, new LatLng(lat,lon),new LatLng(stllat,stllon), null);
			Log.d("POIDAL", "pid "+String.valueOf(pid));
			Log.d("POIDAL", "name "+String.valueOf(name));
			Log.d("POIDAL", "lon "+String.valueOf(lon));
			ClimbXpertData.POIList.add(poi);
		}
		ClimbXpertData.pid = ClimbXpertData.POIList.size();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    public void openSearch(View view)
    {
    	//opening the search window with an indication that it came from the main window 
    	//(to distinguish calls from outer intents or from navigation context)
    	Intent intent = new Intent(this, SearchActivity.class);
    	intent.putExtra(ACTIVITY_ID, MAIN_ACTIVITY_ID);
    	startActivity(intent);
    }
    
    
    public void openNavigate(View view)
    {
    	//TODO: check if there are relevant locations nearby before opening the navigate activity
    	//consider putting the location search logic in the navigation activity (makes it simpler since we need to search for the nearest location in the navigation activity anyway). 
    	
    	//opening the navigate window with an indication that it came from the main window 
    	//(to distinguish calls from outer intents or from navigation context)
    	Intent intent = new Intent(this, NavigateActivity.class);
    	intent.putExtra(ACTIVITY_ID, MAIN_ACTIVITY_ID);
    	startActivity(intent);
    }
    
    public void openCameraView(View view)
    {
    	Intent intent = new Intent(this, CameraViewActivity.class);
    	intent.putExtra(ACTIVITY_ID, MAIN_ACTIVITY_ID);
    	startActivity(intent);
    }
    
    
    public void openTestView(View view)
    {
    	Intent intent = new Intent(this, TestActivity.class);
    	intent.putExtra(ACTIVITY_ID, MAIN_ACTIVITY_ID);
    	startActivity(intent);
    }
    
    public void openPOIMenuView(View view)
    {
    	Intent intent = new Intent(this, POIManagerActivity.class);
    	
    //	intent.putExtra("poi", arrayList);
    	intent.putExtra(ACTIVITY_ID, MAIN_ACTIVITY_ID);
    	startActivity(intent);
    }
    
}
