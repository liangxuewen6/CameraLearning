package com.liangxuewen.cameralearning.cameraApi1;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;

import com.liangxuewen.cameralearning.R;

import java.io.IOException;


public class CameraApi1 extends Activity implements TextureView.SurfaceTextureListener {
    private static final String TAG = "CameraApi1";
    private TextureView mTextureView;
    private SurfaceTexture mSurfaceTexture;
    private Camera mCamera;
    private Camera.Parameters mParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_api1);
        mTextureView = findViewById(R.id.camera_textureview_with_api1);
        mTextureView.setSurfaceTextureListener(this);

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        mSurfaceTexture = surfaceTexture;

        mCamera = Camera.open();
        mParameters = mCamera.getParameters();
        /*mParameters.setRotation(270);
        mCamera.setParameters(mParameters);*/
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(0, info);
        Log.v(TAG, "Orientation = " + info.orientation);
        mCamera.setDisplayOrientation(90);

        try {
            mCamera.setPreviewTexture(mSurfaceTexture);
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
        mCamera.startPreview();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        surfaceTexture.release();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }
}
