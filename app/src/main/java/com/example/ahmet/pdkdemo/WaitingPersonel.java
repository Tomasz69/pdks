package com.example.ahmet.pdkdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.ahmet.pdkdemo.Database.DatabaseHelper;


/**
 * Bu nesne asil amaci bekle
 * Main Actvy den gelen  duruma gore gosterecek mesaji belirler
 * ve sonra verilen kodu bellirli bi zaman da durdurur.
 * ve sonra sistem tekrar kaldigi yerden devam eder
 */


public class WaitingPersonel implements Runnable {
    private Handler handler; // Handler mesaj nesnesi
    private DatabaseHelper dbhelper; // Database nesnesi
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Main actvty den gelen duruma gore mesaj gosterme
     * @param c  : Ana contex
     * @param h : Mainactivty deki Handler nesnesi
     */
    public WaitingPersonel(Context c,Handler h ){
        this.handler = h;
        dbhelper = new DatabaseHelper(c);
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * gelen duruna gore islem gerceklesir sleep ile bekletilir ve devam edilir
     */
    @Override
    public void run() {
        try{
            Message msg1 = handler.obtainMessage(8); // Handler nesnesine durum 8 yollanir
            handler.sendMessage(msg1); // mesaj gonderilir
            Thread.sleep(7000); // sistem bekletilir
            Message msg2 = handler.obtainMessage(5); // Handler nesnesine durum 5 yollanir
            handler.sendMessage(msg2); // mesaj gonderilir
            dbhelper.deleteDepoPersonel(); // tuttulan en son kart silinir
            }
        catch (Exception e){
            Log.e("HandlerWaitngEror",e.getMessage().toString());
        }
    }
}
