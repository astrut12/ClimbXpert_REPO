package com.example.climbxpert;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import com.example.climbxpert.LoggerTools;

public class SearchActivity extends FragmentActivity 
			implements 
			ConnectionCallbacks, //allow connection to location service
			OnConnectionFailedListener, //notify when connection to location service failed
			LocationListener, //listen to location changes
			OnMyLocationButtonClickListener //listen to clicks on the location buttons
{

	private GoogleMap map;
	private LocationClient locClient;
	
	
	 private static final LocationRequest REQUEST = LocationRequest.create()
	            .setInterval(5000)         // 5 seconds
	            .setFastestInterval(16)    // 16ms = 60fps
	            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); 
	 			//TODO consider lowering the accuracy - this may affect performance
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);	
		
		//TODO check what else needs to be initialized in this point
	}

	
	
	
	
	@Override
	protected void onPause() {
		super.onPause();
		//TODO: consider wrapping locClient with null check or putting action in utility function
		locClient.disconnect();
	}





	@Override
	protected void onResume() {
		super.onResume();
		setupMap();
		setupLocationClient();
		locClient.connect();
		//TODO check what other initializations are required here
	}




	/**
	 * Initialize the map object if it is not already initialized 
	 */
	public void setupMap()
	{
		if (null == map)
		{
			map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			
			if (null != map)
			{
				map.setMyLocationEnabled(true);
				
				map.setOnMyLocationButtonClickListener(this);
			}
			
		}
	}
	
	/**
	 * initialize the locClient object if not already initialized
	 */
	public void setupLocationClient()
	{
		if (null == locClient)
		{
			locClient = new LocationClient(getApplicationContext(), this, this);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	
	/**
	 * Handle location changes
	 */
	@Override
	public void onLocationChanged(Location location) {
		
		
		
		LoggerTools.LogToast(this, "Location was changed:" + location);
		
		//TODO add logic that check if the camera should be updated or not (during navigation etc.)
		//maybe make it update the location on first connection only
		
		map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
					.target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .zoom(15.5f)
                    .bearing(300)//TODO: need to check the compass direction and insert it into the bearing
                    .tilt(0)
                    .build()));
	}

	/**
	 * handler for failed connection to the location service
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO consider handling failure connections (alert or abort) 
		LoggerTools.LogToast(this, "Failed connection to the location service!");
	}

	/**
	 * handler on successful connection to the location service
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		//requesting to be notified on location changes. the REQUEST object will define the update rate and accuracy
		locClient.requestLocationUpdates(REQUEST, this);
		//TODO maybe add a flag notification that a connection was made - can be useful to know the first connection
	}

	/**
	 * handler for location service disconnection
	 */
	@Override
	public void onDisconnected() {
		// TODO Consider what to do here - notification? abort?
		
	}


	/**
	 * handler to location button click
	 */
	@Override
	public boolean onMyLocationButtonClick() {

		//TODO add call to set current camera location
		
		LoggerTools.LogToast(this, "Location button clicked");
		
		return false; //false to allow default handling of location button click 
	}


}
