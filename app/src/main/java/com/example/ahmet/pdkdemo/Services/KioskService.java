package com.example.ahmet.pdkdemo.Services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.example.ahmet.pdkdemo.MainActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class KioskService extends Service {

    private static final long INTERVAL = TimeUnit.SECONDS.toMillis(2);
    private static final String TAG = KioskService.class.getSimpleName();
    private static final String PREF_KIOSK_MODE = "pref_kiosk_mode";

    private Thread t = null;
    private Context ctx = null;
    public  static boolean running = false;

    @Override
    public void onDestroy() {
        Log.i(TAG, "Stopping service 'com.crispymtn.kioskapplication.KioskService'");
        running =false;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Starting service 'com.crispymtn.kioskapplication.KioskService'");
        running = true;
        ctx = this;


        t = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    handleKioskMode();
                    try {
                        Thread.sleep(INTERVAL);
                    } catch (InterruptedException e) {
                        Log.i(TAG, "Thread interrupted: 'com.crispymtn.kioskapplication.KioskService'");
                    }
                }while(running);
                stopSelf();
            }
        });

        t.start();
        return Service.START_NOT_STICKY;
    }

    private void handleKioskMode() {

        if(isKioskModeActive(getApplicationContext())) {

            if(isInBackground()) {
                restoreApp();
            }
        }
    }

    private boolean isInBackground() {
        String processName;
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager manager = (ActivityManager)ctx.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = manager.getRunningAppProcesses();
            processName = tasks.get(0).processName;
        }else{

            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            processName = componentInfo.getPackageName();

        }

        return (!ctx.getApplicationContext().getPackageName().equals(processName));
    }

    private void restoreApp() {

        Intent i = new Intent(ctx, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);

    }

    public boolean isKioskModeActive(final Context context) {
        return true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "Bind service");
        return null;
    }
}
