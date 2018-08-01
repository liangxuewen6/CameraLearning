package com.liangxuewen.cameralearning.cameraApi1;

import android.hardware.Camera;

public class CameraApi1Utils {

    private static CameraManager sCameraManager;
    private static Camera sCamera;

    /*必须得到一个Camera类的实例才能访问相机（除非你使用Intent快速访问相机）。
    为了访问相机基本功能，可以使用Camera#open()方法来获得一个Camera的实例*/
    public static Camera getCameraInstance(int cameraId) {
        if (sCamera == null) {
            sCamera = Camera.open(cameraId);
        }
        return sCamera;
    }

    /*可以使用Camera#getParameters()方法来获取相机参数信息，
    可以根据返回值 Camera.Parameters 类来查看当前camea支持哪些参数设置等。
    当使用API 9或者更高时，你可以使用Camera.getCameraInfo()静态方法来获取前后camera的ID，
    以及camera数据流的方向和是否能禁止拍照快门声音标记*/
    public static Camera.CameraInfo getCameraInfo(int cameraId) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, cameraInfo);
        return cameraInfo;
    }

    public static CameraManager getCameraManagerInstance() {
        if (sCameraManager == null) {
            sCameraManager = new AndroidCameraManagerImpl();
        }
        return sCameraManager;
    }

}
