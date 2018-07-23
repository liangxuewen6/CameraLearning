package com.liangxuewen.cameralearning.handlerthreadpool;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;

public class DispatchThread extends Thread {
    private static final String TAG = "lxw-DispatchThread";

    private final Queue<Runnable> mJobQueue;
    private Handler mMainHandler;
    private HandlerThread mMainHanderThread;

    private static final long MAX_MESSAGE_QUEUE_LENGTH = 256;

    public DispatchThread(Handler mainHandler, HandlerThread mainHandlerThread) {
        super("DispatchThread");
        this.mJobQueue = new LinkedList<Runnable>();;
        mMainHandler = mainHandler;
        mMainHanderThread = mainHandlerThread;
    }


    public void runJob(Runnable job) {
        synchronized (mJobQueue) {
            if (mJobQueue.size() == MAX_MESSAGE_QUEUE_LENGTH) {
                throw new RuntimeException("Camera master thread job queue full");
            }
            mJobQueue.add(job);
            Log.v(TAG,"----runJob mJobQueue size ="+mJobQueue.size());
            mJobQueue.notifyAll();
        }
    }


    @Override
    public void run() {
        Log.v(TAG,"----run----");
        while (true) {
            Log.v(TAG,"----run while(true)----");
            Runnable job = null;
            synchronized (mJobQueue) {
                while (mJobQueue.size() == 0) {
                    Log.v(TAG,"----run wait----");
                    try {
                        mJobQueue.wait();
                    }catch (InterruptedException ex) {
                        ex.printStackTrace();
                        Log.v(TAG,"----run InterruptedException----");
                        break;
                    }
                }
                Log.v(TAG,"----mJobQueue poll----");
                job = mJobQueue.poll();
            }

            if (job == null) {
                if (false) {
                    break;
                }
                continue;
            }
            Log.v(TAG,"Runnable run");
            job.run();
        }
        Log.v(TAG,"mMainHanderThread quitSafely");
        mMainHanderThread.quitSafely();
    }
}
