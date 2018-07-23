package com.liangxuewen.cameralearning.asynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.liangxuewen.cameralearning.R;

public class AsyncTaskActivity extends AppCompatActivity {
    private static final String TAG = "lxw-AsyncTaskActivity";
    private AsyncTask<Void,Integer,Boolean> mAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);
    }

    public void startDownloadTask(View v) {
        mAsyncTask = new downloadTask();
        //mAsyncTask.execute();//连续启动时串行执行
        mAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);//连续启动时并行执行
    }

    public void stopDownloadTask(View v) {
        if (mAsyncTask != null) {
            mAsyncTask.cancel(true);
        }
    }


    class downloadTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            Log.v(TAG,"---onPreExecute----"+Thread.currentThread().getId());
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.v(TAG,"---doInBackground----"+Thread.currentThread().getId());
            int progress = 0;
            while (progress != 100) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progress = progress + 10;
                //在doInBackground这个方法中是不可以进行UI操作的，
                // 如果需要更新UI元素，比如说反馈当前任务的执行进度，
                // 可以调用publishProgress(Progress...)方法来完成。
                publishProgress(progress);
                Log.v(TAG,"isCancelled = "+isCancelled()+"  progress = "+progress);
                if (isCancelled()) {
                    break;
                }
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.v(TAG,"---onProgressUpdate----"+Thread.currentThread().getId()+"  progress = "+values[0].intValue());
            Toast.makeText(AsyncTaskActivity.this, "progress :" + values[0].intValue(), Toast.LENGTH_SHORT).show();
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean b) {
            Log.v(TAG,"---onPostExecute----"+Thread.currentThread().getId());
            super.onPostExecute(b);
            Toast.makeText(AsyncTaskActivity.this, "Download done!" + b, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.v(TAG,"---onCancelled----"+Thread.currentThread().getId());
        }
    }
}
