package com.example.climbxpert;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;
 
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.location.Location;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.climbxpert.LoggerTools;
import com.example.climbxpert.POI.POI;

public class SearchActivity extends Activity
			implements 
			OnMyLocationButtonClickListener, //listen to clicks on the location buttons
			OnInfoWindowClickListener, //listen to click events for marker's info bubbles
			LocationListener
{
	//------------------------------------Constants-----------------------------------------
	private static final long MIN_LOCATION_UPDATE_INTERVAL = 5000;
	private static final float MIN_LOCATION_UPDATE_DISTANCE = 20;
	
	//----------------------------------Activity Fields-------------------------------------
	
	// Location service.
	private LocationManager locationManager;
	private String locationProvider;
	
	// A map element to refer to the map fragment.
	private GoogleMap googleMap;
	
	
	private POI poi; //TODO: check what is it exactly
	
	// General MarkerOptions for the activity
	private MarkerOptions markerOptions; //TODO: check if it's necessary
	// General LatLng for the activity
	private LatLng latLng; //TODO: check if it's necessary
	
	
	
	// The last received location from the location service.
	private Location lastKnownLocation;
	
	//---------------------------------Activity Life-cycle Methods---------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		// getInstance of the location service.
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		// Check if enabled and if not send user to the GSP settings
		// Better solution would be to display a dialog and suggesting to
		// go to the settings
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		}
	    // Set to default provider.
	    locationProvider = locationManager.getBestProvider(new Criteria(), false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(locationProvider, MIN_LOCATION_UPDATE_INTERVAL, MIN_LOCATION_UPDATE_DISTANCE, this);
//		setupMap();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		locationManager.removeUpdates(this);
	}

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
	
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	//-------------------------------------Methods------------------------------------------
	
	/**
	 * Initialize the map object if it is not already initialized 
	 */
	public void setupMap()
	{
		if (googleMap == null)
		{

			MapFragment supportMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
			// Getting a reference to the map
			googleMap = supportMapFragment.getMap();

		
			if (googleMap != null)
			{
				// Getting reference to btn_find of the layout activity_main
				Button btn_find = (Button) findViewById(R.id.btn_find);

				// Defining button click event listener for the find button
				OnClickListener findClickListener = new OnClickListener() {
					@Override
					public void onClick(View v) {
						// Getting reference to EditText to get the user input location
						EditText etLocation = (EditText) findViewById(R.id.et_location);

						// Getting user input location
						String location = etLocation.getText().toString();

						if(location!=null && !location.equals("")){
							new GeocoderTask().execute(location);
						}
					}
				};

				// Setting button click event listener for the find button
				btn_find.setOnClickListener(findClickListener);

				
				googleMap.setMyLocationEnabled(true);
				
				googleMap.setOnMyLocationButtonClickListener(this);
				
				googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				
				//replacing the default marker info window 
				googleMap.setInfoWindowAdapter(new POIInfoWindowAdapter());
				
				//setting listener for infoWindow clicks
				googleMap.setOnInfoWindowClickListener(this);
				
				//initializing a set of markers for the map.
				insertMarkers();
				
			}
			
		}
	}

	//---------------------------------Private Methods--------------------------
	
	/***
	 * Loading the POI into the map
	 */
	private void insertMarkers()
	{
		//TODO consider passing the POI ID within the Marker Snippet attribute.
		
		for(POI poi : ClimbXpertData.POIList) {
			googleMap.addMarker(new MarkerOptions()
			.position(poi.carNavigation).snippet(poi.info)
			.title(String.valueOf(poi.pid)));	
		}
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
		if (null != latlng && null != googleMap)
		{	
			googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
			.target(latlng)
			.zoom(15.5f)
			.bearing(300)//TODO: need to check the compass direction and insert it into the bearing
			.tilt(0)
			.build()));
		}
	}
	
	//---------------------------------------GUI Handlers--------------------------------------
	
	/**
	 * handler to location button click
	 */
	@Override
	public boolean onMyLocationButtonClick() {

		MoveToCurrentLocation();
		
		return false; 
	}
	
	/**
	 * Handler for markers' info window clicks.
	 */
	@Override
	public void onInfoWindowClick(Marker marker) {
		//TODO: 1. Get routes of POI from remote DB 2. set intent with pid 3.open activity
		
		double lat,lng;
		try{
			lat = lastKnownLocation.getLatitude();
			lng = lastKnownLocation.getLongitude();
		}
		catch(Exception e){
			lat = 30;
			lng= 30;
		}
		Intent intent = new Intent(this,POIInfoActivity.class); 
		intent.putExtra("pid",poi.pid);
		intent.putExtra("currLat",lat);
		intent.putExtra("currLng",lng);
		startActivityForResult(intent, 0); 


	}

	//------------------------------------------Helper Classes---------------------------------------------------
	
	/**
	 * The class replaces the default handler for rendering Markers' info bubbles. 
	 *
	 */
	public class POIInfoWindowAdapter implements InfoWindowAdapter {
		
		private final View markerContent;
		
		public POIInfoWindowAdapter() {
			markerContent = getLayoutInflater().inflate(R.layout.poi_marker, null);

		}
		
		/***
		 * creating a view to display inside a POI marker bubble.
		 */
		@Override
		public View getInfoContents(Marker marker) {
	
			
			poi = ClimbXpertData.getPOI(Integer.valueOf(marker.getTitle()));
			
			((TextView) markerContent.findViewById(R.id.markerTextView1)).setText(poi.name);
			
			((TextView) markerContent.findViewById(R.id.markerTextView2)).setText(poi.info);
			
			//TODO: Set image of POI
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
	
	/*************************************/
	// An AsyncTask class for accessing the GeoCoding Web Service
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{
 
        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;
 
            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }
 
        @Override
        protected void onPostExecute(List<Address> addresses) {
 
            if(addresses==null || addresses.size()==0){
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }
 
            // Clears all the existing markers on the map
            googleMap.clear();
 
            // Adding Markers on Google Map for each matching address
            for(int i=0;i<addresses.size();i++){
 
                Address address = (Address) addresses.get(i);
 
                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
 
                String addressText = String.format("%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                address.getCountryName());
 
                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);
 
                googleMap.addMarker(markerOptions);
 
                // Locate the first location
                if(i==0)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }	
}
