package pl.undersoft.picatch;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.SurfaceHolder;



import android.content.Context;

import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;


public class PICATCH_CAMERA extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private PreviewCallback previewCallback;
    private AutoFocusCallback autoFocusCallback;
   // private SurfaceView cameraSurfaceView;

    public PICATCH_CAMERA(Context context, Camera camera,
                         PreviewCallback previewCb,
                         AutoFocusCallback autoFocusCb) {
        super(context);
        mCamera = camera;
        previewCallback = previewCb;
        autoFocusCallback = autoFocusCb;

        /* 
         * Set camera to continuous focus if supported, otherwise use
         * software auto-focus. Only works for API level >=9.
         */
        /*
        Camera.Parameters parameters = camera.getParameters();
        for (String f : parameters.getSupportedFocusModes()) {
            if (f == Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) {
                mCamera.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                autoFocusCallback = null;
                break;
            }
        }
        */

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        //mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            Log.d("DBG", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Camera preview released in activity
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        /*
         * If your preview can change or rotate, take care of those events here.
         * Make sure to stop the preview before resizing or reformatting it.
         */
        if (mHolder.getSurface() == null){
            Log.d("DBG", "Error preview not exist");
          // preview surface does not exist
          return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
          // ignore: tried to stop a non-existent preview
            Log.d("DBG", "Error stoping preview: " + e.getMessage());
        }

        /*try {
            // Hard code camera surface rotation 90 degs to match Activity view in portrait


            Camera.Parameters parametersFlash = mCamera.getParameters();
            parametersFlash.setExposureCompensation(parametersFlash.getMinExposureCompensation());
            parametersFlash.setAutoExposureLock(true);
            parametersFlash.setColorEffect(Parameters.EFFECT_MONO);
            parametersFlash.setFocusMode(Parameters.FOCUS_MODE_FIXED);


            mCamera.setParameters(parametersFlash);
            mCamera.setDisplayOrientation(90);

            mCamera.setPreviewDisplay(mHolder);
            mCamera.setPreviewCallback(previewCallback);

            mCamera.startPreview();

        } catch (Exception e){
            Log.d("DBG", "Error starting camera preview with flash: " + e.getMessage());

        } */
    }
    
    public void resetPreview(boolean state, boolean haveflash) {
	    try {
	        mCamera.stopPreview();
            mCamera.cancelAutoFocus();
	    } catch (Exception e) {
	      // ignore: tried to stop a non-existent preview
            Log.d("DBG", "Error stoping preview: " + e.getMessage());
	    }
	    try {
			Thread.sleep(100);
	    } catch (Exception e) {
		      // ignore: tried to stop a non-existent preview
	    }
		


        Camera.Parameters parametersFlash = mCamera.getParameters();
		if(state) {


                parametersFlash.setAutoExposureLock(false);
              //  parametersFlash.setFocusMode(Parameters.FOCUS_MODE_AUTO);
                parametersFlash.setFocusMode(Parameters.FOCUS_MODE_AUTO);
                parametersFlash.setColorEffect(Parameters.EFFECT_MONO);
          //  parametersFlash.setPreviewSize(640, 480);
            if (haveflash) {

                parametersFlash.setExposureCompensation(parametersFlash.getExposureCompensation());
                parametersFlash.setFlashMode(Parameters.FLASH_MODE_TORCH);

            }
            mCamera.setParameters(parametersFlash);
            try {
                // Hard code camera surface rotation 90 degs to match Activity view in portrait

                mCamera.setDisplayOrientation(90);

                mCamera.setPreviewDisplay(mHolder);

                mCamera.startPreview();
                mCamera.setPreviewCallback(previewCallback);
                mCamera.autoFocus(autoFocusCallback);
            } catch (Exception e){
                Log.d("DBG", "Error starting camera preview: " + e.getMessage());
            }
		} else {


            parametersFlash.setAutoExposureLock(true);
            parametersFlash.setFocusMode(Parameters.FOCUS_MODE_AUTO);
            parametersFlash.setColorEffect(Parameters.EFFECT_MONO);
           // parametersFlash.setPreviewSize(640, 480);
            if (haveflash){

                parametersFlash.setExposureCompensation(parametersFlash.getExposureCompensation());
                parametersFlash.setFlashMode(Parameters.FLASH_MODE_OFF);
            }
            mCamera.setParameters(parametersFlash);
            try {
                // Hard code camera surface rotation 90 degs to match Activity view in portrait
                mCamera.setDisplayOrientation(90);

                mCamera.setPreviewDisplay(mHolder);

                mCamera.stopPreview();
                mCamera.setPreviewCallback(previewCallback);
                mCamera.cancelAutoFocus();
                //mCamera.cancelAutoFocus();
                //mCamera.autoFocus(autoFocusCallback);

            } catch (Exception e){
                Log.d("DBG", "Error starting camera preview: " + e.getMessage());
            }
        }



	}
}

