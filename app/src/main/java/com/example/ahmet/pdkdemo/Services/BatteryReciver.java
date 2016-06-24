package com.example.ahmet.pdkdemo.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.ahmet.pdkdemo.ControlInterface;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ahmet on 01.03.2016.
 */
public class BatteryReciver extends BroadcastReceiver {

    private Handler h;
    public BatteryReciver(Handler h){
        this.h=h;
    }



    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_TIME_CHANGED) ||
                intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String str = sdf.format(new Date());

            Bundle b = new Bundle();
            Message msg = h.obtainMessage(ControlInterface.HOURS_SHOW);
            b.putString("date", str);
            msg.setData(b);
            h.sendMessage(msg);

            Log.i("fds","fdv");

        }





    }
}
