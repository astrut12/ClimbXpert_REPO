package com.example.climbxpert;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.example.climbxpert.POI.ClimbRoute;
import com.example.climbxpert.POI.POI;
import com.google.android.gms.maps.model.LatLng;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MainActivity extends Activity {
	
	boolean debug = true;
	
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
		Button POIb = (Button) findViewById(R.id.POIViewButton);
		Button Testingb = (Button) findViewById(R.id.TestingViewButton);
		Button Camerab = (Button) findViewById(R.id.CameraViewButton);
		if(!debug) {
			POIb.setVisibility(View.GONE);
			Testingb.setVisibility(View.GONE);
			Camerab.setVisibility(View.GONE);
		}
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
		ParseQuery<ParseObject> route_query = new ParseQuery<ParseObject>("Route");
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
			
			if (poi.routes == null)
			{
				poi.routes = new ArrayList<ClimbRoute>();
			}
			
			for (ParseObject ro : route_query.find())
			{
				if (ro.getInt("pid") == pid)
				{
					poi.routes.add(new ClimbRoute(ro.getString("name"),ro.getString("info"), ro.getString("rank"), ro.getInt("rid"), (float)ro.getDouble("azimuth"),(float) ro.getDouble("tilt"), pid));
				}
			}
			
			ClimbXpertData.POIList.add(poi);
		}
		ClimbXpertData.pid = ClimbXpertData.POIList.size();
	
		ClimbXpertData.rid = route_query.count();
		
		//TODO: just a POI for testing
//		ArrayList<ClimbRoute> testAlonRoutes = new ArrayList<ClimbRoute>();
//		ClimbRoute testAlonRoute = new ClimbRoute();
	//	testAlonRoute.azimuth = 175;
	//	testAlonRoute.tilt = 0;
	//	testAlonRoute.imageRscID = R.drawable.test;
	//	testAlonRoutes.add(testAlonRoute);
	//	POI testAlonPOI = new POI(5, "ALON test", "asdfadsf", 0, new LatLng(30, 30), new LatLng(30, 30), testAlonRoutes);
	//	ClimbXpertData.POIList.add(testAlonPOI);
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
    	Intent intent = new Intent(this, FindNearPOIActivity.class);
    	startActivity(intent);
    }
    
    public void openCameraView(View view)
    {
    	Intent intent = new Intent(this, CameraViewActivity.class);
    	intent.putExtra(ACTIVITY_ID, MAIN_ACTIVITY_ID);
    	intent.putExtra("pid", ClimbXpertData.POIList.get(1).pid);
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
