package com.example.ahmet.pdkdemo.SendDataPhp;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.ahmet.pdkdemo.BasicObject.SendPersonel;
import com.example.ahmet.pdkdemo.BasicObject.Setting;
import com.example.ahmet.pdkdemo.Database.DatabaseHelper;
import com.example.ahmet.pdkdemo.MessgeWaitng;

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
 * Created by Ahmet on 15.12.2015.
 */
public class deneme extends AsyncTask<Void,Void, Void> {

    private String urlOnline = "http://xmlpanel.com/panel/server/";

    private String tc;
    private String resimyolu;
    private String zaman;
    private String mesaiaralik;
    private String tip;
    private String id;
    private Context context;
    private DatabaseHelper db;
    private static String responseStr = null;

    private static  String lisans = null;
    private static  String  adres = null;

    public deneme(SendPersonel snd,Context c){

        this.tc = snd.getSEND_TCNO();
        this.resimyolu = snd.getSEND_URL();
        this.zaman = snd.getSEND_TARIH();
        this.mesaiaralik = snd.getSEND_MESAI();
        this.tip = snd.getSEND_MESAI_tip();
        this.id = snd.getSEND_ID();
        this.context = c ;

         db = new DatabaseHelper(context);
    }

    @Override
    protected Void doInBackground(Void... params) {


        if(!id.equals("")){

            try{

                ArrayList<Setting> settingArrayList = new ArrayList<>();
                settingArrayList = getSettingLisansAndAdres();

                for(int i = 0 ; i < settingArrayList.size() ; i++)
                {
                    adres = settingArrayList.get(i).getAdres();
                    lisans = settingArrayList.get(i).getLisans();
                }

                adres = urlOnline + adres;
                Log.i("Sonuclar",lisans+" "+adres);


                String postReceiverUrl = adres;
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(postReceiverUrl);
                File file = new File(resimyolu);
                file.getAbsolutePath();
                FileBody fileBody = new FileBody(file);
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                reqEntity.addPart("Resim", fileBody);
                reqEntity.addPart("Tc", new StringBody(tc));
                reqEntity.addPart("Zaman", new StringBody(zaman));
                reqEntity.addPart("MesaiAralik", new StringBody(mesaiaralik));
                reqEntity.addPart("CihazMacId",new StringBody(getMacAdres()));
                reqEntity.addPart("KullaniciLisans",new StringBody(getSeriNumber()));
                reqEntity.addPart("Tipi",new StringBody(tip));

                httpPost.setEntity(reqEntity);
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity resEntity = response.getEntity();


                if (resEntity != null) {

                    responseStr = EntityUtils.toString(resEntity).trim();
                    Log.i("InfoServerSend", responseStr);


                    if (!responseStr.equals("#Kayit=OK,#Resim=OK")) {
                        Log.i("dd", "Gonderme Basarisiz");
                    }
                    else if (responseStr.equals("#Kayit=OK,#Resim=OK")) {

                        Log.i("TempPersonelSendServer",id);
                        db.deleteSelectPersonel(id);
                    }

                }




            }
            catch (Exception e){
                Log.e("DenemePostErorr", e.getMessage().toString());
            }
        }




        return null;
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

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);


    }


    /**
     * lisans ve adres bilgileri getirlildi
     * @return
     */
    public ArrayList<Setting> getSettingLisansAndAdres(){
        DatabaseHelper db = new DatabaseHelper(context);
        ArrayList <Setting> list = new ArrayList<Setting>();
        list = db.getElementSettingLisansAndAdres();
        return  list;

    }

    /**
     * Burasi sql den cekilir
     * @return
     */
    public String getSeriNumber() {

        //String seriNumnber = "a9a081b8816c1cb65086e4fe3fceb6454b51b170";

        String seriNumnber = null;


        seriNumnber = db.getSettingElements();
        Log.i("seriNumber",seriNumnber);



        return seriNumnber;
    }



}
