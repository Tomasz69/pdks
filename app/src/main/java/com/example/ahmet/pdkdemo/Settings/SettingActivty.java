package com.example.ahmet.pdkdemo.Settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.ahmet.pdkdemo.BasicObject.HadnleObj;
import com.example.ahmet.pdkdemo.Database.DatabaseHelper;
import com.example.ahmet.pdkdemo.DeviceListActivity;
import com.example.ahmet.pdkdemo.Fragments.OthersFragmnet;
import com.example.ahmet.pdkdemo.Fragments.SettingFragment;
import com.example.ahmet.pdkdemo.Fragments.TextInfoFragmnet;
import com.example.ahmet.pdkdemo.HandleXML;
import com.example.ahmet.pdkdemo.MainActivity;
import com.example.ahmet.pdkdemo.R;
import com.example.ahmet.pdkdemo.Services.BluetoothChatService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by Ahmet on 11.04.2016.
 */
public class SettingActivty extends FragmentActivity implements View.OnClickListener {

    FragmentManager manager;
    Fragment fragment = null;

    Button btnsetting,btninfo,btnother; // fragmnetlere gecis butonlari
    android.support.v4.app.FragmentTransaction transaction; // fragmnet gecisi nesnesi

    RelativeLayout anapanelid; // ana main layout
    Button btnkaydet,btntest,btnsifila,btncikis; // kayit butonu


    // 1 rakamsal 2 ayarlar 3 diger
    private static int HangiFragmnet;


    /// edittext viewler degiskenleri (SETTING VIEW)
    private EditText SunucuAdresEdittext;
    private EditText LisansEdittext;
    private EditText KisayolAdresEdittext;
    private EditText EnEdittext;
    private EditText BoyEdittext;
    private EditText AnahtarEdittext;

    /// buton degiskenleri
    private ToggleButton KioskBtn;
    private RadioButton OnKameraBtn;
    private RadioButton ArkaKameraBtn;
    private Button Btnkaydet;
    private Button BtnGuncelle;
    private Button BtnKapat;
    private RadioGroup radioGroup;
    private Button btnBluetooth;

    private static  String snucu,lisans,kisayol,en,boy,anahtar,kiosk,onKamera;
    DatabaseHelper db;

    ///  shreadpref TAG //////
    private static final String TAG_MAIN_PREFF = "ana";
    private static final String TAG_KISAYOL = "kisayol";
    public  static final String TAG_kiosk = "kiosk";
    private static String kioskmode = "false";
    private static final String TAG_KAMERA_STATE = "kamera";


    // Presfrens elements ////
    private SharedPreferences mSharedPrefs;
    private SharedPreferences.Editor mPrefsEditor;


    // other view leri
    private EditText tumuguncelleedit;
    private EditText tumusiledit;
    private EditText AppKapatedittext;

    private static String  tumunusil;
    private static String  tumunuguncelle;
    private static String  appkapa;

    private static Handler myhandler;


    private BluetoothAdapter mBluetoothAdapter = null;

    // Intent cevap kodlari
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;


