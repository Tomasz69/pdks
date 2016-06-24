package com.example.ahmet.pdkdemo.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmet.pdkdemo.Database.DatabaseHelper;
import com.example.ahmet.pdkdemo.R;

/**
 * Created by Ahmet on 09.04.2016.
 */
@SuppressLint("ValidFragment")
public class TextInfoFragmnet extends Fragment {

    private DatabaseHelper db ;
    private TextView macadrestxt,iciptxt,personelsayisi,gecicipersonel;

    @SuppressLint("ValidFragment")
    public TextInfoFragmnet(Context c){
        db = new DatabaseHelper(c);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.textlayoutfragment, container, false);
        return rootView;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        macadrestxt = (TextView) view.findViewById(R.id.mactxt);
        iciptxt = (TextView) view .findViewById(R.id.ipicctxt);
        personelsayisi = (TextView) view.findViewById(R.id.personelid);
        gecicipersonel = (TextView) view.findViewById(R.id.gecicipersonel);

        personelsayisi.setText("KAYITLI PERSONEL :"+ db.getCountTable());
        macadrestxt.setText("MAC ADRES : "+GetMacAdress());
        iciptxt.setText("DIS IP : "+ setExternalIp());
        gecicipersonel.setText("MESAI SAYISI : " +getTempCountPersonel() );
    }

    private String getTempCountPersonel() {
        String count = null;
        count = String.valueOf(db.getCountTemprry());
        return count;
    }


    private String GetMacAdress() {

        String adres = null;

        try {
            WifiManager manager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            adres = info.getMacAddress();

            Log.i("MACadres:", adres);
        } catch (Exception E) {
            Log.e("MacAdresErorr:", E.getMessage().toString());
        }
        return adres;
    }


    /**
     * dis IP degerini alir.
     */
    private String setExternalIp() {
        String icip;
        WifiManager wm = (WifiManager) getActivity().getSystemService(getActivity().WIFI_SERVICE);
        icip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        Log.i("IP_EXTRENAL", icip);

        return icip;
    }
}
