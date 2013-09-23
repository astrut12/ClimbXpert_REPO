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
    PhoneOrientation currStandOrientation;
    
    
    //sensors
    private SensorManager sensMngr;
	private Sensor magnoSensor;
	private Sensor graviSensor;
    
	private final int BUFFER_SENSOR = 30;
	
	private float[] currAzimuth = new float[BUFFER_SENSOR];
	private int currIndAz = 0;
	private float[] currTilt = new float[BUFFER_SENSOR];;
	private int currIndTi = 0;
	
	private final float AZIMUTH_TOLERANCE = 100;
	
	private final float TILT_TOLERANCE = 100;
	
	
	
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

		//setting the tilt and azimuth of the orientation		
		stlc.StandOrientation.azimuth = (float) 91;
		stlc.StandOrientation.tilt = (float) 0;
		
		currStandOrientation = stlc.StandOrientation;
		
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
    	
    	sensMngr.registerListener(this, magnoSensor,SensorManager.SENSOR_DELAY_FASTEST);
    	sensMngr.registerListener(this, graviSensor,SensorManager.SENSOR_DELAY_FASTEST);
    	
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
			// for azimuth use Y and Z axis of the magnetic field 
			currAzimuth[currIndAz] = (float) MathOrientation.getAngle(event.values[1], event.values[2]);
			
			currIndAz = (currIndAz<BUFFER_SENSOR-1 ? currIndAz + 1 : 0);
		}
		if (event.sensor.getType() == Sensor.TYPE_GRAVITY)
		{
			// for tilt use X and Z axis of the gravity field
			currTilt[currIndTi] = (float) MathOrientation.getAngle(event.values[0], event.values[2]);
			
			currIndTi = (currIndTi<BUFFER_SENSOR-1 ? currIndTi + 1 : 0);
		}
		
		
		if (checkProximity())
		{
			positionBitmap();
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
		
		if (Math.abs(currStandOrientation.getAzimuthDifference(currAzimuth[0])) < AZIMUTH_TOLERANCE &&
				Math.abs(currStandOrientation.getTiltDifference(currTilt[0])) < TILT_TOLERANCE	)
		{
			return true;
		}
		return false;
	}
	
	
	/**
	 * calculates the offsets of the bitmap according to the current tilt and 
	 */
	private void positionBitmap()
	{
		//TODO calculate the proper scaling for vertical alignment
		int verticalScale = 40;
		int horizontalScale = 40;
		
		
		float azimuth = 0;
		float tilt = 0;
		
		for (int i=0; i<BUFFER_SENSOR; i++)
		{
			azimuth += currAzimuth[i] / BUFFER_SENSOR;
			tilt += currTilt[i] / BUFFER_SENSOR;
		}
		
		
		mRouteImageView.setLeft(-(int)((currStandOrientation.getAzimuthDifference(azimuth))*verticalScale));
		
		mRouteImageView.setTop(-(int)((currStandOrientation.getTiltDifference(tilt))*horizontalScale));
		
	}

}