    private HadnleObj mHandler;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ekran full screen yapildi.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.multifragmentsettinglayout); // anamain layout nesnesi olusturukdu

        db =  new DatabaseHelper(this);
        // yerel Bluetooth adaptoru alin
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        hideSoftKeyboard();

        btnsetting = (Button)findViewById(R.id.settingbtn);
        btnother = (Button)findViewById(R.id.otherbtn);
        btninfo = (Button)findViewById(R.id.textbtn);
        btncikis = (Button) findViewById(R.id.btncikis);
        btnsifila = (Button) findViewById(R.id.btnsifirla);
        btntest = (Button) findViewById(R.id.btntest);
        BtnGuncelle = (Button) findViewById(R.id.btnguncelle);
        btnkaydet = (Button) findViewById(R.id.buttonid);
        BtnKapat = (Button) findViewById(R.id.btnkapat);
        btnBluetooth = (Button) findViewById(R.id.btnBluettoh);

        anapanelid = (RelativeLayout) findViewById(R.id.anapanelid);

        btnsetting.setOnClickListener(this);
        btninfo.setOnClickListener(this);
        btnother.setOnClickListener(this);
        btnkaydet.setOnClickListener(this);
        btntest.setOnClickListener(this);
        btncikis.setOnClickListener(this);
        btnsifila.setOnClickListener(this);
        BtnGuncelle.setOnClickListener(this);
        BtnKapat.setOnClickListener(this);
        btnBluetooth.setOnClickListener(this);

        mSharedPrefs = getSharedPreferences(TAG_MAIN_PREFF, MODE_PRIVATE);
        mPrefsEditor = mSharedPrefs.edit();

        btntest.setEnabled(false);
        btntest.setBackgroundResource(R.drawable.teston);
        btnkaydet.setEnabled(false);
        btnkaydet.setBackgroundResource(R.drawable.kaydeton);
        btnsifila.setEnabled(true);
        btnsifila.setBackgroundResource(R.drawable.sifirlaarka);
        btncikis.setEnabled(true);
        btncikis.setBackgroundResource(R.drawable.cikisarka);
        BtnGuncelle.setEnabled(true);
        BtnGuncelle.setBackgroundResource(R.drawable.guncellearka);
        BtnKapat.setEnabled(true);
        BtnKapat.setBackgroundResource(R.drawable.kapatarka);



        // default olraak birinci nesne secildi
        if(fragment == null){

            fragment = new TextInfoFragmnet(this);
            FragmentYerlestir();
            HangiFragmnet = 1;
            btninfo.setBackgroundResource(R.drawable.durumon);

        }

        // eger ekrana klavyesi ailmissa ekrana tiklanmasi ile kapanir
        anapanelid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });


    }

    /**
     * Secilen fragmnet frame layouta yerlestirlir
     */
    private void FragmentYerlestir() {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_main, fragment);
        transaction.commit();
    }

    private void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    @Override
    public void onClick(View v) {


        if(v.getId() == R.id.btnsifirla){

            try {
                Dialog();
            }
            catch (Exception e){
                Log.e("DioagExeption",e.getMessage().toString());
            }
        }
        else if(v.getId() == R.id.btncikis){
            AnaMainYonlendir();
            hideSoftKeyboard();

        }
        else if(v.getId() == R.id.btntest){
            SettingViewCreat();// setting view leri olusturulur.
            GetValueViews();
            new ServerKontrolAsctack(SunucuAdresEdittext.getText().toString(),LisansEdittext.getText().toString()).execute();
        }


       else if(v.getId() == R.id.settingbtn){
            fragment = new SettingFragment(this);
            btnsetting.setBackgroundResource(R.drawable.ayaron);
            btninfo.setBackgroundResource(R.drawable.durumarka);
            btnother.setBackgroundResource(R.drawable.digerarka);
            HangiFragmnet = 2;
            hideSoftKeyboard();
            btntest.setEnabled(true);
            btntest.setBackgroundResource(R.drawable.testarka);
            btnkaydet.setEnabled(true);
            btnkaydet.setBackgroundResource(R.drawable.kaydetarka);
            btnsifila.setEnabled(true);
            btnsifila.setBackgroundResource(R.drawable.sifirlaarka);
            btncikis.setEnabled(true);
            btncikis.setBackgroundResource(R.drawable.cikisarka);
            BtnGuncelle.setEnabled(true);
            BtnGuncelle.setBackgroundResource(R.drawable.guncellearka);
            BtnKapat.setEnabled(true);
            BtnKapat.setBackgroundResource(R.drawable.kapatarka);

        }
        else if(v.getId() == R.id.otherbtn){
            fragment = new OthersFragmnet(this);
            btninfo.setBackgroundResource(R.drawable.durumarka);
            btnsetting.setBackgroundResource(R.drawable.ayararka);
            btnother.setBackgroundResource(R.drawable.digeron);
            HangiFragmnet = 3;
            hideSoftKeyboard();
            btntest.setEnabled(false);
            btntest.setBackgroundResource(R.drawable.teston);
            btnkaydet.setEnabled(true);
            btnkaydet.setBackgroundResource(R.drawable.kaydetarka);
            btnsifila.setEnabled(true);
            btnsifila.setBackgroundResource(R.drawable.sifirlaarka);
            btncikis.setEnabled(true);
            btncikis.setBackgroundResource(R.drawable.cikisarka);
            BtnGuncelle.setEnabled(true);
            BtnGuncelle.setBackgroundResource(R.drawable.guncellearka);
            BtnKapat.setEnabled(true);
            BtnKapat.setBackgroundResource(R.drawable.kapatarka);

        }
        else if (v.getId() == R.id.textbtn){
            fragment = new TextInfoFragmnet(this);
            btninfo.setBackgroundResource(R.drawable.durumon);
            btnsetting.setBackgroundResource(R.drawable.ayararka);
            btnother.setBackgroundResource(R.drawable.digerarka);


            HangiFragmnet = 1;
            hideSoftKeyboard();
            btntest.setEnabled(false);
            btntest.setBackgroundResource(R.drawable.teston);
            btnkaydet.setEnabled(false);
            btnkaydet.setBackgroundResource(R.drawable.kaydeton);
            btnsifila.setEnabled(true);
            btnsifila.setBackgroundResource(R.drawable.sifirlaarka);
            btncikis.setEnabled(true);
            btncikis.setBackgroundResource(R.drawable.cikisarka);
            BtnGuncelle.setEnabled(true);
            BtnGuncelle.setBackgroundResource(R.drawable.guncellearka);
            BtnKapat.setEnabled(true);
            BtnKapat.setBackgroundResource(R.drawable.kapatarka);

        }

        else if (v.getId() == R.id.btnkapat){

            UygulamaKapat();

        }

        else if(v.getId() == R.id.btnguncelle){

            DataUpdate();
            AnaMainYonlendir();
            hideSoftKeyboard();
        }

        else if(v.getId() == R.id.btnBluettoh){

            if (!mBluetoothAdapter.isEnabled()) {
                Toast.makeText(getApplicationContext(), "Lutfen Bluetooth'u kapatmatmayiniz", Toast.LENGTH_LONG).show();
            } else {
                Intent serverIntent = new Intent(getApplicationContext(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
            }

        }

        else if (v.getId() == R.id.buttonid){

            // direk ana ekrana yonledirilir.
            if(HangiFragmnet == 1){
                AnaMainYonlendir();
                hideSoftKeyboard();
            }
            else if(HangiFragmnet == 2){ // Setting deki verileri al ve ekle veya guncelle
                SettingProsses();
                hideSoftKeyboard();
            }
            else if(HangiFragmnet == 3){ // Others deki verileri al veya guncelle
                try{
                    OthersProsses();
                    hideSoftKeyboard();
                }catch (Exception e){
                    Log.e("OthersSettingEx",e.getMessage().toString());
                }

            }
        }

        FragmentYerlestir();
    }

    private void UygulamaKapat() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }

    private void DataUpdate() {

        HandleXML obj;
        String finalUrl = "http://xmlpanel.com/panel/server/xmlpersonel.php";
        obj = new HandleXML(finalUrl, this, 1); // 1  oluinca birkez tum personelller ceklecek
        obj.fetchXML();
        Toast.makeText(getBaseContext(), "personeller yuklenmistir.", Toast.LENGTH_SHORT).show();
    }

    private void Dialog() throws Exception{
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Sifirla();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivty.this);
        builder.setMessage(R.string.sifirlamaktext).setPositiveButton("Evet", dialogClickListener)
                .setNegativeButton(R.string.hayirstring, dialogClickListener).show();
    }

    private void OthersProsses() throws Exception{
        OthersViewCreat();
        GetOthersViewCreat();
        InsertOtherDatabase();

    }

    private void InsertOtherDatabase() {
        int dbcount;

        String kisayol ;
        String appkapastring;
        dbcount = db.getcountSetting();


        if(db.getKisayol() != null){
            kisayol = db.getKisayol();
        }
        else{
            kisayol = "";
        }



        if(!tumunusil.equals(tumunuguncelle)){
            if (tumunusil.equals("")
                    || tumunuguncelle.equals("")
                    || appkapa.equals("")
                    || appkapa.equals(tumunusil)
                    || appkapa.equals(tumunuguncelle)
                    || (kisayol.equals(tumunuguncelle))
                    || (kisayol.equals(tumunusil))
                    || (kisayol.equals(appkapa))) {
                Toast.makeText(getApplicationContext(),R.string.bosalanmesaj, Toast.LENGTH_SHORT).show();
            }
            else {

                if(dbcount == 0){
                    db.InsertOther(tumunusil,tumunuguncelle,appkapa);
                }
                else if (dbcount == 1){
                    db.UpdateOther(tumunuguncelle,tumunusil,appkapa);
                }

                AnaMainYonlendir();
            }
        }
        else{
            Toast.makeText(getApplicationContext(),R.string.uyariayni, Toast.LENGTH_SHORT).show();

        }
    }

    private void GetOthersViewCreat() {
        tumunuguncelle = tumuguncelleedit.getText().toString();
        tumunusil = tumusiledit.getText().toString();
        appkapa = AppKapatedittext.getText().toString();

    }

    private void OthersViewCreat() {
        tumuguncelleedit = (EditText)findViewById(R.id.tumunuguncelleid);
        tumusiledit = (EditText) findViewById(R.id.tumunusilid);
        AppKapatedittext = (EditText) findViewById(R.id.appkapatedit);

    }

    private void Sifirla() {
        db.deleteSetting();
        db.deleteallPersonel();
        db.InsertSettingAll("http://xmlpanel.com/panel/server/", "000000000000000000000000000", "0001", "true", "000367", "000368", "000369", "500", "500", "1","000333");
        AnaMainYonlendir();
    }

    private void SettingProsses() {

        SettingViewCreat();// setting view leri olusturulur.
        GetValueViews();  // view value degerileri aldik
        InsertSettingDatabese(); // veri tabanina kayit


    }

    private void InsertSettingDatabese() {

        int dbcount;

        dbcount = db.getcountSetting();


        if (SunucuAdresEdittext.getText().toString().equals("") ||
                LisansEdittext.getText().toString().equals("") ||
                EnEdittext.getText().toString().equals("") ||
                BoyEdittext.getText().toString().equals("") ||
                KisayolAdresEdittext.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), R.string.bosalanmesaj, Toast.LENGTH_SHORT).show();
        }
        else{

            if(KisayolAdresEdittext.getText().toString().equals(db.getSettingAppkapa())||KisayolAdresEdittext.getText().toString().equals(db.getSettingTumunusil())|| KisayolAdresEdittext.getText().toString().toString().equals(db.getSettingTumunuupdate())){
                Toast.makeText(getApplicationContext(), R.string.uyariayni, Toast.LENGTH_SHORT).show();
            }

            else{

                if(dbcount == 0){

                    db.InsertSetting(SunucuAdresEdittext.getText().toString(),
                            "a9a081b8816c1cb65086e4fe3fceb6454b51b170",
                            EnEdittext.getText().toString(),
                            BoyEdittext.getText().toString(),
                            KisayolAdresEdittext.getText().toString(),
                            AnahtarEdittext.getText().toString(),
                            kiosk,
                            String.valueOf(onKamera));

                }
                else if (dbcount == 1){
                    db.UpdateSetting(SunucuAdresEdittext.getText().toString(),
                            "a9a081b8816c1cb65086e4fe3fceb6454b51b170",
                            EnEdittext.getText().toString(),
                            BoyEdittext.getText().toString(),
                            KisayolAdresEdittext.getText().toString(),
                            AnahtarEdittext.getText().toString(),
                            kiosk,
                            String.valueOf(onKamera));
                }

                mPrefsEditor.putString(TAG_KISAYOL, KisayolAdresEdittext.getText().toString());
                mPrefsEditor.putString(TAG_kiosk, kioskmode);
                mPrefsEditor.commit();

                AnaMainYonlendir();
            }
        }



    }

    private void GetValueViews() {


        snucu = SunucuAdresEdittext.getText().toString();
        lisans = LisansEdittext.getText().toString();
        kisayol = KisayolAdresEdittext .getText().toString();
        en = EnEdittext.getText().toString();
        boy = BoyEdittext.getText().toString();
        anahtar = AnahtarEdittext.getText().toString();

        if(KioskBtn.isChecked()){
            kiosk = "true";
        }
        else {
            kiosk = "false";
        }
        if(OnKameraBtn.isChecked()){
            onKamera = "0";
        }
        else {
            onKamera = "1";
        }

    }

    private void SettingViewCreat() {

        SunucuAdresEdittext = (EditText)  findViewById(R.id.editadres);
        LisansEdittext = (EditText) findViewById(R.id.editlisans);
        KisayolAdresEdittext = (EditText) findViewById(R.id.editkisayol);
        EnEdittext = (EditText) findViewById(R.id.editen);
        BoyEdittext = (EditText)  findViewById(R.id.editboy);
        AnahtarEdittext = (EditText)  findViewById(R.id.editkod);
        KioskBtn = (ToggleButton) findViewById(R.id.tglkioskbtn);
        OnKameraBtn = (RadioButton) findViewById(R.id.rdnon);
        ArkaKameraBtn = (RadioButton) findViewById(R.id.rdnarka);

    }

    private void AnaMainYonlendir() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }


    class ServerKontrolAsctack extends AsyncTask<Void,Void,Void> {

        private String  gelenSunucuDegeri = null ;
        private String  sunucadi;
        private String  sunucsonekadi;


        public ServerKontrolAsctack(String a,String b){
            this.sunucadi = a;
            this.sunucsonekadi = b;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(gelenSunucuDegeri != null){

                if(gelenSunucuDegeri.equals("ok")){
                    Toast.makeText(getApplicationContext(), R.string.baglantibasarili, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.baglantibasarisiz, Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), R.string.baglantibasarisiz, Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        protected Void doInBackground(Void... params) {
            KontrolServer();
            return null;
        }


        @SuppressLint("LongLogTag")
        private void KontrolServer() {
            String lasturl;
            lasturl = sunucadi + "cihazbaglantitest.php?KullaniciLisans="+sunucsonekadi;
            Log.i("SunucuTest",lasturl);
            try {
                Document doc;
                doc = Jsoup.connect(lasturl).ignoreContentType(true).get();
                if(doc != null){
                    Elements links = doc.select("body");
                    gelenSunucuDegeri = links.text().toString();
                }
                else{gelenSunucuDegeri = null;}
            } catch (Exception e) {
                 gelenSunucuDegeri = null;
            }

        }

    }

}
