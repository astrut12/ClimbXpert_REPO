package com.example.climbxpert;

import com.example.climbxpert.POI.POI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class FindNearPOIActivity extends Activity
implements
ConnectionCallbacks, //allow connection to location service
OnConnectionFailedListener, //notify when connection to location service failed
LocationListener //listen to location changes
{

	private boolean NavigateOpened = false;
	
	// Client for connecting to location service
	private LocationClient locClient;

	// Options for location requests
	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(1000)         // 5 seconds
			.setFastestInterval(16)    // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	private static final int RETURN_RESULT_NAVIGATE_ACTIVITY = 1; 

	// The last received location from the location service
	private Location lastKnownLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_near_poi);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.find_near_poi, menu);
		return true;
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (!NavigateOpened)
		{
			setupLocationClient();
			locClient.connect();
		}
	}

	@Override
	protected void onPause(){
		super.onPause();
		if (null != locClient)
		{
			locClient.disconnect();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		lastKnownLocation = location;

		// we only need one location.
		locClient.disconnect();

		LatLng currentLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());

		POI closestPOI = null;
		double closestDistance = -1;

		for (POI poi : ClimbXpertData.POIList)
		{
			double currDistance = MathOrientation.calculateLocationsDistance(currentLocation, poi.standLocation);
			if (null == closestPOI)
			{
				closestPOI = poi;
				closestDistance = currDistance;
			}
			else
			{
				if (currDistance < closestDistance)
				{
					closestPOI = poi;
					closestDistance = currDistance;
				}
			}

			if (null != closestPOI)
			{
				if (!NavigateOpened)
				{
					NavigateOpened = true;
					Intent intent = new Intent(this, NavigateActivity.class);
					intent.putExtra("pid", closestPOI.pid);
					startActivityForResult(intent, RETURN_RESULT_NAVIGATE_ACTIVITY);
				}
			}
			else
			{
				LoggerTools.LogToast(this, "No routes found nearby");
			}
		}
	}

	protected void onActivityResult(int reqCode, int resCode, Intent data) { 
		if (reqCode == RETURN_RESULT_NAVIGATE_ACTIVITY)
		{
			finish();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		LoggerTools.LogToast(this, "could not get you location.");

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		locClient.requestLocationUpdates(REQUEST, this);				
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	/***
	 * initialize the locClient object if not already initialized
	 */
	public void setupLocationClient()
	{
		if (null == locClient)
		{
			locClient = new LocationClient(getApplicationContext(), this, this);
		}
	}
}
