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
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.climbxpert.LoggerTools;

public class SearchActivity extends FragmentActivity 
			implements 
			ConnectionCallbacks, //allow connection to location service
			OnConnectionFailedListener, //notify when connection to location service failed
			LocationListener, //listen to location changes
			OnMyLocationButtonClickListener //listen to clicks on the location buttons
{

	// A map element to refer to the map fragment
	private GoogleMap map;
	
	// Client for connecting to location service
	private LocationClient locClient;
	
	// Options for location requests
	private static final LocationRequest REQUEST = LocationRequest.create()
	            .setInterval(5000)         // 5 seconds
	            .setFastestInterval(16)    // 16ms = 60fps
	            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); 
	 			//TODO consider lowering the accuracy - this may affect performance
	
	// The last received location from the location service
	private Location lastKnownLocation;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);	
		
		//TODO check what else needs to be initialized in this point
	}

	
	/***
	 * Disconnect from location service
	 */
	@Override
	protected void onPause() {
		super.onPause();
		
		if (null != locClient)
			locClient.disconnect();
	}

	/***
	 * Connect to the location service.
	 * On first load will initialize the map location client members. 
	 */
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
				
				map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				
				map.setInfoWindowAdapter(new POIInfoWindowAdapter());
				//initializing a set of markers for the map.
				insertMarkers();
				
			}
			
		}
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
				
		if (null == lastKnownLocation)
		{
			//first time we get the location
			lastKnownLocation = location;
			MoveToCurrentLocation();
		}
		else
		{
			lastKnownLocation = location;
			
		}
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
	}

	/**
	 * handler for location service disconnection
	 */
	@Override
	public void onDisconnected() {
		
	}


	/**
	 * handler to location button click
	 */
	@Override
	public boolean onMyLocationButtonClick() {

		MoveToCurrentLocation();
		
		return false; 
	}

	
	//Private functions

	/***
	 * Loading the POI into the map
	 */
	private void insertMarkers()
	{
		//TODO replace this set of markers with locations from the application's POI database
		//TODO consider passing the POI ID within the Marker Snippet attribute.
		
		
		map.addMarker(new MarkerOptions()
					.position(new LatLng(31.762, 35.201))
					.title("test1"));
		
		map.addMarker(new MarkerOptions()
					.position(new LatLng(31.764, 35.204))
					.title("test2"));
		
		map.addMarker(new MarkerOptions()
					.position(new LatLng(31.762, 35.203))
					.title("test3"));
	}
	
	
	/***
	 * Move the map's camera to the current location.
	 * The current location is stored asynchronously whenever a location change is received.
	 */
	private void MoveToCurrentLocation()
	{
		if (null != lastKnownLocation)
		{
			MoveToLocation(new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude()));
		}
		else
		{
			LoggerTools.LogToast(this, "Corrent location not known yet");
		}
	}
	
	
	/***
	 * Move the map's camera to the given coordinates.
	 * @param latlng	The coordinates to move the camera to
	 */
	private void MoveToLocation(LatLng latlng)
	{
		if (null != latlng && null != map)
		{	
			map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
			.target(latlng)
	        .zoom(15.5f)
	        .bearing(300)//TODO: need to check the compass direction and insert it into the bearing
	        .tilt(0)
	        .build()));
		}
	}

	
	
	class POIInfoWindowAdapter implements InfoWindowAdapter {
		
		private final View markerContent;
		
		public POIInfoWindowAdapter() {
			markerContent = getLayoutInflater().inflate(R.layout.poi_marker, null);

		}
		
		/***
		 * creating a view to display inside a POI marker bubble.
		 */
		@Override
		public View getInfoContents(Marker marker) {
	
			((TextView) markerContent.findViewById(R.id.markerTextView1)).setText("The title: " + marker.getTitle());
			
			((TextView) markerContent.findViewById(R.id.markerTextView2)).setText("The content: " + marker.getTitle());
			
			((ImageView) markerContent.findViewById(R.id.markerImage)).setImageResource(0);

			
			return markerContent;
		}

		
		/***
		 * Does nothing (skip to {@see POIInfoWindowAdapter#getInfoContents(Marker)})
		 */
		@Override
		public View getInfoWindow(Marker marker) {
			//return null since we want to set the content only (for now)
			//if we want to control the whole look of the bubble we should implement this instead.
			return null;
		}

	}
	
}
