package com.liangxuewen.cameralearning.cameratest;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;

import com.liangxuewen.cameralearning.R;

import java.io.IOException;

public class TextureViewCameraActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener{

    private static final String TAG= "lxw-TextureViewCameraActivity";
    private TextureView mCameraTextureView;
    private SurfaceTexture mSurfaceTexture;
    private Camera mCamera;
    private Camera.Parameters mParameters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_texture_view);
        mCameraTextureView = findViewById(R.id.camera_textureview);
        mCameraTextureView.setSurfaceTextureListener(this);
        //mCameraTextureView.setRotation(90);
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.v(TAG,"----onSurfaceTextureAvailable----");
        mCamera = Camera.open();
        mParameters = mCamera.getParameters();
        /*mParameters.setRotation(270);
        mCamera.setParameters(mParameters);*/
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(0,info);
        Log.v(TAG, "Orientation = "+info.orientation);
        mCamera.setDisplayOrientation(90);
        mSurfaceTexture = surfaceTexture;

        try {
            mCamera.setPreviewTexture(mSurfaceTexture);
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
        mCamera.startPreview();

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.v(TAG,"----onSurfaceTextureSizeChanged----");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        Log.v(TAG,"----onSurfaceTextureDestroyed----");
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        surfaceTexture.release();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        Log.v(TAG,"----onSurfaceTextureUpdated----");

    }
}
