package com.liangxuewen.cameralearning.cameraApi1;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import com.liangxuewen.cameralearning.R;
import com.liangxuewen.cameralearning.Utils.Utils;

import java.io.File;
import java.io.IOException;


public class CameraApi1Activity extends Activity implements TextureView.SurfaceTextureListener {
    private static final String TAG = "CameraApi1Activity";
    private TextureView mTextureView;
    private SurfaceTexture mSurfaceTexture;
    private Camera mCamera;
    private Camera.Parameters mParameters;
    private static final int CAMERA_MODULE = 0;
    private static final int VIDEO_MODULE = 1;
    private int mModule = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean hasCamera = Utils.checkCameraHardware(this);
        if (!hasCamera) {
            Toast.makeText(this, R.string.miss_camera_warning, Toast.LENGTH_SHORT).show();
            finish();
        }
        setContentView(R.layout.activity_camera_api1);
        mTextureView = findViewById(R.id.camera_textureview_with_api1);
        mTextureView.setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        mSurfaceTexture = surfaceTexture;

        mCamera = CameraApi1Utils.getCameraInstance(0);
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

    public void onShutterButtonOnclick(View view) {
        switch (mModule) {
            case CAMERA_MODULE:

                /*代码中调用Camera#takePciture()方法来进行拍照。
                该方法接受三个参数，
                第一个参数ShutterCallback响应快门的接口，
                第二个参数PictureCallback接收raw格式的图片数据，
                第三个参数PictureCallback接收jpeg格式的图片数据
                */
                break;
        }
    }

    private Camera.PictureCallback mPictureCallBack = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            //File pictureFile = getO
        }
    };
}
