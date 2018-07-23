package com.liangxuewen.cameralearning.handler;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.os.HandlerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.liangxuewen.cameralearning.R;

public class HandlerActivity extends AppCompatActivity {
    private static final String TAG = "lxw-HandlerActivity";
    private TextView mShowMsgText;
    private Handler mMainHandler;
    private Handler mNewThreadHandler;
    private MyThread mMythread;
    private Handler mHandlerThreadHandler;
    private static final int UPDATE_MSG = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
        mShowMsgText = findViewById(R.id.show_message);
    }

    public void startMainThreadHandler(View v) {

        mMainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case UPDATE_MSG:
                        String logmsg = "mMainHandler thread id = " + mMainHandler.getLooper().getThread().getId()
                                + " ; Main Thread id " + getMainLooper().getThread().getId();
                        Log.v(TAG, logmsg);
                        mShowMsgText.setText(logmsg);
                        break;
                }
            }
        };
        mMainHandler.sendEmptyMessage(UPDATE_MSG);
    }

    public void startNewThreadHandler(View v) {
        /*运行上述代码，我们发现一个问题，就是此代码一会崩溃、一会不崩溃，通过查看日志我们看到崩溃的原因是空指针。
        谁为空？？？查到是我们的Looper对象，怎么会那？我不是在子线程的run方法中初始化Looper对象了么？
        话是没错，但是你要知道，当你statr子线程的时候，虽然子线程的run方法得到执行，
        但是主线程中代码依然会向下执行，造成空指针的原因是当我们new Handler(childThread.childLooper)的时候，
        run方法中的Looper对象还没初始化。当然这种情况是随机的，所以造成偶现的崩溃。


        HandlerThread类就是解决这个问题的关键所在*/
        mMythread = new MyThread();
        mMythread.start();
        mNewThreadHandler = new Handler(mMythread.myLooper) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case UPDATE_MSG:
                        String logmsg = "mNewThreadHandler thread id = " + mNewThreadHandler.getLooper().getThread().getId()
                                + " ; Main Thread id " + getMainLooper().getThread().getId();
                        Log.d(TAG, logmsg);
                        break;
                }
            }
        };
        mNewThreadHandler.sendEmptyMessage(UPDATE_MSG);
    }

    private class MyThread extends Thread {
        public Looper myLooper;

        @Override
        public void run() {
            Looper.prepare();//创建与当前线程相关的Looper
            myLooper = Looper.myLooper();//获取当前线程的Looper对象
            Looper.loop();//调用此方法，消息才会循环处理
        }
    }


    public void newHandlerThread(View view) {
        HandlerThread myHandlerThread = new HandlerThread("this is myHandlerThread");
        myHandlerThread.start();
        mHandlerThreadHandler = new Handler(myHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case UPDATE_MSG:
                        String logmsg = "myHandlerThreadHandler thread id = " + mHandlerThreadHandler.getLooper().getThread().getId()
                                + " ; Main Thread id " + getMainLooper().getThread().getId();
                        Log.d(TAG, logmsg);
                        //mShowMsgText.setText(logmsg);
                        mMainHandler.sendEmptyMessage(UPDATE_MSG);
                        break;
                }

            }
        };
        mHandlerThreadHandler.sendEmptyMessage(UPDATE_MSG);
    }

    public void clear(View v) {
        mShowMsgText.setText("");
    }
}
