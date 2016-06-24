package com.example.ahmet.pdkdemo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;

import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmet.pdkdemo.BasicObject.HadnleObj;
import com.example.ahmet.pdkdemo.BasicObject.TempPersonel;
import com.example.ahmet.pdkdemo.BasicObject.Personel;
import com.example.ahmet.pdkdemo.Database.DatabaseHelper;
import com.example.ahmet.pdkdemo.SendDataPhp.OnlineSystemSendPhp;
import com.example.ahmet.pdkdemo.Services.AutoOpenBroadcast;
import com.example.ahmet.pdkdemo.Services.BatteryReciver;
import com.example.ahmet.pdkdemo.Services.BluetoothChatService;
import com.example.ahmet.pdkdemo.Services.ConrolService;
import com.example.ahmet.pdkdemo.Services.GpsService;
import com.example.ahmet.pdkdemo.Services.NetworkControlService;
import com.example.ahmet.pdkdemo.Services.ServerControlService;

import com.example.ahmet.pdkdemo.Settings.SettingActivty;
import com.example.ahmet.pdkdemo.Settings.WifiSetting;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class MainActivity extends FragmentActivity implements OnClickListener {

    IntentFilter s_intentFilter; // olusturulan intente ozel bir gorev verilmek icin tanimlanmis nesne
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String FILTER = "just.a.filter"; //  interne service fillitresi
    public static final String FILTER2 = "just.b.filter"; // sunucu service
    public static final String KEY_networking = "key"; // internet servicenin hangi sinifta cagirlacagini berlirleyen anahtar
    public static final String KEY_server = "key2";  //  sunuc serivicesinin hangi sinifta cagiralacagini belirleyen anahtar
    public static String gelen_cevap_server = null; // sunucu serivsinden gelen cevap
    public static String gelen_cevap_network = null;  // internet servicsinden gelen cevap
    private LinearLayout mLayout; // rakamlari tanimladigimiz layout
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int countview = -1; // manuel giris stringin uzunlugu
    private static boolean kartMi = false; // kart ile mi yoksa tc ile mi
    private TempPersonel personel; // gecici bellge kayit edilecek personel nesnesi
    private Personel p = new Personel(); // gonderilecek personel nesnesi
    private DatabaseHelper dbhelper; // Veritabani islemlerini gerceklestirecek nesne
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0; // manuel giris butonlarimiz
    private Button btnok, btnsil;
    private Kamera kamera; // Kamera ile islemleri yapacagimiz bizim yazdigimiz nesne
    private TempPersonel pp; //
    private String urlImageString; // resim dosya yolu
    protected PowerManager.WakeLock mWakeLock; // uygulamanin gununu tasaruflu yapan nesne
    Camera mCamera; // cihazin kamera nesnesi
    private static int KameraState; // on mu yoksa arka kamera mi onu tanimlayan bayrak deger.
    SurfaceView mPreview; // kamera ekranin  canli cekim yapabilmesi icin kullanilacak nesne
    // Presfrens elements ////
    private SharedPreferences mSharedPrefs; // SettingAcitvy den gelen database kullanmaya gerek olmayan nesneleri tanimladigmiz nesne
    private static boolean isInOut; // giris mi cikismi yapmis islem yapan kisi
    ///  shreadpref TAG //////
    private static final String TAG_MAIN_PREFF = "ana"; // gelen sheredpref nesnesinin filitresi
    private EditText edit; // eger bluetooth ile degilde klavye okuyucu ile baglanti saglanirsa gelen deger icine atilacagi edittex
    // asagidaki uc nesne kameranin onune acililacak bilgilerdirme viewleridir
    private TextView infotxt, infoclck, kartnottxt, infoisimtxtt;
    private RelativeLayout infolayout;
    private ImageView infoservermessage;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    String isimpersonel; // personel sordan sonra tutlacak ismi
    private static String kartNo; // kart numarasi
    private static String kisayolTxt = "150"; // defalut olarak ayarlar menusunu eger degisiklik yapilrsa bir kereligine  girilecek deger
    private static String tarih; // islem basildiginda tutlacak zamani tutar
    private BatteryReciver mReceiver; // batarya durumunu kontrol eden nesne

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7
    private static String tip; // islem yapan kisinin giris tipini yani girismi yoksa cikismi yapmis onu tutan nesne
    private static String tcno = ""; // islem yapan kisinin tcnosunu tutar
    private static String karttcno = ""; // islem yapan kisinin eger karli giris yaparsa tc nosunu tutar
    private static String newtc = ""; // islem yapan kisi kartli yada manuel giris yaptiktan sonra son olarak kullanmak uzere tanimlanir tc no icin
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //// Kiosk modu elemanlari /////
    private static String kioskmodeState = "false"; // default olarak kisok false durumda baslar
    private PendingIntent intent; // kisok mode aktif ise kise bootreciver icin alarm olusturur.
    private static boolean isMain = true; // ana paneldemi yoksa ayarlar panelindemisin onu anlamizi saglamak icin tanimladi
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private MediaPlayer mPlayerWorrong, mPlayerOK; // personel islem yaptiktan sonra anlamasi icin zil sesi tanimlanir
    FrameLayout preview; // kameranin gosterilecegi layout
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static String tmnDeleteStr;  // kisayol olarak gelen silme kisayolunun durumunu tutar
    private static String tmnUpdateStr; // kisayol olarak gelen guncelleme kisayolunun durumunu tutar.
    private static String appkapa;      // kisayol olarak gelen uygulama kapatma kisayolunu durumunu tutar.
    private BluetoothAdapter mBluetoothAdapter = null; // bluetooth durumunu getiren adaptor
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Intent cevap kodlari
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1; // baglantiyi guvenli moda yapmamiz icin tanimlariz
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2; // baglantiyi gevenli olmayan modda yapariz
    private static final int REQUEST_ENABLE_BT = 3; // eger bluetooth aciksa bu deger  tanimlanir.
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static BluetoothChatService mChatService = null; // bluetooh baglanti nesnesi
    private StringBuffer mOutStringBuffer; // gelen kart degernin buffer nesnesinde getiririz
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    GpsService gps; // GPS Servisizmiz





    @SuppressWarnings("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // kiosk mode acik kalacak activity nin degerini tutar.
        final int flags = getIntent().getFlags();

        super.onCreate(savedInstanceState);

        // uygulama yi full screen yapan kisim
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //responce  icin ekran genisligine gore layout secer
        setContentView(R.layout.deneme);



        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // ekranin acik kalmasi


        registerReceiver(mBatteryregister, new IntentFilter( // degisen durumu kontrol eder.
                Intent.ACTION_BATTERY_CHANGED));

        gps = new GpsService(this); //  gps servisi baslattik ve gps durumunu kontrol ediyoruz
        if(!gps.canGetLocation()){
            gps.showSettingsAlert();
        }
        // yerel Bluetooth adaptoru alin
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        personel = new TempPersonel(); // personel nesnesi olusturuldu
        dbhelper = new DatabaseHelper(this); // database nesnesi olusturuldu
        if (dbhelper.getKisayol() != null) { // kisayol degeri daha once eklenmemis ise
            kisayolTxt = dbhelper.getKisayol(); // kisayol degerini databaseden alir
        } else {
            kisayolTxt = "150"; // degilse default deger devam eder.
        }
        if (dbhelper.getcountSetting() != 0) { // eger ayarlar bolum eklenmmise yada guncellemisse
            kioskmodeState = dbhelper.getKiosk(); // kisoask durum databaseden alinir
        } else {
            kioskmodeState = "false"; // degilde defaulr olarak false yapilir.
        }
        if (dbhelper.getcountSetting() != 0) { // eger ayarlar bolum eklenmmise yada guncellemisse

            if(dbhelper.getKameraState() != null){
                KameraState = Integer.parseInt(dbhelper.getKameraState()); // kamera durumu databaseden getirilir.
            }
            else {
                KameraState = 0;
            }

        } else {
            KameraState = 0; // degilse default olarak arka olarak baslar
        }
        if (dbhelper.getSettingTumunusil() != null) {
            tmnDeleteStr = dbhelper.getSettingTumunusil();
            Log.i("tmnDeleteStr", tmnDeleteStr);
        }
        if (dbhelper.getSettingTumunuupdate() != null) {
            tmnUpdateStr = dbhelper.getSettingTumunuupdate();
            Log.i("tmnUpdateStr", tmnUpdateStr);
        }
        if (dbhelper.getSettingAppkapa() != null) {
            appkapa = dbhelper.getSettingAppkapa();
            Log.i("tmnUpdateStr", tmnUpdateStr);
        }
        mSharedPrefs = getSharedPreferences(TAG_MAIN_PREFF, MODE_PRIVATE);
        // fullscreen yapildi uygulama
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //  uygulama acikken isiginin kesilmemesi saglanir
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();
        //kamera nesnesi getirildi
        mCamera = getCameraInstance();
        //ony veya uyari sesini getiroyoz
        mPlayerWorrong = MediaPlayer.create(getApplication(), R.raw.nosound);
        mPlayerOK = MediaPlayer.create(getApplication(), R.raw.yessound);
        // preview olusturulur ve mainactivty e baglanir.
        mPreview = new CameraPreview(this, mCamera);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        // rakamlari dinamik olarark artirmak icin olusturulan viewler bu layouta aittir
        mLayout = (LinearLayout) findViewById(R.id.relativeLayout2);
        // view ' ler getirilir.
        edit = (EditText) findViewById(R.id.editkart);
        infolayout = (RelativeLayout) findViewById(R.id.infolayout);
        infotxt = (TextView) findViewById(R.id.infotiptxt);
        infoclck = (TextView) findViewById(R.id.showclock);
        kartnottxt = (TextView) findViewById(R.id.kartnotxt);
        infoisimtxtt = (TextView) findViewById(R.id.infoisimtxtt);
        infoservermessage = (ImageView) findViewById(R.id.infoserveryesil);
        btn0 = (Button) findViewById(R.id.buton0);
        btn1 = (Button) findViewById(R.id.buton1);
        btn2 = (Button) findViewById(R.id.buton2);
        btn3 = (Button) findViewById(R.id.buton3);
        btn4 = (Button) findViewById(R.id.buton4);
        btn5 = (Button) findViewById(R.id.buton5);
        btn6 = (Button) findViewById(R.id.buton6);
        btn7 = (Button) findViewById(R.id.buton7);
        btn9 = (Button) findViewById(R.id.buton9);
        btn8 = (Button) findViewById(R.id.buton8);
        btnok = (Button) findViewById(R.id.butonok);

        btnsil = (Button) findViewById(R.id.butonsil);
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btnok.setOnClickListener(this);
        btnsil.setOnClickListener(this);

        // wifi nesnesi olustrlur ve wifi acilir.
        WifiSetting wifi = new WifiSetting(getApplicationContext());
        wifi.open();
        mReceiver = new BatteryReciver(mHandler1);

        s_intentFilter = new IntentFilter();
        s_intentFilter.addAction(Intent.ACTION_TIME_TICK);
        s_intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        s_intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        registerReceiver(mReceiver, s_intentFilter);
        infoclck.setText(getDate2());
        BluetoothIsEnable();
        //newtwork kontrol servisi baslatilir.
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // internetin durumu string tipinde getirilir.
                gelen_cevap_network = intent.getStringExtra(KEY_networking);
                Log.i("gelencevpainternet", gelen_cevap_network);
                infoclck.setText(getDate2());
            }
        }, new IntentFilter(FILTER));
        startService(new Intent(this, NetworkControlService.class));
        // sunucu ile baglanti kontrol servisi baslatilir
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // sunucu baglanti kontrol durumu getirlir.
        BluetoothControl();
        gelen_cevap_server = intent.getStringExtra(KEY_server);
                Log.i("gelencevpaserver", gelen_cevap_server);
            // sunucudan duruma gore kontrol servisi baslar ve xml parse etme islemi gerceklesir.
            if (gelen_cevap_server.equals("evet")) {
                warrningresponse("2");
                intent = new Intent(context, ConrolService.class);
                intent.putExtra("ServerControl", gelen_cevap_server);
                startService(intent);
            }
            else {
                warrningresponse("1");
            }
            }
        }, new IntentFilter(FILTER2));
        startService(new Intent(this, ServerControlService.class));
        // kiosk un yapilacagi actvity pending intente aktarilir.
        intent = PendingIntent.getActivity(getBaseContext(), 0,
                new Intent(getIntent()), flags);
        // kiosk alarm threadi olusturlur.
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, intent);
                System.exit(2);
            }
        });
        // edit kart listener ///
        edit.setTextColor(Color.BLACK);
        edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    try {
                        KartLigiris(v, edit.getText().toString());
                    } catch (Exception e) {
                        Log.e("ExpetionEdittext", e.getMessage().toString());
                    }
                    return true;
                }
                return false;
            }
        });

    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Bluettooh konrol edilir
     */
    private void BluetoothControl() {
        BluetoothSocetIsConnec();
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Daha once baglanti yoksa en son mac adres baglanmaya calisir
     */
    private void BluetoothSocetIsConnec() {
        /// eger bluetooth socet kopmussa.
        if (mChatService.getState() !=  BluetoothChatService.STATE_NONE) {
            try {
                BluetoothTryConnect();
            } catch (Exception e) {
                Log.e("Bluetoothtrycon", e.getMessage().toString());
            }
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * bluetooth aclip acilmadigini kontrol eder
     */
    private void BluetoothIsEnable() {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Kartli islemleri baslatan metod
     * @param v
     * @param kart
     * @throws Exception
     */
    private void KartLigiris(View v, String kart) throws Exception {
        if (!kart.equals("")) { // kart no bos degilse
            try {
                if (dbhelper.getCountTable() != 0) { // sistemde personel mevcut ise
                    kartMi = true; // kart li girs bayrak degeri tanimlanir
                    try {
                        karttcno = dbhelper.getQueryPersonelTC(kart); // karta gore tc sorgusu yapilir
                        edit.setText(""); // edit text bosaltilir
                        if (karttcno != null) { // tc mevcut ise
                            tcIslemleri(v); // islemler baslatillir
                            Log.i("SuanTcislermleri", "bitti");
                        } else {
                            new Thread(new MessgeWaitng(mHandler1, 2, 5)).start(); // boyle bir personel yoksa mesaj gosterilir
                            mPlayerWorrong.start(); // hata sesi baslatilir
                        }
                    } catch (Exception e) {
                        Log.e("KartExeption2", e.getMessage().toString());
                    }
                } else {
                    new Thread(new MessgeWaitng(mHandler1, 2, 5)).start(); // boyle bir personel yoksa mesaj gosterilir
                    mPlayerWorrong.start();
                }
            } catch (Exception e) {
                Log.e("KartExeption", e.getMessage().toString());
            }
        } else {
            Toast.makeText(getBaseContext(), "Kart numarasi algilanamadi.", Toast.LENGTH_SHORT).show();
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Bluetooth servisi hazirlar
     */
    private void setupChat() {
        mChatService = new BluetoothChatService(getApplicationContext(), mHandler2); // hadnler ile  gelen kart no degeri ni dondururuz
        mOutStringBuffer = new StringBuffer(""); // string buffer ile hazir hale getirilir
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Donanim tuslari dinlenir
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.i("BACK", "BACK");
            finish();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            Log.i("maf", "home");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        // uygulama kapatildiginda eger kisok mode aktif ise baslatilir.
        if ((kioskmodeState.equals("true") && isMain)) {
            throw new NullPointerException();
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onStart() {
    super.onStart();

        if(mChatService != null){

            Intent intent = getIntent();
            finish();
            startActivity(intent);

        }


        if (mCamera == null) {
            mCamera = getCameraInstance();
            mCamera.startPreview(); // kamera icin privew nesnesi baslatilir.
        }

        setupChat(); // servisi hazirlariz
        isMain = true; // kiosk mode eger mainactivty durumnda ise true olur.


    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Eger bluetooth baglanti kaparsa sonradan tekrar baglanmaya calisir
     * @throws Exception
     */
    private void BluetoothTryConnect() throws Exception {
        String gelenMac = null; // mac adresi tutar
        setupChat(); // servis hazirlanir
        if (dbhelper.getCountMctable() != 0) { // daha once veri bluetooth eklenmis ise
            gelenMac = dbhelper.getData(); // mac adres kayit edilir
            Log.i("MacAdres",gelenMac);
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(gelenMac); // cihaz nesnesi olusturulur
            mChatService.connect(device, true); // baglantis saglatmayaa calisir
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onResume() {

        if (mCamera == null) { // kamera daha once acilmamissa
            mCamera = getCameraInstance(); // kamera nesnesini getirir
        }
        if (mPreview == null) { // kamera destegi bos ise
            mPreview = new CameraPreview(this, mCamera); // nenemize yolla
            preview.addView(mPreview); // ekle preview ekle
        }
        if (mChatService != null) { // service bos degilse
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) { // cihaza bagli degilse
                mChatService.start(); // servisi baslat
            }
        }

        super.onResume();
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MainActicty", "stop");
        if (mChatService != null) { // service bos deilse
            mChatService.stop(); // kapat servisi
        }

    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onPause() {
        if (mCamera != null) { // kamera dolu ise
            mCamera.stopPreview(); // durdur kameraya
            mCamera.release(); //kamerayi serbes birak
            mCamera = null;
        }
        if (mPreview != null) { // preview dolu ise
            preview.removeView(mPreview); //kaldir
            mPreview = null; // sifrla
        }
        Log.i("MainActicty", "pause");
        // eger kiosk mode aktiv ise uygulama durduruldugudan kiosk baslatilir.
        if ((kioskmodeState.equals("true") && isMain)) {
            // throw new NullPointerException();
        }
        super.onPause();
        // mCamera.setPreviewCallback(null); // uygulama pause  durumunda preview bosaltilir.
        // mCamera.release(); // kamera serbes birakilir.


    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        if ((kioskmodeState.equals("true") && isMain)) {// kisok mode acik ve program ama main de ise
            throw new NullPointerException(); // kisok mode calisir
        }
        Log.i("MainActicty", "ondestrory");
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("MainActicty", "restart");
        isMain = true; // kiosk mode eger mainactivty durumnda ise true olur.
        // kioskmodeState = mSharedPrefs.getString(TAG_kiosk, null); // kiosk durumu getirilir.
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * oclick metodu layout da view leri dinamik olarak eklenir.
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buton0:
                mLayout.addView(insertTextChrekter("0"));
                countview++;
                tcno = tcno + "0";
                break;
            case R.id.buton1:
                mLayout.addView(insertTextChrekter("1"));
                countview++;
                tcno = tcno + "1";
                break;
            case R.id.buton2:
                mLayout.addView(insertTextChrekter("2"));
                countview++;
                tcno = tcno + "2";
                break;
            case R.id.buton3:
                mLayout.addView(insertTextChrekter("3"));
                countview++;
                tcno = tcno + "3";
                break;
            case R.id.buton4:
                mLayout.addView(insertTextChrekter("4"));
                countview++;
                tcno = tcno + "4";
                break;
            case R.id.buton5:
                mLayout.addView(insertTextChrekter("5"));
                countview++;
                tcno = tcno + "5";
                break;
            case R.id.buton6:
                mLayout.addView(insertTextChrekter("6"));
                countview++;
                tcno = tcno + "6";
                break;
            case R.id.buton7:
                mLayout.addView(insertTextChrekter("7"));
                countview++;
                tcno = tcno + "7";
                break;
            case R.id.buton8:
                mLayout.addView(insertTextChrekter("8"));
                countview++;
                tcno = tcno + "8";
                break;
            case R.id.buton9:
                mLayout.addView(insertTextChrekter("9"));
                countview++;
                tcno = tcno + "9";
                break;
            case R.id.butonsil:
                if (countview >= 0) {
                    tcno = tcno.substring(0, tcno.length() - 1);
                    mLayout.removeViewAt(countview);
                    countview--;
                }
                break;
            case R.id.butonok: // manuel giris yapildiktan sonra verileri tcislem mtodu cagiirlir
                if (!tcno.equals("")) {
                    kartMi = false;
                    tcIslemleri(v);
                    rakamTemizle();
                }
                break;


        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Sunuc ile kontrol edilen baglantinin ekranda nasil gorunecegini kontrol eder.
     */
    private void warrningresponse(String flag) {
        Bundle b = new Bundle(); // veri gonderen nesne olustururlur.
        Message msg = mHandler1.obtainMessage(13); // Handler hangi case icine girecegini belirler
        if (flag.equals("1")) { // bayrak deger 1 ise
            b.putString("warring", "1"); // gonderilecek wariing mesaji 1
        } else if (flag.equals("2")) { // bayrak 2 ise
            b.putString("warring", "2"); // gonderilecek mesaj 2
        }
        msg.setData(b); // veri bundle eklenir
        mHandler1.sendMessage(msg); // Handler ' e gonderililir.

    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * eger mamuel islem yapilmissa tc alanini temizler.
     */
    private void rakamTemizle() {
        countview = -1; // herbir rakam bir view dir bu yuzden tum view ler sayaci sifirlanir
        tcno = ""; // tc stringi temizlenir.
        mLayout.removeAllViews(); // layout temizlenir.
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * gelen verinin tc nomu kisayol mu tc ise beklemesi mi gerek yoksa devam mi etmeli
     * son karti ekrana bastirir
     * eger guncelleme kisayolunu secilirsa veriler guncellenir
     * @param v
     */
    private void tcIslemleri(View v) {
        String newtc = "";
        if (kartMi) { // kartmi
            newtc = karttcno; // kaydet
        } else { // maunuel mi
            newtc = tcno; // kaydet
        }
        Log.i("PersonelTC", newtc);
        // tiklanan tc ye gore isim ve kart no getirilir.
        p = dbhelper.getQueryPersonelKart(newtc); // islem yapan kisinin kart no ve isim bilgisi getirilir
        isimpersonel = p.getAdSyoad(); // isim geldi
        kartNo = p.getKartno(); // kartno geldi
        if (kartNo != null) {  // karti yoksa
            kartnottxt.setText(kartNo); // kart no ekranda goster
            Log.i("KartNoBosmu", kartNo);
        }
        else {
            Log.i("KartNoBosmu","evet");
        }
        // uygulanin ayarlar activty de kaydedilen kisayol verisi buraya getirilir.
        //eger bu if calisirsa tum verileri xml panelden cekilir.
        if (newtc.equals(tmnUpdateStr)) { // manuel giris yapmissa guncelle ise
            HandleXML obj; //
            String finalUrl = "http://xmlpanel.com/panel/server/";
            obj = new HandleXML(finalUrl, this, 1); // 1  oluinca birkez tum personelller ceklecek
            obj.fetchXML();
            Toast.makeText(getBaseContext(),R.string.guncelleme, Toast.LENGTH_SHORT).show();
        } else if (newtc.equals(appkapa)) {
            finish();
        }
        // bu id calisirsa tum personeller silinir. FORMAT islevi yapar.
        else if (newtc.equals(tmnDeleteStr)) {
            // tum verileri sil
            dbhelper.deleteallTemprorry();
            dbhelper.deleteallPersonel();
            Toast.makeText(getBaseContext(),R.string.silmekisayol, Toast.LENGTH_SHORT).show();
        }
        // eger kisayol activty girmek istiyorsak bu if calisir.
        else if (newtc.equals(kisayolTxt)) {
            isMain = false;
            // Intent settingIntent = new Intent(MainActivity.this, OtherSetting.class);
            //Intent settingIntent = new Intent(MainActivity.this, NewSetttingActivty.class);
            try {
                Intent settingIntent = new Intent(MainActivity.this, SettingActivty.class);
                Bundle bnd = new Bundle();
                bnd.putString("servercontrol", gelen_cevap_server);
                settingIntent.putExtras(bnd);
                startActivity(settingIntent);
            }
            catch (Exception e) {
                Log.e("ErorrExperion", e.getMessage().toString());
            }
        } else {
            try {
                if (dbhelper.queryPersonel(newtc) == 0) { // tc yoksa  tc yoksa mesaji gosterir.
                    // eger bu sorgu calisirsa ekrandan handle mesaji gosterilir.
                    new Thread(new MessgeWaitng(mHandler1, 2, 5)).start();
                    mPlayerWorrong.start();
                } else {
                    String lastTC = null;
                    if (dbhelper.getCountDepoPersonel() != 0) {
                        lastTC = dbhelper.getLastPersonel();
                        if (newtc.equals(lastTC)) {
                            new Thread(new WaitingPersonel(getApplication(), mHandler1)).start(); // tiklamadan sonra kisinin bilgileri
                            mPlayerWorrong.start();
                        } else {
                            try {
                                dbhelper.deleteDepoPersonel();
                                dbhelper.InsertDepoPersonel(newtc);
                                mPlayerOK.start();
                                ControlTip(v);
                                // showAlertDialog(v); // mesai uyari paneli acilir.
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (dbhelper.getCountDepoPersonel() == 0) {
                        try {
                            dbhelper.InsertDepoPersonel(newtc);
                            mPlayerOK.start(); //////////////////////////////////////////////////////////////////////////
                            ControlTip(v);
                            //showAlertDialog(v); // mesai uyari paneli acilir.
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            catch (Exception e) {
                 Log.e("HataM", e.getMessage().toString());
            }
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * personel mesai bilgileri bir personel nesnesi haline getirilir.
     * @return
     */
    private TempPersonel inOnlineSendData(String tc) {
        TempPersonel perOnline = new TempPersonel();
        perOnline.setTckimlikno(tc);
        perOnline.setDate(getDate());
        return perOnline;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * eger enternet veya sunucu problemi varsa personel mesai bilgileri giris durmunda  personel nesnesi haline getirilir
     * ve database ye eklenir.
     */
    public void outTemprroyTable() {
        String newtc = "";
        //
        if (kartMi) {
            newtc = karttcno;
        } else {
            newtc = tcno;
        }
        pp = new TempPersonel();
        pp.setDate(tarih);
        pp.setXmesaiTipi("2");
        pp.setTckimlikno(newtc);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * enternette veya sunucuda problem varsa , personel cikis yapmissa
     * sql kayit islemi olur.
     */
    private void InTemprroyTable() {
        String newtc = "";
        if (kartMi) {
            newtc = karttcno;
        } else {
            newtc = tcno;
        }
        pp = new TempPersonel();
        pp.setDate(tarih);
        pp.setXmesaiTipi("1");
        pp.setTckimlikno(newtc);
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   /*
    * ilk giris de saat getirilir.
    */
    private String getDate2() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String str = sdf.format(new Date());
        return str;
    }
 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * cihazin suan ki tarih ve saatini getirir.
     * @return
     */
    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss:SSS");
        String str = sdf.format(new Date());
        return str;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * tc alan layoutuna dinamik olarak buyutulur.
     * @param text gelen string deger view eklenir.
     * @return
     */
    private TextView insertTextChrekter(String text) {
        final LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); // mamuel islem icin tc text alani olusturldu
        final TextView textView = new TextView(this);
        textView.setLayoutParams(lparams);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(30);
        textView.setText(text);
        return textView;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * cihazin kamera nesnesi getirilir.
     * @return
     */
    public Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(KameraState); // kamera durumuna secilir ve cihazin destekledigi kamera nesnesi getirilir.
        } catch (Exception e) {
            Log.e("CameraGet",e.getMessage().toString());
        }
        return c;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*  resim cekilir ve resim yolu olusturulur
     *  veritabanina eklenir
     */
    private PictureCallback mPictureTemp = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            kamera = new Kamera(getApplicationContext(), newtc, getDate()); // kamera nesnesine tc ve tarih yollanir.
            kamera.CropSaveGallery(data); // resim byte olarak sd karta eklenir.
            kamera.insertImageTemp(pp); // resim dosya yolu veri tabanina eklenir.
            mCamera.startPreview(); // kamera nesnesi guncellenir.
        }
    };
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Eger sunucu baglantisi veya internette bi sorun varsa foto cekilip adreslendikten sonra bekllege  eklenir
     * @param v
     * @throws IOException
     */
    public void captureImageTemp(View v) throws IOException {
        if (kartMi) {  // kart li islem ise
            this.newtc = karttcno; // tc kaydet
        } else { // manuel giris ise
            this.newtc = tcno; // tc kaydet
        }
        mCamera.takePicture(null, null, mPictureTemp);// foto ceker.
        new Thread(new MessgeWaitng(mHandler1, 1, 5)).start(); // tiklamadan sonra kisinin bilgileri
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * tc ve tarih verileri resim yolunu olusturur.
     * veriler gondermek uzere onlineSystem sinifina yollanir.
     */
    private PictureCallback mPictureOnline = new PictureCallback()  {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            kamera = new Kamera(getApplicationContext(), newtc, getDate()); // kamera nesnesi olusturlur ve resim
            kamera.CropSaveGallery(data); // resim sd kartina eklenir
            urlImageString = kamera.getImageFileUrl(); // dosya yolu getirlir.
            new OnlineSystemSendPhp(getApplicationContext(), urlImageString, mHandler1, tip).execute(inOnlineSendData(newtc)); // resim online yollanir
            mCamera.startPreview(); // kamera nesnemiz surfacaview eklenir.
        }
    };
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @param v ekranda gosterlirmesi icin panelin contex in view nesneis getirlir
     * @param tip islem yapan personelin mesai tipi getirilir
     * @throws IOException
     */
    public void captureImageOnline(View v, String tip) throws IOException {
        if (kartMi) { // islem krt ile mi olmus
            this.newtc = karttcno; // tc kayit edilir.
        } else { // kisi manuel giris yapmis ise
            this.newtc = tcno;  // tc kayit et
        }
        this.tip = tip; // mesai tipini al
        try{
            new Thread(new MessgeWaitng(mHandler1, 1, 5)).start(); // tiklamadan sonra kisinin bilgileri.
            mCamera.takePicture(null, null, mPictureOnline); // problemssiz ise baglanti veriler yollanir.
        }
        catch (Exception e){
            Log.i("SondDURMhATA",e.getMessage());
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Servis ve activtyleri dinleyerek ekrana cevap uretir.
     */
    public final Handler mHandler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ControlInterface.MESSAGE_PER_NO:// personel bulunamassa ekrana gostericegi yazi
                    infolayout.setBackgroundResource(R.drawable.kamerahayir);
                    infotxt.setText(R.string.uyari_mesaji);
                    break;
                case ControlInterface.MESSAGE_PER_OK: // personel mesai durumu basarili ise
                    if (isInOut) { // personellin islemi giris ise
                        infolayout.setBackgroundResource(R.drawable.kameraevet);// kamera ekraninin onune resim gosterilir
                        infotxt.setTextColor(Color.BLACK); // gelecek yazilar siyah secilir
                        infotxt.setText(R.string.girisDurum); // giris basarili yazisi bastirlir.
                        infoisimtxtt.setText(isimpersonel); // personelin ismi bastirilir.
                    } else { // personelin islemi cikis ise
                        infolayout.setBackgroundResource(R.drawable.kameraevet); // kamera ekrani onune resim gosterilir
                        infotxt.setTextColor(Color.BLACK);// gelecek yazilar siyah secilir
                        infotxt.setText(R.string.girisDurum);// cikis basarili yazisi gosterilir
                        infoisimtxtt.setText(isimpersonel);// personelin ismi bastirlir.
                    }
                    break;
                case ControlInterface.MESSAGE_EMPETY_INFO:// ekranda bilgi mesajlarini siler
                    infolayout.setBackgroundResource(0); // ekrandan resim silinir ve kamera cekim ekrani geri gelir
                    infotxt.setTextColor(Color.BLACK); // yazi tipileri siyah kalir
                    infotxt.setText(""); // yazilar  temizlenir
                    infoisimtxtt.setText(""); // yazilar temizlenir
                    break;
                case ControlInterface.MESSAGE_WAITING_PERSON: // eger personel son kart ise bu mesaj gosterilir.
                    infolayout.setBackgroundResource(R.drawable.kamerahayir);
                    infotxt.setTextColor(Color.BLACK);
                    infotxt.setText(R.string.bekleme_mesaji);
                    break;
                case ControlInterface.MESSAGE_SERVER_ADRESS_ERORR: // eger sunucudan gelen mesaj bekledigimiz mesaj degilse bu mesaj gosterilir
                    Toast.makeText(getBaseContext(), R.string.sununucu_hata_mesaji, Toast.LENGTH_SHORT).show();
                    break;
                case ControlInterface.HOURS_SHOW: // broadcast reciverdan tarih degistikce texview guncellenir.
                    String result = msg.getData().getString("date");
                    infoclck.setText(result);
                    break;
                case 13:
                    String responseSunucu = msg.getData().getString("warring"); // sunucuda hata varsa yada internet yoksa iconlar ayarlanir
                    if (responseSunucu.equals("2")) {
                        infoservermessage.setImageResource(R.drawable.yesil);
                    } else {
                        // icon kirmizi
                        infoservermessage.setImageResource(R.drawable.red);
                    }
                    break;
            }
        }
    };
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Bu Handler Bluetooth serivis dinler duruma gore mesaji alir.
     */
    public final Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.ERORR:
                    if (null != getApplicationContext()) { // eger bluetooth kapatilirsa
                        Toast.makeText(getApplicationContext(),"BLUETOOTH'U KAPATMAYIN",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:// eger cihaza veri yollacacaksak
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    //  mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ: // bluetooth cihazindan kart numarasi buraya getirilir.
                    final String readMessage = (String) msg.obj;
                    SonKarekterSil(readMessage); // gelen deger islenmek uzere bu metoda yollanir
                    break;
                case Constants.MESSAGE_DEVICE_NAME: // cihazla baglanti saglandiysa bu metod cagirlir.
                    if (null != getApplicationContext()) {
                        Toast.makeText(getApplicationContext(), "Baglanti Basarili ", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST: // baglanti koptugu zaman bu metod cagirlir.
                    if (null != getApplicationContext()) {
                        Toast.makeText(getApplicationContext(), msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();

                    }
                    break;
            }
        }
    };

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Cihazdan gelen string degeri bazi islemlerden gecirip karnoyu kullanmak uzere Karlti giris metodu cagirilir.
     * @param str : Bluetooth cihazdan gelen kart numarasi
     */
    private void SonKarekterSil(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == '\n') { // enter kareteri varsa silinir
            str = str.substring(0, str.length() - 1);
        }
        str = str.replaceAll("\\s+", ""); // bosluklar temizlenir
        View v = null;
        if(!str.equals("")){ // eger gelen string bos degilse degerlendirlir
            try {
                KartLigiris(v, str); // Kullanmak uzere metodu cagirir.
            } catch (Exception e) {
                Log.e("ExpetionEdittext", e.getMessage().toString());
            }
        }

    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * manuel mi yoksa kartlami girlimis ona gore bilgiler getirlir ve veritaninda basan personel en son ne tip
     * islem yapmissa personelin simdi ki islem tipini belirler
      @throws IOException
     */
    public void ControlTip(View v) throws IOException {
        int i; // database deki talonun elemanini olup olmadigi anlamamiz icin tanimlariz
        String tip = ""; // islem yapan kisinin giris 1 ve cikis 2 degerini tuttariz
        String newtc = ""; // kartli ile yada tc ile giris yapan personelin tc numarasini tuttariz
        if (kartMi) { // kartli la mi giris yapmis
            newtc = karttcno; //  kart ile giris yapan kisinin tc si tc nosu son hali atilir.
            Log.i("KATRTLITC", newtc);
        } else {
            newtc = tcno;
            Log.i("MANUELTC", newtc);
        }
        i = dbhelper.getTipTcPersonel(newtc); // bu tc numarasina gore personel varmi
        Log.i("TC", String.valueOf(i));
        if (i == 0) { // yoksa veriler ilk kez girir oldugu icin giris ile baslatir.
            dbhelper.InsertOrTipPersonel(newtc, "1", 1); // personlin tc sine gore mesai tipi eklenir
            inOnClick(v);// verileri yollanacagi metodua yollanir
            i = dbhelper.getTipTcPersonel(newtc);
            Log.i("TCC", String.valueOf(i));
            Log.i("geldim", "geldim1");
        } else {
            tip = dbhelper.getTip(newtc); // eger daha once giris yapmis ise mesai tipi getirilir.
            Log.i("tip", tip);
            if (tip.equals("1")) { // mesai tipi giris ise
                dbhelper.InsertOrTipPersonel(newtc, "2", 2); // personeli tipiini guncelle
                outOnClick(v); // veriler gonderilecek metoda eklenir
                Log.i("geldim", "geldim2");
            } else if (tip.equals("2")) { // mesai tipi cikis ise
                dbhelper.InsertOrTipPersonel(newtc, "1", 2); // persenel tipi guncellenir
                inOnClick(v); // verileri gonderilecek metod acigirlir.
                Log.i("geldim", "geldim3");

            }
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Cikis yapan kisinin sunucu ve internet durumuna gore veriler gonderilir veya bellege kaydedilir.
     * @param v
     */
    private void outOnClick(View v) {
        tarih = getDate();// islem yaptigi zamanki tarih getirilir.
        if (gelen_cevap_network.equals("evet") && gelen_cevap_server.equals("evet")) {// enternet ve sunucu durumuna gore personel verileri eklenir
            try {
                captureImageOnline(v, "2");// resim cekilir.
                isInOut = false; // personel giris mi cikismi yapmis o belirlenir
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                outTemprroyTable();// enternet baglantisi olmadigi icin veriler sql kaydedilir.
                captureImageTemp(v); // resim cekilir.
                new Thread(new MessgeWaitng(mHandler1, 1, 6)).start();//handle mesaj gosterimi
                isInOut = false; // giris mi cikismi yapmis bayrak deger aktarilir.
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * giris yapan kisinin sunucu ve internet durumunu gore veriler gonderilir yada bellege kaydedilir.
     * @param v
     */
    private void inOnClick(View v) {
        tarih = getDate(); // islem yapildigi anki tarih getirilir.
        if (gelen_cevap_server.equals("evet") && gelen_cevap_network.equals("evet")) { // internet ve sunucu baglantisi varsa
            try {
                captureImageOnline(v, "1"); // resim cekilit.
                isInOut = true; // girismi yapmis cikismi belirlenir.
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                InTemprroyTable(); // eger degilse veriler kendi sql kaydetmek uzere bu metod kullanilir.
                captureImageTemp(v); // resim cekilir.
                new Thread(new MessgeWaitng(mHandler1, 1, 6)).start();//handle mesaj gosterimi
               isInOut = true;// girismi yapmis cikismi belirlenir.
            } catch (Exception e) {
                Log.e("hataintemptrroy", e.getMessage());
            }

        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Setting activty den gelen result abstarck metodun gelen degerini getirir
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                //DeviceListActivity bir cihaz ile dondugunde baglamak icin guvenli mod
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                //DeviceListActivity bir cihaz ile dondugunde baglamak icin guvenli olmayan  mod
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // Cihazda ki bluetooth aktif edilince service baslatilir.
                if (resultCode == Activity.RESULT_OK) {
                    setupChat();
                } else {
                    // Eger bluetooth aktif degilse

                    Toast.makeText(getApplicationContext(), R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Mac adresi alinmis cihazla baglanti kurulur
     * @param data : Intent ile gelen veri bilgileri
     * @param secure : guvenli mod ile mi yoksa guvenli olmayan yol ile mi baglanilsin
     */
    private void connectDevice(Intent data, boolean secure) {
        String address = data.getExtras()// Cihazin mac adresi getirilir.
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);// Cihaz nesne olarak degistirlir.
        mChatService.connect(device, secure);// Cihaza baglanilir.
    }




///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private BroadcastReceiver mBatteryregister = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent i) {

            String sendString;
            int level = i.getIntExtra("level", 0);

            if(level>90){
                // bluetooth kapat
                sendString = "e";
                byte[] send = sendString.getBytes();
                mChatService.write(send);
                Log.i("BATTERYdURUM","BİRSEYKAPA");

            }
            else if(level<30){
                // bluetooth ac
                sendString = "E";
                byte[] send = sendString.getBytes();
                mChatService.write(send);
                Log.i("BATTERYdURUM","BATTERYAC");

            }
            else {
                Log.i("BATTERYdURUM","BİRSEYYOLLAMA");
            }
        }
    };

}