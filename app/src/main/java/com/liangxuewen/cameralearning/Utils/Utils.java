package com.liangxuewen.cameralearning.Utils;

import android.content.Context;
import android.content.pm.PackageManager;

public class Utils {

    private static boolean isRecordLocationEnabled = false;

    private final static String TARGET_RECORD_LOCATION_ENABLE = "persist.sys.cam.gps";


    //检查设备是否含有相机
    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        } else {
            return false;
        }
    }
}
