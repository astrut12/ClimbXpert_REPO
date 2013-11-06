package com.example.climbxpert;

import com.example.climbxpert.POI.POI;
import com.example.utils.LocationServicesUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigateActivity extends Activity 
				implements
				SensorEventListener, 		// Listen to sensor events.
				ConnectionCallbacks, 		// Actions according to Google location services connection.
				OnConnectionFailedListener,	// Actions on Google location services connection failure.
				LocationListener 			// Listen to location changes from Google location services.
{
	
	//-----------------------------------------Constants-------------------------------------------
	
	
	//-------------------------------------------Fields--------------------------------------------
	
	private SensorManager sensMngr;
	private Sensor magno;
	private TextView xV;
	private TextView yV;
	private TextView zV;
	private TextView tV;
	private TextView tGeo;
	private TextView tDistance;
	private POI currentPOI;
	
	// Client for connecting to location service
	private LocationClient mcLocationClient;
	
	// Location updates options.
	private LocationRequest LOCATION_REQUEST;
	
	// The last received location from the location service
	private Location lastKnownLocation;
	
	private LatLng targetLocation;
	
	
	// The minimum distance in meters to enable camera view button
	private final double MINIMUM_DISTANCE_TO_TARGET = 5.0;
	
	//-------------------------------Activity Life-Cycle Callbacks---------------------------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigate);
		
		xV = (TextView)findViewById(R.id.xValue);
		yV = (TextView)findViewById(R.id.yValue);
		zV = (TextView)findViewById(R.id.zValue);
		tV = (TextView)findViewById(R.id.targetOffset);
		tGeo = (TextView) findViewById(R.id.geoData);
		tDistance = (TextView)findViewById(R.id.targetDistance);
		
		if(!ClimbXpertData.debug) {
			xV.setVisibility(View.GONE);
			yV.setVisibility(View.GONE);
			zV.setVisibility(View.GONE);
			tV.setVisibility(View.GONE);
			tGeo.setVisibility(View.GONE);
			tDistance.setVisibility(View.GONE);
		}
		sensMngr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		magno = sensMngr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		
		//TODO maybe -1 is not invalid id
//		defaultTargetLocation = new LatLng(31.762641,35.201756);
		int recievedPID = this.getIntent().getIntExtra("pid", -1);
		currentPOI = ClimbXpertData.getPOI(recievedPID);
		if (recievedPID < 0 || currentPOI == null) {
			LoggerTools.LogToast(this, "Could not recieve POI from calling activity.");
		}
		
		targetLocation = currentPOI.standLocation;
		
		final int interval = getResources().getInteger(R.integer.compass_location_interval);
		final int fastestInterval = getResources().getInteger(R.integer.compass_location_fastest_interval);
		
		// If not defined otherwise in resources, gets high accuracy.
		final int priorityResource = getResources().getInteger(R.integer.compass_priority);
		final int priority = priorityResource == 0 ? LocationRequest.PRIORITY_HIGH_ACCURACY : priorityResource;
		
		LOCATION_REQUEST = LocationRequest.create()
				.setInterval(interval)
				.setFastestInterval(fastestInterval)
				.setPriority(priority); 
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		sensMngr.registerListener(this, magno, SensorManager.SENSOR_DELAY_NORMAL);
		setUpLocationClientIfNeeded();
		mcLocationClient.connect();
	}
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		switch (requestCode) {
//		// Google location services connection encountered a problem.
//		case ErrorDialogFragment.CONNECTION_FAILURE_RESOLUTION_REQUEST:
//			switch (resultCode) {
//			// Try to connect again.
//			case Activity.RESULT_OK:
//				setUpLocationClientIfNeeded();
//				mcLocationClient.connect();
//				break;
//			}
//		default:
//			super.onActivityResult(requestCode, resultCode, data);
//		}
//	}
	
	@Override
	protected void onPause(){
		super.onPause();
		sensMngr.unregisterListener(this);
		if (null != mcLocationClient)
		{
			mcLocationClient.disconnect();
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Do nothing
	}

	//--------------------------Sensors and Location Services Callbacks----------------------------
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.d(this.getLocalClassName(), getString(R.string.d_connection_failed));
		LocationServicesUtils.tryResolveConnection(result, this);
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		Log.i(this.getLocalClassName(), getString(R.string.d_connected));
		mcLocationClient.requestLocationUpdates(LOCATION_REQUEST, this);		
	}

	@Override
	public void onDisconnected() {
		Log.i(this.getLocalClassName(), getString(R.string.d_disconnected));		
	}
	
	/**
	 * Updates the direction of the compass' arrow in relation to given magnetic
	 * data and last known location. 
	 */
	@Override
	public void onSensorChanged(SensorEvent se) {

		ImageView img = (ImageView)findViewById(R.id.arrowImage);
		
		
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
		
		tDistance.setText("Distance:" + distance);
		
		//TODO consider if Geo information is relevant for users
		
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
	
	//-------------------------------------Private Methods-----------------------------------------
	
	//TODO: consider to remove or move to utils package
	/***
	 * initialize the locClient object if not already initialized
	 */
	private void setUpLocationClientIfNeeded()
	{
		if (mcLocationClient == null)
		{
			mcLocationClient = new LocationClient(getApplicationContext(), this, this);
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
	
	
	private void openCameraView(View view)
    {
    	Intent intent = new Intent(this, CameraViewActivity.class);
    	intent.putExtra("pid",currentPOI.pid);
    	startActivity(intent);
    }
}
