package com.example.ahmet.pdkdemo.SendDataPhp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ahmet.pdkdemo.BasicObject.SendPersonel;
import com.example.ahmet.pdkdemo.Database.DatabaseHelper;
import com.oschrenk.io.Base64;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ahmet on 10.12.2015.
 */
public class OfflineSytemSendDataPHP extends AsyncTask <ArrayList<SendPersonel> ,Void, ArrayList<SendPersonel>> {

    ArrayList<SendPersonel> itemList;
    String url = "http://xmlpanel.com/panel/server/mesaiclient.php";
    Context context;
    private String seriNumber;

    public OfflineSytemSendDataPHP(Context c){
        this.context=c;
    }

    @Override
    protected ArrayList<SendPersonel> doInBackground(ArrayList<SendPersonel>... arrayLists) {

        itemList = arrayLists[0];



        for(int i = 0 ;i<itemList.size();i++){



            String imageName = itemList.get(i).getSEND_URL();
            Log.i("resimyolu",imageName);
            try{

                String postReceiverUrl = url;
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(postReceiverUrl);
                File file = new File(imageName);
                file.getAbsolutePath();
                FileBody fileBody = new FileBody(file);
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                reqEntity.addPart("Resim", fileBody);
                reqEntity.addPart("Tc", new StringBody(itemList.get(i).getSEND_TCNO()));
                reqEntity.addPart("Zaman", new StringBody(itemList.get(i).getSEND_TARIH()));
                reqEntity.addPart("MesaiAralik", new StringBody(itemList.get(i).getSEND_MESAI()));
                reqEntity.addPart("Tipi", new StringBody(itemList.get(i).getSEND_MESAI_tip()));
                reqEntity.addPart("CihazMacId",  new StringBody(getMacAdres()));
                reqEntity.addPart("KullaniciLisans",  new StringBody(getSeriNumber()));


                httpPost.setEntity(reqEntity);
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity resEntity = response.getEntity();

                if (resEntity != null) {

                    String responseStr = EntityUtils.toString(resEntity).trim();

                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
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

    /**
     * Burasi sql den cekilir
     * @return
     */
    public String getSeriNumber() {

        //String seriNumnber = "a9a081b8816c1cb65086e4fe3fceb6454b51b170";


        String seriNumnber = null;

        DatabaseHelper db = new DatabaseHelper(context);
        seriNumnber = db.getSettingElements();

        Log.i("getSERNO",seriNumnber);

        return seriNumnber;
    }
}
