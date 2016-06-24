package com.example.ahmet.pdkdemo;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

import com.example.ahmet.pdkdemo.BasicObject.TempPersonel;
import com.example.ahmet.pdkdemo.BasicObject.Personel;
import com.example.ahmet.pdkdemo.Database.DatabaseHelper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * Bu nesne xml formatinda ki verilieri ceker ve database kayteder.
 */

public class HandleXML {

    private String urlString; // veri cekecegimiz xml url adres
    private XmlPullParserFactory xmlFactoryObject; // xml parcalama nesnesi
    private XmlPullParser myparser = null; // xml parse etme nesnesi
    private Personel mpersonel; // kayit yapacak personel nesnesi
    private ArrayList<Personel> liste; // personel nesnesinin listesi
    private ArrayList<TempPersonel> listePersone;
    private Context context; // Mainden yada Settin Actvtyden gelen context
    private DatabaseHelper db; // Database nesnes,
    private int flag; // Tumunu mu cekecek yoksa bi tanesini cekecek onu belirleyen bayrak deger
    private static boolean mesaivarmi = false; // mesai durumunu sorgulayan deger
    private static String Silmi ;
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public HandleXML(String url, Context c, int flag) {
        liste = new ArrayList<Personel>(); // gelen personel listesi olusturulur
        listePersone = new ArrayList<TempPersonel>();
        this.context = c;  // contex bagladik
        db = new DatabaseHelper(context); // veritabani nesnesi olusturudu
        this.urlString = url; // xml url adresini aldik
        this.flag = flag; // bayrak degerini aldik
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Adres veritabanindan getirildi.
     * @return
     */
    private String getAdres() {
        String adres = null;  // adres stringi
        try {
            adres = db.getAdres(); // gelen adres veritabanindan
        } catch (Exception e) {

        }
        return adres;
    }
 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * XML formatinda sayfa ile baglanti saglanir
     * Hangi metodla veri ceklecek belirlenir
     */
    public void fetchXML() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (flag == 1) {  // gelen bayrak degerini gore adres belirlenir
                    urlString = getAdres() + "xmlpersonel.php?CihazMacId=" + getMacAdres() + "&KullaniciLisans=" + getLisans() + "&Islem=Tumu";
                } else if (flag == 0) {
                    urlString = getAdres() + "xmlpersonel.php?CihazMacId=" + getMacAdres() + "&KullaniciLisans=" + getLisans();
                }
                try {
                    URL url = new URL(urlString); // string nesnesi url nesnesine donusturlur
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // baglanti saglanir
                    conn.setReadTimeout(10000); // okurken en fazla timeouta dusecek sure
                    conn.setConnectTimeout(15000); //
                    conn.setRequestMethod("GET"); // get bisimimnde alinir
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream stream = conn.getInputStream();
                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    myparser = xmlFactoryObject.newPullParser();
                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myparser.setInput(stream, null);
                    parseXMLAndStoreIt(myparser); // parcalama islemi yapilir.
                } catch (Exception e) {
                    Log.e("HandleExpecionErorr", e.getMessage().toString());
                }
            }
        });
        thread.start();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * pase edilmis veriyi parcalam islemi burada yapilir.
     * @param myParser
     * @return
     */
    public boolean parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String text = null; // gelen her bir string tipine getirlir.
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String tagname = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("Personel")) { // tagname verilince
                            mpersonel = new Personel(); // personel nesnesini olustur
                        }
                        break;
                    case XmlPullParser.TEXT: // parse edilecek text tipinden ise
                        text = myParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("Personel")) { // ve tag name personel ile biten hepsini aliriz
                            liste.add(mpersonel); // listeye personeli atariz
                        } else if (tagname.equalsIgnoreCase("PersonelTc")) { // PersonelTC ile baslayan tag getir
                            mpersonel.setTCno(text); // nesneye at
                        } else if (tagname.equalsIgnoreCase("PersonelKartNo")) {  // Personel kartno tag getirir.
                            mpersonel.setKartno(text); // kartnoyu personel nesnesine at
                        } else if (tagname.equalsIgnoreCase("PersonelAdSoyadi")) { // personel adini soyadni cek
                            mpersonel.setAdSyoad(text); // nesneye ekle
                        } else if (tagname.equalsIgnoreCase("PersonelVardiya")) {
                            if (!text.equals("")) {
                                mpersonel.setMesaiDurumu(text);
                                Log.i("MesaiDurumu ", text);
                            } else {
                                mpersonel.setMesaiDurumu("");
                            }
                        }

                        break;
                    default:
                        break;
                }
                event = myParser.next();
            }
        } catch (Exception e) {
            Log.e("hata_parse", e.getMessage());
        }


       return  sendDatabase(liste);
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Database ' e arralisti ekle
     * @param liste
     * @return
     */
    public boolean sendDatabase(ArrayList<Personel> liste) {
        for (int i = 0; i < liste.size(); i++) {
             int a = db.queryPersonel(liste.get(i).getTCno()); // ekleme yada guncelleme islemi gerceklisir.
                if (!liste.get(i).getKartno().equals("0"))// mesai durumu olmayanlari eklemiyorum.
                {

                    db.InsertOrUpdatePersonel(liste.get(i), a);
                }

            else{
                    db.deleteRecordPersonel(liste.get(i).getTCno());
                }
        }
    return true;

    }


 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Cihazin mac adresi getirilir
     * @return
     */
    private String getMacAdres() {
        String adres = null;
        try {
            WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            adres = info.getMacAddress();
            Log.i("MACadres:", adres);
        } catch (Exception E) {
            Log.e("MacAdresErorr:", E.getMessage().toString());
        }
        return adres;
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Uygulamanin lisans numarasi getirilir.
     * @return
     */
    private String getLisans() {
        String lisans = null;
        try {
            lisans = db.getSettingElements();
        } catch (Exception e) {
            Log.e("getlisansErooor:", e.getMessage().toString());
        }
        return lisans;
    }
}