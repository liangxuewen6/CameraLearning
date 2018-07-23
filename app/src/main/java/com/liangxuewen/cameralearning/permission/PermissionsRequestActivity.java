package com.liangxuewen.cameralearning.permission;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.liangxuewen.cameralearning.MainActivity;
import com.liangxuewen.cameralearning.R;

public class PermissionsRequestActivity extends Activity {
    private static final String TAG = "PermissionsRequestActivity";
    private int mNumPermissionsToRequest;

    private boolean mShouldRequestCameraPermission;
    private boolean mFlagHasCameraPermission;

    private boolean mShouldRequestMicrophonePermission;
    private boolean mFlagHasMicrophonePermission;

    private boolean mShouldRequestStoragePermission;
    private boolean mFlagHasStoragePermission;

    private boolean mShouldRequestWriteStoragePermission;
    private boolean mFlagHasWriteStoragePermission;

    private boolean mShouldRequestLocationPermission;

    private int mIndexPermissionRequestCamera;
    private int mIndexPermissionRequestMicrophone;
    private int mIndexPermissionRequestLocation;
    private int mIndexPermissionRequestStorage;
    private int mIndexPermissionRequestWriteStorage;

    private static int PERMISSION_REQUEST_CODE = 1;

    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_request);

        Intent cameraIntent = (Intent) getIntent().getParcelableExtra("cameraIntent");

        Window win = getWindow();
        //if (isSecureCamera) {
        if (false) {
            win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        } else {
            win.clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        }
        mNumPermissionsToRequest = 0;
        checkPermissions();
    }

    private void checkPermissions() {
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mNumPermissionsToRequest++;
            mShouldRequestCameraPermission = true;
        } else {
            mFlagHasCameraPermission = true;
        }

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            mNumPermissionsToRequest++;
            mShouldRequestMicrophonePermission = true;
        } else {
            mFlagHasMicrophonePermission = true;
        }

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            mNumPermissionsToRequest++;
            mShouldRequestStoragePermission = true;
        } else {
            mFlagHasStoragePermission = true;
        }

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            mNumPermissionsToRequest++;
            mShouldRequestWriteStoragePermission = true;
        } else {
            mFlagHasWriteStoragePermission = true;
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            mNumPermissionsToRequest += 2;
            mShouldRequestLocationPermission = true;
        }

        if (mNumPermissionsToRequest != 0) {
            /* SPRD:fix bug519999 Create header photo, pop permission error
             * original code
             * if (!isKeyguardLocked() && !mSettingsManager.getBoolean(SettingsManager.SCOPE_GLOBAL,
             *      Keys.KEY_HAS_SEEN_PERMISSIONS_DIALOGS)) {
             */
            //if (!isSecureCamera) {
            if (true) {
                buildPermissionsRequest();
            } else {
                // Permissions dialog has already been shown, or we're on
                // lockscreen, and we're still missing permissions.
                handlePermissionsFailure();
            }
        } else {
            handlePermissionsSuccess();
        }
    }


    private void buildPermissionsRequest() {
        String[] permissionsToRequest = new String[mNumPermissionsToRequest];
        int permissionsRequestIndex = 0;

        if (mShouldRequestCameraPermission) {
            permissionsToRequest[permissionsRequestIndex] = Manifest.permission.CAMERA;
            mIndexPermissionRequestCamera = permissionsRequestIndex;
            permissionsRequestIndex++;
        }
        if (mShouldRequestMicrophonePermission) {
            permissionsToRequest[permissionsRequestIndex] = Manifest.permission.RECORD_AUDIO;
            mIndexPermissionRequestMicrophone = permissionsRequestIndex;
            permissionsRequestIndex++;
        }
        if (mShouldRequestStoragePermission) {
            permissionsToRequest[permissionsRequestIndex] = Manifest.permission.READ_EXTERNAL_STORAGE;
            mIndexPermissionRequestStorage = permissionsRequestIndex;
            permissionsRequestIndex++;
        }
        if (mShouldRequestWriteStoragePermission) {
            permissionsToRequest[permissionsRequestIndex] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            mIndexPermissionRequestWriteStorage = permissionsRequestIndex;
            permissionsRequestIndex++;
        }
        if (mShouldRequestLocationPermission) {
            permissionsToRequest[permissionsRequestIndex] = Manifest.permission.ACCESS_COARSE_LOCATION;
            permissionsToRequest[permissionsRequestIndex + 1] = Manifest.permission.ACCESS_FINE_LOCATION; // SPRD: fix bug 744895
            mIndexPermissionRequestLocation = permissionsRequestIndex;
        }

        requestPermissions(permissionsToRequest, PERMISSION_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.v(TAG, "onPermissionsResult counts: " + permissions.length + ":" + grantResults.length);
          /*
            Bug640995 camera fail Dialog display unnormal
         */
        if (permissions.length == 0) {
            return;
        }
        mFlagHasCameraPermission = checkPermissionResult(mShouldRequestCameraPermission, grantResults, mIndexPermissionRequestCamera, mFlagHasCameraPermission);
        mFlagHasMicrophonePermission = checkPermissionResult(mShouldRequestMicrophonePermission, grantResults, mIndexPermissionRequestMicrophone, mFlagHasMicrophonePermission);
        mFlagHasStoragePermission = checkPermissionResult(mShouldRequestStoragePermission, grantResults, mIndexPermissionRequestStorage, mFlagHasStoragePermission);
        mFlagHasWriteStoragePermission = checkPermissionResult(mShouldRequestWriteStoragePermission, grantResults, mIndexPermissionRequestWriteStorage, mFlagHasWriteStoragePermission);
        if (mFlagHasCameraPermission && mFlagHasMicrophonePermission && mFlagHasStoragePermission) {
            handlePermissionsSuccess();
        }
    }

    private boolean checkPermissionResult(boolean shouldRequestPermission, int[] grantResults, int permissionIndex, boolean permissionFlag) {
        if (shouldRequestPermission) {
            if (grantResults.length > 0 && grantResults[permissionIndex] == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                handlePermissionsFailure();
                return permissionFlag;
            }
        }
        return permissionFlag;
    }

    private void handlePermissionsFailure() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            return;
        }

        mAlertDialog = new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.camera_error_title))
                .setMessage(getResources().getString(R.string.error_permissions))
                .setCancelable(false)
                .setOnKeyListener(new Dialog.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            finish();
                        }
                        return true;
                    }
                })
                .setPositiveButton(getResources().getString(R.string.dialog_dismiss),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //if (isCaptureIntent()) {
                                if (false) {
                                    setResult(RESULT_CANCELED);
                                }
                                setResult(RESULT_CANCELED);
                                finish();
                            }
                        })
                .show();
    }

    private void handlePermissionsSuccess() {
        //if (!isCaptureIntent()) {
        if (true) {
            Intent intent = new Intent(this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("result", MainActivity.DAFAULT_PERMISSION_OK);
            /*intent.putExtras(bundle);
            intent.setAction(cameraIntentAction);*/
            startActivity(intent);
        } else {
            setResult(RESULT_OK);
        }

        /*try {
            isShouldFinishNextTime = ActivityManagerNative.getDefault().isInLockTaskMode();
        } catch (Exception e) {
            isShouldFinishNextTime = false;
        }*/
        finish();
    }
}
