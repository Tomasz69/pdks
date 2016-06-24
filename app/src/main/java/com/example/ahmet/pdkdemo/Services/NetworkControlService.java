package com.example.ahmet.pdkdemo.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.ahmet.pdkdemo.MainActivity;
import com.example.ahmet.pdkdemo.MessgeWaitng;
import com.example.ahmet.pdkdemo.Settings.WifiSetting;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ahmet on 30.11.2015.
 */
public class NetworkControlService extends Service {

    private static Timer timer = new Timer();
    private Context ctx;

    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    public void onCreate()
    {
        super.onCreate();
        ctx = this;
        startService();
    }

    private void startService()
    {
        timer.scheduleAtFixedRate(new mainTask(), 0,6000);
    }

    private class mainTask extends TimerTask
    {
        public void run()
        {
          if(isNetworking()){

              //sunucu servisini baslatacak

              LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(
                      new Intent(MainActivity.FILTER).putExtra(MainActivity.KEY_networking, "evet"));

          }
            else{

              //gecici bellege eklenecek


              LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(
                      new Intent(MainActivity.FILTER).putExtra(MainActivity.KEY_networking, "hayir"));
          }
        }
    }

    /**
     * internet varmi kotrolu yapar
     * @return
     */
    private boolean isNetworking() {
        WifiSetting wifi = new WifiSetting(getApplicationContext());
        wifi.WifiControl();
        ConnectivityManager connectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    public void onDestroy()
    {
        super.onDestroy();
        Log.i("network_servis", "yok edildi");
    }



}
