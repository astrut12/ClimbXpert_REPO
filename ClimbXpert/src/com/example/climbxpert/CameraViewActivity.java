package com.example.climbxpert;

import android.hardware.Camera;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.FrameLayout;

public class CameraViewActivity extends Activity {

    private Camera mCamera;
    private CameraView mPreview;
    private RouteDrawView mDraw;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_view);
		
        // Create an instance of Camera
        mCamera = getCameraInstance();
        if (mCamera == null) {
        	LoggerTools.LogToast(this, "could not initiate camera.");
        	finishActivity(RESULT_CANCELED);
        }

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraView(this, mCamera);
        mDraw = new RouteDrawView(this);
        
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_view);
        preview.addView(mPreview);
        preview.addView(mDraw);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera_view, menu);
		return true;
	}
	
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
	
    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

}
