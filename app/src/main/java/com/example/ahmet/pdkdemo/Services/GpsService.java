package com.example.ahmet.pdkdemo.Services;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

import com.example.ahmet.pdkdemo.R;

/**
 * Created by Ahmet on 18.04.2016.
 */
public class GpsService extends Service implements LocationListener {

    private final Context mContext;

    // gps varmi bayrak degeri
    boolean isGPSEnabled = false;

    // internet varmi bayrak degeri
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location; //  yer
    double latitude; // en
    double longitude; // boy

    // cihazin gps konumunu gucenllerken kac metre civarinda olmus
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 metre

    // kac saniyede bir guncelleme yapacak
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 sn


    protected LocationManager locationManager;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public GpsService(Context context) {
        this.mContext = context;
        getLocation();
    }


    /**
     * cihazin o anki konumu getirir.
     * @return
     */
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // gps durumunu getirir.
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // internet durumunu getirir
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // interneti desteklemiyor.
            } else {
                this.canGetLocation = true;

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                     if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // eger gps destegi varsa GPS serivisi baslatilir
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                         if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    /**
     * en degeri alinir
     * @return
     */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }


        return latitude;
    }

    /**
     * boylam degeri alinir
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }


        return longitude;
    }


    /**
     * gps ve internet durumunu tutan flag degerini gonderir
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS AYARLARI");

        // Setting Dialog Message
        alertDialog.setMessage(R.string.gpsuyarimesaj);

        // On pressing Settings button
        alertDialog.setPositiveButton("AYARLAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("iptal", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

}
