package com.liangxuewen.cameralearning;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.liangxuewen.cameralearning.Utils.ApiHelper;
import com.liangxuewen.cameralearning.asynctask.AsyncTaskActivity;
import com.liangxuewen.cameralearning.cameraApi1.CameraApi1;
import com.liangxuewen.cameralearning.cameratest.SurfaceViewCameraActivity;
import com.liangxuewen.cameralearning.cameratest.TextureViewCameraActivity;
import com.liangxuewen.cameralearning.cameratest.TextureViewVideoActivity;
import com.liangxuewen.cameralearning.handler.HandlerActivity;
import com.liangxuewen.cameralearning.handlerthreadpool.HandlerThreadPoolActivity;
import com.liangxuewen.cameralearning.permission.PermissionsRequestActivity;

public class MainActivity extends AppCompatActivity {

    private boolean mHasCriticalPermissions = false;

    public static final String DAFAULT_PERMISSION_OK = "2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
    }

    public void startCameraTextureView(View view) {
        Intent intent = new Intent(MainActivity.this, TextureViewCameraActivity.class);
        startActivity(intent);
    }

    public void startVideoTextureView(View view) {
        Intent intent = new Intent(MainActivity.this, TextureViewVideoActivity.class);
        startActivity(intent);
    }

    public void startCameraSurfaceView(View view) {
        Intent intent = new Intent(MainActivity.this, SurfaceViewCameraActivity.class);
        startActivity(intent);
    }

    public void startHandlerTestActivity(View view) {
        Intent intent = new Intent(MainActivity.this, HandlerActivity.class);
        startActivity(intent);
    }

    public void startHandlerThreadPool(View view) {
        Intent intent = new Intent(MainActivity.this, HandlerThreadPoolActivity.class);
        startActivity(intent);
    }


    public void startAsyncTaskAcitivty(View view) {
        Intent intent = new Intent(MainActivity.this, AsyncTaskActivity.class);
        startActivity(intent);
    }

    public void startCameraApi1Acitivty(View view) {
        Intent intent = new Intent(MainActivity.this, CameraApi1.class);
        startActivity(intent);
    }


    private void checkPermissions() {
        if (!ApiHelper.isMOrHigher()) {
            mHasCriticalPermissions = true;
            return;
        }

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                &&
                checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mHasCriticalPermissions = true;
        } else {
            mHasCriticalPermissions = false;
        }

        /* SPRD:fix bug 611957 check gps permission should not continue to do task @ */
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mHasCriticalPermissions = false;
        }
        /* @ */

        if (!mHasCriticalPermissions) {
            Intent cameraIntent = getIntent();
            Bundle data = new Bundle();
            data.putParcelable("cameraIntent", cameraIntent);
            Intent intent = new Intent(this, PermissionsRequestActivity.class);
            intent.putExtras(data);

            /*if (!isCaptureIntent()) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//SPRD:fix bug704535
                startActivity(intent);
                finish();
            } else {
                startActivityForResult(intent, 1);
            }*/
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            finish();
        }
    }
}
