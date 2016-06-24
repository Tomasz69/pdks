package com.example.ahmet.pdkdemo.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ahmet.pdkdemo.Database.DatabaseHelper;
import com.example.ahmet.pdkdemo.MainActivity;
import com.example.ahmet.pdkdemo.R;
import com.example.ahmet.pdkdemo.Settings.OtherSetting;

/**
 * Created by Ahmet on 09.04.2016.
 */
@SuppressLint("ValidFragment")
public class OthersFragmnet extends Fragment {

    private EditText tumuguncelleedit;
    private EditText tumusiledit;
    private EditText appkapatedit;

    private Button fabrikabtn;
    private Button kaydetbtn;

    private  Context c;

    private DatabaseHelper db;

    @SuppressLint("ValidFragment")
    public OthersFragmnet (Context c){
        this.c = c;
        db = new DatabaseHelper(c);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.otherlayoutfragmnet, container, false);
        return rootView;

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tumuguncelleedit = (EditText)view.findViewById(R.id.tumunuguncelleid);
        tumusiledit = (EditText) view.findViewById(R.id.tumunusilid);
        appkapatedit = (EditText) view.findViewById(R.id.appkapatedit);

        tumuguncelleedit.setText(db.getSettingTumunuupdate());
        tumusiledit.setText(db.getSettingTumunusil());
        appkapatedit.setText(db.getSettingAppkapa());

    }


}
