package com.cl.testapp.widget.test;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.cl.testapp.R;

import java.util.Random;

/**
 *
 * Created by Administrator on 2016-12-26.
 */
public class TestWidgetService extends Service{

    private MediaPlayer mMediaPlayer;
    private TestBroadcastReceiver mReceiver;
    // 更新 widget 的广播对应的action
    private final String ACTION_START = "com.cl.testapp.action.start";
    private final String ACTION_REFESH = "com.cl.testapp.action.refresh";
    // 周期性更新 widget 的周期
    private static final int UPDATE_TIME = 5000;
    // 周期性更新 widget 的线程
    private UpdateThread mUpdateThread;
    private Context mContext;
    // 更新周期的计数
    private int count=0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        // 创建并开启线程UpdateThread
        mUpdateThread = new UpdateThread();
        mUpdateThread.start();
        //注册广播
        mReceiver=new TestBroadcastReceiver();
        IntentFilter filter=new IntentFilter();
        filter.setPriority(Integer.MAX_VALUE);
        filter.addAction(ACTION_REFESH);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        // 中断线程，即结束线程。
        if (mUpdateThread != null) {
            mUpdateThread.interrupt();
        }
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    private class UpdateThread extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                count = 0;
                while (true) {
                    count++;
                    Intent updateIntent = new Intent(ACTION_START);
                    mContext.sendBroadcast(updateIntent);
                    Thread.sleep(UPDATE_TIME);
                }
            } catch (InterruptedException e) {
                // 将 InterruptedException 定义在while循环之外，意味着抛出 InterruptedException 异常时，终止线程。
                e.printStackTrace();
            }
        }
    }

    private void startAlarm(Context context) {
        try {
            if (mMediaPlayer == null){
                mMediaPlayer = MediaPlayer.create(context, R.raw.music);
                mMediaPlayer.setLooping(false);
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
            }
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }else {
                mMediaPlayer.prepareAsync();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    class TestBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_REFESH)){
                Log.i("xl", "TestWidgetService " + intent.getAction());
                // 获得随机数
                int index = (new Random().nextInt(5));
                Log.i("xl" , "index " + index);
                if (index == 1){
                    startAlarm(mContext);
                }
            }
        }
    }

}
