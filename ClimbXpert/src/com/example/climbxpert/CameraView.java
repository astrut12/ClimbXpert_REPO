package com.example.climbxpert;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class CameraView extends SurfaceView implements Callback {
	private SurfaceHolder mHolder;
    private Camera mCamera;
    private Activity mActivity;

	public CameraView(Context context, Camera camera) {
		super(context);
        mCamera = camera;
        mActivity = (Activity) context;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            
            Camera.Parameters camParams = mCamera.getParameters();
            List<Size> Sizes = camParams.getSupportedPreviewSizes();
            camParams.setPreviewSize(Sizes.get(0).width, Sizes.get(0).height);
            mCamera.setParameters(camParams);
            
            
            mCamera.startPreview();
        } catch (IOException e) {
            LoggerTools.LogToast(mActivity, "Error setting camera preview: " + e.getMessage());
        }
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
       // empty. Releasing the Camera preview in the calling Activity.		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// empty. Suits only full horizontal view.
	}

}
