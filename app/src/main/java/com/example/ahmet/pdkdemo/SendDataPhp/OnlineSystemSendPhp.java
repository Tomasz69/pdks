package com.example.ahmet.pdkdemo.SendDataPhp;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.ahmet.pdkdemo.BasicObject.Personel;
import com.example.ahmet.pdkdemo.BasicObject.Setting;
import com.example.ahmet.pdkdemo.BasicObject.TempPersonel;
import com.example.ahmet.pdkdemo.BasicObject.SendPersonel;
import com.example.ahmet.pdkdemo.Database.DatabaseHelper;
import com.example.ahmet.pdkdemo.MessgeWaitng;
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
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Ahmet on 11.12.2015.
 */
public class OnlineSystemSendPhp extends AsyncTask<TempPersonel,Void,Void> {


    private Context context;
    private SendPersonel sendPersonel;
    private TempPersonel tmpPersonel;
    private String urlOnline = "";
    private String urlImage;
    private String tip;
    private Handler mHndler;

    private static String lisans = null;
    private static String adres = null;
    private static String kod = null;

    private static String adreskalici = "mesaiclient.php";

    private static String enlemboylam ;
    private DatabaseHelper db;

    GpsService gps;

    public OnlineSystemSendPhp(Context c,String urlImage,Handler h,String tip){

        sendPersonel = new SendPersonel();
        tmpPersonel = new TempPersonel();
        gps = new GpsService(c);
        db = new DatabaseHelper (c);
        this.context = c;
        this.urlImage = urlImage;
        this.tip = tip;
        this.mHndler = h;
        getMacAdres();

    }


    private void getEnlemBoylam(){
        enlemboylam = String.valueOf(gps.getLatitude()) + "-" +String.valueOf(gps.getLongitude());
    }




    @Override
    protected Void doInBackground(TempPersonel... itemMasterPersonels) {

        getEnlemBoylam();
        ArrayList<Setting> settingArrayList = new ArrayList<>();
        settingArrayList = getSettingLisansAndAdres();

        for(int i = 0 ; i < settingArrayList.size() ; i++){
            adres = settingArrayList.get(i).getAdres();
            lisans = settingArrayList.get(i).getLisans();
            kod = settingArrayList.get(i).getKod();
        }

        urlOnline = adres + adreskalici ;

        DatabaseHelper db = new DatabaseHelper(context);
        sendPersonel = db.InnerSendDataOnline(itemMasterPersonels[0]);

        String zamanAltiresizstr = ZamanAltireKaldir(sendPersonel.getSEND_TARIH());


            try{
                String postReceiverUrl = urlOnline;
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(postReceiverUrl);
                File file = new File(urlImage);
                file.getAbsolutePath();
                FileBody fileBody = new FileBody(file);
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                reqEntity.addPart("Resim", fileBody);
                reqEntity.addPart("Tc", new StringBody(sendPersonel.getSEND_TCNO()));
                reqEntity.addPart("Zaman", new StringBody(zamanAltiresizstr));
                reqEntity.addPart("MesaiVardiya", new StringBody(sendPersonel.getSEND_MESAI()));
                reqEntity.addPart("CihazMacId",new StringBody(getMacAdres()));
                reqEntity.addPart("KullaniciLisans",new StringBody(lisans));
                reqEntity.addPart("Tipi",new StringBody(tip));
                reqEntity.addPart("Anahtar", new StringBody(kod));
                reqEntity.addPart("MesaiGps", new StringBody(enlemboylam));
                Log.i("KartNoPersonel",getKartNo(sendPersonel.getSEND_TCNO()));
                reqEntity.addPart("KartNo", new StringBody(getKartNo(sendPersonel.getSEND_TCNO())));

                httpPost.setEntity(reqEntity);
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity resEntity = response.getEntity();


                if (resEntity != null) {

                    String responseStr = EntityUtils.toString(resEntity).trim();
                    Log.i("SncuResponce",responseStr);

                    if(!responseStr.equals("#Kayit=OK,#Resim=OK")){ /// eger sunucuda sorun varsa kendi belleginine kaydet.

                        Log.i("SunucuHatasi", "Gonderme Basarisiz " + responseStr + adres + tip);

                        if(!urlImage.equals("")){
                            tmpPersonel.setDate(sendPersonel.getSEND_TARIH());
                            tmpPersonel.setXmesaiTipi(tip);
                            tmpPersonel.setTckimlikno(sendPersonel.getSEND_TCNO());
                            db.InsertImageTemporry(tmpPersonel,urlImage);
                        }
                    }
                    else{
                        Log.i("Sncu","basarili");
                    }
                }
                else{
                    Log.i("SuncurESPONCE","CEVAP_YOK");
                }
            }catch (Exception e){
        }





        return null;
    }

    private String getKartNo(String tc){

        String kartNo = null;
        kartNo =  db.getPersonelKart(tc);
        return kartNo;
    }

    private String ZamanAltireKaldir(String send_tarih) {

        String swap ;
        swap = send_tarih.replaceAll("_", " ");
        swap = swap.replace("-", ".");
        swap = swap.substring(0,swap.length()-4);


        return swap;
    }


    private String getMacAdres(){
        String adres = null;

        try{
            WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            adres = info.getMacAddress();

            Log.i("MACadres:",adres);
        }catch (Exception E){
            Log.e("MacAdresErorr:", E.getMessage().toString());
        }
        return adres;
    }

    /**
     * Burasi sql den cekilir
     * @return
     */
    public String getSeriNumber() {

        //String seriNumnber = "a9a081b8816c1cb65086e4fe3fceb6454b51b170";

        String seriNumnber = null;

        DatabaseHelper db = new DatabaseHelper(context);
        seriNumnber = db.getSettingElements();
        Log.i("seriNumber",seriNumnber);



            return seriNumnber;
    }

    public ArrayList<Setting> getSettingLisansAndAdres() {
        DatabaseHelper db = new DatabaseHelper(context);
        ArrayList<Setting> list = new ArrayList<Setting>();
        list = db.getElementSettingLisansAndAdres();
        return list;
    }



}
