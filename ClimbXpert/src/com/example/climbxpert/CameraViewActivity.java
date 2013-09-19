package com.example.climbxpert;

import android.hardware.Camera;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class CameraViewActivity extends Activity {

    private Camera mCamera;
    private CameraView mPreview;
//    private RouteDrawView mDraw;
    private ImageView mRouteImageView;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_view);
		
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
        releaseCamera();
    }
    
    
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
        
    }

    /**
     * Releasing camera resources
     */
    private void releaseCamera(){
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

}
