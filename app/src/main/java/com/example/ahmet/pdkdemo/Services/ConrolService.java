package com.example.ahmet.pdkdemo.Services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.ahmet.pdkdemo.BasicObject.SendPersonel;
import com.example.ahmet.pdkdemo.Database.DatabaseHelper;
import com.example.ahmet.pdkdemo.HandleXML;
import com.example.ahmet.pdkdemo.BasicObject.TempPersonel;
import com.example.ahmet.pdkdemo.SendDataPhp.OfflineSytemSendDataPHP;
import com.example.ahmet.pdkdemo.SendDataPhp.deneme;
import com.example.ahmet.pdkdemo.SendDataPhp.offlinesystem;
import com.example.ahmet.pdkdemo.Settings.WifiSetting;


import java.util.ArrayList;
/**
 * Created by Ahmet on 06.12.2015.
 */
public class ConrolService extends IntentService {


    public static final String FILTER_CONTROL1_SERVER = "filter1";
    public static final String KEY_server_CONTROL = "anahtar1";
    DatabaseHelper dbhelper;
    private SendPersonel sndper;
    ArrayList<SendPersonel> gonderliste;
    HandleXML obj;
    private String finalUrl = "xmlpersonel.php";


    public ConrolService() {
        super(ConrolService.class.getName());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        dbhelper = new DatabaseHelper(this);
        gonderliste = new ArrayList<SendPersonel>();
        sndper = new SendPersonel();




    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String url;

        url = dbhelper.getAdres()+finalUrl;
        obj = new HandleXML(url,this,0); // 0 servis eger parse surekli olunca
        obj.fetchXML();



        gonderliste = dbhelper.InsertSendTable();


        if (dbhelper.getCountTemprry() != 0 && dbhelper.getCountTable() != 0) {

            new offlinesystem(this).execute(); // daha gonderilememis datalari gonderiyor

        }

        if (dbhelper.getCountTemprry() == 0) {
            Log.i("percount", "prsonelveyatable " + String.valueOf(dbhelper.getCountTemprry()));
            gonderliste.clear();
        }


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("intentServis", "intent servis basladi....");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("IntentServis", "intent servis yok edildi");


        super.onDestroy();
    }
}
