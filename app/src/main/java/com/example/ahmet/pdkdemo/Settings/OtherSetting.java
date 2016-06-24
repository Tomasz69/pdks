package com.example.ahmet.pdkdemo.Settings;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.ahmet.pdkdemo.Database.DatabaseHelper;
import com.example.ahmet.pdkdemo.MainActivity;
import com.example.ahmet.pdkdemo.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Created by Ahmet on 17.02.2016.
 */
@SuppressWarnings("ResourceType")
public class OtherSetting extends Activity {


    public static final String TAG_kiosk = "kiosk";

    ///  shreadpref TAG //////
    private static final String TAG_MAIN_PREFF = "ana";
    private static final String TAG_KISAYOL = "kisayol";

    // Presfrens elements ////
    private SharedPreferences mSharedPrefs;
    private SharedPreferences.Editor mPrefsEditor;


    private static String kisayol = "150";
    private static String en = "500";
    private static String boy = "500";
    private static String lisan = "a9a081b8816c1cb65086e4fe3fceb6454b51b170";
    private static String adres = "http://xmlpanel.com/panel/server/";
    private static String device_ip = "";
    private static String kod = "100000";
    private static String personelsayisistr = "0";

    public static final String MyPREFERENCES = "MyPrefs";
    private EditText adresedit, lisansedit, enedit, boyedit, kisayoledit, editkod;
    private Button btndzn;
    private DatabaseHelper db;

    private TextView ipic;

    private RelativeLayout settingmainlayout;

    private static  boolean flag = false;




    /// kiosk mode elememtn ////

    private static String kioskmode = "false";
    private ToggleButton tglbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.setting);

        db = new DatabaseHelper(this);

        btndzn = (Button) findViewById(R.id.buttondzn);
        adresedit = (EditText) findViewById(R.id.editadres);
        lisansedit = (EditText) findViewById(R.id.editlisans);
        enedit = (EditText) findViewById(R.id.editen);
        boyedit = (EditText) findViewById(R.id.editboy);
        kisayoledit = (EditText) findViewById(R.id.editkisayol);
        tglbtn = (ToggleButton) findViewById(R.id.tglkioskbtn);
        settingmainlayout = (RelativeLayout) findViewById(R.id.settingmainlayout);
        editkod = (EditText) findViewById(R.id.editkod);

        TextView mctxt = (TextView) findViewById(R.id.macadresID);
        mctxt.setText(getMacAdres());

        TextView ipdis = (TextView) findViewById(R.id.ipid);
        ipdis.setText(setExternalIp());

        TextView kisi = (TextView) findViewById(R.id.kisisayisi);
        kisi.setText(getTempCountPersonel());


        TextView personelsaiyis = (TextView)findViewById(R.id.textView14);

        if(!String.valueOf(db.getCountTable()).equals("0")){
            personelsayisistr = String.valueOf(db.getCountTable());
        }

        personelsaiyis.setText(personelsayisistr);

        ipic = (TextView) findViewById(R.id.icipid);

        adresedit.setFocusableInTouchMode(true);

        mSharedPrefs = getSharedPreferences(TAG_MAIN_PREFF, MODE_PRIVATE);
        mPrefsEditor = mSharedPrefs.edit();


        String kisokState = mSharedPrefs.getString(TAG_kiosk, null);

        Log.i("kioskMode", kioskmode);

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(adresedit, InputMethodManager.SHOW_IMPLICIT);


        for (int i = 0; i < db.getElementSettings().size(); i++) {

            adres = db.getElementSettings().get(i).getAdres();
            en = db.getElementSettings().get(i).getEn();
            boy = db.getElementSettings().get(i).getBoy();
            lisan = db.getElementSettings().get(i).getLisans();
            kod = db.getElementSettings().get(i).getKod();

        }

        if (mSharedPrefs.getString(TAG_KISAYOL, null) != null) {
            kisayol = mSharedPrefs.getString(TAG_KISAYOL, null);
        }

        adresedit.setText(adres);
        kisayoledit.setText(kisayol);
        enedit.setText(en);
        boyedit.setText(boy);
        lisansedit.setText(lisan);
        editkod.setText(kod);

        if (kisokState != null) {
            if (kisokState.equals("true")) {
                tglbtn.setChecked(true);
                kioskmode = "true";
            } else if (kisokState.equals("false")) {
                tglbtn.setChecked(false);
                kioskmode = "false";
            }
        }

        tglbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    kioskmode = "true";

                } else {

                    kioskmode = "false";
                }
            }
        });

        btndzn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                if (adresedit.getText().toString().equals("") ||
                        lisansedit.getText().toString().equals("") ||
                        enedit.getText().toString().equals("") ||
                        boyedit.getText().toString().equals("") ||
                        kisayoledit.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), "bos alan birakmayiniz", Toast.LENGTH_SHORT).show();
                } else {


                    db.deleteSetting();


                    /*  db.InsertSetting(adresedit.getText().toString(),
                            "a9a081b8816c1cb65086e4fe3fceb6454b51b170",
                            enedit.getText().toString(),
                            boyedit.getText().toString(),
                            kisayoledit.getText().toString(),
                            editkod.getText().toString());
                    db.countSettingelement();
                    */
                    mPrefsEditor.putString(TAG_KISAYOL, kisayoledit.getText().toString());
                    mPrefsEditor.putString(TAG_kiosk, kioskmode);

                    mPrefsEditor.commit();


                    Toast.makeText(getBaseContext(), "ekleme islemi basarili ", Toast.LENGTH_SHORT).show();


                    yonledir();


                }


            }
        });


        settingmainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });


    }


    private String getTempCountPersonel() {
        String count = null;
        count = String.valueOf(db.getCountTemprry());
        return count;
    }


    @Override
    protected void onPause() {
        super.onPause();
        yonledir();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        yonledir();
    }


    private String getMacAdres() {
        String adres = null;

        try {
            WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            adres = info.getMacAddress();

            Log.i("MACadres:", adres);
        } catch (Exception E) {
            Log.e("MacAdresErorr:", E.getMessage().toString());
        }
        return adres;
    }

    private void yonledir() {
        Intent i = new Intent(OtherSetting.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    /**
     * dis IP degerini alir.
     */
    private String setExternalIp() {
        String icip;
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        icip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        Log.i("IP_EXTRENAL", icip);

        return icip;
    }


    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(adresedit, InputMethodManager.SHOW_IMPLICIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }






}
