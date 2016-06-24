package com.example.ahmet.pdkdemo.Settings;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.ahmet.pdkdemo.Fragments.OthersFragmnet;
import com.example.ahmet.pdkdemo.Fragments.SettingFragment;
import com.example.ahmet.pdkdemo.Fragments.TextInfoFragmnet;
import com.example.ahmet.pdkdemo.MainActivity;
import com.example.ahmet.pdkdemo.R;
import com.example.ahmet.pdkdemo.TabListener;

/**
 * Created by Ahmet on 09.04.2016.
 */
public class NewSetttingActivty extends FragmentActivity implements View.OnClickListener {

    FragmentManager manager;
    Fragment fragment = null;

    Button btnsetting,btninfo,btnother;
    android.support.v4.app.FragmentTransaction transaction;

    RelativeLayout anapanelid;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.multifragmentsettinglayout);

        btnsetting = (Button)findViewById(R.id.settingbtn);
        btnother = (Button)findViewById(R.id.otherbtn);
        btninfo = (Button)findViewById(R.id.textbtn);

        btn = (Button) findViewById(R.id.buttonid);

        anapanelid = (RelativeLayout) findViewById(R.id.anapanelid);

        btnsetting.setOnClickListener(this);
        btninfo.setOnClickListener(this);
        btnother.setOnClickListener(this);
        btn.setOnClickListener(this);

        if(fragment == null){
            fragment = new TextInfoFragmnet(this);
            FragmentYerlestir();
        }

        anapanelid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });


    }

    private void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.settingbtn){
            fragment = new SettingFragment(this);
        }
        else if(v.getId() == R.id.otherbtn){
            fragment = new OthersFragmnet(this);
        }
        else if (v.getId() == R.id.textbtn){
            fragment = new TextInfoFragmnet(this);
        }
        else if(v.getId() == R.id.buttonid){
            EditText e = (EditText) findViewById(R.id.editadres);
            RadioButton t = (RadioButton) findViewById(R.id.rdnon);
            String ef = null;
            if(t.isChecked()){
                ef = "evet";
            }

            Toast.makeText(getApplicationContext(),ef, Toast.LENGTH_SHORT).show();
        }

        FragmentYerlestir();

    }

    private void FragmentYerlestir(){

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_main, fragment);
        transaction.commit();
    }


    @Override
    public void onPause() {
        super.onPause();
        yonledir();
    }

    private void yonledir() {

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        yonledir();
    }
}
