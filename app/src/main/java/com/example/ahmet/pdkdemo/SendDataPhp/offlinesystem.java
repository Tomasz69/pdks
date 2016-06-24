package com.example.ahmet.pdkdemo.SendDataPhp;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ahmet.pdkdemo.BasicObject.SendPersonel;
import com.example.ahmet.pdkdemo.BasicObject.Setting;
import com.example.ahmet.pdkdemo.Database.DatabaseHelper;
import com.example.ahmet.pdkdemo.Services.GpsService;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Ahmet on 24.03.2016.
 */
public class offlinesystem extends AsyncTask<Void, Void, Void> {


    private String urlOnline = "";

    private String tc;
    private String resimyolu;
    private String zaman;
    private String mesaiaralik;
    private String tip;
    private String idpersonel;
    private Context context;
    private DatabaseHelper db;
    private static String responseStr = null;

    private static String lisans = null;
    private static String adres = null;
    private static String kod = null;

    ArrayList<SendPersonel> gonderliste;

    private GpsService gps;
    private static String EnlemBoylam;


    public offlinesystem(Context c) {


        this.context = c;
        gonderliste = new ArrayList<SendPersonel>();
        db = new DatabaseHelper(context);
        gps = new GpsService(c);
    }

    private void getEnlemBoylam(){
        EnlemBoylam = String.valueOf(gps.getLatitude()) + "-" + String.valueOf(gps.getLongitude());
        Log.i("EnlemBoylam",EnlemBoylam);
    }

    @Override
    protected Void doInBackground(Void... params) {

            getEnlemBoylam();


            gonderliste = db.InsertSendTable();

            ArrayList<Setting> settingArrayList = new ArrayList<>();
            settingArrayList = getSettingLisansAndAdres();

            for (int i = 0; i < settingArrayList.size(); i++) {

            adres = settingArrayList.get(i).getAdres();
            lisans = settingArrayList.get(i).getLisans();
            kod = settingArrayList.get(i).getKod();
        }

        urlOnline =  adres + "mesaiclient.php";

        for (int i = 0; i < gonderliste.size(); i++) {

            getPersonelInfo(i);
            senddata();
            controlsend();

        }


        return null;
    }

    private void controlsend() {

        if (!responseStr.equals("#Kayit=OK,#Resim=OK")) {
            Log.i("dd", "Gonderme Basarisiz");
        } else if (responseStr.equals("#Kayit=OK,#Resim=OK")) {

            Log.i("TempPersonelSendServer", idpersonel);
            db.deleteSelectPersonel(idpersonel);
        }
    }
    private String ZamanAltireKaldir(String send_tarih) {

        String swap ;
        swap = send_tarih.replaceAll("_", " ");
        swap = swap.replace("-", ".");
        swap = swap.substring(0,swap.length()-4);


        return swap;
    }

    private String getKartNo(String tc){

        String kartNo = null;
        kartNo =  db.getPersonelKart(tc);
        return kartNo;
    }

    private void senddata() {
        try {

            String postReceiverUrl = urlOnline;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(postReceiverUrl);
            File file = new File(resimyolu);
            file.getAbsolutePath();
            FileBody fileBody = new FileBody(file);
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            reqEntity.addPart("Resim", fileBody);
            reqEntity.addPart("Tc", new StringBody(tc));
            reqEntity.addPart("Zaman", new StringBody( ZamanAltireKaldir(zaman)));
            reqEntity.addPart("MesaiVardiya", new StringBody(mesaiaralik));
            reqEntity.addPart("CihazMacId", new StringBody(getMacAdres()));
            reqEntity.addPart("KullaniciLisans", new StringBody(getSeriNumber()));
            reqEntity.addPart("Tipi", new StringBody(tip));
            reqEntity.addPart("Anahtar", new StringBody(kod));
            reqEntity.addPart("MesaiGps", new StringBody(EnlemBoylam));
            reqEntity.addPart("KartNo", new StringBody(getKartNo(tc)));



            httpPost.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {

                responseStr = EntityUtils.toString(resEntity).trim();
                Log.i("InfoServerSend", responseStr);

            }

        } catch (Exception e) {

        }


    }

    private String getMacAdres() {
        String adres = null;

        try {
            WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            adres = info.getMacAddress();

            Log.i("MACadres:", adres);
        } catch (Exception E) {
            Log.e("MacAdresErorr:", E.getMessage().toString());
        }
        return adres;
    }

    /**
     * Burasi sql den cekilir
     *
     * @return
     */
    public String getSeriNumber() {

        //String seriNumnber = "a9a081b8816c1cb65086e4fe3fceb6454b51b170";

        String seriNumnber = null;


        seriNumnber = db.getSettingElements();
        Log.i("seriNumber", seriNumnber);


        return seriNumnber;
    }


    private void getPersonelInfo(int i) {

        tc = gonderliste.get(i).getSEND_TCNO();
        resimyolu = gonderliste.get(i).getSEND_URL();
        zaman = gonderliste.get(i).getSEND_TARIH();

        mesaiaralik = gonderliste.get(i).getSEND_MESAI();
        tip = gonderliste.get(i).getSEND_MESAI_tip();
        idpersonel = gonderliste.get(i).getSEND_ID();

    }

    /**
     * lisans ve adres bilgileri getirlildi
     *
     * @return
     */
    public ArrayList<Setting> getSettingLisansAndAdres() {
        DatabaseHelper db = new DatabaseHelper(context);
        ArrayList<Setting> list = new ArrayList<Setting>();
        list = db.getElementSettingLisansAndAdres();
        return list;

    }


}
