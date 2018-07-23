package com.liangxuewen.cameralearning.handlerthreadpool;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.liangxuewen.cameralearning.R;

public class HandlerThreadPoolActivity extends AppCompatActivity {
    private static final String TAG = "lxw-HandlerThreadPoolActivity";
    private Handler mMainHandler;
    private HandlerThread mMainHandlerThread;
    protected DispatchThread mDispatchThread;
    private static final int MSG1 = 1;
    private static final int MSG2 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_thread_pool);

        mMainHandlerThread = new HandlerThread("this is mainhandelrthread");
        mMainHandlerThread.start();
        mMainHandler = new Handler(mMainHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG1:
                        Log.v(TAG,"----MSG1---");
                        break;
                    case MSG2:
                        Log.v(TAG,"----MSG2---");
                        break;
                }
            }
        };

        mDispatchThread = new DispatchThread(mMainHandler, mMainHandlerThread);
        mDispatchThread.start();
    }


    public void newJob1(View v) {
        mDispatchThread.runJob(new Runnable() {
            @Override
            public void run() {
                mMainHandler.sendEmptyMessage(MSG1);
            }
        });


    }

    public void newJob2(View v) {
        mDispatchThread.runJob(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mMainHandler.sendEmptyMessage(MSG2);
            }
        });
    }
}
