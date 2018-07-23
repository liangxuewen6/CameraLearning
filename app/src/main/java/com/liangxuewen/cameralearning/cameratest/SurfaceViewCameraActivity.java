package com.liangxuewen.cameralearning.cameratest;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.liangxuewen.cameralearning.R;

import java.io.IOException;

/*1，依据布局，获取Surfaceview对象；

2，获取Holder对象并设置属性；

3，绑定SurfaceHolder.Callback回调接口；

4，回调接口的surfaceCreated方法中设置Camera并设置对应属性；

5，设置拍照点击事件，在监听事件中绑定Camera.PictureCallback回调监听；

6，PictureCallback监听中依据回调数据，进行结果保存。【保存照片并展示】*/
public class SurfaceViewCameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private static final String TAG ="lxw-SurfaceViewCameraActivity";

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_surface_view);
        mSurfaceView = findViewById(R.id.camera_surfaceview);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(mSurfaceHolder);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.v(TAG,"----surfaceCreated----");
        mCamera =Camera.open();
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.v(TAG,"----surfaceChanged----");
        mCamera.setDisplayOrientation(getDisplayOrientation());
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.v(TAG,"----surfaceDestroyed----");
        mSurfaceHolder.removeCallback(this);
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    // 获取当前窗口管理器显示方向
    private int getDisplayOrientation(){
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation){
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        android.hardware.Camera.CameraInfo camInfo =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, camInfo);

        int result = (camInfo.orientation - degrees + 360) % 360;

        return result;
    }
}
