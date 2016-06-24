package com.example.ahmet.pdkdemo.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.ahmet.pdkdemo.MainActivity;

/**
 * Created by Ahmet on 17.04.2016.
 */
public class AutoOpenBroadcast extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        try{
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            }
        catch (Exception e){
            Log.i("Broadcast",e.getMessage().toString());
        }

    }
}
