package com.example.climbxpert;

import com.example.climbxpert.POI.POI;
import com.google.android.gms.maps.model.LatLng;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigateActivity extends Activity 
				implements
				SensorEventListener,
				android.location.LocationListener
{

	private SensorManager sensMngr;
	private Sensor magno;
	
	private LocationManager locationManager;
	private String locationProvider;
	
	private POI currentPOI;
	
	// The last received location from the location service
	private Location lastKnownLocation;
	
	private LatLng targetLocation;
	
	
	// The minimum distance in meters to enable camera view button
	private final double MINIMUM_DISTANCE_TO_TARGET = 1000.0;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigate);
		
		sensMngr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		magno = sensMngr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		// Check if enabled and if not send user to the GSP settings
		// Better solution would be to display a dialog and suggesting to
		// go to the settings
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		}
	    // Define the criteria how to select the locatioin provider -> use
	    // default
	    Criteria criteria = new Criteria();
	    criteria.setAccuracy(Criteria.ACCURACY_FINE);
	    locationProvider = locationManager.getBestProvider(criteria, false);
	    
	    
	    
	    
		
		//TODO maybe -1 is not invalid id
//		defaultTargetLocation = new LatLng(31.762641,35.201756);
		int recievedPID = this.getIntent().getIntExtra("pid", -1);
		currentPOI = ClimbXpertData.getPOI(recievedPID);
		if (recievedPID < 0 || currentPOI == null) {
			LoggerTools.LogToast(this, "Could not recieve POI from calling activity.");
		}
		
		targetLocation = currentPOI.standLocation;
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		sensMngr.registerListener(this, magno, SensorManager.SENSOR_DELAY_NORMAL);
		locationManager.requestLocationUpdates(locationProvider, 1000, 1, this);
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		sensMngr.unregisterListener(this);
		locationManager.removeUpdates(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.navigate, menu);
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Do nothing
	}

	/**
	 * Updates the direction of the compass' arrow in relation to given magnetic
	 * data and last known location. 
	 */
	@Override
	public void onSensorChanged(SensorEvent se) {

		ImageView img = (ImageView)findViewById(R.id.arrowImage);
		TextView xV = (TextView)findViewById(R.id.xValue);
		TextView yV = (TextView)findViewById(R.id.yValue);
		TextView zV = (TextView)findViewById(R.id.zValue);
		TextView tV = (TextView)findViewById(R.id.targetOffset);
		
		float compassOffset = 0;
		float locationOffset =  getAngleToLocation();
		
		xV.setText("X:" + se.values[0]);
		yV.setText("Y:" + se.values[1]);
		tV.setText("target:" + locationOffset);
		
		float x = se.values[0];
		float y = se.values[1];
		
		
		double Dangle = MathOrientation.normalizeAngle((float)MathOrientation.getAngle(x,y)-90);
		
		
		float rotaionangle = compassOffset - locationOffset - (float)Dangle;
		zV.setText("rotaion Angle:" + rotaionangle);
		
		
		img.setRotation(rotaionangle);
		
	}
	
	/**
	 * Updates the last known location and update distance calculations.
	 * Triggers Camera View button if close enough.
	 */
	@Override
	public void onLocationChanged(Location location) {
		lastKnownLocation = location;
		
		double distance = getDistanceToTarget();
		
		// show the user the remaining distance
		TextView tDistance = (TextView)findViewById(R.id.targetDistance);
		tDistance.setText("Distance:" + distance);
		
		//TODO consider if Geo information is relevant for users
		TextView tGeo = (TextView) findViewById(R.id.geoData);
		tGeo.setText("Geo:" + location.getLatitude() + ", " + location.getLongitude());
		
		if (distance < MINIMUM_DISTANCE_TO_TARGET)
		{
			enableCameraViewButton();
		}
		else
		{
			disableCamraViewButton();
		}
		
	}
	
	/**
	 * Calculates the angle from the last known location to the target location 
	 * @return The angle degrees, normalized.
	 */
	private float getAngleToLocation()
	{
		
		if (null == lastKnownLocation)
		{
			return 0; // no current location yet
			
		}
		
		// latitude is y axis, longitude is x axis
		return (float) MathOrientation.normalizeAngle((float)
				MathOrientation.getAngle(targetLocation.longitude - lastKnownLocation.getLongitude(),
										targetLocation.latitude - lastKnownLocation.getLatitude())-90);

	}
	
	
	/**
	 * Calculates the distance from the last known location to the target location.
	 * This is done with approximation with the assumption that the distance between 
	 * the two locations is negligible comparing to earth radius.  
	 * @return The distance in meters
	 */
	private double getDistanceToTarget()
	{
		if (null == lastKnownLocation)
		{
			return -1;
		}
		
		return MathOrientation.calculateLocationsDistance(
					new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 
					targetLocation);
		
	}
	

	/**
	 *  Hide the Camera View button.
	 */
	private void disableCamraViewButton() {
		Button cvb = (Button) findViewById(R.id.CameraViewButton);
		
		cvb.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * Display the Camera View button and make it clickable
	 */
	private void enableCameraViewButton() {
		Button cvb = (Button) findViewById(R.id.CameraViewButton);
		
		cvb.setVisibility(View.VISIBLE);
	}
	
	
	public void openCameraView(View view)
    {
    	Intent intent = new Intent(this, CameraViewActivity.class);
    	intent.putExtra("pid",currentPOI.pid);
    	startActivity(intent);
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
}
