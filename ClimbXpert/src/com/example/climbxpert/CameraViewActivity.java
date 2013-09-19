package com.example.climbxpert;

import java.util.ArrayList;

import com.example.climbxpert.POI.POI;
import com.example.climbxpert.POI.PhoneOrientation;
import com.example.climbxpert.POI.StandLocation;
import com.google.android.gms.maps.model.LatLng;

import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CameraViewActivity extends Activity
			implements SensorEventListener{

    private Camera mCamera;
    private CameraView mPreview;
//    private RouteDrawView mDraw;
    private ImageView mRouteImageView;
    
    private POI currPoi;
    
    //sensors
    private SensorManager sensMngr;
	private Sensor magnoSensor;
	private Sensor graviSensor;
    
	
	private float[] lastMagneticData = {0,0,0};
	private float[] lastGravityData = {0,0,0};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_view);
		
		
		//TODO this is an example POI. this should be replaced with the current POI
		//this set of coordinates work in my apartment facing the computer... (itai)
		ArrayList<StandLocation> standLocList = new ArrayList<StandLocation>();
		
		StandLocation stlc = new StandLocation();
		
		stlc.StandInMap = new LatLng(31.762, 35.201);
		stlc.StandOrientation = new PhoneOrientation();
		//setting the coordinates for orientation
		stlc.StandOrientation.GravityTolerance = 5;
		stlc.StandOrientation.xGravity = (float) 9.7;
		stlc.StandOrientation.yGravity = (float) 0.5;
		stlc.StandOrientation.zGravity = (float) 0.5;
		stlc.StandOrientation.MagneticTolerance = 5;
		stlc.StandOrientation.xMagnetic = (float) -28.5;
		stlc.StandOrientation.yMagnetic = (float) 1.6;
		stlc.StandOrientation.zMagnetic = (float) 22.3;
		
		standLocList.add(stlc);
		
		currPoi = new POI(1, "Example POI", "Information on this POI", new LatLng(31.764, 35.204), standLocList);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera_view, menu);
		return true;
	}
	
	
	/**
	 * Try to get a camera instance for the front facing camera. 
	 * @return A Camera instance if successful, null otherwise.
	 */
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
	
	/**
	 * releasing the camera for other applications
	 */
    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }
    
    /**
     * Getting camera instance, initializing relevant views and subscribing to sensor events
     */
    @Override
    protected void onResume()
    {
    	super.onResume();
    	// Create an instance of Camera
        mCamera = getCameraInstance();
        if (mCamera == null) {
        	LoggerTools.LogToast(this, "could not initiate camera.");
        	finishActivity(RESULT_CANCELED);
        }
        
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraView(this, mCamera);
//        mDraw = new RouteDrawView(this);
        mRouteImageView = new ImageView(this);
        mRouteImageView.setImageResource(R.drawable.test);
        
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_view);
        preview.addView(mPreview);
//        preview.addView(mDraw);
        preview.addView(mRouteImageView);
                
        subscribeToSensors();
    }

    /**
     * Releasing camera resources
     */
    private void releaseCamera(){
        
    	unsubscribeSensors();
    	
    	if (mRouteImageView != null)
        {
        	mRouteImageView = null;
        }
    	
    	if (mPreview != null)
        {
        	mPreview.closeView();
        	mPreview = null;
        }
    	
    	if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
        
    }
    
    
    /**
     * Subscribe to Magnetic and Gravity sensors.
     * Initialize sensor manager and other sensors if they weren't before. 
     */
    private void subscribeToSensors()
    {
    	if (null == sensMngr)
    	{
    		sensMngr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    	}
    	if (null == magnoSensor)
    	{
    		magnoSensor = sensMngr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    	}
    	if (null == graviSensor)
    	{
    		graviSensor = sensMngr.getDefaultSensor(Sensor.TYPE_GRAVITY);
    	}
    	
    	sensMngr.registerListener(this, magnoSensor,SensorManager.SENSOR_DELAY_NORMAL);
    	sensMngr.registerListener(this, graviSensor,SensorManager.SENSOR_DELAY_NORMAL);
    	
    }
    
    /**
     * Unsubscribe from all sensor events coming to this activity. 
     */
    private void unsubscribeSensors()
    {
    	sensMngr.unregisterListener(this);
    }

    
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		 //TODO consider putting some handling of accuracy change here. 
		//		perhaps listening for the required sensors initialized on the first time 
		LoggerTools.LogToastShort(this, "Sensor Accuracy changed:" + sensor.getType());
	}

	
	/**
	 * Listen to sensor change events.
	 * Updates the last know coordinates of the relevant sensor and check if the proximity has changed.
	 * If the phone's orientation matches the standing location orientation it will display the route.
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
		{
			lastMagneticData[0] = event.values[0];
			lastMagneticData[1] = event.values[1];
			lastMagneticData[2] = event.values[2];
		}
		if (event.sensor.getType() == Sensor.TYPE_GRAVITY)
		{
			lastGravityData[0] = event.values[0];
			lastGravityData[1] = event.values[1];
			lastGravityData[2] = event.values[2];
		}
		
//		StringBuilder sb = new StringBuilder();
//		sb.append("xg=" + lastGravityData[0] + "\n");
//		sb.append("yg=" + lastGravityData[1] + "\n");
//		sb.append("zg=" + lastGravityData[2] + "\n");
//		
//		sb.append("xm=" + lastMagneticData[0] + "\n");
//		sb.append("ym=" + lastMagneticData[1] + "\n");
//		sb.append("zm=" + lastMagneticData[2] + "\n");
//		
//		LoggerTools.LogToastShort(this, sb.toString());
		
		
		
		
		if (checkProximity())
		{
			mRouteImageView.setVisibility(View.VISIBLE);
		}
		else
		{
			mRouteImageView.setVisibility(View.INVISIBLE);
		}
	}
	
	
	/**
	 * check if the last known coordinates for magnetic and gravity fields are matching the 
	 * current standing location orientation.
	 * @return True if the last known coordinates match the standing location orientation, false otherwise.
	 */
	private boolean checkProximity()
	{
		if (currPoi.standLocationList.get(0).StandOrientation.
				CheckGravityProximity(lastGravityData[0], lastGravityData[1], lastGravityData[2]) &&
				currPoi.standLocationList.get(0).StandOrientation.
				CheckMagneticProximity(lastMagneticData[0], lastMagneticData[1], lastMagneticData[2]))
		{
			return true;
		}
		return false;
	}

}
