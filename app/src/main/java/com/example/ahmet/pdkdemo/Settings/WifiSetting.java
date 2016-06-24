package com.example.ahmet.pdkdemo.Settings;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by Ahmet on 30.11.2015.
 */
public class WifiSetting {


    WifiManager wifi;
    Context context;

    /**
     * wifimanger sinifini wifiserivisi kullaniriz
     * @param context ana activityden context aliyoruz.
     */
    public WifiSetting(Context context){
         this.context = context;
         wifi= (WifiManager) context. getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * wifi ac
     * @return
     */
    public boolean open(){

        boolean result_open = false;

        try {
            wifi.setWifiEnabled(true);
            result_open = true;
        }catch (Exception e){
            Log.e("Erorr_wifi_open",e.getMessage());
            result_open = false;
        }
        return result_open;
    }


    public void WifiControl(){
        if(!wifi.isWifiEnabled()){
            wifi.setWifiEnabled(true);
        }
    }





    /**
     * wifi kapa
     * @return
     */
    public boolean close(){
        boolean result_close = false;

        try{
            wifi.setWifiEnabled(false);
            result_close = true;
        }catch (Exception e){
            Log.e("Error_wifi_close",e.getMessage());
        }

        return  result_close;
    }
}
