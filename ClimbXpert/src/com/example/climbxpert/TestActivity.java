package com.example.climbxpert;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.TextView;

public class TestActivity extends Activity 
				implements SensorEventListener{

	private SensorManager sensMngr;
	private Sensor magnoSensor;
	private Sensor graviSensor;
	
	
	private TextView tvgx;
	private TextView tvgy;
	private TextView tvgz;
	private TextView tvmx;
	private TextView tvmy;
	private TextView tvmz;
	//angles
	private TextView tvgxza;
	private TextView tvmyza;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		tvgx = (TextView) findViewById(R.id.tvgx);
		tvgy = (TextView) findViewById(R.id.tvgy);
		tvgz = (TextView) findViewById(R.id.tvgz);
		tvmx = (TextView) findViewById(R.id.tvmx);
		tvmy = (TextView) findViewById(R.id.tvmy);
		tvmz = (TextView) findViewById(R.id.tvmz);
		
		tvgxza = (TextView) findViewById(R.id.tvgxza);
		tvmyza = (TextView) findViewById(R.id.tvmyza);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		subscribeToSensors();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		unsubscribeSensors();
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
		LoggerTools.LogToastShort(this, "Sensor Accuracy changed:" + sensor.getType());		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
		{
			tvmx.setText("MX=" + event.values[0]);
			tvmy.setText("MY=" + event.values[1]);
			tvmz.setText("MZ=" + event.values[2]);
			
			//calculating angle between magnetic's y and z coordinates
			double myzAngle = MathOrientation.getAngle(event.values[1], event.values[2]);
			
			tvmyza.setText("MYZAngle=" + myzAngle);
			
		}
		if (event.sensor.getType() == Sensor.TYPE_GRAVITY)
		{
			tvgx.setText("GX=" + event.values[0]);
			tvgy.setText("GY=" + event.values[1]);
			tvgz.setText("GZ=" + event.values[2]);
			
			//calculating angle between gravity's x and z coordinates
			double gxzAngle = MathOrientation.getAngle(event.values[0], event.values[2]);
			
			tvgxza.setText("GXZAngle=" + gxzAngle);
		}		
	}

}
