/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.ahmet.pdkdemo;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.example.ahmet.pdkdemo.Database.DatabaseHelper;
import java.util.Set;


public class DeviceListActivity extends Activity {


    public static final String MyPREFERENCES = "MyPrefs" ; // gecici bellek icin referans kodu
    public static final String Name = "nameKey"; // gecici bellek icin isim TAG
    SharedPreferences sharedpreferences; // gecici veritabani nesnesi
    private DatabaseHelper db; // veritabani nesnesi
    private static final String TAG = "DeviceListActivity"; // log icin TAG
    public static String EXTRA_DEVICE_ADDRESS = "device_address"; // activye gondereilecek mac adres verisi icin Intent filirtersi
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); // windows kurulumu
        setContentView(R.layout.activity_device_list); // layout yuklenmsi
        db = new DatabaseHelper(this); // veritabani nesnesi
        setResult(Activity.RESULT_CANCELED); // sonuc alici disari yedekler eger sonuc iptal ise
        Button scanButton = (Button) findViewById(R.id.button_scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doDiscovery(); // cihaz ara
                v.setVisibility(View.GONE); // listview aktif hale getir
            }
        });
        ArrayAdapter<String> pairedDevicesArrayAdapter =
                new ArrayAdapter<String>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        //Bul ve eslestirilmis cihazlar icin ListView kurmak
        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(pairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);
        // Bul ve yeni kesfedilen cihazlar icin ListView kurmak
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);
        // Bir cihaz tespit edildiginde yayinlari icin kayit
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        // kesif bittikten sonra yayinlari icin kayit
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
        // yerel Bluetooth adaptoru alin
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // su anda eslestirilmis cihazlar bir dizi alýn
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // eslestirilmis cihazlar varsa, ArrayAdapter her birini ekleyin
        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            pairedDevicesArrayAdapter.add(noDevices);
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // artýk keþif yapmýyoruz emin olun
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }
        // Kaydini yayin dinleyici
        this.unregisterReceiver(mReceiver);
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Bluetooth Adaptorü ile cihaz bulma baslatmak
     */
    private void doDiscovery() {
        // basliginda tarama belirtiniz
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scanning);
        // Yeni cihazlar için altyazi acma
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
        // Bluetooth Adaptörü Talebi keþif
        mBtAdapter.startDiscovery();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Listviews tüm cihazlar için onclick dinleyici
     */
    private AdapterView.OnItemClickListener mDeviceClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            //  keþif Ýptal
            mBtAdapter.cancelDiscovery();
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(Name, address);
            // eger yeni cihaz baglanmissa gelirse database kaydet
            db.deleteData();
            db.InsertData(address);
            finish();



        }
    };
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * The BroadcastReceiver that listens for discovered devices and changes the title when
     * discovery is finished
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.select_device);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };

}
