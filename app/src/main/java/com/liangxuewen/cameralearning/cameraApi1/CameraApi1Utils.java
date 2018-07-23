package com.liangxuewen.cameralearning.cameraApi1;

import android.hardware.Camera;

public class CameraApi1Utils {

    private static CameraManager sCameraManager;

    public static CameraManager getCameraManagerInstance() {
        if (sCameraManager == null) {
            sCameraManager =  new AndroidCameraManagerImpl();
        }
        return sCameraManager;
    }
}
