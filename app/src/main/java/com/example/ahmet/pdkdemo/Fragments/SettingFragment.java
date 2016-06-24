package com.example.ahmet.pdkdemo.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.ahmet.pdkdemo.BasicObject.Setting;
import com.example.ahmet.pdkdemo.Database.DatabaseHelper;
import com.example.ahmet.pdkdemo.MainActivity;
import com.example.ahmet.pdkdemo.R;
import com.example.ahmet.pdkdemo.Settings.NewSetttingActivty;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.TimerTask;

/**
 * Created by Ahmet on 09.04.2016.
 */
@SuppressLint("ValidFragment")
public class SettingFragment extends Fragment{



    /// edittext viewler degiskenleri
    private EditText SunucuAdresEdittext;
    private EditText LisansEdittext;
    private EditText KisayolAdresEdittext;
    private EditText EnEdittext;
    private EditText BoyEdittext;
    private EditText AnahtarEdittext;

    /// buton degiskenleri
    private Button KontrolBtn;
    private ToggleButton KioskBtn;
    private RadioButton OnKameraBtn;
    private RadioButton ArkaKameraBtn;
    private Button Btnkaydet;
    private RadioGroup radioGroup;

    // kendi yaptigimiz database nesnesi getirliyor
    private DatabaseHelper db;


    // defalutl olarak gelen degiskenler
    private static String Kisayol = "150";
    private static String En = "500";
    private static String Boy = "500";
    private static String Lisans = "a9a081b8816c1cb65086e4fe3fceb6454b51b170";
    private static String Adres = "http://xmlpanel.com/panel/server/";
    private static String Kod = "100000";
    private static String kisayol = "150";

    private static int flag = 0;

    private Context c;


    /// kiosk mode elememtn ////

     private ToggleButton tglbtn;


    ///  shreadpref TAG //////
    private static final String TAG_MAIN_PREFF = "ana";
    private static final String TAG_KISAYOL = "kisayol";
    public  static final String TAG_kiosk = "kiosk";
    private static String kioskmode = "false";
    private static final String TAG_KAMERA_STATE = "kamera";


    // Presfrens elements ////
    private SharedPreferences mSharedPrefs;
    private SharedPreferences.Editor mPrefsEditor;





    @SuppressLint("ValidFragment")
    public SettingFragment(Context context){
        this.c = context;
        db = new DatabaseHelper(context);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.settinglayoutfragment, container, false);
         return rootView;
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        SunucuAdresEdittext = (EditText) view.findViewById(R.id.editadres);
        LisansEdittext = (EditText) view.findViewById(R.id.editlisans);
        KisayolAdresEdittext = (EditText) view.findViewById(R.id.editkisayol);
        EnEdittext = (EditText) view.findViewById(R.id.editen);
        BoyEdittext = (EditText) view.findViewById(R.id.editboy);
        AnahtarEdittext = (EditText) view.findViewById(R.id.editkod);


        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        KioskBtn = (ToggleButton) view.findViewById(R.id.tglkioskbtn);
        OnKameraBtn = (RadioButton) radioGroup.findViewById(R.id.rdnon);
        ArkaKameraBtn = (RadioButton) radioGroup.findViewById(R.id.rdnarka);
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(SunucuAdresEdittext, InputMethodManager.SHOW_IMPLICIT);
        mSharedPrefs = c.getSharedPreferences(TAG_MAIN_PREFF, c.MODE_PRIVATE);
        mPrefsEditor = mSharedPrefs.edit();


        if(db.getcountSetting() !=0){
            kisayol = db.getKisayol();

            if(db.getKameraState() != null)
            {
                flag = Integer.parseInt(db.getKameraState());
            }

            kioskmode = db.getKiosk();
        }
        else{
            kisayol = "150";
            flag = 0;
            kioskmode = "false";
        }

        if(flag == 0){
            OnKameraBtn.setChecked(true);
            ArkaKameraBtn.setChecked(false);
            flag = 0;
        }
        else if(flag == 1){
            ArkaKameraBtn.setChecked(true);
            OnKameraBtn.setChecked(false);
            flag = 1;
        }



        if (kioskmode != null) {
            if (kioskmode.equals("true")) {
                KioskBtn.setChecked(true);
                kioskmode = "true";
            } else if (kioskmode.equals("false")) {
                KioskBtn.setChecked(false);
                kioskmode = "false";
            }
        }

        KioskBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {kioskmode = "true";}
                else {kioskmode = "false";}
            }
        });


        DefaultStringSettingDoldur();
        AddEdittext();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rdnon){
                    flag = 0;
                }
                else if (checkedId == R.id.rdnarka){
                    flag = 1;
                }
            }
        });

    }




    private void AddEdittext() {

        SunucuAdresEdittext.setText(Adres);
        EnEdittext.setText(En);
        BoyEdittext.setText(Boy);
        LisansEdittext.setText(Lisans);
        AnahtarEdittext.setText(Kod);
        KisayolAdresEdittext.setText(kisayol);


    }

    private void DefaultStringSettingDoldur(){

        for(int i = 0 ;i< db.getElementSettings().size() ; i++){

            Adres = db.getElementSettings().get(i).getAdres();
            En = db.getElementSettings().get(i).getEn();
            Boy = db.getElementSettings().get(i).getBoy();
            Lisans = db.getElementSettings().get(i).getLisans();
            Kod = db.getElementSettings().get(i).getKod();

        }


    }

}
