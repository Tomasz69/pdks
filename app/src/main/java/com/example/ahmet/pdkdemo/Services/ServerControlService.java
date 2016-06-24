package com.example.ahmet.pdkdemo.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.ahmet.pdkdemo.Database.DatabaseHelper;
import com.example.ahmet.pdkdemo.MainActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ahmet on 30.11.2015.
 */
public class ServerControlService extends Service {


    private static Timer timer = new Timer();
    private Context ctx;
    private static String url = "http://xmlpanel.com/panel/server/";
    private String urladres;

    DatabaseHelper db;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ctx = this;
        db = new DatabaseHelper(this);
        startService();
    }

    private void startService() {
        timer.scheduleAtFixedRate(new mainTask(), 0,6000);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    private class mainTask extends TimerTask {
        @Override
        public void run() {
            if (isConnectedToServer(getUrladres())) {
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(
                        new Intent(MainActivity.FILTER2).putExtra(MainActivity.KEY_server, "evet"));
            } else {
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(
                        new Intent(MainActivity.FILTER2).putExtra(MainActivity.KEY_server, "hayir"));
            }


        }


    }


    //**************************************************************************************

    public String getUrladres() {
        urladres = db.getAdres();
        return urladres;
    }

    private String getUrlLisans(){
        String lisans = null;
        lisans = db.getLisans();
        return  lisans;
    }

    private boolean isConnectedToServer(String url) {
        boolean result_server_connection = false;


        String lasturl ;
        lasturl = url+"cihazbaglantitest.php?KullaniciLisans="+getUrlLisans();

        Log.i("urlConnectionServer",lasturl);

        try {

            Document doc;
            doc = Jsoup.connect(lasturl).ignoreContentType(true).get();

            if (doc != null) {

                Elements links = doc.select("body");
                String geledeger = links.text().toString();
                Log.i("gelendeger", geledeger);
                if (links.text().equals("ok")) {
                    result_server_connection = true;
                } else {
                    result_server_connection = false;
                }
            } else {
                result_server_connection = false;
            }


        } catch (Exception e) {
            result_server_connection = false;
        }
        return result_server_connection;
    }



}
